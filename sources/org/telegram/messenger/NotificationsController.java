package org.telegram.messenger;

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
import org.telegram.tgnet.TLRPC$ReactionNotificationsFrom;
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
    private static final LongSparseArray sharedPrefCachedKeys;
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private boolean channelGroupsCreated;
    private Runnable checkStoryPushesRunnable;
    private final ArrayList<MessageObject> delayedPushMessages;
    NotificationsSettingsFacade dialogsNotificationsFacade;
    private final LongSparseArray fcmRandomMessagesDict;
    private Boolean groupsCreated;
    private boolean inChatSoundEnabled;
    private int lastBadgeCount;
    private int lastButtonId;
    public long lastNotificationChannelCreateTime;
    private int lastOnlineFromOtherDevice;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private final LongSparseArray lastWearNotifiedMessageId;
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
    private final LongSparseArray pushDialogs;
    private final LongSparseArray pushDialogsOverrideMention;
    private final ArrayList<MessageObject> pushMessages;
    private final LongSparseArray pushMessagesDict;
    public boolean showBadgeMessages;
    public boolean showBadgeMuted;
    public boolean showBadgeNumber;
    private final LongSparseArray smartNotificationsDialogs;
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    char[] spoilerChars;
    private final ArrayList<StoryNotification> storyPushMessages;
    private final LongSparseArray storyPushMessagesDict;
    private int total_unread_count;
    private final LongSparseArray wearNotificationsIds;
    private static final DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    public static long globalSecretChatId = DialogObject.makeEncryptedDialogId(1);

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
        sharedPrefCachedKeys = new LongSparseArray();
    }

    public NotificationsController(int i) {
        super(i);
        this.pushMessages = new ArrayList<>();
        this.delayedPushMessages = new ArrayList<>();
        this.pushMessagesDict = new LongSparseArray();
        this.fcmRandomMessagesDict = new LongSparseArray();
        this.smartNotificationsDialogs = new LongSparseArray();
        this.pushDialogs = new LongSparseArray();
        this.wearNotificationsIds = new LongSparseArray();
        this.lastWearNotifiedMessageId = new LongSparseArray();
        this.pushDialogsOverrideMention = new LongSparseArray();
        this.popupMessages = new ArrayList<>();
        this.popupReplyMessages = new ArrayList<>();
        this.openedInBubbleDialogs = new HashSet<>();
        this.storyPushMessages = new ArrayList<>();
        this.storyPushMessagesDict = new LongSparseArray();
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

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0057, code lost:
        if (r0 == 2) goto L33;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0071  */
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
                i = sharedPreferences.getInt(z ? "popupChannel" : DialogObject.isChatDialog(j) ? "popupGroup" : "popupAll", 0);
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

    private void appendMessage(MessageObject messageObject) {
        for (int i = 0; i < this.pushMessages.size(); i++) {
            if (this.pushMessages.get(i).getId() == messageObject.getId() && this.pushMessages.get(i).getDialogId() == messageObject.getDialogId() && this.pushMessages.get(i).isStoryPush == messageObject.isStoryPush) {
                return;
            }
        }
        this.pushMessages.add(0, messageObject);
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

    /* JADX WARN: Removed duplicated region for block: B:28:0x00f4 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00f8 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0105 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0126 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0154 A[Catch: Exception -> 0x0066, TRY_LEAVE, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
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
                    if (DialogObject.isUserDialog(j)) {
                        intent2.putExtra("chatId", -j);
                    } else {
                        intent2.putExtra("userId", j);
                    }
                    intent2.putExtra("currentAccount", this.currentAccount);
                    if (bitmap == null) {
                        createWithResource = IconCompat.createWithAdaptiveBitmap(bitmap);
                    } else if (tLRPC$User != null) {
                        createWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, tLRPC$User.bot ? R.drawable.book_bot : R.drawable.book_user);
                    } else {
                        createWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.book_group);
                    }
                    if (z) {
                        builder.setBubbleMetadata(null);
                    } else {
                        NotificationCompat.BubbleMetadata.Builder builder2 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 167772160), createWithResource);
                        builder2.setSuppressNotification(this.openedDialogId == j);
                        builder2.setAutoExpandBubble(false);
                        builder2.setDesiredHeight(AndroidUtilities.dp(640.0f));
                        builder.setBubbleMetadata(builder2.build());
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
            if (DialogObject.isUserDialog(j)) {
            }
            intent22.putExtra("currentAccount", this.currentAccount);
            if (bitmap == null) {
            }
            if (z) {
            }
            return str2;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
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

    private void dismissNotification() {
        FileLog.d("NotificationsController dismissNotification");
        try {
            notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            for (int i = 0; i < this.wearNotificationsIds.size(); i++) {
                if (!this.openedInBubbleDialogs.contains(Long.valueOf(this.wearNotificationsIds.keyAt(i)))) {
                    notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(i)).intValue());
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

    public static String getGlobalNotificationsKey(int i) {
        return i == 0 ? "EnableGroup2" : i == 1 ? "EnableAll2" : "EnableChannel2";
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

    private int getNotifyOverride(SharedPreferences sharedPreferences, long j, long j2) {
        int property = this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY, j, j2, -1);
        if (property != 3 || this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL, j, j2, 0) < getConnectionsManager().getCurrentTime()) {
            return property;
        }
        return 2;
    }

    public static String getSharedPrefKey(long j, long j2) {
        return getSharedPrefKey(j, j2, false);
    }

    public static String getSharedPrefKey(long j, long j2, boolean z) {
        if (z) {
            return j2 != 0 ? String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2)) : String.valueOf(j);
        }
        long j3 = (j2 << 12) + j;
        LongSparseArray longSparseArray = sharedPrefCachedKeys;
        int indexOfKey = longSparseArray.indexOfKey(j3);
        if (indexOfKey >= 0) {
            return (String) longSparseArray.valueAt(indexOfKey);
        }
        String format = j2 != 0 ? String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2)) : String.valueOf(j);
        longSparseArray.put(j3, format);
        return format;
    }

    /* JADX WARN: Code restructure failed: missing block: B:153:0x0251, code lost:
        if (r10.getBoolean("EnablePreviewAll", true) == false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0262, code lost:
        if (r10.getBoolean("EnablePreviewGroup", r7) != false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x026a, code lost:
        if (r10.getBoolean("EnablePreviewChannel", r7) != false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x026c, code lost:
        r1 = r28.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x027c, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L686;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x027e, code lost:
        r29[0] = null;
        r4 = r1.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0286, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetSameChatWallPaper) == false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x028e, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.WallpaperSameNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x0291, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper) == false) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0299, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.WallpaperNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x029c, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached) == false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x02a4, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x02a7, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L683;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x02ab, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp) == false) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x02b2, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto) == false) goto L166;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x02c2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.R.string.NotificationContactNewPhoto, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x02c5, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation) == false) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02c7, code lost:
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().getFormatterYear().format(r28.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().getFormatterDay().format(r28.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r3 = getUserConfig().getCurrentUser().first_name;
        r0 = r28.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0329, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, r3, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x032c, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L681;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0330, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0336, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x033a, code lost:
        if (r4.video == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0342, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x0349, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x034e, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0350, code lost:
        r7 = r4.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0356, code lost:
        if (r7 != 0) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x035f, code lost:
        if (r4.users.size() != 1) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0361, code lost:
        r7 = ((java.lang.Long) r28.messageOwner.action.users.get(0)).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x0378, code lost:
        if (r7 == 0) goto L214;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x0382, code lost:
        if (r28.messageOwner.peer_id.channel_id == 0) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x0386, code lost:
        if (r3.megagroup != false) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x039b, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x039e, code lost:
        if (r7 != r22) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x03b3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03b4, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03c0, code lost:
        if (r0 != null) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03c2, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x03c8, code lost:
        if (r5 != r0.id) goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x03cc, code lost:
        if (r3.megagroup == false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x03e1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x03f5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x040e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x040f, code lost:
        r4 = new java.lang.StringBuilder();
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x041f, code lost:
        if (r5 >= r28.messageOwner.action.users.size()) goto L226;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x0421, code lost:
        r6 = getMessagesController().getUser((java.lang.Long) r28.messageOwner.action.users.get(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0435, code lost:
        if (r6 == null) goto L225;
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0437, code lost:
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x043f, code lost:
        if (r4.length() == 0) goto L222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0441, code lost:
        r4.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0444, code lost:
        r4.append(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0447, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x0462, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r3.title, r4.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x0466, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x047a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x047d, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0485, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x0488, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L270;
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x048a, code lost:
        r5 = r4.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0490, code lost:
        if (r5 != 0) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0499, code lost:
        if (r4.users.size() != 1) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x049b, code lost:
        r5 = ((java.lang.Long) r28.messageOwner.action.users.get(0)).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x04b2, code lost:
        if (r5 == 0) goto L255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x04b6, code lost:
        if (r5 != r22) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x04cb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x04cc, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x04d8, code lost:
        if (r0 != null) goto L253;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x04da, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x04f4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04f5, code lost:
        r4 = new java.lang.StringBuilder();
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0505, code lost:
        if (r5 >= r28.messageOwner.action.users.size()) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0507, code lost:
        r6 = getMessagesController().getUser((java.lang.Long) r28.messageOwner.action.users.get(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x051b, code lost:
        if (r6 == null) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x051d, code lost:
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0525, code lost:
        if (r4.length() == 0) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0527, code lost:
        r4.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x052a, code lost:
        r4.append(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x052d, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0548, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r3.title, r4.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x054b, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x0553, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0556, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x056b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0570, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0583, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r2, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0586, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x058a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0590, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0592, code lost:
        r7 = r4.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0596, code lost:
        if (r7 != r22) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x05ab, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x05b1, code lost:
        if (r7 != r5) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x05c3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x05c4, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r28.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x05d6, code lost:
        if (r0 != null) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x05d8, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x05f4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r2, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x05f7, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L305;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x05ff, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0602, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L309;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x060a, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x060f, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x061f, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0624, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L317;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x0632, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0635, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x063d, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0640, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiveawayLaunch) == false) goto L325;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x0648, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x064b, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiveawayResults) == false) goto L329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0653, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0656, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x065c, code lost:
        if (r3 == null) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0662, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r3) == false) goto L337;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0666, code lost:
        if (r3.megagroup == false) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x066c, code lost:
        r0 = r28.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x066e, code lost:
        if (r0 != null) goto L341;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0683, code lost:
        return org.telegram.messenger.LocaleController.formatString(r26, org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x0684, code lost:
        r12 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x068d, code lost:
        if (r0.isMusic() == false) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x069f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x06a6, code lost:
        if (r0.isVideo() == false) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x06b0, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x06d9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📹 " + r0.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x06ed, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x06f2, code lost:
        if (r0.isGif() == false) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x06fc, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L359;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x0725, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🎬 " + r0.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0739, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0741, code lost:
        if (r0.isVoice() == false) goto L365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0753, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x0758, code lost:
        if (r0.isRoundVideo() == false) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x076a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x076f, code lost:
        if (r0.isSticker() != false) goto L428;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x0775, code lost:
        if (r0.isAnimatedSticker() == false) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x077b, code lost:
        r5 = r0.messageOwner;
        r9 = r5.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x0781, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L381;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0789, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x07b2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📎 " + r0.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x07c6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x07c9, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x07cd, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x07d6, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L389;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x07eb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x07ee, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L393;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x07f0, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0810, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r2, r3.title, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x0813, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0815, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x081b, code lost:
        if (r0.quiz == false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0837, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0852, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0855, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x085d, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L407;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x0886, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🖼 " + r0.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x089a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x08a0, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x08b2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08b3, code lost:
        r5 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x08b5, code lost:
        if (r5 == null) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x08bb, code lost:
        if (r5.length() <= 0) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x08bd, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x08c3, code lost:
        if (r0.length() <= 20) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x08c5, code lost:
        r5 = new java.lang.StringBuilder();
        r7 = 0;
        r5.append((java.lang.Object) r0.subSequence(0, 20));
        r5.append("...");
        r0 = r5.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x08da, code lost:
        r7 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x08db, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r3 = r3.title;
        r4 = new java.lang.Object[3];
        r4[r7] = r2;
        r4[1] = r0;
        r4[2] = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x08ee, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0900, code lost:
        return org.telegram.messenger.LocaleController.formatString(r12, org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0911, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0912, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0916, code lost:
        if (r0 == null) goto L433;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x092c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r2, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x093e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x093f, code lost:
        if (r3 == null) goto L535;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0941, code lost:
        r0 = r28.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0943, code lost:
        if (r0 != null) goto L441;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0954, code lost:
        return org.telegram.messenger.LocaleController.formatString(r25, org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0955, code lost:
        r11 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x095c, code lost:
        if (r0.isMusic() == false) goto L445;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x096c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0973, code lost:
        if (r0.isVideo() == false) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x097d, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L451;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b8, code lost:
        if (r10.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x09a3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x09b4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x09b9, code lost:
        if (r0.isGif() == false) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x09c3, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L459;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x09e9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x09fa, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0a01, code lost:
        if (r0.isVoice() == false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a16, code lost:
        if (r0.isRoundVideo() == false) goto L469;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a26, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0a2b, code lost:
        if (r0.isSticker() != false) goto L528;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0a31, code lost:
        if (r0.isAnimatedSticker() == false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0a36, code lost:
        r2 = r0.messageOwner;
        r6 = r2.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0a3c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L481;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0a44, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0a6a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0a7b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0a7e, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0a82, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0a8a, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L489;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0a9c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0a9f, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0aa1, code lost:
        r6 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0abe, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r3.title, org.telegram.messenger.ContactsController.formatName(r6.first_name, r6.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0ac1, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L501;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0ac3, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r6).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0ac9, code lost:
        if (r0.quiz == false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0ae2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0afa, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0afd, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L509;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0b05, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b2b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00c4, code lost:
        if (r10.getBoolean("EnablePreviewChannel", r1) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0b3c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b41, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L513;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b51, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0b52, code lost:
        r2 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0b54, code lost:
        if (r2 == null) goto L523;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0b5a, code lost:
        if (r2.length() <= 0) goto L523;
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0b5c, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0b62, code lost:
        if (r0.length() <= 20) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0b64, code lost:
        r2 = new java.lang.StringBuilder();
        r6 = 0;
        r2.append((java.lang.Object) r0.subSequence(0, 20));
        r2.append("...");
        r0 = r2.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0b79, code lost:
        r6 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0b7a, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = r3.title;
        r3 = new java.lang.Object[2];
        r3[r6] = r2;
        r3[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0b8a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0b99, code lost:
        return org.telegram.messenger.LocaleController.formatString(r11, org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0ba8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0ba9, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0bad, code lost:
        if (r0 == null) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0bc1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0bd1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0bd2, code lost:
        r0 = r28.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0bd5, code lost:
        if (r0 != null) goto L539;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0be3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0bea, code lost:
        if (r0.isMusic() == false) goto L543;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0bf8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0bff, code lost:
        if (r0.isVideo() == false) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0c09, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0c2d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r2, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0c3c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0c41, code lost:
        if (r0.isGif() == false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0c4b, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L557;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0c6f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r2, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0c7e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0c85, code lost:
        if (r0.isVoice() == false) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0c93, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0c98, code lost:
        if (r0.isRoundVideo() == false) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0ca6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0cab, code lost:
        if (r0.isSticker() != false) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0cb1, code lost:
        if (r0.isAnimatedSticker() == false) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0cb7, code lost:
        r5 = r0.messageOwner;
        r9 = r5.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0cbd, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0cc5, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0ce9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r2, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x0cf8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x0cfb, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x0cff, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x0d07, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x0d17, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x0d1a, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0d1c, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x0d37, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r2, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x0d3a, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L599;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0d3c, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0d42, code lost:
        if (r0.quiz == false) goto L597;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0d59, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r2, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0d6f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r2, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0d72, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L607;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0d7a, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L605;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0d9e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r2, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x0dad, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0db2, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0dc0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x0dc1, code lost:
        r5 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0dc3, code lost:
        if (r5 == null) goto L621;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0dc9, code lost:
        if (r5.length() <= 0) goto L621;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x0dcb, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0dd1, code lost:
        if (r0.length() <= 20) goto L620;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x0dd3, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0de8, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x0de9, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r3 = new java.lang.Object[2];
        r3[r5] = r2;
        r3[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0df7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x0e04, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x0e11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x0e12, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x0e16, code lost:
        if (r0 == null) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x0e27, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x0e34, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0e37, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L649;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x0e39, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r4).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0e41, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0e45, code lost:
        if (r11 != r22) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0e69, code lost:
        if (r11 != r22) goto L647;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x0e87, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x0e8a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L653;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x0e92, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x0e95, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPrizeStars) == false) goto L664;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x0e97, code lost:
        r4 = (org.telegram.tgnet.TLRPC$TL_messageActionPrizeStars) r4;
        r0 = org.telegram.messenger.DialogObject.getPeerDialogId(r4.boost_peer);
        r5 = (r0 > 0 ? 1 : (r0 == 0 ? 0 : -1));
        r2 = getMessagesController();
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x0ea7, code lost:
        if (r5 < 0) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0ea9, code lost:
        r1 = org.telegram.messenger.UserObject.getForcedFirstName(r2.getUser(java.lang.Long.valueOf(r0)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x0eb6, code lost:
        r0 = r2.getChat(java.lang.Long.valueOf(-r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x0ebf, code lost:
        if (r0 != null) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x0ec1, code lost:
        r1 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x0ec4, code lost:
        r1 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x0ed5, code lost:
        return org.telegram.messenger.LocaleController.formatPluralStringComma("BoostingReceivedStars", (int) r4.stars, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x0ed6, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x0ee0, code lost:
        if (r1.peer_id.channel_id == 0) goto L675;
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x0ee4, code lost:
        if (r3.megagroup != false) goto L675;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x0eea, code lost:
        if (r28.isVideoAvatar() == false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x0efc, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x0f0d, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x0f13, code lost:
        if (r28.isVideoAvatar() == false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x0f27, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x0f3a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r2, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x0f41, code lost:
        return r28.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x0f4f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x0f54, code lost:
        if (r28.isMediaEmpty() == false) goto L694;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x0f5e, code lost:
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L692;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x0f64, code lost:
        return replaceSpoilers(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x0f6b, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x0f70, code lost:
        if (r28.type != 29) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x0f78, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r28) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0f7a, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r28);
        r1 = r0.extended_media.size();
        r2 = 0;
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0f88, code lost:
        if (r2 >= r1) goto L732;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f8a, code lost:
        r4 = r0.extended_media.get(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x0f94, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f96, code lost:
        r3 = ((org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) r4).media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x0f9c, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x0fa4, code lost:
        if (org.telegram.messenger.MessageObject.isVideoDocument(r3.document) == false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x0fac, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) == false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x0fb4, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r4).flags & 4) == 0) goto L726;
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x0fb6, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x0fb8, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:733:0x0fbb, code lost:
        if (r3 == false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x0fbf, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x0fc2, code lost:
        r0 = org.telegram.messenger.R.string.AttachPaidMedia;
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x0fc4, code lost:
        if (r1 != 1) goto L720;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0fc6, code lost:
        if (r3 == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x0fc8, code lost:
        r1 = org.telegram.messenger.R.string.AttachVideo;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x0fcb, code lost:
        r1 = org.telegram.messenger.R.string.AttachPhoto;
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x0fcd, code lost:
        r1 = org.telegram.messenger.LocaleController.getString(r1);
        r2 = 1;
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x0fd4, code lost:
        if (r3 == false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x0fd6, code lost:
        r2 = "Media";
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x0fd8, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x0fda, code lost:
        r2 = "Photos";
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x0fdd, code lost:
        r1 = org.telegram.messenger.LocaleController.formatPluralString(r2, r1, new java.lang.Object[0]);
        r2 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x0fe4, code lost:
        r2 = new java.lang.Object[r2];
        r2[r3] = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0fec, code lost:
        return org.telegram.messenger.LocaleController.formatString(r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0ff1, code lost:
        if (r28.isVoiceOnce() == false) goto L737;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0ff9, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x0ffe, code lost:
        if (r28.isRoundOnce() == false) goto L741;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x1006, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x1007, code lost:
        r1 = r28.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x100d, code lost:
        if ((r1.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1015, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x102a, code lost:
        return "🖼 " + replaceSpoilers(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1031, code lost:
        if (r28.messageOwner.media.ttl_seconds == 0) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1039, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x1040, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1045, code lost:
        if (r28.isVideo() == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x104f, code lost:
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x1064, code lost:
        return "📹 " + replaceSpoilers(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x106b, code lost:
        if (r28.messageOwner.media.ttl_seconds == 0) goto L763;
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x1073, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x107a, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x107f, code lost:
        if (r28.isGame() == false) goto L769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x1087, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x108c, code lost:
        if (r28.isVoice() == false) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x1094, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x1099, code lost:
        if (r28.isRoundVideo() == false) goto L777;
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:0x10a1, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:0x10a6, code lost:
        if (r28.isMusic() == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:0x10ae, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:797:0x10af, code lost:
        r1 = r28.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x10b5, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x10bd, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x10c0, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L793;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x10c8, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll.quiz == false) goto L791;
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:0x10d0, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x10d7, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x10da, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L797;
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x10e2, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingGiveaway);
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:0x10e5, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x10ed, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingGiveawayResults);
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:0x10f0, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L853;
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x10f4, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L805;
     */
    /* JADX WARN: Code restructure failed: missing block: B:823:0x10fa, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L809;
     */
    /* JADX WARN: Code restructure failed: missing block: B:825:0x1102, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:827:0x1105, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L835;
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:0x110b, code lost:
        if (r28.isSticker() != false) goto L829;
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:0x1111, code lost:
        if (r28.isAnimatedSticker() == false) goto L815;
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:0x1118, code lost:
        if (r28.isGif() == false) goto L823;
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:0x1122, code lost:
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:0x1137, code lost:
        return "🎬 " + replaceSpoilers(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:0x113e, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:0x1147, code lost:
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L827;
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:0x115c, code lost:
        return "📎 " + replaceSpoilers(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:0x1163, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:0x1164, code lost:
        r0 = r28.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:0x1168, code lost:
        if (r0 == null) goto L833;
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:0x1184, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:0x118b, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:0x118e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaStory) == false) goto L847;
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:0x1194, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaStory) r1).via_mention == false) goto L845;
     */
    /* JADX WARN: Code restructure failed: missing block: B:857:0x1196, code lost:
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r2 = r29[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:0x119b, code lost:
        if (r2 != null) goto L844;
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:0x119f, code lost:
        r16 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:862:0x11ac, code lost:
        return org.telegram.messenger.LocaleController.formatString("StoryNotificationMention", r0, r16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:864:0x11b3, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:0x11ba, code lost:
        if (android.text.TextUtils.isEmpty(r28.messageText) != false) goto L851;
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:0x11c0, code lost:
        return replaceSpoilers(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:0x11c7, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:872:0x11ce, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:887:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:888:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:889:?, code lost:
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
        String str4 = "";
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

    /* JADX WARN: Code restructure failed: missing block: B:274:0x0677, code lost:
        if (r14.getBoolean(r36, true) == false) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0683, code lost:
        if (r14.getBoolean(r35, r12) != false) goto L258;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0685, code lost:
        r4 = r46.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x0689, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x068b, code lost:
        r12 = r4.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x068f, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0691, code lost:
        r1 = r12.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x0695, code lost:
        if (r1 != 0) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x069e, code lost:
        if (r12.users.size() != 1) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x06a0, code lost:
        r1 = ((java.lang.Long) r46.messageOwner.action.users.get(0)).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x06b3, code lost:
        if (r1 == 0) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x06bd, code lost:
        if (r46.messageOwner.peer_id.channel_id == 0) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06c1, code lost:
        if (r3.megagroup != false) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x06c3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x06da, code lost:
        if (r1 != r31) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x06dc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x06f1, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06fd, code lost:
        if (r0 != null) goto L280;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x06ff, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0705, code lost:
        if (r5 != r0.id) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0709, code lost:
        if (r3.megagroup == false) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x070b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0720, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0735, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0751, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0763, code lost:
        if (r4 >= r46.messageOwner.action.users.size()) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0765, code lost:
        r5 = getMessagesController().getUser((java.lang.Long) r46.messageOwner.action.users.get(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0779, code lost:
        if (r5 == null) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x077b, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
        r13 = r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0785, code lost:
        if (r1.length() == 0) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0787, code lost:
        r1.append(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x078a, code lost:
        r1.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x078f, code lost:
        r13 = r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0792, code lost:
        r4 = r4 + 1;
        r24 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x0796, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r3.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x07b7, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x07b9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x07cf, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x07d5, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x07d7, code lost:
        r1 = r12.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x07db, code lost:
        if (r1 != 0) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x07e4, code lost:
        if (r12.users.size() != 1) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x07e6, code lost:
        r1 = ((java.lang.Long) r46.messageOwner.action.users.get(0)).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x07f9, code lost:
        if (r1 == 0) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x07fd, code lost:
        if (r1 != r31) goto L318;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x07ff, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0814, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0820, code lost:
        if (r0 != null) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0822, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0824, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0840, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0852, code lost:
        if (r4 >= r46.messageOwner.action.users.size()) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0854, code lost:
        r5 = getMessagesController().getUser((java.lang.Long) r46.messageOwner.action.users.get(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0868, code lost:
        if (r5 == null) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x086a, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0872, code lost:
        if (r1.length() == 0) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0874, code lost:
        r1.append(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0877, code lost:
        r1.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x087a, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x087d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r3.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0899, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x089b, code lost:
        r12 = (org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) r12;
        r0 = org.telegram.messenger.MessagesController.getInstance(r45.currentAccount).getChat(java.lang.Long.valueOf(-org.telegram.messenger.DialogObject.getPeerDialogId(r12.boost_peer)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x08b4, code lost:
        if (r0 != null) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x08b6, code lost:
        r15 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x08b8, code lost:
        r15 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x08ba, code lost:
        if (r15 != null) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x08bc, code lost:
        r0 = org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x08c4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGiftCode", org.telegram.messenger.R.string.NotificationMessageGiftCode, r15, org.telegram.messenger.LocaleController.formatPluralString("Months", r12.months, new java.lang.Object[0]));
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x08e8, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x08ea, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0903, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x0905, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r7, r12.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x091a, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L554;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x091e, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L355;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0924, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0926, code lost:
        r1 = r12.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x092a, code lost:
        if (r1 != r31) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x092c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0946, code lost:
        if (r1 != r5) goto L363;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0948, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x095a, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r46.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x096c, code lost:
        if (r0 != null) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x096e, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0970, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r7, r3.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x098f, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x0995, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x099b, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x099d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x09b5, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x09b7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r12.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x09c7, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x09cd, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L538;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x09d5, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r3) == false) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x09d9, code lost:
        if (r3.megagroup == false) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x09e0, code lost:
        r1 = r46.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x09e2, code lost:
        if (r1 != null) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x09e4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x09fe, code lost:
        if (r1.isMusic() == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0a00, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0a16, code lost:
        if (r1.isVideo() == false) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a20, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L396;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0a22, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "📹 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a48, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0a5e, code lost:
        if (r1.isGif() == false) goto L403;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0a68, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0a6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "🎬 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0a90, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0aa8, code lost:
        if (r1.isVoice() == false) goto L406;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0aaa, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0abe, code lost:
        if (r1.isRoundVideo() == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0ac0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0ad4, code lost:
        if (r1.isSticker() != false) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0ada, code lost:
        if (r1.isAnimatedSticker() == false) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0adf, code lost:
        r4 = r1.messageOwner;
        r7 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0ae5, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L419;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0aed, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0aef, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "📎 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0b15, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0b29, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0b2d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0b35, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0b37, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b4b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L429;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0b4d, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r46.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r3.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0b72, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0b74, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0b7a, code lost:
        if (r0.quiz == false) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0b7c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b95, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0bb0, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L441;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0bb8, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L440;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0bba, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r3.title, "🖼 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0be0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0bf6, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0bf8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0c08, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0c0a, code lost:
        if (r0 == null) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0c10, code lost:
        if (r0.length() <= 0) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0c12, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0c18, code lost:
        if (r0.length() <= 20) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0c1a, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0c31, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0c32, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = r3.title;
        r3 = new java.lang.Object[2];
        r3[r4] = r2;
        r3[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0c44, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c54, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0c64, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0c68, code lost:
        if (r0 == null) goto L460;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0c6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0c7e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c8f, code lost:
        r1 = r46.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0c91, code lost:
        if (r1 != null) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0c93, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0caf, code lost:
        if (r1.isMusic() == false) goto L468;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0cb1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0cc9, code lost:
        if (r1.isVideo() == false) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0cd3, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0cd5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📹 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0cfe, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0d17, code lost:
        if (r1.isGif() == false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0d21, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0d23, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🎬 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0d4c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0d68, code lost:
        if (r1.isVoice() == false) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0d6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0d80, code lost:
        if (r1.isRoundVideo() == false) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0d82, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0d98, code lost:
        if (r1.isSticker() != false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0d9e, code lost:
        if (r1.isAnimatedSticker() == false) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0da4, code lost:
        r4 = r1.messageOwner;
        r8 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0daa, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0db2, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L495;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0db4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📎 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0ddd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0df4, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L531;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0df8, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0e01, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L503;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0e03, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0e1a, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0e1c, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r46.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r7, r3.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0e44, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L512;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0e46, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r8).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0e4c, code lost:
        if (r0.quiz == false) goto L511;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e4e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0e6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0e88, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0e90, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L517;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0e92, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🖼 " + r1.messageOwner.message, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0ebb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0ed5, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L521;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0ed7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0ee9, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0eeb, code lost:
        if (r0 == null) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0ef1, code lost:
        if (r0.length() <= 0) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0ef3, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0ef9, code lost:
        if (r0.length() <= 20) goto L529;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0efb, code lost:
        r1 = new java.lang.StringBuilder();
        r4 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0f10, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0f11, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = r3.title;
        r3 = new java.lang.Object[3];
        r3[r4] = r7;
        r3[1] = r0;
        r3[2] = r2;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0f26, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0f39, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0f4b, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0f4f, code lost:
        if (r0 == null) goto L537;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0f51, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r7, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0f67, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0f7c, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0f81, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0f83, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r12).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0f8b, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0f8f, code lost:
        if (r1 != r31) goto L547;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0f91, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0f9c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r29, org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0fb3, code lost:
        if (r1 != r31) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0fb5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0fc3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0fd6, code lost:
        if ((r12 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0fe6, code lost:
        if (r4.peer_id.channel_id == 0) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0fea, code lost:
        if (r3.megagroup != false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0ff0, code lost:
        if (r46.isVideoAvatar() == false) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0ff2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x1004, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x101b, code lost:
        if (r46.isVideoAvatar() == false) goto L565;
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x101d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x1031, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x104b, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r3) == false) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x104f, code lost:
        if (r3.megagroup != false) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x1055, code lost:
        if (r46.isMediaEmpty() == false) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x1057, code lost:
        if (r47 != false) goto L577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x1061, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x1063, code lost:
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r7, r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x107c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x1094, code lost:
        if (r46.type != 29) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x109c, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r46) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x109e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r46)).stars_amount, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x10b7, code lost:
        r1 = r46.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x10bd, code lost:
        if ((r1.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x10bf, code lost:
        if (r47 != false) goto L590;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x10c7, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L590;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x10c9, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "🖼 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x10ef, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x1103, code lost:
        if (r46.isVideo() == false) goto L599;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x1105, code lost:
        if (r47 != false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x110f, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x1111, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "📹 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x1137, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x114d, code lost:
        if (r46.isVoice() == false) goto L602;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x114f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x1161, code lost:
        if (r46.isRoundVideo() == false) goto L605;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x1163, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x1175, code lost:
        if (r46.isMusic() == false) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x1177, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x1185, code lost:
        r1 = r46.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x118b, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x118d, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r7, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x11ac, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x11ae, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x11b4, code lost:
        if (r0.quiz == false) goto L616;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x11b6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r7, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x11cd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r7, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x11e6, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L620;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x11e8, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r1;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r3.title, java.lang.Integer.valueOf(r1.quantity), java.lang.Integer.valueOf(r1.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x1210, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L659;
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x1214, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x121c, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L627;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x121e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x1230, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x1236, code lost:
        if (r46.isSticker() != false) goto L647;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x123c, code lost:
        if (r46.isAnimatedSticker() == false) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x1246, code lost:
        if (r46.isGif() == false) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x1248, code lost:
        if (r47 != false) goto L640;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x1252, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L640;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x1254, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "🎬 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x127a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x128a, code lost:
        if (r47 != false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x1294, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x1296, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r7, "📎 " + r46.messageOwner.message);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x12bc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x12cc, code lost:
        r0 = r46.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x12d0, code lost:
        if (r0 == null) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x12d2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x12e3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x12f2, code lost:
        if (r47 != false) goto L658;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x12fa, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageText) != false) goto L658;
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x12fc, code lost:
        r15 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r7, r46.messageText);
        r48[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x1310, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x131d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x1333, code lost:
        if (r46.isMediaEmpty() == false) goto L668;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x1335, code lost:
        if (r47 != false) goto L667;
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x133f, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L667;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x1341, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r7, r3.title, r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x135b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r34, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x1370, code lost:
        r5 = r34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x1376, code lost:
        if (r46.type != 29) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x137e, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r46) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x1380, code lost:
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChatMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r46)).stars_amount, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x139c, code lost:
        r2 = r46.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x13a2, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L680;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x13a4, code lost:
        if (r47 != false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x13ac, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x13ae, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "🖼 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x13d7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x13f0, code lost:
        if (r46.isVideo() == false) goto L687;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x13f2, code lost:
        if (r47 != false) goto L686;
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x13fc, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L686;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x13fe, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "📹 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x1427, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x1443, code lost:
        if (r46.isVoice() == false) goto L690;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x1445, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x145b, code lost:
        if (r46.isRoundVideo() == false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x145d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x1473, code lost:
        if (r46.isMusic() == false) goto L696;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x1475, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x1487, code lost:
        r2 = r46.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x148d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x148f, code lost:
        r2 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r2;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r7, r3.title, org.telegram.messenger.ContactsController.formatName(r2.first_name, r2.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x14b3, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x14b5, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x14bb, code lost:
        if (r0.quiz == false) goto L704;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x14bd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x14d9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r7, r3.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x14f7, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L708;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x14f9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r7, r3.title, r2.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x1517, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x1519, code lost:
        r2 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r2;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r3.title, java.lang.Integer.valueOf(r2.quantity), java.lang.Integer.valueOf(r2.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x1540, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x1542, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x1550, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x1554, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L718;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x155d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L721;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x155f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x1576, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x157c, code lost:
        if (r46.isSticker() != false) goto L739;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x1582, code lost:
        if (r46.isAnimatedSticker() == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x158c, code lost:
        if (r46.isGif() == false) goto L734;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x158e, code lost:
        if (r47 != false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x1598, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x159a, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "🎬 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x15c3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x15d8, code lost:
        if (r47 != false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x15e2, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageOwner.message) != false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x15e4, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r2 = r3.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r7, r2, "📎 " + r46.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x160d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x1622, code lost:
        r0 = r46.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1626, code lost:
        if (r0 == null) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1628, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r7, r3.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x163e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x1651, code lost:
        if (r47 != false) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1659, code lost:
        if (android.text.TextUtils.isEmpty(r46.messageText) != false) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x165b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r7, r3.title, r46.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x1673, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r5, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r7, r3.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x1686, code lost:
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
    /* JADX WARN: Removed duplicated region for block: B:272:0x066e  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x16a1  */
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
        int i;
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
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0 && (!z4 || ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))))) {
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
        if (j3 <= 0) {
            str = "NotificationMessageGroupNoText";
            str2 = "EnablePreviewChannel";
            str3 = "EnablePreviewGroup";
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat != null) {
                str4 = chat.title;
            }
            str4 = null;
        } else if (messageObject.messageOwner.from_scheduled) {
            str2 = "EnablePreviewChannel";
            str = "NotificationMessageGroupNoText";
            str4 = LocaleController.getString(j == clientUserId ? R.string.MessageScheduledReminderNotification : R.string.NotificationMessageScheduledName);
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
        if (!DialogObject.isEncryptedDialog(j)) {
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
                }
                return null;
            } else if (z4 && notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                        i = R.string.WallpaperSameNotification;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                        i = R.string.WallpaperNotification;
                    } else {
                        if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached)) {
                            if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                                formatString = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, str4);
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                                formatString = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, str4);
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                                String formatString4 = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().getFormatterDay().format(messageObject.messageOwner.date * 1000));
                                int i2 = R.string.NotificationUnrecognizedDevice;
                                String str9 = getUserConfig().getCurrentUser().first_name;
                                TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                                formatString = LocaleController.formatString("NotificationUnrecognizedDevice", i2, str9, formatString4, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                            } else if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent)) {
                                if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall)) {
                                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                                        String str10 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                                        if (!TextUtils.isEmpty(str10)) {
                                            c = 0;
                                            z3 = true;
                                            formatString3 = j == clientUserId ? LocaleController.formatString("ChangedChatThemeYou", R.string.ChatThemeChangedYou, str10) : LocaleController.formatString("ChangedChatThemeTo", R.string.ChatThemeChangedTo, str4, str10);
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
                                i = tLRPC$MessageAction.video ? R.string.CallMessageVideoIncomingMissed : R.string.CallMessageIncomingMissed;
                            }
                        }
                        formatString = messageObject.messageText.toString();
                    }
                } else if (!messageObject.isMediaEmpty()) {
                    TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                    if (tLRPC$Message3.media instanceof TLRPC$TL_messageMediaPhoto) {
                        if (!z && !TextUtils.isEmpty(tLRPC$Message3.message)) {
                            int i3 = R.string.NotificationMessageText;
                            formatString2 = LocaleController.formatString("NotificationMessageText", i3, str4, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString2;
                        }
                        formatString = messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, str4) : LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, str4);
                    } else if (messageObject.isVideo()) {
                        if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            int i4 = R.string.NotificationMessageText;
                            formatString2 = LocaleController.formatString("NotificationMessageText", i4, str4, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString2;
                        }
                        formatString = messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, str4) : LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, str4);
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
                            formatString = tLRPC$Poll.quiz ? LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, str4, tLRPC$Poll.question.text) : LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, str4, tLRPC$Poll.question.text);
                        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                            formatString = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, str4);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                            formatString = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, str4);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String stickerEmoji = messageObject.getStickerEmoji();
                                formatString = stickerEmoji != null ? LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, str4, stickerEmoji) : LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, str4);
                            } else if (messageObject.isGif()) {
                                if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    int i5 = R.string.NotificationMessageText;
                                    formatString2 = LocaleController.formatString("NotificationMessageText", i5, str4, "🎬 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                    return formatString2;
                                }
                                formatString = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, str4);
                            } else if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                int i6 = R.string.NotificationMessageText;
                                formatString2 = LocaleController.formatString("NotificationMessageText", i6, str4, "📎 " + messageObject.messageOwner.message);
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
                } else if (z) {
                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
                } else if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    formatString2 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str4, messageObject.messageOwner.message);
                    zArr[0] = true;
                    return formatString2;
                } else {
                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
                }
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str4);
            }
            return formatString;
        }
        i = R.string.YouHaveNewMessage;
        formatString = LocaleController.getString(i);
        return formatString;
    }

    private int getTotalAllUnreadCount() {
        int size;
        FileLog.d("getTotalAllUnreadCount: init 0");
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            if (UserConfig.getInstance(i2).isClientActivated() && (SharedConfig.showNotificationsForAllAccounts || UserConfig.selectedAccount == i2)) {
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
                                        FileLog.d("getTotalAllUnreadCount: account=" + i2 + " count += getDialogUnreadCount (" + MessagesController.getInstance(i2).getDialogUnreadCount(tLRPC$Dialog) + ")");
                                        i += MessagesController.getInstance(i2).getDialogUnreadCount(tLRPC$Dialog);
                                    }
                                }
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else {
                            FileLog.d("getTotalAllUnreadCount: account=" + i2 + " count += total_unread_count (" + notificationsController.total_unread_count + ")");
                            size = notificationsController.total_unread_count;
                        }
                    } else if (notificationsController.showBadgeMuted) {
                        try {
                            int size3 = MessagesController.getInstance(i2).allDialogs.size();
                            for (int i4 = 0; i4 < size3; i4++) {
                                TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(i2).allDialogs.get(i4);
                                if ((!DialogObject.isChatDialog(tLRPC$Dialog2.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog2.id)))) && MessagesController.getInstance(i2).getDialogUnreadCount(tLRPC$Dialog2) != 0) {
                                    FileLog.d("getTotalAllUnreadCount: account=" + i2 + " count++ if getDialogUnreadCount != 0 (" + MessagesController.getInstance(i2).getDialogUnreadCount(tLRPC$Dialog2) + ")");
                                    i++;
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e((Throwable) e2, false);
                        }
                    } else {
                        FileLog.d("getTotalAllUnreadCount: account=" + i2 + " count += controller.pushDialogs (" + notificationsController.pushDialogs.size() + ")");
                        size = notificationsController.pushDialogs.size();
                    }
                    i += size;
                }
            }
        }
        FileLog.d("getTotalAllUnreadCount: total is " + i);
        return i;
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

    private boolean isPersonalMessage(MessageObject messageObject) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        return (tLRPC$Peer != null && tLRPC$Peer.chat_id == 0 && tLRPC$Peer.channel_id == 0 && ((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty))) || messageObject.isStoryReactionPush;
    }

    private boolean isSilentMessage(MessageObject messageObject) {
        return messageObject.messageOwner.silent || messageObject.isReactionPush;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dismissNotification$35() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
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
    public /* synthetic */ void lambda$hideNotifications$34() {
        notificationManager.cancel(this.notificationId);
        this.lastWearNotifiedMessageId.clear();
        for (int i = 0; i < this.wearNotificationsIds.size(); i++) {
            notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(i)).intValue());
        }
        this.wearNotificationsIds.clear();
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
    public static /* synthetic */ void lambda$loadTopicsNotificationsExceptions$50(Consumer consumer, HashSet hashSet) {
        if (consumer != null) {
            consumer.accept(hashSet);
        }
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
    public static /* synthetic */ void lambda$playOutChatSound$45(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
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
    public /* synthetic */ void lambda$processDeleteStory$14(long j, int i) {
        StoryNotification storyNotification = (StoryNotification) this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            storyNotification.dateByIds.remove(Integer.valueOf(i));
            if (!storyNotification.dateByIds.isEmpty()) {
                getMessagesStorage().putStoryPushMessage(storyNotification);
                return;
            }
            this.storyPushMessagesDict.remove(j);
            this.storyPushMessages.remove(storyNotification);
            getMessagesStorage().deleteStoryPushMessage(j);
            showOrUpdateNotification(false);
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0074 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0138  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDialogsUpdateRead$28(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        int i;
        boolean z;
        boolean z2;
        int i2;
        int intValue;
        int i3;
        Integer num;
        int i4 = this.total_unread_count;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        int i5 = 0;
        while (true) {
            if (i5 >= longSparseIntArray.size()) {
                break;
            }
            long keyAt = longSparseIntArray.keyAt(i5);
            Integer num2 = (Integer) this.pushDialogs.get(keyAt);
            int i6 = longSparseIntArray.get(keyAt);
            if (DialogObject.isChatDialog(keyAt)) {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-keyAt));
                i6 = (chat == null || chat.min || ChatObject.isNotInChat(chat)) ? 0 : 0;
                if (chat != null) {
                    z = chat.forum;
                    i = i6;
                    if (!z) {
                        int notifyOverride = getNotifyOverride(notificationsSettings, keyAt, 0L);
                        if (notifyOverride == -1) {
                            z2 = isGlobalNotificationsEnabled(keyAt, false, false);
                        } else if (notifyOverride == 2) {
                            z2 = false;
                        }
                        if (this.notifyCheck && !z2 && (num = (Integer) this.pushDialogsOverrideMention.get(keyAt)) != null && num.intValue() != 0) {
                            i = num.intValue();
                            z2 = true;
                        }
                        if (i == 0) {
                            this.smartNotificationsDialogs.remove(keyAt);
                        }
                        if (i < 0) {
                            if (num2 == null) {
                                i5++;
                            } else {
                                i = num2.intValue() + i;
                            }
                        }
                        if ((!z2 || i == 0) && num2 != null) {
                            if (getMessagesController().isForum(keyAt)) {
                                i2 = this.total_unread_count;
                                intValue = num2.intValue() > 0 ? 1 : 0;
                            } else {
                                i2 = this.total_unread_count;
                                intValue = num2.intValue();
                            }
                            this.total_unread_count = i2 - intValue;
                        }
                        if (i == 0) {
                            this.pushDialogs.remove(keyAt);
                            this.pushDialogsOverrideMention.remove(keyAt);
                            int i7 = 0;
                            while (i7 < this.pushMessages.size()) {
                                MessageObject messageObject = this.pushMessages.get(i7);
                                if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == keyAt && !messageObject.isStoryReactionPush) {
                                    if (isPersonalMessage(messageObject)) {
                                        this.personalCount--;
                                    }
                                    this.pushMessages.remove(i7);
                                    i7--;
                                    this.delayedPushMessages.remove(messageObject);
                                    long j = messageObject.messageOwner.peer_id.channel_id;
                                    long j2 = j != 0 ? -j : 0L;
                                    SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(j2);
                                    if (sparseArray != null) {
                                        sparseArray.remove(messageObject.getId());
                                        if (sparseArray.size() == 0) {
                                            this.pushMessagesDict.remove(j2);
                                        }
                                    }
                                    arrayList.add(messageObject);
                                }
                                i7++;
                            }
                        } else if (z2) {
                            if (getMessagesController().isForum(keyAt)) {
                                i3 = this.total_unread_count + (i <= 0 ? 0 : 1);
                            } else {
                                i3 = this.total_unread_count + i;
                            }
                            this.total_unread_count = i3;
                            this.pushDialogs.put(keyAt, Integer.valueOf(i));
                        }
                        i5++;
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
                    this.total_unread_count = i2 - intValue;
                    if (i == 0) {
                    }
                    i5++;
                }
            }
            i = i6;
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
            this.total_unread_count = i2 - intValue;
            if (i == 0) {
            }
            i5++;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$26(arrayList);
                }
            });
        }
        if (i4 != this.total_unread_count) {
            if (this.notifyCheck) {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            } else {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
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
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(j);
                if (sparseArray == null) {
                    break;
                }
                MessageObject messageObject2 = (MessageObject) sparseArray.get(messageObject.getId());
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStoryReactions$17() {
        int i = 0;
        boolean z = false;
        while (i < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i);
            if (messageObject != null && messageObject.isStoryReactionPush) {
                this.pushMessages.remove(i);
                i--;
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(messageObject.getDialogId());
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$30(int i) {
        if (this.total_unread_count == 0) {
            this.popupMessages.clear();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$31(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, Collection collection) {
        long j;
        long j2;
        boolean z;
        LongSparseArray longSparseArray2;
        int i;
        long j3;
        boolean z2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        SharedPreferences sharedPreferences;
        MessageObject messageObject;
        SparseArray sparseArray;
        long j4;
        long j5;
        int i2;
        TLRPC$Message tLRPC$Message;
        boolean isGlobalNotificationsEnabled;
        SparseArray sparseArray2;
        ArrayList arrayList3 = arrayList;
        this.pushDialogs.clear();
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        boolean z3 = false;
        this.total_unread_count = 0;
        this.personalCount = 0;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        LongSparseArray longSparseArray3 = new LongSparseArray();
        long j6 = 0;
        if (arrayList3 != null) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                TLRPC$Message tLRPC$Message2 = (TLRPC$Message) arrayList3.get(i3);
                if (tLRPC$Message2 != null && ((tLRPC$MessageFwdHeader = tLRPC$Message2.fwd_from) == null || !tLRPC$MessageFwdHeader.imported)) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) && (!tLRPC$Message2.silent || (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined)))) {
                        long j7 = tLRPC$Message2.peer_id.channel_id;
                        long j8 = j7 != j6 ? -j7 : j6;
                        SparseArray sparseArray3 = (SparseArray) this.pushMessagesDict.get(j8);
                        if (sparseArray3 == null || sparseArray3.indexOfKey(tLRPC$Message2.id) < 0) {
                            MessageObject messageObject2 = new MessageObject(this.currentAccount, tLRPC$Message2, z3, z3);
                            if (isPersonalMessage(messageObject2)) {
                                this.personalCount++;
                            }
                            sharedPreferences = notificationsSettings;
                            long dialogId = messageObject2.getDialogId();
                            long topicId = MessageObject.getTopicId(this.currentAccount, messageObject2.messageOwner, getMessagesController().isForum(messageObject2));
                            long fromChatId = messageObject2.messageOwner.mentioned ? messageObject2.getFromChatId() : dialogId;
                            int indexOfKey = longSparseArray3.indexOfKey(fromChatId);
                            if (indexOfKey < 0 || topicId != 0) {
                                messageObject = messageObject2;
                                sparseArray = sparseArray3;
                                j4 = dialogId;
                                j5 = j8;
                                i2 = i3;
                                tLRPC$Message = tLRPC$Message2;
                                int notifyOverride = getNotifyOverride(sharedPreferences, fromChatId, topicId);
                                isGlobalNotificationsEnabled = notifyOverride == -1 ? isGlobalNotificationsEnabled(fromChatId, messageObject.isReactionPush, messageObject.isStoryReactionPush) : notifyOverride != 2;
                                longSparseArray3.put(fromChatId, Boolean.valueOf(isGlobalNotificationsEnabled));
                            } else {
                                isGlobalNotificationsEnabled = ((Boolean) longSparseArray3.valueAt(indexOfKey)).booleanValue();
                                messageObject = messageObject2;
                                sparseArray = sparseArray3;
                                i2 = i3;
                                j4 = dialogId;
                                j5 = j8;
                                tLRPC$Message = tLRPC$Message2;
                            }
                            if (isGlobalNotificationsEnabled && (fromChatId != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray();
                                    this.pushMessagesDict.put(j5, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(tLRPC$Message.id, messageObject);
                                appendMessage(messageObject);
                                if (j4 != fromChatId) {
                                    long j9 = j4;
                                    Integer num = (Integer) this.pushDialogsOverrideMention.get(j9);
                                    this.pushDialogsOverrideMention.put(j9, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                }
                            }
                            i3 = i2 + 1;
                            arrayList3 = arrayList;
                            notificationsSettings = sharedPreferences;
                            z3 = false;
                            j6 = 0;
                        }
                    }
                }
                i2 = i3;
                sharedPreferences = notificationsSettings;
                i3 = i2 + 1;
                arrayList3 = arrayList;
                notificationsSettings = sharedPreferences;
                z3 = false;
                j6 = 0;
            }
        }
        SharedPreferences sharedPreferences2 = notificationsSettings;
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            long keyAt = longSparseArray.keyAt(i4);
            int indexOfKey2 = longSparseArray3.indexOfKey(keyAt);
            if (indexOfKey2 >= 0) {
                z2 = ((Boolean) longSparseArray3.valueAt(indexOfKey2)).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(sharedPreferences2, keyAt, 0L);
                boolean isGlobalNotificationsEnabled2 = notifyOverride2 == -1 ? isGlobalNotificationsEnabled(keyAt, false, false) : notifyOverride2 != 2;
                longSparseArray3.put(keyAt, Boolean.valueOf(isGlobalNotificationsEnabled2));
                z2 = isGlobalNotificationsEnabled2;
            }
            if (z2) {
                Integer num2 = (Integer) longSparseArray.valueAt(i4);
                int intValue = num2.intValue();
                this.pushDialogs.put(keyAt, num2);
                this.total_unread_count = getMessagesController().isForum(keyAt) ? this.total_unread_count + (intValue > 0 ? 1 : 0) : this.total_unread_count + intValue;
            }
        }
        if (arrayList2 != null) {
            int i5 = 0;
            while (i5 < arrayList2.size()) {
                MessageObject messageObject3 = (MessageObject) arrayList2.get(i5);
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
                    if (indexOfKey3 < 0 || topicId2 != 0) {
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
                    } else {
                        j = j10;
                        z = ((Boolean) longSparseArray3.valueAt(indexOfKey3)).booleanValue();
                        j2 = fromChatId2;
                    }
                    if (z && (j2 != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                        if (id != 0) {
                            if (messageObject3.isStoryReactionPush) {
                                j3 = messageObject3.getDialogId();
                            } else {
                                long j12 = messageObject3.messageOwner.peer_id.channel_id;
                                j3 = j12 != 0 ? -j12 : 0L;
                            }
                            SparseArray sparseArray4 = (SparseArray) this.pushMessagesDict.get(j3);
                            if (sparseArray4 == null) {
                                sparseArray4 = new SparseArray();
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
                            Integer num3 = (Integer) this.pushDialogsOverrideMention.get(dialogId2);
                            this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num3 == null ? 1 : num3.intValue() + 1));
                        }
                        Integer num4 = (Integer) this.pushDialogs.get(j2);
                        int intValue2 = num4 != null ? num4.intValue() + 1 : 1;
                        if (getMessagesController().isForum(j2)) {
                            if (num4 != null) {
                                this.total_unread_count -= num4.intValue() > 0 ? 1 : 0;
                            }
                            i = this.total_unread_count + (intValue2 > 0 ? 1 : 0);
                        } else {
                            if (num4 != null) {
                                this.total_unread_count -= num4.intValue();
                            }
                            i = this.total_unread_count + intValue2;
                        }
                        this.total_unread_count = i;
                        this.pushDialogs.put(j2, Integer.valueOf(intValue2));
                        i5++;
                        longSparseArray3 = longSparseArray2;
                    }
                }
                longSparseArray2 = longSparseArray3;
                i5++;
                longSparseArray3 = longSparseArray2;
            }
        }
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                StoryNotification storyNotification = (StoryNotification) it.next();
                long j14 = storyNotification.dialogId;
                StoryNotification storyNotification2 = (StoryNotification) this.storyPushMessagesDict.get(j14);
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004a, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x020d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$25(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        int i;
        int i2;
        Integer num;
        boolean z3;
        long j;
        boolean z4;
        long j2;
        long j3;
        LongSparseArray longSparseArray;
        int i3;
        boolean z5;
        MessageObject messageObject;
        long j4;
        long j5;
        long j6;
        LongSparseArray longSparseArray2;
        SparseArray sparseArray;
        long j7;
        long j8;
        boolean z6;
        long j9;
        SparseArray sparseArray2;
        String str;
        long j10;
        MessageObject messageObject2;
        TLRPC$Message tLRPC$Message;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray3 = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z7 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i4 = 0;
        int i5 = 0;
        boolean z8 = false;
        boolean z9 = false;
        boolean z10 = false;
        boolean z11 = false;
        while (i5 < arrayList.size()) {
            MessageObject messageObject3 = (MessageObject) arrayList3.get(i5);
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
                FileLog.d("skipped message because 1");
                longSparseArray = longSparseArray3;
                z3 = z7;
                i3 = i5;
                z5 = z8;
                z8 = z5;
                longSparseArray2 = longSparseArray;
                i5 = i3 + 1;
                arrayList3 = arrayList;
                longSparseArray3 = longSparseArray2;
                z7 = z3;
            }
            if (!MessageObject.isTopicActionMessage(messageObject3)) {
                if (messageObject3.isStoryPush) {
                    long currentTimeMillis = messageObject3.messageOwner == null ? System.currentTimeMillis() : tLRPC$Message.date * 1000;
                    long dialogId = messageObject3.getDialogId();
                    int id = messageObject3.getId();
                    StoryNotification storyNotification = (StoryNotification) this.storyPushMessagesDict.get(dialogId);
                    if (storyNotification != null) {
                        storyNotification.dateByIds.put(Integer.valueOf(id), new Pair<>(Long.valueOf(currentTimeMillis), Long.valueOf(currentTimeMillis + 86400000)));
                        boolean z12 = storyNotification.hidden;
                        boolean z13 = messageObject3.isStoryPushHidden;
                        if (z12 != z13) {
                            storyNotification.hidden = z13;
                            z11 = true;
                        }
                        storyNotification.date = storyNotification.getLeastDate();
                        getMessagesStorage().putStoryPushMessage(storyNotification);
                        z9 = true;
                    } else {
                        StoryNotification storyNotification2 = new StoryNotification(dialogId, messageObject3.localName, id, currentTimeMillis);
                        storyNotification2.hidden = messageObject3.isStoryPushHidden;
                        this.storyPushMessages.add(storyNotification2);
                        this.storyPushMessagesDict.put(dialogId, storyNotification2);
                        getMessagesStorage().putStoryPushMessage(storyNotification2);
                        z8 = true;
                        z11 = true;
                    }
                    Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
                        @Override // j$.util.function.ToLongFunction
                        public final long applyAsLong(Object obj) {
                            long j11;
                            j11 = ((NotificationsController.StoryNotification) obj).date;
                            return j11;
                        }
                    }));
                    longSparseArray2 = longSparseArray3;
                    z3 = z7;
                    i3 = i5;
                } else {
                    int id2 = messageObject3.getId();
                    if (messageObject3.isFcmMessage()) {
                        j = messageObject3.messageOwner.random_id;
                        z3 = z7;
                    } else {
                        z3 = z7;
                        j = 0;
                    }
                    long dialogId2 = messageObject3.getDialogId();
                    if (messageObject3.isFcmMessage()) {
                        z4 = messageObject3.localChannel;
                    } else {
                        if (DialogObject.isChatDialog(dialogId2)) {
                            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId2));
                            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                z4 = true;
                            }
                        }
                        z4 = false;
                    }
                    if (messageObject3.isStoryReactionPush) {
                        long j11 = j;
                        j3 = messageObject3.getDialogId();
                        j2 = j11;
                    } else {
                        j2 = j;
                        long j12 = messageObject3.messageOwner.peer_id.channel_id;
                        j3 = j12 != 0 ? -j12 : 0L;
                    }
                    SparseArray sparseArray3 = (SparseArray) this.pushMessagesDict.get(j3);
                    MessageObject messageObject4 = sparseArray3 != null ? (MessageObject) sparseArray3.get(id2) : null;
                    MessageObject messageObject5 = messageObject4;
                    if (messageObject4 == null) {
                        i3 = i5;
                        z5 = z8;
                        long j13 = messageObject3.messageOwner.random_id;
                        if (j13 != 0) {
                            messageObject = (MessageObject) this.fcmRandomMessagesDict.get(j13);
                            if (messageObject != null) {
                                longSparseArray = longSparseArray3;
                                this.fcmRandomMessagesDict.remove(messageObject3.messageOwner.random_id);
                            } else {
                                longSparseArray = longSparseArray3;
                            }
                            if (messageObject == null) {
                                if (messageObject.isFcmMessage()) {
                                    if (sparseArray3 == null) {
                                        sparseArray3 = new SparseArray();
                                        this.pushMessagesDict.put(j3, sparseArray3);
                                    }
                                    sparseArray3.put(id2, messageObject3);
                                    int indexOf = this.pushMessages.indexOf(messageObject);
                                    if (indexOf >= 0) {
                                        this.pushMessages.set(indexOf, messageObject3);
                                        j10 = j3;
                                        messageObject2 = messageObject3;
                                        i4 = addToPopupMessages(arrayList2, messageObject3, dialogId2, z4, notificationsSettings);
                                    } else {
                                        j10 = j3;
                                        messageObject2 = messageObject3;
                                    }
                                    if (z) {
                                        boolean z14 = messageObject2.localEdit;
                                        if (z14) {
                                            getMessagesStorage().putPushMessage(messageObject2);
                                        }
                                        z9 = z14;
                                    }
                                } else {
                                    j10 = j3;
                                }
                                str = "skipped message because old message with same dialog and message ids exist: did=" + j10 + ", mid=" + id2;
                            } else {
                                long j14 = j3;
                                if (z9) {
                                    str = "skipped message because edited";
                                } else {
                                    if (z) {
                                        getMessagesStorage().putPushMessage(messageObject3);
                                    }
                                    long topicId = MessageObject.getTopicId(this.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
                                    if (dialogId2 == this.openedDialogId && ApplicationLoader.isScreenOn && !messageObject3.isStoryReactionPush) {
                                        if (!z) {
                                            playInChatSound();
                                        }
                                        str = "skipped message because chat is already opened (openedDialogId = " + this.openedDialogId + ")";
                                    } else {
                                        TLRPC$Message tLRPC$Message3 = messageObject3.messageOwner;
                                        if (!tLRPC$Message3.mentioned) {
                                            j4 = dialogId2;
                                        } else if (z3 || !(tLRPC$Message3.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                            j4 = messageObject3.getFromChatId();
                                        } else {
                                            str = "skipped message because message is mention of pinned";
                                        }
                                        if (isPersonalMessage(messageObject3)) {
                                            this.personalCount++;
                                        }
                                        DialogObject.isChatDialog(j4);
                                        LongSparseArray longSparseArray4 = longSparseArray;
                                        int indexOfKey = longSparseArray4.indexOfKey(j4);
                                        int i6 = i4;
                                        if (indexOfKey < 0 || topicId != 0) {
                                            j5 = dialogId2;
                                            j6 = j2;
                                            longSparseArray2 = longSparseArray4;
                                            long j15 = j4;
                                            sparseArray = sparseArray3;
                                            boolean z15 = z4;
                                            j7 = j14;
                                            int notifyOverride = getNotifyOverride(notificationsSettings, j4, topicId);
                                            if (notifyOverride == -1) {
                                                z6 = isGlobalNotificationsEnabled(j15, Boolean.valueOf(z15), messageObject3.isReactionPush, messageObject3.isStoryReactionPush);
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("NotificationsController: process new messages, isGlobalNotificationsEnabled(");
                                                j8 = j15;
                                                sb.append(j8);
                                                sb.append(", ");
                                                z4 = z15;
                                                sb.append(z4);
                                                sb.append(", ");
                                                sb.append(messageObject3.isReactionPush);
                                                sb.append(", ");
                                                sb.append(messageObject3.isStoryReactionPush);
                                                sb.append(") = ");
                                                sb.append(z6);
                                                FileLog.d(sb.toString());
                                            } else {
                                                z4 = z15;
                                                j8 = j15;
                                                z6 = notifyOverride != 2;
                                            }
                                            longSparseArray2.put(j8, Boolean.valueOf(z6));
                                        } else {
                                            z6 = ((Boolean) longSparseArray4.valueAt(indexOfKey)).booleanValue();
                                            j7 = j14;
                                            j5 = dialogId2;
                                            j6 = j2;
                                            longSparseArray2 = longSparseArray4;
                                            sparseArray = sparseArray3;
                                            j8 = j4;
                                        }
                                        FileLog.d("NotificationsController: process new messages, value is " + z6 + " (" + j8 + ", " + z4 + ", " + messageObject3.isReactionPush + ", " + messageObject3.isStoryReactionPush + ")");
                                        if (z6) {
                                            if (z) {
                                                j9 = j8;
                                                i4 = i6;
                                            } else {
                                                j9 = j8;
                                                i4 = addToPopupMessages(arrayList2, messageObject3, j8, z4, notificationsSettings);
                                            }
                                            if (!z10) {
                                                z10 = messageObject3.messageOwner.from_scheduled;
                                            }
                                            this.delayedPushMessages.add(messageObject3);
                                            appendMessage(messageObject3);
                                            if (id2 != 0) {
                                                if (sparseArray == null) {
                                                    sparseArray2 = new SparseArray();
                                                    this.pushMessagesDict.put(j7, sparseArray2);
                                                } else {
                                                    sparseArray2 = sparseArray;
                                                }
                                                sparseArray2.put(id2, messageObject3);
                                            } else {
                                                long j16 = j6;
                                                if (j16 != 0) {
                                                    this.fcmRandomMessagesDict.put(j16, messageObject3);
                                                }
                                            }
                                            if (j5 != j9) {
                                                long j17 = j5;
                                                Integer num2 = (Integer) this.pushDialogsOverrideMention.get(j17);
                                                this.pushDialogsOverrideMention.put(j17, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                                            }
                                        } else {
                                            j9 = j8;
                                            i4 = i6;
                                        }
                                        if (messageObject3.isReactionPush) {
                                            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                            sparseBooleanArray.put(id2, true);
                                            getMessagesController().checkUnreadReactions(j9, topicId, sparseBooleanArray);
                                        }
                                        z8 = true;
                                    }
                                }
                            }
                            FileLog.d(str);
                            z8 = z5;
                            longSparseArray2 = longSparseArray;
                        } else {
                            longSparseArray = longSparseArray3;
                        }
                    } else {
                        longSparseArray = longSparseArray3;
                        i3 = i5;
                        z5 = z8;
                    }
                    messageObject = messageObject5;
                    if (messageObject == null) {
                    }
                    FileLog.d(str);
                    z8 = z5;
                    longSparseArray2 = longSparseArray;
                }
                i5 = i3 + 1;
                arrayList3 = arrayList;
                longSparseArray3 = longSparseArray2;
                z7 = z3;
            }
            FileLog.d("skipped message because 1");
            longSparseArray = longSparseArray3;
            z3 = z7;
            i3 = i5;
            z5 = z8;
            z8 = z5;
            longSparseArray2 = longSparseArray;
            i5 = i3 + 1;
            arrayList3 = arrayList;
            longSparseArray3 = longSparseArray2;
            z7 = z3;
        }
        final int i7 = i4;
        boolean z16 = z8;
        if (z16) {
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
        if (z || z10) {
            if (z9) {
                FileLog.d("NotificationsController processNewMessages: edited branch, showOrUpdateNotification " + this.notifyCheck);
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z16) {
                FileLog.d("NotificationsController processNewMessages: added branch");
                MessageObject messageObject6 = (MessageObject) arrayList.get(0);
                long dialogId3 = messageObject6.getDialogId();
                long topicId2 = MessageObject.getTopicId(this.currentAccount, messageObject6.messageOwner, getMessagesController().isForum(dialogId3));
                Boolean valueOf = messageObject6.isFcmMessage() ? Boolean.valueOf(messageObject6.localChannel) : null;
                int i8 = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(notificationsSettings, dialogId3, topicId2);
                boolean isGlobalNotificationsEnabled = notifyOverride2 == -1 ? isGlobalNotificationsEnabled(dialogId3, valueOf, messageObject6.isReactionPush, messageObject6.isStoryReactionPush) : notifyOverride2 != 2;
                Integer num3 = (Integer) this.pushDialogs.get(dialogId3);
                if (num3 != null) {
                    i = 1;
                    i2 = num3.intValue() + 1;
                } else {
                    i = 1;
                    i2 = 1;
                }
                if (this.notifyCheck && !isGlobalNotificationsEnabled && (num = (Integer) this.pushDialogsOverrideMention.get(dialogId3)) != null && num.intValue() != 0) {
                    i2 = num.intValue();
                    isGlobalNotificationsEnabled = true;
                }
                if (isGlobalNotificationsEnabled && !messageObject6.isStoryPush) {
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
                if (i8 != this.total_unread_count || z11) {
                    this.delayedPushMessages.clear();
                    FileLog.d("NotificationsController processNewMessages: added branch: " + this.notifyCheck);
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
        if (z11) {
            updateStoryPushesRunnable();
        }
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$19(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ca, code lost:
        if (r1.messageOwner.date <= r23) goto L56;
     */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processReadMessages$20(LongSparseIntArray longSparseIntArray, final ArrayList arrayList, long j, int i, int i2, boolean z) {
        long j2;
        SparseArray sparseArray;
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
                        SparseArray sparseArray2 = (SparseArray) this.pushMessagesDict.get(j3);
                        if (sparseArray2 != null) {
                            sparseArray2.remove(messageObject.getId());
                            if (sparseArray2.size() == 0) {
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
                if (messageObject2.getDialogId() == j && !messageObject2.isStoryReactionPush) {
                    if (i2 == 0) {
                        int id = messageObject2.getId();
                        if (z) {
                            if (isPersonalMessage(messageObject2)) {
                                this.personalCount--;
                            }
                            if (messageObject2.isStoryReactionPush) {
                                long j6 = messageObject2.messageOwner.peer_id.channel_id;
                                j2 = j6 != 0 ? -j6 : 0L;
                            } else {
                                j2 = messageObject2.getDialogId();
                            }
                            sparseArray = (SparseArray) this.pushMessagesDict.get(j2);
                            if (sparseArray != null) {
                                sparseArray.remove(messageObject2.getId());
                                if (sparseArray.size() == 0) {
                                    this.pushMessagesDict.remove(j2);
                                }
                            }
                            this.pushMessages.remove(i6);
                            this.delayedPushMessages.remove(messageObject2);
                            arrayList.add(messageObject2);
                            i6--;
                        } else {
                            if (isPersonalMessage(messageObject2)) {
                            }
                            if (messageObject2.isStoryReactionPush) {
                            }
                            sparseArray = (SparseArray) this.pushMessagesDict.get(j2);
                            if (sparseArray != null) {
                            }
                            this.pushMessages.remove(i6);
                            this.delayedPushMessages.remove(messageObject2);
                            arrayList.add(messageObject2);
                            i6--;
                        }
                    }
                    i6++;
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
    public /* synthetic */ void lambda$processReadStories$15(long j) {
        StoryNotification storyNotification = (StoryNotification) this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            this.storyPushMessagesDict.remove(j);
            this.storyPushMessages.remove(storyNotification);
            getMessagesStorage().deleteStoryPushMessage(j);
            showOrUpdateNotification(false);
            updateStoryPushesRunnable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSeenStoryReactions$13(int i) {
        int i2 = 0;
        boolean z = false;
        while (i2 < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i2);
            if (messageObject.isStoryReactionPush && Math.abs(messageObject.getId()) == i) {
                this.pushMessages.remove(i2);
                SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(messageObject.getDialogId());
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$12(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        int intValue;
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
            Integer num3 = (Integer) this.pushDialogs.get(j);
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
                        SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(j);
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
                    intValue = i4 + (num4.intValue() <= 0 ? 0 : 1);
                } else {
                    int intValue2 = this.total_unread_count - num3.intValue();
                    this.total_unread_count = intValue2;
                    intValue = intValue2 + num4.intValue();
                }
                this.total_unread_count = intValue;
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
            if (this.notifyCheck) {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            } else {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$9(LongSparseArray longSparseArray, boolean z, final ArrayList arrayList) {
        long j;
        Integer num;
        int intValue;
        int intValue2;
        LongSparseArray longSparseArray2 = longSparseArray;
        int i = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        int i2 = 0;
        while (i2 < longSparseArray.size()) {
            long keyAt = longSparseArray2.keyAt(i2);
            SparseArray sparseArray = (SparseArray) this.pushMessagesDict.get(keyAt);
            if (sparseArray != null) {
                ArrayList arrayList2 = (ArrayList) longSparseArray2.get(keyAt);
                int size = arrayList2.size();
                int i3 = 0;
                while (i3 < size) {
                    int intValue3 = ((Integer) arrayList2.get(i3)).intValue();
                    MessageObject messageObject = (MessageObject) sparseArray.get(intValue3);
                    if (messageObject == null || messageObject.isStoryReactionPush || (z && !messageObject.isReactionPush)) {
                        j = keyAt;
                    } else {
                        j = keyAt;
                        long dialogId = messageObject.getDialogId();
                        Integer num2 = (Integer) this.pushDialogs.get(dialogId);
                        if (num2 == null) {
                            num2 = 0;
                        }
                        int intValue4 = num2.intValue() - 1;
                        Integer valueOf = Integer.valueOf(intValue4);
                        if (intValue4 <= 0) {
                            this.smartNotificationsDialogs.remove(dialogId);
                            num = 0;
                        } else {
                            num = valueOf;
                        }
                        if (!num.equals(num2)) {
                            if (getMessagesController().isForum(dialogId)) {
                                intValue = this.total_unread_count - (num2.intValue() > 0 ? 1 : 0);
                                this.total_unread_count = intValue;
                                intValue2 = num.intValue() > 0 ? 1 : 0;
                            } else {
                                intValue = this.total_unread_count - num2.intValue();
                                this.total_unread_count = intValue;
                                intValue2 = num.intValue();
                            }
                            this.total_unread_count = intValue + intValue2;
                            this.pushDialogs.put(dialogId, num);
                        }
                        if (num.intValue() == 0) {
                            this.pushDialogs.remove(dialogId);
                            this.pushDialogsOverrideMention.remove(dialogId);
                        }
                        sparseArray.remove(intValue3);
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
            if (this.notifyCheck) {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            } else {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
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
    public /* synthetic */ void lambda$repeatNotificationMaybe$38() {
        int i = Calendar.getInstance().get(11);
        if (i < 11 || i > 22) {
            scheduleNotificationRepeat();
            return;
        }
        notificationManager.cancel(this.notificationId);
        showOrUpdateNotification(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLastOnlineFromOtherDevice$4(int i) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("set last online from other device = " + i);
        }
        this.lastOnlineFromOtherDevice = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedDialogId$2(long j, long j2) {
        this.openedDialogId = j;
        this.openedTopicId = j2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedInBubble$3(boolean z, long j) {
        if (z) {
            this.openedInBubbleDialogs.add(Long.valueOf(j));
        } else {
            this.openedInBubbleDialogs.remove(Long.valueOf(j));
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNotifications$33() {
        showOrUpdateNotification(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBadge$32() {
        setBadge(getTotalAllUnreadCount());
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0060  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void resetNotificationSound(NotificationCompat.Builder builder, long j, long j2, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str2;
        String str3;
        FileLog.d("resetNotificationSound");
        Uri uri2 = Settings.System.DEFAULT_RINGTONE_URI;
        if (uri2 == null || uri == null || TextUtils.equals(uri2.toString(), uri.toString())) {
            return;
        }
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String uri3 = uri2.toString();
        String string = LocaleController.getString(R.string.DefaultRingtone);
        if (z) {
            if (i3 == 2) {
                str2 = "ChannelSound";
            } else if (i3 == 0) {
                str2 = "GroupSound";
            } else if (i3 == 1) {
                str2 = "GlobalSound";
            } else if (i3 == 3) {
                str2 = "StoriesSound";
            } else {
                if (i3 == 4 || i3 == 5) {
                    edit.putString("ReactionSound", string);
                }
                if (i3 != 2) {
                    str3 = "ChannelSoundPath";
                } else if (i3 == 0) {
                    str3 = "GroupSoundPath";
                } else if (i3 == 1) {
                    str3 = "GlobalSoundPath";
                } else if (i3 == 3) {
                    str3 = "StoriesSoundPath";
                } else {
                    if (i3 == 4 || i3 == 5) {
                        edit.putString("ReactionSound", uri3);
                    }
                    getNotificationsController().lambda$deleteNotificationChannelGlobal$40(i3, -1);
                }
                edit.putString(str3, uri3);
                getNotificationsController().lambda$deleteNotificationChannelGlobal$40(i3, -1);
            }
            edit.putString(str2, string);
            if (i3 != 2) {
            }
            edit.putString(str3, uri3);
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

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, ConnectionsManager.FileTypeVideo);
            int i = getAccountInstance().getNotificationsSettings().getInt("repeat_messages", 60);
            if (i <= 0 || this.personalCount <= 0) {
                this.alarmManager.cancel(service);
            } else {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + (i * 60000), service);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void setBadge(int i) {
        if (this.lastBadgeCount == i) {
            return;
        }
        FileLog.d("setBadge " + i);
        this.lastBadgeCount = i;
        NotificationBadge.applyCount(i);
    }

    private void setNotificationChannel(Notification notification, NotificationCompat.Builder builder, boolean z) {
        builder.setChannelId(z ? OTHER_NOTIFICATIONS_CHANNEL : notification.getChannelId());
    }

    /* JADX WARN: Code restructure failed: missing block: B:175:0x04fc, code lost:
        if (r7.local_id != 0) goto L662;
     */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0480  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0541  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0549  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0821  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0831 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:320:0x0846  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x085e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:339:0x088a  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x09d6 A[LOOP:5: B:363:0x09ce->B:365:0x09d6, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:368:0x09f2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x0a15  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0a30  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x0a41  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0ba1  */
    /* JADX WARN: Removed duplicated region for block: B:473:0x0c71  */
    /* JADX WARN: Removed duplicated region for block: B:512:0x0d7b  */
    /* JADX WARN: Removed duplicated region for block: B:514:0x0d7f  */
    /* JADX WARN: Removed duplicated region for block: B:527:0x0db2  */
    /* JADX WARN: Removed duplicated region for block: B:531:0x0e0a  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x0e3a  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x0e5d  */
    /* JADX WARN: Removed duplicated region for block: B:552:0x0ea2  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x0f39  */
    /* JADX WARN: Removed duplicated region for block: B:569:0x0f56  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x0f83  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:583:0x0fca  */
    /* JADX WARN: Removed duplicated region for block: B:586:0x0feb  */
    /* JADX WARN: Removed duplicated region for block: B:589:0x1047  */
    /* JADX WARN: Removed duplicated region for block: B:594:0x1084  */
    /* JADX WARN: Removed duplicated region for block: B:599:0x10a2  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x10c5  */
    /* JADX WARN: Removed duplicated region for block: B:603:0x10e2  */
    /* JADX WARN: Removed duplicated region for block: B:608:0x1106  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:611:0x113b  */
    /* JADX WARN: Removed duplicated region for block: B:617:0x1196 A[Catch: Exception -> 0x119e, TryCatch #4 {Exception -> 0x119e, blocks: (B:615:0x118f, B:617:0x1196, B:620:0x11a0), top: B:731:0x118f }] */
    /* JADX WARN: Removed duplicated region for block: B:626:0x11b8  */
    /* JADX WARN: Removed duplicated region for block: B:628:0x11c3  */
    /* JADX WARN: Removed duplicated region for block: B:630:0x11c8  */
    /* JADX WARN: Removed duplicated region for block: B:638:0x11dd  */
    /* JADX WARN: Removed duplicated region for block: B:646:0x11f5  */
    /* JADX WARN: Removed duplicated region for block: B:648:0x11fb  */
    /* JADX WARN: Removed duplicated region for block: B:651:0x1207  */
    /* JADX WARN: Removed duplicated region for block: B:656:0x1214  */
    /* JADX WARN: Removed duplicated region for block: B:669:0x12a5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:678:0x12d7  */
    /* JADX WARN: Removed duplicated region for block: B:682:0x1374  */
    /* JADX WARN: Removed duplicated region for block: B:689:0x13bf  */
    /* JADX WARN: Removed duplicated region for block: B:698:0x13f2  */
    /* JADX WARN: Removed duplicated region for block: B:708:0x1459  */
    /* JADX WARN: Removed duplicated region for block: B:737:0x0d88 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:749:0x0217 A[EDGE_INSN: B:749:0x0217->B:63:0x0217 ?: BREAK  , SYNTHETIC] */
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
        String str4;
        Notification notification;
        int id;
        ArrayList<StoryNotification> arrayList2;
        boolean z6;
        MessageObject messageObject;
        LongSparseArray longSparseArray4;
        ArrayList arrayList3;
        long j3;
        long j4;
        long j5;
        int i8;
        LongSparseArray longSparseArray5;
        DialogKey dialogKey;
        long j6;
        ArrayList<StoryNotification> arrayList4;
        long j7;
        TLRPC$User tLRPC$User;
        TLRPC$User tLRPC$User2;
        String string;
        TLRPC$Chat tLRPC$Chat;
        boolean z7;
        boolean z8;
        boolean z9;
        TLRPC$FileLocation tLRPC$FileLocation;
        String str5;
        boolean z10;
        SharedPreferences sharedPreferences;
        NotificationsController notificationsController2;
        int i9;
        int i10;
        ArrayList arrayList5;
        Notification notification2;
        boolean z11;
        LongSparseArray longSparseArray6;
        LongSparseArray longSparseArray7;
        long j8;
        DialogKey dialogKey2;
        TLRPC$FileLocation tLRPC$FileLocation2;
        String str6;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$User tLRPC$User3;
        TLRPC$TL_forumTopic findTopic;
        StringBuilder sb;
        String str7;
        String userName;
        TLRPC$FileLocation tLRPC$FileLocation3;
        String str8;
        TLRPC$FileLocation tLRPC$FileLocation4;
        int i11;
        TLRPC$FileLocation tLRPC$FileLocation5;
        boolean z12;
        TLRPC$User tLRPC$User4;
        SharedPreferences sharedPreferences2;
        Bitmap bitmap;
        File file;
        Bitmap bitmap2;
        String str9;
        File file2;
        LongSparseArray longSparseArray8;
        Bitmap bitmap3;
        int i12;
        TLRPC$Chat tLRPC$Chat3;
        Integer num;
        String str10;
        String formatString;
        NotificationCompat.Action build;
        String str11;
        NotificationCompat.Action action;
        NotificationCompat.MessagingStyle messagingStyle;
        int i13;
        String str12;
        long j9;
        MessageObject messageObject2;
        NotificationCompat.MessagingStyle messagingStyle2;
        long j10;
        DialogKey dialogKey3;
        String str13;
        ArrayList<StoryNotification> arrayList6;
        LongSparseArray longSparseArray9;
        StringBuilder sb2;
        long j11;
        int i14;
        ArrayList arrayList7;
        Bitmap bitmap4;
        String str14;
        DialogKey dialogKey4;
        String str15;
        DialogKey dialogKey5;
        long j12;
        long j13;
        long j14;
        LongSparseArray longSparseArray10;
        Person person;
        String str16;
        StringBuilder sb3;
        int i15;
        String[] strArr;
        Person person2;
        File file3;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation6;
        LongSparseArray longSparseArray11;
        String str17;
        NotificationCompat.MessagingStyle messagingStyle3;
        int i16;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        ArrayList arrayList8;
        List messages;
        Uri uri2;
        File file4;
        final File file5;
        Context context;
        StringBuilder sb4;
        final Uri uriForFile;
        File file6;
        int i17;
        DialogKey dialogKey6;
        long j15;
        String str18;
        String str19;
        StringBuilder sb5;
        NotificationCompat.Action action2;
        long j16;
        ArrayList<StoryNotification> arrayList9;
        long j17;
        NotificationCompat.Builder category;
        String str20;
        TLRPC$User tLRPC$User5;
        int size3;
        int i18;
        int i19;
        String str21;
        ArrayList arrayList10;
        int i20;
        Intent intent;
        LongSparseArray longSparseArray12;
        DialogKey dialogKey7;
        String string2;
        int i21;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation7;
        Bitmap bitmap5;
        Bitmap decodeFile;
        String formatPluralString;
        String str22;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3;
        TLRPC$FileLocation tLRPC$FileLocation8;
        FileLog.d("showExtraNotifications pushMessages.size()=" + this.pushMessages.size());
        int i22 = Build.VERSION.SDK_INT;
        if (i22 >= 26) {
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
        if (i22 <= 19) {
            notificationManager.notify(this.notificationId, build2);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
                return;
            }
            return;
        }
        NotificationsController notificationsController3 = this;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList11 = new ArrayList();
        if (!notificationsController3.storyPushMessages.isEmpty()) {
            arrayList11.add(new DialogKey(0L, 0L, true));
        }
        LongSparseArray longSparseArray13 = new LongSparseArray();
        for (int i23 = 0; i23 < notificationsController3.pushMessages.size(); i23++) {
            MessageObject messageObject3 = notificationsController3.pushMessages.get(i23);
            long dialogId = messageObject3.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController3.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
            int i24 = notificationsSettings.getInt("dismissDate" + dialogId, 0);
            if (messageObject3.isStoryPush || messageObject3.messageOwner.date > i24) {
                ArrayList arrayList12 = (ArrayList) longSparseArray13.get(dialogId);
                if (arrayList12 == null) {
                    ArrayList arrayList13 = new ArrayList();
                    longSparseArray13.put(dialogId, arrayList13);
                    FileLog.d("showExtraNotifications: sortedDialogs += " + dialogId);
                    arrayList11.add(new DialogKey(dialogId, topicId, false));
                    arrayList12 = arrayList13;
                }
                arrayList12.add(messageObject3);
            } else {
                FileLog.d("showExtraNotifications: dialog " + dialogId + " is skipped, message date (" + messageObject3.messageOwner.date + " <= " + i24 + ")");
            }
        }
        LongSparseArray longSparseArray14 = new LongSparseArray();
        for (int i25 = 0; i25 < notificationsController3.wearNotificationsIds.size(); i25++) {
            longSparseArray14.put(notificationsController3.wearNotificationsIds.keyAt(i25), (Integer) notificationsController3.wearNotificationsIds.valueAt(i25));
        }
        notificationsController3.wearNotificationsIds.clear();
        ArrayList arrayList14 = new ArrayList();
        int i26 = Build.VERSION.SDK_INT;
        if (i26 > 27) {
            if (arrayList11.size() <= (notificationsController3.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i26 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                FileLog.d("showExtraNotifications: passcode=" + (SharedConfig.passcodeHash.length() <= 0) + " waitingForPasscode=" + z5 + " selfUserId=" + clientUserId + " useSummaryNotification=" + z4);
                longSparseArray = new LongSparseArray();
                size = arrayList11.size();
                i5 = 0;
                while (true) {
                    if (i5 < size) {
                        break;
                    }
                    int i27 = size;
                    if (arrayList14.size() >= 7) {
                        FileLog.d("showExtraNotifications: break from holders, count over 7");
                        break;
                    }
                    DialogKey dialogKey8 = (DialogKey) arrayList11.get(i5);
                    int i28 = i5;
                    ArrayList arrayList15 = arrayList14;
                    if (dialogKey8.story) {
                        ArrayList<StoryNotification> arrayList16 = new ArrayList<>();
                        if (notificationsController3.storyPushMessages.isEmpty()) {
                            FileLog.d("showExtraNotifications: [" + dialogKey8.dialogId + "] continue; story but storyPushMessages is empty");
                            j8 = clientUserId;
                            longSparseArray6 = longSparseArray;
                            z11 = z4;
                            z10 = z5;
                            longSparseArray7 = longSparseArray14;
                            longSparseArray4 = longSparseArray13;
                            arrayList3 = arrayList11;
                            sharedPreferences = notificationsSettings;
                            notification2 = build2;
                            notificationsController2 = notificationsController3;
                            i9 = i27;
                            i10 = i28;
                            arrayList5 = arrayList15;
                            i5 = i10 + 1;
                            arrayList14 = arrayList5;
                            size = i9;
                            clientUserId = j8;
                            notificationsSettings = sharedPreferences;
                            z5 = z10;
                            arrayList11 = arrayList3;
                            longSparseArray13 = longSparseArray4;
                            z4 = z11;
                            longSparseArray14 = longSparseArray7;
                            longSparseArray = longSparseArray6;
                            build2 = notification2;
                            i4 = -1;
                            notificationsController3 = notificationsController2;
                        } else {
                            notification = build2;
                            str4 = "showExtraNotifications: [";
                            long j18 = notificationsController3.storyPushMessages.get(0).dialogId;
                            int i29 = 0;
                            for (Iterator<Integer> it = notificationsController3.storyPushMessages.get(0).dateByIds.keySet().iterator(); it.hasNext(); it = it) {
                                i29 = Math.max(i29, it.next().intValue());
                            }
                            z6 = z4;
                            longSparseArray4 = longSparseArray13;
                            messageObject = null;
                            arrayList3 = arrayList11;
                            j3 = 0;
                            id = i29;
                            j4 = j18;
                            arrayList2 = arrayList16;
                        }
                    } else {
                        str4 = "showExtraNotifications: [";
                        notification = build2;
                        long j19 = dialogKey8.dialogId;
                        long j20 = dialogKey8.topicId;
                        ArrayList<StoryNotification> arrayList17 = (ArrayList) longSparseArray13.get(j19);
                        id = ((MessageObject) arrayList17.get(0)).getId();
                        arrayList2 = arrayList17;
                        z6 = z4;
                        messageObject = (MessageObject) arrayList17.get(0);
                        longSparseArray4 = longSparseArray13;
                        arrayList3 = arrayList11;
                        j3 = j20;
                        j4 = j19;
                    }
                    int i30 = (Integer) longSparseArray14.get(j4);
                    int i31 = id;
                    LongSparseArray longSparseArray15 = longSparseArray;
                    if (dialogKey8.story) {
                        i30 = 2147483646;
                        j5 = j3;
                    } else if (i30 == null) {
                        j5 = j3;
                        i30 = Integer.valueOf(((int) j4) + ((int) (j4 >> 32)));
                    } else {
                        j5 = j3;
                        longSparseArray14.remove(j4);
                    }
                    Integer num2 = i30;
                    int i32 = 0;
                    for (int i33 = 0; i33 < arrayList2.size(); i33++) {
                        if (i32 < ((MessageObject) arrayList2.get(i33)).messageOwner.date) {
                            i32 = ((MessageObject) arrayList2.get(i33)).messageOwner.date;
                        }
                    }
                    if (dialogKey8.story) {
                        TLRPC$User user = getMessagesController().getUser(Long.valueOf(j4));
                        longSparseArray5 = longSparseArray14;
                        if (notificationsController3.storyPushMessages.size() == 1) {
                            formatPluralString = user != null ? UserObject.getFirstName(user) : notificationsController3.storyPushMessages.get(0).localName;
                            i8 = i32;
                        } else {
                            i8 = i32;
                            formatPluralString = LocaleController.formatPluralString("Stories", notificationsController3.storyPushMessages.size(), new Object[0]);
                        }
                        if (user == null || (tLRPC$UserProfilePhoto3 = user.photo) == null || (tLRPC$FileLocation8 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            str22 = formatPluralString;
                        } else {
                            str22 = formatPluralString;
                            if (tLRPC$FileLocation8.volume_id != 0 && tLRPC$FileLocation8.local_id != 0) {
                                tLRPC$User2 = user;
                                dialogKey = dialogKey8;
                                j6 = clientUserId;
                                tLRPC$FileLocation = tLRPC$FileLocation8;
                                string = str22;
                                tLRPC$Chat = null;
                                z7 = false;
                                z8 = false;
                                z9 = false;
                                arrayList4 = arrayList2;
                                j7 = j5;
                            }
                        }
                        tLRPC$User2 = user;
                        dialogKey = dialogKey8;
                        j6 = clientUserId;
                        string = str22;
                        tLRPC$Chat = null;
                        z7 = false;
                        z8 = false;
                        z9 = false;
                        tLRPC$FileLocation = null;
                        arrayList4 = arrayList2;
                        j7 = j5;
                    } else {
                        i8 = i32;
                        longSparseArray5 = longSparseArray14;
                        if (DialogObject.isEncryptedDialog(j4)) {
                            dialogKey = dialogKey8;
                            j6 = clientUserId;
                            arrayList4 = arrayList2;
                            j7 = j5;
                            if (j4 != globalSecretChatId) {
                                int encryptedChatId = DialogObject.getEncryptedChatId(j4);
                                TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                                if (encryptedChat == null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        str5 = "not found secret chat to show dialog notification " + encryptedChatId;
                                        FileLog.w(str5);
                                    }
                                    z10 = z5;
                                    sharedPreferences = notificationsSettings;
                                    notificationsController2 = notificationsController3;
                                    i9 = i27;
                                    i10 = i28;
                                    arrayList5 = arrayList15;
                                    notification2 = notification;
                                    z11 = z6;
                                    longSparseArray6 = longSparseArray15;
                                    longSparseArray7 = longSparseArray5;
                                    j8 = j6;
                                } else {
                                    tLRPC$User = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                                    if (tLRPC$User == null) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            str5 = "not found secret chat user to show dialog notification " + encryptedChat.user_id;
                                            FileLog.w(str5);
                                        }
                                        z10 = z5;
                                        sharedPreferences = notificationsSettings;
                                        notificationsController2 = notificationsController3;
                                        i9 = i27;
                                        i10 = i28;
                                        arrayList5 = arrayList15;
                                        notification2 = notification;
                                        z11 = z6;
                                        longSparseArray6 = longSparseArray15;
                                        longSparseArray7 = longSparseArray5;
                                        j8 = j6;
                                    }
                                }
                                i5 = i10 + 1;
                                arrayList14 = arrayList5;
                                size = i9;
                                clientUserId = j8;
                                notificationsSettings = sharedPreferences;
                                z5 = z10;
                                arrayList11 = arrayList3;
                                longSparseArray13 = longSparseArray4;
                                z4 = z11;
                                longSparseArray14 = longSparseArray7;
                                longSparseArray = longSparseArray6;
                                build2 = notification2;
                                i4 = -1;
                                notificationsController3 = notificationsController2;
                            } else {
                                tLRPC$User = null;
                            }
                            tLRPC$User2 = tLRPC$User;
                            string = LocaleController.getString(R.string.SecretChatName);
                            tLRPC$Chat = null;
                            z7 = false;
                            z8 = false;
                            z9 = false;
                            tLRPC$FileLocation = null;
                        } else {
                            z7 = (messageObject == null || messageObject.isReactionPush || messageObject.isStoryReactionPush || j4 == 777000) ? false : true;
                            if (DialogObject.isUserDialog(j4)) {
                                tLRPC$User3 = getMessagesController().getUser(Long.valueOf(j4));
                                if (tLRPC$User3 != null) {
                                    userName = UserObject.getUserName(tLRPC$User3);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = tLRPC$User3.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        arrayList4 = arrayList2;
                                        str8 = userName;
                                        if (tLRPC$FileLocation3.volume_id != 0 && tLRPC$FileLocation3.local_id != 0) {
                                            tLRPC$FileLocation4 = tLRPC$FileLocation3;
                                            if (UserObject.isReplyUser(j4)) {
                                                i11 = R.string.RepliesTitle;
                                            } else if (j4 == clientUserId) {
                                                i11 = R.string.MessageScheduledReminderNotification;
                                            } else {
                                                dialogKey = dialogKey8;
                                                j6 = clientUserId;
                                                tLRPC$FileLocation = tLRPC$FileLocation4;
                                                j7 = j5;
                                                str6 = str8;
                                                tLRPC$Chat2 = null;
                                                z8 = false;
                                                z9 = false;
                                                tLRPC$User2 = tLRPC$User3;
                                                String str23 = str6;
                                                tLRPC$Chat = tLRPC$Chat2;
                                                string = str23;
                                            }
                                            tLRPC$User2 = tLRPC$User3;
                                            dialogKey = dialogKey8;
                                            j6 = clientUserId;
                                            string = LocaleController.getString(i11);
                                            tLRPC$FileLocation = tLRPC$FileLocation4;
                                            j7 = j5;
                                            tLRPC$Chat = null;
                                            z8 = false;
                                            z9 = false;
                                        }
                                        tLRPC$FileLocation4 = null;
                                        if (UserObject.isReplyUser(j4)) {
                                        }
                                        tLRPC$User2 = tLRPC$User3;
                                        dialogKey = dialogKey8;
                                        j6 = clientUserId;
                                        string = LocaleController.getString(i11);
                                        tLRPC$FileLocation = tLRPC$FileLocation4;
                                        j7 = j5;
                                        tLRPC$Chat = null;
                                        z8 = false;
                                        z9 = false;
                                    }
                                } else if (messageObject.isFcmMessage()) {
                                    userName = messageObject.localName;
                                } else {
                                    if (BuildVars.LOGS_ENABLED) {
                                        sb = new StringBuilder();
                                        str7 = "not found user to show dialog notification ";
                                        sb.append(str7);
                                        sb.append(j4);
                                        FileLog.w(sb.toString());
                                    }
                                    j8 = clientUserId;
                                    z10 = z5;
                                    sharedPreferences = notificationsSettings;
                                    notificationsController2 = notificationsController3;
                                    i9 = i27;
                                    i10 = i28;
                                    arrayList5 = arrayList15;
                                    notification2 = notification;
                                    z11 = z6;
                                    longSparseArray6 = longSparseArray15;
                                    longSparseArray7 = longSparseArray5;
                                    i5 = i10 + 1;
                                    arrayList14 = arrayList5;
                                    size = i9;
                                    clientUserId = j8;
                                    notificationsSettings = sharedPreferences;
                                    z5 = z10;
                                    arrayList11 = arrayList3;
                                    longSparseArray13 = longSparseArray4;
                                    z4 = z11;
                                    longSparseArray14 = longSparseArray7;
                                    longSparseArray = longSparseArray6;
                                    build2 = notification2;
                                    i4 = -1;
                                    notificationsController3 = notificationsController2;
                                }
                                arrayList4 = arrayList2;
                                str8 = userName;
                                tLRPC$FileLocation4 = null;
                                if (UserObject.isReplyUser(j4)) {
                                }
                                tLRPC$User2 = tLRPC$User3;
                                dialogKey = dialogKey8;
                                j6 = clientUserId;
                                string = LocaleController.getString(i11);
                                tLRPC$FileLocation = tLRPC$FileLocation4;
                                j7 = j5;
                                tLRPC$Chat = null;
                                z8 = false;
                                z9 = false;
                            } else {
                                arrayList4 = arrayList2;
                                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j4));
                                if (chat != null) {
                                    boolean z13 = chat.megagroup;
                                    boolean z14 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str24 = chat.title;
                                    z8 = z13;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) == null) {
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey8;
                                    } else {
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey8;
                                        if (tLRPC$FileLocation2.volume_id != 0) {
                                        }
                                    }
                                    tLRPC$FileLocation2 = null;
                                    if (j5 != 0) {
                                        dialogKey = dialogKey2;
                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                        z9 = z14;
                                        j7 = j5;
                                        if (getMessagesController().getTopicsController().findTopic(chat.id, j7) != null) {
                                            string = findTopic.title + " in " + str24;
                                            if (z7) {
                                                str6 = string;
                                                tLRPC$Chat2 = chat;
                                                tLRPC$User3 = null;
                                                tLRPC$User2 = tLRPC$User3;
                                                String str232 = str6;
                                                tLRPC$Chat = tLRPC$Chat2;
                                                string = str232;
                                            } else {
                                                z7 = ChatObject.canSendPlain(chat);
                                                tLRPC$Chat = chat;
                                                tLRPC$User2 = null;
                                            }
                                        }
                                    } else {
                                        dialogKey = dialogKey2;
                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                        z9 = z14;
                                        j7 = j5;
                                    }
                                    string = str24;
                                    if (z7) {
                                    }
                                } else if (messageObject.isFcmMessage()) {
                                    boolean isSupergroup = messageObject.isSupergroup();
                                    dialogKey = dialogKey8;
                                    j6 = clientUserId;
                                    string = messageObject.localName;
                                    z9 = messageObject.localChannel;
                                    z8 = isSupergroup;
                                    j7 = j5;
                                    z7 = false;
                                    tLRPC$User2 = null;
                                    tLRPC$FileLocation = null;
                                    tLRPC$Chat = chat;
                                } else {
                                    if (BuildVars.LOGS_ENABLED) {
                                        sb = new StringBuilder();
                                        str7 = "not found chat to show dialog notification ";
                                        sb.append(str7);
                                        sb.append(j4);
                                        FileLog.w(sb.toString());
                                    }
                                    j8 = clientUserId;
                                    z10 = z5;
                                    sharedPreferences = notificationsSettings;
                                    notificationsController2 = notificationsController3;
                                    i9 = i27;
                                    i10 = i28;
                                    arrayList5 = arrayList15;
                                    notification2 = notification;
                                    z11 = z6;
                                    longSparseArray6 = longSparseArray15;
                                    longSparseArray7 = longSparseArray5;
                                    i5 = i10 + 1;
                                    arrayList14 = arrayList5;
                                    size = i9;
                                    clientUserId = j8;
                                    notificationsSettings = sharedPreferences;
                                    z5 = z10;
                                    arrayList11 = arrayList3;
                                    longSparseArray13 = longSparseArray4;
                                    z4 = z11;
                                    longSparseArray14 = longSparseArray7;
                                    longSparseArray = longSparseArray6;
                                    build2 = notification2;
                                    i4 = -1;
                                    notificationsController3 = notificationsController2;
                                }
                            }
                        }
                    }
                    if (messageObject != null && messageObject.isStoryReactionPush && !notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                        string = LocaleController.getString(R.string.NotificationHiddenChatName);
                        z7 = false;
                        tLRPC$FileLocation = null;
                    }
                    if (z5) {
                        string = LocaleController.getString(DialogObject.isChatDialog(j4) ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName);
                        tLRPC$FileLocation5 = null;
                        z7 = false;
                    } else {
                        tLRPC$FileLocation5 = tLRPC$FileLocation;
                    }
                    if (tLRPC$FileLocation5 != null) {
                        sharedPreferences2 = notificationsSettings;
                        File pathToAttach = getFileLoader().getPathToAttach(tLRPC$FileLocation5, true);
                        tLRPC$User4 = tLRPC$User2;
                        if (Build.VERSION.SDK_INT < 28) {
                            z12 = z5;
                            bitmap5 = null;
                            BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation5, null, "50_50");
                            if (imageFromMemory != null) {
                                decodeFile = imageFromMemory.getBitmap();
                            } else {
                                try {
                                    if (pathToAttach.exists()) {
                                        float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                        decodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options);
                                    }
                                } catch (Throwable unused) {
                                }
                            }
                            file = pathToAttach;
                            bitmap = decodeFile;
                        } else {
                            z12 = z5;
                            bitmap5 = null;
                        }
                        file = pathToAttach;
                        bitmap = bitmap5;
                    } else {
                        z12 = z5;
                        tLRPC$User4 = tLRPC$User2;
                        sharedPreferences2 = notificationsSettings;
                        bitmap = null;
                        file = null;
                    }
                    if (tLRPC$Chat != null) {
                        Person.Builder name = new Person.Builder().setName(string);
                        if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                            loadRoundAvatar(file, name);
                        }
                        bitmap2 = bitmap;
                        str9 = "Stories";
                        file2 = file;
                        longSparseArray8 = longSparseArray15;
                        longSparseArray8.put(-tLRPC$Chat.id, name.build());
                    } else {
                        bitmap2 = bitmap;
                        str9 = "Stories";
                        file2 = file;
                        longSparseArray8 = longSparseArray15;
                    }
                    String str25 = str9;
                    if ((z9 && !z8) || !z7 || SharedConfig.isWaitingForPasscodeEnter || j6 == j4 || UserObject.isReplyUser(j4)) {
                        str10 = "max_id";
                        num = num2;
                        bitmap3 = bitmap2;
                        i12 = i31;
                        tLRPC$Chat3 = tLRPC$Chat;
                        build = null;
                    } else {
                        bitmap3 = bitmap2;
                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                        intent2.putExtra("dialog_id", j4);
                        i12 = i31;
                        intent2.putExtra("max_id", i12);
                        intent2.putExtra("topic_id", j7);
                        intent2.putExtra("currentAccount", notificationsController3.currentAccount);
                        tLRPC$Chat3 = tLRPC$Chat;
                        num = num2;
                        PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent2, 167772160);
                        RemoteInput build3 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString(R.string.Reply)).build();
                        if (DialogObject.isChatDialog(j4)) {
                            str10 = "max_id";
                            formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, string);
                        } else {
                            str10 = "max_id";
                            formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, string);
                        }
                        build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build3).setShowsUserInterface(false).build();
                    }
                    Integer num3 = (Integer) notificationsController3.pushDialogs.get(j4);
                    if (num3 == null) {
                        num3 = 0;
                    }
                    DialogKey dialogKey9 = dialogKey;
                    int size4 = dialogKey9.story ? notificationsController3.storyPushMessages.size() : Math.max(num3.intValue(), arrayList4.size());
                    String format = (size4 <= 1 || Build.VERSION.SDK_INT >= 28) ? string : String.format("%1$s (%2$d)", string, Integer.valueOf(size4));
                    long j21 = j6;
                    long j22 = j4;
                    Person person3 = (Person) longSparseArray8.get(j21);
                    int i34 = i12;
                    if (Build.VERSION.SDK_INT >= 28 && person3 == null) {
                        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j21));
                        if (user2 == null) {
                            user2 = getUserConfig().getCurrentUser();
                        }
                        if (user2 != null) {
                            try {
                                tLRPC$UserProfilePhoto2 = user2.photo;
                            } catch (Throwable th) {
                                th = th;
                                str11 = string;
                                action = build;
                            }
                            if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation7 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                str11 = string;
                                action = build;
                                try {
                                    if (tLRPC$FileLocation7.volume_id != 0 && tLRPC$FileLocation7.local_id != 0) {
                                        Person.Builder name2 = new Person.Builder().setName(LocaleController.getString(R.string.FromYou));
                                        loadRoundAvatar(getFileLoader().getPathToAttach(user2.photo.photo_small, true), name2);
                                        person3 = name2.build();
                                        longSparseArray8.put(j21, person3);
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    FileLog.e(th);
                                    messagingStyle = (person3 == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person3) : new NotificationCompat.MessagingStyle("");
                                    i13 = Build.VERSION.SDK_INT;
                                    if (i13 >= 28) {
                                    }
                                    messagingStyle.setConversationTitle(format);
                                    messagingStyle.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j22)) || UserObject.isReplyUser(j22));
                                    StringBuilder sb6 = new StringBuilder();
                                    String[] strArr2 = new String[1];
                                    boolean[] zArr = new boolean[1];
                                    if (dialogKey9.story) {
                                    }
                                    Intent intent3 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                    intent3.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    intent3.setFlags(ConnectionsManager.FileTypeFile);
                                    intent3.addCategory("android.intent.category.LAUNCHER");
                                    MessageObject messageObject4 = messageObject2;
                                    if (messageObject2 == null) {
                                    }
                                    dialogKey6 = dialogKey3;
                                    if (dialogKey6.story) {
                                    }
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("show extra notifications chatId ");
                                    sb7.append(j11);
                                    sb7.append(" topicId ");
                                    j15 = j10;
                                    sb7.append(j15);
                                    FileLog.d(sb7.toString());
                                    if (j15 != 0) {
                                    }
                                    String str26 = str12;
                                    intent3.putExtra(str26, notificationsController3.currentAccount);
                                    PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent3, 1140850688);
                                    NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                    NotificationCompat.Action action3 = action;
                                    if (action != null) {
                                    }
                                    int i35 = i14;
                                    Intent intent4 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                    intent4.addFlags(32);
                                    intent4.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                    intent4.putExtra("dialog_id", j11);
                                    intent4.putExtra(str10, i34);
                                    intent4.putExtra(str26, notificationsController3.currentAccount);
                                    ArrayList arrayList18 = arrayList7;
                                    Bitmap bitmap6 = bitmap4;
                                    NotificationCompat.Action build4 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                    if (DialogObject.isEncryptedDialog(j11)) {
                                    }
                                    i5 = i10 + 1;
                                    arrayList14 = arrayList5;
                                    size = i9;
                                    clientUserId = j8;
                                    notificationsSettings = sharedPreferences;
                                    z5 = z10;
                                    arrayList11 = arrayList3;
                                    longSparseArray13 = longSparseArray4;
                                    z4 = z11;
                                    longSparseArray14 = longSparseArray7;
                                    longSparseArray = longSparseArray6;
                                    build2 = notification2;
                                    i4 = -1;
                                    notificationsController3 = notificationsController2;
                                }
                                messagingStyle = (person3 == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person3) : new NotificationCompat.MessagingStyle("");
                                i13 = Build.VERSION.SDK_INT;
                                if (i13 >= 28 || ((DialogObject.isChatDialog(j22) && !z9) || UserObject.isReplyUser(j22))) {
                                    messagingStyle.setConversationTitle(format);
                                }
                                messagingStyle.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j22)) || UserObject.isReplyUser(j22));
                                StringBuilder sb62 = new StringBuilder();
                                String[] strArr22 = new String[1];
                                boolean[] zArr2 = new boolean[1];
                                if (dialogKey9.story) {
                                    ArrayList<String> arrayList19 = new ArrayList<>();
                                    ArrayList<Object> arrayList20 = new ArrayList<>();
                                    Pair<Integer, Boolean> parseStoryPushes = notificationsController3.parseStoryPushes(arrayList19, arrayList20);
                                    int intValue = ((Integer) parseStoryPushes.first).intValue();
                                    boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                    str12 = "currentAccount";
                                    if (booleanValue) {
                                        string2 = LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]);
                                    } else if (arrayList19.isEmpty()) {
                                        FileLog.d(str4 + dialogKey9.dialogId + "] continue; story but names is empty");
                                        longSparseArray6 = longSparseArray8;
                                        j8 = j21;
                                        notificationsController2 = notificationsController3;
                                        i9 = i27;
                                        i10 = i28;
                                        arrayList5 = arrayList15;
                                        notification2 = notification;
                                        z11 = z6;
                                        sharedPreferences = sharedPreferences2;
                                        longSparseArray7 = longSparseArray5;
                                        z10 = z12;
                                        i5 = i10 + 1;
                                        arrayList14 = arrayList5;
                                        size = i9;
                                        clientUserId = j8;
                                        notificationsSettings = sharedPreferences;
                                        z5 = z10;
                                        arrayList11 = arrayList3;
                                        longSparseArray13 = longSparseArray4;
                                        z4 = z11;
                                        longSparseArray14 = longSparseArray7;
                                        longSparseArray = longSparseArray6;
                                        build2 = notification2;
                                        i4 = -1;
                                        notificationsController3 = notificationsController2;
                                    } else if (arrayList19.size() != 1) {
                                        messageObject2 = messageObject;
                                        if (arrayList19.size() == 2) {
                                            longSparseArray12 = longSparseArray8;
                                            sb62.append(LocaleController.formatString(R.string.StoryNotification2, arrayList19.get(0), arrayList19.get(1)));
                                            dialogKey7 = dialogKey9;
                                            j9 = j21;
                                            long j23 = Long.MAX_VALUE;
                                            while (i21 < notificationsController3.storyPushMessages.size()) {
                                            }
                                            messagingStyle.setGroupConversation(false);
                                            if (arrayList19.size() == 1) {
                                            }
                                            messagingStyle.addMessage(sb62, j23, new Person.Builder().setName(r0).build());
                                            if (booleanValue) {
                                            }
                                            sb2 = sb62;
                                            str14 = r0;
                                        } else {
                                            longSparseArray12 = longSparseArray8;
                                            if (arrayList19.size() == 3 && notificationsController3.storyPushMessages.size() == 3) {
                                                j9 = j21;
                                                sb62.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController3.cutLastName(arrayList19.get(0)), notificationsController3.cutLastName(arrayList19.get(1)), notificationsController3.cutLastName(arrayList19.get(2))));
                                                dialogKey7 = dialogKey9;
                                            } else {
                                                j9 = j21;
                                                dialogKey7 = dialogKey9;
                                                sb62.append(LocaleController.formatPluralString("StoryNotification4", notificationsController3.storyPushMessages.size() - 2, notificationsController3.cutLastName(arrayList19.get(0)), notificationsController3.cutLastName(arrayList19.get(1))));
                                            }
                                            long j232 = Long.MAX_VALUE;
                                            while (i21 < notificationsController3.storyPushMessages.size()) {
                                            }
                                            messagingStyle.setGroupConversation(false);
                                            if (arrayList19.size() == 1) {
                                            }
                                            messagingStyle.addMessage(sb62, j232, new Person.Builder().setName(r0).build());
                                            if (booleanValue) {
                                            }
                                            sb2 = sb62;
                                            str14 = r0;
                                        }
                                    } else if (intValue == 1) {
                                        string2 = LocaleController.getString("StoryNotificationSingle");
                                    } else {
                                        messageObject2 = messageObject;
                                        sb62.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList19.get(0)));
                                        dialogKey7 = dialogKey9;
                                        longSparseArray12 = longSparseArray8;
                                        j9 = j21;
                                        long j2322 = Long.MAX_VALUE;
                                        for (i21 = 0; i21 < notificationsController3.storyPushMessages.size(); i21++) {
                                            j2322 = Math.min(notificationsController3.storyPushMessages.get(i21).date, j2322);
                                        }
                                        messagingStyle.setGroupConversation(false);
                                        String formatPluralString2 = (arrayList19.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str25, intValue, new Object[0]) : arrayList19.get(0);
                                        messagingStyle.addMessage(sb62, j2322, new Person.Builder().setName(formatPluralString2).build());
                                        if (booleanValue) {
                                            bitmap4 = loadMultipleAvatars(arrayList20);
                                            messagingStyle2 = messagingStyle;
                                            j10 = j7;
                                            dialogKey3 = dialogKey7;
                                            str13 = str3;
                                            arrayList6 = arrayList4;
                                            j11 = j22;
                                            longSparseArray9 = longSparseArray12;
                                            i14 = 0;
                                            arrayList7 = null;
                                        } else {
                                            messagingStyle2 = messagingStyle;
                                            j10 = j7;
                                            dialogKey3 = dialogKey7;
                                            str13 = str3;
                                            arrayList6 = arrayList4;
                                            j11 = j22;
                                            longSparseArray9 = longSparseArray12;
                                            i14 = 0;
                                            arrayList7 = null;
                                            bitmap4 = null;
                                        }
                                        sb2 = sb62;
                                        str14 = formatPluralString2;
                                    }
                                    sb62.append(string2);
                                    dialogKey7 = dialogKey9;
                                    messageObject2 = messageObject;
                                    longSparseArray12 = longSparseArray8;
                                    j9 = j21;
                                    long j23222 = Long.MAX_VALUE;
                                    while (i21 < notificationsController3.storyPushMessages.size()) {
                                    }
                                    messagingStyle.setGroupConversation(false);
                                    if (arrayList19.size() == 1) {
                                    }
                                    messagingStyle.addMessage(sb62, j23222, new Person.Builder().setName(formatPluralString2).build());
                                    if (booleanValue) {
                                    }
                                    sb2 = sb62;
                                    str14 = formatPluralString2;
                                } else {
                                    DialogKey dialogKey10 = dialogKey9;
                                    str12 = "currentAccount";
                                    LongSparseArray longSparseArray16 = longSparseArray8;
                                    j9 = j21;
                                    String str27 = str4;
                                    messageObject2 = messageObject;
                                    int size5 = arrayList4.size() - 1;
                                    int i36 = 0;
                                    ArrayList arrayList21 = null;
                                    while (size5 >= 0) {
                                        ArrayList<StoryNotification> arrayList22 = arrayList4;
                                        MessageObject messageObject5 = (MessageObject) arrayList22.get(size5);
                                        int i37 = i36;
                                        ArrayList arrayList23 = arrayList21;
                                        long topicId2 = MessageObject.getTopicId(notificationsController3.currentAccount, messageObject5.messageOwner, getMessagesController().isForum(messageObject5));
                                        if (j7 != topicId2) {
                                            StringBuilder sb8 = new StringBuilder();
                                            sb8.append(str27);
                                            dialogKey4 = dialogKey10;
                                            sb8.append(dialogKey4.dialogId);
                                            sb8.append("] continue; topic id is not equal: topicId=");
                                            sb8.append(j7);
                                            sb8.append(" messageTopicId=");
                                            sb8.append(topicId2);
                                            sb8.append("; selfId=");
                                            sb8.append(getUserConfig().getClientUserId());
                                            FileLog.d(sb8.toString());
                                        } else {
                                            dialogKey4 = dialogKey10;
                                            String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject5, strArr22, zArr2);
                                            if (j22 == j9) {
                                                strArr22[0] = str11;
                                            } else if (DialogObject.isChatDialog(j22) && messageObject5.messageOwner.from_scheduled) {
                                                strArr22[0] = LocaleController.getString(R.string.NotificationMessageScheduledName);
                                            }
                                            if (shortStringForMessage != null) {
                                                if (sb62.length() > 0) {
                                                    sb62.append("\n\n");
                                                }
                                                if (j22 != j9 && messageObject5.messageOwner.from_scheduled && DialogObject.isUserDialog(j22)) {
                                                    str15 = str27;
                                                    shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString(R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                    sb62.append(shortStringForMessage);
                                                    dialogKey5 = dialogKey4;
                                                } else {
                                                    str15 = str27;
                                                    String str28 = strArr22[0];
                                                    dialogKey5 = dialogKey4;
                                                    if (str28 != null) {
                                                        sb62.append(String.format("%1$s: %2$s", str28, shortStringForMessage));
                                                    } else {
                                                        sb62.append(shortStringForMessage);
                                                    }
                                                }
                                                String str29 = shortStringForMessage;
                                                NotificationCompat.MessagingStyle messagingStyle4 = messagingStyle;
                                                int i38 = size5;
                                                j12 = j22;
                                                if (!DialogObject.isUserDialog(j22)) {
                                                    if (z9) {
                                                        j13 = -j12;
                                                    } else if (DialogObject.isChatDialog(j12)) {
                                                        j13 = messageObject5.getSenderId();
                                                    }
                                                    j14 = j7;
                                                    longSparseArray10 = longSparseArray16;
                                                    person = (Person) longSparseArray10.get(j13 + (j7 << 16));
                                                    str16 = strArr22[0];
                                                    if (str16 == null) {
                                                        if (z12) {
                                                            if (DialogObject.isChatDialog(j12)) {
                                                                if (!z9) {
                                                                    i17 = R.string.NotificationHiddenChatUserName;
                                                                } else if (Build.VERSION.SDK_INT > 27) {
                                                                    i17 = R.string.NotificationHiddenChatName;
                                                                }
                                                                str16 = LocaleController.getString(i17);
                                                            } else if (Build.VERSION.SDK_INT > 27) {
                                                                i17 = R.string.NotificationHiddenName;
                                                                str16 = LocaleController.getString(i17);
                                                            }
                                                        }
                                                        str16 = "";
                                                    }
                                                    if (person == null && TextUtils.equals(person.getName(), str16)) {
                                                        person2 = person;
                                                        sb3 = sb62;
                                                        i15 = i38;
                                                        strArr = strArr22;
                                                    } else {
                                                        Person.Builder name3 = new Person.Builder().setName(str16);
                                                        if (zArr2[0] || DialogObject.isEncryptedDialog(j12) || Build.VERSION.SDK_INT < 28) {
                                                            sb3 = sb62;
                                                            i15 = i38;
                                                            strArr = strArr22;
                                                        } else {
                                                            if (DialogObject.isUserDialog(j12) || z9) {
                                                                sb3 = sb62;
                                                                i15 = i38;
                                                                strArr = strArr22;
                                                                file3 = file2;
                                                            } else {
                                                                long senderId = messageObject5.getSenderId();
                                                                strArr = strArr22;
                                                                sb3 = sb62;
                                                                TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(senderId));
                                                                if (user3 == null && (user3 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                    getMessagesController().putUser(user3, true);
                                                                }
                                                                if (user3 == null || (tLRPC$UserProfilePhoto = user3.photo) == null || (tLRPC$FileLocation6 = tLRPC$UserProfilePhoto.photo_small) == null) {
                                                                    i15 = i38;
                                                                } else {
                                                                    i15 = i38;
                                                                    if (tLRPC$FileLocation6.volume_id != 0 && tLRPC$FileLocation6.local_id != 0) {
                                                                        file3 = getFileLoader().getPathToAttach(user3.photo.photo_small, true);
                                                                    }
                                                                }
                                                                file3 = null;
                                                            }
                                                            loadRoundAvatar(file3, name3);
                                                        }
                                                        Person build5 = name3.build();
                                                        longSparseArray10.put(j13, build5);
                                                        person2 = build5;
                                                    }
                                                    if (DialogObject.isEncryptedDialog(j12)) {
                                                        if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z12 || messageObject5.isSecretMedia() || !(messageObject5.type == 1 || messageObject5.isSticker())) {
                                                            longSparseArray11 = longSparseArray10;
                                                            str17 = str3;
                                                            messagingStyle3 = messagingStyle4;
                                                            i16 = i15;
                                                        } else {
                                                            File pathToMessage = getFileLoader().getPathToMessage(messageObject5.messageOwner);
                                                            if (pathToMessage.exists() && messageObject5.hasMediaSpoilers()) {
                                                                file5 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                if (file5.exists()) {
                                                                    file6 = pathToMessage;
                                                                    longSparseArray11 = longSparseArray10;
                                                                } else {
                                                                    try {
                                                                        Bitmap decodeFile2 = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                        Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile2);
                                                                        decodeFile2.recycle();
                                                                        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile2.getWidth(), decodeFile2.getHeight(), true);
                                                                        Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                        stackBlurBitmapMax.recycle();
                                                                        Canvas canvas = new Canvas(createScaledBitmap);
                                                                        longSparseArray11 = longSparseArray10;
                                                                        try {
                                                                            notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i4) * 0.325f)));
                                                                            file6 = pathToMessage;
                                                                        } catch (Exception e) {
                                                                            e = e;
                                                                            file6 = pathToMessage;
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
                                                                            NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str29, messageObject5.messageOwner.date * 1000, person2);
                                                                            String str30 = !messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                                            if (file4.exists()) {
                                                                            }
                                                                        }
                                                                    } catch (Exception e3) {
                                                                        e = e3;
                                                                        file6 = pathToMessage;
                                                                        longSparseArray11 = longSparseArray10;
                                                                    }
                                                                }
                                                                file4 = file6;
                                                            } else {
                                                                longSparseArray11 = longSparseArray10;
                                                                file4 = pathToMessage;
                                                                file5 = null;
                                                            }
                                                            NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str29, messageObject5.messageOwner.date * 1000, person2);
                                                            String str302 = !messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                            if (file4.exists()) {
                                                                str17 = str3;
                                                                if (getFileLoader().isLoadingFile(file4.getName())) {
                                                                    Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                    StringBuilder sb9 = new StringBuilder();
                                                                    i16 = i15;
                                                                    sb9.append(notificationsController3.currentAccount);
                                                                    sb9.append("");
                                                                    uriForFile = appendPath.appendPath(sb9.toString()).appendPath(file4.getName()).appendQueryParameter("final_path", file4.getAbsolutePath()).build();
                                                                    if (uriForFile == null) {
                                                                    }
                                                                }
                                                                i16 = i15;
                                                                uriForFile = null;
                                                                if (uriForFile == null) {
                                                                }
                                                            } else {
                                                                try {
                                                                    context = ApplicationLoader.applicationContext;
                                                                    sb4 = new StringBuilder();
                                                                    sb4.append(ApplicationLoader.getApplicationId());
                                                                    str17 = str3;
                                                                } catch (Exception e4) {
                                                                    e = e4;
                                                                    str17 = str3;
                                                                }
                                                                try {
                                                                    sb4.append(str17);
                                                                    uriForFile = FileProvider.getUriForFile(context, sb4.toString(), file4);
                                                                    i16 = i15;
                                                                } catch (Exception e5) {
                                                                    e = e5;
                                                                    FileLog.e(e);
                                                                    i16 = i15;
                                                                    uriForFile = null;
                                                                    if (uriForFile == null) {
                                                                    }
                                                                }
                                                                if (uriForFile == null) {
                                                                    message2.setData(str302, uriForFile);
                                                                    messagingStyle3 = messagingStyle4;
                                                                    messagingStyle3.addMessage(message2);
                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda55
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            NotificationsController.lambda$showExtraNotifications$42(uriForFile, file5);
                                                                        }
                                                                    }, 20000L);
                                                                    if (!TextUtils.isEmpty(messageObject5.caption)) {
                                                                        messagingStyle3.addMessage(messageObject5.caption, messageObject5.messageOwner.date * 1000, person2);
                                                                    }
                                                                    if (zArr2[0] && !z12 && messageObject5.isVoice()) {
                                                                        messages = messagingStyle3.getMessages();
                                                                        if (!messages.isEmpty()) {
                                                                            File pathToMessage2 = getFileLoader().getPathToMessage(messageObject5.messageOwner);
                                                                            if (Build.VERSION.SDK_INT >= 24) {
                                                                                try {
                                                                                    uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + str17, pathToMessage2);
                                                                                } catch (Exception unused2) {
                                                                                    uri2 = null;
                                                                                }
                                                                            } else {
                                                                                uri2 = Uri.fromFile(pathToMessage2);
                                                                            }
                                                                            if (uri2 != null) {
                                                                                ((NotificationCompat.MessagingStyle.Message) messages.get(messages.size() - 1)).setData("audio/ogg", uri2);
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    messagingStyle3 = messagingStyle4;
                                                                }
                                                            }
                                                        }
                                                        messagingStyle3.addMessage(str29, messageObject5.messageOwner.date * 1000, person2);
                                                        if (zArr2[0]) {
                                                            messages = messagingStyle3.getMessages();
                                                            if (!messages.isEmpty()) {
                                                            }
                                                        }
                                                    } else {
                                                        longSparseArray11 = longSparseArray10;
                                                        str17 = str3;
                                                        messagingStyle3 = messagingStyle4;
                                                        i16 = i15;
                                                        messagingStyle3.addMessage(str29, messageObject5.messageOwner.date * 1000, person2);
                                                    }
                                                    if (j12 == 777000 && (tLRPC$ReplyMarkup = messageObject5.messageOwner.reply_markup) != null) {
                                                        arrayList8 = tLRPC$ReplyMarkup.rows;
                                                        i36 = messageObject5.getId();
                                                        size5 = i16 - 1;
                                                        messagingStyle = messagingStyle3;
                                                        str3 = str17;
                                                        str27 = str15;
                                                        strArr22 = strArr;
                                                        sb62 = sb3;
                                                        arrayList4 = arrayList22;
                                                        dialogKey10 = dialogKey5;
                                                        j7 = j14;
                                                        longSparseArray16 = longSparseArray11;
                                                        i4 = -1;
                                                        j22 = j12;
                                                        arrayList21 = arrayList8;
                                                    }
                                                    i36 = i37;
                                                    arrayList8 = arrayList23;
                                                    size5 = i16 - 1;
                                                    messagingStyle = messagingStyle3;
                                                    str3 = str17;
                                                    str27 = str15;
                                                    strArr22 = strArr;
                                                    sb62 = sb3;
                                                    arrayList4 = arrayList22;
                                                    dialogKey10 = dialogKey5;
                                                    j7 = j14;
                                                    longSparseArray16 = longSparseArray11;
                                                    i4 = -1;
                                                    j22 = j12;
                                                    arrayList21 = arrayList8;
                                                }
                                                j13 = j12;
                                                j14 = j7;
                                                longSparseArray10 = longSparseArray16;
                                                person = (Person) longSparseArray10.get(j13 + (j7 << 16));
                                                str16 = strArr22[0];
                                                if (str16 == null) {
                                                }
                                                if (person == null) {
                                                }
                                                Person.Builder name32 = new Person.Builder().setName(str16);
                                                if (zArr2[0]) {
                                                }
                                                sb3 = sb62;
                                                i15 = i38;
                                                strArr = strArr22;
                                                Person build52 = name32.build();
                                                longSparseArray10.put(j13, build52);
                                                person2 = build52;
                                                if (DialogObject.isEncryptedDialog(j12)) {
                                                }
                                                if (j12 == 777000) {
                                                    arrayList8 = tLRPC$ReplyMarkup.rows;
                                                    i36 = messageObject5.getId();
                                                    size5 = i16 - 1;
                                                    messagingStyle = messagingStyle3;
                                                    str3 = str17;
                                                    str27 = str15;
                                                    strArr22 = strArr;
                                                    sb62 = sb3;
                                                    arrayList4 = arrayList22;
                                                    dialogKey10 = dialogKey5;
                                                    j7 = j14;
                                                    longSparseArray16 = longSparseArray11;
                                                    i4 = -1;
                                                    j22 = j12;
                                                    arrayList21 = arrayList8;
                                                }
                                                i36 = i37;
                                                arrayList8 = arrayList23;
                                                size5 = i16 - 1;
                                                messagingStyle = messagingStyle3;
                                                str3 = str17;
                                                str27 = str15;
                                                strArr22 = strArr;
                                                sb62 = sb3;
                                                arrayList4 = arrayList22;
                                                dialogKey10 = dialogKey5;
                                                j7 = j14;
                                                longSparseArray16 = longSparseArray11;
                                                i4 = -1;
                                                j22 = j12;
                                                arrayList21 = arrayList8;
                                            } else if (BuildVars.LOGS_ENABLED) {
                                                FileLog.w("message text is null for " + messageObject5.getId() + " did = " + messageObject5.getDialogId());
                                            }
                                        }
                                        dialogKey5 = dialogKey4;
                                        str15 = str27;
                                        j14 = j7;
                                        j12 = j22;
                                        longSparseArray11 = longSparseArray16;
                                        messagingStyle3 = messagingStyle;
                                        sb3 = sb62;
                                        strArr = strArr22;
                                        str17 = str3;
                                        i16 = size5;
                                        i36 = i37;
                                        arrayList8 = arrayList23;
                                        size5 = i16 - 1;
                                        messagingStyle = messagingStyle3;
                                        str3 = str17;
                                        str27 = str15;
                                        strArr22 = strArr;
                                        sb62 = sb3;
                                        arrayList4 = arrayList22;
                                        dialogKey10 = dialogKey5;
                                        j7 = j14;
                                        longSparseArray16 = longSparseArray11;
                                        i4 = -1;
                                        j22 = j12;
                                        arrayList21 = arrayList8;
                                    }
                                    messagingStyle2 = messagingStyle;
                                    j10 = j7;
                                    dialogKey3 = dialogKey10;
                                    str13 = str3;
                                    arrayList6 = arrayList4;
                                    longSparseArray9 = longSparseArray16;
                                    sb2 = sb62;
                                    ArrayList arrayList24 = arrayList21;
                                    j11 = j22;
                                    i14 = i36;
                                    arrayList7 = arrayList24;
                                    bitmap4 = bitmap3;
                                    str14 = str11;
                                }
                                Intent intent32 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent32.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent32.setFlags(ConnectionsManager.FileTypeFile);
                                intent32.addCategory("android.intent.category.LAUNCHER");
                                MessageObject messageObject42 = messageObject2;
                                if (messageObject2 == null && messageObject42.isStoryReactionPush) {
                                    intent32.putExtra("storyId", Math.abs(messageObject42.getId()));
                                    str3 = str13;
                                    dialogKey6 = dialogKey3;
                                } else {
                                    dialogKey6 = dialogKey3;
                                    if (dialogKey6.story) {
                                        long[] jArr2 = new long[notificationsController3.storyPushMessages.size()];
                                        int i39 = 0;
                                        while (i39 < notificationsController3.storyPushMessages.size()) {
                                            jArr2[i39] = notificationsController3.storyPushMessages.get(i39).dialogId;
                                            i39++;
                                            str13 = str13;
                                        }
                                        str3 = str13;
                                        intent32.putExtra("storyDialogIds", jArr2);
                                    } else {
                                        str3 = str13;
                                        if (DialogObject.isEncryptedDialog(j11)) {
                                            intent32.putExtra("encId", DialogObject.getEncryptedChatId(j11));
                                        } else if (DialogObject.isUserDialog(j11)) {
                                            intent32.putExtra("userId", j11);
                                        } else {
                                            intent32.putExtra("chatId", -j11);
                                        }
                                    }
                                }
                                StringBuilder sb72 = new StringBuilder();
                                sb72.append("show extra notifications chatId ");
                                sb72.append(j11);
                                sb72.append(" topicId ");
                                j15 = j10;
                                sb72.append(j15);
                                FileLog.d(sb72.toString());
                                if (j15 != 0) {
                                    intent32.putExtra("topicId", j15);
                                }
                                String str262 = str12;
                                intent32.putExtra(str262, notificationsController3.currentAccount);
                                PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent32, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                NotificationCompat.Action action32 = action;
                                if (action != null) {
                                    wearableExtender2.addAction(action32);
                                }
                                int i352 = i14;
                                Intent intent42 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent42.addFlags(32);
                                intent42.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent42.putExtra("dialog_id", j11);
                                intent42.putExtra(str10, i34);
                                intent42.putExtra(str262, notificationsController3.currentAccount);
                                ArrayList arrayList182 = arrayList7;
                                Bitmap bitmap62 = bitmap4;
                                NotificationCompat.Action build42 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j11)) {
                                    str18 = str262;
                                    if (j11 != globalSecretChatId) {
                                        sb5 = new StringBuilder();
                                        sb5.append("tgenc");
                                        sb5.append(DialogObject.getEncryptedChatId(j11));
                                        sb5.append("_");
                                        sb5.append(i34);
                                        str19 = sb5.toString();
                                        if (str19 == null) {
                                        }
                                        StringBuilder sb10 = new StringBuilder();
                                        sb10.append("tgaccount");
                                        long j24 = j9;
                                        sb10.append(j24);
                                        wearableExtender2.setBridgeTag(sb10.toString());
                                        if (dialogKey6.story) {
                                        }
                                        NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                        if (dialogKey6.story) {
                                        }
                                        category = autoCancel.setNumber(arrayList9.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                        intent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent.putExtra("messageDate", i8);
                                        intent.putExtra("dialogId", j11);
                                        str20 = str18;
                                        intent.putExtra(str20, notificationsController3.currentAccount);
                                        if (dialogKey6.story) {
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160));
                                        if (z6) {
                                        }
                                        if (action32 != null) {
                                        }
                                        if (!z12) {
                                        }
                                        if (arrayList3.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j11)) {
                                        }
                                        if (bitmap62 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        if (tLRPC$Chat3 == null) {
                                        }
                                        tLRPC$User5 = tLRPC$User4;
                                        Notification notification3 = notification;
                                        boolean z15 = z6;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        FileLog.d("showExtraNotifications: holders.add " + j11);
                                        i9 = i27;
                                        j8 = j16;
                                        z10 = z12;
                                        TLRPC$Chat tLRPC$Chat4 = tLRPC$Chat3;
                                        z11 = z15;
                                        longSparseArray6 = longSparseArray9;
                                        i10 = i28;
                                        longSparseArray7 = longSparseArray5;
                                        sharedPreferences = sharedPreferences2;
                                        notification2 = notification3;
                                        arrayList5 = arrayList15;
                                        arrayList5.add(new 1NotificationHolder(num.intValue(), j11, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat4, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j11, num);
                                    } else {
                                        str19 = null;
                                        if (str19 == null) {
                                        }
                                        StringBuilder sb102 = new StringBuilder();
                                        sb102.append("tgaccount");
                                        long j242 = j9;
                                        sb102.append(j242);
                                        wearableExtender2.setBridgeTag(sb102.toString());
                                        if (dialogKey6.story) {
                                        }
                                        NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                        if (dialogKey6.story) {
                                        }
                                        category = autoCancel2.setNumber(arrayList9.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                        intent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent.putExtra("messageDate", i8);
                                        intent.putExtra("dialogId", j11);
                                        str20 = str18;
                                        intent.putExtra(str20, notificationsController3.currentAccount);
                                        if (dialogKey6.story) {
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160));
                                        if (z6) {
                                        }
                                        if (action32 != null) {
                                        }
                                        if (!z12) {
                                        }
                                        if (arrayList3.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j11)) {
                                        }
                                        if (bitmap62 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        if (tLRPC$Chat3 == null) {
                                        }
                                        tLRPC$User5 = tLRPC$User4;
                                        Notification notification32 = notification;
                                        boolean z152 = z6;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        FileLog.d("showExtraNotifications: holders.add " + j11);
                                        i9 = i27;
                                        j8 = j16;
                                        z10 = z12;
                                        TLRPC$Chat tLRPC$Chat42 = tLRPC$Chat3;
                                        z11 = z152;
                                        longSparseArray6 = longSparseArray9;
                                        i10 = i28;
                                        longSparseArray7 = longSparseArray5;
                                        sharedPreferences = sharedPreferences2;
                                        notification2 = notification32;
                                        arrayList5 = arrayList15;
                                        arrayList5.add(new 1NotificationHolder(num.intValue(), j11, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat42, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j11, num);
                                    }
                                } else if (DialogObject.isUserDialog(j11)) {
                                    str19 = "tguser" + j11 + "_" + i34;
                                    str18 = str262;
                                    if (str19 == null) {
                                        wearableExtender2.setDismissalId(str19);
                                        NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                        wearableExtender3.setDismissalId("summary_" + str19);
                                        builder.extend(wearableExtender3);
                                    }
                                    StringBuilder sb1022 = new StringBuilder();
                                    sb1022.append("tgaccount");
                                    long j2422 = j9;
                                    sb1022.append(j2422);
                                    wearableExtender2.setBridgeTag(sb1022.toString());
                                    if (dialogKey6.story) {
                                        action2 = build42;
                                        j16 = j2422;
                                        arrayList9 = arrayList6;
                                        j17 = ((MessageObject) arrayList9.get(0)).messageOwner.date * 1000;
                                    } else {
                                        j16 = j2422;
                                        j17 = Long.MAX_VALUE;
                                        int i40 = 0;
                                        while (i40 < notificationsController3.storyPushMessages.size()) {
                                            j17 = Math.min(notificationsController3.storyPushMessages.get(i40).date, j17);
                                            i40++;
                                            build42 = build42;
                                        }
                                        action2 = build42;
                                        arrayList9 = arrayList6;
                                    }
                                    NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                    if (dialogKey6.story) {
                                        arrayList9 = notificationsController3.storyPushMessages;
                                    }
                                    category = autoCancel22.setNumber(arrayList9.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                    try {
                                        intent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent.putExtra("messageDate", i8);
                                        intent.putExtra("dialogId", j11);
                                        str20 = str18;
                                    } catch (Exception e6) {
                                        e = e6;
                                        str20 = str18;
                                    }
                                    try {
                                        intent.putExtra(str20, notificationsController3.currentAccount);
                                        if (dialogKey6.story) {
                                            intent.putExtra("story", true);
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160));
                                    } catch (Exception e7) {
                                        e = e7;
                                        FileLog.e(e);
                                        if (z6) {
                                        }
                                        if (action32 != null) {
                                        }
                                        if (!z12) {
                                        }
                                        if (arrayList3.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j11)) {
                                        }
                                        if (bitmap62 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        if (tLRPC$Chat3 == null) {
                                        }
                                        tLRPC$User5 = tLRPC$User4;
                                        Notification notification322 = notification;
                                        boolean z1522 = z6;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        FileLog.d("showExtraNotifications: holders.add " + j11);
                                        i9 = i27;
                                        j8 = j16;
                                        z10 = z12;
                                        TLRPC$Chat tLRPC$Chat422 = tLRPC$Chat3;
                                        z11 = z1522;
                                        longSparseArray6 = longSparseArray9;
                                        i10 = i28;
                                        longSparseArray7 = longSparseArray5;
                                        sharedPreferences = sharedPreferences2;
                                        notification2 = notification322;
                                        arrayList5 = arrayList15;
                                        arrayList5.add(new 1NotificationHolder(num.intValue(), j11, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat422, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j11, num);
                                        i5 = i10 + 1;
                                        arrayList14 = arrayList5;
                                        size = i9;
                                        clientUserId = j8;
                                        notificationsSettings = sharedPreferences;
                                        z5 = z10;
                                        arrayList11 = arrayList3;
                                        longSparseArray13 = longSparseArray4;
                                        z4 = z11;
                                        longSparseArray14 = longSparseArray7;
                                        longSparseArray = longSparseArray6;
                                        build2 = notification2;
                                        i4 = -1;
                                        notificationsController3 = notificationsController2;
                                    }
                                    if (z6) {
                                        category.setGroup(notificationsController3.notificationGroup);
                                        category.setGroupAlertBehavior(1);
                                    }
                                    if (action32 != null) {
                                        category.addAction(action32);
                                    }
                                    if (!z12 && !dialogKey6.story && (messageObject42 == null || !messageObject42.isStoryReactionPush)) {
                                        category.addAction(action2);
                                    }
                                    if (arrayList3.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey6.story) {
                                        category.setSubText(str);
                                    }
                                    if (DialogObject.isEncryptedDialog(j11)) {
                                        category.setLocalOnly(true);
                                    }
                                    if (bitmap62 != null) {
                                        category.setLargeIcon(bitmap62);
                                    }
                                    if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList182 != null) {
                                        size3 = arrayList182.size();
                                        i18 = 0;
                                        while (i18 < size3) {
                                            ArrayList arrayList25 = arrayList182;
                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = (TLRPC$TL_keyboardButtonRow) arrayList25.get(i18);
                                            int size6 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                            int i41 = 0;
                                            while (i41 < size6) {
                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = (TLRPC$KeyboardButton) tLRPC$TL_keyboardButtonRow.buttons.get(i41);
                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                    i19 = size3;
                                                    arrayList10 = arrayList25;
                                                    Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                    intent5.putExtra(str20, notificationsController3.currentAccount);
                                                    intent5.putExtra("did", j11);
                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                    if (bArr != null) {
                                                        intent5.putExtra("data", bArr);
                                                    }
                                                    int i42 = i352;
                                                    intent5.putExtra("mid", i42);
                                                    String str31 = tLRPC$KeyboardButton.text;
                                                    Context context2 = ApplicationLoader.applicationContext;
                                                    str21 = str20;
                                                    int i43 = notificationsController3.lastButtonId;
                                                    i20 = i42;
                                                    notificationsController3.lastButtonId = i43 + 1;
                                                    category.addAction(0, str31, PendingIntent.getBroadcast(context2, i43, intent5, 167772160));
                                                } else {
                                                    i19 = size3;
                                                    str21 = str20;
                                                    arrayList10 = arrayList25;
                                                    i20 = i352;
                                                }
                                                i41++;
                                                size3 = i19;
                                                arrayList25 = arrayList10;
                                                i352 = i20;
                                                str20 = str21;
                                            }
                                            i18++;
                                            arrayList182 = arrayList25;
                                            i352 = i352;
                                        }
                                    }
                                    if (tLRPC$Chat3 == null || tLRPC$User4 == null) {
                                        tLRPC$User5 = tLRPC$User4;
                                    } else {
                                        tLRPC$User5 = tLRPC$User4;
                                        String str32 = tLRPC$User5.phone;
                                        if (str32 != null && str32.length() > 0) {
                                            category.addPerson("tel:+" + tLRPC$User5.phone);
                                        }
                                    }
                                    Notification notification3222 = notification;
                                    boolean z15222 = z6;
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        notificationsController3.setNotificationChannel(notification3222, category, z15222);
                                    }
                                    FileLog.d("showExtraNotifications: holders.add " + j11);
                                    i9 = i27;
                                    j8 = j16;
                                    z10 = z12;
                                    TLRPC$Chat tLRPC$Chat4222 = tLRPC$Chat3;
                                    z11 = z15222;
                                    longSparseArray6 = longSparseArray9;
                                    i10 = i28;
                                    longSparseArray7 = longSparseArray5;
                                    sharedPreferences = sharedPreferences2;
                                    notification2 = notification3222;
                                    arrayList5 = arrayList15;
                                    arrayList5.add(new 1NotificationHolder(num.intValue(), j11, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat4222, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                    notificationsController2 = this;
                                    notificationsController2.wearNotificationsIds.put(j11, num);
                                } else {
                                    sb5 = new StringBuilder();
                                    sb5.append("tgchat");
                                    str18 = str262;
                                    sb5.append(-j11);
                                    sb5.append("_");
                                    sb5.append(i34);
                                    str19 = sb5.toString();
                                    if (str19 == null) {
                                    }
                                    StringBuilder sb10222 = new StringBuilder();
                                    sb10222.append("tgaccount");
                                    long j24222 = j9;
                                    sb10222.append(j24222);
                                    wearableExtender2.setBridgeTag(sb10222.toString());
                                    if (dialogKey6.story) {
                                    }
                                    NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                    if (dialogKey6.story) {
                                    }
                                    category = autoCancel222.setNumber(arrayList9.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                    intent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                    intent.putExtra("messageDate", i8);
                                    intent.putExtra("dialogId", j11);
                                    str20 = str18;
                                    intent.putExtra(str20, notificationsController3.currentAccount);
                                    if (dialogKey6.story) {
                                    }
                                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160));
                                    if (z6) {
                                    }
                                    if (action32 != null) {
                                    }
                                    if (!z12) {
                                        category.addAction(action2);
                                    }
                                    if (arrayList3.size() != 1) {
                                    }
                                    if (DialogObject.isEncryptedDialog(j11)) {
                                    }
                                    if (bitmap62 != null) {
                                    }
                                    if (!AndroidUtilities.needShowPasscode(false)) {
                                        size3 = arrayList182.size();
                                        i18 = 0;
                                        while (i18 < size3) {
                                        }
                                    }
                                    if (tLRPC$Chat3 == null) {
                                    }
                                    tLRPC$User5 = tLRPC$User4;
                                    Notification notification32222 = notification;
                                    boolean z152222 = z6;
                                    if (Build.VERSION.SDK_INT >= 26) {
                                    }
                                    FileLog.d("showExtraNotifications: holders.add " + j11);
                                    i9 = i27;
                                    j8 = j16;
                                    z10 = z12;
                                    TLRPC$Chat tLRPC$Chat42222 = tLRPC$Chat3;
                                    z11 = z152222;
                                    longSparseArray6 = longSparseArray9;
                                    i10 = i28;
                                    longSparseArray7 = longSparseArray5;
                                    sharedPreferences = sharedPreferences2;
                                    notification2 = notification32222;
                                    arrayList5 = arrayList15;
                                    arrayList5.add(new 1NotificationHolder(num.intValue(), j11, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat42222, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                    notificationsController2 = this;
                                    notificationsController2.wearNotificationsIds.put(j11, num);
                                }
                                i5 = i10 + 1;
                                arrayList14 = arrayList5;
                                size = i9;
                                clientUserId = j8;
                                notificationsSettings = sharedPreferences;
                                z5 = z10;
                                arrayList11 = arrayList3;
                                longSparseArray13 = longSparseArray4;
                                z4 = z11;
                                longSparseArray14 = longSparseArray7;
                                longSparseArray = longSparseArray6;
                                build2 = notification2;
                                i4 = -1;
                                notificationsController3 = notificationsController2;
                            }
                        }
                    }
                    str11 = string;
                    action = build;
                    messagingStyle = (person3 == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person3) : new NotificationCompat.MessagingStyle("");
                    i13 = Build.VERSION.SDK_INT;
                    if (i13 >= 28) {
                    }
                    messagingStyle.setConversationTitle(format);
                    messagingStyle.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j22)) || UserObject.isReplyUser(j22));
                    StringBuilder sb622 = new StringBuilder();
                    String[] strArr222 = new String[1];
                    boolean[] zArr22 = new boolean[1];
                    if (dialogKey9.story) {
                    }
                    Intent intent322 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent322.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent322.setFlags(ConnectionsManager.FileTypeFile);
                    intent322.addCategory("android.intent.category.LAUNCHER");
                    MessageObject messageObject422 = messageObject2;
                    if (messageObject2 == null) {
                    }
                    dialogKey6 = dialogKey3;
                    if (dialogKey6.story) {
                    }
                    StringBuilder sb722 = new StringBuilder();
                    sb722.append("show extra notifications chatId ");
                    sb722.append(j11);
                    sb722.append(" topicId ");
                    j15 = j10;
                    sb722.append(j15);
                    FileLog.d(sb722.toString());
                    if (j15 != 0) {
                    }
                    String str2622 = str12;
                    intent322.putExtra(str2622, notificationsController3.currentAccount);
                    PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent322, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                    NotificationCompat.Action action322 = action;
                    if (action != null) {
                    }
                    int i3522 = i14;
                    Intent intent422 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent422.addFlags(32);
                    intent422.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent422.putExtra("dialog_id", j11);
                    intent422.putExtra(str10, i34);
                    intent422.putExtra(str2622, notificationsController3.currentAccount);
                    ArrayList arrayList1822 = arrayList7;
                    Bitmap bitmap622 = bitmap4;
                    NotificationCompat.Action build422 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(j11)) {
                    }
                    i5 = i10 + 1;
                    arrayList14 = arrayList5;
                    size = i9;
                    clientUserId = j8;
                    notificationsSettings = sharedPreferences;
                    z5 = z10;
                    arrayList11 = arrayList3;
                    longSparseArray13 = longSparseArray4;
                    z4 = z11;
                    longSparseArray14 = longSparseArray7;
                    longSparseArray = longSparseArray6;
                    build2 = notification2;
                    i4 = -1;
                    notificationsController3 = notificationsController2;
                }
                LongSparseArray longSparseArray17 = longSparseArray;
                longSparseArray2 = longSparseArray14;
                Notification notification4 = build2;
                NotificationsController notificationsController4 = notificationsController3;
                ArrayList arrayList26 = arrayList14;
                if (z4) {
                    notificationsController = notificationsController4;
                    arrayList = arrayList26;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel summary with id " + notificationsController.notificationId);
                        }
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show summary with id " + notificationsController4.notificationId);
                    }
                    try {
                        notificationManager.notify(notificationsController4.notificationId, notification4);
                        notificationsController = notificationsController4;
                        arrayList = arrayList26;
                    } catch (SecurityException e8) {
                        FileLog.e(e8);
                        notificationsController = this;
                        arrayList = arrayList26;
                        notificationsController.resetNotificationSound(builder, j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3);
                    }
                }
                i6 = 0;
                while (i6 < longSparseArray2.size()) {
                    LongSparseArray longSparseArray18 = longSparseArray2;
                    if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray18.keyAt(i6)))) {
                        Integer num4 = (Integer) longSparseArray18.valueAt(i6);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel notification id " + num4);
                        }
                        notificationManager.cancel(num4.intValue());
                    }
                    i6++;
                    longSparseArray2 = longSparseArray18;
                }
                ArrayList arrayList27 = new ArrayList(arrayList.size());
                FileLog.d("showExtraNotifications: holders.size()=" + arrayList.size());
                size2 = arrayList.size();
                i7 = 0;
                while (i7 < size2) {
                    ArrayList arrayList28 = arrayList;
                    1NotificationHolder r4 = (1NotificationHolder) arrayList28.get(i7);
                    arrayList27.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r4.dialogId)) {
                        longSparseArray3 = longSparseArray17;
                    } else {
                        NotificationCompat.Builder builder3 = r4.notification;
                        long j25 = r4.dialogId;
                        longSparseArray3 = longSparseArray17;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j25, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j25), !r4.story);
                        if (createNotificationShortcut != null) {
                            arrayList27.add(createNotificationShortcut);
                        }
                    }
                    FileLog.d("showExtraNotifications: holders[" + i7 + "].call()");
                    r4.call();
                    if (!unsupportedNotificationShortcut() && !arrayList27.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList27);
                    }
                    i7++;
                    arrayList = arrayList28;
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
        if (SharedConfig.passcodeHash.length() <= 0) {
        }
        FileLog.d("showExtraNotifications: passcode=" + (SharedConfig.passcodeHash.length() <= 0) + " waitingForPasscode=" + z5 + " selfUserId=" + clientUserId + " useSummaryNotification=" + z4);
        longSparseArray = new LongSparseArray();
        size = arrayList11.size();
        i5 = 0;
        while (true) {
            if (i5 < size) {
            }
            i5 = i10 + 1;
            arrayList14 = arrayList5;
            size = i9;
            clientUserId = j8;
            notificationsSettings = sharedPreferences;
            z5 = z10;
            arrayList11 = arrayList3;
            longSparseArray13 = longSparseArray4;
            z4 = z11;
            longSparseArray14 = longSparseArray7;
            longSparseArray = longSparseArray6;
            build2 = notification2;
            i4 = -1;
            notificationsController3 = notificationsController2;
        }
        LongSparseArray longSparseArray172 = longSparseArray;
        longSparseArray2 = longSparseArray14;
        Notification notification42 = build2;
        NotificationsController notificationsController42 = notificationsController3;
        ArrayList arrayList262 = arrayList14;
        if (z4) {
        }
        i6 = 0;
        while (i6 < longSparseArray2.size()) {
        }
        ArrayList arrayList272 = new ArrayList(arrayList.size());
        FileLog.d("showExtraNotifications: holders.size()=" + arrayList.size());
        size2 = arrayList.size();
        i7 = 0;
        while (i7 < size2) {
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:452:0x0acc
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private void showOrUpdateNotification(boolean r56) {
        /*
            Method dump skipped, instructions count: 3652
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showOrUpdateNotification(boolean):void");
    }

    private boolean unsupportedNotificationShortcut() {
        return Build.VERSION.SDK_INT < 29 || !SharedConfig.chatBubbles;
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

    /* JADX WARN: Code restructure failed: missing block: B:197:0x044f, code lost:
        if (r12 != false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0457, code lost:
        if (r12 != false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0460, code lost:
        if (r12 != false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x046d, code lost:
        if (r12 != false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0473, code lost:
        if (r12 != false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0488, code lost:
        if (r12 != false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x048a, code lost:
        r14 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x048c, code lost:
        r14 = 2;
     */
    /* JADX WARN: Removed duplicated region for block: B:190:0x043f  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x049e  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x052f  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x054f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:266:0x058e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:274:0x05a1 A[LOOP:1: B:272:0x059c->B:274:0x05a1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x05b2  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x05be A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:284:0x05cf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:294:0x05e7  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x05fe  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x05ab A[EDGE_INSN: B:326:0x05ab->B:275:0x05ab ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01cb  */
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
        StringBuilder sb2;
        boolean z6;
        AudioAttributes build;
        AudioAttributes build2;
        NotificationChannel notificationChannel;
        int importance;
        Uri sound;
        long[] vibrationPattern;
        boolean shouldVibrate;
        boolean z7;
        long[] jArr3;
        int lightColor;
        String str23;
        String str24;
        String str25;
        int i6;
        long[] jArr4;
        SharedPreferences.Editor editor;
        boolean z8;
        String str26;
        boolean z9;
        int i7;
        String str27;
        int i8;
        String str28;
        String str29;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str30 = "stories";
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
            String str31 = str3;
            str4 = str2;
            str5 = str31;
        }
        boolean z10 = !z && DialogObject.isEncryptedDialog(j);
        boolean z11 = (z2 || str5 == null || !notificationsSettings.getBoolean(str5, false)) ? false : true;
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
            formatString = LocaleController.getString(R.string.NotificationsSilent);
            str30 = "silent";
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
                        str8 = str30;
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
            String str32 = str8 + "_" + MD52;
            string = notificationsSettings.getString(str32, null);
            String string3 = notificationsSettings.getString(str32 + "_s", null);
            StringBuilder sb4 = new StringBuilder();
            if (string == null) {
                notificationChannel = systemNotificationManager.getNotificationChannel(string);
                if (BuildVars.LOGS_ENABLED) {
                    str15 = str4;
                    StringBuilder sb5 = new StringBuilder();
                    str14 = "channel_";
                    sb5.append("current channel for ");
                    sb5.append(string);
                    sb5.append(" = ");
                    sb5.append(notificationChannel);
                    FileLog.d(sb5.toString());
                } else {
                    str14 = "channel_";
                    str15 = str4;
                }
                if (notificationChannel == null) {
                    str11 = "_s";
                    jArr2 = jArr;
                    sb = sb4;
                    str13 = "secret";
                    notificationsController = this;
                    j3 = j;
                    z4 = z11;
                    str16 = "_";
                    str17 = str32;
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
                    while (true) {
                        str22 = str21;
                        if (i5 < jArr2.length) {
                        }
                        sb.append(jArr2[i5]);
                        i5++;
                        str21 = str22;
                    }
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
                } else if (!z3 && !z11) {
                    importance = notificationChannel.getImportance();
                    sound = notificationChannel.getSound();
                    vibrationPattern = notificationChannel.getVibrationPattern();
                    z4 = z11;
                    shouldVibrate = notificationChannel.shouldVibrate();
                    if (shouldVibrate || vibrationPattern != null) {
                        z7 = shouldVibrate;
                        jArr3 = vibrationPattern;
                    } else {
                        z7 = shouldVibrate;
                        jArr3 = new long[]{0, 0};
                    }
                    lightColor = notificationChannel.getLightColor();
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
                    if (!z && z10) {
                        sb4.append("secret");
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("current channel settings for " + string + " = " + ((Object) sb4) + " old = " + string3);
                    }
                    String MD53 = Utilities.MD5(sb4.toString());
                    sb4.setLength(0);
                    if (z2 && i2 != importance) {
                        str11 = "_s";
                        jArr2 = jArr;
                        i4 = i;
                        sb = sb4;
                        str23 = string3;
                        str24 = string;
                        str13 = "secret";
                        notificationsController = this;
                        str25 = MD53;
                        str16 = "_";
                        z5 = false;
                        z4 = true;
                        j3 = j;
                        str17 = str32;
                    } else if (MD53.equals(string3)) {
                        str11 = "_s";
                        jArr2 = jArr;
                        i4 = i;
                        sb = sb4;
                        str23 = string3;
                        str24 = string;
                        str13 = "secret";
                        notificationsController = this;
                        str25 = MD53;
                        str16 = "_";
                        j3 = j;
                        str17 = str32;
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
                                str17 = str32;
                                str24 = string;
                                str29 = "secret";
                                str25 = MD53;
                                i6 = lightColor;
                                str16 = "_";
                                sb = sb4;
                                str23 = string3;
                            } else {
                                if (i3 == 3) {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY);
                                    i6 = lightColor;
                                    sb6.append(getSharedPrefKey(j, 0L));
                                    edit.putBoolean(sb6.toString(), false);
                                    str28 = "secret";
                                } else {
                                    str28 = "secret";
                                    i6 = lightColor;
                                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, 0L), 2);
                                }
                                str11 = "_s";
                                str23 = string3;
                                str17 = str32;
                                str24 = string;
                                sb = sb4;
                                str25 = MD53;
                                str16 = "_";
                                str29 = str28;
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            j3 = j;
                            str13 = str29;
                            editor = edit;
                            jArr4 = jArr3;
                        } else {
                            str11 = "_s";
                            str17 = str32;
                            str24 = string;
                            str13 = "secret";
                            str25 = MD53;
                            i6 = lightColor;
                            str16 = "_";
                            sb = sb4;
                            j3 = j;
                            str23 = string3;
                            if (importance == i2) {
                                jArr4 = jArr3;
                                editor = null;
                                z8 = false;
                                notificationsController = this;
                                jArr2 = jArr;
                                z9 = z7;
                                if ((!notificationsController.isEmptyVibration(jArr2)) == z9) {
                                    if (!z2) {
                                        if (editor == null) {
                                            editor = notificationsSettings.edit();
                                        }
                                        if (!z) {
                                            str27 = "vibrate_" + j3;
                                        } else if (i3 == 2) {
                                            str27 = "vibrate_channel";
                                        } else if (i3 == 0) {
                                            str27 = "vibrate_group";
                                        } else if (i3 == 3) {
                                            str27 = "vibrate_stories";
                                        } else if (i3 == 4 || i3 == 5) {
                                            str27 = "vibrate_react";
                                        } else {
                                            str27 = "vibrate_messages";
                                        }
                                        editor.putInt(str27, i8);
                                    }
                                    jArr2 = jArr4;
                                    i4 = i;
                                    i7 = i6;
                                    z8 = true;
                                } else {
                                    i4 = i;
                                    i7 = i6;
                                }
                                if (i7 != i4) {
                                    if (!z2) {
                                        if (editor == null) {
                                            editor = notificationsSettings.edit();
                                        }
                                        editor.putInt(z ? i3 == 2 ? "ChannelLed" : i3 == 0 ? "GroupLed" : i3 == 3 ? "StoriesLed" : (i3 == 5 || i3 == 4) ? "ReactionsLed" : "MessagesLed" : "color_" + j3, i7);
                                    }
                                    i4 = i7;
                                    z8 = true;
                                }
                                if (editor != null) {
                                    editor.commit();
                                }
                                z5 = z8;
                            } else if (z2) {
                                jArr4 = jArr3;
                                editor = null;
                            } else {
                                SharedPreferences.Editor edit2 = notificationsSettings.edit();
                                jArr4 = jArr3;
                                int i9 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
                                if (z) {
                                    if (i3 == 3) {
                                        edit2.putBoolean("EnableAllStories", true);
                                    } else if (i3 == 4) {
                                        edit2.putBoolean("EnableReactionsMessages", true);
                                        edit2.putBoolean("EnableReactionsStories", true);
                                    } else {
                                        edit2.putInt(getGlobalNotificationsKey(i3), 0);
                                    }
                                    str26 = i3 == 2 ? "priority_channel" : i3 == 0 ? "priority_group" : i3 == 3 ? "priority_stories" : (i3 == 4 || i3 == 5) ? "priority_react" : "priority_messages";
                                } else if (i3 == 3) {
                                    edit2.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j3, true);
                                    editor = edit2;
                                } else {
                                    edit2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j3, 0);
                                    edit2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j3);
                                    str26 = "priority_" + j3;
                                }
                                edit2.putInt(str26, i9);
                                editor = edit2;
                            }
                        }
                        z8 = true;
                        notificationsController = this;
                        jArr2 = jArr;
                        z9 = z7;
                        if ((!notificationsController.isEmptyVibration(jArr2)) == z9) {
                        }
                        if (i7 != i4) {
                        }
                        if (editor != null) {
                        }
                        z5 = z8;
                    }
                    str19 = str25;
                    str20 = str23;
                    str18 = str24;
                    if (z5 || str19 == null) {
                        str21 = str11;
                        if (!z4 || str19 == null || !z2 || !z) {
                            i5 = 0;
                            while (true) {
                                str22 = str21;
                                if (i5 < jArr2.length) {
                                    break;
                                }
                                sb.append(jArr2[i5]);
                                i5++;
                                str21 = str22;
                            }
                            sb.append(i4);
                            uri3 = uri;
                            if (uri3 != null) {
                                sb.append(uri.toString());
                            }
                            sb.append(i2);
                            if (!z && z10) {
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
                                if (z) {
                                    sb2 = new StringBuilder();
                                    sb2.append(notificationsController.currentAccount);
                                    sb2.append(str14);
                                    sb2.append(str17);
                                } else {
                                    sb2 = new StringBuilder();
                                    sb2.append(notificationsController.currentAccount);
                                    sb2.append(str14);
                                    sb2.append(j3);
                                }
                                sb2.append(str16);
                                sb2.append(Utilities.random.nextLong());
                                str18 = sb2.toString();
                                if (z10) {
                                    str9 = LocaleController.getString(R.string.SecretChatName);
                                }
                                NotificationChannel notificationChannel2 = new NotificationChannel(str18, str9, i2);
                                notificationChannel2.setGroup(str15);
                                if (i4 != 0) {
                                    notificationChannel2.enableLights(true);
                                    notificationChannel2.setLightColor(i4);
                                    z6 = false;
                                } else {
                                    z6 = false;
                                    notificationChannel2.enableLights(false);
                                }
                                if (notificationsController.isEmptyVibration(jArr2)) {
                                    notificationChannel2.enableVibration(z6);
                                } else {
                                    notificationChannel2.enableVibration(true);
                                    if (jArr2.length > 0) {
                                        notificationChannel2.setVibrationPattern(jArr2);
                                    }
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
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str17);
                        str21 = str11;
                        sb7.append(str21);
                        putString.putString(sb7.toString(), str19).commit();
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
                    sb = sb4;
                    str10 = string3;
                    str12 = string;
                    str13 = "secret";
                    notificationsController = this;
                    j3 = j;
                    z4 = z11;
                    str16 = "_";
                    str17 = str32;
                    i4 = i;
                }
            } else {
                sb = sb4;
                str10 = string3;
                str11 = "_s";
                str12 = string;
                str13 = "secret";
                notificationsController = this;
                j3 = j;
                str14 = "channel_";
                str15 = str4;
                z4 = z11;
                str16 = "_";
                i4 = i;
                str17 = str32;
                jArr2 = jArr;
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
            while (true) {
                str22 = str21;
                if (i5 < jArr2.length) {
                }
                sb.append(jArr2[i5]);
                i5++;
                str21 = str22;
            }
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
            formatString = z2 ? LocaleController.formatString(R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb8 = new StringBuilder();
            sb8.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb8.append(j);
            sb8.append("_");
            sb8.append(j2);
            str30 = sb8.toString();
        }
        str9 = formatString;
        str8 = str30;
        String str322 = str8 + "_" + MD52;
        string = notificationsSettings.getString(str322, null);
        String string32 = notificationsSettings.getString(str322 + "_s", null);
        StringBuilder sb42 = new StringBuilder();
        if (string == null) {
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
        while (true) {
            str22 = str21;
            if (i5 < jArr2.length) {
            }
            sb.append(jArr2[i5]);
            i5++;
            str21 = str22;
        }
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

    public void clearDialogNotificationsSettings(long j, long j2) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String sharedPrefKey = getSharedPrefKey(j, j2);
        SharedPreferences.Editor remove = edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
        remove.remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey);
        getMessagesStorage().setDialogFlags(j, 0L);
        TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) getMessagesController().dialogs_dict.get(j);
        if (tLRPC$Dialog != null) {
            tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
        }
        edit.commit();
        getNotificationsController().updateServerNotificationsSettings(j, j2, true);
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

    public void deleteNotificationChannel(long j, long j2) {
        deleteNotificationChannel(j, j2, -1);
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

    protected void ensureGroupsCreated() {
        List notificationChannels;
        String id;
        int importance;
        SharedPreferences.Editor remove;
        String str;
        List notificationChannelGroups;
        String id2;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (this.groupsCreated == null) {
            this.groupsCreated = Boolean.valueOf(notificationsSettings.getBoolean("groupsCreated5", false));
        }
        if (!this.groupsCreated.booleanValue()) {
            try {
                String str2 = this.currentAccount + "channel";
                notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                SharedPreferences.Editor editor = null;
                for (int i = 0; i < size; i++) {
                    NotificationChannel m = NotificationsController$$ExternalSyntheticApiModelOutline13.m(notificationChannels.get(i));
                    id = m.getId();
                    if (id.startsWith(str2)) {
                        importance = m.getImportance();
                        if (importance != 4 && importance != 5 && !id.contains("_ia_")) {
                            if (id.contains("_channels_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                remove = editor.remove("priority_channel").remove("vibrate_channel").remove("ChannelSoundPath");
                                str = "ChannelSound";
                            } else if (id.contains("_reactions_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                remove = editor.remove("priority_react").remove("vibrate_react").remove("ReactionSoundPath");
                                str = "ReactionSound";
                            } else if (id.contains("_groups_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                remove = editor.remove("priority_group").remove("vibrate_group").remove("GroupSoundPath");
                                str = "GroupSound";
                            } else if (id.contains("_private_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editor.remove("priority_messages");
                                remove = editor.remove("priority_group").remove("vibrate_messages").remove("GlobalSoundPath");
                                str = "GlobalSound";
                            } else {
                                long longValue = Utilities.parseLong(id.substring(9, id.indexOf(95, 9))).longValue();
                                if (longValue != 0) {
                                    if (editor == null) {
                                        editor = getAccountInstance().getNotificationsSettings().edit();
                                    }
                                    remove = editor.remove("priority_" + longValue).remove("vibrate_" + longValue).remove("sound_path_" + longValue);
                                    str = "sound_" + longValue;
                                }
                            }
                            remove.remove(str);
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
        String str3 = "channels" + this.currentAccount;
        String str4 = "groups" + this.currentAccount;
        int size2 = notificationChannelGroups.size();
        String str5 = "other" + this.currentAccount;
        String str6 = "reactions" + this.currentAccount;
        String str7 = "stories" + this.currentAccount;
        String str8 = "private" + this.currentAccount;
        for (int i2 = 0; i2 < size2; i2++) {
            id2 = NotificationsController$$ExternalSyntheticApiModelOutline16.m(notificationChannelGroups.get(i2)).getId();
            if (str3 != null && str3.equals(id2)) {
                str3 = null;
            } else if (str4 != null && str4.equals(id2)) {
                str4 = null;
            } else if (str7 != null && str7.equals(id2)) {
                str7 = null;
            } else if (str6 != null && str6.equals(id2)) {
                str6 = null;
            } else if (str8 != null && str8.equals(id2)) {
                str8 = null;
            } else if (str5 != null && str5.equals(id2)) {
                str5 = null;
            }
            if (str3 == null && str7 == null && str6 == null && str4 == null && str8 == null && str5 == null) {
                break;
            }
        }
        if (str3 != null || str4 != null || str6 != null || str7 != null || str8 != null || str5 != null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                getUserConfig().getCurrentUser();
            }
            String str9 = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : "";
            ArrayList arrayList = new ArrayList();
            if (str3 != null) {
                arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString(R.string.NotificationsChannels) + str9));
            }
            if (str4 != null) {
                arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString(R.string.NotificationsGroups) + str9));
            }
            if (str7 != null) {
                arrayList.add(new NotificationChannelGroup(str7, LocaleController.getString(R.string.NotificationsStories) + str9));
            }
            if (str6 != null) {
                arrayList.add(new NotificationChannelGroup(str6, LocaleController.getString(R.string.NotificationsReactions) + str9));
            }
            if (str8 != null) {
                arrayList.add(new NotificationChannelGroup(str8, LocaleController.getString(R.string.NotificationsPrivateChats) + str9));
            }
            if (str5 != null) {
                arrayList.add(new NotificationChannelGroup(str5, LocaleController.getString(R.string.NotificationsOther) + str9));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
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

    public NotificationsSettingsFacade getNotificationsSettingsFacade() {
        return this.dialogsNotificationsFacade;
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
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

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$hideNotifications$34();
            }
        });
    }

    public boolean isGlobalNotificationsEnabled(int i) {
        SharedPreferences notificationsSettings;
        String str;
        if (i == 4) {
            notificationsSettings = getAccountInstance().getNotificationsSettings();
            str = "EnableReactionsMessages";
        } else if (i == 5) {
            notificationsSettings = getAccountInstance().getNotificationsSettings();
            str = "EnableReactionsStories";
        } else if (i != 3) {
            return getAccountInstance().getNotificationsSettings().getInt(getGlobalNotificationsKey(i), 0) < getConnectionsManager().getCurrentTime();
        } else {
            notificationsSettings = getAccountInstance().getNotificationsSettings();
            str = "EnableAllStories";
        }
        return notificationsSettings.getBoolean(str, true);
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

    public boolean isGlobalNotificationsEnabled(long j, boolean z, boolean z2) {
        return isGlobalNotificationsEnabled(j, null, z, z2);
    }

    public void loadTopicsNotificationsExceptions(final long j, final Consumer<HashSet<Integer>> consumer) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$loadTopicsNotificationsExceptions$51(j, consumer);
            }
        });
    }

    public void muteDialog(long j, long j2, boolean z) {
        if (z) {
            getInstance(this.currentAccount).muteUntil(j, j2, ConnectionsManager.DEFAULT_DATACENTER_ID);
            return;
        }
        boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j, false, false);
        boolean z2 = j2 != 0;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        if (!isGlobalNotificationsEnabled || z2) {
            edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 0);
        } else {
            edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2));
        }
        if (j2 == 0) {
            getMessagesStorage().setDialogFlags(j, 0L);
            TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) getMessagesController().dialogs_dict.get(j);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        }
        edit.apply();
        updateServerNotificationsSettings(j, j2);
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
            } else if (isGlobalNotificationsEnabled || z) {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 2);
            } else {
                edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                j3 = 0L;
            }
            edit.apply();
            if (j2 == 0) {
                getInstance(this.currentAccount).removeNotificationsForDialog(j);
                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(j, j3);
                TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
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

    public void processDeleteStory(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDeleteStory$14(j, i);
            }
        });
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

    public void processEditedMessages(final LongSparseArray longSparseArray) {
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

    public void processIgnoreStories() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStories$16();
            }
        });
    }

    public void processIgnoreStories(final long j) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStories$18(j);
            }
        });
    }

    public void processIgnoreStoryReactions() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStoryReactions$17();
            }
        });
    }

    public void processLoadedUnreadMessages(final LongSparseArray longSparseArray, final ArrayList<TLRPC$Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC$User> arrayList3, ArrayList<TLRPC$Chat> arrayList4, ArrayList<TLRPC$EncryptedChat> arrayList5, final Collection<StoryNotification> collection) {
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

    public void processNewMessages(final ArrayList<MessageObject> arrayList, final boolean z, final boolean z2, final CountDownLatch countDownLatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("NotificationsController: processNewMessages msgs.size()=");
        sb.append(arrayList == null ? "null" : Integer.valueOf(arrayList.size()));
        sb.append(" isLast=");
        sb.append(z);
        sb.append(" isFcm=");
        sb.append(z2);
        sb.append(")");
        FileLog.d(sb.toString());
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

    public void processReadMessages(final LongSparseIntArray longSparseIntArray, final long j, final int i, final int i2, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$20(longSparseIntArray, arrayList, j, i2, i, z);
            }
        });
    }

    public void processReadStories() {
    }

    public void processReadStories(final long j, int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadStories$15(j);
            }
        });
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

    public void removeDeletedHisoryFromNotifications(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$removeDeletedHisoryFromNotifications$12(longSparseIntArray, arrayList);
            }
        });
    }

    public void removeDeletedMessagesFromNotifications(final LongSparseArray longSparseArray, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$removeDeletedMessagesFromNotifications$9(longSparseArray, z, arrayList);
            }
        });
    }

    public void removeNotificationsForDialog(long j) {
        processReadMessages(null, j, 0, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
        LongSparseIntArray longSparseIntArray = new LongSparseIntArray();
        longSparseIntArray.put(j, 0);
        processDialogsUpdateRead(longSparseIntArray);
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

    public void setDialogNotificationsSettings(long j, long j2, int i) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(j);
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

    public void setGlobalNotificationsEnabled(int i, int i2) {
        getAccountInstance().getNotificationsSettings().edit().putInt(getGlobalNotificationsKey(i), i2).commit();
        updateServerNotificationsSettings(i);
        getMessagesStorage().updateMutedDialogsFiltersCounters();
        deleteNotificationChannelGlobal(i);
    }

    public void setInChatSoundEnabled(boolean z) {
        this.inChatSoundEnabled = z;
    }

    public void setLastOnlineFromOtherDevice(final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setLastOnlineFromOtherDevice$4(i);
            }
        });
    }

    public void setOpenedDialogId(final long j, final long j2) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedDialogId$2(j, j2);
            }
        });
    }

    public void setOpenedInBubble(final long j, final boolean z) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedInBubble$3(z, j);
            }
        });
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda73
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$showNotifications$33();
            }
        });
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$updateBadge$32();
            }
        });
    }

    public void updateServerNotificationsSettings(int i) {
        TLRPC$TL_reactionsNotifySettings tLRPC$TL_reactionsNotifySettings;
        TLRPC$ReactionNotificationsFrom tLRPC$TL_reactionNotificationsFromAll;
        TLRPC$TL_reactionsNotifySettings tLRPC$TL_reactionsNotifySettings2;
        TLRPC$ReactionNotificationsFrom tLRPC$TL_reactionNotificationsFromAll2;
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings;
        String str;
        String str2;
        String str3;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (i == 4 || i == 5) {
            TLRPC$TL_account_setReactionsNotifySettings tLRPC$TL_account_setReactionsNotifySettings = new TLRPC$TL_account_setReactionsNotifySettings();
            tLRPC$TL_account_setReactionsNotifySettings.settings = new TLRPC$TL_reactionsNotifySettings();
            if (notificationsSettings.getBoolean("EnableReactionsMessages", true)) {
                tLRPC$TL_account_setReactionsNotifySettings.settings.flags |= 1;
                if (notificationsSettings.getBoolean("EnableReactionsMessagesContacts", false)) {
                    tLRPC$TL_reactionsNotifySettings2 = tLRPC$TL_account_setReactionsNotifySettings.settings;
                    tLRPC$TL_reactionNotificationsFromAll2 = new TLRPC$TL_reactionNotificationsFromContacts();
                } else {
                    tLRPC$TL_reactionsNotifySettings2 = tLRPC$TL_account_setReactionsNotifySettings.settings;
                    tLRPC$TL_reactionNotificationsFromAll2 = new TLRPC$TL_reactionNotificationsFromAll();
                }
                tLRPC$TL_reactionsNotifySettings2.messages_notify_from = tLRPC$TL_reactionNotificationsFromAll2;
            }
            if (notificationsSettings.getBoolean("EnableReactionsStories", true)) {
                tLRPC$TL_account_setReactionsNotifySettings.settings.flags |= 2;
                if (notificationsSettings.getBoolean("EnableReactionsStoriesContacts", false)) {
                    tLRPC$TL_reactionsNotifySettings = tLRPC$TL_account_setReactionsNotifySettings.settings;
                    tLRPC$TL_reactionNotificationsFromAll = new TLRPC$TL_reactionNotificationsFromContacts();
                } else {
                    tLRPC$TL_reactionsNotifySettings = tLRPC$TL_account_setReactionsNotifySettings.settings;
                    tLRPC$TL_reactionNotificationsFromAll = new TLRPC$TL_reactionNotificationsFromAll();
                }
                tLRPC$TL_reactionsNotifySettings.stories_notify_from = tLRPC$TL_reactionNotificationsFromAll;
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
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings2 = new TLRPC$TL_inputPeerNotifySettings();
        tLRPC$TL_account_updateNotifySettings.settings = tLRPC$TL_inputPeerNotifySettings2;
        tLRPC$TL_inputPeerNotifySettings2.flags = 5;
        if (i == 0) {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyChats();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableGroup2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewGroup", true);
            tLRPC$TL_inputPeerNotifySettings = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings.flags |= 8;
            str = "GroupSoundDocId";
            str2 = "GroupSoundPath";
            str3 = "GroupSound";
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
            getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda52
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    NotificationsController.lambda$updateServerNotificationsSettings$49(tLObject, tLRPC$TL_error);
                }
            });
        } else {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyBroadcasts();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
            tLRPC$TL_inputPeerNotifySettings = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings.flags |= 8;
            str = "ChannelSoundDocId";
            str2 = "ChannelSoundPath";
            str3 = "ChannelSound";
        }
        tLRPC$TL_inputPeerNotifySettings.sound = getInputSound(notificationsSettings, str3, str, str2);
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda52
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                NotificationsController.lambda$updateServerNotificationsSettings$49(tLObject, tLRPC$TL_error);
            }
        });
    }

    public void updateServerNotificationsSettings(long j, long j2) {
        updateServerNotificationsSettings(j, j2, true);
    }

    public void updateServerNotificationsSettings(long j, long j2, boolean z) {
        int i = 0;
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
        int i2 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), -1);
        if (i2 != -1) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings4 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings4.flags |= 4;
            if (i2 == 3) {
                i = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, j2), 0);
            } else if (i2 == 2) {
                i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            tLRPC$TL_inputPeerNotifySettings4.mute_until = i;
        }
        long j3 = notificationsSettings.getLong("sound_document_id_" + getSharedPrefKey(j, j2), 0L);
        String string = notificationsSettings.getString("sound_path_" + getSharedPrefKey(j, j2), null);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings5 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings5.flags = tLRPC$TL_inputPeerNotifySettings5.flags | 8;
        if (j3 != 0) {
            TLRPC$TL_notificationSoundRingtone tLRPC$TL_notificationSoundRingtone = new TLRPC$TL_notificationSoundRingtone();
            tLRPC$TL_notificationSoundRingtone.id = j3;
            tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundRingtone;
        } else if (string == null) {
            tLRPC$TL_inputPeerNotifySettings5.sound = new TLRPC$TL_notificationSoundDefault();
        } else if (string.equalsIgnoreCase("NoSound")) {
            tLRPC$TL_account_updateNotifySettings.settings.sound = new TLRPC$TL_notificationSoundNone();
        } else {
            TLRPC$TL_notificationSoundLocal tLRPC$TL_notificationSoundLocal = new TLRPC$TL_notificationSoundLocal();
            tLRPC$TL_notificationSoundLocal.title = notificationsSettings.getString("sound_" + getSharedPrefKey(j, j2), null);
            tLRPC$TL_notificationSoundLocal.data = string;
            tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundLocal;
        }
        if (j2 == 0 || j == getUserConfig().getClientUserId()) {
            TLRPC$TL_inputNotifyPeer tLRPC$TL_inputNotifyPeer = new TLRPC$TL_inputNotifyPeer();
            tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyPeer;
            tLRPC$TL_inputNotifyPeer.peer = getMessagesController().getInputPeer(j);
        } else {
            TLRPC$TL_inputNotifyForumTopic tLRPC$TL_inputNotifyForumTopic = new TLRPC$TL_inputNotifyForumTopic();
            tLRPC$TL_inputNotifyForumTopic.peer = getMessagesController().getInputPeer(j);
            tLRPC$TL_inputNotifyForumTopic.top_msg_id = (int) j2;
            tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyForumTopic;
        }
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda72
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                NotificationsController.lambda$updateServerNotificationsSettings$47(tLObject, tLRPC$TL_error);
            }
        });
    }
}
