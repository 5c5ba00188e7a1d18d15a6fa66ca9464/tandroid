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
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$PhoneCall;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputPhoneCall;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonDisconnect;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonHangup;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscarded;
import org.telegram.tgnet.TLRPC$TL_phone_discardCall;
import org.telegram.tgnet.TLRPC$TL_phone_receivedCall;
import org.telegram.tgnet.TLRPC$TL_updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.VoIPPermissionActivity;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class VoIPPreNotificationService {
    public static TLRPC$PhoneCall pendingCall;
    public static Intent pendingVoIP;
    private static MediaPlayer ringtonePlayer;
    private static final Object sync = new Object();
    private static Vibrator vibrator;

    /* JADX WARN: Removed duplicated region for block: B:27:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0245  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Notification makeNotification(Context context, int i, long j, long j2, boolean z) {
        NotificationChannel notificationChannel;
        NotificationChannel notificationChannel2;
        NotificationChannel notificationChannel3;
        boolean z2;
        String string;
        int i2;
        String string2;
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
        TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Notification.Builder contentIntent = new Notification.Builder(context).setContentTitle(LocaleController.getString(z ? R.string.VoipInVideoCallBranding : R.string.VoipInCallBranding)).setSmallIcon(R.drawable.ic_call).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, LaunchActivity.class).setAction("voip"), 301989888));
        SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
        int i3 = globalNotificationsSettings.getInt("calls_notification_channel", 0);
        notificationChannel = notificationManager.getNotificationChannel("incoming_calls2" + i3);
        if (notificationChannel != null) {
            id2 = notificationChannel.getId();
            notificationManager.deleteNotificationChannel(id2);
        }
        notificationChannel2 = notificationManager.getNotificationChannel("incoming_calls3" + i3);
        if (notificationChannel2 != null) {
            id = notificationChannel2.getId();
            notificationManager.deleteNotificationChannel(id);
        }
        notificationChannel3 = notificationManager.getNotificationChannel("incoming_calls4" + i3);
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
                        NotificationChannel notificationChannel4 = new NotificationChannel("incoming_calls4" + i3, LocaleController.getString(R.string.IncomingCallsSystemSetting), 4);
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
                    contentIntent.setChannelId("incoming_calls4" + i3);
                    Intent intent = new Intent(context, VoIPActionsReceiver.class);
                    intent.setAction(context.getPackageName() + ".DECLINE_CALL");
                    intent.putExtra("call_id", j2);
                    string = LocaleController.getString(R.string.VoipDeclineCall);
                    i2 = Build.VERSION.SDK_INT;
                    if (i2 >= 24 && i2 < 31) {
                        SpannableString spannableString = new SpannableString(string);
                        spannableString.setSpan(new ForegroundColorSpan(-769226), 0, spannableString.length(), 0);
                    }
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 301989888);
                    Intent intent2 = new Intent(context, VoIPActionsReceiver.class);
                    intent2.setAction(context.getPackageName() + ".ANSWER_CALL");
                    intent2.putExtra("call_id", j2);
                    string2 = LocaleController.getString(R.string.VoipAnswerCall);
                    if (i2 >= 24 && i2 < 31) {
                        SpannableString spannableString2 = new SpannableString(string2);
                        spannableString2.setSpan(new ForegroundColorSpan(-16733696), 0, spannableString2.length(), 0);
                    }
                    PendingIntent activity = PendingIntent.getActivity(context, 0, new Intent(context, LaunchActivity.class).setAction("voip_answer"), 301989888);
                    contentIntent.setPriority(2);
                    contentIntent.setShowWhen(false);
                    if (i2 >= 21) {
                        contentIntent.setColor(-13851168);
                        contentIntent.setVibrate(new long[0]);
                        contentIntent.setCategory("call");
                        Intent intent3 = pendingVoIP;
                        if (intent3 != null) {
                            contentIntent.setFullScreenIntent(PendingIntent.getActivity(context, 0, intent3, ConnectionsManager.FileTypeVideo), true);
                        }
                        if (user != null && !TextUtils.isEmpty(user.phone)) {
                            contentIntent.addPerson("tel:" + user.phone);
                        }
                    }
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
            notificationManager.deleteNotificationChannel("incoming_calls4" + i3);
            i3++;
            globalNotificationsSettings.edit().putInt("calls_notification_channel", i3).commit();
        }
        z2 = true;
        if (z2) {
        }
        contentIntent.setChannelId("incoming_calls4" + i3);
        Intent intent4 = new Intent(context, VoIPActionsReceiver.class);
        intent4.setAction(context.getPackageName() + ".DECLINE_CALL");
        intent4.putExtra("call_id", j2);
        string = LocaleController.getString(R.string.VoipDeclineCall);
        i2 = Build.VERSION.SDK_INT;
        if (i2 >= 24) {
            SpannableString spannableString3 = new SpannableString(string);
            spannableString3.setSpan(new ForegroundColorSpan(-769226), 0, spannableString3.length(), 0);
        }
        PendingIntent broadcast2 = PendingIntent.getBroadcast(context, 0, intent4, 301989888);
        Intent intent22 = new Intent(context, VoIPActionsReceiver.class);
        intent22.setAction(context.getPackageName() + ".ANSWER_CALL");
        intent22.putExtra("call_id", j2);
        string2 = LocaleController.getString(R.string.VoipAnswerCall);
        if (i2 >= 24) {
            SpannableString spannableString22 = new SpannableString(string2);
            spannableString22.setSpan(new ForegroundColorSpan(-16733696), 0, spannableString22.length(), 0);
        }
        PendingIntent activity2 = PendingIntent.getActivity(context, 0, new Intent(context, LaunchActivity.class).setAction("voip_answer"), 301989888);
        contentIntent.setPriority(2);
        contentIntent.setShowWhen(false);
        if (i2 >= 21) {
        }
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

    /* JADX WARN: Removed duplicated region for block: B:46:0x00f2 A[Catch: all -> 0x002d, TryCatch #1 {all -> 0x002d, Exception -> 0x007f, blocks: (B:13:0x0027, B:15:0x002b, B:19:0x0030, B:21:0x0046, B:24:0x0052, B:26:0x0069, B:31:0x0089, B:39:0x00a7, B:44:0x00db, B:46:0x00f2, B:51:0x0114, B:53:0x011a, B:58:0x0128, B:64:0x013f, B:65:0x014e, B:56:0x0122, B:47:0x0108, B:32:0x008e, B:34:0x0092, B:36:0x009c, B:38:0x00a2, B:29:0x0081, B:41:0x00cf, B:43:0x00d6, B:22:0x004c), top: B:72:0x0027 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0108 A[Catch: all -> 0x002d, TryCatch #1 {all -> 0x002d, Exception -> 0x007f, blocks: (B:13:0x0027, B:15:0x002b, B:19:0x0030, B:21:0x0046, B:24:0x0052, B:26:0x0069, B:31:0x0089, B:39:0x00a7, B:44:0x00db, B:46:0x00f2, B:51:0x0114, B:53:0x011a, B:58:0x0128, B:64:0x013f, B:65:0x014e, B:56:0x0122, B:47:0x0108, B:32:0x008e, B:34:0x0092, B:36:0x009c, B:38:0x00a2, B:29:0x0081, B:41:0x00cf, B:43:0x00d6, B:22:0x004c), top: B:72:0x0027 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0138  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void startRinging(Context context, int i, long j) {
        int i2;
        String string;
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
                    string = notificationsSettings.getString("ringtone_path_" + j, null);
                } else {
                    string = notificationsSettings.getString("CallsRingtonePath", null);
                }
                if (string == null) {
                    parse = RingtoneManager.getDefaultUri(1);
                } else {
                    Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
                    if (uri != null && string.equalsIgnoreCase(uri.getPath())) {
                        parse = RingtoneManager.getDefaultUri(1);
                    } else {
                        parse = Uri.parse(string);
                        z = false;
                        FileLog.d("start ringtone with " + z + " " + parse);
                        ringtonePlayer.setDataSource(context, parse);
                        ringtonePlayer.prepareAsync();
                        if (!notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                            i2 = notificationsSettings.getInt("calls_vibrate_" + j, 0);
                        } else {
                            i2 = notificationsSettings.getInt("vibrate_calls", 0);
                        }
                        if ((i2 != 2 && i2 != 4 && (audioManager.getRingerMode() == 1 || audioManager.getRingerMode() == 2)) || (i2 == 4 && audioManager.getRingerMode() == 1)) {
                            Vibrator vibrator2 = (Vibrator) context.getSystemService("vibrator");
                            vibrator = vibrator2;
                            vibrator2.vibrate(new long[]{0, i2 == 1 ? 350L : i2 == 3 ? 1400L : 700L, 500}, 0);
                        }
                    }
                }
                z = true;
                FileLog.d("start ringtone with " + z + " " + parse);
                ringtonePlayer.setDataSource(context, parse);
                ringtonePlayer.prepareAsync();
                if (!notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                }
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
    public static /* synthetic */ void lambda$startRinging$0(MediaPlayer mediaPlayer) {
        try {
            ringtonePlayer.start();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private static void stopRinging() {
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

    public static void show(final Context context, final Intent intent, final TLRPC$PhoneCall tLRPC$PhoneCall) {
        FileLog.d("VoIPPreNotification.show()");
        if (tLRPC$PhoneCall == null || intent == null) {
            dismiss(context);
            FileLog.d("VoIPPreNotification.show(): call or intent is null");
            return;
        }
        TLRPC$PhoneCall tLRPC$PhoneCall2 = pendingCall;
        if (tLRPC$PhoneCall2 == null || tLRPC$PhoneCall2.id != tLRPC$PhoneCall.id) {
            dismiss(context);
            pendingVoIP = intent;
            pendingCall = tLRPC$PhoneCall;
            final int intExtra = intent.getIntExtra("account", UserConfig.selectedAccount);
            final long longExtra = intent.getLongExtra("user_id", 0L);
            final boolean z = tLRPC$PhoneCall.video;
            acknowledge(context, intExtra, tLRPC$PhoneCall, new Runnable() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPPreNotificationService.lambda$show$1(intent, tLRPC$PhoneCall, context, intExtra, longExtra, z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$show$1(Intent intent, TLRPC$PhoneCall tLRPC$PhoneCall, Context context, int i, long j, boolean z) {
        pendingVoIP = intent;
        pendingCall = tLRPC$PhoneCall;
        ((NotificationManager) context.getSystemService("notification")).notify(203, makeNotification(context, i, j, tLRPC$PhoneCall.id, z));
        startRinging(context, i, j);
    }

    private static void acknowledge(final Context context, int i, TLRPC$PhoneCall tLRPC$PhoneCall, final Runnable runnable) {
        if (tLRPC$PhoneCall instanceof TLRPC$TL_phoneCallDiscarded) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("Call " + tLRPC$PhoneCall.id + " was discarded before the voip pre notification started, stopping");
            }
            pendingVoIP = null;
            pendingCall = null;
        } else if (XiaomiUtilities.isMIUI() && !XiaomiUtilities.isCustomPermissionGranted(XiaomiUtilities.OP_SHOW_WHEN_LOCKED) && ((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("MIUI: no permission to show when locked but the screen is locked. ¯\\_(ツ)_/¯");
            }
            pendingVoIP = null;
            pendingCall = null;
        } else {
            TLRPC$TL_phone_receivedCall tLRPC$TL_phone_receivedCall = new TLRPC$TL_phone_receivedCall();
            TLRPC$TL_inputPhoneCall tLRPC$TL_inputPhoneCall = new TLRPC$TL_inputPhoneCall();
            tLRPC$TL_phone_receivedCall.peer = tLRPC$TL_inputPhoneCall;
            tLRPC$TL_inputPhoneCall.id = tLRPC$PhoneCall.id;
            tLRPC$TL_inputPhoneCall.access_hash = tLRPC$PhoneCall.access_hash;
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_phone_receivedCall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    VoIPPreNotificationService.lambda$acknowledge$3(context, runnable, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$acknowledge$3(final Context context, final Runnable runnable, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                VoIPPreNotificationService.lambda$acknowledge$2(TLObject.this, tLRPC$TL_error, context, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$acknowledge$2(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, Context context, Runnable runnable) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.w("(VoIPPreNotification) receivedCall response = " + tLObject);
        }
        if (tLRPC$TL_error == null) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("error on receivedCall: " + tLRPC$TL_error);
        }
        pendingVoIP = null;
        pendingCall = null;
        dismiss(context);
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

    public static boolean isVideo() {
        Intent intent = pendingVoIP;
        return intent != null && intent.getBooleanExtra(MediaStreamTrack.VIDEO_TRACK_KIND, false);
    }

    public static void answer(Context context) {
        FileLog.d("VoIPPreNotification.answer()");
        Intent intent = pendingVoIP;
        if (intent == null) {
            FileLog.d("VoIPPreNotification.answer(): pending intent is not found");
            return;
        }
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
        TLRPC$TL_phone_discardCall tLRPC$TL_phone_discardCall = new TLRPC$TL_phone_discardCall();
        TLRPC$TL_inputPhoneCall tLRPC$TL_inputPhoneCall = new TLRPC$TL_inputPhoneCall();
        tLRPC$TL_phone_discardCall.peer = tLRPC$TL_inputPhoneCall;
        TLRPC$PhoneCall tLRPC$PhoneCall = pendingCall;
        tLRPC$TL_inputPhoneCall.access_hash = tLRPC$PhoneCall.access_hash;
        tLRPC$TL_inputPhoneCall.id = tLRPC$PhoneCall.id;
        tLRPC$TL_phone_discardCall.duration = 0;
        tLRPC$TL_phone_discardCall.connection_id = 0L;
        if (i == 2) {
            tLRPC$TL_phone_discardCall.reason = new TLRPC$TL_phoneCallDiscardReasonDisconnect();
        } else if (i == 3) {
            tLRPC$TL_phone_discardCall.reason = new TLRPC$TL_phoneCallDiscardReasonMissed();
        } else if (i == 4) {
            tLRPC$TL_phone_discardCall.reason = new TLRPC$TL_phoneCallDiscardReasonBusy();
        } else {
            tLRPC$TL_phone_discardCall.reason = new TLRPC$TL_phoneCallDiscardReasonHangup();
        }
        FileLog.e("discardCall " + tLRPC$TL_phone_discardCall.reason);
        ConnectionsManager.getInstance(intExtra).sendRequest(tLRPC$TL_phone_discardCall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPPreNotificationService$$ExternalSyntheticLambda13
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                VoIPPreNotificationService.lambda$decline$4(intExtra, tLObject, tLRPC$TL_error);
            }
        }, 2);
        dismiss(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$decline$4(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("(VoIPPreNotification) error on phone.discardCall: " + tLRPC$TL_error);
                return;
            }
            return;
        }
        if (tLObject instanceof TLRPC$TL_updates) {
            MessagesController.getInstance(i).processUpdates((TLRPC$TL_updates) tLObject, false);
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("(VoIPPreNotification) phone.discardCall " + tLObject);
        }
    }

    public static void dismiss(Context context) {
        FileLog.d("VoIPPreNotification.dismiss()");
        pendingVoIP = null;
        pendingCall = null;
        ((NotificationManager) context.getSystemService("notification")).cancel(203);
        stopRinging();
    }
}
