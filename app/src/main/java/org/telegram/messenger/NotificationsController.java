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
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.audio.SilenceSkippingAudioProcessor;
import com.google.android.exoplayer2.upstream.cache.ContentMetadata;
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
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;
/* loaded from: classes4.dex */
public class NotificationsController extends BaseController {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static volatile NotificationsController[] Instance = null;
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
    public static String OTHER_NOTIFICATIONS_CHANNEL = null;
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

    static {
        notificationManager = null;
        systemNotificationManager = null;
        if (Build.VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
            notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
            systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
            checkOtherNotificationsChannel();
        }
        audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService("audio");
        Instance = new NotificationsController[4];
        lockObjects = new Object[4];
        for (int i = 0; i < 4; i++) {
            lockObjects[i] = new Object();
        }
    }

    public static NotificationsController getInstance(int num) {
        NotificationsController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (lockObjects[num]) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    NotificationsController[] notificationsControllerArr = Instance;
                    NotificationsController notificationsController = new NotificationsController(num);
                    localInstance = notificationsController;
                    notificationsControllerArr[num] = notificationsController;
                }
            }
        }
        return localInstance;
    }

    public NotificationsController(int instance) {
        super(instance);
        StringBuilder sb = new StringBuilder();
        sb.append("messages");
        sb.append(this.currentAccount == 0 ? "" : Integer.valueOf(this.currentAccount));
        this.notificationGroup = sb.toString();
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        this.inChatSoundEnabled = preferences.getBoolean("EnableInChatSound", true);
        this.showBadgeNumber = preferences.getBoolean("badgeNumber", true);
        this.showBadgeMuted = preferences.getBoolean("badgeNumberMuted", false);
        this.showBadgeMessages = preferences.getBoolean("badgeNumberMessages", true);
        notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
        systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
        try {
            audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService("audio");
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            this.alarmManager = (AlarmManager) ApplicationLoader.applicationContext.getSystemService(NotificationCompat.CATEGORY_ALARM);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            PowerManager pm = (PowerManager) ApplicationLoader.applicationContext.getSystemService("power");
            PowerManager.WakeLock newWakeLock = pm.newWakeLock(1, "telegram:notification_delay_lock");
            this.notificationDelayWakelock = newWakeLock;
            newWakeLock.setReferenceCounted(false);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1101lambda$new$0$orgtelegrammessengerNotificationsController();
            }
        };
    }

    /* renamed from: lambda$new$0$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1101lambda$new$0$orgtelegrammessengerNotificationsController() {
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
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        SharedPreferences preferences = null;
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            OTHER_NOTIFICATIONS_CHANNEL = preferences.getString("OtherKey", "Other3");
        }
        NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
        if (notificationChannel != null && notificationChannel.getImportance() == 0) {
            systemNotificationManager.deleteNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
            OTHER_NOTIFICATIONS_CHANNEL = null;
            notificationChannel = null;
        }
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            if (preferences == null) {
                preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            }
            OTHER_NOTIFICATIONS_CHANNEL = "Other" + Utilities.random.nextLong();
            preferences.edit().putString("OtherKey", OTHER_NOTIFICATIONS_CHANNEL).commit();
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

    public void muteUntil(long did, int selectedTimeInSeconds) {
        long flags;
        if (did != 0) {
            SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
            SharedPreferences.Editor editor = preferences.edit();
            boolean defaultEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(did);
            if (selectedTimeInSeconds == Integer.MAX_VALUE) {
                if (!defaultEnabled) {
                    editor.remove("notify2_" + did);
                    flags = 0;
                } else {
                    editor.putInt("notify2_" + did, 2);
                    flags = 1;
                }
            } else {
                editor.putInt("notify2_" + did, 3);
                editor.putInt("notifyuntil_" + did, getConnectionsManager().getCurrentTime() + selectedTimeInSeconds);
                flags = (((long) selectedTimeInSeconds) << 32) | 1;
            }
            getInstance(this.currentAccount).removeNotificationsForDialog(did);
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(did, flags);
            editor.commit();
            TLRPC.Dialog dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(did);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                if (selectedTimeInSeconds != Integer.MAX_VALUE || defaultEnabled) {
                    dialog.notify_settings.mute_until = selectedTimeInSeconds;
                }
            }
            getInstance(this.currentAccount).updateServerNotificationsSettings(did);
        }
    }

    public void cleanup() {
        this.popupMessages.clear();
        this.popupReplyMessages.clear();
        this.channelGroupsCreated = false;
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1094lambda$cleanup$1$orgtelegrammessengerNotificationsController();
            }
        });
    }

    /* renamed from: lambda$cleanup$1$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1094lambda$cleanup$1$orgtelegrammessengerNotificationsController() {
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
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                systemNotificationManager.deleteNotificationChannelGroup("channels" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("groups" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("private" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("other" + this.currentAccount);
                String keyStart = this.currentAccount + "channel";
                List<NotificationChannel> list = systemNotificationManager.getNotificationChannels();
                int count = list.size();
                for (int a = 0; a < count; a++) {
                    NotificationChannel channel = list.get(a);
                    String id = channel.getId();
                    if (id.startsWith(keyStart)) {
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
            } catch (Throwable e3) {
                FileLog.e(e3);
            }
        }
    }

    public void setInChatSoundEnabled(boolean value) {
        this.inChatSoundEnabled = value;
    }

    /* renamed from: lambda$setOpenedDialogId$2$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1123x8a2b000c(long dialog_id) {
        this.openedDialogId = dialog_id;
    }

    public void setOpenedDialogId(final long dialog_id) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1123x8a2b000c(dialog_id);
            }
        });
    }

    public void setOpenedInBubble(final long dialogId, final boolean opened) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1124x8c233e1d(opened, dialogId);
            }
        });
    }

    /* renamed from: lambda$setOpenedInBubble$3$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1124x8c233e1d(boolean opened, long dialogId) {
        if (opened) {
            this.openedInBubbleDialogs.add(Long.valueOf(dialogId));
        } else {
            this.openedInBubbleDialogs.remove(Long.valueOf(dialogId));
        }
    }

    public void setLastOnlineFromOtherDevice(final int time) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1122xfb3acdad(time);
            }
        });
    }

    /* renamed from: lambda$setLastOnlineFromOtherDevice$4$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1122xfb3acdad(int time) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("set last online from other device = " + time);
        }
        this.lastOnlineFromOtherDevice = time;
    }

    public void removeNotificationsForDialog(long did) {
        processReadMessages(null, did, 0, Integer.MAX_VALUE, false);
        LongSparseIntArray dialogsToUpdate = new LongSparseIntArray();
        dialogsToUpdate.put(did, 0);
        processDialogsUpdateRead(dialogsToUpdate);
    }

    public boolean hasMessagesToReply() {
        for (int a = 0; a < this.pushMessages.size(); a++) {
            MessageObject messageObject = this.pushMessages.get(a);
            long dialog_id = messageObject.getDialogId();
            if ((!messageObject.messageOwner.mentioned || !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialog_id) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
                return true;
            }
        }
        return false;
    }

    public void forceShowPopupForReply() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1099x16c2e2d7();
            }
        });
    }

    /* renamed from: lambda$forceShowPopupForReply$6$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1099x16c2e2d7() {
        final ArrayList<MessageObject> popupArray = new ArrayList<>();
        for (int a = 0; a < this.pushMessages.size(); a++) {
            MessageObject messageObject = this.pushMessages.get(a);
            long dialog_id = messageObject.getDialogId();
            if ((!messageObject.messageOwner.mentioned || !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialog_id) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
                popupArray.add(0, messageObject);
            }
        }
        if (!popupArray.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1098xdcf840f8(popupArray);
                }
            });
        }
    }

    /* renamed from: lambda$forceShowPopupForReply$5$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1098xdcf840f8(ArrayList popupArray) {
        this.popupReplyMessages = popupArray;
        Intent popupIntent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
        popupIntent.putExtra("force", true);
        popupIntent.putExtra("currentAccount", this.currentAccount);
        popupIntent.setFlags(268763140);
        ApplicationLoader.applicationContext.startActivity(popupIntent);
        Intent it = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        ApplicationLoader.applicationContext.sendBroadcast(it);
    }

    public void removeDeletedMessagesFromNotifications(final LongSparseArray<ArrayList<Integer>> deletedMessages) {
        final ArrayList<MessageObject> popupArrayRemove = new ArrayList<>(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1120x6483a23c(deletedMessages, popupArrayRemove);
            }
        });
    }

    /* renamed from: lambda$removeDeletedMessagesFromNotifications$9$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1120x6483a23c(LongSparseArray deletedMessages, final ArrayList popupArrayRemove) {
        long key;
        Integer newCount;
        LongSparseArray longSparseArray = deletedMessages;
        int old_unread_count = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        int a = 0;
        while (true) {
            int i = 0;
            if (a >= deletedMessages.size()) {
                break;
            }
            long key2 = longSparseArray.keyAt(a);
            SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(key2);
            if (sparseArray != null) {
                ArrayList<Integer> mids = (ArrayList) longSparseArray.get(key2);
                int b = 0;
                int N = mids.size();
                while (b < N) {
                    int mid = mids.get(b).intValue();
                    MessageObject messageObject = sparseArray.get(mid);
                    if (messageObject == null) {
                        key = key2;
                    } else {
                        key = key2;
                        long dialogId = messageObject.getDialogId();
                        Integer currentCount = this.pushDialogs.get(dialogId);
                        if (currentCount == null) {
                            currentCount = Integer.valueOf(i);
                        }
                        Integer newCount2 = Integer.valueOf(currentCount.intValue() - 1);
                        if (newCount2.intValue() > 0) {
                            newCount = newCount2;
                        } else {
                            Integer newCount3 = Integer.valueOf(i);
                            this.smartNotificationsDialogs.remove(dialogId);
                            newCount = newCount3;
                        }
                        if (!newCount.equals(currentCount)) {
                            int intValue = this.total_unread_count - currentCount.intValue();
                            this.total_unread_count = intValue;
                            this.total_unread_count = intValue + newCount.intValue();
                            this.pushDialogs.put(dialogId, newCount);
                        }
                        if (newCount.intValue() == 0) {
                            this.pushDialogs.remove(dialogId);
                            this.pushDialogsOverrideMention.remove(dialogId);
                        }
                        sparseArray.remove(mid);
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(messageObject);
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        popupArrayRemove.add(messageObject);
                    }
                    b++;
                    key2 = key;
                    i = 0;
                }
                long key3 = key2;
                if (sparseArray.size() == 0) {
                    this.pushMessagesDict.remove(key3);
                }
            }
            a++;
            longSparseArray = deletedMessages;
        }
        if (!popupArrayRemove.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1118xf0ee5e7e(popupArrayRemove);
                }
            });
        }
        if (old_unread_count != this.total_unread_count) {
            if (this.notifyCheck) {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            } else {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            }
            final int pushDialogsCount = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1119x2ab9005d(pushDialogsCount);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* renamed from: lambda$removeDeletedMessagesFromNotifications$7$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1118xf0ee5e7e(ArrayList popupArrayRemove) {
        int size = popupArrayRemove.size();
        for (int a = 0; a < size; a++) {
            this.popupMessages.remove(popupArrayRemove.get(a));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* renamed from: lambda$removeDeletedMessagesFromNotifications$8$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1119x2ab9005d(int pushDialogsCount) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(pushDialogsCount));
    }

    public void removeDeletedHisoryFromNotifications(final LongSparseIntArray deletedMessages) {
        final ArrayList<MessageObject> popupArrayRemove = new ArrayList<>(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1117xc147f6e8(deletedMessages, popupArrayRemove);
            }
        });
    }

    /* renamed from: lambda$removeDeletedHisoryFromNotifications$12$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1117xc147f6e8(LongSparseIntArray deletedMessages, final ArrayList popupArrayRemove) {
        long key;
        int i;
        LongSparseIntArray longSparseIntArray = deletedMessages;
        int old_unread_count = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        int a = 0;
        while (a < deletedMessages.size()) {
            long key2 = longSparseIntArray.keyAt(a);
            long dialogId = -key2;
            long id = longSparseIntArray.get(key2);
            Integer currentCount = this.pushDialogs.get(dialogId);
            if (currentCount == null) {
                currentCount = 0;
            }
            Integer newCount = currentCount;
            int c = 0;
            while (c < this.pushMessages.size()) {
                MessageObject messageObject = this.pushMessages.get(c);
                if (messageObject.getDialogId() == dialogId) {
                    key = key2;
                    if (messageObject.getId() > id) {
                        i = 1;
                    } else {
                        SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(dialogId);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(dialogId);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(messageObject);
                        c--;
                        if (isPersonalMessage(messageObject)) {
                            i = 1;
                            this.personalCount--;
                        } else {
                            i = 1;
                        }
                        popupArrayRemove.add(messageObject);
                        newCount = Integer.valueOf(newCount.intValue() - i);
                    }
                } else {
                    key = key2;
                    i = 1;
                }
                c += i;
                key2 = key;
            }
            if (newCount.intValue() <= 0) {
                newCount = 0;
                this.smartNotificationsDialogs.remove(dialogId);
            }
            if (!newCount.equals(currentCount)) {
                int intValue = this.total_unread_count - currentCount.intValue();
                this.total_unread_count = intValue;
                this.total_unread_count = intValue + newCount.intValue();
                this.pushDialogs.put(dialogId, newCount);
            }
            if (newCount.intValue() == 0) {
                this.pushDialogs.remove(dialogId);
                this.pushDialogsOverrideMention.remove(dialogId);
            }
            a++;
            longSparseIntArray = deletedMessages;
        }
        if (popupArrayRemove.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1115x4db2b32a(popupArrayRemove);
                }
            });
        }
        if (old_unread_count != this.total_unread_count) {
            if (this.notifyCheck) {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            } else {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            }
            final int pushDialogsCount = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1116x877d5509(pushDialogsCount);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* renamed from: lambda$removeDeletedHisoryFromNotifications$10$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1115x4db2b32a(ArrayList popupArrayRemove) {
        int size = popupArrayRemove.size();
        for (int a = 0; a < size; a++) {
            this.popupMessages.remove(popupArrayRemove.get(a));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* renamed from: lambda$removeDeletedHisoryFromNotifications$11$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1116x877d5509(int pushDialogsCount) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(pushDialogsCount));
    }

    public void processReadMessages(final LongSparseIntArray inbox, final long dialogId, final int maxDate, final int maxId, final boolean isPopup) {
        final ArrayList<MessageObject> popupArrayRemove = new ArrayList<>(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1114x6297df38(inbox, popupArrayRemove, dialogId, maxId, maxDate, isPopup);
            }
        });
    }

    /* renamed from: lambda$processReadMessages$14$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1114x6297df38(LongSparseIntArray inbox, final ArrayList popupArrayRemove, long dialogId, int maxId, int maxDate, boolean isPopup) {
        long did;
        long did2;
        long j = 0;
        if (inbox != null) {
            int b = 0;
            while (b < inbox.size()) {
                long key = inbox.keyAt(b);
                int messageId = inbox.get(key);
                int a = 0;
                while (a < this.pushMessages.size()) {
                    MessageObject messageObject = this.pushMessages.get(a);
                    if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == key && messageObject.getId() <= messageId) {
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        popupArrayRemove.add(messageObject);
                        if (messageObject.messageOwner.peer_id.channel_id != j) {
                            did2 = -messageObject.messageOwner.peer_id.channel_id;
                        } else {
                            did2 = 0;
                        }
                        SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(did2);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(did2);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(a);
                        a--;
                    }
                    a++;
                    j = 0;
                }
                b++;
                j = 0;
            }
        }
        if (dialogId != 0 && (maxId != 0 || maxDate != 0)) {
            int a2 = 0;
            while (a2 < this.pushMessages.size()) {
                MessageObject messageObject2 = this.pushMessages.get(a2);
                if (messageObject2.getDialogId() == dialogId) {
                    boolean remove = false;
                    if (maxDate != 0) {
                        if (messageObject2.messageOwner.date <= maxDate) {
                            remove = true;
                        }
                    } else if (!isPopup) {
                        if (messageObject2.getId() <= maxId || maxId < 0) {
                            remove = true;
                        }
                    } else if (messageObject2.getId() == maxId || maxId < 0) {
                        remove = true;
                    }
                    if (remove) {
                        if (isPersonalMessage(messageObject2)) {
                            this.personalCount--;
                        }
                        if (messageObject2.messageOwner.peer_id.channel_id != 0) {
                            did = -messageObject2.messageOwner.peer_id.channel_id;
                        } else {
                            did = 0;
                        }
                        SparseArray<MessageObject> sparseArray2 = this.pushMessagesDict.get(did);
                        if (sparseArray2 != null) {
                            sparseArray2.remove(messageObject2.getId());
                            if (sparseArray2.size() == 0) {
                                this.pushMessagesDict.remove(did);
                            }
                        }
                        this.pushMessages.remove(a2);
                        this.delayedPushMessages.remove(messageObject2);
                        popupArrayRemove.add(messageObject2);
                        a2--;
                    }
                }
                a2++;
            }
        }
        if (!popupArrayRemove.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1113x28cd3d59(popupArrayRemove);
                }
            });
        }
    }

    /* renamed from: lambda$processReadMessages$13$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1113x28cd3d59(ArrayList popupArrayRemove) {
        int size = popupArrayRemove.size();
        for (int a = 0; a < size; a++) {
            this.popupMessages.remove(popupArrayRemove.get(a));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    private int addToPopupMessages(ArrayList<MessageObject> popupArrayAdd, MessageObject messageObject, long dialogId, boolean isChannel, SharedPreferences preferences) {
        int popup = 0;
        if (!DialogObject.isEncryptedDialog(dialogId)) {
            if (preferences.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialogId, false)) {
                popup = preferences.getInt("popup_" + dialogId, 0);
            }
            if (popup == 0) {
                if (isChannel) {
                    popup = preferences.getInt("popupChannel", 0);
                } else {
                    popup = preferences.getInt(DialogObject.isChatDialog(dialogId) ? "popupGroup" : "popupAll", 0);
                }
            } else if (popup == 1) {
                popup = 3;
            } else if (popup == 2) {
                popup = 0;
            }
        }
        if (popup != 0 && messageObject.messageOwner.peer_id.channel_id != 0 && !messageObject.isSupergroup()) {
            popup = 0;
        }
        if (popup != 0) {
            popupArrayAdd.add(0, messageObject);
        }
        return popup;
    }

    public void processEditedMessages(final LongSparseArray<ArrayList<MessageObject>> editedMessages) {
        if (editedMessages.size() == 0) {
            return;
        }
        new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1107xd706556a(editedMessages);
            }
        });
    }

    /* renamed from: lambda$processEditedMessages$15$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1107xd706556a(LongSparseArray editedMessages) {
        long did;
        LongSparseArray longSparseArray = editedMessages;
        boolean updated = false;
        int a = 0;
        int N = editedMessages.size();
        while (a < N) {
            long dialogId = longSparseArray.keyAt(a);
            if (this.pushDialogs.indexOfKey(dialogId) >= 0) {
                ArrayList<MessageObject> messages = (ArrayList) longSparseArray.valueAt(a);
                int N2 = messages.size();
                for (int b = 0; b < N2; b++) {
                    MessageObject messageObject = messages.get(b);
                    if (messageObject.messageOwner.peer_id.channel_id != 0) {
                        did = -messageObject.messageOwner.peer_id.channel_id;
                    } else {
                        did = 0;
                    }
                    SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(did);
                    if (sparseArray == null) {
                        break;
                    }
                    MessageObject oldMessage = sparseArray.get(messageObject.getId());
                    if (oldMessage != null && oldMessage.isReactionPush) {
                        oldMessage = null;
                    }
                    if (oldMessage != null) {
                        updated = true;
                        sparseArray.put(messageObject.getId(), messageObject);
                        int idx = this.pushMessages.indexOf(oldMessage);
                        if (idx >= 0) {
                            this.pushMessages.set(idx, messageObject);
                        }
                        int idx2 = this.delayedPushMessages.indexOf(oldMessage);
                        if (idx2 >= 0) {
                            this.delayedPushMessages.set(idx2, messageObject);
                        }
                    }
                }
            }
            a++;
            longSparseArray = editedMessages;
        }
        if (updated) {
            showOrUpdateNotification(false);
        }
    }

    public void processNewMessages(final ArrayList<MessageObject> messageObjects, final boolean isLast, final boolean isFcm, final CountDownLatch countDownLatch) {
        if (messageObjects.isEmpty()) {
            if (countDownLatch != null) {
                countDownLatch.countDown();
                return;
            }
            return;
        }
        final ArrayList<MessageObject> popupArrayAdd = new ArrayList<>(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1112xffba819a(messageObjects, popupArrayAdd, isFcm, isLast, countDownLatch);
            }
        });
    }

    /* renamed from: lambda$processNewMessages$18$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1112xffba819a(ArrayList messageObjects, final ArrayList popupArrayAdd, boolean isFcm, boolean isLast, CountDownLatch countDownLatch) {
        Boolean isChannel;
        boolean canAddValue;
        Integer override;
        LongSparseArray<Boolean> settingsCache;
        boolean allowPinned;
        int a;
        boolean added;
        int popup;
        long randomId;
        boolean isChannel2;
        long did;
        long randomId2;
        boolean value;
        SparseArray<MessageObject> sparseArray;
        long dialogId;
        boolean edited;
        int i;
        long dialogId2;
        SparseArray<MessageObject> sparseArray2;
        boolean value2;
        SparseArray<MessageObject> sparseArray3;
        boolean added2;
        int popup2;
        MessageObject messageObject;
        ArrayList arrayList = messageObjects;
        LongSparseArray<Boolean> settingsCache2 = new LongSparseArray<>();
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        boolean allowPinned2 = preferences.getBoolean("PinnedMessages", true);
        boolean messageObject2 = false;
        boolean edited2 = false;
        int mid = 0;
        boolean hasScheduled = false;
        int a2 = 0;
        while (a2 < messageObjects.size()) {
            MessageObject messageObject3 = (MessageObject) arrayList.get(a2);
            if (messageObject3.messageOwner != null) {
                if (!messageObject3.isImportedForward() && !(messageObject3.messageOwner.action instanceof TLRPC.TL_messageActionSetMessagesTTL)) {
                    if (messageObject3.messageOwner.silent) {
                        if (!(messageObject3.messageOwner.action instanceof TLRPC.TL_messageActionContactSignUp)) {
                            if (messageObject3.messageOwner.action instanceof TLRPC.TL_messageActionUserJoined) {
                                a = a2;
                                settingsCache = settingsCache2;
                                allowPinned = allowPinned2;
                                added = messageObject2;
                                popup = mid;
                                mid = popup;
                                messageObject2 = added;
                                a2 = a + 1;
                                arrayList = messageObjects;
                                allowPinned2 = allowPinned;
                                settingsCache2 = settingsCache;
                            }
                        }
                    }
                }
                a = a2;
                settingsCache = settingsCache2;
                allowPinned = allowPinned2;
                added = messageObject2;
                popup = mid;
                mid = popup;
                messageObject2 = added;
                a2 = a + 1;
                arrayList = messageObjects;
                allowPinned2 = allowPinned;
                settingsCache2 = settingsCache;
            }
            int mid2 = messageObject3.getId();
            if (messageObject3.isFcmMessage()) {
                a = a2;
                randomId = messageObject3.messageOwner.random_id;
            } else {
                a = a2;
                randomId = 0;
            }
            allowPinned = allowPinned2;
            long dialogId3 = messageObject3.getDialogId();
            if (messageObject3.isFcmMessage()) {
                isChannel2 = messageObject3.localChannel;
            } else {
                boolean isChannel3 = DialogObject.isChatDialog(dialogId3);
                if (isChannel3) {
                    TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId3));
                    boolean isChannel4 = ChatObject.isChannel(chat) && !chat.megagroup;
                    isChannel2 = isChannel4;
                } else {
                    isChannel2 = false;
                }
            }
            if (messageObject3.messageOwner.peer_id.channel_id != 0) {
                did = -messageObject3.messageOwner.peer_id.channel_id;
            } else {
                did = 0;
            }
            SparseArray<MessageObject> sparseArray4 = this.pushMessagesDict.get(did);
            MessageObject oldMessageObject = sparseArray4 != null ? sparseArray4.get(mid2) : null;
            if (oldMessageObject == null) {
                randomId2 = randomId;
                if (messageObject3.messageOwner.random_id == 0) {
                    settingsCache = settingsCache2;
                } else {
                    settingsCache = settingsCache2;
                    oldMessageObject = this.fcmRandomMessagesDict.get(messageObject3.messageOwner.random_id);
                    if (oldMessageObject != null) {
                        this.fcmRandomMessagesDict.remove(messageObject3.messageOwner.random_id);
                    }
                }
            } else {
                randomId2 = randomId;
                settingsCache = settingsCache2;
            }
            MessageObject oldMessageObject2 = oldMessageObject;
            if (oldMessageObject2 == null) {
                added = messageObject2;
                popup = mid;
                long randomId3 = randomId2;
                long did2 = did;
                if (!edited2) {
                    if (isFcm) {
                        getMessagesStorage().putPushMessage(messageObject3);
                    }
                    if (dialogId3 == this.openedDialogId && ApplicationLoader.isScreenOn) {
                        if (!isFcm) {
                            playInChatSound();
                        }
                    } else {
                        if (messageObject3.messageOwner.mentioned) {
                            if (allowPinned || !(messageObject3.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage)) {
                                dialogId3 = messageObject3.getFromChatId();
                            }
                        }
                        if (isPersonalMessage(messageObject3)) {
                            this.personalCount++;
                        }
                        DialogObject.isChatDialog(dialogId3);
                        LongSparseArray<Boolean> settingsCache3 = settingsCache;
                        int index = settingsCache3.indexOfKey(dialogId3);
                        if (index >= 0) {
                            sparseArray = sparseArray4;
                            value = settingsCache3.valueAt(index).booleanValue();
                        } else {
                            int notifyOverride = getNotifyOverride(preferences, dialogId3);
                            if (notifyOverride == -1) {
                                value2 = isGlobalNotificationsEnabled(dialogId3, Boolean.valueOf(isChannel2));
                            } else {
                                value2 = notifyOverride != 2;
                            }
                            sparseArray = sparseArray4;
                            settingsCache3.put(dialogId3, Boolean.valueOf(value2));
                            value = value2;
                        }
                        if (!value) {
                            settingsCache = settingsCache3;
                            dialogId = dialogId3;
                            edited = edited2;
                        } else {
                            if (!isFcm) {
                                settingsCache = settingsCache3;
                                long j = dialogId3;
                                dialogId = dialogId3;
                                dialogId2 = dialogId3;
                                edited = edited2;
                                i = 0;
                                popup = addToPopupMessages(popupArrayAdd, messageObject3, j, isChannel2, preferences);
                            } else {
                                settingsCache = settingsCache3;
                                dialogId = dialogId3;
                                edited = edited2;
                                i = 0;
                                dialogId2 = dialogId3;
                            }
                            if (!hasScheduled) {
                                hasScheduled = messageObject3.messageOwner.from_scheduled;
                            }
                            this.delayedPushMessages.add(messageObject3);
                            this.pushMessages.add(i, messageObject3);
                            if (mid2 != 0) {
                                if (sparseArray != null) {
                                    sparseArray2 = sparseArray;
                                } else {
                                    sparseArray2 = new SparseArray<>();
                                    this.pushMessagesDict.put(did2, sparseArray2);
                                }
                                sparseArray2.put(mid2, messageObject3);
                            } else if (randomId3 != 0) {
                                this.fcmRandomMessagesDict.put(randomId3, messageObject3);
                            }
                            if (dialogId2 != dialogId) {
                                Integer current = this.pushDialogsOverrideMention.get(dialogId2);
                                this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(current == null ? 1 : current.intValue() + 1));
                            }
                        }
                        if (messageObject3.isReactionPush) {
                            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                            sparseBooleanArray.put(mid2, true);
                            getMessagesController().checkUnreadReactions(dialogId, sparseBooleanArray);
                        }
                        edited2 = edited;
                        messageObject2 = true;
                        mid = popup;
                        a2 = a + 1;
                        arrayList = messageObjects;
                        allowPinned2 = allowPinned;
                        settingsCache2 = settingsCache;
                    }
                }
                mid = popup;
                messageObject2 = added;
                a2 = a + 1;
                arrayList = messageObjects;
                allowPinned2 = allowPinned;
                settingsCache2 = settingsCache;
            } else if (!oldMessageObject2.isFcmMessage()) {
                added = messageObject2;
                popup = mid;
                mid = popup;
                messageObject2 = added;
                a2 = a + 1;
                arrayList = messageObjects;
                allowPinned2 = allowPinned;
                settingsCache2 = settingsCache;
            } else {
                if (sparseArray4 != null) {
                    sparseArray3 = sparseArray4;
                } else {
                    SparseArray<MessageObject> sparseArray5 = new SparseArray<>();
                    this.pushMessagesDict.put(did, sparseArray5);
                    sparseArray3 = sparseArray5;
                }
                sparseArray3.put(mid2, messageObject3);
                int idxOld = this.pushMessages.indexOf(oldMessageObject2);
                if (idxOld >= 0) {
                    this.pushMessages.set(idxOld, messageObject3);
                    added2 = messageObject2;
                    messageObject = messageObject3;
                    popup2 = addToPopupMessages(popupArrayAdd, messageObject3, dialogId3, isChannel2, preferences);
                } else {
                    added2 = messageObject2;
                    popup2 = mid;
                    messageObject = messageObject3;
                }
                if (isFcm) {
                    boolean z = messageObject.localEdit;
                    edited2 = z;
                    if (z) {
                        getMessagesStorage().putPushMessage(messageObject);
                    }
                }
                mid = popup2;
                messageObject2 = added2;
                a2 = a + 1;
                arrayList = messageObjects;
                allowPinned2 = allowPinned;
                settingsCache2 = settingsCache;
            }
        }
        boolean added3 = messageObject2;
        boolean edited3 = edited2;
        final int popup3 = mid;
        if (added3) {
            this.notifyCheck = isLast;
        }
        if (!popupArrayAdd.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1110x8c253ddc(popupArrayAdd, popup3);
                }
            });
        }
        if (isFcm || hasScheduled) {
            if (edited3) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (added3) {
                MessageObject messageObject4 = (MessageObject) messageObjects.get(0);
                long dialog_id = messageObject4.getDialogId();
                if (messageObject4.isFcmMessage()) {
                    isChannel = Boolean.valueOf(messageObject4.localChannel);
                } else {
                    isChannel = null;
                }
                int old_unread_count = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(preferences, dialog_id);
                if (notifyOverride2 == -1) {
                    canAddValue = isGlobalNotificationsEnabled(dialog_id, isChannel);
                } else {
                    canAddValue = notifyOverride2 != 2;
                }
                Integer currentCount = this.pushDialogs.get(dialog_id);
                int newCount = currentCount != null ? currentCount.intValue() + 1 : 1;
                if (this.notifyCheck && !canAddValue && (override = this.pushDialogsOverrideMention.get(dialog_id)) != null && override.intValue() != 0) {
                    canAddValue = true;
                    newCount = override.intValue();
                }
                if (canAddValue) {
                    if (currentCount != null) {
                        this.total_unread_count -= currentCount.intValue();
                    }
                    this.total_unread_count += newCount;
                    this.pushDialogs.put(dialog_id, Integer.valueOf(newCount));
                }
                if (old_unread_count != this.total_unread_count) {
                    this.delayedPushMessages.clear();
                    showOrUpdateNotification(this.notifyCheck);
                    final int pushDialogsCount = this.pushDialogs.size();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsController.this.m1111xc5efdfbb(pushDialogsCount);
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

    /* renamed from: lambda$processNewMessages$16$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1110x8c253ddc(ArrayList popupArrayAdd, int popupFinal) {
        this.popupMessages.addAll(0, popupArrayAdd);
        if (ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn) {
            if (popupFinal == 3 || ((popupFinal == 1 && ApplicationLoader.isScreenOn) || (popupFinal == 2 && !ApplicationLoader.isScreenOn))) {
                Intent popupIntent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
                popupIntent.setFlags(268763140);
                try {
                    ApplicationLoader.applicationContext.startActivity(popupIntent);
                } catch (Throwable th) {
                }
            }
        }
    }

    /* renamed from: lambda$processNewMessages$17$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1111xc5efdfbb(int pushDialogsCount) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(pushDialogsCount));
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }

    public void processDialogsUpdateRead(final LongSparseIntArray dialogsToUpdate) {
        final ArrayList<MessageObject> popupArrayToRemove = new ArrayList<>();
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1106xc2d50a00(dialogsToUpdate, popupArrayToRemove);
            }
        });
    }

    /* renamed from: lambda$processDialogsUpdateRead$21$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1106xc2d50a00(LongSparseIntArray dialogsToUpdate, final ArrayList popupArrayToRemove) {
        boolean z;
        long dialogId;
        long did;
        Integer override;
        TLRPC.Chat chat;
        int old_unread_count = this.total_unread_count;
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        int b = 0;
        while (true) {
            boolean canAddValue = false;
            z = true;
            int i = 1;
            if (b >= dialogsToUpdate.size()) {
                break;
            }
            long dialogId2 = dialogsToUpdate.keyAt(b);
            Integer currentCount = this.pushDialogs.get(dialogId2);
            int newCount = dialogsToUpdate.get(dialogId2);
            if (DialogObject.isChatDialog(dialogId2) && ((chat = getMessagesController().getChat(Long.valueOf(-dialogId2))) == null || chat.min || ChatObject.isNotInChat(chat))) {
                newCount = 0;
            }
            int notifyOverride = getNotifyOverride(preferences, dialogId2);
            if (notifyOverride == -1) {
                canAddValue = isGlobalNotificationsEnabled(dialogId2);
            } else if (notifyOverride != 2) {
                canAddValue = true;
            }
            if (this.notifyCheck && !canAddValue && (override = this.pushDialogsOverrideMention.get(dialogId2)) != null && override.intValue() != 0) {
                canAddValue = true;
                newCount = override.intValue();
            }
            if (newCount == 0) {
                this.smartNotificationsDialogs.remove(dialogId2);
            }
            if (newCount < 0) {
                if (currentCount == null) {
                    b++;
                } else {
                    newCount += currentCount.intValue();
                }
            }
            if ((canAddValue || newCount == 0) && currentCount != null) {
                this.total_unread_count -= currentCount.intValue();
            }
            if (newCount == 0) {
                this.pushDialogs.remove(dialogId2);
                this.pushDialogsOverrideMention.remove(dialogId2);
                int a = 0;
                while (a < this.pushMessages.size()) {
                    MessageObject messageObject = this.pushMessages.get(a);
                    if (messageObject.messageOwner.from_scheduled || messageObject.getDialogId() != dialogId2) {
                        dialogId = dialogId2;
                    } else {
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount -= i;
                        }
                        this.pushMessages.remove(a);
                        a--;
                        this.delayedPushMessages.remove(messageObject);
                        dialogId = dialogId2;
                        if (messageObject.messageOwner.peer_id.channel_id != 0) {
                            did = -messageObject.messageOwner.peer_id.channel_id;
                        } else {
                            did = 0;
                        }
                        SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(did);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(did);
                            }
                        }
                        popupArrayToRemove.add(messageObject);
                    }
                    i = 1;
                    a++;
                    dialogId2 = dialogId;
                }
            } else if (canAddValue) {
                this.total_unread_count += newCount;
                this.pushDialogs.put(dialogId2, Integer.valueOf(newCount));
            }
            b++;
        }
        if (!popupArrayToRemove.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1104x91a07ef7(popupArrayToRemove);
                }
            });
        }
        if (old_unread_count != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                if (this.lastOnlineFromOtherDevice <= getConnectionsManager().getCurrentTime()) {
                    z = false;
                }
                scheduleNotificationDelay(z);
            }
            final int pushDialogsCount = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1105x890a6821(pushDialogsCount);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* renamed from: lambda$processDialogsUpdateRead$19$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1104x91a07ef7(ArrayList popupArrayToRemove) {
        int size = popupArrayToRemove.size();
        for (int a = 0; a < size; a++) {
            this.popupMessages.remove(popupArrayToRemove.get(a));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* renamed from: lambda$processDialogsUpdateRead$20$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1105x890a6821(int pushDialogsCount) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(pushDialogsCount));
    }

    public void processLoadedUnreadMessages(final LongSparseArray<Integer> dialogs, final ArrayList<TLRPC.Message> messages, final ArrayList<MessageObject> push, ArrayList<TLRPC.User> users, ArrayList<TLRPC.Chat> chats, ArrayList<TLRPC.EncryptedChat> encryptedChats) {
        getMessagesController().putUsers(users, true);
        getMessagesController().putChats(chats, true);
        getMessagesController().putEncryptedChats(encryptedChats, true);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1109xf8b52a58(messages, dialogs, push);
            }
        });
    }

    /* renamed from: lambda$processLoadedUnreadMessages$23$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1109xf8b52a58(ArrayList messages, LongSparseArray dialogs, ArrayList push) {
        SharedPreferences preferences;
        boolean value;
        long did;
        boolean value2;
        boolean value3;
        int a;
        long did2;
        long dialog_id;
        boolean value4;
        ArrayList arrayList = messages;
        ArrayList arrayList2 = push;
        this.pushDialogs.clear();
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        boolean z = false;
        this.total_unread_count = 0;
        this.personalCount = 0;
        SharedPreferences preferences2 = getAccountInstance().getNotificationsSettings();
        LongSparseArray<Boolean> settingsCache = new LongSparseArray<>();
        long j = 0;
        int i = -1;
        int i2 = 1;
        if (arrayList != null) {
            int a2 = 0;
            while (a2 < messages.size()) {
                TLRPC.Message message = (TLRPC.Message) arrayList.get(a2);
                if (message == null) {
                    a = a2;
                } else {
                    if (message.fwd_from == null || !message.fwd_from.imported) {
                        if (message.action instanceof TLRPC.TL_messageActionSetMessagesTTL) {
                            a = a2;
                        } else {
                            if (message.silent) {
                                if (!(message.action instanceof TLRPC.TL_messageActionContactSignUp)) {
                                    if (message.action instanceof TLRPC.TL_messageActionUserJoined) {
                                        a = a2;
                                    }
                                }
                            }
                            if (message.peer_id.channel_id != j) {
                                did2 = -message.peer_id.channel_id;
                            } else {
                                did2 = 0;
                            }
                            SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(did2);
                            if (sparseArray != null && sparseArray.indexOfKey(message.id) >= 0) {
                                a = a2;
                            } else {
                                MessageObject messageObject = new MessageObject(this.currentAccount, message, z, z);
                                if (isPersonalMessage(messageObject)) {
                                    this.personalCount += i2;
                                }
                                long dialog_id2 = messageObject.getDialogId();
                                if (!messageObject.messageOwner.mentioned) {
                                    a = a2;
                                    dialog_id = dialog_id2;
                                } else {
                                    a = a2;
                                    dialog_id = messageObject.getFromChatId();
                                }
                                int index = settingsCache.indexOfKey(dialog_id);
                                if (index >= 0) {
                                    value4 = settingsCache.valueAt(index).booleanValue();
                                } else {
                                    int notifyOverride = getNotifyOverride(preferences2, dialog_id);
                                    if (notifyOverride == i) {
                                        value4 = isGlobalNotificationsEnabled(dialog_id);
                                    } else {
                                        value4 = notifyOverride != 2;
                                    }
                                    settingsCache.put(dialog_id, Boolean.valueOf(value4));
                                }
                                if (value4 && (dialog_id != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                    if (sparseArray == null) {
                                        sparseArray = new SparseArray<>();
                                        this.pushMessagesDict.put(did2, sparseArray);
                                    }
                                    sparseArray.put(message.id, messageObject);
                                    this.pushMessages.add(0, messageObject);
                                    if (dialog_id2 != dialog_id) {
                                        Integer current = this.pushDialogsOverrideMention.get(dialog_id2);
                                        this.pushDialogsOverrideMention.put(dialog_id2, Integer.valueOf(current == null ? 1 : current.intValue() + 1));
                                    }
                                }
                            }
                        }
                    }
                    a = a2;
                }
                a2 = a + 1;
                arrayList = messages;
                z = false;
                j = 0;
                i = -1;
                i2 = 1;
            }
        }
        for (int a3 = 0; a3 < dialogs.size(); a3++) {
            long dialog_id3 = dialogs.keyAt(a3);
            int index2 = settingsCache.indexOfKey(dialog_id3);
            if (index2 >= 0) {
                value2 = settingsCache.valueAt(index2).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(preferences2, dialog_id3);
                if (notifyOverride2 == -1) {
                    value3 = isGlobalNotificationsEnabled(dialog_id3);
                } else {
                    value3 = notifyOverride2 != 2;
                }
                settingsCache.put(dialog_id3, Boolean.valueOf(value3));
                value2 = value3;
            }
            if (value2) {
                int count = ((Integer) dialogs.valueAt(a3)).intValue();
                this.pushDialogs.put(dialog_id3, Integer.valueOf(count));
                this.total_unread_count += count;
            }
        }
        if (arrayList2 != null) {
            int a4 = 0;
            while (a4 < push.size()) {
                MessageObject messageObject2 = (MessageObject) arrayList2.get(a4);
                int mid = messageObject2.getId();
                if (this.pushMessagesDict.indexOfKey(mid) >= 0) {
                    preferences = preferences2;
                } else {
                    if (isPersonalMessage(messageObject2)) {
                        this.personalCount++;
                    }
                    long dialogId = messageObject2.getDialogId();
                    long randomId = messageObject2.messageOwner.random_id;
                    if (messageObject2.messageOwner.mentioned) {
                        dialogId = messageObject2.getFromChatId();
                    }
                    int index3 = settingsCache.indexOfKey(dialogId);
                    if (index3 >= 0) {
                        value = settingsCache.valueAt(index3).booleanValue();
                    } else {
                        int notifyOverride3 = getNotifyOverride(preferences2, dialogId);
                        if (notifyOverride3 == -1) {
                            value = isGlobalNotificationsEnabled(dialogId);
                        } else {
                            value = notifyOverride3 != 2;
                        }
                        settingsCache.put(dialogId, Boolean.valueOf(value));
                    }
                    if (value) {
                        if (dialogId == this.openedDialogId && ApplicationLoader.isScreenOn) {
                            preferences = preferences2;
                        } else {
                            if (mid != 0) {
                                if (messageObject2.messageOwner.peer_id.channel_id != 0) {
                                    did = -messageObject2.messageOwner.peer_id.channel_id;
                                } else {
                                    did = 0;
                                }
                                SparseArray<MessageObject> sparseArray2 = this.pushMessagesDict.get(did);
                                if (sparseArray2 != null) {
                                    preferences = preferences2;
                                } else {
                                    sparseArray2 = new SparseArray<>();
                                    preferences = preferences2;
                                    this.pushMessagesDict.put(did, sparseArray2);
                                }
                                sparseArray2.put(mid, messageObject2);
                            } else {
                                preferences = preferences2;
                                if (randomId != 0) {
                                    this.fcmRandomMessagesDict.put(randomId, messageObject2);
                                }
                            }
                            this.pushMessages.add(0, messageObject2);
                            if (dialogId != dialogId) {
                                Integer current2 = this.pushDialogsOverrideMention.get(dialogId);
                                this.pushDialogsOverrideMention.put(dialogId, Integer.valueOf(current2 == null ? 1 : current2.intValue() + 1));
                            }
                            Integer currentCount = this.pushDialogs.get(dialogId);
                            int newCount = currentCount != null ? currentCount.intValue() + 1 : 1;
                            if (currentCount != null) {
                                this.total_unread_count -= currentCount.intValue();
                            }
                            this.total_unread_count += newCount;
                            this.pushDialogs.put(dialogId, Integer.valueOf(newCount));
                        }
                    } else {
                        preferences = preferences2;
                    }
                }
                a4++;
                arrayList2 = push;
                preferences2 = preferences;
            }
        }
        final int pushDialogsCount = this.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1108xbeea8879(pushDialogsCount);
            }
        });
        showOrUpdateNotification(SystemClock.elapsedRealtime() / 1000 < 60);
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* renamed from: lambda$processLoadedUnreadMessages$22$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1108xbeea8879(int pushDialogsCount) {
        if (this.total_unread_count == 0) {
            this.popupMessages.clear();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(pushDialogsCount));
    }

    private int getTotalAllUnreadCount() {
        int count = 0;
        for (int a = 0; a < 4; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                NotificationsController controller = getInstance(a);
                if (controller.showBadgeNumber) {
                    if (controller.showBadgeMessages) {
                        if (controller.showBadgeMuted) {
                            try {
                                ArrayList<TLRPC.Dialog> dialogs = new ArrayList<>(MessagesController.getInstance(a).allDialogs);
                                int N = dialogs.size();
                                for (int i = 0; i < N; i++) {
                                    TLRPC.Dialog dialog = dialogs.get(i);
                                    if (dialog != null && DialogObject.isChatDialog(dialog.id)) {
                                        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-dialog.id));
                                        if (ChatObject.isNotInChat(chat)) {
                                        }
                                    }
                                    if (dialog != null && dialog.unread_count != 0) {
                                        count += dialog.unread_count;
                                    }
                                }
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else {
                            count += controller.total_unread_count;
                        }
                    } else if (controller.showBadgeMuted) {
                        try {
                            int N2 = MessagesController.getInstance(a).allDialogs.size();
                            for (int i2 = 0; i2 < N2; i2++) {
                                TLRPC.Dialog dialog2 = MessagesController.getInstance(a).allDialogs.get(i2);
                                if (DialogObject.isChatDialog(dialog2.id)) {
                                    TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(-dialog2.id));
                                    if (ChatObject.isNotInChat(chat2)) {
                                    }
                                }
                                if (dialog2.unread_count != 0) {
                                    count++;
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    } else {
                        count += controller.pushDialogs.size();
                    }
                }
            }
        }
        return count;
    }

    /* renamed from: lambda$updateBadge$24$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1126x8d3d4342() {
        setBadge(getTotalAllUnreadCount());
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1126x8d3d4342();
            }
        });
    }

    private void setBadge(int count) {
        if (this.lastBadgeCount == count) {
            return;
        }
        this.lastBadgeCount = count;
        NotificationBadge.applyCount(count);
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getShortStringForMessage(MessageObject messageObject, String[] userName, boolean[] preview) {
        long selfUsedId;
        char c;
        int i;
        char c2;
        char c3;
        int i2;
        char c4;
        char c5;
        int i3;
        char c6;
        char c7;
        char c8;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("NotificationHiddenMessage", org.telegram.messenger.beta.R.string.NotificationHiddenMessage);
        }
        long dialogId = messageObject.messageOwner.dialog_id;
        int i4 = (messageObject.messageOwner.peer_id.chat_id > 0L ? 1 : (messageObject.messageOwner.peer_id.chat_id == 0L ? 0 : -1));
        TLRPC.Peer peer = messageObject.messageOwner.peer_id;
        long chat_id = i4 != 0 ? peer.chat_id : peer.channel_id;
        long fromId = messageObject.messageOwner.peer_id.user_id;
        if (preview != null) {
            preview[0] = true;
        }
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        boolean dialogPreviewEnabled = preferences.getBoolean("content_preview_" + dialogId, true);
        if (messageObject.isFcmMessage()) {
            if (chat_id == 0 && fromId != 0) {
                if (Build.VERSION.SDK_INT > 27) {
                    userName[0] = messageObject.localName;
                }
                if (!dialogPreviewEnabled || !preferences.getBoolean("EnablePreviewAll", true)) {
                    if (preview != null) {
                        preview[0] = false;
                    }
                    return LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                }
            } else if (chat_id != 0) {
                if (messageObject.messageOwner.peer_id.channel_id == 0) {
                    c8 = 0;
                } else if (messageObject.isSupergroup()) {
                    c8 = 0;
                } else {
                    if (Build.VERSION.SDK_INT > 27) {
                        userName[0] = messageObject.localName;
                    }
                    if (dialogPreviewEnabled || ((!messageObject.localChannel && !preferences.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !preferences.getBoolean("EnablePreviewChannel", true)))) {
                        if (preview != null) {
                            preview[0] = false;
                        }
                        if (messageObject.messageOwner.peer_id.channel_id == 0 && !messageObject.isSupergroup()) {
                            return LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, messageObject.localName);
                        }
                        return LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName);
                    }
                }
                userName[c8] = messageObject.localUserName;
                if (dialogPreviewEnabled) {
                }
                if (preview != null) {
                }
                if (messageObject.messageOwner.peer_id.channel_id == 0) {
                }
                return LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName);
            }
            return replaceSpoilers(messageObject);
        }
        long selfUsedId2 = getUserConfig().getClientUserId();
        if (fromId == 0) {
            fromId = messageObject.getFromChatId();
            if (fromId == 0) {
                fromId = -chat_id;
            }
        } else if (fromId == selfUsedId2) {
            fromId = messageObject.getFromChatId();
        }
        if (dialogId == 0) {
            if (chat_id != 0) {
                dialogId = -chat_id;
            } else if (fromId != 0) {
                dialogId = fromId;
            }
        }
        String name = null;
        if (UserObject.isReplyUser(dialogId) && messageObject.messageOwner.fwd_from != null && messageObject.messageOwner.fwd_from.from_id != null) {
            fromId = MessageObject.getPeerId(messageObject.messageOwner.fwd_from.from_id);
        }
        if (fromId > 0) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(fromId));
            if (user != null) {
                String name2 = UserObject.getUserName(user);
                if (chat_id != 0) {
                    userName[0] = name2;
                } else if (Build.VERSION.SDK_INT > 27) {
                    userName[0] = name2;
                } else {
                    userName[0] = null;
                }
                name = name2;
            }
        } else {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-fromId));
            if (chat == null) {
                name = null;
            } else {
                name = chat.title;
                userName[0] = name;
            }
        }
        if (name == null || fromId <= 0 || !UserObject.isReplyUser(dialogId) || messageObject.messageOwner.fwd_from == null || messageObject.messageOwner.fwd_from.saved_from_peer == null) {
            selfUsedId = selfUsedId2;
        } else {
            long id = MessageObject.getPeerId(messageObject.messageOwner.fwd_from.saved_from_peer);
            if (!DialogObject.isChatDialog(id)) {
                selfUsedId = selfUsedId2;
            } else {
                selfUsedId = selfUsedId2;
                TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(-id));
                if (chat2 != null) {
                    name = name + " @ " + chat2.title;
                    if (userName[0] != null) {
                        userName[0] = name;
                    }
                }
            }
        }
        if (name == null) {
            return null;
        }
        TLRPC.Chat chat3 = null;
        if (chat_id != 0) {
            chat3 = getMessagesController().getChat(Long.valueOf(chat_id));
            if (chat3 == null) {
                return null;
            }
            if (ChatObject.isChannel(chat3) && !chat3.megagroup && Build.VERSION.SDK_INT <= 27) {
                userName[0] = null;
            }
        }
        if (DialogObject.isEncryptedDialog(dialogId)) {
            userName[0] = null;
            return LocaleController.getString("NotificationHiddenMessage", org.telegram.messenger.beta.R.string.NotificationHiddenMessage);
        }
        boolean isChannel = ChatObject.isChannel(chat3) && !chat3.megagroup;
        if (dialogPreviewEnabled && ((chat_id == 0 && fromId != 0 && preferences.getBoolean("EnablePreviewAll", true)) || (chat_id != 0 && ((!isChannel && preferences.getBoolean("EnablePreviewGroup", true)) || (isChannel && preferences.getBoolean("EnablePreviewChannel", true)))))) {
            if (!(messageObject.messageOwner instanceof TLRPC.TL_messageService)) {
                if (messageObject.isMediaEmpty()) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return replaceSpoilers(messageObject);
                    }
                    return LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return "🖼 " + replaceSpoilers(messageObject);
                    } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                        return LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.beta.R.string.AttachDestructingPhoto);
                    } else {
                        return LocaleController.getString("AttachPhoto", org.telegram.messenger.beta.R.string.AttachPhoto);
                    }
                } else if (messageObject.isVideo()) {
                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return "📹 " + replaceSpoilers(messageObject);
                    } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                        return LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.beta.R.string.AttachDestructingVideo);
                    } else {
                        return LocaleController.getString("AttachVideo", org.telegram.messenger.beta.R.string.AttachVideo);
                    }
                } else if (messageObject.isGame()) {
                    return LocaleController.getString("AttachGame", org.telegram.messenger.beta.R.string.AttachGame);
                } else {
                    if (messageObject.isVoice()) {
                        return LocaleController.getString("AttachAudio", org.telegram.messenger.beta.R.string.AttachAudio);
                    }
                    if (messageObject.isRoundVideo()) {
                        return LocaleController.getString("AttachRound", org.telegram.messenger.beta.R.string.AttachRound);
                    }
                    if (messageObject.isMusic()) {
                        return LocaleController.getString("AttachMusic", org.telegram.messenger.beta.R.string.AttachMusic);
                    }
                    if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                        return LocaleController.getString("AttachContact", org.telegram.messenger.beta.R.string.AttachContact);
                    }
                    if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                        if (((TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media).poll.quiz) {
                            return LocaleController.getString("QuizPoll", org.telegram.messenger.beta.R.string.QuizPoll);
                        }
                        return LocaleController.getString("Poll", org.telegram.messenger.beta.R.string.Poll);
                    } else if ((messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                        return LocaleController.getString("AttachLocation", org.telegram.messenger.beta.R.string.AttachLocation);
                    } else {
                        if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                            return LocaleController.getString("AttachLiveLocation", org.telegram.messenger.beta.R.string.AttachLiveLocation);
                        }
                        if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String emoji = messageObject.getStickerEmoji();
                                if (emoji != null) {
                                    return emoji + " " + LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                }
                                return LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                            } else if (messageObject.isGif()) {
                                if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    return "🎬 " + replaceSpoilers(messageObject);
                                }
                                return LocaleController.getString("AttachGif", org.telegram.messenger.beta.R.string.AttachGif);
                            } else if (Build.VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                return LocaleController.getString("AttachDocument", org.telegram.messenger.beta.R.string.AttachDocument);
                            } else {
                                return "📎 " + replaceSpoilers(messageObject);
                            }
                        } else if (!TextUtils.isEmpty(messageObject.messageText)) {
                            return replaceSpoilers(messageObject);
                        } else {
                            return LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                        }
                    }
                }
            }
            userName[0] = null;
            if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGeoProximityReached) {
                return messageObject.messageText.toString();
            }
            if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserJoined) && !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionContactSignUp)) {
                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                    return LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.beta.R.string.NotificationContactNewPhoto, name);
                }
                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                    String date = LocaleController.formatString("formatDateAtTime", org.telegram.messenger.beta.R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000));
                    return LocaleController.formatString("NotificationUnrecognizedDevice", org.telegram.messenger.beta.R.string.NotificationUnrecognizedDevice, getUserConfig().getCurrentUser().first_name, date, messageObject.messageOwner.action.title, messageObject.messageOwner.action.address);
                }
                if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGameScore) && !(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPaymentSent)) {
                    if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPhoneCall) {
                        if (messageObject.messageOwner.action.video) {
                            return LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageVideoIncomingMissed);
                        }
                        return LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageIncomingMissed);
                    } else if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatAddUser)) {
                        if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGroupCall) {
                            return LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.beta.R.string.NotificationGroupCreatedCall, name, chat3.title);
                        }
                        if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGroupCallScheduled) {
                            return messageObject.messageText.toString();
                        }
                        if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionInviteToGroupCall)) {
                            if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                                return LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.beta.R.string.NotificationInvitedToGroupByLink, name, chat3.title);
                            }
                            if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatEditTitle) {
                                return LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.beta.R.string.NotificationEditedGroupName, name, messageObject.messageOwner.action.title);
                            }
                            if ((messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                                return (messageObject.messageOwner.peer_id.channel_id == 0 || chat3.megagroup) ? messageObject.isVideoAvatar() ? LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.beta.R.string.NotificationEditedGroupVideo, name, chat3.title) : LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.beta.R.string.NotificationEditedGroupPhoto, name, chat3.title) : messageObject.isVideoAvatar() ? LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.beta.R.string.ChannelVideoEditNotification, chat3.title) : LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.beta.R.string.ChannelPhotoEditNotification, chat3.title);
                            } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                                if (messageObject.messageOwner.action.user_id == selfUsedId) {
                                    return LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.beta.R.string.NotificationGroupKickYou, name, chat3.title);
                                }
                                if (messageObject.messageOwner.action.user_id != fromId) {
                                    TLRPC.User u2 = getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.action.user_id));
                                    if (u2 == null) {
                                        return null;
                                    }
                                    return LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.beta.R.string.NotificationGroupKickMember, name, chat3.title, UserObject.getUserName(u2));
                                }
                                return LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.beta.R.string.NotificationGroupLeftMember, name, chat3.title);
                            } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatCreate) {
                                return messageObject.messageText.toString();
                            } else {
                                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChannelCreate) {
                                    return messageObject.messageText.toString();
                                }
                                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                    return LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, chat3.title);
                                }
                                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                    return LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, messageObject.messageOwner.action.title);
                                }
                                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                    return messageObject.messageText.toString();
                                }
                                if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage)) {
                                    if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionSetChatTheme) {
                                        String emoticon = ((TLRPC.TL_messageActionSetChatTheme) messageObject.messageOwner.action).emoticon;
                                        if (TextUtils.isEmpty(emoticon)) {
                                            if (dialogId == selfUsedId) {
                                                String msg = LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new Object[0]);
                                                return msg;
                                            }
                                            String msg2 = LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, name, emoticon);
                                            return msg2;
                                        } else if (dialogId == selfUsedId) {
                                            String msg3 = LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, emoticon);
                                            return msg3;
                                        } else {
                                            String msg4 = LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, name, emoticon);
                                            return msg4;
                                        }
                                    } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest) {
                                        return messageObject.messageText.toString();
                                    } else {
                                        return null;
                                    }
                                } else if (chat3 != null && (!ChatObject.isChannel(chat3) || chat3.megagroup)) {
                                    if (messageObject.replyMessageObject == null) {
                                        return LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, name, chat3.title);
                                    }
                                    MessageObject object = messageObject.replyMessageObject;
                                    if (object.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusic, name, chat3.title);
                                    }
                                    if (object.isVideo()) {
                                        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "📹 " + object.messageOwner.message, chat3.title);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideo, name, chat3.title);
                                    } else if (object.isGif()) {
                                        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "🎬 " + object.messageOwner.message, chat3.title);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.beta.R.string.NotificationActionPinnedGif, name, chat3.title);
                                    } else if (object.isVoice()) {
                                        return LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoice, name, chat3.title);
                                    } else {
                                        if (object.isRoundVideo()) {
                                            return LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.beta.R.string.NotificationActionPinnedRound, name, chat3.title);
                                        }
                                        if (object.isSticker() || object.isAnimatedSticker()) {
                                            String emoji2 = object.getStickerEmoji();
                                            return emoji2 != null ? LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmoji, name, chat3.title, emoji2) : LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.beta.R.string.NotificationActionPinnedSticker, name, chat3.title);
                                        } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                            if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                                return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "📎 " + object.messageOwner.message, chat3.title);
                                            }
                                            return LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.beta.R.string.NotificationActionPinnedFile, name, chat3.title);
                                        } else {
                                            if (object.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) {
                                                c6 = 0;
                                                i3 = 2;
                                                c5 = 1;
                                            } else if (!(object.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                if (object.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLive, name, chat3.title);
                                                }
                                                if (object.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                                                    TLRPC.TL_messageMediaContact mediaContact = (TLRPC.TL_messageMediaContact) object.messageOwner.media;
                                                    return LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContact2, name, chat3.title, ContactsController.formatName(mediaContact.first_name, mediaContact.last_name));
                                                } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                    TLRPC.TL_messageMediaPoll mediaPoll = (TLRPC.TL_messageMediaPoll) object.messageOwner.media;
                                                    if (mediaPoll.poll.quiz) {
                                                        return LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuiz2, name, chat3.title, mediaPoll.poll.question);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPoll2, name, chat3.title, mediaPoll.poll.question);
                                                } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "🖼 " + object.messageOwner.message, chat3.title);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhoto, name, chat3.title);
                                                } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                    return LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.beta.R.string.NotificationActionPinnedGame, name, chat3.title);
                                                } else {
                                                    if (object.messageText != null && object.messageText.length() > 0) {
                                                        CharSequence message = object.messageText;
                                                        if (message.length() <= 20) {
                                                            c7 = 0;
                                                        } else {
                                                            StringBuilder sb = new StringBuilder();
                                                            c7 = 0;
                                                            sb.append((Object) message.subSequence(0, 20));
                                                            sb.append("...");
                                                            message = sb.toString();
                                                        }
                                                        Object[] objArr = new Object[3];
                                                        objArr[c7] = name;
                                                        objArr[1] = message;
                                                        objArr[2] = chat3.title;
                                                        return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, objArr);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, name, chat3.title);
                                                }
                                            } else {
                                                c6 = 0;
                                                i3 = 2;
                                                c5 = 1;
                                            }
                                            Object[] objArr2 = new Object[i3];
                                            objArr2[c6] = name;
                                            objArr2[c5] = chat3.title;
                                            return LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeo, objArr2);
                                        }
                                    }
                                } else if (chat3 == null) {
                                    if (messageObject.replyMessageObject == null) {
                                        return LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextUser, name);
                                    }
                                    MessageObject object2 = messageObject.replyMessageObject;
                                    if (object2.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusicUser, name);
                                    }
                                    if (object2.isVideo()) {
                                        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, name, "📹 " + object2.messageOwner.message);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoUser, name);
                                    } else if (object2.isGif()) {
                                        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, name, "🎬 " + object2.messageOwner.message);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifUser, name);
                                    } else if (object2.isVoice()) {
                                        return LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceUser, name);
                                    } else {
                                        if (object2.isRoundVideo()) {
                                            return LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundUser, name);
                                        }
                                        if (object2.isSticker() || object2.isAnimatedSticker()) {
                                            String emoji3 = object2.getStickerEmoji();
                                            return emoji3 != null ? LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiUser, name, emoji3) : LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerUser, name);
                                        } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                            if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                                return LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, name, "📎 " + object2.messageOwner.message);
                                            }
                                            return LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileUser, name);
                                        } else {
                                            if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) {
                                                i = 1;
                                                c = 0;
                                            } else if (!(object2.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveUser, name);
                                                }
                                                if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                                                    TLRPC.TL_messageMediaContact mediaContact2 = (TLRPC.TL_messageMediaContact) object2.messageOwner.media;
                                                    return LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactUser, name, ContactsController.formatName(mediaContact2.first_name, mediaContact2.last_name));
                                                } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                    TLRPC.TL_messageMediaPoll mediaPoll2 = (TLRPC.TL_messageMediaPoll) object2.messageOwner.media;
                                                    return mediaPoll2.poll.quiz ? LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizUser, name, mediaPoll2.poll.question) : LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollUser, name, mediaPoll2.poll.question);
                                                } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, name, "🖼 " + object2.messageOwner.message);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoUser, name);
                                                } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                    return LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameUser, name);
                                                } else {
                                                    if (object2.messageText != null && object2.messageText.length() > 0) {
                                                        CharSequence message2 = object2.messageText;
                                                        if (message2.length() <= 20) {
                                                            c2 = 0;
                                                        } else {
                                                            StringBuilder sb2 = new StringBuilder();
                                                            c2 = 0;
                                                            sb2.append((Object) message2.subSequence(0, 20));
                                                            sb2.append("...");
                                                            message2 = sb2.toString();
                                                        }
                                                        Object[] objArr3 = new Object[2];
                                                        objArr3[c2] = name;
                                                        objArr3[1] = message2;
                                                        return LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, objArr3);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextUser, name);
                                                }
                                            } else {
                                                i = 1;
                                                c = 0;
                                            }
                                            Object[] objArr4 = new Object[i];
                                            objArr4[c] = name;
                                            return LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoUser, objArr4);
                                        }
                                    }
                                } else if (messageObject.replyMessageObject == null) {
                                    return LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, chat3.title);
                                } else {
                                    MessageObject object3 = messageObject.replyMessageObject;
                                    if (object3.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusicChannel, chat3.title);
                                    }
                                    if (object3.isVideo()) {
                                        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object3.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat3.title, "📹 " + object3.messageOwner.message);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoChannel, chat3.title);
                                    } else if (object3.isGif()) {
                                        if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object3.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat3.title, "🎬 " + object3.messageOwner.message);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifChannel, chat3.title);
                                    } else if (object3.isVoice()) {
                                        return LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceChannel, chat3.title);
                                    } else {
                                        if (object3.isRoundVideo()) {
                                            return LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundChannel, chat3.title);
                                        }
                                        if (object3.isSticker() || object3.isAnimatedSticker()) {
                                            String emoji4 = object3.getStickerEmoji();
                                            return emoji4 != null ? LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiChannel, chat3.title, emoji4) : LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerChannel, chat3.title);
                                        } else if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                            if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object3.messageOwner.message)) {
                                                return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat3.title, "📎 " + object3.messageOwner.message);
                                            }
                                            return LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileChannel, chat3.title);
                                        } else {
                                            if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) {
                                                i2 = 1;
                                                c3 = 0;
                                            } else if (!(object3.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveChannel, chat3.title);
                                                }
                                                if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                                                    TLRPC.TL_messageMediaContact mediaContact3 = (TLRPC.TL_messageMediaContact) object3.messageOwner.media;
                                                    return LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactChannel2, chat3.title, ContactsController.formatName(mediaContact3.first_name, mediaContact3.last_name));
                                                } else if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                    TLRPC.TL_messageMediaPoll mediaPoll3 = (TLRPC.TL_messageMediaPoll) object3.messageOwner.media;
                                                    return mediaPoll3.poll.quiz ? LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizChannel2, chat3.title, mediaPoll3.poll.question) : LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollChannel2, chat3.title, mediaPoll3.poll.question);
                                                } else if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object3.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat3.title, "🖼 " + object3.messageOwner.message);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoChannel, chat3.title);
                                                } else if (object3.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                    return LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameChannel, chat3.title);
                                                } else {
                                                    if (object3.messageText != null && object3.messageText.length() > 0) {
                                                        CharSequence message3 = object3.messageText;
                                                        if (message3.length() <= 20) {
                                                            c4 = 0;
                                                        } else {
                                                            StringBuilder sb3 = new StringBuilder();
                                                            c4 = 0;
                                                            sb3.append((Object) message3.subSequence(0, 20));
                                                            sb3.append("...");
                                                            message3 = sb3.toString();
                                                        }
                                                        Object[] objArr5 = new Object[2];
                                                        objArr5[c4] = chat3.title;
                                                        objArr5[1] = message3;
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, objArr5);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, chat3.title);
                                                }
                                            } else {
                                                i2 = 1;
                                                c3 = 0;
                                            }
                                            Object[] objArr6 = new Object[i2];
                                            objArr6[c3] = chat3.title;
                                            return LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoChannel, objArr6);
                                        }
                                    }
                                }
                            }
                        }
                        long singleUserId = messageObject.messageOwner.action.user_id;
                        if (singleUserId == 0 && messageObject.messageOwner.action.users.size() == 1) {
                            singleUserId = messageObject.messageOwner.action.users.get(0).longValue();
                        }
                        if (singleUserId != 0) {
                            if (singleUserId == selfUsedId) {
                                return LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedYouToCall, name, chat3.title);
                            }
                            TLRPC.User u22 = getMessagesController().getUser(Long.valueOf(singleUserId));
                            if (u22 == null) {
                                return null;
                            }
                            return LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, name, chat3.title, UserObject.getUserName(u22));
                        }
                        StringBuilder names = new StringBuilder();
                        for (int a = 0; a < messageObject.messageOwner.action.users.size(); a++) {
                            TLRPC.User user2 = getMessagesController().getUser(messageObject.messageOwner.action.users.get(a));
                            if (user2 != null) {
                                String name22 = UserObject.getUserName(user2);
                                if (names.length() != 0) {
                                    names.append(", ");
                                }
                                names.append(name22);
                            }
                        }
                        return LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, name, chat3.title, names.toString());
                    } else {
                        long singleUserId2 = messageObject.messageOwner.action.user_id;
                        if (singleUserId2 == 0 && messageObject.messageOwner.action.users.size() == 1) {
                            singleUserId2 = messageObject.messageOwner.action.users.get(0).longValue();
                        }
                        if (singleUserId2 != 0) {
                            if (messageObject.messageOwner.peer_id.channel_id != 0 && !chat3.megagroup) {
                                return LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.beta.R.string.ChannelAddedByNotification, name, chat3.title);
                            }
                            if (singleUserId2 == selfUsedId) {
                                return LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.beta.R.string.NotificationInvitedToGroup, name, chat3.title);
                            }
                            TLRPC.User u23 = getMessagesController().getUser(Long.valueOf(singleUserId2));
                            if (u23 == null) {
                                return null;
                            }
                            return fromId == u23.id ? chat3.megagroup ? LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.beta.R.string.NotificationGroupAddSelfMega, name, chat3.title) : LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.beta.R.string.NotificationGroupAddSelf, name, chat3.title) : LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, name, chat3.title, UserObject.getUserName(u23));
                        }
                        StringBuilder names2 = new StringBuilder();
                        for (int a2 = 0; a2 < messageObject.messageOwner.action.users.size(); a2++) {
                            TLRPC.User user3 = getMessagesController().getUser(messageObject.messageOwner.action.users.get(a2));
                            if (user3 != null) {
                                String name23 = UserObject.getUserName(user3);
                                if (names2.length() != 0) {
                                    names2.append(", ");
                                }
                                names2.append(name23);
                            }
                        }
                        return LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, name, chat3.title, names2.toString());
                    }
                }
                return messageObject.messageText.toString();
            }
            return LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.beta.R.string.NotificationContactJoined, name);
        }
        if (preview != null) {
            preview[0] = false;
        }
        return LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
    }

    private String replaceSpoilers(MessageObject messageObject) {
        String text = messageObject.messageOwner.message;
        if (text == null || messageObject == null || messageObject.messageOwner == null || messageObject.messageOwner.entities == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
            if (messageObject.messageOwner.entities.get(i) instanceof TLRPC.TL_messageEntitySpoiler) {
                TLRPC.TL_messageEntitySpoiler spoiler = (TLRPC.TL_messageEntitySpoiler) messageObject.messageOwner.entities.get(i);
                for (int j = 0; j < spoiler.length; j++) {
                    char[] cArr = this.spoilerChars;
                    stringBuilder.setCharAt(spoiler.offset + j, cArr[j % cArr.length]);
                }
            }
        }
        return stringBuilder.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:258:0x07cc, code lost:
        if (r11.getBoolean("EnablePreviewGroup", true) != false) goto L263;
     */
    /* JADX WARN: Removed duplicated region for block: B:256:0x07c1  */
    /* JADX WARN: Removed duplicated region for block: B:758:0x18dd  */
    /* JADX WARN: Removed duplicated region for block: B:760:0x18e5  */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean shortMessage, boolean[] text, boolean[] preview) {
        String str;
        TLRPC.Chat chat;
        boolean isChannel;
        String str2;
        int i;
        char c;
        char c2;
        int i2;
        char c3;
        int i3;
        char c4;
        char c5;
        char c6;
        CharSequence message;
        String msg;
        char c7;
        int i4;
        char c8;
        CharSequence message2;
        String msg2;
        boolean z;
        SharedPreferences preferences;
        int i5;
        char c9;
        char c10;
        boolean z2;
        String msg3;
        char c11;
        char c12;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", org.telegram.messenger.beta.R.string.YouHaveNewMessage);
        }
        long dialogId = messageObject.messageOwner.dialog_id;
        int i6 = (messageObject.messageOwner.peer_id.chat_id > 0L ? 1 : (messageObject.messageOwner.peer_id.chat_id == 0L ? 0 : -1));
        TLRPC.Peer peer = messageObject.messageOwner.peer_id;
        long chatId = i6 != 0 ? peer.chat_id : peer.channel_id;
        long fromId = messageObject.messageOwner.peer_id.user_id;
        if (preview != 0) {
            preview[0] = true;
        }
        SharedPreferences preferences2 = getAccountInstance().getNotificationsSettings();
        boolean dialogPreviewEnabled = preferences2.getBoolean("content_preview_" + dialogId, true);
        if (messageObject.isFcmMessage()) {
            if (chatId == 0 && fromId != 0) {
                if (!dialogPreviewEnabled || !preferences2.getBoolean("EnablePreviewAll", true)) {
                    if (preview == 0) {
                        c12 = 0;
                    } else {
                        c12 = 0;
                        preview[0] = false;
                    }
                    Object[] objArr = new Object[1];
                    objArr[c12] = messageObject.localName;
                    return LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, objArr);
                }
                c11 = 0;
            } else if (chatId == 0) {
                c11 = 0;
            } else if (!dialogPreviewEnabled || ((!messageObject.localChannel && !preferences2.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !preferences2.getBoolean("EnablePreviewChannel", true)))) {
                if (preview != 0) {
                    preview[0] = false;
                }
                if (messageObject.messageOwner.peer_id.channel_id != 0 && !messageObject.isSupergroup()) {
                    return LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, messageObject.localName);
                }
                return LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName);
            } else {
                c11 = 0;
            }
            text[c11] = true;
            return (String) messageObject.messageText;
        }
        long selfUsedId = getUserConfig().getClientUserId();
        if (fromId == 0) {
            fromId = messageObject.getFromChatId();
            if (fromId == 0) {
                fromId = -chatId;
            }
        } else if (fromId == selfUsedId) {
            fromId = messageObject.getFromChatId();
        }
        if (dialogId == 0) {
            if (chatId != 0) {
                dialogId = -chatId;
            } else if (fromId != 0) {
                dialogId = fromId;
            }
        }
        String name = null;
        if (fromId > 0) {
            if (messageObject.messageOwner.from_scheduled) {
                if (dialogId == selfUsedId) {
                    name = LocaleController.getString("MessageScheduledReminderNotification", org.telegram.messenger.beta.R.string.MessageScheduledReminderNotification);
                    str = "NotificationMessageGroupNoText";
                } else {
                    name = LocaleController.getString("NotificationMessageScheduledName", org.telegram.messenger.beta.R.string.NotificationMessageScheduledName);
                    str = "NotificationMessageGroupNoText";
                }
            } else {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(fromId));
                if (user != null) {
                    name = UserObject.getUserName(user);
                }
                str = "NotificationMessageGroupNoText";
            }
        } else {
            str = "NotificationMessageGroupNoText";
            TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(-fromId));
            if (chat2 == null) {
                name = null;
            } else {
                name = chat2.title;
            }
        }
        if (name == null) {
            return null;
        }
        TLRPC.Chat chat3 = null;
        if (chatId != 0 && (chat3 = getMessagesController().getChat(Long.valueOf(chatId))) == null) {
            return null;
        }
        if (!DialogObject.isEncryptedDialog(dialogId)) {
            String str3 = str;
            TLRPC.Chat chat4 = chat3;
            if (chatId != 0 || fromId == 0) {
                long fromId2 = fromId;
                if (chatId != 0) {
                    if (ChatObject.isChannel(chat4)) {
                        chat = chat4;
                        if (!chat.megagroup) {
                            isChannel = true;
                            if (!dialogPreviewEnabled) {
                                if (isChannel) {
                                }
                                if (!isChannel || !preferences2.getBoolean("EnablePreviewChannel", true)) {
                                    str2 = str3;
                                }
                                if (!(messageObject.messageOwner instanceof TLRPC.TL_messageService)) {
                                    if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                        if (messageObject.isMediaEmpty()) {
                                            if (!shortMessage && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                String msg4 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, messageObject.messageOwner.message);
                                                text[0] = true;
                                                return msg4;
                                            }
                                            String msg5 = LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, name);
                                            return msg5;
                                        } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                            if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                String msg6 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "🖼 " + messageObject.messageOwner.message);
                                                text[0] = true;
                                                return msg6;
                                            }
                                            String msg7 = LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.beta.R.string.ChannelMessagePhoto, name);
                                            return msg7;
                                        } else if (messageObject.isVideo()) {
                                            if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                String msg8 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "📹 " + messageObject.messageOwner.message);
                                                text[0] = true;
                                                return msg8;
                                            }
                                            String msg9 = LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.beta.R.string.ChannelMessageVideo, name);
                                            return msg9;
                                        } else if (messageObject.isVoice()) {
                                            String msg10 = LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.beta.R.string.ChannelMessageAudio, name);
                                            return msg10;
                                        } else if (messageObject.isRoundVideo()) {
                                            String msg11 = LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.beta.R.string.ChannelMessageRound, name);
                                            return msg11;
                                        } else if (messageObject.isMusic()) {
                                            String msg12 = LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.beta.R.string.ChannelMessageMusic, name);
                                            return msg12;
                                        } else if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaContact)) {
                                            if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                                                if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeo)) {
                                                    if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                        if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                            String msg13 = LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.beta.R.string.ChannelMessageLiveLocation, name);
                                                            return msg13;
                                                        } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                                                String emoji = messageObject.getStickerEmoji();
                                                                if (emoji != null) {
                                                                    String msg14 = LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.beta.R.string.ChannelMessageStickerEmoji, name, emoji);
                                                                    return msg14;
                                                                }
                                                                String msg15 = LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.beta.R.string.ChannelMessageSticker, name);
                                                                return msg15;
                                                            } else if (messageObject.isGif()) {
                                                                if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                                    String msg16 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "🎬 " + messageObject.messageOwner.message);
                                                                    text[0] = true;
                                                                    return msg16;
                                                                }
                                                                String msg17 = LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.beta.R.string.ChannelMessageGIF, name);
                                                                return msg17;
                                                            } else if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                                String msg18 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "📎 " + messageObject.messageOwner.message);
                                                                text[0] = true;
                                                                return msg18;
                                                            } else {
                                                                String msg19 = LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.beta.R.string.ChannelMessageDocument, name);
                                                                return msg19;
                                                            }
                                                        } else if (!shortMessage && !TextUtils.isEmpty(messageObject.messageText)) {
                                                            String msg20 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, messageObject.messageText);
                                                            text[0] = true;
                                                            return msg20;
                                                        } else {
                                                            String msg21 = LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, name);
                                                            return msg21;
                                                        }
                                                    }
                                                    c3 = 0;
                                                    i2 = 1;
                                                } else {
                                                    c3 = 0;
                                                    i2 = 1;
                                                }
                                                Object[] objArr2 = new Object[i2];
                                                objArr2[c3] = name;
                                                String msg22 = LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.beta.R.string.ChannelMessageMap, objArr2);
                                                return msg22;
                                            }
                                            TLRPC.TL_messageMediaPoll mediaPoll = (TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media;
                                            if (mediaPoll.poll.quiz) {
                                                String msg23 = LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.beta.R.string.ChannelMessageQuiz2, name, mediaPoll.poll.question);
                                                return msg23;
                                            }
                                            String msg24 = LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.beta.R.string.ChannelMessagePoll2, name, mediaPoll.poll.question);
                                            return msg24;
                                        } else {
                                            TLRPC.TL_messageMediaContact mediaContact = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                                            String msg25 = LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.beta.R.string.ChannelMessageContact2, name, ContactsController.formatName(mediaContact.first_name, mediaContact.last_name));
                                            return msg25;
                                        }
                                    } else if (!messageObject.isMediaEmpty()) {
                                        if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                            if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                String msg26 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, name, chat.title, "🖼 " + messageObject.messageOwner.message);
                                                return msg26;
                                            }
                                            String msg27 = LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.beta.R.string.NotificationMessageGroupPhoto, name, chat.title);
                                            return msg27;
                                        } else if (messageObject.isVideo()) {
                                            if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                String msg28 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, name, chat.title, "📹 " + messageObject.messageOwner.message);
                                                return msg28;
                                            }
                                            String msg29 = LocaleController.formatString(" ", org.telegram.messenger.beta.R.string.NotificationMessageGroupVideo, name, chat.title);
                                            return msg29;
                                        } else if (messageObject.isVoice()) {
                                            String msg30 = LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.beta.R.string.NotificationMessageGroupAudio, name, chat.title);
                                            return msg30;
                                        } else if (messageObject.isRoundVideo()) {
                                            String msg31 = LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.beta.R.string.NotificationMessageGroupRound, name, chat.title);
                                            return msg31;
                                        } else if (messageObject.isMusic()) {
                                            String msg32 = LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.beta.R.string.NotificationMessageGroupMusic, name, chat.title);
                                            return msg32;
                                        } else if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaContact)) {
                                            if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                                                if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                    String msg33 = LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.beta.R.string.NotificationMessageGroupGame, name, chat.title, messageObject.messageOwner.media.game.title);
                                                    return msg33;
                                                }
                                                if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeo)) {
                                                    if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                        if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                            String msg34 = LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.beta.R.string.NotificationMessageGroupLiveLocation, name, chat.title);
                                                            return msg34;
                                                        } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                                                String emoji2 = messageObject.getStickerEmoji();
                                                                if (emoji2 != null) {
                                                                    String msg35 = LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.beta.R.string.NotificationMessageGroupStickerEmoji, name, chat.title, emoji2);
                                                                    return msg35;
                                                                }
                                                                String msg36 = LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.beta.R.string.NotificationMessageGroupSticker, name, chat.title);
                                                                return msg36;
                                                            } else if (messageObject.isGif()) {
                                                                if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                                    String msg37 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, name, chat.title, "🎬 " + messageObject.messageOwner.message);
                                                                    return msg37;
                                                                }
                                                                String msg38 = LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.beta.R.string.NotificationMessageGroupGif, name, chat.title);
                                                                return msg38;
                                                            } else if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                                String msg39 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, name, chat.title, "📎 " + messageObject.messageOwner.message);
                                                                return msg39;
                                                            } else {
                                                                String msg40 = LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.beta.R.string.NotificationMessageGroupDocument, name, chat.title);
                                                                return msg40;
                                                            }
                                                        } else if (shortMessage || TextUtils.isEmpty(messageObject.messageText)) {
                                                            String msg41 = LocaleController.formatString(str3, org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, name, chat.title);
                                                            return msg41;
                                                        } else {
                                                            String msg42 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, name, chat.title, messageObject.messageText);
                                                            return msg42;
                                                        }
                                                    }
                                                    c2 = 0;
                                                    c = 1;
                                                    i = 2;
                                                } else {
                                                    c2 = 0;
                                                    c = 1;
                                                    i = 2;
                                                }
                                                Object[] objArr3 = new Object[i];
                                                objArr3[c2] = name;
                                                objArr3[c] = chat.title;
                                                String msg43 = LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.beta.R.string.NotificationMessageGroupMap, objArr3);
                                                return msg43;
                                            }
                                            TLRPC.TL_messageMediaPoll mediaPoll2 = (TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media;
                                            if (mediaPoll2.poll.quiz) {
                                                String msg44 = LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.beta.R.string.NotificationMessageGroupQuiz2, name, chat.title, mediaPoll2.poll.question);
                                                return msg44;
                                            }
                                            String msg45 = LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.beta.R.string.NotificationMessageGroupPoll2, name, chat.title, mediaPoll2.poll.question);
                                            return msg45;
                                        } else {
                                            TLRPC.TL_messageMediaContact mediaContact2 = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                                            String msg46 = LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.beta.R.string.NotificationMessageGroupContact2, name, chat.title, ContactsController.formatName(mediaContact2.first_name, mediaContact2.last_name));
                                            return msg46;
                                        }
                                    } else if (shortMessage || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                        String msg47 = LocaleController.formatString(str3, org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, name, chat.title);
                                        return msg47;
                                    } else {
                                        String msg48 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, name, chat.title, messageObject.messageOwner.message);
                                        return msg48;
                                    }
                                } else if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatAddUser)) {
                                    if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGroupCall) {
                                        String msg49 = LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.beta.R.string.NotificationGroupCreatedCall, name, chat.title);
                                        return msg49;
                                    } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGroupCallScheduled) {
                                        String msg50 = messageObject.messageText.toString();
                                        return msg50;
                                    } else if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionInviteToGroupCall)) {
                                        if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                                            String msg51 = LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.beta.R.string.NotificationInvitedToGroupByLink, name, chat.title);
                                            return msg51;
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatEditTitle) {
                                            String msg52 = LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.beta.R.string.NotificationEditedGroupName, name, messageObject.messageOwner.action.title);
                                            return msg52;
                                        } else if ((messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                                            if (messageObject.messageOwner.peer_id.channel_id != 0 && !chat.megagroup) {
                                                if (messageObject.isVideoAvatar()) {
                                                    String msg53 = LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.beta.R.string.ChannelVideoEditNotification, chat.title);
                                                    return msg53;
                                                }
                                                String msg54 = LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.beta.R.string.ChannelPhotoEditNotification, chat.title);
                                                return msg54;
                                            } else if (messageObject.isVideoAvatar()) {
                                                String msg55 = LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.beta.R.string.NotificationEditedGroupVideo, name, chat.title);
                                                return msg55;
                                            } else {
                                                String msg56 = LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.beta.R.string.NotificationEditedGroupPhoto, name, chat.title);
                                                return msg56;
                                            }
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                                            if (messageObject.messageOwner.action.user_id == selfUsedId) {
                                                String msg57 = LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.beta.R.string.NotificationGroupKickYou, name, chat.title);
                                                return msg57;
                                            } else if (messageObject.messageOwner.action.user_id != fromId2) {
                                                TLRPC.User u2 = getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.action.user_id));
                                                if (u2 == null) {
                                                    return null;
                                                }
                                                String msg58 = LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.beta.R.string.NotificationGroupKickMember, name, chat.title, UserObject.getUserName(u2));
                                                return msg58;
                                            } else {
                                                String msg59 = LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.beta.R.string.NotificationGroupLeftMember, name, chat.title);
                                                return msg59;
                                            }
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatCreate) {
                                            String msg60 = messageObject.messageText.toString();
                                            return msg60;
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChannelCreate) {
                                            String msg61 = messageObject.messageText.toString();
                                            return msg61;
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                            String msg62 = LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, chat.title);
                                            return msg62;
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                            String msg63 = LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, messageObject.messageOwner.action.title);
                                            return msg63;
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                            String msg64 = messageObject.messageText.toString();
                                            return msg64;
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage) {
                                            if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                                if (messageObject.replyMessageObject == null) {
                                                    String msg65 = LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, name, chat.title);
                                                    return msg65;
                                                }
                                                MessageObject object = messageObject.replyMessageObject;
                                                if (object.isMusic()) {
                                                    String msg66 = LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusic, name, chat.title);
                                                    return msg66;
                                                } else if (object.isVideo()) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "📹 " + object.messageOwner.message, chat.title);
                                                    }
                                                    String msg67 = LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideo, name, chat.title);
                                                    return msg67;
                                                } else if (object.isGif()) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "🎬 " + object.messageOwner.message, chat.title);
                                                    }
                                                    String msg68 = LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.beta.R.string.NotificationActionPinnedGif, name, chat.title);
                                                    return msg68;
                                                } else if (object.isVoice()) {
                                                    String msg69 = LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoice, name, chat.title);
                                                    return msg69;
                                                } else if (object.isRoundVideo()) {
                                                    String msg70 = LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.beta.R.string.NotificationActionPinnedRound, name, chat.title);
                                                    return msg70;
                                                } else if (object.isSticker() || object.isAnimatedSticker()) {
                                                    String emoji3 = object.getStickerEmoji();
                                                    String msg71 = emoji3 != null ? LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmoji, name, chat.title, emoji3) : LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.beta.R.string.NotificationActionPinnedSticker, name, chat.title);
                                                    return msg71;
                                                } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "📎 " + object.messageOwner.message, chat.title);
                                                    }
                                                    String msg72 = LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.beta.R.string.NotificationActionPinnedFile, name, chat.title);
                                                    return msg72;
                                                } else {
                                                    if (object.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) {
                                                        c5 = 0;
                                                        c4 = 1;
                                                        i3 = 2;
                                                    } else if (!(object.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                        if (object.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                            String msg73 = LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLive, name, chat.title);
                                                            return msg73;
                                                        } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                                                            TLRPC.TL_messageMediaContact mediaContact3 = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                                                            String msg74 = LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContact2, name, chat.title, ContactsController.formatName(mediaContact3.first_name, mediaContact3.last_name));
                                                            return msg74;
                                                        } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                            TLRPC.TL_messageMediaPoll mediaPoll3 = (TLRPC.TL_messageMediaPoll) object.messageOwner.media;
                                                            if (mediaPoll3.poll.quiz) {
                                                                msg = LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuiz2, name, chat.title, mediaPoll3.poll.question);
                                                            } else {
                                                                msg = LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPoll2, name, chat.title, mediaPoll3.poll.question);
                                                            }
                                                            return msg;
                                                        } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                                            if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object.messageOwner.message)) {
                                                                return LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, name, "🖼 " + object.messageOwner.message, chat.title);
                                                            }
                                                            String msg75 = LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhoto, name, chat.title);
                                                            return msg75;
                                                        } else if (object.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                            String msg76 = LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.beta.R.string.NotificationActionPinnedGame, name, chat.title);
                                                            return msg76;
                                                        } else if (object.messageText != null && object.messageText.length() > 0) {
                                                            CharSequence message3 = object.messageText;
                                                            if (message3.length() <= 20) {
                                                                c6 = 0;
                                                                message = message3;
                                                            } else {
                                                                StringBuilder sb = new StringBuilder();
                                                                c6 = 0;
                                                                sb.append((Object) message3.subSequence(0, 20));
                                                                sb.append("...");
                                                                message = sb.toString();
                                                            }
                                                            Object[] objArr4 = new Object[3];
                                                            objArr4[c6] = name;
                                                            objArr4[1] = message;
                                                            objArr4[2] = chat.title;
                                                            String msg77 = LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, objArr4);
                                                            return msg77;
                                                        } else {
                                                            String msg78 = LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, name, chat.title);
                                                            return msg78;
                                                        }
                                                    } else {
                                                        c5 = 0;
                                                        c4 = 1;
                                                        i3 = 2;
                                                    }
                                                    Object[] objArr5 = new Object[i3];
                                                    objArr5[c5] = name;
                                                    objArr5[c4] = chat.title;
                                                    String msg79 = LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeo, objArr5);
                                                    return msg79;
                                                }
                                            } else if (messageObject.replyMessageObject == null) {
                                                String msg80 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, chat.title);
                                                return msg80;
                                            } else {
                                                MessageObject object2 = messageObject.replyMessageObject;
                                                if (object2.isMusic()) {
                                                    String msg81 = LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusicChannel, chat.title);
                                                    return msg81;
                                                } else if (object2.isVideo()) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat.title, "📹 " + object2.messageOwner.message);
                                                    }
                                                    String msg82 = LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoChannel, chat.title);
                                                    return msg82;
                                                } else if (object2.isGif()) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat.title, "🎬 " + object2.messageOwner.message);
                                                    }
                                                    String msg83 = LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifChannel, chat.title);
                                                    return msg83;
                                                } else if (object2.isVoice()) {
                                                    String msg84 = LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceChannel, chat.title);
                                                    return msg84;
                                                } else if (object2.isRoundVideo()) {
                                                    String msg85 = LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundChannel, chat.title);
                                                    return msg85;
                                                } else if (object2.isSticker() || object2.isAnimatedSticker()) {
                                                    String emoji4 = object2.getStickerEmoji();
                                                    String msg86 = emoji4 != null ? LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiChannel, chat.title, emoji4) : LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerChannel, chat.title);
                                                    return msg86;
                                                } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                                    if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat.title, "📎 " + object2.messageOwner.message);
                                                    }
                                                    String msg87 = LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileChannel, chat.title);
                                                    return msg87;
                                                } else {
                                                    if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) {
                                                        i4 = 1;
                                                        c7 = 0;
                                                    } else if (!(object2.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                                        if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                            String msg88 = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveChannel, chat.title);
                                                            return msg88;
                                                        } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                                                            TLRPC.TL_messageMediaContact mediaContact4 = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                                                            String msg89 = LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactChannel2, chat.title, ContactsController.formatName(mediaContact4.first_name, mediaContact4.last_name));
                                                            return msg89;
                                                        } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                            TLRPC.TL_messageMediaPoll mediaPoll4 = (TLRPC.TL_messageMediaPoll) object2.messageOwner.media;
                                                            String msg90 = mediaPoll4.poll.quiz ? LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizChannel2, chat.title, mediaPoll4.poll.question) : LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollChannel2, chat.title, mediaPoll4.poll.question);
                                                            return msg90;
                                                        } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                                                            if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(object2.messageOwner.message)) {
                                                                return LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, chat.title, "🖼 " + object2.messageOwner.message);
                                                            }
                                                            String msg91 = LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoChannel, chat.title);
                                                            return msg91;
                                                        } else if (object2.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                            String msg92 = LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameChannel, chat.title);
                                                            return msg92;
                                                        } else if (object2.messageText != null && object2.messageText.length() > 0) {
                                                            CharSequence message4 = object2.messageText;
                                                            if (message4.length() <= 20) {
                                                                c8 = 0;
                                                                message2 = message4;
                                                            } else {
                                                                StringBuilder sb2 = new StringBuilder();
                                                                c8 = 0;
                                                                sb2.append((Object) message4.subSequence(0, 20));
                                                                sb2.append("...");
                                                                message2 = sb2.toString();
                                                            }
                                                            Object[] objArr6 = new Object[2];
                                                            objArr6[c8] = chat.title;
                                                            objArr6[1] = message2;
                                                            String msg93 = LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, objArr6);
                                                            return msg93;
                                                        } else {
                                                            String msg94 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, chat.title);
                                                            return msg94;
                                                        }
                                                    } else {
                                                        i4 = 1;
                                                        c7 = 0;
                                                    }
                                                    Object[] objArr7 = new Object[i4];
                                                    objArr7[c7] = chat.title;
                                                    String msg95 = LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoChannel, objArr7);
                                                    return msg95;
                                                }
                                            }
                                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGameScore) {
                                            String msg96 = messageObject.messageText.toString();
                                            return msg96;
                                        } else if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionSetChatTheme)) {
                                            if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest) {
                                                String msg97 = messageObject.messageText.toString();
                                                return msg97;
                                            }
                                        } else {
                                            String emoticon = ((TLRPC.TL_messageActionSetChatTheme) messageObject.messageOwner.action).emoticon;
                                            if (TextUtils.isEmpty(emoticon)) {
                                                if (dialogId == selfUsedId) {
                                                    String msg98 = LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new Object[0]);
                                                    return msg98;
                                                }
                                                String msg99 = LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, name, emoticon);
                                                return msg99;
                                            } else if (dialogId == selfUsedId) {
                                                String msg100 = LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, emoticon);
                                                return msg100;
                                            } else {
                                                String msg101 = LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, name, emoticon);
                                                return msg101;
                                            }
                                        }
                                    } else {
                                        long singleUserId = messageObject.messageOwner.action.user_id;
                                        if (singleUserId == 0 && messageObject.messageOwner.action.users.size() == 1) {
                                            singleUserId = messageObject.messageOwner.action.users.get(0).longValue();
                                        }
                                        if (singleUserId != 0) {
                                            if (singleUserId == selfUsedId) {
                                                String msg102 = LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedYouToCall, name, chat.title);
                                                return msg102;
                                            }
                                            TLRPC.User u22 = getMessagesController().getUser(Long.valueOf(singleUserId));
                                            if (u22 == null) {
                                                return null;
                                            }
                                            String msg103 = LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, name, chat.title, UserObject.getUserName(u22));
                                            return msg103;
                                        }
                                        StringBuilder names = new StringBuilder();
                                        for (int a = 0; a < messageObject.messageOwner.action.users.size(); a++) {
                                            TLRPC.User user2 = getMessagesController().getUser(messageObject.messageOwner.action.users.get(a));
                                            if (user2 != null) {
                                                String name2 = UserObject.getUserName(user2);
                                                if (names.length() != 0) {
                                                    names.append(", ");
                                                }
                                                names.append(name2);
                                            }
                                        }
                                        String msg104 = LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, name, chat.title, names.toString());
                                        return msg104;
                                    }
                                } else {
                                    long singleUserId2 = messageObject.messageOwner.action.user_id;
                                    if (singleUserId2 == 0 && messageObject.messageOwner.action.users.size() == 1) {
                                        singleUserId2 = messageObject.messageOwner.action.users.get(0).longValue();
                                    }
                                    if (singleUserId2 != 0) {
                                        if (messageObject.messageOwner.peer_id.channel_id != 0 && !chat.megagroup) {
                                            String msg105 = LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.beta.R.string.ChannelAddedByNotification, name, chat.title);
                                            return msg105;
                                        } else if (singleUserId2 == selfUsedId) {
                                            String msg106 = LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.beta.R.string.NotificationInvitedToGroup, name, chat.title);
                                            return msg106;
                                        } else {
                                            TLRPC.User u23 = getMessagesController().getUser(Long.valueOf(singleUserId2));
                                            if (u23 == null) {
                                                return null;
                                            }
                                            if (fromId2 == u23.id) {
                                                msg2 = chat.megagroup ? LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.beta.R.string.NotificationGroupAddSelfMega, name, chat.title) : LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.beta.R.string.NotificationGroupAddSelf, name, chat.title);
                                            } else {
                                                msg2 = LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, name, chat.title, UserObject.getUserName(u23));
                                            }
                                            return msg2;
                                        }
                                    }
                                    StringBuilder names2 = new StringBuilder();
                                    for (int a2 = 0; a2 < messageObject.messageOwner.action.users.size(); a2++) {
                                        TLRPC.User user3 = getMessagesController().getUser(messageObject.messageOwner.action.users.get(a2));
                                        if (user3 != null) {
                                            String name22 = UserObject.getUserName(user3);
                                            if (names2.length() != 0) {
                                                names2.append(", ");
                                            }
                                            names2.append(name22);
                                        }
                                    }
                                    String msg107 = LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, name, chat.title, names2.toString());
                                    return msg107;
                                }
                            } else {
                                str2 = str3;
                            }
                            if (preview != 0) {
                                preview[0] = false;
                            }
                            if (!ChatObject.isChannel(chat) && !chat.megagroup) {
                                String msg108 = LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, name);
                                return msg108;
                            }
                            String msg109 = LocaleController.formatString(str2, org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, name, chat.title);
                            return msg109;
                        }
                    } else {
                        chat = chat4;
                    }
                    isChannel = false;
                    if (!dialogPreviewEnabled) {
                    }
                    if (preview != 0) {
                    }
                    if (!ChatObject.isChannel(chat)) {
                    }
                    String msg1092 = LocaleController.formatString(str2, org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, name, chat.title);
                    return msg1092;
                }
            } else {
                if (dialogPreviewEnabled) {
                    preferences = preferences2;
                    if (!preferences.getBoolean("EnablePreviewAll", true)) {
                        z = 0;
                    } else if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                        if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGeoProximityReached) {
                            String msg110 = messageObject.messageText.toString();
                            return msg110;
                        } else if ((messageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserJoined) || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionContactSignUp)) {
                            String msg111 = LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.beta.R.string.NotificationContactJoined, name);
                            return msg111;
                        } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                            String msg112 = LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.beta.R.string.NotificationContactNewPhoto, name);
                            return msg112;
                        } else if (!(messageObject.messageOwner.action instanceof TLRPC.TL_messageActionLoginUnknownLocation)) {
                            if ((messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGameScore) || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPaymentSent)) {
                                String msg113 = messageObject.messageText.toString();
                                return msg113;
                            } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionPhoneCall) {
                                if (messageObject.messageOwner.action.video) {
                                    String msg114 = LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageVideoIncomingMissed);
                                    return msg114;
                                }
                                String msg115 = LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageIncomingMissed);
                                return msg115;
                            } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionSetChatTheme) {
                                String emoticon2 = ((TLRPC.TL_messageActionSetChatTheme) messageObject.messageOwner.action).emoticon;
                                if (TextUtils.isEmpty(emoticon2)) {
                                    msg3 = dialogId == selfUsedId ? LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new Object[0]) : LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, name, emoticon2);
                                    z2 = true;
                                    c10 = 0;
                                } else if (dialogId == selfUsedId) {
                                    z2 = true;
                                    c10 = 0;
                                    msg3 = LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, emoticon2);
                                } else {
                                    z2 = true;
                                    c10 = 0;
                                    msg3 = LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, name, emoticon2);
                                }
                                text[c10] = z2;
                                return msg3;
                            }
                        } else {
                            String date = LocaleController.formatString("formatDateAtTime", org.telegram.messenger.beta.R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000));
                            return LocaleController.formatString("NotificationUnrecognizedDevice", org.telegram.messenger.beta.R.string.NotificationUnrecognizedDevice, getUserConfig().getCurrentUser().first_name, date, messageObject.messageOwner.action.title, messageObject.messageOwner.action.address);
                        }
                    } else if (messageObject.isMediaEmpty()) {
                        if (!shortMessage) {
                            if (TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                String msg116 = LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, name);
                                return msg116;
                            }
                            String msg117 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, messageObject.messageOwner.message);
                            text[0] = true;
                            return msg117;
                        }
                        String msg118 = LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, name);
                        return msg118;
                    } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                        if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            String msg119 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "🖼 " + messageObject.messageOwner.message);
                            text[0] = true;
                            return msg119;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            String msg120 = LocaleController.formatString("NotificationMessageSDPhoto", org.telegram.messenger.beta.R.string.NotificationMessageSDPhoto, name);
                            return msg120;
                        } else {
                            String msg121 = LocaleController.formatString("NotificationMessagePhoto", org.telegram.messenger.beta.R.string.NotificationMessagePhoto, name);
                            return msg121;
                        }
                    } else if (messageObject.isVideo()) {
                        if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            String msg122 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "📹 " + messageObject.messageOwner.message);
                            text[0] = true;
                            return msg122;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            String msg123 = LocaleController.formatString("NotificationMessageSDVideo", org.telegram.messenger.beta.R.string.NotificationMessageSDVideo, name);
                            return msg123;
                        } else {
                            String msg124 = LocaleController.formatString("NotificationMessageVideo", org.telegram.messenger.beta.R.string.NotificationMessageVideo, name);
                            return msg124;
                        }
                    } else if (messageObject.isGame()) {
                        String msg125 = LocaleController.formatString("NotificationMessageGame", org.telegram.messenger.beta.R.string.NotificationMessageGame, name, messageObject.messageOwner.media.game.title);
                        return msg125;
                    } else if (messageObject.isVoice()) {
                        String msg126 = LocaleController.formatString("NotificationMessageAudio", org.telegram.messenger.beta.R.string.NotificationMessageAudio, name);
                        return msg126;
                    } else if (messageObject.isRoundVideo()) {
                        String msg127 = LocaleController.formatString("NotificationMessageRound", org.telegram.messenger.beta.R.string.NotificationMessageRound, name);
                        return msg127;
                    } else if (messageObject.isMusic()) {
                        String msg128 = LocaleController.formatString("NotificationMessageMusic", org.telegram.messenger.beta.R.string.NotificationMessageMusic, name);
                        return msg128;
                    } else if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaContact)) {
                        if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                            if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeo)) {
                                if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                                    if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                                        String msg129 = LocaleController.formatString("NotificationMessageLiveLocation", org.telegram.messenger.beta.R.string.NotificationMessageLiveLocation, name);
                                        return msg129;
                                    } else if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                                        if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                            String emoji5 = messageObject.getStickerEmoji();
                                            return emoji5 != null ? LocaleController.formatString("NotificationMessageStickerEmoji", org.telegram.messenger.beta.R.string.NotificationMessageStickerEmoji, name, emoji5) : LocaleController.formatString("NotificationMessageSticker", org.telegram.messenger.beta.R.string.NotificationMessageSticker, name);
                                        } else if (messageObject.isGif()) {
                                            if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                                String msg130 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "🎬 " + messageObject.messageOwner.message);
                                                text[0] = true;
                                                return msg130;
                                            }
                                            String msg131 = LocaleController.formatString("NotificationMessageGif", org.telegram.messenger.beta.R.string.NotificationMessageGif, name);
                                            return msg131;
                                        } else if (!shortMessage && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                            String msg132 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, "📎 " + messageObject.messageOwner.message);
                                            text[0] = true;
                                            return msg132;
                                        } else {
                                            String msg133 = LocaleController.formatString("NotificationMessageDocument", org.telegram.messenger.beta.R.string.NotificationMessageDocument, name);
                                            return msg133;
                                        }
                                    } else if (shortMessage || TextUtils.isEmpty(messageObject.messageText)) {
                                        String msg134 = LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, name);
                                        return msg134;
                                    } else {
                                        String msg135 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, name, messageObject.messageText);
                                        text[0] = true;
                                        return msg135;
                                    }
                                }
                                c9 = 0;
                                i5 = 1;
                            } else {
                                c9 = 0;
                                i5 = 1;
                            }
                            Object[] objArr8 = new Object[i5];
                            objArr8[c9] = name;
                            String msg136 = LocaleController.formatString("NotificationMessageMap", org.telegram.messenger.beta.R.string.NotificationMessageMap, objArr8);
                            return msg136;
                        }
                        TLRPC.TL_messageMediaPoll mediaPoll5 = (TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media;
                        String msg137 = mediaPoll5.poll.quiz ? LocaleController.formatString("NotificationMessageQuiz2", org.telegram.messenger.beta.R.string.NotificationMessageQuiz2, name, mediaPoll5.poll.question) : LocaleController.formatString("NotificationMessagePoll2", org.telegram.messenger.beta.R.string.NotificationMessagePoll2, name, mediaPoll5.poll.question);
                        return msg137;
                    } else {
                        TLRPC.TL_messageMediaContact mediaContact5 = (TLRPC.TL_messageMediaContact) messageObject.messageOwner.media;
                        String msg138 = LocaleController.formatString("NotificationMessageContact2", org.telegram.messenger.beta.R.string.NotificationMessageContact2, name, ContactsController.formatName(mediaContact5.first_name, mediaContact5.last_name));
                        return msg138;
                    }
                } else {
                    preferences = preferences2;
                    z = 0;
                }
                if (preview != 0) {
                    preview[z] = z;
                }
                Object[] objArr9 = new Object[1];
                char c13 = z ? 1 : 0;
                char c14 = z ? 1 : 0;
                char c15 = z ? 1 : 0;
                objArr9[c13] = name;
                String msg139 = LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, objArr9);
                return msg139;
            }
            return null;
        }
        String msg140 = LocaleController.getString("YouHaveNewMessage", org.telegram.messenger.beta.R.string.YouHaveNewMessage);
        return msg140;
    }

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent pintent = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, 0);
            SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
            int minutes = preferences.getInt("repeat_messages", 60);
            if (minutes > 0 && this.personalCount > 0) {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + (minutes * 60 * 1000), pintent);
            } else {
                this.alarmManager.cancel(pintent);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean isPersonalMessage(MessageObject messageObject) {
        return messageObject.messageOwner.peer_id != null && messageObject.messageOwner.peer_id.chat_id == 0 && messageObject.messageOwner.peer_id.channel_id == 0 && (messageObject.messageOwner.action == null || (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionEmpty));
    }

    private int getNotifyOverride(SharedPreferences preferences, long dialog_id) {
        int notifyOverride = preferences.getInt("notify2_" + dialog_id, -1);
        if (notifyOverride == 3) {
            int muteUntil = preferences.getInt("notifyuntil_" + dialog_id, 0);
            if (muteUntil >= getConnectionsManager().getCurrentTime()) {
                return 2;
            }
            return notifyOverride;
        }
        return notifyOverride;
    }

    /* renamed from: lambda$showNotifications$25$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1125x8a90ed32() {
        showOrUpdateNotification(false);
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1125x8a90ed32();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1100x832e582c();
            }
        });
    }

    /* renamed from: lambda$hideNotifications$26$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1100x832e582c() {
        notificationManager.cancel(this.notificationId);
        this.lastWearNotifiedMessageId.clear();
        for (int a = 0; a < this.wearNotificationsIds.size(); a++) {
            notificationManager.cancel(this.wearNotificationsIds.valueAt(a).intValue());
        }
        this.wearNotificationsIds.clear();
    }

    private void dismissNotification() {
        try {
            notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            for (int a = 0; a < this.wearNotificationsIds.size(); a++) {
                long did = this.wearNotificationsIds.keyAt(a);
                if (!this.openedInBubbleDialogs.contains(Long.valueOf(did))) {
                    notificationManager.cancel(this.wearNotificationsIds.valueAt(a).intValue());
                }
            }
            this.wearNotificationsIds.clear();
            AndroidUtilities.runOnUIThread(NotificationsController$$ExternalSyntheticLambda31.INSTANCE);
        } catch (Exception e) {
            FileLog.e(e);
        }
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
            SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
            int notifyOverride = getNotifyOverride(preferences, this.openedDialogId);
            if (notifyOverride == 2) {
                return;
            }
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.m1102xa67ee1();
                }
            });
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* renamed from: lambda$playInChatSound$29$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1102xa67ee1() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundPlay) <= 500) {
            return;
        }
        try {
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(NotificationsController$$ExternalSyntheticLambda22.INSTANCE);
            }
            if (this.soundIn == 0 && !this.soundInLoaded) {
                this.soundInLoaded = true;
                this.soundIn = this.soundPool.load(ApplicationLoader.applicationContext, org.telegram.messenger.beta.R.raw.sound_in, 1);
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

    public static /* synthetic */ void lambda$playInChatSound$28(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            try {
                soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private void scheduleNotificationDelay(boolean onlineReason) {
        try {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("delay notification start, onlineReason = " + onlineReason);
            }
            this.notificationDelayWakelock.acquire(10000L);
            notificationsQueue.cancelRunnable(this.notificationDelayRunnable);
            notificationsQueue.postRunnable(this.notificationDelayRunnable, onlineReason ? 3000 : 1000);
        } catch (Exception e) {
            FileLog.e(e);
            showOrUpdateNotification(this.notifyCheck);
        }
    }

    public void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1121x309788cf();
            }
        });
    }

    /* renamed from: lambda$repeatNotificationMaybe$30$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1121x309788cf() {
        int hour = Calendar.getInstance().get(11);
        if (hour >= 11 && hour <= 22) {
            notificationManager.cancel(this.notificationId);
            showOrUpdateNotification(true);
            return;
        }
        scheduleNotificationRepeat();
    }

    private boolean isEmptyVibration(long[] pattern) {
        if (pattern == null || pattern.length == 0) {
            return false;
        }
        for (int a = 0; a < pattern.length; a++) {
            if (pattern[a] != 0) {
                return false;
            }
        }
        return true;
    }

    public void deleteNotificationChannel(long dialogId) {
        deleteNotificationChannel(dialogId, -1);
    }

    /* renamed from: deleteNotificationChannelInternal */
    public void m1096xab324d39(long dialogId, int what) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor editor = preferences.edit();
            if (what == 0 || what == -1) {
                String key = "org.telegram.key" + dialogId;
                String channelId = preferences.getString(key, null);
                if (channelId != null) {
                    editor.remove(key).remove(key + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(channelId);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel internal " + channelId);
                    }
                }
            }
            if (what == 1 || what == -1) {
                String key2 = "org.telegram.keyia" + dialogId;
                String channelId2 = preferences.getString(key2, null);
                if (channelId2 != null) {
                    editor.remove(key2).remove(key2 + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(channelId2);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel internal " + channelId2);
                    }
                }
            }
            editor.commit();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannel(final long dialogId, final int what) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1096xab324d39(dialogId, what);
            }
        });
    }

    public void deleteNotificationChannelGlobal(int type) {
        deleteNotificationChannelGlobal(type, -1);
    }

    /* renamed from: deleteNotificationChannelGlobalInternal */
    public void m1097xb6f20c1b(int type, int what) {
        String overwriteKey;
        String key;
        String key2;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor editor = preferences.edit();
            if (what == 0 || what == -1) {
                if (type == 2) {
                    key2 = "channels";
                } else if (type == 0) {
                    key2 = "groups";
                } else {
                    key2 = "private";
                }
                String channelId = preferences.getString(key2, null);
                if (channelId != null) {
                    SharedPreferences.Editor remove = editor.remove(key2);
                    remove.remove(key2 + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(channelId);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel global internal " + channelId);
                    }
                }
            }
            if (what == 1 || what == -1) {
                if (type == 2) {
                    key = "channels_ia";
                } else if (type == 0) {
                    key = "groups_ia";
                } else {
                    key = "private_ia";
                }
                String channelId2 = preferences.getString(key, null);
                if (channelId2 != null) {
                    SharedPreferences.Editor remove2 = editor.remove(key);
                    remove2.remove(key + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(channelId2);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel global internal " + channelId2);
                    }
                }
            }
            if (type == 2) {
                overwriteKey = "overwrite_channel";
            } else if (type == 0) {
                overwriteKey = "overwrite_group";
            } else {
                overwriteKey = "overwrite_private";
            }
            editor.remove(overwriteKey);
            editor.commit();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannelGlobal(final int type, final int what) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1097xb6f20c1b(type, what);
            }
        });
    }

    public void deleteAllNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1095xdfb4577b();
            }
        });
    }

    /* renamed from: lambda$deleteAllNotificationChannels$33$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1095xdfb4577b() {
        try {
            SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
            Map<String, ?> values = preferences.getAll();
            SharedPreferences.Editor editor = preferences.edit();
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("org.telegram.key")) {
                    if (!key.endsWith("_s")) {
                        String id = (String) entry.getValue();
                        systemNotificationManager.deleteNotificationChannel(id);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete all channel " + id);
                        }
                    }
                    editor.remove(key);
                }
            }
            editor.commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean unsupportedNotificationShortcut() {
        return Build.VERSION.SDK_INT < 29 || !SharedConfig.chatBubbles;
    }

    private String createNotificationShortcut(NotificationCompat.Builder builder, long did, String name, TLRPC.User user, TLRPC.Chat chat, Person person) {
        Exception e;
        String id;
        Intent shortcutIntent;
        IconCompat icon;
        if (!unsupportedNotificationShortcut()) {
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                return null;
            }
            try {
                id = "ndid_" + did;
                shortcutIntent = new Intent(ApplicationLoader.applicationContext, OpenChatReceiver.class);
                shortcutIntent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                if (did > 0) {
                    shortcutIntent.putExtra("userId", did);
                } else {
                    shortcutIntent.putExtra("chatId", -did);
                }
            } catch (Exception e2) {
                e = e2;
            }
            try {
                ShortcutInfoCompat.Builder shortcutBuilder = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, id).setShortLabel(chat != null ? name : UserObject.getFirstName(user)).setLongLabel(name).setIntent(new Intent("android.intent.action.VIEW")).setIntent(shortcutIntent).setLongLived(true).setLocusId(new LocusIdCompat(id));
                Bitmap avatar = null;
                if (person != null) {
                    shortcutBuilder.setPerson(person);
                    shortcutBuilder.setIcon(person.getIcon());
                    if (person.getIcon() != null) {
                        avatar = person.getIcon().getBitmap();
                    }
                }
                ShortcutInfoCompat shortcut = shortcutBuilder.build();
                ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, shortcut);
                builder.setShortcutInfo(shortcut);
                Intent intent = new Intent(ApplicationLoader.applicationContext, BubbleActivity.class);
                intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                if (DialogObject.isUserDialog(did)) {
                    intent.putExtra("userId", did);
                } else {
                    intent.putExtra("chatId", -did);
                }
                intent.putExtra("currentAccount", this.currentAccount);
                if (avatar != null) {
                    icon = IconCompat.createWithAdaptiveBitmap(avatar);
                } else if (user != null) {
                    icon = IconCompat.createWithResource(ApplicationLoader.applicationContext, user.bot ? org.telegram.messenger.beta.R.drawable.book_bot : org.telegram.messenger.beta.R.drawable.book_user);
                } else {
                    icon = IconCompat.createWithResource(ApplicationLoader.applicationContext, org.telegram.messenger.beta.R.drawable.book_group);
                }
                NotificationCompat.BubbleMetadata.Builder bubbleBuilder = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 134217728), icon);
                bubbleBuilder.setSuppressNotification(this.openedDialogId == did);
                bubbleBuilder.setAutoExpandBubble(false);
                bubbleBuilder.setDesiredHeight(AndroidUtilities.dp(640.0f));
                builder.setBubbleMetadata(bubbleBuilder.build());
                return id;
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                return null;
            }
        }
        return null;
    }

    protected void ensureGroupsCreated() {
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        if (this.groupsCreated == null) {
            this.groupsCreated = Boolean.valueOf(preferences.getBoolean("groupsCreated4", false));
        }
        if (!this.groupsCreated.booleanValue()) {
            try {
                String keyStart = this.currentAccount + "channel";
                List<NotificationChannel> list = systemNotificationManager.getNotificationChannels();
                int count = list.size();
                SharedPreferences.Editor editor = null;
                for (int a = 0; a < count; a++) {
                    NotificationChannel channel = list.get(a);
                    String id = channel.getId();
                    if (id.startsWith(keyStart)) {
                        int importance = channel.getImportance();
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
                                long dialogId = Utilities.parseLong(id.substring(9, id.indexOf(95, 9))).longValue();
                                if (dialogId != 0) {
                                    if (editor == null) {
                                        editor = getAccountInstance().getNotificationsSettings().edit();
                                    }
                                    editor.remove("priority_" + dialogId).remove("vibrate_" + dialogId).remove("sound_path_" + dialogId).remove("sound_" + dialogId);
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
            preferences.edit().putBoolean("groupsCreated4", true).commit();
            this.groupsCreated = true;
        }
        if (!this.channelGroupsCreated) {
            List<NotificationChannelGroup> list2 = systemNotificationManager.getNotificationChannelGroups();
            String channelsId = "channels" + this.currentAccount;
            String groupsId = "groups" + this.currentAccount;
            String privateId = "private" + this.currentAccount;
            String otherId = "other" + this.currentAccount;
            int N = list2.size();
            for (int a2 = 0; a2 < N; a2++) {
                String id2 = list2.get(a2).getId();
                if (channelsId != null && channelsId.equals(id2)) {
                    channelsId = null;
                } else if (groupsId != null && groupsId.equals(id2)) {
                    groupsId = null;
                } else if (privateId != null && privateId.equals(id2)) {
                    privateId = null;
                } else if (otherId != null && otherId.equals(id2)) {
                    otherId = null;
                }
                if (channelsId == null && groupsId == null && privateId == null && otherId == null) {
                    break;
                }
            }
            if (channelsId != null || groupsId != null || privateId != null || otherId != null) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
                if (user == null) {
                    getUserConfig().getCurrentUser();
                }
                String userName = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : "";
                ArrayList<NotificationChannelGroup> channelGroups = new ArrayList<>();
                if (channelsId != null) {
                    channelGroups.add(new NotificationChannelGroup(channelsId, LocaleController.getString("NotificationsChannels", org.telegram.messenger.beta.R.string.NotificationsChannels) + userName));
                }
                if (groupsId != null) {
                    channelGroups.add(new NotificationChannelGroup(groupsId, LocaleController.getString("NotificationsGroups", org.telegram.messenger.beta.R.string.NotificationsGroups) + userName));
                }
                if (privateId != null) {
                    channelGroups.add(new NotificationChannelGroup(privateId, LocaleController.getString("NotificationsPrivateChats", org.telegram.messenger.beta.R.string.NotificationsPrivateChats) + userName));
                }
                if (otherId != null) {
                    channelGroups.add(new NotificationChannelGroup(otherId, LocaleController.getString("NotificationsOther", org.telegram.messenger.beta.R.string.NotificationsOther) + userName));
                }
                systemNotificationManager.createNotificationChannelGroups(channelGroups);
            }
            this.channelGroupsCreated = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:193:0x0483 A[LOOP:1: B:191:0x047e->B:193:0x0483, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x049c  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x04e9  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x05d8  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0493 A[EDGE_INSN: B:248:0x0493->B:194:0x0493 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String validateChannelId(long dialogId, String name, long[] vibrationPattern, int ledColor, Uri sound, int importance, boolean isDefault, boolean isInApp, boolean isSilent, int type) {
        String overwriteKey;
        String groupId;
        String name2;
        String key;
        String str;
        String groupId2;
        String str2;
        boolean secretChat;
        long j;
        int i;
        String settings;
        StringBuilder newSettings;
        String channelId;
        int ledColor2;
        long[] vibrationPattern2;
        String channelId2;
        SharedPreferences preferences;
        String key2;
        String str3;
        Uri uri;
        boolean z;
        int a;
        String newSettingsHash;
        String channelId3;
        String settings2;
        boolean vibrate;
        long[] channelVibrationPattern;
        String channelId4;
        String settings3;
        String newSettingsHash2;
        int ledColor3;
        long[] vibrationPattern3;
        long[] vibrationPattern4;
        long[] vibrationPattern5;
        int ledColor4;
        int priority;
        String name3;
        ensureGroupsCreated();
        SharedPreferences preferences2 = getAccountInstance().getNotificationsSettings();
        String str4 = "groups";
        String key3 = "private";
        String str5 = "channels";
        if (isSilent) {
            groupId = "other" + this.currentAccount;
            overwriteKey = null;
        } else if (type == 2) {
            groupId = str5 + this.currentAccount;
            overwriteKey = "overwrite_channel";
        } else if (type == 0) {
            groupId = str4 + this.currentAccount;
            overwriteKey = "overwrite_group";
        } else {
            groupId = key3 + this.currentAccount;
            overwriteKey = "overwrite_private";
        }
        boolean secretChat2 = !isDefault && DialogObject.isEncryptedDialog(dialogId);
        boolean shouldOverwrite = !isInApp && overwriteKey != null && preferences2.getBoolean(overwriteKey, false);
        String soundHash = Utilities.MD5(sound == null ? "NoSound" : sound.toString());
        if (soundHash != null && soundHash.length() > 5) {
            soundHash = soundHash.substring(0, 5);
        }
        if (isSilent) {
            String name4 = LocaleController.getString("NotificationsSilent", org.telegram.messenger.beta.R.string.NotificationsSilent);
            key3 = NotificationCompat.GROUP_KEY_SILENT;
            name2 = name4;
        } else if (isDefault) {
            name2 = isInApp ? LocaleController.getString("NotificationsInAppDefault", org.telegram.messenger.beta.R.string.NotificationsInAppDefault) : LocaleController.getString("NotificationsDefault", org.telegram.messenger.beta.R.string.NotificationsDefault);
            if (type == 2) {
                if (isInApp) {
                    str5 = "channels_ia";
                }
                key3 = str5;
            } else if (type == 0) {
                if (isInApp) {
                    str4 = "groups_ia";
                }
                key3 = str4;
            } else if (isInApp) {
                key3 = "private_ia";
            }
        } else {
            if (!isInApp) {
                name3 = name;
            } else {
                name3 = LocaleController.formatString("NotificationsChatInApp", org.telegram.messenger.beta.R.string.NotificationsChatInApp, name);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(isInApp ? "org.telegram.keyia" : "org.telegram.key");
            sb.append(dialogId);
            key3 = sb.toString();
            name2 = name3;
        }
        String key4 = key3 + "_" + soundHash;
        String channelId5 = preferences2.getString(key4, null);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(key4);
        String name5 = name2;
        sb2.append("_s");
        String settings4 = preferences2.getString(sb2.toString(), null);
        StringBuilder newSettings2 = new StringBuilder();
        boolean edited = false;
        if (channelId5 == null) {
            channelId3 = channelId5;
            key = key4;
            str2 = "_";
            str = "_s";
            groupId2 = groupId;
            settings2 = settings4;
            i = importance;
            newSettings = newSettings2;
            secretChat = secretChat2;
            j = dialogId;
        } else {
            NotificationChannel existingChannel = systemNotificationManager.getNotificationChannel(channelId5);
            groupId2 = groupId;
            if (!BuildVars.LOGS_ENABLED) {
                str2 = "_";
            } else {
                str2 = "_";
                FileLog.d("current channel for " + channelId5 + " = " + existingChannel);
            }
            if (existingChannel != null) {
                if (isSilent || shouldOverwrite) {
                    channelId3 = channelId5;
                    key = key4;
                    str = "_s";
                    settings2 = settings4;
                    i = importance;
                    newSettings = newSettings2;
                    secretChat = secretChat2;
                    j = dialogId;
                } else {
                    int channelImportance = existingChannel.getImportance();
                    Uri channelSound = existingChannel.getSound();
                    long[] channelVibrationPattern2 = existingChannel.getVibrationPattern();
                    str = "_s";
                    boolean vibrate2 = existingChannel.shouldVibrate();
                    if (vibrate2 || channelVibrationPattern2 != null) {
                        key = key4;
                        vibrate = vibrate2;
                        channelVibrationPattern = channelVibrationPattern2;
                    } else {
                        key = key4;
                        vibrate = vibrate2;
                        channelVibrationPattern = new long[]{0, 0};
                    }
                    int channelLedColor = existingChannel.getLightColor();
                    if (channelVibrationPattern != null) {
                        for (long j2 : channelVibrationPattern) {
                            newSettings2.append(j2);
                        }
                        newSettings = newSettings2;
                    } else {
                        newSettings = newSettings2;
                    }
                    newSettings.append(channelLedColor);
                    if (channelSound != null) {
                        newSettings.append(channelSound.toString());
                    }
                    newSettings.append(channelImportance);
                    if (!isDefault && secretChat2) {
                        newSettings.append("secret");
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("current channel settings for " + channelId5 + " = " + ((Object) newSettings) + " old = " + settings4);
                    }
                    String newSettingsHash3 = Utilities.MD5(newSettings.toString());
                    newSettings.setLength(0);
                    if (newSettingsHash3.equals(settings4)) {
                        i = importance;
                        newSettingsHash2 = newSettingsHash3;
                        settings3 = settings4;
                        secretChat = secretChat2;
                        j = dialogId;
                        channelId4 = channelId5;
                        vibrationPattern3 = vibrationPattern;
                        ledColor3 = ledColor;
                    } else {
                        SharedPreferences.Editor editor = null;
                        if (channelImportance == 0) {
                            editor = preferences2.edit();
                            if (isDefault) {
                                if (isInApp) {
                                    settings3 = settings4;
                                    secretChat = secretChat2;
                                    j = dialogId;
                                } else {
                                    editor.putInt(getGlobalNotificationsKey(type), Integer.MAX_VALUE);
                                    updateServerNotificationsSettings(type);
                                    settings3 = settings4;
                                    secretChat = secretChat2;
                                    j = dialogId;
                                }
                            } else {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("notify2_");
                                settings3 = settings4;
                                secretChat = secretChat2;
                                j = dialogId;
                                sb3.append(j);
                                editor.putInt(sb3.toString(), 2);
                                updateServerNotificationsSettings(j, true);
                            }
                            i = importance;
                            newSettingsHash2 = newSettingsHash3;
                            edited = true;
                        } else {
                            settings3 = settings4;
                            secretChat = secretChat2;
                            j = dialogId;
                            i = importance;
                            if (channelImportance == i) {
                                newSettingsHash2 = newSettingsHash3;
                            } else {
                                if (isInApp) {
                                    newSettingsHash2 = newSettingsHash3;
                                } else {
                                    editor = preferences2.edit();
                                    newSettingsHash2 = newSettingsHash3;
                                    if (channelImportance == 4 || channelImportance == 5) {
                                        priority = 1;
                                    } else if (channelImportance == 1) {
                                        priority = 4;
                                    } else if (channelImportance == 2) {
                                        priority = 5;
                                    } else {
                                        priority = 0;
                                    }
                                    if (!isDefault) {
                                        editor.putInt("notify2_" + j, 0);
                                        editor.remove("notifyuntil_" + j);
                                        editor.putInt("priority_" + j, priority);
                                    } else {
                                        editor.putInt(getGlobalNotificationsKey(type), 0).commit();
                                        if (type == 2) {
                                            editor.putInt("priority_channel", priority);
                                        } else if (type == 0) {
                                            editor.putInt("priority_group", priority);
                                        } else {
                                            editor.putInt("priority_messages", priority);
                                        }
                                    }
                                }
                                edited = true;
                            }
                        }
                        boolean hasVibration = !isEmptyVibration(vibrationPattern);
                        boolean vibrate3 = vibrate;
                        if (hasVibration == vibrate3) {
                            channelId4 = channelId5;
                            vibrationPattern4 = vibrationPattern;
                        } else {
                            if (isInApp) {
                                channelId4 = channelId5;
                            } else {
                                if (editor == null) {
                                    editor = preferences2.edit();
                                }
                                if (isDefault) {
                                    channelId4 = channelId5;
                                    if (type == 2) {
                                        editor.putInt("vibrate_channel", vibrate3 ? 0 : 2);
                                    } else if (type == 0) {
                                        editor.putInt("vibrate_group", vibrate3 ? 0 : 2);
                                    } else {
                                        editor.putInt("vibrate_messages", vibrate3 ? 0 : 2);
                                    }
                                } else {
                                    channelId4 = channelId5;
                                    editor.putInt("vibrate_" + j, vibrate3 ? 0 : 2);
                                }
                            }
                            vibrationPattern4 = channelVibrationPattern;
                            edited = true;
                        }
                        if (channelLedColor == ledColor) {
                            vibrationPattern5 = vibrationPattern4;
                            ledColor4 = ledColor;
                        } else {
                            if (isInApp) {
                                vibrationPattern5 = vibrationPattern4;
                            } else {
                                if (editor == null) {
                                    editor = preferences2.edit();
                                }
                                if (isDefault) {
                                    vibrationPattern5 = vibrationPattern4;
                                    if (type == 2) {
                                        editor.putInt("ChannelLed", channelLedColor);
                                    } else if (type == 0) {
                                        editor.putInt("GroupLed", channelLedColor);
                                    } else {
                                        editor.putInt("MessagesLed", channelLedColor);
                                    }
                                } else {
                                    vibrationPattern5 = vibrationPattern4;
                                    editor.putInt("color_" + j, channelLedColor);
                                }
                            }
                            ledColor4 = channelLedColor;
                            edited = true;
                        }
                        if (editor != null) {
                            editor.commit();
                        }
                        ledColor3 = ledColor4;
                        vibrationPattern3 = vibrationPattern5;
                    }
                    ledColor2 = ledColor3;
                    settings = settings3;
                    channelId = channelId4;
                    vibrationPattern2 = vibrationPattern3;
                    channelId2 = newSettingsHash2;
                }
            } else {
                key = key4;
                str = "_s";
                i = importance;
                newSettings = newSettings2;
                secretChat = secretChat2;
                j = dialogId;
                ledColor2 = ledColor;
                channelId = null;
                settings = null;
                channelId2 = null;
                vibrationPattern2 = vibrationPattern;
            }
            if (edited || channelId2 == null) {
                str3 = str;
                key2 = key;
                if (!shouldOverwrite || channelId2 == null || !isInApp || !isDefault) {
                    a = 0;
                    while (true) {
                        String newSettingsHash4 = channelId2;
                        if (a < vibrationPattern2.length) {
                            break;
                        }
                        newSettings.append(vibrationPattern2[a]);
                        a++;
                        channelId2 = newSettingsHash4;
                        preferences2 = preferences2;
                    }
                    preferences = preferences2;
                    newSettings.append(ledColor2);
                    uri = sound;
                    if (uri != null) {
                        newSettings.append(sound.toString());
                    }
                    newSettings.append(i);
                    if (!isDefault && secretChat) {
                        newSettings.append("secret");
                    }
                    newSettingsHash = Utilities.MD5(newSettings.toString());
                    if (isSilent && channelId != null && (shouldOverwrite || !settings.equals(newSettingsHash))) {
                        try {
                            systemNotificationManager.deleteNotificationChannel(channelId);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete channel by settings change " + channelId);
                        }
                        channelId = null;
                        channelId2 = newSettingsHash;
                    } else {
                        channelId2 = newSettingsHash;
                    }
                    if (channelId == null) {
                        channelId = !isDefault ? this.currentAccount + "channel_" + j + str2 + Utilities.random.nextLong() : this.currentAccount + "channel_" + key2 + str2 + Utilities.random.nextLong();
                        NotificationChannel notificationChannel = new NotificationChannel(channelId, secretChat ? LocaleController.getString("SecretChatName", org.telegram.messenger.beta.R.string.SecretChatName) : name5, i);
                        notificationChannel.setGroup(groupId2);
                        if (ledColor2 != 0) {
                            z = true;
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(ledColor2);
                        } else {
                            z = true;
                            notificationChannel.enableLights(false);
                        }
                        if (!isEmptyVibration(vibrationPattern2)) {
                            notificationChannel.enableVibration(z);
                            if (vibrationPattern2.length > 0) {
                                notificationChannel.setVibrationPattern(vibrationPattern2);
                            }
                        } else {
                            notificationChannel.enableVibration(false);
                        }
                        AudioAttributes.Builder builder = new AudioAttributes.Builder();
                        builder.setContentType(4);
                        builder.setUsage(5);
                        if (uri != null) {
                            notificationChannel.setSound(uri, builder.build());
                        } else {
                            notificationChannel.setSound(null, null);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("create new channel " + channelId);
                        }
                        this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                        systemNotificationManager.createNotificationChannel(notificationChannel);
                        preferences.edit().putString(key2, channelId).putString(key2 + str3, channelId2).commit();
                    }
                    return channelId;
                }
            } else {
                key2 = key;
                SharedPreferences.Editor putString = preferences2.edit().putString(key2, channelId);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(key2);
                String str6 = str;
                sb4.append(str6);
                putString.putString(sb4.toString(), channelId2).commit();
                if (!BuildVars.LOGS_ENABLED) {
                    str3 = str6;
                } else {
                    FileLog.d("change edited channel " + channelId);
                    str3 = str6;
                }
            }
            uri = sound;
            preferences = preferences2;
            if (channelId == null) {
            }
            return channelId;
        }
        vibrationPattern2 = vibrationPattern;
        ledColor2 = ledColor;
        channelId2 = null;
        settings = settings2;
        channelId = channelId3;
        if (edited) {
        }
        str3 = str;
        key2 = key;
        if (!shouldOverwrite) {
        }
        a = 0;
        while (true) {
            String newSettingsHash42 = channelId2;
            if (a < vibrationPattern2.length) {
            }
            newSettings.append(vibrationPattern2[a]);
            a++;
            channelId2 = newSettingsHash42;
            preferences2 = preferences2;
        }
        preferences = preferences2;
        newSettings.append(ledColor2);
        uri = sound;
        if (uri != null) {
        }
        newSettings.append(i);
        if (!isDefault) {
            newSettings.append("secret");
        }
        newSettingsHash = Utilities.MD5(newSettings.toString());
        if (isSilent) {
        }
        channelId2 = newSettingsHash;
        if (channelId == null) {
        }
        return channelId;
    }

    /* JADX WARN: Code restructure failed: missing block: B:84:0x0185, code lost:
        if (r5 == 0) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0187, code lost:
        r2 = org.telegram.messenger.LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.beta.R.string.NotificationHiddenChatName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0191, code lost:
        r2 = org.telegram.messenger.LocaleController.getString("NotificationHiddenName", org.telegram.messenger.beta.R.string.NotificationHiddenName);
     */
    /* JADX WARN: Removed duplicated region for block: B:106:0x020f A[Catch: Exception -> 0x0dce, TRY_ENTER, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0230 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x028f A[Catch: Exception -> 0x0dce, TRY_ENTER, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0307 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x03cb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x03e1  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x04bc A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x04e4  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x04ff A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x05b4  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x05ce  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0680  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x06e2  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x06e6  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x06ee A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x06fb  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0702 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x070b A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x071a  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0720  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x072c  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0730 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0762  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0766  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x078b  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x079a  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x07de A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:325:0x08bc A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0942 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:342:0x0995 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00ce A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:362:0x09f7  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x0a4a  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0a58  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x0a5e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:441:0x0b7d A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0b89 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:445:0x0b9d  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x0c10 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:485:0x0d26 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:493:0x0d4e A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0d67 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x011b A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0126  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x014a A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x014f A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x016b A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x017d  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01b5 A[Catch: Exception -> 0x0dce, TRY_ENTER, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01f4 A[Catch: Exception -> 0x0dce, TryCatch #1 {Exception -> 0x0dce, blocks: (B:10:0x0024, B:12:0x004b, B:14:0x004f, B:16:0x005c, B:18:0x0064, B:20:0x0079, B:21:0x0080, B:22:0x0086, B:26:0x0098, B:30:0x00a8, B:33:0x00b6, B:35:0x00ce, B:37:0x00de, B:39:0x00e4, B:40:0x00ea, B:42:0x00f0, B:48:0x00ff, B:50:0x011b, B:60:0x0139, B:62:0x013f, B:65:0x014a, B:66:0x014f, B:67:0x0155, B:69:0x015b, B:74:0x0163, B:76:0x016b, B:85:0x0187, B:86:0x0191, B:87:0x019b, B:89:0x01a8, B:92:0x01b5, B:94:0x01bd, B:95:0x01ca, B:97:0x01e9, B:99:0x01f4, B:103:0x0204, B:106:0x020f, B:107:0x0230, B:108:0x0272, B:111:0x028f, B:116:0x02b2, B:117:0x02c7, B:119:0x02cc, B:120:0x02e1, B:121:0x02f5, B:122:0x0307, B:124:0x032c, B:126:0x0344, B:131:0x034e, B:132:0x0355, B:136:0x0364, B:137:0x0379, B:139:0x037e, B:140:0x0393, B:141:0x03a7, B:143:0x03ad, B:144:0x03b7, B:147:0x03cd, B:157:0x03e7, B:159:0x0401, B:162:0x0439, B:164:0x0445, B:165:0x0461, B:167:0x0479, B:168:0x0485, B:170:0x0489, B:175:0x04bc, B:178:0x04d6, B:182:0x04e5, B:184:0x04ff, B:186:0x0552, B:187:0x055e, B:188:0x0577, B:190:0x0592, B:197:0x05d0, B:199:0x05e5, B:200:0x05f4, B:201:0x05fc, B:202:0x0629, B:204:0x0640, B:205:0x064f, B:206:0x0657, B:209:0x068d, B:211:0x0697, B:212:0x06a6, B:213:0x06ae, B:219:0x06e8, B:221:0x06ee, B:231:0x070b, B:233:0x0713, B:243:0x0730, B:246:0x073c, B:250:0x0749, B:270:0x0781, B:276:0x07a4, B:278:0x07de, B:282:0x07ed, B:285:0x0801, B:288:0x0812, B:290:0x0818, B:293:0x0828, B:295:0x0833, B:298:0x083b, B:300:0x0841, B:302:0x0847, B:304:0x0855, B:306:0x085d, B:311:0x0876, B:313:0x087c, B:315:0x0882, B:317:0x0890, B:319:0x0898, B:325:0x08bc, B:327:0x08cf, B:329:0x08d5, B:331:0x08e0, B:334:0x0944, B:336:0x0948, B:338:0x0950, B:340:0x096b, B:342:0x0995, B:344:0x09a2, B:367:0x0a02, B:377:0x0a1d, B:382:0x0a2e, B:385:0x0a3c, B:389:0x0a4e, B:396:0x0a62, B:400:0x0a74, B:402:0x0a7c, B:404:0x0ab2, B:406:0x0ab7, B:408:0x0abf, B:410:0x0ac5, B:412:0x0acd, B:416:0x0ada, B:417:0x0af6, B:419:0x0b05, B:420:0x0b0c, B:422:0x0b16, B:423:0x0b1f, B:425:0x0b25, B:427:0x0b2d, B:436:0x0b60, B:437:0x0b6a, B:441:0x0b7d, B:444:0x0b89, B:447:0x0ba0, B:454:0x0bc6, B:456:0x0bdf, B:457:0x0bec, B:458:0x0c09, B:460:0x0c10, B:462:0x0c14, B:464:0x0c1f, B:466:0x0c25, B:468:0x0c32, B:470:0x0c50, B:472:0x0c60, B:474:0x0c7f, B:476:0x0c8b, B:478:0x0cbf, B:479:0x0cd3, B:485:0x0d26, B:487:0x0d2c, B:489:0x0d34, B:491:0x0d3a, B:493:0x0d4e, B:494:0x0d67, B:495:0x0d7f, B:261:0x0769), top: B:503:0x0024, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showOrUpdateNotification(boolean notifyAboutLast) {
        long override_dialog_id;
        long userId;
        long userId2;
        TLRPC.Chat chat;
        int notifyOverride;
        boolean notifyDisabled;
        boolean value;
        String chatName;
        boolean replace;
        boolean z;
        int notifyOverride2;
        String name;
        String detailText;
        long chatId;
        long override_dialog_id2;
        SharedPreferences preferences;
        String str;
        String detailText2;
        long dialog_id;
        boolean z2;
        String lastMessage;
        String name2;
        TLRPC.Chat chat2;
        String detailText3;
        SharedPreferences preferences2;
        long dialog_id2;
        boolean isInApp;
        boolean customIsInternalSound;
        int chatType;
        Integer customLedColor;
        boolean isDefault;
        int customImportance;
        int customImportance2;
        String customSoundPath;
        String lastMessage2;
        String customSoundPath2;
        boolean z3;
        String lastMessage3;
        boolean vibrateOnlyIfSilent;
        int silent;
        int ledColor;
        String soundPath;
        int vibrate;
        boolean isDefault2;
        boolean vibrateOnlyIfSilent2;
        String customSoundPath3;
        boolean isDefault3;
        boolean isDefault4;
        boolean isDefault5;
        int vibrate2;
        int importance;
        String soundPath2;
        int ledColor2;
        int vibrate3;
        long userId3;
        String defaultPath;
        String customSoundPath4;
        TLRPC.FileLocation photoPath;
        TLRPC.User user;
        TLRPC.Chat chat3;
        long[] vibrationPattern;
        int configImportance;
        boolean z4;
        boolean z5;
        String defaultPath2;
        int importance2;
        int vibrate4;
        MessageObject lastMessageObject;
        SharedPreferences preferences3;
        String soundPath3;
        boolean hasCallback;
        int ledColor3;
        long dialog_id3;
        MessageObject lastMessageObject2;
        SharedPreferences preferences4;
        Intent dismissIntent;
        int size;
        String soundPath4;
        long dialog_id4;
        String lastMessage4;
        Uri sound;
        String defaultPath3;
        long chatId2;
        int vibrate5;
        int mode;
        int vibrate6;
        int vibrate7;
        boolean isDefault6;
        String soundPath5;
        String soundPath6;
        String soundPath7;
        String customSoundPath5;
        boolean customIsInternalSound2;
        Integer customLedColor2;
        int notifyDelay;
        int notifyMaxCount;
        long userId4;
        if (!getUserConfig().isClientActivated() || this.pushMessages.isEmpty() || (!SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount)) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            MessageObject lastMessageObject3 = this.pushMessages.get(0);
            SharedPreferences preferences5 = getAccountInstance().getNotificationsSettings();
            int dismissDate = preferences5.getInt("dismissDate", 0);
            if (lastMessageObject3.messageOwner.date <= dismissDate) {
                dismissNotification();
                return;
            }
            long override_dialog_id3 = lastMessageObject3.getDialogId();
            if (!lastMessageObject3.messageOwner.mentioned) {
                override_dialog_id = override_dialog_id3;
            } else {
                override_dialog_id = lastMessageObject3.getFromChatId();
            }
            lastMessageObject3.getId();
            boolean isChannel = false;
            long chatId3 = lastMessageObject3.messageOwner.peer_id.chat_id != 0 ? lastMessageObject3.messageOwner.peer_id.chat_id : lastMessageObject3.messageOwner.peer_id.channel_id;
            long userId5 = lastMessageObject3.messageOwner.peer_id.user_id;
            if (!lastMessageObject3.isFromUser()) {
                userId4 = userId5;
            } else {
                if (userId5 != 0 && userId5 != getUserConfig().getClientUserId()) {
                    userId4 = userId5;
                }
                userId = lastMessageObject3.messageOwner.from_id.user_id;
                TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(userId));
                if (chatId3 != 0) {
                    userId2 = userId;
                    chat = null;
                } else {
                    userId2 = userId;
                    TLRPC.Chat chat4 = getMessagesController().getChat(Long.valueOf(chatId3));
                    if (chat4 == null && lastMessageObject3.isFcmMessage()) {
                        chat = chat4;
                        isChannel = lastMessageObject3.localChannel;
                    } else {
                        boolean isChannel2 = ChatObject.isChannel(chat4);
                        chat = chat4;
                        isChannel = isChannel2 && !chat4.megagroup;
                    }
                }
                boolean isInternalSoundFile = false;
                notifyOverride = getNotifyOverride(preferences5, override_dialog_id);
                notifyDisabled = false;
                if (notifyOverride != -1) {
                    value = isGlobalNotificationsEnabled(override_dialog_id3, Boolean.valueOf(isChannel));
                } else {
                    value = notifyOverride != 2;
                }
                if (((chatId3 != 0 || chat != null) && user2 != null) || !lastMessageObject3.isFcmMessage()) {
                    replace = true;
                    if (chat == null) {
                        chatName = chat.title;
                    } else {
                        chatName = UserObject.getUserName(user2);
                    }
                } else {
                    replace = true;
                    chatName = lastMessageObject3.localName;
                }
                if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                    z = false;
                    boolean passcode = z;
                    if (DialogObject.isEncryptedDialog(override_dialog_id3)) {
                        notifyOverride2 = notifyOverride;
                        if (this.pushDialogs.size() <= 1 && !passcode) {
                            name = chatName;
                            if (UserConfig.getActivatedAccountsCount() > 1) {
                                if (this.pushDialogs.size() == 1) {
                                    detailText = UserObject.getFirstName(getUserConfig().getCurrentUser());
                                } else {
                                    detailText = UserObject.getFirstName(getUserConfig().getCurrentUser()) + "・";
                                }
                            } else {
                                detailText = "";
                            }
                            chatId = chatId3;
                            if (this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT >= 23) {
                                str = "color_";
                                override_dialog_id2 = override_dialog_id;
                                preferences = preferences5;
                                detailText2 = detailText;
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                                if (this.pushMessages.size() == 1) {
                                    MessageObject messageObject = this.pushMessages.get(0);
                                    boolean[] text = new boolean[1];
                                    dialog_id = override_dialog_id3;
                                    String stringForMessage = getStringForMessage(messageObject, false, text, null);
                                    lastMessage = stringForMessage;
                                    String message = stringForMessage;
                                    z2 = isSilentMessage(messageObject);
                                    if (message == null) {
                                        return;
                                    }
                                    if (replace) {
                                        if (chat != null) {
                                            message = message.replace(" @ " + name, "");
                                        } else if (text[0]) {
                                            message = message.replace(name + ": ", "");
                                        } else {
                                            message = message.replace(name + " ", "");
                                        }
                                    }
                                    mBuilder.setContentText(message);
                                    mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
                                } else {
                                    String lastMessage5 = null;
                                    dialog_id = override_dialog_id3;
                                    mBuilder.setContentText(detailText2);
                                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                                    inboxStyle.setBigContentTitle(name);
                                    int count = Math.min(10, this.pushMessages.size());
                                    boolean[] text2 = new boolean[1];
                                    int i = 0;
                                    boolean z6 = true;
                                    while (i < count) {
                                        int count2 = count;
                                        MessageObject messageObject2 = this.pushMessages.get(i);
                                        NotificationCompat.Builder mBuilder2 = mBuilder;
                                        String detailText4 = detailText2;
                                        String message2 = getStringForMessage(messageObject2, false, text2, null);
                                        if (message2 != null && messageObject2.messageOwner.date > dismissDate) {
                                            if (z6) {
                                                lastMessage5 = message2;
                                                z6 = isSilentMessage(messageObject2);
                                            }
                                            if (this.pushDialogs.size() == 1 && replace) {
                                                if (chat != null) {
                                                    message2 = message2.replace(" @ " + name, "");
                                                } else if (text2[0]) {
                                                    message2 = message2.replace(name + ": ", "");
                                                } else {
                                                    message2 = message2.replace(name + " ", "");
                                                }
                                            }
                                            inboxStyle.addLine(message2);
                                        }
                                        i++;
                                        count = count2;
                                        mBuilder = mBuilder2;
                                        detailText2 = detailText4;
                                    }
                                    inboxStyle.setSummaryText(detailText2);
                                    mBuilder = mBuilder;
                                    mBuilder.setStyle(inboxStyle);
                                    z2 = z6;
                                    lastMessage = lastMessage5;
                                }
                                if (notifyAboutLast || !value || MediaController.getInstance().isRecordingAudio() || z2) {
                                    notifyDisabled = true;
                                }
                                if (!notifyDisabled || dialog_id != override_dialog_id2 || chat == null) {
                                    name2 = name;
                                    chat2 = chat;
                                    detailText3 = detailText2;
                                    preferences2 = preferences;
                                    dialog_id2 = dialog_id;
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(ContentMetadata.KEY_CUSTOM_PREFIX);
                                    dialog_id2 = dialog_id;
                                    sb.append(dialog_id2);
                                    preferences2 = preferences;
                                    if (preferences2.getBoolean(sb.toString(), false)) {
                                        int notifyMaxCount2 = preferences2.getInt("smart_max_count_" + dialog_id2, 2);
                                        notifyDelay = preferences2.getInt("smart_delay_" + dialog_id2, 180);
                                        notifyMaxCount = notifyMaxCount2;
                                    } else {
                                        notifyMaxCount = 2;
                                        notifyDelay = 180;
                                    }
                                    if (notifyMaxCount != 0) {
                                        Point dialogInfo = this.smartNotificationsDialogs.get(dialog_id2);
                                        if (dialogInfo == null) {
                                            detailText3 = detailText2;
                                            name2 = name;
                                            chat2 = chat;
                                            this.smartNotificationsDialogs.put(dialog_id2, new Point(1, (int) (SystemClock.elapsedRealtime() / 1000)));
                                        } else {
                                            name2 = name;
                                            chat2 = chat;
                                            detailText3 = detailText2;
                                            int lastTime = dialogInfo.y;
                                            if (lastTime + notifyDelay < SystemClock.elapsedRealtime() / 1000) {
                                                dialogInfo.set(1, (int) (SystemClock.elapsedRealtime() / 1000));
                                            } else {
                                                int count3 = dialogInfo.x;
                                                if (count3 < notifyMaxCount) {
                                                    dialogInfo.set(count3 + 1, (int) (SystemClock.elapsedRealtime() / 1000));
                                                } else {
                                                    notifyDisabled = true;
                                                }
                                            }
                                        }
                                    } else {
                                        name2 = name;
                                        chat2 = chat;
                                        detailText3 = detailText2;
                                    }
                                }
                                if (!notifyDisabled) {
                                    if (!preferences2.getBoolean("sound_enabled_" + dialog_id2, true)) {
                                        notifyDisabled = true;
                                    }
                                }
                                String defaultPath4 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                isInApp = !ApplicationLoader.mainInterfacePaused;
                                if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
                                    int customVibrate = preferences2.getInt("vibrate_" + dialog_id2, 0);
                                    int customImportance3 = preferences2.getInt("priority_" + dialog_id2, 3);
                                    isDefault = true;
                                    chatType = 1;
                                    long soundDocumentId = preferences2.getLong("sound_document_id_" + dialog_id2, 0L);
                                    lastMessage2 = lastMessage;
                                    if (soundDocumentId != 0) {
                                        customIsInternalSound2 = true;
                                        customSoundPath5 = getMediaDataController().ringtoneDataStore.getSoundPath(soundDocumentId);
                                    } else {
                                        customSoundPath5 = preferences2.getString("sound_path_" + dialog_id2, null);
                                        customIsInternalSound2 = false;
                                    }
                                    customIsInternalSound = customIsInternalSound2;
                                    StringBuilder sb2 = new StringBuilder();
                                    String customSoundPath6 = customSoundPath5;
                                    String customSoundPath7 = str;
                                    sb2.append(customSoundPath7);
                                    sb2.append(dialog_id2);
                                    if (preferences2.contains(sb2.toString())) {
                                        customLedColor2 = Integer.valueOf(preferences2.getInt(customSoundPath7 + dialog_id2, 0));
                                    } else {
                                        customLedColor2 = null;
                                    }
                                    customLedColor = customLedColor2;
                                    customImportance2 = customImportance3;
                                    customImportance = customVibrate;
                                    customSoundPath = customSoundPath6;
                                } else {
                                    isDefault = true;
                                    chatType = 1;
                                    lastMessage2 = lastMessage;
                                    customLedColor = null;
                                    customImportance2 = 3;
                                    customIsInternalSound = false;
                                    customImportance = 0;
                                    customSoundPath = null;
                                }
                                customSoundPath2 = customSoundPath;
                                if (chatId != 0) {
                                    if (isChannel) {
                                        vibrateOnlyIfSilent = false;
                                        long soundDocumentId2 = preferences2.getLong("ChannelSoundDocId", 0L);
                                        lastMessage3 = lastMessage2;
                                        if (soundDocumentId2 != 0) {
                                            isInternalSoundFile = true;
                                            soundPath7 = getMediaDataController().ringtoneDataStore.getSoundPath(soundDocumentId2);
                                        } else {
                                            soundPath7 = preferences2.getString("ChannelSoundPath", defaultPath4);
                                        }
                                        int vibrate8 = preferences2.getInt("vibrate_channel", 0);
                                        int importance3 = preferences2.getInt("priority_channel", 1);
                                        chatType = 2;
                                        z3 = z2;
                                        isDefault2 = isDefault;
                                        vibrate = vibrate8;
                                        soundPath = soundPath7;
                                        ledColor = preferences2.getInt("ChannelLed", -16776961);
                                        silent = importance3;
                                    } else {
                                        lastMessage3 = lastMessage2;
                                        vibrateOnlyIfSilent = false;
                                        long soundDocumentId3 = preferences2.getLong("GroupSoundDocId", 0L);
                                        z3 = z2;
                                        isDefault2 = isDefault;
                                        if (soundDocumentId3 != 0) {
                                            isInternalSoundFile = true;
                                            soundPath6 = getMediaDataController().ringtoneDataStore.getSoundPath(soundDocumentId3);
                                        } else {
                                            soundPath6 = preferences2.getString("GroupSoundPath", defaultPath4);
                                        }
                                        int vibrate9 = preferences2.getInt("vibrate_group", 0);
                                        int importance4 = preferences2.getInt("priority_group", 1);
                                        chatType = 0;
                                        vibrate = vibrate9;
                                        soundPath = soundPath6;
                                        ledColor = preferences2.getInt("GroupLed", -16776961);
                                        silent = importance4;
                                    }
                                } else {
                                    lastMessage3 = lastMessage2;
                                    vibrateOnlyIfSilent = false;
                                    z3 = z2;
                                    isDefault2 = isDefault;
                                    if (userId2 == 0) {
                                        vibrate = 0;
                                        soundPath = null;
                                        ledColor = -16776961;
                                        silent = 0;
                                    } else {
                                        long soundDocumentId4 = preferences2.getLong("GlobalSoundDocId", 0L);
                                        if (soundDocumentId4 != 0) {
                                            isInternalSoundFile = true;
                                            soundPath5 = getMediaDataController().ringtoneDataStore.getSoundPath(soundDocumentId4);
                                        } else {
                                            soundPath5 = preferences2.getString("GlobalSoundPath", defaultPath4);
                                        }
                                        int vibrate10 = preferences2.getInt("vibrate_messages", 0);
                                        int importance5 = preferences2.getInt("priority_messages", 1);
                                        chatType = 1;
                                        vibrate = vibrate10;
                                        soundPath = soundPath5;
                                        ledColor = preferences2.getInt("MessagesLed", -16776961);
                                        silent = importance5;
                                    }
                                }
                                if (vibrate != 4) {
                                    vibrateOnlyIfSilent2 = vibrateOnlyIfSilent;
                                } else {
                                    vibrateOnlyIfSilent2 = true;
                                    vibrate = 0;
                                }
                                if (!TextUtils.isEmpty(customSoundPath2)) {
                                    customSoundPath3 = customSoundPath2;
                                    if (!TextUtils.equals(soundPath, customSoundPath3)) {
                                        isInternalSoundFile = customIsInternalSound;
                                        soundPath = customSoundPath3;
                                        isDefault2 = false;
                                    }
                                } else {
                                    customSoundPath3 = customSoundPath2;
                                }
                                boolean isDefault7 = isDefault2;
                                if (customImportance2 == 3 && silent != customImportance2) {
                                    silent = customImportance2;
                                    isDefault3 = false;
                                } else {
                                    isDefault3 = isDefault7;
                                }
                                if (customLedColor != null) {
                                    isDefault6 = isDefault3;
                                    if (customLedColor.intValue() != ledColor) {
                                        ledColor = customLedColor.intValue();
                                        isDefault4 = false;
                                        if (customImportance == 0) {
                                            isDefault5 = isDefault4;
                                            if (customImportance != 4 && customImportance != vibrate) {
                                                vibrate = customImportance;
                                                isDefault5 = false;
                                            }
                                        } else {
                                            isDefault5 = isDefault4;
                                        }
                                        if (!isInApp) {
                                            int vibrate11 = vibrate;
                                            if (!preferences2.getBoolean("EnableInAppSounds", true)) {
                                                soundPath = null;
                                            }
                                            if (preferences2.getBoolean("EnableInAppVibrate", true)) {
                                                vibrate7 = vibrate11;
                                            } else {
                                                vibrate7 = 2;
                                            }
                                            int vibrate12 = vibrate7;
                                            if (!preferences2.getBoolean("EnableInAppPriority", false)) {
                                                silent = 0;
                                                vibrate = vibrate12;
                                            } else if (silent != 2) {
                                                vibrate = vibrate12;
                                            } else {
                                                silent = 1;
                                                vibrate = vibrate12;
                                            }
                                        }
                                        if (vibrateOnlyIfSilent2 || vibrate == 2) {
                                            vibrate5 = vibrate;
                                        } else {
                                            try {
                                                mode = audioManager.getRingerMode();
                                            } catch (Exception e) {
                                                vibrate5 = vibrate;
                                                FileLog.e(e);
                                            }
                                            if (mode != 0) {
                                                vibrate6 = vibrate;
                                                if (mode != 1) {
                                                    vibrate2 = 2;
                                                    if (notifyDisabled) {
                                                        int i2 = silent;
                                                        importance = vibrate2;
                                                        vibrate3 = i2;
                                                        int i3 = ledColor;
                                                        soundPath2 = soundPath;
                                                        ledColor2 = i3;
                                                    } else {
                                                        importance = 0;
                                                        vibrate3 = 0;
                                                        soundPath2 = null;
                                                        ledColor2 = 0;
                                                    }
                                                    Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                    StringBuilder sb3 = new StringBuilder();
                                                    sb3.append("com.tmessages.openchat");
                                                    int vibrate13 = importance;
                                                    String customSoundPath8 = customSoundPath3;
                                                    sb3.append(Math.random());
                                                    sb3.append(Integer.MAX_VALUE);
                                                    intent.setAction(sb3.toString());
                                                    intent.setFlags(ConnectionsManager.FileTypeFile);
                                                    if (DialogObject.isEncryptedDialog(dialog_id2)) {
                                                        if (this.pushDialogs.size() != 1) {
                                                            chatId2 = userId2;
                                                        } else if (chatId != 0) {
                                                            intent.putExtra("chatId", chatId);
                                                            chatId2 = userId2;
                                                        } else if (userId2 != 0) {
                                                            chatId2 = userId2;
                                                            intent.putExtra("userId", chatId2);
                                                        } else {
                                                            chatId2 = userId2;
                                                        }
                                                        if (AndroidUtilities.needShowPasscode()) {
                                                            customSoundPath4 = customSoundPath8;
                                                            userId3 = chatId2;
                                                            defaultPath = defaultPath4;
                                                            user = user2;
                                                            chat3 = chat2;
                                                        } else if (SharedConfig.isWaitingForPasscodeEnter) {
                                                            customSoundPath4 = customSoundPath8;
                                                            userId3 = chatId2;
                                                            defaultPath = defaultPath4;
                                                            user = user2;
                                                            chat3 = chat2;
                                                        } else {
                                                            customSoundPath4 = customSoundPath8;
                                                            if (this.pushDialogs.size() != 1 || Build.VERSION.SDK_INT >= 28) {
                                                                userId3 = chatId2;
                                                                defaultPath = defaultPath4;
                                                                user = user2;
                                                                chat3 = chat2;
                                                            } else if (chat2 != null) {
                                                                chat3 = chat2;
                                                                if (chat3.photo == null || chat3.photo.photo_small == null) {
                                                                    userId3 = chatId2;
                                                                } else {
                                                                    userId3 = chatId2;
                                                                    if (chat3.photo.photo_small.volume_id != 0 && chat3.photo.photo_small.local_id != 0) {
                                                                        defaultPath = defaultPath4;
                                                                        user = user2;
                                                                        photoPath = chat3.photo.photo_small;
                                                                        intent.putExtra("currentAccount", this.currentAccount);
                                                                        long dialog_id5 = dialog_id2;
                                                                        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, C.BUFFER_FLAG_ENCRYPTED);
                                                                        MessageObject lastMessageObject4 = lastMessageObject3;
                                                                        mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject4.messageOwner.date * 1000).setColor(-15618822);
                                                                        mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                                                        if (chat3 == null || user == null || user.phone == null || user.phone.length() <= 0) {
                                                                            vibrationPattern = null;
                                                                        } else {
                                                                            StringBuilder sb4 = new StringBuilder();
                                                                            vibrationPattern = null;
                                                                            sb4.append("tel:+");
                                                                            sb4.append(user.phone);
                                                                            mBuilder.addPerson(sb4.toString());
                                                                        }
                                                                        Intent dismissIntent2 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                        dismissIntent2.putExtra("messageDate", lastMessageObject4.messageOwner.date);
                                                                        dismissIntent2.putExtra("currentAccount", this.currentAccount);
                                                                        Uri sound2 = null;
                                                                        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent2, 134217728));
                                                                        if (photoPath != null) {
                                                                            BitmapDrawable img = ImageLoader.getInstance().getImageFromMemory(photoPath, null, "50_50");
                                                                            if (img != null) {
                                                                                mBuilder.setLargeIcon(img.getBitmap());
                                                                            } else {
                                                                                try {
                                                                                    File file = getFileLoader().getPathToAttach(photoPath, true);
                                                                                    if (file.exists()) {
                                                                                        float scaleFactor = 160.0f / AndroidUtilities.dp(50.0f);
                                                                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                                                                        try {
                                                                                            options.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                                                                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                                                                            if (bitmap != null) {
                                                                                                mBuilder.setLargeIcon(bitmap);
                                                                                            }
                                                                                        } catch (Throwable th) {
                                                                                        }
                                                                                    }
                                                                                } catch (Throwable th2) {
                                                                                }
                                                                            }
                                                                        }
                                                                        if (notifyAboutLast) {
                                                                            z4 = z3;
                                                                            if (z4) {
                                                                                configImportance = 0;
                                                                            } else {
                                                                                if (vibrate3 == 0) {
                                                                                    mBuilder.setPriority(0);
                                                                                    configImportance = 0;
                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                        configImportance = 3;
                                                                                    }
                                                                                } else {
                                                                                    configImportance = 0;
                                                                                    if (vibrate3 != 1 && vibrate3 != 2) {
                                                                                        if (vibrate3 == 4) {
                                                                                            mBuilder.setPriority(-2);
                                                                                            if (Build.VERSION.SDK_INT >= 26) {
                                                                                                configImportance = 1;
                                                                                            }
                                                                                        } else if (vibrate3 == 5) {
                                                                                            mBuilder.setPriority(-1);
                                                                                            if (Build.VERSION.SDK_INT >= 26) {
                                                                                                configImportance = 2;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    mBuilder.setPriority(1);
                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                        configImportance = 4;
                                                                                    }
                                                                                }
                                                                                if (!z4 || notifyDisabled) {
                                                                                    importance2 = vibrate3;
                                                                                    defaultPath2 = defaultPath;
                                                                                    vibrate4 = vibrate13;
                                                                                    z5 = z4;
                                                                                    long[] vibrationPattern2 = {0, 0};
                                                                                    mBuilder.setVibrate(vibrationPattern2);
                                                                                    vibrationPattern = vibrationPattern2;
                                                                                } else {
                                                                                    if (isInApp && !preferences2.getBoolean("EnableInAppPreview", true)) {
                                                                                        importance2 = vibrate3;
                                                                                        String str2 = lastMessage3;
                                                                                        z5 = z4;
                                                                                        lastMessage4 = str2;
                                                                                        if (soundPath2 != null || soundPath2.equals("NoSound")) {
                                                                                            defaultPath2 = defaultPath;
                                                                                        } else if (Build.VERSION.SDK_INT >= 26) {
                                                                                            if (!soundPath2.equals("Default")) {
                                                                                                defaultPath3 = defaultPath;
                                                                                                if (!soundPath2.equals(defaultPath3)) {
                                                                                                    if (isInternalSoundFile) {
                                                                                                        Uri sound3 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", new File(soundPath2));
                                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", sound3, 1);
                                                                                                        defaultPath2 = defaultPath3;
                                                                                                        sound = sound3;
                                                                                                    } else {
                                                                                                        defaultPath2 = defaultPath3;
                                                                                                        sound = Uri.parse(soundPath2);
                                                                                                    }
                                                                                                    if (ledColor2 != 0) {
                                                                                                        mBuilder.setLights(ledColor2, 1000, 1000);
                                                                                                    }
                                                                                                    vibrate4 = vibrate13;
                                                                                                    if (vibrate4 == 2) {
                                                                                                        long[] vibrationPattern3 = {0, 0};
                                                                                                        mBuilder.setVibrate(vibrationPattern3);
                                                                                                        vibrationPattern = vibrationPattern3;
                                                                                                        sound2 = sound;
                                                                                                    } else if (vibrate4 == 1) {
                                                                                                        long[] vibrationPattern4 = {0, 100, 0, 100};
                                                                                                        mBuilder.setVibrate(vibrationPattern4);
                                                                                                        vibrationPattern = vibrationPattern4;
                                                                                                        sound2 = sound;
                                                                                                    } else {
                                                                                                        if (vibrate4 != 0 && vibrate4 != 4) {
                                                                                                            if (vibrate4 == 3) {
                                                                                                                long[] vibrationPattern5 = {0, 1000};
                                                                                                                mBuilder.setVibrate(vibrationPattern5);
                                                                                                                vibrationPattern = vibrationPattern5;
                                                                                                                sound2 = sound;
                                                                                                            } else {
                                                                                                                sound2 = sound;
                                                                                                            }
                                                                                                        }
                                                                                                        mBuilder.setDefaults(2);
                                                                                                        vibrationPattern = new long[0];
                                                                                                        sound2 = sound;
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                defaultPath3 = defaultPath;
                                                                                            }
                                                                                            defaultPath2 = defaultPath3;
                                                                                            sound = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                            if (ledColor2 != 0) {
                                                                                            }
                                                                                            vibrate4 = vibrate13;
                                                                                            if (vibrate4 == 2) {
                                                                                            }
                                                                                        } else {
                                                                                            String defaultPath5 = defaultPath;
                                                                                            if (soundPath2.equals(defaultPath5)) {
                                                                                                mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                                                                defaultPath2 = defaultPath5;
                                                                                            } else if (Build.VERSION.SDK_INT < 24 || !soundPath2.startsWith("file://") || AndroidUtilities.isInternalUri(Uri.parse(soundPath2))) {
                                                                                                defaultPath2 = defaultPath5;
                                                                                                mBuilder.setSound(Uri.parse(soundPath2), 5);
                                                                                            } else {
                                                                                                try {
                                                                                                    defaultPath2 = defaultPath5;
                                                                                                } catch (Exception e2) {
                                                                                                    defaultPath2 = defaultPath5;
                                                                                                }
                                                                                                try {
                                                                                                    Uri uri = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", new File(soundPath2.replace("file://", "")));
                                                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uri, 1);
                                                                                                    mBuilder.setSound(uri, 5);
                                                                                                } catch (Exception e3) {
                                                                                                    mBuilder.setSound(Uri.parse(soundPath2), 5);
                                                                                                    sound = null;
                                                                                                    if (ledColor2 != 0) {
                                                                                                    }
                                                                                                    vibrate4 = vibrate13;
                                                                                                    if (vibrate4 == 2) {
                                                                                                    }
                                                                                                    boolean hasCallback2 = false;
                                                                                                    if (!AndroidUtilities.needShowPasscode()) {
                                                                                                    }
                                                                                                    ledColor3 = ledColor2;
                                                                                                    soundPath3 = soundPath2;
                                                                                                    preferences3 = preferences2;
                                                                                                    dialog_id3 = dialog_id5;
                                                                                                    lastMessageObject = lastMessageObject4;
                                                                                                    hasCallback = false;
                                                                                                    if (!hasCallback) {
                                                                                                    }
                                                                                                    showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                                                                    scheduleNotificationRepeat();
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        sound = null;
                                                                                        if (ledColor2 != 0) {
                                                                                        }
                                                                                        vibrate4 = vibrate13;
                                                                                        if (vibrate4 == 2) {
                                                                                        }
                                                                                    }
                                                                                    if (lastMessage3.length() <= 100) {
                                                                                        importance2 = vibrate3;
                                                                                        String lastMessage6 = lastMessage3;
                                                                                        z5 = z4;
                                                                                        lastMessage4 = lastMessage6;
                                                                                    } else {
                                                                                        StringBuilder sb5 = new StringBuilder();
                                                                                        importance2 = vibrate3;
                                                                                        String lastMessage7 = lastMessage3;
                                                                                        z5 = z4;
                                                                                        sb5.append(lastMessage7.substring(0, 100).replace('\n', ' ').trim());
                                                                                        sb5.append("...");
                                                                                        lastMessage4 = sb5.toString();
                                                                                    }
                                                                                    mBuilder.setTicker(lastMessage4);
                                                                                    if (soundPath2 != null) {
                                                                                    }
                                                                                    defaultPath2 = defaultPath;
                                                                                    sound = null;
                                                                                    if (ledColor2 != 0) {
                                                                                    }
                                                                                    vibrate4 = vibrate13;
                                                                                    if (vibrate4 == 2) {
                                                                                    }
                                                                                }
                                                                                boolean hasCallback22 = false;
                                                                                if (!AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || lastMessageObject4.getDialogId() != 777000) {
                                                                                    ledColor3 = ledColor2;
                                                                                    soundPath3 = soundPath2;
                                                                                    preferences3 = preferences2;
                                                                                    dialog_id3 = dialog_id5;
                                                                                    lastMessageObject = lastMessageObject4;
                                                                                } else if (lastMessageObject4.messageOwner.reply_markup == null) {
                                                                                    ledColor3 = ledColor2;
                                                                                    soundPath3 = soundPath2;
                                                                                    preferences3 = preferences2;
                                                                                    dialog_id3 = dialog_id5;
                                                                                    lastMessageObject = lastMessageObject4;
                                                                                } else {
                                                                                    ArrayList<TLRPC.TL_keyboardButtonRow> rows = lastMessageObject4.messageOwner.reply_markup.rows;
                                                                                    int a = 0;
                                                                                    int size2 = rows.size();
                                                                                    while (a < size2) {
                                                                                        boolean hasCallback3 = hasCallback22;
                                                                                        TLRPC.TL_keyboardButtonRow row = rows.get(a);
                                                                                        ArrayList<TLRPC.TL_keyboardButtonRow> rows2 = rows;
                                                                                        int size22 = row.buttons.size();
                                                                                        int b = ledColor2;
                                                                                        int ledColor4 = 0;
                                                                                        while (ledColor4 < size22) {
                                                                                            int size23 = size22;
                                                                                            TLRPC.KeyboardButton button = row.buttons.get(ledColor4);
                                                                                            TLRPC.TL_keyboardButtonRow row2 = row;
                                                                                            if (button instanceof TLRPC.TL_keyboardButtonCallback) {
                                                                                                soundPath4 = soundPath2;
                                                                                                size = size2;
                                                                                                Intent callbackIntent = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                                                callbackIntent.putExtra("currentAccount", this.currentAccount);
                                                                                                dismissIntent = dismissIntent2;
                                                                                                dialog_id4 = dialog_id5;
                                                                                                callbackIntent.putExtra("did", dialog_id4);
                                                                                                if (button.data == null) {
                                                                                                    preferences4 = preferences2;
                                                                                                } else {
                                                                                                    preferences4 = preferences2;
                                                                                                    callbackIntent.putExtra("data", button.data);
                                                                                                }
                                                                                                callbackIntent.putExtra("mid", lastMessageObject4.getId());
                                                                                                String str3 = button.text;
                                                                                                Context context = ApplicationLoader.applicationContext;
                                                                                                int i4 = this.lastButtonId;
                                                                                                lastMessageObject2 = lastMessageObject4;
                                                                                                this.lastButtonId = i4 + 1;
                                                                                                mBuilder.addAction(0, str3, PendingIntent.getBroadcast(context, i4, callbackIntent, 134217728));
                                                                                                hasCallback3 = true;
                                                                                            } else {
                                                                                                soundPath4 = soundPath2;
                                                                                                size = size2;
                                                                                                dismissIntent = dismissIntent2;
                                                                                                preferences4 = preferences2;
                                                                                                dialog_id4 = dialog_id5;
                                                                                                lastMessageObject2 = lastMessageObject4;
                                                                                            }
                                                                                            ledColor4++;
                                                                                            size22 = size23;
                                                                                            row = row2;
                                                                                            soundPath2 = soundPath4;
                                                                                            preferences2 = preferences4;
                                                                                            lastMessageObject4 = lastMessageObject2;
                                                                                            dialog_id5 = dialog_id4;
                                                                                            size2 = size;
                                                                                            dismissIntent2 = dismissIntent;
                                                                                        }
                                                                                        a++;
                                                                                        ledColor2 = b;
                                                                                        rows = rows2;
                                                                                        hasCallback22 = hasCallback3;
                                                                                        lastMessageObject4 = lastMessageObject4;
                                                                                        dialog_id5 = dialog_id5;
                                                                                        size2 = size2;
                                                                                        dismissIntent2 = dismissIntent2;
                                                                                    }
                                                                                    hasCallback = hasCallback22;
                                                                                    ledColor3 = ledColor2;
                                                                                    soundPath3 = soundPath2;
                                                                                    preferences3 = preferences2;
                                                                                    dialog_id3 = dialog_id5;
                                                                                    lastMessageObject = lastMessageObject4;
                                                                                    if (!hasCallback && Build.VERSION.SDK_INT < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                                                        Intent replyIntent = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                                                        replyIntent.putExtra("currentAccount", this.currentAccount);
                                                                                        if (Build.VERSION.SDK_INT > 19) {
                                                                                            mBuilder.addAction(org.telegram.messenger.beta.R.drawable.ic_ab_reply2, LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, replyIntent, 134217728));
                                                                                        } else {
                                                                                            mBuilder.addAction(org.telegram.messenger.beta.R.drawable.ic_ab_reply, LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, replyIntent, 134217728));
                                                                                        }
                                                                                    }
                                                                                    showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                                                    scheduleNotificationRepeat();
                                                                                }
                                                                                hasCallback = false;
                                                                                if (!hasCallback) {
                                                                                    Intent replyIntent2 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                                                    replyIntent2.putExtra("currentAccount", this.currentAccount);
                                                                                    if (Build.VERSION.SDK_INT > 19) {
                                                                                    }
                                                                                }
                                                                                showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                                                scheduleNotificationRepeat();
                                                                            }
                                                                        } else {
                                                                            configImportance = 0;
                                                                            z4 = z3;
                                                                        }
                                                                        mBuilder.setPriority(-1);
                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                            configImportance = 2;
                                                                        }
                                                                        if (!z4) {
                                                                        }
                                                                        importance2 = vibrate3;
                                                                        defaultPath2 = defaultPath;
                                                                        vibrate4 = vibrate13;
                                                                        z5 = z4;
                                                                        long[] vibrationPattern22 = {0, 0};
                                                                        mBuilder.setVibrate(vibrationPattern22);
                                                                        vibrationPattern = vibrationPattern22;
                                                                        boolean hasCallback222 = false;
                                                                        if (!AndroidUtilities.needShowPasscode()) {
                                                                        }
                                                                        ledColor3 = ledColor2;
                                                                        soundPath3 = soundPath2;
                                                                        preferences3 = preferences2;
                                                                        dialog_id3 = dialog_id5;
                                                                        lastMessageObject = lastMessageObject4;
                                                                        hasCallback = false;
                                                                        if (!hasCallback) {
                                                                        }
                                                                        showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                                        scheduleNotificationRepeat();
                                                                    }
                                                                }
                                                                defaultPath = defaultPath4;
                                                                user = user2;
                                                            } else {
                                                                userId3 = chatId2;
                                                                chat3 = chat2;
                                                                if (user2 == null) {
                                                                    defaultPath = defaultPath4;
                                                                    user = user2;
                                                                } else {
                                                                    user = user2;
                                                                    if (user.photo == null || user.photo.photo_small == null) {
                                                                        defaultPath = defaultPath4;
                                                                    } else {
                                                                        defaultPath = defaultPath4;
                                                                        if (user.photo.photo_small.volume_id != 0 && user.photo.photo_small.local_id != 0) {
                                                                            photoPath = user.photo.photo_small;
                                                                            intent.putExtra("currentAccount", this.currentAccount);
                                                                            long dialog_id52 = dialog_id2;
                                                                            PendingIntent contentIntent2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, C.BUFFER_FLAG_ENCRYPTED);
                                                                            MessageObject lastMessageObject42 = lastMessageObject3;
                                                                            mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent2).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject42.messageOwner.date * 1000).setColor(-15618822);
                                                                            mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                                                            if (chat3 == null) {
                                                                            }
                                                                            vibrationPattern = null;
                                                                            Intent dismissIntent22 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                            dismissIntent22.putExtra("messageDate", lastMessageObject42.messageOwner.date);
                                                                            dismissIntent22.putExtra("currentAccount", this.currentAccount);
                                                                            Uri sound22 = null;
                                                                            mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent22, 134217728));
                                                                            if (photoPath != null) {
                                                                            }
                                                                            if (notifyAboutLast) {
                                                                            }
                                                                            mBuilder.setPriority(-1);
                                                                            if (Build.VERSION.SDK_INT >= 26) {
                                                                            }
                                                                            if (!z4) {
                                                                            }
                                                                            importance2 = vibrate3;
                                                                            defaultPath2 = defaultPath;
                                                                            vibrate4 = vibrate13;
                                                                            z5 = z4;
                                                                            long[] vibrationPattern222 = {0, 0};
                                                                            mBuilder.setVibrate(vibrationPattern222);
                                                                            vibrationPattern = vibrationPattern222;
                                                                            boolean hasCallback2222 = false;
                                                                            if (!AndroidUtilities.needShowPasscode()) {
                                                                            }
                                                                            ledColor3 = ledColor2;
                                                                            soundPath3 = soundPath2;
                                                                            preferences3 = preferences2;
                                                                            dialog_id3 = dialog_id52;
                                                                            lastMessageObject = lastMessageObject42;
                                                                            hasCallback = false;
                                                                            if (!hasCallback) {
                                                                            }
                                                                            showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound22, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                                            scheduleNotificationRepeat();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        photoPath = null;
                                                        intent.putExtra("currentAccount", this.currentAccount);
                                                        long dialog_id522 = dialog_id2;
                                                        PendingIntent contentIntent22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, C.BUFFER_FLAG_ENCRYPTED);
                                                        MessageObject lastMessageObject422 = lastMessageObject3;
                                                        mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent22).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject422.messageOwner.date * 1000).setColor(-15618822);
                                                        mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                                        if (chat3 == null) {
                                                        }
                                                        vibrationPattern = null;
                                                        Intent dismissIntent222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        dismissIntent222.putExtra("messageDate", lastMessageObject422.messageOwner.date);
                                                        dismissIntent222.putExtra("currentAccount", this.currentAccount);
                                                        Uri sound222 = null;
                                                        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent222, 134217728));
                                                        if (photoPath != null) {
                                                        }
                                                        if (notifyAboutLast) {
                                                        }
                                                        mBuilder.setPriority(-1);
                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                        }
                                                        if (!z4) {
                                                        }
                                                        importance2 = vibrate3;
                                                        defaultPath2 = defaultPath;
                                                        vibrate4 = vibrate13;
                                                        z5 = z4;
                                                        long[] vibrationPattern2222 = {0, 0};
                                                        mBuilder.setVibrate(vibrationPattern2222);
                                                        vibrationPattern = vibrationPattern2222;
                                                        boolean hasCallback22222 = false;
                                                        if (!AndroidUtilities.needShowPasscode()) {
                                                        }
                                                        ledColor3 = ledColor2;
                                                        soundPath3 = soundPath2;
                                                        preferences3 = preferences2;
                                                        dialog_id3 = dialog_id522;
                                                        lastMessageObject = lastMessageObject422;
                                                        hasCallback = false;
                                                        if (!hasCallback) {
                                                        }
                                                        showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                        scheduleNotificationRepeat();
                                                    }
                                                    userId3 = userId2;
                                                    user = user2;
                                                    customSoundPath4 = customSoundPath8;
                                                    defaultPath = defaultPath4;
                                                    chat3 = chat2;
                                                    if (this.pushDialogs.size() == 1 && dialog_id2 != globalSecretChatId) {
                                                        intent.putExtra("encId", DialogObject.getEncryptedChatId(dialog_id2));
                                                    }
                                                    photoPath = null;
                                                    intent.putExtra("currentAccount", this.currentAccount);
                                                    long dialog_id5222 = dialog_id2;
                                                    PendingIntent contentIntent222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, C.BUFFER_FLAG_ENCRYPTED);
                                                    MessageObject lastMessageObject4222 = lastMessageObject3;
                                                    mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject4222.messageOwner.date * 1000).setColor(-15618822);
                                                    mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                                    if (chat3 == null) {
                                                    }
                                                    vibrationPattern = null;
                                                    Intent dismissIntent2222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                    dismissIntent2222.putExtra("messageDate", lastMessageObject4222.messageOwner.date);
                                                    dismissIntent2222.putExtra("currentAccount", this.currentAccount);
                                                    Uri sound2222 = null;
                                                    mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent2222, 134217728));
                                                    if (photoPath != null) {
                                                    }
                                                    if (notifyAboutLast) {
                                                    }
                                                    mBuilder.setPriority(-1);
                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                    }
                                                    if (!z4) {
                                                    }
                                                    importance2 = vibrate3;
                                                    defaultPath2 = defaultPath;
                                                    vibrate4 = vibrate13;
                                                    z5 = z4;
                                                    long[] vibrationPattern22222 = {0, 0};
                                                    mBuilder.setVibrate(vibrationPattern22222);
                                                    vibrationPattern = vibrationPattern22222;
                                                    boolean hasCallback222222 = false;
                                                    if (!AndroidUtilities.needShowPasscode()) {
                                                    }
                                                    ledColor3 = ledColor2;
                                                    soundPath3 = soundPath2;
                                                    preferences3 = preferences2;
                                                    dialog_id3 = dialog_id5222;
                                                    lastMessageObject = lastMessageObject4222;
                                                    hasCallback = false;
                                                    if (!hasCallback) {
                                                    }
                                                    showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                                    scheduleNotificationRepeat();
                                                }
                                            } else {
                                                vibrate6 = vibrate;
                                            }
                                            vibrate2 = vibrate6;
                                            if (notifyDisabled) {
                                            }
                                            Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            StringBuilder sb32 = new StringBuilder();
                                            sb32.append("com.tmessages.openchat");
                                            int vibrate132 = importance;
                                            String customSoundPath82 = customSoundPath3;
                                            sb32.append(Math.random());
                                            sb32.append(Integer.MAX_VALUE);
                                            intent2.setAction(sb32.toString());
                                            intent2.setFlags(ConnectionsManager.FileTypeFile);
                                            if (DialogObject.isEncryptedDialog(dialog_id2)) {
                                            }
                                            photoPath = null;
                                            intent2.putExtra("currentAccount", this.currentAccount);
                                            long dialog_id52222 = dialog_id2;
                                            PendingIntent contentIntent2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, C.BUFFER_FLAG_ENCRYPTED);
                                            MessageObject lastMessageObject42222 = lastMessageObject3;
                                            mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent2222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject42222.messageOwner.date * 1000).setColor(-15618822);
                                            mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                            if (chat3 == null) {
                                            }
                                            vibrationPattern = null;
                                            Intent dismissIntent22222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            dismissIntent22222.putExtra("messageDate", lastMessageObject42222.messageOwner.date);
                                            dismissIntent22222.putExtra("currentAccount", this.currentAccount);
                                            Uri sound22222 = null;
                                            mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent22222, 134217728));
                                            if (photoPath != null) {
                                            }
                                            if (notifyAboutLast) {
                                            }
                                            mBuilder.setPriority(-1);
                                            if (Build.VERSION.SDK_INT >= 26) {
                                            }
                                            if (!z4) {
                                            }
                                            importance2 = vibrate3;
                                            defaultPath2 = defaultPath;
                                            vibrate4 = vibrate132;
                                            z5 = z4;
                                            long[] vibrationPattern222222 = {0, 0};
                                            mBuilder.setVibrate(vibrationPattern222222);
                                            vibrationPattern = vibrationPattern222222;
                                            boolean hasCallback2222222 = false;
                                            if (!AndroidUtilities.needShowPasscode()) {
                                            }
                                            ledColor3 = ledColor2;
                                            soundPath3 = soundPath2;
                                            preferences3 = preferences2;
                                            dialog_id3 = dialog_id52222;
                                            lastMessageObject = lastMessageObject42222;
                                            hasCallback = false;
                                            if (!hasCallback) {
                                            }
                                            showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound22222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                            scheduleNotificationRepeat();
                                        }
                                        vibrate2 = vibrate5;
                                        if (notifyDisabled) {
                                        }
                                        Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        StringBuilder sb322 = new StringBuilder();
                                        sb322.append("com.tmessages.openchat");
                                        int vibrate1322 = importance;
                                        String customSoundPath822 = customSoundPath3;
                                        sb322.append(Math.random());
                                        sb322.append(Integer.MAX_VALUE);
                                        intent22.setAction(sb322.toString());
                                        intent22.setFlags(ConnectionsManager.FileTypeFile);
                                        if (DialogObject.isEncryptedDialog(dialog_id2)) {
                                        }
                                        photoPath = null;
                                        intent22.putExtra("currentAccount", this.currentAccount);
                                        long dialog_id522222 = dialog_id2;
                                        PendingIntent contentIntent22222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, C.BUFFER_FLAG_ENCRYPTED);
                                        MessageObject lastMessageObject422222 = lastMessageObject3;
                                        mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent22222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject422222.messageOwner.date * 1000).setColor(-15618822);
                                        mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                        if (chat3 == null) {
                                        }
                                        vibrationPattern = null;
                                        Intent dismissIntent222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        dismissIntent222222.putExtra("messageDate", lastMessageObject422222.messageOwner.date);
                                        dismissIntent222222.putExtra("currentAccount", this.currentAccount);
                                        Uri sound222222 = null;
                                        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent222222, 134217728));
                                        if (photoPath != null) {
                                        }
                                        if (notifyAboutLast) {
                                        }
                                        mBuilder.setPriority(-1);
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        if (!z4) {
                                        }
                                        importance2 = vibrate3;
                                        defaultPath2 = defaultPath;
                                        vibrate4 = vibrate1322;
                                        z5 = z4;
                                        long[] vibrationPattern2222222 = {0, 0};
                                        mBuilder.setVibrate(vibrationPattern2222222);
                                        vibrationPattern = vibrationPattern2222222;
                                        boolean hasCallback22222222 = false;
                                        if (!AndroidUtilities.needShowPasscode()) {
                                        }
                                        ledColor3 = ledColor2;
                                        soundPath3 = soundPath2;
                                        preferences3 = preferences2;
                                        dialog_id3 = dialog_id522222;
                                        lastMessageObject = lastMessageObject422222;
                                        hasCallback = false;
                                        if (!hasCallback) {
                                        }
                                        showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                        scheduleNotificationRepeat();
                                    }
                                } else {
                                    isDefault6 = isDefault3;
                                }
                                isDefault4 = isDefault6;
                                if (customImportance == 0) {
                                }
                                if (!isInApp) {
                                }
                                if (vibrateOnlyIfSilent2) {
                                }
                                vibrate5 = vibrate;
                                vibrate2 = vibrate5;
                                if (notifyDisabled) {
                                }
                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb3222 = new StringBuilder();
                                sb3222.append("com.tmessages.openchat");
                                int vibrate13222 = importance;
                                String customSoundPath8222 = customSoundPath3;
                                sb3222.append(Math.random());
                                sb3222.append(Integer.MAX_VALUE);
                                intent222.setAction(sb3222.toString());
                                intent222.setFlags(ConnectionsManager.FileTypeFile);
                                if (DialogObject.isEncryptedDialog(dialog_id2)) {
                                }
                                photoPath = null;
                                intent222.putExtra("currentAccount", this.currentAccount);
                                long dialog_id5222222 = dialog_id2;
                                PendingIntent contentIntent222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, C.BUFFER_FLAG_ENCRYPTED);
                                MessageObject lastMessageObject4222222 = lastMessageObject3;
                                mBuilder.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject4222222.messageOwner.date * 1000).setColor(-15618822);
                                mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                if (chat3 == null) {
                                }
                                vibrationPattern = null;
                                Intent dismissIntent2222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                dismissIntent2222222.putExtra("messageDate", lastMessageObject4222222.messageOwner.date);
                                dismissIntent2222222.putExtra("currentAccount", this.currentAccount);
                                Uri sound2222222 = null;
                                mBuilder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent2222222, 134217728));
                                if (photoPath != null) {
                                }
                                if (notifyAboutLast) {
                                }
                                mBuilder.setPriority(-1);
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                if (!z4) {
                                }
                                importance2 = vibrate3;
                                defaultPath2 = defaultPath;
                                vibrate4 = vibrate13222;
                                z5 = z4;
                                long[] vibrationPattern22222222 = {0, 0};
                                mBuilder.setVibrate(vibrationPattern22222222);
                                vibrationPattern = vibrationPattern22222222;
                                boolean hasCallback222222222 = false;
                                if (!AndroidUtilities.needShowPasscode()) {
                                }
                                ledColor3 = ledColor2;
                                soundPath3 = soundPath2;
                                preferences3 = preferences2;
                                dialog_id3 = dialog_id5222222;
                                lastMessageObject = lastMessageObject4222222;
                                hasCallback = false;
                                if (!hasCallback) {
                                }
                                showExtraNotifications(mBuilder, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                                scheduleNotificationRepeat();
                            }
                            if (this.pushDialogs.size() == 1) {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(detailText);
                                str = "color_";
                                preferences = preferences5;
                                sb6.append(LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]));
                                detailText2 = sb6.toString();
                                override_dialog_id2 = override_dialog_id;
                            } else {
                                str = "color_";
                                preferences = preferences5;
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(detailText);
                                override_dialog_id2 = override_dialog_id;
                                sb7.append(LocaleController.formatString("NotificationMessagesPeopleDisplayOrder", org.telegram.messenger.beta.R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", this.pushDialogs.size(), new Object[0])));
                                detailText2 = sb7.toString();
                            }
                            NotificationCompat.Builder mBuilder3 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                            if (this.pushMessages.size() == 1) {
                            }
                            if (notifyAboutLast) {
                            }
                            notifyDisabled = true;
                            if (!notifyDisabled) {
                            }
                            name2 = name;
                            chat2 = chat;
                            detailText3 = detailText2;
                            preferences2 = preferences;
                            dialog_id2 = dialog_id;
                            if (!notifyDisabled) {
                            }
                            String defaultPath42 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                            isInApp = !ApplicationLoader.mainInterfacePaused;
                            if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
                            }
                            customSoundPath2 = customSoundPath;
                            if (chatId != 0) {
                            }
                            if (vibrate != 4) {
                            }
                            if (!TextUtils.isEmpty(customSoundPath2)) {
                            }
                            boolean isDefault72 = isDefault2;
                            if (customImportance2 == 3) {
                            }
                            isDefault3 = isDefault72;
                            if (customLedColor != null) {
                            }
                            isDefault4 = isDefault6;
                            if (customImportance == 0) {
                            }
                            if (!isInApp) {
                            }
                            if (vibrateOnlyIfSilent2) {
                            }
                            vibrate5 = vibrate;
                            vibrate2 = vibrate5;
                            if (notifyDisabled) {
                            }
                            Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            StringBuilder sb32222 = new StringBuilder();
                            sb32222.append("com.tmessages.openchat");
                            int vibrate132222 = importance;
                            String customSoundPath82222 = customSoundPath3;
                            sb32222.append(Math.random());
                            sb32222.append(Integer.MAX_VALUE);
                            intent2222.setAction(sb32222.toString());
                            intent2222.setFlags(ConnectionsManager.FileTypeFile);
                            if (DialogObject.isEncryptedDialog(dialog_id2)) {
                            }
                            photoPath = null;
                            intent2222.putExtra("currentAccount", this.currentAccount);
                            long dialog_id52222222 = dialog_id2;
                            PendingIntent contentIntent2222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, C.BUFFER_FLAG_ENCRYPTED);
                            MessageObject lastMessageObject42222222 = lastMessageObject3;
                            mBuilder3.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent2222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject42222222.messageOwner.date * 1000).setColor(-15618822);
                            mBuilder3.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                            if (chat3 == null) {
                            }
                            vibrationPattern = null;
                            Intent dismissIntent22222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            dismissIntent22222222.putExtra("messageDate", lastMessageObject42222222.messageOwner.date);
                            dismissIntent22222222.putExtra("currentAccount", this.currentAccount);
                            Uri sound22222222 = null;
                            mBuilder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent22222222, 134217728));
                            if (photoPath != null) {
                            }
                            if (notifyAboutLast) {
                            }
                            mBuilder3.setPriority(-1);
                            if (Build.VERSION.SDK_INT >= 26) {
                            }
                            if (!z4) {
                            }
                            importance2 = vibrate3;
                            defaultPath2 = defaultPath;
                            vibrate4 = vibrate132222;
                            z5 = z4;
                            long[] vibrationPattern222222222 = {0, 0};
                            mBuilder3.setVibrate(vibrationPattern222222222);
                            vibrationPattern = vibrationPattern222222222;
                            boolean hasCallback2222222222 = false;
                            if (!AndroidUtilities.needShowPasscode()) {
                            }
                            ledColor3 = ledColor2;
                            soundPath3 = soundPath2;
                            preferences3 = preferences2;
                            dialog_id3 = dialog_id52222222;
                            lastMessageObject = lastMessageObject42222222;
                            hasCallback = false;
                            if (!hasCallback) {
                            }
                            showExtraNotifications(mBuilder3, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound22222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                            scheduleNotificationRepeat();
                        }
                    } else {
                        notifyOverride2 = notifyOverride;
                    }
                    String name3 = LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName);
                    replace = false;
                    name = name3;
                    if (UserConfig.getActivatedAccountsCount() > 1) {
                    }
                    chatId = chatId3;
                    if (this.pushDialogs.size() == 1) {
                        str = "color_";
                        override_dialog_id2 = override_dialog_id;
                        preferences = preferences5;
                        detailText2 = detailText;
                        NotificationCompat.Builder mBuilder32 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() == 1) {
                        }
                        if (notifyAboutLast) {
                        }
                        notifyDisabled = true;
                        if (!notifyDisabled) {
                        }
                        name2 = name;
                        chat2 = chat;
                        detailText3 = detailText2;
                        preferences2 = preferences;
                        dialog_id2 = dialog_id;
                        if (!notifyDisabled) {
                        }
                        String defaultPath422 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        isInApp = !ApplicationLoader.mainInterfacePaused;
                        if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
                        }
                        customSoundPath2 = customSoundPath;
                        if (chatId != 0) {
                        }
                        if (vibrate != 4) {
                        }
                        if (!TextUtils.isEmpty(customSoundPath2)) {
                        }
                        boolean isDefault722 = isDefault2;
                        if (customImportance2 == 3) {
                        }
                        isDefault3 = isDefault722;
                        if (customLedColor != null) {
                        }
                        isDefault4 = isDefault6;
                        if (customImportance == 0) {
                        }
                        if (!isInApp) {
                        }
                        if (vibrateOnlyIfSilent2) {
                        }
                        vibrate5 = vibrate;
                        vibrate2 = vibrate5;
                        if (notifyDisabled) {
                        }
                        Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb322222 = new StringBuilder();
                        sb322222.append("com.tmessages.openchat");
                        int vibrate1322222 = importance;
                        String customSoundPath822222 = customSoundPath3;
                        sb322222.append(Math.random());
                        sb322222.append(Integer.MAX_VALUE);
                        intent22222.setAction(sb322222.toString());
                        intent22222.setFlags(ConnectionsManager.FileTypeFile);
                        if (DialogObject.isEncryptedDialog(dialog_id2)) {
                        }
                        photoPath = null;
                        intent22222.putExtra("currentAccount", this.currentAccount);
                        long dialog_id522222222 = dialog_id2;
                        PendingIntent contentIntent22222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, C.BUFFER_FLAG_ENCRYPTED);
                        MessageObject lastMessageObject422222222 = lastMessageObject3;
                        mBuilder32.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent22222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject422222222.messageOwner.date * 1000).setColor(-15618822);
                        mBuilder32.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                        if (chat3 == null) {
                        }
                        vibrationPattern = null;
                        Intent dismissIntent222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        dismissIntent222222222.putExtra("messageDate", lastMessageObject422222222.messageOwner.date);
                        dismissIntent222222222.putExtra("currentAccount", this.currentAccount);
                        Uri sound222222222 = null;
                        mBuilder32.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent222222222, 134217728));
                        if (photoPath != null) {
                        }
                        if (notifyAboutLast) {
                        }
                        mBuilder32.setPriority(-1);
                        if (Build.VERSION.SDK_INT >= 26) {
                        }
                        if (!z4) {
                        }
                        importance2 = vibrate3;
                        defaultPath2 = defaultPath;
                        vibrate4 = vibrate1322222;
                        z5 = z4;
                        long[] vibrationPattern2222222222 = {0, 0};
                        mBuilder32.setVibrate(vibrationPattern2222222222);
                        vibrationPattern = vibrationPattern2222222222;
                        boolean hasCallback22222222222 = false;
                        if (!AndroidUtilities.needShowPasscode()) {
                        }
                        ledColor3 = ledColor2;
                        soundPath3 = soundPath2;
                        preferences3 = preferences2;
                        dialog_id3 = dialog_id522222222;
                        lastMessageObject = lastMessageObject422222222;
                        hasCallback = false;
                        if (!hasCallback) {
                        }
                        showExtraNotifications(mBuilder32, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound222222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                        scheduleNotificationRepeat();
                    }
                    if (this.pushDialogs.size() == 1) {
                    }
                    NotificationCompat.Builder mBuilder322 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (this.pushMessages.size() == 1) {
                    }
                    if (notifyAboutLast) {
                    }
                    notifyDisabled = true;
                    if (!notifyDisabled) {
                    }
                    name2 = name;
                    chat2 = chat;
                    detailText3 = detailText2;
                    preferences2 = preferences;
                    dialog_id2 = dialog_id;
                    if (!notifyDisabled) {
                    }
                    String defaultPath4222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                    isInApp = !ApplicationLoader.mainInterfacePaused;
                    if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
                    }
                    customSoundPath2 = customSoundPath;
                    if (chatId != 0) {
                    }
                    if (vibrate != 4) {
                    }
                    if (!TextUtils.isEmpty(customSoundPath2)) {
                    }
                    boolean isDefault7222 = isDefault2;
                    if (customImportance2 == 3) {
                    }
                    isDefault3 = isDefault7222;
                    if (customLedColor != null) {
                    }
                    isDefault4 = isDefault6;
                    if (customImportance == 0) {
                    }
                    if (!isInApp) {
                    }
                    if (vibrateOnlyIfSilent2) {
                    }
                    vibrate5 = vibrate;
                    vibrate2 = vibrate5;
                    if (notifyDisabled) {
                    }
                    Intent intent222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    StringBuilder sb3222222 = new StringBuilder();
                    sb3222222.append("com.tmessages.openchat");
                    int vibrate13222222 = importance;
                    String customSoundPath8222222 = customSoundPath3;
                    sb3222222.append(Math.random());
                    sb3222222.append(Integer.MAX_VALUE);
                    intent222222.setAction(sb3222222.toString());
                    intent222222.setFlags(ConnectionsManager.FileTypeFile);
                    if (DialogObject.isEncryptedDialog(dialog_id2)) {
                    }
                    photoPath = null;
                    intent222222.putExtra("currentAccount", this.currentAccount);
                    long dialog_id5222222222 = dialog_id2;
                    PendingIntent contentIntent222222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222, C.BUFFER_FLAG_ENCRYPTED);
                    MessageObject lastMessageObject4222222222 = lastMessageObject3;
                    mBuilder322.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent222222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject4222222222.messageOwner.date * 1000).setColor(-15618822);
                    mBuilder322.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                    if (chat3 == null) {
                    }
                    vibrationPattern = null;
                    Intent dismissIntent2222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    dismissIntent2222222222.putExtra("messageDate", lastMessageObject4222222222.messageOwner.date);
                    dismissIntent2222222222.putExtra("currentAccount", this.currentAccount);
                    Uri sound2222222222 = null;
                    mBuilder322.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent2222222222, 134217728));
                    if (photoPath != null) {
                    }
                    if (notifyAboutLast) {
                    }
                    mBuilder322.setPriority(-1);
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    if (!z4) {
                    }
                    importance2 = vibrate3;
                    defaultPath2 = defaultPath;
                    vibrate4 = vibrate13222222;
                    z5 = z4;
                    long[] vibrationPattern22222222222 = {0, 0};
                    mBuilder322.setVibrate(vibrationPattern22222222222);
                    vibrationPattern = vibrationPattern22222222222;
                    boolean hasCallback222222222222 = false;
                    if (!AndroidUtilities.needShowPasscode()) {
                    }
                    ledColor3 = ledColor2;
                    soundPath3 = soundPath2;
                    preferences3 = preferences2;
                    dialog_id3 = dialog_id5222222222;
                    lastMessageObject = lastMessageObject4222222222;
                    hasCallback = false;
                    if (!hasCallback) {
                    }
                    showExtraNotifications(mBuilder322, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2222222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                    scheduleNotificationRepeat();
                }
                z = true;
                boolean passcode2 = z;
                if (DialogObject.isEncryptedDialog(override_dialog_id3)) {
                }
                String name32 = LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName);
                replace = false;
                name = name32;
                if (UserConfig.getActivatedAccountsCount() > 1) {
                }
                chatId = chatId3;
                if (this.pushDialogs.size() == 1) {
                }
                if (this.pushDialogs.size() == 1) {
                }
                NotificationCompat.Builder mBuilder3222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() == 1) {
                }
                if (notifyAboutLast) {
                }
                notifyDisabled = true;
                if (!notifyDisabled) {
                }
                name2 = name;
                chat2 = chat;
                detailText3 = detailText2;
                preferences2 = preferences;
                dialog_id2 = dialog_id;
                if (!notifyDisabled) {
                }
                String defaultPath42222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                isInApp = !ApplicationLoader.mainInterfacePaused;
                if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
                }
                customSoundPath2 = customSoundPath;
                if (chatId != 0) {
                }
                if (vibrate != 4) {
                }
                if (!TextUtils.isEmpty(customSoundPath2)) {
                }
                boolean isDefault72222 = isDefault2;
                if (customImportance2 == 3) {
                }
                isDefault3 = isDefault72222;
                if (customLedColor != null) {
                }
                isDefault4 = isDefault6;
                if (customImportance == 0) {
                }
                if (!isInApp) {
                }
                if (vibrateOnlyIfSilent2) {
                }
                vibrate5 = vibrate;
                vibrate2 = vibrate5;
                if (notifyDisabled) {
                }
                Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                StringBuilder sb32222222 = new StringBuilder();
                sb32222222.append("com.tmessages.openchat");
                int vibrate132222222 = importance;
                String customSoundPath82222222 = customSoundPath3;
                sb32222222.append(Math.random());
                sb32222222.append(Integer.MAX_VALUE);
                intent2222222.setAction(sb32222222.toString());
                intent2222222.setFlags(ConnectionsManager.FileTypeFile);
                if (DialogObject.isEncryptedDialog(dialog_id2)) {
                }
                photoPath = null;
                intent2222222.putExtra("currentAccount", this.currentAccount);
                long dialog_id52222222222 = dialog_id2;
                PendingIntent contentIntent2222222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222222, C.BUFFER_FLAG_ENCRYPTED);
                MessageObject lastMessageObject42222222222 = lastMessageObject3;
                mBuilder3222.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent2222222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject42222222222.messageOwner.date * 1000).setColor(-15618822);
                mBuilder3222.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                if (chat3 == null) {
                }
                vibrationPattern = null;
                Intent dismissIntent22222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                dismissIntent22222222222.putExtra("messageDate", lastMessageObject42222222222.messageOwner.date);
                dismissIntent22222222222.putExtra("currentAccount", this.currentAccount);
                Uri sound22222222222 = null;
                mBuilder3222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent22222222222, 134217728));
                if (photoPath != null) {
                }
                if (notifyAboutLast) {
                }
                mBuilder3222.setPriority(-1);
                if (Build.VERSION.SDK_INT >= 26) {
                }
                if (!z4) {
                }
                importance2 = vibrate3;
                defaultPath2 = defaultPath;
                vibrate4 = vibrate132222222;
                z5 = z4;
                long[] vibrationPattern222222222222 = {0, 0};
                mBuilder3222.setVibrate(vibrationPattern222222222222);
                vibrationPattern = vibrationPattern222222222222;
                boolean hasCallback2222222222222 = false;
                if (!AndroidUtilities.needShowPasscode()) {
                }
                ledColor3 = ledColor2;
                soundPath3 = soundPath2;
                preferences3 = preferences2;
                dialog_id3 = dialog_id52222222222;
                lastMessageObject = lastMessageObject42222222222;
                hasCallback = false;
                if (!hasCallback) {
                }
                showExtraNotifications(mBuilder3222, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound22222222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                scheduleNotificationRepeat();
            }
            userId = userId4;
            TLRPC.User user22 = getMessagesController().getUser(Long.valueOf(userId));
            if (chatId3 != 0) {
            }
            boolean isInternalSoundFile2 = false;
            notifyOverride = getNotifyOverride(preferences5, override_dialog_id);
            notifyDisabled = false;
            if (notifyOverride != -1) {
            }
            if (chatId3 != 0) {
            }
            replace = true;
            if (chat == null) {
            }
            if (!AndroidUtilities.needShowPasscode()) {
                z = false;
                boolean passcode22 = z;
                if (DialogObject.isEncryptedDialog(override_dialog_id3)) {
                }
                String name322 = LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName);
                replace = false;
                name = name322;
                if (UserConfig.getActivatedAccountsCount() > 1) {
                }
                chatId = chatId3;
                if (this.pushDialogs.size() == 1) {
                }
                if (this.pushDialogs.size() == 1) {
                }
                NotificationCompat.Builder mBuilder32222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() == 1) {
                }
                if (notifyAboutLast) {
                }
                notifyDisabled = true;
                if (!notifyDisabled) {
                }
                name2 = name;
                chat2 = chat;
                detailText3 = detailText2;
                preferences2 = preferences;
                dialog_id2 = dialog_id;
                if (!notifyDisabled) {
                }
                String defaultPath422222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                isInApp = !ApplicationLoader.mainInterfacePaused;
                if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
                }
                customSoundPath2 = customSoundPath;
                if (chatId != 0) {
                }
                if (vibrate != 4) {
                }
                if (!TextUtils.isEmpty(customSoundPath2)) {
                }
                boolean isDefault722222 = isDefault2;
                if (customImportance2 == 3) {
                }
                isDefault3 = isDefault722222;
                if (customLedColor != null) {
                }
                isDefault4 = isDefault6;
                if (customImportance == 0) {
                }
                if (!isInApp) {
                }
                if (vibrateOnlyIfSilent2) {
                }
                vibrate5 = vibrate;
                vibrate2 = vibrate5;
                if (notifyDisabled) {
                }
                Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                StringBuilder sb322222222 = new StringBuilder();
                sb322222222.append("com.tmessages.openchat");
                int vibrate1322222222 = importance;
                String customSoundPath822222222 = customSoundPath3;
                sb322222222.append(Math.random());
                sb322222222.append(Integer.MAX_VALUE);
                intent22222222.setAction(sb322222222.toString());
                intent22222222.setFlags(ConnectionsManager.FileTypeFile);
                if (DialogObject.isEncryptedDialog(dialog_id2)) {
                }
                photoPath = null;
                intent22222222.putExtra("currentAccount", this.currentAccount);
                long dialog_id522222222222 = dialog_id2;
                PendingIntent contentIntent22222222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222222, C.BUFFER_FLAG_ENCRYPTED);
                MessageObject lastMessageObject422222222222 = lastMessageObject3;
                mBuilder32222.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent22222222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject422222222222.messageOwner.date * 1000).setColor(-15618822);
                mBuilder32222.setCategory(NotificationCompat.CATEGORY_MESSAGE);
                if (chat3 == null) {
                }
                vibrationPattern = null;
                Intent dismissIntent222222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                dismissIntent222222222222.putExtra("messageDate", lastMessageObject422222222222.messageOwner.date);
                dismissIntent222222222222.putExtra("currentAccount", this.currentAccount);
                Uri sound222222222222 = null;
                mBuilder32222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent222222222222, 134217728));
                if (photoPath != null) {
                }
                if (notifyAboutLast) {
                }
                mBuilder32222.setPriority(-1);
                if (Build.VERSION.SDK_INT >= 26) {
                }
                if (!z4) {
                }
                importance2 = vibrate3;
                defaultPath2 = defaultPath;
                vibrate4 = vibrate1322222222;
                z5 = z4;
                long[] vibrationPattern2222222222222 = {0, 0};
                mBuilder32222.setVibrate(vibrationPattern2222222222222);
                vibrationPattern = vibrationPattern2222222222222;
                boolean hasCallback22222222222222 = false;
                if (!AndroidUtilities.needShowPasscode()) {
                }
                ledColor3 = ledColor2;
                soundPath3 = soundPath2;
                preferences3 = preferences2;
                dialog_id3 = dialog_id522222222222;
                lastMessageObject = lastMessageObject422222222222;
                hasCallback = false;
                if (!hasCallback) {
                }
                showExtraNotifications(mBuilder32222, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound222222222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
                scheduleNotificationRepeat();
            }
            z = true;
            boolean passcode222 = z;
            if (DialogObject.isEncryptedDialog(override_dialog_id3)) {
            }
            String name3222 = LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName);
            replace = false;
            name = name3222;
            if (UserConfig.getActivatedAccountsCount() > 1) {
            }
            chatId = chatId3;
            if (this.pushDialogs.size() == 1) {
            }
            if (this.pushDialogs.size() == 1) {
            }
            NotificationCompat.Builder mBuilder322222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            if (this.pushMessages.size() == 1) {
            }
            if (notifyAboutLast) {
            }
            notifyDisabled = true;
            if (!notifyDisabled) {
            }
            name2 = name;
            chat2 = chat;
            detailText3 = detailText2;
            preferences2 = preferences;
            dialog_id2 = dialog_id;
            if (!notifyDisabled) {
            }
            String defaultPath4222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
            isInApp = !ApplicationLoader.mainInterfacePaused;
            if (preferences2.getBoolean(ContentMetadata.KEY_CUSTOM_PREFIX + dialog_id2, false)) {
            }
            customSoundPath2 = customSoundPath;
            if (chatId != 0) {
            }
            if (vibrate != 4) {
            }
            if (!TextUtils.isEmpty(customSoundPath2)) {
            }
            boolean isDefault7222222 = isDefault2;
            if (customImportance2 == 3) {
            }
            isDefault3 = isDefault7222222;
            if (customLedColor != null) {
            }
            isDefault4 = isDefault6;
            if (customImportance == 0) {
            }
            if (!isInApp) {
            }
            if (vibrateOnlyIfSilent2) {
            }
            vibrate5 = vibrate;
            vibrate2 = vibrate5;
            if (notifyDisabled) {
            }
            Intent intent222222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            StringBuilder sb3222222222 = new StringBuilder();
            sb3222222222.append("com.tmessages.openchat");
            int vibrate13222222222 = importance;
            String customSoundPath8222222222 = customSoundPath3;
            sb3222222222.append(Math.random());
            sb3222222222.append(Integer.MAX_VALUE);
            intent222222222.setAction(sb3222222222.toString());
            intent222222222.setFlags(ConnectionsManager.FileTypeFile);
            if (DialogObject.isEncryptedDialog(dialog_id2)) {
            }
            photoPath = null;
            intent222222222.putExtra("currentAccount", this.currentAccount);
            long dialog_id5222222222222 = dialog_id2;
            PendingIntent contentIntent222222222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222222, C.BUFFER_FLAG_ENCRYPTED);
            MessageObject lastMessageObject4222222222222 = lastMessageObject3;
            mBuilder322222.setContentTitle(name2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(contentIntent222222222222).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(lastMessageObject4222222222222.messageOwner.date * 1000).setColor(-15618822);
            mBuilder322222.setCategory(NotificationCompat.CATEGORY_MESSAGE);
            if (chat3 == null) {
            }
            vibrationPattern = null;
            Intent dismissIntent2222222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            dismissIntent2222222222222.putExtra("messageDate", lastMessageObject4222222222222.messageOwner.date);
            dismissIntent2222222222222.putExtra("currentAccount", this.currentAccount);
            Uri sound2222222222222 = null;
            mBuilder322222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, dismissIntent2222222222222, 134217728));
            if (photoPath != null) {
            }
            if (notifyAboutLast) {
            }
            mBuilder322222.setPriority(-1);
            if (Build.VERSION.SDK_INT >= 26) {
            }
            if (!z4) {
            }
            importance2 = vibrate3;
            defaultPath2 = defaultPath;
            vibrate4 = vibrate13222222222;
            z5 = z4;
            long[] vibrationPattern22222222222222 = {0, 0};
            mBuilder322222.setVibrate(vibrationPattern22222222222222);
            vibrationPattern = vibrationPattern22222222222222;
            boolean hasCallback222222222222222 = false;
            if (!AndroidUtilities.needShowPasscode()) {
            }
            ledColor3 = ledColor2;
            soundPath3 = soundPath2;
            preferences3 = preferences2;
            dialog_id3 = dialog_id5222222222222;
            lastMessageObject = lastMessageObject4222222222222;
            hasCallback = false;
            if (!hasCallback) {
            }
            showExtraNotifications(mBuilder322222, detailText3, dialog_id3, chatName, vibrationPattern, ledColor3, sound2222222222222, configImportance, isDefault5, isInApp, notifyDisabled, chatType);
            scheduleNotificationRepeat();
        } catch (Exception e4) {
            FileLog.e(e4);
        }
    }

    private boolean isSilentMessage(MessageObject messageObject) {
        return messageObject.messageOwner.silent || messageObject.isReactionPush;
    }

    private void setNotificationChannel(Notification mainNotification, NotificationCompat.Builder builder, boolean useSummaryNotification) {
        if (useSummaryNotification) {
            builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
        } else {
            builder.setChannelId(mainNotification.getChannelId());
        }
    }

    public void resetNotificationSound(NotificationCompat.Builder notificationBuilder, long dialogId, String chatName, long[] vibrationPattern, int ledColor, Uri sound, int importance, boolean isDefault, boolean isInApp, boolean isSilent, int chatType) {
        Uri defaultSound = Settings.System.DEFAULT_RINGTONE_URI;
        if (defaultSound == null || sound == null || TextUtils.equals(defaultSound.toString(), sound.toString())) {
            return;
        }
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        SharedPreferences.Editor editor = preferences.edit();
        String newSound = defaultSound.toString();
        String ringtoneName = LocaleController.getString("DefaultRingtone", org.telegram.messenger.beta.R.string.DefaultRingtone);
        if (isDefault) {
            if (chatType == 2) {
                editor.putString("ChannelSound", ringtoneName);
            } else if (chatType == 0) {
                editor.putString("GroupSound", ringtoneName);
            } else {
                editor.putString("GlobalSound", ringtoneName);
            }
            if (chatType == 2) {
                editor.putString("ChannelSoundPath", newSound);
            } else if (chatType == 0) {
                editor.putString("GroupSoundPath", newSound);
            } else {
                editor.putString("GlobalSoundPath", newSound);
            }
            getNotificationsController().m1097xb6f20c1b(chatType, -1);
        } else {
            editor.putString("sound_" + dialogId, ringtoneName);
            editor.putString("sound_path_" + dialogId, newSound);
            m1096xab324d39(dialogId, -1);
        }
        editor.commit();
        notificationBuilder.setChannelId(validateChannelId(dialogId, chatName, vibrationPattern, ledColor, Settings.System.DEFAULT_RINGTONE_URI, importance, isDefault, isInApp, isSilent, chatType));
        notificationManager.notify(this.notificationId, notificationBuilder.build());
    }

    /* JADX WARN: Removed duplicated region for block: B:136:0x0426  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0458  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x04fb  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x050b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x051c  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x05cd  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x05d5  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x05e7  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0613 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x06b8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x06d3  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x06ea A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0723  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x097a  */
    /* JADX WARN: Removed duplicated region for block: B:415:0x0af0  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x0b79  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x0b83  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x0bad  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x0bb3  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x0c0f  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0c4c  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0c75  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x0c9d  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x0d5c  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x0d67  */
    /* JADX WARN: Removed duplicated region for block: B:451:0x0d6d  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x0d71  */
    /* JADX WARN: Removed duplicated region for block: B:456:0x0d7b  */
    /* JADX WARN: Removed duplicated region for block: B:462:0x0d8f  */
    /* JADX WARN: Removed duplicated region for block: B:463:0x0d94  */
    /* JADX WARN: Removed duplicated region for block: B:465:0x0d97  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0d9f  */
    /* JADX WARN: Removed duplicated region for block: B:469:0x0dab  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0e9f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:498:0x0ece  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x0ed6  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x1002  */
    /* JADX WARN: Removed duplicated region for block: B:530:0x1054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder notificationBuilder, String summary, long lastDialogId, String chatName, long[] vibrationPattern, int ledColor, Uri sound, int importance, boolean isDefault, boolean isInApp, boolean isSilent, int chatType) {
        int i;
        LongSparseArray<Person> personCache;
        boolean useSummaryNotification;
        LongSparseArray<Integer> oldIdsWear;
        Notification mainNotification;
        ArrayList<C1NotificationHolder> holders;
        int a;
        int size;
        int a2;
        LongSparseArray<Integer> oldIdsWear2;
        LongSparseArray<Person> personCache2;
        Notification mainNotification2;
        SecurityException e;
        LongSparseArray<ArrayList<MessageObject>> messagesByDialogs;
        ArrayList<Long> sortedDialogs;
        Integer internalId;
        ArrayList<C1NotificationHolder> holders2;
        Notification mainNotification3;
        LongSparseArray<Person> personCache3;
        long selfUserId;
        LongSparseArray<Integer> oldIdsWear3;
        boolean useSummaryNotification2;
        ArrayList<C1NotificationHolder> holders3;
        int maxDate;
        boolean useSummaryNotification3;
        LongSparseArray<Integer> oldIdsWear4;
        boolean canReply;
        TLRPC.Chat chat;
        TLRPC.User user;
        String name;
        Notification mainNotification4;
        TLRPC.User user2;
        String name2;
        TLRPC.FileLocation photoPath;
        String str;
        File avatalFile;
        Bitmap avatarBitmap;
        TLRPC.Chat chat2;
        Bitmap avatarBitmap2;
        TLRPC.FileLocation photoPath2;
        File avatalFile2;
        String str2;
        Integer internalId2;
        MessageObject lastMessageObject;
        File avatalFile3;
        NotificationCompat.Action wearReplyAction;
        Integer count;
        Integer count2;
        int n;
        String conversationName;
        Person selfPerson;
        int n2;
        int maxId;
        Person selfPerson2;
        String str3;
        Person selfPerson3;
        NotificationCompat.MessagingStyle messagingStyle;
        StringBuilder text;
        NotificationCompat.Action wearReplyAction2;
        int rowsMid;
        int a3;
        NotificationCompat.Action wearReplyAction3;
        PendingIntent readPendingIntent;
        String dismissalID;
        NotificationCompat.Action wearReplyAction4;
        int maxId2;
        long selfUserId2;
        Intent dismissIntent;
        NotificationCompat.Action wearReplyAction5;
        NotificationCompat.Action wearReplyAction6;
        Bitmap avatarBitmap3;
        long dialogId;
        StringBuilder text2;
        Intent dismissIntent2;
        Bitmap avatarBitmap4;
        int rowsMid2;
        TLRPC.User user3;
        Notification mainNotification5;
        boolean useSummaryNotification4;
        String str4;
        long dialogId2;
        StringBuilder text3;
        Intent dismissIntent3;
        int rowsMid3;
        String name3;
        String str5;
        String str6;
        long selfUserId3;
        String str7;
        boolean[] preview;
        LongSparseArray<Person> personCache4;
        String[] senderName;
        NotificationCompat.MessagingStyle messagingStyle2;
        String message;
        long uid;
        NotificationCompat.MessagingStyle messagingStyle3;
        String str8;
        String str9;
        String personName;
        Person person;
        Uri uri;
        Uri uri2;
        File avatar;
        TLRPC.User sender;
        Throwable e2;
        Person selfPerson4;
        NotificationCompat.Action wearReplyAction7;
        String replyToString;
        String name4;
        NotificationsController notificationsController = this;
        if (Build.VERSION.SDK_INT >= 26) {
            notificationBuilder.setChannelId(validateChannelId(lastDialogId, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType));
        }
        Notification mainNotification6 = notificationBuilder.build();
        if (Build.VERSION.SDK_INT < 18) {
            notificationManager.notify(notificationsController.notificationId, mainNotification6);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
                return;
            }
            return;
        }
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        ArrayList<Long> sortedDialogs2 = new ArrayList<>();
        LongSparseArray<ArrayList<MessageObject>> messagesByDialogs2 = new LongSparseArray<>();
        int a4 = 0;
        while (true) {
            i = 0;
            if (a4 >= notificationsController.pushMessages.size()) {
                break;
            }
            MessageObject messageObject = notificationsController.pushMessages.get(a4);
            long dialog_id = messageObject.getDialogId();
            int dismissDate = preferences.getInt("dismissDate" + dialog_id, 0);
            if (messageObject.messageOwner.date > dismissDate) {
                ArrayList<MessageObject> arrayList = messagesByDialogs2.get(dialog_id);
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                    messagesByDialogs2.put(dialog_id, arrayList);
                    sortedDialogs2.add(Long.valueOf(dialog_id));
                }
                arrayList.add(messageObject);
            }
            a4++;
        }
        LongSparseArray<Integer> oldIdsWear5 = new LongSparseArray<>();
        for (int i2 = 0; i2 < notificationsController.wearNotificationsIds.size(); i2++) {
            oldIdsWear5.put(notificationsController.wearNotificationsIds.keyAt(i2), notificationsController.wearNotificationsIds.valueAt(i2));
        }
        notificationsController.wearNotificationsIds.clear();
        ArrayList<C1NotificationHolder> holders4 = new ArrayList<>();
        boolean useSummaryNotification5 = Build.VERSION.SDK_INT <= 27 || sortedDialogs2.size() > 1;
        if (useSummaryNotification5 && Build.VERSION.SDK_INT >= 26) {
            checkOtherNotificationsChannel();
        }
        long selfUserId4 = getUserConfig().getClientUserId();
        boolean waitingForPasscode = AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
        int maxCount = 7;
        LongSparseArray<Person> personCache5 = new LongSparseArray<>();
        int size2 = sortedDialogs2.size();
        int b = 0;
        while (true) {
            if (b >= size2) {
                personCache = personCache5;
                useSummaryNotification = useSummaryNotification5;
                oldIdsWear = oldIdsWear5;
                mainNotification = mainNotification6;
                holders = holders4;
                break;
            } else if (holders4.size() >= maxCount) {
                personCache = personCache5;
                useSummaryNotification = useSummaryNotification5;
                oldIdsWear = oldIdsWear5;
                mainNotification = mainNotification6;
                holders = holders4;
                break;
            } else {
                int size3 = size2;
                int b2 = b;
                long dialogId3 = sortedDialogs2.get(b).longValue();
                SharedPreferences preferences2 = preferences;
                ArrayList<MessageObject> messageObjects = messagesByDialogs2.get(dialogId3);
                int maxId3 = messageObjects.get(i).getId();
                Integer internalId3 = oldIdsWear5.get(dialogId3);
                int maxCount2 = maxCount;
                if (internalId3 == null) {
                    messagesByDialogs = messagesByDialogs2;
                    sortedDialogs = sortedDialogs2;
                    internalId = Integer.valueOf(((int) dialogId3) + ((int) (dialogId3 >> 32)));
                } else {
                    messagesByDialogs = messagesByDialogs2;
                    sortedDialogs = sortedDialogs2;
                    oldIdsWear5.remove(dialogId3);
                    internalId = internalId3;
                }
                MessageObject lastMessageObject2 = messageObjects.get(0);
                int maxDate2 = 0;
                int maxDate3 = 0;
                while (true) {
                    holders2 = holders4;
                    if (maxDate3 >= messageObjects.size()) {
                        break;
                    }
                    if (maxDate2 < messageObjects.get(maxDate3).messageOwner.date) {
                        maxDate2 = messageObjects.get(maxDate3).messageOwner.date;
                    }
                    maxDate3++;
                    holders4 = holders2;
                }
                boolean isChannel = false;
                boolean isSupergroup = false;
                TLRPC.FileLocation photoPath3 = null;
                Bitmap avatarBitmap5 = null;
                if (!DialogObject.isEncryptedDialog(dialogId3)) {
                    canReply = dialogId3 != 777000;
                    if (DialogObject.isUserDialog(dialogId3)) {
                        user = getMessagesController().getUser(Long.valueOf(dialogId3));
                        if (user == null) {
                            if (lastMessageObject2.isFcmMessage()) {
                                name = lastMessageObject2.localName;
                                useSummaryNotification3 = useSummaryNotification5;
                                maxDate = maxDate2;
                                oldIdsWear4 = oldIdsWear5;
                            } else {
                                if (!BuildVars.LOGS_ENABLED) {
                                    personCache3 = personCache5;
                                    useSummaryNotification2 = useSummaryNotification5;
                                    mainNotification3 = mainNotification6;
                                    selfUserId = selfUserId4;
                                    holders3 = holders2;
                                    oldIdsWear3 = oldIdsWear5;
                                } else {
                                    FileLog.w("not found user to show dialog notification " + dialogId3);
                                    personCache3 = personCache5;
                                    useSummaryNotification2 = useSummaryNotification5;
                                    mainNotification3 = mainNotification6;
                                    selfUserId = selfUserId4;
                                    holders3 = holders2;
                                    oldIdsWear3 = oldIdsWear5;
                                }
                                b = b2 + 1;
                                holders4 = holders3;
                                size2 = size3;
                                preferences = preferences2;
                                useSummaryNotification5 = useSummaryNotification2;
                                maxCount = maxCount2;
                                sortedDialogs2 = sortedDialogs;
                                messagesByDialogs2 = messagesByDialogs;
                                oldIdsWear5 = oldIdsWear3;
                                selfUserId4 = selfUserId;
                                personCache5 = personCache3;
                                mainNotification6 = mainNotification3;
                                i = 0;
                            }
                        } else {
                            oldIdsWear4 = oldIdsWear5;
                            name = UserObject.getUserName(user);
                            if (user.photo == null || user.photo.photo_small == null) {
                                useSummaryNotification3 = useSummaryNotification5;
                                maxDate = maxDate2;
                            } else {
                                useSummaryNotification3 = useSummaryNotification5;
                                maxDate = maxDate2;
                                if (user.photo.photo_small.volume_id != 0 && user.photo.photo_small.local_id != 0) {
                                    photoPath3 = user.photo.photo_small;
                                }
                            }
                        }
                        if (UserObject.isReplyUser(dialogId3)) {
                            name = LocaleController.getString("RepliesTitle", org.telegram.messenger.beta.R.string.RepliesTitle);
                            chat = null;
                        } else if (dialogId3 != selfUserId4) {
                            chat = null;
                        } else {
                            name = LocaleController.getString("MessageScheduledReminderNotification", org.telegram.messenger.beta.R.string.MessageScheduledReminderNotification);
                            chat = null;
                        }
                        if (!waitingForPasscode) {
                            mainNotification4 = mainNotification6;
                            name2 = name;
                            TLRPC.FileLocation fileLocation = photoPath3;
                            user2 = user;
                            photoPath = fileLocation;
                        } else {
                            if (DialogObject.isChatDialog(dialogId3)) {
                                mainNotification4 = mainNotification6;
                                name4 = LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.beta.R.string.NotificationHiddenChatName);
                            } else {
                                mainNotification4 = mainNotification6;
                                name4 = LocaleController.getString("NotificationHiddenName", org.telegram.messenger.beta.R.string.NotificationHiddenName);
                            }
                            canReply = false;
                            name2 = name4;
                            user2 = user;
                            photoPath = null;
                        }
                        String str10 = "NotificationHiddenName";
                        if (photoPath == null) {
                            str = "NotificationHiddenChatName";
                            avatarBitmap = null;
                            avatalFile = null;
                        } else {
                            File avatalFile4 = getFileLoader().getPathToAttach(photoPath, true);
                            if (Build.VERSION.SDK_INT < 28) {
                                str = "NotificationHiddenChatName";
                                BitmapDrawable img = ImageLoader.getInstance().getImageFromMemory(photoPath, null, "50_50");
                                if (img != null) {
                                    Bitmap avatarBitmap6 = img.getBitmap();
                                    avatarBitmap = avatarBitmap6;
                                    avatalFile = avatalFile4;
                                } else {
                                    try {
                                        if (avatalFile4.exists()) {
                                            float scaleFactor = 160.0f / AndroidUtilities.dp(50.0f);
                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                            try {
                                                options.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                                avatarBitmap5 = BitmapFactory.decodeFile(avatalFile4.getAbsolutePath(), options);
                                            } catch (Throwable th) {
                                            }
                                        }
                                        avatarBitmap = avatarBitmap5;
                                        avatalFile = avatalFile4;
                                    } catch (Throwable th2) {
                                    }
                                }
                            } else {
                                str = "NotificationHiddenChatName";
                            }
                            avatarBitmap = null;
                            avatalFile = avatalFile4;
                        }
                        if (chat == null) {
                            avatarBitmap2 = avatarBitmap;
                            photoPath2 = photoPath;
                            chat2 = chat;
                            avatalFile2 = avatalFile;
                        } else {
                            Person.Builder personBuilder = new Person.Builder().setName(name2);
                            if (avatalFile == null || !avatalFile.exists()) {
                                avatarBitmap2 = avatarBitmap;
                                photoPath2 = photoPath;
                            } else {
                                photoPath2 = photoPath;
                                avatarBitmap2 = avatarBitmap;
                                if (Build.VERSION.SDK_INT >= 28) {
                                    notificationsController.loadRoundAvatar(avatalFile, personBuilder);
                                }
                            }
                            avatalFile2 = avatalFile;
                            chat2 = chat;
                            personCache5.put(-chat.id, personBuilder.build());
                        }
                        String str11 = "currentAccount";
                        if (!isChannel && !isSupergroup) {
                            wearReplyAction7 = null;
                            str2 = "max_id";
                            avatalFile3 = avatalFile2;
                            lastMessageObject = lastMessageObject2;
                            internalId2 = internalId;
                        } else if (canReply || SharedConfig.isWaitingForPasscodeEnter || selfUserId4 == dialogId3 || UserObject.isReplyUser(dialogId3)) {
                            wearReplyAction7 = null;
                            str2 = "max_id";
                            avatalFile3 = avatalFile2;
                            lastMessageObject = lastMessageObject2;
                            internalId2 = internalId;
                        } else {
                            avatalFile3 = avatalFile2;
                            lastMessageObject = lastMessageObject2;
                            Intent replyIntent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                            replyIntent.putExtra("dialog_id", dialogId3);
                            replyIntent.putExtra("max_id", maxId3);
                            replyIntent.putExtra(str11, notificationsController.currentAccount);
                            internalId2 = internalId;
                            PendingIntent replyPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId.intValue(), replyIntent, 134217728);
                            RemoteInput remoteInputWear = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply)).build();
                            if (DialogObject.isChatDialog(dialogId3)) {
                                str2 = "max_id";
                                replyToString = LocaleController.formatString("ReplyToGroup", org.telegram.messenger.beta.R.string.ReplyToGroup, name2);
                            } else {
                                str2 = "max_id";
                                replyToString = LocaleController.formatString("ReplyToUser", org.telegram.messenger.beta.R.string.ReplyToUser, name2);
                            }
                            wearReplyAction = new NotificationCompat.Action.Builder((int) org.telegram.messenger.beta.R.drawable.ic_reply_icon, replyToString, replyPendingIntent).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(remoteInputWear).setShowsUserInterface(false).build();
                            count = notificationsController.pushDialogs.get(dialogId3);
                            if (count == null) {
                                count2 = count;
                            } else {
                                count2 = 0;
                            }
                            n = Math.max(count2.intValue(), messageObjects.size());
                            if (n > 1 || Build.VERSION.SDK_INT >= 28) {
                                String conversationName2 = name2;
                                conversationName = conversationName2;
                            } else {
                                conversationName = String.format("%1$s (%2$d)", name2, Integer.valueOf(n));
                            }
                            selfPerson = personCache5.get(selfUserId4);
                            if (Build.VERSION.SDK_INT >= 28 || selfPerson != null) {
                                maxId = maxId3;
                                n2 = n;
                                selfPerson2 = selfPerson;
                            } else {
                                TLRPC.User sender2 = getMessagesController().getUser(Long.valueOf(selfUserId4));
                                if (sender2 != null) {
                                    sender = sender2;
                                } else {
                                    sender = getUserConfig().getCurrentUser();
                                }
                                if (sender != null) {
                                    try {
                                        if (sender.photo != null && sender.photo.photo_small != null) {
                                            maxId = maxId3;
                                            n2 = n;
                                            try {
                                                if (sender.photo.photo_small.volume_id != 0 && sender.photo.photo_small.local_id != 0) {
                                                    Person.Builder personBuilder2 = new Person.Builder().setName(LocaleController.getString("FromYou", org.telegram.messenger.beta.R.string.FromYou));
                                                    try {
                                                        File avatar2 = getFileLoader().getPathToAttach(sender.photo.photo_small, true);
                                                        notificationsController.loadRoundAvatar(avatar2, personBuilder2);
                                                        selfPerson4 = personBuilder2.build();
                                                    } catch (Throwable th3) {
                                                        e2 = th3;
                                                    }
                                                    try {
                                                        personCache5.put(selfUserId4, selfPerson4);
                                                        selfPerson = selfPerson4;
                                                    } catch (Throwable th4) {
                                                        e2 = th4;
                                                        selfPerson = selfPerson4;
                                                        FileLog.e(e2);
                                                        selfPerson2 = selfPerson;
                                                        boolean needAddPerson = !(lastMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest);
                                                        str3 = "";
                                                        if (selfPerson2 == null) {
                                                        }
                                                        selfPerson3 = selfPerson2;
                                                        messagingStyle = new NotificationCompat.MessagingStyle(str3);
                                                        if (Build.VERSION.SDK_INT >= 28) {
                                                        }
                                                        messagingStyle.setConversationTitle(conversationName);
                                                        messagingStyle.setGroupConversation(Build.VERSION.SDK_INT >= 28 || (!isChannel && DialogObject.isChatDialog(dialogId3)) || UserObject.isReplyUser(dialogId3));
                                                        text = new StringBuilder();
                                                        String[] senderName2 = new String[1];
                                                        wearReplyAction2 = wearReplyAction;
                                                        boolean[] preview2 = new boolean[1];
                                                        rowsMid = 0;
                                                        a3 = messageObjects.size() - 1;
                                                        ArrayList<TLRPC.TL_keyboardButtonRow> rows = null;
                                                        while (a3 >= 0) {
                                                        }
                                                        LongSparseArray<Person> personCache6 = personCache5;
                                                        String str12 = str11;
                                                        ArrayList<MessageObject> messageObjects2 = messageObjects;
                                                        String name5 = name2;
                                                        long selfUserId5 = selfUserId4;
                                                        NotificationCompat.MessagingStyle messagingStyle4 = messagingStyle;
                                                        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                        intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                                                        intent.setFlags(ConnectionsManager.FileTypeFile);
                                                        intent.addCategory("android.intent.category.LAUNCHER");
                                                        if (DialogObject.isEncryptedDialog(dialogId3)) {
                                                        }
                                                        String str13 = str12;
                                                        intent.putExtra(str13, notificationsController.currentAccount);
                                                        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, C.BUFFER_FLAG_ENCRYPTED);
                                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                                        if (wearReplyAction2 == null) {
                                                        }
                                                        Intent msgHeardIntent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                        msgHeardIntent.addFlags(32);
                                                        msgHeardIntent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                        msgHeardIntent.putExtra("dialog_id", dialogId3);
                                                        int maxId4 = maxId;
                                                        msgHeardIntent.putExtra(str2, maxId4);
                                                        msgHeardIntent.putExtra(str13, notificationsController.currentAccount);
                                                        PendingIntent readPendingIntent2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                                                        NotificationCompat.Action readAction = new NotificationCompat.Action.Builder((int) org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), readPendingIntent2).setSemanticAction(2).setShowsUserInterface(false).build();
                                                        if (!DialogObject.isEncryptedDialog(dialogId3)) {
                                                        }
                                                        if (dismissalID != null) {
                                                        }
                                                        wearableExtender.setBridgeTag("tgaccount" + selfUserId2);
                                                        long date = ((long) messageObjects2.get(0).messageOwner.date) * 1000;
                                                        long selfUserId6 = selfUserId2;
                                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name5).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects2.size()).setColor(-15618822).setGroupSummary(false).setWhen(date).setShowWhen(true).setStyle(messagingStyle4).setContentIntent(contentIntent).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - date)).setCategory(NotificationCompat.CATEGORY_MESSAGE);
                                                        dismissIntent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        dismissIntent.putExtra("messageDate", maxDate);
                                                        dismissIntent.putExtra("dialogId", dialogId3);
                                                        dismissIntent.putExtra(str13, notificationsController.currentAccount);
                                                        builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), dismissIntent, 134217728));
                                                        if (useSummaryNotification3) {
                                                        }
                                                        if (wearReplyAction4 == null) {
                                                        }
                                                        if (!waitingForPasscode) {
                                                        }
                                                        if (sortedDialogs.size() != 1) {
                                                        }
                                                        if (DialogObject.isEncryptedDialog(dialogId3)) {
                                                        }
                                                        if (avatarBitmap2 == null) {
                                                        }
                                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                                        }
                                                        dialogId = dialogId3;
                                                        avatarBitmap4 = avatarBitmap3;
                                                        dismissIntent2 = dismissIntent;
                                                        text2 = text;
                                                        rowsMid2 = rowsMid;
                                                        if (chat2 == null) {
                                                        }
                                                        user3 = user2;
                                                        if (Build.VERSION.SDK_INT < 26) {
                                                        }
                                                        personCache3 = personCache6;
                                                        useSummaryNotification2 = useSummaryNotification4;
                                                        oldIdsWear3 = oldIdsWear4;
                                                        selfUserId = selfUserId6;
                                                        mainNotification3 = mainNotification5;
                                                        holders3 = holders2;
                                                        holders3.add(new C1NotificationHolder(internalId2.intValue(), dialogId, name5, user3, chat2, builder, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType));
                                                        notificationsController = this;
                                                        notificationsController.wearNotificationsIds.put(dialogId, internalId2);
                                                        b = b2 + 1;
                                                        holders4 = holders3;
                                                        size2 = size3;
                                                        preferences = preferences2;
                                                        useSummaryNotification5 = useSummaryNotification2;
                                                        maxCount = maxCount2;
                                                        sortedDialogs2 = sortedDialogs;
                                                        messagesByDialogs2 = messagesByDialogs;
                                                        oldIdsWear5 = oldIdsWear3;
                                                        selfUserId4 = selfUserId;
                                                        personCache5 = personCache3;
                                                        mainNotification6 = mainNotification3;
                                                        i = 0;
                                                    }
                                                }
                                                selfPerson2 = selfPerson;
                                            } catch (Throwable th5) {
                                                e2 = th5;
                                            }
                                        }
                                    } catch (Throwable th6) {
                                        e2 = th6;
                                        maxId = maxId3;
                                        n2 = n;
                                    }
                                }
                                maxId = maxId3;
                                n2 = n;
                                selfPerson2 = selfPerson;
                            }
                            boolean needAddPerson2 = !(lastMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest);
                            str3 = "";
                            if (selfPerson2 == null && needAddPerson2) {
                                selfPerson3 = selfPerson2;
                                messagingStyle = new NotificationCompat.MessagingStyle(selfPerson2);
                            } else {
                                selfPerson3 = selfPerson2;
                                messagingStyle = new NotificationCompat.MessagingStyle(str3);
                            }
                            if (Build.VERSION.SDK_INT >= 28 || ((DialogObject.isChatDialog(dialogId3) && !isChannel) || UserObject.isReplyUser(dialogId3))) {
                                messagingStyle.setConversationTitle(conversationName);
                            }
                            messagingStyle.setGroupConversation(Build.VERSION.SDK_INT >= 28 || (!isChannel && DialogObject.isChatDialog(dialogId3)) || UserObject.isReplyUser(dialogId3));
                            text = new StringBuilder();
                            String[] senderName22 = new String[1];
                            wearReplyAction2 = wearReplyAction;
                            boolean[] preview22 = new boolean[1];
                            rowsMid = 0;
                            a3 = messageObjects.size() - 1;
                            ArrayList<TLRPC.TL_keyboardButtonRow> rows2 = null;
                            while (a3 >= 0) {
                                ArrayList<MessageObject> messageObjects3 = messageObjects;
                                MessageObject messageObject2 = messageObjects.get(a3);
                                String message2 = notificationsController.getShortStringForMessage(messageObject2, senderName22, preview22);
                                String str14 = str11;
                                if (dialogId3 == selfUserId4) {
                                    senderName22[0] = name2;
                                    name3 = name2;
                                } else if (DialogObject.isChatDialog(dialogId3)) {
                                    name3 = name2;
                                    if (messageObject2.messageOwner.from_scheduled) {
                                        senderName22[0] = LocaleController.getString("NotificationMessageScheduledName", org.telegram.messenger.beta.R.string.NotificationMessageScheduledName);
                                    }
                                } else {
                                    name3 = name2;
                                }
                                if (message2 == null) {
                                    if (!BuildVars.LOGS_ENABLED) {
                                        preview = preview22;
                                        str7 = str3;
                                        selfUserId3 = selfUserId4;
                                        str5 = str10;
                                        str6 = str;
                                        personCache4 = personCache5;
                                        senderName = senderName22;
                                        messagingStyle2 = messagingStyle;
                                    } else {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("message text is null for ");
                                        sb.append(messageObject2.getId());
                                        sb.append(" did = ");
                                        preview = preview22;
                                        sb.append(messageObject2.getDialogId());
                                        FileLog.w(sb.toString());
                                        str7 = str3;
                                        selfUserId3 = selfUserId4;
                                        str5 = str10;
                                        str6 = str;
                                        personCache4 = personCache5;
                                        senderName = senderName22;
                                        messagingStyle2 = messagingStyle;
                                    }
                                } else {
                                    NotificationCompat.MessagingStyle messagingStyle5 = messagingStyle;
                                    preview = preview22;
                                    if (text.length() > 0) {
                                        text.append("\n\n");
                                    }
                                    if (dialogId3 != selfUserId4 && messageObject2.messageOwner.from_scheduled && DialogObject.isUserDialog(dialogId3)) {
                                        String message3 = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", org.telegram.messenger.beta.R.string.NotificationMessageScheduledName), message2);
                                        text.append(message3);
                                        message = message3;
                                    } else {
                                        if (senderName22[0] != null) {
                                            text.append(String.format("%1$s: %2$s", senderName22[0], message2));
                                        } else {
                                            text.append(message2);
                                        }
                                        message = message2;
                                    }
                                    if (DialogObject.isUserDialog(dialogId3)) {
                                        uid = dialogId3;
                                    } else if (isChannel) {
                                        uid = -dialogId3;
                                    } else if (DialogObject.isChatDialog(dialogId3)) {
                                        uid = messageObject2.getSenderId();
                                    } else {
                                        uid = dialogId3;
                                    }
                                    Person person2 = personCache5.get(uid);
                                    if (senderName22[0] == null) {
                                        if (!waitingForPasscode) {
                                            messagingStyle3 = messagingStyle5;
                                            selfUserId3 = selfUserId4;
                                            str8 = str10;
                                            str9 = str;
                                        } else if (DialogObject.isChatDialog(dialogId3)) {
                                            if (isChannel) {
                                                messagingStyle3 = messagingStyle5;
                                                selfUserId3 = selfUserId4;
                                                if (Build.VERSION.SDK_INT <= 27) {
                                                    str9 = str;
                                                    str8 = str10;
                                                } else {
                                                    str9 = str;
                                                    str8 = str10;
                                                    personName = LocaleController.getString(str9, org.telegram.messenger.beta.R.string.NotificationHiddenChatName);
                                                }
                                            } else {
                                                messagingStyle3 = messagingStyle5;
                                                selfUserId3 = selfUserId4;
                                                str9 = str;
                                                str8 = str10;
                                                personName = LocaleController.getString("NotificationHiddenChatUserName", org.telegram.messenger.beta.R.string.NotificationHiddenChatUserName);
                                            }
                                        } else {
                                            messagingStyle3 = messagingStyle5;
                                            selfUserId3 = selfUserId4;
                                            str9 = str;
                                            if (Build.VERSION.SDK_INT <= 27) {
                                                str8 = str10;
                                            } else {
                                                str8 = str10;
                                                personName = LocaleController.getString(str8, org.telegram.messenger.beta.R.string.NotificationHiddenName);
                                            }
                                        }
                                        personName = "";
                                    } else {
                                        messagingStyle3 = messagingStyle5;
                                        selfUserId3 = selfUserId4;
                                        str8 = str10;
                                        str9 = str;
                                        personName = senderName22[0];
                                    }
                                    if (person2 != null) {
                                        senderName = senderName22;
                                        if (TextUtils.equals(person2.getName(), personName)) {
                                            person = person2;
                                            str6 = str9;
                                            str5 = str8;
                                            if (DialogObject.isEncryptedDialog(dialogId3)) {
                                                boolean setPhoto = false;
                                                if (!preview[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice()) {
                                                    personCache4 = personCache5;
                                                    messagingStyle2 = messagingStyle3;
                                                    str7 = str3;
                                                } else if (waitingForPasscode || messageObject2.isSecretMedia()) {
                                                    personCache4 = personCache5;
                                                    messagingStyle2 = messagingStyle3;
                                                    str7 = str3;
                                                } else if (messageObject2.type == 1 || messageObject2.isSticker()) {
                                                    File attach = getFileLoader().getPathToMessage(messageObject2.messageOwner);
                                                    NotificationCompat.MessagingStyle.Message msg = new NotificationCompat.MessagingStyle.Message(message, messageObject2.messageOwner.date * 1000, person);
                                                    String mimeType = messageObject2.isSticker() ? "image/webp" : "image/jpeg";
                                                    if (attach.exists()) {
                                                        try {
                                                            uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", attach);
                                                        } catch (Exception e3) {
                                                            FileLog.e(e3);
                                                            uri2 = null;
                                                        }
                                                        personCache4 = personCache5;
                                                    } else if (getFileLoader().isLoadingFile(attach.getName())) {
                                                        Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.AUTHORITY).appendPath("msg_media_raw");
                                                        StringBuilder sb2 = new StringBuilder();
                                                        personCache4 = personCache5;
                                                        sb2.append(notificationsController.currentAccount);
                                                        sb2.append(str3);
                                                        Uri.Builder _uri = appendPath.appendPath(sb2.toString()).appendPath(attach.getName()).appendQueryParameter("final_path", attach.getAbsolutePath());
                                                        uri2 = _uri.build();
                                                    } else {
                                                        personCache4 = personCache5;
                                                        uri2 = null;
                                                    }
                                                    if (uri2 != null) {
                                                        msg.setData(mimeType, uri2);
                                                        messagingStyle2 = messagingStyle3;
                                                        messagingStyle2.addMessage(msg);
                                                        final Uri uriFinal = uri2;
                                                        str7 = str3;
                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uri2, 1);
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                ApplicationLoader.applicationContext.revokeUriPermission(uriFinal, 1);
                                                            }
                                                        }, SilenceSkippingAudioProcessor.DEFAULT_PADDING_SILENCE_US);
                                                        if (!TextUtils.isEmpty(messageObject2.caption)) {
                                                            messagingStyle2.addMessage(messageObject2.caption, messageObject2.messageOwner.date * 1000, person);
                                                        }
                                                        setPhoto = true;
                                                    } else {
                                                        messagingStyle2 = messagingStyle3;
                                                        str7 = str3;
                                                    }
                                                } else {
                                                    personCache4 = personCache5;
                                                    messagingStyle2 = messagingStyle3;
                                                    str7 = str3;
                                                }
                                                if (!setPhoto) {
                                                    messagingStyle2.addMessage(message, messageObject2.messageOwner.date * 1000, person);
                                                }
                                                if (preview[0] && !waitingForPasscode && messageObject2.isVoice()) {
                                                    List<NotificationCompat.MessagingStyle.Message> messages = messagingStyle2.getMessages();
                                                    if (!messages.isEmpty()) {
                                                        File f = getFileLoader().getPathToMessage(messageObject2.messageOwner);
                                                        if (Build.VERSION.SDK_INT >= 24) {
                                                            try {
                                                                uri = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", f);
                                                            } catch (Exception e4) {
                                                                uri = null;
                                                            }
                                                        } else {
                                                            uri = Uri.fromFile(f);
                                                        }
                                                        if (uri != null) {
                                                            NotificationCompat.MessagingStyle.Message addedMessage = messages.get(messages.size() - 1);
                                                            addedMessage.setData("audio/ogg", uri);
                                                        }
                                                    }
                                                }
                                            } else {
                                                personCache4 = personCache5;
                                                messagingStyle2 = messagingStyle3;
                                                str7 = str3;
                                                messagingStyle2.addMessage(message, messageObject2.messageOwner.date * 1000, person);
                                            }
                                            if (dialogId3 == 777000 && messageObject2.messageOwner.reply_markup != null) {
                                                ArrayList<TLRPC.TL_keyboardButtonRow> rows3 = messageObject2.messageOwner.reply_markup.rows;
                                                rows2 = rows3;
                                                rowsMid = messageObject2.getId();
                                            }
                                        }
                                    } else {
                                        senderName = senderName22;
                                    }
                                    Person.Builder personBuilder3 = new Person.Builder().setName(personName);
                                    if (!preview[0] || DialogObject.isEncryptedDialog(dialogId3)) {
                                        str6 = str9;
                                        str5 = str8;
                                    } else if (Build.VERSION.SDK_INT < 28) {
                                        str6 = str9;
                                        str5 = str8;
                                    } else {
                                        if (DialogObject.isUserDialog(dialogId3)) {
                                            str6 = str9;
                                            str5 = str8;
                                        } else if (isChannel) {
                                            str6 = str9;
                                            str5 = str8;
                                        } else {
                                            str6 = str9;
                                            long fromId = messageObject2.getSenderId();
                                            str5 = str8;
                                            TLRPC.User sender3 = getMessagesController().getUser(Long.valueOf(fromId));
                                            if (sender3 == null && (sender3 = getMessagesStorage().getUserSync(fromId)) != null) {
                                                getMessagesController().putUser(sender3, true);
                                            }
                                            if (sender3 != null && sender3.photo != null && sender3.photo.photo_small != null && sender3.photo.photo_small.volume_id != 0 && sender3.photo.photo_small.local_id != 0) {
                                                avatar = getFileLoader().getPathToAttach(sender3.photo.photo_small, true);
                                            } else {
                                                avatar = null;
                                            }
                                            notificationsController.loadRoundAvatar(avatar, personBuilder3);
                                        }
                                        avatar = avatalFile3;
                                        notificationsController.loadRoundAvatar(avatar, personBuilder3);
                                    }
                                    Person person3 = personBuilder3.build();
                                    personCache5.put(uid, person3);
                                    person = person3;
                                    if (DialogObject.isEncryptedDialog(dialogId3)) {
                                    }
                                    if (dialogId3 == 777000) {
                                        ArrayList<TLRPC.TL_keyboardButtonRow> rows32 = messageObject2.messageOwner.reply_markup.rows;
                                        rows2 = rows32;
                                        rowsMid = messageObject2.getId();
                                    }
                                }
                                a3--;
                                messagingStyle = messagingStyle2;
                                senderName22 = senderName;
                                personCache5 = personCache4;
                                messageObjects = messageObjects3;
                                str11 = str14;
                                name2 = name3;
                                preview22 = preview;
                                str3 = str7;
                                selfUserId4 = selfUserId3;
                                str = str6;
                                str10 = str5;
                            }
                            LongSparseArray<Person> personCache62 = personCache5;
                            String str122 = str11;
                            ArrayList<MessageObject> messageObjects22 = messageObjects;
                            String name52 = name2;
                            long selfUserId52 = selfUserId4;
                            NotificationCompat.MessagingStyle messagingStyle42 = messagingStyle;
                            Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            intent2.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                            intent2.setFlags(ConnectionsManager.FileTypeFile);
                            intent2.addCategory("android.intent.category.LAUNCHER");
                            if (DialogObject.isEncryptedDialog(dialogId3)) {
                                intent2.putExtra("encId", DialogObject.getEncryptedChatId(dialogId3));
                            } else if (DialogObject.isUserDialog(dialogId3)) {
                                intent2.putExtra("userId", dialogId3);
                            } else {
                                intent2.putExtra("chatId", -dialogId3);
                            }
                            String str132 = str122;
                            intent2.putExtra(str132, notificationsController.currentAccount);
                            PendingIntent contentIntent2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, C.BUFFER_FLAG_ENCRYPTED);
                            NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                            if (wearReplyAction2 == null) {
                                wearReplyAction3 = wearReplyAction2;
                            } else {
                                wearReplyAction3 = wearReplyAction2;
                                wearableExtender2.addAction(wearReplyAction3);
                            }
                            Intent msgHeardIntent2 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                            msgHeardIntent2.addFlags(32);
                            msgHeardIntent2.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                            msgHeardIntent2.putExtra("dialog_id", dialogId3);
                            int maxId42 = maxId;
                            msgHeardIntent2.putExtra(str2, maxId42);
                            msgHeardIntent2.putExtra(str132, notificationsController.currentAccount);
                            PendingIntent readPendingIntent22 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent2, 134217728);
                            NotificationCompat.Action readAction2 = new NotificationCompat.Action.Builder((int) org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), readPendingIntent22).setSemanticAction(2).setShowsUserInterface(false).build();
                            if (!DialogObject.isEncryptedDialog(dialogId3)) {
                                if (DialogObject.isUserDialog(dialogId3)) {
                                    readPendingIntent = readPendingIntent22;
                                    dismissalID = "tguser" + dialogId3 + "_" + maxId42;
                                } else {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("tgchat");
                                    readPendingIntent = readPendingIntent22;
                                    sb3.append(-dialogId3);
                                    sb3.append("_");
                                    sb3.append(maxId42);
                                    dismissalID = sb3.toString();
                                }
                            } else {
                                readPendingIntent = readPendingIntent22;
                                dismissalID = dialogId3 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(dialogId3) + "_" + maxId42 : null;
                            }
                            if (dismissalID != null) {
                                wearableExtender2.setDismissalId(dismissalID);
                                NotificationCompat.WearableExtender summaryExtender = new NotificationCompat.WearableExtender();
                                summaryExtender.setDismissalId("summary_" + dismissalID);
                                maxId2 = maxId42;
                                wearReplyAction4 = wearReplyAction3;
                                selfUserId2 = selfUserId52;
                                notificationBuilder.extend(summaryExtender);
                            } else {
                                maxId2 = maxId42;
                                wearReplyAction4 = wearReplyAction3;
                                selfUserId2 = selfUserId52;
                            }
                            wearableExtender2.setBridgeTag("tgaccount" + selfUserId2);
                            long date2 = ((long) messageObjects22.get(0).messageOwner.date) * 1000;
                            long selfUserId62 = selfUserId2;
                            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name52).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects22.size()).setColor(-15618822).setGroupSummary(false).setWhen(date2).setShowWhen(true).setStyle(messagingStyle42).setContentIntent(contentIntent2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - date2)).setCategory(NotificationCompat.CATEGORY_MESSAGE);
                            dismissIntent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            dismissIntent.putExtra("messageDate", maxDate);
                            dismissIntent.putExtra("dialogId", dialogId3);
                            dismissIntent.putExtra(str132, notificationsController.currentAccount);
                            builder2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), dismissIntent, 134217728));
                            if (useSummaryNotification3) {
                                builder2.setGroup(notificationsController.notificationGroup);
                                builder2.setGroupAlertBehavior(1);
                            }
                            if (wearReplyAction4 == null) {
                                wearReplyAction5 = wearReplyAction4;
                            } else {
                                wearReplyAction5 = wearReplyAction4;
                                builder2.addAction(wearReplyAction5);
                            }
                            if (!waitingForPasscode) {
                                builder2.addAction(readAction2);
                            }
                            if (sortedDialogs.size() != 1 && !TextUtils.isEmpty(summary)) {
                                builder2.setSubText(summary);
                            }
                            if (DialogObject.isEncryptedDialog(dialogId3)) {
                                builder2.setLocalOnly(true);
                            }
                            if (avatarBitmap2 == null) {
                                wearReplyAction6 = wearReplyAction5;
                                avatarBitmap3 = avatarBitmap2;
                            } else {
                                wearReplyAction6 = wearReplyAction5;
                                avatarBitmap3 = avatarBitmap2;
                                builder2.setLargeIcon(avatarBitmap3);
                            }
                            if (!AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                                dialogId = dialogId3;
                                avatarBitmap4 = avatarBitmap3;
                                dismissIntent2 = dismissIntent;
                                text2 = text;
                                rowsMid2 = rowsMid;
                            } else if (rows2 != null) {
                                int rc = rows2.size();
                                int r = 0;
                                while (r < rc) {
                                    Bitmap avatarBitmap7 = avatarBitmap3;
                                    ArrayList<TLRPC.TL_keyboardButtonRow> rows4 = rows2;
                                    int rc2 = rc;
                                    TLRPC.TL_keyboardButtonRow row = rows4.get(r);
                                    int cc = row.buttons.size();
                                    int c = 0;
                                    while (c < cc) {
                                        int cc2 = cc;
                                        TLRPC.KeyboardButton button = row.buttons.get(c);
                                        TLRPC.TL_keyboardButtonRow row2 = row;
                                        if (!(button instanceof TLRPC.TL_keyboardButtonCallback)) {
                                            str4 = str132;
                                            dialogId2 = dialogId3;
                                            dismissIntent3 = dismissIntent;
                                            text3 = text;
                                            rowsMid3 = rowsMid;
                                        } else {
                                            dismissIntent3 = dismissIntent;
                                            text3 = text;
                                            Intent callbackIntent = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                            callbackIntent.putExtra(str132, notificationsController.currentAccount);
                                            callbackIntent.putExtra("did", dialogId3);
                                            if (button.data != null) {
                                                callbackIntent.putExtra("data", button.data);
                                            }
                                            rowsMid3 = rowsMid;
                                            callbackIntent.putExtra("mid", rowsMid3);
                                            String str15 = button.text;
                                            str4 = str132;
                                            Context context = ApplicationLoader.applicationContext;
                                            int i3 = notificationsController.lastButtonId;
                                            dialogId2 = dialogId3;
                                            notificationsController.lastButtonId = i3 + 1;
                                            builder2.addAction(0, str15, PendingIntent.getBroadcast(context, i3, callbackIntent, 134217728));
                                        }
                                        c++;
                                        rowsMid = rowsMid3;
                                        cc = cc2;
                                        row = row2;
                                        dismissIntent = dismissIntent3;
                                        text = text3;
                                        dialogId3 = dialogId2;
                                        str132 = str4;
                                    }
                                    r++;
                                    avatarBitmap3 = avatarBitmap7;
                                    rc = rc2;
                                    text = text;
                                    dialogId3 = dialogId3;
                                    rows2 = rows4;
                                }
                                dialogId = dialogId3;
                                avatarBitmap4 = avatarBitmap3;
                                dismissIntent2 = dismissIntent;
                                text2 = text;
                                rowsMid2 = rowsMid;
                            } else {
                                dialogId = dialogId3;
                                avatarBitmap4 = avatarBitmap3;
                                dismissIntent2 = dismissIntent;
                                text2 = text;
                                rowsMid2 = rowsMid;
                            }
                            if (chat2 == null || user2 == null) {
                                user3 = user2;
                            } else {
                                user3 = user2;
                                if (user3.phone != null && user3.phone.length() > 0) {
                                    builder2.addPerson("tel:+" + user3.phone);
                                }
                            }
                            if (Build.VERSION.SDK_INT < 26) {
                                useSummaryNotification4 = useSummaryNotification3;
                                mainNotification5 = mainNotification4;
                            } else {
                                useSummaryNotification4 = useSummaryNotification3;
                                mainNotification5 = mainNotification4;
                                notificationsController.setNotificationChannel(mainNotification5, builder2, useSummaryNotification4);
                            }
                            personCache3 = personCache62;
                            useSummaryNotification2 = useSummaryNotification4;
                            oldIdsWear3 = oldIdsWear4;
                            selfUserId = selfUserId62;
                            mainNotification3 = mainNotification5;
                            holders3 = holders2;
                            holders3.add(new C1NotificationHolder(internalId2.intValue(), dialogId, name52, user3, chat2, builder2, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType));
                            notificationsController = this;
                            notificationsController.wearNotificationsIds.put(dialogId, internalId2);
                            b = b2 + 1;
                            holders4 = holders3;
                            size2 = size3;
                            preferences = preferences2;
                            useSummaryNotification5 = useSummaryNotification2;
                            maxCount = maxCount2;
                            sortedDialogs2 = sortedDialogs;
                            messagesByDialogs2 = messagesByDialogs;
                            oldIdsWear5 = oldIdsWear3;
                            selfUserId4 = selfUserId;
                            personCache5 = personCache3;
                            mainNotification6 = mainNotification3;
                            i = 0;
                        }
                        wearReplyAction = wearReplyAction7;
                        count = notificationsController.pushDialogs.get(dialogId3);
                        if (count == null) {
                        }
                        n = Math.max(count2.intValue(), messageObjects.size());
                        if (n > 1) {
                        }
                        String conversationName22 = name2;
                        conversationName = conversationName22;
                        selfPerson = personCache5.get(selfUserId4);
                        if (Build.VERSION.SDK_INT >= 28) {
                        }
                        maxId = maxId3;
                        n2 = n;
                        selfPerson2 = selfPerson;
                        boolean needAddPerson22 = !(lastMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest);
                        str3 = "";
                        if (selfPerson2 == null) {
                        }
                        selfPerson3 = selfPerson2;
                        messagingStyle = new NotificationCompat.MessagingStyle(str3);
                        if (Build.VERSION.SDK_INT >= 28) {
                        }
                        messagingStyle.setConversationTitle(conversationName);
                        messagingStyle.setGroupConversation(Build.VERSION.SDK_INT >= 28 || (!isChannel && DialogObject.isChatDialog(dialogId3)) || UserObject.isReplyUser(dialogId3));
                        text = new StringBuilder();
                        String[] senderName222 = new String[1];
                        wearReplyAction2 = wearReplyAction;
                        boolean[] preview222 = new boolean[1];
                        rowsMid = 0;
                        a3 = messageObjects.size() - 1;
                        ArrayList<TLRPC.TL_keyboardButtonRow> rows22 = null;
                        while (a3 >= 0) {
                        }
                        LongSparseArray<Person> personCache622 = personCache5;
                        String str1222 = str11;
                        ArrayList<MessageObject> messageObjects222 = messageObjects;
                        String name522 = name2;
                        long selfUserId522 = selfUserId4;
                        NotificationCompat.MessagingStyle messagingStyle422 = messagingStyle;
                        Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        intent22.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                        intent22.setFlags(ConnectionsManager.FileTypeFile);
                        intent22.addCategory("android.intent.category.LAUNCHER");
                        if (DialogObject.isEncryptedDialog(dialogId3)) {
                        }
                        String str1322 = str1222;
                        intent22.putExtra(str1322, notificationsController.currentAccount);
                        PendingIntent contentIntent22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, C.BUFFER_FLAG_ENCRYPTED);
                        NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                        if (wearReplyAction2 == null) {
                        }
                        Intent msgHeardIntent22 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                        msgHeardIntent22.addFlags(32);
                        msgHeardIntent22.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                        msgHeardIntent22.putExtra("dialog_id", dialogId3);
                        int maxId422 = maxId;
                        msgHeardIntent22.putExtra(str2, maxId422);
                        msgHeardIntent22.putExtra(str1322, notificationsController.currentAccount);
                        PendingIntent readPendingIntent222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent22, 134217728);
                        NotificationCompat.Action readAction22 = new NotificationCompat.Action.Builder((int) org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), readPendingIntent222).setSemanticAction(2).setShowsUserInterface(false).build();
                        if (!DialogObject.isEncryptedDialog(dialogId3)) {
                        }
                        if (dismissalID != null) {
                        }
                        wearableExtender22.setBridgeTag("tgaccount" + selfUserId2);
                        long date22 = ((long) messageObjects222.get(0).messageOwner.date) * 1000;
                        long selfUserId622 = selfUserId2;
                        NotificationCompat.Builder builder22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name522).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects222.size()).setColor(-15618822).setGroupSummary(false).setWhen(date22).setShowWhen(true).setStyle(messagingStyle422).setContentIntent(contentIntent22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - date22)).setCategory(NotificationCompat.CATEGORY_MESSAGE);
                        dismissIntent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        dismissIntent.putExtra("messageDate", maxDate);
                        dismissIntent.putExtra("dialogId", dialogId3);
                        dismissIntent.putExtra(str1322, notificationsController.currentAccount);
                        builder22.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), dismissIntent, 134217728));
                        if (useSummaryNotification3) {
                        }
                        if (wearReplyAction4 == null) {
                        }
                        if (!waitingForPasscode) {
                        }
                        if (sortedDialogs.size() != 1) {
                        }
                        if (DialogObject.isEncryptedDialog(dialogId3)) {
                        }
                        if (avatarBitmap2 == null) {
                        }
                        if (!AndroidUtilities.needShowPasscode(false)) {
                        }
                        dialogId = dialogId3;
                        avatarBitmap4 = avatarBitmap3;
                        dismissIntent2 = dismissIntent;
                        text2 = text;
                        rowsMid2 = rowsMid;
                        if (chat2 == null) {
                        }
                        user3 = user2;
                        if (Build.VERSION.SDK_INT < 26) {
                        }
                        personCache3 = personCache622;
                        useSummaryNotification2 = useSummaryNotification4;
                        oldIdsWear3 = oldIdsWear4;
                        selfUserId = selfUserId622;
                        mainNotification3 = mainNotification5;
                        holders3 = holders2;
                        holders3.add(new C1NotificationHolder(internalId2.intValue(), dialogId, name522, user3, chat2, builder22, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType));
                        notificationsController = this;
                        notificationsController.wearNotificationsIds.put(dialogId, internalId2);
                        b = b2 + 1;
                        holders4 = holders3;
                        size2 = size3;
                        preferences = preferences2;
                        useSummaryNotification5 = useSummaryNotification2;
                        maxCount = maxCount2;
                        sortedDialogs2 = sortedDialogs;
                        messagesByDialogs2 = messagesByDialogs;
                        oldIdsWear5 = oldIdsWear3;
                        selfUserId4 = selfUserId;
                        personCache5 = personCache3;
                        mainNotification6 = mainNotification3;
                        i = 0;
                    } else {
                        useSummaryNotification3 = useSummaryNotification5;
                        maxDate = maxDate2;
                        oldIdsWear4 = oldIdsWear5;
                        TLRPC.Chat chat3 = getMessagesController().getChat(Long.valueOf(-dialogId3));
                        if (chat3 == null) {
                            if (lastMessageObject2.isFcmMessage()) {
                                isSupergroup = lastMessageObject2.isSupergroup();
                                String name6 = lastMessageObject2.localName;
                                chat = chat3;
                                name = name6;
                                isChannel = lastMessageObject2.localChannel;
                                user = null;
                            } else {
                                if (!BuildVars.LOGS_ENABLED) {
                                    personCache3 = personCache5;
                                    mainNotification3 = mainNotification6;
                                    selfUserId = selfUserId4;
                                    holders3 = holders2;
                                    oldIdsWear3 = oldIdsWear4;
                                    useSummaryNotification2 = useSummaryNotification3;
                                } else {
                                    FileLog.w("not found chat to show dialog notification " + dialogId3);
                                    personCache3 = personCache5;
                                    mainNotification3 = mainNotification6;
                                    selfUserId = selfUserId4;
                                    holders3 = holders2;
                                    oldIdsWear3 = oldIdsWear4;
                                    useSummaryNotification2 = useSummaryNotification3;
                                }
                                b = b2 + 1;
                                holders4 = holders3;
                                size2 = size3;
                                preferences = preferences2;
                                useSummaryNotification5 = useSummaryNotification2;
                                maxCount = maxCount2;
                                sortedDialogs2 = sortedDialogs;
                                messagesByDialogs2 = messagesByDialogs;
                                oldIdsWear5 = oldIdsWear3;
                                selfUserId4 = selfUserId;
                                personCache5 = personCache3;
                                mainNotification6 = mainNotification3;
                                i = 0;
                            }
                        } else {
                            boolean isSupergroup2 = chat3.megagroup;
                            isChannel = ChatObject.isChannel(chat3) && !chat3.megagroup;
                            String name7 = chat3.title;
                            if (chat3.photo != null && chat3.photo.photo_small != null && chat3.photo.photo_small.volume_id != 0 && chat3.photo.photo_small.local_id != 0) {
                                chat = chat3;
                                isSupergroup = isSupergroup2;
                                name = name7;
                                photoPath3 = chat3.photo.photo_small;
                                user = null;
                            } else {
                                chat = chat3;
                                isSupergroup = isSupergroup2;
                                name = name7;
                                user = null;
                            }
                        }
                        if (!waitingForPasscode) {
                        }
                        String str102 = "NotificationHiddenName";
                        if (photoPath == null) {
                        }
                        if (chat == null) {
                        }
                        String str112 = "currentAccount";
                        if (!isChannel) {
                        }
                        if (canReply) {
                        }
                        wearReplyAction7 = null;
                        str2 = "max_id";
                        avatalFile3 = avatalFile2;
                        lastMessageObject = lastMessageObject2;
                        internalId2 = internalId;
                        wearReplyAction = wearReplyAction7;
                        count = notificationsController.pushDialogs.get(dialogId3);
                        if (count == null) {
                        }
                        n = Math.max(count2.intValue(), messageObjects.size());
                        if (n > 1) {
                        }
                        String conversationName222 = name2;
                        conversationName = conversationName222;
                        selfPerson = personCache5.get(selfUserId4);
                        if (Build.VERSION.SDK_INT >= 28) {
                        }
                        maxId = maxId3;
                        n2 = n;
                        selfPerson2 = selfPerson;
                        boolean needAddPerson222 = !(lastMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest);
                        str3 = "";
                        if (selfPerson2 == null) {
                        }
                        selfPerson3 = selfPerson2;
                        messagingStyle = new NotificationCompat.MessagingStyle(str3);
                        if (Build.VERSION.SDK_INT >= 28) {
                        }
                        messagingStyle.setConversationTitle(conversationName);
                        messagingStyle.setGroupConversation(Build.VERSION.SDK_INT >= 28 || (!isChannel && DialogObject.isChatDialog(dialogId3)) || UserObject.isReplyUser(dialogId3));
                        text = new StringBuilder();
                        String[] senderName2222 = new String[1];
                        wearReplyAction2 = wearReplyAction;
                        boolean[] preview2222 = new boolean[1];
                        rowsMid = 0;
                        a3 = messageObjects.size() - 1;
                        ArrayList<TLRPC.TL_keyboardButtonRow> rows222 = null;
                        while (a3 >= 0) {
                        }
                        LongSparseArray<Person> personCache6222 = personCache5;
                        String str12222 = str112;
                        ArrayList<MessageObject> messageObjects2222 = messageObjects;
                        String name5222 = name2;
                        long selfUserId5222 = selfUserId4;
                        NotificationCompat.MessagingStyle messagingStyle4222 = messagingStyle;
                        Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        intent222.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                        intent222.setFlags(ConnectionsManager.FileTypeFile);
                        intent222.addCategory("android.intent.category.LAUNCHER");
                        if (DialogObject.isEncryptedDialog(dialogId3)) {
                        }
                        String str13222 = str12222;
                        intent222.putExtra(str13222, notificationsController.currentAccount);
                        PendingIntent contentIntent222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, C.BUFFER_FLAG_ENCRYPTED);
                        NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                        if (wearReplyAction2 == null) {
                        }
                        Intent msgHeardIntent222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                        msgHeardIntent222.addFlags(32);
                        msgHeardIntent222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                        msgHeardIntent222.putExtra("dialog_id", dialogId3);
                        int maxId4222 = maxId;
                        msgHeardIntent222.putExtra(str2, maxId4222);
                        msgHeardIntent222.putExtra(str13222, notificationsController.currentAccount);
                        PendingIntent readPendingIntent2222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent222, 134217728);
                        NotificationCompat.Action readAction222 = new NotificationCompat.Action.Builder((int) org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), readPendingIntent2222).setSemanticAction(2).setShowsUserInterface(false).build();
                        if (!DialogObject.isEncryptedDialog(dialogId3)) {
                        }
                        if (dismissalID != null) {
                        }
                        wearableExtender222.setBridgeTag("tgaccount" + selfUserId2);
                        long date222 = ((long) messageObjects2222.get(0).messageOwner.date) * 1000;
                        long selfUserId6222 = selfUserId2;
                        NotificationCompat.Builder builder222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name5222).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects2222.size()).setColor(-15618822).setGroupSummary(false).setWhen(date222).setShowWhen(true).setStyle(messagingStyle4222).setContentIntent(contentIntent222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - date222)).setCategory(NotificationCompat.CATEGORY_MESSAGE);
                        dismissIntent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        dismissIntent.putExtra("messageDate", maxDate);
                        dismissIntent.putExtra("dialogId", dialogId3);
                        dismissIntent.putExtra(str13222, notificationsController.currentAccount);
                        builder222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), dismissIntent, 134217728));
                        if (useSummaryNotification3) {
                        }
                        if (wearReplyAction4 == null) {
                        }
                        if (!waitingForPasscode) {
                        }
                        if (sortedDialogs.size() != 1) {
                        }
                        if (DialogObject.isEncryptedDialog(dialogId3)) {
                        }
                        if (avatarBitmap2 == null) {
                        }
                        if (!AndroidUtilities.needShowPasscode(false)) {
                        }
                        dialogId = dialogId3;
                        avatarBitmap4 = avatarBitmap3;
                        dismissIntent2 = dismissIntent;
                        text2 = text;
                        rowsMid2 = rowsMid;
                        if (chat2 == null) {
                        }
                        user3 = user2;
                        if (Build.VERSION.SDK_INT < 26) {
                        }
                        personCache3 = personCache6222;
                        useSummaryNotification2 = useSummaryNotification4;
                        oldIdsWear3 = oldIdsWear4;
                        selfUserId = selfUserId6222;
                        mainNotification3 = mainNotification5;
                        holders3 = holders2;
                        holders3.add(new C1NotificationHolder(internalId2.intValue(), dialogId, name5222, user3, chat2, builder222, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType));
                        notificationsController = this;
                        notificationsController.wearNotificationsIds.put(dialogId, internalId2);
                        b = b2 + 1;
                        holders4 = holders3;
                        size2 = size3;
                        preferences = preferences2;
                        useSummaryNotification5 = useSummaryNotification2;
                        maxCount = maxCount2;
                        sortedDialogs2 = sortedDialogs;
                        messagesByDialogs2 = messagesByDialogs;
                        oldIdsWear5 = oldIdsWear3;
                        selfUserId4 = selfUserId;
                        personCache5 = personCache3;
                        mainNotification6 = mainNotification3;
                        i = 0;
                    }
                } else {
                    useSummaryNotification3 = useSummaryNotification5;
                    maxDate = maxDate2;
                    oldIdsWear4 = oldIdsWear5;
                    canReply = false;
                    if (dialogId3 == globalSecretChatId) {
                        user = null;
                    } else {
                        int encryptedChatId = DialogObject.getEncryptedChatId(dialogId3);
                        TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            if (!BuildVars.LOGS_ENABLED) {
                                personCache3 = personCache5;
                                mainNotification3 = mainNotification6;
                                selfUserId = selfUserId4;
                                holders3 = holders2;
                                oldIdsWear3 = oldIdsWear4;
                                useSummaryNotification2 = useSummaryNotification3;
                            } else {
                                FileLog.w("not found secret chat to show dialog notification " + encryptedChatId);
                                personCache3 = personCache5;
                                mainNotification3 = mainNotification6;
                                selfUserId = selfUserId4;
                                holders3 = holders2;
                                oldIdsWear3 = oldIdsWear4;
                                useSummaryNotification2 = useSummaryNotification3;
                            }
                        } else {
                            user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                            if (user == null) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.w("not found secret chat user to show dialog notification " + encryptedChat.user_id);
                                    personCache3 = personCache5;
                                    mainNotification3 = mainNotification6;
                                    selfUserId = selfUserId4;
                                    holders3 = holders2;
                                    oldIdsWear3 = oldIdsWear4;
                                    useSummaryNotification2 = useSummaryNotification3;
                                } else {
                                    personCache3 = personCache5;
                                    mainNotification3 = mainNotification6;
                                    selfUserId = selfUserId4;
                                    holders3 = holders2;
                                    oldIdsWear3 = oldIdsWear4;
                                    useSummaryNotification2 = useSummaryNotification3;
                                }
                            }
                        }
                        b = b2 + 1;
                        holders4 = holders3;
                        size2 = size3;
                        preferences = preferences2;
                        useSummaryNotification5 = useSummaryNotification2;
                        maxCount = maxCount2;
                        sortedDialogs2 = sortedDialogs;
                        messagesByDialogs2 = messagesByDialogs;
                        oldIdsWear5 = oldIdsWear3;
                        selfUserId4 = selfUserId;
                        personCache5 = personCache3;
                        mainNotification6 = mainNotification3;
                        i = 0;
                    }
                    name = LocaleController.getString("SecretChatName", org.telegram.messenger.beta.R.string.SecretChatName);
                    photoPath3 = null;
                    chat = null;
                    if (!waitingForPasscode) {
                    }
                    String str1022 = "NotificationHiddenName";
                    if (photoPath == null) {
                    }
                    if (chat == null) {
                    }
                    String str1122 = "currentAccount";
                    if (!isChannel) {
                    }
                    if (canReply) {
                    }
                    wearReplyAction7 = null;
                    str2 = "max_id";
                    avatalFile3 = avatalFile2;
                    lastMessageObject = lastMessageObject2;
                    internalId2 = internalId;
                    wearReplyAction = wearReplyAction7;
                    count = notificationsController.pushDialogs.get(dialogId3);
                    if (count == null) {
                    }
                    n = Math.max(count2.intValue(), messageObjects.size());
                    if (n > 1) {
                    }
                    String conversationName2222 = name2;
                    conversationName = conversationName2222;
                    selfPerson = personCache5.get(selfUserId4);
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    maxId = maxId3;
                    n2 = n;
                    selfPerson2 = selfPerson;
                    boolean needAddPerson2222 = !(lastMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest);
                    str3 = "";
                    if (selfPerson2 == null) {
                    }
                    selfPerson3 = selfPerson2;
                    messagingStyle = new NotificationCompat.MessagingStyle(str3);
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    messagingStyle.setConversationTitle(conversationName);
                    messagingStyle.setGroupConversation(Build.VERSION.SDK_INT >= 28 || (!isChannel && DialogObject.isChatDialog(dialogId3)) || UserObject.isReplyUser(dialogId3));
                    text = new StringBuilder();
                    String[] senderName22222 = new String[1];
                    wearReplyAction2 = wearReplyAction;
                    boolean[] preview22222 = new boolean[1];
                    rowsMid = 0;
                    a3 = messageObjects.size() - 1;
                    ArrayList<TLRPC.TL_keyboardButtonRow> rows2222 = null;
                    while (a3 >= 0) {
                    }
                    LongSparseArray<Person> personCache62222 = personCache5;
                    String str122222 = str1122;
                    ArrayList<MessageObject> messageObjects22222 = messageObjects;
                    String name52222 = name2;
                    long selfUserId52222 = selfUserId4;
                    NotificationCompat.MessagingStyle messagingStyle42222 = messagingStyle;
                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent2222.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                    intent2222.setFlags(ConnectionsManager.FileTypeFile);
                    intent2222.addCategory("android.intent.category.LAUNCHER");
                    if (DialogObject.isEncryptedDialog(dialogId3)) {
                    }
                    String str132222 = str122222;
                    intent2222.putExtra(str132222, notificationsController.currentAccount);
                    PendingIntent contentIntent2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, C.BUFFER_FLAG_ENCRYPTED);
                    NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
                    if (wearReplyAction2 == null) {
                    }
                    Intent msgHeardIntent2222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    msgHeardIntent2222.addFlags(32);
                    msgHeardIntent2222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    msgHeardIntent2222.putExtra("dialog_id", dialogId3);
                    int maxId42222 = maxId;
                    msgHeardIntent2222.putExtra(str2, maxId42222);
                    msgHeardIntent2222.putExtra(str132222, notificationsController.currentAccount);
                    PendingIntent readPendingIntent22222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent2222, 134217728);
                    NotificationCompat.Action readAction2222 = new NotificationCompat.Action.Builder((int) org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), readPendingIntent22222).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (!DialogObject.isEncryptedDialog(dialogId3)) {
                    }
                    if (dismissalID != null) {
                    }
                    wearableExtender2222.setBridgeTag("tgaccount" + selfUserId2);
                    long date2222 = ((long) messageObjects22222.get(0).messageOwner.date) * 1000;
                    long selfUserId62222 = selfUserId2;
                    NotificationCompat.Builder builder2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name52222).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects22222.size()).setColor(-15618822).setGroupSummary(false).setWhen(date2222).setShowWhen(true).setStyle(messagingStyle42222).setContentIntent(contentIntent2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - date2222)).setCategory(NotificationCompat.CATEGORY_MESSAGE);
                    dismissIntent = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    dismissIntent.putExtra("messageDate", maxDate);
                    dismissIntent.putExtra("dialogId", dialogId3);
                    dismissIntent.putExtra(str132222, notificationsController.currentAccount);
                    builder2222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), dismissIntent, 134217728));
                    if (useSummaryNotification3) {
                    }
                    if (wearReplyAction4 == null) {
                    }
                    if (!waitingForPasscode) {
                    }
                    if (sortedDialogs.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(dialogId3)) {
                    }
                    if (avatarBitmap2 == null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    dialogId = dialogId3;
                    avatarBitmap4 = avatarBitmap3;
                    dismissIntent2 = dismissIntent;
                    text2 = text;
                    rowsMid2 = rowsMid;
                    if (chat2 == null) {
                    }
                    user3 = user2;
                    if (Build.VERSION.SDK_INT < 26) {
                    }
                    personCache3 = personCache62222;
                    useSummaryNotification2 = useSummaryNotification4;
                    oldIdsWear3 = oldIdsWear4;
                    selfUserId = selfUserId62222;
                    mainNotification3 = mainNotification5;
                    holders3 = holders2;
                    holders3.add(new C1NotificationHolder(internalId2.intValue(), dialogId, name52222, user3, chat2, builder2222, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType));
                    notificationsController = this;
                    notificationsController.wearNotificationsIds.put(dialogId, internalId2);
                    b = b2 + 1;
                    holders4 = holders3;
                    size2 = size3;
                    preferences = preferences2;
                    useSummaryNotification5 = useSummaryNotification2;
                    maxCount = maxCount2;
                    sortedDialogs2 = sortedDialogs;
                    messagesByDialogs2 = messagesByDialogs;
                    oldIdsWear5 = oldIdsWear3;
                    selfUserId4 = selfUserId;
                    personCache5 = personCache3;
                    mainNotification6 = mainNotification3;
                    i = 0;
                }
            }
        }
        if (useSummaryNotification) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary with id " + notificationsController.notificationId);
            }
            try {
                mainNotification2 = mainNotification;
                try {
                    notificationManager.notify(notificationsController.notificationId, mainNotification2);
                } catch (SecurityException e5) {
                    e = e5;
                    FileLog.e(e);
                    resetNotificationSound(notificationBuilder, lastDialogId, chatName, vibrationPattern, ledColor, sound, importance, isDefault, isInApp, isSilent, chatType);
                    a = 0;
                    while (a < oldIdsWear.size()) {
                    }
                    LongSparseArray<Integer> oldIdsWear6 = oldIdsWear;
                    ArrayList<String> ids = new ArrayList<>(holders.size());
                    size = holders.size();
                    a2 = 0;
                    while (a2 < size) {
                    }
                }
            } catch (SecurityException e6) {
                e = e6;
                mainNotification2 = mainNotification;
            }
        } else if (notificationsController.openedInBubbleDialogs.isEmpty()) {
            notificationManager.cancel(notificationsController.notificationId);
        }
        a = 0;
        while (a < oldIdsWear.size()) {
            LongSparseArray<Integer> oldIdsWear7 = oldIdsWear;
            long did = oldIdsWear7.keyAt(a);
            if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(did))) {
                Integer id = oldIdsWear7.valueAt(a);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("cancel notification id " + id);
                }
                notificationManager.cancel(id.intValue());
            }
            a++;
            oldIdsWear = oldIdsWear7;
        }
        LongSparseArray<Integer> oldIdsWear62 = oldIdsWear;
        ArrayList<String> ids2 = new ArrayList<>(holders.size());
        size = holders.size();
        a2 = 0;
        while (a2 < size) {
            C1NotificationHolder holder = holders.get(a2);
            ids2.clear();
            if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(holder.dialogId)) {
                oldIdsWear2 = oldIdsWear62;
                personCache2 = personCache;
            } else {
                oldIdsWear2 = oldIdsWear62;
                personCache2 = personCache;
                String shortcutId = createNotificationShortcut(holder.notification, holder.dialogId, holder.name, holder.user, holder.chat, personCache2.get(holder.dialogId));
                if (shortcutId != null) {
                    ids2.add(shortcutId);
                }
            }
            holder.call();
            if (!unsupportedNotificationShortcut() && !ids2.isEmpty()) {
                ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, ids2);
            }
            a2++;
            personCache = personCache2;
            oldIdsWear62 = oldIdsWear2;
        }
    }

    /* renamed from: org.telegram.messenger.NotificationsController$1NotificationHolder */
    /* loaded from: classes4.dex */
    public class C1NotificationHolder {
        TLRPC.Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        TLRPC.User user;
        final /* synthetic */ String val$chatName;
        final /* synthetic */ int val$chatType;
        final /* synthetic */ int val$importance;
        final /* synthetic */ boolean val$isDefault;
        final /* synthetic */ boolean val$isInApp;
        final /* synthetic */ boolean val$isSilent;
        final /* synthetic */ int val$ledColor;
        final /* synthetic */ Uri val$sound;
        final /* synthetic */ long[] val$vibrationPattern;

        C1NotificationHolder(int i, long li, String n, TLRPC.User u, TLRPC.Chat c, NotificationCompat.Builder builder, String str, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
            NotificationsController.this = this$0;
            this.val$chatName = str;
            this.val$vibrationPattern = jArr;
            this.val$ledColor = i2;
            this.val$sound = uri;
            this.val$importance = i3;
            this.val$isDefault = z;
            this.val$isInApp = z2;
            this.val$isSilent = z3;
            this.val$chatType = i4;
            this.id = i;
            this.name = n;
            this.user = u;
            this.chat = c;
            this.notification = builder;
            this.dialogId = li;
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

    private void loadRoundAvatar(File avatar, Person.Builder personBuilder) {
        if (avatar != null) {
            try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(avatar), NotificationsController$$ExternalSyntheticLambda0.INSTANCE);
                IconCompat icon = IconCompat.createWithBitmap(bitmap);
                personBuilder.setIcon(icon);
            } catch (Throwable th) {
            }
        }
    }

    public static /* synthetic */ int lambda$loadRoundAvatar$35(Canvas canvas) {
        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        path.addRoundRect(0.0f, 0.0f, width, height, width / 2, width / 2, Path.Direction.CW);
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.m1103x9f53e1fc();
            }
        });
    }

    /* renamed from: lambda$playOutChatSound$38$org-telegram-messenger-NotificationsController */
    public /* synthetic */ void m1103x9f53e1fc() {
        try {
            if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundOutPlay) <= 100) {
                return;
            }
            this.lastSoundOutPlay = SystemClock.elapsedRealtime();
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(NotificationsController$$ExternalSyntheticLambda33.INSTANCE);
            }
            if (this.soundOut == 0 && !this.soundOutLoaded) {
                this.soundOutLoaded = true;
                this.soundOut = this.soundPool.load(ApplicationLoader.applicationContext, org.telegram.messenger.beta.R.raw.sound_out, 1);
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

    public static /* synthetic */ void lambda$playOutChatSound$37(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            try {
                soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void clearDialogNotificationsSettings(long did) {
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        SharedPreferences.Editor editor = preferences.edit();
        SharedPreferences.Editor remove = editor.remove("notify2_" + did);
        remove.remove(ContentMetadata.KEY_CUSTOM_PREFIX + did);
        getMessagesStorage().setDialogFlags(did, 0L);
        TLRPC.Dialog dialog = getMessagesController().dialogs_dict.get(did);
        if (dialog != null) {
            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
        }
        editor.commit();
        getNotificationsController().updateServerNotificationsSettings(did, true);
    }

    public void setDialogNotificationsSettings(long dialog_id, int setting) {
        long flags;
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        SharedPreferences.Editor editor = preferences.edit();
        TLRPC.Dialog dialog = MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(dialog_id);
        if (setting == 4) {
            boolean defaultEnabled = isGlobalNotificationsEnabled(dialog_id);
            if (defaultEnabled) {
                editor.remove("notify2_" + dialog_id);
            } else {
                editor.putInt("notify2_" + dialog_id, 0);
            }
            getMessagesStorage().setDialogFlags(dialog_id, 0L);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
            }
        } else {
            int untilTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
            if (setting == 0) {
                untilTime += 3600;
            } else if (setting == 1) {
                untilTime += 28800;
            } else if (setting == 2) {
                untilTime += 172800;
            } else if (setting == 3) {
                untilTime = Integer.MAX_VALUE;
            }
            if (setting == 3) {
                editor.putInt("notify2_" + dialog_id, 2);
                flags = 1;
            } else {
                editor.putInt("notify2_" + dialog_id, 3);
                editor.putInt("notifyuntil_" + dialog_id, untilTime);
                flags = (((long) untilTime) << 32) | 1;
            }
            getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(dialog_id);
            MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(dialog_id, flags);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                dialog.notify_settings.mute_until = untilTime;
            }
        }
        editor.commit();
        updateServerNotificationsSettings(dialog_id);
    }

    public void updateServerNotificationsSettings(long dialog_id) {
        updateServerNotificationsSettings(dialog_id, true);
    }

    public void updateServerNotificationsSettings(long dialogId, boolean post) {
        int i = 0;
        if (post) {
            getNotificationCenter().postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
        if (DialogObject.isEncryptedDialog(dialogId)) {
            return;
        }
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        TLRPC.TL_account_updateNotifySettings req = new TLRPC.TL_account_updateNotifySettings();
        req.settings = new TLRPC.TL_inputPeerNotifySettings();
        req.settings.flags |= 1;
        req.settings.show_previews = preferences.getBoolean("content_preview_" + dialogId, true);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings = req.settings;
        tL_inputPeerNotifySettings.flags = tL_inputPeerNotifySettings.flags | 2;
        req.settings.silent = preferences.getBoolean("silent_" + dialogId, false);
        int mute_type = preferences.getInt("notify2_" + dialogId, -1);
        if (mute_type != -1) {
            req.settings.flags |= 4;
            if (mute_type == 3) {
                req.settings.mute_until = preferences.getInt("notifyuntil_" + dialogId, 0);
            } else {
                TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings2 = req.settings;
                if (mute_type == 2) {
                    i = Integer.MAX_VALUE;
                }
                tL_inputPeerNotifySettings2.mute_until = i;
            }
        }
        long soundDocumentId = preferences.getLong("sound_document_id_" + dialogId, 0L);
        String soundPath = preferences.getString("sound_path_" + dialogId, null);
        TLRPC.TL_inputPeerNotifySettings tL_inputPeerNotifySettings3 = req.settings;
        tL_inputPeerNotifySettings3.flags = tL_inputPeerNotifySettings3.flags | 8;
        if (soundDocumentId != 0) {
            TLRPC.TL_notificationSoundRingtone ringtoneSound = new TLRPC.TL_notificationSoundRingtone();
            ringtoneSound.id = soundDocumentId;
            req.settings.sound = ringtoneSound;
        } else if (soundPath != null) {
            if (soundPath.equals("NoSound")) {
                req.settings.sound = new TLRPC.TL_notificationSoundNone();
            } else {
                TLRPC.TL_notificationSoundLocal localSound = new TLRPC.TL_notificationSoundLocal();
                localSound.title = preferences.getString("sound_" + dialogId, null);
                localSound.data = soundPath;
                req.settings.sound = localSound;
            }
        } else {
            req.settings.sound = new TLRPC.TL_notificationSoundDefault();
        }
        req.peer = new TLRPC.TL_inputNotifyPeer();
        ((TLRPC.TL_inputNotifyPeer) req.peer).peer = getMessagesController().getInputPeer(dialogId);
        getConnectionsManager().sendRequest(req, NotificationsController$$ExternalSyntheticLambda32.INSTANCE);
    }

    public static /* synthetic */ void lambda$updateServerNotificationsSettings$39(TLObject response, TLRPC.TL_error error) {
    }

    public void updateServerNotificationsSettings(int type) {
        String soundPathPref;
        String soundDocumentIdPref;
        String soundNamePref;
        SharedPreferences preferences = getAccountInstance().getNotificationsSettings();
        TLRPC.TL_account_updateNotifySettings req = new TLRPC.TL_account_updateNotifySettings();
        req.settings = new TLRPC.TL_inputPeerNotifySettings();
        req.settings.flags = 5;
        if (type == 0) {
            req.peer = new TLRPC.TL_inputNotifyChats();
            req.settings.mute_until = preferences.getInt("EnableGroup2", 0);
            req.settings.show_previews = preferences.getBoolean("EnablePreviewGroup", true);
            soundNamePref = "GroupSound";
            soundDocumentIdPref = "GroupSoundDocId";
            soundPathPref = "GroupSoundPath";
        } else if (type == 1) {
            req.peer = new TLRPC.TL_inputNotifyUsers();
            req.settings.mute_until = preferences.getInt("EnableAll2", 0);
            req.settings.show_previews = preferences.getBoolean("EnablePreviewAll", true);
            soundNamePref = "GlobalSound";
            soundDocumentIdPref = "GlobalSoundDocId";
            soundPathPref = "GlobalSoundPath";
        } else {
            req.peer = new TLRPC.TL_inputNotifyBroadcasts();
            req.settings.mute_until = preferences.getInt("EnableChannel2", 0);
            req.settings.show_previews = preferences.getBoolean("EnablePreviewChannel", true);
            soundNamePref = "ChannelSound";
            soundDocumentIdPref = "ChannelSoundDocId";
            soundPathPref = "ChannelSoundPath";
        }
        req.settings.flags |= 8;
        long soundDocumentId = preferences.getLong(soundDocumentIdPref, 0L);
        String soundPath = preferences.getString(soundPathPref, "NoSound");
        if (soundDocumentId != 0) {
            TLRPC.TL_notificationSoundRingtone ringtoneSound = new TLRPC.TL_notificationSoundRingtone();
            ringtoneSound.id = soundDocumentId;
            req.settings.sound = ringtoneSound;
        } else if (soundPath != null) {
            if (soundPath.equals("NoSound")) {
                req.settings.sound = new TLRPC.TL_notificationSoundNone();
            } else {
                TLRPC.TL_notificationSoundLocal localSound = new TLRPC.TL_notificationSoundLocal();
                localSound.title = preferences.getString(soundNamePref, null);
                localSound.data = soundPath;
                req.settings.sound = localSound;
            }
        } else {
            req.settings.sound = new TLRPC.TL_notificationSoundDefault();
        }
        getConnectionsManager().sendRequest(req, NotificationsController$$ExternalSyntheticLambda34.INSTANCE);
    }

    public static /* synthetic */ void lambda$updateServerNotificationsSettings$40(TLObject response, TLRPC.TL_error error) {
    }

    public boolean isGlobalNotificationsEnabled(long dialogId) {
        return isGlobalNotificationsEnabled(dialogId, null);
    }

    public boolean isGlobalNotificationsEnabled(long dialogId, Boolean forceChannel) {
        int type;
        if (DialogObject.isChatDialog(dialogId)) {
            if (forceChannel != null) {
                if (forceChannel.booleanValue()) {
                    type = 2;
                } else {
                    type = 0;
                }
            } else {
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId));
                if (ChatObject.isChannel(chat) && !chat.megagroup) {
                    type = 2;
                } else {
                    type = 0;
                }
            }
        } else {
            type = 1;
        }
        return isGlobalNotificationsEnabled(type);
    }

    public boolean isGlobalNotificationsEnabled(int type) {
        return getAccountInstance().getNotificationsSettings().getInt(getGlobalNotificationsKey(type), 0) < getConnectionsManager().getCurrentTime();
    }

    public void setGlobalNotificationsEnabled(int type, int time) {
        getAccountInstance().getNotificationsSettings().edit().putInt(getGlobalNotificationsKey(type), time).commit();
        updateServerNotificationsSettings(type);
        getMessagesStorage().updateMutedDialogsFiltersCounters();
        deleteNotificationChannelGlobal(type);
    }

    public static String getGlobalNotificationsKey(int type) {
        if (type == 0) {
            return "EnableGroup2";
        }
        if (type == 1) {
            return "EnableAll2";
        }
        return "EnableChannel2";
    }

    public void muteDialog(long dialog_id, boolean mute) {
        if (mute) {
            getInstance(this.currentAccount).muteUntil(dialog_id, Integer.MAX_VALUE);
            return;
        }
        boolean defaultEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(dialog_id);
        SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
        SharedPreferences.Editor editor = preferences.edit();
        if (defaultEnabled) {
            editor.remove("notify2_" + dialog_id);
        } else {
            editor.putInt("notify2_" + dialog_id, 0);
        }
        getMessagesStorage().setDialogFlags(dialog_id, 0L);
        editor.apply();
        TLRPC.Dialog dialog = getMessagesController().dialogs_dict.get(dialog_id);
        if (dialog != null) {
            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
        }
        updateServerNotificationsSettings(dialog_id);
    }
}
