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

    /* JADX WARN: Code restructure failed: missing block: B:153:0x024d, code lost:
        if (r9.getBoolean("EnablePreviewAll", true) == false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x025e, code lost:
        if (r9.getBoolean("EnablePreviewGroup", r6) != false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0266, code lost:
        if (r9.getBoolean("EnablePreviewChannel", r6) != false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0268, code lost:
        r3 = r27.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0278, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L667;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x027a, code lost:
        r28[0] = null;
        r6 = r3.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0282, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetSameChatWallPaper) == false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x028a, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.WallpaperSameNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x028d, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper) == false) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0295, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.WallpaperNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0298, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached) == false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x02a0, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x02a3, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L664;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x02a7, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp) == false) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x02ae, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto) == false) goto L166;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x02be, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.R.string.NotificationContactNewPhoto, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x02c1, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation) == false) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02c3, code lost:
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().getFormatterYear().format(r27.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().getFormatterDay().format(r27.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r3 = getUserConfig().getCurrentUser().first_name;
        r0 = r27.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0325, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, r3, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x0328, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L662;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x032c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0332, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0336, code lost:
        if (r6.video == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x033e, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x0345, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x034a, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x034c, code lost:
        r7 = r6.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0352, code lost:
        if (r7 != 0) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x035b, code lost:
        if (r6.users.size() != 1) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x035d, code lost:
        r7 = r27.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x0374, code lost:
        if (r7 == 0) goto L214;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x037e, code lost:
        if (r27.messageOwner.peer_id.channel_id == 0) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x0382, code lost:
        if (r2.megagroup != false) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0397, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x039a, code lost:
        if (r7 != r21) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x03af, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03b0, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03bc, code lost:
        if (r0 != null) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03be, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x03c4, code lost:
        if (r4 != r0.id) goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x03c8, code lost:
        if (r2.megagroup == false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x03dd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x03f1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x040a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r1, r2.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x040b, code lost:
        r4 = new java.lang.StringBuilder();
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x041b, code lost:
        if (r5 >= r27.messageOwner.action.users.size()) goto L226;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x041d, code lost:
        r6 = getMessagesController().getUser(r27.messageOwner.action.users.get(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0431, code lost:
        if (r6 == null) goto L225;
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0433, code lost:
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x043b, code lost:
        if (r4.length() == 0) goto L222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x043d, code lost:
        r4.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0440, code lost:
        r4.append(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0443, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x045e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r1, r2.title, r4.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x0462, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x0476, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x0479, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0481, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x0484, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L270;
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x0486, code lost:
        r3 = r6.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x048c, code lost:
        if (r3 != 0) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0495, code lost:
        if (r6.users.size() != 1) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0497, code lost:
        r3 = r27.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x04ae, code lost:
        if (r3 == 0) goto L255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x04b2, code lost:
        if (r3 != r21) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x04c7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x04c8, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x04d4, code lost:
        if (r0 != null) goto L253;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x04d6, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x04f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r1, r2.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04f1, code lost:
        r3 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0501, code lost:
        if (r4 >= r27.messageOwner.action.users.size()) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0503, code lost:
        r6 = getMessagesController().getUser(r27.messageOwner.action.users.get(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0517, code lost:
        if (r6 == null) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x0519, code lost:
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0521, code lost:
        if (r3.length() == 0) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0523, code lost:
        r3.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0526, code lost:
        r3.append(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x0529, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0544, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r1, r2.title, r3.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0547, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x054f, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0552, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x0567, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x056c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x057f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0582, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0586, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x058c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x058e, code lost:
        r6 = r6.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0592, code lost:
        if (r6 != r21) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x05a7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x05ad, code lost:
        if (r6 != r4) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x05bf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x05c0, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r27.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x05d2, code lost:
        if (r0 != null) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x05d4, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x05f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r1, r2.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x05f3, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L305;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x05fb, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x05fe, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L309;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0606, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x060b, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x061b, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0620, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L317;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x062e, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0631, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0639, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x063c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L625;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x0642, code lost:
        if (r2 == null) goto L427;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0648, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r2) == false) goto L329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x064c, code lost:
        if (r2.megagroup == false) goto L427;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0652, code lost:
        r0 = r27.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0654, code lost:
        if (r0 != null) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0669, code lost:
        return org.telegram.messenger.LocaleController.formatString(r25, org.telegram.messenger.R.string.NotificationActionPinnedNoText, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x066a, code lost:
        r11 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0673, code lost:
        if (r0.isMusic() == false) goto L337;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0685, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x068c, code lost:
        if (r0.isVideo() == false) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0696, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x06bf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r1, "📹 " + r0.messageOwner.message, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x06d3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x06d8, code lost:
        if (r0.isGif() == false) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x06e2, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x070b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r1, "🎬 " + r0.messageOwner.message, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x071f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x0727, code lost:
        if (r0.isVoice() == false) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0739, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x073e, code lost:
        if (r0.isRoundVideo() == false) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0750, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0755, code lost:
        if (r0.isSticker() != false) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x075b, code lost:
        if (r0.isAnimatedSticker() == false) goto L365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x0761, code lost:
        r5 = r0.messageOwner;
        r9 = r5.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0767, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x076f, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0798, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r1, "📎 " + r0.messageOwner.message, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x07ac, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x07af, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L417;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x07b3, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x07bc, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L381;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x07d1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x07d4, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x07d6, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x07f6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r1, r2.title, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x07f9, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L393;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x07fb, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0801, code lost:
        if (r0.quiz == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x081d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r1, r2.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0838, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r1, r2.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x083b, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x0843, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x086c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r1, "🖼 " + r0.messageOwner.message, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0880, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0886, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L405;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0898, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0899, code lost:
        r5 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x089b, code lost:
        if (r5 == null) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x08a1, code lost:
        if (r5.length() <= 0) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x08a3, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x08a9, code lost:
        if (r0.length() <= 20) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x08ab, code lost:
        r5 = new java.lang.StringBuilder();
        r7 = 0;
        r5.append((java.lang.Object) r0.subSequence(0, 20));
        r5.append("...");
        r0 = r5.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x08c0, code lost:
        r7 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08c1, code lost:
        r3 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = r2.title;
        r4 = new java.lang.Object[3];
        r4[r7] = r1;
        r4[1] = r0;
        r4[2] = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x08d4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r3, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x08e6, code lost:
        return org.telegram.messenger.LocaleController.formatString(r11, org.telegram.messenger.R.string.NotificationActionPinnedNoText, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x08f7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x08f8, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x08fc, code lost:
        if (r0 == null) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0912, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r1, r2.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0924, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0925, code lost:
        if (r2 == null) goto L527;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0927, code lost:
        r0 = r27.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0929, code lost:
        if (r0 != null) goto L433;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x093a, code lost:
        return org.telegram.messenger.LocaleController.formatString(r24, org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x093b, code lost:
        r1 = r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0942, code lost:
        if (r0.isMusic() == false) goto L437;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0952, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0959, code lost:
        if (r0.isVideo() == false) goto L445;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0963, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0989, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r2.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x099a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x099f, code lost:
        if (r0.isGif() == false) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x09a9, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L451;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b4, code lost:
        if (r9.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x09cf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r2.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x09e0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x09e7, code lost:
        if (r0.isVoice() == false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x09f7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x09fc, code lost:
        if (r0.isRoundVideo() == false) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0a0c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0a11, code lost:
        if (r0.isSticker() != false) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a17, code lost:
        if (r0.isAnimatedSticker() == false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a1c, code lost:
        r5 = r0.messageOwner;
        r9 = r5.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0a22, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0a2a, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L471;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0a50, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r2.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0a61, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0a64, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L517;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0a68, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0a70, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L481;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0a82, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0a85, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0a87, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0aa4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r2.title, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0aa7, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0aa9, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0aaf, code lost:
        if (r0.quiz == false) goto L491;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0ac8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r2.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0ae0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r2.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0ae3, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L501;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0aeb, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0b11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r2.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0b22, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0b27, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L505;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0b37, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0b38, code lost:
        r5 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b3a, code lost:
        if (r5 == null) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00c0, code lost:
        if (r9.getBoolean("EnablePreviewChannel", r1) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0b40, code lost:
        if (r5.length() <= 0) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0b42, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b48, code lost:
        if (r0.length() <= 20) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0b4a, code lost:
        r1 = new java.lang.StringBuilder();
        r5 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b5f, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0b60, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r3 = new java.lang.Object[2];
        r3[r5] = r2.title;
        r3[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0b70, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0b7f, code lost:
        return org.telegram.messenger.LocaleController.formatString(r1, org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0b8e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0b8f, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0b93, code lost:
        if (r0 == null) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0ba7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r2.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0bb7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0bb8, code lost:
        r0 = r27.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0bbc, code lost:
        if (r0 != null) goto L531;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0bca, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0bd1, code lost:
        if (r0.isMusic() == false) goto L535;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0bdf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0be6, code lost:
        if (r0.isVideo() == false) goto L543;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0bf0, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0c14, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r1, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0c23, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0c28, code lost:
        if (r0.isGif() == false) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0c32, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0c56, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r1, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0c65, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0c6c, code lost:
        if (r0.isVoice() == false) goto L555;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0c7a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0c7f, code lost:
        if (r0.isRoundVideo() == false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0c8d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0c92, code lost:
        if (r0.isSticker() != false) goto L618;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0c98, code lost:
        if (r0.isAnimatedSticker() == false) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0c9e, code lost:
        r2 = r0.messageOwner;
        r6 = r2.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0ca4, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0cac, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0cd0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r1, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x0cdf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0ce2, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L615;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0ce6, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L575;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0cee, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0cfe, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x0d01, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x0d03, code lost:
        r6 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x0d1e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r1, org.telegram.messenger.ContactsController.formatName(r6.first_name, r6.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x0d21, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0d23, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r6).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x0d29, code lost:
        if (r0.quiz == false) goto L589;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x0d40, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r1, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x0d56, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r1, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x0d59, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L599;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0d61, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L597;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0d85, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r1, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0d94, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0d99, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0da7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0da8, code lost:
        r2 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0daa, code lost:
        if (r2 == null) goto L613;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x0db0, code lost:
        if (r2.length() <= 0) goto L613;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x0db2, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0db8, code lost:
        if (r0.length() <= 20) goto L612;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x0dba, code lost:
        r2 = new java.lang.StringBuilder();
        r6 = 0;
        r2.append((java.lang.Object) r0.subSequence(0, 20));
        r2.append("...");
        r0 = r2.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0dcf, code lost:
        r6 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x0dd0, code lost:
        r2 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r3 = new java.lang.Object[2];
        r3[r6] = r1;
        r3[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0dde, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r2, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0deb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0df8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x0df9, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0dfd, code lost:
        if (r0 == null) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0e0e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x0e1b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x0e1e, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x0e20, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r6).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x0e28, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L635;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x0e2c, code lost:
        if (r10 != r21) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0e50, code lost:
        if (r10 != r21) goto L639;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x0e6e, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x0e71, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x0e79, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0e7a, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x0e84, code lost:
        if (r3.peer_id.channel_id == 0) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x0e88, code lost:
        if (r2.megagroup != false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x0e8e, code lost:
        if (r27.isVideoAvatar() == false) goto L654;
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x0ea0, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x0eb1, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x0eb7, code lost:
        if (r27.isVideoAvatar() == false) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x0ecb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x0ede, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r1, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x0ee5, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x0ef3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x0ef8, code lost:
        if (r27.isMediaEmpty() == false) goto L675;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x0f02, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x0f08, code lost:
        return replaceSpoilers(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x0f0f, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x0f14, code lost:
        if (r27.type != 29) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x0f1c, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r27) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x0f1e, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r27);
        r1 = r0.extended_media.size();
        r2 = 0;
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x0f2c, code lost:
        if (r2 >= r1) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x0f2e, code lost:
        r4 = r0.extended_media.get(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x0f38, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) == false) goto L708;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x0f3a, code lost:
        r3 = ((org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) r4).media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0f40, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0f48, code lost:
        if (org.telegram.messenger.MessageObject.isVideoDocument(r3.document) == false) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x0f50, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) == false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x0f58, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r4).flags & 4) == 0) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x0f5a, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0f5c, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x0f5f, code lost:
        if (r3 == false) goto L690;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0f63, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0f66, code lost:
        r0 = org.telegram.messenger.R.string.AttachPaidMedia;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f68, code lost:
        if (r1 != 1) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x0f6a, code lost:
        if (r3 == false) goto L700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f6c, code lost:
        r1 = org.telegram.messenger.R.string.AttachVideo;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x0f6f, code lost:
        r1 = org.telegram.messenger.R.string.AttachPhoto;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x0f71, code lost:
        r1 = org.telegram.messenger.LocaleController.getString(r1);
        r2 = 1;
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x0f78, code lost:
        if (r3 == false) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x0f7a, code lost:
        r2 = "Media";
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x0f7c, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0f7e, code lost:
        r2 = "Photos";
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x0f81, code lost:
        r1 = org.telegram.messenger.LocaleController.formatPluralString(r2, r1, new java.lang.Object[0]);
        r2 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0f88, code lost:
        r2 = new java.lang.Object[r2];
        r2[r3] = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x0f90, code lost:
        return org.telegram.messenger.LocaleController.formatString(r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x0f95, code lost:
        if (r27.isVoiceOnce() == false) goto L718;
     */
    /* JADX WARN: Code restructure failed: missing block: B:733:0x0f9d, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x0fa2, code lost:
        if (r27.isRoundOnce() == false) goto L722;
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x0faa, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0fab, code lost:
        r1 = r27.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x0fb1, code lost:
        if ((r1.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L734;
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x0fb9, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x0fce, code lost:
        return "🖼 " + replaceSpoilers(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x0fd5, code lost:
        if (r27.messageOwner.media.ttl_seconds == 0) goto L732;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x0fdd, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x0fe4, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x0fe9, code lost:
        if (r27.isVideo() == false) goto L746;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x0ff3, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L740;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x1008, code lost:
        return "📹 " + replaceSpoilers(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x100f, code lost:
        if (r27.messageOwner.media.ttl_seconds == 0) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x1017, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x101e, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x1023, code lost:
        if (r27.isGame() == false) goto L750;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x102b, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x1030, code lost:
        if (r27.isVoice() == false) goto L754;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1038, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x103d, code lost:
        if (r27.isRoundVideo() == false) goto L758;
     */
    /* JADX WARN: Code restructure failed: missing block: B:773:0x1045, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:775:0x104a, code lost:
        if (r27.isMusic() == false) goto L762;
     */
    /* JADX WARN: Code restructure failed: missing block: B:777:0x1052, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x1053, code lost:
        r1 = r27.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:779:0x1059, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L766;
     */
    /* JADX WARN: Code restructure failed: missing block: B:781:0x1061, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:783:0x1064, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L774;
     */
    /* JADX WARN: Code restructure failed: missing block: B:785:0x106c, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll.quiz == false) goto L772;
     */
    /* JADX WARN: Code restructure failed: missing block: B:787:0x1074, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:789:0x107b, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:791:0x107e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L778;
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:0x1086, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingGiveaway);
     */
    /* JADX WARN: Code restructure failed: missing block: B:795:0x1089, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L782;
     */
    /* JADX WARN: Code restructure failed: missing block: B:797:0x1091, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingGiveawayResults);
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:0x1094, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L833;
     */
    /* JADX WARN: Code restructure failed: missing block: B:801:0x1098, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L786;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x109e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L790;
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:0x10a6, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x10a9, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L816;
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x10af, code lost:
        if (r27.isSticker() != false) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x10b5, code lost:
        if (r27.isAnimatedSticker() == false) goto L796;
     */
    /* JADX WARN: Code restructure failed: missing block: B:815:0x10bc, code lost:
        if (r27.isGif() == false) goto L804;
     */
    /* JADX WARN: Code restructure failed: missing block: B:817:0x10c6, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L802;
     */
    /* JADX WARN: Code restructure failed: missing block: B:819:0x10db, code lost:
        return "🎬 " + replaceSpoilers(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:821:0x10e2, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:823:0x10eb, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L808;
     */
    /* JADX WARN: Code restructure failed: missing block: B:825:0x1100, code lost:
        return "📎 " + replaceSpoilers(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:827:0x1107, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:0x1108, code lost:
        r0 = r27.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:0x110c, code lost:
        if (r0 == null) goto L814;
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:0x1128, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:0x112f, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:0x1132, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaStory) == false) goto L827;
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:0x1138, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaStory) r1).via_mention == false) goto L825;
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:0x113a, code lost:
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r2 = r28[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:0x113f, code lost:
        if (r2 != null) goto L823;
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:0x1141, code lost:
        r2 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:0x114e, code lost:
        return org.telegram.messenger.LocaleController.formatString("StoryNotificationMention", r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:0x1155, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:0x115c, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageText) != false) goto L831;
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:0x1162, code lost:
        return replaceSpoilers(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:0x1169, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:0x1170, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:867:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:?, code lost:
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
            return LocaleController.getString(R.string.NotificationHiddenMessage);
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
                    return LocaleController.getString(R.string.Message);
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
                str3 = UserObject.getUserName(user);
                if (j2 != 0) {
                    strArr[0] = str3;
                    str = "NotificationActionPinnedNoTextChannel";
                    str2 = "NotificationActionPinnedNoText";
                } else {
                    str = "NotificationActionPinnedNoTextChannel";
                    str2 = "NotificationActionPinnedNoText";
                    if (Build.VERSION.SDK_INT > 27) {
                        strArr[0] = str3;
                    } else {
                        strArr[0] = null;
                    }
                }
            } else {
                str = "NotificationActionPinnedNoTextChannel";
                str2 = "NotificationActionPinnedNoText";
                str3 = null;
            }
        } else {
            str = "NotificationActionPinnedNoTextChannel";
            str2 = "NotificationActionPinnedNoText";
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
            return LocaleController.getString(R.string.NotificationHiddenMessage);
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
        if (zArr != null) {
            zArr[0] = false;
        }
        return LocaleController.getString(R.string.Message);
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

    /* JADX WARN: Code restructure failed: missing block: B:274:0x0696, code lost:
        if (r14.getBoolean(r36, true) == false) goto L756;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x06a2, code lost:
        if (r14.getBoolean(r35, r12) != false) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x06a4, code lost:
        r4 = r46.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x06a8, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x06aa, code lost:
        r12 = r4.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x06ae, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x06b0, code lost:
        r1 = r12.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x06b4, code lost:
        if (r1 != 0) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x06bd, code lost:
        if (r12.users.size() != 1) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x06bf, code lost:
        r1 = r46.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x06d2, code lost:
        if (r1 == 0) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x06dc, code lost:
        if (r46.messageOwner.peer_id.channel_id == 0) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06e0, code lost:
        if (r3.megagroup != false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x06e2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x06f9, code lost:
        if (r1 != r31) goto L276;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x06fb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0710, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x071c, code lost:
        if (r0 != null) goto L279;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x071e, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0724, code lost:
        if (r5 != r0.id) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0728, code lost:
        if (r3.megagroup == false) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x072a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x073f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0754, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0770, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0782, code lost:
        if (r4 >= r46.messageOwner.action.users.size()) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0784, code lost:
        r5 = getMessagesController().getUser(r46.messageOwner.action.users.get(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0798, code lost:
        if (r5 == null) goto L297;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x079a, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
        r13 = r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x07a4, code lost:
        if (r1.length() == 0) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x07a6, code lost:
        r1.append(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x07a9, code lost:
        r1.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x07ae, code lost:
        r13 = r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x07b1, code lost:
        r4 = r4 + 1;
        r24 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x07b5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r3.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x07d6, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x07d8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x07ee, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x07f0, code lost:
        r0 = r46.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x07fa, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x07fc, code lost:
        r1 = r12.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0800, code lost:
        if (r1 != 0) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0809, code lost:
        if (r12.users.size() != 1) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x080b, code lost:
        r1 = r46.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x081e, code lost:
        if (r1 == 0) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0822, code lost:
        if (r1 != r31) goto L318;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0824, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0839, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0845, code lost:
        if (r0 != null) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0847, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0849, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0865, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0877, code lost:
        if (r4 >= r46.messageOwner.action.users.size()) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0879, code lost:
        r5 = getMessagesController().getUser(r46.messageOwner.action.users.get(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x088d, code lost:
        if (r5 == null) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x088f, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0897, code lost:
        if (r1.length() == 0) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0899, code lost:
        r1.append(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x089c, code lost:
        r1.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x089f, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x08a2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r3.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x08be, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x08c0, code lost:
        r12 = (org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) r12;
        r0 = org.telegram.messenger.MessagesController.getInstance(r45.currentAccount).getChat(java.lang.Long.valueOf(-org.telegram.messenger.DialogObject.getPeerDialogId(r12.boost_peer)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x08d9, code lost:
        if (r0 != null) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x08db, code lost:
        r15 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x08dd, code lost:
        r15 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x08df, code lost:
        if (r15 != null) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x08e1, code lost:
        r0 = org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x08e9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGiftCode", org.telegram.messenger.R.string.NotificationMessageGiftCode, r15, org.telegram.messenger.LocaleController.formatPluralString("Months", r12.months, new java.lang.Object[0]));
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x090d, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x090f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0928, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x092a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r7, r12.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x093f, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x0943, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L355;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0949, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x094b, code lost:
        r1 = r12.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x094f, code lost:
        if (r1 != r31) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0951, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x096b, code lost:
        if (r1 != r5) goto L363;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x096d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x097f, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r46.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0991, code lost:
        if (r0 != null) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0993, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0995, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r7, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x09b4, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x09b6, code lost:
        r0 = r46.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x09c0, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x09c2, code lost:
        r0 = r46.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x09cc, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x09ce, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x09e6, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x09e8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r12.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x09f8, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x09fa, code lost:
        r0 = r46.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0a04, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x0a0c, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r3) == false) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x0a10, code lost:
        if (r3.megagroup == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0a17, code lost:
        r1 = r46.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0a19, code lost:
        if (r1 != null) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x0a1b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0a35, code lost:
        if (r1.isMusic() == false) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0a37, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0a4d, code lost:
        if (r1.isVideo() == false) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a57, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0a59, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "📹 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a7f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0a95, code lost:
        if (r1.isGif() == false) goto L406;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0a9f, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L405;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0aa1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "🎬 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0ac7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0adf, code lost:
        if (r1.isVoice() == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0ae1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0af5, code lost:
        if (r1.isRoundVideo() == false) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0af7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0b0b, code lost:
        if (r1.isSticker() != false) goto L459;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0b11, code lost:
        if (r1.isAnimatedSticker() == false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0b16, code lost:
        r4 = r1.messageOwner;
        r7 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0b1c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0b24, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L421;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0b26, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "📎 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0b4c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0b60, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0b64, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0b6c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L429;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0b6e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b82, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0b84, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r46.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r3.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0ba9, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0bab, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0bb1, code lost:
        if (r0.quiz == false) goto L437;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0bb3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0bcc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0be7, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0bef, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0bf1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "🖼 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0c17, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0c2d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0c2f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0c3f, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0c41, code lost:
        if (r0 == null) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0c47, code lost:
        if (r0.length() <= 0) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0c49, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0c4f, code lost:
        if (r0.length() <= 20) goto L455;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0c51, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0c68, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0c69, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = r3.title;
        r3 = new java.lang.Object[2];
        r3[r4] = r2;
        r3[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0c7b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c8b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0c9b, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0c9f, code lost:
        if (r0 == null) goto L463;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0ca1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0cb5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0cc6, code lost:
        r1 = r46.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0cc8, code lost:
        if (r1 != null) goto L468;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0cca, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0ce6, code lost:
        if (r1.isMusic() == false) goto L471;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0ce8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0d00, code lost:
        if (r1.isVideo() == false) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0d0a, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0d0c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📹 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0d35, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0d4e, code lost:
        if (r1.isGif() == false) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0d58, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L482;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0d5a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🎬 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0d83, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0d9f, code lost:
        if (r1.isVoice() == false) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0da1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0db7, code lost:
        if (r1.isRoundVideo() == false) goto L489;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0db9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0dcf, code lost:
        if (r1.isSticker() != false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0dd5, code lost:
        if (r1.isAnimatedSticker() == false) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0ddb, code lost:
        r4 = r1.messageOwner;
        r8 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0de1, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0de9, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0deb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📎 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0e14, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0e2b, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0e2f, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L503;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0e38, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0e3a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0e51, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L509;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0e53, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r46.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r7, r3.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0e7b, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0e7d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r8).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0e83, code lost:
        if (r0.quiz == false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e85, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0ea1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0ebf, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L521;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0ec7, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0ec9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🖼 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0ef2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0f0c, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0f0e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0f20, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0f22, code lost:
        if (r0 == null) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0f28, code lost:
        if (r0.length() <= 0) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0f2a, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0f30, code lost:
        if (r0.length() <= 20) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0f32, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0f47, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0f48, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = r3.title;
        r3 = new java.lang.Object[3];
        r3[r4] = r7;
        r3[1] = r0;
        r3[2] = r2;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0f5d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0f70, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0f82, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0f86, code lost:
        if (r0 == null) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0f88, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r7, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0f9e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0fb3, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0fb5, code lost:
        r0 = r46.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0fbf, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0fc1, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r12).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0fc9, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0fcd, code lost:
        if (r1 != r31) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0fcf, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0fda, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r29, org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0ff1, code lost:
        if (r1 != r31) goto L555;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0ff3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x1001, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x1014, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x1016, code lost:
        r0 = r46.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x1024, code lost:
        if (r4.peer_id.channel_id == 0) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x1028, code lost:
        if (r3.megagroup != false) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x102e, code lost:
        if (r46.isVideoAvatar() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x1030, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x1042, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x1059, code lost:
        if (r46.isVideoAvatar() == false) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x105b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x106f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x1089, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r3) == false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x108d, code lost:
        if (r3.megagroup != false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x1093, code lost:
        if (r46.isMediaEmpty() == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x1095, code lost:
        if (r47 != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x109f, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x10a1, code lost:
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r7, r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x10ba, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x10d2, code lost:
        if (r46.type != 29) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x10da, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r46) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x10dc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r46)).stars_amount, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x10f5, code lost:
        r1 = r46.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x10fb, code lost:
        if ((r1.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x10fd, code lost:
        if (r47 != false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x1105, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x1107, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "🖼 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x112d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x1141, code lost:
        if (r46.isVideo() == false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x1143, code lost:
        if (r47 != false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x114d, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x114f, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "📹 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x1175, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x118b, code lost:
        if (r46.isVoice() == false) goto L607;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x118d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x119f, code lost:
        if (r46.isRoundVideo() == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x11a1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x11b3, code lost:
        if (r46.isMusic() == false) goto L613;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x11b5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x11c3, code lost:
        r1 = r46.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x11c9, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L616;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x11cb, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r7, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x11ea, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x11ec, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x11f2, code lost:
        if (r0.quiz == false) goto L621;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x11f4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r7, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x120b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r7, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x1224, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L625;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x1226, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r1;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r3.title, java.lang.Integer.valueOf(r1.quantity), java.lang.Integer.valueOf(r1.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x124e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L664;
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x1252, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L629;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x125a, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x125c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x126e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L657;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x1274, code lost:
        if (r46.isSticker() != false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x127a, code lost:
        if (r46.isAnimatedSticker() == false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x1284, code lost:
        if (r46.isGif() == false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x1286, code lost:
        if (r47 != false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x1290, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x1292, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "🎬 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x12b8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x12c8, code lost:
        if (r47 != false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x12d2, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x12d4, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "📎 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x12fa, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x130a, code lost:
        r0 = r46.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x130e, code lost:
        if (r0 == null) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x1310, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x1321, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x1330, code lost:
        if (r47 != false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1338, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageText) != false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x133a, code lost:
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r7, r46.messageText);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x134e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x135b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x1371, code lost:
        if (r46.isMediaEmpty() == false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x1373, code lost:
        if (r47 != false) goto L672;
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x137d, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L672;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x137f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r7, r3.title, r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x1399, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r34, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x13ae, code lost:
        r5 = r34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x13b4, code lost:
        if (r46.type != 29) goto L678;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x13bc, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r46) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L678;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x13be, code lost:
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChatMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r46)).stars_amount, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x13da, code lost:
        r2 = r46.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x13e0, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x13e2, code lost:
        if (r47 != false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x13ea, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x13ec, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "🖼 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x1415, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x142e, code lost:
        if (r46.isVideo() == false) goto L692;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x1430, code lost:
        if (r47 != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x143a, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x143c, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "📹 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x1465, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x1481, code lost:
        if (r46.isVoice() == false) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x1483, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x1499, code lost:
        if (r46.isRoundVideo() == false) goto L698;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x149b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x14b1, code lost:
        if (r46.isMusic() == false) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x14b3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x14c5, code lost:
        r2 = r46.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x14cb, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L704;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x14cd, code lost:
        r2 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r2;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r7, r3.title, org.telegram.messenger.ContactsController.formatName(r2.first_name, r2.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x14f1, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L710;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x14f3, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x14f9, code lost:
        if (r0.quiz == false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x14fb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1517, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x1535, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x1537, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r7, r3.title, r2.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x1555, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x1557, code lost:
        r2 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r2;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r3.title, java.lang.Integer.valueOf(r2.quantity), java.lang.Integer.valueOf(r2.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x157e, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x1580, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x158e, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L754;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x1592, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x159b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L726;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x159d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x15b4, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x15ba, code lost:
        if (r46.isSticker() != false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x15c0, code lost:
        if (r46.isAnimatedSticker() == false) goto L732;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x15ca, code lost:
        if (r46.isGif() == false) goto L739;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x15cc, code lost:
        if (r47 != false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x15d6, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x15d8, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "🎬 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x1601, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x1616, code lost:
        if (r47 != false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1620, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x1622, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "📎 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x164b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x1660, code lost:
        r0 = r46.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1664, code lost:
        if (r0 == null) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1666, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r7, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x167c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x168f, code lost:
        if (r47 != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1697, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageText) != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1699, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r7, r3.title, r46.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x16b1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r5, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x16c4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:?, code lost:
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:?, code lost:
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:801:?, code lost:
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:?, code lost:
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:803:?, code lost:
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:?, code lost:
        return r15;
     */
    /* JADX WARN: Removed duplicated region for block: B:272:0x068d  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x16df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        String str;
        String str2;
        String str3;
        String str4;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        String str5;
        boolean z2;
        String formatString;
        String formatString2;
        char c;
        boolean z3;
        String formatString3;
        String string;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString(R.string.YouHaveNewMessage);
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
        String str6 = ", ";
        long j3 = tLRPC$Peer.user_id;
        if (zArr2 != null) {
            zArr2[0] = true;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z4 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j, true);
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
            if (messageObject.messageOwner.from_scheduled) {
                if (j == clientUserId) {
                    string = LocaleController.getString(R.string.MessageScheduledReminderNotification);
                } else {
                    string = LocaleController.getString(R.string.NotificationMessageScheduledName);
                }
                str2 = "EnablePreviewChannel";
                str = "NotificationMessageGroupNoText";
                str4 = string;
                str3 = "EnablePreviewGroup";
            } else {
                str = "NotificationMessageGroupNoText";
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(j3));
                if (user != null) {
                    str2 = "EnablePreviewChannel";
                    str3 = "EnablePreviewGroup";
                    str4 = UserObject.getUserName(user);
                } else {
                    str2 = "EnablePreviewChannel";
                    str3 = "EnablePreviewGroup";
                    str4 = null;
                }
            }
        } else {
            str = "NotificationMessageGroupNoText";
            str2 = "EnablePreviewChannel";
            str3 = "EnablePreviewGroup";
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat != null) {
                str4 = chat.title;
            }
            str4 = null;
        }
        if (str4 == null) {
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
            formatString = LocaleController.getString(R.string.YouHaveNewMessage);
        } else {
            String str7 = str3;
            TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat;
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
                    if (ChatObject.isChannel(tLRPC$Chat3)) {
                        tLRPC$Chat2 = tLRPC$Chat3;
                        if (!tLRPC$Chat2.megagroup) {
                            str5 = "ChatThemeDisabled";
                            z2 = true;
                            if (z4) {
                                boolean z5 = !z2 ? true : true;
                                if (z2) {
                                }
                            }
                            String str8 = str;
                            if (zArr2 != null) {
                                zArr2[0] = false;
                            }
                            formatString = (ChatObject.isChannel(tLRPC$Chat2) || tLRPC$Chat2.megagroup) ? (messageObject.type == 29 || !(MessageObject.getMedia(messageObject) instanceof TLRPC$TL_messageMediaPaidMedia)) ? LocaleController.formatString(str8, R.string.NotificationMessageGroupNoText, str4, tLRPC$Chat2.title) : LocaleController.formatPluralString("NotificationMessagePaidMedia", (int) ((TLRPC$TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, str4) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, str4);
                            return formatString;
                        }
                    } else {
                        tLRPC$Chat2 = tLRPC$Chat3;
                    }
                    str5 = "ChatThemeDisabled";
                    z2 = false;
                    if (z4) {
                    }
                    String str82 = str;
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
                        formatString = LocaleController.getString(R.string.WallpaperSameNotification);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                        formatString = LocaleController.getString(R.string.WallpaperNotification);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        formatString = messageObject.messageText.toString();
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                        formatString = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, str4);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        formatString = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, str4);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        String formatString4 = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().getFormatterDay().format(messageObject.messageOwner.date * 1000));
                        int i = R.string.NotificationUnrecognizedDevice;
                        String str9 = getUserConfig().getCurrentUser().first_name;
                        TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                        formatString = LocaleController.formatString("NotificationUnrecognizedDevice", i, str9, formatString4, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent)) {
                        formatString = messageObject.messageText.toString();
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                        if (tLRPC$MessageAction.video) {
                            formatString = LocaleController.getString(R.string.CallMessageVideoIncomingMissed);
                        } else {
                            formatString = LocaleController.getString(R.string.CallMessageIncomingMissed);
                        }
                    } else {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                            String str10 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                            if (!TextUtils.isEmpty(str10)) {
                                c = 0;
                                z3 = true;
                                if (j == clientUserId) {
                                    formatString3 = LocaleController.formatString("ChangedChatThemeYou", R.string.ChatThemeChangedYou, str10);
                                } else {
                                    formatString3 = LocaleController.formatString("ChangedChatThemeTo", R.string.ChatThemeChangedTo, str4, str10);
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
                                formatString3 = LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, str4, str10);
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
                            formatString2 = LocaleController.formatString("NotificationMessageText", i2, str4, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString2;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            formatString = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, str4);
                        } else {
                            formatString = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, str4);
                        }
                    } else if (messageObject.isVideo()) {
                        if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            int i3 = R.string.NotificationMessageText;
                            formatString2 = LocaleController.formatString("NotificationMessageText", i3, str4, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString2;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            formatString = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, str4);
                        } else {
                            formatString = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, str4);
                        }
                    } else if (messageObject.isGame()) {
                        formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, str4, messageObject.messageOwner.media.game.title);
                    } else if (messageObject.isVoice()) {
                        formatString = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, str4);
                    } else if (messageObject.isRoundVideo()) {
                        formatString = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, str4);
                    } else if (messageObject.isMusic()) {
                        formatString = LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, str4);
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                            TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = (TLRPC$TL_messageMediaContact) tLRPC$MessageMedia;
                            formatString = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, str4, ContactsController.formatName(tLRPC$TL_messageMediaContact.first_name, tLRPC$TL_messageMediaContact.last_name));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveaway) {
                            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) tLRPC$MessageMedia;
                            formatString = LocaleController.formatString("NotificationMessageChannelGiveaway", R.string.NotificationMessageChannelGiveaway, str4, Integer.valueOf(tLRPC$TL_messageMediaGiveaway.quantity), Integer.valueOf(tLRPC$TL_messageMediaGiveaway.months));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveawayResults) {
                            formatString = LocaleController.formatString("BoostingGiveawayResults", R.string.BoostingGiveawayResults, new Object[0]);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                            TLRPC$Poll tLRPC$Poll = ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll;
                            if (tLRPC$Poll.quiz) {
                                formatString = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, str4, tLRPC$Poll.question.text);
                            } else {
                                formatString = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, str4, tLRPC$Poll.question.text);
                            }
                        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                            formatString = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, str4);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                            formatString = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, str4);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String stickerEmoji = messageObject.getStickerEmoji();
                                if (stickerEmoji != null) {
                                    formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, str4, stickerEmoji);
                                } else {
                                    formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, str4);
                                }
                            } else if (messageObject.isGif()) {
                                if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    int i4 = R.string.NotificationMessageText;
                                    formatString2 = LocaleController.formatString("NotificationMessageText", i4, str4, "🎬 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                    return formatString2;
                                }
                                formatString = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, str4);
                            } else if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                int i5 = R.string.NotificationMessageText;
                                formatString2 = LocaleController.formatString("NotificationMessageText", i5, str4, "📎 " + messageObject.messageOwner.message);
                                zArr[0] = true;
                                return formatString2;
                            } else {
                                formatString = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, str4);
                            }
                        } else if (!z && !TextUtils.isEmpty(messageObject.messageText)) {
                            formatString2 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str4, messageObject.messageText);
                            zArr[0] = true;
                            return formatString2;
                        } else {
                            formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
                        }
                    }
                } else if (!z) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        formatString2 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str4, messageObject.messageOwner.message);
                        zArr[0] = true;
                        return formatString2;
                    }
                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
                } else {
                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
                }
                return formatString;
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
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
                arrayList.add(new NotificationChannelGroup(str2, LocaleController.getString(R.string.NotificationsChannels) + str8));
            }
            if (str3 != null) {
                arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString(R.string.NotificationsGroups) + str8));
            }
            if (str6 != null) {
                arrayList.add(new NotificationChannelGroup(str6, LocaleController.getString(R.string.NotificationsStories) + str8));
            }
            if (str5 != null) {
                arrayList.add(new NotificationChannelGroup(str5, LocaleController.getString(R.string.NotificationsReactions) + str8));
            }
            if (str7 != null) {
                arrayList.add(new NotificationChannelGroup(str7, LocaleController.getString(R.string.NotificationsPrivateChats) + str8));
            }
            if (str4 != null) {
                arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString(R.string.NotificationsOther) + str8));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:185:0x0434  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x04a9  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x04ad  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x04fc  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x056c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x05ab A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:273:0x05bc A[LOOP:1: B:271:0x05b9->B:273:0x05bc, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:276:0x05d1  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x05dd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:283:0x05ee A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x061d  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01ce  */
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
        String str8;
        String str9;
        String string;
        StringBuilder sb;
        String str10;
        String str11;
        String str12;
        String str13;
        NotificationsController notificationsController;
        long j3;
        String str14;
        String str15;
        boolean z4;
        String str16;
        int i4;
        String str17;
        long[] jArr2;
        String str18;
        String str19;
        String str20;
        boolean z5;
        String str21;
        int i5;
        String str22;
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
        String str23;
        String str24;
        String str25;
        long[] jArr4;
        SharedPreferences.Editor editor;
        boolean z9;
        boolean z10;
        String str26;
        String str27;
        String str28;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str29 = "stories";
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
            StringBuilder sb2 = new StringBuilder();
            str6 = "reactions";
            sb2.append("NoSound");
            sb2.append(2);
            uri2 = sb2.toString();
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
            formatString = LocaleController.getString(R.string.NotificationsSilent);
            str29 = "silent";
        } else if (z) {
            String string2 = LocaleController.getString(z2 ? R.string.NotificationsInAppDefault : R.string.NotificationsDefault);
            if (i3 == 2) {
                if (z2) {
                    str8 = "channels_ia";
                    str9 = string2;
                } else {
                    str9 = string2;
                    str8 = "channels";
                }
            } else if (i3 != 0) {
                if (i3 == 3) {
                    if (z2) {
                        str8 = "stories_ia";
                    } else {
                        str9 = string2;
                        str8 = str29;
                    }
                } else if (i3 == 4 || i3 == 5) {
                    str8 = z2 ? "reactions_ia" : str6;
                } else if (z2) {
                    str8 = "private_ia";
                } else {
                    str9 = string2;
                    str8 = str7;
                }
                str9 = string2;
            } else if (z2) {
                str8 = "groups_ia";
                str9 = string2;
            } else {
                str9 = string2;
                str8 = "groups";
            }
            String str31 = str8 + "_" + MD52;
            string = notificationsSettings.getString(str31, null);
            String string3 = notificationsSettings.getString(str31 + "_s", null);
            StringBuilder sb3 = new StringBuilder();
            if (string != null) {
                sb = sb3;
                str10 = string3;
                str11 = "_s";
                str12 = string;
                str13 = "secret";
                notificationsController = this;
                j3 = j;
                str14 = "channel_";
                str15 = str4;
                z4 = z12;
                str16 = "_";
                i4 = i;
                str17 = str31;
                jArr2 = jArr;
            } else {
                notificationChannel = systemNotificationManager.getNotificationChannel(string);
                if (!BuildVars.LOGS_ENABLED) {
                    str14 = "channel_";
                    str15 = str4;
                } else {
                    str15 = str4;
                    StringBuilder sb4 = new StringBuilder();
                    str14 = "channel_";
                    sb4.append("current channel for ");
                    sb4.append(string);
                    sb4.append(" = ");
                    sb4.append(notificationChannel);
                    FileLog.d(sb4.toString());
                }
                if (notificationChannel == null) {
                    str11 = "_s";
                    jArr2 = jArr;
                    sb = sb3;
                    str13 = "secret";
                    notificationsController = this;
                    j3 = j;
                    z4 = z12;
                    str16 = "_";
                    str17 = str31;
                    i4 = i;
                    str20 = null;
                    str19 = null;
                    str18 = null;
                    z5 = false;
                    if (z5) {
                    }
                    str21 = str11;
                    if (!z4) {
                    }
                    i5 = 0;
                    while (i5 < jArr2.length) {
                    }
                    str22 = str21;
                    sb.append(i4);
                    uri3 = uri;
                    if (uri3 != null) {
                    }
                    sb.append(i2);
                    if (!z) {
                        sb.append(str13);
                    }
                    MD5 = Utilities.MD5(sb.toString());
                    if (!z3) {
                        systemNotificationManager.deleteNotificationChannel(str18);
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        str18 = null;
                    }
                    if (str18 == null) {
                    }
                    return str18;
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
                            sb3.append(j4);
                        }
                    }
                    sb3.append(lightColor);
                    if (sound != null) {
                        sb3.append(sound.toString());
                    }
                    sb3.append(importance);
                    if (!z && z11) {
                        sb3.append("secret");
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("current channel settings for " + string + " = " + ((Object) sb3) + " old = " + string3);
                    }
                    String MD53 = Utilities.MD5(sb3.toString());
                    sb3.setLength(0);
                    if (MD53.equals(string3)) {
                        str11 = "_s";
                        jArr2 = jArr;
                        sb = sb3;
                        str23 = string3;
                        str24 = string;
                        str13 = "secret";
                        notificationsController = this;
                        str25 = MD53;
                        str16 = "_";
                        j3 = j;
                        str17 = str31;
                        i4 = i;
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
                                str17 = str31;
                                str24 = string;
                                str28 = "secret";
                                str25 = MD53;
                                jArr4 = jArr3;
                                str27 = "_";
                                sb = sb3;
                                str23 = string3;
                            } else {
                                if (i3 == 3) {
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY);
                                    jArr4 = jArr3;
                                    sb5.append(getSharedPrefKey(j, 0L));
                                    edit.putBoolean(sb5.toString(), false);
                                    str26 = "secret";
                                } else {
                                    str26 = "secret";
                                    jArr4 = jArr3;
                                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, 0L), 2);
                                }
                                str11 = "_s";
                                str23 = string3;
                                str17 = str31;
                                str24 = string;
                                sb = sb3;
                                str25 = MD53;
                                str27 = "_";
                                str28 = str26;
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            j3 = j;
                            str16 = str27;
                            str13 = str28;
                            editor = edit;
                        } else {
                            str11 = "_s";
                            str17 = str31;
                            str24 = string;
                            str13 = "secret";
                            str25 = MD53;
                            jArr4 = jArr3;
                            sb = sb3;
                            j3 = j;
                            str23 = string3;
                            if (importance == i2) {
                                str16 = "_";
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
                                    i4 = i;
                                    jArr2 = jArr4;
                                    z9 = true;
                                } else {
                                    i4 = i;
                                }
                                if (lightColor != i4) {
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
                                    i4 = lightColor;
                                    z9 = true;
                                }
                                if (editor != null) {
                                    editor.commit();
                                }
                                z5 = z9;
                            } else if (z2) {
                                str16 = "_";
                                editor = null;
                            } else {
                                SharedPreferences.Editor edit2 = notificationsSettings.edit();
                                str16 = "_";
                                int i6 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
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
                                        edit2.putInt("priority_channel", i6);
                                    } else if (i3 == 0) {
                                        edit2.putInt("priority_group", i6);
                                    } else if (i3 == 3) {
                                        edit2.putInt("priority_stories", i6);
                                    } else if (i3 == 4 || i3 == 5) {
                                        edit2.putInt("priority_react", i6);
                                    } else {
                                        edit2.putInt("priority_messages", i6);
                                    }
                                } else if (i3 == 3) {
                                    edit2.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j3, true);
                                } else {
                                    edit2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j3, 0);
                                    edit2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j3);
                                    edit2.putInt("priority_" + j3, i6);
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
                        if (lightColor != i4) {
                        }
                        if (editor != null) {
                        }
                        z5 = z9;
                    }
                    str19 = str25;
                    str20 = str23;
                    str18 = str24;
                    if (z5 || str19 == null) {
                        str21 = str11;
                        if (!z4 || str19 == null || !z2 || !z) {
                            i5 = 0;
                            while (i5 < jArr2.length) {
                                sb.append(jArr2[i5]);
                                i5++;
                                str21 = str21;
                            }
                            str22 = str21;
                            sb.append(i4);
                            uri3 = uri;
                            if (uri3 != null) {
                                sb.append(uri.toString());
                            }
                            sb.append(i2);
                            if (!z && z11) {
                                sb.append(str13);
                            }
                            MD5 = Utilities.MD5(sb.toString());
                            if (!z3 && str18 != null && (z4 || !str20.equals(MD5))) {
                                try {
                                    systemNotificationManager.deleteNotificationChannel(str18);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("delete channel by settings change " + str18);
                                }
                                str18 = null;
                            }
                            if (str18 == null) {
                                str18 = z ? notificationsController.currentAccount + str14 + str17 + str16 + Utilities.random.nextLong() : notificationsController.currentAccount + str14 + j3 + str16 + Utilities.random.nextLong();
                                if (z11) {
                                    str9 = LocaleController.getString(R.string.SecretChatName);
                                }
                                NotificationChannel notificationChannel2 = new NotificationChannel(str18, str9, i2);
                                notificationChannel2.setGroup(str15);
                                if (i4 != 0) {
                                    z6 = true;
                                    notificationChannel2.enableLights(true);
                                    notificationChannel2.setLightColor(i4);
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
                                    FileLog.d("create new channel " + str18);
                                }
                                notificationsController.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                                systemNotificationManager.createNotificationChannel(notificationChannel2);
                                notificationsSettings.edit().putString(str17, str18).putString(str17 + str22, MD5).commit();
                            }
                            return str18;
                        }
                    } else {
                        SharedPreferences.Editor putString = notificationsSettings.edit().putString(str17, str18);
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str17);
                        str21 = str11;
                        sb6.append(str21);
                        putString.putString(sb6.toString(), str19).commit();
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("change edited channel " + str18);
                        }
                    }
                    MD5 = str19;
                    str22 = str21;
                    uri3 = uri;
                    if (str18 == null) {
                    }
                    return str18;
                } else {
                    str11 = "_s";
                    jArr2 = jArr;
                    sb = sb3;
                    str10 = string3;
                    str12 = string;
                    str13 = "secret";
                    notificationsController = this;
                    j3 = j;
                    z4 = z12;
                    str16 = "_";
                    str17 = str31;
                    i4 = i;
                }
            }
            str20 = str10;
            str18 = str12;
            str19 = null;
            z5 = false;
            if (z5) {
            }
            str21 = str11;
            if (!z4) {
            }
            i5 = 0;
            while (i5 < jArr2.length) {
            }
            str22 = str21;
            sb.append(i4);
            uri3 = uri;
            if (uri3 != null) {
            }
            sb.append(i2);
            if (!z) {
            }
            MD5 = Utilities.MD5(sb.toString());
            if (!z3) {
            }
            if (str18 == null) {
            }
            return str18;
        } else {
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb7 = new StringBuilder();
            sb7.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb7.append(j);
            sb7.append("_");
            sb7.append(j2);
            str29 = sb7.toString();
        }
        str9 = formatString;
        str8 = str29;
        String str312 = str8 + "_" + MD52;
        string = notificationsSettings.getString(str312, null);
        String string32 = notificationsSettings.getString(str312 + "_s", null);
        StringBuilder sb32 = new StringBuilder();
        if (string != null) {
        }
        str20 = str10;
        str18 = str12;
        str19 = null;
        z5 = false;
        if (z5) {
        }
        str21 = str11;
        if (!z4) {
        }
        i5 = 0;
        while (i5 < jArr2.length) {
        }
        str22 = str21;
        sb.append(i4);
        uri3 = uri;
        if (uri3 != null) {
        }
        sb.append(i2);
        if (!z) {
        }
        MD5 = Utilities.MD5(sb.toString());
        if (!z3) {
        }
        if (str18 == null) {
        }
        return str18;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:452:0x0b0c
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private void showOrUpdateNotification(boolean r56) {
        /*
            Method dump skipped, instructions count: 3746
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
        String string = LocaleController.getString(R.string.DefaultRingtone);
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

    /* JADX WARN: Can't wrap try/catch for region: R(86:54|(2:56|(3:58|59|60)(4:61|(2:64|62)|65|66))(1:699)|67|(1:69)(2:(1:696)(1:698)|697)|70|(4:73|(2:75|76)(1:78)|77|71)|79|80|(5:82|(2:(1:85)(1:603)|86)(1:604)|(1:602)(2:92|(2:96|97))|601|97)(2:605|(4:(1:679)(1:614)|615|(7:617|(2:619|(1:621)(5:633|(1:635)|636|348|349))(2:637|(6:641|(1:643)|623|624|(1:626)(2:628|(1:630)(2:631|632))|627))|622|623|624|(0)(0)|627)(2:645|(2:647|(1:649)(5:650|(1:652)|636|348|349))(9:653|(1:678)(1:657)|658|(1:677)(2:662|(1:664))|676|666|(2:668|(2:670|(1:672)(2:673|632)))(1:675)|674|(0)(0)))|60)(3:680|(7:682|(2:684|(1:686))(2:688|(2:690|(1:692)))|687|347|348|349|60)(1:694)|693))|(1:103)|(3:105|(1:107)(1:599)|108)(1:600)|109|(3:111|(3:113|(1:115)(3:586|587|(3:589|(1:591)(1:593)|592))|116)(1:597)|594)(1:598)|(3:118|(1:124)|125)(1:585)|126|(3:580|(1:582)(1:584)|583)(2:129|130)|131|(1:133)|134|(1:136)(1:572)|137|(2:570|571)(1:141)|142|143|(3:146|(1:148)|(3:150|151|(64:155|156|157|(54:161|162|163|(1:558)(1:167)|168|(1:557)(1:171)|172|173|(1:556)|180|(1:555)(1:187)|188|(14:190|(1:192)(2:344|(5:346|347|348|349|60)(2:350|(1:(1:353)(11:354|194|195|196|(2:199|197)|200|201|(1:343)(1:204)|205|(1:207)(1:342)|208))(2:355|(11:357|195|196|(1:197)|200|201|(0)|343|205|(0)(0)|208)(11:358|(1:363)(1:362)|196|(1:197)|200|201|(0)|343|205|(0)(0)|208))))|193|194|195|196|(1:197)|200|201|(0)|343|205|(0)(0)|208)(4:364|(6:366|(3:372|(1:374)(2:548|(1:552))|(2:376|(1:378))(23:379|(1:381)|382|(2:544|(1:546)(1:547))(1:388)|389|390|(12:(1:393)(2:540|(1:542))|394|(2:(1:409)(2:397|(2:(2:400|(1:402))(1:405)|403)(2:406|(2:408|403)))|404)|410|(3:509|(1:539)(3:515|(2:537|538)(4:518|(1:522)|(1:536)(2:528|(1:532))|535)|533)|534)(1:414)|415|(7:417|(1:507)(7:430|(1:506)(3:434|(9:488|489|490|491|492|493|494|495|496)(1:436)|437)|438|(1:440)(1:487)|441|442|(6:476|477|478|479|480|(5:472|(1:474)|450|451|(2:456|(3:458|(2:463|464)(1:460)|(1:462)))))(4:444|(2:475|(0))|446|(0)))|448|449|450|451|(3:454|456|(0)))(1:508)|467|(3:471|370|371)|369|370|371)|543|394|(0)|410|(1:412)|509|(1:511)|539|534|415|(0)(0)|467|(4:469|471|370|371)|369|370|371))|368|369|370|371)|553|554)|209|(2:327|(4:329|(2:332|330)|333|334)(2:335|(1:337)(2:338|(1:340)(1:341))))(1:213)|214|(1:216)|217|(1:219)|220|(2:222|(1:224)(1:322))(2:323|(1:325)(1:326))|(1:226)(1:321)|227|(4:229|(2:232|230)|233|234)(1:320)|235|(1:237)|238|239|240|241|242|243|244|245|246|(1:248)|249|(1:251)|(1:253)|(1:260)|261|(1:309)(1:267)|268|(1:270)|(1:272)|273|(3:278|(4:280|(3:282|(4:284|(1:286)|287|288)(2:290|291)|289)|292|293)|294)|295|(1:308)(2:298|(1:302))|303|(1:305)|306|307|60)|563|(1:165)|558|168|(0)|557|172|173|(1:175)|556|180|(1:183)|555|188|(0)(0)|209|(1:211)|327|(0)(0)|214|(0)|217|(0)|220|(0)(0)|(0)(0)|227|(0)(0)|235|(0)|238|239|240|241|242|243|244|245|246|(0)|249|(0)|(0)|(2:255|260)|261|(1:263)|309|268|(0)|(0)|273|(4:275|278|(0)|294)|295|(0)|308|303|(0)|306|307|60)))|569|563|(0)|558|168|(0)|557|172|173|(0)|556|180|(0)|555|188|(0)(0)|209|(0)|327|(0)(0)|214|(0)|217|(0)|220|(0)(0)|(0)(0)|227|(0)(0)|235|(0)|238|239|240|241|242|243|244|245|246|(0)|249|(0)|(0)|(0)|261|(0)|309|268|(0)|(0)|273|(0)|295|(0)|308|303|(0)|306|307|60) */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x03b3, code lost:
        if (r10.local_id != 0) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0469, code lost:
        if (r1.local_id != 0) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x10d1, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x10e3, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x10e4, code lost:
        r2 = r49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x10e7, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x10e8, code lost:
        r4 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x10eb, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x03bb  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x04ad  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04b6  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0570  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0587  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0590  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x05e2  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x05e9  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x0610  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x068a  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x069b  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x06d5  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x06e0  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x06e8  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x073a  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0792  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x07a2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x07b7  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x07cf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:335:0x07fb  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x093a A[LOOP:5: B:359:0x0932->B:361:0x093a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0956 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0979  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x098c  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x0998  */
    /* JADX WARN: Removed duplicated region for block: B:415:0x0aba  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x0b97  */
    /* JADX WARN: Removed duplicated region for block: B:508:0x0ca1  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x0ca5  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x0cd4  */
    /* JADX WARN: Removed duplicated region for block: B:527:0x0d2e  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x0d6d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0d7f  */
    /* JADX WARN: Removed duplicated region for block: B:547:0x0dc4  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0dd9  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0e52  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x0e70  */
    /* JADX WARN: Removed duplicated region for block: B:569:0x0ea0  */
    /* JADX WARN: Removed duplicated region for block: B:578:0x0ee9  */
    /* JADX WARN: Removed duplicated region for block: B:581:0x0f0a  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x0f66  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0fa6  */
    /* JADX WARN: Removed duplicated region for block: B:593:0x0fcf  */
    /* JADX WARN: Removed duplicated region for block: B:594:0x0ff2  */
    /* JADX WARN: Removed duplicated region for block: B:597:0x100f  */
    /* JADX WARN: Removed duplicated region for block: B:602:0x1036  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x106c  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:613:0x10c9 A[Catch: Exception -> 0x10d1, TryCatch #8 {Exception -> 0x10d1, blocks: (B:611:0x10c2, B:613:0x10c9, B:616:0x10d3), top: B:734:0x10c2 }] */
    /* JADX WARN: Removed duplicated region for block: B:624:0x10f0  */
    /* JADX WARN: Removed duplicated region for block: B:626:0x10fb  */
    /* JADX WARN: Removed duplicated region for block: B:628:0x1100  */
    /* JADX WARN: Removed duplicated region for block: B:636:0x1117  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x112f  */
    /* JADX WARN: Removed duplicated region for block: B:646:0x1135  */
    /* JADX WARN: Removed duplicated region for block: B:649:0x1141  */
    /* JADX WARN: Removed duplicated region for block: B:654:0x114e  */
    /* JADX WARN: Removed duplicated region for block: B:667:0x11d5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:676:0x1207  */
    /* JADX WARN: Removed duplicated region for block: B:680:0x128f  */
    /* JADX WARN: Removed duplicated region for block: B:687:0x12db  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:693:0x12f4  */
    /* JADX WARN: Removed duplicated region for block: B:703:0x1343  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:720:0x0cae A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:726:0x0744 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0287  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02aa  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, long j2, String str2, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str3;
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
        int i8;
        boolean z6;
        ArrayList<StoryNotification> arrayList2;
        ArrayList arrayList3;
        int id;
        LongSparseArray longSparseArray4;
        ArrayList arrayList4;
        long j3;
        long j4;
        Notification notification;
        MessageObject messageObject;
        int i9;
        long j5;
        Integer num;
        int i10;
        LongSparseArray longSparseArray5;
        int i11;
        ArrayList<StoryNotification> arrayList5;
        long j6;
        DialogKey dialogKey2;
        String str4;
        Integer num2;
        long j7;
        TLRPC$User tLRPC$User;
        TLRPC$User tLRPC$User2;
        String string;
        TLRPC$FileLocation tLRPC$FileLocation;
        boolean z7;
        boolean z8;
        TLRPC$Chat tLRPC$Chat;
        boolean z9;
        NotificationsController notificationsController2;
        LongSparseArray longSparseArray6;
        SharedPreferences sharedPreferences;
        int i12;
        int i13;
        boolean z10;
        ArrayList arrayList6;
        Notification notification2;
        LongSparseArray longSparseArray7;
        long j8;
        TLRPC$FileLocation tLRPC$FileLocation2;
        String str5;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$User tLRPC$User3;
        TLRPC$TL_forumTopic findTopic;
        String userName;
        TLRPC$FileLocation tLRPC$FileLocation4;
        TLRPC$User tLRPC$User4;
        String str6;
        String str7;
        TLRPC$FileLocation tLRPC$FileLocation5;
        SharedPreferences sharedPreferences2;
        Bitmap bitmap;
        File file;
        Bitmap bitmap2;
        File file2;
        File file3;
        NotificationsController notificationsController3;
        TLRPC$User tLRPC$User5;
        TLRPC$Chat tLRPC$Chat3;
        String str8;
        int i14;
        String formatString;
        NotificationCompat.Action build;
        Integer num3;
        DialogKey dialogKey3;
        int max;
        long j9;
        long j10;
        Person person;
        NotificationCompat.MessagingStyle messagingStyle;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i15;
        NotificationsController notificationsController4;
        NotificationCompat.Action action;
        DialogKey dialogKey4;
        String str9;
        MessageObject messageObject2;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList7;
        long j11;
        StringBuilder sb;
        LongSparseArray longSparseArray8;
        String str10;
        ArrayList<StoryNotification> arrayList8;
        NotificationCompat.MessagingStyle messagingStyle3;
        int i16;
        Bitmap bitmap3;
        String str11;
        String str12;
        long j12;
        NotificationCompat.MessagingStyle messagingStyle4;
        long j13;
        StringBuilder sb2;
        LongSparseArray longSparseArray9;
        Person person2;
        String str13;
        String str14;
        boolean z11;
        String[] strArr;
        StringBuilder sb3;
        Person person3;
        File file4;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation6;
        String str15;
        LongSparseArray longSparseArray10;
        String str16;
        NotificationCompat.MessagingStyle messagingStyle5;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        int id2;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        File file5;
        final File file6;
        Context context;
        StringBuilder sb4;
        final Uri uriForFile;
        File file7;
        Bitmap createScaledBitmap;
        Canvas canvas;
        String string2;
        DialogKey dialogKey5;
        int i17;
        MessageObject messageObject3;
        long j14;
        NotificationCompat.Action action2;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList9;
        Bitmap bitmap4;
        String str17;
        NotificationCompat.Action action3;
        String str18;
        long j15;
        long j16;
        ArrayList<StoryNotification> arrayList10;
        long j17;
        NotificationCompat.Builder category;
        long j18;
        TLRPC$User tLRPC$User6;
        int size3;
        int i18;
        int i19;
        String str19;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList11;
        long j19;
        LongSparseArray longSparseArray11;
        int i20;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation7;
        Bitmap bitmap5;
        Bitmap decodeFile;
        String string3;
        String formatPluralString;
        String str20;
        String str21;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3;
        TLRPC$FileLocation tLRPC$FileLocation8;
        int i21 = Build.VERSION.SDK_INT;
        if (i21 >= 26) {
            i4 = -1;
            str3 = ".provider";
            builder2 = builder;
            builder2.setChannelId(validateChannelId(j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
        } else {
            str3 = ".provider";
            builder2 = builder;
            i4 = -1;
        }
        Notification build2 = builder.build();
        if (i21 <= 19) {
            notificationManager.notify(this.notificationId, build2);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
                return;
            }
            return;
        }
        NotificationsController notificationsController5 = this;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList12 = new ArrayList();
        if (!notificationsController5.storyPushMessages.isEmpty()) {
            arrayList12.add(new DialogKey(0L, 0L, true));
        }
        LongSparseArray longSparseArray12 = new LongSparseArray();
        for (int i22 = 0; i22 < notificationsController5.pushMessages.size(); i22++) {
            MessageObject messageObject4 = notificationsController5.pushMessages.get(i22);
            long dialogId = messageObject4.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController5.currentAccount, messageObject4.messageOwner, getMessagesController().isForum(messageObject4));
            int i23 = notificationsSettings.getInt("dismissDate" + dialogId, 0);
            if (messageObject4.isStoryPush || messageObject4.messageOwner.date > i23) {
                ArrayList arrayList13 = (ArrayList) longSparseArray12.get(dialogId);
                if (arrayList13 == null) {
                    ArrayList arrayList14 = new ArrayList();
                    longSparseArray12.put(dialogId, arrayList14);
                    arrayList12.add(new DialogKey(dialogId, topicId, false));
                    arrayList13 = arrayList14;
                }
                arrayList13.add(messageObject4);
            }
        }
        LongSparseArray longSparseArray13 = new LongSparseArray();
        for (int i24 = 0; i24 < notificationsController5.wearNotificationsIds.size(); i24++) {
            longSparseArray13.put(notificationsController5.wearNotificationsIds.keyAt(i24), notificationsController5.wearNotificationsIds.valueAt(i24));
        }
        notificationsController5.wearNotificationsIds.clear();
        ArrayList arrayList15 = new ArrayList();
        int i25 = Build.VERSION.SDK_INT;
        if (i25 > 27) {
            if (arrayList12.size() <= (notificationsController5.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i25 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                SharedConfig.passcodeHash.length();
                longSparseArray = new LongSparseArray();
                size = arrayList12.size();
                i5 = 0;
                while (i5 < size && arrayList15.size() < 7) {
                    dialogKey = (DialogKey) arrayList12.get(i5);
                    int i26 = size;
                    if (!dialogKey.story) {
                        ArrayList<StoryNotification> arrayList16 = new ArrayList<>();
                        if (notificationsController5.storyPushMessages.isEmpty()) {
                            j8 = clientUserId;
                            i13 = i5;
                            z10 = z4;
                            longSparseArray6 = longSparseArray;
                            longSparseArray7 = longSparseArray13;
                            longSparseArray4 = longSparseArray12;
                            arrayList4 = arrayList12;
                            sharedPreferences = notificationsSettings;
                            notification2 = build2;
                            notificationsController2 = notificationsController5;
                            i12 = i26;
                            str6 = str3;
                            arrayList6 = arrayList15;
                            i5 = i13 + 1;
                            arrayList15 = arrayList6;
                            size = i12;
                            notificationsSettings = sharedPreferences;
                            arrayList12 = arrayList4;
                            longSparseArray12 = longSparseArray4;
                            clientUserId = j8;
                            z4 = z10;
                            str3 = str6;
                            longSparseArray13 = longSparseArray7;
                            longSparseArray = longSparseArray6;
                            build2 = notification2;
                            i4 = -1;
                            notificationsController5 = notificationsController2;
                        } else {
                            i8 = i5;
                            long j20 = notificationsController5.storyPushMessages.get(0).dialogId;
                            int i27 = 0;
                            for (Integer num4 : notificationsController5.storyPushMessages.get(0).dateByIds.keySet()) {
                                i27 = Math.max(i27, num4.intValue());
                            }
                            longSparseArray4 = longSparseArray12;
                            notification = build2;
                            messageObject = null;
                            ArrayList arrayList17 = arrayList15;
                            id = i27;
                            arrayList2 = arrayList16;
                            z6 = z4;
                            j4 = j20;
                            arrayList3 = arrayList17;
                            arrayList4 = arrayList12;
                            j3 = 0;
                        }
                    } else {
                        i8 = i5;
                        long j21 = dialogKey.dialogId;
                        z6 = z4;
                        long j22 = dialogKey.topicId;
                        ArrayList<StoryNotification> arrayList18 = (ArrayList) longSparseArray12.get(j21);
                        arrayList2 = arrayList18;
                        arrayList3 = arrayList15;
                        id = ((MessageObject) arrayList18.get(0)).getId();
                        longSparseArray4 = longSparseArray12;
                        arrayList4 = arrayList12;
                        j3 = j22;
                        j4 = j21;
                        notification = build2;
                        messageObject = (MessageObject) arrayList18.get(0);
                    }
                    Integer num5 = (Integer) longSparseArray13.get(j4);
                    i9 = id;
                    if (dialogKey.story) {
                        if (num5 == null) {
                            j5 = j3;
                            num5 = Integer.valueOf(((int) j4) + ((int) (j4 >> 32)));
                        } else {
                            j5 = j3;
                            longSparseArray13.remove(j4);
                        }
                        num = num5;
                    } else {
                        num = 2147483646;
                        j5 = j3;
                    }
                    int i28 = 0;
                    for (i10 = 0; i10 < arrayList2.size(); i10++) {
                        if (i28 < ((MessageObject) arrayList2.get(i10)).messageOwner.date) {
                            i28 = ((MessageObject) arrayList2.get(i10)).messageOwner.date;
                        }
                    }
                    if (!dialogKey.story) {
                        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j4));
                        longSparseArray5 = longSparseArray13;
                        if (notificationsController5.storyPushMessages.size() == 1) {
                            if (user2 != null) {
                                formatPluralString = UserObject.getFirstName(user2);
                            } else {
                                formatPluralString = notificationsController5.storyPushMessages.get(0).localName;
                            }
                            i11 = i28;
                        } else {
                            i11 = i28;
                            formatPluralString = LocaleController.formatPluralString("Stories", notificationsController5.storyPushMessages.size(), new Object[0]);
                        }
                        if (user2 == null || (tLRPC$UserProfilePhoto3 = user2.photo) == null || (tLRPC$FileLocation8 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            str20 = "Stories";
                            str21 = formatPluralString;
                        } else {
                            str20 = "Stories";
                            str21 = formatPluralString;
                            if (tLRPC$FileLocation8.volume_id != 0 && tLRPC$FileLocation8.local_id != 0) {
                                j6 = clientUserId;
                                dialogKey2 = dialogKey;
                                tLRPC$FileLocation = tLRPC$FileLocation8;
                                str4 = str20;
                                num2 = num;
                                z7 = false;
                                z8 = false;
                                tLRPC$Chat = null;
                                z9 = false;
                                tLRPC$User2 = user2;
                                string = str21;
                                arrayList5 = arrayList2;
                                j7 = j5;
                            }
                        }
                        j6 = clientUserId;
                        dialogKey2 = dialogKey;
                        str4 = str20;
                        num2 = num;
                        tLRPC$FileLocation = null;
                        z7 = false;
                        z8 = false;
                        tLRPC$Chat = null;
                        z9 = false;
                        tLRPC$User2 = user2;
                        string = str21;
                        arrayList5 = arrayList2;
                        j7 = j5;
                    } else {
                        longSparseArray5 = longSparseArray13;
                        i11 = i28;
                        if (!DialogObject.isEncryptedDialog(j4)) {
                            z7 = (messageObject == null || messageObject.isReactionPush || messageObject.isStoryReactionPush || j4 == 777000) ? false : true;
                            if (DialogObject.isUserDialog(j4)) {
                                TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j4));
                                if (user3 == null) {
                                    if (messageObject.isFcmMessage()) {
                                        userName = messageObject.localName;
                                    } else {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.w("not found user to show dialog notification " + j4);
                                        }
                                        longSparseArray6 = longSparseArray;
                                        sharedPreferences = notificationsSettings;
                                        notificationsController2 = notificationsController5;
                                        i12 = i26;
                                        i13 = i8;
                                        z10 = z6;
                                        arrayList6 = arrayList3;
                                        notification2 = notification;
                                        longSparseArray7 = longSparseArray5;
                                        j8 = clientUserId;
                                        str6 = str3;
                                    }
                                } else {
                                    userName = UserObject.getUserName(user3);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user3.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        tLRPC$User4 = user3;
                                        arrayList5 = arrayList2;
                                        if (tLRPC$FileLocation4.volume_id != 0) {
                                        }
                                        tLRPC$FileLocation4 = null;
                                        if (!UserObject.isReplyUser(j4)) {
                                            string = LocaleController.getString(R.string.RepliesTitle);
                                        } else if (j4 == clientUserId) {
                                            string = LocaleController.getString(R.string.MessageScheduledReminderNotification);
                                        } else {
                                            j6 = clientUserId;
                                            dialogKey2 = dialogKey;
                                            str5 = userName;
                                            tLRPC$FileLocation3 = tLRPC$FileLocation4;
                                            num2 = num;
                                            j7 = j5;
                                            tLRPC$User3 = tLRPC$User4;
                                            tLRPC$Chat2 = null;
                                            z8 = false;
                                            z9 = false;
                                            str4 = "Stories";
                                            tLRPC$User2 = tLRPC$User3;
                                            tLRPC$Chat = tLRPC$Chat2;
                                            string = str5;
                                            tLRPC$FileLocation = tLRPC$FileLocation3;
                                        }
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey;
                                        tLRPC$FileLocation = tLRPC$FileLocation4;
                                        num2 = num;
                                        j7 = j5;
                                        z8 = false;
                                        tLRPC$Chat = null;
                                        z9 = false;
                                        TLRPC$User tLRPC$User7 = tLRPC$User4;
                                        str4 = "Stories";
                                        tLRPC$User2 = tLRPC$User7;
                                    }
                                }
                                tLRPC$User4 = user3;
                                arrayList5 = arrayList2;
                                tLRPC$FileLocation4 = null;
                                if (!UserObject.isReplyUser(j4)) {
                                }
                                j6 = clientUserId;
                                dialogKey2 = dialogKey;
                                tLRPC$FileLocation = tLRPC$FileLocation4;
                                num2 = num;
                                j7 = j5;
                                z8 = false;
                                tLRPC$Chat = null;
                                z9 = false;
                                TLRPC$User tLRPC$User72 = tLRPC$User4;
                                str4 = "Stories";
                                tLRPC$User2 = tLRPC$User72;
                            } else {
                                arrayList5 = arrayList2;
                                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j4));
                                if (chat == null) {
                                    if (messageObject.isFcmMessage()) {
                                        boolean isSupergroup = messageObject.isSupergroup();
                                        String str22 = messageObject.localName;
                                        z8 = messageObject.localChannel;
                                        str4 = "Stories";
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey;
                                        z9 = isSupergroup;
                                        num2 = num;
                                        j7 = j5;
                                        tLRPC$FileLocation = null;
                                        z7 = false;
                                        tLRPC$User2 = null;
                                        tLRPC$Chat = chat;
                                        string = str22;
                                    } else {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.w("not found chat to show dialog notification " + j4);
                                        }
                                        longSparseArray6 = longSparseArray;
                                        sharedPreferences = notificationsSettings;
                                        notificationsController2 = notificationsController5;
                                        i12 = i26;
                                        i13 = i8;
                                        z10 = z6;
                                        arrayList6 = arrayList3;
                                        notification2 = notification;
                                        longSparseArray7 = longSparseArray5;
                                        j8 = clientUserId;
                                        str6 = str3;
                                    }
                                } else {
                                    boolean z12 = chat.megagroup;
                                    z8 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str23 = chat.title;
                                    str4 = "Stories";
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) == null) {
                                        dialogKey2 = dialogKey;
                                        z9 = z12;
                                    } else {
                                        dialogKey2 = dialogKey;
                                        z9 = z12;
                                        if (tLRPC$FileLocation2.volume_id != 0) {
                                        }
                                    }
                                    tLRPC$FileLocation2 = null;
                                    if (j5 != 0) {
                                        num2 = num;
                                        j6 = clientUserId;
                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                        j7 = j5;
                                        if (getMessagesController().getTopicsController().findTopic(chat.id, j7) != null) {
                                            str5 = findTopic.title + " in " + str23;
                                            if (z7) {
                                                tLRPC$FileLocation3 = tLRPC$FileLocation;
                                                tLRPC$Chat2 = chat;
                                                tLRPC$User3 = null;
                                                tLRPC$User2 = tLRPC$User3;
                                                tLRPC$Chat = tLRPC$Chat2;
                                                string = str5;
                                                tLRPC$FileLocation = tLRPC$FileLocation3;
                                            } else {
                                                z7 = ChatObject.canSendPlain(chat);
                                                tLRPC$Chat = chat;
                                                string = str5;
                                                tLRPC$User2 = null;
                                            }
                                        }
                                    } else {
                                        j6 = clientUserId;
                                        num2 = num;
                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                        j7 = j5;
                                    }
                                    str5 = str23;
                                    if (z7) {
                                    }
                                }
                            }
                            i5 = i13 + 1;
                            arrayList15 = arrayList6;
                            size = i12;
                            notificationsSettings = sharedPreferences;
                            arrayList12 = arrayList4;
                            longSparseArray12 = longSparseArray4;
                            clientUserId = j8;
                            z4 = z10;
                            str3 = str6;
                            longSparseArray13 = longSparseArray7;
                            longSparseArray = longSparseArray6;
                            build2 = notification2;
                            i4 = -1;
                            notificationsController5 = notificationsController2;
                        } else {
                            arrayList5 = arrayList2;
                            j6 = clientUserId;
                            dialogKey2 = dialogKey;
                            str4 = "Stories";
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
                                i12 = i26;
                                i13 = i8;
                                z10 = z6;
                                arrayList6 = arrayList3;
                                notification2 = notification;
                                longSparseArray7 = longSparseArray5;
                                j8 = j6;
                                str6 = str3;
                                i5 = i13 + 1;
                                arrayList15 = arrayList6;
                                size = i12;
                                notificationsSettings = sharedPreferences;
                                arrayList12 = arrayList4;
                                longSparseArray12 = longSparseArray4;
                                clientUserId = j8;
                                z4 = z10;
                                str3 = str6;
                                longSparseArray13 = longSparseArray7;
                                longSparseArray = longSparseArray6;
                                build2 = notification2;
                                i4 = -1;
                                notificationsController5 = notificationsController2;
                            } else {
                                tLRPC$User = null;
                            }
                            tLRPC$User2 = tLRPC$User;
                            string = LocaleController.getString(R.string.SecretChatName);
                            tLRPC$FileLocation = null;
                            z7 = false;
                            z8 = false;
                            tLRPC$Chat = null;
                            z9 = false;
                        }
                    }
                    if (messageObject != null && messageObject.isStoryReactionPush && !notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                        string = LocaleController.getString(R.string.NotificationHiddenChatName);
                        tLRPC$FileLocation = null;
                        z7 = false;
                    }
                    if (z5) {
                        TLRPC$FileLocation tLRPC$FileLocation9 = tLRPC$FileLocation;
                        str7 = string;
                        tLRPC$FileLocation5 = tLRPC$FileLocation9;
                    } else {
                        if (DialogObject.isChatDialog(j4)) {
                            string3 = LocaleController.getString(R.string.NotificationHiddenChatName);
                        } else {
                            string3 = LocaleController.getString(R.string.NotificationHiddenName);
                        }
                        str7 = string3;
                        tLRPC$FileLocation5 = null;
                        z7 = false;
                    }
                    if (tLRPC$FileLocation5 == null) {
                        file = getFileLoader().getPathToAttach(tLRPC$FileLocation5, true);
                        if (Build.VERSION.SDK_INT < 28) {
                            sharedPreferences2 = notificationsSettings;
                            bitmap5 = null;
                            BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation5, null, "50_50");
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
                            sharedPreferences2 = notificationsSettings;
                            bitmap5 = null;
                        }
                        bitmap = bitmap5;
                    } else {
                        sharedPreferences2 = notificationsSettings;
                        bitmap = null;
                        file = null;
                    }
                    if (tLRPC$Chat == null) {
                        Person.Builder name = new Person.Builder().setName(str7);
                        if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                            loadRoundAvatar(file, name);
                        }
                        bitmap2 = bitmap;
                        file2 = file;
                        longSparseArray.put(-tLRPC$Chat.id, name.build());
                    } else {
                        bitmap2 = bitmap;
                        file2 = file;
                    }
                    Bitmap bitmap6 = bitmap2;
                    if (!(z8 || z9) || !z7 || SharedConfig.isWaitingForPasscodeEnter || j6 == j4 || UserObject.isReplyUser(j4)) {
                        str8 = "max_id";
                        tLRPC$Chat3 = tLRPC$Chat;
                        file3 = file2;
                        i14 = i9;
                        notificationsController3 = this;
                        tLRPC$User5 = tLRPC$User2;
                        build = null;
                    } else {
                        file3 = file2;
                        Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                        intent.putExtra("dialog_id", j4);
                        intent.putExtra("max_id", i9);
                        intent.putExtra("topic_id", j7);
                        notificationsController3 = this;
                        tLRPC$User5 = tLRPC$User2;
                        intent.putExtra("currentAccount", notificationsController3.currentAccount);
                        tLRPC$Chat3 = tLRPC$Chat;
                        str8 = "max_id";
                        PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent, 167772160);
                        RemoteInput build3 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString(R.string.Reply)).build();
                        if (!DialogObject.isChatDialog(j4)) {
                            i14 = i9;
                            formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, str7);
                        } else {
                            i14 = i9;
                            formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, str7);
                        }
                        build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build3).setShowsUserInterface(false).build();
                    }
                    num3 = notificationsController3.pushDialogs.get(j4);
                    if (num3 == null) {
                        num3 = 0;
                    }
                    dialogKey3 = dialogKey2;
                    if (!dialogKey3.story) {
                        max = notificationsController3.storyPushMessages.size();
                    } else {
                        max = Math.max(num3.intValue(), arrayList5.size());
                    }
                    String format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? str7 : String.format("%1$s (%2$d)", str7, Integer.valueOf(max));
                    j9 = j6;
                    Person person4 = (Person) longSparseArray.get(j9);
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
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                if (tLRPC$FileLocation7.volume_id != 0 && tLRPC$FileLocation7.local_id != 0) {
                                    Person.Builder name2 = new Person.Builder().setName(LocaleController.getString(R.string.FromYou));
                                    loadRoundAvatar(getFileLoader().getPathToAttach(user.photo.photo_small, true), name2);
                                    person = name2.build();
                                    try {
                                        longSparseArray.put(j9, person);
                                    } catch (Throwable th3) {
                                        th = th3;
                                        person4 = person;
                                        FileLog.e(th);
                                        person = person4;
                                        if (messageObject == null) {
                                        }
                                        String str24 = "";
                                        if (person == null) {
                                        }
                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                        messagingStyle2 = messagingStyle;
                                        i15 = Build.VERSION.SDK_INT;
                                        if (i15 >= 28) {
                                        }
                                        messagingStyle2.setConversationTitle(format);
                                        messagingStyle2.setGroupConversation(i15 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                        StringBuilder sb5 = new StringBuilder();
                                        String[] strArr2 = new String[1];
                                        boolean[] zArr = new boolean[1];
                                        if (dialogKey3.story) {
                                        }
                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        StringBuilder sb6 = new StringBuilder();
                                        sb6.append("com.tmessages.openchat");
                                        String str25 = str10;
                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList19 = arrayList7;
                                        sb6.append(Math.random());
                                        sb6.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent2.setAction(sb6.toString());
                                        intent2.setFlags(ConnectionsManager.FileTypeFile);
                                        intent2.addCategory("android.intent.category.LAUNCHER");
                                        MessageObject messageObject5 = messageObject2;
                                        if (messageObject2 == null) {
                                        }
                                        dialogKey5 = dialogKey4;
                                        if (dialogKey5.story) {
                                        }
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append("show extra notifications chatId ");
                                        sb7.append(j4);
                                        sb7.append(" topicId ");
                                        j14 = j10;
                                        sb7.append(j14);
                                        FileLog.d(sb7.toString());
                                        if (j14 != 0) {
                                        }
                                        String str26 = str9;
                                        intent2.putExtra(str26, notificationsController4.currentAccount);
                                        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                        action2 = action;
                                        if (action != null) {
                                        }
                                        int i29 = i17;
                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                        intent3.addFlags(32);
                                        intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent3.putExtra("dialog_id", j4);
                                        int i30 = i14;
                                        intent3.putExtra(str8, i30);
                                        intent3.putExtra(str26, notificationsController4.currentAccount);
                                        arrayList9 = arrayList19;
                                        bitmap4 = bitmap3;
                                        NotificationCompat.Action build4 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (!DialogObject.isEncryptedDialog(j4)) {
                                        }
                                        if (str18 != null) {
                                        }
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append("tgaccount");
                                        long j23 = j11;
                                        sb8.append(j23);
                                        wearableExtender.setBridgeTag(sb8.toString());
                                        if (dialogKey5.story) {
                                        }
                                        NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str11).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                        if (dialogKey5.story) {
                                        }
                                        category = autoCancel.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent4.putExtra("messageDate", i11);
                                        j18 = j15;
                                        intent4.putExtra("dialogId", j18);
                                        String str27 = str17;
                                        intent4.putExtra(str27, notificationsController4.currentAccount);
                                        if (dialogKey5.story) {
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4, 167772160));
                                        if (z6) {
                                        }
                                        if (action2 != null) {
                                        }
                                        if (!z5) {
                                        }
                                        if (arrayList4.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j18)) {
                                        }
                                        if (bitmap4 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        if (tLRPC$Chat3 == null) {
                                        }
                                        tLRPC$User6 = tLRPC$User5;
                                        boolean z13 = z6;
                                        Notification notification3 = notification;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        i12 = i26;
                                        j8 = j16;
                                        z10 = z13;
                                        i13 = i8;
                                        longSparseArray6 = longSparseArray8;
                                        longSparseArray7 = longSparseArray5;
                                        str6 = str25;
                                        sharedPreferences = sharedPreferences2;
                                        notification2 = notification3;
                                        arrayList6 = arrayList3;
                                        arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str11, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j18, num2);
                                        i5 = i13 + 1;
                                        arrayList15 = arrayList6;
                                        size = i12;
                                        notificationsSettings = sharedPreferences;
                                        arrayList12 = arrayList4;
                                        longSparseArray12 = longSparseArray4;
                                        clientUserId = j8;
                                        z4 = z10;
                                        str3 = str6;
                                        longSparseArray13 = longSparseArray7;
                                        longSparseArray = longSparseArray6;
                                        build2 = notification2;
                                        i4 = -1;
                                        notificationsController5 = notificationsController2;
                                    }
                                    boolean z14 = (messageObject == null && (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) ? false : true;
                                    String str242 = "";
                                    if (person == null && z14) {
                                        messagingStyle = new NotificationCompat.MessagingStyle(person);
                                    } else {
                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                    }
                                    messagingStyle2 = messagingStyle;
                                    i15 = Build.VERSION.SDK_INT;
                                    if (i15 >= 28 || ((DialogObject.isChatDialog(j4) && !z8) || UserObject.isReplyUser(j4))) {
                                        messagingStyle2.setConversationTitle(format);
                                    }
                                    messagingStyle2.setGroupConversation(i15 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                    StringBuilder sb52 = new StringBuilder();
                                    String[] strArr22 = new String[1];
                                    boolean[] zArr2 = new boolean[1];
                                    if (dialogKey3.story) {
                                        ArrayList<String> arrayList20 = new ArrayList<>();
                                        ArrayList<Object> arrayList21 = new ArrayList<>();
                                        notificationsController4 = this;
                                        Pair<Integer, Boolean> parseStoryPushes = notificationsController4.parseStoryPushes(arrayList20, arrayList21);
                                        int intValue = ((Integer) parseStoryPushes.first).intValue();
                                        boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                        if (booleanValue) {
                                            j19 = j9;
                                            sb52.append(LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]));
                                        } else {
                                            j19 = j9;
                                            if (arrayList20.isEmpty()) {
                                                longSparseArray6 = longSparseArray;
                                                notificationsController2 = notificationsController4;
                                                i12 = i26;
                                                i13 = i8;
                                                z10 = z6;
                                                arrayList6 = arrayList3;
                                                notification2 = notification;
                                                sharedPreferences = sharedPreferences2;
                                                longSparseArray7 = longSparseArray5;
                                                j8 = j19;
                                                str6 = str3;
                                                i5 = i13 + 1;
                                                arrayList15 = arrayList6;
                                                size = i12;
                                                notificationsSettings = sharedPreferences;
                                                arrayList12 = arrayList4;
                                                longSparseArray12 = longSparseArray4;
                                                clientUserId = j8;
                                                z4 = z10;
                                                str3 = str6;
                                                longSparseArray13 = longSparseArray7;
                                                longSparseArray = longSparseArray6;
                                                build2 = notification2;
                                                i4 = -1;
                                                notificationsController5 = notificationsController2;
                                            } else if (arrayList20.size() != 1) {
                                                action = build;
                                                if (arrayList20.size() != 2) {
                                                    str9 = "currentAccount";
                                                    if (arrayList20.size() == 3 && notificationsController4.storyPushMessages.size() == 3) {
                                                        dialogKey4 = dialogKey3;
                                                        messageObject2 = messageObject;
                                                        sb52.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController4.cutLastName(arrayList20.get(0)), notificationsController4.cutLastName(arrayList20.get(1)), notificationsController4.cutLastName(arrayList20.get(2))));
                                                        longSparseArray11 = longSparseArray;
                                                    } else {
                                                        dialogKey4 = dialogKey3;
                                                        messageObject2 = messageObject;
                                                        longSparseArray11 = longSparseArray;
                                                        sb52.append(LocaleController.formatPluralString("StoryNotification4", notificationsController4.storyPushMessages.size() - 2, notificationsController4.cutLastName(arrayList20.get(0)), notificationsController4.cutLastName(arrayList20.get(1))));
                                                    }
                                                    long j24 = Long.MAX_VALUE;
                                                    while (i20 < notificationsController4.storyPushMessages.size()) {
                                                    }
                                                    messagingStyle2.setGroupConversation(false);
                                                    if (arrayList20.size() == 1) {
                                                    }
                                                    messagingStyle2.addMessage(sb52, j24, new Person.Builder().setName(r0).build());
                                                    if (booleanValue) {
                                                    }
                                                    arrayList7 = null;
                                                    arrayList8 = arrayList5;
                                                } else {
                                                    str9 = "currentAccount";
                                                    sb52.append(LocaleController.formatString(R.string.StoryNotification2, arrayList20.get(0), arrayList20.get(1)));
                                                    longSparseArray11 = longSparseArray;
                                                    dialogKey4 = dialogKey3;
                                                    messageObject2 = messageObject;
                                                    long j242 = Long.MAX_VALUE;
                                                    while (i20 < notificationsController4.storyPushMessages.size()) {
                                                    }
                                                    messagingStyle2.setGroupConversation(false);
                                                    if (arrayList20.size() == 1) {
                                                    }
                                                    messagingStyle2.addMessage(sb52, j242, new Person.Builder().setName(r0).build());
                                                    if (booleanValue) {
                                                    }
                                                    arrayList7 = null;
                                                    arrayList8 = arrayList5;
                                                }
                                            } else if (intValue == 1) {
                                                sb52.append(LocaleController.getString("StoryNotificationSingle"));
                                            } else {
                                                action = build;
                                                sb52.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList20.get(0)));
                                                longSparseArray11 = longSparseArray;
                                                dialogKey4 = dialogKey3;
                                                str9 = "currentAccount";
                                                messageObject2 = messageObject;
                                                long j2422 = Long.MAX_VALUE;
                                                for (i20 = 0; i20 < notificationsController4.storyPushMessages.size(); i20++) {
                                                    j2422 = Math.min(notificationsController4.storyPushMessages.get(i20).date, j2422);
                                                }
                                                messagingStyle2.setGroupConversation(false);
                                                String formatPluralString2 = (arrayList20.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str4, intValue, new Object[0]) : arrayList20.get(0);
                                                messagingStyle2.addMessage(sb52, j2422, new Person.Builder().setName(formatPluralString2).build());
                                                if (booleanValue) {
                                                    bitmap3 = loadMultipleAvatars(arrayList21);
                                                    str11 = formatPluralString2;
                                                    messagingStyle3 = messagingStyle2;
                                                    sb = sb52;
                                                    longSparseArray8 = longSparseArray11;
                                                    str10 = str3;
                                                    j11 = j19;
                                                    i16 = 0;
                                                } else {
                                                    str11 = formatPluralString2;
                                                    messagingStyle3 = messagingStyle2;
                                                    sb = sb52;
                                                    longSparseArray8 = longSparseArray11;
                                                    str10 = str3;
                                                    j11 = j19;
                                                    i16 = 0;
                                                    bitmap3 = null;
                                                }
                                                arrayList7 = null;
                                                arrayList8 = arrayList5;
                                            }
                                        }
                                        longSparseArray11 = longSparseArray;
                                        action = build;
                                        dialogKey4 = dialogKey3;
                                        str9 = "currentAccount";
                                        messageObject2 = messageObject;
                                        long j24222 = Long.MAX_VALUE;
                                        while (i20 < notificationsController4.storyPushMessages.size()) {
                                        }
                                        messagingStyle2.setGroupConversation(false);
                                        if (arrayList20.size() == 1) {
                                        }
                                        messagingStyle2.addMessage(sb52, j24222, new Person.Builder().setName(formatPluralString2).build());
                                        if (booleanValue) {
                                        }
                                        arrayList7 = null;
                                        arrayList8 = arrayList5;
                                    } else {
                                        notificationsController4 = this;
                                        LongSparseArray longSparseArray14 = longSparseArray;
                                        action = build;
                                        dialogKey4 = dialogKey3;
                                        str9 = "currentAccount";
                                        messageObject2 = messageObject;
                                        long j25 = j9;
                                        int size4 = arrayList5.size() - 1;
                                        int i31 = 0;
                                        arrayList7 = null;
                                        while (size4 >= 0) {
                                            int i32 = i31;
                                            ArrayList<StoryNotification> arrayList22 = arrayList5;
                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList23 = arrayList7;
                                            MessageObject messageObject6 = (MessageObject) arrayList22.get(size4);
                                            int i33 = size4;
                                            if (j10 == MessageObject.getTopicId(notificationsController4.currentAccount, messageObject6.messageOwner, getMessagesController().isForum(messageObject6))) {
                                                String shortStringForMessage = notificationsController4.getShortStringForMessage(messageObject6, strArr22, zArr2);
                                                if (j4 == j25) {
                                                    strArr22[0] = str7;
                                                } else if (DialogObject.isChatDialog(j4) && messageObject6.messageOwner.from_scheduled) {
                                                    strArr22[0] = LocaleController.getString(R.string.NotificationMessageScheduledName);
                                                }
                                                if (shortStringForMessage == null) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.w("message text is null for " + messageObject6.getId() + " did = " + messageObject6.getDialogId());
                                                    }
                                                } else {
                                                    if (sb52.length() > 0) {
                                                        sb52.append("\n\n");
                                                    }
                                                    if (j4 != j25 && messageObject6.messageOwner.from_scheduled && DialogObject.isUserDialog(j4)) {
                                                        str12 = str7;
                                                        j12 = j25;
                                                        shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString(R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                        sb52.append(shortStringForMessage);
                                                        messagingStyle4 = messagingStyle2;
                                                    } else {
                                                        str12 = str7;
                                                        j12 = j25;
                                                        String str28 = strArr22[0];
                                                        if (str28 != null) {
                                                            messagingStyle4 = messagingStyle2;
                                                            sb52.append(String.format("%1$s: %2$s", str28, shortStringForMessage));
                                                        } else {
                                                            messagingStyle4 = messagingStyle2;
                                                            sb52.append(shortStringForMessage);
                                                        }
                                                    }
                                                    String str29 = shortStringForMessage;
                                                    if (!DialogObject.isUserDialog(j4)) {
                                                        if (z8) {
                                                            j13 = -j4;
                                                        } else if (DialogObject.isChatDialog(j4)) {
                                                            j13 = messageObject6.getSenderId();
                                                        }
                                                        sb2 = sb52;
                                                        longSparseArray9 = longSparseArray14;
                                                        person2 = (Person) longSparseArray9.get(j13 + (j10 << 16));
                                                        str13 = strArr22[0];
                                                        if (str13 == null) {
                                                            if (z5) {
                                                                if (DialogObject.isChatDialog(j4)) {
                                                                    if (z8) {
                                                                        if (Build.VERSION.SDK_INT > 27) {
                                                                            string2 = LocaleController.getString(R.string.NotificationHiddenChatName);
                                                                        }
                                                                    } else {
                                                                        string2 = LocaleController.getString(R.string.NotificationHiddenChatUserName);
                                                                    }
                                                                    str13 = string2;
                                                                } else if (Build.VERSION.SDK_INT > 27) {
                                                                    string2 = LocaleController.getString(R.string.NotificationHiddenName);
                                                                    str13 = string2;
                                                                }
                                                            }
                                                            str13 = str242;
                                                        }
                                                        if (person2 == null && TextUtils.equals(person2.getName(), str13)) {
                                                            str14 = str242;
                                                            z11 = z8;
                                                            strArr = strArr22;
                                                            person3 = person2;
                                                            sb3 = sb2;
                                                        } else {
                                                            Person.Builder name3 = new Person.Builder().setName(str13);
                                                            if (zArr2[0] || DialogObject.isEncryptedDialog(j4) || Build.VERSION.SDK_INT < 28) {
                                                                str14 = str242;
                                                                z11 = z8;
                                                                strArr = strArr22;
                                                                sb3 = sb2;
                                                            } else {
                                                                if (DialogObject.isUserDialog(j4) || z8) {
                                                                    str14 = str242;
                                                                    z11 = z8;
                                                                    strArr = strArr22;
                                                                    sb3 = sb2;
                                                                    file4 = file3;
                                                                } else {
                                                                    long senderId = messageObject6.getSenderId();
                                                                    z11 = z8;
                                                                    strArr = strArr22;
                                                                    TLRPC$User user4 = getMessagesController().getUser(Long.valueOf(senderId));
                                                                    if (user4 == null && (user4 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                        getMessagesController().putUser(user4, true);
                                                                    }
                                                                    if (user4 == null || (tLRPC$UserProfilePhoto = user4.photo) == null || (tLRPC$FileLocation6 = tLRPC$UserProfilePhoto.photo_small) == null) {
                                                                        str14 = str242;
                                                                        sb3 = sb2;
                                                                    } else {
                                                                        str14 = str242;
                                                                        sb3 = sb2;
                                                                        if (tLRPC$FileLocation6.volume_id != 0 && tLRPC$FileLocation6.local_id != 0) {
                                                                            file4 = getFileLoader().getPathToAttach(user4.photo.photo_small, true);
                                                                        }
                                                                    }
                                                                    file4 = null;
                                                                }
                                                                loadRoundAvatar(file4, name3);
                                                            }
                                                            Person build5 = name3.build();
                                                            longSparseArray9.put(j13, build5);
                                                            person3 = build5;
                                                        }
                                                        if (DialogObject.isEncryptedDialog(j4)) {
                                                            if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z5 || messageObject6.isSecretMedia() || !(messageObject6.type == 1 || messageObject6.isSticker())) {
                                                                str15 = str14;
                                                                longSparseArray10 = longSparseArray9;
                                                                str16 = str3;
                                                            } else {
                                                                File pathToMessage = getFileLoader().getPathToMessage(messageObject6.messageOwner);
                                                                if (pathToMessage.exists() && messageObject6.hasMediaSpoilers()) {
                                                                    file6 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                    if (file6.exists()) {
                                                                        file7 = pathToMessage;
                                                                        longSparseArray10 = longSparseArray9;
                                                                    } else {
                                                                        try {
                                                                            Bitmap decodeFile2 = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                            Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile2);
                                                                            decodeFile2.recycle();
                                                                            createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile2.getWidth(), decodeFile2.getHeight(), true);
                                                                            Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                            stackBlurBitmapMax.recycle();
                                                                            canvas = new Canvas(createScaledBitmap);
                                                                            longSparseArray10 = longSparseArray9;
                                                                            try {
                                                                                notificationsController4.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i4) * 0.325f)));
                                                                                file7 = pathToMessage;
                                                                            } catch (Exception e) {
                                                                                e = e;
                                                                                file7 = pathToMessage;
                                                                            }
                                                                        } catch (Exception e2) {
                                                                            e = e2;
                                                                            file7 = pathToMessage;
                                                                            longSparseArray10 = longSparseArray9;
                                                                        }
                                                                        try {
                                                                            notificationsController4.mediaSpoilerEffect.setBounds(0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight());
                                                                            notificationsController4.mediaSpoilerEffect.draw(canvas);
                                                                            FileOutputStream fileOutputStream = new FileOutputStream(file6);
                                                                            createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                                                            fileOutputStream.close();
                                                                            createScaledBitmap.recycle();
                                                                            file5 = file6;
                                                                        } catch (Exception e3) {
                                                                            e = e3;
                                                                            FileLog.e(e);
                                                                            file5 = file7;
                                                                            str15 = str14;
                                                                            NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str29, messageObject6.messageOwner.date * 1000, person3);
                                                                            String str30 = !messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                            if (!file5.exists()) {
                                                                            }
                                                                            if (j4 == 777000) {
                                                                            }
                                                                            arrayList7 = arrayList23;
                                                                            id2 = i32;
                                                                            i4 = -1;
                                                                            size4 = i33 - 1;
                                                                            str242 = str15;
                                                                            str3 = str16;
                                                                            sb52 = sb3;
                                                                            z8 = z11;
                                                                            arrayList5 = arrayList22;
                                                                            str7 = str12;
                                                                            strArr22 = strArr;
                                                                            longSparseArray14 = longSparseArray10;
                                                                            i31 = id2;
                                                                            messagingStyle2 = messagingStyle5;
                                                                            j25 = j12;
                                                                        }
                                                                    }
                                                                    file5 = file7;
                                                                } else {
                                                                    longSparseArray10 = longSparseArray9;
                                                                    file5 = pathToMessage;
                                                                    file6 = null;
                                                                }
                                                                str15 = str14;
                                                                NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str29, messageObject6.messageOwner.date * 1000, person3);
                                                                String str302 = !messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                if (!file5.exists()) {
                                                                    try {
                                                                        context = ApplicationLoader.applicationContext;
                                                                        sb4 = new StringBuilder();
                                                                        sb4.append(ApplicationLoader.getApplicationId());
                                                                        str16 = str3;
                                                                    } catch (Exception e4) {
                                                                        e = e4;
                                                                        str16 = str3;
                                                                    }
                                                                    try {
                                                                        sb4.append(str16);
                                                                        uriForFile = FileProvider.getUriForFile(context, sb4.toString(), file5);
                                                                    } catch (Exception e5) {
                                                                        e = e5;
                                                                        FileLog.e(e);
                                                                        uriForFile = null;
                                                                        if (uriForFile != null) {
                                                                        }
                                                                        messagingStyle5 = messagingStyle4;
                                                                        messagingStyle5.addMessage(str29, messageObject6.messageOwner.date * 1000, person3);
                                                                        if (zArr2[0]) {
                                                                        }
                                                                        if (j4 == 777000) {
                                                                        }
                                                                        arrayList7 = arrayList23;
                                                                        id2 = i32;
                                                                        i4 = -1;
                                                                        size4 = i33 - 1;
                                                                        str242 = str15;
                                                                        str3 = str16;
                                                                        sb52 = sb3;
                                                                        z8 = z11;
                                                                        arrayList5 = arrayList22;
                                                                        str7 = str12;
                                                                        strArr22 = strArr;
                                                                        longSparseArray14 = longSparseArray10;
                                                                        i31 = id2;
                                                                        messagingStyle2 = messagingStyle5;
                                                                        j25 = j12;
                                                                    }
                                                                    if (uriForFile != null) {
                                                                        message2.setData(str302, uriForFile);
                                                                        messagingStyle5 = messagingStyle4;
                                                                        messagingStyle5.addMessage(message2);
                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda55
                                                                            @Override // java.lang.Runnable
                                                                            public final void run() {
                                                                                NotificationsController.lambda$showExtraNotifications$42(uriForFile, file6);
                                                                            }
                                                                        }, 20000L);
                                                                        if (!TextUtils.isEmpty(messageObject6.caption)) {
                                                                            messagingStyle5.addMessage(messageObject6.caption, messageObject6.messageOwner.date * 1000, person3);
                                                                        }
                                                                        if (zArr2[0] && !z5 && messageObject6.isVoice()) {
                                                                            messages = messagingStyle5.getMessages();
                                                                            if (!messages.isEmpty()) {
                                                                                File pathToMessage2 = getFileLoader().getPathToMessage(messageObject6.messageOwner);
                                                                                if (Build.VERSION.SDK_INT >= 24) {
                                                                                    try {
                                                                                        uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + str16, pathToMessage2);
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
                                                                    str16 = str3;
                                                                    if (getFileLoader().isLoadingFile(file5.getName())) {
                                                                        Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                        StringBuilder sb9 = new StringBuilder();
                                                                        sb9.append(notificationsController4.currentAccount);
                                                                        str15 = str15;
                                                                        sb9.append(str15);
                                                                        uriForFile = appendPath.appendPath(sb9.toString()).appendPath(file5.getName()).appendQueryParameter("final_path", file5.getAbsolutePath()).build();
                                                                        if (uriForFile != null) {
                                                                        }
                                                                    }
                                                                    uriForFile = null;
                                                                    if (uriForFile != null) {
                                                                    }
                                                                }
                                                            }
                                                            messagingStyle5 = messagingStyle4;
                                                            messagingStyle5.addMessage(str29, messageObject6.messageOwner.date * 1000, person3);
                                                            if (zArr2[0]) {
                                                                messages = messagingStyle5.getMessages();
                                                                if (!messages.isEmpty()) {
                                                                }
                                                            }
                                                        } else {
                                                            str15 = str14;
                                                            longSparseArray10 = longSparseArray9;
                                                            str16 = str3;
                                                            messagingStyle5 = messagingStyle4;
                                                            messagingStyle5.addMessage(str29, messageObject6.messageOwner.date * 1000, person3);
                                                        }
                                                        if (j4 == 777000 && (tLRPC$ReplyMarkup = messageObject6.messageOwner.reply_markup) != null) {
                                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList24 = tLRPC$ReplyMarkup.rows;
                                                            id2 = messageObject6.getId();
                                                            arrayList7 = arrayList24;
                                                            i4 = -1;
                                                            size4 = i33 - 1;
                                                            str242 = str15;
                                                            str3 = str16;
                                                            sb52 = sb3;
                                                            z8 = z11;
                                                            arrayList5 = arrayList22;
                                                            str7 = str12;
                                                            strArr22 = strArr;
                                                            longSparseArray14 = longSparseArray10;
                                                            i31 = id2;
                                                            messagingStyle2 = messagingStyle5;
                                                            j25 = j12;
                                                        }
                                                        arrayList7 = arrayList23;
                                                        id2 = i32;
                                                        i4 = -1;
                                                        size4 = i33 - 1;
                                                        str242 = str15;
                                                        str3 = str16;
                                                        sb52 = sb3;
                                                        z8 = z11;
                                                        arrayList5 = arrayList22;
                                                        str7 = str12;
                                                        strArr22 = strArr;
                                                        longSparseArray14 = longSparseArray10;
                                                        i31 = id2;
                                                        messagingStyle2 = messagingStyle5;
                                                        j25 = j12;
                                                    }
                                                    j13 = j4;
                                                    sb2 = sb52;
                                                    longSparseArray9 = longSparseArray14;
                                                    person2 = (Person) longSparseArray9.get(j13 + (j10 << 16));
                                                    str13 = strArr22[0];
                                                    if (str13 == null) {
                                                    }
                                                    if (person2 == null) {
                                                    }
                                                    Person.Builder name32 = new Person.Builder().setName(str13);
                                                    if (zArr2[0]) {
                                                    }
                                                    str14 = str242;
                                                    z11 = z8;
                                                    strArr = strArr22;
                                                    sb3 = sb2;
                                                    Person build52 = name32.build();
                                                    longSparseArray9.put(j13, build52);
                                                    person3 = build52;
                                                    if (DialogObject.isEncryptedDialog(j4)) {
                                                    }
                                                    if (j4 == 777000) {
                                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList242 = tLRPC$ReplyMarkup.rows;
                                                        id2 = messageObject6.getId();
                                                        arrayList7 = arrayList242;
                                                        i4 = -1;
                                                        size4 = i33 - 1;
                                                        str242 = str15;
                                                        str3 = str16;
                                                        sb52 = sb3;
                                                        z8 = z11;
                                                        arrayList5 = arrayList22;
                                                        str7 = str12;
                                                        strArr22 = strArr;
                                                        longSparseArray14 = longSparseArray10;
                                                        i31 = id2;
                                                        messagingStyle2 = messagingStyle5;
                                                        j25 = j12;
                                                    }
                                                    arrayList7 = arrayList23;
                                                    id2 = i32;
                                                    i4 = -1;
                                                    size4 = i33 - 1;
                                                    str242 = str15;
                                                    str3 = str16;
                                                    sb52 = sb3;
                                                    z8 = z11;
                                                    arrayList5 = arrayList22;
                                                    str7 = str12;
                                                    strArr22 = strArr;
                                                    longSparseArray14 = longSparseArray10;
                                                    i31 = id2;
                                                    messagingStyle2 = messagingStyle5;
                                                    j25 = j12;
                                                }
                                            }
                                            str12 = str7;
                                            j12 = j25;
                                            strArr = strArr22;
                                            longSparseArray10 = longSparseArray14;
                                            str16 = str3;
                                            messagingStyle5 = messagingStyle2;
                                            sb3 = sb52;
                                            z11 = z8;
                                            str15 = str242;
                                            arrayList7 = arrayList23;
                                            id2 = i32;
                                            i4 = -1;
                                            size4 = i33 - 1;
                                            str242 = str15;
                                            str3 = str16;
                                            sb52 = sb3;
                                            z8 = z11;
                                            arrayList5 = arrayList22;
                                            str7 = str12;
                                            strArr22 = strArr;
                                            longSparseArray14 = longSparseArray10;
                                            i31 = id2;
                                            messagingStyle2 = messagingStyle5;
                                            j25 = j12;
                                        }
                                        j11 = j25;
                                        sb = sb52;
                                        int i34 = i31;
                                        longSparseArray8 = longSparseArray14;
                                        str10 = str3;
                                        arrayList8 = arrayList5;
                                        messagingStyle3 = messagingStyle2;
                                        i16 = i34;
                                        bitmap3 = bitmap6;
                                        str11 = str7;
                                    }
                                    Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                    StringBuilder sb62 = new StringBuilder();
                                    sb62.append("com.tmessages.openchat");
                                    String str252 = str10;
                                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList192 = arrayList7;
                                    sb62.append(Math.random());
                                    sb62.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    intent22.setAction(sb62.toString());
                                    intent22.setFlags(ConnectionsManager.FileTypeFile);
                                    intent22.addCategory("android.intent.category.LAUNCHER");
                                    MessageObject messageObject52 = messageObject2;
                                    if (messageObject2 == null && messageObject52.isStoryReactionPush) {
                                        intent22.putExtra("storyId", Math.abs(messageObject52.getId()));
                                        i17 = i16;
                                        messageObject3 = messageObject52;
                                        dialogKey5 = dialogKey4;
                                    } else {
                                        dialogKey5 = dialogKey4;
                                        if (dialogKey5.story) {
                                            long[] jArr2 = new long[notificationsController4.storyPushMessages.size()];
                                            int i35 = 0;
                                            while (i35 < notificationsController4.storyPushMessages.size()) {
                                                jArr2[i35] = notificationsController4.storyPushMessages.get(i35).dialogId;
                                                i35++;
                                                i16 = i16;
                                                messageObject52 = messageObject52;
                                            }
                                            i17 = i16;
                                            messageObject3 = messageObject52;
                                            intent22.putExtra("storyDialogIds", jArr2);
                                        } else {
                                            i17 = i16;
                                            messageObject3 = messageObject52;
                                            if (DialogObject.isEncryptedDialog(j4)) {
                                                intent22.putExtra("encId", DialogObject.getEncryptedChatId(j4));
                                            } else if (DialogObject.isUserDialog(j4)) {
                                                intent22.putExtra("userId", j4);
                                            } else {
                                                intent22.putExtra("chatId", -j4);
                                            }
                                        }
                                    }
                                    StringBuilder sb72 = new StringBuilder();
                                    sb72.append("show extra notifications chatId ");
                                    sb72.append(j4);
                                    sb72.append(" topicId ");
                                    j14 = j10;
                                    sb72.append(j14);
                                    FileLog.d(sb72.toString());
                                    if (j14 != 0) {
                                        intent22.putExtra("topicId", j14);
                                    }
                                    String str262 = str9;
                                    intent22.putExtra(str262, notificationsController4.currentAccount);
                                    PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                    NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                    action2 = action;
                                    if (action != null) {
                                        wearableExtender2.addAction(action2);
                                    }
                                    int i292 = i17;
                                    Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                    intent32.addFlags(32);
                                    intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                    intent32.putExtra("dialog_id", j4);
                                    int i302 = i14;
                                    intent32.putExtra(str8, i302);
                                    intent32.putExtra(str262, notificationsController4.currentAccount);
                                    arrayList9 = arrayList192;
                                    bitmap4 = bitmap3;
                                    NotificationCompat.Action build42 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                    if (!DialogObject.isEncryptedDialog(j4)) {
                                        if (DialogObject.isUserDialog(j4)) {
                                            str17 = str262;
                                            action3 = build42;
                                            str18 = "tguser" + j4 + "_" + i302;
                                        } else {
                                            StringBuilder sb10 = new StringBuilder();
                                            sb10.append("tgchat");
                                            str17 = str262;
                                            action3 = build42;
                                            sb10.append(-j4);
                                            sb10.append("_");
                                            sb10.append(i302);
                                            str18 = sb10.toString();
                                        }
                                    } else {
                                        str17 = str262;
                                        action3 = build42;
                                        str18 = j4 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j4) + "_" + i302 : null;
                                    }
                                    if (str18 != null) {
                                        wearableExtender2.setDismissalId(str18);
                                        NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                        wearableExtender3.setDismissalId("summary_" + str18);
                                        builder.extend(wearableExtender3);
                                    }
                                    StringBuilder sb82 = new StringBuilder();
                                    sb82.append("tgaccount");
                                    long j232 = j11;
                                    sb82.append(j232);
                                    wearableExtender2.setBridgeTag(sb82.toString());
                                    if (dialogKey5.story) {
                                        j16 = j232;
                                        j17 = Long.MAX_VALUE;
                                        int i36 = 0;
                                        while (i36 < notificationsController4.storyPushMessages.size()) {
                                            j17 = Math.min(notificationsController4.storyPushMessages.get(i36).date, j17);
                                            i36++;
                                            j4 = j4;
                                        }
                                        j15 = j4;
                                        arrayList10 = arrayList8;
                                    } else {
                                        j15 = j4;
                                        j16 = j232;
                                        arrayList10 = arrayList8;
                                        j17 = ((MessageObject) arrayList10.get(0)).messageOwner.date * 1000;
                                    }
                                    NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str11).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                    if (dialogKey5.story) {
                                        arrayList10 = notificationsController4.storyPushMessages;
                                    }
                                    category = autoCancel2.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                    Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                    intent42.putExtra("messageDate", i11);
                                    j18 = j15;
                                    intent42.putExtra("dialogId", j18);
                                    String str272 = str17;
                                    intent42.putExtra(str272, notificationsController4.currentAccount);
                                    if (dialogKey5.story) {
                                        intent42.putExtra("story", true);
                                    }
                                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42, 167772160));
                                    if (z6) {
                                        category.setGroup(notificationsController4.notificationGroup);
                                        category.setGroupAlertBehavior(1);
                                    }
                                    if (action2 != null) {
                                        category.addAction(action2);
                                    }
                                    if (!z5 && !dialogKey5.story && (messageObject3 == null || !messageObject3.isStoryReactionPush)) {
                                        category.addAction(action3);
                                    }
                                    if (arrayList4.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey5.story) {
                                        category.setSubText(str);
                                    }
                                    if (DialogObject.isEncryptedDialog(j18)) {
                                        category.setLocalOnly(true);
                                    }
                                    if (bitmap4 != null) {
                                        category.setLargeIcon(bitmap4);
                                    }
                                    if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList9 != null) {
                                        size3 = arrayList9.size();
                                        i18 = 0;
                                        while (i18 < size3) {
                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList25 = arrayList9;
                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList25.get(i18);
                                            int size5 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                            int i37 = 0;
                                            while (i37 < size5) {
                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i37);
                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                    i19 = size3;
                                                    arrayList11 = arrayList25;
                                                    Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                    intent5.putExtra(str272, notificationsController4.currentAccount);
                                                    intent5.putExtra("did", j18);
                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                    if (bArr != null) {
                                                        intent5.putExtra("data", bArr);
                                                    }
                                                    intent5.putExtra("mid", i292);
                                                    String str31 = tLRPC$KeyboardButton.text;
                                                    Context context2 = ApplicationLoader.applicationContext;
                                                    str19 = str272;
                                                    int i38 = notificationsController4.lastButtonId;
                                                    notificationsController4.lastButtonId = i38 + 1;
                                                    category.addAction(0, str31, PendingIntent.getBroadcast(context2, i38, intent5, 167772160));
                                                } else {
                                                    i19 = size3;
                                                    str19 = str272;
                                                    arrayList11 = arrayList25;
                                                }
                                                i37++;
                                                size3 = i19;
                                                arrayList25 = arrayList11;
                                                str272 = str19;
                                            }
                                            i18++;
                                            arrayList9 = arrayList25;
                                        }
                                    }
                                    if (tLRPC$Chat3 == null || tLRPC$User5 == null) {
                                        tLRPC$User6 = tLRPC$User5;
                                    } else {
                                        tLRPC$User6 = tLRPC$User5;
                                        String str32 = tLRPC$User6.phone;
                                        if (str32 != null && str32.length() > 0) {
                                            category.addPerson("tel:+" + tLRPC$User6.phone);
                                        }
                                    }
                                    boolean z132 = z6;
                                    Notification notification32 = notification;
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        notificationsController4.setNotificationChannel(notification32, category, z132);
                                    }
                                    i12 = i26;
                                    j8 = j16;
                                    z10 = z132;
                                    i13 = i8;
                                    longSparseArray6 = longSparseArray8;
                                    longSparseArray7 = longSparseArray5;
                                    str6 = str252;
                                    sharedPreferences = sharedPreferences2;
                                    notification2 = notification32;
                                    arrayList6 = arrayList3;
                                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str11, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                    notificationsController2 = this;
                                    notificationsController2.wearNotificationsIds.put(j18, num2);
                                    i5 = i13 + 1;
                                    arrayList15 = arrayList6;
                                    size = i12;
                                    notificationsSettings = sharedPreferences;
                                    arrayList12 = arrayList4;
                                    longSparseArray12 = longSparseArray4;
                                    clientUserId = j8;
                                    z4 = z10;
                                    str3 = str6;
                                    longSparseArray13 = longSparseArray7;
                                    longSparseArray = longSparseArray6;
                                    build2 = notification2;
                                    i4 = -1;
                                    notificationsController5 = notificationsController2;
                                }
                                person = person4;
                                if (messageObject == null) {
                                }
                                String str2422 = "";
                                if (person == null) {
                                }
                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                messagingStyle2 = messagingStyle;
                                i15 = Build.VERSION.SDK_INT;
                                if (i15 >= 28) {
                                }
                                messagingStyle2.setConversationTitle(format);
                                messagingStyle2.setGroupConversation(i15 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                StringBuilder sb522 = new StringBuilder();
                                String[] strArr222 = new String[1];
                                boolean[] zArr22 = new boolean[1];
                                if (dialogKey3.story) {
                                }
                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb622 = new StringBuilder();
                                sb622.append("com.tmessages.openchat");
                                String str2522 = str10;
                                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList1922 = arrayList7;
                                sb622.append(Math.random());
                                sb622.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent222.setAction(sb622.toString());
                                intent222.setFlags(ConnectionsManager.FileTypeFile);
                                intent222.addCategory("android.intent.category.LAUNCHER");
                                MessageObject messageObject522 = messageObject2;
                                if (messageObject2 == null) {
                                }
                                dialogKey5 = dialogKey4;
                                if (dialogKey5.story) {
                                }
                                StringBuilder sb722 = new StringBuilder();
                                sb722.append("show extra notifications chatId ");
                                sb722.append(j4);
                                sb722.append(" topicId ");
                                j14 = j10;
                                sb722.append(j14);
                                FileLog.d(sb722.toString());
                                if (j14 != 0) {
                                }
                                String str2622 = str9;
                                intent222.putExtra(str2622, notificationsController4.currentAccount);
                                PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                action2 = action;
                                if (action != null) {
                                }
                                int i2922 = i17;
                                Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent322.addFlags(32);
                                intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent322.putExtra("dialog_id", j4);
                                int i3022 = i14;
                                intent322.putExtra(str8, i3022);
                                intent322.putExtra(str2622, notificationsController4.currentAccount);
                                arrayList9 = arrayList1922;
                                bitmap4 = bitmap3;
                                NotificationCompat.Action build422 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (!DialogObject.isEncryptedDialog(j4)) {
                                }
                                if (str18 != null) {
                                }
                                StringBuilder sb822 = new StringBuilder();
                                sb822.append("tgaccount");
                                long j2322 = j11;
                                sb822.append(j2322);
                                wearableExtender22.setBridgeTag(sb822.toString());
                                if (dialogKey5.story) {
                                }
                                NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str11).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                if (dialogKey5.story) {
                                }
                                category = autoCancel22.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent422.putExtra("messageDate", i11);
                                j18 = j15;
                                intent422.putExtra("dialogId", j18);
                                String str2722 = str17;
                                intent422.putExtra(str2722, notificationsController4.currentAccount);
                                if (dialogKey5.story) {
                                }
                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent422, 167772160));
                                if (z6) {
                                }
                                if (action2 != null) {
                                }
                                if (!z5) {
                                    category.addAction(action3);
                                }
                                if (arrayList4.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(j18)) {
                                }
                                if (bitmap4 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                    size3 = arrayList9.size();
                                    i18 = 0;
                                    while (i18 < size3) {
                                    }
                                }
                                if (tLRPC$Chat3 == null) {
                                }
                                tLRPC$User6 = tLRPC$User5;
                                boolean z1322 = z6;
                                Notification notification322 = notification;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                i12 = i26;
                                j8 = j16;
                                z10 = z1322;
                                i13 = i8;
                                longSparseArray6 = longSparseArray8;
                                longSparseArray7 = longSparseArray5;
                                str6 = str2522;
                                sharedPreferences = sharedPreferences2;
                                notification2 = notification322;
                                arrayList6 = arrayList3;
                                arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str11, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j18, num2);
                                i5 = i13 + 1;
                                arrayList15 = arrayList6;
                                size = i12;
                                notificationsSettings = sharedPreferences;
                                arrayList12 = arrayList4;
                                longSparseArray12 = longSparseArray4;
                                clientUserId = j8;
                                z4 = z10;
                                str3 = str6;
                                longSparseArray13 = longSparseArray7;
                                longSparseArray = longSparseArray6;
                                build2 = notification2;
                                i4 = -1;
                                notificationsController5 = notificationsController2;
                            }
                        }
                    }
                    j10 = j7;
                    person = person4;
                    if (messageObject == null) {
                    }
                    String str24222 = "";
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    messagingStyle2 = messagingStyle;
                    i15 = Build.VERSION.SDK_INT;
                    if (i15 >= 28) {
                    }
                    messagingStyle2.setConversationTitle(format);
                    messagingStyle2.setGroupConversation(i15 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                    StringBuilder sb5222 = new StringBuilder();
                    String[] strArr2222 = new String[1];
                    boolean[] zArr222 = new boolean[1];
                    if (dialogKey3.story) {
                    }
                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    StringBuilder sb6222 = new StringBuilder();
                    sb6222.append("com.tmessages.openchat");
                    String str25222 = str10;
                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList19222 = arrayList7;
                    sb6222.append(Math.random());
                    sb6222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent2222.setAction(sb6222.toString());
                    intent2222.setFlags(ConnectionsManager.FileTypeFile);
                    intent2222.addCategory("android.intent.category.LAUNCHER");
                    MessageObject messageObject5222 = messageObject2;
                    if (messageObject2 == null) {
                    }
                    dialogKey5 = dialogKey4;
                    if (dialogKey5.story) {
                    }
                    StringBuilder sb7222 = new StringBuilder();
                    sb7222.append("show extra notifications chatId ");
                    sb7222.append(j4);
                    sb7222.append(" topicId ");
                    j14 = j10;
                    sb7222.append(j14);
                    FileLog.d(sb7222.toString());
                    if (j14 != 0) {
                    }
                    String str26222 = str9;
                    intent2222.putExtra(str26222, notificationsController4.currentAccount);
                    PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                    action2 = action;
                    if (action != null) {
                    }
                    int i29222 = i17;
                    Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent3222.addFlags(32);
                    intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent3222.putExtra("dialog_id", j4);
                    int i30222 = i14;
                    intent3222.putExtra(str8, i30222);
                    intent3222.putExtra(str26222, notificationsController4.currentAccount);
                    arrayList9 = arrayList19222;
                    bitmap4 = bitmap3;
                    NotificationCompat.Action build4222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (!DialogObject.isEncryptedDialog(j4)) {
                    }
                    if (str18 != null) {
                    }
                    StringBuilder sb8222 = new StringBuilder();
                    sb8222.append("tgaccount");
                    long j23222 = j11;
                    sb8222.append(j23222);
                    wearableExtender222.setBridgeTag(sb8222.toString());
                    if (dialogKey5.story) {
                    }
                    NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str11).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                    if (dialogKey5.story) {
                    }
                    category = autoCancel222.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                    Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent4222.putExtra("messageDate", i11);
                    j18 = j15;
                    intent4222.putExtra("dialogId", j18);
                    String str27222 = str17;
                    intent4222.putExtra(str27222, notificationsController4.currentAccount);
                    if (dialogKey5.story) {
                    }
                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4222, 167772160));
                    if (z6) {
                    }
                    if (action2 != null) {
                    }
                    if (!z5) {
                    }
                    if (arrayList4.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j18)) {
                    }
                    if (bitmap4 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat3 == null) {
                    }
                    tLRPC$User6 = tLRPC$User5;
                    boolean z13222 = z6;
                    Notification notification3222 = notification;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    i12 = i26;
                    j8 = j16;
                    z10 = z13222;
                    i13 = i8;
                    longSparseArray6 = longSparseArray8;
                    longSparseArray7 = longSparseArray5;
                    str6 = str25222;
                    sharedPreferences = sharedPreferences2;
                    notification2 = notification3222;
                    arrayList6 = arrayList3;
                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str11, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j18, num2);
                    i5 = i13 + 1;
                    arrayList15 = arrayList6;
                    size = i12;
                    notificationsSettings = sharedPreferences;
                    arrayList12 = arrayList4;
                    longSparseArray12 = longSparseArray4;
                    clientUserId = j8;
                    z4 = z10;
                    str3 = str6;
                    longSparseArray13 = longSparseArray7;
                    longSparseArray = longSparseArray6;
                    build2 = notification2;
                    i4 = -1;
                    notificationsController5 = notificationsController2;
                }
                LongSparseArray longSparseArray15 = longSparseArray;
                longSparseArray2 = longSparseArray13;
                Notification notification4 = build2;
                NotificationsController notificationsController6 = notificationsController5;
                ArrayList arrayList26 = arrayList15;
                if (!z4) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show summary with id " + notificationsController6.notificationId);
                    }
                    try {
                        notificationManager.notify(notificationsController6.notificationId, notification4);
                        notificationsController = notificationsController6;
                        arrayList = arrayList26;
                    } catch (SecurityException e6) {
                        FileLog.e(e6);
                        notificationsController = this;
                        arrayList = arrayList26;
                        notificationsController.resetNotificationSound(builder, j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3);
                    }
                } else {
                    notificationsController = notificationsController6;
                    arrayList = arrayList26;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                }
                i6 = 0;
                while (i6 < longSparseArray2.size()) {
                    LongSparseArray longSparseArray16 = longSparseArray2;
                    if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray16.keyAt(i6)))) {
                        Integer num6 = (Integer) longSparseArray16.valueAt(i6);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel notification id " + num6);
                        }
                        notificationManager.cancel(num6.intValue());
                    }
                    i6++;
                    longSparseArray2 = longSparseArray16;
                }
                ArrayList arrayList27 = new ArrayList(arrayList.size());
                size2 = arrayList.size();
                i7 = 0;
                while (i7 < size2) {
                    ArrayList arrayList28 = arrayList;
                    1NotificationHolder r4 = (1NotificationHolder) arrayList28.get(i7);
                    arrayList27.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r4.dialogId)) {
                        longSparseArray3 = longSparseArray15;
                    } else {
                        NotificationCompat.Builder builder3 = r4.notification;
                        long j26 = r4.dialogId;
                        longSparseArray3 = longSparseArray15;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j26, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j26), !r4.story);
                        if (createNotificationShortcut != null) {
                            arrayList27.add(createNotificationShortcut);
                        }
                    }
                    r4.call();
                    if (!unsupportedNotificationShortcut() && !arrayList27.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList27);
                    }
                    i7++;
                    arrayList = arrayList28;
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
        longSparseArray = new LongSparseArray();
        size = arrayList12.size();
        i5 = 0;
        while (i5 < size) {
            dialogKey = (DialogKey) arrayList12.get(i5);
            int i262 = size;
            if (!dialogKey.story) {
            }
            Integer num52 = (Integer) longSparseArray13.get(j4);
            i9 = id;
            if (dialogKey.story) {
            }
            int i282 = 0;
            while (i10 < arrayList2.size()) {
            }
            if (!dialogKey.story) {
            }
            if (messageObject != null) {
                string = LocaleController.getString(R.string.NotificationHiddenChatName);
                tLRPC$FileLocation = null;
                z7 = false;
            }
            if (z5) {
            }
            if (tLRPC$FileLocation5 == null) {
            }
            if (tLRPC$Chat == null) {
            }
            Bitmap bitmap62 = bitmap2;
            if (z8) {
            }
            file3 = file2;
            Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
            intent6.putExtra("dialog_id", j4);
            intent6.putExtra("max_id", i9);
            intent6.putExtra("topic_id", j7);
            notificationsController3 = this;
            tLRPC$User5 = tLRPC$User2;
            intent6.putExtra("currentAccount", notificationsController3.currentAccount);
            tLRPC$Chat3 = tLRPC$Chat;
            str8 = "max_id";
            PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent6, 167772160);
            RemoteInput build32 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString(R.string.Reply)).build();
            if (!DialogObject.isChatDialog(j4)) {
            }
            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build32).setShowsUserInterface(false).build();
            num3 = notificationsController3.pushDialogs.get(j4);
            if (num3 == null) {
            }
            dialogKey3 = dialogKey2;
            if (!dialogKey3.story) {
            }
            if (max > 1) {
            }
            j9 = j6;
            Person person42 = (Person) longSparseArray.get(j9);
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
            String str242222 = "";
            if (person == null) {
            }
            messagingStyle = new NotificationCompat.MessagingStyle("");
            messagingStyle2 = messagingStyle;
            i15 = Build.VERSION.SDK_INT;
            if (i15 >= 28) {
            }
            messagingStyle2.setConversationTitle(format);
            messagingStyle2.setGroupConversation(i15 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
            StringBuilder sb52222 = new StringBuilder();
            String[] strArr22222 = new String[1];
            boolean[] zArr2222 = new boolean[1];
            if (dialogKey3.story) {
            }
            Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            StringBuilder sb62222 = new StringBuilder();
            sb62222.append("com.tmessages.openchat");
            String str252222 = str10;
            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList192222 = arrayList7;
            sb62222.append(Math.random());
            sb62222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent22222.setAction(sb62222.toString());
            intent22222.setFlags(ConnectionsManager.FileTypeFile);
            intent22222.addCategory("android.intent.category.LAUNCHER");
            MessageObject messageObject52222 = messageObject2;
            if (messageObject2 == null) {
            }
            dialogKey5 = dialogKey4;
            if (dialogKey5.story) {
            }
            StringBuilder sb72222 = new StringBuilder();
            sb72222.append("show extra notifications chatId ");
            sb72222.append(j4);
            sb72222.append(" topicId ");
            j14 = j10;
            sb72222.append(j14);
            FileLog.d(sb72222.toString());
            if (j14 != 0) {
            }
            String str262222 = str9;
            intent22222.putExtra(str262222, notificationsController4.currentAccount);
            PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1140850688);
            NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
            action2 = action;
            if (action != null) {
            }
            int i292222 = i17;
            Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent32222.addFlags(32);
            intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent32222.putExtra("dialog_id", j4);
            int i302222 = i14;
            intent32222.putExtra(str8, i302222);
            intent32222.putExtra(str262222, notificationsController4.currentAccount);
            arrayList9 = arrayList192222;
            bitmap4 = bitmap3;
            NotificationCompat.Action build42222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
            if (!DialogObject.isEncryptedDialog(j4)) {
            }
            if (str18 != null) {
            }
            StringBuilder sb82222 = new StringBuilder();
            sb82222.append("tgaccount");
            long j232222 = j11;
            sb82222.append(j232222);
            wearableExtender2222.setBridgeTag(sb82222.toString());
            if (dialogKey5.story) {
            }
            NotificationCompat.Builder autoCancel2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str11).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
            if (dialogKey5.story) {
            }
            category = autoCancel2222.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
            Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent42222.putExtra("messageDate", i11);
            j18 = j15;
            intent42222.putExtra("dialogId", j18);
            String str272222 = str17;
            intent42222.putExtra(str272222, notificationsController4.currentAccount);
            if (dialogKey5.story) {
            }
            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42222, 167772160));
            if (z6) {
            }
            if (action2 != null) {
            }
            if (!z5) {
            }
            if (arrayList4.size() != 1) {
            }
            if (DialogObject.isEncryptedDialog(j18)) {
            }
            if (bitmap4 != null) {
            }
            if (!AndroidUtilities.needShowPasscode(false)) {
            }
            if (tLRPC$Chat3 == null) {
            }
            tLRPC$User6 = tLRPC$User5;
            boolean z132222 = z6;
            Notification notification32222 = notification;
            if (Build.VERSION.SDK_INT >= 26) {
            }
            i12 = i262;
            j8 = j16;
            z10 = z132222;
            i13 = i8;
            longSparseArray6 = longSparseArray8;
            longSparseArray7 = longSparseArray5;
            str6 = str252222;
            sharedPreferences = sharedPreferences2;
            notification2 = notification32222;
            arrayList6 = arrayList3;
            arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str11, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
            notificationsController2 = this;
            notificationsController2.wearNotificationsIds.put(j18, num2);
            i5 = i13 + 1;
            arrayList15 = arrayList6;
            size = i12;
            notificationsSettings = sharedPreferences;
            arrayList12 = arrayList4;
            longSparseArray12 = longSparseArray4;
            clientUserId = j8;
            z4 = z10;
            str3 = str6;
            longSparseArray13 = longSparseArray7;
            longSparseArray = longSparseArray6;
            build2 = notification2;
            i4 = -1;
            notificationsController5 = notificationsController2;
        }
        LongSparseArray longSparseArray152 = longSparseArray;
        longSparseArray2 = longSparseArray13;
        Notification notification42 = build2;
        NotificationsController notificationsController62 = notificationsController5;
        ArrayList arrayList262 = arrayList15;
        if (!z4) {
        }
        i6 = 0;
        while (i6 < longSparseArray2.size()) {
        }
        ArrayList arrayList272 = new ArrayList(arrayList.size());
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
