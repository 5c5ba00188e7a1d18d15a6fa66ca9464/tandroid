package org.telegram.messenger.voip;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.messenger.voip.VoIPServiceState;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.VoIPFragment;
import org.telegram.ui.VoIPPermissionActivity;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class VoIPPreNotificationService {
    public static State currentState;
    public static TLRPC.PhoneCall pendingCall;
    public static Intent pendingVoIP;
    private static MediaPlayer ringtonePlayer;
    private static final Object sync = new Object();
    private static Vibrator vibrator;

    /* loaded from: classes3.dex */
    public static final class State implements VoIPServiceState {
        private final TLRPC.PhoneCall call;
        private final int currentAccount;
        private boolean destroyed;
        private final long userId;

        public State(int i, long j, TLRPC.PhoneCall phoneCall) {
            this.currentAccount = i;
            this.userId = j;
            this.call = phoneCall;
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public void acceptIncomingCall() {
            VoIPPreNotificationService.answer(ApplicationLoader.applicationContext);
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public void declineIncomingCall() {
            VoIPPreNotificationService.decline(ApplicationLoader.applicationContext, 1);
        }

        public void destroy() {
            if (this.destroyed) {
                return;
            }
            this.destroyed = true;
            if (VoIPFragment.getInstance() != null) {
                VoIPFragment.getInstance().onStateChanged(getCallState());
            }
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public /* synthetic */ long getCallDuration() {
            return VoIPServiceState.-CC.$default$getCallDuration(this);
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public int getCallState() {
            return this.destroyed ? 11 : 15;
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public TLRPC.PhoneCall getPrivateCall() {
            return this.call;
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public TLRPC.User getUser() {
            return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.userId));
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public boolean isOutgoing() {
            return false;
        }

        @Override // org.telegram.messenger.voip.VoIPServiceState
        public void stopRinging() {
            VoIPPreNotificationService.stopRinging();
        }
    }

    private static void acknowledge(final Context context, int i, TLRPC.PhoneCall phoneCall, final Runnable runnable) {
        if (phoneCall instanceof TLRPC.TL_phoneCallDiscarded) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("Call " + phoneCall.id + " was discarded before the voip pre notification started, stopping");
            }
            pendingVoIP = null;
            pendingCall = null;
            State state = currentState;
            if (state != null) {
                state.destroy();
            }
        } else if (!XiaomiUtilities.isMIUI() || XiaomiUtilities.isCustomPermissionGranted(XiaomiUtilities.OP_SHOW_WHEN_LOCKED) || !((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            TLRPC.TL_phone_receivedCall tL_phone_receivedCall = new TLRPC.TL_phone_receivedCall();
            TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
            tL_phone_receivedCall.peer = tL_inputPhoneCall;
            tL_inputPhoneCall.id = phoneCall.id;
            tL_inputPhoneCall.access_hash = phoneCall.access_hash;
            ConnectionsManager.getInstance(i).sendRequest(tL_phone_receivedCall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    VoIPPreNotificationService.lambda$acknowledge$3(context, runnable, tLObject, tL_error);
                }
            }, 2);
        } else {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("MIUI: no permission to show when locked but the screen is locked. ¯\\_(ツ)_/¯");
            }
            pendingVoIP = null;
            pendingCall = null;
            State state2 = currentState;
            if (state2 != null) {
                state2.destroy();
            }
        }
    }

    public static void answer(Context context) {
        FileLog.d("VoIPPreNotification.answer()");
        Intent intent = pendingVoIP;
        if (intent == null) {
            FileLog.d("VoIPPreNotification.answer(): pending intent is not found");
            return;
        }
        currentState = null;
        intent.getIntExtra("account", UserConfig.selectedAccount);
        if (VoIPService.getSharedInstance() != null) {
            VoIPService.getSharedInstance().acceptIncomingCall();
        } else {
            pendingVoIP.putExtra("openFragment", true);
            if (!PermissionRequest.hasPermission("android.permission.RECORD_AUDIO") || (isVideo() && !PermissionRequest.hasPermission("android.permission.CAMERA"))) {
                try {
                    PendingIntent.getActivity(context, 0, new Intent(context, VoIPPermissionActivity.class).addFlags(268435456), 1107296256).send();
                    return;
                } catch (Exception e) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("Error starting permission activity", e);
                        return;
                    }
                    return;
                }
            }
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(pendingVoIP);
            } else {
                context.startService(pendingVoIP);
            }
            pendingVoIP = null;
        }
        dismiss(context);
    }

    public static void decline(Context context, int i) {
        FileLog.d("VoIPPreNotification.decline(" + i + ")");
        Intent intent = pendingVoIP;
        if (intent == null || pendingCall == null) {
            FileLog.d("VoIPPreNotification.decline(" + i + "): pending intent or call is not found");
            return;
        }
        final int intExtra = intent.getIntExtra("account", UserConfig.selectedAccount);
        TLRPC.TL_phone_discardCall tL_phone_discardCall = new TLRPC.TL_phone_discardCall();
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        tL_phone_discardCall.peer = tL_inputPhoneCall;
        TLRPC.PhoneCall phoneCall = pendingCall;
        tL_inputPhoneCall.access_hash = phoneCall.access_hash;
        tL_inputPhoneCall.id = phoneCall.id;
        tL_phone_discardCall.duration = 0;
        tL_phone_discardCall.connection_id = 0L;
        tL_phone_discardCall.reason = i != 2 ? i != 3 ? i != 4 ? new TLRPC.TL_phoneCallDiscardReasonHangup() : new TLRPC.TL_phoneCallDiscardReasonBusy() : new TLRPC.TL_phoneCallDiscardReasonMissed() : new TLRPC.TL_phoneCallDiscardReasonDisconnect();
        FileLog.e("discardCall " + tL_phone_discardCall.reason);
        ConnectionsManager.getInstance(intExtra).sendRequest(tL_phone_discardCall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda13
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPPreNotificationService.lambda$decline$4(intExtra, tLObject, tL_error);
            }
        }, 2);
        dismiss(context);
    }

    public static void dismiss(Context context) {
        FileLog.d("VoIPPreNotification.dismiss()");
        pendingVoIP = null;
        pendingCall = null;
        State state = currentState;
        if (state != null) {
            state.destroy();
        }
        ((NotificationManager) context.getSystemService("notification")).cancel(203);
        stopRinging();
    }

    public static State getState() {
        return currentState;
    }

    public static boolean isVideo() {
        Intent intent = pendingVoIP;
        return intent != null && intent.getBooleanExtra(MediaStreamTrack.VIDEO_TRACK_KIND, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$acknowledge$2(TLObject tLObject, TLRPC.TL_error tL_error, Context context, Runnable runnable) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.w("(VoIPPreNotification) receivedCall response = " + tLObject);
        }
        if (tL_error == null) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("error on receivedCall: " + tL_error);
        }
        pendingVoIP = null;
        pendingCall = null;
        State state = currentState;
        if (state != null) {
            state.destroy();
        }
        dismiss(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$acknowledge$3(final Context context, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                VoIPPreNotificationService.lambda$acknowledge$2(TLObject.this, tL_error, context, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$decline$4(int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("(VoIPPreNotification) error on phone.discardCall: " + tL_error);
                return;
            }
            return;
        }
        if (tLObject instanceof TLRPC.TL_updates) {
            MessagesController.getInstance(i).processUpdates((TLRPC.TL_updates) tLObject, false);
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("(VoIPPreNotification) phone.discardCall " + tLObject);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$show$1(Intent intent, TLRPC.PhoneCall phoneCall, Context context, int i, long j, boolean z) {
        pendingVoIP = intent;
        pendingCall = phoneCall;
        ((NotificationManager) context.getSystemService("notification")).notify(203, makeNotification(context, i, j, phoneCall.id, z));
        startRinging(context, i, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startRinging$0(MediaPlayer mediaPlayer) {
        try {
            ringtonePlayer.start();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x027d  */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Notification makeNotification(Context context, int i, long j, long j2, boolean z) {
        NotificationChannel notificationChannel;
        NotificationChannel notificationChannel2;
        NotificationChannel notificationChannel3;
        boolean z2;
        int i2;
        int i3;
        int i4;
        String formatName;
        Person.Builder name;
        Icon createWithAdaptiveBitmap;
        Person.Builder icon;
        Person build;
        Notification.CallStyle forIncomingCall;
        AudioAttributes.Builder contentType;
        AudioAttributes.Builder legacyStreamType;
        AudioAttributes.Builder usage;
        AudioAttributes build2;
        int importance;
        Uri sound;
        String id;
        String id2;
        if (Build.VERSION.SDK_INT < 33) {
            return null;
        }
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Intent action = new Intent(context, LaunchActivity.class).setAction("voip");
        Notification.Builder contentIntent = new Notification.Builder(context).setContentTitle(LocaleController.getString(z ? R.string.VoipInVideoCallBranding : R.string.VoipInCallBranding)).setSmallIcon(R.drawable.ic_call).setContentIntent(PendingIntent.getActivity(context, 0, action, 301989888));
        SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
        int i5 = globalNotificationsSettings.getInt("calls_notification_channel", 0);
        notificationChannel = notificationManager.getNotificationChannel("incoming_calls2" + i5);
        if (notificationChannel != null) {
            id2 = notificationChannel.getId();
            notificationManager.deleteNotificationChannel(id2);
        }
        notificationChannel2 = notificationManager.getNotificationChannel("incoming_calls3" + i5);
        if (notificationChannel2 != null) {
            id = notificationChannel2.getId();
            notificationManager.deleteNotificationChannel(id);
        }
        notificationChannel3 = notificationManager.getNotificationChannel("incoming_calls4" + i5);
        if (notificationChannel3 != null) {
            importance = notificationChannel3.getImportance();
            if (importance >= 4) {
                sound = notificationChannel3.getSound();
                if (sound == null) {
                    z2 = false;
                    if (z2) {
                        contentType = new AudioAttributes.Builder().setContentType(4);
                        legacyStreamType = contentType.setLegacyStreamType(2);
                        usage = legacyStreamType.setUsage(2);
                        build2 = usage.build();
                        NotificationChannel notificationChannel4 = new NotificationChannel("incoming_calls4" + i5, LocaleController.getString(R.string.IncomingCallsSystemSetting), 4);
                        try {
                            notificationChannel4.setSound(null, build2);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        notificationChannel4.setDescription(LocaleController.getString(R.string.IncomingCallsSystemSettingDescription));
                        notificationChannel4.enableVibration(false);
                        notificationChannel4.enableLights(false);
                        notificationChannel4.setBypassDnd(true);
                        try {
                            notificationManager.createNotificationChannel(notificationChannel4);
                        } catch (Exception e2) {
                            FileLog.e(e2);
                            return null;
                        }
                    }
                    contentIntent.setChannelId("incoming_calls4" + i5);
                    Intent intent = new Intent(context, VoIPActionsReceiver.class);
                    intent.setAction(context.getPackageName() + ".DECLINE_CALL");
                    intent.putExtra("call_id", j2);
                    String string = LocaleController.getString(R.string.VoipDeclineCall);
                    i2 = Build.VERSION.SDK_INT;
                    if (i2 >= 24 || i2 >= 31) {
                        i3 = 0;
                    } else {
                        SpannableString spannableString = new SpannableString(string);
                        i3 = 0;
                        spannableString.setSpan(new ForegroundColorSpan(-769226), 0, spannableString.length(), 0);
                    }
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, i3, intent, 301989888);
                    Intent intent2 = new Intent(context, VoIPActionsReceiver.class);
                    intent2.setAction(context.getPackageName() + ".ANSWER_CALL");
                    intent2.putExtra("call_id", j2);
                    String string2 = LocaleController.getString(R.string.VoipAnswerCall);
                    if (i2 >= 24 || i2 >= 31) {
                        i4 = 0;
                    } else {
                        SpannableString spannableString2 = new SpannableString(string2);
                        i4 = 0;
                        spannableString2.setSpan(new ForegroundColorSpan(-16733696), 0, spannableString2.length(), 0);
                    }
                    PendingIntent activity = PendingIntent.getActivity(context, i4, new Intent(context, LaunchActivity.class).setAction("voip_answer"), 301989888);
                    contentIntent.setPriority(2);
                    contentIntent.setShowWhen(i4);
                    if (i2 >= 21) {
                        contentIntent.setColor(-13851168);
                        contentIntent.setVibrate(new long[i4]);
                        contentIntent.setCategory("call");
                        contentIntent.setFullScreenIntent(PendingIntent.getActivity(context, i4, action, ConnectionsManager.FileTypeVideo), true);
                        if (user != null && !TextUtils.isEmpty(user.phone)) {
                            contentIntent.addPerson("tel:" + user.phone);
                        }
                    }
                    Intent intent3 = new Intent(ApplicationLoader.applicationContext, VoIPActionsReceiver.class);
                    intent3.setAction(context.getPackageName() + ".HIDE_CALL");
                    contentIntent.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, intent3, 167772160));
                    Bitmap roundAvatarBitmap = VoIPService.getRoundAvatarBitmap(context, i, user);
                    formatName = ContactsController.formatName(user);
                    if (TextUtils.isEmpty(formatName)) {
                        formatName = "___";
                    }
                    name = new Person.Builder().setName(formatName);
                    createWithAdaptiveBitmap = Icon.createWithAdaptiveBitmap(roundAvatarBitmap);
                    icon = name.setIcon(createWithAdaptiveBitmap);
                    build = icon.build();
                    forIncomingCall = Notification.CallStyle.forIncomingCall(build, broadcast, activity);
                    contentIntent.setStyle(forIncomingCall);
                    return contentIntent.build();
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("User messed up the notification channel; deleting it and creating a proper one");
            }
            notificationManager.deleteNotificationChannel("incoming_calls4" + i5);
            i5++;
            globalNotificationsSettings.edit().putInt("calls_notification_channel", i5).commit();
        }
        z2 = true;
        if (z2) {
        }
        contentIntent.setChannelId("incoming_calls4" + i5);
        Intent intent4 = new Intent(context, VoIPActionsReceiver.class);
        intent4.setAction(context.getPackageName() + ".DECLINE_CALL");
        intent4.putExtra("call_id", j2);
        String string3 = LocaleController.getString(R.string.VoipDeclineCall);
        i2 = Build.VERSION.SDK_INT;
        if (i2 >= 24) {
        }
        i3 = 0;
        PendingIntent broadcast2 = PendingIntent.getBroadcast(context, i3, intent4, 301989888);
        Intent intent22 = new Intent(context, VoIPActionsReceiver.class);
        intent22.setAction(context.getPackageName() + ".ANSWER_CALL");
        intent22.putExtra("call_id", j2);
        String string22 = LocaleController.getString(R.string.VoipAnswerCall);
        if (i2 >= 24) {
        }
        i4 = 0;
        PendingIntent activity2 = PendingIntent.getActivity(context, i4, new Intent(context, LaunchActivity.class).setAction("voip_answer"), 301989888);
        contentIntent.setPriority(2);
        contentIntent.setShowWhen(i4);
        if (i2 >= 21) {
        }
        Intent intent32 = new Intent(ApplicationLoader.applicationContext, VoIPActionsReceiver.class);
        intent32.setAction(context.getPackageName() + ".HIDE_CALL");
        contentIntent.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, intent32, 167772160));
        Bitmap roundAvatarBitmap2 = VoIPService.getRoundAvatarBitmap(context, i, user);
        formatName = ContactsController.formatName(user);
        if (TextUtils.isEmpty(formatName)) {
        }
        name = new Person.Builder().setName(formatName);
        createWithAdaptiveBitmap = Icon.createWithAdaptiveBitmap(roundAvatarBitmap2);
        icon = name.setIcon(createWithAdaptiveBitmap);
        build = icon.build();
        forIncomingCall = Notification.CallStyle.forIncomingCall(build, broadcast2, activity2);
        contentIntent.setStyle(forIncomingCall);
        return contentIntent.build();
    }

    public static boolean open(Context context) {
        if (VoIPService.getSharedInstance() != null) {
            return true;
        }
        Intent intent = pendingVoIP;
        if (intent == null || pendingCall == null) {
            return false;
        }
        intent.getIntExtra("account", UserConfig.selectedAccount);
        pendingVoIP.putExtra("openFragment", true);
        pendingVoIP.putExtra("accept", false);
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(pendingVoIP);
        } else {
            context.startService(pendingVoIP);
        }
        pendingVoIP = null;
        dismiss(context);
        return true;
    }

    public static void show(final Context context, final Intent intent, final TLRPC.PhoneCall phoneCall) {
        FileLog.d("VoIPPreNotification.show()");
        if (phoneCall == null || intent == null) {
            dismiss(context);
            FileLog.d("VoIPPreNotification.show(): call or intent is null");
            return;
        }
        TLRPC.PhoneCall phoneCall2 = pendingCall;
        if (phoneCall2 == null || phoneCall2.id != phoneCall.id) {
            dismiss(context);
            pendingVoIP = intent;
            pendingCall = phoneCall;
            final int intExtra = intent.getIntExtra("account", UserConfig.selectedAccount);
            final long longExtra = intent.getLongExtra("user_id", 0L);
            final boolean z = phoneCall.video;
            currentState = new State(intExtra, longExtra, phoneCall);
            acknowledge(context, intExtra, phoneCall, new Runnable() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPPreNotificationService.lambda$show$1(intent, phoneCall, context, intExtra, longExtra, z);
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00eb A[Catch: all -> 0x002d, TryCatch #1 {all -> 0x002d, Exception -> 0x007b, blocks: (B:13:0x0027, B:15:0x002b, B:19:0x0030, B:21:0x0046, B:24:0x0052, B:26:0x0069, B:30:0x007f, B:32:0x0085, B:40:0x00a0, B:45:0x00d4, B:47:0x00eb, B:49:0x0100, B:52:0x0109, B:54:0x010f, B:59:0x011d, B:65:0x0134, B:66:0x0143, B:57:0x0117, B:33:0x008a, B:35:0x008e, B:39:0x009b, B:42:0x00c8, B:44:0x00cf, B:22:0x004c), top: B:73:0x0027 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x012d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void startRinging(Context context, int i, long j) {
        String str;
        int i2;
        String str2;
        Uri uri;
        Uri parse;
        boolean z;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(i);
        AudioManager audioManager = (AudioManager) context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        boolean z2 = audioManager.getRingerMode() != 0;
        boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
        if (z2 && ringtonePlayer == null) {
            synchronized (sync) {
                try {
                } catch (Exception e) {
                    FileLog.e(e);
                    MediaPlayer mediaPlayer = ringtonePlayer;
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        ringtonePlayer = null;
                    }
                } finally {
                }
                if (ringtonePlayer != null) {
                    return;
                }
                MediaPlayer mediaPlayer2 = new MediaPlayer();
                ringtonePlayer = mediaPlayer2;
                mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda10
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public final void onPrepared(MediaPlayer mediaPlayer3) {
                        VoIPPreNotificationService.lambda$startRinging$0(mediaPlayer3);
                    }
                });
                ringtonePlayer.setLooping(true);
                if (isWiredHeadsetOn) {
                    ringtonePlayer.setAudioStreamType(0);
                } else {
                    ringtonePlayer.setAudioStreamType(2);
                }
                if (notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                    str2 = "ringtone_path_" + j;
                } else {
                    str2 = "CallsRingtonePath";
                }
                String string = notificationsSettings.getString(str2, null);
                if (string != null && ((uri = Settings.System.DEFAULT_RINGTONE_URI) == null || !string.equalsIgnoreCase(uri.getPath()))) {
                    parse = Uri.parse(string);
                    z = false;
                    FileLog.d("start ringtone with " + z + " " + parse);
                    ringtonePlayer.setDataSource(context, parse);
                    ringtonePlayer.prepareAsync();
                    if (notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                        str = "vibrate_calls";
                    } else {
                        str = "calls_vibrate_" + j;
                    }
                    i2 = notificationsSettings.getInt(str, 0);
                    if ((i2 != 2 && i2 != 4 && (audioManager.getRingerMode() == 1 || audioManager.getRingerMode() == 2)) || (i2 == 4 && audioManager.getRingerMode() == 1)) {
                        Vibrator vibrator2 = (Vibrator) context.getSystemService("vibrator");
                        vibrator = vibrator2;
                        vibrator2.vibrate(new long[]{0, i2 == 1 ? 350L : i2 == 3 ? 1400L : 700L, 500}, 0);
                    }
                }
                parse = RingtoneManager.getDefaultUri(1);
                z = true;
                FileLog.d("start ringtone with " + z + " " + parse);
                ringtonePlayer.setDataSource(context, parse);
                ringtonePlayer.prepareAsync();
                if (notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                }
                i2 = notificationsSettings.getInt(str, 0);
                if (i2 != 2) {
                    Vibrator vibrator22 = (Vibrator) context.getSystemService("vibrator");
                    vibrator = vibrator22;
                    vibrator22.vibrate(new long[]{0, i2 == 1 ? 350L : i2 == 3 ? 1400L : 700L, 500}, 0);
                }
                Vibrator vibrator222 = (Vibrator) context.getSystemService("vibrator");
                vibrator = vibrator222;
                vibrator222.vibrate(new long[]{0, i2 == 1 ? 350L : i2 == 3 ? 1400L : 700L, 500}, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stopRinging() {
        synchronized (sync) {
            try {
                MediaPlayer mediaPlayer = ringtonePlayer;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    ringtonePlayer.release();
                    ringtonePlayer = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Vibrator vibrator2 = vibrator;
        if (vibrator2 != null) {
            vibrator2.cancel();
            vibrator = null;
        }
    }
}
