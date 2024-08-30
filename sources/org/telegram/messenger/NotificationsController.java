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
        r7 = ((java.lang.Long) r27.messageOwner.action.users.get(0)).longValue();
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
        r6 = getMessagesController().getUser((java.lang.Long) r27.messageOwner.action.users.get(r5));
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
        r3 = ((java.lang.Long) r27.messageOwner.action.users.get(0)).longValue();
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
        r6 = getMessagesController().getUser((java.lang.Long) r27.messageOwner.action.users.get(r4));
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
    /* JADX WARN: Removed duplicated region for block: B:73:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01d5  */
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
        int i3;
        boolean z5;
        MessageObject messageObject;
        long j3;
        SparseArray sparseArray;
        long j4;
        long j5;
        long j6;
        int i4;
        long j7;
        boolean z6;
        long j8;
        SparseArray sparseArray2;
        MessageObject messageObject2;
        TLRPC$Message tLRPC$Message;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z7 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i5 = 0;
        int i6 = 0;
        boolean z8 = false;
        boolean z9 = false;
        boolean z10 = false;
        boolean z11 = false;
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
                z3 = z7;
                i3 = i6;
                z5 = z8;
                z8 = z5;
                i6 = i3 + 1;
                arrayList3 = arrayList;
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
                            long j9;
                            j9 = ((NotificationsController.StoryNotification) obj).date;
                            return j9;
                        }
                    }));
                    z3 = z7;
                    i3 = i6;
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
                        j2 = messageObject3.getDialogId();
                    } else {
                        long j9 = messageObject3.messageOwner.peer_id.channel_id;
                        j2 = j9 != 0 ? -j9 : 0L;
                    }
                    SparseArray sparseArray3 = (SparseArray) this.pushMessagesDict.get(j2);
                    MessageObject messageObject4 = sparseArray3 != null ? (MessageObject) sparseArray3.get(id2) : null;
                    long j10 = j;
                    if (messageObject4 == null) {
                        long j11 = messageObject3.messageOwner.random_id;
                        if (j11 != 0 && (messageObject4 = (MessageObject) this.fcmRandomMessagesDict.get(j11)) != null) {
                            i3 = i6;
                            z5 = z8;
                            this.fcmRandomMessagesDict.remove(messageObject3.messageOwner.random_id);
                            messageObject = messageObject4;
                            if (messageObject == null) {
                                if (messageObject.isFcmMessage()) {
                                    if (sparseArray3 == null) {
                                        sparseArray3 = new SparseArray();
                                        this.pushMessagesDict.put(j2, sparseArray3);
                                    }
                                    sparseArray3.put(id2, messageObject3);
                                    int indexOf = this.pushMessages.indexOf(messageObject);
                                    if (indexOf >= 0) {
                                        this.pushMessages.set(indexOf, messageObject3);
                                        messageObject2 = messageObject3;
                                        i5 = addToPopupMessages(arrayList2, messageObject3, dialogId2, z4, notificationsSettings);
                                    } else {
                                        messageObject2 = messageObject3;
                                    }
                                    if (z) {
                                        boolean z14 = messageObject2.localEdit;
                                        if (z14) {
                                            getMessagesStorage().putPushMessage(messageObject2);
                                        }
                                        z9 = z14;
                                    }
                                }
                            } else if (!z9) {
                                if (z) {
                                    getMessagesStorage().putPushMessage(messageObject3);
                                }
                                long topicId = MessageObject.getTopicId(this.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
                                if (dialogId2 != this.openedDialogId || !ApplicationLoader.isScreenOn || messageObject3.isStoryReactionPush) {
                                    TLRPC$Message tLRPC$Message3 = messageObject3.messageOwner;
                                    if (!tLRPC$Message3.mentioned) {
                                        j3 = dialogId2;
                                    } else if (z3 || !(tLRPC$Message3.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                        j3 = messageObject3.getFromChatId();
                                    }
                                    if (isPersonalMessage(messageObject3)) {
                                        this.personalCount++;
                                    }
                                    DialogObject.isChatDialog(j3);
                                    int indexOfKey = longSparseArray.indexOfKey(j3);
                                    if (indexOfKey < 0 || topicId != 0) {
                                        sparseArray = sparseArray3;
                                        j4 = dialogId2;
                                        j5 = j10;
                                        long j12 = j3;
                                        j6 = j2;
                                        i4 = i5;
                                        int notifyOverride = getNotifyOverride(notificationsSettings, j3, topicId);
                                        boolean isGlobalNotificationsEnabled = notifyOverride == -1 ? isGlobalNotificationsEnabled(j12, Boolean.valueOf(z4), messageObject3.isReactionPush, messageObject3.isStoryReactionPush) : notifyOverride != 2;
                                        j7 = j12;
                                        longSparseArray.put(j7, Boolean.valueOf(isGlobalNotificationsEnabled));
                                        z6 = isGlobalNotificationsEnabled;
                                    } else {
                                        z6 = ((Boolean) longSparseArray.valueAt(indexOfKey)).booleanValue();
                                        sparseArray = sparseArray3;
                                        j6 = j2;
                                        i4 = i5;
                                        j4 = dialogId2;
                                        j5 = j10;
                                        j7 = j3;
                                    }
                                    if (z6) {
                                        if (z) {
                                            j8 = j7;
                                            i5 = i4;
                                        } else {
                                            j8 = j7;
                                            i5 = addToPopupMessages(arrayList2, messageObject3, j7, z4, notificationsSettings);
                                        }
                                        if (!z10) {
                                            z10 = messageObject3.messageOwner.from_scheduled;
                                        }
                                        this.delayedPushMessages.add(messageObject3);
                                        appendMessage(messageObject3);
                                        if (id2 != 0) {
                                            if (sparseArray == null) {
                                                sparseArray2 = new SparseArray();
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
                                            Integer num2 = (Integer) this.pushDialogsOverrideMention.get(j13);
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
                                    z8 = true;
                                } else if (!z) {
                                    playInChatSound();
                                }
                            }
                            z8 = z5;
                        }
                    }
                    i3 = i6;
                    z5 = z8;
                    messageObject = messageObject4;
                    if (messageObject == null) {
                    }
                    z8 = z5;
                }
                i6 = i3 + 1;
                arrayList3 = arrayList;
                z7 = z3;
            }
            z3 = z7;
            i3 = i6;
            z5 = z8;
            z8 = z5;
            i6 = i3 + 1;
            arrayList3 = arrayList;
            z7 = z3;
        }
        final int i7 = i5;
        boolean z15 = z8;
        if (z15) {
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
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z15) {
                MessageObject messageObject5 = (MessageObject) arrayList.get(0);
                long dialogId3 = messageObject5.getDialogId();
                long topicId2 = MessageObject.getTopicId(this.currentAccount, messageObject5.messageOwner, getMessagesController().isForum(dialogId3));
                Boolean valueOf = messageObject5.isFcmMessage() ? Boolean.valueOf(messageObject5.localChannel) : null;
                int i8 = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(notificationsSettings, dialogId3, topicId2);
                boolean isGlobalNotificationsEnabled2 = notifyOverride2 == -1 ? isGlobalNotificationsEnabled(dialogId3, valueOf, messageObject5.isReactionPush, messageObject5.isStoryReactionPush) : notifyOverride2 != 2;
                Integer num3 = (Integer) this.pushDialogs.get(dialogId3);
                if (num3 != null) {
                    i = 1;
                    i2 = num3.intValue() + 1;
                } else {
                    i = 1;
                    i2 = 1;
                }
                if (this.notifyCheck && !isGlobalNotificationsEnabled2 && (num = (Integer) this.pushDialogsOverrideMention.get(dialogId3)) != null && num.intValue() != 0) {
                    i2 = num.intValue();
                    isGlobalNotificationsEnabled2 = true;
                }
                if (isGlobalNotificationsEnabled2 && !messageObject5.isStoryPush) {
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
    /* JADX WARN: Removed duplicated region for block: B:23:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void resetNotificationSound(NotificationCompat.Builder builder, long j, long j2, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str2;
        String str3;
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

    /* JADX WARN: Can't wrap try/catch for region: R(86:54|(2:56|(3:58|59|60)(4:61|(2:64|62)|65|66))(1:702)|67|(1:69)(2:(1:699)(1:701)|700)|70|(4:73|(2:75|76)(1:78)|77|71)|79|80|(5:82|(2:(1:85)(1:604)|86)(1:605)|(1:603)(2:92|(2:96|97))|602|97)(2:606|(4:(1:681)(1:615)|616|(7:618|(2:620|(1:622)(5:634|(2:636|637)|638|349|350))(2:639|(6:643|(1:645)|624|625|(1:627)(2:629|(1:631)(2:632|633))|628))|623|624|625|(0)(0)|628)(2:647|(2:649|(1:651)(5:652|(2:654|637)|638|349|350))(9:655|(1:680)(1:659)|660|(1:679)(2:664|(1:666))|678|668|(2:670|(2:672|(1:674)(2:675|633)))(1:677)|676|(0)(0)))|60)(3:682|(6:684|(3:686|(2:688|689)|690)(2:691|(3:693|(2:695|689)|690))|348|349|350|60)(1:697)|696))|(1:103)|(4:105|(1:107)(1:600)|108|109)(1:601)|110|(3:112|(3:114|(1:116)(3:587|588|(3:590|(1:592)(1:594)|593))|117)(1:598)|595)(1:599)|(3:119|(1:125)|126)(1:586)|127|(3:581|(1:583)(1:585)|584)(2:130|131)|132|(1:134)|135|(1:137)(1:573)|138|(2:571|572)(1:142)|143|144|(3:147|(1:149)|(3:151|152|(64:156|157|158|(54:162|163|164|(1:559)(1:168)|169|(1:558)(1:172)|173|174|(1:557)|181|(1:556)(1:188)|189|(14:191|(1:193)(2:345|(5:347|348|349|350|60)(2:351|(1:(1:354)(11:355|195|196|197|(2:200|198)|201|202|(1:344)(1:205)|206|(1:208)(1:343)|209))(2:356|(11:358|196|197|(1:198)|201|202|(0)|344|206|(0)(0)|209)(11:359|(1:364)(1:363)|197|(1:198)|201|202|(0)|344|206|(0)(0)|209))))|194|195|196|197|(1:198)|201|202|(0)|344|206|(0)(0)|209)(4:365|(6:367|(3:373|(1:375)(2:549|(1:553))|(2:377|(1:379))(23:380|(1:382)|383|(2:545|(1:547)(1:548))(1:389)|390|391|(12:(1:394)(2:541|(1:543))|395|(2:(1:410)(2:398|(2:(2:401|(1:403))(1:406)|404)(2:407|(2:409|404)))|405)|411|(3:510|(1:540)(3:516|(2:538|539)(4:519|(1:523)|(1:537)(2:529|(1:533))|536)|534)|535)(1:415)|416|(7:418|(1:508)(7:431|(1:507)(3:435|(9:489|490|491|492|493|494|495|496|497)(1:437)|438)|439|(1:441)(1:488)|442|443|(6:477|478|479|480|481|(5:473|(1:475)|451|452|(2:457|(3:459|(2:464|465)(1:461)|(1:463)))))(4:445|(2:476|(0))|447|(0)))|449|450|451|452|(3:455|457|(0)))(1:509)|468|(3:472|371|372)|370|371|372)|544|395|(0)|411|(1:413)|510|(1:512)|540|535|416|(0)(0)|468|(4:470|472|371|372)|370|371|372))|369|370|371|372)|554|555)|210|(2:328|(4:330|(2:333|331)|334|335)(2:336|(1:338)(2:339|(1:341)(1:342))))(1:214)|215|(1:217)|218|(1:220)|221|(2:223|(1:225)(1:323))(2:324|(1:326)(1:327))|(1:227)(1:322)|228|(4:230|(2:233|231)|234|235)(1:321)|236|(1:238)|239|240|241|242|243|244|245|246|247|(1:249)|250|(1:252)|(1:254)|(1:261)|262|(1:310)(1:268)|269|(1:271)|(1:273)|274|(3:279|(4:281|(3:283|(4:285|(1:287)|288|289)(2:291|292)|290)|293|294)|295)|296|(1:309)(2:299|(1:303))|304|(1:306)|307|308|60)|564|(1:166)|559|169|(0)|558|173|174|(1:176)|557|181|(1:184)|556|189|(0)(0)|210|(1:212)|328|(0)(0)|215|(0)|218|(0)|221|(0)(0)|(0)(0)|228|(0)(0)|236|(0)|239|240|241|242|243|244|245|246|247|(0)|250|(0)|(0)|(2:256|261)|262|(1:264)|310|269|(0)|(0)|274|(4:276|279|(0)|295)|296|(0)|309|304|(0)|307|308|60)))|570|564|(0)|559|169|(0)|558|173|174|(0)|557|181|(0)|556|189|(0)(0)|210|(0)|328|(0)(0)|215|(0)|218|(0)|221|(0)(0)|(0)(0)|228|(0)(0)|236|(0)|239|240|241|242|243|244|245|246|247|(0)|250|(0)|(0)|(0)|262|(0)|310|269|(0)|(0)|274|(0)|296|(0)|309|304|(0)|307|308|60) */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x03b2, code lost:
        if (r10.local_id != 0) goto L625;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0457, code lost:
        if (r1.local_id != 0) goto L668;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x10a7, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x10b9, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x10ba, code lost:
        r2 = r49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x10bd, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x10be, code lost:
        r4 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x10c1, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:105:0x031f  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x049b  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x04a4  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x055b  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x056f  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0578  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x05ca  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x05d1  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x05f8  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0672  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0683  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x06bd  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x06c8  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x06d0  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0722  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x077a  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x078a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:319:0x079f  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x07b7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:338:0x07e3  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x091b A[LOOP:5: B:362:0x0913->B:364:0x091b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:367:0x0937 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:372:0x095a  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x096d  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0979  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0a99  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x0b6e  */
    /* JADX WARN: Removed duplicated region for block: B:511:0x0c78  */
    /* JADX WARN: Removed duplicated region for block: B:513:0x0c7c  */
    /* JADX WARN: Removed duplicated region for block: B:526:0x0cab  */
    /* JADX WARN: Removed duplicated region for block: B:530:0x0d05  */
    /* JADX WARN: Removed duplicated region for block: B:537:0x0d44 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:542:0x0d56  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0d9b  */
    /* JADX WARN: Removed duplicated region for block: B:553:0x0db0  */
    /* JADX WARN: Removed duplicated region for block: B:562:0x0e29  */
    /* JADX WARN: Removed duplicated region for block: B:567:0x0e47  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x0e77  */
    /* JADX WARN: Removed duplicated region for block: B:581:0x0ebf  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x0ee0  */
    /* JADX WARN: Removed duplicated region for block: B:587:0x0f3c  */
    /* JADX WARN: Removed duplicated region for block: B:591:0x0f7c  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x0fa5  */
    /* JADX WARN: Removed duplicated region for block: B:597:0x0fc8  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x0fe5  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x100c  */
    /* JADX WARN: Removed duplicated region for block: B:608:0x1042  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:616:0x109f A[Catch: Exception -> 0x10a7, TryCatch #10 {Exception -> 0x10a7, blocks: (B:614:0x1098, B:616:0x109f, B:619:0x10a9), top: B:741:0x1098 }] */
    /* JADX WARN: Removed duplicated region for block: B:627:0x10c6  */
    /* JADX WARN: Removed duplicated region for block: B:629:0x10d1  */
    /* JADX WARN: Removed duplicated region for block: B:631:0x10d6  */
    /* JADX WARN: Removed duplicated region for block: B:639:0x10ed  */
    /* JADX WARN: Removed duplicated region for block: B:647:0x1105  */
    /* JADX WARN: Removed duplicated region for block: B:649:0x110b  */
    /* JADX WARN: Removed duplicated region for block: B:652:0x1117  */
    /* JADX WARN: Removed duplicated region for block: B:657:0x1124  */
    /* JADX WARN: Removed duplicated region for block: B:670:0x11ab A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:679:0x11dd  */
    /* JADX WARN: Removed duplicated region for block: B:683:0x1265  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0217  */
    /* JADX WARN: Removed duplicated region for block: B:690:0x12b0  */
    /* JADX WARN: Removed duplicated region for block: B:696:0x12c9  */
    /* JADX WARN: Removed duplicated region for block: B:706:0x1318  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:733:0x072c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:745:0x0c85 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0286  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02a9  */
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
        String str5;
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
        String str6;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$User tLRPC$User3;
        TLRPC$TL_forumTopic findTopic;
        StringBuilder sb;
        String str7;
        String userName;
        TLRPC$FileLocation tLRPC$FileLocation4;
        TLRPC$User tLRPC$User4;
        int i14;
        String str8;
        String str9;
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
        String str10;
        int i15;
        String formatString;
        NotificationCompat.Action build;
        Integer num3;
        DialogKey dialogKey3;
        long j9;
        long j10;
        Person person;
        NotificationCompat.MessagingStyle messagingStyle;
        int i16;
        NotificationsController notificationsController4;
        NotificationCompat.Action action;
        DialogKey dialogKey4;
        String str11;
        MessageObject messageObject2;
        ArrayList arrayList7;
        long j11;
        StringBuilder sb2;
        LongSparseArray longSparseArray8;
        String str12;
        ArrayList<StoryNotification> arrayList8;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i17;
        Bitmap bitmap3;
        String str13;
        String str14;
        long j12;
        NotificationCompat.MessagingStyle messagingStyle3;
        long j13;
        StringBuilder sb3;
        LongSparseArray longSparseArray9;
        Person person2;
        String str15;
        String str16;
        boolean z11;
        String[] strArr;
        StringBuilder sb4;
        Person person3;
        File file4;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation6;
        String str17;
        LongSparseArray longSparseArray10;
        String str18;
        NotificationCompat.MessagingStyle messagingStyle4;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        int id2;
        List messages;
        Uri uri2;
        File file5;
        final File file6;
        Context context;
        StringBuilder sb5;
        final Uri uriForFile;
        File file7;
        Bitmap createScaledBitmap;
        Canvas canvas;
        int i18;
        DialogKey dialogKey5;
        int i19;
        MessageObject messageObject3;
        long j14;
        NotificationCompat.Action action2;
        ArrayList arrayList9;
        Bitmap bitmap4;
        String str19;
        NotificationCompat.Action action3;
        String str20;
        long j15;
        long j16;
        ArrayList<StoryNotification> arrayList10;
        long j17;
        NotificationCompat.Builder category;
        long j18;
        TLRPC$User tLRPC$User6;
        int size3;
        int i20;
        int i21;
        String str21;
        ArrayList arrayList11;
        LongSparseArray longSparseArray11;
        String string2;
        int i22;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation7;
        Bitmap bitmap5;
        Bitmap decodeFile;
        String formatPluralString;
        String str22;
        String str23;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3;
        TLRPC$FileLocation tLRPC$FileLocation8;
        int i23 = Build.VERSION.SDK_INT;
        if (i23 >= 26) {
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
        if (i23 <= 19) {
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
        for (int i24 = 0; i24 < notificationsController5.pushMessages.size(); i24++) {
            MessageObject messageObject4 = notificationsController5.pushMessages.get(i24);
            long dialogId = messageObject4.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController5.currentAccount, messageObject4.messageOwner, getMessagesController().isForum(messageObject4));
            int i25 = notificationsSettings.getInt("dismissDate" + dialogId, 0);
            if (messageObject4.isStoryPush || messageObject4.messageOwner.date > i25) {
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
        for (int i26 = 0; i26 < notificationsController5.wearNotificationsIds.size(); i26++) {
            longSparseArray13.put(notificationsController5.wearNotificationsIds.keyAt(i26), (Integer) notificationsController5.wearNotificationsIds.valueAt(i26));
        }
        notificationsController5.wearNotificationsIds.clear();
        ArrayList arrayList15 = new ArrayList();
        int i27 = Build.VERSION.SDK_INT;
        if (i27 > 27) {
            if (arrayList12.size() <= (notificationsController5.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i27 >= 26) {
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
                    int i28 = size;
                    if (dialogKey.story) {
                        i8 = i5;
                        long j19 = dialogKey.dialogId;
                        z6 = z4;
                        long j20 = dialogKey.topicId;
                        ArrayList<StoryNotification> arrayList16 = (ArrayList) longSparseArray12.get(j19);
                        arrayList2 = arrayList16;
                        arrayList3 = arrayList15;
                        id = ((MessageObject) arrayList16.get(0)).getId();
                        longSparseArray4 = longSparseArray12;
                        arrayList4 = arrayList12;
                        j3 = j20;
                        j4 = j19;
                        notification = build2;
                        messageObject = (MessageObject) arrayList16.get(0);
                    } else {
                        ArrayList<StoryNotification> arrayList17 = new ArrayList<>();
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
                            i12 = i28;
                            str8 = str3;
                            arrayList6 = arrayList15;
                            i5 = i13 + 1;
                            arrayList15 = arrayList6;
                            size = i12;
                            notificationsSettings = sharedPreferences;
                            arrayList12 = arrayList4;
                            longSparseArray12 = longSparseArray4;
                            clientUserId = j8;
                            z4 = z10;
                            str3 = str8;
                            longSparseArray13 = longSparseArray7;
                            longSparseArray = longSparseArray6;
                            build2 = notification2;
                            i4 = -1;
                            notificationsController5 = notificationsController2;
                        } else {
                            i8 = i5;
                            long j21 = notificationsController5.storyPushMessages.get(0).dialogId;
                            int i29 = 0;
                            for (Integer num4 : notificationsController5.storyPushMessages.get(0).dateByIds.keySet()) {
                                i29 = Math.max(i29, num4.intValue());
                            }
                            longSparseArray4 = longSparseArray12;
                            notification = build2;
                            messageObject = null;
                            ArrayList arrayList18 = arrayList15;
                            id = i29;
                            arrayList2 = arrayList17;
                            z6 = z4;
                            j4 = j21;
                            arrayList3 = arrayList18;
                            arrayList4 = arrayList12;
                            j3 = 0;
                        }
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
                    int i30 = 0;
                    for (i10 = 0; i10 < arrayList2.size(); i10++) {
                        if (i30 < ((MessageObject) arrayList2.get(i10)).messageOwner.date) {
                            i30 = ((MessageObject) arrayList2.get(i10)).messageOwner.date;
                        }
                    }
                    if (dialogKey.story) {
                        longSparseArray5 = longSparseArray13;
                        i11 = i30;
                        if (DialogObject.isEncryptedDialog(j4)) {
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
                                        str5 = "not found secret chat to show dialog notification " + encryptedChatId;
                                        FileLog.w(str5);
                                    }
                                    notificationsController2 = this;
                                    longSparseArray6 = longSparseArray;
                                    sharedPreferences = notificationsSettings;
                                    i12 = i28;
                                    i13 = i8;
                                    z10 = z6;
                                    arrayList6 = arrayList3;
                                    notification2 = notification;
                                    longSparseArray7 = longSparseArray5;
                                    j8 = j6;
                                } else {
                                    tLRPC$User = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                                    if (tLRPC$User == null) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            str5 = "not found secret chat user to show dialog notification " + encryptedChat.user_id;
                                            FileLog.w(str5);
                                        }
                                        notificationsController2 = this;
                                        longSparseArray6 = longSparseArray;
                                        sharedPreferences = notificationsSettings;
                                        i12 = i28;
                                        i13 = i8;
                                        z10 = z6;
                                        arrayList6 = arrayList3;
                                        notification2 = notification;
                                        longSparseArray7 = longSparseArray5;
                                        j8 = j6;
                                    }
                                }
                                str8 = str3;
                                i5 = i13 + 1;
                                arrayList15 = arrayList6;
                                size = i12;
                                notificationsSettings = sharedPreferences;
                                arrayList12 = arrayList4;
                                longSparseArray12 = longSparseArray4;
                                clientUserId = j8;
                                z4 = z10;
                                str3 = str8;
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
                        } else {
                            z7 = (messageObject == null || messageObject.isReactionPush || messageObject.isStoryReactionPush || j4 == 777000) ? false : true;
                            if (DialogObject.isUserDialog(j4)) {
                                TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j4));
                                if (user2 != null) {
                                    userName = UserObject.getUserName(user2);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user2.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        tLRPC$User4 = user2;
                                        arrayList5 = arrayList2;
                                        if (tLRPC$FileLocation4.volume_id != 0) {
                                        }
                                        tLRPC$FileLocation4 = null;
                                        if (!UserObject.isReplyUser(j4)) {
                                            i14 = R.string.RepliesTitle;
                                        } else if (j4 == clientUserId) {
                                            i14 = R.string.MessageScheduledReminderNotification;
                                        } else {
                                            j6 = clientUserId;
                                            dialogKey2 = dialogKey;
                                            str6 = userName;
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
                                            string = str6;
                                            tLRPC$FileLocation = tLRPC$FileLocation3;
                                        }
                                        string = LocaleController.getString(i14);
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
                                    longSparseArray6 = longSparseArray;
                                    sharedPreferences = notificationsSettings;
                                    notificationsController2 = notificationsController5;
                                    i12 = i28;
                                    i13 = i8;
                                    z10 = z6;
                                    arrayList6 = arrayList3;
                                    notification2 = notification;
                                    longSparseArray7 = longSparseArray5;
                                    j8 = clientUserId;
                                    str8 = str3;
                                }
                                tLRPC$User4 = user2;
                                arrayList5 = arrayList2;
                                tLRPC$FileLocation4 = null;
                                if (!UserObject.isReplyUser(j4)) {
                                }
                                string = LocaleController.getString(i14);
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
                                if (chat != null) {
                                    boolean z12 = chat.megagroup;
                                    z8 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str24 = chat.title;
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
                                            str6 = findTopic.title + " in " + str24;
                                            if (z7) {
                                                tLRPC$FileLocation3 = tLRPC$FileLocation;
                                                tLRPC$Chat2 = chat;
                                                tLRPC$User3 = null;
                                                tLRPC$User2 = tLRPC$User3;
                                                tLRPC$Chat = tLRPC$Chat2;
                                                string = str6;
                                                tLRPC$FileLocation = tLRPC$FileLocation3;
                                            } else {
                                                z7 = ChatObject.canSendPlain(chat);
                                                tLRPC$Chat = chat;
                                                string = str6;
                                                tLRPC$User2 = null;
                                            }
                                        }
                                    } else {
                                        j6 = clientUserId;
                                        num2 = num;
                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                        j7 = j5;
                                    }
                                    str6 = str24;
                                    if (z7) {
                                    }
                                } else if (messageObject.isFcmMessage()) {
                                    boolean isSupergroup = messageObject.isSupergroup();
                                    String str25 = messageObject.localName;
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
                                    string = str25;
                                } else {
                                    if (BuildVars.LOGS_ENABLED) {
                                        sb = new StringBuilder();
                                        str7 = "not found chat to show dialog notification ";
                                        sb.append(str7);
                                        sb.append(j4);
                                        FileLog.w(sb.toString());
                                    }
                                    longSparseArray6 = longSparseArray;
                                    sharedPreferences = notificationsSettings;
                                    notificationsController2 = notificationsController5;
                                    i12 = i28;
                                    i13 = i8;
                                    z10 = z6;
                                    arrayList6 = arrayList3;
                                    notification2 = notification;
                                    longSparseArray7 = longSparseArray5;
                                    j8 = clientUserId;
                                    str8 = str3;
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
                            str3 = str8;
                            longSparseArray13 = longSparseArray7;
                            longSparseArray = longSparseArray6;
                            build2 = notification2;
                            i4 = -1;
                            notificationsController5 = notificationsController2;
                        }
                    } else {
                        TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j4));
                        longSparseArray5 = longSparseArray13;
                        if (notificationsController5.storyPushMessages.size() == 1) {
                            formatPluralString = user3 != null ? UserObject.getFirstName(user3) : notificationsController5.storyPushMessages.get(0).localName;
                            i11 = i30;
                        } else {
                            i11 = i30;
                            formatPluralString = LocaleController.formatPluralString("Stories", notificationsController5.storyPushMessages.size(), new Object[0]);
                        }
                        if (user3 == null || (tLRPC$UserProfilePhoto3 = user3.photo) == null || (tLRPC$FileLocation8 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            str22 = "Stories";
                            str23 = formatPluralString;
                        } else {
                            str22 = "Stories";
                            str23 = formatPluralString;
                            if (tLRPC$FileLocation8.volume_id != 0 && tLRPC$FileLocation8.local_id != 0) {
                                j6 = clientUserId;
                                dialogKey2 = dialogKey;
                                tLRPC$FileLocation = tLRPC$FileLocation8;
                                str4 = str22;
                                num2 = num;
                                z7 = false;
                                z8 = false;
                                tLRPC$Chat = null;
                                z9 = false;
                                tLRPC$User2 = user3;
                                string = str23;
                                arrayList5 = arrayList2;
                                j7 = j5;
                            }
                        }
                        j6 = clientUserId;
                        dialogKey2 = dialogKey;
                        str4 = str22;
                        num2 = num;
                        tLRPC$FileLocation = null;
                        z7 = false;
                        z8 = false;
                        tLRPC$Chat = null;
                        z9 = false;
                        tLRPC$User2 = user3;
                        string = str23;
                        arrayList5 = arrayList2;
                        j7 = j5;
                    }
                    if (messageObject != null && messageObject.isStoryReactionPush && !notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                        string = LocaleController.getString(R.string.NotificationHiddenChatName);
                        tLRPC$FileLocation = null;
                        z7 = false;
                    }
                    if (z5) {
                        TLRPC$FileLocation tLRPC$FileLocation9 = tLRPC$FileLocation;
                        str9 = string;
                        tLRPC$FileLocation5 = tLRPC$FileLocation9;
                    } else {
                        str9 = LocaleController.getString(DialogObject.isChatDialog(j4) ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName);
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
                        Person.Builder name = new Person.Builder().setName(str9);
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
                        str10 = "max_id";
                        tLRPC$Chat3 = tLRPC$Chat;
                        file3 = file2;
                        i15 = i9;
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
                        str10 = "max_id";
                        PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent, 167772160);
                        RemoteInput build3 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString(R.string.Reply)).build();
                        if (DialogObject.isChatDialog(j4)) {
                            i15 = i9;
                            formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, str9);
                        } else {
                            i15 = i9;
                            formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, str9);
                        }
                        build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build3).setShowsUserInterface(false).build();
                    }
                    num3 = (Integer) notificationsController3.pushDialogs.get(j4);
                    if (num3 == null) {
                        num3 = 0;
                    }
                    dialogKey3 = dialogKey2;
                    int size4 = !dialogKey3.story ? notificationsController3.storyPushMessages.size() : Math.max(num3.intValue(), arrayList5.size());
                    String format = (size4 > 1 || Build.VERSION.SDK_INT >= 28) ? str9 : String.format("%1$s (%2$d)", str9, Integer.valueOf(size4));
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
                                        String str26 = "";
                                        messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                                        i16 = Build.VERSION.SDK_INT;
                                        if (i16 >= 28) {
                                        }
                                        messagingStyle.setConversationTitle(format);
                                        messagingStyle.setGroupConversation(i16 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                        StringBuilder sb6 = new StringBuilder();
                                        String[] strArr2 = new String[1];
                                        boolean[] zArr = new boolean[1];
                                        if (dialogKey3.story) {
                                        }
                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append("com.tmessages.openchat");
                                        String str27 = str12;
                                        ArrayList arrayList19 = arrayList7;
                                        sb7.append(Math.random());
                                        sb7.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent2.setAction(sb7.toString());
                                        intent2.setFlags(ConnectionsManager.FileTypeFile);
                                        intent2.addCategory("android.intent.category.LAUNCHER");
                                        MessageObject messageObject5 = messageObject2;
                                        if (messageObject2 == null) {
                                        }
                                        dialogKey5 = dialogKey4;
                                        if (dialogKey5.story) {
                                        }
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append("show extra notifications chatId ");
                                        sb8.append(j4);
                                        sb8.append(" topicId ");
                                        j14 = j10;
                                        sb8.append(j14);
                                        FileLog.d(sb8.toString());
                                        if (j14 != 0) {
                                        }
                                        String str28 = str11;
                                        intent2.putExtra(str28, notificationsController4.currentAccount);
                                        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                        action2 = action;
                                        if (action != null) {
                                        }
                                        int i31 = i19;
                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                        intent3.addFlags(32);
                                        intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent3.putExtra("dialog_id", j4);
                                        int i32 = i15;
                                        intent3.putExtra(str10, i32);
                                        intent3.putExtra(str28, notificationsController4.currentAccount);
                                        arrayList9 = arrayList19;
                                        bitmap4 = bitmap3;
                                        NotificationCompat.Action build4 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (DialogObject.isEncryptedDialog(j4)) {
                                        }
                                        if (str20 != null) {
                                        }
                                        StringBuilder sb9 = new StringBuilder();
                                        sb9.append("tgaccount");
                                        long j22 = j11;
                                        sb9.append(j22);
                                        wearableExtender.setBridgeTag(sb9.toString());
                                        if (dialogKey5.story) {
                                        }
                                        NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str13).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                        if (dialogKey5.story) {
                                        }
                                        category = autoCancel.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent4.putExtra("messageDate", i11);
                                        j18 = j15;
                                        intent4.putExtra("dialogId", j18);
                                        String str29 = str19;
                                        intent4.putExtra(str29, notificationsController4.currentAccount);
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
                                        i12 = i28;
                                        j8 = j16;
                                        z10 = z13;
                                        i13 = i8;
                                        longSparseArray6 = longSparseArray8;
                                        longSparseArray7 = longSparseArray5;
                                        str8 = str27;
                                        sharedPreferences = sharedPreferences2;
                                        notification2 = notification3;
                                        arrayList6 = arrayList3;
                                        arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str13, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
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
                                        str3 = str8;
                                        longSparseArray13 = longSparseArray7;
                                        longSparseArray = longSparseArray6;
                                        build2 = notification2;
                                        i4 = -1;
                                        notificationsController5 = notificationsController2;
                                    }
                                    String str262 = "";
                                    messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                                    i16 = Build.VERSION.SDK_INT;
                                    if (i16 >= 28 || ((DialogObject.isChatDialog(j4) && !z8) || UserObject.isReplyUser(j4))) {
                                        messagingStyle.setConversationTitle(format);
                                    }
                                    messagingStyle.setGroupConversation(i16 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                    StringBuilder sb62 = new StringBuilder();
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
                                            string2 = LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]);
                                        } else if (arrayList20.isEmpty()) {
                                            longSparseArray6 = longSparseArray;
                                            notificationsController2 = notificationsController4;
                                            i12 = i28;
                                            i13 = i8;
                                            z10 = z6;
                                            arrayList6 = arrayList3;
                                            notification2 = notification;
                                            sharedPreferences = sharedPreferences2;
                                            longSparseArray7 = longSparseArray5;
                                            j8 = j9;
                                            str8 = str3;
                                            i5 = i13 + 1;
                                            arrayList15 = arrayList6;
                                            size = i12;
                                            notificationsSettings = sharedPreferences;
                                            arrayList12 = arrayList4;
                                            longSparseArray12 = longSparseArray4;
                                            clientUserId = j8;
                                            z4 = z10;
                                            str3 = str8;
                                            longSparseArray13 = longSparseArray7;
                                            longSparseArray = longSparseArray6;
                                            build2 = notification2;
                                            i4 = -1;
                                            notificationsController5 = notificationsController2;
                                        } else if (arrayList20.size() != 1) {
                                            action = build;
                                            if (arrayList20.size() == 2) {
                                                str11 = "currentAccount";
                                                sb62.append(LocaleController.formatString(R.string.StoryNotification2, arrayList20.get(0), arrayList20.get(1)));
                                                longSparseArray11 = longSparseArray;
                                                dialogKey4 = dialogKey3;
                                                messageObject2 = messageObject;
                                                long j23 = Long.MAX_VALUE;
                                                while (i22 < notificationsController4.storyPushMessages.size()) {
                                                }
                                                messagingStyle.setGroupConversation(false);
                                                if (arrayList20.size() == 1) {
                                                }
                                                messagingStyle.addMessage(sb62, j23, new Person.Builder().setName(r0).build());
                                                if (booleanValue) {
                                                }
                                                arrayList7 = null;
                                                arrayList8 = arrayList5;
                                            } else {
                                                str11 = "currentAccount";
                                                if (arrayList20.size() == 3 && notificationsController4.storyPushMessages.size() == 3) {
                                                    dialogKey4 = dialogKey3;
                                                    messageObject2 = messageObject;
                                                    sb62.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController4.cutLastName(arrayList20.get(0)), notificationsController4.cutLastName(arrayList20.get(1)), notificationsController4.cutLastName(arrayList20.get(2))));
                                                    longSparseArray11 = longSparseArray;
                                                } else {
                                                    dialogKey4 = dialogKey3;
                                                    messageObject2 = messageObject;
                                                    longSparseArray11 = longSparseArray;
                                                    sb62.append(LocaleController.formatPluralString("StoryNotification4", notificationsController4.storyPushMessages.size() - 2, notificationsController4.cutLastName(arrayList20.get(0)), notificationsController4.cutLastName(arrayList20.get(1))));
                                                }
                                                long j232 = Long.MAX_VALUE;
                                                while (i22 < notificationsController4.storyPushMessages.size()) {
                                                }
                                                messagingStyle.setGroupConversation(false);
                                                if (arrayList20.size() == 1) {
                                                }
                                                messagingStyle.addMessage(sb62, j232, new Person.Builder().setName(r0).build());
                                                if (booleanValue) {
                                                }
                                                arrayList7 = null;
                                                arrayList8 = arrayList5;
                                            }
                                        } else if (intValue == 1) {
                                            string2 = LocaleController.getString("StoryNotificationSingle");
                                        } else {
                                            action = build;
                                            sb62.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList20.get(0)));
                                            longSparseArray11 = longSparseArray;
                                            dialogKey4 = dialogKey3;
                                            str11 = "currentAccount";
                                            messageObject2 = messageObject;
                                            long j2322 = Long.MAX_VALUE;
                                            for (i22 = 0; i22 < notificationsController4.storyPushMessages.size(); i22++) {
                                                j2322 = Math.min(notificationsController4.storyPushMessages.get(i22).date, j2322);
                                            }
                                            messagingStyle.setGroupConversation(false);
                                            String formatPluralString2 = (arrayList20.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str4, intValue, new Object[0]) : arrayList20.get(0);
                                            messagingStyle.addMessage(sb62, j2322, new Person.Builder().setName(formatPluralString2).build());
                                            if (booleanValue) {
                                                bitmap3 = loadMultipleAvatars(arrayList21);
                                                str13 = formatPluralString2;
                                                messagingStyle2 = messagingStyle;
                                                sb2 = sb62;
                                                longSparseArray8 = longSparseArray11;
                                                str12 = str3;
                                                j11 = j9;
                                                i17 = 0;
                                            } else {
                                                str13 = formatPluralString2;
                                                messagingStyle2 = messagingStyle;
                                                sb2 = sb62;
                                                longSparseArray8 = longSparseArray11;
                                                str12 = str3;
                                                j11 = j9;
                                                i17 = 0;
                                                bitmap3 = null;
                                            }
                                            arrayList7 = null;
                                            arrayList8 = arrayList5;
                                        }
                                        sb62.append(string2);
                                        longSparseArray11 = longSparseArray;
                                        action = build;
                                        dialogKey4 = dialogKey3;
                                        str11 = "currentAccount";
                                        messageObject2 = messageObject;
                                        long j23222 = Long.MAX_VALUE;
                                        while (i22 < notificationsController4.storyPushMessages.size()) {
                                        }
                                        messagingStyle.setGroupConversation(false);
                                        if (arrayList20.size() == 1) {
                                        }
                                        messagingStyle.addMessage(sb62, j23222, new Person.Builder().setName(formatPluralString2).build());
                                        if (booleanValue) {
                                        }
                                        arrayList7 = null;
                                        arrayList8 = arrayList5;
                                    } else {
                                        notificationsController4 = this;
                                        LongSparseArray longSparseArray14 = longSparseArray;
                                        action = build;
                                        dialogKey4 = dialogKey3;
                                        str11 = "currentAccount";
                                        messageObject2 = messageObject;
                                        long j24 = j9;
                                        int size5 = arrayList5.size() - 1;
                                        int i33 = 0;
                                        arrayList7 = null;
                                        while (size5 >= 0) {
                                            int i34 = i33;
                                            ArrayList<StoryNotification> arrayList22 = arrayList5;
                                            ArrayList arrayList23 = arrayList7;
                                            MessageObject messageObject6 = (MessageObject) arrayList22.get(size5);
                                            int i35 = size5;
                                            if (j10 == MessageObject.getTopicId(notificationsController4.currentAccount, messageObject6.messageOwner, getMessagesController().isForum(messageObject6))) {
                                                String shortStringForMessage = notificationsController4.getShortStringForMessage(messageObject6, strArr22, zArr2);
                                                if (j4 == j24) {
                                                    strArr22[0] = str9;
                                                } else if (DialogObject.isChatDialog(j4) && messageObject6.messageOwner.from_scheduled) {
                                                    strArr22[0] = LocaleController.getString(R.string.NotificationMessageScheduledName);
                                                }
                                                if (shortStringForMessage != null) {
                                                    if (sb62.length() > 0) {
                                                        sb62.append("\n\n");
                                                    }
                                                    if (j4 != j24 && messageObject6.messageOwner.from_scheduled && DialogObject.isUserDialog(j4)) {
                                                        str14 = str9;
                                                        j12 = j24;
                                                        shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString(R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                        sb62.append(shortStringForMessage);
                                                        messagingStyle3 = messagingStyle;
                                                    } else {
                                                        str14 = str9;
                                                        j12 = j24;
                                                        String str30 = strArr22[0];
                                                        messagingStyle3 = messagingStyle;
                                                        if (str30 != null) {
                                                            sb62.append(String.format("%1$s: %2$s", str30, shortStringForMessage));
                                                        } else {
                                                            sb62.append(shortStringForMessage);
                                                        }
                                                    }
                                                    String str31 = shortStringForMessage;
                                                    if (!DialogObject.isUserDialog(j4)) {
                                                        if (z8) {
                                                            j13 = -j4;
                                                        } else if (DialogObject.isChatDialog(j4)) {
                                                            j13 = messageObject6.getSenderId();
                                                        }
                                                        sb3 = sb62;
                                                        longSparseArray9 = longSparseArray14;
                                                        person2 = (Person) longSparseArray9.get(j13 + (j10 << 16));
                                                        str15 = strArr22[0];
                                                        if (str15 == null) {
                                                            if (z5) {
                                                                if (DialogObject.isChatDialog(j4)) {
                                                                    if (!z8) {
                                                                        i18 = R.string.NotificationHiddenChatUserName;
                                                                    } else if (Build.VERSION.SDK_INT > 27) {
                                                                        i18 = R.string.NotificationHiddenChatName;
                                                                    }
                                                                    str15 = LocaleController.getString(i18);
                                                                } else if (Build.VERSION.SDK_INT > 27) {
                                                                    i18 = R.string.NotificationHiddenName;
                                                                    str15 = LocaleController.getString(i18);
                                                                }
                                                            }
                                                            str15 = str262;
                                                        }
                                                        if (person2 == null && TextUtils.equals(person2.getName(), str15)) {
                                                            str16 = str262;
                                                            z11 = z8;
                                                            strArr = strArr22;
                                                            person3 = person2;
                                                            sb4 = sb3;
                                                        } else {
                                                            Person.Builder name3 = new Person.Builder().setName(str15);
                                                            if (zArr2[0] || DialogObject.isEncryptedDialog(j4) || Build.VERSION.SDK_INT < 28) {
                                                                str16 = str262;
                                                                z11 = z8;
                                                                strArr = strArr22;
                                                                sb4 = sb3;
                                                            } else {
                                                                if (DialogObject.isUserDialog(j4) || z8) {
                                                                    str16 = str262;
                                                                    z11 = z8;
                                                                    strArr = strArr22;
                                                                    sb4 = sb3;
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
                                                                        str16 = str262;
                                                                        sb4 = sb3;
                                                                    } else {
                                                                        str16 = str262;
                                                                        sb4 = sb3;
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
                                                                str17 = str16;
                                                                longSparseArray10 = longSparseArray9;
                                                                str18 = str3;
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
                                                                            str17 = str16;
                                                                            NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str31, messageObject6.messageOwner.date * 1000, person3);
                                                                            String str32 = !messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                            if (file5.exists()) {
                                                                            }
                                                                            if (j4 == 777000) {
                                                                            }
                                                                            arrayList7 = arrayList23;
                                                                            id2 = i34;
                                                                            i4 = -1;
                                                                            size5 = i35 - 1;
                                                                            str262 = str17;
                                                                            str3 = str18;
                                                                            sb62 = sb4;
                                                                            z8 = z11;
                                                                            arrayList5 = arrayList22;
                                                                            str9 = str14;
                                                                            strArr22 = strArr;
                                                                            longSparseArray14 = longSparseArray10;
                                                                            i33 = id2;
                                                                            messagingStyle = messagingStyle4;
                                                                            j24 = j12;
                                                                        }
                                                                    }
                                                                    file5 = file7;
                                                                } else {
                                                                    longSparseArray10 = longSparseArray9;
                                                                    file5 = pathToMessage;
                                                                    file6 = null;
                                                                }
                                                                str17 = str16;
                                                                NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str31, messageObject6.messageOwner.date * 1000, person3);
                                                                String str322 = !messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                if (file5.exists()) {
                                                                    str18 = str3;
                                                                    if (getFileLoader().isLoadingFile(file5.getName())) {
                                                                        Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                        StringBuilder sb10 = new StringBuilder();
                                                                        sb10.append(notificationsController4.currentAccount);
                                                                        str17 = str17;
                                                                        sb10.append(str17);
                                                                        uriForFile = appendPath.appendPath(sb10.toString()).appendPath(file5.getName()).appendQueryParameter("final_path", file5.getAbsolutePath()).build();
                                                                        if (uriForFile != null) {
                                                                        }
                                                                    }
                                                                    uriForFile = null;
                                                                    if (uriForFile != null) {
                                                                    }
                                                                } else {
                                                                    try {
                                                                        context = ApplicationLoader.applicationContext;
                                                                        sb5 = new StringBuilder();
                                                                        sb5.append(ApplicationLoader.getApplicationId());
                                                                        str18 = str3;
                                                                    } catch (Exception e4) {
                                                                        e = e4;
                                                                        str18 = str3;
                                                                    }
                                                                    try {
                                                                        sb5.append(str18);
                                                                        uriForFile = FileProvider.getUriForFile(context, sb5.toString(), file5);
                                                                    } catch (Exception e5) {
                                                                        e = e5;
                                                                        FileLog.e(e);
                                                                        uriForFile = null;
                                                                        if (uriForFile != null) {
                                                                        }
                                                                        messagingStyle4 = messagingStyle3;
                                                                        messagingStyle4.addMessage(str31, messageObject6.messageOwner.date * 1000, person3);
                                                                        if (zArr2[0]) {
                                                                        }
                                                                        if (j4 == 777000) {
                                                                        }
                                                                        arrayList7 = arrayList23;
                                                                        id2 = i34;
                                                                        i4 = -1;
                                                                        size5 = i35 - 1;
                                                                        str262 = str17;
                                                                        str3 = str18;
                                                                        sb62 = sb4;
                                                                        z8 = z11;
                                                                        arrayList5 = arrayList22;
                                                                        str9 = str14;
                                                                        strArr22 = strArr;
                                                                        longSparseArray14 = longSparseArray10;
                                                                        i33 = id2;
                                                                        messagingStyle = messagingStyle4;
                                                                        j24 = j12;
                                                                    }
                                                                    if (uriForFile != null) {
                                                                        message2.setData(str322, uriForFile);
                                                                        messagingStyle4 = messagingStyle3;
                                                                        messagingStyle4.addMessage(message2);
                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda55
                                                                            @Override // java.lang.Runnable
                                                                            public final void run() {
                                                                                NotificationsController.lambda$showExtraNotifications$42(uriForFile, file6);
                                                                            }
                                                                        }, 20000L);
                                                                        if (!TextUtils.isEmpty(messageObject6.caption)) {
                                                                            messagingStyle4.addMessage(messageObject6.caption, messageObject6.messageOwner.date * 1000, person3);
                                                                        }
                                                                        if (zArr2[0] && !z5 && messageObject6.isVoice()) {
                                                                            messages = messagingStyle4.getMessages();
                                                                            if (!messages.isEmpty()) {
                                                                                File pathToMessage2 = getFileLoader().getPathToMessage(messageObject6.messageOwner);
                                                                                if (Build.VERSION.SDK_INT >= 24) {
                                                                                    try {
                                                                                        uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + str18, pathToMessage2);
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
                                                                    }
                                                                }
                                                            }
                                                            messagingStyle4 = messagingStyle3;
                                                            messagingStyle4.addMessage(str31, messageObject6.messageOwner.date * 1000, person3);
                                                            if (zArr2[0]) {
                                                                messages = messagingStyle4.getMessages();
                                                                if (!messages.isEmpty()) {
                                                                }
                                                            }
                                                        } else {
                                                            str17 = str16;
                                                            longSparseArray10 = longSparseArray9;
                                                            str18 = str3;
                                                            messagingStyle4 = messagingStyle3;
                                                            messagingStyle4.addMessage(str31, messageObject6.messageOwner.date * 1000, person3);
                                                        }
                                                        if (j4 == 777000 && (tLRPC$ReplyMarkup = messageObject6.messageOwner.reply_markup) != null) {
                                                            ArrayList arrayList24 = tLRPC$ReplyMarkup.rows;
                                                            id2 = messageObject6.getId();
                                                            arrayList7 = arrayList24;
                                                            i4 = -1;
                                                            size5 = i35 - 1;
                                                            str262 = str17;
                                                            str3 = str18;
                                                            sb62 = sb4;
                                                            z8 = z11;
                                                            arrayList5 = arrayList22;
                                                            str9 = str14;
                                                            strArr22 = strArr;
                                                            longSparseArray14 = longSparseArray10;
                                                            i33 = id2;
                                                            messagingStyle = messagingStyle4;
                                                            j24 = j12;
                                                        }
                                                        arrayList7 = arrayList23;
                                                        id2 = i34;
                                                        i4 = -1;
                                                        size5 = i35 - 1;
                                                        str262 = str17;
                                                        str3 = str18;
                                                        sb62 = sb4;
                                                        z8 = z11;
                                                        arrayList5 = arrayList22;
                                                        str9 = str14;
                                                        strArr22 = strArr;
                                                        longSparseArray14 = longSparseArray10;
                                                        i33 = id2;
                                                        messagingStyle = messagingStyle4;
                                                        j24 = j12;
                                                    }
                                                    j13 = j4;
                                                    sb3 = sb62;
                                                    longSparseArray9 = longSparseArray14;
                                                    person2 = (Person) longSparseArray9.get(j13 + (j10 << 16));
                                                    str15 = strArr22[0];
                                                    if (str15 == null) {
                                                    }
                                                    if (person2 == null) {
                                                    }
                                                    Person.Builder name32 = new Person.Builder().setName(str15);
                                                    if (zArr2[0]) {
                                                    }
                                                    str16 = str262;
                                                    z11 = z8;
                                                    strArr = strArr22;
                                                    sb4 = sb3;
                                                    Person build52 = name32.build();
                                                    longSparseArray9.put(j13, build52);
                                                    person3 = build52;
                                                    if (DialogObject.isEncryptedDialog(j4)) {
                                                    }
                                                    if (j4 == 777000) {
                                                        ArrayList arrayList242 = tLRPC$ReplyMarkup.rows;
                                                        id2 = messageObject6.getId();
                                                        arrayList7 = arrayList242;
                                                        i4 = -1;
                                                        size5 = i35 - 1;
                                                        str262 = str17;
                                                        str3 = str18;
                                                        sb62 = sb4;
                                                        z8 = z11;
                                                        arrayList5 = arrayList22;
                                                        str9 = str14;
                                                        strArr22 = strArr;
                                                        longSparseArray14 = longSparseArray10;
                                                        i33 = id2;
                                                        messagingStyle = messagingStyle4;
                                                        j24 = j12;
                                                    }
                                                    arrayList7 = arrayList23;
                                                    id2 = i34;
                                                    i4 = -1;
                                                    size5 = i35 - 1;
                                                    str262 = str17;
                                                    str3 = str18;
                                                    sb62 = sb4;
                                                    z8 = z11;
                                                    arrayList5 = arrayList22;
                                                    str9 = str14;
                                                    strArr22 = strArr;
                                                    longSparseArray14 = longSparseArray10;
                                                    i33 = id2;
                                                    messagingStyle = messagingStyle4;
                                                    j24 = j12;
                                                } else if (BuildVars.LOGS_ENABLED) {
                                                    FileLog.w("message text is null for " + messageObject6.getId() + " did = " + messageObject6.getDialogId());
                                                }
                                            }
                                            str14 = str9;
                                            j12 = j24;
                                            strArr = strArr22;
                                            longSparseArray10 = longSparseArray14;
                                            str18 = str3;
                                            messagingStyle4 = messagingStyle;
                                            sb4 = sb62;
                                            z11 = z8;
                                            str17 = str262;
                                            arrayList7 = arrayList23;
                                            id2 = i34;
                                            i4 = -1;
                                            size5 = i35 - 1;
                                            str262 = str17;
                                            str3 = str18;
                                            sb62 = sb4;
                                            z8 = z11;
                                            arrayList5 = arrayList22;
                                            str9 = str14;
                                            strArr22 = strArr;
                                            longSparseArray14 = longSparseArray10;
                                            i33 = id2;
                                            messagingStyle = messagingStyle4;
                                            j24 = j12;
                                        }
                                        j11 = j24;
                                        sb2 = sb62;
                                        int i36 = i33;
                                        longSparseArray8 = longSparseArray14;
                                        str12 = str3;
                                        arrayList8 = arrayList5;
                                        messagingStyle2 = messagingStyle;
                                        i17 = i36;
                                        bitmap3 = bitmap6;
                                        str13 = str9;
                                    }
                                    Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                    StringBuilder sb72 = new StringBuilder();
                                    sb72.append("com.tmessages.openchat");
                                    String str272 = str12;
                                    ArrayList arrayList192 = arrayList7;
                                    sb72.append(Math.random());
                                    sb72.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    intent22.setAction(sb72.toString());
                                    intent22.setFlags(ConnectionsManager.FileTypeFile);
                                    intent22.addCategory("android.intent.category.LAUNCHER");
                                    MessageObject messageObject52 = messageObject2;
                                    if (messageObject2 == null && messageObject52.isStoryReactionPush) {
                                        intent22.putExtra("storyId", Math.abs(messageObject52.getId()));
                                        i19 = i17;
                                        messageObject3 = messageObject52;
                                        dialogKey5 = dialogKey4;
                                    } else {
                                        dialogKey5 = dialogKey4;
                                        if (dialogKey5.story) {
                                            long[] jArr2 = new long[notificationsController4.storyPushMessages.size()];
                                            int i37 = 0;
                                            while (i37 < notificationsController4.storyPushMessages.size()) {
                                                jArr2[i37] = notificationsController4.storyPushMessages.get(i37).dialogId;
                                                i37++;
                                                i17 = i17;
                                                messageObject52 = messageObject52;
                                            }
                                            i19 = i17;
                                            messageObject3 = messageObject52;
                                            intent22.putExtra("storyDialogIds", jArr2);
                                        } else {
                                            i19 = i17;
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
                                    StringBuilder sb82 = new StringBuilder();
                                    sb82.append("show extra notifications chatId ");
                                    sb82.append(j4);
                                    sb82.append(" topicId ");
                                    j14 = j10;
                                    sb82.append(j14);
                                    FileLog.d(sb82.toString());
                                    if (j14 != 0) {
                                        intent22.putExtra("topicId", j14);
                                    }
                                    String str282 = str11;
                                    intent22.putExtra(str282, notificationsController4.currentAccount);
                                    PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                    NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                    action2 = action;
                                    if (action != null) {
                                        wearableExtender2.addAction(action2);
                                    }
                                    int i312 = i19;
                                    Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                    intent32.addFlags(32);
                                    intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                    intent32.putExtra("dialog_id", j4);
                                    int i322 = i15;
                                    intent32.putExtra(str10, i322);
                                    intent32.putExtra(str282, notificationsController4.currentAccount);
                                    arrayList9 = arrayList192;
                                    bitmap4 = bitmap3;
                                    NotificationCompat.Action build42 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                    if (DialogObject.isEncryptedDialog(j4)) {
                                        str19 = str282;
                                        action3 = build42;
                                        str20 = j4 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j4) + "_" + i322 : null;
                                    } else if (DialogObject.isUserDialog(j4)) {
                                        str19 = str282;
                                        action3 = build42;
                                        str20 = "tguser" + j4 + "_" + i322;
                                    } else {
                                        StringBuilder sb11 = new StringBuilder();
                                        sb11.append("tgchat");
                                        str19 = str282;
                                        action3 = build42;
                                        sb11.append(-j4);
                                        sb11.append("_");
                                        sb11.append(i322);
                                        str20 = sb11.toString();
                                    }
                                    if (str20 != null) {
                                        wearableExtender2.setDismissalId(str20);
                                        NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                        wearableExtender3.setDismissalId("summary_" + str20);
                                        builder.extend(wearableExtender3);
                                    }
                                    StringBuilder sb92 = new StringBuilder();
                                    sb92.append("tgaccount");
                                    long j222 = j11;
                                    sb92.append(j222);
                                    wearableExtender2.setBridgeTag(sb92.toString());
                                    if (dialogKey5.story) {
                                        j16 = j222;
                                        j17 = Long.MAX_VALUE;
                                        int i38 = 0;
                                        while (i38 < notificationsController4.storyPushMessages.size()) {
                                            j17 = Math.min(notificationsController4.storyPushMessages.get(i38).date, j17);
                                            i38++;
                                            j4 = j4;
                                        }
                                        j15 = j4;
                                        arrayList10 = arrayList8;
                                    } else {
                                        j15 = j4;
                                        j16 = j222;
                                        arrayList10 = arrayList8;
                                        j17 = ((MessageObject) arrayList10.get(0)).messageOwner.date * 1000;
                                    }
                                    NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str13).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                    if (dialogKey5.story) {
                                        arrayList10 = notificationsController4.storyPushMessages;
                                    }
                                    category = autoCancel2.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                    Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                    intent42.putExtra("messageDate", i11);
                                    j18 = j15;
                                    intent42.putExtra("dialogId", j18);
                                    String str292 = str19;
                                    intent42.putExtra(str292, notificationsController4.currentAccount);
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
                                        i20 = 0;
                                        while (i20 < size3) {
                                            ArrayList arrayList25 = arrayList9;
                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = (TLRPC$TL_keyboardButtonRow) arrayList25.get(i20);
                                            int size6 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                            int i39 = 0;
                                            while (i39 < size6) {
                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = (TLRPC$KeyboardButton) tLRPC$TL_keyboardButtonRow.buttons.get(i39);
                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                    i21 = size3;
                                                    arrayList11 = arrayList25;
                                                    Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                    intent5.putExtra(str292, notificationsController4.currentAccount);
                                                    intent5.putExtra("did", j18);
                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                    if (bArr != null) {
                                                        intent5.putExtra("data", bArr);
                                                    }
                                                    intent5.putExtra("mid", i312);
                                                    String str33 = tLRPC$KeyboardButton.text;
                                                    Context context2 = ApplicationLoader.applicationContext;
                                                    str21 = str292;
                                                    int i40 = notificationsController4.lastButtonId;
                                                    notificationsController4.lastButtonId = i40 + 1;
                                                    category.addAction(0, str33, PendingIntent.getBroadcast(context2, i40, intent5, 167772160));
                                                } else {
                                                    i21 = size3;
                                                    str21 = str292;
                                                    arrayList11 = arrayList25;
                                                }
                                                i39++;
                                                size3 = i21;
                                                arrayList25 = arrayList11;
                                                str292 = str21;
                                            }
                                            i20++;
                                            arrayList9 = arrayList25;
                                        }
                                    }
                                    if (tLRPC$Chat3 == null || tLRPC$User5 == null) {
                                        tLRPC$User6 = tLRPC$User5;
                                    } else {
                                        tLRPC$User6 = tLRPC$User5;
                                        String str34 = tLRPC$User6.phone;
                                        if (str34 != null && str34.length() > 0) {
                                            category.addPerson("tel:+" + tLRPC$User6.phone);
                                        }
                                    }
                                    boolean z132 = z6;
                                    Notification notification32 = notification;
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        notificationsController4.setNotificationChannel(notification32, category, z132);
                                    }
                                    i12 = i28;
                                    j8 = j16;
                                    z10 = z132;
                                    i13 = i8;
                                    longSparseArray6 = longSparseArray8;
                                    longSparseArray7 = longSparseArray5;
                                    str8 = str272;
                                    sharedPreferences = sharedPreferences2;
                                    notification2 = notification32;
                                    arrayList6 = arrayList3;
                                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str13, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
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
                                    str3 = str8;
                                    longSparseArray13 = longSparseArray7;
                                    longSparseArray = longSparseArray6;
                                    build2 = notification2;
                                    i4 = -1;
                                    notificationsController5 = notificationsController2;
                                }
                                person = person4;
                                String str2622 = "";
                                messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                                i16 = Build.VERSION.SDK_INT;
                                if (i16 >= 28) {
                                }
                                messagingStyle.setConversationTitle(format);
                                messagingStyle.setGroupConversation(i16 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                StringBuilder sb622 = new StringBuilder();
                                String[] strArr222 = new String[1];
                                boolean[] zArr22 = new boolean[1];
                                if (dialogKey3.story) {
                                }
                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb722 = new StringBuilder();
                                sb722.append("com.tmessages.openchat");
                                String str2722 = str12;
                                ArrayList arrayList1922 = arrayList7;
                                sb722.append(Math.random());
                                sb722.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent222.setAction(sb722.toString());
                                intent222.setFlags(ConnectionsManager.FileTypeFile);
                                intent222.addCategory("android.intent.category.LAUNCHER");
                                MessageObject messageObject522 = messageObject2;
                                if (messageObject2 == null) {
                                }
                                dialogKey5 = dialogKey4;
                                if (dialogKey5.story) {
                                }
                                StringBuilder sb822 = new StringBuilder();
                                sb822.append("show extra notifications chatId ");
                                sb822.append(j4);
                                sb822.append(" topicId ");
                                j14 = j10;
                                sb822.append(j14);
                                FileLog.d(sb822.toString());
                                if (j14 != 0) {
                                }
                                String str2822 = str11;
                                intent222.putExtra(str2822, notificationsController4.currentAccount);
                                PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                action2 = action;
                                if (action != null) {
                                }
                                int i3122 = i19;
                                Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent322.addFlags(32);
                                intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent322.putExtra("dialog_id", j4);
                                int i3222 = i15;
                                intent322.putExtra(str10, i3222);
                                intent322.putExtra(str2822, notificationsController4.currentAccount);
                                arrayList9 = arrayList1922;
                                bitmap4 = bitmap3;
                                NotificationCompat.Action build422 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j4)) {
                                }
                                if (str20 != null) {
                                }
                                StringBuilder sb922 = new StringBuilder();
                                sb922.append("tgaccount");
                                long j2222 = j11;
                                sb922.append(j2222);
                                wearableExtender22.setBridgeTag(sb922.toString());
                                if (dialogKey5.story) {
                                }
                                NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str13).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                if (dialogKey5.story) {
                                }
                                category = autoCancel22.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent422.putExtra("messageDate", i11);
                                j18 = j15;
                                intent422.putExtra("dialogId", j18);
                                String str2922 = str19;
                                intent422.putExtra(str2922, notificationsController4.currentAccount);
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
                                    i20 = 0;
                                    while (i20 < size3) {
                                    }
                                }
                                if (tLRPC$Chat3 == null) {
                                }
                                tLRPC$User6 = tLRPC$User5;
                                boolean z1322 = z6;
                                Notification notification322 = notification;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                i12 = i28;
                                j8 = j16;
                                z10 = z1322;
                                i13 = i8;
                                longSparseArray6 = longSparseArray8;
                                longSparseArray7 = longSparseArray5;
                                str8 = str2722;
                                sharedPreferences = sharedPreferences2;
                                notification2 = notification322;
                                arrayList6 = arrayList3;
                                arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str13, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
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
                                str3 = str8;
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
                    String str26222 = "";
                    messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                    i16 = Build.VERSION.SDK_INT;
                    if (i16 >= 28) {
                    }
                    messagingStyle.setConversationTitle(format);
                    messagingStyle.setGroupConversation(i16 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                    StringBuilder sb6222 = new StringBuilder();
                    String[] strArr2222 = new String[1];
                    boolean[] zArr222 = new boolean[1];
                    if (dialogKey3.story) {
                    }
                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    StringBuilder sb7222 = new StringBuilder();
                    sb7222.append("com.tmessages.openchat");
                    String str27222 = str12;
                    ArrayList arrayList19222 = arrayList7;
                    sb7222.append(Math.random());
                    sb7222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent2222.setAction(sb7222.toString());
                    intent2222.setFlags(ConnectionsManager.FileTypeFile);
                    intent2222.addCategory("android.intent.category.LAUNCHER");
                    MessageObject messageObject5222 = messageObject2;
                    if (messageObject2 == null) {
                    }
                    dialogKey5 = dialogKey4;
                    if (dialogKey5.story) {
                    }
                    StringBuilder sb8222 = new StringBuilder();
                    sb8222.append("show extra notifications chatId ");
                    sb8222.append(j4);
                    sb8222.append(" topicId ");
                    j14 = j10;
                    sb8222.append(j14);
                    FileLog.d(sb8222.toString());
                    if (j14 != 0) {
                    }
                    String str28222 = str11;
                    intent2222.putExtra(str28222, notificationsController4.currentAccount);
                    PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                    action2 = action;
                    if (action != null) {
                    }
                    int i31222 = i19;
                    Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent3222.addFlags(32);
                    intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent3222.putExtra("dialog_id", j4);
                    int i32222 = i15;
                    intent3222.putExtra(str10, i32222);
                    intent3222.putExtra(str28222, notificationsController4.currentAccount);
                    arrayList9 = arrayList19222;
                    bitmap4 = bitmap3;
                    NotificationCompat.Action build4222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(j4)) {
                    }
                    if (str20 != null) {
                    }
                    StringBuilder sb9222 = new StringBuilder();
                    sb9222.append("tgaccount");
                    long j22222 = j11;
                    sb9222.append(j22222);
                    wearableExtender222.setBridgeTag(sb9222.toString());
                    if (dialogKey5.story) {
                    }
                    NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str13).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                    if (dialogKey5.story) {
                    }
                    category = autoCancel222.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                    Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent4222.putExtra("messageDate", i11);
                    j18 = j15;
                    intent4222.putExtra("dialogId", j18);
                    String str29222 = str19;
                    intent4222.putExtra(str29222, notificationsController4.currentAccount);
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
                    i12 = i28;
                    j8 = j16;
                    z10 = z13222;
                    i13 = i8;
                    longSparseArray6 = longSparseArray8;
                    longSparseArray7 = longSparseArray5;
                    str8 = str27222;
                    sharedPreferences = sharedPreferences2;
                    notification2 = notification3222;
                    arrayList6 = arrayList3;
                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str13, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
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
                    str3 = str8;
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
                if (z4) {
                    notificationsController = notificationsController6;
                    arrayList = arrayList26;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                } else {
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
                        long j25 = r4.dialogId;
                        longSparseArray3 = longSparseArray15;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j25, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j25), !r4.story);
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
            int i282 = size;
            if (dialogKey.story) {
            }
            Integer num52 = (Integer) longSparseArray13.get(j4);
            i9 = id;
            if (dialogKey.story) {
            }
            int i302 = 0;
            while (i10 < arrayList2.size()) {
            }
            if (dialogKey.story) {
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
            str10 = "max_id";
            PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent6, 167772160);
            RemoteInput build32 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString(R.string.Reply)).build();
            if (DialogObject.isChatDialog(j4)) {
            }
            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build32).setShowsUserInterface(false).build();
            num3 = (Integer) notificationsController3.pushDialogs.get(j4);
            if (num3 == null) {
            }
            dialogKey3 = dialogKey2;
            if (!dialogKey3.story) {
            }
            if (size4 > 1) {
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
            String str262222 = "";
            messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
            i16 = Build.VERSION.SDK_INT;
            if (i16 >= 28) {
            }
            messagingStyle.setConversationTitle(format);
            messagingStyle.setGroupConversation(i16 >= 28 || (!z8 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
            StringBuilder sb62222 = new StringBuilder();
            String[] strArr22222 = new String[1];
            boolean[] zArr2222 = new boolean[1];
            if (dialogKey3.story) {
            }
            Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            StringBuilder sb72222 = new StringBuilder();
            sb72222.append("com.tmessages.openchat");
            String str272222 = str12;
            ArrayList arrayList192222 = arrayList7;
            sb72222.append(Math.random());
            sb72222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent22222.setAction(sb72222.toString());
            intent22222.setFlags(ConnectionsManager.FileTypeFile);
            intent22222.addCategory("android.intent.category.LAUNCHER");
            MessageObject messageObject52222 = messageObject2;
            if (messageObject2 == null) {
            }
            dialogKey5 = dialogKey4;
            if (dialogKey5.story) {
            }
            StringBuilder sb82222 = new StringBuilder();
            sb82222.append("show extra notifications chatId ");
            sb82222.append(j4);
            sb82222.append(" topicId ");
            j14 = j10;
            sb82222.append(j14);
            FileLog.d(sb82222.toString());
            if (j14 != 0) {
            }
            String str282222 = str11;
            intent22222.putExtra(str282222, notificationsController4.currentAccount);
            PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1140850688);
            NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
            action2 = action;
            if (action != null) {
            }
            int i312222 = i19;
            Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent32222.addFlags(32);
            intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent32222.putExtra("dialog_id", j4);
            int i322222 = i15;
            intent32222.putExtra(str10, i322222);
            intent32222.putExtra(str282222, notificationsController4.currentAccount);
            arrayList9 = arrayList192222;
            bitmap4 = bitmap3;
            NotificationCompat.Action build42222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
            if (DialogObject.isEncryptedDialog(j4)) {
            }
            if (str20 != null) {
            }
            StringBuilder sb92222 = new StringBuilder();
            sb92222.append("tgaccount");
            long j222222 = j11;
            sb92222.append(j222222);
            wearableExtender2222.setBridgeTag(sb92222.toString());
            if (dialogKey5.story) {
            }
            NotificationCompat.Builder autoCancel2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str13).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
            if (dialogKey5.story) {
            }
            category = autoCancel2222.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
            Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent42222.putExtra("messageDate", i11);
            j18 = j15;
            intent42222.putExtra("dialogId", j18);
            String str292222 = str19;
            intent42222.putExtra(str292222, notificationsController4.currentAccount);
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
            i12 = i282;
            j8 = j16;
            z10 = z132222;
            i13 = i8;
            longSparseArray6 = longSparseArray8;
            longSparseArray7 = longSparseArray5;
            str8 = str272222;
            sharedPreferences = sharedPreferences2;
            notification2 = notification32222;
            arrayList6 = arrayList3;
            arrayList6.add(new 1NotificationHolder(num2.intValue(), j18, dialogKey5.story, j14, str13, tLRPC$User6, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
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
            str3 = str8;
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
        if (z4) {
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

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:454:0x0acb
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private void showOrUpdateNotification(boolean r56) {
        /*
            Method dump skipped, instructions count: 3650
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

    /* JADX WARN: Code restructure failed: missing block: B:193:0x0434, code lost:
        if (r9 != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x043c, code lost:
        if (r9 != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0445, code lost:
        if (r9 != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0452, code lost:
        if (r9 != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x0458, code lost:
        if (r9 != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x046d, code lost:
        if (r9 != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x046f, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0471, code lost:
        r4 = 2;
     */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0424  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x047b  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0510  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0530 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x056f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0582 A[LOOP:1: B:268:0x057d->B:270:0x0582, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0593  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x059f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x05b0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:290:0x05c8  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x05df  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x058c A[EDGE_INSN: B:322:0x058c->B:271:0x058c ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01cd  */
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
        String str23;
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
        String str24;
        String str25;
        String str26;
        long[] jArr4;
        SharedPreferences.Editor editor;
        boolean z8;
        String str27;
        boolean z9;
        String str28;
        int i6;
        String str29;
        String str30;
        String str31;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str32 = "stories";
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
            String str33 = str3;
            str4 = str2;
            str5 = str33;
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
            str32 = "silent";
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
                        str8 = str32;
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
            String str34 = str8 + "_" + MD52;
            string = notificationsSettings.getString(str34, null);
            String string3 = notificationsSettings.getString(str34 + "_s", null);
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
                    str17 = str34;
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
                    shouldVibrate = notificationChannel.shouldVibrate();
                    if (shouldVibrate || vibrationPattern != null) {
                        z7 = shouldVibrate;
                        z4 = z11;
                        jArr3 = vibrationPattern;
                    } else {
                        z7 = shouldVibrate;
                        z4 = z11;
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
                    if (MD53.equals(string3)) {
                        str11 = "_s";
                        jArr2 = jArr;
                        sb = sb4;
                        str24 = string3;
                        str25 = string;
                        str13 = "secret";
                        notificationsController = this;
                        str26 = MD53;
                        str16 = "_";
                        j3 = j;
                        str17 = str34;
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
                                str17 = str34;
                                str25 = string;
                                str31 = "secret";
                                str26 = MD53;
                                jArr4 = jArr3;
                                str30 = "_";
                                sb = sb4;
                                str24 = string3;
                            } else {
                                if (i3 == 3) {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY);
                                    jArr4 = jArr3;
                                    sb6.append(getSharedPrefKey(j, 0L));
                                    edit.putBoolean(sb6.toString(), false);
                                    str29 = "secret";
                                } else {
                                    str29 = "secret";
                                    jArr4 = jArr3;
                                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, 0L), 2);
                                }
                                str11 = "_s";
                                str24 = string3;
                                str17 = str34;
                                str25 = string;
                                sb = sb4;
                                str26 = MD53;
                                str30 = "_";
                                str31 = str29;
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            j3 = j;
                            str16 = str30;
                            str13 = str31;
                            editor = edit;
                        } else {
                            str11 = "_s";
                            str17 = str34;
                            str25 = string;
                            str13 = "secret";
                            str26 = MD53;
                            jArr4 = jArr3;
                            sb = sb4;
                            j3 = j;
                            str24 = string3;
                            if (importance == i2) {
                                str16 = "_";
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
                                            str28 = "vibrate_" + j3;
                                        } else if (i3 == 2) {
                                            str28 = "vibrate_channel";
                                        } else if (i3 == 0) {
                                            str28 = "vibrate_group";
                                        } else if (i3 == 3) {
                                            str28 = "vibrate_stories";
                                        } else if (i3 == 4 || i3 == 5) {
                                            str28 = "vibrate_react";
                                        } else {
                                            str28 = "vibrate_messages";
                                        }
                                        editor.putInt(str28, i6);
                                    }
                                    i4 = i;
                                    jArr2 = jArr4;
                                    z8 = true;
                                } else {
                                    i4 = i;
                                }
                                if (lightColor != i4) {
                                    if (!z2) {
                                        if (editor == null) {
                                            editor = notificationsSettings.edit();
                                        }
                                        editor.putInt(z ? i3 == 2 ? "ChannelLed" : i3 == 0 ? "GroupLed" : i3 == 3 ? "StoriesLed" : (i3 == 5 || i3 == 4) ? "ReactionsLed" : "MessagesLed" : "color_" + j3, lightColor);
                                    }
                                    i4 = lightColor;
                                    z8 = true;
                                }
                                if (editor != null) {
                                    editor.commit();
                                }
                                z5 = z8;
                            } else if (z2) {
                                str16 = "_";
                                editor = null;
                            } else {
                                SharedPreferences.Editor edit2 = notificationsSettings.edit();
                                str16 = "_";
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
                                    str27 = i3 == 2 ? "priority_channel" : i3 == 0 ? "priority_group" : i3 == 3 ? "priority_stories" : (i3 == 4 || i3 == 5) ? "priority_react" : "priority_messages";
                                } else if (i3 == 3) {
                                    edit2.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j3, true);
                                    editor = edit2;
                                } else {
                                    edit2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j3, 0);
                                    edit2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j3);
                                    str27 = "priority_" + j3;
                                }
                                edit2.putInt(str27, i7);
                                editor = edit2;
                            }
                        }
                        z8 = true;
                        notificationsController = this;
                        jArr2 = jArr;
                        z9 = z7;
                        if ((!notificationsController.isEmptyVibration(jArr2)) == z9) {
                        }
                        if (lightColor != i4) {
                        }
                        if (editor != null) {
                        }
                        z5 = z8;
                    }
                    str19 = str26;
                    str20 = str24;
                    str18 = str25;
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
                                    str23 = str16;
                                } else {
                                    str23 = str16;
                                    sb2 = new StringBuilder();
                                    sb2.append(notificationsController.currentAccount);
                                    sb2.append(str14);
                                    sb2.append(j3);
                                }
                                sb2.append(str23);
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
                    str17 = str34;
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
                str17 = str34;
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
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb8 = new StringBuilder();
            sb8.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb8.append(j);
            sb8.append("_");
            sb8.append(j2);
            str32 = sb8.toString();
        }
        str9 = formatString;
        str8 = str32;
        String str342 = str8 + "_" + MD52;
        string = notificationsSettings.getString(str342, null);
        String string32 = notificationsSettings.getString(str342 + "_s", null);
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
