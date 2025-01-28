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
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
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

    class 1NotificationHolder {
        TLRPC.Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        boolean story;
        long topicId;
        TLRPC.User user;
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

        1NotificationHolder(int i, long j, boolean z, long j2, String str, TLRPC.User user, TLRPC.Chat chat, NotificationCompat.Builder builder, long j3, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z2, boolean z3, boolean z4, int i4) {
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
            this.user = user;
            this.chat = chat;
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
        this.spoilerChars = new char[]{10252, 10338, 10385, 10280, 10277, 10286, 10321};
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

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0057, code lost:
    
        if (r0 == 2) goto L24;
     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0071  */
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

    /* JADX WARN: Removed duplicated region for block: B:22:0x00f4 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:8:0x0021, B:11:0x0062, B:12:0x006d, B:15:0x007d, B:17:0x00a6, B:19:0x00b6, B:20:0x00c0, B:22:0x00f4, B:23:0x00fc, B:25:0x0105, B:27:0x0126, B:30:0x013d, B:34:0x0154, B:36:0x010c, B:38:0x0112, B:39:0x0117, B:40:0x0115, B:41:0x011c, B:42:0x00f8, B:44:0x0079, B:45:0x0069), top: B:7:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0105 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:8:0x0021, B:11:0x0062, B:12:0x006d, B:15:0x007d, B:17:0x00a6, B:19:0x00b6, B:20:0x00c0, B:22:0x00f4, B:23:0x00fc, B:25:0x0105, B:27:0x0126, B:30:0x013d, B:34:0x0154, B:36:0x010c, B:38:0x0112, B:39:0x0117, B:40:0x0115, B:41:0x011c, B:42:0x00f8, B:44:0x0079, B:45:0x0069), top: B:7:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0126 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:8:0x0021, B:11:0x0062, B:12:0x006d, B:15:0x007d, B:17:0x00a6, B:19:0x00b6, B:20:0x00c0, B:22:0x00f4, B:23:0x00fc, B:25:0x0105, B:27:0x0126, B:30:0x013d, B:34:0x0154, B:36:0x010c, B:38:0x0112, B:39:0x0117, B:40:0x0115, B:41:0x011c, B:42:0x00f8, B:44:0x0079, B:45:0x0069), top: B:7:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0154 A[Catch: Exception -> 0x0066, TRY_LEAVE, TryCatch #0 {Exception -> 0x0066, blocks: (B:8:0x0021, B:11:0x0062, B:12:0x006d, B:15:0x007d, B:17:0x00a6, B:19:0x00b6, B:20:0x00c0, B:22:0x00f4, B:23:0x00fc, B:25:0x0105, B:27:0x0126, B:30:0x013d, B:34:0x0154, B:36:0x010c, B:38:0x0112, B:39:0x0117, B:40:0x0115, B:41:0x011c, B:42:0x00f8, B:44:0x0079, B:45:0x0069), top: B:7:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f8 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:8:0x0021, B:11:0x0062, B:12:0x006d, B:15:0x007d, B:17:0x00a6, B:19:0x00b6, B:20:0x00c0, B:22:0x00f4, B:23:0x00fc, B:25:0x0105, B:27:0x0126, B:30:0x013d, B:34:0x0154, B:36:0x010c, B:38:0x0112, B:39:0x0117, B:40:0x0115, B:41:0x011c, B:42:0x00f8, B:44:0x0079, B:45:0x0069), top: B:7:0x0021 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC.User user, TLRPC.Chat chat, Person person, boolean z) {
        Bitmap bitmap;
        IconCompat createWithResource;
        if (unsupportedNotificationShortcut() || (ChatObject.isChannel(chat) && !chat.megagroup)) {
            return null;
        }
        try {
            String str2 = "ndid_" + j;
            Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) OpenChatReceiver.class);
            intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
            if (j > 0) {
                intent.putExtra("userId", j);
            } else {
                intent.putExtra("chatId", -j);
            }
            ShortcutInfoCompat.Builder locusId = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(chat != null ? str : UserObject.getFirstName(user)).setLongLabel(str).setIntent(new Intent("android.intent.action.VIEW")).setIntent(intent).setLongLived(true).setLocusId(new LocusIdCompat(str2));
            if (person != null) {
                locusId.setPerson(person);
                locusId.setIcon(person.getIcon());
                if (person.getIcon() != null) {
                    bitmap = person.getIcon().getBitmap();
                    ShortcutInfoCompat build = locusId.build();
                    ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, build);
                    builder.setShortcutInfo(build);
                    Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) BubbleActivity.class);
                    intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    if (DialogObject.isUserDialog(j)) {
                        intent2.putExtra("chatId", -j);
                    } else {
                        intent2.putExtra("userId", j);
                    }
                    intent2.putExtra("currentAccount", this.currentAccount);
                    if (bitmap == null) {
                        createWithResource = IconCompat.createWithAdaptiveBitmap(bitmap);
                    } else if (user != null) {
                        createWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, user.bot ? R.drawable.book_bot : R.drawable.book_user);
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
            Intent intent22 = new Intent(ApplicationLoader.applicationContext, (Class<?>) BubbleActivity.class);
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
        if (indexOf < 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, indexOf));
        sb.append(str.endsWith("…") ? "…" : "");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deleteNotificationChannelInternal, reason: merged with bridge method [inline-methods] */
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

    private TLRPC.NotificationSound getInputSound(SharedPreferences sharedPreferences, String str, String str2, String str3) {
        long j = sharedPreferences.getLong(str2, 0L);
        String string = sharedPreferences.getString(str3, "NoSound");
        if (j != 0) {
            TLRPC.TL_notificationSoundRingtone tL_notificationSoundRingtone = new TLRPC.TL_notificationSoundRingtone();
            tL_notificationSoundRingtone.id = j;
            return tL_notificationSoundRingtone;
        }
        if (string == null) {
            return new TLRPC.TL_notificationSoundDefault();
        }
        if (string.equalsIgnoreCase("NoSound")) {
            return new TLRPC.TL_notificationSoundNone();
        }
        TLRPC.TL_notificationSoundLocal tL_notificationSoundLocal = new TLRPC.TL_notificationSoundLocal();
        tL_notificationSoundLocal.title = sharedPreferences.getString(str, null);
        tL_notificationSoundLocal.data = string;
        return tL_notificationSoundLocal;
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

    /* JADX WARN: Code restructure failed: missing block: B:134:0x0226, code lost:
    
        if (r6.getBoolean("EnablePreviewAll", true) == false) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x023e, code lost:
    
        r1 = r23.messageOwner;
        r3 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0250, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageService) == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0252, code lost:
    
        r24[0] = null;
        r2 = r1.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x025a, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionSetSameChatWallPaper) == false) goto L169;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0262, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.WallpaperSameNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0265, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionSetChatWallPaper) == false) goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x026d, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.WallpaperNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0270, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGeoProximityReached) == false) goto L177;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0278, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x027b, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserJoined) != false) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x027f, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionContactSignUp) == false) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0285, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto) == false) goto L186;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0293, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationContactNewPhoto, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0296, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionLoginUnknownLocation) == false) goto L190;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0298, code lost:
    
        r1 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().getFormatterYear().format(r23.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().getFormatterDay().format(r23.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r3 = getUserConfig().getCurrentUser().first_name;
        r0 = r23.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x02f6, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(r2, r3, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x02f9, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore) != false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x02fd, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent) != false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0301, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSentMe) == false) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0307, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionStarGift) != false) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x030b, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGiftPremium) == false) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0311, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall) == false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0315, code lost:
    
        if (r2.video == false) goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x031d, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0324, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0329, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser) == false) goto L255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x032b, code lost:
    
        r5 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x0331, code lost:
    
        if (r5 != 0) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x033a, code lost:
    
        if (r2.users.size() != 1) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x033c, code lost:
    
        r5 = r23.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x0351, code lost:
    
        if (r5 == 0) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x035b, code lost:
    
        if (r23.messageOwner.peer_id.channel_id == 0) goto L225;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x035f, code lost:
    
        if (r11.megagroup != false) goto L225;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0372, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelAddedByNotification, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x0378, code lost:
    
        if (r5 != r17) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0388, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationInvitedToGroup, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0389, code lost:
    
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0395, code lost:
    
        if (r0 != null) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0397, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x039d, code lost:
    
        if (r13 != r0.id) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x03a1, code lost:
    
        if (r11.megagroup == false) goto L239;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x03b4, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x03c6, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddSelf, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x03df, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddMember, r4, r11.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x03e0, code lost:
    
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x03f0, code lost:
    
        if (r2 >= r23.messageOwner.action.users.size()) goto L894;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x03f2, code lost:
    
        r3 = getMessagesController().getUser(r23.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x0406, code lost:
    
        if (r3 == null) goto L896;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x0408, code lost:
    
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0410, code lost:
    
        if (r1.length() == 0) goto L251;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0412, code lost:
    
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x0415, code lost:
    
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0418, code lost:
    
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x0433, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddMember, r4, r11.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x0436, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGroupCall) == false) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x043a, code lost:
    
        if (r2.duration == 0) goto L261;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x044d, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupEndedCall, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x045f, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupCreatedCall, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x0462, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGroupCallScheduled) == false) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x046a, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x046d, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionInviteToGroupCall) == false) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x046f, code lost:
    
        r5 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0475, code lost:
    
        if (r5 != 0) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x047e, code lost:
    
        if (r2.users.size() != 1) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0480, code lost:
    
        r5 = r23.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0495, code lost:
    
        if (r5 == 0) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x0499, code lost:
    
        if (r5 != r17) goto L280;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x04ac, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x04ad, code lost:
    
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x04b9, code lost:
    
        if (r0 != null) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x04bb, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x04d5, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r4, r11.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x04d6, code lost:
    
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x04e6, code lost:
    
        if (r2 >= r23.messageOwner.action.users.size()) goto L897;
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x04e8, code lost:
    
        r3 = getMessagesController().getUser(r23.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x04fc, code lost:
    
        if (r3 == null) goto L899;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x04fe, code lost:
    
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0506, code lost:
    
        if (r1.length() == 0) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0508, code lost:
    
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x050b, code lost:
    
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x050e, code lost:
    
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0529, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r4, r11.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x052c, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGiftCode) == false) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0534, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x0537, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByLink) == false) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x054b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0551, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditTitle) == false) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0562, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationEditedGroupName, r4, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0565, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto) != false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0569, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeletePhoto) == false) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x056f, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser) == false) goto L331;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0571, code lost:
    
        r1 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x0575, code lost:
    
        if (r1 != r17) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0588, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupKickYou, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x058e, code lost:
    
        if (r1 != r13) goto L325;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x059e, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupLeftMember, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x059f, code lost:
    
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r23.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x05b1, code lost:
    
        if (r0 != null) goto L329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x05b3, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x05cd, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupKickMember, r4, r11.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x05d0, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatCreate) == false) goto L335;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x05d8, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x05db, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate) == false) goto L339;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x05e3, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05e6, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo) == false) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x05f6, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x05fb, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom) == false) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0609, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x060c, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken) == false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0614, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0617, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGiveawayLaunch) == false) goto L355;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x061f, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0622, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGiveawayResults) == false) goto L359;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x062a, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x062d, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage) == false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0633, code lost:
    
        if (r11 == null) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0639, code lost:
    
        if (org.telegram.messenger.ChatObject.isChannel(r11) == false) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x063d, code lost:
    
        if (r11.megagroup == false) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0644, code lost:
    
        r0 = r23.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0646, code lost:
    
        if (r0 != null) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0659, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoText, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0661, code lost:
    
        if (r0.isMusic() == false) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0671, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedMusic, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0676, code lost:
    
        if (r0.isVideo() == false) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0680, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L383;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x06a9, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "📹 " + r0.messageOwner.message, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x06bb, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVideo, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x06c0, code lost:
    
        if (r0.isGif() == false) goto L393;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x06ca, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x06f3, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "🎬 " + r0.messageOwner.message, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0705, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGif, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x070d, code lost:
    
        if (r0.isVoice() == false) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x071d, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVoice, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x0722, code lost:
    
        if (r0.isRoundVideo() == false) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x0732, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedRound, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x0737, code lost:
    
        if (r0.isSticker() != false) goto L405;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x073d, code lost:
    
        if (r0.isAnimatedSticker() == false) goto L406;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x0743, code lost:
    
        r3 = r0.messageOwner;
        r7 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x0749, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0751, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x077a, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "📎 " + r0.messageOwner.message, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x078c, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedFile, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x078f, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0793, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L419;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x079c, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x07af, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x07b2, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L427;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x07b4, code lost:
    
        r7 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x07d2, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedContact2, r4, r11.title, org.telegram.messenger.ContactsController.formatName(r7.first_name, r7.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x07d5, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x07d7, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x07dd, code lost:
    
        if (r0.quiz == false) goto L433;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x07f7, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r4, r11.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0810, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r4, r11.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0813, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x081b, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L441;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0844, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "🖼 " + r0.messageOwner.message, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0856, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x085c, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame) == false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x086c, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGame, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x086d, code lost:
    
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x086f, code lost:
    
        if (r3 == null) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0875, code lost:
    
        if (r3.length() <= 0) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x0877, code lost:
    
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x087d, code lost:
    
        if (r0.length() <= 20) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x087f, code lost:
    
        r3 = new java.lang.StringBuilder();
        r7 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0895, code lost:
    
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = r11.title;
        r3 = new java.lang.Object[3];
        r3[r7] = r4;
        r3[1] = r0;
        r3[2] = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x08a8, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0894, code lost:
    
        r7 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x08ba, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoText, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x08c9, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeo, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x08ca, code lost:
    
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x08ce, code lost:
    
        if (r0 == null) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x08e2, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r4, r11.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x08f2, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedSticker, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08f3, code lost:
    
        r0 = r23.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x08f5, code lost:
    
        if (r11 == null) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x08f7, code lost:
    
        if (r0 != null) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0905, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x090a, code lost:
    
        if (r0.isMusic() == false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0918, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x091d, code lost:
    
        if (r0.isVideo() == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0927, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L482;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00a8, code lost:
    
        if (r6.getBoolean("EnablePreviewGroup", true) != false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x094d, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r11.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x095c, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0961, code lost:
    
        if (r0.isGif() == false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x096b, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0991, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r11.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x09a0, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x09a7, code lost:
    
        if (r0.isVoice() == false) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x09b5, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x09ba, code lost:
    
        if (r0.isRoundVideo() == false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x09c8, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x09cd, code lost:
    
        if (r0.isSticker() != false) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x09d3, code lost:
    
        if (r0.isAnimatedSticker() == false) goto L505;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x09d9, code lost:
    
        r3 = r0.messageOwner;
        r4 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x09df, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L513;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x09e7, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L511;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0a0d, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r11.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0a1c, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0a1f, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L517;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a23, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a2b, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a3b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00b4, code lost:
    
        if (r6.getBoolean("EnablePreviewChannel", r2) == false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0a3e, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0a40, code lost:
    
        r4 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0a5b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r11.title, org.telegram.messenger.ContactsController.formatName(r4.first_name, r4.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0a5e, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0a60, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r4).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0a66, code lost:
    
        if (r0.quiz == false) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0a7d, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r11.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0a93, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r11.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0a96, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0a9e, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0ac4, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r11.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0ad3, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0ad8, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame) == false) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0ae6, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0ae7, code lost:
    
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0ae9, code lost:
    
        if (r3 == null) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0aef, code lost:
    
        if (r3.length() <= 0) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0af1, code lost:
    
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0af7, code lost:
    
        if (r0.length() <= 20) goto L553;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0af9, code lost:
    
        r3 = new java.lang.StringBuilder();
        r7 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0b0f, code lost:
    
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r3 = new java.lang.Object[2];
        r3[r7] = r11.title;
        r3[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0b1f, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0b0e, code lost:
    
        r7 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0b2e, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0b3b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0b3c, code lost:
    
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0b40, code lost:
    
        if (r0 == null) goto L564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b51, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r11.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b5e, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0b5f, code lost:
    
        if (r0 != null) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0b6b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0b70, code lost:
    
        if (r0.isMusic() == false) goto L573;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0b7c, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0b81, code lost:
    
        if (r0.isVideo() == false) goto L581;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0b8b, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0baf, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r4, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0bbc, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0bc1, code lost:
    
        if (r0.isGif() == false) goto L589;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0bcb, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0bef, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r4, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0bfc, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0c03, code lost:
    
        if (r0.isVoice() == false) goto L593;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0c0f, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0c14, code lost:
    
        if (r0.isRoundVideo() == false) goto L597;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0c20, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0c25, code lost:
    
        if (r0.isSticker() != false) goto L601;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0c2b, code lost:
    
        if (r0.isAnimatedSticker() == false) goto L602;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0c31, code lost:
    
        r3 = r0.messageOwner;
        r7 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0c37, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0c3f, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0c63, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r4, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0c70, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0c73, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0c77, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L615;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0c7f, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L619;
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0c8d, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0c91, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0c93, code lost:
    
        r7 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0cab, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r4, org.telegram.messenger.ContactsController.formatName(r7.first_name, r7.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x0cae, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0cb0, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0cb6, code lost:
    
        if (r0.quiz == false) goto L629;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0ccb, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r4, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0cdf, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r4, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x0ce2, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L639;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x0cea, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x0d0e, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r4, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0d1b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0d20, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame) == false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x0d2c, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x0d2d, code lost:
    
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0d2f, code lost:
    
        if (r3 == null) goto L653;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0d35, code lost:
    
        if (r3.length() <= 0) goto L653;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x0d37, code lost:
    
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0d3d, code lost:
    
        if (r0.length() <= 20) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0d3f, code lost:
    
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x0d55, code lost:
    
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r2 = new java.lang.Object[2];
        r2[r5] = r4;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0d63, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x0d54, code lost:
    
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x0d70, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0d7b, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0d7c, code lost:
    
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x0d80, code lost:
    
        if (r0 == null) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x0d8f, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x0d9a, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x0d9d, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionSetChatTheme) == false) goto L676;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0d9f, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageActionSetChatTheme) r2).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x0da7, code lost:
    
        if (android.text.TextUtils.isEmpty(r0) == false) goto L671;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x0dab, code lost:
    
        if (r7 != r17) goto L670;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:?, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:?, code lost:
    
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x0dcd, code lost:
    
        if (r7 != r17) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:?, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x0de7, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChatThemeChangedTo, r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0dea, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByRequest) == false) goto L680;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x0df2, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x0df5, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPrizeStars) == false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0df7, code lost:
    
        r2 = (org.telegram.tgnet.TLRPC.TL_messageActionPrizeStars) r2;
        r0 = org.telegram.messenger.DialogObject.getPeerDialogId(r2.boost_peer);
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x0e03, code lost:
    
        if (r0 < 0) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0e05, code lost:
    
        r3 = org.telegram.messenger.UserObject.getForcedFirstName(getMessagesController().getUser(java.lang.Long.valueOf(r0)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0e37, code lost:
    
        return org.telegram.messenger.LocaleController.formatPluralStringComma("BoostingReceivedStars", (int) r2.stars, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x0e16, code lost:
    
        r0 = getMessagesController().getChat(java.lang.Long.valueOf(-r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x0e23, code lost:
    
        if (r0 != null) goto L688;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x0e26, code lost:
    
        r3 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x0e3a, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentRefunded) == false) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x0e42, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x0e43, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x0e4d, code lost:
    
        if (r1.peer_id.channel_id == 0) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0e51, code lost:
    
        if (r11.megagroup != false) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x0e57, code lost:
    
        if (r23.isVideoAvatar() == false) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0e67, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelVideoEditNotification, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x0e76, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelPhotoEditNotification, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x0e7c, code lost:
    
        if (r23.isVideoAvatar() == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x0e8e, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationEditedGroupVideo, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x0e9f, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r4, r11.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x0ea6, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x0ead, code lost:
    
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x0eba, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationContactJoined, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x0ebf, code lost:
    
        if (r23.isMediaEmpty() == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x0ec9, code lost:
    
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x0ecf, code lost:
    
        return replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x0ed6, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x0edb, code lost:
    
        if (r23.type != 29) goto L764;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x0ee3, code lost:
    
        if ((org.telegram.messenger.MessageObject.getMedia(r23) instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPaidMedia) == false) goto L764;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0ee5, code lost:
    
        r0 = (org.telegram.tgnet.TLRPC.TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r23);
        r1 = r0.extended_media.size();
        r2 = false;
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x0ef3, code lost:
    
        if (r3 >= r1) goto L901;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0ef5, code lost:
    
        r4 = r0.extended_media.get(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x0eff, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageExtendedMedia) == false) goto L741;
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x0f01, code lost:
    
        r2 = ((org.telegram.tgnet.TLRPC.TL_messageExtendedMedia) r4).media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x0f07, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L740;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x0f0f, code lost:
    
        if (org.telegram.messenger.MessageObject.isVideoDocument(r2.document) == false) goto L740;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x0f21, code lost:
    
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0f26, code lost:
    
        if (r2 == false) goto L750;
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x0f2a, code lost:
    
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0f2d, code lost:
    
        r0 = org.telegram.messenger.R.string.AttachPaidMedia;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0f2f, code lost:
    
        if (r1 != 1) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f31, code lost:
    
        if (r2 == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x0f33, code lost:
    
        r1 = org.telegram.messenger.R.string.AttachVideo;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f38, code lost:
    
        r1 = org.telegram.messenger.LocaleController.getString(r1);
        r2 = 1;
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x0f4f, code lost:
    
        r2 = new java.lang.Object[r2];
        r2[r3] = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x0f57, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x0f36, code lost:
    
        r1 = org.telegram.messenger.R.string.AttachPhoto;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x0f3f, code lost:
    
        if (r2 == false) goto L760;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x0f41, code lost:
    
        r2 = "Media";
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0f43, code lost:
    
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x0f48, code lost:
    
        r1 = org.telegram.messenger.LocaleController.formatPluralString(r2, r1, new java.lang.Object[0]);
        r2 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0f45, code lost:
    
        r2 = "Photos";
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x0f23, code lost:
    
        r2 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x0f17, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageExtendedMediaPreview) == false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x0f1f, code lost:
    
        if ((((org.telegram.tgnet.TLRPC.TL_messageExtendedMediaPreview) r4).flags & 4) == 0) goto L746;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0f5c, code lost:
    
        if (r23.isVoiceOnce() == false) goto L768;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x0f64, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x0f69, code lost:
    
        if (r23.isRoundOnce() == false) goto L772;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x0f71, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x0f72, code lost:
    
        r1 = r23.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x0f78, code lost:
    
        if ((r1.media instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L784;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0f80, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L778;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0f95, code lost:
    
        return "🖼 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0f9c, code lost:
    
        if (r23.messageOwner.media.ttl_seconds == 0) goto L782;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x0fa4, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x0fab, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x0fb0, code lost:
    
        if (r23.isVideo() == false) goto L796;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x0fba, code lost:
    
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L790;
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x0fcf, code lost:
    
        return "📹 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x0fd6, code lost:
    
        if (r23.messageOwner.media.ttl_seconds == 0) goto L794;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x0fde, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x0fe5, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x0fea, code lost:
    
        if (r23.isGame() == false) goto L800;
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x0ff2, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x0ff7, code lost:
    
        if (r23.isVoice() == false) goto L804;
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x0fff, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x1004, code lost:
    
        if (r23.isRoundVideo() == false) goto L808;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x100c, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x1011, code lost:
    
        if (r23.isMusic() == false) goto L812;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x1019, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:785:0x101a, code lost:
    
        r1 = r23.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x1020, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L816;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x1028, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x102b, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L824;
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:0x1033, code lost:
    
        if (((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r1).poll.quiz == false) goto L822;
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:0x103b, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:0x1042, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x1045, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGiveaway) == false) goto L828;
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x104d, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingGiveaway);
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x1050, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGiveawayResults) == false) goto L832;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x1058, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingGiveawayResults);
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:0x105b, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L886;
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x105f, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L837;
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x1065, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L841;
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x106d, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:0x1070, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L868;
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x1076, code lost:
    
        if (r23.isSticker() != false) goto L862;
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:0x107c, code lost:
    
        if (r23.isAnimatedSticker() == false) goto L848;
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x1083, code lost:
    
        if (r23.isGif() == false) goto L856;
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:0x108d, code lost:
    
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L854;
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:0x10a2, code lost:
    
        return "🎬 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:0x10a9, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:0x10b2, code lost:
    
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L860;
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:0x10c7, code lost:
    
        return "📎 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:0x10ce, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:0x10cf, code lost:
    
        r0 = r23.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:0x10d3, code lost:
    
        if (r0 == null) goto L866;
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:0x10ef, code lost:
    
        return r0 + " " + org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:0x10f6, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:0x10f9, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaStory) == false) goto L880;
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:0x10ff, code lost:
    
        if (((org.telegram.tgnet.TLRPC.TL_messageMediaStory) r1).via_mention == false) goto L878;
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:0x1101, code lost:
    
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r2 = r24[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:0x1106, code lost:
    
        if (r2 != null) goto L875;
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:0x1114, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(r0, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:0x110a, code lost:
    
        r3 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:0x111b, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:0x1122, code lost:
    
        if (android.text.TextUtils.isEmpty(r23.messageText) != false) goto L884;
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:0x1128, code lost:
    
        return replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:0x112f, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:0x1136, code lost:
    
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:863:0x0234, code lost:
    
        if (r6.getBoolean("EnablePreviewGroup", r2) != false) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:0x023c, code lost:
    
        if (r6.getBoolean("EnablePreviewChannel", r2) != false) goto L163;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getShortStringForMessage(MessageObject messageObject, String[] strArr, boolean[] zArr) {
        String str;
        char c;
        TLRPC.Chat chat;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        TLRPC.Chat chat2;
        TLRPC.MessageFwdHeader messageFwdHeader2;
        TLRPC.Peer peer2;
        boolean z;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString(R.string.NotificationHiddenMessage);
        }
        TLRPC.Message message = messageObject.messageOwner;
        long j = message.dialog_id;
        TLRPC.Peer peer3 = message.peer_id;
        long j2 = peer3.chat_id;
        if (j2 == 0) {
            j2 = peer3.channel_id;
        }
        long j3 = peer3.user_id;
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
                return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString(R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString(R.string.ChannelMessageNoText, messageObject.localName);
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
        if (UserObject.isReplyUser(j) && (messageFwdHeader2 = messageObject.messageOwner.fwd_from) != null && (peer2 = messageFwdHeader2.from_id) != null) {
            j3 = MessageObject.getPeerId(peer2);
        }
        if (j3 > 0) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(j3));
            if (user != null) {
                str = UserObject.getUserName(user);
                if (j2 != 0) {
                    strArr[0] = str;
                } else if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = str;
                } else {
                    strArr[0] = null;
                }
            }
            str = null;
        } else {
            TLRPC.Chat chat3 = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat3 != null) {
                str = chat3.title;
                strArr[0] = str;
            }
            str = null;
        }
        if (str != null && j3 > 0 && UserObject.isReplyUser(j) && (messageFwdHeader = messageObject.messageOwner.fwd_from) != null && (peer = messageFwdHeader.saved_from_peer) != null) {
            long peerId = MessageObject.getPeerId(peer);
            if (DialogObject.isChatDialog(peerId) && (chat2 = getMessagesController().getChat(Long.valueOf(-peerId))) != null) {
                str = str + " @ " + chat2.title;
                if (strArr[0] != null) {
                    strArr[0] = str;
                }
            }
        }
        if (str == null) {
            return null;
        }
        if (j2 != 0) {
            chat = getMessagesController().getChat(Long.valueOf(j2));
            if (chat == null) {
                return null;
            }
            if (!ChatObject.isChannel(chat) || chat.megagroup || Build.VERSION.SDK_INT > 27) {
                c = 0;
            } else {
                c = 0;
                strArr[0] = null;
            }
        } else {
            c = 0;
            chat = null;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            strArr[c] = null;
            return LocaleController.getString(R.string.NotificationHiddenMessage);
        }
        boolean z4 = ChatObject.isChannel(chat) && !chat.megagroup;
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

    /* JADX WARN: Code restructure failed: missing block: B:271:0x05fd, code lost:
    
        if (r5.getBoolean("EnablePreviewGroup", true) == false) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x060b, code lost:
    
        r3 = r28.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x060f, code lost:
    
        if ((r3 instanceof org.telegram.tgnet.TLRPC.TL_messageService) == false) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0611, code lost:
    
        r5 = r3.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x0617, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser) == false) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x0619, code lost:
    
        r1 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x061f, code lost:
    
        if (r1 != 0) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0628, code lost:
    
        if (r5.users.size() != 1) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x062a, code lost:
    
        r1 = r28.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x063f, code lost:
    
        if (r1 == 0) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0649, code lost:
    
        if (r28.messageOwner.peer_id.channel_id == 0) goto L307;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x064d, code lost:
    
        if (r10.megagroup != false) goto L307;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x064f, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelAddedByNotification, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0667, code lost:
    
        if (r1 != r18) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x0669, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationInvitedToGroup, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x0679, code lost:
    
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0685, code lost:
    
        if (r0 != null) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0687, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x068d, code lost:
    
        if (r12 != r0.id) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0691, code lost:
    
        if (r10.megagroup == false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0693, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x06a6, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddSelf, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06b9, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddMember, r4, r10.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x06d3, code lost:
    
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06e3, code lost:
    
        if (r2 >= r28.messageOwner.action.users.size()) goto L807;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x06e5, code lost:
    
        r3 = getMessagesController().getUser(r28.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x06f9, code lost:
    
        if (r3 == null) goto L809;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x06fb, code lost:
    
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0703, code lost:
    
        if (r1.length() == 0) goto L329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0705, code lost:
    
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0708, code lost:
    
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x070b, code lost:
    
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x070e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupAddMember, r4, r10.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x072b, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGroupCall) == false) goto L340;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x072f, code lost:
    
        if (r5.duration == 0) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0741, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupEndedCall, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x0752, code lost:
    
        return org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupCreatedCall, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0755, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGroupCallScheduled) == false) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x075b, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionInviteToGroupCall) == false) goto L371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x075d, code lost:
    
        r1 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0763, code lost:
    
        if (r1 != 0) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x076c, code lost:
    
        if (r5.users.size() != 1) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x076e, code lost:
    
        r1 = r28.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0783, code lost:
    
        if (r1 == 0) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0787, code lost:
    
        if (r1 != r18) goto L355;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x0789, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x079c, code lost:
    
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x07a8, code lost:
    
        if (r0 != null) goto L359;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x07aa, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x07ac, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r4, r10.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x07c6, code lost:
    
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x07d6, code lost:
    
        if (r2 >= r28.messageOwner.action.users.size()) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x07d8, code lost:
    
        r3 = getMessagesController().getUser(r28.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x07ec, code lost:
    
        if (r3 == null) goto L812;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x07ee, code lost:
    
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x07f6, code lost:
    
        if (r1.length() == 0) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x07f8, code lost:
    
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x07fb, code lost:
    
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x07fe, code lost:
    
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0801, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r4, r10.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x081d, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGiftCode) == false) goto L380;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x081f, code lost:
    
        r5 = (org.telegram.tgnet.TLRPC.TL_messageActionGiftCode) r5;
        r0 = org.telegram.messenger.MessagesController.getInstance(r27.currentAccount).getChat(java.lang.Long.valueOf(-org.telegram.messenger.DialogObject.getPeerDialogId(r5.boost_peer)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0838, code lost:
    
        if (r0 != null) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x083a, code lost:
    
        r15 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x083e, code lost:
    
        if (r15 != null) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0840, code lost:
    
        r0 = org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0848, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGiftCode, r15, org.telegram.messenger.LocaleController.formatPluralString("Months", r5.months, new java.lang.Object[0]));
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x083c, code lost:
    
        r15 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x086b, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByLink) == false) goto L383;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x086d, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x0880, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditTitle) == false) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x0882, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationEditedGroupName, r4, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x0895, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto) != false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0899, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeletePhoto) == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x089f, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser) == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x08a1, code lost:
    
        r1 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x08a5, code lost:
    
        if (r1 != r18) goto L396;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x08a7, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupKickYou, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x08bf, code lost:
    
        if (r1 != r12) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x08c1, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupLeftMember, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x08d1, code lost:
    
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r28.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x08e3, code lost:
    
        if (r0 != null) goto L403;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x08e5, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x08e7, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationGroupKickMember, r4, r10.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0901, code lost:
    
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0904, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatCreate) == false) goto L407;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x090a, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate) == false) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0910, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo) == false) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0912, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x0926, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom) == false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x0928, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x0938, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken) == false) goto L419;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x093e, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage) == false) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0948, code lost:
    
        if (org.telegram.messenger.ChatObject.isChannel(r10) == false) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x094c, code lost:
    
        if (r10.megagroup == false) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x0952, code lost:
    
        r3 = r28.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x0954, code lost:
    
        if (r3 != null) goto L429;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0956, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x096c, code lost:
    
        if (r3.isMusic() == false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x096e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0980, code lost:
    
        if (r3.isVideo() == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x098a, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.messageOwner.message) != false) goto L437;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x098c, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r10.title, "📹 " + r3.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x09b2, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x09c6, code lost:
    
        if (r3.isGif() == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x09d0, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.messageOwner.message) != false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x09d2, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r10.title, "🎬 " + r3.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x09f8, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a0e, code lost:
    
        if (r3.isVoice() == false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0a10, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0a22, code lost:
    
        if (r3.isRoundVideo() == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0a24, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0a36, code lost:
    
        if (r3.isSticker() != false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0a3c, code lost:
    
        if (r3.isAnimatedSticker() == false) goto L455;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0a42, code lost:
    
        r1 = r3.messageOwner;
        r4 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0a48, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0a50, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L460;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0a52, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r10.title, "📎 " + r3.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0a78, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0a8a, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0a8e, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0a96, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L469;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0a98, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0aaa, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0aac, code lost:
    
        r0 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r28.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r10.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0acf, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0ad1, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r4).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0ad7, code lost:
    
        if (r0.quiz == false) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0ad9, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r10.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0af0, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r10.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0b09, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0b11, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0b13, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r10.title, "🖼 " + r3.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0b39, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0b4d, code lost:
    
        if ((r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame) == false) goto L487;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0b4f, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b5d, code lost:
    
        r0 = r3.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b5f, code lost:
    
        if (r0 == null) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x0b65, code lost:
    
        if (r0.length() <= 0) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0b67, code lost:
    
        r0 = r3.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0b6d, code lost:
    
        if (r0.length() <= 20) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0b6f, code lost:
    
        r1 = new java.lang.StringBuilder();
        r7 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0b85, code lost:
    
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r3 = new java.lang.Object[2];
        r3[r7] = r10.title;
        r3[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString(r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0b84, code lost:
    
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0b97, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0ba7, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0bb5, code lost:
    
        r0 = r3.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0bb9, code lost:
    
        if (r0 == null) goto L501;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0bbb, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r10.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0bcc, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0bda, code lost:
    
        r3 = r28.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0bdc, code lost:
    
        if (r3 != null) goto L505;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0bde, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoText, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0bf4, code lost:
    
        if (r3.isMusic() == false) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0bf6, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedMusic, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c0a, code lost:
    
        if (r3.isVideo() == false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0c14, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.messageOwner.message) != false) goto L513;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0c16, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "📹 " + r3.messageOwner.message, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0c3f, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVideo, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0c56, code lost:
    
        if (r3.isGif() == false) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0c60, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.messageOwner.message) != false) goto L519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0c62, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "🎬 " + r3.messageOwner.message, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0c8b, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGif, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0ca5, code lost:
    
        if (r3.isVoice() == false) goto L523;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0ca7, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedVoice, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0cbb, code lost:
    
        if (r3.isRoundVideo() == false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0cbd, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedRound, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0cd1, code lost:
    
        if (r3.isSticker() != false) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0cd7, code lost:
    
        if (r3.isAnimatedSticker() == false) goto L531;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0cdd, code lost:
    
        r1 = r3.messageOwner;
        r7 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0ce3, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L537;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0ceb, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0ced, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "📎 " + r3.messageOwner.message, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0d16, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedFile, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0d2b, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0d2f, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0d38, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L545;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0d3a, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0d4f, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0d51, code lost:
    
        r0 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r28.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedContact2, r4, r10.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0d77, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L554;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0d79, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0d7f, code lost:
    
        if (r0.quiz == false) goto L553;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0d81, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r4, r10.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0d9b, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r4, r10.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0db7, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0dbf, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0dc1, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedText, r4, "🖼 " + r3.messageOwner.message, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0dea, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e02, code lost:
    
        if ((r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame) == false) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0e04, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGame, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0e14, code lost:
    
        r0 = r3.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0e16, code lost:
    
        if (r0 == null) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0e1c, code lost:
    
        if (r0.length() <= 0) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0e1e, code lost:
    
        r0 = r3.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0e24, code lost:
    
        if (r0.length() <= 20) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0e26, code lost:
    
        r1 = new java.lang.StringBuilder();
        r2 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0e3c, code lost:
    
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r3 = r10.title;
        r5 = new java.lang.Object[3];
        r5[r2] = r4;
        r5[1] = r0;
        r5[2] = r3;
        r0 = org.telegram.messenger.LocaleController.formatString(r1, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0e3b, code lost:
    
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0e51, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedNoText, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0e64, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedGeo, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0e74, code lost:
    
        r0 = r3.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0e78, code lost:
    
        if (r0 == null) goto L577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0e7a, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r4, r10.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0e8e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationActionPinnedSticker, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0ea1, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore) == false) goto L581;
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0ea6, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionSetChatTheme) == false) goto L593;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0ea8, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageActionSetChatTheme) r5).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0eb0, code lost:
    
        if (android.text.TextUtils.isEmpty(r0) == false) goto L589;
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0eb4, code lost:
    
        if (r6 != r18) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0eb6, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0ec1, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0ed8, code lost:
    
        if (r6 != r18) goto L592;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0eda, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0ee6, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChatThemeChangedTo, r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0ef7, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByRequest) == false) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0f09, code lost:
    
        if (r3.peer_id.channel_id == 0) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0f0d, code lost:
    
        if (r10.megagroup != false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0f13, code lost:
    
        if (r28.isVideoAvatar() == false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0f15, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelVideoEditNotification, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0f25, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelPhotoEditNotification, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0f3a, code lost:
    
        if (r28.isVideoAvatar() == false) goto L607;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0f3c, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationEditedGroupVideo, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0f4e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0f66, code lost:
    
        if (org.telegram.messenger.ChatObject.isChannel(r10) == false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0f6a, code lost:
    
        if (r10.megagroup != false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0f70, code lost:
    
        if (r28.isMediaEmpty() == false) goto L619;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0f72, code lost:
    
        if (r29 != false) goto L618;
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0f7c, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L618;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0f7e, code lost:
    
        r15 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageText, r4, r28.messageOwner.message);
        r30[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0f95, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageNoText, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0fa5, code lost:
    
        if (r28.type != 29) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x0fad, code lost:
    
        if ((org.telegram.messenger.MessageObject.getMedia(r28) instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPaidMedia) == false) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0faf, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC.TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r28)).stars_amount, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x0fc8, code lost:
    
        r3 = r28.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x0fce, code lost:
    
        if ((r3.media instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x0fd0, code lost:
    
        if (r29 != false) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0fd8, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x0fda, code lost:
    
        r15 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageText, r4, "🖼 " + r28.messageOwner.message);
        r30[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x1000, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessagePhoto, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x1012, code lost:
    
        if (r28.isVideo() == false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x1014, code lost:
    
        if (r29 != false) goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x101e, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x1020, code lost:
    
        r15 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageText, r4, "📹 " + r28.messageOwner.message);
        r30[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x1046, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageVideo, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x105a, code lost:
    
        if (r28.isVoice() == false) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x105c, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageAudio, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x106c, code lost:
    
        if (r28.isRoundVideo() == false) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x106e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageRound, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x107e, code lost:
    
        if (r28.isMusic() == false) goto L647;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x1080, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageMusic, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x108c, code lost:
    
        r1 = r28.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x1092, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x1094, code lost:
    
        r1 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r1;
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageContact2, r4, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x10b1, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x10b3, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x10b9, code lost:
    
        if (r0.quiz == false) goto L655;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x10bb, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageQuiz2, r4, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x10d0, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessagePoll2, r4, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x10e7, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGiveaway) == false) goto L659;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x10e9, code lost:
    
        r1 = (org.telegram.tgnet.TLRPC.TL_messageMediaGiveaway) r1;
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r10.title, java.lang.Integer.valueOf(r1.quantity), java.lang.Integer.valueOf(r1.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x110f, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x1113, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L664;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x111b, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L667;
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x111d, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageLiveLocation, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x112d, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L690;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x1133, code lost:
    
        if (r28.isSticker() != false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x1139, code lost:
    
        if (r28.isAnimatedSticker() == false) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x1143, code lost:
    
        if (r28.isGif() == false) goto L681;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x1145, code lost:
    
        if (r29 != false) goto L680;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x114f, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L680;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x1151, code lost:
    
        r15 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageText, r4, "🎬 " + r28.messageOwner.message);
        r30[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x1177, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageGIF, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x1185, code lost:
    
        if (r29 != false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x118f, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x1191, code lost:
    
        r15 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageText, r4, "📎 " + r28.messageOwner.message);
        r30[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x11b7, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageDocument, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x11c5, code lost:
    
        r0 = r28.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x11c9, code lost:
    
        if (r0 == null) goto L689;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x11cb, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x11da, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageSticker, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x11e7, code lost:
    
        if (r29 != false) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x11ef, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageText) != false) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x11f1, code lost:
    
        r15 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageText, r4, r28.messageText);
        r30[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:?, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x1205, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageNoText, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1212, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.ChannelMessageMap, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x1222, code lost:
    
        if (r28.isMediaEmpty() == false) goto L704;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x1224, code lost:
    
        if (r29 != false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x122e, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x1230, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupText, r4, r10.title, r28.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x124a, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupNoText, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x125f, code lost:
    
        if (r28.type != 29) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x1267, code lost:
    
        if ((org.telegram.messenger.MessageObject.getMedia(r28) instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPaidMedia) == false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x1269, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChatMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC.TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r28)).stars_amount, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x1285, code lost:
    
        r3 = r28.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x128b, code lost:
    
        if ((r3.media instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto) == false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x128d, code lost:
    
        if (r29 != false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x1295, code lost:
    
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x1297, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupText, r4, r10.title, "🖼 " + r28.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x12c0, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x12d7, code lost:
    
        if (r28.isVideo() == false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x12d9, code lost:
    
        if (r29 != false) goto L722;
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x12e3, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L722;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x12e5, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupText, r4, r10.title, "📹 " + r28.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x130e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupVideo, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x1328, code lost:
    
        if (r28.isVoice() == false) goto L726;
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x132a, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupAudio, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x133e, code lost:
    
        if (r28.isRoundVideo() == false) goto L729;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x1340, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupRound, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x1354, code lost:
    
        if (r28.isMusic() == false) goto L732;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x1356, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupMusic, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x1366, code lost:
    
        r1 = r28.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x136c, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact) == false) goto L735;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x136e, code lost:
    
        r1 = (org.telegram.tgnet.TLRPC.TL_messageMediaContact) r1;
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupContact2, r4, r10.title, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x1390, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPoll) == false) goto L741;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x1392, code lost:
    
        r0 = ((org.telegram.tgnet.TLRPC.TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x1398, code lost:
    
        if (r0.quiz == false) goto L740;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x139a, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r4, r10.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x13b4, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r4, r10.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x13d0, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame) == false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x13d2, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupGame, r4, r10.title, r1.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x13ee, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGiveaway) == false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x13f0, code lost:
    
        r1 = (org.telegram.tgnet.TLRPC.TL_messageMediaGiveaway) r1;
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r10.title, java.lang.Integer.valueOf(r1.quantity), java.lang.Integer.valueOf(r1.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x1417, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGiveawayResults) == false) goto L750;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x1419, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.BoostingGiveawayResults, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x1425, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo) != false) goto L754;
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x1429, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue) == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x1432, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive) == false) goto L758;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x1434, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x1449, code lost:
    
        if ((r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument) == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x144f, code lost:
    
        if (r28.isSticker() != false) goto L764;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x1455, code lost:
    
        if (r28.isAnimatedSticker() == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x145f, code lost:
    
        if (r28.isGif() == false) goto L772;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x1461, code lost:
    
        if (r29 != false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x146b, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x146d, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupText, r4, r10.title, "🎬 " + r28.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x1496, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupGif, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x14a9, code lost:
    
        if (r29 != false) goto L776;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x14b3, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageOwner.message) != false) goto L776;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x14b5, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupText, r4, r10.title, "📎 " + r28.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x14de, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupDocument, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x14f1, code lost:
    
        r0 = r28.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x14f5, code lost:
    
        if (r0 == null) goto L780;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x14f7, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r4, r10.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x150b, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupSticker, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x151c, code lost:
    
        if (r29 != false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1524, code lost:
    
        if (android.text.TextUtils.isEmpty(r28.messageText) != false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1526, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupText, r4, r10.title, r28.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x153e, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString(org.telegram.messenger.R.string.NotificationMessageGroupNoText, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1551, code lost:
    
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r4, r10.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x0609, code lost:
    
        if (r5.getBoolean("EnablePreviewChannel", r14) != false) goto L291;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        String str;
        TLRPC.Chat chat;
        String formatPluralString;
        String formatString;
        char c;
        boolean z2;
        String formatString2;
        int i;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString(R.string.YouHaveNewMessage);
        }
        if (messageObject.isStoryPush || messageObject.isStoryMentionPush) {
            return "!" + messageObject.messageOwner.message;
        }
        TLRPC.Message message = messageObject.messageOwner;
        long j = message.dialog_id;
        TLRPC.Peer peer = message.peer_id;
        long j2 = peer.chat_id;
        if (j2 == 0) {
            j2 = peer.channel_id;
        }
        long j3 = peer.user_id;
        if (zArr2 != null) {
            zArr2[0] = true;
        }
        if (messageObject.getDialogId() == UserObject.VERIFY && messageObject.getForwardedFromId() != null) {
            j3 = messageObject.getForwardedFromId().longValue();
            j2 = j3 < 0 ? -j3 : 0L;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z3 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j, true);
        if (messageObject.isFcmMessage()) {
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0 && (!z3 || ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))))) {
                    if (zArr2 != null) {
                        zArr2[0] = false;
                    }
                    return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString(R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString(R.string.ChannelMessageNoText, messageObject.localName);
                }
            } else if (!z3 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return LocaleController.formatString(R.string.NotificationMessageNoText, messageObject.localName);
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
            TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat2 != null) {
                str = chat2.title;
            }
            str = null;
        } else if (messageObject.messageOwner.from_scheduled) {
            str = LocaleController.getString(j == clientUserId ? R.string.MessageScheduledReminderNotification : R.string.NotificationMessageScheduledName);
        } else {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(j3));
            if (user != null) {
                str = UserObject.getUserName(user);
            }
            str = null;
        }
        if (str == null) {
            return null;
        }
        if (j2 != 0) {
            chat = getMessagesController().getChat(Long.valueOf(j2));
            if (chat == null) {
                return null;
            }
        } else {
            chat = null;
        }
        if (!DialogObject.isEncryptedDialog(j)) {
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
                    boolean z4 = ChatObject.isChannel(chat) && !chat.megagroup;
                    if (z3) {
                        boolean z5 = !z4 ? true : true;
                        if (z4) {
                        }
                    }
                    if (zArr2 != null) {
                        zArr2[0] = false;
                    }
                    formatPluralString = (!ChatObject.isChannel(chat) || chat.megagroup) ? (messageObject.type == 29 && (MessageObject.getMedia(messageObject) instanceof TLRPC.TL_messageMediaPaidMedia)) ? LocaleController.formatPluralString("NotificationMessagePaidMedia", (int) ((TLRPC.TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, str) : LocaleController.formatString(R.string.NotificationMessageGroupNoText, str, chat.title) : LocaleController.formatString(R.string.ChannelMessageNoText, str);
                }
                String str2 = null;
                return str2;
            }
            if (z3 && notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                TLRPC.Message message2 = messageObject.messageOwner;
                if (message2 instanceof TLRPC.TL_messageService) {
                    TLRPC.MessageAction messageAction = message2.action;
                    if (messageAction instanceof TLRPC.TL_messageActionSetSameChatWallPaper) {
                        i = R.string.WallpaperSameNotification;
                    } else if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
                        i = R.string.WallpaperNotification;
                    } else {
                        if (!(messageAction instanceof TLRPC.TL_messageActionGeoProximityReached)) {
                            if ((messageAction instanceof TLRPC.TL_messageActionUserJoined) || (messageAction instanceof TLRPC.TL_messageActionContactSignUp)) {
                                formatPluralString = LocaleController.formatString(R.string.NotificationContactJoined, str);
                            } else if (messageAction instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                                formatPluralString = LocaleController.formatString(R.string.NotificationContactNewPhoto, str);
                            } else if (messageAction instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                                String formatString3 = LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().getFormatterDay().format(messageObject.messageOwner.date * 1000));
                                int i2 = R.string.NotificationUnrecognizedDevice;
                                String str3 = getUserConfig().getCurrentUser().first_name;
                                TLRPC.MessageAction messageAction2 = messageObject.messageOwner.action;
                                formatPluralString = LocaleController.formatString(i2, str3, formatString3, messageAction2.title, messageAction2.address);
                            } else if (!(messageAction instanceof TLRPC.TL_messageActionGameScore) && !(messageAction instanceof TLRPC.TL_messageActionPaymentSent) && !(messageAction instanceof TLRPC.TL_messageActionPaymentSentMe) && !(messageAction instanceof TLRPC.TL_messageActionStarGift) && !(messageAction instanceof TLRPC.TL_messageActionGiftPremium)) {
                                if (!(messageAction instanceof TLRPC.TL_messageActionPhoneCall)) {
                                    if (messageAction instanceof TLRPC.TL_messageActionSetChatTheme) {
                                        String str4 = ((TLRPC.TL_messageActionSetChatTheme) messageAction).emoticon;
                                        if (!TextUtils.isEmpty(str4)) {
                                            c = 0;
                                            z2 = true;
                                            formatString2 = j == clientUserId ? LocaleController.formatString(R.string.ChatThemeChangedYou, str4) : LocaleController.formatString(R.string.ChatThemeChangedTo, str, str4);
                                        } else {
                                            if (j == clientUserId) {
                                                c = 0;
                                                formatString = LocaleController.formatString(R.string.ChatThemeDisabledYou, new Object[0]);
                                                z2 = true;
                                                zArr[c] = z2;
                                                return formatString;
                                            }
                                            c = 0;
                                            z2 = true;
                                            formatString2 = LocaleController.formatString(R.string.ChatThemeDisabled, str, str4);
                                        }
                                        formatString = formatString2;
                                        zArr[c] = z2;
                                        return formatString;
                                    }
                                    String str22 = null;
                                    return str22;
                                }
                                i = messageAction.video ? R.string.CallMessageVideoIncomingMissed : R.string.CallMessageIncomingMissed;
                            }
                        }
                        formatPluralString = messageObject.messageText.toString();
                    }
                } else if (!messageObject.isMediaEmpty()) {
                    TLRPC.Message message3 = messageObject.messageOwner;
                    if (message3.media instanceof TLRPC.TL_messageMediaPhoto) {
                        if (!z && !TextUtils.isEmpty(message3.message)) {
                            formatString = LocaleController.formatString(R.string.NotificationMessageText, str, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString;
                        }
                        formatPluralString = messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString(R.string.NotificationMessageSDPhoto, str) : LocaleController.formatString(R.string.NotificationMessagePhoto, str);
                    } else if (messageObject.isVideo()) {
                        if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            formatString = LocaleController.formatString(R.string.NotificationMessageText, str, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString;
                        }
                        formatPluralString = messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString(R.string.NotificationMessageSDVideo, str) : LocaleController.formatString(R.string.NotificationMessageVideo, str);
                    } else if (messageObject.isGame()) {
                        formatPluralString = LocaleController.formatString(R.string.NotificationMessageGame, str, messageObject.messageOwner.media.game.title);
                    } else if (messageObject.isVoice()) {
                        formatPluralString = LocaleController.formatString(R.string.NotificationMessageAudio, str);
                    } else if (messageObject.isRoundVideo()) {
                        formatPluralString = LocaleController.formatString(R.string.NotificationMessageRound, str);
                    } else if (messageObject.isMusic()) {
                        formatPluralString = LocaleController.formatString(R.string.NotificationMessageMusic, str);
                    } else {
                        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                        if (messageMedia instanceof TLRPC.TL_messageMediaContact) {
                            TLRPC.TL_messageMediaContact tL_messageMediaContact = (TLRPC.TL_messageMediaContact) messageMedia;
                            formatPluralString = LocaleController.formatString(R.string.NotificationMessageContact2, str, ContactsController.formatName(tL_messageMediaContact.first_name, tL_messageMediaContact.last_name));
                        } else if (messageMedia instanceof TLRPC.TL_messageMediaGiveaway) {
                            TLRPC.TL_messageMediaGiveaway tL_messageMediaGiveaway = (TLRPC.TL_messageMediaGiveaway) messageMedia;
                            formatPluralString = LocaleController.formatString(R.string.NotificationMessageChannelGiveaway, str, Integer.valueOf(tL_messageMediaGiveaway.quantity), Integer.valueOf(tL_messageMediaGiveaway.months));
                        } else if (messageMedia instanceof TLRPC.TL_messageMediaGiveawayResults) {
                            formatPluralString = LocaleController.formatString(R.string.BoostingGiveawayResults, new Object[0]);
                        } else if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                            TLRPC.Poll poll = ((TLRPC.TL_messageMediaPoll) messageMedia).poll;
                            formatPluralString = poll.quiz ? LocaleController.formatString(R.string.NotificationMessageQuiz2, str, poll.question.text) : LocaleController.formatString(R.string.NotificationMessagePoll2, str, poll.question.text);
                        } else if ((messageMedia instanceof TLRPC.TL_messageMediaGeo) || (messageMedia instanceof TLRPC.TL_messageMediaVenue)) {
                            formatPluralString = LocaleController.formatString(R.string.NotificationMessageMap, str);
                        } else if (messageMedia instanceof TLRPC.TL_messageMediaGeoLive) {
                            formatPluralString = LocaleController.formatString(R.string.NotificationMessageLiveLocation, str);
                        } else if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String stickerEmoji = messageObject.getStickerEmoji();
                                formatPluralString = stickerEmoji != null ? LocaleController.formatString(R.string.NotificationMessageStickerEmoji, str, stickerEmoji) : LocaleController.formatString(R.string.NotificationMessageSticker, str);
                            } else if (messageObject.isGif()) {
                                if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    formatString = LocaleController.formatString(R.string.NotificationMessageText, str, "🎬 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                    return formatString;
                                }
                                formatPluralString = LocaleController.formatString(R.string.NotificationMessageGif, str);
                            } else {
                                if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    formatString = LocaleController.formatString(R.string.NotificationMessageText, str, "📎 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                    return formatString;
                                }
                                formatPluralString = LocaleController.formatString(R.string.NotificationMessageDocument, str);
                            }
                        } else {
                            if (!z && !TextUtils.isEmpty(messageObject.messageText)) {
                                formatString = LocaleController.formatString(R.string.NotificationMessageText, str, messageObject.messageText);
                                zArr[0] = true;
                                return formatString;
                            }
                            formatPluralString = LocaleController.formatString(R.string.NotificationMessageNoText, str);
                        }
                    }
                } else if (z) {
                    formatPluralString = LocaleController.formatString(R.string.NotificationMessageNoText, str);
                } else {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        formatString = LocaleController.formatString(R.string.NotificationMessageText, str, messageObject.messageOwner.message);
                        zArr[0] = true;
                        return formatString;
                    }
                    formatPluralString = LocaleController.formatString(R.string.NotificationMessageNoText, str);
                }
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                formatPluralString = LocaleController.formatString(R.string.NotificationMessageNoText, str);
            }
            return formatPluralString;
        }
        i = R.string.YouHaveNewMessage;
        formatPluralString = LocaleController.getString(i);
        return formatPluralString;
    }

    private int getTotalAllUnreadCount() {
        int size;
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
                                    TLRPC.Dialog dialog = (TLRPC.Dialog) arrayList.get(i3);
                                    if ((dialog == null || !DialogObject.isChatDialog(dialog.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-dialog.id)))) && dialog != null) {
                                        i += MessagesController.getInstance(i2).getDialogUnreadCount(dialog);
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
                                TLRPC.Dialog dialog2 = MessagesController.getInstance(i2).allDialogs.get(i4);
                                if ((!DialogObject.isChatDialog(dialog2.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-dialog2.id)))) && MessagesController.getInstance(i2).getDialogUnreadCount(dialog2) != 0) {
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
        TLRPC.MessageAction messageAction;
        TLRPC.Message message = messageObject.messageOwner;
        TLRPC.Peer peer = message.peer_id;
        return (peer != null && peer.chat_id == 0 && peer.channel_id == 0 && ((messageAction = message.action) == null || (messageAction instanceof TLRPC.TL_messageActionEmpty))) || messageObject.isStoryReactionPush;
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
        Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupNotificationActivity.class);
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
            TLRPC.Message message = messageObject.messageOwner;
            if ((!message.mentioned || !(message.action instanceof TLRPC.TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
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
            consumer.r(hashSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopicsNotificationsExceptions$51(long j, final Consumer consumer) {
        final HashSet hashSet = new HashSet();
        Iterator<Map.Entry<String, ?>> it = MessagesController.getNotificationsSettings(this.currentAccount).getAll().entrySet().iterator();
        while (it.hasNext()) {
            String key = it.next().getKey();
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
    /* JADX WARN: Removed duplicated region for block: B:15:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0074 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x00bd  */
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
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-keyAt));
                if (chat == null || chat.min || ChatObject.isNotInChat(chat)) {
                    i6 = 0;
                }
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
        TLRPC.MessageFwdHeader messageFwdHeader;
        SharedPreferences sharedPreferences;
        MessageObject messageObject;
        SparseArray sparseArray;
        long j4;
        long j5;
        int i2;
        TLRPC.Message message;
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
                TLRPC.Message message2 = (TLRPC.Message) arrayList3.get(i3);
                if (message2 != null && ((messageFwdHeader = message2.fwd_from) == null || !messageFwdHeader.imported)) {
                    TLRPC.MessageAction messageAction = message2.action;
                    if (!(messageAction instanceof TLRPC.TL_messageActionSetMessagesTTL) && (!message2.silent || (!(messageAction instanceof TLRPC.TL_messageActionContactSignUp) && !(messageAction instanceof TLRPC.TL_messageActionUserJoined)))) {
                        long j7 = message2.peer_id.channel_id;
                        long j8 = j7 != j6 ? -j7 : j6;
                        SparseArray sparseArray3 = (SparseArray) this.pushMessagesDict.get(j8);
                        if (sparseArray3 == null || sparseArray3.indexOfKey(message2.id) < 0) {
                            MessageObject messageObject2 = new MessageObject(this.currentAccount, message2, z3, z3);
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
                                message = message2;
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
                                message = message2;
                            }
                            if (isGlobalNotificationsEnabled && (fromChatId != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray();
                                    this.pushMessagesDict.put(j5, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(message.id, messageObject);
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
                    TLRPC.Message message3 = messageObject3.messageOwner;
                    long j10 = message3.random_id;
                    long fromChatId2 = message3.mentioned ? messageObject3.getFromChatId() : dialogId2;
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
                Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupNotificationActivity.class);
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
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004a, code lost:
    
        if ((r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserJoined) == false) goto L17;
     */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x020f  */
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
                    TLRPC.Message message = messageObject3.messageOwner;
                    TLRPC.MessageAction messageAction = message.action;
                    if (!(messageAction instanceof TLRPC.TL_messageActionSetMessagesTTL)) {
                        if (message.silent) {
                            if (!(messageAction instanceof TLRPC.TL_messageActionContactSignUp)) {
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
                    long currentTimeMillis = messageObject3.messageOwner == null ? System.currentTimeMillis() : r0.date * 1000;
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
                            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId2));
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
                                        TLRPC.Message message2 = messageObject3.messageOwner;
                                        if (!message2.mentioned) {
                                            j4 = dialogId2;
                                        } else if (z3 || !(message2.action instanceof TLRPC.TL_messageActionPinMessage)) {
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
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ca, code lost:
    
        if (r1.messageOwner.date <= r23) goto L59;
     */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00f6  */
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
        } else {
            notificationManager.cancel(this.notificationId);
            showOrUpdateNotification(true);
        }
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
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$47(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$48(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$49(TLObject tLObject, TLRPC.TL_error tL_error) {
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
            } else if (obj instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) obj;
                Rect rect3 = rect2;
                try {
                    i2 = i3;
                    textPaint = textPaint2;
                    try {
                        try {
                            paint2.setShader(new LinearGradient(size, size2, size, size2 + f2, new int[]{Theme.getColor(Theme.keys_avatar_background[AvatarDrawable.getColorIndex(user.id)]), Theme.getColor(Theme.keys_avatar_background2[AvatarDrawable.getColorIndex(user.id)])}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
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
                                AvatarDrawable.getAvatarSymbols(user.first_name, user.last_name, null, sb);
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
        TLRPC.FileLocation fileLocation;
        int min = Math.min(3, this.storyPushMessages.size());
        boolean z = false;
        int i2 = 0;
        while (i < min) {
            StoryNotification storyNotification = this.storyPushMessages.get(i);
            i2 += storyNotification.dateByIds.size();
            z |= storyNotification.hidden;
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(storyNotification.dialogId));
            if (user == null && (user = getMessagesStorage().getUserSync(storyNotification.dialogId)) != null) {
                getMessagesController().putUser(user, true);
            }
            Object obj = null;
            if (user != null) {
                str = UserObject.getUserName(user);
                TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
                if (userProfilePhoto != null && (fileLocation = userProfilePhoto.photo_small) != null && fileLocation.volume_id != 0 && fileLocation.local_id != 0) {
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
        TLRPC.Message message;
        String str;
        if (messageObject == null || (message = messageObject.messageOwner) == null || (str = message.message) == null || message.entities == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str);
        if (messageObject.didSpoilLoginCode()) {
            return sb.toString();
        }
        for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
            if (messageObject.messageOwner.entities.get(i) instanceof TLRPC.TL_messageEntitySpoiler) {
                TLRPC.TL_messageEntitySpoiler tL_messageEntitySpoiler = (TLRPC.TL_messageEntitySpoiler) messageObject.messageOwner.entities.get(i);
                for (int i2 = 0; i2 < tL_messageEntitySpoiler.length; i2++) {
                    int i3 = tL_messageEntitySpoiler.offset + i2;
                    char[] cArr = this.spoilerChars;
                    sb.setCharAt(i3, cArr[i2 % cArr.length]);
                }
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0060  */
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
            dispatchQueue.postRunnable(this.notificationDelayRunnable, z ? 3000 : MediaDataController.MAX_STYLE_RUNS_COUNT);
        } catch (Exception e) {
            FileLog.e(e);
            showOrUpdateNotification(this.notifyCheck);
        }
    }

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, ConnectionsManager.FileTypeVideo);
            if (getAccountInstance().getNotificationsSettings().getInt("repeat_messages", 60) <= 0 || this.personalCount <= 0) {
                this.alarmManager.cancel(service);
            } else {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + (r1 * 60000), service);
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

    /* JADX WARN: Can't wrap try/catch for region: R(84:58|(2:60|(4:62|63|64|65)(4:66|(2:69|67)|70|71))(1:809)|72|(1:74)(2:805|(1:807)(1:808))|75|76|(4:79|(2:81|82)(1:84)|83|77)|85|86|(5:88|(2:(1:91)(1:676)|92)(1:677)|(1:675)(2:98|(2:102|103))|674|103)(2:678|(5:(1:788)(2:685|(4:687|688|(15:690|(2:692|(1:694)(4:739|(2:741|742)|743|744))(2:745|(14:749|(1:751)|696|697|(1:699)(2:731|(1:733)(2:734|(1:736)(9:737|738|701|(1:730)(2:706|(2:708|(6:712|(1:714)|720|716|(1:718)|719))(6:722|(2:724|(2:726|(1:728)))|720|716|(0)|719))|721|720|716|(0)|719)))|700|701|(0)|730|721|720|716|(0)|719))|695|696|697|(0)(0)|700|701|(0)|730|721|720|716|(0)|719)(10:753|(2:755|(1:757)(4:758|(2:760|742)|743|744))(9:761|(1:786)(1:765)|766|(1:785)(2:770|(1:772))|784|774|(2:776|(2:778|(1:780)(2:781|738)))(1:783)|782|(0)(0))|701|(0)|730|721|720|716|(0)|719)|65))|787|688|(0)(0)|65)(3:789|(5:791|(3:793|(2:795|796)|797)(2:798|(3:800|(2:802|796)|797))|63|64|65)(1:804)|803))|(1:673)(2:107|(1:109))|(4:111|(1:113)(1:116)|114|115)|(3:118|(3:120|(1:122)(3:660|661|(3:663|(1:665)(1:667)|666))|123)(1:671)|668)(1:672)|(3:125|(1:131)|132)(1:659)|133|(3:654|(1:656)(1:658)|657)(2:136|137)|138|(1:140)|141|(1:143)(1:646)|144|(2:644|645)(1:148)|149|150|(3:153|(1:155)|(3:157|158|(62:162|163|164|(51:168|169|170|(1:632)(1:174)|175|(1:631)(1:178)|179|180|(1:630)|187|(1:629)(1:194)|195|(14:197|(1:199)(2:376|(3:378|379|65)(2:380|(1:(1:383)(11:384|201|202|203|(2:206|204)|207|208|(1:375)(1:211)|212|(1:214)(1:374)|215))(2:385|(11:387|202|203|(1:204)|207|208|(0)|375|212|(0)(0)|215)(12:388|(1:394)(1:392)|393|203|(1:204)|207|208|(0)|375|212|(0)(0)|215))))|200|201|202|203|(1:204)|207|208|(0)|375|212|(0)(0)|215)(4:395|(6:397|(1:399)(3:404|(2:619|(1:621)(2:622|(1:626)))(1:408)|(3:410|(1:412)|413)(18:414|(1:416)|417|(2:615|(1:617)(1:618))(1:423)|424|425|(3:607|(1:(1:610)(2:611|(1:613)))|614)(1:429)|430|(2:(1:445)(2:433|(2:(2:436|(1:438))(1:441)|439)(2:442|(2:444|439)))|440)|446|(3:545|(1:606)(6:551|(2:604|605)(3:554|(1:558)|(1:603)(1:568))|(2:574|(2:576|(5:582|(2:586|587)|589|590|587))(5:591|(4:597|(2:601|587)|590|587)|589|590|587))|602|590|587)|588)(1:450)|451|(6:453|(1:543)(7:466|(1:542)(2:470|(9:524|525|526|527|528|529|530|531|532)(1:472))|473|(1:475)(1:523)|476|477|(7:511|512|513|514|515|516|(5:483|(1:485)|486|487|(2:492|(3:494|(2:499|500)(1:496)|(1:498))))(1:508))(4:479|(2:510|(0)(0))|481|(0)(0)))|509|486|487|(3:490|492|(0)))(1:544)|503|(3:507|402|403)|401|402|403))|400|401|402|403)|627|628)|216|(2:359|(4:361|(2:364|362)|365|366)(2:367|(1:369)(2:370|(1:372)(1:373))))(1:220)|221|(1:223)|224|(1:226)|227|(2:229|(1:231)(1:354))(2:355|(1:357)(1:358))|(1:233)(1:353)|234|(4:236|(2:239|237)|240|241)(1:352)|242|(1:244)|245|246|247|(1:249)|(4:251|252|253|(1:255))(1:348)|256|(1:258)|(1:343)(3:264|(5:267|(2:268|(1:341)(2:270|(2:273|274)(1:272)))|275|(1:278)(1:277)|265)|342)|(1:280)|281|(2:(1:284)|(1:291))|292|(1:340)(1:298)|299|(1:301)|(1:303)|304|(3:309|(4:311|(3:313|(4:315|(1:317)|318|319)(2:321|322)|320)|323|324)|325)|326|(1:339)(2:329|(1:333))|334|(1:336)|337|338|65)|637|(1:172)|632|175|(0)|631|179|180|(1:182)|630|187|(1:190)|629|195|(0)(0)|216|(1:218)|359|(0)(0)|221|(0)|224|(0)|227|(0)(0)|(0)(0)|234|(0)(0)|242|(0)|245|246|247|(0)|(0)(0)|256|(0)|(1:260)|343|(0)|281|(0)|292|(1:294)|340|299|(0)|(0)|304|(4:306|309|(0)|325)|326|(0)|339|334|(0)|337|338|65)))|643|637|(0)|632|175|(0)|631|179|180|(0)|630|187|(0)|629|195|(0)(0)|216|(0)|359|(0)(0)|221|(0)|224|(0)|227|(0)(0)|(0)(0)|234|(0)(0)|242|(0)|245|246|247|(0)|(0)(0)|256|(0)|(0)|343|(0)|281|(0)|292|(0)|340|299|(0)|(0)|304|(0)|326|(0)|339|334|(0)|337|338|65) */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x12c1, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x12c2, code lost:
    
        r1 = r50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x0597, code lost:
    
        if (r0.local_id != 0) goto L222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x05ba, code lost:
    
        if (r0.local_id != 0) goto L222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x047c, code lost:
    
        if (r5.local_id != 0) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:773:0x0511, code lost:
    
        if (r5.local_id != 0) goto L183;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0896  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x08a6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x08bb  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x08d3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0901  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0a49 A[LOOP:5: B:204:0x0a41->B:206:0x0a49, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0a65 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0a88  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x104c  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x10de  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x10ff  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x115d  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x11c5  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x1205  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x1260  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x12b9 A[Catch: Exception -> 0x12c1, TRY_LEAVE, TryCatch #12 {Exception -> 0x12c1, blocks: (B:247:0x129b, B:249:0x12b9), top: B:246:0x129b }] */
    /* JADX WARN: Removed duplicated region for block: B:251:0x12c7  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x12ee  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x12f9  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x134c  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x138e  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x13ad  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x13c5  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x13cb  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x13d7  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x13e4  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x1469 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:336:0x149b  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x12d7  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x122c  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x11e8  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x119c  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x1069  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x1096  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0a9b  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x0aa9  */
    /* JADX WARN: Removed duplicated region for block: B:475:0x0e94  */
    /* JADX WARN: Removed duplicated region for block: B:479:0x0ecb  */
    /* JADX WARN: Removed duplicated region for block: B:483:0x0f23  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0f76  */
    /* JADX WARN: Removed duplicated region for block: B:508:0x0f53  */
    /* JADX WARN: Removed duplicated region for block: B:511:0x0ea1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:523:0x0e98  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:690:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:699:0x0482  */
    /* JADX WARN: Removed duplicated region for block: B:703:0x0565 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:718:0x05c7  */
    /* JADX WARN: Removed duplicated region for block: B:731:0x0494  */
    /* JADX WARN: Removed duplicated region for block: B:753:0x04b0  */
    /* JADX WARN: Removed duplicated region for block: B:780:0x054a  */
    /* JADX WARN: Removed duplicated region for block: B:781:0x0553  */
    /* JADX WARN: Removed duplicated region for block: B:814:0x1539  */
    /* JADX WARN: Removed duplicated region for block: B:823:0x15b8  */
    /* JADX WARN: Removed duplicated region for block: B:835:0x1620  */
    /* JADX WARN: Removed duplicated region for block: B:857:0x1585  */
    /* JADX WARN: Removed duplicated region for block: B:863:0x0223 A[EDGE_INSN: B:863:0x0223->B:812:0x0223 BREAK  A[LOOP:2: B:55:0x0203->B:65:0x1519], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:864:0x01cb  */
    /* JADX WARN: Type inference failed for: r9v10 */
    /* JADX WARN: Type inference failed for: r9v11, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r9v13 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, long j2, String str2, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str3;
        String str4;
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
        String str5;
        Notification notification;
        long j3;
        long j4;
        int id;
        ArrayList<StoryNotification> arrayList2;
        LongSparseArray longSparseArray4;
        boolean z6;
        LongSparseArray longSparseArray5;
        MessageObject messageObject;
        ArrayList arrayList3;
        SharedPreferences sharedPreferences;
        int i8;
        LongSparseArray longSparseArray6;
        ArrayList<StoryNotification> arrayList4;
        DialogKey dialogKey;
        long j5;
        String str6;
        long j6;
        Integer num;
        TLRPC.User user;
        String string;
        TLRPC.User user2;
        boolean z7;
        TLRPC.FileLocation fileLocation;
        TLRPC.Chat chat;
        boolean z8;
        boolean z9;
        String str7;
        boolean z10;
        NotificationsController notificationsController2;
        int i9;
        ArrayList arrayList5;
        int i10;
        Notification notification2;
        String str8;
        boolean z11;
        LongSparseArray longSparseArray7;
        SharedPreferences sharedPreferences2;
        ArrayList arrayList6;
        LongSparseArray longSparseArray8;
        long j7;
        boolean z12;
        TLRPC.Chat chat2;
        boolean z13;
        DialogKey dialogKey2;
        TLRPC.FileLocation fileLocation2;
        String str9;
        String str10;
        TLRPC.Chat chat3;
        TLRPC.User user3;
        boolean z14;
        String str11;
        boolean canSendPlain;
        TLRPC.User user4;
        StringBuilder sb;
        String str12;
        TLRPC.FileLocation fileLocation3;
        String userName;
        int i11;
        SharedPreferences sharedPreferences3;
        SharedPreferences sharedPreferences4;
        TLRPC.User user5;
        File file;
        Bitmap bitmap;
        File file2;
        Bitmap bitmap2;
        LongSparseArray longSparseArray9;
        TLRPC.Chat chat4;
        boolean z15;
        String str13;
        int i12;
        String str14;
        ?? r9;
        String formatString;
        NotificationCompat.Action build;
        long j8;
        Person person;
        NotificationCompat.MessagingStyle messagingStyle;
        int i13;
        NotificationCompat.Action action;
        MessageObject messageObject2;
        long j9;
        DialogKey dialogKey3;
        ArrayList<TLRPC.TL_keyboardButtonRow> arrayList7;
        String str15;
        String str16;
        ArrayList<StoryNotification> arrayList8;
        long j10;
        LongSparseArray longSparseArray10;
        String str17;
        Bitmap bitmap3;
        int i14;
        String str18;
        String str19;
        NotificationCompat.MessagingStyle messagingStyle2;
        boolean[] zArr;
        String str20;
        String str21;
        long j11;
        long j12;
        String[] strArr;
        boolean z16;
        int i15;
        Person person2;
        File file3;
        File file4;
        File file5;
        int i16;
        TLRPC.ChatPhoto chatPhoto;
        TLRPC.FileLocation fileLocation4;
        TLRPC.UserProfilePhoto userProfilePhoto;
        TLRPC.FileLocation fileLocation5;
        TLRPC.UserProfilePhoto userProfilePhoto2;
        TLRPC.FileLocation fileLocation6;
        String str22;
        LongSparseArray longSparseArray11;
        String str23;
        String str24;
        TLRPC.ReplyMarkup replyMarkup;
        List messages;
        Uri uri2;
        final File file6;
        final Uri uriForFile;
        Bitmap createScaledBitmap;
        Canvas canvas;
        int i17;
        DialogKey dialogKey4;
        long j13;
        ArrayList<TLRPC.TL_keyboardButtonRow> arrayList9;
        Bitmap bitmap4;
        NotificationCompat.Action action2;
        MessageObject messageObject3;
        String str25;
        NotificationCompat.Action action3;
        ArrayList<StoryNotification> arrayList10;
        long j14;
        MessageObject messageObject4;
        TLRPC.TL_keyboardButtonCopy tL_keyboardButtonCopy;
        TLRPC.User user6;
        int size3;
        int i18;
        int i19;
        ArrayList<TLRPC.TL_keyboardButtonRow> arrayList11;
        TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow;
        TLRPC.Message message;
        TLRPC.ReplyMarkup replyMarkup2;
        LongSparseArray longSparseArray12;
        String formatPluralString;
        String string2;
        int i20;
        TLRPC.UserProfilePhoto userProfilePhoto3;
        TLRPC.FileLocation fileLocation7;
        Bitmap bitmap5;
        Bitmap decodeFile;
        TLRPC.UserProfilePhoto userProfilePhoto4;
        FileLog.d("showExtraNotifications pushMessages.size()=" + this.pushMessages.size());
        int i21 = Build.VERSION.SDK_INT;
        if (i21 >= 26) {
            str3 = "%1$s: %2$s";
            i4 = -1;
            str4 = ".provider";
            builder2 = builder;
            builder2.setChannelId(validateChannelId(j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
        } else {
            str3 = "%1$s: %2$s";
            str4 = ".provider";
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
        NotificationsController notificationsController3 = this;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList12 = new ArrayList();
        if (!notificationsController3.storyPushMessages.isEmpty()) {
            arrayList12.add(new DialogKey(0L, 0L, true));
        }
        LongSparseArray longSparseArray13 = new LongSparseArray();
        for (int i22 = 0; i22 < notificationsController3.pushMessages.size(); i22++) {
            MessageObject messageObject5 = notificationsController3.pushMessages.get(i22);
            long dialogId = messageObject5.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController3.currentAccount, messageObject5.messageOwner, getMessagesController().isForum(messageObject5));
            int i23 = notificationsSettings.getInt("dismissDate" + dialogId, 0);
            if (messageObject5.isStoryPush || messageObject5.messageOwner.date > i23) {
                ArrayList arrayList13 = (ArrayList) longSparseArray13.get(dialogId);
                if (arrayList13 == null) {
                    ArrayList arrayList14 = new ArrayList();
                    longSparseArray13.put(dialogId, arrayList14);
                    FileLog.d("showExtraNotifications: sortedDialogs += " + dialogId);
                    arrayList12.add(new DialogKey(dialogId, topicId, false));
                    arrayList13 = arrayList14;
                }
                arrayList13.add(messageObject5);
            } else {
                FileLog.d("showExtraNotifications: dialog " + dialogId + " is skipped, message date (" + messageObject5.messageOwner.date + " <= " + i23 + ")");
            }
        }
        LongSparseArray longSparseArray14 = new LongSparseArray();
        for (int i24 = 0; i24 < notificationsController3.wearNotificationsIds.size(); i24++) {
            longSparseArray14.put(notificationsController3.wearNotificationsIds.keyAt(i24), (Integer) notificationsController3.wearNotificationsIds.valueAt(i24));
        }
        notificationsController3.wearNotificationsIds.clear();
        ArrayList arrayList15 = new ArrayList();
        int i25 = Build.VERSION.SDK_INT;
        if (i25 > 27) {
            if (arrayList12.size() <= (notificationsController3.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i25 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                FileLog.d("showExtraNotifications: passcode=" + (SharedConfig.passcodeHash.length() <= 0) + " waitingForPasscode=" + z5 + " selfUserId=" + clientUserId + " useSummaryNotification=" + z4);
                longSparseArray = new LongSparseArray();
                size = arrayList12.size();
                i5 = 0;
                while (true) {
                    if (i5 < size) {
                        break;
                    }
                    int i26 = size;
                    if (arrayList15.size() >= 7) {
                        FileLog.d("showExtraNotifications: break from holders, count over 7");
                        break;
                    }
                    DialogKey dialogKey5 = (DialogKey) arrayList12.get(i5);
                    ArrayList arrayList16 = arrayList15;
                    int i27 = i5;
                    if (dialogKey5.story) {
                        ArrayList<StoryNotification> arrayList17 = new ArrayList<>();
                        if (notificationsController3.storyPushMessages.isEmpty()) {
                            FileLog.d("showExtraNotifications: [" + dialogKey5.dialogId + "] continue; story but storyPushMessages is empty");
                            j7 = clientUserId;
                            longSparseArray7 = longSparseArray;
                            z11 = z4;
                            z10 = z5;
                            longSparseArray8 = longSparseArray14;
                            longSparseArray5 = longSparseArray13;
                            arrayList6 = arrayList12;
                            sharedPreferences2 = notificationsSettings;
                            notification2 = build2;
                            notificationsController2 = notificationsController3;
                            i9 = i26;
                            arrayList5 = arrayList16;
                            i10 = i27;
                            str8 = str3;
                            i5 = i10 + 1;
                            arrayList15 = arrayList5;
                            size = i9;
                            z5 = z10;
                            arrayList12 = arrayList6;
                            z4 = z11;
                            notificationsSettings = sharedPreferences2;
                            longSparseArray13 = longSparseArray5;
                            clientUserId = j7;
                            longSparseArray14 = longSparseArray8;
                            str3 = str8;
                            longSparseArray = longSparseArray7;
                            build2 = notification2;
                            notificationsController3 = notificationsController2;
                        } else {
                            notification = build2;
                            str5 = "showExtraNotifications: [";
                            long j15 = notificationsController3.storyPushMessages.get(0).dialogId;
                            int i28 = 0;
                            for (Iterator<Integer> it = notificationsController3.storyPushMessages.get(0).dateByIds.keySet().iterator(); it.hasNext(); it = it) {
                                i28 = Math.max(i28, it.next().intValue());
                            }
                            longSparseArray4 = longSparseArray;
                            z6 = z4;
                            longSparseArray5 = longSparseArray13;
                            messageObject = null;
                            id = i28;
                            j3 = 0;
                            arrayList2 = arrayList17;
                            j4 = j15;
                        }
                    } else {
                        str5 = "showExtraNotifications: [";
                        notification = build2;
                        long j16 = dialogKey5.dialogId;
                        j3 = dialogKey5.topicId;
                        ArrayList<StoryNotification> arrayList18 = (ArrayList) longSparseArray13.get(j16);
                        j4 = j16;
                        id = ((MessageObject) arrayList18.get(0)).getId();
                        arrayList2 = arrayList18;
                        longSparseArray4 = longSparseArray;
                        z6 = z4;
                        longSparseArray5 = longSparseArray13;
                        messageObject = (MessageObject) arrayList18.get(0);
                    }
                    int i29 = (Integer) longSparseArray14.get(dialogKey5.dialogId);
                    if (dialogKey5.story) {
                        i29 = 2147483646;
                        arrayList3 = arrayList12;
                        sharedPreferences = notificationsSettings;
                    } else {
                        arrayList3 = arrayList12;
                        sharedPreferences = notificationsSettings;
                        long j17 = dialogKey5.dialogId;
                        if (i29 == null) {
                            i29 = Integer.valueOf(((int) j17) + ((int) (j17 >> 32)));
                        } else {
                            longSparseArray14.remove(j17);
                        }
                    }
                    Integer num2 = i29;
                    int i30 = 0;
                    for (int i31 = 0; i31 < arrayList2.size(); i31++) {
                        if (i30 < ((MessageObject) arrayList2.get(i31)).messageOwner.date) {
                            i30 = ((MessageObject) arrayList2.get(i31)).messageOwner.date;
                        }
                    }
                    if (dialogKey5.story) {
                        TLRPC.User user7 = getMessagesController().getUser(Long.valueOf(j4));
                        longSparseArray6 = longSparseArray14;
                        if (notificationsController3.storyPushMessages.size() == 1) {
                            string = user7 != null ? UserObject.getFirstName(user7) : notificationsController3.storyPushMessages.get(0).localName;
                            i8 = i30;
                        } else {
                            i8 = i30;
                            string = LocaleController.formatPluralString("Stories", notificationsController3.storyPushMessages.size(), new Object[0]);
                        }
                        if (user7 == null || (userProfilePhoto4 = user7.photo) == null || (fileLocation = userProfilePhoto4.photo_small) == null) {
                            arrayList4 = arrayList2;
                        } else {
                            arrayList4 = arrayList2;
                            if (fileLocation.volume_id != 0 && fileLocation.local_id != 0) {
                                dialogKey = dialogKey5;
                                j5 = clientUserId;
                                str6 = "Stories";
                                j6 = j4;
                                z7 = false;
                                chat = null;
                                z8 = false;
                                z9 = false;
                                num = num2;
                                user2 = user7;
                            }
                        }
                        dialogKey = dialogKey5;
                        j5 = clientUserId;
                        str6 = "Stories";
                        j6 = j4;
                        z7 = false;
                        fileLocation = null;
                        chat = null;
                        z8 = false;
                        z9 = false;
                        num = num2;
                        user2 = user7;
                    } else {
                        i8 = i30;
                        longSparseArray6 = longSparseArray14;
                        arrayList4 = arrayList2;
                        if (DialogObject.isEncryptedDialog(j4)) {
                            dialogKey = dialogKey5;
                            j5 = clientUserId;
                            str6 = "Stories";
                            j6 = j4;
                            num = num2;
                            if (j6 != globalSecretChatId) {
                                int encryptedChatId = DialogObject.getEncryptedChatId(j6);
                                TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                                if (encryptedChat == null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        str7 = "not found secret chat to show dialog notification " + encryptedChatId;
                                        FileLog.w(str7);
                                    }
                                    z10 = z5;
                                    notificationsController2 = notificationsController3;
                                    i9 = i26;
                                    arrayList5 = arrayList16;
                                    i10 = i27;
                                    notification2 = notification;
                                    str8 = str3;
                                    z11 = z6;
                                    longSparseArray7 = longSparseArray4;
                                    sharedPreferences2 = sharedPreferences;
                                    arrayList6 = arrayList3;
                                    longSparseArray8 = longSparseArray6;
                                    j7 = j5;
                                } else {
                                    user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                                    if (user == null) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            str7 = "not found secret chat user to show dialog notification " + encryptedChat.user_id;
                                            FileLog.w(str7);
                                        }
                                        z10 = z5;
                                        notificationsController2 = notificationsController3;
                                        i9 = i26;
                                        arrayList5 = arrayList16;
                                        i10 = i27;
                                        notification2 = notification;
                                        str8 = str3;
                                        z11 = z6;
                                        longSparseArray7 = longSparseArray4;
                                        sharedPreferences2 = sharedPreferences;
                                        arrayList6 = arrayList3;
                                        longSparseArray8 = longSparseArray6;
                                        j7 = j5;
                                    }
                                }
                                i5 = i10 + 1;
                                arrayList15 = arrayList5;
                                size = i9;
                                z5 = z10;
                                arrayList12 = arrayList6;
                                z4 = z11;
                                notificationsSettings = sharedPreferences2;
                                longSparseArray13 = longSparseArray5;
                                clientUserId = j7;
                                longSparseArray14 = longSparseArray8;
                                str3 = str8;
                                longSparseArray = longSparseArray7;
                                build2 = notification2;
                                notificationsController3 = notificationsController2;
                            } else {
                                user = null;
                            }
                            string = LocaleController.getString(R.string.SecretChatName);
                            user2 = user;
                            z7 = false;
                            fileLocation = null;
                            chat = null;
                            z8 = false;
                            z9 = false;
                        } else {
                            if (messageObject == null || messageObject.isReactionPush || messageObject.isStoryReactionPush) {
                                j6 = j4;
                            } else {
                                j6 = j4;
                                if (j6 != 777000) {
                                    z12 = true;
                                    if (DialogObject.isUserDialog(j6)) {
                                        str6 = "Stories";
                                        num = num2;
                                        chat2 = getMessagesController().getChat(Long.valueOf(-j6));
                                        if (chat2 != null) {
                                            boolean z17 = chat2.megagroup;
                                            z13 = ChatObject.isChannel(chat2) && !chat2.megagroup;
                                            String str26 = chat2.title;
                                            z8 = z17;
                                            TLRPC.ChatPhoto chatPhoto2 = chat2.photo;
                                            if (chatPhoto2 == null || (fileLocation2 = chatPhoto2.photo_small) == null) {
                                                j5 = clientUserId;
                                                dialogKey2 = dialogKey5;
                                            } else {
                                                j5 = clientUserId;
                                                dialogKey2 = dialogKey5;
                                                if (fileLocation2.volume_id != 0) {
                                                }
                                            }
                                            fileLocation2 = null;
                                            if (j3 != 0) {
                                                dialogKey = dialogKey2;
                                                TLRPC.TL_forumTopic findTopic = getMessagesController().getTopicsController().findTopic(chat2.id, j3);
                                                if (findTopic != null) {
                                                    str9 = findTopic.title + " in " + str26;
                                                    if (z12) {
                                                        str10 = str9;
                                                        chat3 = chat2;
                                                        user3 = null;
                                                        TLRPC.Chat chat5 = chat3;
                                                        canSendPlain = z12;
                                                        user4 = user3;
                                                        chat2 = chat5;
                                                        boolean z18 = z13;
                                                        str11 = str10;
                                                        z14 = z18;
                                                    } else {
                                                        z14 = z13;
                                                        str11 = str9;
                                                        canSendPlain = ChatObject.canSendPlain(chat2);
                                                        user4 = null;
                                                    }
                                                }
                                            } else {
                                                dialogKey = dialogKey2;
                                            }
                                            str9 = str26;
                                            if (z12) {
                                            }
                                        } else if (messageObject.isFcmMessage()) {
                                            boolean isSupergroup = messageObject.isSupergroup();
                                            String str27 = messageObject.localName;
                                            z8 = isSupergroup;
                                            dialogKey = dialogKey5;
                                            j5 = clientUserId;
                                            z14 = messageObject.localChannel;
                                            user4 = null;
                                            canSendPlain = false;
                                            str11 = str27;
                                            fileLocation2 = null;
                                        } else {
                                            if (BuildVars.LOGS_ENABLED) {
                                                sb = new StringBuilder();
                                                str12 = "not found chat to show dialog notification ";
                                                sb.append(str12);
                                                sb.append(j6);
                                                FileLog.w(sb.toString());
                                            }
                                            z10 = z5;
                                            notificationsController2 = notificationsController3;
                                            i9 = i26;
                                            arrayList5 = arrayList16;
                                            i10 = i27;
                                            notification2 = notification;
                                            str8 = str3;
                                            z11 = z6;
                                            longSparseArray7 = longSparseArray4;
                                            sharedPreferences2 = sharedPreferences;
                                            arrayList6 = arrayList3;
                                            longSparseArray8 = longSparseArray6;
                                            j7 = clientUserId;
                                        }
                                        if (j6 == UserObject.VERIFY) {
                                        }
                                        user2 = user4;
                                        z9 = canSendPlain;
                                        z7 = z14;
                                        fileLocation3 = fileLocation2;
                                        string = str11;
                                        if (j6 == UserObject.VERIFY) {
                                        }
                                        chat = chat2;
                                        fileLocation = fileLocation3;
                                    } else {
                                        user3 = getMessagesController().getUser(Long.valueOf(j6));
                                        if (user3 != null) {
                                            userName = UserObject.getUserName(user3);
                                            TLRPC.UserProfilePhoto userProfilePhoto5 = user3.photo;
                                            if (userProfilePhoto5 != null && (fileLocation2 = userProfilePhoto5.photo_small) != null) {
                                                str6 = "Stories";
                                                num = num2;
                                                if (fileLocation2.volume_id != 0) {
                                                }
                                                fileLocation2 = null;
                                                if (j6 != UserObject.VERIFY) {
                                                    i11 = R.string.VerifyCodesNotifications;
                                                } else if (UserObject.isReplyUser(j6)) {
                                                    i11 = R.string.RepliesTitle;
                                                } else if (j6 == clientUserId) {
                                                    i11 = R.string.MessageScheduledReminderNotification;
                                                } else {
                                                    dialogKey = dialogKey5;
                                                    j5 = clientUserId;
                                                    str10 = userName;
                                                    chat3 = null;
                                                    z13 = false;
                                                    z8 = false;
                                                    TLRPC.Chat chat52 = chat3;
                                                    canSendPlain = z12;
                                                    user4 = user3;
                                                    chat2 = chat52;
                                                    boolean z182 = z13;
                                                    str11 = str10;
                                                    z14 = z182;
                                                    if (j6 == UserObject.VERIFY || messageObject == null || messageObject.getForwardedFromId() == null) {
                                                        user2 = user4;
                                                        z9 = canSendPlain;
                                                    } else {
                                                        Long forwardedFromId = messageObject.getForwardedFromId();
                                                        user2 = user4;
                                                        z9 = canSendPlain;
                                                        long longValue = forwardedFromId.longValue();
                                                        if (DialogObject.isUserDialog(longValue)) {
                                                            TLRPC.UserProfilePhoto userProfilePhoto6 = getMessagesController().getUser(forwardedFromId).photo;
                                                            if (userProfilePhoto6 != null && (fileLocation3 = userProfilePhoto6.photo_small) != null) {
                                                                z7 = z14;
                                                                if (fileLocation3.volume_id != 0) {
                                                                }
                                                                fileLocation3 = fileLocation2;
                                                                string = str11;
                                                                if (j6 == UserObject.VERIFY) {
                                                                    z9 = false;
                                                                }
                                                                chat = chat2;
                                                                fileLocation = fileLocation3;
                                                            }
                                                        } else {
                                                            z7 = z14;
                                                            TLRPC.ChatPhoto chatPhoto3 = getMessagesController().getChat(Long.valueOf(-longValue)).photo;
                                                            if (chatPhoto3 != null) {
                                                                fileLocation3 = chatPhoto3.photo_small;
                                                                if (fileLocation3 != null) {
                                                                    if (fileLocation3.volume_id != 0) {
                                                                    }
                                                                }
                                                            }
                                                            fileLocation3 = fileLocation2;
                                                            string = str11;
                                                            if (j6 == UserObject.VERIFY) {
                                                            }
                                                            chat = chat2;
                                                            fileLocation = fileLocation3;
                                                        }
                                                    }
                                                    z7 = z14;
                                                    fileLocation3 = fileLocation2;
                                                    string = str11;
                                                    if (j6 == UserObject.VERIFY) {
                                                    }
                                                    chat = chat2;
                                                    fileLocation = fileLocation3;
                                                }
                                                str11 = LocaleController.getString(i11);
                                                dialogKey = dialogKey5;
                                                j5 = clientUserId;
                                                z14 = false;
                                                z8 = false;
                                                canSendPlain = z12;
                                                user4 = user3;
                                                chat2 = null;
                                                if (j6 == UserObject.VERIFY) {
                                                }
                                                user2 = user4;
                                                z9 = canSendPlain;
                                                z7 = z14;
                                                fileLocation3 = fileLocation2;
                                                string = str11;
                                                if (j6 == UserObject.VERIFY) {
                                                }
                                                chat = chat2;
                                                fileLocation = fileLocation3;
                                            }
                                        } else if (messageObject.isFcmMessage()) {
                                            userName = messageObject.localName;
                                        } else {
                                            if (BuildVars.LOGS_ENABLED) {
                                                sb = new StringBuilder();
                                                str12 = "not found user to show dialog notification ";
                                                sb.append(str12);
                                                sb.append(j6);
                                                FileLog.w(sb.toString());
                                            }
                                            z10 = z5;
                                            notificationsController2 = notificationsController3;
                                            i9 = i26;
                                            arrayList5 = arrayList16;
                                            i10 = i27;
                                            notification2 = notification;
                                            str8 = str3;
                                            z11 = z6;
                                            longSparseArray7 = longSparseArray4;
                                            sharedPreferences2 = sharedPreferences;
                                            arrayList6 = arrayList3;
                                            longSparseArray8 = longSparseArray6;
                                            j7 = clientUserId;
                                        }
                                        str6 = "Stories";
                                        num = num2;
                                        fileLocation2 = null;
                                        if (j6 != UserObject.VERIFY) {
                                        }
                                        str11 = LocaleController.getString(i11);
                                        dialogKey = dialogKey5;
                                        j5 = clientUserId;
                                        z14 = false;
                                        z8 = false;
                                        canSendPlain = z12;
                                        user4 = user3;
                                        chat2 = null;
                                        if (j6 == UserObject.VERIFY) {
                                        }
                                        user2 = user4;
                                        z9 = canSendPlain;
                                        z7 = z14;
                                        fileLocation3 = fileLocation2;
                                        string = str11;
                                        if (j6 == UserObject.VERIFY) {
                                        }
                                        chat = chat2;
                                        fileLocation = fileLocation3;
                                    }
                                    i5 = i10 + 1;
                                    arrayList15 = arrayList5;
                                    size = i9;
                                    z5 = z10;
                                    arrayList12 = arrayList6;
                                    z4 = z11;
                                    notificationsSettings = sharedPreferences2;
                                    longSparseArray13 = longSparseArray5;
                                    clientUserId = j7;
                                    longSparseArray14 = longSparseArray8;
                                    str3 = str8;
                                    longSparseArray = longSparseArray7;
                                    build2 = notification2;
                                    notificationsController3 = notificationsController2;
                                }
                            }
                            z12 = false;
                            if (DialogObject.isUserDialog(j6)) {
                            }
                            i5 = i10 + 1;
                            arrayList15 = arrayList5;
                            size = i9;
                            z5 = z10;
                            arrayList12 = arrayList6;
                            z4 = z11;
                            notificationsSettings = sharedPreferences2;
                            longSparseArray13 = longSparseArray5;
                            clientUserId = j7;
                            longSparseArray14 = longSparseArray8;
                            str3 = str8;
                            longSparseArray = longSparseArray7;
                            build2 = notification2;
                            notificationsController3 = notificationsController2;
                        }
                    }
                    if (messageObject == null || !messageObject.isStoryReactionPush) {
                        sharedPreferences3 = sharedPreferences;
                    } else {
                        sharedPreferences3 = sharedPreferences;
                        if (!sharedPreferences3.getBoolean("EnableReactionsPreview", true)) {
                            string = LocaleController.getString(R.string.NotificationHiddenChatName);
                            fileLocation = null;
                            z9 = false;
                        }
                    }
                    if (z5) {
                        string = LocaleController.getString(DialogObject.isChatDialog(j6) ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName);
                        fileLocation = null;
                        z9 = false;
                    }
                    if (fileLocation != null) {
                        File pathToAttach = getFileLoader().getPathToAttach(fileLocation, true);
                        sharedPreferences4 = sharedPreferences3;
                        if (Build.VERSION.SDK_INT < 28) {
                            user5 = user2;
                            bitmap5 = null;
                            BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
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
                            bitmap = decodeFile;
                            file = pathToAttach;
                        } else {
                            user5 = user2;
                            bitmap5 = null;
                        }
                        file = pathToAttach;
                        bitmap = bitmap5;
                    } else {
                        sharedPreferences4 = sharedPreferences3;
                        user5 = user2;
                        file = null;
                        bitmap = null;
                    }
                    if (chat != null) {
                        Person.Builder name = new Person.Builder().setName(string);
                        if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                            loadRoundAvatar(file, name);
                        }
                        bitmap2 = bitmap;
                        file2 = file;
                        longSparseArray9 = longSparseArray4;
                        longSparseArray9.put(-chat.id, name.build());
                    } else {
                        file2 = file;
                        bitmap2 = bitmap;
                        longSparseArray9 = longSparseArray4;
                    }
                    Bitmap bitmap6 = bitmap2;
                    if ((z7 && !z8) || !z9 || SharedConfig.isWaitingForPasscodeEnter || j5 == j6 || UserObject.isReplyUser(j6)) {
                        str13 = "max_id";
                        str14 = "dialog_id";
                        z15 = z5;
                        i12 = id;
                        chat4 = chat;
                        build = null;
                    } else {
                        chat4 = chat;
                        z15 = z5;
                        Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) WearReplyReceiver.class);
                        intent.putExtra("dialog_id", j6);
                        intent.putExtra("max_id", id);
                        intent.putExtra("topic_id", j3);
                        intent.putExtra("currentAccount", notificationsController3.currentAccount);
                        str13 = "max_id";
                        PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160);
                        RemoteInput build3 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString(R.string.Reply)).build();
                        if (DialogObject.isChatDialog(j6)) {
                            i12 = id;
                            formatString = LocaleController.formatString(R.string.ReplyToGroup, string);
                            str14 = "dialog_id";
                            r9 = 1;
                        } else {
                            i12 = id;
                            str14 = "dialog_id";
                            r9 = 1;
                            formatString = LocaleController.formatString(R.string.ReplyToUser, string);
                        }
                        build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(r9).setSemanticAction(r9).addRemoteInput(build3).setShowsUserInterface(false).build();
                    }
                    Integer num3 = (Integer) notificationsController3.pushDialogs.get(j6);
                    if (num3 == null) {
                        num3 = 0;
                    }
                    DialogKey dialogKey6 = dialogKey;
                    int size4 = dialogKey6.story ? notificationsController3.storyPushMessages.size() : Math.max(num3.intValue(), arrayList4.size());
                    String format = (size4 <= 1 || Build.VERSION.SDK_INT >= 28) ? string : String.format("%1$s (%2$d)", string, Integer.valueOf(size4));
                    long j18 = j5;
                    Person person3 = (Person) longSparseArray9.get(j18);
                    if (Build.VERSION.SDK_INT >= 28 && person3 == null) {
                        TLRPC.User user8 = getMessagesController().getUser(Long.valueOf(j18));
                        if (user8 == null) {
                            user8 = getUserConfig().getCurrentUser();
                        }
                        if (user8 != null) {
                            try {
                                userProfilePhoto3 = user8.photo;
                            } catch (Throwable th) {
                                th = th;
                                j8 = j3;
                            }
                            if (userProfilePhoto3 != null && (fileLocation7 = userProfilePhoto3.photo_small) != null) {
                                j8 = j3;
                                try {
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                if (fileLocation7.volume_id != 0 && fileLocation7.local_id != 0) {
                                    Person.Builder name2 = new Person.Builder().setName(LocaleController.getString(R.string.FromYou));
                                    loadRoundAvatar(getFileLoader().getPathToAttach(user8.photo.photo_small, true), name2);
                                    person = name2.build();
                                    try {
                                        longSparseArray9.put(j18, person);
                                    } catch (Throwable th3) {
                                        th = th3;
                                        person3 = person;
                                        FileLog.e(th);
                                        person = person3;
                                        String str28 = "";
                                        messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                                        i13 = Build.VERSION.SDK_INT;
                                        if (i13 >= 28) {
                                        }
                                        messagingStyle.setConversationTitle(format);
                                        messagingStyle.setGroupConversation(i13 >= 28 || (!z7 && DialogObject.isChatDialog(j6)) || UserObject.isReplyUser(j6));
                                        StringBuilder sb2 = new StringBuilder();
                                        action = build;
                                        String[] strArr2 = new String[1];
                                        messageObject2 = messageObject;
                                        boolean[] zArr2 = new boolean[1];
                                        if (dialogKey6.story) {
                                        }
                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                        intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent2.setFlags(ConnectionsManager.FileTypeFile);
                                        intent2.addCategory("android.intent.category.LAUNCHER");
                                        if (messageObject2 == null) {
                                        }
                                        dialogKey4 = dialogKey3;
                                        if (dialogKey4.story) {
                                        }
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("show extra notifications chatId ");
                                        sb3.append(j6);
                                        sb3.append(" topicId ");
                                        j13 = j10;
                                        sb3.append(j13);
                                        FileLog.d(sb3.toString());
                                        if (j13 != 0) {
                                        }
                                        intent2.putExtra("currentAccount", notificationsController3.currentAccount);
                                        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                        if (action != null) {
                                        }
                                        int i32 = i14;
                                        arrayList9 = arrayList7;
                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, (Class<?>) AutoMessageHeardReceiver.class);
                                        intent3.addFlags(32);
                                        intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent3.putExtra(str14, j6);
                                        int i33 = i12;
                                        intent3.putExtra(str13, i33);
                                        intent3.putExtra("currentAccount", notificationsController3.currentAccount);
                                        bitmap4 = bitmap3;
                                        NotificationCompat.Action build4 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (!DialogObject.isEncryptedDialog(j6)) {
                                        }
                                        if (str25 == null) {
                                        }
                                        wearableExtender.setBridgeTag("tgaccount" + j9);
                                        if (dialogKey4.story) {
                                        }
                                        NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str17).setSmallIcon(R.drawable.notification).setContentText(sb2.toString()).setAutoCancel(true);
                                        if (dialogKey4.story) {
                                        }
                                        NotificationCompat.Builder category = autoCancel.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j14).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j14)).setCategory("msg");
                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                        intent4.putExtra("messageDate", i8);
                                        intent4.putExtra("dialogId", j6);
                                        intent4.putExtra("currentAccount", notificationsController3.currentAccount);
                                        if (dialogKey4.story) {
                                        }
                                        if (messageObject3 == null) {
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4, 167772160));
                                        if (z6) {
                                        }
                                        if (messageObject4 != null) {
                                        }
                                        tL_keyboardButtonCopy = null;
                                        if (tL_keyboardButtonCopy != null) {
                                        }
                                        if (dialogKey4.dialogId != UserObject.VERIFY) {
                                        }
                                        if (arrayList3.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j6)) {
                                        }
                                        if (bitmap4 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        if (chat4 == null) {
                                        }
                                        user6 = user5;
                                        Notification notification3 = notification;
                                        boolean z19 = z6;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        FileLog.d("showExtraNotifications: holders.add " + j6);
                                        i9 = i26;
                                        j7 = j9;
                                        z10 = z15;
                                        z11 = z19;
                                        longSparseArray7 = longSparseArray10;
                                        longSparseArray8 = longSparseArray6;
                                        i10 = i27;
                                        str8 = str15;
                                        arrayList6 = arrayList3;
                                        TLRPC.Chat chat6 = chat4;
                                        sharedPreferences2 = sharedPreferences4;
                                        notification2 = notification3;
                                        arrayList5 = arrayList16;
                                        arrayList5.add(new 1NotificationHolder(num.intValue(), j6, dialogKey4.story, j13, str17, user6, chat6, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j6, num);
                                        i5 = i10 + 1;
                                        arrayList15 = arrayList5;
                                        size = i9;
                                        z5 = z10;
                                        arrayList12 = arrayList6;
                                        z4 = z11;
                                        notificationsSettings = sharedPreferences2;
                                        longSparseArray13 = longSparseArray5;
                                        clientUserId = j7;
                                        longSparseArray14 = longSparseArray8;
                                        str3 = str8;
                                        longSparseArray = longSparseArray7;
                                        build2 = notification2;
                                        notificationsController3 = notificationsController2;
                                    }
                                    String str282 = "";
                                    messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                                    i13 = Build.VERSION.SDK_INT;
                                    if (i13 >= 28 || ((DialogObject.isChatDialog(j6) && !z7) || UserObject.isReplyUser(j6))) {
                                        messagingStyle.setConversationTitle(format);
                                    }
                                    messagingStyle.setGroupConversation(i13 >= 28 || (!z7 && DialogObject.isChatDialog(j6)) || UserObject.isReplyUser(j6));
                                    StringBuilder sb22 = new StringBuilder();
                                    action = build;
                                    String[] strArr22 = new String[1];
                                    messageObject2 = messageObject;
                                    boolean[] zArr22 = new boolean[1];
                                    if (dialogKey6.story) {
                                        LongSparseArray longSparseArray15 = longSparseArray9;
                                        j9 = j18;
                                        String str29 = str5;
                                        dialogKey3 = dialogKey6;
                                        int size5 = arrayList4.size() - 1;
                                        int i34 = 0;
                                        arrayList7 = null;
                                        while (size5 >= 0) {
                                            ArrayList<StoryNotification> arrayList19 = arrayList4;
                                            int i35 = i34;
                                            MessageObject messageObject6 = (MessageObject) arrayList19.get(size5);
                                            ArrayList<TLRPC.TL_keyboardButtonRow> arrayList20 = arrayList7;
                                            long topicId2 = MessageObject.getTopicId(notificationsController3.currentAccount, messageObject6.messageOwner, getMessagesController().isForum(messageObject6));
                                            if (j8 != topicId2) {
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append(str29);
                                                sb4.append(j6);
                                                sb4.append("] continue; topic id is not equal: topicId=");
                                                str18 = str282;
                                                str19 = str29;
                                                long j19 = j8;
                                                sb4.append(j19);
                                                sb4.append(" messageTopicId=");
                                                sb4.append(topicId2);
                                                sb4.append("; selfId=");
                                                sb4.append(getUserConfig().getClientUserId());
                                                FileLog.d(sb4.toString());
                                                z16 = z7;
                                                zArr = zArr22;
                                                j12 = j19;
                                                str22 = str3;
                                                str23 = str4;
                                            } else {
                                                str18 = str282;
                                                str19 = str29;
                                                long j20 = j8;
                                                String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject6, strArr22, zArr22);
                                                if (j6 != UserObject.VERIFY || messageObject6.getForwardedFromId() == null) {
                                                    messagingStyle2 = messagingStyle;
                                                    zArr = zArr22;
                                                    if (j6 == j9) {
                                                        strArr22[0] = string;
                                                    } else if (DialogObject.isChatDialog(j6) && messageObject6.messageOwner.from_scheduled) {
                                                        strArr22[0] = LocaleController.getString(R.string.NotificationMessageScheduledName);
                                                    }
                                                } else {
                                                    messagingStyle2 = messagingStyle;
                                                    zArr = zArr22;
                                                    strArr22[0] = getMessagesController().getPeerName(messageObject6.getForwardedFromId().longValue());
                                                }
                                                if (shortStringForMessage == null) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.w("message text is null for " + messageObject6.getId() + " did = " + messageObject6.getDialogId());
                                                    }
                                                    z16 = z7;
                                                    j12 = j20;
                                                    str22 = str3;
                                                    str23 = str4;
                                                    messagingStyle = messagingStyle2;
                                                } else {
                                                    if (sb22.length() > 0) {
                                                        sb22.append("\n\n");
                                                    }
                                                    if (j6 != j9 && messageObject6.messageOwner.from_scheduled && DialogObject.isUserDialog(j6)) {
                                                        str20 = str3;
                                                        shortStringForMessage = String.format(str20, LocaleController.getString(R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                        sb22.append(shortStringForMessage);
                                                        str21 = string;
                                                    } else {
                                                        str20 = str3;
                                                        String str30 = strArr22[0];
                                                        str21 = string;
                                                        if (str30 != null) {
                                                            sb22.append(String.format(str20, str30, shortStringForMessage));
                                                        } else {
                                                            sb22.append(shortStringForMessage);
                                                        }
                                                    }
                                                    String str31 = shortStringForMessage;
                                                    if (j6 != UserObject.VERIFY || messageObject6.getForwardedFromId() == null) {
                                                        if (!DialogObject.isUserDialog(j6)) {
                                                            if (z7) {
                                                                j11 = -j6;
                                                            } else if (DialogObject.isChatDialog(j6)) {
                                                                j11 = messageObject6.getSenderId();
                                                            }
                                                        }
                                                        j11 = j6;
                                                    } else {
                                                        j11 = messageObject6.getForwardedFromId().longValue();
                                                    }
                                                    j12 = j20;
                                                    LongSparseArray longSparseArray16 = longSparseArray15;
                                                    Person person4 = (Person) longSparseArray16.get(j11 + (j20 << 16));
                                                    String str32 = strArr22[0];
                                                    if (str32 == null) {
                                                        if (z15) {
                                                            if (DialogObject.isChatDialog(j6)) {
                                                                if (!z7) {
                                                                    i17 = R.string.NotificationHiddenChatUserName;
                                                                } else if (Build.VERSION.SDK_INT > 27) {
                                                                    i17 = R.string.NotificationHiddenChatName;
                                                                }
                                                                str32 = LocaleController.getString(i17);
                                                            } else if (Build.VERSION.SDK_INT > 27) {
                                                                i17 = R.string.NotificationHiddenName;
                                                                str32 = LocaleController.getString(i17);
                                                            }
                                                        }
                                                        str32 = str18;
                                                    }
                                                    if (person4 == null || !TextUtils.equals(person4.getName(), str32)) {
                                                        Person.Builder name3 = new Person.Builder().setName(str32);
                                                        if (!zArr[0] || DialogObject.isEncryptedDialog(j6) || Build.VERSION.SDK_INT < 28) {
                                                            strArr = strArr22;
                                                            z16 = z7;
                                                            i15 = size5;
                                                        } else {
                                                            if (DialogObject.isUserDialog(j6) || z7) {
                                                                strArr = strArr22;
                                                                z16 = z7;
                                                                file3 = file2;
                                                            } else {
                                                                long senderId = messageObject6.getSenderId();
                                                                strArr = strArr22;
                                                                z16 = z7;
                                                                TLRPC.User user9 = getMessagesController().getUser(Long.valueOf(senderId));
                                                                if (user9 == null && (user9 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                    getMessagesController().putUser(user9, true);
                                                                }
                                                                file3 = (user9 == null || (userProfilePhoto2 = user9.photo) == null || (fileLocation6 = userProfilePhoto2.photo_small) == null || fileLocation6.volume_id == 0 || fileLocation6.local_id == 0) ? null : getFileLoader().getPathToAttach(user9.photo.photo_small, true);
                                                            }
                                                            if (file3 == null && j6 == UserObject.VERIFY && messageObject6.getForwardedFromId() != null) {
                                                                if (j11 >= 0) {
                                                                    TLRPC.User user10 = getMessagesController().getUser(Long.valueOf(j11));
                                                                    if (user10 != null && (userProfilePhoto = user10.photo) != null && (fileLocation5 = userProfilePhoto.photo_small) != null) {
                                                                        file4 = file3;
                                                                        i16 = size5;
                                                                        if (fileLocation5.volume_id != 0 && fileLocation5.local_id != 0) {
                                                                            file5 = getFileLoader().getPathToAttach(user10.photo.photo_small, true);
                                                                            i15 = i16;
                                                                            loadRoundAvatar(file5, name3);
                                                                        }
                                                                        i15 = i16;
                                                                        file5 = file4;
                                                                        loadRoundAvatar(file5, name3);
                                                                    }
                                                                } else {
                                                                    file4 = file3;
                                                                    i16 = size5;
                                                                    TLRPC.Chat chat7 = getMessagesController().getChat(Long.valueOf(-j11));
                                                                    if (chat7 != null && (chatPhoto = chat7.photo) != null && (fileLocation4 = chatPhoto.photo_small) != null) {
                                                                        i15 = i16;
                                                                        if (fileLocation4.volume_id != 0 && fileLocation4.local_id != 0) {
                                                                            file5 = getFileLoader().getPathToAttach(chat7.photo.photo_small, true);
                                                                            loadRoundAvatar(file5, name3);
                                                                        }
                                                                        file5 = file4;
                                                                        loadRoundAvatar(file5, name3);
                                                                    }
                                                                    i15 = i16;
                                                                    file5 = file4;
                                                                    loadRoundAvatar(file5, name3);
                                                                }
                                                            }
                                                            file4 = file3;
                                                            i15 = size5;
                                                            file5 = file4;
                                                            loadRoundAvatar(file5, name3);
                                                        }
                                                        Person build5 = name3.build();
                                                        longSparseArray16.put(j11, build5);
                                                        person2 = build5;
                                                    } else {
                                                        strArr = strArr22;
                                                        z16 = z7;
                                                        person2 = person4;
                                                        i15 = size5;
                                                    }
                                                    if (DialogObject.isEncryptedDialog(j6)) {
                                                        str22 = str20;
                                                        longSparseArray11 = longSparseArray16;
                                                        str23 = str4;
                                                        messagingStyle = messagingStyle2;
                                                        str24 = str18;
                                                        messagingStyle.addMessage(str31, messageObject6.messageOwner.date * 1000, person2);
                                                    } else {
                                                        if (!zArr[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z15 || messageObject6.isSecretMedia() || !(messageObject6.type == 1 || messageObject6.isSticker())) {
                                                            str22 = str20;
                                                            longSparseArray11 = longSparseArray16;
                                                            str23 = str4;
                                                            messagingStyle = messagingStyle2;
                                                            str24 = str18;
                                                        } else {
                                                            File pathToMessage = getFileLoader().getPathToMessage(messageObject6.messageOwner);
                                                            if (pathToMessage.exists() && messageObject6.hasMediaSpoilers()) {
                                                                file6 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                if (file6.exists()) {
                                                                    str22 = str20;
                                                                    longSparseArray11 = longSparseArray16;
                                                                } else {
                                                                    try {
                                                                        Bitmap decodeFile2 = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                        Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile2);
                                                                        decodeFile2.recycle();
                                                                        createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile2.getWidth(), decodeFile2.getHeight(), true);
                                                                        Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                        stackBlurBitmapMax.recycle();
                                                                        canvas = new Canvas(createScaledBitmap);
                                                                        longSparseArray11 = longSparseArray16;
                                                                        try {
                                                                            notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i4) * 0.325f)));
                                                                            str22 = str20;
                                                                        } catch (Exception e) {
                                                                            e = e;
                                                                            str22 = str20;
                                                                        }
                                                                    } catch (Exception e2) {
                                                                        e = e2;
                                                                        str22 = str20;
                                                                        longSparseArray11 = longSparseArray16;
                                                                    }
                                                                    try {
                                                                        notificationsController3.mediaSpoilerEffect.setBounds(0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight());
                                                                        notificationsController3.mediaSpoilerEffect.draw(canvas);
                                                                        FileOutputStream fileOutputStream = new FileOutputStream(file6);
                                                                        createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                                                        fileOutputStream.close();
                                                                        createScaledBitmap.recycle();
                                                                        pathToMessage = file6;
                                                                    } catch (Exception e3) {
                                                                        e = e3;
                                                                        FileLog.e(e);
                                                                        NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str31, messageObject6.messageOwner.date * 1000, person2);
                                                                        String str33 = !messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                        if (pathToMessage.exists()) {
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                str22 = str20;
                                                                longSparseArray11 = longSparseArray16;
                                                                file6 = null;
                                                            }
                                                            NotificationCompat.MessagingStyle.Message message22 = new NotificationCompat.MessagingStyle.Message(str31, messageObject6.messageOwner.date * 1000, person2);
                                                            String str332 = !messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                            if (pathToMessage.exists()) {
                                                                str23 = str4;
                                                                if (getFileLoader().isLoadingFile(pathToMessage.getName())) {
                                                                    Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                    StringBuilder sb5 = new StringBuilder();
                                                                    sb5.append(notificationsController3.currentAccount);
                                                                    str24 = str18;
                                                                    sb5.append(str24);
                                                                    uriForFile = appendPath.appendPath(sb5.toString()).appendPath(pathToMessage.getName()).appendQueryParameter("final_path", pathToMessage.getAbsolutePath()).build();
                                                                    if (uriForFile == null) {
                                                                    }
                                                                }
                                                                str24 = str18;
                                                                uriForFile = null;
                                                                if (uriForFile == null) {
                                                                }
                                                            } else {
                                                                try {
                                                                    Context context = ApplicationLoader.applicationContext;
                                                                    StringBuilder sb6 = new StringBuilder();
                                                                    sb6.append(ApplicationLoader.getApplicationId());
                                                                    str23 = str4;
                                                                    try {
                                                                        sb6.append(str23);
                                                                        uriForFile = FileProvider.getUriForFile(context, sb6.toString(), pathToMessage);
                                                                        str24 = str18;
                                                                    } catch (Exception e4) {
                                                                        e = e4;
                                                                        FileLog.e(e);
                                                                        str24 = str18;
                                                                        uriForFile = null;
                                                                        if (uriForFile == null) {
                                                                        }
                                                                    }
                                                                } catch (Exception e5) {
                                                                    e = e5;
                                                                    str23 = str4;
                                                                }
                                                                if (uriForFile == null) {
                                                                    message22.setData(str332, uriForFile);
                                                                    messagingStyle = messagingStyle2;
                                                                    messagingStyle.addMessage(message22);
                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda55
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            NotificationsController.lambda$showExtraNotifications$42(uriForFile, file6);
                                                                        }
                                                                    }, 20000L);
                                                                    if (!TextUtils.isEmpty(messageObject6.caption)) {
                                                                        messagingStyle.addMessage(messageObject6.caption, messageObject6.messageOwner.date * 1000, person2);
                                                                    }
                                                                    if (zArr[0] && !z15 && messageObject6.isVoice()) {
                                                                        messages = messagingStyle.getMessages();
                                                                        if (!messages.isEmpty()) {
                                                                            File pathToMessage2 = getFileLoader().getPathToMessage(messageObject6.messageOwner);
                                                                            if (Build.VERSION.SDK_INT >= 24) {
                                                                                try {
                                                                                    uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + str23, pathToMessage2);
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
                                                                    messagingStyle = messagingStyle2;
                                                                }
                                                            }
                                                        }
                                                        messagingStyle.addMessage(str31, messageObject6.messageOwner.date * 1000, person2);
                                                        if (zArr[0]) {
                                                            messages = messagingStyle.getMessages();
                                                            if (!messages.isEmpty()) {
                                                            }
                                                        }
                                                    }
                                                    if (j6 == 777000 && (replyMarkup = messageObject6.messageOwner.reply_markup) != null) {
                                                        arrayList7 = replyMarkup.rows;
                                                        i34 = messageObject6.getId();
                                                        i4 = -1;
                                                        size5 = i15 - 1;
                                                        str4 = str23;
                                                        str282 = str24;
                                                        string = str21;
                                                        zArr22 = zArr;
                                                        strArr22 = strArr;
                                                        arrayList4 = arrayList19;
                                                        str29 = str19;
                                                        z7 = z16;
                                                        longSparseArray15 = longSparseArray11;
                                                        j8 = j12;
                                                        str3 = str22;
                                                    }
                                                    i34 = i35;
                                                    arrayList7 = arrayList20;
                                                    i4 = -1;
                                                    size5 = i15 - 1;
                                                    str4 = str23;
                                                    str282 = str24;
                                                    string = str21;
                                                    zArr22 = zArr;
                                                    strArr22 = strArr;
                                                    arrayList4 = arrayList19;
                                                    str29 = str19;
                                                    z7 = z16;
                                                    longSparseArray15 = longSparseArray11;
                                                    j8 = j12;
                                                    str3 = str22;
                                                }
                                            }
                                            longSparseArray11 = longSparseArray15;
                                            str24 = str18;
                                            strArr = strArr22;
                                            i15 = size5;
                                            str21 = string;
                                            i34 = i35;
                                            arrayList7 = arrayList20;
                                            i4 = -1;
                                            size5 = i15 - 1;
                                            str4 = str23;
                                            str282 = str24;
                                            string = str21;
                                            zArr22 = zArr;
                                            strArr22 = strArr;
                                            arrayList4 = arrayList19;
                                            str29 = str19;
                                            z7 = z16;
                                            longSparseArray15 = longSparseArray11;
                                            j8 = j12;
                                            str3 = str22;
                                        }
                                        str15 = str3;
                                        str16 = str4;
                                        arrayList8 = arrayList4;
                                        j10 = j8;
                                        longSparseArray10 = longSparseArray15;
                                        str17 = string;
                                        bitmap3 = bitmap6;
                                        i14 = i34;
                                    } else {
                                        ArrayList<String> arrayList21 = new ArrayList<>();
                                        ArrayList<Object> arrayList22 = new ArrayList<>();
                                        Pair<Integer, Boolean> parseStoryPushes = notificationsController3.parseStoryPushes(arrayList21, arrayList22);
                                        int intValue = ((Integer) parseStoryPushes.first).intValue();
                                        boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                        if (booleanValue) {
                                            string2 = LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]);
                                        } else if (arrayList21.isEmpty()) {
                                            FileLog.d(str5 + j6 + "] continue; story but names is empty");
                                            longSparseArray7 = longSparseArray9;
                                            notificationsController2 = notificationsController3;
                                            i9 = i26;
                                            arrayList5 = arrayList16;
                                            i10 = i27;
                                            notification2 = notification;
                                            str8 = str3;
                                            z11 = z6;
                                            arrayList6 = arrayList3;
                                            longSparseArray8 = longSparseArray6;
                                            z10 = z15;
                                            sharedPreferences2 = sharedPreferences4;
                                            j7 = j18;
                                            i5 = i10 + 1;
                                            arrayList15 = arrayList5;
                                            size = i9;
                                            z5 = z10;
                                            arrayList12 = arrayList6;
                                            z4 = z11;
                                            notificationsSettings = sharedPreferences2;
                                            longSparseArray13 = longSparseArray5;
                                            clientUserId = j7;
                                            longSparseArray14 = longSparseArray8;
                                            str3 = str8;
                                            longSparseArray = longSparseArray7;
                                            build2 = notification2;
                                            notificationsController3 = notificationsController2;
                                        } else if (arrayList21.size() != 1) {
                                            dialogKey3 = dialogKey6;
                                            if (arrayList21.size() == 2) {
                                                longSparseArray12 = longSparseArray9;
                                                sb22.append(LocaleController.formatString(R.string.StoryNotification2, arrayList21.get(0), arrayList21.get(1)));
                                                j9 = j18;
                                                long j21 = Long.MAX_VALUE;
                                                while (i20 < notificationsController3.storyPushMessages.size()) {
                                                }
                                                messagingStyle.setGroupConversation(false);
                                                if (arrayList21.size() == 1) {
                                                }
                                                messagingStyle.addMessage(sb22, j21, new Person.Builder().setName(r0).build());
                                                if (booleanValue) {
                                                }
                                                arrayList7 = null;
                                            } else {
                                                longSparseArray12 = longSparseArray9;
                                                if (arrayList21.size() == 3 && notificationsController3.storyPushMessages.size() == 3) {
                                                    j9 = j18;
                                                    formatPluralString = LocaleController.formatString(R.string.StoryNotification3, notificationsController3.cutLastName(arrayList21.get(0)), notificationsController3.cutLastName(arrayList21.get(1)), notificationsController3.cutLastName(arrayList21.get(2)));
                                                } else {
                                                    j9 = j18;
                                                    formatPluralString = LocaleController.formatPluralString("StoryNotification4", notificationsController3.storyPushMessages.size() - 2, notificationsController3.cutLastName(arrayList21.get(0)), notificationsController3.cutLastName(arrayList21.get(1)));
                                                }
                                                sb22.append(formatPluralString);
                                                long j212 = Long.MAX_VALUE;
                                                while (i20 < notificationsController3.storyPushMessages.size()) {
                                                }
                                                messagingStyle.setGroupConversation(false);
                                                if (arrayList21.size() == 1) {
                                                }
                                                messagingStyle.addMessage(sb22, j212, new Person.Builder().setName(r0).build());
                                                if (booleanValue) {
                                                }
                                                arrayList7 = null;
                                            }
                                        } else if (intValue == 1) {
                                            string2 = LocaleController.getString("StoryNotificationSingle");
                                        } else {
                                            dialogKey3 = dialogKey6;
                                            sb22.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList21.get(0)));
                                            longSparseArray12 = longSparseArray9;
                                            j9 = j18;
                                            long j2122 = Long.MAX_VALUE;
                                            for (i20 = 0; i20 < notificationsController3.storyPushMessages.size(); i20++) {
                                                j2122 = Math.min(notificationsController3.storyPushMessages.get(i20).date, j2122);
                                            }
                                            messagingStyle.setGroupConversation(false);
                                            String formatPluralString2 = (arrayList21.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str6, intValue, new Object[0]) : arrayList21.get(0);
                                            messagingStyle.addMessage(sb22, j2122, new Person.Builder().setName(formatPluralString2).build());
                                            if (booleanValue) {
                                                bitmap3 = loadMultipleAvatars(arrayList22);
                                                str17 = formatPluralString2;
                                                str15 = str3;
                                                str16 = str4;
                                                arrayList8 = arrayList4;
                                                j10 = j8;
                                                longSparseArray10 = longSparseArray12;
                                                i14 = 0;
                                            } else {
                                                str17 = formatPluralString2;
                                                str15 = str3;
                                                str16 = str4;
                                                arrayList8 = arrayList4;
                                                j10 = j8;
                                                longSparseArray10 = longSparseArray12;
                                                i14 = 0;
                                                bitmap3 = null;
                                            }
                                            arrayList7 = null;
                                        }
                                        sb22.append(string2);
                                        dialogKey3 = dialogKey6;
                                        longSparseArray12 = longSparseArray9;
                                        j9 = j18;
                                        long j21222 = Long.MAX_VALUE;
                                        while (i20 < notificationsController3.storyPushMessages.size()) {
                                        }
                                        messagingStyle.setGroupConversation(false);
                                        if (arrayList21.size() == 1) {
                                        }
                                        messagingStyle.addMessage(sb22, j21222, new Person.Builder().setName(formatPluralString2).build());
                                        if (booleanValue) {
                                        }
                                        arrayList7 = null;
                                    }
                                    Intent intent22 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                    intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    intent22.setFlags(ConnectionsManager.FileTypeFile);
                                    intent22.addCategory("android.intent.category.LAUNCHER");
                                    if (messageObject2 == null && messageObject2.isStoryReactionPush) {
                                        intent22.putExtra("storyId", Math.abs(messageObject2.getId()));
                                        str4 = str16;
                                        dialogKey4 = dialogKey3;
                                    } else {
                                        dialogKey4 = dialogKey3;
                                        if (dialogKey4.story) {
                                            str4 = str16;
                                            if (DialogObject.isEncryptedDialog(j6)) {
                                                intent22.putExtra("encId", DialogObject.getEncryptedChatId(j6));
                                            } else if (DialogObject.isUserDialog(j6)) {
                                                intent22.putExtra("userId", j6);
                                            } else {
                                                intent22.putExtra("chatId", -j6);
                                            }
                                        } else {
                                            long[] jArr2 = new long[notificationsController3.storyPushMessages.size()];
                                            int i36 = 0;
                                            while (i36 < notificationsController3.storyPushMessages.size()) {
                                                jArr2[i36] = notificationsController3.storyPushMessages.get(i36).dialogId;
                                                i36++;
                                                str16 = str16;
                                            }
                                            str4 = str16;
                                            intent22.putExtra("storyDialogIds", jArr2);
                                        }
                                    }
                                    StringBuilder sb32 = new StringBuilder();
                                    sb32.append("show extra notifications chatId ");
                                    sb32.append(j6);
                                    sb32.append(" topicId ");
                                    j13 = j10;
                                    sb32.append(j13);
                                    FileLog.d(sb32.toString());
                                    if (j13 != 0) {
                                        intent22.putExtra("topicId", j13);
                                    }
                                    intent22.putExtra("currentAccount", notificationsController3.currentAccount);
                                    PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                    NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                    if (action != null) {
                                        wearableExtender2.addAction(action);
                                    }
                                    int i322 = i14;
                                    arrayList9 = arrayList7;
                                    Intent intent32 = new Intent(ApplicationLoader.applicationContext, (Class<?>) AutoMessageHeardReceiver.class);
                                    intent32.addFlags(32);
                                    intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                    intent32.putExtra(str14, j6);
                                    int i332 = i12;
                                    intent32.putExtra(str13, i332);
                                    intent32.putExtra("currentAccount", notificationsController3.currentAccount);
                                    bitmap4 = bitmap3;
                                    NotificationCompat.Action build42 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                    if (!DialogObject.isEncryptedDialog(j6)) {
                                        action2 = build42;
                                        messageObject3 = messageObject2;
                                        str25 = j6 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j6) + "_" + i332 : null;
                                    } else if (DialogObject.isUserDialog(j6)) {
                                        str25 = "tguser" + j6 + "_" + i332;
                                        action2 = build42;
                                        messageObject3 = messageObject2;
                                    } else {
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append("tgchat");
                                        action2 = build42;
                                        messageObject3 = messageObject2;
                                        sb7.append(-j6);
                                        sb7.append("_");
                                        sb7.append(i332);
                                        str25 = sb7.toString();
                                    }
                                    if (str25 == null) {
                                        wearableExtender2.setDismissalId(str25);
                                        NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                        wearableExtender3.setDismissalId("summary_" + str25);
                                        builder.extend(wearableExtender3);
                                    }
                                    wearableExtender2.setBridgeTag("tgaccount" + j9);
                                    if (dialogKey4.story) {
                                        action3 = action2;
                                        arrayList10 = arrayList8;
                                        j14 = ((MessageObject) arrayList10.get(0)).messageOwner.date * 1000;
                                    } else {
                                        j14 = Long.MAX_VALUE;
                                        int i37 = 0;
                                        while (i37 < notificationsController3.storyPushMessages.size()) {
                                            j14 = Math.min(notificationsController3.storyPushMessages.get(i37).date, j14);
                                            i37++;
                                            action2 = action2;
                                        }
                                        action3 = action2;
                                        arrayList10 = arrayList8;
                                    }
                                    NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str17).setSmallIcon(R.drawable.notification).setContentText(sb22.toString()).setAutoCancel(true);
                                    if (dialogKey4.story) {
                                        arrayList10 = notificationsController3.storyPushMessages;
                                    }
                                    NotificationCompat.Builder category2 = autoCancel2.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j14).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j14)).setCategory("msg");
                                    Intent intent42 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                    intent42.putExtra("messageDate", i8);
                                    intent42.putExtra("dialogId", j6);
                                    intent42.putExtra("currentAccount", notificationsController3.currentAccount);
                                    if (dialogKey4.story) {
                                        intent42.putExtra("story", true);
                                    }
                                    if (messageObject3 == null) {
                                        messageObject4 = messageObject3;
                                        try {
                                            if (messageObject4.isStoryReactionPush) {
                                                intent42.putExtra("storyReaction", true);
                                            }
                                        } catch (Exception e6) {
                                            e = e6;
                                            FileLog.e(e);
                                            if (z6) {
                                            }
                                            if (messageObject4 != null) {
                                            }
                                            tL_keyboardButtonCopy = null;
                                            if (tL_keyboardButtonCopy != null) {
                                            }
                                            if (dialogKey4.dialogId != UserObject.VERIFY) {
                                            }
                                            if (arrayList3.size() != 1) {
                                            }
                                            if (DialogObject.isEncryptedDialog(j6)) {
                                            }
                                            if (bitmap4 != null) {
                                            }
                                            if (!AndroidUtilities.needShowPasscode(false)) {
                                            }
                                            if (chat4 == null) {
                                            }
                                            user6 = user5;
                                            Notification notification32 = notification;
                                            boolean z192 = z6;
                                            if (Build.VERSION.SDK_INT >= 26) {
                                            }
                                            FileLog.d("showExtraNotifications: holders.add " + j6);
                                            i9 = i26;
                                            j7 = j9;
                                            z10 = z15;
                                            z11 = z192;
                                            longSparseArray7 = longSparseArray10;
                                            longSparseArray8 = longSparseArray6;
                                            i10 = i27;
                                            str8 = str15;
                                            arrayList6 = arrayList3;
                                            TLRPC.Chat chat62 = chat4;
                                            sharedPreferences2 = sharedPreferences4;
                                            notification2 = notification32;
                                            arrayList5 = arrayList16;
                                            arrayList5.add(new 1NotificationHolder(num.intValue(), j6, dialogKey4.story, j13, str17, user6, chat62, category2, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                            notificationsController2 = this;
                                            notificationsController2.wearNotificationsIds.put(j6, num);
                                            i5 = i10 + 1;
                                            arrayList15 = arrayList5;
                                            size = i9;
                                            z5 = z10;
                                            arrayList12 = arrayList6;
                                            z4 = z11;
                                            notificationsSettings = sharedPreferences2;
                                            longSparseArray13 = longSparseArray5;
                                            clientUserId = j7;
                                            longSparseArray14 = longSparseArray8;
                                            str3 = str8;
                                            longSparseArray = longSparseArray7;
                                            build2 = notification2;
                                            notificationsController3 = notificationsController2;
                                        }
                                    } else {
                                        messageObject4 = messageObject3;
                                    }
                                    category2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42, 167772160));
                                    if (z6) {
                                        category2.setGroup(notificationsController3.notificationGroup);
                                        category2.setGroupAlertBehavior(1);
                                    }
                                    if (messageObject4 != null || (message = messageObject4.messageOwner) == null || (replyMarkup2 = message.reply_markup) == null) {
                                        tL_keyboardButtonCopy = null;
                                    } else {
                                        tL_keyboardButtonCopy = null;
                                        for (int i38 = 0; i38 < replyMarkup2.rows.size(); i38++) {
                                            int i39 = 0;
                                            while (true) {
                                                if (i39 >= replyMarkup2.rows.get(i38).buttons.size()) {
                                                    break;
                                                }
                                                if (replyMarkup2.rows.get(i38).buttons.get(i39) instanceof TLRPC.TL_keyboardButtonCopy) {
                                                    tL_keyboardButtonCopy = (TLRPC.TL_keyboardButtonCopy) replyMarkup2.rows.get(i38).buttons.get(i39);
                                                    break;
                                                }
                                                i39++;
                                            }
                                            if (tL_keyboardButtonCopy != null) {
                                                break;
                                            }
                                        }
                                    }
                                    if (tL_keyboardButtonCopy != null) {
                                        Intent intent5 = new Intent(ApplicationLoader.applicationContext, (Class<?>) CopyCodeReceiver.class);
                                        intent5.addFlags(32);
                                        intent5.setAction("org.telegram.messenger.ACTION_COPY_CODE");
                                        intent5.putExtra("text", tL_keyboardButtonCopy.copy_text);
                                        category2.addAction(new NotificationCompat.Action.Builder(R.drawable.msg_copy, tL_keyboardButtonCopy.text, PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent5, 167772160)).setShowsUserInterface(false).build());
                                    }
                                    if (dialogKey4.dialogId != UserObject.VERIFY) {
                                        if (action != null) {
                                            category2.addAction(action);
                                        }
                                        if (!z15 && !dialogKey4.story && (messageObject4 == null || !messageObject4.isStoryReactionPush)) {
                                            category2.addAction(action3);
                                        }
                                    }
                                    if (arrayList3.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey4.story) {
                                        category2.setSubText(str);
                                    }
                                    if (DialogObject.isEncryptedDialog(j6)) {
                                        category2.setLocalOnly(true);
                                    }
                                    if (bitmap4 != null) {
                                        category2.setLargeIcon(bitmap4);
                                    }
                                    if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList9 != null) {
                                        size3 = arrayList9.size();
                                        i18 = 0;
                                        while (i18 < size3) {
                                            ArrayList<TLRPC.TL_keyboardButtonRow> arrayList23 = arrayList9;
                                            TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow2 = arrayList23.get(i18);
                                            int size6 = tL_keyboardButtonRow2.buttons.size();
                                            int i40 = 0;
                                            while (i40 < size6) {
                                                TLRPC.KeyboardButton keyboardButton = tL_keyboardButtonRow2.buttons.get(i40);
                                                if (keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) {
                                                    i19 = size3;
                                                    arrayList11 = arrayList23;
                                                    Intent intent6 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationCallbackReceiver.class);
                                                    intent6.putExtra("currentAccount", notificationsController3.currentAccount);
                                                    intent6.putExtra("did", j6);
                                                    byte[] bArr = keyboardButton.data;
                                                    if (bArr != null) {
                                                        intent6.putExtra("data", bArr);
                                                    }
                                                    intent6.putExtra("mid", i322);
                                                    String str34 = keyboardButton.text;
                                                    Context context2 = ApplicationLoader.applicationContext;
                                                    int i41 = notificationsController3.lastButtonId;
                                                    tL_keyboardButtonRow = tL_keyboardButtonRow2;
                                                    notificationsController3.lastButtonId = i41 + 1;
                                                    category2.addAction(0, str34, PendingIntent.getBroadcast(context2, i41, intent6, 167772160));
                                                } else {
                                                    i19 = size3;
                                                    arrayList11 = arrayList23;
                                                    tL_keyboardButtonRow = tL_keyboardButtonRow2;
                                                }
                                                i40++;
                                                size3 = i19;
                                                arrayList23 = arrayList11;
                                                tL_keyboardButtonRow2 = tL_keyboardButtonRow;
                                            }
                                            i18++;
                                            arrayList9 = arrayList23;
                                        }
                                    }
                                    if (chat4 == null || user5 == null) {
                                        user6 = user5;
                                    } else {
                                        user6 = user5;
                                        String str35 = user6.phone;
                                        if (str35 != null && str35.length() > 0) {
                                            category2.addPerson("tel:+" + user6.phone);
                                        }
                                    }
                                    Notification notification322 = notification;
                                    boolean z1922 = z6;
                                    if (Build.VERSION.SDK_INT >= 26) {
                                        notificationsController3.setNotificationChannel(notification322, category2, z1922);
                                    }
                                    FileLog.d("showExtraNotifications: holders.add " + j6);
                                    i9 = i26;
                                    j7 = j9;
                                    z10 = z15;
                                    z11 = z1922;
                                    longSparseArray7 = longSparseArray10;
                                    longSparseArray8 = longSparseArray6;
                                    i10 = i27;
                                    str8 = str15;
                                    arrayList6 = arrayList3;
                                    TLRPC.Chat chat622 = chat4;
                                    sharedPreferences2 = sharedPreferences4;
                                    notification2 = notification322;
                                    arrayList5 = arrayList16;
                                    arrayList5.add(new 1NotificationHolder(num.intValue(), j6, dialogKey4.story, j13, str17, user6, chat622, category2, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                    notificationsController2 = this;
                                    notificationsController2.wearNotificationsIds.put(j6, num);
                                    i5 = i10 + 1;
                                    arrayList15 = arrayList5;
                                    size = i9;
                                    z5 = z10;
                                    arrayList12 = arrayList6;
                                    z4 = z11;
                                    notificationsSettings = sharedPreferences2;
                                    longSparseArray13 = longSparseArray5;
                                    clientUserId = j7;
                                    longSparseArray14 = longSparseArray8;
                                    str3 = str8;
                                    longSparseArray = longSparseArray7;
                                    build2 = notification2;
                                    notificationsController3 = notificationsController2;
                                }
                                person = person3;
                                String str2822 = "";
                                messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                                i13 = Build.VERSION.SDK_INT;
                                if (i13 >= 28) {
                                }
                                messagingStyle.setConversationTitle(format);
                                messagingStyle.setGroupConversation(i13 >= 28 || (!z7 && DialogObject.isChatDialog(j6)) || UserObject.isReplyUser(j6));
                                StringBuilder sb222 = new StringBuilder();
                                action = build;
                                String[] strArr222 = new String[1];
                                messageObject2 = messageObject;
                                boolean[] zArr222 = new boolean[1];
                                if (dialogKey6.story) {
                                }
                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                intent222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent222.setFlags(ConnectionsManager.FileTypeFile);
                                intent222.addCategory("android.intent.category.LAUNCHER");
                                if (messageObject2 == null) {
                                }
                                dialogKey4 = dialogKey3;
                                if (dialogKey4.story) {
                                }
                                StringBuilder sb322 = new StringBuilder();
                                sb322.append("show extra notifications chatId ");
                                sb322.append(j6);
                                sb322.append(" topicId ");
                                j13 = j10;
                                sb322.append(j13);
                                FileLog.d(sb322.toString());
                                if (j13 != 0) {
                                }
                                intent222.putExtra("currentAccount", notificationsController3.currentAccount);
                                PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                if (action != null) {
                                }
                                int i3222 = i14;
                                arrayList9 = arrayList7;
                                Intent intent322 = new Intent(ApplicationLoader.applicationContext, (Class<?>) AutoMessageHeardReceiver.class);
                                intent322.addFlags(32);
                                intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent322.putExtra(str14, j6);
                                int i3322 = i12;
                                intent322.putExtra(str13, i3322);
                                intent322.putExtra("currentAccount", notificationsController3.currentAccount);
                                bitmap4 = bitmap3;
                                NotificationCompat.Action build422 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (!DialogObject.isEncryptedDialog(j6)) {
                                }
                                if (str25 == null) {
                                }
                                wearableExtender22.setBridgeTag("tgaccount" + j9);
                                if (dialogKey4.story) {
                                }
                                NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str17).setSmallIcon(R.drawable.notification).setContentText(sb222.toString()).setAutoCancel(true);
                                if (dialogKey4.story) {
                                }
                                NotificationCompat.Builder category22 = autoCancel22.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j14).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j14)).setCategory("msg");
                                Intent intent422 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                intent422.putExtra("messageDate", i8);
                                intent422.putExtra("dialogId", j6);
                                intent422.putExtra("currentAccount", notificationsController3.currentAccount);
                                if (dialogKey4.story) {
                                }
                                if (messageObject3 == null) {
                                }
                                category22.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422, 167772160));
                                if (z6) {
                                }
                                if (messageObject4 != null) {
                                }
                                tL_keyboardButtonCopy = null;
                                if (tL_keyboardButtonCopy != null) {
                                }
                                if (dialogKey4.dialogId != UserObject.VERIFY) {
                                }
                                if (arrayList3.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(j6)) {
                                }
                                if (bitmap4 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                    size3 = arrayList9.size();
                                    i18 = 0;
                                    while (i18 < size3) {
                                    }
                                }
                                if (chat4 == null) {
                                }
                                user6 = user5;
                                Notification notification3222 = notification;
                                boolean z19222 = z6;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                FileLog.d("showExtraNotifications: holders.add " + j6);
                                i9 = i26;
                                j7 = j9;
                                z10 = z15;
                                z11 = z19222;
                                longSparseArray7 = longSparseArray10;
                                longSparseArray8 = longSparseArray6;
                                i10 = i27;
                                str8 = str15;
                                arrayList6 = arrayList3;
                                TLRPC.Chat chat6222 = chat4;
                                sharedPreferences2 = sharedPreferences4;
                                notification2 = notification3222;
                                arrayList5 = arrayList16;
                                arrayList5.add(new 1NotificationHolder(num.intValue(), j6, dialogKey4.story, j13, str17, user6, chat6222, category22, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j6, num);
                                i5 = i10 + 1;
                                arrayList15 = arrayList5;
                                size = i9;
                                z5 = z10;
                                arrayList12 = arrayList6;
                                z4 = z11;
                                notificationsSettings = sharedPreferences2;
                                longSparseArray13 = longSparseArray5;
                                clientUserId = j7;
                                longSparseArray14 = longSparseArray8;
                                str3 = str8;
                                longSparseArray = longSparseArray7;
                                build2 = notification2;
                                notificationsController3 = notificationsController2;
                            }
                        }
                    }
                    j8 = j3;
                    person = person3;
                    String str28222 = "";
                    messagingStyle = (person == null && (messageObject != null || !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest))) ? new NotificationCompat.MessagingStyle(person) : new NotificationCompat.MessagingStyle("");
                    i13 = Build.VERSION.SDK_INT;
                    if (i13 >= 28) {
                    }
                    messagingStyle.setConversationTitle(format);
                    messagingStyle.setGroupConversation(i13 >= 28 || (!z7 && DialogObject.isChatDialog(j6)) || UserObject.isReplyUser(j6));
                    StringBuilder sb2222 = new StringBuilder();
                    action = build;
                    String[] strArr2222 = new String[1];
                    messageObject2 = messageObject;
                    boolean[] zArr2222 = new boolean[1];
                    if (dialogKey6.story) {
                    }
                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                    intent2222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent2222.setFlags(ConnectionsManager.FileTypeFile);
                    intent2222.addCategory("android.intent.category.LAUNCHER");
                    if (messageObject2 == null) {
                    }
                    dialogKey4 = dialogKey3;
                    if (dialogKey4.story) {
                    }
                    StringBuilder sb3222 = new StringBuilder();
                    sb3222.append("show extra notifications chatId ");
                    sb3222.append(j6);
                    sb3222.append(" topicId ");
                    j13 = j10;
                    sb3222.append(j13);
                    FileLog.d(sb3222.toString());
                    if (j13 != 0) {
                    }
                    intent2222.putExtra("currentAccount", notificationsController3.currentAccount);
                    PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                    if (action != null) {
                    }
                    int i32222 = i14;
                    arrayList9 = arrayList7;
                    Intent intent3222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) AutoMessageHeardReceiver.class);
                    intent3222.addFlags(32);
                    intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent3222.putExtra(str14, j6);
                    int i33222 = i12;
                    intent3222.putExtra(str13, i33222);
                    intent3222.putExtra("currentAccount", notificationsController3.currentAccount);
                    bitmap4 = bitmap3;
                    NotificationCompat.Action build4222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString(R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (!DialogObject.isEncryptedDialog(j6)) {
                    }
                    if (str25 == null) {
                    }
                    wearableExtender222.setBridgeTag("tgaccount" + j9);
                    if (dialogKey4.story) {
                    }
                    NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str17).setSmallIcon(R.drawable.notification).setContentText(sb2222.toString()).setAutoCancel(true);
                    if (dialogKey4.story) {
                    }
                    NotificationCompat.Builder category222 = autoCancel222.setNumber(arrayList10.size()).setColor(-15618822).setGroupSummary(false).setWhen(j14).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j14)).setCategory("msg");
                    Intent intent4222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                    intent4222.putExtra("messageDate", i8);
                    intent4222.putExtra("dialogId", j6);
                    intent4222.putExtra("currentAccount", notificationsController3.currentAccount);
                    if (dialogKey4.story) {
                    }
                    if (messageObject3 == null) {
                    }
                    category222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4222, 167772160));
                    if (z6) {
                    }
                    if (messageObject4 != null) {
                    }
                    tL_keyboardButtonCopy = null;
                    if (tL_keyboardButtonCopy != null) {
                    }
                    if (dialogKey4.dialogId != UserObject.VERIFY) {
                    }
                    if (arrayList3.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j6)) {
                    }
                    if (bitmap4 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (chat4 == null) {
                    }
                    user6 = user5;
                    Notification notification32222 = notification;
                    boolean z192222 = z6;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    FileLog.d("showExtraNotifications: holders.add " + j6);
                    i9 = i26;
                    j7 = j9;
                    z10 = z15;
                    z11 = z192222;
                    longSparseArray7 = longSparseArray10;
                    longSparseArray8 = longSparseArray6;
                    i10 = i27;
                    str8 = str15;
                    arrayList6 = arrayList3;
                    TLRPC.Chat chat62222 = chat4;
                    sharedPreferences2 = sharedPreferences4;
                    notification2 = notification32222;
                    arrayList5 = arrayList16;
                    arrayList5.add(new 1NotificationHolder(num.intValue(), j6, dialogKey4.story, j13, str17, user6, chat62222, category222, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j6, num);
                    i5 = i10 + 1;
                    arrayList15 = arrayList5;
                    size = i9;
                    z5 = z10;
                    arrayList12 = arrayList6;
                    z4 = z11;
                    notificationsSettings = sharedPreferences2;
                    longSparseArray13 = longSparseArray5;
                    clientUserId = j7;
                    longSparseArray14 = longSparseArray8;
                    str3 = str8;
                    longSparseArray = longSparseArray7;
                    build2 = notification2;
                    notificationsController3 = notificationsController2;
                }
                LongSparseArray longSparseArray17 = longSparseArray;
                longSparseArray2 = longSparseArray14;
                Notification notification4 = build2;
                NotificationsController notificationsController4 = notificationsController3;
                ArrayList arrayList24 = arrayList15;
                if (z4) {
                    notificationsController = notificationsController4;
                    arrayList = arrayList24;
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
                        arrayList = arrayList24;
                    } catch (SecurityException e7) {
                        FileLog.e(e7);
                        notificationsController = this;
                        arrayList = arrayList24;
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
                ArrayList arrayList25 = new ArrayList(arrayList.size());
                FileLog.d("showExtraNotifications: holders.size()=" + arrayList.size());
                size2 = arrayList.size();
                i7 = 0;
                while (i7 < size2) {
                    ArrayList arrayList26 = arrayList;
                    1NotificationHolder r4 = (1NotificationHolder) arrayList26.get(i7);
                    arrayList25.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r4.dialogId)) {
                        longSparseArray3 = longSparseArray17;
                    } else {
                        NotificationCompat.Builder builder3 = r4.notification;
                        long j22 = r4.dialogId;
                        longSparseArray3 = longSparseArray17;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j22, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j22), !r4.story);
                        if (createNotificationShortcut != null) {
                            arrayList25.add(createNotificationShortcut);
                        }
                    }
                    FileLog.d("showExtraNotifications: holders[" + i7 + "].call()");
                    r4.call();
                    if (!unsupportedNotificationShortcut() && !arrayList25.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList25);
                    }
                    i7++;
                    arrayList = arrayList26;
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
        size = arrayList12.size();
        i5 = 0;
        while (true) {
            if (i5 < size) {
            }
            i5 = i10 + 1;
            arrayList15 = arrayList5;
            size = i9;
            z5 = z10;
            arrayList12 = arrayList6;
            z4 = z11;
            notificationsSettings = sharedPreferences2;
            longSparseArray13 = longSparseArray5;
            clientUserId = j7;
            longSparseArray14 = longSparseArray8;
            str3 = str8;
            longSparseArray = longSparseArray7;
            build2 = notification2;
            notificationsController3 = notificationsController2;
        }
        LongSparseArray longSparseArray172 = longSparseArray;
        longSparseArray2 = longSparseArray14;
        Notification notification42 = build2;
        NotificationsController notificationsController42 = notificationsController3;
        ArrayList arrayList242 = arrayList15;
        if (z4) {
        }
        i6 = 0;
        while (i6 < longSparseArray2.size()) {
        }
        ArrayList arrayList252 = new ArrayList(arrayList.size());
        FileLog.d("showExtraNotifications: holders.size()=" + arrayList.size());
        size2 = arrayList.size();
        i7 = 0;
        while (i7 < size2) {
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(54:122|123|(8:125|(2:127|(1:129)(1:578))(1:579)|130|(1:135)|572|573|(1:575)(1:577)|576)(1:580)|136|137|(3:553|554|(1:556)(4:(3:(2:567|(1:569)(1:570))(1:560)|561|562)(1:571)|563|(1:565)|566))(4:140|(4:142|(7:144|(2:146|(1:148))(1:166)|151|(1:153)|154|(3:(1:158)(2:161|(1:163)(1:164))|159|160)|165)(1:167)|149|150)|168|169)|(46:177|(1:551)(3:182|(1:184)(1:550)|(45:186|(1:188)(3:541|(1:543)(2:545|(1:547)(43:548|(2:191|(33:193|194|(4:196|(1:198)(1:537)|199|(1:201)(1:536))(1:538)|202|(5:527|528|(1:530)(1:535)|531|(1:533)(1:534))(2:206|(2:(3:209|(1:211)(1:503)|212)(3:504|(1:506)(1:508)|507)|213)(2:509|(6:(1:512)(1:525)|513|(1:515)(2:(1:522)(1:524)|523)|516|(1:518)(1:520)|519)(1:526)))|(1:215)(1:502)|216|(1:501)(1:220)|(1:500)(1:223)|(1:227)|(1:499)(1:232)|(7:234|(1:236)|237|(1:239)|240|241|(1:243)(1:497))(1:498)|(3:247|248|(1:252))|(1:258)(1:496)|259|(1:261)(2:445|(4:447|(2:450|448)|451|452)(18:453|(4:455|(2:457|(1:459)(2:460|(1:462)))|463|(1:(2:472|(16:480|264|(1:271)|272|273|274|(1:276)|277|(3:279|280|281)(1:441)|282|(1:284)(1:(12:425|(1:427)(3:428|429|(4:431|(1:433)(1:438)|434|(1:436)))|286|(2:421|422)(1:(4:291|292|(1:294)|401)(2:402|(4:417|418|(1:420)|401)(3:406|(2:408|(1:410))(2:411|(2:413|(2:415|416)))|401)))|(1:400)(7:(3:395|(1:397)(1:399)|398)|(2:304|(8:306|(1:(5:312|(1:314)|315|(1:317)(2:363|(1:365)(6:(2:373|374)(2:369|(1:371)(1:372))|320|(1:362)(4:328|(4:330|(3:332|(4:334|(1:336)(1:340)|337|338)(2:341|342)|339)|343|344)|345|346)|(4:354|(1:356)(1:359)|357|358)|360|361))|318)(6:375|376|(0)|315|(0)(0)|318))|377|376|(0)|315|(0)(0)|318)(4:378|(1:380)(3:383|(2:389|390)|393)|381|382))|394|(0)|315|(0)(0)|318)|319|320|(1:322)|362|(7:348|350|352|354|(0)(0)|357|358)|360|361))|285|286|(1:288)|421|422))(1:(2:482|(16:490|264|(3:267|269|271)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422)))))(2:491|(1:495))|263|264|(0)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422))|262|263|264|(0)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422))(1:540)|539|194|(0)(0)|202|(1:204)|527|528|(0)(0)|531|(0)(0)|(0)(0)|216|(1:218)|501|(0)|500|(2:225|227)|(1:229)|499|(0)(0)|(4:245|247|248|(2:250|252))|(0)(0)|259|(0)(0)|262|263|264|(0)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422))|544)|189|(0)(0)|539|194|(0)(0)|202|(0)|527|528|(0)(0)|531|(0)(0)|(0)(0)|216|(0)|501|(0)|500|(0)|(0)|499|(0)(0)|(0)|(0)(0)|259|(0)(0)|262|263|264|(0)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422))|549|189|(0)(0)|539|194|(0)(0)|202|(0)|527|528|(0)(0)|531|(0)(0)|(0)(0)|216|(0)|501|(0)|500|(0)|(0)|499|(0)(0)|(0)|(0)(0)|259|(0)(0)|262|263|264|(0)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422)|552|(1:179)|551|549|189|(0)(0)|539|194|(0)(0)|202|(0)|527|528|(0)(0)|531|(0)(0)|(0)(0)|216|(0)|501|(0)|500|(0)|(0)|499|(0)(0)|(0)|(0)(0)|259|(0)(0)|262|263|264|(0)|272|273|274|(0)|277|(0)(0)|282|(0)(0)|285|286|(0)|421|422) */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0be5, code lost:
    
        if (android.os.Build.VERSION.SDK_INT >= 26) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0b23, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0b40, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x035a, code lost:
    
        if (r3 != null) goto L134;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:110:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x03c8 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x03ed A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x04bf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x06ac A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x06f7 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0794 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:215:0x08ff  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x090a A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:222:0x091d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0924 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0931  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x093d A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0963  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x097a  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x09b2 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0adb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:276:0x0b1b A[Catch: all -> 0x0b23, TryCatch #0 {all -> 0x0b23, blocks: (B:274:0x0b00, B:276:0x0b1b, B:277:0x0b26, B:281:0x0b2e, B:282:0x0b36), top: B:273:0x0b00 }] */
    /* JADX WARN: Removed duplicated region for block: B:279:0x0b2a  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0b45 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0ba2  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0ce0 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x0cea A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:356:0x0e22 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:359:0x0e34 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:363:0x0cf4  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x0b4b  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x0b35  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x09c3 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:496:0x097f  */
    /* JADX WARN: Removed duplicated region for block: B:498:0x095f  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x0902  */
    /* JADX WARN: Removed duplicated region for block: B:530:0x08bc A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:533:0x08f2  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x08f8  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x08c8 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:538:0x0781  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x06d0  */
    /* JADX WARN: Removed duplicated region for block: B:556:0x058f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:557:0x0590  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x012f A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:580:0x04a0  */
    /* JADX WARN: Removed duplicated region for block: B:583:0x03e3 A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:585:0x03af  */
    /* JADX WARN: Removed duplicated region for block: B:590:0x03bb A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:592:0x03a9  */
    /* JADX WARN: Removed duplicated region for block: B:599:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:614:0x013a A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x032e A[Catch: Exception -> 0x0061, TryCatch #4 {Exception -> 0x0061, blocks: (B:12:0x0032, B:13:0x003e, B:15:0x0046, B:19:0x005d, B:23:0x006a, B:25:0x0074, B:27:0x0084, B:29:0x0089, B:35:0x008e, B:38:0x0094, B:39:0x00a7, B:41:0x00af, B:43:0x00e2, B:45:0x0103, B:47:0x010b, B:49:0x0114, B:52:0x011b, B:55:0x012f, B:57:0x01eb, B:58:0x0219, B:60:0x022b, B:62:0x0231, B:64:0x0235, B:66:0x0253, B:67:0x025a, B:70:0x0271, B:74:0x027d, B:76:0x0289, B:77:0x028f, B:79:0x029a, B:81:0x02a0, B:83:0x02ae, B:84:0x02ca, B:86:0x02dc, B:88:0x02ec, B:90:0x02f2, B:91:0x030e, B:93:0x032e, B:99:0x035c, B:101:0x0362, B:102:0x036e, B:104:0x0374, B:108:0x037e, B:111:0x0391, B:117:0x03c4, B:119:0x03c8, B:125:0x03ed, B:127:0x03f6, B:129:0x03fe, B:130:0x042b, B:132:0x0436, B:136:0x04a9, B:140:0x04c8, B:142:0x04ec, B:144:0x0504, B:146:0x0508, B:150:0x0564, B:153:0x0518, B:154:0x051e, B:158:0x052b, B:159:0x0536, B:160:0x0558, B:161:0x053b, B:163:0x0540, B:164:0x054c, B:165:0x055c, B:169:0x0570, B:172:0x05e3, B:182:0x05fc, B:184:0x0618, B:186:0x0651, B:188:0x065b, B:191:0x06ac, B:194:0x06d6, B:196:0x06f7, B:198:0x0738, B:199:0x0758, B:201:0x076a, B:202:0x078e, B:204:0x0794, B:209:0x07ac, B:211:0x07ba, B:212:0x07cd, B:216:0x0904, B:218:0x090a, B:225:0x0924, B:227:0x092a, B:234:0x093d, B:237:0x0948, B:240:0x0951, B:256:0x0975, B:259:0x0980, B:261:0x09b2, B:264:0x0a83, B:267:0x0add, B:269:0x0ae1, B:271:0x0ae7, B:284:0x0b45, B:292:0x0baa, B:298:0x0bf2, B:302:0x0c33, B:304:0x0c3b, B:306:0x0c3f, B:308:0x0c47, B:312:0x0c50, B:314:0x0ce0, B:317:0x0cea, B:320:0x0d2f, B:322:0x0d35, B:324:0x0d39, B:326:0x0d44, B:328:0x0d4a, B:330:0x0d54, B:332:0x0d63, B:334:0x0d73, B:336:0x0d90, B:337:0x0d9a, B:339:0x0dca, B:344:0x0dda, B:348:0x0dfc, B:350:0x0e02, B:352:0x0e0a, B:354:0x0e10, B:356:0x0e22, B:357:0x0e2f, B:358:0x0e42, B:359:0x0e34, B:360:0x0e45, B:365:0x0cf7, B:371:0x0d0b, B:374:0x0d19, B:375:0x0c79, B:377:0x0c7f, B:378:0x0c82, B:380:0x0c8c, B:382:0x0cda, B:385:0x0c94, B:387:0x0c9c, B:393:0x0cd5, B:395:0x0bfe, B:397:0x0c06, B:398:0x0c2e, B:400:0x0d22, B:408:0x0bbf, B:413:0x0bcd, B:418:0x0bd6, B:422:0x0be0, B:425:0x0b4d, B:427:0x0b5a, B:444:0x0b40, B:445:0x09c3, B:447:0x09c9, B:448:0x09d2, B:450:0x09da, B:452:0x09e9, B:453:0x09f0, B:455:0x09f6, B:459:0x0a05, B:462:0x0a11, B:463:0x0a17, B:465:0x0a1d, B:467:0x0a24, B:469:0x0a2d, B:472:0x0a35, B:474:0x0a39, B:476:0x0a3d, B:478:0x0a45, B:482:0x0a4f, B:484:0x0a55, B:486:0x0a59, B:488:0x0a61, B:491:0x0a68, B:493:0x0a73, B:495:0x0a79, B:503:0x07c6, B:504:0x07fa, B:506:0x080a, B:507:0x081d, B:508:0x0816, B:513:0x0850, B:515:0x0858, B:516:0x0870, B:523:0x086b, B:528:0x08ae, B:530:0x08bc, B:531:0x08cf, B:535:0x08c8, B:537:0x0745, B:541:0x0670, B:543:0x0680, B:544:0x0698, B:545:0x068a, B:547:0x068e, B:554:0x057d, B:560:0x0596, B:561:0x05a1, B:562:0x05c3, B:563:0x05c9, B:566:0x05d0, B:567:0x05a6, B:569:0x05ab, B:570:0x05b7, B:572:0x0442, B:575:0x044d, B:576:0x049a, B:577:0x0466, B:578:0x040b, B:581:0x03d4, B:583:0x03e3, B:587:0x03b5, B:588:0x03bd, B:589:0x03b8, B:590:0x03bb, B:595:0x0367, B:596:0x036a, B:602:0x02f8, B:604:0x02fe, B:610:0x02bb, B:612:0x026e, B:614:0x013a, B:617:0x0142, B:620:0x014b, B:621:0x0152, B:622:0x0162, B:624:0x0169, B:625:0x017f, B:627:0x0186, B:629:0x018e, B:630:0x01c1, B:631:0x0124, B:633:0x020e, B:248:0x0966), top: B:11:0x0032, inners: #3 }] */
    /* JADX WARN: Type inference failed for: r13v47 */
    /* JADX WARN: Type inference failed for: r13v48 */
    /* JADX WARN: Type inference failed for: r13v58 */
    /* JADX WARN: Type inference failed for: r1v110, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r1v111 */
    /* JADX WARN: Type inference failed for: r1v113, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r1v115, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r2v33, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r9v57, types: [org.telegram.messenger.MessageObject] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showOrUpdateNotification(boolean z) {
        MessageObject messageObject;
        Bitmap bitmap;
        long j;
        long j2;
        String str;
        TLRPC.Chat chat;
        boolean z2;
        int notifyOverride;
        long j3;
        boolean z3;
        TLRPC.User user;
        String userName;
        boolean z4;
        boolean z5;
        long j4;
        NotificationsController notificationsController;
        String string;
        boolean z6;
        String str2;
        SharedPreferences sharedPreferences;
        String str3;
        long j5;
        TLRPC.User user2;
        SharedPreferences sharedPreferences2;
        long j6;
        String str4;
        boolean z7;
        MessageObject messageObject2;
        String stringForMessage;
        String str5;
        String str6;
        StringBuilder sb;
        boolean z8;
        boolean z9;
        boolean z10;
        SharedPreferences sharedPreferences3;
        long j7;
        boolean z11;
        boolean z12;
        boolean z13;
        long j8;
        boolean z14;
        boolean z15;
        String str7;
        NotificationCompat.Builder builder;
        String str8;
        long j9;
        int i;
        String str9;
        Integer num;
        int i2;
        boolean z16;
        String str10;
        CharSequence charSequence;
        long j10;
        long j11;
        long j12;
        String string2;
        boolean z17;
        int i3;
        int i4;
        MessageObject messageObject3;
        boolean z18;
        int i5;
        String str11;
        int i6;
        int i7;
        int i8;
        boolean z19;
        String str12;
        int i9;
        boolean z20;
        boolean z21;
        int i10;
        String str13;
        int i11;
        NotificationsController notificationsController2;
        TLRPC.User user3;
        TLRPC.FileLocation fileLocation;
        TLRPC.FileLocation fileLocation2;
        NotificationCompat.Builder builder2;
        long[] jArr;
        boolean z22;
        int i12;
        long[] jArr2;
        long[] jArr3;
        long[] jArr4;
        int i13;
        long j13;
        boolean z23;
        int i14;
        int i15;
        String string3;
        Context context;
        int i16;
        int i17;
        TLRPC.ReplyMarkup replyMarkup;
        TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow;
        int i18;
        int i19;
        MessageObject messageObject4;
        long j14;
        int i20;
        long[] jArr5;
        Uri parse;
        ?? r1;
        int i21;
        String str14;
        int ringerMode;
        String string4;
        boolean z24;
        String string5;
        boolean z25;
        String string6;
        boolean z26;
        int i22;
        int i23;
        int i24;
        int elapsedRealtime;
        int i25;
        boolean z27;
        StringBuilder sb2;
        StringBuilder sb3;
        String formatPluralString;
        String formatPluralString2;
        String str15 = "file://";
        if (!getUserConfig().isClientActivated() || ((this.pushMessages.isEmpty() && this.storyPushMessages.isEmpty()) || !(SharedConfig.showNotificationsForAllAccounts || this.currentAccount == UserConfig.selectedAccount))) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            int i26 = 0;
            long j15 = 0;
            StoryNotification storyNotification = null;
            while (i26 < this.pushMessages.size()) {
                MessageObject messageObject5 = this.pushMessages.get(i26);
                String str16 = str15;
                long j16 = messageObject5.messageOwner.date;
                if (j15 < j16) {
                    j15 = j16;
                    storyNotification = messageObject5;
                }
                i26++;
                str15 = str16;
            }
            String str17 = str15;
            StoryNotification storyNotification2 = storyNotification;
            for (int i27 = 0; i27 < this.storyPushMessages.size(); i27++) {
                StoryNotification storyNotification3 = this.storyPushMessages.get(i27);
                long j17 = storyNotification3.date;
                if (j15 < j17 / 1000) {
                    j15 = j17 / 1000;
                    storyNotification2 = storyNotification3;
                }
            }
            if (storyNotification2 == null) {
                return;
            }
            if (storyNotification2 instanceof StoryNotification) {
                StoryNotification storyNotification4 = storyNotification2;
                TLRPC.TL_message tL_message = new TLRPC.TL_message();
                tL_message.date = (int) (System.currentTimeMillis() / 1000);
                int i28 = 0;
                boolean z28 = false;
                for (int i29 = 0; i29 < this.storyPushMessages.size(); i29++) {
                    z28 |= this.storyPushMessages.get(i29).hidden;
                    tL_message.date = Math.min(tL_message.date, (int) (this.storyPushMessages.get(i29).date / 1000));
                    i28 += this.storyPushMessages.get(i29).dateByIds.size();
                }
                TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                long j18 = storyNotification4.dialogId;
                tL_peerUser.user_id = j18;
                tL_message.dialog_id = j18;
                tL_message.peer_id = tL_peerUser;
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<Object> arrayList2 = new ArrayList<>();
                parseStoryPushes(arrayList, arrayList2);
                Bitmap loadMultipleAvatars = SharedConfig.getDevicePerformanceClass() >= 1 ? loadMultipleAvatars(arrayList2) : null;
                if (!z28 && this.storyPushMessages.size() < 2 && !arrayList.isEmpty()) {
                    formatPluralString = arrayList.get(0);
                    String str18 = formatPluralString;
                    if (!z28) {
                        formatPluralString2 = LocaleController.formatPluralString("StoryNotificationHidden", i28, new Object[0]);
                    } else if (arrayList.isEmpty()) {
                        formatPluralString2 = "";
                    } else if (arrayList.size() == 1) {
                        formatPluralString2 = i28 == 1 ? LocaleController.getString("StoryNotificationSingle") : LocaleController.formatPluralString("StoryNotification1", i28, arrayList.get(0));
                    } else if (arrayList.size() == 2) {
                        formatPluralString2 = LocaleController.formatString(R.string.StoryNotification2, arrayList.get(0), arrayList.get(1));
                    } else if (arrayList.size() == 3 && this.storyPushMessages.size() == 3) {
                        formatPluralString2 = LocaleController.formatString(R.string.StoryNotification3, cutLastName(arrayList.get(0)), cutLastName(arrayList.get(1)), cutLastName(arrayList.get(2)));
                    } else {
                        formatPluralString2 = LocaleController.formatPluralString("StoryNotification4", this.storyPushMessages.size() - 2, cutLastName(arrayList.get(0)), cutLastName(arrayList.get(1)));
                        tL_message.message = formatPluralString2;
                        MessageObject messageObject6 = new MessageObject(this.currentAccount, tL_message, tL_message.message, str18, str18, false, false, false, false);
                        messageObject6.isStoryPush = true;
                        messageObject = messageObject6;
                        bitmap = loadMultipleAvatars;
                    }
                    tL_message.message = formatPluralString2;
                    MessageObject messageObject62 = new MessageObject(this.currentAccount, tL_message, tL_message.message, str18, str18, false, false, false, false);
                    messageObject62.isStoryPush = true;
                    messageObject = messageObject62;
                    bitmap = loadMultipleAvatars;
                }
                formatPluralString = LocaleController.formatPluralString("Stories", i28, new Object[0]);
                String str182 = formatPluralString;
                if (!z28) {
                }
                tL_message.message = formatPluralString2;
                MessageObject messageObject622 = new MessageObject(this.currentAccount, tL_message, tL_message.message, str182, str182, false, false, false, false);
                messageObject622.isStoryPush = true;
                messageObject = messageObject622;
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
            boolean z29 = messageObject.isStoryPush;
            long fromChatId = messageObject.messageOwner.mentioned ? messageObject.getFromChatId() : dialogId;
            messageObject.getId();
            TLRPC.Peer peer = messageObject.messageOwner.peer_id;
            long j19 = peer.chat_id;
            if (j19 == 0) {
                j19 = peer.channel_id;
            }
            long j20 = j19;
            long j21 = peer.user_id;
            if (messageObject.isFromUser() && (j21 == 0 || j21 == getUserConfig().getClientUserId())) {
                j21 = messageObject.messageOwner.from_id.user_id;
            }
            if (messageObject.getDialogId() != UserObject.VERIFY || messageObject.getForwardedFromId() == null) {
                j = j21;
                j2 = j20;
            } else if (messageObject.getForwardedFromId().longValue() >= 0) {
                j = messageObject.getForwardedFromId().longValue();
                j2 = 0;
            } else {
                j2 = messageObject.getForwardedFromId().longValue();
                j = 0;
            }
            TLRPC.User user4 = getMessagesController().getUser(Long.valueOf(j));
            if (j2 != 0) {
                str = "currentAccount";
                TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(j2));
                if (chat2 == null && messageObject.isFcmMessage()) {
                    z2 = messageObject.localChannel;
                    chat = chat2;
                } else if (!ChatObject.isChannel(chat2) || chat2.megagroup) {
                    chat = chat2;
                } else {
                    chat = chat2;
                    z2 = true;
                }
                String str19 = str;
                TLRPC.Chat chat3 = chat;
                long j22 = j2;
                Bitmap bitmap2 = bitmap;
                int i31 = i30;
                notifyOverride = getNotifyOverride(notificationsSettings, fromChatId, topicId);
                if (notifyOverride != -1) {
                    Boolean valueOf = Boolean.valueOf(z2);
                    boolean z30 = messageObject.isReactionPush;
                    z3 = isGlobalNotificationsEnabled(dialogId, valueOf, z30, z30);
                    j3 = j22;
                } else if (notifyOverride != 2) {
                    j3 = j22;
                    z3 = true;
                } else {
                    j3 = j22;
                    z3 = false;
                }
                if (j3 == 0 && chat3 == null) {
                    user = user4;
                } else {
                    user = user4;
                }
                if (messageObject.isFcmMessage()) {
                    userName = messageObject.localName;
                    if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                        z4 = false;
                        z5 = !"samsung".equalsIgnoreCase(Build.MANUFACTURER);
                        if (DialogObject.isEncryptedDialog(dialogId)) {
                            if (z5) {
                                j4 = topicId;
                                notificationsController = this;
                                if (notificationsController.pushDialogs.size() <= 1) {
                                }
                            } else {
                                j4 = topicId;
                                notificationsController = this;
                            }
                            if (!z4) {
                                string = userName;
                                z6 = true;
                                if (!messageObject.isReactionPush && !messageObject.isStoryReactionPush) {
                                    str2 = userName;
                                    sharedPreferences = notificationsSettings;
                                    str3 = str19;
                                    String str20 = string;
                                    if (z5) {
                                        user2 = user;
                                        if (UserConfig.getActivatedAccountsCount() <= 1) {
                                            str4 = "";
                                        } else if (notificationsController.pushDialogs.size() == 1) {
                                            str4 = UserObject.getFirstName(getUserConfig().getCurrentUser());
                                        } else {
                                            str4 = UserObject.getFirstName(getUserConfig().getCurrentUser()) + "・";
                                        }
                                        j5 = j3;
                                        if (notificationsController.pushDialogs.size() == 1 && Build.VERSION.SDK_INT >= 23) {
                                            sharedPreferences2 = sharedPreferences;
                                            j6 = dialogId;
                                        }
                                        if (notificationsController.pushDialogs.size() == 1) {
                                            sb3 = new StringBuilder();
                                            sb3.append(str4);
                                            sharedPreferences2 = sharedPreferences;
                                            sb3.append(LocaleController.formatPluralString("NewMessages", notificationsController.total_unread_count, new Object[0]));
                                            j6 = dialogId;
                                        } else {
                                            sharedPreferences2 = sharedPreferences;
                                            sb3 = new StringBuilder();
                                            sb3.append(str4);
                                            j6 = dialogId;
                                            sb3.append(LocaleController.formatString(R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", notificationsController.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", notificationsController.pushDialogs.size(), new Object[0])));
                                        }
                                        str4 = sb3.toString();
                                    } else {
                                        j5 = j3;
                                        user2 = user;
                                        sharedPreferences2 = sharedPreferences;
                                        j6 = dialogId;
                                        str4 = "";
                                    }
                                    NotificationCompat.Builder builder3 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                                    if (notificationsController.pushMessages.size() > 1 || !z5) {
                                        z7 = z3;
                                        boolean[] zArr = new boolean[1];
                                        String str21 = str4;
                                        messageObject2 = messageObject;
                                        stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr, null);
                                        boolean isSilentMessage = notificationsController.isSilentMessage(messageObject2);
                                        if (stringForMessage == null) {
                                            return;
                                        }
                                        if (z6) {
                                            if (chat3 != null && z5) {
                                                sb = new StringBuilder();
                                                sb.append(" @ ");
                                                sb.append(str20);
                                            } else if (zArr[0]) {
                                                sb = new StringBuilder();
                                                sb.append(str20);
                                                sb.append(": ");
                                            } else {
                                                sb = new StringBuilder();
                                                sb.append(str20);
                                                sb.append(" ");
                                            }
                                            str5 = stringForMessage.replace(sb.toString(), "");
                                        } else {
                                            str5 = stringForMessage;
                                        }
                                        builder3.setContentText(str5);
                                        if (!z5) {
                                            str21 = str5;
                                        }
                                        builder3.setStyle(new NotificationCompat.BigTextStyle().bigText(str5));
                                        str6 = stringForMessage;
                                        str4 = str21;
                                        z8 = isSilentMessage;
                                    } else {
                                        builder3.setContentText(str4);
                                        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                                        inboxStyle.setBigContentTitle(str20);
                                        int min = Math.min(10, notificationsController.pushMessages.size());
                                        z7 = z3;
                                        boolean[] zArr2 = new boolean[1];
                                        MessageObject messageObject7 = messageObject;
                                        int i32 = 0;
                                        ?? r13 = 2;
                                        String str22 = null;
                                        while (i32 < min) {
                                            int i33 = min;
                                            MessageObject messageObject8 = notificationsController.pushMessages.get(i32);
                                            NotificationCompat.Builder builder4 = builder3;
                                            String str23 = str4;
                                            int i34 = i32;
                                            String stringForMessage2 = notificationsController.getStringForMessage(messageObject8, false, zArr2, null);
                                            if (stringForMessage2 != null) {
                                                if (messageObject8.isStoryPush) {
                                                    i25 = i31;
                                                } else {
                                                    i25 = i31;
                                                    if (messageObject8.messageOwner.date <= i25) {
                                                        z27 = r13;
                                                    }
                                                }
                                                boolean z31 = r13;
                                                if (r13 == 2) {
                                                    str22 = stringForMessage2;
                                                    z31 = notificationsController.isSilentMessage(messageObject8);
                                                }
                                                if (notificationsController.pushDialogs.size() == 1 && z6) {
                                                    if (chat3 != null) {
                                                        sb2 = new StringBuilder();
                                                        sb2.append(" @ ");
                                                        sb2.append(str20);
                                                    } else if (zArr2[0]) {
                                                        sb2 = new StringBuilder();
                                                        sb2.append(str20);
                                                        sb2.append(": ");
                                                    } else {
                                                        sb2 = new StringBuilder();
                                                        sb2.append(str20);
                                                        sb2.append(" ");
                                                    }
                                                    stringForMessage2 = stringForMessage2.replace(sb2.toString(), "");
                                                }
                                                inboxStyle.addLine(stringForMessage2);
                                                z27 = z31;
                                            } else {
                                                i25 = i31;
                                                z27 = r13;
                                            }
                                            i32 = i34 + 1;
                                            i31 = i25;
                                            min = i33;
                                            str4 = str23;
                                            builder3 = builder4;
                                            r13 = z27;
                                        }
                                        inboxStyle.setSummaryText(str4);
                                        builder3.setStyle(inboxStyle);
                                        str6 = str22;
                                        messageObject2 = messageObject7;
                                        z8 = r13;
                                    }
                                    if (z && z7 && !MediaController.getInstance().isRecordingAudio() && !z8) {
                                        z9 = false;
                                        if (z9 && j6 == fromChatId && chat3 != null) {
                                            StringBuilder sb4 = new StringBuilder();
                                            sb4.append(NotificationsSettingsFacade.PROPERTY_CUSTOM);
                                            j7 = j6;
                                            sb4.append(j7);
                                            sharedPreferences3 = sharedPreferences2;
                                            if (sharedPreferences3.getBoolean(sb4.toString(), false)) {
                                                i23 = sharedPreferences3.getInt("smart_max_count_" + j7, 2);
                                                StringBuilder sb5 = new StringBuilder();
                                                z10 = z9;
                                                sb5.append("smart_delay_");
                                                sb5.append(j7);
                                                i22 = sharedPreferences3.getInt(sb5.toString(), NotificationCenter.updateBotMenuButton);
                                            } else {
                                                z10 = z9;
                                                i22 = NotificationCenter.updateBotMenuButton;
                                                i23 = 2;
                                            }
                                            if (i23 != 0) {
                                                Point point = (Point) notificationsController.smartNotificationsDialogs.get(j7);
                                                if (point == null) {
                                                    z11 = z8;
                                                    notificationsController.smartNotificationsDialogs.put(j7, new Point(1, (int) (SystemClock.elapsedRealtime() / 1000)));
                                                } else {
                                                    z11 = z8;
                                                    if (point.y + i22 < SystemClock.elapsedRealtime() / 1000) {
                                                        elapsedRealtime = (int) (SystemClock.elapsedRealtime() / 1000);
                                                        i24 = 1;
                                                    } else {
                                                        int i35 = point.x;
                                                        if (i35 < i23) {
                                                            i24 = i35 + 1;
                                                            elapsedRealtime = (int) (SystemClock.elapsedRealtime() / 1000);
                                                        } else {
                                                            z12 = true;
                                                            if (z12) {
                                                                StringBuilder sb6 = new StringBuilder();
                                                                sb6.append("sound_enabled_");
                                                                z13 = z12;
                                                                j8 = j4;
                                                                sb6.append(getSharedPrefKey(j7, j8));
                                                                if (!sharedPreferences3.getBoolean(sb6.toString(), true)) {
                                                                    z14 = true;
                                                                    String path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                                    z15 = !ApplicationLoader.mainInterfacePaused;
                                                                    getSharedPrefKey(j7, j8);
                                                                    if (notificationsController.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j7, j8, false)) {
                                                                        int property = notificationsController.dialogsNotificationsFacade.getProperty("vibrate_", j7, j8, 0);
                                                                        int property2 = notificationsController.dialogsNotificationsFacade.getProperty("priority_", j7, j8, 3);
                                                                        str7 = str6;
                                                                        builder = builder3;
                                                                        long property3 = notificationsController.dialogsNotificationsFacade.getProperty("sound_document_id_", j7, j8, 0L);
                                                                        str8 = str4;
                                                                        if (property3 != 0) {
                                                                            str9 = getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                                                                            z26 = true;
                                                                        } else {
                                                                            str9 = notificationsController.dialogsNotificationsFacade.getPropertyString("sound_path_", j7, j8, null);
                                                                            z26 = false;
                                                                        }
                                                                        int property4 = notificationsController.dialogsNotificationsFacade.getProperty("color_", j7, j8, 0);
                                                                        if (property4 != 0) {
                                                                            num = Integer.valueOf(property4);
                                                                            z16 = z26;
                                                                            j9 = j8;
                                                                            i2 = property;
                                                                            i = property2;
                                                                        } else {
                                                                            z16 = z26;
                                                                            j9 = j8;
                                                                            i2 = property;
                                                                            i = property2;
                                                                            num = null;
                                                                        }
                                                                    } else {
                                                                        str7 = str6;
                                                                        builder = builder3;
                                                                        str8 = str4;
                                                                        j9 = j8;
                                                                        i = 3;
                                                                        str9 = null;
                                                                        num = null;
                                                                        i2 = 0;
                                                                        z16 = false;
                                                                    }
                                                                    if (!messageObject2.isReactionPush || messageObject2.isStoryReactionPush) {
                                                                        str10 = str7;
                                                                        charSequence = str20;
                                                                        MessageObject messageObject9 = messageObject2;
                                                                        j10 = j;
                                                                        j11 = j7;
                                                                        j12 = sharedPreferences3.getLong("ReactionSoundDocId", 0L);
                                                                        if (j12 != 0) {
                                                                            string2 = getMediaDataController().ringtoneDataStore.getSoundPath(j12);
                                                                            z17 = true;
                                                                        } else {
                                                                            string2 = sharedPreferences3.getString("ReactionSoundPath", path);
                                                                            z17 = false;
                                                                        }
                                                                        i3 = sharedPreferences3.getInt("vibrate_react", 0);
                                                                        int i36 = sharedPreferences3.getInt("priority_react", 1);
                                                                        i4 = sharedPreferences3.getInt("ReactionsLed", -16776961);
                                                                        String str24 = string2;
                                                                        messageObject3 = messageObject9;
                                                                        z18 = z17;
                                                                        i5 = i36;
                                                                        if (messageObject3.isStoryReactionPush) {
                                                                            str11 = str24;
                                                                            i6 = 4;
                                                                            i7 = 5;
                                                                        } else {
                                                                            str11 = str24;
                                                                            i6 = 4;
                                                                            i7 = 4;
                                                                        }
                                                                    } else {
                                                                        MessageObject messageObject10 = messageObject2;
                                                                        if (j5 != 0) {
                                                                            if (z2) {
                                                                                str10 = str7;
                                                                                charSequence = str20;
                                                                                long j23 = sharedPreferences3.getLong("ChannelSoundDocId", 0L);
                                                                                if (j23 != 0) {
                                                                                    string6 = getMediaDataController().ringtoneDataStore.getSoundPath(j23);
                                                                                    z25 = true;
                                                                                } else {
                                                                                    string6 = sharedPreferences3.getString("ChannelSoundPath", path);
                                                                                    z25 = false;
                                                                                }
                                                                                int i37 = sharedPreferences3.getInt("vibrate_channel", 0);
                                                                                i5 = sharedPreferences3.getInt("priority_channel", 1);
                                                                                i4 = sharedPreferences3.getInt("ChannelLed", -16776961);
                                                                                j11 = j7;
                                                                                messageObject3 = messageObject10;
                                                                                i6 = 4;
                                                                                i3 = i37;
                                                                                str11 = string6;
                                                                                i7 = 2;
                                                                            } else {
                                                                                str10 = str7;
                                                                                charSequence = str20;
                                                                                long j24 = sharedPreferences3.getLong("GroupSoundDocId", 0L);
                                                                                if (j24 != 0) {
                                                                                    string5 = getMediaDataController().ringtoneDataStore.getSoundPath(j24);
                                                                                    z25 = true;
                                                                                } else {
                                                                                    string5 = sharedPreferences3.getString("GroupSoundPath", path);
                                                                                    z25 = false;
                                                                                }
                                                                                int i38 = sharedPreferences3.getInt("vibrate_group", 0);
                                                                                i5 = sharedPreferences3.getInt("priority_group", 1);
                                                                                i4 = sharedPreferences3.getInt("GroupLed", -16776961);
                                                                                j11 = j7;
                                                                                messageObject3 = messageObject10;
                                                                                i6 = 4;
                                                                                i7 = 0;
                                                                                i3 = i38;
                                                                                str11 = string5;
                                                                            }
                                                                            z18 = z25;
                                                                            j10 = j;
                                                                        } else {
                                                                            str10 = str7;
                                                                            charSequence = str20;
                                                                            j10 = j;
                                                                            if (j10 != 0) {
                                                                                long j25 = sharedPreferences3.getLong(z29 ? "StoriesSoundDocId" : "GlobalSoundDocId", 0L);
                                                                                if (j25 != 0) {
                                                                                    string4 = getMediaDataController().ringtoneDataStore.getSoundPath(j25);
                                                                                    z24 = true;
                                                                                } else {
                                                                                    string4 = sharedPreferences3.getString(z29 ? "StoriesSoundPath" : "GlobalSoundPath", path);
                                                                                    z24 = false;
                                                                                }
                                                                                int i39 = sharedPreferences3.getInt("vibrate_messages", 0);
                                                                                String str25 = string4;
                                                                                int i40 = sharedPreferences3.getInt("priority_messages", 1);
                                                                                z18 = z24;
                                                                                int i41 = sharedPreferences3.getInt("MessagesLed", -16776961);
                                                                                j11 = j7;
                                                                                i3 = i39;
                                                                                i5 = i40;
                                                                                messageObject3 = messageObject10;
                                                                                i6 = 4;
                                                                                i7 = z29 ? 3 : 1;
                                                                                i4 = i41;
                                                                                str11 = str25;
                                                                            } else {
                                                                                j11 = j7;
                                                                                messageObject3 = messageObject10;
                                                                                str11 = null;
                                                                                i5 = 0;
                                                                                i3 = 0;
                                                                                i6 = 4;
                                                                                i4 = -16776961;
                                                                                i7 = 1;
                                                                                z18 = false;
                                                                            }
                                                                        }
                                                                    }
                                                                    if (i3 == i6) {
                                                                        z19 = true;
                                                                        i8 = 0;
                                                                    } else {
                                                                        i8 = i3;
                                                                        z19 = false;
                                                                    }
                                                                    if (!TextUtils.isEmpty(str9) || TextUtils.equals(str11, str9)) {
                                                                        str12 = str11;
                                                                        z16 = z18;
                                                                        i9 = 3;
                                                                        z20 = true;
                                                                    } else {
                                                                        str12 = str9;
                                                                        i9 = 3;
                                                                        z20 = false;
                                                                    }
                                                                    if (i != i9 || i5 == i) {
                                                                        i = i5;
                                                                    } else {
                                                                        z20 = false;
                                                                    }
                                                                    if (num != null && num.intValue() != i4) {
                                                                        i4 = num.intValue();
                                                                        z20 = false;
                                                                    }
                                                                    if (i2 != 0 || i2 == 4 || i2 == i8) {
                                                                        z21 = z20;
                                                                        i10 = i8;
                                                                    } else {
                                                                        i10 = i2;
                                                                        z21 = false;
                                                                    }
                                                                    if (z15) {
                                                                        if (!sharedPreferences3.getBoolean("EnableInAppSounds", true)) {
                                                                            str12 = null;
                                                                        }
                                                                        if (!sharedPreferences3.getBoolean("EnableInAppVibrate", true)) {
                                                                            i10 = 2;
                                                                        }
                                                                        str13 = str12;
                                                                        i = sharedPreferences3.getBoolean("EnableInAppPopup", true) ? 2 : 0;
                                                                    } else {
                                                                        str13 = str12;
                                                                    }
                                                                    if (z19 && i10 != 2) {
                                                                        try {
                                                                            ringerMode = audioManager.getRingerMode();
                                                                            if (ringerMode != 0 && ringerMode != 1) {
                                                                                i10 = 2;
                                                                            }
                                                                        } catch (Exception e) {
                                                                            FileLog.e(e);
                                                                        }
                                                                    }
                                                                    if (z14) {
                                                                        i = 0;
                                                                        str13 = null;
                                                                        i10 = 0;
                                                                        i11 = 0;
                                                                    } else {
                                                                        i11 = i4;
                                                                    }
                                                                    Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                                                    StringBuilder sb7 = new StringBuilder();
                                                                    sb7.append("com.tmessages.openchat");
                                                                    boolean z32 = z21;
                                                                    sb7.append(Math.random());
                                                                    sb7.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                                    intent.setAction(sb7.toString());
                                                                    intent.setFlags(ConnectionsManager.FileTypeFile);
                                                                    if (messageObject3.isStoryReactionPush) {
                                                                        intent.putExtra("storyId", Math.abs(messageObject3.getId()));
                                                                        notificationsController2 = this;
                                                                    } else {
                                                                        notificationsController2 = this;
                                                                        if (messageObject3.isStoryPush) {
                                                                            long[] jArr6 = new long[notificationsController2.storyPushMessages.size()];
                                                                            for (int i42 = 0; i42 < notificationsController2.storyPushMessages.size(); i42++) {
                                                                                jArr6[i42] = notificationsController2.storyPushMessages.get(i42).dialogId;
                                                                            }
                                                                            intent.putExtra("storyDialogIds", jArr6);
                                                                        } else {
                                                                            if (DialogObject.isEncryptedDialog(j11)) {
                                                                                user3 = user2;
                                                                                if (notificationsController2.pushDialogs.size() == 1 && j11 != globalSecretChatId) {
                                                                                    intent.putExtra("encId", DialogObject.getEncryptedChatId(j11));
                                                                                }
                                                                            } else {
                                                                                if (notificationsController2.pushDialogs.size() == 1) {
                                                                                    if (j5 != 0) {
                                                                                        intent.putExtra("chatId", j5);
                                                                                    } else if (j10 != 0) {
                                                                                        intent.putExtra("userId", j10);
                                                                                    }
                                                                                }
                                                                                if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter && notificationsController2.pushDialogs.size() == 1 && Build.VERSION.SDK_INT < 28) {
                                                                                    if (chat3 != null) {
                                                                                        TLRPC.ChatPhoto chatPhoto = chat3.photo;
                                                                                        if (chatPhoto != null && (fileLocation2 = chatPhoto.photo_small) != null && fileLocation2.volume_id != 0 && fileLocation2.local_id != 0) {
                                                                                            fileLocation = fileLocation2;
                                                                                            user3 = user2;
                                                                                            String str26 = str3;
                                                                                            intent.putExtra(str26, notificationsController2.currentAccount);
                                                                                            int i43 = i10;
                                                                                            builder2 = builder;
                                                                                            NotificationCompat.Builder showWhen = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                                                                            SharedPreferences sharedPreferences4 = sharedPreferences3;
                                                                                            showWhen.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                                            builder2.setCategory("msg");
                                                                                            if (chat3 == null && user3 != null && (str14 = user3.phone) != null && str14.length() > 0) {
                                                                                                builder2.addPerson("tel:+" + user3.phone);
                                                                                            }
                                                                                            Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                                                                            intent2.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                                            intent2.putExtra(str26, notificationsController2.currentAccount);
                                                                                            if (messageObject3.isStoryPush) {
                                                                                                intent2.putExtra("story", true);
                                                                                            }
                                                                                            if (messageObject3.isStoryReactionPush) {
                                                                                                i21 = 1;
                                                                                            } else {
                                                                                                i21 = 1;
                                                                                                intent2.putExtra("storyReaction", true);
                                                                                            }
                                                                                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent2, 167772160));
                                                                                            if (bitmap2 == null) {
                                                                                                builder2.setLargeIcon(bitmap2);
                                                                                            } else if (fileLocation != null) {
                                                                                                jArr = null;
                                                                                                BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation, null, "50_50");
                                                                                                if (imageFromMemory != null) {
                                                                                                    builder2.setLargeIcon(imageFromMemory.getBitmap());
                                                                                                } else {
                                                                                                    try {
                                                                                                        File pathToAttach = getFileLoader().getPathToAttach(fileLocation, true);
                                                                                                        if (pathToAttach.exists()) {
                                                                                                            float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                                                                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                                                                                            options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                                                                                            Bitmap decodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options);
                                                                                                            if (decodeFile != null) {
                                                                                                                builder2.setLargeIcon(decodeFile);
                                                                                                            }
                                                                                                        }
                                                                                                    } catch (Throwable unused) {
                                                                                                    }
                                                                                                }
                                                                                                boolean z33 = z11;
                                                                                                if (z || z33) {
                                                                                                    builder2.setPriority(-1);
                                                                                                } else if (i == 0) {
                                                                                                    builder2.setPriority(0);
                                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                                        z22 = true;
                                                                                                        i12 = 3;
                                                                                                    }
                                                                                                    z22 = true;
                                                                                                    i12 = 0;
                                                                                                } else if (i == 1 || i == 2) {
                                                                                                    builder2.setPriority(1);
                                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                                        z22 = true;
                                                                                                        i12 = 4;
                                                                                                    }
                                                                                                    z22 = true;
                                                                                                    i12 = 0;
                                                                                                } else {
                                                                                                    if (i == 4) {
                                                                                                        builder2.setPriority(-2);
                                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                                            z22 = true;
                                                                                                            i12 = 1;
                                                                                                        }
                                                                                                    } else if (i == 5) {
                                                                                                        builder2.setPriority(-1);
                                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                                            z22 = true;
                                                                                                            i12 = 2;
                                                                                                        }
                                                                                                    }
                                                                                                    z22 = true;
                                                                                                    i12 = 0;
                                                                                                }
                                                                                                if (z33 == z22 || z14) {
                                                                                                    long[] jArr7 = {0, 0};
                                                                                                    builder2.setVibrate(jArr7);
                                                                                                    jArr2 = jArr7;
                                                                                                    jArr3 = jArr;
                                                                                                } else {
                                                                                                    if (!z15 || (sharedPreferences4.getBoolean("EnableInAppPreview", z22) && str10 != null)) {
                                                                                                        builder2.setTicker(str10.length() > 100 ? str10.substring(0, 100).replace('\n', ' ').trim() + "..." : str10);
                                                                                                    }
                                                                                                    if (str13 != null && !str13.equalsIgnoreCase("NoSound")) {
                                                                                                        int i44 = Build.VERSION.SDK_INT;
                                                                                                        if (i44 >= 26) {
                                                                                                            if (!str13.equalsIgnoreCase("Default") && !str13.equals(path)) {
                                                                                                                if (z16) {
                                                                                                                    ?? uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str13));
                                                                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                                                    jArr5 = uriForFile;
                                                                                                                    if (i11 != 0) {
                                                                                                                        builder2.setLights(i11, MediaDataController.MAX_STYLE_RUNS_COUNT, MediaDataController.MAX_STYLE_RUNS_COUNT);
                                                                                                                    }
                                                                                                                    if (i43 != 2) {
                                                                                                                        jArr2 = new long[]{0, 0};
                                                                                                                        builder2.setVibrate(jArr2);
                                                                                                                    } else {
                                                                                                                        if (i43 != 1) {
                                                                                                                            if (i43 == 0 || i43 == 4) {
                                                                                                                                builder2.setDefaults(2);
                                                                                                                                jArr4 = jArr5;
                                                                                                                                jArr = new long[0];
                                                                                                                            } else if (i43 == 3) {
                                                                                                                                long[] jArr8 = {0, 1000};
                                                                                                                                builder2.setVibrate(jArr8);
                                                                                                                                jArr3 = jArr5;
                                                                                                                                jArr2 = jArr8;
                                                                                                                            } else {
                                                                                                                                jArr4 = jArr5;
                                                                                                                            }
                                                                                                                            if (!AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject3.getDialogId() != 777000 || (replyMarkup = messageObject3.messageOwner.reply_markup) == null) {
                                                                                                                                i13 = i12;
                                                                                                                                j13 = j11;
                                                                                                                                z23 = false;
                                                                                                                            } else {
                                                                                                                                ArrayList<TLRPC.TL_keyboardButtonRow> arrayList3 = replyMarkup.rows;
                                                                                                                                int size = arrayList3.size();
                                                                                                                                int i45 = 0;
                                                                                                                                z23 = false;
                                                                                                                                while (i45 < size) {
                                                                                                                                    TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow2 = arrayList3.get(i45);
                                                                                                                                    int size2 = tL_keyboardButtonRow2.buttons.size();
                                                                                                                                    int i46 = 0;
                                                                                                                                    while (i46 < size2) {
                                                                                                                                        ArrayList<TLRPC.TL_keyboardButtonRow> arrayList4 = arrayList3;
                                                                                                                                        TLRPC.KeyboardButton keyboardButton = tL_keyboardButtonRow2.buttons.get(i46);
                                                                                                                                        int i47 = size;
                                                                                                                                        if (keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) {
                                                                                                                                            tL_keyboardButtonRow = tL_keyboardButtonRow2;
                                                                                                                                            Intent intent3 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationCallbackReceiver.class);
                                                                                                                                            intent3.putExtra(str26, notificationsController2.currentAccount);
                                                                                                                                            i18 = size2;
                                                                                                                                            j14 = j11;
                                                                                                                                            intent3.putExtra("did", j14);
                                                                                                                                            byte[] bArr = keyboardButton.data;
                                                                                                                                            if (bArr != null) {
                                                                                                                                                i19 = i12;
                                                                                                                                                intent3.putExtra("data", bArr);
                                                                                                                                            } else {
                                                                                                                                                i19 = i12;
                                                                                                                                            }
                                                                                                                                            intent3.putExtra("mid", messageObject3.getId());
                                                                                                                                            String str27 = keyboardButton.text;
                                                                                                                                            Context context2 = ApplicationLoader.applicationContext;
                                                                                                                                            int i48 = notificationsController2.lastButtonId;
                                                                                                                                            messageObject4 = messageObject3;
                                                                                                                                            notificationsController2.lastButtonId = i48 + 1;
                                                                                                                                            builder2.addAction(0, str27, PendingIntent.getBroadcast(context2, i48, intent3, 167772160));
                                                                                                                                            i20 = 1;
                                                                                                                                            z23 = true;
                                                                                                                                        } else {
                                                                                                                                            tL_keyboardButtonRow = tL_keyboardButtonRow2;
                                                                                                                                            i18 = size2;
                                                                                                                                            i19 = i12;
                                                                                                                                            messageObject4 = messageObject3;
                                                                                                                                            j14 = j11;
                                                                                                                                            i20 = 1;
                                                                                                                                        }
                                                                                                                                        i46 += i20;
                                                                                                                                        size = i47;
                                                                                                                                        j11 = j14;
                                                                                                                                        arrayList3 = arrayList4;
                                                                                                                                        tL_keyboardButtonRow2 = tL_keyboardButtonRow;
                                                                                                                                        size2 = i18;
                                                                                                                                        i12 = i19;
                                                                                                                                        messageObject3 = messageObject4;
                                                                                                                                    }
                                                                                                                                    i45++;
                                                                                                                                    arrayList3 = arrayList3;
                                                                                                                                    i12 = i12;
                                                                                                                                }
                                                                                                                                i13 = i12;
                                                                                                                                j13 = j11;
                                                                                                                            }
                                                                                                                            if (!z23 && (i14 = Build.VERSION.SDK_INT) < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                                                                                                Intent intent4 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                                                                                                                intent4.putExtra(str26, notificationsController2.currentAccount);
                                                                                                                                if (i14 > 19) {
                                                                                                                                    i15 = R.drawable.ic_ab_reply2;
                                                                                                                                    string3 = LocaleController.getString(R.string.Reply);
                                                                                                                                    context = ApplicationLoader.applicationContext;
                                                                                                                                    i16 = 167772160;
                                                                                                                                    i17 = 2;
                                                                                                                                } else {
                                                                                                                                    i15 = R.drawable.ic_ab_reply;
                                                                                                                                    string3 = LocaleController.getString(R.string.Reply);
                                                                                                                                    context = ApplicationLoader.applicationContext;
                                                                                                                                    i16 = 167772160;
                                                                                                                                    i17 = 2;
                                                                                                                                }
                                                                                                                                builder2.addAction(i15, string3, PendingIntent.getBroadcast(context, i17, intent4, i16));
                                                                                                                            }
                                                                                                                            showExtraNotifications(builder2, str8, j13, j9, str2, jArr, i11, jArr4, i13, z32, z15, z14, i7);
                                                                                                                            scheduleNotificationRepeat();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        jArr2 = new long[]{0, 100, 0, 100};
                                                                                                                        builder2.setVibrate(jArr2);
                                                                                                                    }
                                                                                                                    jArr3 = jArr5;
                                                                                                                } else {
                                                                                                                    r1 = Uri.parse(str13);
                                                                                                                    jArr5 = r1;
                                                                                                                    if (i11 != 0) {
                                                                                                                    }
                                                                                                                    if (i43 != 2) {
                                                                                                                    }
                                                                                                                    jArr3 = jArr5;
                                                                                                                }
                                                                                                            }
                                                                                                            r1 = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                                            jArr5 = r1;
                                                                                                            if (i11 != 0) {
                                                                                                            }
                                                                                                            if (i43 != 2) {
                                                                                                            }
                                                                                                            jArr3 = jArr5;
                                                                                                        } else {
                                                                                                            if (str13.equals(path)) {
                                                                                                                parse = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                                            } else {
                                                                                                                if (i44 >= 24 && str13.startsWith(str17) && !AndroidUtilities.isInternalUri(Uri.parse(str13))) {
                                                                                                                    try {
                                                                                                                        Uri uriForFile2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str13.replace(str17, "")));
                                                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile2, 1);
                                                                                                                        builder2.setSound(uriForFile2, 5);
                                                                                                                    } catch (Exception unused2) {
                                                                                                                    }
                                                                                                                }
                                                                                                                parse = Uri.parse(str13);
                                                                                                            }
                                                                                                            builder2.setSound(parse, 5);
                                                                                                        }
                                                                                                    }
                                                                                                    jArr5 = jArr;
                                                                                                    if (i11 != 0) {
                                                                                                    }
                                                                                                    if (i43 != 2) {
                                                                                                    }
                                                                                                    jArr3 = jArr5;
                                                                                                }
                                                                                                jArr = jArr2;
                                                                                                jArr4 = jArr3;
                                                                                                if (AndroidUtilities.needShowPasscode()) {
                                                                                                }
                                                                                                i13 = i12;
                                                                                                j13 = j11;
                                                                                                z23 = false;
                                                                                                if (!z23) {
                                                                                                    Intent intent42 = new Intent(ApplicationLoader.applicationContext, (Class<?>) PopupReplyReceiver.class);
                                                                                                    intent42.putExtra(str26, notificationsController2.currentAccount);
                                                                                                    if (i14 > 19) {
                                                                                                    }
                                                                                                    builder2.addAction(i15, string3, PendingIntent.getBroadcast(context, i17, intent42, i16));
                                                                                                }
                                                                                                showExtraNotifications(builder2, str8, j13, j9, str2, jArr, i11, jArr4, i13, z32, z15, z14, i7);
                                                                                                scheduleNotificationRepeat();
                                                                                                return;
                                                                                            }
                                                                                            jArr = null;
                                                                                            boolean z332 = z11;
                                                                                            if (z) {
                                                                                            }
                                                                                            builder2.setPriority(-1);
                                                                                        }
                                                                                    } else if (user2 != null) {
                                                                                        user3 = user2;
                                                                                        TLRPC.UserProfilePhoto userProfilePhoto = user3.photo;
                                                                                        if (userProfilePhoto != null && (fileLocation = userProfilePhoto.photo_small) != null && fileLocation.volume_id != 0 && fileLocation.local_id != 0) {
                                                                                            String str262 = str3;
                                                                                            intent.putExtra(str262, notificationsController2.currentAccount);
                                                                                            int i432 = i10;
                                                                                            builder2 = builder;
                                                                                            NotificationCompat.Builder showWhen2 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                                                                            SharedPreferences sharedPreferences42 = sharedPreferences3;
                                                                                            showWhen2.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                                            builder2.setCategory("msg");
                                                                                            if (chat3 == null) {
                                                                                                builder2.addPerson("tel:+" + user3.phone);
                                                                                            }
                                                                                            Intent intent22 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                                                                            intent22.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                                            intent22.putExtra(str262, notificationsController2.currentAccount);
                                                                                            if (messageObject3.isStoryPush) {
                                                                                            }
                                                                                            if (messageObject3.isStoryReactionPush) {
                                                                                            }
                                                                                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent22, 167772160));
                                                                                            if (bitmap2 == null) {
                                                                                            }
                                                                                            jArr = null;
                                                                                            boolean z3322 = z11;
                                                                                            if (z) {
                                                                                            }
                                                                                            builder2.setPriority(-1);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            fileLocation = null;
                                                                            String str2622 = str3;
                                                                            intent.putExtra(str2622, notificationsController2.currentAccount);
                                                                            int i4322 = i10;
                                                                            builder2 = builder;
                                                                            NotificationCompat.Builder showWhen22 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                                                            SharedPreferences sharedPreferences422 = sharedPreferences3;
                                                                            showWhen22.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                            builder2.setCategory("msg");
                                                                            if (chat3 == null) {
                                                                            }
                                                                            Intent intent222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                                                            intent222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                            intent222.putExtra(str2622, notificationsController2.currentAccount);
                                                                            if (messageObject3.isStoryPush) {
                                                                            }
                                                                            if (messageObject3.isStoryReactionPush) {
                                                                            }
                                                                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent222, 167772160));
                                                                            if (bitmap2 == null) {
                                                                            }
                                                                            jArr = null;
                                                                            boolean z33222 = z11;
                                                                            if (z) {
                                                                            }
                                                                            builder2.setPriority(-1);
                                                                        }
                                                                    }
                                                                    user3 = user2;
                                                                    fileLocation = null;
                                                                    String str26222 = str3;
                                                                    intent.putExtra(str26222, notificationsController2.currentAccount);
                                                                    int i43222 = i10;
                                                                    builder2 = builder;
                                                                    NotificationCompat.Builder showWhen222 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                                                    SharedPreferences sharedPreferences4222 = sharedPreferences3;
                                                                    showWhen222.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                    builder2.setCategory("msg");
                                                                    if (chat3 == null) {
                                                                    }
                                                                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                                                    intent2222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                    intent2222.putExtra(str26222, notificationsController2.currentAccount);
                                                                    if (messageObject3.isStoryPush) {
                                                                    }
                                                                    if (messageObject3.isStoryReactionPush) {
                                                                    }
                                                                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent2222, 167772160));
                                                                    if (bitmap2 == null) {
                                                                    }
                                                                    jArr = null;
                                                                    boolean z332222 = z11;
                                                                    if (z) {
                                                                    }
                                                                    builder2.setPriority(-1);
                                                                }
                                                            } else {
                                                                z13 = z12;
                                                                j8 = j4;
                                                            }
                                                            z14 = z13;
                                                            String path2 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                            z15 = !ApplicationLoader.mainInterfacePaused;
                                                            getSharedPrefKey(j7, j8);
                                                            if (notificationsController.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j7, j8, false)) {
                                                            }
                                                            if (messageObject2.isReactionPush) {
                                                            }
                                                            str10 = str7;
                                                            charSequence = str20;
                                                            MessageObject messageObject92 = messageObject2;
                                                            j10 = j;
                                                            j11 = j7;
                                                            j12 = sharedPreferences3.getLong("ReactionSoundDocId", 0L);
                                                            if (j12 != 0) {
                                                            }
                                                            i3 = sharedPreferences3.getInt("vibrate_react", 0);
                                                            int i362 = sharedPreferences3.getInt("priority_react", 1);
                                                            i4 = sharedPreferences3.getInt("ReactionsLed", -16776961);
                                                            String str242 = string2;
                                                            messageObject3 = messageObject92;
                                                            z18 = z17;
                                                            i5 = i362;
                                                            if (messageObject3.isStoryReactionPush) {
                                                            }
                                                            if (i3 == i6) {
                                                            }
                                                            if (TextUtils.isEmpty(str9)) {
                                                            }
                                                            str12 = str11;
                                                            z16 = z18;
                                                            i9 = 3;
                                                            z20 = true;
                                                            if (i != i9) {
                                                            }
                                                            i = i5;
                                                            if (num != null) {
                                                                i4 = num.intValue();
                                                                z20 = false;
                                                            }
                                                            if (i2 != 0) {
                                                            }
                                                            z21 = z20;
                                                            i10 = i8;
                                                            if (z15) {
                                                            }
                                                            if (z19) {
                                                                ringerMode = audioManager.getRingerMode();
                                                                if (ringerMode != 0) {
                                                                    i10 = 2;
                                                                }
                                                            }
                                                            if (z14) {
                                                            }
                                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                                            StringBuilder sb72 = new StringBuilder();
                                                            sb72.append("com.tmessages.openchat");
                                                            boolean z322 = z21;
                                                            sb72.append(Math.random());
                                                            sb72.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                            intent5.setAction(sb72.toString());
                                                            intent5.setFlags(ConnectionsManager.FileTypeFile);
                                                            if (messageObject3.isStoryReactionPush) {
                                                            }
                                                            user3 = user2;
                                                            fileLocation = null;
                                                            String str262222 = str3;
                                                            intent5.putExtra(str262222, notificationsController2.currentAccount);
                                                            int i432222 = i10;
                                                            builder2 = builder;
                                                            NotificationCompat.Builder showWhen2222 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                                            SharedPreferences sharedPreferences42222 = sharedPreferences3;
                                                            showWhen2222.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                            builder2.setCategory("msg");
                                                            if (chat3 == null) {
                                                            }
                                                            Intent intent22222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                                            intent22222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                            intent22222.putExtra(str262222, notificationsController2.currentAccount);
                                                            if (messageObject3.isStoryPush) {
                                                            }
                                                            if (messageObject3.isStoryReactionPush) {
                                                            }
                                                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent22222, 167772160));
                                                            if (bitmap2 == null) {
                                                            }
                                                            jArr = null;
                                                            boolean z3322222 = z11;
                                                            if (z) {
                                                            }
                                                            builder2.setPriority(-1);
                                                        }
                                                    }
                                                    point.set(i24, elapsedRealtime);
                                                }
                                                z12 = z10;
                                                if (z12) {
                                                }
                                                z14 = z13;
                                                String path22 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                z15 = !ApplicationLoader.mainInterfacePaused;
                                                getSharedPrefKey(j7, j8);
                                                if (notificationsController.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j7, j8, false)) {
                                                }
                                                if (messageObject2.isReactionPush) {
                                                }
                                                str10 = str7;
                                                charSequence = str20;
                                                MessageObject messageObject922 = messageObject2;
                                                j10 = j;
                                                j11 = j7;
                                                j12 = sharedPreferences3.getLong("ReactionSoundDocId", 0L);
                                                if (j12 != 0) {
                                                }
                                                i3 = sharedPreferences3.getInt("vibrate_react", 0);
                                                int i3622 = sharedPreferences3.getInt("priority_react", 1);
                                                i4 = sharedPreferences3.getInt("ReactionsLed", -16776961);
                                                String str2422 = string2;
                                                messageObject3 = messageObject922;
                                                z18 = z17;
                                                i5 = i3622;
                                                if (messageObject3.isStoryReactionPush) {
                                                }
                                                if (i3 == i6) {
                                                }
                                                if (TextUtils.isEmpty(str9)) {
                                                }
                                                str12 = str11;
                                                z16 = z18;
                                                i9 = 3;
                                                z20 = true;
                                                if (i != i9) {
                                                }
                                                i = i5;
                                                if (num != null) {
                                                }
                                                if (i2 != 0) {
                                                }
                                                z21 = z20;
                                                i10 = i8;
                                                if (z15) {
                                                }
                                                if (z19) {
                                                }
                                                if (z14) {
                                                }
                                                Intent intent52 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                                StringBuilder sb722 = new StringBuilder();
                                                sb722.append("com.tmessages.openchat");
                                                boolean z3222 = z21;
                                                sb722.append(Math.random());
                                                sb722.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                intent52.setAction(sb722.toString());
                                                intent52.setFlags(ConnectionsManager.FileTypeFile);
                                                if (messageObject3.isStoryReactionPush) {
                                                }
                                                user3 = user2;
                                                fileLocation = null;
                                                String str2622222 = str3;
                                                intent52.putExtra(str2622222, notificationsController2.currentAccount);
                                                int i4322222 = i10;
                                                builder2 = builder;
                                                NotificationCompat.Builder showWhen22222 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                                SharedPreferences sharedPreferences422222 = sharedPreferences3;
                                                showWhen22222.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                builder2.setCategory("msg");
                                                if (chat3 == null) {
                                                }
                                                Intent intent222222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                                intent222222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                intent222222.putExtra(str2622222, notificationsController2.currentAccount);
                                                if (messageObject3.isStoryPush) {
                                                }
                                                if (messageObject3.isStoryReactionPush) {
                                                }
                                                builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent222222, 167772160));
                                                if (bitmap2 == null) {
                                                }
                                                jArr = null;
                                                boolean z33222222 = z11;
                                                if (z) {
                                                }
                                                builder2.setPriority(-1);
                                            }
                                        } else {
                                            z10 = z9;
                                            sharedPreferences3 = sharedPreferences2;
                                            j7 = j6;
                                        }
                                        z11 = z8;
                                        z12 = z10;
                                        if (z12) {
                                        }
                                        z14 = z13;
                                        String path222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                        z15 = !ApplicationLoader.mainInterfacePaused;
                                        getSharedPrefKey(j7, j8);
                                        if (notificationsController.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j7, j8, false)) {
                                        }
                                        if (messageObject2.isReactionPush) {
                                        }
                                        str10 = str7;
                                        charSequence = str20;
                                        MessageObject messageObject9222 = messageObject2;
                                        j10 = j;
                                        j11 = j7;
                                        j12 = sharedPreferences3.getLong("ReactionSoundDocId", 0L);
                                        if (j12 != 0) {
                                        }
                                        i3 = sharedPreferences3.getInt("vibrate_react", 0);
                                        int i36222 = sharedPreferences3.getInt("priority_react", 1);
                                        i4 = sharedPreferences3.getInt("ReactionsLed", -16776961);
                                        String str24222 = string2;
                                        messageObject3 = messageObject9222;
                                        z18 = z17;
                                        i5 = i36222;
                                        if (messageObject3.isStoryReactionPush) {
                                        }
                                        if (i3 == i6) {
                                        }
                                        if (TextUtils.isEmpty(str9)) {
                                        }
                                        str12 = str11;
                                        z16 = z18;
                                        i9 = 3;
                                        z20 = true;
                                        if (i != i9) {
                                        }
                                        i = i5;
                                        if (num != null) {
                                        }
                                        if (i2 != 0) {
                                        }
                                        z21 = z20;
                                        i10 = i8;
                                        if (z15) {
                                        }
                                        if (z19) {
                                        }
                                        if (z14) {
                                        }
                                        Intent intent522 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                        StringBuilder sb7222 = new StringBuilder();
                                        sb7222.append("com.tmessages.openchat");
                                        boolean z32222 = z21;
                                        sb7222.append(Math.random());
                                        sb7222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent522.setAction(sb7222.toString());
                                        intent522.setFlags(ConnectionsManager.FileTypeFile);
                                        if (messageObject3.isStoryReactionPush) {
                                        }
                                        user3 = user2;
                                        fileLocation = null;
                                        String str26222222 = str3;
                                        intent522.putExtra(str26222222, notificationsController2.currentAccount);
                                        int i43222222 = i10;
                                        builder2 = builder;
                                        NotificationCompat.Builder showWhen222222 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                        SharedPreferences sharedPreferences4222222 = sharedPreferences3;
                                        showWhen222222.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                        builder2.setCategory("msg");
                                        if (chat3 == null) {
                                        }
                                        Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                        intent2222222.putExtra("messageDate", messageObject3.messageOwner.date);
                                        intent2222222.putExtra(str26222222, notificationsController2.currentAccount);
                                        if (messageObject3.isStoryPush) {
                                        }
                                        if (messageObject3.isStoryReactionPush) {
                                        }
                                        builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent2222222, 167772160));
                                        if (bitmap2 == null) {
                                        }
                                        jArr = null;
                                        boolean z332222222 = z11;
                                        if (z) {
                                        }
                                        builder2.setPriority(-1);
                                    }
                                    z9 = true;
                                    if (z9) {
                                    }
                                    z10 = z9;
                                    sharedPreferences3 = sharedPreferences2;
                                    j7 = j6;
                                    z11 = z8;
                                    z12 = z10;
                                    if (z12) {
                                    }
                                    z14 = z13;
                                    String path2222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                    z15 = !ApplicationLoader.mainInterfacePaused;
                                    getSharedPrefKey(j7, j8);
                                    if (notificationsController.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j7, j8, false)) {
                                    }
                                    if (messageObject2.isReactionPush) {
                                    }
                                    str10 = str7;
                                    charSequence = str20;
                                    MessageObject messageObject92222 = messageObject2;
                                    j10 = j;
                                    j11 = j7;
                                    j12 = sharedPreferences3.getLong("ReactionSoundDocId", 0L);
                                    if (j12 != 0) {
                                    }
                                    i3 = sharedPreferences3.getInt("vibrate_react", 0);
                                    int i362222 = sharedPreferences3.getInt("priority_react", 1);
                                    i4 = sharedPreferences3.getInt("ReactionsLed", -16776961);
                                    String str242222 = string2;
                                    messageObject3 = messageObject92222;
                                    z18 = z17;
                                    i5 = i362222;
                                    if (messageObject3.isStoryReactionPush) {
                                    }
                                    if (i3 == i6) {
                                    }
                                    if (TextUtils.isEmpty(str9)) {
                                    }
                                    str12 = str11;
                                    z16 = z18;
                                    i9 = 3;
                                    z20 = true;
                                    if (i != i9) {
                                    }
                                    i = i5;
                                    if (num != null) {
                                    }
                                    if (i2 != 0) {
                                    }
                                    z21 = z20;
                                    i10 = i8;
                                    if (z15) {
                                    }
                                    if (z19) {
                                    }
                                    if (z14) {
                                    }
                                    Intent intent5222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
                                    StringBuilder sb72222 = new StringBuilder();
                                    sb72222.append("com.tmessages.openchat");
                                    boolean z322222 = z21;
                                    sb72222.append(Math.random());
                                    sb72222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    intent5222.setAction(sb72222.toString());
                                    intent5222.setFlags(ConnectionsManager.FileTypeFile);
                                    if (messageObject3.isStoryReactionPush) {
                                    }
                                    user3 = user2;
                                    fileLocation = null;
                                    String str262222222 = str3;
                                    intent5222.putExtra(str262222222, notificationsController2.currentAccount);
                                    int i432222222 = i10;
                                    builder2 = builder;
                                    NotificationCompat.Builder showWhen2222222 = builder2.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(notificationsController2.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222, 1140850688)).setGroup(notificationsController2.notificationGroup).setGroupSummary(true).setShowWhen(true);
                                    SharedPreferences sharedPreferences42222222 = sharedPreferences3;
                                    showWhen2222222.setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                    builder2.setCategory("msg");
                                    if (chat3 == null) {
                                    }
                                    Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, (Class<?>) NotificationDismissReceiver.class);
                                    intent22222222.putExtra("messageDate", messageObject3.messageOwner.date);
                                    intent22222222.putExtra(str262222222, notificationsController2.currentAccount);
                                    if (messageObject3.isStoryPush) {
                                    }
                                    if (messageObject3.isStoryReactionPush) {
                                    }
                                    builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i21, intent22222222, 167772160));
                                    if (bitmap2 == null) {
                                    }
                                    jArr = null;
                                    boolean z3322222222 = z11;
                                    if (z) {
                                    }
                                    builder2.setPriority(-1);
                                }
                                str2 = userName;
                                sharedPreferences = notificationsSettings;
                                str3 = str19;
                                if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                                    string = LocaleController.getString(R.string.NotificationHiddenName);
                                }
                                String str202 = string;
                                if (z5) {
                                }
                                NotificationCompat.Builder builder32 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                                if (notificationsController.pushMessages.size() > 1) {
                                }
                                z7 = z3;
                                boolean[] zArr3 = new boolean[1];
                                String str212 = str4;
                                messageObject2 = messageObject;
                                stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr3, null);
                                boolean isSilentMessage2 = notificationsController.isSilentMessage(messageObject2);
                                if (stringForMessage == null) {
                                }
                            }
                        } else {
                            j4 = topicId;
                            notificationsController = this;
                        }
                        string = LocaleController.getString(!z4 ? j3 != 0 ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName : R.string.AppName);
                        z6 = false;
                        if (!messageObject.isReactionPush) {
                            str2 = userName;
                            sharedPreferences = notificationsSettings;
                            str3 = str19;
                            String str2022 = string;
                            if (z5) {
                            }
                            NotificationCompat.Builder builder322 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                            if (notificationsController.pushMessages.size() > 1) {
                            }
                            z7 = z3;
                            boolean[] zArr32 = new boolean[1];
                            String str2122 = str4;
                            messageObject2 = messageObject;
                            stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr32, null);
                            boolean isSilentMessage22 = notificationsController.isSilentMessage(messageObject2);
                            if (stringForMessage == null) {
                            }
                        }
                        str2 = userName;
                        sharedPreferences = notificationsSettings;
                        str3 = str19;
                        if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                        }
                        String str20222 = string;
                        if (z5) {
                        }
                        NotificationCompat.Builder builder3222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (notificationsController.pushMessages.size() > 1) {
                        }
                        z7 = z3;
                        boolean[] zArr322 = new boolean[1];
                        String str21222 = str4;
                        messageObject2 = messageObject;
                        stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr322, null);
                        boolean isSilentMessage222 = notificationsController.isSilentMessage(messageObject2);
                        if (stringForMessage == null) {
                        }
                    }
                    z4 = true;
                    z5 = !"samsung".equalsIgnoreCase(Build.MANUFACTURER);
                    if (DialogObject.isEncryptedDialog(dialogId)) {
                    }
                    string = LocaleController.getString(!z4 ? j3 != 0 ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName : R.string.AppName);
                    z6 = false;
                    if (!messageObject.isReactionPush) {
                    }
                    str2 = userName;
                    sharedPreferences = notificationsSettings;
                    str3 = str19;
                    if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                    }
                    String str202222 = string;
                    if (z5) {
                    }
                    NotificationCompat.Builder builder32222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (notificationsController.pushMessages.size() > 1) {
                    }
                    z7 = z3;
                    boolean[] zArr3222 = new boolean[1];
                    String str212222 = str4;
                    messageObject2 = messageObject;
                    stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr3222, null);
                    boolean isSilentMessage2222 = notificationsController.isSilentMessage(messageObject2);
                    if (stringForMessage == null) {
                    }
                }
                userName = chat3 != null ? chat3.title : UserObject.getUserName(user);
                if (!AndroidUtilities.needShowPasscode()) {
                    z4 = false;
                    z5 = !"samsung".equalsIgnoreCase(Build.MANUFACTURER);
                    if (DialogObject.isEncryptedDialog(dialogId)) {
                    }
                    string = LocaleController.getString(!z4 ? j3 != 0 ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName : R.string.AppName);
                    z6 = false;
                    if (!messageObject.isReactionPush) {
                    }
                    str2 = userName;
                    sharedPreferences = notificationsSettings;
                    str3 = str19;
                    if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                    }
                    String str2022222 = string;
                    if (z5) {
                    }
                    NotificationCompat.Builder builder322222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (notificationsController.pushMessages.size() > 1) {
                    }
                    z7 = z3;
                    boolean[] zArr32222 = new boolean[1];
                    String str2122222 = str4;
                    messageObject2 = messageObject;
                    stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr32222, null);
                    boolean isSilentMessage22222 = notificationsController.isSilentMessage(messageObject2);
                    if (stringForMessage == null) {
                    }
                }
                z4 = true;
                z5 = !"samsung".equalsIgnoreCase(Build.MANUFACTURER);
                if (DialogObject.isEncryptedDialog(dialogId)) {
                }
                string = LocaleController.getString(!z4 ? j3 != 0 ? R.string.NotificationHiddenChatName : R.string.NotificationHiddenName : R.string.AppName);
                z6 = false;
                if (!messageObject.isReactionPush) {
                }
                str2 = userName;
                sharedPreferences = notificationsSettings;
                str3 = str19;
                if (!sharedPreferences.getBoolean("EnableReactionsPreview", true)) {
                }
                String str20222222 = string;
                if (z5) {
                }
                NotificationCompat.Builder builder3222222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (notificationsController.pushMessages.size() > 1) {
                }
                z7 = z3;
                boolean[] zArr322222 = new boolean[1];
                String str21222222 = str4;
                messageObject2 = messageObject;
                stringForMessage = notificationsController.getStringForMessage(messageObject2, false, zArr322222, null);
                boolean isSilentMessage222222 = notificationsController.isSilentMessage(messageObject2);
                if (stringForMessage == null) {
                }
            } else {
                str = "currentAccount";
                chat = null;
            }
            z2 = false;
            String str192 = str;
            TLRPC.Chat chat32 = chat;
            long j222 = j2;
            Bitmap bitmap22 = bitmap;
            int i312 = i30;
            notifyOverride = getNotifyOverride(notificationsSettings, fromChatId, topicId);
            if (notifyOverride != -1) {
            }
            if (j3 == 0) {
            }
            user = user4;
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    private boolean unsupportedNotificationShortcut() {
        return Build.VERSION.SDK_INT < 29 || !SharedConfig.chatBubbles;
    }

    private void updateStoryPushesRunnable() {
        long j = Long.MAX_VALUE;
        for (int i = 0; i < this.storyPushMessages.size(); i++) {
            Iterator<Pair<Long, Long>> it = this.storyPushMessages.get(i).dateByIds.values().iterator();
            while (it.hasNext()) {
                j = Math.min(j, ((Long) it.next().second).longValue());
            }
        }
        DispatchQueue dispatchQueue = notificationsQueue;
        dispatchQueue.cancelRunnable(this.checkStoryPushesRunnable);
        long currentTimeMillis = j - System.currentTimeMillis();
        if (j != Long.MAX_VALUE) {
            dispatchQueue.postRunnable(this.checkStoryPushesRunnable, Math.max(0L, currentTimeMillis));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:153:0x0451, code lost:
    
        if (r12 != false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x048e, code lost:
    
        r14 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x048c, code lost:
    
        r14 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0459, code lost:
    
        if (r12 != false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0462, code lost:
    
        if (r12 != false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x046f, code lost:
    
        if (r12 != false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x0475, code lost:
    
        if (r12 != false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x048a, code lost:
    
        if (r12 != false) goto L221;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x05a3 A[LOOP:1: B:99:0x059e->B:101:0x05a3, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:102:0x05ad A[EDGE_INSN: B:102:0x05ad->B:103:0x05ad BREAK  A[LOOP:1: B:99:0x059e->B:101:0x05a3], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x05b4  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x05c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x05d1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x05e9  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0441  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x04a0  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x04e1  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x049a  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0531  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0551 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0600  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0590 A[ADDED_TO_REGION] */
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
        } else {
            if (z) {
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
                    }
                    if (!z3 && !z11) {
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
                    }
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
            }
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
        edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey).remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey);
        getMessagesStorage().setDialogFlags(j, 0L);
        TLRPC.Dialog dialog = (TLRPC.Dialog) getMessagesController().dialogs_dict.get(j);
        if (dialog != null) {
            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
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

    /* renamed from: deleteNotificationChannelGlobalInternal, reason: merged with bridge method [inline-methods] */
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
                    edit.remove(str).remove(str + "_s");
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
                    edit.remove(str2).remove(str2 + "_s");
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
        String str5 = "private" + this.currentAccount;
        String str6 = "stories" + this.currentAccount;
        String str7 = "reactions" + this.currentAccount;
        String str8 = "other" + this.currentAccount;
        int size2 = notificationChannelGroups.size();
        String str9 = str8;
        String str10 = str7;
        String str11 = str6;
        String str12 = str5;
        for (int i2 = 0; i2 < size2; i2++) {
            id2 = NotificationsController$$ExternalSyntheticApiModelOutline16.m(notificationChannelGroups.get(i2)).getId();
            if (str3 != null && str3.equals(id2)) {
                str3 = null;
            } else if (str4 != null && str4.equals(id2)) {
                str4 = null;
            } else if (str11 != null && str11.equals(id2)) {
                str11 = null;
            } else if (str10 != null && str10.equals(id2)) {
                str10 = null;
            } else if (str12 != null && str12.equals(id2)) {
                str12 = null;
            } else if (str9 != null && str9.equals(id2)) {
                str9 = null;
            }
            if (str3 == null && str11 == null && str10 == null && str4 == null && str12 == null && str9 == null) {
                break;
            }
        }
        if (str3 != null || str4 != null || str10 != null || str11 != null || str12 != null || str9 != null) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                getUserConfig().getCurrentUser();
            }
            String str13 = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : "";
            ArrayList arrayList = new ArrayList();
            if (str3 != null) {
                arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString(R.string.NotificationsChannels) + str13));
            }
            if (str4 != null) {
                arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString(R.string.NotificationsGroups) + str13));
            }
            if (str11 != null) {
                arrayList.add(new NotificationChannelGroup(str11, LocaleController.getString(R.string.NotificationsStories) + str13));
            }
            if (str10 != null) {
                arrayList.add(new NotificationChannelGroup(str10, LocaleController.getString(R.string.NotificationsReactions) + str13));
            }
            if (str12 != null) {
                arrayList.add(new NotificationChannelGroup(str12, LocaleController.getString(R.string.NotificationsPrivateChats) + str13));
            }
            if (str9 != null) {
                arrayList.add(new NotificationChannelGroup(str9, LocaleController.getString(R.string.NotificationsOther) + str13));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
    }

    protected void forceShowPopupForReply() {
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
                TLRPC.Message message = messageObject.messageOwner;
                if ((!message.mentioned || !(message.action instanceof TLRPC.TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && ((messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) && dialogId != UserObject.VERIFY)) {
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
        } else {
            if (i != 3) {
                return getAccountInstance().getNotificationsSettings().getInt(getGlobalNotificationsKey(i), 0) < getConnectionsManager().getCurrentTime();
            }
            notificationsSettings = getAccountInstance().getNotificationsSettings();
            str = "EnableAllStories";
        }
        return notificationsSettings.getBoolean(str, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0014, code lost:
    
        if (r3.booleanValue() != false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002e, code lost:
    
        r1 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x002c, code lost:
    
        if (r1.megagroup == false) goto L16;
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
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-j));
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
            TLRPC.Dialog dialog = (TLRPC.Dialog) getMessagesController().dialogs_dict.get(j);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
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
                TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
                if (dialog != null) {
                    TLRPC.TL_peerNotifySettings tL_peerNotifySettings = new TLRPC.TL_peerNotifySettings();
                    dialog.notify_settings = tL_peerNotifySettings;
                    if (i != Integer.MAX_VALUE || isGlobalNotificationsEnabled) {
                        tL_peerNotifySettings.mute_until = i;
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

    public void processLoadedUnreadMessages(final LongSparseArray longSparseArray, final ArrayList<TLRPC.Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC.User> arrayList3, ArrayList<TLRPC.Chat> arrayList4, ArrayList<TLRPC.EncryptedChat> arrayList5, final Collection<StoryNotification> collection) {
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

    protected void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$repeatNotificationMaybe$38();
            }
        });
    }

    public void setDialogNotificationsSettings(long j, long j2, int i) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(j);
        if (i == 4) {
            if (isGlobalNotificationsEnabled(j, false, false)) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2));
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 0);
            }
            getMessagesStorage().setDialogFlags(j, 0L);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
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
            if (dialog != null) {
                TLRPC.TL_peerNotifySettings tL_peerNotifySettings = new TLRPC.TL_peerNotifySettings();
                dialog.notify_settings = tL_peerNotifySettings;
                tL_peerNotifySettings.mute_until = currentTime;
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
        TL_account.TL_reactionsNotifySettings tL_reactionsNotifySettings;
        TL_account.ReactionNotificationsFrom tL_reactionNotificationsFromAll;
        TL_account.TL_reactionsNotifySettings tL_reactionsNotifySettings2;
        TL_account.ReactionNotificationsFrom tL_reactionNotificationsFromAll2;
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings;
        String str;
        String str2;
        String str3;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (i == 4 || i == 5) {
            TL_account.setReactionsNotifySettings setreactionsnotifysettings = new TL_account.setReactionsNotifySettings();
            setreactionsnotifysettings.settings = new TL_account.TL_reactionsNotifySettings();
            if (notificationsSettings.getBoolean("EnableReactionsMessages", true)) {
                setreactionsnotifysettings.settings.flags |= 1;
                if (notificationsSettings.getBoolean("EnableReactionsMessagesContacts", false)) {
                    tL_reactionsNotifySettings2 = setreactionsnotifysettings.settings;
                    tL_reactionNotificationsFromAll2 = new TL_account.TL_reactionNotificationsFromContacts();
                } else {
                    tL_reactionsNotifySettings2 = setreactionsnotifysettings.settings;
                    tL_reactionNotificationsFromAll2 = new TL_account.TL_reactionNotificationsFromAll();
                }
                tL_reactionsNotifySettings2.messages_notify_from = tL_reactionNotificationsFromAll2;
            }
            if (notificationsSettings.getBoolean("EnableReactionsStories", true)) {
                setreactionsnotifysettings.settings.flags |= 2;
                if (notificationsSettings.getBoolean("EnableReactionsStoriesContacts", false)) {
                    tL_reactionsNotifySettings = setreactionsnotifysettings.settings;
                    tL_reactionNotificationsFromAll = new TL_account.TL_reactionNotificationsFromContacts();
                } else {
                    tL_reactionsNotifySettings = setreactionsnotifysettings.settings;
                    tL_reactionNotificationsFromAll = new TL_account.TL_reactionNotificationsFromAll();
                }
                tL_reactionsNotifySettings.stories_notify_from = tL_reactionNotificationsFromAll;
            }
            setreactionsnotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnableReactionsPreview", true);
            setreactionsnotifysettings.settings.sound = getInputSound(notificationsSettings, "ReactionSound", "ReactionSoundDocId", "ReactionSoundPath");
            getConnectionsManager().sendRequest(setreactionsnotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda53
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    NotificationsController.lambda$updateServerNotificationsSettings$48(tLObject, tL_error);
                }
            });
            return;
        }
        TL_account.updateNotifySettings updatenotifysettings = new TL_account.updateNotifySettings();
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings2 = new TLRPC.TL_inputPeerNotifySettings();
        updatenotifysettings.settings = tL_inputPeerNotifySettings2;
        tL_inputPeerNotifySettings2.flags = 5;
        if (i == 0) {
            updatenotifysettings.peer = new TLRPC.TL_inputNotifyChats();
            updatenotifysettings.settings.mute_until = notificationsSettings.getInt("EnableGroup2", 0);
            updatenotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewGroup", true);
            tL_inputPeerNotifySettings = updatenotifysettings.settings;
            tL_inputPeerNotifySettings.flags |= 8;
            str = "GroupSoundDocId";
            str2 = "GroupSoundPath";
            str3 = "GroupSound";
        } else {
            if (i == 1 || i == 3) {
                updatenotifysettings.peer = new TLRPC.TL_inputNotifyUsers();
                updatenotifysettings.settings.mute_until = notificationsSettings.getInt("EnableAll2", 0);
                updatenotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewAll", true);
                TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings3 = updatenotifysettings.settings;
                tL_inputPeerNotifySettings3.flags |= 128;
                tL_inputPeerNotifySettings3.stories_hide_sender = notificationsSettings.getBoolean("EnableHideStoriesSenders", false);
                if (notificationsSettings.contains("EnableAllStories")) {
                    TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings4 = updatenotifysettings.settings;
                    tL_inputPeerNotifySettings4.flags |= 64;
                    tL_inputPeerNotifySettings4.stories_muted = !notificationsSettings.getBoolean("EnableAllStories", true);
                }
                TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings5 = updatenotifysettings.settings;
                tL_inputPeerNotifySettings5.flags |= 8;
                tL_inputPeerNotifySettings5.sound = getInputSound(notificationsSettings, "GlobalSound", "GlobalSoundDocId", "GlobalSoundPath");
                TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings6 = updatenotifysettings.settings;
                tL_inputPeerNotifySettings6.flags |= 256;
                tL_inputPeerNotifySettings6.stories_sound = getInputSound(notificationsSettings, "StoriesSound", "StoriesSoundDocId", "StoriesSoundPath");
                getConnectionsManager().sendRequest(updatenotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda52
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        NotificationsController.lambda$updateServerNotificationsSettings$49(tLObject, tL_error);
                    }
                });
            }
            updatenotifysettings.peer = new TLRPC.TL_inputNotifyBroadcasts();
            updatenotifysettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            updatenotifysettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
            tL_inputPeerNotifySettings = updatenotifysettings.settings;
            tL_inputPeerNotifySettings.flags |= 8;
            str = "ChannelSoundDocId";
            str2 = "ChannelSoundPath";
            str3 = "ChannelSound";
        }
        tL_inputPeerNotifySettings.sound = getInputSound(notificationsSettings, str3, str, str2);
        getConnectionsManager().sendRequest(updatenotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda52
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                NotificationsController.lambda$updateServerNotificationsSettings$49(tLObject, tL_error);
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
        TL_account.updateNotifySettings updatenotifysettings = new TL_account.updateNotifySettings();
        updatenotifysettings.settings = new TLRPC.TL_inputPeerNotifySettings();
        String sharedPrefKey = getSharedPrefKey(j, j2);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings = updatenotifysettings.settings;
        tL_inputPeerNotifySettings.flags |= 1;
        tL_inputPeerNotifySettings.show_previews = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + sharedPrefKey, true);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings2 = updatenotifysettings.settings;
        tL_inputPeerNotifySettings2.flags = tL_inputPeerNotifySettings2.flags | 2;
        tL_inputPeerNotifySettings2.silent = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + sharedPrefKey, false);
        if (notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey)) {
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings3 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings3.flags |= 64;
            tL_inputPeerNotifySettings3.stories_muted = !notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, true);
        }
        int i2 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), -1);
        if (i2 != -1) {
            TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings4 = updatenotifysettings.settings;
            tL_inputPeerNotifySettings4.flags |= 4;
            if (i2 == 3) {
                i = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, j2), 0);
            } else if (i2 == 2) {
                i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            tL_inputPeerNotifySettings4.mute_until = i;
        }
        long j3 = notificationsSettings.getLong("sound_document_id_" + getSharedPrefKey(j, j2), 0L);
        String string = notificationsSettings.getString("sound_path_" + getSharedPrefKey(j, j2), null);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings5 = updatenotifysettings.settings;
        tL_inputPeerNotifySettings5.flags = tL_inputPeerNotifySettings5.flags | 8;
        if (j3 != 0) {
            TLRPC.TL_notificationSoundRingtone tL_notificationSoundRingtone = new TLRPC.TL_notificationSoundRingtone();
            tL_notificationSoundRingtone.id = j3;
            updatenotifysettings.settings.sound = tL_notificationSoundRingtone;
        } else if (string == null) {
            tL_inputPeerNotifySettings5.sound = new TLRPC.TL_notificationSoundDefault();
        } else if (string.equalsIgnoreCase("NoSound")) {
            updatenotifysettings.settings.sound = new TLRPC.TL_notificationSoundNone();
        } else {
            TLRPC.TL_notificationSoundLocal tL_notificationSoundLocal = new TLRPC.TL_notificationSoundLocal();
            tL_notificationSoundLocal.title = notificationsSettings.getString("sound_" + getSharedPrefKey(j, j2), null);
            tL_notificationSoundLocal.data = string;
            updatenotifysettings.settings.sound = tL_notificationSoundLocal;
        }
        if (j2 == 0 || j == getUserConfig().getClientUserId()) {
            TLRPC.TL_inputNotifyPeer tL_inputNotifyPeer = new TLRPC.TL_inputNotifyPeer();
            updatenotifysettings.peer = tL_inputNotifyPeer;
            tL_inputNotifyPeer.peer = getMessagesController().getInputPeer(j);
        } else {
            TLRPC.TL_inputNotifyForumTopic tL_inputNotifyForumTopic = new TLRPC.TL_inputNotifyForumTopic();
            tL_inputNotifyForumTopic.peer = getMessagesController().getInputPeer(j);
            tL_inputNotifyForumTopic.top_msg_id = (int) j2;
            updatenotifysettings.peer = tL_inputNotifyForumTopic;
        }
        getConnectionsManager().sendRequest(updatenotifysettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda72
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                NotificationsController.lambda$updateServerNotificationsSettings$47(tLObject, tL_error);
            }
        });
    }
}
