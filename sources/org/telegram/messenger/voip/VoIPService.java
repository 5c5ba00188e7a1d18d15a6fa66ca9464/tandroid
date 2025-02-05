package org.telegram.messenger.voip;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRouter;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RemoteViews;
import android.widget.Toast;
import j$.util.Map;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.messenger.voip.Instance;
import org.telegram.messenger.voip.NativeInstance;
import org.telegram.messenger.voip.VoIPController;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.RequestDelegateTimestamp;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_phone;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.JoinCallAlert;
import org.telegram.ui.Components.PermissionRequest;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.VoIPFeedbackActivity;
import org.telegram.ui.VoIPFragment;
import org.telegram.ui.VoIPPermissionActivity;
import org.webrtc.MediaStreamTrack;
import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;
import org.webrtc.voiceengine.WebRtcAudioTrack;

/* loaded from: classes3.dex */
public class VoIPService extends Service implements SensorEventListener, AudioManager.OnAudioFocusChangeListener, VoIPController.ConnectionStateListener, NotificationCenter.NotificationCenterDelegate, VoIPServiceState {
    public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    public static final int AUDIO_ROUTE_BLUETOOTH = 2;
    public static final int AUDIO_ROUTE_EARPIECE = 0;
    public static final int AUDIO_ROUTE_SPEAKER = 1;
    public static final int CALL_MIN_LAYER = 65;
    public static final int CAPTURE_DEVICE_CAMERA = 0;
    public static final int CAPTURE_DEVICE_SCREEN = 1;
    public static final int DISCARD_REASON_DISCONNECT = 2;
    public static final int DISCARD_REASON_HANGUP = 1;
    public static final int DISCARD_REASON_LINE_BUSY = 4;
    public static final int DISCARD_REASON_MISSED = 3;
    private static final int ID_INCOMING_CALL_NOTIFICATION = 202;
    public static final int ID_INCOMING_CALL_PRENOTIFICATION = 203;
    private static final int ID_ONGOING_CALL_NOTIFICATION = 201;
    private static final int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
    public static final int QUALITY_FULL = 2;
    public static final int QUALITY_MEDIUM = 1;
    public static final int QUALITY_SMALL = 0;
    public static final int STATE_BUSY = 17;
    public static final int STATE_CREATING = 6;
    public static final int STATE_ENDED = 11;
    public static final int STATE_ESTABLISHED = 3;
    public static final int STATE_EXCHANGING_KEYS = 12;
    public static final int STATE_FAILED = 4;
    public static final int STATE_HANGING_UP = 10;
    public static final int STATE_RECONNECTING = 5;
    public static final int STATE_REQUESTING = 14;
    public static final int STATE_RINGING = 16;
    public static final int STATE_WAITING = 13;
    public static final int STATE_WAITING_INCOMING = 15;
    public static final int STATE_WAIT_INIT = 1;
    public static final int STATE_WAIT_INIT_ACK = 2;
    public static NativeInstance.AudioLevelsCallback audioLevelsCallback;
    public static TL_phone.PhoneCall callIShouldHavePutIntoIntent;
    private static Runnable setModeRunnable;
    private static VoIPService sharedInstance;
    private byte[] a_or_b;
    private boolean audioConfigured;
    private AudioDeviceCallback audioDeviceCallback;
    private byte[] authKey;
    private boolean bluetoothScoActive;
    private boolean bluetoothScoConnecting;
    private BluetoothAdapter btAdapter;
    private int callDiscardReason;
    private int callReqId;
    private long callStartTime;
    private TLRPC.Chat chat;
    private int checkRequestId;
    private int classGuid;
    private Runnable connectingSoundRunnable;
    private PowerManager.WakeLock cpuWakelock;
    private boolean createGroupCall;
    public String currentBluetoothDeviceName;
    public boolean currentGroupModeStreaming;
    private Runnable delayedStartOutgoingCall;
    private boolean didDeleteConnectionServiceContact;
    private boolean endCallAfterRequest;
    boolean fetchingBluetoothDeviceName;
    private boolean forceRating;
    private int foregroundId;
    private Notification foregroundNotification;
    private boolean foregroundStarted;
    private byte[] g_a;
    private byte[] g_a_hash;
    private boolean gotMediaProjection;
    public ChatObject.Call groupCall;
    private volatile CountDownLatch groupCallBottomSheetLatch;
    private TLRPC.InputPeer groupCallPeer;
    private boolean hasAudioFocus;
    public boolean hasFewPeers;
    private boolean instantAccept;
    private boolean isBtHeadsetConnected;
    private volatile boolean isCallEnded;
    private boolean isHeadsetPlugged;
    private boolean isOutgoing;
    private boolean isPrivateScreencast;
    private boolean isProximityNear;
    public boolean isRtmpStream;
    private boolean isVideoAvailable;
    private String joinHash;
    private long keyFingerprint;
    private String lastError;
    private int lastForegroundType;
    private NetworkInfo lastNetInfo;
    private SensorEvent lastSensorEvent;
    private long lastTypingTimeSend;
    private Boolean mHasEarpiece;
    private boolean micMute;
    public boolean micSwitching;
    private TLRPC.TL_dataJSON myParams;
    private boolean needPlayEndSound;
    private boolean needRateCall;
    private boolean needSendDebugLog;
    private boolean needSwitchToBluetoothAfterScoActivates;
    private boolean notificationsDisabled;
    private Runnable onDestroyRunnable;
    private boolean playedConnectedSound;
    private boolean playingSound;
    private Instance.TrafficStats prevTrafficStats;
    public TL_phone.PhoneCall privateCall;
    private PowerManager.WakeLock proximityWakelock;
    private boolean reconnectScreenCapture;
    private MediaPlayer ringtonePlayer;
    private int scheduleDate;
    private Runnable shortPollRunnable;
    private int signalBarCount;
    private SoundPool soundPool;
    private int spAllowTalkId;
    private int spBusyId;
    private int spConnectingId;
    private int spEndId;
    private int spFailedID;
    private int spPlayId;
    private int spRingbackID;
    private int spStartRecordId;
    private int spVoiceChatConnecting;
    private int spVoiceChatEndId;
    private int spVoiceChatStartId;
    private boolean speakerphoneStateToSet;
    private boolean startedRinging;
    private boolean switchingAccount;
    private boolean switchingCamera;
    private boolean switchingStream;
    private Runnable switchingStreamTimeoutRunnable;
    private CallConnection systemCallConnection;
    private Runnable timeoutRunnable;
    private boolean unmutedByHold;
    private Runnable updateNotificationRunnable;
    private TLRPC.User user;
    private Vibrator vibrator;
    public boolean videoCall;
    private boolean wasConnected;
    private boolean wasEstablished;
    public static final boolean USE_CONNECTION_SERVICE = isDeviceCompatibleWithConnectionServiceAPI();
    private static final Object sync = new Object();
    private int currentAccount = -1;
    private int currentState = 0;
    private boolean isFrontFaceCamera = true;
    private int previousAudioOutput = -1;
    private ArrayList<StateListener> stateListeners = new ArrayList<>();
    private int remoteVideoState = 0;
    private int[] mySource = new int[2];
    private NativeInstance[] tgVoip = new NativeInstance[2];
    private long[] captureDevice = new long[2];
    private boolean[] destroyCaptureDevice = {true, true};
    private int[] videoState = {0, 0};
    private int remoteAudioState = 1;
    private int audioRouteToSet = 2;
    public final SharedUIParams sharedUIParams = new SharedUIParams();
    private ArrayList<TL_phone.PhoneCall> pendingUpdates = new ArrayList<>();
    private HashMap<String, Integer> currentStreamRequestTimestamp = new HashMap<>();
    private Runnable afterSoundRunnable = new 1();
    private BluetoothProfile.ServiceListener serviceListener = new BluetoothProfile.ServiceListener() { // from class: org.telegram.messenger.voip.VoIPService.2
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            try {
                if (Build.VERSION.SDK_INT < 31) {
                    Iterator<BluetoothDevice> it = bluetoothProfile.getConnectedDevices().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        BluetoothDevice next = it.next();
                        if (bluetoothProfile.getConnectionState(next) == 2) {
                            VoIPService.this.currentBluetoothDeviceName = next.getName();
                            break;
                        }
                    }
                }
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(i, bluetoothProfile);
                VoIPService.this.fetchingBluetoothDeviceName = false;
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() { // from class: org.telegram.messenger.voip.VoIPService.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (VoIPService.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
                VoIPService.this.isHeadsetPlugged = intent.getIntExtra("state", 0) == 1;
                if (VoIPService.this.isHeadsetPlugged && VoIPService.this.proximityWakelock != null && VoIPService.this.proximityWakelock.isHeld()) {
                    VoIPService.this.proximityWakelock.release();
                }
                if (VoIPService.this.isHeadsetPlugged) {
                    AudioManager audioManager = (AudioManager) VoIPService.this.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
                    if (VoipAudioManager.get().isSpeakerphoneOn()) {
                        VoIPService.this.previousAudioOutput = 0;
                    } else if (audioManager.isBluetoothScoOn()) {
                        VoIPService.this.previousAudioOutput = 2;
                    } else {
                        VoIPService.this.previousAudioOutput = 1;
                    }
                    VoIPService.this.setAudioOutput(1);
                } else if (VoIPService.this.previousAudioOutput >= 0) {
                    VoIPService voIPService = VoIPService.this;
                    voIPService.setAudioOutput(voIPService.previousAudioOutput);
                    VoIPService.this.previousAudioOutput = -1;
                }
                VoIPService.this.isProximityNear = false;
                VoIPService.this.updateOutputGainControlState();
                return;
            }
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                VoIPService.this.updateNetworkType();
                return;
            }
            if ("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED".equals(intent.getAction())) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("bt headset state = " + intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0));
                }
                VoIPService.this.updateBluetoothHeadsetState(intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0) == 2);
                return;
            }
            if (!"android.media.ACTION_SCO_AUDIO_STATE_UPDATED".equals(intent.getAction())) {
                if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
                    if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(intent.getStringExtra("state"))) {
                        VoIPService.this.hangUp();
                        return;
                    }
                    return;
                } else if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                    for (int i = 0; i < VoIPService.this.stateListeners.size(); i++) {
                        ((StateListener) VoIPService.this.stateListeners.get(i)).onScreenOnChange(true);
                    }
                    return;
                } else {
                    if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                        for (int i2 = 0; i2 < VoIPService.this.stateListeners.size(); i2++) {
                            ((StateListener) VoIPService.this.stateListeners.get(i2)).onScreenOnChange(false);
                        }
                        return;
                    }
                    return;
                }
            }
            int intExtra = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", 0);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Bluetooth SCO state updated: " + intExtra);
            }
            if (intExtra == 0 && VoIPService.this.isBtHeadsetConnected && (!VoIPService.this.btAdapter.isEnabled() || !PermissionRequest.hasPermission("android.permission.BLUETOOTH_CONNECT") || VoIPService.this.btAdapter.getProfileConnectionState(1) != 2)) {
                VoIPService.this.updateBluetoothHeadsetState(false);
                return;
            }
            VoIPService.this.bluetoothScoConnecting = intExtra == 2;
            VoIPService.this.bluetoothScoActive = intExtra == 1;
            if (VoIPService.this.bluetoothScoActive) {
                VoIPService.this.fetchBluetoothDeviceName();
                if (VoIPService.this.needSwitchToBluetoothAfterScoActivates) {
                    VoIPService.this.needSwitchToBluetoothAfterScoActivates = false;
                    AudioManager audioManager2 = (AudioManager) VoIPService.this.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
                    VoipAudioManager.get().setSpeakerphoneOn(false);
                    audioManager2.setBluetoothScoOn(true);
                }
            }
            Iterator it = VoIPService.this.stateListeners.iterator();
            while (it.hasNext()) {
                ((StateListener) it.next()).onAudioSettingsChanged();
            }
        }
    };
    private final HashMap<String, TLRPC.TL_groupCallParticipant> waitingFrameParticipant = new HashMap<>();
    private final LruCache<String, ProxyVideoSink> proxyVideoSinkLruCache = new LruCache<String, ProxyVideoSink>(6) { // from class: org.telegram.messenger.voip.VoIPService.4
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.util.LruCache
        public void entryRemoved(boolean z, String str, ProxyVideoSink proxyVideoSink, ProxyVideoSink proxyVideoSink2) {
            super.entryRemoved(z, (boolean) str, proxyVideoSink, proxyVideoSink2);
            VoIPService.this.tgVoip[0].removeIncomingVideoOutput(proxyVideoSink.nativeInstance);
        }
    };
    private ProxyVideoSink[] localSink = new ProxyVideoSink[2];
    private ProxyVideoSink[] remoteSink = new ProxyVideoSink[2];
    private ProxyVideoSink[] currentBackgroundSink = new ProxyVideoSink[2];
    private String[] currentBackgroundEndpointId = new String[2];
    private HashMap<String, ProxyVideoSink> remoteSinks = new HashMap<>();

    class 1 implements Runnable {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            VoIPService.this.soundPool.release();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$run$1(AudioManager audioManager) {
            synchronized (VoIPService.sync) {
                try {
                    if (VoIPService.setModeRunnable == null) {
                        return;
                    }
                    Runnable unused = VoIPService.setModeRunnable = null;
                    try {
                        audioManager.setMode(0);
                    } catch (SecurityException e) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("Error setting audio more to normal", e);
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            final AudioManager audioManager = (AudioManager) VoIPService.this.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            VoipAudioManager voipAudioManager = VoipAudioManager.get();
            audioManager.abandonAudioFocus(VoIPService.this);
            audioManager.unregisterMediaButtonEventReceiver(new ComponentName(VoIPService.this, (Class<?>) VoIPMediaButtonReceiver.class));
            if (VoIPService.this.audioDeviceCallback != null) {
                audioManager.unregisterAudioDeviceCallback(VoIPService.this.audioDeviceCallback);
            }
            if (!VoIPService.USE_CONNECTION_SERVICE && VoIPService.sharedInstance == null) {
                if (VoIPService.this.isBtHeadsetConnected) {
                    audioManager.stopBluetoothSco();
                    audioManager.setBluetoothScoOn(false);
                    VoIPService.this.bluetoothScoActive = false;
                    VoIPService.this.bluetoothScoConnecting = false;
                }
                voipAudioManager.setSpeakerphoneOn(false);
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.1.this.lambda$run$0();
                }
            });
            Utilities.globalQueue.postRunnable(VoIPService.setModeRunnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.1.lambda$run$1(audioManager);
                }
            });
        }
    }

    class 5 implements VideoSink {
        final /* synthetic */ String val$endpointId;
        final /* synthetic */ boolean val$screencast;

        5(String str, boolean z) {
            this.val$endpointId = str;
            this.val$screencast = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFrame$0(String str, VideoSink videoSink, boolean z) {
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) VoIPService.this.waitingFrameParticipant.remove(str);
            ProxyVideoSink proxyVideoSink = (ProxyVideoSink) VoIPService.this.remoteSinks.get(str);
            if (proxyVideoSink != null && proxyVideoSink.target == videoSink) {
                VoIPService.this.proxyVideoSinkLruCache.put(str, proxyVideoSink);
                VoIPService.this.remoteSinks.remove(str);
                proxyVideoSink.setTarget(null);
            }
            if (tL_groupCallParticipant != null) {
                if (z) {
                    tL_groupCallParticipant.hasPresentationFrame = 2;
                } else {
                    tL_groupCallParticipant.hasCameraFrame = 2;
                }
            }
            ChatObject.Call call = VoIPService.this.groupCall;
            if (call != null) {
                call.updateVisibleParticipants();
            }
        }

        @Override // org.webrtc.VideoSink
        public void onFrame(VideoFrame videoFrame) {
            if (videoFrame == null || videoFrame.getBuffer().getHeight() == 0 || videoFrame.getBuffer().getWidth() == 0) {
                return;
            }
            final String str = this.val$endpointId;
            final boolean z = this.val$screencast;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.5.this.lambda$onFrame$0(str, this, z);
                }
            });
        }

        @Override // org.webrtc.VideoSink
        public /* synthetic */ void setParentSink(VideoSink videoSink) {
            VideoSink.-CC.$default$setParentSink(this, videoSink);
        }
    }

    class 7 implements Runnable {
        7() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            if (VoIPService.this.spPlayId == 0) {
                VoIPService voIPService = VoIPService.this;
                voIPService.spPlayId = voIPService.soundPool.play(VoIPService.this.spConnectingId, 1.0f, 1.0f, 0, -1, 1.0f);
            }
            if (VoIPService.this.spPlayId == 0) {
                AndroidUtilities.runOnUIThread(this, 100L);
            } else {
                VoIPService.this.connectingSoundRunnable = null;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (VoIPService.sharedInstance == null) {
                return;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.7.this.lambda$run$0();
                }
            });
        }
    }

    public class CallConnection extends Connection {
        public CallConnection() {
            setConnectionProperties(128);
            setAudioModeIsVoip(true);
        }

        @Override // android.telecom.Connection
        public void onAnswer() {
            VoIPService.this.acceptIncomingCallFromNotification();
        }

        @Override // android.telecom.Connection
        public void onCallAudioStateChanged(CallAudioState callAudioState) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("ConnectionService call audio state changed: " + callAudioState);
            }
            Iterator it = VoIPService.this.stateListeners.iterator();
            while (it.hasNext()) {
                ((StateListener) it.next()).onAudioSettingsChanged();
            }
        }

        @Override // android.telecom.Connection
        public void onCallEvent(String str, Bundle bundle) {
            super.onCallEvent(str, bundle);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("ConnectionService onCallEvent " + str);
            }
        }

        @Override // android.telecom.Connection
        public void onDisconnect() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("ConnectionService onDisconnect");
            }
            setDisconnected(new DisconnectCause(2));
            destroy();
            VoIPService.this.systemCallConnection = null;
            VoIPService.this.hangUp();
        }

        @Override // android.telecom.Connection
        public void onReject() {
            VoIPService.this.needPlayEndSound = false;
            VoIPService.this.declineIncomingCall(1, null);
        }

        @Override // android.telecom.Connection
        public void onShowIncomingCallUi() {
            VoIPService.this.startRinging();
        }

        @Override // android.telecom.Connection
        public void onSilence() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("onSlience");
            }
            VoIPService.this.stopRinging();
        }

        @Override // android.telecom.Connection
        public void onStateChanged(int i) {
            String stateToString;
            super.onStateChanged(i);
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder sb = new StringBuilder();
                sb.append("ConnectionService onStateChanged ");
                stateToString = Connection.stateToString(i);
                sb.append(stateToString);
                FileLog.d(sb.toString());
            }
            if (i == 4) {
                ContactsController.getInstance(VoIPService.this.currentAccount).deleteConnectionServiceContact();
                VoIPService.this.didDeleteConnectionServiceContact = true;
            }
        }
    }

    public static class ProxyVideoSink implements VideoSink {
        private VideoSink background;
        private long nativeInstance;
        private VideoSink target;

        @Override // org.webrtc.VideoSink
        public synchronized void onFrame(VideoFrame videoFrame) {
            try {
                VideoSink videoSink = this.target;
                if (videoSink != null) {
                    videoSink.onFrame(videoFrame);
                }
                VideoSink videoSink2 = this.background;
                if (videoSink2 != null) {
                    videoSink2.onFrame(videoFrame);
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        public synchronized void removeBackground(VideoSink videoSink) {
            if (this.background == videoSink) {
                this.background = null;
            }
        }

        public synchronized void removeTarget(VideoSink videoSink) {
            if (this.target == videoSink) {
                this.target = null;
            }
        }

        public synchronized void setBackground(VideoSink videoSink) {
            try {
                VideoSink videoSink2 = this.background;
                if (videoSink2 != null) {
                    videoSink2.setParentSink(null);
                }
                this.background = videoSink;
                if (videoSink != null) {
                    videoSink.setParentSink(this);
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        @Override // org.webrtc.VideoSink
        public /* synthetic */ void setParentSink(VideoSink videoSink) {
            VideoSink.-CC.$default$setParentSink(this, videoSink);
        }

        public synchronized void setTarget(VideoSink videoSink) {
            try {
                VideoSink videoSink2 = this.target;
                if (videoSink2 != videoSink) {
                    if (videoSink2 != null) {
                        videoSink2.setParentSink(null);
                    }
                    this.target = videoSink;
                    if (videoSink != null) {
                        videoSink.setParentSink(this);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        public synchronized void swap() {
            VideoSink videoSink;
            if (this.target != null && (videoSink = this.background) != null) {
                this.target = videoSink;
                this.background = null;
            }
        }
    }

    private static class RequestedParticipant {
        public int audioSsrc;
        public TLRPC.TL_groupCallParticipant participant;

        public RequestedParticipant(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, int i) {
            this.participant = tL_groupCallParticipant;
            this.audioSsrc = i;
        }
    }

    public static class SharedUIParams {
        public boolean cameraAlertWasShowed;
        public boolean tapToVideoTooltipWasShowed;
        public boolean wasVideoCall;
    }

    public interface StateListener {

        public abstract /* synthetic */ class -CC {
            public static void $default$onAudioSettingsChanged(StateListener stateListener) {
            }

            public static void $default$onCameraFirstFrameAvailable(StateListener stateListener) {
            }

            public static void $default$onCameraSwitch(StateListener stateListener, boolean z) {
            }

            public static void $default$onMediaStateUpdated(StateListener stateListener, int i, int i2) {
            }

            public static void $default$onScreenOnChange(StateListener stateListener, boolean z) {
            }

            public static void $default$onSignalBarsCountChanged(StateListener stateListener, int i) {
            }

            public static void $default$onStateChanged(StateListener stateListener, int i) {
            }

            public static void $default$onVideoAvailableChange(StateListener stateListener, boolean z) {
            }
        }

        void onAudioSettingsChanged();

        void onCameraFirstFrameAvailable();

        void onCameraSwitch(boolean z);

        void onMediaStateUpdated(int i, int i2);

        void onScreenOnChange(boolean z);

        void onSignalBarsCountChanged(int i);

        void onStateChanged(int i);

        void onVideoAvailableChange(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
    
        if (r0 != 0) goto L27;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void acceptIncomingCallFromNotification() {
        int checkSelfPermission;
        int checkSelfPermission2;
        showNotification();
        if (Build.VERSION.SDK_INT >= 23) {
            checkSelfPermission = checkSelfPermission("android.permission.RECORD_AUDIO");
            if (checkSelfPermission == 0) {
                if (this.privateCall.video) {
                    checkSelfPermission2 = checkSelfPermission("android.permission.CAMERA");
                }
            }
            try {
                PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) VoIPPermissionActivity.class).addFlags(268435456), 1107296256).send();
                return;
            } catch (Exception e) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Error starting permission activity", e);
                    return;
                }
                return;
            }
        }
        acceptIncomingCall();
        try {
            PendingIntent.getActivity(this, 0, new Intent(this, getUIActivityClass()).setAction("voip"), ConnectionsManager.FileTypeVideo).send();
        } catch (Exception e2) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error starting incall activity", e2);
            }
        }
    }

    private void acknowledgeCall(final boolean z) {
        TL_phone.PhoneCall phoneCall = this.privateCall;
        if (phoneCall == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("Call is null, wtf");
            }
            stopSelf();
            return;
        }
        if (phoneCall instanceof TL_phone.TL_phoneCallDiscarded) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("Call " + this.privateCall.id + " was discarded before the service started, stopping");
            }
            stopSelf();
            return;
        }
        if (XiaomiUtilities.isMIUI() && !XiaomiUtilities.isCustomPermissionGranted(XiaomiUtilities.OP_SHOW_WHEN_LOCKED) && ((KeyguardManager) getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("MIUI: no permission to show when locked but the screen is locked. ¯\\_(ツ)_/¯");
            }
            stopSelf();
            return;
        }
        TL_phone.receivedCall receivedcall = new TL_phone.receivedCall();
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        receivedcall.peer = tL_inputPhoneCall;
        TL_phone.PhoneCall phoneCall2 = this.privateCall;
        tL_inputPhoneCall.id = phoneCall2.id;
        tL_inputPhoneCall.access_hash = phoneCall2.access_hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(receivedcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda95
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$acknowledgeCall$12(z, tLObject, tL_error);
            }
        }, 2);
    }

    private PhoneAccountHandle addAccountToTelecomManager() {
        PhoneAccount.Builder capabilities;
        Icon createWithResource;
        PhoneAccount.Builder icon;
        PhoneAccount.Builder highlightColor;
        PhoneAccount.Builder addSupportedUriScheme;
        PhoneAccount build;
        TelecomManager m = VoIPService$$ExternalSyntheticApiModelOutline5.m(getSystemService("telecom"));
        TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
        PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(new ComponentName(this, (Class<?>) TelegramConnectionService.class), "" + currentUser.id);
        capabilities = new PhoneAccount.Builder(phoneAccountHandle, ContactsController.formatName(currentUser.first_name, currentUser.last_name)).setCapabilities(2048);
        createWithResource = Icon.createWithResource(this, R.drawable.ic_launcher_dr);
        icon = capabilities.setIcon(createWithResource);
        highlightColor = icon.setHighlightColor(-13851168);
        addSupportedUriScheme = highlightColor.addSupportedUriScheme("sip");
        build = addSupportedUriScheme.build();
        m.registerPhoneAccount(build);
        return phoneAccountHandle;
    }

    private void broadcastUnknownParticipants(long j, int[] iArr) {
        if (this.groupCall == null || this.tgVoip[0] == null) {
            return;
        }
        long selfId = getSelfId();
        int length = iArr.length;
        ArrayList arrayList = null;
        for (int i = 0; i < length; i++) {
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = this.groupCall.participantsBySources.get(iArr[i]);
            if (tL_groupCallParticipant == null && (tL_groupCallParticipant = this.groupCall.participantsByVideoSources.get(iArr[i])) == null) {
                tL_groupCallParticipant = this.groupCall.participantsByPresentationSources.get(iArr[i]);
            }
            if (tL_groupCallParticipant != null && MessageObject.getPeerId(tL_groupCallParticipant.peer) != selfId && tL_groupCallParticipant.source != 0) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(new RequestedParticipant(tL_groupCallParticipant, iArr[i]));
            }
        }
        if (arrayList != null) {
            int[] iArr2 = new int[arrayList.size()];
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                iArr2[i2] = ((RequestedParticipant) arrayList.get(i2)).audioSsrc;
            }
            this.tgVoip[0].onMediaDescriptionAvailable(j, iArr2);
            int size2 = arrayList.size();
            for (int i3 = 0; i3 < size2; i3++) {
                RequestedParticipant requestedParticipant = (RequestedParticipant) arrayList.get(i3);
                TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = requestedParticipant.participant;
                if (tL_groupCallParticipant2.muted_by_you) {
                    this.tgVoip[0].setVolume(requestedParticipant.audioSsrc, 0.0d);
                } else {
                    NativeInstance nativeInstance = this.tgVoip[0];
                    int i4 = requestedParticipant.audioSsrc;
                    double participantVolume = ChatObject.getParticipantVolume(tL_groupCallParticipant2);
                    Double.isNaN(participantVolume);
                    nativeInstance.setVolume(i4, participantVolume / 10000.0d);
                }
            }
        }
    }

    private void callEnded() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Call " + getCallID() + " ended");
        }
        this.isCallEnded = true;
        if (this.groupCall != null && (!this.playedConnectedSound || this.onDestroyRunnable != null)) {
            this.needPlayEndSound = false;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda125
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$callEnded$93();
            }
        });
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda126
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$callEnded$94();
            }
        });
        Runnable runnable = this.connectingSoundRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.connectingSoundRunnable = null;
        }
        int i = 700;
        if (this.needPlayEndSound) {
            this.playingSound = true;
            if (this.groupCall == null) {
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda127
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$callEnded$95();
                    }
                });
            } else {
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda128
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$callEnded$96();
                    }
                }, 100L);
                i = 500;
            }
            AndroidUtilities.runOnUIThread(this.afterSoundRunnable, i);
        }
        Runnable runnable2 = this.timeoutRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.timeoutRunnable = null;
        }
        endConnectionServiceCall(this.needPlayEndSound ? i : 0L);
        stopSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callFailed() {
        NativeInstance nativeInstance = this.tgVoip[0];
        callFailed(nativeInstance != null ? nativeInstance.getLastError() : Instance.ERROR_UNKNOWN);
    }

    private void callFailed(String str) {
        CallConnection callConnection;
        if (this.privateCall != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Discarding failed call");
            }
            TL_phone.discardCall discardcall = new TL_phone.discardCall();
            TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
            discardcall.peer = tL_inputPhoneCall;
            TL_phone.PhoneCall phoneCall = this.privateCall;
            tL_inputPhoneCall.access_hash = phoneCall.access_hash;
            tL_inputPhoneCall.id = phoneCall.id;
            discardcall.duration = (int) (getCallDuration() / 1000);
            NativeInstance nativeInstance = this.tgVoip[0];
            discardcall.connection_id = nativeInstance != null ? nativeInstance.getPreferredRelayId() : 0L;
            discardcall.reason = new TLRPC.TL_phoneCallDiscardReasonDisconnect();
            FileLog.e("discardCall " + discardcall.reason);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(discardcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda120
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    VoIPService.lambda$callFailed$84(tLObject, tL_error);
                }
            });
        }
        try {
            throw new Exception("Call " + getCallID() + " failed with error: " + str);
        } catch (Exception e) {
            FileLog.e(e);
            this.lastError = str;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda121
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$callFailed$85();
                }
            });
            if (TextUtils.equals(str, Instance.ERROR_LOCALIZED) && this.soundPool != null) {
                this.playingSound = true;
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda122
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$callFailed$86();
                    }
                });
                AndroidUtilities.runOnUIThread(this.afterSoundRunnable, 1000L);
            }
            if (USE_CONNECTION_SERVICE && (callConnection = this.systemCallConnection) != null) {
                callConnection.setDisconnected(new DisconnectCause(1));
                this.systemCallConnection.destroy();
                this.systemCallConnection = null;
            }
            stopSelf();
        }
    }

    private void cancelGroupCheckShortPoll() {
        int[] iArr = this.mySource;
        if (iArr[1] == 0 && iArr[0] == 0) {
            if (this.checkRequestId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkRequestId, false);
                this.checkRequestId = 0;
            }
            Runnable runnable = this.shortPollRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.shortPollRunnable = null;
            }
        }
    }

    private void checkIsNear() {
        if (this.remoteVideoState == 2 || this.videoState[0] == 2) {
            checkIsNear(false);
        }
    }

    private void checkIsNear(boolean z) {
        if (z != this.isProximityNear) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("proximity " + z);
            }
            this.isProximityNear = z;
            try {
                if (z) {
                    this.proximityWakelock.acquire();
                } else {
                    this.proximityWakelock.release(1);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkUpdateBluetoothHeadset() {
        BluetoothAdapter bluetoothAdapter;
        int deviceType;
        if (USE_CONNECTION_SERVICE || (bluetoothAdapter = this.btAdapter) == null || !bluetoothAdapter.isEnabled()) {
            return;
        }
        try {
            MediaRouter mediaRouter = (MediaRouter) getSystemService("media_router");
            AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            if (Build.VERSION.SDK_INT < 24) {
                updateBluetoothHeadsetState(this.btAdapter.getProfileConnectionState(1) == 2);
                Iterator<StateListener> it = this.stateListeners.iterator();
                while (it.hasNext()) {
                    it.next().onAudioSettingsChanged();
                }
                return;
            }
            MediaRouter.RouteInfo selectedRoute = mediaRouter.getSelectedRoute(1);
            if (PermissionRequest.hasPermission("android.permission.BLUETOOTH_CONNECT")) {
                deviceType = selectedRoute.getDeviceType();
                if (deviceType == 3) {
                    updateBluetoothHeadsetState(this.btAdapter.getProfileConnectionState(1) == 2);
                    Iterator<StateListener> it2 = this.stateListeners.iterator();
                    while (it2.hasNext()) {
                        it2.next().onAudioSettingsChanged();
                    }
                    return;
                }
            }
            updateBluetoothHeadsetState(audioManager.isBluetoothA2dpOn());
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private void configureDeviceForCall() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("configureDeviceForCall, route to set = " + this.audioRouteToSet);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            WebRtcAudioTrack.setAudioTrackUsageAttribute(hasRtmpStream() ? 1 : 2);
            WebRtcAudioTrack.setAudioStreamType(hasRtmpStream() ? Integer.MIN_VALUE : 0);
        }
        this.needPlayEndSound = true;
        final AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        if (!USE_CONNECTION_SERVICE) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda89
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$configureDeviceForCall$82(audioManager);
                }
            });
        }
        SensorManager sensorManager = (SensorManager) getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(8);
        if (defaultSensor != null) {
            try {
                this.proximityWakelock = ((PowerManager) getSystemService("power")).newWakeLock(32, "telegram-voip-prx");
                sensorManager.registerListener(this, defaultSensor, 3);
            } catch (Exception e) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Error initializing proximity sensor", e);
                }
            }
        }
    }

    private int convertDataSavingMode(int i) {
        return i != 3 ? i : ApplicationLoader.isRoaming() ? 1 : 0;
    }

    public static String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                bufferedReader.close();
                return sb.toString();
            }
            sb.append(readLine);
            sb.append("\n");
        }
    }

    private void createGroupInstance(final int i, boolean z) {
        boolean z2;
        String logFilePath;
        if (z) {
            this.mySource[i] = 0;
            if (i == 0) {
                this.switchingAccount = z;
            }
        }
        cancelGroupCheckShortPoll();
        if (i == 0) {
            this.wasConnected = false;
        } else if (!this.wasConnected) {
            this.reconnectScreenCapture = true;
            return;
        }
        if (this.tgVoip[i] == null) {
            if (BuildVars.DEBUG_VERSION) {
                logFilePath = VoIPHelper.getLogFilePath("voip_" + i + "_" + this.groupCall.call.id);
            } else {
                logFilePath = VoIPHelper.getLogFilePath(this.groupCall.call.id, false);
            }
            this.tgVoip[i] = NativeInstance.makeGroup(logFilePath, this.captureDevice[i], i == 1, i == 0 && SharedConfig.noiseSupression, new NativeInstance.PayloadCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda73
                @Override // org.telegram.messenger.voip.NativeInstance.PayloadCallback
                public final void run(int i2, String str) {
                    VoIPService.this.lambda$createGroupInstance$36(i, i2, str);
                }
            }, new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda74
                @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                    VoIPService.this.lambda$createGroupInstance$38(i, iArr, fArr, zArr);
                }
            }, new NativeInstance.VideoSourcesCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda75
                @Override // org.telegram.messenger.voip.NativeInstance.VideoSourcesCallback
                public final void run(long j, int[] iArr) {
                    VoIPService.this.lambda$createGroupInstance$40(i, j, iArr);
                }
            }, new NativeInstance.RequestBroadcastPartCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda76
                @Override // org.telegram.messenger.voip.NativeInstance.RequestBroadcastPartCallback
                public final void run(long j, long j2, int i2, int i3) {
                    VoIPService.this.lambda$createGroupInstance$45(i, j, j2, i2, i3);
                }
            }, new NativeInstance.RequestBroadcastPartCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda77
                @Override // org.telegram.messenger.voip.NativeInstance.RequestBroadcastPartCallback
                public final void run(long j, long j2, int i2, int i3) {
                    VoIPService.this.lambda$createGroupInstance$47(i, j, j2, i2, i3);
                }
            }, new NativeInstance.RequestCurrentTimeCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda78
                @Override // org.telegram.messenger.voip.NativeInstance.RequestCurrentTimeCallback
                public final void run(long j) {
                    VoIPService.this.lambda$createGroupInstance$49(i, j);
                }
            });
            this.tgVoip[i].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda79
                @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                public final void onStateUpdated(int i2, boolean z3) {
                    VoIPService.this.lambda$createGroupInstance$50(i, i2, z3);
                }
            });
            z2 = true;
        } else {
            z2 = false;
        }
        this.tgVoip[i].resetGroupInstance(!z2, false);
        if (this.captureDevice[i] != 0) {
            this.destroyCaptureDevice[i] = false;
        }
        if (i == 0) {
            dispatchStateChanged(1);
        }
    }

    private NativeInstance.SsrcGroup[] createSsrcGroups(TLRPC.TL_groupCallParticipantVideo tL_groupCallParticipantVideo) {
        if (tL_groupCallParticipantVideo.source_groups.isEmpty()) {
            return null;
        }
        int size = tL_groupCallParticipantVideo.source_groups.size();
        NativeInstance.SsrcGroup[] ssrcGroupArr = new NativeInstance.SsrcGroup[size];
        for (int i = 0; i < size; i++) {
            ssrcGroupArr[i] = new NativeInstance.SsrcGroup();
            TLRPC.TL_groupCallParticipantVideoSourceGroup tL_groupCallParticipantVideoSourceGroup = tL_groupCallParticipantVideo.source_groups.get(i);
            NativeInstance.SsrcGroup ssrcGroup = ssrcGroupArr[i];
            ssrcGroup.semantics = tL_groupCallParticipantVideoSourceGroup.semantics;
            ssrcGroup.ssrcs = new int[tL_groupCallParticipantVideoSourceGroup.sources.size()];
            int i2 = 0;
            while (true) {
                int[] iArr = ssrcGroupArr[i].ssrcs;
                if (i2 < iArr.length) {
                    iArr[i2] = tL_groupCallParticipantVideoSourceGroup.sources.get(i2).intValue();
                    i2++;
                }
            }
        }
        return ssrcGroupArr;
    }

    private void dispatchStateChanged(int i) {
        CallConnection callConnection;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("== Call " + getCallID() + " state changed to " + i + " ==");
        }
        this.currentState = i;
        if (USE_CONNECTION_SERVICE && i == 3 && (callConnection = this.systemCallConnection) != null) {
            callConnection.setActive();
        }
        for (int i2 = 0; i2 < this.stateListeners.size(); i2++) {
            this.stateListeners.get(i2).onStateChanged(i);
        }
    }

    private void endConnectionServiceCall(long j) {
        if (USE_CONNECTION_SERVICE) {
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$endConnectionServiceCall$97();
                }
            };
            if (j > 0) {
                AndroidUtilities.runOnUIThread(runnable, j);
            } else {
                runnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchBluetoothDeviceName() {
        if (this.fetchingBluetoothDeviceName) {
            return;
        }
        try {
            this.currentBluetoothDeviceName = null;
            this.fetchingBluetoothDeviceName = true;
            BluetoothAdapter.getDefaultAdapter().getProfileProxy(this, this.serviceListener, 1);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
    }

    private int getCurrentForegroundType() {
        return getCurrentForegroundType(this, this.gotMediaProjection);
    }

    private static int getCurrentForegroundType(ContextWrapper contextWrapper, boolean z) {
        int checkSelfPermission;
        int checkSelfPermission2;
        if (Build.VERSION.SDK_INT < 29) {
            return NotificationCenter.channelSuggestedBotsUpdate;
        }
        checkSelfPermission = contextWrapper.checkSelfPermission("android.permission.CAMERA");
        int i = checkSelfPermission == 0 ? 64 : 0;
        checkSelfPermission2 = contextWrapper.checkSelfPermission("android.permission.RECORD_AUDIO");
        if (checkSelfPermission2 == 0) {
            i |= 128;
        }
        if (z) {
            i |= 32;
        }
        return i | 2;
    }

    private String[] getEmoji() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(this.authKey);
            byteArrayOutputStream.write(this.g_a);
        } catch (IOException unused) {
        }
        return EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()));
    }

    private int getNetworkType() {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
        this.lastNetInfo = activeNetworkInfo;
        if (activeNetworkInfo != null) {
            int type = activeNetworkInfo.getType();
            if (type == 0) {
                switch (activeNetworkInfo.getSubtype()) {
                    case 1:
                        return 1;
                    case 2:
                    case 7:
                        return 2;
                    case 3:
                    case 5:
                        return 3;
                    case 4:
                    case 11:
                    case 14:
                    default:
                        return 11;
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 15:
                        return 4;
                    case 13:
                        return 5;
                }
            }
            if (type == 1) {
                return 6;
            }
            if (type == 9) {
                return 7;
            }
        }
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bitmap getRoundAvatarBitmap(Context context, int i, TLObject tLObject) {
        Bitmap decodeFile;
        Bitmap bitmap;
        Bitmap bitmap2 = null;
        try {
        } catch (Throwable th) {
            FileLog.e(th);
        }
        if (!(tLObject instanceof TLRPC.User)) {
            TLRPC.Chat chat = (TLRPC.Chat) tLObject;
            TLRPC.ChatPhoto chatPhoto = chat.photo;
            if (chatPhoto != null && chatPhoto.photo_small != null) {
                BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(chat.photo.photo_small, null, "50_50");
                if (imageFromMemory != null) {
                    bitmap = imageFromMemory.getBitmap();
                    decodeFile = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    bitmap2 = decodeFile;
                } else {
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inMutable = true;
                        decodeFile = BitmapFactory.decodeFile(FileLoader.getInstance(i).getPathToAttach(chat.photo.photo_small, true).toString(), options);
                        bitmap2 = decodeFile;
                    } catch (Throwable th2) {
                        th = th2;
                        FileLog.e(th);
                        if (bitmap2 == null) {
                        }
                        Canvas canvas = new Canvas(bitmap2);
                        Path path = new Path();
                        path.addCircle(bitmap2.getWidth() / 2, bitmap2.getHeight() / 2, bitmap2.getWidth() / 2, Path.Direction.CW);
                        path.toggleInverseFillType();
                        Paint paint = new Paint(1);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawPath(path, paint);
                        return bitmap2;
                    }
                }
            }
            if (bitmap2 == null) {
            }
            Canvas canvas2 = new Canvas(bitmap2);
            Path path2 = new Path();
            path2.addCircle(bitmap2.getWidth() / 2, bitmap2.getHeight() / 2, bitmap2.getWidth() / 2, Path.Direction.CW);
            path2.toggleInverseFillType();
            Paint paint2 = new Paint(1);
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas2.drawPath(path2, paint2);
            return bitmap2;
        }
        TLRPC.User user = (TLRPC.User) tLObject;
        TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
        if (userProfilePhoto != null && userProfilePhoto.photo_small != null) {
            BitmapDrawable imageFromMemory2 = ImageLoader.getInstance().getImageFromMemory(user.photo.photo_small, null, "50_50");
            if (imageFromMemory2 != null) {
                bitmap = imageFromMemory2.getBitmap();
                decodeFile = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                bitmap2 = decodeFile;
            } else {
                try {
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inMutable = true;
                    decodeFile = BitmapFactory.decodeFile(FileLoader.getInstance(i).getPathToAttach(user.photo.photo_small, true).toString(), options2);
                    bitmap2 = decodeFile;
                } catch (Throwable th3) {
                    th = th3;
                    FileLog.e(th);
                    if (bitmap2 == null) {
                    }
                    Canvas canvas22 = new Canvas(bitmap2);
                    Path path22 = new Path();
                    path22.addCircle(bitmap2.getWidth() / 2, bitmap2.getHeight() / 2, bitmap2.getWidth() / 2, Path.Direction.CW);
                    path22.toggleInverseFillType();
                    Paint paint22 = new Paint(1);
                    paint22.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas22.drawPath(path22, paint22);
                    return bitmap2;
                }
            }
        }
        if (bitmap2 == null) {
            Theme.createDialogsResources(context);
            AvatarDrawable avatarDrawable = tLObject instanceof TLRPC.User ? new AvatarDrawable((TLRPC.User) tLObject) : new AvatarDrawable((TLRPC.Chat) tLObject);
            bitmap2 = Bitmap.createBitmap(AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f), Bitmap.Config.ARGB_8888);
            avatarDrawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
            avatarDrawable.draw(new Canvas(bitmap2));
        }
        Canvas canvas222 = new Canvas(bitmap2);
        Path path222 = new Path();
        path222.addCircle(bitmap2.getWidth() / 2, bitmap2.getHeight() / 2, bitmap2.getWidth() / 2, Path.Direction.CW);
        path222.toggleInverseFillType();
        Paint paint222 = new Paint(1);
        paint222.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas222.drawPath(path222, paint222);
        return bitmap2;
    }

    public static VoIPService getSharedInstance() {
        return sharedInstance;
    }

    public static VoIPServiceState getSharedState() {
        VoIPService voIPService = sharedInstance;
        if (voIPService != null) {
            return voIPService;
        }
        if (Build.VERSION.SDK_INT >= 33) {
            return VoIPPreNotificationService.getState();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStatsNetworkType() {
        NetworkInfo networkInfo = this.lastNetInfo;
        if (networkInfo == null || networkInfo.getType() != 0) {
            return 1;
        }
        return this.lastNetInfo.isRoaming() ? 2 : 0;
    }

    public static String getStringFromFile(String str) {
        FileInputStream fileInputStream = new FileInputStream(new File(str));
        String convertStreamToString = convertStreamToString(fileInputStream);
        fileInputStream.close();
        return convertStreamToString;
    }

    private Class<? extends Activity> getUIActivityClass() {
        return LaunchActivity.class;
    }

    public static boolean hasRtmpStream() {
        return (getSharedInstance() == null || getSharedInstance().groupCall == null || !getSharedInstance().groupCall.call.rtmp_stream) ? false : true;
    }

    private void initializeAccountRelatedThings() {
        updateServerConfig();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:128:0x02f5, code lost:
    
        if (r2 == 0) goto L113;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0251 A[Catch: Exception -> 0x01e9, TRY_LEAVE, TryCatch #3 {Exception -> 0x01e9, blocks: (B:75:0x0188, B:77:0x01dc, B:79:0x01ee, B:90:0x0200, B:92:0x020f, B:93:0x0218, B:95:0x021e, B:99:0x0233, B:102:0x0251), top: B:74:0x0188 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0269 A[Catch: Exception -> 0x002b, TryCatch #2 {Exception -> 0x002b, blocks: (B:5:0x0010, B:7:0x0014, B:8:0x002e, B:10:0x003a, B:11:0x0045, B:12:0x006c, B:14:0x0075, B:15:0x007f, B:17:0x0085, B:31:0x00a0, B:37:0x00a6, B:42:0x00aa, B:49:0x00c2, B:51:0x00d9, B:53:0x00e1, B:55:0x00f6, B:60:0x0102, B:64:0x010c, B:66:0x0110, B:68:0x0136, B:71:0x0176, B:104:0x0255, B:105:0x025e, B:107:0x0269, B:109:0x0271, B:111:0x0284, B:113:0x028a, B:114:0x02a9, B:117:0x02ca, B:120:0x02d6, B:121:0x02e1, B:123:0x02e5, B:125:0x02e9, B:127:0x02ef, B:129:0x02f7, B:130:0x030c, B:131:0x0311, B:133:0x037f, B:134:0x0382, B:136:0x038a, B:138:0x039a, B:146:0x012d, B:152:0x0040), top: B:4:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02c7  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02d4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x02e5 A[Catch: Exception -> 0x002b, TryCatch #2 {Exception -> 0x002b, blocks: (B:5:0x0010, B:7:0x0014, B:8:0x002e, B:10:0x003a, B:11:0x0045, B:12:0x006c, B:14:0x0075, B:15:0x007f, B:17:0x0085, B:31:0x00a0, B:37:0x00a6, B:42:0x00aa, B:49:0x00c2, B:51:0x00d9, B:53:0x00e1, B:55:0x00f6, B:60:0x0102, B:64:0x010c, B:66:0x0110, B:68:0x0136, B:71:0x0176, B:104:0x0255, B:105:0x025e, B:107:0x0269, B:109:0x0271, B:111:0x0284, B:113:0x028a, B:114:0x02a9, B:117:0x02ca, B:120:0x02d6, B:121:0x02e1, B:123:0x02e5, B:125:0x02e9, B:127:0x02ef, B:129:0x02f7, B:130:0x030c, B:131:0x0311, B:133:0x037f, B:134:0x0382, B:136:0x038a, B:138:0x039a, B:146:0x012d, B:152:0x0040), top: B:4:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x037f A[Catch: Exception -> 0x002b, TryCatch #2 {Exception -> 0x002b, blocks: (B:5:0x0010, B:7:0x0014, B:8:0x002e, B:10:0x003a, B:11:0x0045, B:12:0x006c, B:14:0x0075, B:15:0x007f, B:17:0x0085, B:31:0x00a0, B:37:0x00a6, B:42:0x00aa, B:49:0x00c2, B:51:0x00d9, B:53:0x00e1, B:55:0x00f6, B:60:0x0102, B:64:0x010c, B:66:0x0110, B:68:0x0136, B:71:0x0176, B:104:0x0255, B:105:0x025e, B:107:0x0269, B:109:0x0271, B:111:0x0284, B:113:0x028a, B:114:0x02a9, B:117:0x02ca, B:120:0x02d6, B:121:0x02e1, B:123:0x02e5, B:125:0x02e9, B:127:0x02ef, B:129:0x02f7, B:130:0x030c, B:131:0x0311, B:133:0x037f, B:134:0x0382, B:136:0x038a, B:138:0x039a, B:146:0x012d, B:152:0x0040), top: B:4:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x025c  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x012d A[Catch: Exception -> 0x002b, TryCatch #2 {Exception -> 0x002b, blocks: (B:5:0x0010, B:7:0x0014, B:8:0x002e, B:10:0x003a, B:11:0x0045, B:12:0x006c, B:14:0x0075, B:15:0x007f, B:17:0x0085, B:31:0x00a0, B:37:0x00a6, B:42:0x00aa, B:49:0x00c2, B:51:0x00d9, B:53:0x00e1, B:55:0x00f6, B:60:0x0102, B:64:0x010c, B:66:0x0110, B:68:0x0136, B:71:0x0176, B:104:0x0255, B:105:0x025e, B:107:0x0269, B:109:0x0271, B:111:0x0284, B:113:0x028a, B:114:0x02a9, B:117:0x02ca, B:120:0x02d6, B:121:0x02e1, B:123:0x02e5, B:125:0x02e9, B:127:0x02ef, B:129:0x02f7, B:130:0x030c, B:131:0x0311, B:133:0x037f, B:134:0x0382, B:136:0x038a, B:138:0x039a, B:146:0x012d, B:152:0x0040), top: B:4:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0110 A[Catch: Exception -> 0x002b, TryCatch #2 {Exception -> 0x002b, blocks: (B:5:0x0010, B:7:0x0014, B:8:0x002e, B:10:0x003a, B:11:0x0045, B:12:0x006c, B:14:0x0075, B:15:0x007f, B:17:0x0085, B:31:0x00a0, B:37:0x00a6, B:42:0x00aa, B:49:0x00c2, B:51:0x00d9, B:53:0x00e1, B:55:0x00f6, B:60:0x0102, B:64:0x010c, B:66:0x0110, B:68:0x0136, B:71:0x0176, B:104:0x0255, B:105:0x025e, B:107:0x0269, B:109:0x0271, B:111:0x0284, B:113:0x028a, B:114:0x02a9, B:117:0x02ca, B:120:0x02d6, B:121:0x02e1, B:123:0x02e5, B:125:0x02e9, B:127:0x02ef, B:129:0x02f7, B:130:0x030c, B:131:0x0311, B:133:0x037f, B:134:0x0382, B:136:0x038a, B:138:0x039a, B:146:0x012d, B:152:0x0040), top: B:4:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0188 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x020f A[Catch: Exception -> 0x01e9, TryCatch #3 {Exception -> 0x01e9, blocks: (B:75:0x0188, B:77:0x01dc, B:79:0x01ee, B:90:0x0200, B:92:0x020f, B:93:0x0218, B:95:0x021e, B:99:0x0233, B:102:0x0251), top: B:74:0x0188 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void initiateActualEncryptedCall() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int size;
        int i;
        boolean z5;
        SharedPreferences sharedPreferences;
        ArrayList arrayList;
        final VoIPService voIPService;
        Instance.Proxy proxy;
        boolean z6;
        long j;
        int checkSelfPermission;
        String string;
        VoIPService voIPService2 = this;
        Runnable runnable = voIPService2.timeoutRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            voIPService2.timeoutRunnable = null;
        }
        try {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("InitCall: keyID=" + voIPService2.keyFingerprint);
            }
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(voIPService2.currentAccount);
            Set<String> stringSet = notificationsSettings.getStringSet("calls_access_hashes", null);
            HashSet hashSet = stringSet != null ? new HashSet(stringSet) : new HashSet();
            hashSet.add(voIPService2.privateCall.id + " " + voIPService2.privateCall.access_hash + " " + System.currentTimeMillis());
            while (hashSet.size() > 20) {
                Iterator it = hashSet.iterator();
                long j2 = Long.MAX_VALUE;
                String str = null;
                while (it.hasNext()) {
                    String str2 = (String) it.next();
                    String[] split = str2.split(" ");
                    if (split.length >= 2) {
                        try {
                            long parseLong = Long.parseLong(split[2]);
                            if (parseLong < j2) {
                                str = str2;
                                j2 = parseLong;
                            }
                        } catch (Exception unused) {
                        }
                    }
                    it.remove();
                }
                if (str != null) {
                    hashSet.remove(str);
                }
            }
            notificationsSettings.edit().putStringSet("calls_access_hashes", hashSet).commit();
            try {
                z = AcousticEchoCanceler.isAvailable();
            } catch (Exception unused2) {
                z = false;
            }
            try {
                z2 = NoiseSuppressor.isAvailable();
            } catch (Exception unused3) {
                z2 = false;
            }
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            MessagesController messagesController = MessagesController.getInstance(voIPService2.currentAccount);
            double d = messagesController.callConnectTimeout;
            Double.isNaN(d);
            double d2 = d / 1000.0d;
            double d3 = messagesController.callPacketTimeout;
            Double.isNaN(d3);
            double d4 = d3 / 1000.0d;
            int convertDataSavingMode = voIPService2.convertDataSavingMode(globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault()));
            Instance.ServerConfig globalServerConfig = Instance.getGlobalServerConfig();
            if (z && globalServerConfig.useSystemAec) {
                z3 = false;
                if (z2 && globalServerConfig.useSystemNs) {
                    z4 = false;
                    String logFilePath = BuildVars.DEBUG_VERSION ? VoIPHelper.getLogFilePath(voIPService2.privateCall.id, false) : VoIPHelper.getLogFilePath("voip" + voIPService2.privateCall.id);
                    String logFilePath2 = VoIPHelper.getLogFilePath(voIPService2.privateCall.id, true);
                    TL_phone.PhoneCall phoneCall = voIPService2.privateCall;
                    Instance.Config config = new Instance.Config(d2, d4, convertDataSavingMode, phoneCall.p2p_allowed, z3, z4, true, false, globalServerConfig.enableStunMarking, logFilePath, logFilePath2, phoneCall.protocol.max_layer);
                    String absolutePath = new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_persistent_state.json").getAbsolutePath();
                    boolean z7 = globalMainSettings.getBoolean("dbg_force_tcp_in_calls", false);
                    int i2 = !z7 ? 3 : 2;
                    size = voIPService2.privateCall.connections.size();
                    Instance.Endpoint[] endpointArr = new Instance.Endpoint[size];
                    ArrayList arrayList2 = new ArrayList();
                    i = 0;
                    while (i < size) {
                        try {
                            TLRPC.PhoneConnection phoneConnection = voIPService2.privateCall.connections.get(i);
                            SharedPreferences sharedPreferences2 = globalMainSettings;
                            boolean z8 = z7;
                            int i3 = size;
                            int i4 = i;
                            ArrayList arrayList3 = arrayList2;
                            Instance.Endpoint[] endpointArr2 = endpointArr;
                            endpointArr2[i4] = new Instance.Endpoint(phoneConnection instanceof TLRPC.TL_phoneConnectionWebrtc, phoneConnection.id, phoneConnection.ip, phoneConnection.ipv6, phoneConnection.port, i2, phoneConnection.peer_tag, phoneConnection.turn, phoneConnection.stun, phoneConnection.username, phoneConnection.password, phoneConnection.tcp);
                            if (phoneConnection instanceof TLRPC.TL_phoneConnection) {
                                arrayList3.add(Long.valueOf(((TLRPC.TL_phoneConnection) phoneConnection).id));
                            }
                            i = i4 + 1;
                            voIPService2 = this;
                            arrayList2 = arrayList3;
                            globalMainSettings = sharedPreferences2;
                            z7 = z8;
                            size = i3;
                            endpointArr = endpointArr2;
                        } catch (Exception e) {
                            e = e;
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.e("error starting call", e);
                            }
                            callFailed();
                            return;
                        }
                    }
                    z5 = z7;
                    sharedPreferences = globalMainSettings;
                    int i5 = size;
                    arrayList = arrayList2;
                    Instance.Endpoint[] endpointArr3 = endpointArr;
                    if (!arrayList.isEmpty()) {
                        Collections.sort(arrayList);
                        HashMap hashMap = new HashMap();
                        int i6 = 0;
                        while (i6 < arrayList.size()) {
                            Long l = (Long) arrayList.get(i6);
                            i6++;
                            hashMap.put(l, Integer.valueOf(i6));
                        }
                        for (int i7 = 0; i7 < i5; i7++) {
                            Instance.Endpoint endpoint = endpointArr3[i7];
                            endpoint.reflectorId = ((Integer) Map.-EL.getOrDefault(hashMap, Long.valueOf(endpoint.id), 0)).intValue();
                        }
                    }
                    if (z5) {
                        voIPService = this;
                    } else {
                        voIPService = this;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda31
                            @Override // java.lang.Runnable
                            public final void run() {
                                VoIPService.this.lambda$initiateActualEncryptedCall$54();
                            }
                        });
                    }
                    if (sharedPreferences.getBoolean("proxy_enabled", false) && sharedPreferences.getBoolean("proxy_enabled_calls", false)) {
                        string = sharedPreferences.getString("proxy_ip", null);
                        String string2 = sharedPreferences.getString("proxy_secret", null);
                        if (!TextUtils.isEmpty(string) && TextUtils.isEmpty(string2)) {
                            proxy = new Instance.Proxy(string, sharedPreferences.getInt("proxy_port", 0), sharedPreferences.getString("proxy_user", null), sharedPreferences.getString("proxy_pass", null));
                            Instance.EncryptionKey encryptionKey = new Instance.EncryptionKey(voIPService.authKey, voIPService.isOutgoing);
                            z6 = "2.7.7".compareTo(voIPService.privateCall.protocol.library_versions.get(0)) > 0;
                            j = voIPService.captureDevice[0];
                            if (j != 0 && !z6) {
                                NativeInstance.destroyVideoCapturer(j);
                                voIPService.captureDevice[0] = 0;
                                voIPService.videoState[0] = 0;
                            }
                            if (!voIPService.isOutgoing) {
                                if (voIPService.videoCall) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        checkSelfPermission = voIPService.checkSelfPermission("android.permission.CAMERA");
                                    }
                                    voIPService.captureDevice[0] = NativeInstance.createVideoCapturer(voIPService.localSink[0], voIPService.isFrontFaceCamera ? 1 : 0);
                                    voIPService.videoState[0] = 2;
                                }
                                voIPService.videoState[0] = 0;
                            }
                            voIPService.tgVoip[0] = Instance.makeInstance(voIPService.privateCall.protocol.library_versions.get(0), config, absolutePath, endpointArr3, proxy, getNetworkType(), encryptionKey, voIPService.remoteSink[0], voIPService.captureDevice[0], new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda32
                                @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                                public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                                    VoIPService.this.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
                                }
                            });
                            voIPService.tgVoip[0].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda33
                                @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                                public final void onStateUpdated(int i8, boolean z9) {
                                    VoIPService.this.onConnectionStateChanged(i8, z9);
                                }
                            });
                            voIPService.tgVoip[0].setOnSignalBarsUpdatedListener(new Instance.OnSignalBarsUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda34
                                @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
                                public final void onSignalBarsUpdated(int i8) {
                                    VoIPService.this.onSignalBarCountChanged(i8);
                                }
                            });
                            voIPService.tgVoip[0].setOnSignalDataListener(new Instance.OnSignalingDataListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda35
                                @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
                                public final void onSignalingData(byte[] bArr) {
                                    VoIPService.this.onSignalingData(bArr);
                                }
                            });
                            voIPService.tgVoip[0].setOnRemoteMediaStateUpdatedListener(new Instance.OnRemoteMediaStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda36
                                @Override // org.telegram.messenger.voip.Instance.OnRemoteMediaStateUpdatedListener
                                public final void onMediaStateUpdated(int i8, int i9) {
                                    VoIPService.this.lambda$initiateActualEncryptedCall$57(i8, i9);
                                }
                            });
                            voIPService.tgVoip[0].setMuteMicrophone(voIPService.micMute);
                            if (z6 != voIPService.isVideoAvailable) {
                                voIPService.isVideoAvailable = z6;
                                for (int i8 = 0; i8 < voIPService.stateListeners.size(); i8++) {
                                    voIPService.stateListeners.get(i8).onVideoAvailableChange(voIPService.isVideoAvailable);
                                }
                            }
                            voIPService.destroyCaptureDevice[0] = false;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (VoIPService.this.tgVoip[0] != null) {
                                        VoIPService voIPService3 = VoIPService.this;
                                        voIPService3.updateTrafficStats(voIPService3.tgVoip[0], null);
                                        AndroidUtilities.runOnUIThread(this, 5000L);
                                    }
                                }
                            }, 5000L);
                        }
                    }
                    proxy = null;
                    Instance.EncryptionKey encryptionKey2 = new Instance.EncryptionKey(voIPService.authKey, voIPService.isOutgoing);
                    if ("2.7.7".compareTo(voIPService.privateCall.protocol.library_versions.get(0)) > 0) {
                    }
                    j = voIPService.captureDevice[0];
                    if (j != 0) {
                        NativeInstance.destroyVideoCapturer(j);
                        voIPService.captureDevice[0] = 0;
                        voIPService.videoState[0] = 0;
                    }
                    if (!voIPService.isOutgoing) {
                    }
                    voIPService.tgVoip[0] = Instance.makeInstance(voIPService.privateCall.protocol.library_versions.get(0), config, absolutePath, endpointArr3, proxy, getNetworkType(), encryptionKey2, voIPService.remoteSink[0], voIPService.captureDevice[0], new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda32
                        @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                        public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                            VoIPService.this.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
                        }
                    });
                    voIPService.tgVoip[0].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda33
                        @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                        public final void onStateUpdated(int i82, boolean z9) {
                            VoIPService.this.onConnectionStateChanged(i82, z9);
                        }
                    });
                    voIPService.tgVoip[0].setOnSignalBarsUpdatedListener(new Instance.OnSignalBarsUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda34
                        @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
                        public final void onSignalBarsUpdated(int i82) {
                            VoIPService.this.onSignalBarCountChanged(i82);
                        }
                    });
                    voIPService.tgVoip[0].setOnSignalDataListener(new Instance.OnSignalingDataListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda35
                        @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
                        public final void onSignalingData(byte[] bArr) {
                            VoIPService.this.onSignalingData(bArr);
                        }
                    });
                    voIPService.tgVoip[0].setOnRemoteMediaStateUpdatedListener(new Instance.OnRemoteMediaStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda36
                        @Override // org.telegram.messenger.voip.Instance.OnRemoteMediaStateUpdatedListener
                        public final void onMediaStateUpdated(int i82, int i9) {
                            VoIPService.this.lambda$initiateActualEncryptedCall$57(i82, i9);
                        }
                    });
                    voIPService.tgVoip[0].setMuteMicrophone(voIPService.micMute);
                    if (z6 != voIPService.isVideoAvailable) {
                    }
                    voIPService.destroyCaptureDevice[0] = false;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.6
                        @Override // java.lang.Runnable
                        public void run() {
                            if (VoIPService.this.tgVoip[0] != null) {
                                VoIPService voIPService3 = VoIPService.this;
                                voIPService3.updateTrafficStats(voIPService3.tgVoip[0], null);
                                AndroidUtilities.runOnUIThread(this, 5000L);
                            }
                        }
                    }, 5000L);
                }
                z4 = true;
                if (BuildVars.DEBUG_VERSION) {
                }
                String logFilePath3 = BuildVars.DEBUG_VERSION ? VoIPHelper.getLogFilePath(voIPService2.privateCall.id, false) : VoIPHelper.getLogFilePath("voip" + voIPService2.privateCall.id);
                String logFilePath22 = VoIPHelper.getLogFilePath(voIPService2.privateCall.id, true);
                TL_phone.PhoneCall phoneCall2 = voIPService2.privateCall;
                Instance.Config config2 = new Instance.Config(d2, d4, convertDataSavingMode, phoneCall2.p2p_allowed, z3, z4, true, false, globalServerConfig.enableStunMarking, logFilePath3, logFilePath22, phoneCall2.protocol.max_layer);
                String absolutePath2 = new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_persistent_state.json").getAbsolutePath();
                boolean z72 = globalMainSettings.getBoolean("dbg_force_tcp_in_calls", false);
                if (!z72) {
                }
                size = voIPService2.privateCall.connections.size();
                Instance.Endpoint[] endpointArr4 = new Instance.Endpoint[size];
                ArrayList arrayList22 = new ArrayList();
                i = 0;
                while (i < size) {
                }
                z5 = z72;
                sharedPreferences = globalMainSettings;
                int i52 = size;
                arrayList = arrayList22;
                Instance.Endpoint[] endpointArr32 = endpointArr4;
                if (!arrayList.isEmpty()) {
                }
                if (z5) {
                }
                if (sharedPreferences.getBoolean("proxy_enabled", false)) {
                    string = sharedPreferences.getString("proxy_ip", null);
                    String string22 = sharedPreferences.getString("proxy_secret", null);
                    if (!TextUtils.isEmpty(string)) {
                        proxy = new Instance.Proxy(string, sharedPreferences.getInt("proxy_port", 0), sharedPreferences.getString("proxy_user", null), sharedPreferences.getString("proxy_pass", null));
                        Instance.EncryptionKey encryptionKey22 = new Instance.EncryptionKey(voIPService.authKey, voIPService.isOutgoing);
                        if ("2.7.7".compareTo(voIPService.privateCall.protocol.library_versions.get(0)) > 0) {
                        }
                        j = voIPService.captureDevice[0];
                        if (j != 0) {
                        }
                        if (!voIPService.isOutgoing) {
                        }
                        voIPService.tgVoip[0] = Instance.makeInstance(voIPService.privateCall.protocol.library_versions.get(0), config2, absolutePath2, endpointArr32, proxy, getNetworkType(), encryptionKey22, voIPService.remoteSink[0], voIPService.captureDevice[0], new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda32
                            @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                            public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                                VoIPService.this.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
                            }
                        });
                        voIPService.tgVoip[0].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda33
                            @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                            public final void onStateUpdated(int i82, boolean z9) {
                                VoIPService.this.onConnectionStateChanged(i82, z9);
                            }
                        });
                        voIPService.tgVoip[0].setOnSignalBarsUpdatedListener(new Instance.OnSignalBarsUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda34
                            @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
                            public final void onSignalBarsUpdated(int i82) {
                                VoIPService.this.onSignalBarCountChanged(i82);
                            }
                        });
                        voIPService.tgVoip[0].setOnSignalDataListener(new Instance.OnSignalingDataListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda35
                            @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
                            public final void onSignalingData(byte[] bArr) {
                                VoIPService.this.onSignalingData(bArr);
                            }
                        });
                        voIPService.tgVoip[0].setOnRemoteMediaStateUpdatedListener(new Instance.OnRemoteMediaStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda36
                            @Override // org.telegram.messenger.voip.Instance.OnRemoteMediaStateUpdatedListener
                            public final void onMediaStateUpdated(int i82, int i9) {
                                VoIPService.this.lambda$initiateActualEncryptedCall$57(i82, i9);
                            }
                        });
                        voIPService.tgVoip[0].setMuteMicrophone(voIPService.micMute);
                        if (z6 != voIPService.isVideoAvailable) {
                        }
                        voIPService.destroyCaptureDevice[0] = false;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.6
                            @Override // java.lang.Runnable
                            public void run() {
                                if (VoIPService.this.tgVoip[0] != null) {
                                    VoIPService voIPService3 = VoIPService.this;
                                    voIPService3.updateTrafficStats(voIPService3.tgVoip[0], null);
                                    AndroidUtilities.runOnUIThread(this, 5000L);
                                }
                            }
                        }, 5000L);
                    }
                }
                proxy = null;
                Instance.EncryptionKey encryptionKey222 = new Instance.EncryptionKey(voIPService.authKey, voIPService.isOutgoing);
                if ("2.7.7".compareTo(voIPService.privateCall.protocol.library_versions.get(0)) > 0) {
                }
                j = voIPService.captureDevice[0];
                if (j != 0) {
                }
                if (!voIPService.isOutgoing) {
                }
                voIPService.tgVoip[0] = Instance.makeInstance(voIPService.privateCall.protocol.library_versions.get(0), config2, absolutePath2, endpointArr32, proxy, getNetworkType(), encryptionKey222, voIPService.remoteSink[0], voIPService.captureDevice[0], new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda32
                    @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                    public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                        VoIPService.this.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
                    }
                });
                voIPService.tgVoip[0].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda33
                    @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                    public final void onStateUpdated(int i82, boolean z9) {
                        VoIPService.this.onConnectionStateChanged(i82, z9);
                    }
                });
                voIPService.tgVoip[0].setOnSignalBarsUpdatedListener(new Instance.OnSignalBarsUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda34
                    @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
                    public final void onSignalBarsUpdated(int i82) {
                        VoIPService.this.onSignalBarCountChanged(i82);
                    }
                });
                voIPService.tgVoip[0].setOnSignalDataListener(new Instance.OnSignalingDataListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda35
                    @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
                    public final void onSignalingData(byte[] bArr) {
                        VoIPService.this.onSignalingData(bArr);
                    }
                });
                voIPService.tgVoip[0].setOnRemoteMediaStateUpdatedListener(new Instance.OnRemoteMediaStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda36
                    @Override // org.telegram.messenger.voip.Instance.OnRemoteMediaStateUpdatedListener
                    public final void onMediaStateUpdated(int i82, int i9) {
                        VoIPService.this.lambda$initiateActualEncryptedCall$57(i82, i9);
                    }
                });
                voIPService.tgVoip[0].setMuteMicrophone(voIPService.micMute);
                if (z6 != voIPService.isVideoAvailable) {
                }
                voIPService.destroyCaptureDevice[0] = false;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.6
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VoIPService.this.tgVoip[0] != null) {
                            VoIPService voIPService3 = VoIPService.this;
                            voIPService3.updateTrafficStats(voIPService3.tgVoip[0], null);
                            AndroidUtilities.runOnUIThread(this, 5000L);
                        }
                    }
                }, 5000L);
            }
            z3 = true;
            if (z2) {
                z4 = false;
                if (BuildVars.DEBUG_VERSION) {
                }
                String logFilePath32 = BuildVars.DEBUG_VERSION ? VoIPHelper.getLogFilePath(voIPService2.privateCall.id, false) : VoIPHelper.getLogFilePath("voip" + voIPService2.privateCall.id);
                String logFilePath222 = VoIPHelper.getLogFilePath(voIPService2.privateCall.id, true);
                TL_phone.PhoneCall phoneCall22 = voIPService2.privateCall;
                Instance.Config config22 = new Instance.Config(d2, d4, convertDataSavingMode, phoneCall22.p2p_allowed, z3, z4, true, false, globalServerConfig.enableStunMarking, logFilePath32, logFilePath222, phoneCall22.protocol.max_layer);
                String absolutePath22 = new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_persistent_state.json").getAbsolutePath();
                boolean z722 = globalMainSettings.getBoolean("dbg_force_tcp_in_calls", false);
                if (!z722) {
                }
                size = voIPService2.privateCall.connections.size();
                Instance.Endpoint[] endpointArr42 = new Instance.Endpoint[size];
                ArrayList arrayList222 = new ArrayList();
                i = 0;
                while (i < size) {
                }
                z5 = z722;
                sharedPreferences = globalMainSettings;
                int i522 = size;
                arrayList = arrayList222;
                Instance.Endpoint[] endpointArr322 = endpointArr42;
                if (!arrayList.isEmpty()) {
                }
                if (z5) {
                }
                if (sharedPreferences.getBoolean("proxy_enabled", false)) {
                }
                proxy = null;
                Instance.EncryptionKey encryptionKey2222 = new Instance.EncryptionKey(voIPService.authKey, voIPService.isOutgoing);
                if ("2.7.7".compareTo(voIPService.privateCall.protocol.library_versions.get(0)) > 0) {
                }
                j = voIPService.captureDevice[0];
                if (j != 0) {
                }
                if (!voIPService.isOutgoing) {
                }
                voIPService.tgVoip[0] = Instance.makeInstance(voIPService.privateCall.protocol.library_versions.get(0), config22, absolutePath22, endpointArr322, proxy, getNetworkType(), encryptionKey2222, voIPService.remoteSink[0], voIPService.captureDevice[0], new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda32
                    @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                    public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                        VoIPService.this.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
                    }
                });
                voIPService.tgVoip[0].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda33
                    @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                    public final void onStateUpdated(int i82, boolean z9) {
                        VoIPService.this.onConnectionStateChanged(i82, z9);
                    }
                });
                voIPService.tgVoip[0].setOnSignalBarsUpdatedListener(new Instance.OnSignalBarsUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda34
                    @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
                    public final void onSignalBarsUpdated(int i82) {
                        VoIPService.this.onSignalBarCountChanged(i82);
                    }
                });
                voIPService.tgVoip[0].setOnSignalDataListener(new Instance.OnSignalingDataListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda35
                    @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
                    public final void onSignalingData(byte[] bArr) {
                        VoIPService.this.onSignalingData(bArr);
                    }
                });
                voIPService.tgVoip[0].setOnRemoteMediaStateUpdatedListener(new Instance.OnRemoteMediaStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda36
                    @Override // org.telegram.messenger.voip.Instance.OnRemoteMediaStateUpdatedListener
                    public final void onMediaStateUpdated(int i82, int i9) {
                        VoIPService.this.lambda$initiateActualEncryptedCall$57(i82, i9);
                    }
                });
                voIPService.tgVoip[0].setMuteMicrophone(voIPService.micMute);
                if (z6 != voIPService.isVideoAvailable) {
                }
                voIPService.destroyCaptureDevice[0] = false;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.6
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VoIPService.this.tgVoip[0] != null) {
                            VoIPService voIPService3 = VoIPService.this;
                            voIPService3.updateTrafficStats(voIPService3.tgVoip[0], null);
                            AndroidUtilities.runOnUIThread(this, 5000L);
                        }
                    }
                }, 5000L);
            }
            z4 = true;
            if (BuildVars.DEBUG_VERSION) {
            }
            String logFilePath322 = BuildVars.DEBUG_VERSION ? VoIPHelper.getLogFilePath(voIPService2.privateCall.id, false) : VoIPHelper.getLogFilePath("voip" + voIPService2.privateCall.id);
            String logFilePath2222 = VoIPHelper.getLogFilePath(voIPService2.privateCall.id, true);
            TL_phone.PhoneCall phoneCall222 = voIPService2.privateCall;
            Instance.Config config222 = new Instance.Config(d2, d4, convertDataSavingMode, phoneCall222.p2p_allowed, z3, z4, true, false, globalServerConfig.enableStunMarking, logFilePath322, logFilePath2222, phoneCall222.protocol.max_layer);
            String absolutePath222 = new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_persistent_state.json").getAbsolutePath();
            boolean z7222 = globalMainSettings.getBoolean("dbg_force_tcp_in_calls", false);
            if (!z7222) {
            }
            size = voIPService2.privateCall.connections.size();
            Instance.Endpoint[] endpointArr422 = new Instance.Endpoint[size];
            ArrayList arrayList2222 = new ArrayList();
            i = 0;
            while (i < size) {
            }
            z5 = z7222;
            sharedPreferences = globalMainSettings;
            int i5222 = size;
            arrayList = arrayList2222;
            Instance.Endpoint[] endpointArr3222 = endpointArr422;
            if (!arrayList.isEmpty()) {
            }
            if (z5) {
            }
            if (sharedPreferences.getBoolean("proxy_enabled", false)) {
            }
            proxy = null;
            Instance.EncryptionKey encryptionKey22222 = new Instance.EncryptionKey(voIPService.authKey, voIPService.isOutgoing);
            if ("2.7.7".compareTo(voIPService.privateCall.protocol.library_versions.get(0)) > 0) {
            }
            j = voIPService.captureDevice[0];
            if (j != 0) {
            }
            if (!voIPService.isOutgoing) {
            }
            voIPService.tgVoip[0] = Instance.makeInstance(voIPService.privateCall.protocol.library_versions.get(0), config222, absolutePath222, endpointArr3222, proxy, getNetworkType(), encryptionKey22222, voIPService.remoteSink[0], voIPService.captureDevice[0], new NativeInstance.AudioLevelsCallback() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda32
                @Override // org.telegram.messenger.voip.NativeInstance.AudioLevelsCallback
                public final void run(int[] iArr, float[] fArr, boolean[] zArr) {
                    VoIPService.this.lambda$initiateActualEncryptedCall$55(iArr, fArr, zArr);
                }
            });
            voIPService.tgVoip[0].setOnStateUpdatedListener(new Instance.OnStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda33
                @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
                public final void onStateUpdated(int i82, boolean z9) {
                    VoIPService.this.onConnectionStateChanged(i82, z9);
                }
            });
            voIPService.tgVoip[0].setOnSignalBarsUpdatedListener(new Instance.OnSignalBarsUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda34
                @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
                public final void onSignalBarsUpdated(int i82) {
                    VoIPService.this.onSignalBarCountChanged(i82);
                }
            });
            voIPService.tgVoip[0].setOnSignalDataListener(new Instance.OnSignalingDataListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda35
                @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
                public final void onSignalingData(byte[] bArr) {
                    VoIPService.this.onSignalingData(bArr);
                }
            });
            voIPService.tgVoip[0].setOnRemoteMediaStateUpdatedListener(new Instance.OnRemoteMediaStateUpdatedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda36
                @Override // org.telegram.messenger.voip.Instance.OnRemoteMediaStateUpdatedListener
                public final void onMediaStateUpdated(int i82, int i9) {
                    VoIPService.this.lambda$initiateActualEncryptedCall$57(i82, i9);
                }
            });
            voIPService.tgVoip[0].setMuteMicrophone(voIPService.micMute);
            if (z6 != voIPService.isVideoAvailable) {
            }
            voIPService.destroyCaptureDevice[0] = false;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.6
                @Override // java.lang.Runnable
                public void run() {
                    if (VoIPService.this.tgVoip[0] != null) {
                        VoIPService voIPService3 = VoIPService.this;
                        voIPService3.updateTrafficStats(voIPService3.tgVoip[0], null);
                        AndroidUtilities.runOnUIThread(this, 5000L);
                    }
                }
            }, 5000L);
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static boolean isAnyKindOfCallActive() {
        return (getSharedInstance() == null || getSharedInstance().getCallState() == 15) ? false : true;
    }

    private static boolean isDeviceCompatibleWithConnectionServiceAPI() {
        return false;
    }

    private boolean isFinished() {
        int i = this.currentState;
        return i == 11 || i == 4;
    }

    private boolean isRinging() {
        return this.currentState == 15;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$acceptIncomingCall$70() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didStartedCall, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptIncomingCall$71(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error on phone.acceptCall: " + tL_error);
            }
            callFailed();
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.w("accept call ok! " + tLObject);
        }
        TL_phone.PhoneCall phoneCall = ((TL_phone.TL_phone_phoneCall) tLObject).phone_call;
        this.privateCall = phoneCall;
        if (phoneCall instanceof TL_phone.TL_phoneCallDiscarded) {
            onCallUpdated(phoneCall);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptIncomingCall$72(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda124
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$acceptIncomingCall$71(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptIncomingCall$73(MessagesStorage messagesStorage, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            callFailed();
            return;
        }
        TLRPC.messages_DhConfig messages_dhconfig = (TLRPC.messages_DhConfig) tLObject;
        if (tLObject instanceof TLRPC.TL_messages_dhConfig) {
            if (!Utilities.isGoodPrime(messages_dhconfig.p, messages_dhconfig.g)) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("stopping VoIP service, bad prime");
                }
                callFailed();
                return;
            } else {
                messagesStorage.setSecretPBytes(messages_dhconfig.p);
                messagesStorage.setSecretG(messages_dhconfig.g);
                messagesStorage.setLastSecretVersion(messages_dhconfig.version);
                MessagesStorage.getInstance(this.currentAccount).saveSecretParams(messagesStorage.getLastSecretVersion(), messagesStorage.getSecretG(), messagesStorage.getSecretPBytes());
            }
        }
        byte[] bArr = new byte[256];
        for (int i = 0; i < 256; i++) {
            bArr[i] = (byte) (((byte) (Utilities.random.nextDouble() * 256.0d)) ^ messages_dhconfig.random[i]);
        }
        if (this.privateCall == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("call is null");
            }
            callFailed();
            return;
        }
        this.a_or_b = bArr;
        BigInteger modPow = BigInteger.valueOf(messagesStorage.getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, messagesStorage.getSecretPBytes()));
        this.g_a_hash = this.privateCall.g_a_hash;
        byte[] byteArray = modPow.toByteArray();
        if (byteArray.length > 256) {
            byte[] bArr2 = new byte[256];
            System.arraycopy(byteArray, 1, bArr2, 0, 256);
            byteArray = bArr2;
        }
        TL_phone.acceptCall acceptcall = new TL_phone.acceptCall();
        acceptcall.g_b = byteArray;
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        acceptcall.peer = tL_inputPhoneCall;
        TL_phone.PhoneCall phoneCall = this.privateCall;
        tL_inputPhoneCall.id = phoneCall.id;
        tL_inputPhoneCall.access_hash = phoneCall.access_hash;
        TL_phone.TL_phoneCallProtocol tL_phoneCallProtocol = new TL_phone.TL_phoneCallProtocol();
        acceptcall.protocol = tL_phoneCallProtocol;
        tL_phoneCallProtocol.udp_reflector = true;
        tL_phoneCallProtocol.udp_p2p = true;
        tL_phoneCallProtocol.min_layer = 65;
        tL_phoneCallProtocol.max_layer = Instance.getConnectionMaxLayer();
        acceptcall.protocol.library_versions.addAll(Instance.AVAILABLE_VERSIONS);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(acceptcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda111
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                VoIPService.this.lambda$acceptIncomingCall$72(tLObject2, tL_error2);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acknowledgeCall$11(TLObject tLObject, TLRPC.TL_error tL_error, boolean z) {
        if (sharedInstance == null) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.w("receivedCall response = " + tLObject);
        }
        if (tL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("error on receivedCall: " + tL_error);
            }
            stopSelf();
            return;
        }
        if (USE_CONNECTION_SERVICE) {
            ContactsController contactsController = ContactsController.getInstance(this.currentAccount);
            TLRPC.User user = this.user;
            contactsController.createOrUpdateConnectionServiceContact(user.id, user.first_name, user.last_name);
            TelecomManager m = VoIPService$$ExternalSyntheticApiModelOutline5.m(getSystemService("telecom"));
            Bundle bundle = new Bundle();
            bundle.putInt("call_type", 1);
            m.addNewIncomingCall(addAccountToTelecomManager(), bundle);
        }
        if (z) {
            startRinging();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acknowledgeCall$12(final boolean z, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$acknowledgeCall$11(tLObject, tL_error, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callEnded$93() {
        dispatchStateChanged(11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callEnded$94() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
            this.spPlayId = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callEnded$95() {
        this.soundPool.play(this.spEndId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callEnded$96() {
        this.soundPool.play(this.spVoiceChatEndId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$callFailed$84(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("error on phone.discardCall: " + tL_error);
                return;
            }
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("phone.discardCall " + tLObject);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callFailed$85() {
        dispatchStateChanged(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callFailed$86() {
        this.soundPool.play(this.spFailedID, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$configureDeviceForCall$80() {
        if (MediaController.getInstance().isMessagePaused()) {
            return;
        }
        MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$configureDeviceForCall$81(AudioManager audioManager) {
        this.hasAudioFocus = audioManager.requestAudioFocus(this, 0, 2) == 1;
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        if (isBluetoothHeadsetConnected() && hasEarpiece()) {
            int i = this.audioRouteToSet;
            if (i == 0) {
                audioManager.setBluetoothScoOn(false);
            } else if (i == 1) {
                audioManager.setBluetoothScoOn(false);
                voipAudioManager.setSpeakerphoneOn(true);
            } else if (i == 2) {
                if (this.bluetoothScoActive) {
                    audioManager.setBluetoothScoOn(true);
                } else {
                    this.needSwitchToBluetoothAfterScoActivates = true;
                    try {
                        audioManager.startBluetoothSco();
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
            }
            voipAudioManager.setSpeakerphoneOn(false);
        } else if (isBluetoothHeadsetConnected()) {
            audioManager.setBluetoothScoOn(this.speakerphoneStateToSet);
        } else {
            voipAudioManager.setSpeakerphoneOn(this.speakerphoneStateToSet);
            if (this.speakerphoneStateToSet) {
                this.audioRouteToSet = 1;
            } else {
                this.audioRouteToSet = 0;
            }
            SensorEvent sensorEvent = this.lastSensorEvent;
            if (sensorEvent != null) {
                onSensorChanged(sensorEvent);
            }
        }
        updateOutputGainControlState();
        this.audioConfigured = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$configureDeviceForCall$82(final AudioManager audioManager) {
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (!hasRtmpStream()) {
            audioManager.setMode(3);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda110
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$configureDeviceForCall$81(audioManager);
                }
            });
        } else {
            audioManager.setMode(0);
            audioManager.setBluetoothScoOn(false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda109
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.lambda$configureDeviceForCall$80();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$36(int i, int i2, String str) {
        if (i == 0) {
            startGroupCall(i2, str, true);
        } else {
            startScreenCapture(i2, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createGroupInstance$37(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$38(int i, int[] iArr, float[] fArr, boolean[] zArr) {
        ChatObject.Call call;
        if (sharedInstance == null || (call = this.groupCall) == null || i != 0) {
            return;
        }
        call.processVoiceLevelsUpdate(iArr, fArr, zArr);
        float f = 0.0f;
        boolean z = false;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] == 0) {
                if (this.lastTypingTimeSend < SystemClock.uptimeMillis() - 5000 && fArr[i2] > 0.1f && zArr[i2]) {
                    this.lastTypingTimeSend = SystemClock.uptimeMillis();
                    TLRPC.TL_messages_setTyping tL_messages_setTyping = new TLRPC.TL_messages_setTyping();
                    tL_messages_setTyping.action = new TLRPC.TL_speakingInGroupCallAction();
                    tL_messages_setTyping.peer = MessagesController.getInputPeer(this.chat);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_setTyping, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda48
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            VoIPService.lambda$createGroupInstance$37(tLObject, tL_error);
                        }
                    });
                }
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.webRtcMicAmplitudeEvent, Float.valueOf(fArr[i2]));
            } else {
                f = Math.max(f, fArr[i2]);
                z = true;
            }
        }
        if (z) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.webRtcSpeakerAmplitudeEvent, Float.valueOf(f));
            NativeInstance.AudioLevelsCallback audioLevelsCallback2 = audioLevelsCallback;
            if (audioLevelsCallback2 != null) {
                audioLevelsCallback2.run(iArr, fArr, zArr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$39(long j, int[] iArr, ArrayList arrayList) {
        if (sharedInstance == null || this.groupCall == null) {
            return;
        }
        broadcastUnknownParticipants(j, iArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$40(int i, final long j, final int[] iArr) {
        ChatObject.Call call;
        if (sharedInstance == null || (call = this.groupCall) == null || i != 0) {
            return;
        }
        call.processUnknownVideoParticipants(iArr, new ChatObject.Call.OnParticipantsLoad() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda65
            @Override // org.telegram.messenger.ChatObject.Call.OnParticipantsLoad
            public final void onLoad(ArrayList arrayList) {
                VoIPService.this.lambda$createGroupInstance$39(j, iArr, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$41(String str) {
        this.currentStreamRequestTimestamp.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$42(int i) {
        createGroupInstance(i, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$43(final String str, final int i, long j, int i2, int i3, TLObject tLObject, TLRPC.TL_error tL_error, long j2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$createGroupInstance$41(str);
            }
        });
        NativeInstance nativeInstance = this.tgVoip[i];
        if (nativeInstance == null) {
            return;
        }
        if (tLObject != null) {
            NativeByteBuffer nativeByteBuffer = ((TLRPC.TL_upload_file) tLObject).bytes;
            nativeInstance.onStreamPartAvailable(j, nativeByteBuffer.buffer, nativeByteBuffer.limit(), j2, i2, i3);
        } else if ("GROUPCALL_JOIN_MISSING".equals(tL_error.text)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$createGroupInstance$42(i);
                }
            });
        } else {
            this.tgVoip[i].onStreamPartAvailable(j, null, ("TIME_TOO_BIG".equals(tL_error.text) || tL_error.text.startsWith("FLOOD_WAIT")) ? 0 : -1, j2, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$44(String str, int i) {
        this.currentStreamRequestTimestamp.put(str, Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$45(final int i, final long j, long j2, final int i2, final int i3) {
        StringBuilder sb;
        if (i != 0) {
            return;
        }
        TLRPC.TL_upload_getFile tL_upload_getFile = new TLRPC.TL_upload_getFile();
        tL_upload_getFile.limit = 131072;
        TLRPC.TL_inputGroupCallStream tL_inputGroupCallStream = new TLRPC.TL_inputGroupCallStream();
        tL_inputGroupCallStream.call = this.groupCall.getInputGroupCall();
        tL_inputGroupCallStream.time_ms = j;
        if (j2 == 500) {
            tL_inputGroupCallStream.scale = 1;
        }
        if (i2 != 0) {
            tL_inputGroupCallStream.flags |= 1;
            tL_inputGroupCallStream.video_channel = i2;
            tL_inputGroupCallStream.video_quality = i3;
        }
        tL_upload_getFile.location = tL_inputGroupCallStream;
        if (i2 == 0) {
            sb = new StringBuilder();
            sb.append("");
            sb.append(j);
        } else {
            sb = new StringBuilder();
            sb.append(i2);
            sb.append("_");
            sb.append(j);
            sb.append("_");
            sb.append(i3);
        }
        final String sb2 = sb.toString();
        final int sendRequest = AccountInstance.getInstance(this.currentAccount).getConnectionsManager().sendRequest(tL_upload_getFile, new RequestDelegateTimestamp() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda112
            @Override // org.telegram.tgnet.RequestDelegateTimestamp
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error, long j3) {
                VoIPService.this.lambda$createGroupInstance$43(sb2, i, j, i2, i3, tLObject, tL_error, j3);
            }
        }, 2, 2, this.groupCall.call.stream_dc_id);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda113
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$createGroupInstance$44(sb2, sendRequest);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$46(int i, long j, int i2) {
        String str;
        if (i == 0) {
            str = "" + j;
        } else {
            str = i + "_" + j + "_" + i2;
        }
        Integer num = this.currentStreamRequestTimestamp.get(str);
        if (num != null) {
            AccountInstance.getInstance(this.currentAccount).getConnectionsManager().cancelRequest(num.intValue(), true);
            this.currentStreamRequestTimestamp.remove(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$47(int i, final long j, long j2, final int i2, final int i3) {
        if (i != 0) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda108
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$createGroupInstance$46(i2, j, i3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$48(int i, long j, TLObject tLObject, TLRPC.TL_error tL_error, long j2) {
        if (tL_error == null) {
            TL_phone.groupCallStreamChannels groupcallstreamchannels = (TL_phone.groupCallStreamChannels) tLObject;
            r7 = groupcallstreamchannels.channels.isEmpty() ? 0L : groupcallstreamchannels.channels.get(0).last_timestamp_ms;
            ChatObject.Call call = this.groupCall;
            if (!call.loadedRtmpStreamParticipant) {
                call.createRtmpStreamParticipant(groupcallstreamchannels.channels);
                this.groupCall.loadedRtmpStreamParticipant = true;
            }
        }
        NativeInstance nativeInstance = this.tgVoip[i];
        if (nativeInstance != null) {
            nativeInstance.onRequestTimeComplete(j, r7);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupInstance$49(final int i, final long j) {
        TLRPC.GroupCall groupCall;
        ChatObject.Call call = this.groupCall;
        if (call == null || (groupCall = call.call) == null || !groupCall.rtmp_stream) {
            NativeInstance nativeInstance = this.tgVoip[i];
            if (nativeInstance != null) {
                nativeInstance.onRequestTimeComplete(j, ConnectionsManager.getInstance(this.currentAccount).getCurrentTimeMillis());
                return;
            }
            return;
        }
        TL_phone.getGroupCallStreamChannels getgroupcallstreamchannels = new TL_phone.getGroupCallStreamChannels();
        getgroupcallstreamchannels.call = this.groupCall.getInputGroupCall();
        ChatObject.Call call2 = this.groupCall;
        if (call2 != null && call2.call != null && this.tgVoip[i] != null) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(getgroupcallstreamchannels, new RequestDelegateTimestamp() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda40
                @Override // org.telegram.tgnet.RequestDelegateTimestamp
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error, long j2) {
                    VoIPService.this.lambda$createGroupInstance$48(i, j, tLObject, tL_error, j2);
                }
            }, 2, 2, this.groupCall.call.stream_dc_id);
            return;
        }
        NativeInstance nativeInstance2 = this.tgVoip[i];
        if (nativeInstance2 != null) {
            nativeInstance2.onRequestTimeComplete(j, 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$declineIncomingCall$74() {
        if (this.currentState == 10) {
            callEnded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$declineIncomingCall$75(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("error on phone.discardCall: " + tL_error);
                return;
            }
            return;
        }
        if (tLObject instanceof TLRPC.TL_updates) {
            MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.TL_updates) tLObject, false);
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("phone.discardCall " + tLObject);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editCallMember$60(int i, Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            AccountInstance.getInstance(i).getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        } else if (tL_error != null && "GROUPCALL_VIDEO_TOO_MUCH".equals(tL_error.text)) {
            this.groupCall.reloadGroupCall();
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$endConnectionServiceCall$97() {
        DisconnectCause disconnectCause;
        CallConnection callConnection = this.systemCallConnection;
        if (callConnection != null) {
            int i = this.callDiscardReason;
            if (i == 1) {
                disconnectCause = new DisconnectCause(this.isOutgoing ? 2 : 6);
            } else if (i != 2) {
                if (i != 3) {
                    disconnectCause = i != 4 ? new DisconnectCause(3) : new DisconnectCause(7);
                } else {
                    disconnectCause = new DisconnectCause(this.isOutgoing ? 4 : 5);
                }
            } else {
                disconnectCause = new DisconnectCause(1);
            }
            callConnection.setDisconnected(disconnectCause);
            this.systemCallConnection.destroy();
            this.systemCallConnection = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getConnectionAndStartCall$76() {
        this.delayedStartOutgoingCall = null;
        startOutgoingCall();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hangUp$3(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.TL_updates) {
            MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.TL_updates) tLObject, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hangUp$4(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.TL_updates) {
            MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.TL_updates) tLObject, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateActualEncryptedCall$54() {
        Toast.makeText(this, "This call uses TCP which will degrade its quality.", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateActualEncryptedCall$55(int[] iArr, float[] fArr, boolean[] zArr) {
        if (sharedInstance == null || this.privateCall == null) {
            return;
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.webRtcMicAmplitudeEvent, Float.valueOf(fArr[0]));
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.webRtcSpeakerAmplitudeEvent, Float.valueOf(fArr[1]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateActualEncryptedCall$56(int i, int i2) {
        this.remoteAudioState = i;
        this.remoteVideoState = i2;
        checkIsNear();
        for (int i3 = 0; i3 < this.stateListeners.size(); i3++) {
            this.stateListeners.get(i3).onMediaStateUpdated(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateActualEncryptedCall$57(final int i, final int i2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda90
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$initiateActualEncryptedCall$56(i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadResources$79() {
        SoundPool soundPool = new SoundPool(1, 0, 0);
        this.soundPool = soundPool;
        this.spConnectingId = soundPool.load(this, R.raw.voip_connecting, 1);
        this.spRingbackID = this.soundPool.load(this, R.raw.voip_ringback, 1);
        this.spFailedID = this.soundPool.load(this, R.raw.voip_failed, 1);
        this.spEndId = this.soundPool.load(this, R.raw.voip_end, 1);
        this.spBusyId = this.soundPool.load(this, R.raw.voip_busy, 1);
        this.spVoiceChatEndId = this.soundPool.load(this, R.raw.voicechat_leave, 1);
        this.spVoiceChatStartId = this.soundPool.load(this, R.raw.voicechat_join, 1);
        this.spVoiceChatConnecting = this.soundPool.load(this, R.raw.voicechat_connecting, 1);
        this.spAllowTalkId = this.soundPool.load(this, R.raw.voip_onallowtalk, 1);
        this.spStartRecordId = this.soundPool.load(this, R.raw.voip_recordstart, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCallUpdated$15() {
        this.soundPool.play(this.spBusyId, 1.0f, 1.0f, 0, -1, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCallUpdated$16() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
        }
        this.spPlayId = this.soundPool.play(this.spRingbackID, 1.0f, 1.0f, 0, -1, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCallUpdated$17() {
        this.timeoutRunnable = null;
        declineIncomingCall(3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnectionStateChanged$87() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
            this.spPlayId = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnectionStateChanged$88() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
        }
        this.spPlayId = this.soundPool.play(this.groupCall != null ? this.spVoiceChatConnecting : this.spConnectingId, 1.0f, 1.0f, 0, -1, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnectionStateChanged$89(int i) {
        if (i == 3 && this.callStartTime == 0) {
            this.callStartTime = SystemClock.elapsedRealtime();
        }
        if (i == 4) {
            callFailed();
            return;
        }
        if (i == 3) {
            Runnable runnable = this.connectingSoundRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.connectingSoundRunnable = null;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda106
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$onConnectionStateChanged$87();
                }
            });
            if (this.groupCall == null && !this.wasEstablished) {
                this.wasEstablished = true;
                if (!this.isProximityNear && !this.privateCall.video) {
                    try {
                        LaunchActivity.getLastFragment().getFragmentView().performHapticFeedback(3, 2);
                    } catch (Exception unused) {
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService.9
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VoIPService.this.tgVoip[0] != null) {
                            StatsController.getInstance(VoIPService.this.currentAccount).incrementTotalCallsTime(VoIPService.this.getStatsNetworkType(), 5);
                            AndroidUtilities.runOnUIThread(this, 5000L);
                        }
                    }
                }, 5000L);
                if (this.isOutgoing) {
                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(getStatsNetworkType(), 0, 1);
                } else {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(getStatsNetworkType(), 0, 1);
                }
            }
        }
        if (i == 5 && !this.isCallEnded) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda107
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$onConnectionStateChanged$88();
                }
            });
        }
        dispatchStateChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onDestroy$67() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didEndCall, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onDestroy$68(AudioManager audioManager) {
        synchronized (sync) {
            try {
                if (setModeRunnable == null) {
                    return;
                }
                setModeRunnable = null;
                try {
                    audioManager.setMode(0);
                } catch (SecurityException e) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("Error setting audio more to normal", e);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDestroy$69() {
        SoundPool soundPool = this.soundPool;
        if (soundPool != null) {
            soundPool.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSignalBarCountChanged$92(int i) {
        this.signalBarCount = i;
        for (int i2 = 0; i2 < this.stateListeners.size(); i2++) {
            this.stateListeners.get(i2).onSignalBarsCountChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartCommand$1() {
        this.delayedStartOutgoingCall = null;
        startOutgoingCall();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartCommand$2() {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.voipServiceCreated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onTgVoipStop$78(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Sent debug logs, response = " + tLObject);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playAllowTalkSound$91() {
        this.soundPool.play(this.spAllowTalkId, 0.5f, 0.5f, 0, 0, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playConnectedSound$58() {
        this.soundPool.play(this.spVoiceChatStartId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playStartRecordSound$90() {
        this.soundPool.play(this.spStartRecordId, 0.5f, 0.5f, 0, 0, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAcceptedCall$18(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error != null) {
            callFailed();
        } else {
            this.privateCall = ((TL_phone.TL_phone_phoneCall) tLObject).phone_call;
            initiateActualEncryptedCall();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processAcceptedCall$19(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$processAcceptedCall$18(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setMicMute$0() {
        if (this.updateNotificationRunnable == null) {
            return;
        }
        this.updateNotificationRunnable = null;
        TLRPC.Chat chat = this.chat;
        showNotification(chat.title, getRoundAvatarBitmap(this, this.currentAccount, chat));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupCaptureDevice$13() {
        this.micSwitching = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startConnectingSound$59() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
        }
        int play = this.soundPool.play(this.spConnectingId, 1.0f, 1.0f, 0, -1, 1.0f);
        this.spPlayId = play;
        if (play == 0) {
            7 r0 = new 7();
            this.connectingSoundRunnable = r0;
            AndroidUtilities.runOnUIThread(r0, 100L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$20(TLRPC.TL_updateGroupCall tL_updateGroupCall) {
        if (sharedInstance == null) {
            return;
        }
        TLRPC.GroupCall groupCall = this.groupCall.call;
        TLRPC.GroupCall groupCall2 = tL_updateGroupCall.call;
        groupCall.access_hash = groupCall2.access_hash;
        groupCall.id = groupCall2.id;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ChatObject.Call call = this.groupCall;
        messagesController.putGroupCall(call.chatId, call);
        startGroupCall(0, null, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$21(TLRPC.TL_error tL_error) {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowAlert, 6, tL_error.text);
        hangUp(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$22(TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$startGroupCall$21(tL_error);
                }
            });
            return;
        }
        try {
            this.groupCallBottomSheetLatch.await(800L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            FileLog.e(e);
        }
        TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        int i = 0;
        while (true) {
            if (i >= updates.updates.size()) {
                break;
            }
            TLRPC.Update update = updates.updates.get(i);
            if (update instanceof TLRPC.TL_updateGroupCall) {
                final TLRPC.TL_updateGroupCall tL_updateGroupCall = (TLRPC.TL_updateGroupCall) update;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda49
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$startGroupCall$20(tL_updateGroupCall);
                    }
                });
                break;
            }
            i++;
        }
        MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startGroupCall$23() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didStartedCall, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$24(int i) {
        this.mySource[0] = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$25(TLRPC.TL_groupCallParticipant tL_groupCallParticipant) {
        this.mySource[0] = tL_groupCallParticipant.source;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$26(boolean z) {
        this.groupCall.loadMembers(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$27(TLRPC.TL_error tL_error) {
        int i;
        int i2;
        if (!"JOIN_AS_PEER_INVALID".equals(tL_error.text)) {
            if ("GROUPCALL_SSRC_DUPLICATE_MUCH".equals(tL_error.text)) {
                createGroupInstance(0, false);
                return;
            }
            if ("GROUPCALL_INVALID".equals(tL_error.text)) {
                MessagesController.getInstance(this.currentAccount).loadFullChat(this.chat.id, 0, true);
            }
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowAlert, 6, tL_error.text);
            hangUp(0);
            return;
        }
        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chat.id);
        if (chatFull != null) {
            if (chatFull instanceof TLRPC.TL_chatFull) {
                i = chatFull.flags;
                i2 = -32769;
            } else {
                i = chatFull.flags;
                i2 = -67108865;
            }
            chatFull.flags = i & i2;
            chatFull.groupcall_default_join_as = null;
            JoinCallAlert.resetCache();
        }
        hangUp(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCall$28(final int i, final boolean z, TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$startGroupCall$27(tL_error);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startGroupCall$24(i);
            }
        });
        TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        long selfId = getSelfId();
        int size = updates.updates.size();
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC.Update update = updates.updates.get(i2);
            if (update instanceof TLRPC.TL_updateGroupCallParticipants) {
                TLRPC.TL_updateGroupCallParticipants tL_updateGroupCallParticipants = (TLRPC.TL_updateGroupCallParticipants) update;
                int size2 = tL_updateGroupCallParticipants.participants.size();
                int i3 = 0;
                while (true) {
                    if (i3 < size2) {
                        final TLRPC.TL_groupCallParticipant tL_groupCallParticipant = tL_updateGroupCallParticipants.participants.get(i3);
                        if (MessageObject.getPeerId(tL_groupCallParticipant.peer) == selfId) {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda52
                                @Override // java.lang.Runnable
                                public final void run() {
                                    VoIPService.this.lambda$startGroupCall$25(tL_groupCallParticipant);
                                }
                            });
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("join source = " + tL_groupCallParticipant.source);
                            }
                        } else {
                            i3++;
                        }
                    }
                }
            } else if (update instanceof TLRPC.TL_updateGroupCallConnection) {
                TLRPC.TL_updateGroupCallConnection tL_updateGroupCallConnection = (TLRPC.TL_updateGroupCallConnection) update;
                if (!tL_updateGroupCallConnection.presentation) {
                    this.myParams = tL_updateGroupCallConnection.params;
                }
            }
        }
        MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startGroupCall$26(z);
            }
        });
        startGroupCheckShortpoll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0097  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$startGroupCheckShortpoll$33(TLObject tLObject, TL_phone.checkGroupCall checkgroupcall, TLRPC.TL_error tL_error) {
        boolean z;
        boolean z2;
        int[] iArr;
        TLRPC.GroupCall groupCall;
        if (this.shortPollRunnable == null || sharedInstance == null || this.groupCall == null) {
            return;
        }
        this.shortPollRunnable = null;
        this.checkRequestId = 0;
        if (!(tLObject instanceof Vector)) {
            if (tL_error == null || tL_error.code != 400) {
                z = false;
                z2 = false;
            } else {
                int i = this.mySource[1];
                if (i == 0 || !checkgroupcall.sources.contains(Integer.valueOf(i))) {
                    z = false;
                    z2 = true;
                } else {
                    z2 = true;
                    z = true;
                }
            }
            if (z2) {
            }
            if (z) {
            }
            iArr = this.mySource;
            if (iArr[1] == 0) {
            }
            startGroupCheckShortpoll();
        }
        ArrayList<Integer> intArray = ((Vector) tLObject).toIntArray();
        int i2 = this.mySource[0];
        z2 = (i2 == 0 || !checkgroupcall.sources.contains(Integer.valueOf(i2)) || intArray.contains(Integer.valueOf(this.mySource[0]))) ? false : true;
        int i3 = this.mySource[1];
        if (i3 == 0 || !checkgroupcall.sources.contains(Integer.valueOf(i3)) || intArray.contains(Integer.valueOf(this.mySource[1]))) {
            z = false;
            if (z2) {
                createGroupInstance(0, false);
            }
            if (z) {
                createGroupInstance(1, false);
            }
            iArr = this.mySource;
            if (iArr[1] == 0 || iArr[0] != 0 || ((groupCall = this.groupCall.call) != null && groupCall.rtmp_stream)) {
                startGroupCheckShortpoll();
            }
            return;
        }
        z = true;
        if (z2) {
        }
        if (z) {
        }
        iArr = this.mySource;
        if (iArr[1] == 0) {
        }
        startGroupCheckShortpoll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCheckShortpoll$34(final TL_phone.checkGroupCall checkgroupcall, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda117
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startGroupCheckShortpoll$33(tLObject, checkgroupcall, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGroupCheckShortpoll$35() {
        ChatObject.Call call;
        TLRPC.GroupCall groupCall;
        if (this.shortPollRunnable == null || sharedInstance == null || (call = this.groupCall) == null) {
            return;
        }
        int[] iArr = this.mySource;
        int i = 0;
        if (iArr[0] == 0 && iArr[1] == 0 && ((groupCall = call.call) == null || !groupCall.rtmp_stream)) {
            return;
        }
        final TL_phone.checkGroupCall checkgroupcall = new TL_phone.checkGroupCall();
        checkgroupcall.call = this.groupCall.getInputGroupCall();
        while (true) {
            int[] iArr2 = this.mySource;
            if (i >= iArr2.length) {
                this.checkRequestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(checkgroupcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda41
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        VoIPService.this.lambda$startGroupCheckShortpoll$34(checkgroupcall, tLObject, tL_error);
                    }
                });
                return;
            }
            int i2 = iArr2[i];
            if (i2 != 0) {
                checkgroupcall.sources.add(Integer.valueOf(i2));
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutgoingCall$10(MessagesStorage messagesStorage, TLObject tLObject, TLRPC.TL_error tL_error) {
        this.callReqId = 0;
        if (this.endCallAfterRequest) {
            callEnded();
            return;
        }
        if (tL_error != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error on getDhConfig " + tL_error);
            }
            callFailed();
            return;
        }
        TLRPC.messages_DhConfig messages_dhconfig = (TLRPC.messages_DhConfig) tLObject;
        if (tLObject instanceof TLRPC.TL_messages_dhConfig) {
            if (!Utilities.isGoodPrime(messages_dhconfig.p, messages_dhconfig.g)) {
                callFailed();
                return;
            }
            messagesStorage.setSecretPBytes(messages_dhconfig.p);
            messagesStorage.setSecretG(messages_dhconfig.g);
            messagesStorage.setLastSecretVersion(messages_dhconfig.version);
            messagesStorage.saveSecretParams(messagesStorage.getLastSecretVersion(), messagesStorage.getSecretG(), messagesStorage.getSecretPBytes());
        }
        final byte[] bArr = new byte[256];
        for (int i = 0; i < 256; i++) {
            bArr[i] = (byte) (((byte) (Utilities.random.nextDouble() * 256.0d)) ^ messages_dhconfig.random[i]);
        }
        byte[] byteArray = BigInteger.valueOf(messagesStorage.getSecretG()).modPow(new BigInteger(1, bArr), new BigInteger(1, messagesStorage.getSecretPBytes())).toByteArray();
        if (byteArray.length > 256) {
            byte[] bArr2 = new byte[256];
            System.arraycopy(byteArray, 1, bArr2, 0, 256);
            byteArray = bArr2;
        }
        TL_phone.requestCall requestcall = new TL_phone.requestCall();
        requestcall.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(this.user);
        TL_phone.TL_phoneCallProtocol tL_phoneCallProtocol = new TL_phone.TL_phoneCallProtocol();
        requestcall.protocol = tL_phoneCallProtocol;
        requestcall.video = this.videoCall;
        tL_phoneCallProtocol.udp_p2p = true;
        tL_phoneCallProtocol.udp_reflector = true;
        tL_phoneCallProtocol.min_layer = 65;
        tL_phoneCallProtocol.max_layer = Instance.getConnectionMaxLayer();
        requestcall.protocol.library_versions.addAll(Instance.AVAILABLE_VERSIONS);
        this.g_a = byteArray;
        requestcall.g_a_hash = Utilities.computeSHA256(byteArray, 0, byteArray.length);
        requestcall.random_id = Utilities.random.nextInt();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(requestcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda102
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                VoIPService.this.lambda$startOutgoingCall$9(bArr, tLObject2, tL_error2);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startOutgoingCall$5() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didStartedCall, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutgoingCall$6(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (BuildVars.LOGS_ENABLED) {
            if (tL_error != null) {
                FileLog.e("error on phone.discardCall: " + tL_error);
            } else {
                FileLog.d("phone.discardCall " + tLObject);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda116
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.callFailed();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutgoingCall$7() {
        this.timeoutRunnable = null;
        TL_phone.discardCall discardcall = new TL_phone.discardCall();
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        discardcall.peer = tL_inputPhoneCall;
        TL_phone.PhoneCall phoneCall = this.privateCall;
        tL_inputPhoneCall.access_hash = phoneCall.access_hash;
        tL_inputPhoneCall.id = phoneCall.id;
        discardcall.reason = new TLRPC.TL_phoneCallDiscardReasonMissed();
        FileLog.e("discardCall " + discardcall.reason);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(discardcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda58
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$startOutgoingCall$6(tLObject, tL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutgoingCall$8(TLRPC.TL_error tL_error, TLObject tLObject, byte[] bArr) {
        String str;
        if (tL_error == null) {
            this.privateCall = ((TL_phone.TL_phone_phoneCall) tLObject).phone_call;
            this.a_or_b = bArr;
            dispatchStateChanged(13);
            if (this.endCallAfterRequest) {
                hangUp();
                return;
            }
            if (this.pendingUpdates.size() > 0 && this.privateCall != null) {
                Iterator<TL_phone.PhoneCall> it = this.pendingUpdates.iterator();
                while (it.hasNext()) {
                    onCallUpdated(it.next());
                }
                this.pendingUpdates.clear();
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$startOutgoingCall$7();
                }
            };
            this.timeoutRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, MessagesController.getInstance(this.currentAccount).callReceiveTimeout);
            return;
        }
        if (tL_error.code == 400 && "PARTICIPANT_VERSION_OUTDATED".equals(tL_error.text)) {
            str = Instance.ERROR_PEER_OUTDATED;
        } else {
            int i = tL_error.code;
            if (i == 403) {
                str = Instance.ERROR_PRIVACY;
            } else {
                if (i != 406) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("Error on phone.requestCall: " + tL_error);
                    }
                    callFailed();
                    return;
                }
                str = Instance.ERROR_LOCALIZED;
            }
        }
        callFailed(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOutgoingCall$9(final byte[] bArr, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda84
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startOutgoingCall$8(tL_error, tLObject, bArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRingtoneAndVibration$66(MediaPlayer mediaPlayer) {
        try {
            this.ringtonePlayer.start();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startScreenCapture$29(int i) {
        this.mySource[1] = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startScreenCapture$30(TLRPC.Updates updates) {
        if (this.tgVoip[1] != null) {
            long selfId = getSelfId();
            int size = updates.updates.size();
            for (int i = 0; i < size; i++) {
                TLRPC.Update update = updates.updates.get(i);
                if (update instanceof TLRPC.TL_updateGroupCallConnection) {
                    TLRPC.TL_updateGroupCallConnection tL_updateGroupCallConnection = (TLRPC.TL_updateGroupCallConnection) update;
                    if (tL_updateGroupCallConnection.presentation) {
                        this.tgVoip[1].setJoinResponsePayload(tL_updateGroupCallConnection.params.data);
                    }
                } else if (update instanceof TLRPC.TL_updateGroupCallParticipants) {
                    TLRPC.TL_updateGroupCallParticipants tL_updateGroupCallParticipants = (TLRPC.TL_updateGroupCallParticipants) update;
                    int size2 = tL_updateGroupCallParticipants.participants.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 < size2) {
                            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = tL_updateGroupCallParticipants.participants.get(i2);
                            if (MessageObject.getPeerId(tL_groupCallParticipant.peer) == selfId) {
                                TLRPC.TL_groupCallParticipantVideo tL_groupCallParticipantVideo = tL_groupCallParticipant.presentation;
                                if (tL_groupCallParticipantVideo != null) {
                                    if ((tL_groupCallParticipantVideo.flags & 2) != 0) {
                                        this.mySource[1] = tL_groupCallParticipantVideo.audio_source;
                                    } else {
                                        int size3 = tL_groupCallParticipantVideo.source_groups.size();
                                        for (int i3 = 0; i3 < size3; i3++) {
                                            TLRPC.TL_groupCallParticipantVideoSourceGroup tL_groupCallParticipantVideoSourceGroup = tL_groupCallParticipant.presentation.source_groups.get(i3);
                                            if (tL_groupCallParticipantVideoSourceGroup.sources.size() > 0) {
                                                this.mySource[1] = tL_groupCallParticipantVideoSourceGroup.sources.get(0).intValue();
                                            }
                                        }
                                    }
                                }
                            } else {
                                i2++;
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startScreenCapture$31(TLRPC.TL_error tL_error) {
        int i;
        int i2;
        if ("GROUPCALL_VIDEO_TOO_MUCH".equals(tL_error.text)) {
            this.groupCall.reloadGroupCall();
            return;
        }
        if (!"JOIN_AS_PEER_INVALID".equals(tL_error.text)) {
            if ("GROUPCALL_SSRC_DUPLICATE_MUCH".equals(tL_error.text)) {
                createGroupInstance(1, false);
                return;
            } else {
                if ("GROUPCALL_INVALID".equals(tL_error.text)) {
                    MessagesController.getInstance(this.currentAccount).loadFullChat(this.chat.id, 0, true);
                    return;
                }
                return;
            }
        }
        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chat.id);
        if (chatFull != null) {
            if (chatFull instanceof TLRPC.TL_chatFull) {
                i = chatFull.flags;
                i2 = -32769;
            } else {
                i = chatFull.flags;
                i2 = -67108865;
            }
            chatFull.flags = i & i2;
            chatFull.groupcall_default_join_as = null;
            JoinCallAlert.resetCache();
        }
        hangUp(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startScreenCapture$32(final int i, TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda101
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$startScreenCapture$31(tL_error);
                }
            });
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda99
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startScreenCapture$29(i);
            }
        });
        final TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda100
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startScreenCapture$30(updates);
            }
        });
        MessagesController.getInstance(this.currentAccount).processUpdates(updates, false);
        startGroupCheckShortpoll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopScreenCapture$14(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchToSpeaker$61(Boolean bool, Boolean bool2) {
        updateOutputGainControlState();
        Iterator<StateListener> it = this.stateListeners.iterator();
        while (it.hasNext()) {
            it.next().onAudioSettingsChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchToSpeaker$62() {
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        if ((isBluetoothHeadsetConnected() && hasEarpiece()) || this.isHeadsetPlugged || isSpeakerphoneOn()) {
            return;
        }
        voipAudioManager.setSpeakerphoneOn(true);
        voipAudioManager.isBluetoothAndSpeakerOnAsync(new Utilities.Callback2() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda87
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                VoIPService.this.lambda$switchToSpeaker$61((Boolean) obj, (Boolean) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleSpeakerphoneOrShowRouteSheet$63(DialogInterface dialogInterface, int i) {
        if (getSharedInstance() == null) {
            return;
        }
        setAudioOutput(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toggleSpeakerphoneOrShowRouteSheet$64(BottomSheet bottomSheet, Integer num, DialogInterface dialogInterface) {
        for (int i = 0; i < bottomSheet.getItemViews().size(); i++) {
            int i2 = Theme.key_dialogTextBlack;
            bottomSheet.setItemColor(i, Theme.getColor(i2), Theme.getColor(i2));
        }
        if (num != null) {
            int color = Theme.getColor(Theme.key_dialogTextLink);
            bottomSheet.setItemColor(num.intValue(), color, color);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleSpeakerphoneOrShowRouteSheet$65(Boolean bool, Boolean bool2) {
        updateOutputGainControlState();
        Iterator<StateListener> it = this.stateListeners.iterator();
        while (it.hasNext()) {
            it.next().onAudioSettingsChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateBluetoothHeadsetState$83(AudioManager audioManager) {
        try {
            audioManager.startBluetoothSco();
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionState$51(int i) {
        if (this.switchingStreamTimeoutRunnable == null) {
            return;
        }
        this.switchingStream = false;
        lambda$createGroupInstance$50(i, 0, true);
        this.switchingStreamTimeoutRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionState$52() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
        }
        this.spPlayId = this.soundPool.play(this.spVoiceChatConnecting, 1.0f, 1.0f, 0, -1, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionState$53() {
        int i = this.spPlayId;
        if (i != 0) {
            this.soundPool.stop(i);
            this.spPlayId = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerConfig$77(SharedPreferences sharedPreferences, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            String str = ((TLRPC.TL_dataJSON) tLObject).data;
            Instance.setGlobalServerConfig(str);
            sharedPreferences.edit().putString("voip_server_config", str).commit();
        }
    }

    private void loadResources() {
        if (Build.VERSION.SDK_INT >= 21) {
            WebRtcAudioTrack.setAudioTrackUsageAttribute(2);
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$loadResources$79();
            }
        });
    }

    private void onTgVoipPreStop() {
    }

    private void onTgVoipStop(Instance.FinalState finalState) {
        if (this.user == null) {
            return;
        }
        if (TextUtils.isEmpty(finalState.debugLog)) {
            try {
                finalState.debugLog = getStringFromFile(VoIPHelper.getLogFilePath(this.privateCall.id, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!this.needSendDebugLog || finalState.debugLog == null) {
            return;
        }
        TL_phone.saveCallDebug savecalldebug = new TL_phone.saveCallDebug();
        TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
        savecalldebug.debug = tL_dataJSON;
        tL_dataJSON.data = finalState.debugLog;
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        savecalldebug.peer = tL_inputPhoneCall;
        TL_phone.PhoneCall phoneCall = this.privateCall;
        tL_inputPhoneCall.access_hash = phoneCall.access_hash;
        tL_inputPhoneCall.id = phoneCall.id;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(savecalldebug, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda46
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.lambda$onTgVoipStop$78(tLObject, tL_error);
            }
        });
        this.needSendDebugLog = false;
    }

    private void processAcceptedCall() {
        byte[] bArr;
        dispatchStateChanged(12);
        BigInteger bigInteger = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
        BigInteger bigInteger2 = new BigInteger(1, this.privateCall.g_b);
        if (!Utilities.isGoodGaAndGb(bigInteger2, bigInteger)) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("stopping VoIP service, bad Ga and Gb");
            }
            callFailed();
            return;
        }
        byte[] byteArray = bigInteger2.modPow(new BigInteger(1, this.a_or_b), bigInteger).toByteArray();
        if (byteArray.length <= 256) {
            if (byteArray.length < 256) {
                bArr = new byte[256];
                System.arraycopy(byteArray, 0, bArr, 256 - byteArray.length, byteArray.length);
                for (int i = 0; i < 256 - byteArray.length; i++) {
                    bArr[i] = 0;
                }
            }
            byte[] computeSHA1 = Utilities.computeSHA1(byteArray);
            byte[] bArr2 = new byte[8];
            System.arraycopy(computeSHA1, computeSHA1.length - 8, bArr2, 0, 8);
            long bytesToLong = Utilities.bytesToLong(bArr2);
            this.authKey = byteArray;
            this.keyFingerprint = bytesToLong;
            TL_phone.confirmCall confirmcall = new TL_phone.confirmCall();
            confirmcall.g_a = this.g_a;
            confirmcall.key_fingerprint = bytesToLong;
            TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
            confirmcall.peer = tL_inputPhoneCall;
            TL_phone.PhoneCall phoneCall = this.privateCall;
            tL_inputPhoneCall.id = phoneCall.id;
            tL_inputPhoneCall.access_hash = phoneCall.access_hash;
            TL_phone.TL_phoneCallProtocol tL_phoneCallProtocol = new TL_phone.TL_phoneCallProtocol();
            confirmcall.protocol = tL_phoneCallProtocol;
            tL_phoneCallProtocol.max_layer = Instance.getConnectionMaxLayer();
            TL_phone.TL_phoneCallProtocol tL_phoneCallProtocol2 = confirmcall.protocol;
            tL_phoneCallProtocol2.min_layer = 65;
            tL_phoneCallProtocol2.udp_reflector = true;
            tL_phoneCallProtocol2.udp_p2p = true;
            tL_phoneCallProtocol2.library_versions.addAll(Instance.AVAILABLE_VERSIONS);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(confirmcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda63
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    VoIPService.this.lambda$processAcceptedCall$19(tLObject, tL_error);
                }
            });
        }
        bArr = new byte[256];
        System.arraycopy(byteArray, byteArray.length - 256, bArr, 0, 256);
        byteArray = bArr;
        byte[] computeSHA12 = Utilities.computeSHA1(byteArray);
        byte[] bArr22 = new byte[8];
        System.arraycopy(computeSHA12, computeSHA12.length - 8, bArr22, 0, 8);
        long bytesToLong2 = Utilities.bytesToLong(bArr22);
        this.authKey = byteArray;
        this.keyFingerprint = bytesToLong2;
        TL_phone.confirmCall confirmcall2 = new TL_phone.confirmCall();
        confirmcall2.g_a = this.g_a;
        confirmcall2.key_fingerprint = bytesToLong2;
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall2 = new TLRPC.TL_inputPhoneCall();
        confirmcall2.peer = tL_inputPhoneCall2;
        TL_phone.PhoneCall phoneCall2 = this.privateCall;
        tL_inputPhoneCall2.id = phoneCall2.id;
        tL_inputPhoneCall2.access_hash = phoneCall2.access_hash;
        TL_phone.TL_phoneCallProtocol tL_phoneCallProtocol3 = new TL_phone.TL_phoneCallProtocol();
        confirmcall2.protocol = tL_phoneCallProtocol3;
        tL_phoneCallProtocol3.max_layer = Instance.getConnectionMaxLayer();
        TL_phone.TL_phoneCallProtocol tL_phoneCallProtocol22 = confirmcall2.protocol;
        tL_phoneCallProtocol22.min_layer = 65;
        tL_phoneCallProtocol22.udp_reflector = true;
        tL_phoneCallProtocol22.udp_p2p = true;
        tL_phoneCallProtocol22.library_versions.addAll(Instance.AVAILABLE_VERSIONS);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(confirmcall2, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda63
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$processAcceptedCall$19(tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00e1  */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showIncomingNotification(String str, TLObject tLObject, boolean z, int i) {
        int i2;
        int i3;
        Notification notification;
        Person.Builder name;
        Icon createWithAdaptiveBitmap;
        Person.Builder icon;
        Person build;
        Notification.CallStyle forIncomingCall;
        NotificationChannel notificationChannel;
        NotificationChannel notificationChannel2;
        NotificationChannel notificationChannel3;
        boolean z2;
        AudioAttributes.Builder contentType;
        AudioAttributes.Builder legacyStreamType;
        AudioAttributes.Builder usage;
        AudioAttributes build2;
        int importance;
        Uri sound;
        String id;
        String id2;
        Intent intent = new Intent(this, (Class<?>) LaunchActivity.class);
        intent.setAction("voip");
        Notification.Builder contentIntent = new Notification.Builder(this).setContentTitle(LocaleController.getString(z ? R.string.VoipInVideoCallBranding : R.string.VoipInCallBranding)).setSmallIcon(R.drawable.ic_call).setContentIntent(PendingIntent.getActivity(this, 0, intent, ConnectionsManager.FileTypeVideo));
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 26) {
            SharedPreferences globalNotificationsSettings = MessagesController.getGlobalNotificationsSettings();
            int i5 = globalNotificationsSettings.getInt("calls_notification_channel", 0);
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
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
                                stopSelf();
                                return;
                            }
                        }
                        contentIntent.setChannelId("incoming_calls4" + i5);
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
        } else if (i4 >= 21) {
            contentIntent.setSound(null);
        }
        Intent intent2 = new Intent(this, (Class<?>) VoIPActionsReceiver.class);
        intent2.setAction(getPackageName() + ".DECLINE_CALL");
        intent2.putExtra("call_id", getCallID());
        int i6 = R.string.VoipDeclineCall;
        String string = LocaleController.getString(i6);
        int i7 = Build.VERSION.SDK_INT;
        if (i7 < 24 || i7 >= 31) {
            i2 = 0;
        } else {
            SpannableString spannableString = new SpannableString(string);
            i2 = 0;
            spannableString.setSpan(new ForegroundColorSpan(-769226), 0, spannableString.length(), 0);
            string = spannableString;
        }
        PendingIntent broadcast = PendingIntent.getBroadcast(this, i2, intent2, 301989888);
        Intent intent3 = new Intent(this, (Class<?>) VoIPActionsReceiver.class);
        intent3.setAction(getPackageName() + ".ANSWER_CALL");
        intent3.putExtra("call_id", getCallID());
        int i8 = R.string.VoipAnswerCall;
        String string2 = LocaleController.getString(i8);
        if (i7 < 24 || i7 >= 31) {
            i3 = 0;
        } else {
            SpannableString spannableString2 = new SpannableString(string2);
            i3 = 0;
            spannableString2.setSpan(new ForegroundColorSpan(-16733696), 0, spannableString2.length(), 0);
            string2 = spannableString2;
        }
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this, i3, intent3, 301989888);
        contentIntent.setPriority(2);
        contentIntent.setShowWhen(i3);
        if (i7 >= 21) {
            contentIntent.setColor(-13851168);
            contentIntent.setVibrate(new long[i3]);
            contentIntent.setCategory("call");
            contentIntent.setFullScreenIntent(PendingIntent.getActivity(this, i3, intent, ConnectionsManager.FileTypeVideo), true);
            if (tLObject instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) tLObject;
                if (!TextUtils.isEmpty(user.phone)) {
                    contentIntent.addPerson("tel:" + user.phone);
                }
            }
        }
        if (i7 >= 31) {
            Bitmap roundAvatarBitmap = getRoundAvatarBitmap(this, this.currentAccount, tLObject);
            String formatName = ContactsController.formatName(tLObject);
            if (TextUtils.isEmpty(formatName)) {
                formatName = "___";
            }
            name = new Person.Builder().setName(formatName);
            createWithAdaptiveBitmap = Icon.createWithAdaptiveBitmap(roundAvatarBitmap);
            icon = name.setIcon(createWithAdaptiveBitmap);
            build = icon.build();
            forIncomingCall = Notification.CallStyle.forIncomingCall(build, broadcast, broadcast2);
            contentIntent.setStyle(forIncomingCall);
            notification = contentIntent.build();
        } else if (i7 >= 21) {
            contentIntent.addAction(R.drawable.ic_call_end_white_24dp, string, broadcast);
            contentIntent.addAction(R.drawable.ic_call, string2, broadcast2);
            contentIntent.setContentText(str);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), LocaleController.isRTL ? R.layout.call_notification_rtl : R.layout.call_notification);
            remoteViews.setTextViewText(R.id.name, str);
            remoteViews.setViewVisibility(R.id.subtitle, 8);
            if (UserConfig.getActivatedAccountsCount() > 1) {
                TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                remoteViews.setTextViewText(R.id.title, z ? LocaleController.formatString("VoipInVideoCallBrandingWithName", R.string.VoipInVideoCallBrandingWithName, ContactsController.formatName(currentUser.first_name, currentUser.last_name)) : LocaleController.formatString("VoipInCallBrandingWithName", R.string.VoipInCallBrandingWithName, ContactsController.formatName(currentUser.first_name, currentUser.last_name)));
            } else {
                remoteViews.setTextViewText(R.id.title, LocaleController.getString(z ? R.string.VoipInVideoCallBranding : R.string.VoipInCallBranding));
            }
            Bitmap roundAvatarBitmap2 = getRoundAvatarBitmap(this, this.currentAccount, tLObject);
            remoteViews.setTextViewText(R.id.answer_text, LocaleController.getString(i8));
            remoteViews.setTextViewText(R.id.decline_text, LocaleController.getString(i6));
            remoteViews.setImageViewBitmap(R.id.photo, roundAvatarBitmap2);
            remoteViews.setOnClickPendingIntent(R.id.answer_btn, broadcast2);
            remoteViews.setOnClickPendingIntent(R.id.decline_btn, broadcast);
            contentIntent.setLargeIcon(roundAvatarBitmap2);
            notification = contentIntent.getNotification();
            notification.bigContentView = remoteViews;
            notification.headsUpContentView = remoteViews;
        } else {
            contentIntent.setContentText(str);
            contentIntent.addAction(R.drawable.ic_call_end_white_24dp, string, broadcast);
            contentIntent.addAction(R.drawable.ic_call, string2, broadcast2);
            notification = contentIntent.getNotification();
        }
        this.foregroundStarted = true;
        this.foregroundId = 202;
        this.foregroundNotification = notification;
        if (i7 >= 33) {
            int currentForegroundType = getCurrentForegroundType();
            this.lastForegroundType = currentForegroundType;
            startForeground(202, notification, currentForegroundType);
        } else {
            startForeground(202, notification);
        }
        startRingtoneAndVibration();
    }

    private void showNotification() {
        TLRPC.User user = this.user;
        if (user != null) {
            showNotification(ContactsController.formatName(user.first_name, user.last_name), getRoundAvatarBitmap(this, this.currentAccount, this.user));
        } else {
            TLRPC.Chat chat = this.chat;
            showNotification(chat.title, getRoundAvatarBitmap(this, this.currentAccount, chat));
        }
    }

    private void showNotification(String str, Bitmap bitmap) {
        int i;
        int i2;
        Intent action = new Intent(this, (Class<?>) LaunchActivity.class).setAction(this.groupCall != null ? "voip_chat" : "voip");
        if (this.groupCall != null) {
            action.putExtra("currentAccount", this.currentAccount);
        }
        Notification.Builder contentIntent = new Notification.Builder(this).setContentText(str).setContentIntent(PendingIntent.getActivity(this, 50, action, ConnectionsManager.FileTypeVideo));
        if (this.groupCall != null) {
            contentIntent.setContentTitle(LocaleController.getString(ChatObject.isChannelOrGiga(this.chat) ? R.string.VoipLiveStream : R.string.VoipVoiceChat));
            contentIntent.setSmallIcon(isMicMute() ? R.drawable.voicechat_muted : R.drawable.voicechat_active);
        } else {
            contentIntent.setContentTitle(LocaleController.getString(R.string.VoipOutgoingCall));
            contentIntent.setSmallIcon(R.drawable.ic_call);
            contentIntent.setOngoing(true);
        }
        int i3 = Build.VERSION.SDK_INT;
        Intent intent = new Intent(this, (Class<?>) VoIPActionsReceiver.class);
        intent.setAction(getPackageName() + ".END_CALL");
        if (this.groupCall != null) {
            i = R.drawable.ic_call_end_white_24dp;
            i2 = ChatObject.isChannelOrGiga(this.chat) ? R.string.VoipChannelLeaveAlertTitle : R.string.VoipGroupLeaveAlertTitle;
        } else {
            i = R.drawable.ic_call_end_white_24dp;
            i2 = R.string.VoipEndCall;
        }
        contentIntent.addAction(i, LocaleController.getString(i2), PendingIntent.getBroadcast(this, 0, intent, 167772160));
        contentIntent.setPriority(2);
        contentIntent.setShowWhen(false);
        if (i3 >= 26) {
            contentIntent.setColor(-14143951);
            contentIntent.setColorized(true);
        } else if (i3 >= 21) {
            contentIntent.setColor(-13851168);
        }
        if (i3 >= 26) {
            NotificationsController.checkOtherNotificationsChannel();
            contentIntent.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
        }
        if (bitmap != null) {
            contentIntent.setLargeIcon(bitmap);
        }
        try {
            if (this.foregroundStarted) {
                try {
                    stopForeground(true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            this.foregroundStarted = true;
            if (Build.VERSION.SDK_INT < 33) {
                this.foregroundId = 201;
                Notification notification = contentIntent.getNotification();
                this.foregroundNotification = notification;
                startForeground(201, notification);
                return;
            }
            this.foregroundId = 201;
            Notification notification2 = contentIntent.getNotification();
            this.foregroundNotification = notification2;
            int currentForegroundType = getCurrentForegroundType();
            this.lastForegroundType = currentForegroundType;
            startForeground(201, notification2, currentForegroundType);
        } catch (Exception e2) {
            if (bitmap == null || !(e2 instanceof IllegalArgumentException)) {
                return;
            }
            showNotification(str, null);
        }
    }

    private void startConnectingSound() {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda91
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startConnectingSound$59();
            }
        });
    }

    private void startGroupCall(final int i, String str, final boolean z) {
        if (sharedInstance != this) {
            return;
        }
        if (this.createGroupCall) {
            ChatObject.Call call = new ChatObject.Call();
            this.groupCall = call;
            call.call = new TLRPC.TL_groupCall();
            ChatObject.Call call2 = this.groupCall;
            TLRPC.GroupCall groupCall = call2.call;
            groupCall.participants_count = 0;
            groupCall.version = 1;
            groupCall.can_start_video = true;
            groupCall.can_change_join_muted = true;
            groupCall.rtmp_stream = this.isRtmpStream;
            call2.chatId = this.chat.id;
            call2.currentAccount = AccountInstance.getInstance(this.currentAccount);
            this.groupCall.setSelfPeer(this.groupCallPeer);
            this.groupCall.createNoVideoParticipant();
            dispatchStateChanged(6);
            TL_phone.createGroupCall creategroupcall = new TL_phone.createGroupCall();
            creategroupcall.peer = MessagesController.getInputPeer(this.chat);
            creategroupcall.random_id = Utilities.random.nextInt();
            int i2 = this.scheduleDate;
            if (i2 != 0) {
                creategroupcall.schedule_date = i2;
                creategroupcall.flags |= 2;
            }
            if (this.isRtmpStream) {
                creategroupcall.flags |= 4;
            }
            this.groupCallBottomSheetLatch = new CountDownLatch(1);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(creategroupcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda69
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    VoIPService.this.lambda$startGroupCall$22(tLObject, tL_error);
                }
            }, 2);
            this.createGroupCall = false;
            return;
        }
        if (str == null) {
            if (this.groupCall == null) {
                ChatObject.Call groupCall2 = MessagesController.getInstance(this.currentAccount).getGroupCall(this.chat.id, false);
                this.groupCall = groupCall2;
                if (groupCall2 != null) {
                    groupCall2.setSelfPeer(this.groupCallPeer);
                }
            }
            configureDeviceForCall();
            showNotification();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda70
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.lambda$startGroupCall$23();
                }
            });
            createGroupInstance(0, false);
            return;
        }
        if (getSharedInstance() == null || this.groupCall == null) {
            return;
        }
        dispatchStateChanged(1);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("initital source = " + i);
        }
        TL_phone.joinGroupCall joingroupcall = new TL_phone.joinGroupCall();
        joingroupcall.muted = true;
        joingroupcall.video_stopped = this.videoState[0] != 2;
        joingroupcall.call = this.groupCall.getInputGroupCall();
        TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
        joingroupcall.params = tL_dataJSON;
        tL_dataJSON.data = str;
        if (!TextUtils.isEmpty(this.joinHash)) {
            joingroupcall.invite_hash = this.joinHash;
            joingroupcall.flags |= 2;
        }
        TLRPC.InputPeer inputPeer = this.groupCallPeer;
        if (inputPeer != null) {
            joingroupcall.join_as = inputPeer;
        } else {
            TLRPC.TL_inputPeerUser tL_inputPeerUser = new TLRPC.TL_inputPeerUser();
            joingroupcall.join_as = tL_inputPeerUser;
            tL_inputPeerUser.user_id = AccountInstance.getInstance(this.currentAccount).getUserConfig().getClientUserId();
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(joingroupcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda71
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$startGroupCall$28(i, z, tLObject, tL_error);
            }
        });
    }

    private void startGroupCheckShortpoll() {
        ChatObject.Call call;
        TLRPC.GroupCall groupCall;
        if (this.shortPollRunnable != null || sharedInstance == null || (call = this.groupCall) == null) {
            return;
        }
        int[] iArr = this.mySource;
        if (iArr[0] == 0 && iArr[1] == 0 && ((groupCall = call.call) == null || !groupCall.rtmp_stream)) {
            return;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda68
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$startGroupCheckShortpoll$35();
            }
        };
        this.shortPollRunnable = runnable;
        AndroidUtilities.runOnUIThread(runnable, 4000L);
    }

    private void startOutgoingCall() {
        CallConnection callConnection;
        if (USE_CONNECTION_SERVICE && (callConnection = this.systemCallConnection) != null) {
            callConnection.setDialing();
        }
        configureDeviceForCall();
        showNotification();
        startConnectingSound();
        dispatchStateChanged(14);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.lambda$startOutgoingCall$5();
            }
        });
        Utilities.random.nextBytes(new byte[256]);
        TLRPC.TL_messages_getDhConfig tL_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tL_messages_getDhConfig.random_length = 256;
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        tL_messages_getDhConfig.version = messagesStorage.getLastSecretVersion();
        this.callReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getDhConfig, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda67
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$startOutgoingCall$10(messagesStorage, tLObject, tL_error);
            }
        }, 2);
    }

    private void startRatingActivity() {
        try {
            PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) VoIPFeedbackActivity.class).putExtra("call_id", this.privateCall.id).putExtra("call_access_hash", this.privateCall.access_hash).putExtra("call_video", this.privateCall.video).putExtra("account", this.currentAccount).addFlags(805306368), ConnectionsManager.FileTypeVideo).send();
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error starting incall activity", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRinging() {
        CallConnection callConnection;
        if (this.currentState == 15) {
            return;
        }
        if (USE_CONNECTION_SERVICE && (callConnection = this.systemCallConnection) != null) {
            callConnection.setRinging();
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("starting ringing for call " + this.privateCall.id);
        }
        dispatchStateChanged(15);
        if (!this.notificationsDisabled && Build.VERSION.SDK_INT >= 21) {
            TLRPC.User user = this.user;
            showIncomingNotification(ContactsController.formatName(user.first_name, user.last_name), this.user, this.privateCall.video, 0);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Showing incoming call notification");
                return;
            }
            return;
        }
        startRingtoneAndVibration(this.user.id);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Starting incall activity for incoming call");
        }
        try {
            PendingIntent.getActivity(this, 12345, new Intent(this, (Class<?>) LaunchActivity.class).setAction("voip"), ConnectionsManager.FileTypeVideo).send();
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error starting incall activity", e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00f6 A[Catch: all -> 0x0026, TryCatch #0 {all -> 0x0026, Exception -> 0x0086, blocks: (B:10:0x0020, B:12:0x0024, B:14:0x0029, B:16:0x0041, B:19:0x005c, B:21:0x0073, B:22:0x008a, B:24:0x0090, B:26:0x00ab, B:27:0x00df, B:29:0x00f6, B:30:0x010b, B:33:0x0114, B:35:0x011a, B:37:0x0128, B:40:0x013f, B:41:0x014e, B:48:0x0122, B:52:0x0095, B:54:0x0099, B:57:0x00a6, B:61:0x00d3, B:63:0x00da, B:64:0x0047, B:66:0x0050, B:69:0x0059), top: B:9:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0108  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startRingtoneAndVibration(long j) {
        String str;
        int i;
        String str2;
        Uri uri;
        Uri parse;
        boolean z;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        if (audioManager.getRingerMode() == 0 || this.ringtonePlayer != null) {
            return;
        }
        synchronized (sync) {
            try {
            } catch (Exception e) {
                FileLog.e(e);
                MediaPlayer mediaPlayer = this.ringtonePlayer;
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    this.ringtonePlayer = null;
                }
            } finally {
            }
            if (this.ringtonePlayer != null) {
                return;
            }
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.ringtonePlayer = mediaPlayer2;
            mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda59
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer3) {
                    VoIPService.this.lambda$startRingtoneAndVibration$66(mediaPlayer3);
                }
            });
            this.ringtonePlayer.setLooping(true);
            if (this.isHeadsetPlugged) {
                this.ringtonePlayer.setAudioStreamType(0);
            } else {
                this.ringtonePlayer.setAudioStreamType(2);
                if (!USE_CONNECTION_SERVICE) {
                    this.hasAudioFocus = audioManager.requestAudioFocus(this, 2, 2) == 1;
                }
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
                this.ringtonePlayer.setDataSource(this, parse);
                this.ringtonePlayer.prepareAsync();
                if (notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                    str = "vibrate_calls";
                } else {
                    str = "calls_vibrate_" + j;
                }
                i = notificationsSettings.getInt(str, 0);
                if ((i != 2 && i != 4 && (audioManager.getRingerMode() == 1 || audioManager.getRingerMode() == 2)) || (i == 4 && audioManager.getRingerMode() == 1)) {
                    Vibrator vibrator = (Vibrator) getSystemService("vibrator");
                    this.vibrator = vibrator;
                    vibrator.vibrate(new long[]{0, i == 1 ? 350L : i == 3 ? 1400L : 700L, 500}, 0);
                }
            }
            parse = RingtoneManager.getDefaultUri(1);
            z = true;
            FileLog.d("start ringtone with " + z + " " + parse);
            this.ringtonePlayer.setDataSource(this, parse);
            this.ringtonePlayer.prepareAsync();
            if (notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
            }
            i = notificationsSettings.getInt(str, 0);
            if (i != 2) {
                Vibrator vibrator2 = (Vibrator) getSystemService("vibrator");
                this.vibrator = vibrator2;
                vibrator2.vibrate(new long[]{0, i == 1 ? 350L : i == 3 ? 1400L : 700L, 500}, 0);
            }
            Vibrator vibrator22 = (Vibrator) getSystemService("vibrator");
            this.vibrator = vibrator22;
            vibrator22.vibrate(new long[]{0, i == 1 ? 350L : i == 3 ? 1400L : 700L, 500}, 0);
        }
    }

    private void startScreenCapture(final int i, String str) {
        if (getSharedInstance() == null || this.groupCall == null) {
            return;
        }
        this.mySource[1] = 0;
        TL_phone.joinGroupCallPresentation joingroupcallpresentation = new TL_phone.joinGroupCallPresentation();
        joingroupcallpresentation.call = this.groupCall.getInputGroupCall();
        TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
        joingroupcallpresentation.params = tL_dataJSON;
        tL_dataJSON.data = str;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(joingroupcallpresentation, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda132
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$startScreenCapture$32(i, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBluetoothHeadsetState(boolean z) {
        if (z == this.isBtHeadsetConnected) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("updateBluetoothHeadsetState: " + z);
        }
        this.isBtHeadsetConnected = z;
        final AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        if (!z || isRinging() || this.currentState == 0) {
            this.bluetoothScoActive = false;
            this.bluetoothScoConnecting = false;
            audioManager.setBluetoothScoOn(false);
        } else if (this.bluetoothScoActive) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("SCO already active, setting audio routing");
            }
            if (!hasRtmpStream()) {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setBluetoothScoOn(true);
            }
        } else {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("startBluetoothSco");
            }
            if (!hasRtmpStream()) {
                this.needSwitchToBluetoothAfterScoActivates = true;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda85
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.lambda$updateBluetoothHeadsetState$83(audioManager);
                    }
                }, 500L);
            }
        }
        Iterator<StateListener> it = this.stateListeners.iterator();
        while (it.hasNext()) {
            it.next().onAudioSettingsChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateConnectionState, reason: merged with bridge method [inline-methods] */
    public void lambda$createGroupInstance$50(final int i, int i2, boolean z) {
        if (i != 0) {
            return;
        }
        dispatchStateChanged((i2 == 1 || this.switchingStream) ? 3 : 5);
        if (this.switchingStream && (i2 == 0 || (i2 == 1 && z))) {
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda96
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$updateConnectionState$51(i);
                }
            };
            this.switchingStreamTimeoutRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 3000L);
        }
        if (i2 == 0) {
            startGroupCheckShortpoll();
            if (!this.playedConnectedSound || this.spPlayId != 0 || this.switchingStream || this.switchingAccount) {
                return;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$updateConnectionState$52();
                }
            });
            return;
        }
        cancelGroupCheckShortPoll();
        if (!z) {
            this.switchingStream = false;
            this.switchingAccount = false;
        }
        Runnable runnable2 = this.switchingStreamTimeoutRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.switchingStreamTimeoutRunnable = null;
        }
        if (this.playedConnectedSound) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda98
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$updateConnectionState$53();
                }
            });
            Runnable runnable3 = this.connectingSoundRunnable;
            if (runnable3 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable3);
                this.connectingSoundRunnable = null;
            }
        } else {
            playConnectedSound();
        }
        if (this.wasConnected) {
            return;
        }
        this.wasConnected = true;
        if (this.reconnectScreenCapture) {
            createGroupInstance(1, false);
            this.reconnectScreenCapture = false;
        }
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance != null && !this.micMute) {
            nativeInstance.setMuteMicrophone(false);
        }
        setParticipantsVolume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkType() {
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance == null) {
            this.lastNetInfo = getActiveNetworkInfo();
        } else {
            if (nativeInstance.isGroup()) {
                return;
            }
            this.tgVoip[0].setNetworkType(getNetworkType());
        }
    }

    private void updateServerConfig() {
        final SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
        Instance.setGlobalServerConfig(mainSettings.getString("voip_server_config", "{}"));
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_phone.getCallConfig(), new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda88
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.lambda$updateServerConfig$77(mainSettings, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTrafficStats(NativeInstance nativeInstance, Instance.TrafficStats trafficStats) {
        if (trafficStats == null) {
            trafficStats = nativeInstance.getTrafficStats();
        }
        long j = trafficStats.bytesSentWifi;
        Instance.TrafficStats trafficStats2 = this.prevTrafficStats;
        long j2 = j - (trafficStats2 != null ? trafficStats2.bytesSentWifi : 0L);
        long j3 = trafficStats.bytesReceivedWifi - (trafficStats2 != null ? trafficStats2.bytesReceivedWifi : 0L);
        long j4 = trafficStats.bytesSentMobile - (trafficStats2 != null ? trafficStats2.bytesSentMobile : 0L);
        long j5 = trafficStats.bytesReceivedMobile - (trafficStats2 != null ? trafficStats2.bytesReceivedMobile : 0L);
        this.prevTrafficStats = trafficStats;
        if (j2 > 0) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(1, 0, j2);
        }
        if (j3 > 0) {
            StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(1, 0, j3);
        }
        if (j4 > 0) {
            StatsController statsController = StatsController.getInstance(this.currentAccount);
            NetworkInfo networkInfo = this.lastNetInfo;
            statsController.incrementSentBytesCount((networkInfo == null || !networkInfo.isRoaming()) ? 0 : 2, 0, j4);
        }
        if (j5 > 0) {
            StatsController statsController2 = StatsController.getInstance(this.currentAccount);
            NetworkInfo networkInfo2 = this.lastNetInfo;
            statsController2.incrementReceivedBytesCount((networkInfo2 == null || !networkInfo2.isRoaming()) ? 0 : 2, 0, j5);
        }
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public void acceptIncomingCall() {
        updateCurrentForegroundType();
        MessagesController.getInstance(this.currentAccount).ignoreSetOnline = false;
        stopRinging();
        showNotification();
        configureDeviceForCall();
        startConnectingSound();
        dispatchStateChanged(12);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.lambda$acceptIncomingCall$70();
            }
        });
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        TLRPC.TL_messages_getDhConfig tL_messages_getDhConfig = new TLRPC.TL_messages_getDhConfig();
        tL_messages_getDhConfig.random_length = 256;
        tL_messages_getDhConfig.version = messagesStorage.getLastSecretVersion();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getDhConfig, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda38
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$acceptIncomingCall$73(messagesStorage, tLObject, tL_error);
            }
        });
    }

    public ProxyVideoSink addRemoteSink(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, boolean z, VideoSink videoSink, VideoSink videoSink2) {
        if (this.tgVoip[0] == null) {
            return null;
        }
        String str = z ? tL_groupCallParticipant.presentationEndpoint : tL_groupCallParticipant.videoEndpoint;
        if (str == null) {
            return null;
        }
        ProxyVideoSink proxyVideoSink = this.remoteSinks.get(str);
        if (proxyVideoSink != null && proxyVideoSink.target == videoSink) {
            return proxyVideoSink;
        }
        if (proxyVideoSink == null) {
            proxyVideoSink = this.proxyVideoSinkLruCache.remove(str);
        }
        if (proxyVideoSink == null) {
            proxyVideoSink = new ProxyVideoSink();
        }
        if (videoSink != null) {
            proxyVideoSink.setTarget(videoSink);
        }
        if (videoSink2 != null) {
            proxyVideoSink.setBackground(videoSink2);
        }
        this.remoteSinks.put(str, proxyVideoSink);
        proxyVideoSink.nativeInstance = this.tgVoip[0].addIncomingVideoOutput(1, str, createSsrcGroups(z ? tL_groupCallParticipant.presentation : tL_groupCallParticipant.video), proxyVideoSink);
        return proxyVideoSink;
    }

    void callFailedFromConnectionService() {
        if (this.isOutgoing) {
            callFailed(Instance.ERROR_CONNECTION_SERVICE);
        } else {
            hangUp();
        }
    }

    public void checkVideoFrame(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, boolean z) {
        String str = z ? tL_groupCallParticipant.presentationEndpoint : tL_groupCallParticipant.videoEndpoint;
        if (str == null) {
            return;
        }
        if (!z || tL_groupCallParticipant.hasPresentationFrame == 0) {
            if (z || tL_groupCallParticipant.hasCameraFrame == 0) {
                if (this.proxyVideoSinkLruCache.get(str) != null || (this.remoteSinks.get(str) != null && this.waitingFrameParticipant.get(str) == null)) {
                    if (z) {
                        tL_groupCallParticipant.hasPresentationFrame = 2;
                        return;
                    } else {
                        tL_groupCallParticipant.hasCameraFrame = 2;
                        return;
                    }
                }
                if (this.waitingFrameParticipant.containsKey(str)) {
                    this.waitingFrameParticipant.put(str, tL_groupCallParticipant);
                    if (z) {
                        tL_groupCallParticipant.hasPresentationFrame = 1;
                        return;
                    } else {
                        tL_groupCallParticipant.hasCameraFrame = 1;
                        return;
                    }
                }
                if (z) {
                    tL_groupCallParticipant.hasPresentationFrame = 1;
                } else {
                    tL_groupCallParticipant.hasCameraFrame = 1;
                }
                this.waitingFrameParticipant.put(str, tL_groupCallParticipant);
                addRemoteSink(tL_groupCallParticipant, z, new 5(str, z), null);
            }
        }
    }

    public void clearCamera() {
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance != null) {
            nativeInstance.clearVideoCapturer();
        }
    }

    public void clearRemoteSinks() {
        this.proxyVideoSinkLruCache.evictAll();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [int] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    public void createCaptureDevice(boolean z) {
        if (z) {
            this.gotMediaProjection = true;
            updateCurrentForegroundType();
        }
        ?? r2 = z ? 2 : this.isFrontFaceCamera;
        if (this.groupCall == null) {
            if (!this.isPrivateScreencast && z) {
                setVideoState(false, 0);
            }
            this.isPrivateScreencast = z;
            NativeInstance nativeInstance = this.tgVoip[0];
            if (nativeInstance != null) {
                nativeInstance.clearVideoCapturer();
            }
        }
        if (!z) {
            long j = this.captureDevice[z ? 1 : 0];
            if (j != 0 || this.tgVoip[z ? 1 : 0] == null) {
                NativeInstance nativeInstance2 = this.tgVoip[z ? 1 : 0];
                if (nativeInstance2 != null && j != 0) {
                    nativeInstance2.activateVideoCapturer(j);
                }
                if (this.captureDevice[z ? 1 : 0] != 0) {
                    return;
                }
            }
            this.captureDevice[z ? 1 : 0] = NativeInstance.createVideoCapturer(this.localSink[z ? 1 : 0], r2);
            return;
        }
        if (this.groupCall == null) {
            requestVideoCall(true);
            setVideoState(true, 2);
            if (VoIPFragment.getInstance() != null) {
                VoIPFragment.getInstance().onScreenCastStart();
                return;
            }
            return;
        }
        long[] jArr = this.captureDevice;
        if (jArr[z ? 1 : 0] != 0) {
            return;
        }
        jArr[z ? 1 : 0] = NativeInstance.createVideoCapturer(this.localSink[z ? 1 : 0], r2);
        createGroupInstance(1, false);
        setVideoState(true, 2);
        AccountInstance.getInstance(this.currentAccount).getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallScreencastStateChanged, new Object[0]);
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public void declineIncomingCall() {
        declineIncomingCall(1, null);
    }

    public void declineIncomingCall(int i, Runnable runnable) {
        if (this.groupCall != null) {
            stopScreenCapture();
        }
        stopRinging();
        this.callDiscardReason = i;
        int i2 = this.currentState;
        if (i2 == 14) {
            Runnable runnable2 = this.delayedStartOutgoingCall;
            if (runnable2 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable2);
                callEnded();
                return;
            } else {
                dispatchStateChanged(10);
                this.endCallAfterRequest = true;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda61
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$declineIncomingCall$74();
                    }
                }, 5000L);
                return;
            }
        }
        if (i2 == 10 || i2 == 11) {
            return;
        }
        dispatchStateChanged(10);
        if (this.privateCall == null) {
            this.onDestroyRunnable = runnable;
            callEnded();
            if (this.callReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.callReqId, false);
                this.callReqId = 0;
                return;
            }
            return;
        }
        TL_phone.discardCall discardcall = new TL_phone.discardCall();
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        discardcall.peer = tL_inputPhoneCall;
        TL_phone.PhoneCall phoneCall = this.privateCall;
        tL_inputPhoneCall.access_hash = phoneCall.access_hash;
        tL_inputPhoneCall.id = phoneCall.id;
        discardcall.duration = (int) (getCallDuration() / 1000);
        NativeInstance nativeInstance = this.tgVoip[0];
        discardcall.connection_id = nativeInstance != null ? nativeInstance.getPreferredRelayId() : 0L;
        discardcall.reason = i != 2 ? i != 3 ? i != 4 ? new TLRPC.TL_phoneCallDiscardReasonHangup() : new TLRPC.TL_phoneCallDiscardReasonBusy() : new TLRPC.TL_phoneCallDiscardReasonMissed() : new TLRPC.TL_phoneCallDiscardReasonDisconnect();
        FileLog.e("discardCall " + discardcall.reason);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(discardcall, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda62
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$declineIncomingCall$75(tLObject, tL_error);
            }
        }, 2);
        this.onDestroyRunnable = runnable;
        callEnded();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.appDidLogout) {
            callEnded();
        }
    }

    public void editCallMember(TLObject tLObject, Boolean bool, Boolean bool2, Integer num, Boolean bool3, final Runnable runnable) {
        StringBuilder sb;
        long j;
        TLRPC.InputPeer inputPeer;
        if (tLObject == null || this.groupCall == null) {
            return;
        }
        TL_phone.editGroupCallParticipant editgroupcallparticipant = new TL_phone.editGroupCallParticipant();
        editgroupcallparticipant.call = this.groupCall.getInputGroupCall();
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            if (!UserObject.isUserSelf(user) || (inputPeer = this.groupCallPeer) == null) {
                editgroupcallparticipant.participant = MessagesController.getInputPeer(user);
                if (BuildVars.LOGS_ENABLED) {
                    sb = new StringBuilder();
                    sb.append("edit group call part id = ");
                    sb.append(editgroupcallparticipant.participant.user_id);
                    sb.append(" access_hash = ");
                    j = editgroupcallparticipant.participant.user_id;
                    sb.append(j);
                    FileLog.d(sb.toString());
                }
            } else {
                editgroupcallparticipant.participant = inputPeer;
            }
        } else if (tLObject instanceof TLRPC.Chat) {
            editgroupcallparticipant.participant = MessagesController.getInputPeer((TLRPC.Chat) tLObject);
            if (BuildVars.LOGS_ENABLED) {
                sb = new StringBuilder();
                sb.append("edit group call part id = ");
                TLRPC.InputPeer inputPeer2 = editgroupcallparticipant.participant;
                long j2 = inputPeer2.chat_id;
                if (j2 == 0) {
                    j2 = inputPeer2.channel_id;
                }
                sb.append(j2);
                sb.append(" access_hash = ");
                j = editgroupcallparticipant.participant.access_hash;
                sb.append(j);
                FileLog.d(sb.toString());
            }
        }
        if (bool != null) {
            editgroupcallparticipant.muted = bool.booleanValue();
            editgroupcallparticipant.flags |= 1;
        }
        if (num != null) {
            editgroupcallparticipant.volume = num.intValue();
            editgroupcallparticipant.flags |= 2;
        }
        if (bool3 != null) {
            editgroupcallparticipant.raise_hand = bool3.booleanValue();
            editgroupcallparticipant.flags |= 4;
        }
        if (bool2 != null) {
            editgroupcallparticipant.video_stopped = bool2.booleanValue();
            editgroupcallparticipant.flags |= 8;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("edit group call flags = " + editgroupcallparticipant.flags);
        }
        final int i = this.currentAccount;
        AccountInstance.getInstance(i).getConnectionsManager().sendRequest(editgroupcallparticipant, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda72
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$editCallMember$60(i, runnable, tLObject2, tL_error);
            }
        });
    }

    public void forceRating() {
        this.forceRating = true;
    }

    public int getAccount() {
        return this.currentAccount;
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public long getCallDuration() {
        if (this.callStartTime == 0) {
            return 0L;
        }
        return SystemClock.elapsedRealtime() - this.callStartTime;
    }

    public long getCallID() {
        TL_phone.PhoneCall phoneCall = this.privateCall;
        if (phoneCall != null) {
            return phoneCall.id;
        }
        return 0L;
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public int getCallState() {
        return this.currentState;
    }

    public long getCallerId() {
        TLRPC.User user = this.user;
        return user != null ? user.id : -this.chat.id;
    }

    public TLRPC.Chat getChat() {
        return this.chat;
    }

    public CallConnection getConnectionAndStartCall() {
        if (this.systemCallConnection == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("creating call connection");
            }
            CallConnection callConnection = new CallConnection();
            this.systemCallConnection = callConnection;
            callConnection.setInitializing();
            if (this.isOutgoing) {
                Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda118
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$getConnectionAndStartCall$76();
                    }
                };
                this.delayedStartOutgoingCall = runnable;
                AndroidUtilities.runOnUIThread(runnable, 2000L);
            }
            this.systemCallConnection.setAddress(Uri.fromParts("tel", "+99084" + this.user.id, null), 1);
            CallConnection callConnection2 = this.systemCallConnection;
            TLRPC.User user = this.user;
            callConnection2.setCallerDisplayName(ContactsController.formatName(user.first_name, user.last_name), 1);
        }
        return this.systemCallConnection;
    }

    public int getCurrentAudioRoute() {
        CallAudioState callAudioState;
        CallAudioState callAudioState2;
        int route;
        if (!USE_CONNECTION_SERVICE) {
            if (!this.audioConfigured) {
                return this.audioRouteToSet;
            }
            AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            VoipAudioManager voipAudioManager = VoipAudioManager.get();
            if (audioManager.isBluetoothScoOn()) {
                return 2;
            }
            return voipAudioManager.isSpeakerphoneOn() ? 1 : 0;
        }
        CallConnection callConnection = this.systemCallConnection;
        if (callConnection != null) {
            callAudioState = callConnection.getCallAudioState();
            if (callAudioState != null) {
                callAudioState2 = this.systemCallConnection.getCallAudioState();
                route = callAudioState2.getRoute();
                if (route != 1) {
                    if (route == 2) {
                        return 2;
                    }
                    if (route != 4) {
                        if (route == 8) {
                            return 1;
                        }
                    }
                }
                return 0;
            }
        }
        return this.audioRouteToSet;
    }

    public String getDebugString() {
        NativeInstance nativeInstance = this.tgVoip[0];
        return nativeInstance != null ? nativeInstance.getDebugInfo() : "";
    }

    public byte[] getEncryptionKey() {
        return this.authKey;
    }

    public byte[] getGA() {
        return this.g_a;
    }

    public CountDownLatch getGroupCallBottomSheetLatch() {
        return this.groupCallBottomSheetLatch;
    }

    public TLRPC.InputPeer getGroupCallPeer() {
        return this.groupCallPeer;
    }

    public String getLastError() {
        return this.lastError;
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public TL_phone.PhoneCall getPrivateCall() {
        return this.privateCall;
    }

    public int getRemoteAudioState() {
        return this.remoteAudioState;
    }

    public int getRemoteVideoState() {
        return this.remoteVideoState;
    }

    public long getSelfId() {
        TLRPC.InputPeer inputPeer = this.groupCallPeer;
        return inputPeer == null ? UserConfig.getInstance(this.currentAccount).clientUserId : inputPeer instanceof TLRPC.TL_inputPeerUser ? inputPeer.user_id : inputPeer instanceof TLRPC.TL_inputPeerChannel ? -inputPeer.channel_id : -inputPeer.chat_id;
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public TLRPC.User getUser() {
        return this.user;
    }

    public int getVideoState(boolean z) {
        return this.videoState[z ? 1 : 0];
    }

    public void handleNotificationAction(Intent intent) {
        if ((getPackageName() + ".END_CALL").equals(intent.getAction())) {
            stopForeground(true);
            hangUp();
            return;
        }
        if ((getPackageName() + ".DECLINE_CALL").equals(intent.getAction())) {
            stopForeground(true);
            declineIncomingCall(4, null);
            return;
        }
        if ((getPackageName() + ".ANSWER_CALL").equals(intent.getAction())) {
            acceptIncomingCallFromNotification();
        }
    }

    public void hangUp() {
        hangUp(0, null);
    }

    public void hangUp(int i) {
        hangUp(i, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void hangUp(int i, Runnable runnable) {
        RequestDelegate requestDelegate;
        ConnectionsManager connectionsManager;
        TL_phone.leaveGroupCall leavegroupcall;
        int i2 = this.currentState;
        declineIncomingCall((i2 == 16 || (i2 == 13 && this.isOutgoing)) ? 3 : 1, runnable);
        if (this.groupCall == null || i == 2) {
            return;
        }
        if (i == 1) {
            TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chat.id);
            if (chatFull != null) {
                chatFull.flags &= -2097153;
                chatFull.call = null;
                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallUpdated, Long.valueOf(this.chat.id), Long.valueOf(this.groupCall.call.id), Boolean.FALSE);
            }
            TL_phone.discardGroupCall discardgroupcall = new TL_phone.discardGroupCall();
            discardgroupcall.call = this.groupCall.getInputGroupCall();
            ConnectionsManager connectionsManager2 = ConnectionsManager.getInstance(this.currentAccount);
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda104
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    VoIPService.this.lambda$hangUp$3(tLObject, tL_error);
                }
            };
            leavegroupcall = discardgroupcall;
            connectionsManager = connectionsManager2;
        } else {
            TL_phone.leaveGroupCall leavegroupcall2 = new TL_phone.leaveGroupCall();
            leavegroupcall2.call = this.groupCall.getInputGroupCall();
            leavegroupcall2.source = this.mySource[0];
            ConnectionsManager connectionsManager3 = ConnectionsManager.getInstance(this.currentAccount);
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda105
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    VoIPService.this.lambda$hangUp$4(tLObject, tL_error);
                }
            };
            leavegroupcall = leavegroupcall2;
            connectionsManager = connectionsManager3;
        }
        connectionsManager.sendRequest(leavegroupcall, requestDelegate);
    }

    public void hangUp(Runnable runnable) {
        hangUp(0, runnable);
    }

    public boolean hasEarpiece() {
        CallConnection callConnection;
        CallAudioState callAudioState;
        CallAudioState callAudioState2;
        int supportedRouteMask;
        if (USE_CONNECTION_SERVICE && (callConnection = this.systemCallConnection) != null) {
            callAudioState = callConnection.getCallAudioState();
            if (callAudioState != null) {
                callAudioState2 = this.systemCallConnection.getCallAudioState();
                supportedRouteMask = callAudioState2.getSupportedRouteMask();
                return (supportedRouteMask & 5) != 0;
            }
        }
        if (((TelephonyManager) getSystemService("phone")).getPhoneType() != 0) {
            return true;
        }
        Boolean bool = this.mHasEarpiece;
        if (bool != null) {
            return bool.booleanValue();
        }
        try {
            AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            Method method = AudioManager.class.getMethod("getDevicesForStream", Integer.TYPE);
            int i = AudioManager.class.getField("DEVICE_OUT_EARPIECE").getInt(null);
            this.mHasEarpiece = (((Integer) method.invoke(audioManager, 0)).intValue() & i) == i ? Boolean.TRUE : Boolean.FALSE;
        } catch (Throwable th) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error while checking earpiece! ", th);
            }
            this.mHasEarpiece = Boolean.TRUE;
        }
        return this.mHasEarpiece.booleanValue();
    }

    public boolean hasRate() {
        return this.needRateCall || this.forceRating;
    }

    public boolean hasVideoCapturer() {
        return this.captureDevice[0] != 0;
    }

    public boolean isBluetoothHeadsetConnected() {
        CallConnection callConnection;
        CallAudioState callAudioState;
        CallAudioState callAudioState2;
        int supportedRouteMask;
        if (USE_CONNECTION_SERVICE && (callConnection = this.systemCallConnection) != null) {
            callAudioState = callConnection.getCallAudioState();
            if (callAudioState != null) {
                callAudioState2 = this.systemCallConnection.getCallAudioState();
                supportedRouteMask = callAudioState2.getSupportedRouteMask();
                return (supportedRouteMask & 2) != 0;
            }
        }
        return this.isBtHeadsetConnected;
    }

    public boolean isBluetoothOn() {
        return ((AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND)).isBluetoothScoOn();
    }

    public boolean isBluetoothWillOn() {
        return this.needSwitchToBluetoothAfterScoActivates;
    }

    public boolean isFrontFaceCamera() {
        return this.isFrontFaceCamera;
    }

    public boolean isFullscreen(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, boolean z) {
        if (this.currentBackgroundSink[z ? 1 : 0] != null) {
            if (TextUtils.equals(this.currentBackgroundEndpointId[z ? 1 : 0], z ? tL_groupCallParticipant.presentationEndpoint : tL_groupCallParticipant.videoEndpoint)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHangingUp() {
        return this.currentState == 10;
    }

    public boolean isHeadsetPlugged() {
        return this.isHeadsetPlugged;
    }

    public boolean isJoined() {
        int i = this.currentState;
        return (i == 1 || i == 6) ? false : true;
    }

    public boolean isMicMute() {
        return this.micMute;
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public boolean isOutgoing() {
        return this.isOutgoing;
    }

    public boolean isScreencast() {
        return this.isPrivateScreencast;
    }

    public boolean isSpeakerphoneOn() {
        CallConnection callConnection;
        CallAudioState callAudioState;
        CallAudioState callAudioState2;
        int route;
        boolean z = USE_CONNECTION_SERVICE;
        if (z && (callConnection = this.systemCallConnection) != null) {
            callAudioState = callConnection.getCallAudioState();
            if (callAudioState != null) {
                callAudioState2 = this.systemCallConnection.getCallAudioState();
                route = callAudioState2.getRoute();
                return !hasEarpiece() ? route != 2 : route != 8;
            }
        }
        if (!this.audioConfigured || z) {
            return this.speakerphoneStateToSet;
        }
        return hasEarpiece() ? VoipAudioManager.get().isSpeakerphoneOn() : ((AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND)).isBluetoothScoOn();
    }

    public boolean isSwitchingCamera() {
        return this.switchingCamera;
    }

    public boolean isSwitchingStream() {
        return this.switchingStream;
    }

    public boolean isVideoAvailable() {
        return this.isVideoAvailable;
    }

    public void migrateToChat(TLRPC.Chat chat) {
        this.chat = chat;
    }

    public boolean mutedByAdmin() {
        ChatObject.Call call = this.groupCall;
        if (call == null) {
            return false;
        }
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) call.participants.get(getSelfId());
        return (tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted || ChatObject.canManageCalls(this.chat)) ? false : true;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(int i) {
        if (i == 1) {
            this.hasAudioFocus = true;
        } else {
            this.hasAudioFocus = false;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x016e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCallUpdated(TL_phone.PhoneCall phoneCall) {
        byte[] bArr;
        long bytesToLong;
        if (this.user == null) {
            return;
        }
        TL_phone.PhoneCall phoneCall2 = this.privateCall;
        if (phoneCall2 == null) {
            this.pendingUpdates.add(phoneCall);
            return;
        }
        if (phoneCall == null) {
            return;
        }
        if (phoneCall.id != phoneCall2.id) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("onCallUpdated called with wrong call id (got " + phoneCall.id + ", expected " + this.privateCall.id + ")");
                return;
            }
            return;
        }
        if (phoneCall.access_hash == 0) {
            phoneCall.access_hash = phoneCall2.access_hash;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Call updated: " + phoneCall);
        }
        this.privateCall = phoneCall;
        if (phoneCall instanceof TL_phone.TL_phoneCallDiscarded) {
            this.needSendDebugLog = phoneCall.need_debug;
            this.needRateCall = phoneCall.need_rating;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("call discarded, stopping service");
            }
            if (!(phoneCall.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy)) {
                callEnded();
                return;
            }
            dispatchStateChanged(17);
            this.playingSound = true;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda129
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$onCallUpdated$15();
                }
            });
            AndroidUtilities.runOnUIThread(this.afterSoundRunnable, 1500L);
            endConnectionServiceCall(1500L);
            stopSelf();
            return;
        }
        if (!(phoneCall instanceof TL_phone.TL_phoneCall) || this.authKey != null) {
            if ((phoneCall instanceof TL_phone.TL_phoneCallAccepted) && this.authKey == null) {
                processAcceptedCall();
                return;
            }
            if (this.currentState != 13 || phoneCall.receive_date == 0) {
                return;
            }
            dispatchStateChanged(16);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("!!!!!! CALL RECEIVED");
            }
            Runnable runnable = this.connectingSoundRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.connectingSoundRunnable = null;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda130
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$onCallUpdated$16();
                }
            });
            Runnable runnable2 = this.timeoutRunnable;
            if (runnable2 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable2);
                this.timeoutRunnable = null;
            }
            Runnable runnable3 = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda131
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$onCallUpdated$17();
                }
            };
            this.timeoutRunnable = runnable3;
            AndroidUtilities.runOnUIThread(runnable3, MessagesController.getInstance(this.currentAccount).callRingTimeout);
            return;
        }
        byte[] bArr2 = phoneCall.g_a_or_b;
        if (bArr2 == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("stopping VoIP service, Ga == null");
            }
            callFailed();
            return;
        }
        if (!Arrays.equals(this.g_a_hash, Utilities.computeSHA256(bArr2, 0, bArr2.length))) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("stopping VoIP service, Ga hash doesn't match");
            }
            callFailed();
            return;
        }
        this.g_a = phoneCall.g_a_or_b;
        BigInteger bigInteger = new BigInteger(1, phoneCall.g_a_or_b);
        BigInteger bigInteger2 = new BigInteger(1, MessagesStorage.getInstance(this.currentAccount).getSecretPBytes());
        if (!Utilities.isGoodGaAndGb(bigInteger, bigInteger2)) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("stopping VoIP service, bad Ga and Gb (accepting)");
            }
            callFailed();
            return;
        }
        byte[] byteArray = bigInteger.modPow(new BigInteger(1, this.a_or_b), bigInteger2).toByteArray();
        if (byteArray.length <= 256) {
            if (byteArray.length < 256) {
                bArr = new byte[256];
                System.arraycopy(byteArray, 0, bArr, 256 - byteArray.length, byteArray.length);
                for (int i = 0; i < 256 - byteArray.length; i++) {
                    bArr[i] = 0;
                }
            }
            byte[] computeSHA1 = Utilities.computeSHA1(byteArray);
            byte[] bArr3 = new byte[8];
            System.arraycopy(computeSHA1, computeSHA1.length - 8, bArr3, 0, 8);
            this.authKey = byteArray;
            bytesToLong = Utilities.bytesToLong(bArr3);
            this.keyFingerprint = bytesToLong;
            if (bytesToLong != phoneCall.key_fingerprint) {
                initiateActualEncryptedCall();
                return;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("key fingerprints don't match");
            }
            callFailed();
            return;
        }
        bArr = new byte[256];
        System.arraycopy(byteArray, byteArray.length - 256, bArr, 0, 256);
        byteArray = bArr;
        byte[] computeSHA12 = Utilities.computeSHA1(byteArray);
        byte[] bArr32 = new byte[8];
        System.arraycopy(computeSHA12, computeSHA12.length - 8, bArr32, 0, 8);
        this.authKey = byteArray;
        bytesToLong = Utilities.bytesToLong(bArr32);
        this.keyFingerprint = bytesToLong;
        if (bytesToLong != phoneCall.key_fingerprint) {
        }
    }

    protected void onCameraFirstFrameAvailable() {
        for (int i = 0; i < this.stateListeners.size(); i++) {
            this.stateListeners.get(i).onCameraFirstFrameAvailable();
        }
    }

    @Override // org.telegram.messenger.voip.VoIPController.ConnectionStateListener
    public void onConnectionStateChanged(final int i, boolean z) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda123
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$onConnectionStateChanged$89(i);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x010c, code lost:
    
        if (r0 >= 33) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x010e, code lost:
    
        r7.foregroundId = 201;
        r0 = r2.build();
        r7.foregroundNotification = r0;
        r1 = getCurrentForegroundType();
        r7.lastForegroundType = r1;
        startForeground(201, r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0120, code lost:
    
        r7.foregroundId = 201;
        r0 = r2.build();
        r7.foregroundNotification = r0;
        startForeground(201, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0148, code lost:
    
        if (r0 >= 33) goto L51;
     */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreate() {
        Notification.Builder showWhen;
        super.onCreate();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("=============== VoIPService STARTING ===============");
        }
        try {
            AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            Instance.setBufferSize(audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER") != null ? Integer.parseInt(audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER")) : AudioTrack.getMinBufferSize(48000, 4, 2) / 2);
            PowerManager.WakeLock newWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "telegram-voip");
            this.cpuWakelock = newWakeLock;
            newWakeLock.acquire();
            this.btAdapter = audioManager.isBluetoothScoAvailableOffCall() ? BluetoothAdapter.getDefaultAdapter() : null;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            if (!USE_CONNECTION_SERVICE) {
                intentFilter.addAction(ACTION_HEADSET_PLUG);
                if (this.btAdapter != null) {
                    intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                    intentFilter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
                }
                intentFilter.addAction("android.intent.action.PHONE_STATE");
                intentFilter.addAction("android.intent.action.SCREEN_ON");
                intentFilter.addAction("android.intent.action.SCREEN_OFF");
            }
            registerReceiver(this.receiver, intentFilter);
            fetchBluetoothDeviceName();
            if (this.audioDeviceCallback == null) {
                try {
                    this.audioDeviceCallback = new AudioDeviceCallback() { // from class: org.telegram.messenger.voip.VoIPService.8
                        @Override // android.media.AudioDeviceCallback
                        public void onAudioDevicesAdded(AudioDeviceInfo[] audioDeviceInfoArr) {
                            VoIPService.this.checkUpdateBluetoothHeadset();
                        }

                        @Override // android.media.AudioDeviceCallback
                        public void onAudioDevicesRemoved(AudioDeviceInfo[] audioDeviceInfoArr) {
                            VoIPService.this.checkUpdateBluetoothHeadset();
                        }
                    };
                } catch (Throwable th) {
                    FileLog.e(th);
                    this.audioDeviceCallback = null;
                }
            }
            AudioDeviceCallback audioDeviceCallback = this.audioDeviceCallback;
            if (audioDeviceCallback != null) {
                audioManager.registerAudioDeviceCallback(audioDeviceCallback, new Handler(Looper.getMainLooper()));
            }
            audioManager.registerMediaButtonEventReceiver(new ComponentName(this, (Class<?>) VoIPMediaButtonReceiver.class));
            checkUpdateBluetoothHeadset();
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("error initializing voip controller", e);
            }
            callFailed();
        }
        int i = Build.VERSION.SDK_INT;
        if (i < 26) {
            return;
        }
        TL_phone.PhoneCall phoneCall = callIShouldHavePutIntoIntent;
        NotificationsController.checkOtherNotificationsChannel();
        if (phoneCall != null) {
            showWhen = new Notification.Builder(this, NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).setContentTitle(LocaleController.getString(R.string.VoipOutgoingCall)).setShowWhen(false);
            showWhen.setSmallIcon(this.groupCall != null ? isMicMute() ? R.drawable.voicechat_muted : R.drawable.voicechat_active : R.drawable.ic_call);
            this.foregroundStarted = true;
        } else {
            showWhen = new Notification.Builder(this, NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).setContentTitle(LocaleController.getString(R.string.VoipCallEnded)).setShowWhen(false);
            showWhen.setSmallIcon(R.drawable.ic_call);
            this.foregroundStarted = true;
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("=============== VoIPService STOPPING ===============");
        }
        stopForeground(true);
        stopRinging();
        if (this.currentAccount >= 0) {
            if (ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn) {
                MessagesController.getInstance(this.currentAccount).ignoreSetOnline = false;
            }
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
        }
        SensorManager sensorManager = (SensorManager) getSystemService("sensor");
        if (sensorManager.getDefaultSensor(8) != null) {
            sensorManager.unregisterListener(this);
        }
        PowerManager.WakeLock wakeLock = this.proximityWakelock;
        if (wakeLock != null && wakeLock.isHeld()) {
            this.proximityWakelock.release();
        }
        if (this.updateNotificationRunnable != null) {
            Utilities.globalQueue.cancelRunnable(this.updateNotificationRunnable);
            this.updateNotificationRunnable = null;
        }
        Runnable runnable = this.switchingStreamTimeoutRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.switchingStreamTimeoutRunnable = null;
        }
        unregisterReceiver(this.receiver);
        Runnable runnable2 = this.timeoutRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.timeoutRunnable = null;
        }
        super.onDestroy();
        sharedInstance = null;
        FileLog.e("(5) set sharedInstance = null");
        Arrays.fill(this.mySource, 0);
        cancelGroupCheckShortPoll();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.lambda$onDestroy$67();
            }
        });
        if (this.tgVoip[0] != null) {
            StatsController.getInstance(this.currentAccount).incrementTotalCallsTime(getStatsNetworkType(), ((int) (getCallDuration() / 1000)) % 5);
            onTgVoipPreStop();
            if (this.tgVoip[0].isGroup()) {
                NativeInstance nativeInstance = this.tgVoip[0];
                DispatchQueue dispatchQueue = Utilities.globalQueue;
                Objects.requireNonNull(nativeInstance);
                dispatchQueue.postRunnable(new VoIPService$$ExternalSyntheticLambda81(nativeInstance));
                Iterator<Map.Entry<String, Integer>> it = this.currentStreamRequestTimestamp.entrySet().iterator();
                while (it.hasNext()) {
                    AccountInstance.getInstance(this.currentAccount).getConnectionsManager().cancelRequest(it.next().getValue().intValue(), true);
                }
                this.currentStreamRequestTimestamp.clear();
            } else {
                Instance.FinalState stop = this.tgVoip[0].stop();
                updateTrafficStats(this.tgVoip[0], stop.trafficStats);
                onTgVoipStop(stop);
            }
            this.prevTrafficStats = null;
            this.callStartTime = 0L;
            this.tgVoip[0] = null;
            Instance.destroyInstance();
        }
        NativeInstance nativeInstance2 = this.tgVoip[1];
        if (nativeInstance2 != null) {
            Utilities.globalQueue.postRunnable(new VoIPService$$ExternalSyntheticLambda81(nativeInstance2));
            this.tgVoip[1] = null;
        }
        int i = 0;
        while (true) {
            long[] jArr = this.captureDevice;
            if (i >= jArr.length) {
                break;
            }
            long j = jArr[i];
            if (j != 0) {
                if (this.destroyCaptureDevice[i]) {
                    NativeInstance.destroyVideoCapturer(j);
                }
                this.captureDevice[i] = 0;
            }
            i++;
        }
        this.cpuWakelock.release();
        final AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        if (!this.playingSound) {
            VoipAudioManager voipAudioManager = VoipAudioManager.get();
            if (!USE_CONNECTION_SERVICE) {
                if (this.isBtHeadsetConnected || this.bluetoothScoActive || this.bluetoothScoConnecting) {
                    audioManager.stopBluetoothSco();
                    audioManager.setBluetoothScoOn(false);
                    voipAudioManager.setSpeakerphoneOn(false);
                    this.bluetoothScoActive = false;
                    this.bluetoothScoConnecting = false;
                }
                if (this.onDestroyRunnable == null) {
                    DispatchQueue dispatchQueue2 = Utilities.globalQueue;
                    Runnable runnable3 = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda82
                        @Override // java.lang.Runnable
                        public final void run() {
                            VoIPService.lambda$onDestroy$68(audioManager);
                        }
                    };
                    setModeRunnable = runnable3;
                    dispatchQueue2.postRunnable(runnable3);
                }
                audioManager.abandonAudioFocus(this);
            }
            try {
                audioManager.unregisterMediaButtonEventReceiver(new ComponentName(this, (Class<?>) VoIPMediaButtonReceiver.class));
            } catch (Exception e) {
                FileLog.e(e);
            }
            AudioDeviceCallback audioDeviceCallback = this.audioDeviceCallback;
            if (audioDeviceCallback != null) {
                audioManager.unregisterAudioDeviceCallback(audioDeviceCallback);
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda83
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$onDestroy$69();
                }
            });
        }
        if (this.hasAudioFocus) {
            audioManager.abandonAudioFocus(this);
        }
        if (USE_CONNECTION_SERVICE) {
            if (!this.didDeleteConnectionServiceContact) {
                ContactsController.getInstance(this.currentAccount).deleteConnectionServiceContact();
            }
            CallConnection callConnection = this.systemCallConnection;
            if (callConnection != null && !this.playingSound) {
                callConnection.destroy();
            }
        }
        VoIPHelper.lastCallTime = SystemClock.elapsedRealtime();
        setSinks(null, null);
        Runnable runnable4 = this.onDestroyRunnable;
        if (runnable4 != null) {
            runnable4.run();
        }
        int i2 = this.currentAccount;
        if (i2 >= 0) {
            ConnectionsManager.getInstance(i2).setAppPaused(true, false);
            if (ChatObject.isChannel(this.chat)) {
                MessagesController.getInstance(this.currentAccount).startShortPoll(this.chat, this.classGuid, true);
            }
        }
    }

    public void onGroupCallParticipantsUpdate(TLRPC.TL_updateGroupCallParticipants tL_updateGroupCallParticipants) {
        ChatObject.Call call;
        if (this.chat == null || (call = this.groupCall) == null || call.call.id != tL_updateGroupCallParticipants.call.id) {
            return;
        }
        long selfId = getSelfId();
        int size = tL_updateGroupCallParticipants.participants.size();
        for (int i = 0; i < size; i++) {
            TLRPC.TL_groupCallParticipant tL_groupCallParticipant = tL_updateGroupCallParticipants.participants.get(i);
            if (tL_groupCallParticipant.left) {
                int i2 = tL_groupCallParticipant.source;
                if (i2 != 0 && i2 == this.mySource[0]) {
                    int i3 = 0;
                    for (int i4 = 0; i4 < size; i4++) {
                        TLRPC.TL_groupCallParticipant tL_groupCallParticipant2 = tL_updateGroupCallParticipants.participants.get(i4);
                        if (tL_groupCallParticipant2.self || tL_groupCallParticipant2.source == this.mySource[0]) {
                            i3++;
                        }
                    }
                    if (i3 > 1) {
                        hangUp(2);
                        return;
                    }
                }
            } else if (MessageObject.getPeerId(tL_groupCallParticipant.peer) != selfId) {
                continue;
            } else {
                int i5 = tL_groupCallParticipant.source;
                int i6 = this.mySource[0];
                if (i5 != i6 && i6 != 0 && i5 != 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("source mismatch my = " + this.mySource[0] + " psrc = " + tL_groupCallParticipant.source);
                    }
                    hangUp(2);
                    return;
                }
                if (ChatObject.isChannel(this.chat) && this.currentGroupModeStreaming && tL_groupCallParticipant.can_self_unmute) {
                    this.switchingStream = true;
                    createGroupInstance(0, false);
                }
                if (tL_groupCallParticipant.muted) {
                    setMicMute(true, false, false);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0053 A[Catch: Exception -> 0x0063, TRY_ENTER, TryCatch #0 {Exception -> 0x0063, blocks: (B:31:0x0053, B:33:0x005d, B:36:0x0065, B:37:0x0072, B:40:0x0069), top: B:29:0x0051 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0069 A[Catch: Exception -> 0x0063, TryCatch #0 {Exception -> 0x0063, blocks: (B:31:0x0053, B:33:0x005d, B:36:0x0065, B:37:0x0072, B:40:0x0069), top: B:29:0x0051 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onGroupCallUpdated(TLRPC.GroupCall groupCall) {
        ChatObject.Call call;
        boolean optBoolean;
        TLRPC.TL_dataJSON tL_dataJSON;
        if (this.chat == null || (call = this.groupCall) == null) {
            return;
        }
        TLRPC.GroupCall groupCall2 = call.call;
        if (groupCall2.id != groupCall.id) {
            return;
        }
        if (groupCall2 instanceof TLRPC.TL_groupCallDiscarded) {
            hangUp(2);
            return;
        }
        boolean z = false;
        try {
            if (this.myParams != null) {
                try {
                    optBoolean = new JSONObject(this.myParams.data).optBoolean("stream");
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if ((this.currentState == 1 && optBoolean == this.currentGroupModeStreaming) || (tL_dataJSON = this.myParams) == null) {
                    return;
                }
                if (this.playedConnectedSound && optBoolean != this.currentGroupModeStreaming) {
                    this.switchingStream = true;
                }
                this.currentGroupModeStreaming = optBoolean;
                if (optBoolean) {
                    this.tgVoip[0].setJoinResponsePayload(tL_dataJSON.data);
                } else {
                    NativeInstance nativeInstance = this.tgVoip[0];
                    TLRPC.GroupCall groupCall3 = this.groupCall.call;
                    if (groupCall3 != null && groupCall3.rtmp_stream) {
                        z = true;
                    }
                    nativeInstance.prepareForStream(z);
                }
                dispatchStateChanged(2);
                return;
            }
            if (optBoolean) {
            }
            dispatchStateChanged(2);
            return;
        } catch (Exception e2) {
            FileLog.e(e2);
            return;
        }
        optBoolean = false;
        if (this.currentState == 1) {
        }
        if (this.playedConnectedSound) {
            this.switchingStream = true;
        }
        this.currentGroupModeStreaming = optBoolean;
    }

    void onMediaButtonEvent(KeyEvent keyEvent) {
        if (keyEvent == null) {
            return;
        }
        if ((keyEvent.getKeyCode() == 79 || keyEvent.getKeyCode() == 127 || keyEvent.getKeyCode() == 85) && keyEvent.getAction() == 1) {
            if (this.currentState == 15) {
                acceptIncomingCall();
            } else {
                setMicMute(!isMicMute(), false, true);
            }
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        this.lastSensorEvent = sensorEvent;
        if (this.unmutedByHold || this.remoteVideoState == 2 || this.videoState[0] == 2 || sensorEvent.sensor.getType() != 8) {
            return;
        }
        AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        if (this.audioRouteToSet != 0 || this.isHeadsetPlugged || voipAudioManager.isSpeakerphoneOn()) {
            return;
        }
        if (isBluetoothHeadsetConnected() && audioManager.isBluetoothScoOn()) {
            return;
        }
        boolean z = sensorEvent.values[0] < Math.min(sensorEvent.sensor.getMaximumRange(), 3.0f);
        checkIsNear(z);
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.nearEarEvent, Boolean.valueOf(z));
    }

    @Override // org.telegram.messenger.voip.VoIPController.ConnectionStateListener
    public void onSignalBarCountChanged(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda119
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$onSignalBarCountChanged$92(i);
            }
        });
    }

    public void onSignalingData(TLRPC.TL_updatePhoneCallSignalingData tL_updatePhoneCallSignalingData) {
        NativeInstance nativeInstance;
        if (this.user == null || (nativeInstance = this.tgVoip[0]) == null || nativeInstance.isGroup() || getCallID() != tL_updatePhoneCallSignalingData.phone_call_id) {
            return;
        }
        this.tgVoip[0].onSignalingDataReceive(tL_updatePhoneCallSignalingData.data);
    }

    public void onSignalingData(byte[] bArr) {
        if (this.privateCall == null) {
            return;
        }
        TL_phone.sendSignalingData sendsignalingdata = new TL_phone.sendSignalingData();
        TLRPC.TL_inputPhoneCall tL_inputPhoneCall = new TLRPC.TL_inputPhoneCall();
        sendsignalingdata.peer = tL_inputPhoneCall;
        TL_phone.PhoneCall phoneCall = this.privateCall;
        tL_inputPhoneCall.access_hash = phoneCall.access_hash;
        tL_inputPhoneCall.id = phoneCall.id;
        sendsignalingdata.data = bArr;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(sendsignalingdata, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0118 A[LOOP:0: B:23:0x0113->B:25:0x0118, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x012b A[EDGE_INSN: B:26:0x012b->B:27:0x012b BREAK  A[LOOP:0: B:23:0x0113->B:25:0x0118], EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01d3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int onStartCommand(Intent intent, int i, int i2) {
        TLRPC.InputPeer tL_inputPeerUser;
        int i3;
        ProxyVideoSink[] proxyVideoSinkArr;
        boolean z;
        boolean z2;
        int i4;
        int checkSelfPermission;
        if (sharedInstance != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Tried to start the VoIP service when it's already started");
            }
            return 2;
        }
        int intExtra = intent.getIntExtra("account", -1);
        this.currentAccount = intExtra;
        if (intExtra == -1) {
            throw new IllegalStateException("No account specified when starting VoIP service");
        }
        this.classGuid = ConnectionsManager.generateClassGuid();
        long longExtra = intent.getLongExtra("user_id", 0L);
        long longExtra2 = intent.getLongExtra("chat_id", 0L);
        this.createGroupCall = intent.getBooleanExtra("createGroupCall", false);
        this.hasFewPeers = intent.getBooleanExtra("hasFewPeers", false);
        this.isRtmpStream = intent.getBooleanExtra("isRtmpStream", false);
        this.joinHash = intent.getStringExtra("hash");
        long longExtra3 = intent.getLongExtra("peerChannelId", 0L);
        long longExtra4 = intent.getLongExtra("peerChatId", 0L);
        long longExtra5 = intent.getLongExtra("peerUserId", 0L);
        if (longExtra4 != 0) {
            tL_inputPeerUser = new TLRPC.TL_inputPeerChat();
            this.groupCallPeer = tL_inputPeerUser;
            tL_inputPeerUser.chat_id = longExtra4;
        } else {
            if (longExtra3 == 0) {
                if (longExtra5 != 0) {
                    tL_inputPeerUser = new TLRPC.TL_inputPeerUser();
                    this.groupCallPeer = tL_inputPeerUser;
                    tL_inputPeerUser.user_id = longExtra5;
                }
                this.scheduleDate = intent.getIntExtra("scheduleDate", 0);
                this.isOutgoing = intent.getBooleanExtra("is_outgoing", false);
                this.videoCall = intent.getBooleanExtra("video_call", false);
                this.isVideoAvailable = intent.getBooleanExtra("can_video_call", false);
                this.notificationsDisabled = intent.getBooleanExtra("notifications_disabled", false);
                this.instantAccept = intent.getBooleanExtra("accept", false);
                boolean booleanExtra = intent.getBooleanExtra("openFragment", false);
                if (longExtra != 0) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(longExtra));
                }
                if (longExtra2 != 0) {
                    TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(longExtra2));
                    this.chat = chat;
                    if (ChatObject.isChannel(chat)) {
                        MessagesController.getInstance(this.currentAccount).startShortPoll(this.chat, this.classGuid, false);
                    }
                }
                loadResources();
                i3 = 0;
                while (true) {
                    proxyVideoSinkArr = this.localSink;
                    if (i3 >= proxyVideoSinkArr.length) {
                        proxyVideoSinkArr[i3] = new ProxyVideoSink();
                        this.remoteSink[i3] = new ProxyVideoSink();
                        i3++;
                    } else {
                        try {
                            break;
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                }
                this.isHeadsetPlugged = ((AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND)).isWiredHeadsetOn();
                if (this.chat == null && !this.createGroupCall && MessagesController.getInstance(this.currentAccount).getGroupCall(this.chat.id, false) == null) {
                    FileLog.w("VoIPService: trying to open group call without call " + this.chat.id);
                    stopSelf();
                    return 2;
                }
                if (this.videoCall) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        checkSelfPermission = checkSelfPermission("android.permission.CAMERA");
                        if (checkSelfPermission != 0) {
                            i4 = 0;
                            this.videoState[0] = 1;
                            if (!this.isBtHeadsetConnected && !this.isHeadsetPlugged) {
                                setAudioOutput(i4);
                            }
                        }
                    }
                    i4 = 0;
                    this.captureDevice[0] = NativeInstance.createVideoCapturer(this.localSink[0], this.isFrontFaceCamera ? 1 : 0);
                    if (longExtra2 != 0) {
                        this.videoState[0] = 1;
                    } else {
                        this.videoState[0] = 2;
                    }
                    if (!this.isBtHeadsetConnected) {
                        setAudioOutput(i4);
                    }
                }
                if (this.user != null && this.chat == null) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.w("VoIPService: user == null AND chat == null");
                    }
                    stopSelf();
                    return 2;
                }
                sharedInstance = this;
                FileLog.e("(4) set sharedInstance = this");
                synchronized (sync) {
                    try {
                        if (setModeRunnable != null) {
                            Utilities.globalQueue.cancelRunnable(setModeRunnable);
                            setModeRunnable = null;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (this.isOutgoing) {
                    if (this.user != null) {
                        dispatchStateChanged(14);
                        if (USE_CONNECTION_SERVICE) {
                            TelecomManager m = VoIPService$$ExternalSyntheticApiModelOutline5.m(getSystemService("telecom"));
                            Bundle bundle = new Bundle();
                            Bundle bundle2 = new Bundle();
                            bundle.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", addAccountToTelecomManager());
                            bundle2.putInt("call_type", 1);
                            bundle.putBundle("android.telecom.extra.OUTGOING_CALL_EXTRAS", bundle2);
                            ContactsController contactsController = ContactsController.getInstance(this.currentAccount);
                            TLRPC.User user = this.user;
                            contactsController.createOrUpdateConnectionServiceContact(user.id, user.first_name, user.last_name);
                            m.placeCall(Uri.fromParts("tel", "+99084" + this.user.id, null), bundle);
                        } else {
                            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda56
                                @Override // java.lang.Runnable
                                public final void run() {
                                    VoIPService.this.lambda$onStartCommand$1();
                                }
                            };
                            this.delayedStartOutgoingCall = runnable;
                            AndroidUtilities.runOnUIThread(runnable, 2000L);
                        }
                        z2 = false;
                    } else {
                        this.micMute = true;
                        z2 = false;
                        startGroupCall(0, null, false);
                        if (!this.isBtHeadsetConnected && !this.isHeadsetPlugged) {
                            setAudioOutput(0);
                        }
                    }
                    if (intent.getBooleanExtra("start_incall_activity", z2)) {
                        Intent addFlags = new Intent(this, (Class<?>) LaunchActivity.class).setAction(this.user != null ? "voip" : "voip_chat").addFlags(268435456);
                        if (this.chat != null) {
                            addFlags.putExtra("currentAccount", this.currentAccount);
                        }
                        startActivity(addFlags);
                    }
                } else {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeInCallActivity, new Object[0]);
                    TL_phone.PhoneCall phoneCall = callIShouldHavePutIntoIntent;
                    this.privateCall = phoneCall;
                    boolean z3 = phoneCall != null && phoneCall.video;
                    this.videoCall = z3;
                    if (z3) {
                        this.isVideoAvailable = true;
                    }
                    if (!z3 || this.isBtHeadsetConnected || this.isHeadsetPlugged) {
                        z = false;
                    } else {
                        z = false;
                        setAudioOutput(0);
                    }
                    callIShouldHavePutIntoIntent = null;
                    FileLog.e("(3) set VoIPService.callIShouldHavePutIntoIntent = null");
                    if (this.instantAccept) {
                        this.instantAccept = z;
                        acceptIncomingCall();
                    } else if (USE_CONNECTION_SERVICE) {
                        acknowledgeCall(z);
                        showNotification();
                    } else {
                        acknowledgeCall(true);
                    }
                    if (booleanExtra) {
                        Activity findActivity = AndroidUtilities.findActivity(this);
                        if (findActivity == null) {
                            findActivity = LaunchActivity.instance;
                        }
                        if (findActivity == null) {
                            findActivity = AndroidUtilities.findActivity(ApplicationLoader.applicationContext);
                        }
                        if (findActivity != null) {
                            VoIPFragment.show(findActivity, this.currentAccount);
                        }
                    }
                }
                initializeAccountRelatedThings();
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda57
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$onStartCommand$2();
                    }
                });
                return 2;
            }
            tL_inputPeerUser = new TLRPC.TL_inputPeerChannel();
            this.groupCallPeer = tL_inputPeerUser;
            tL_inputPeerUser.channel_id = longExtra3;
        }
        tL_inputPeerUser.access_hash = intent.getLongExtra("peerAccessHash", 0L);
        this.scheduleDate = intent.getIntExtra("scheduleDate", 0);
        this.isOutgoing = intent.getBooleanExtra("is_outgoing", false);
        this.videoCall = intent.getBooleanExtra("video_call", false);
        this.isVideoAvailable = intent.getBooleanExtra("can_video_call", false);
        this.notificationsDisabled = intent.getBooleanExtra("notifications_disabled", false);
        this.instantAccept = intent.getBooleanExtra("accept", false);
        boolean booleanExtra2 = intent.getBooleanExtra("openFragment", false);
        if (longExtra != 0) {
        }
        if (longExtra2 != 0) {
        }
        loadResources();
        i3 = 0;
        while (true) {
            proxyVideoSinkArr = this.localSink;
            if (i3 >= proxyVideoSinkArr.length) {
            }
            proxyVideoSinkArr[i3] = new ProxyVideoSink();
            this.remoteSink[i3] = new ProxyVideoSink();
            i3++;
        }
        this.isHeadsetPlugged = ((AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND)).isWiredHeadsetOn();
        if (this.chat == null) {
        }
        if (this.videoCall) {
        }
        if (this.user != null) {
        }
        sharedInstance = this;
        FileLog.e("(4) set sharedInstance = this");
        synchronized (sync) {
        }
    }

    public void playAllowTalkSound() {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda115
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$playAllowTalkSound$91();
            }
        });
    }

    public void playConnectedSound() {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda103
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$playConnectedSound$58();
            }
        });
        this.playedConnectedSound = true;
    }

    public void playStartRecordSound() {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda114
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$playStartRecordSound$90();
            }
        });
    }

    public void registerStateListener(StateListener stateListener) {
        if (this.stateListeners.contains(stateListener)) {
            return;
        }
        this.stateListeners.add(stateListener);
        int i = this.currentState;
        if (i != 0) {
            stateListener.onStateChanged(i);
        }
        int i2 = this.signalBarCount;
        if (i2 != 0) {
            stateListener.onSignalBarsCountChanged(i2);
        }
    }

    public void removeRemoteSink(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, boolean z) {
        ProxyVideoSink remove;
        NativeInstance nativeInstance;
        if (z) {
            remove = this.remoteSinks.remove(tL_groupCallParticipant.presentationEndpoint);
            if (remove == null) {
                return;
            } else {
                nativeInstance = this.tgVoip[0];
            }
        } else {
            remove = this.remoteSinks.remove(tL_groupCallParticipant.videoEndpoint);
            if (remove == null) {
                return;
            } else {
                nativeInstance = this.tgVoip[0];
            }
        }
        nativeInstance.removeIncomingVideoOutput(remove.nativeInstance);
    }

    public void requestFullScreen(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, boolean z, boolean z2) {
        NativeInstance nativeInstance;
        int i;
        String str = z2 ? tL_groupCallParticipant.presentationEndpoint : tL_groupCallParticipant.videoEndpoint;
        if (str == null) {
            return;
        }
        if (z) {
            nativeInstance = this.tgVoip[0];
            i = 2;
        } else {
            nativeInstance = this.tgVoip[0];
            i = 1;
        }
        nativeInstance.setVideoEndpointQuality(str, i);
    }

    public void requestVideoCall(boolean z) {
        int i = 0;
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance == null) {
            return;
        }
        if (!z) {
            long j = this.captureDevice[0];
            if (j != 0) {
                nativeInstance.setupOutgoingVideoCreated(j);
                this.destroyCaptureDevice[0] = false;
                this.isPrivateScreencast = z;
            }
        }
        ProxyVideoSink proxyVideoSink = this.localSink[0];
        if (z) {
            i = 2;
        } else if (this.isFrontFaceCamera) {
            i = 1;
        }
        nativeInstance.setupOutgoingVideo(proxyVideoSink, i);
        this.isPrivateScreencast = z;
    }

    public void sendCallRating(int i) {
        TL_phone.PhoneCall phoneCall = this.privateCall;
        VoIPHelper.sendCallRating(phoneCall.id, phoneCall.access_hash, this.currentAccount, i);
    }

    public void setAudioOutput(int i) {
        CallConnection callConnection;
        int i2;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("setAudioOutput " + i);
        }
        AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        boolean z = USE_CONNECTION_SERVICE;
        if (z && (callConnection = this.systemCallConnection) != null) {
            if (i == 0) {
                i2 = 8;
            } else if (i == 1) {
                i2 = 5;
            } else if (i == 2) {
                callConnection.setAudioRoute(2);
            }
            callConnection.setAudioRoute(i2);
        } else if (this.audioConfigured && !z) {
            if (i == 0) {
                this.needSwitchToBluetoothAfterScoActivates = false;
                if (this.bluetoothScoActive || this.bluetoothScoConnecting) {
                    audioManager.stopBluetoothSco();
                    this.bluetoothScoActive = false;
                    this.bluetoothScoConnecting = false;
                }
                audioManager.setBluetoothScoOn(false);
                voipAudioManager.setSpeakerphoneOn(true);
                this.audioRouteToSet = 1;
            } else if (i == 1) {
                this.needSwitchToBluetoothAfterScoActivates = false;
                if (this.bluetoothScoActive || this.bluetoothScoConnecting) {
                    audioManager.stopBluetoothSco();
                    this.bluetoothScoActive = false;
                    this.bluetoothScoConnecting = false;
                }
                voipAudioManager.setSpeakerphoneOn(false);
                audioManager.setBluetoothScoOn(false);
                this.audioRouteToSet = 0;
            } else if (i == 2) {
                if (this.bluetoothScoActive) {
                    audioManager.setBluetoothScoOn(true);
                    voipAudioManager.setSpeakerphoneOn(false);
                } else {
                    this.needSwitchToBluetoothAfterScoActivates = true;
                    try {
                        audioManager.startBluetoothSco();
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                this.audioRouteToSet = 2;
            }
            updateOutputGainControlState();
        } else if (i != 0) {
            if (i == 1) {
                this.audioRouteToSet = 0;
            } else if (i == 2) {
                this.audioRouteToSet = 2;
            }
            this.speakerphoneStateToSet = false;
        } else {
            this.audioRouteToSet = 1;
            this.speakerphoneStateToSet = true;
        }
        Iterator<StateListener> it = this.stateListeners.iterator();
        while (it.hasNext()) {
            it.next().onAudioSettingsChanged();
        }
    }

    public void setAudioRoute(int i) {
        int i2 = 1;
        if (i == 1) {
            setAudioOutput(0);
            return;
        }
        if (i != 0) {
            i2 = 2;
            if (i != 2) {
                return;
            }
        }
        setAudioOutput(i2);
    }

    public void setBackgroundSinks(VideoSink videoSink, VideoSink videoSink2) {
        this.localSink[0].setBackground(videoSink);
        this.remoteSink[0].setBackground(videoSink2);
    }

    public void setGroupCallHash(String str) {
        if (!this.currentGroupModeStreaming || TextUtils.isEmpty(str) || str.equals(this.joinHash)) {
            return;
        }
        this.joinHash = str;
        createGroupInstance(0, false);
    }

    public void setGroupCallPeer(TLRPC.InputPeer inputPeer) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ChatObject.Call call = this.groupCall;
        if (call == null) {
            return;
        }
        this.groupCallPeer = inputPeer;
        call.setSelfPeer(inputPeer);
        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.groupCall.chatId);
        if (chatFull != null) {
            TLRPC.Peer peer = this.groupCall.selfPeer;
            chatFull.groupcall_default_join_as = peer;
            if (peer != null) {
                if (chatFull instanceof TLRPC.TL_chatFull) {
                    i4 = chatFull.flags;
                    i5 = 32768;
                } else {
                    i4 = chatFull.flags;
                    i5 = ConnectionsManager.FileTypeFile;
                }
                i3 = i4 | i5;
            } else {
                if (chatFull instanceof TLRPC.TL_chatFull) {
                    i = chatFull.flags;
                    i2 = -32769;
                } else {
                    i = chatFull.flags;
                    i2 = -67108865;
                }
                i3 = i & i2;
            }
            chatFull.flags = i3;
        }
        createGroupInstance(0, true);
        if (this.videoState[1] == 2) {
            createGroupInstance(1, true);
        }
    }

    public void setLocalSink(VideoSink videoSink, boolean z) {
        if (z) {
            return;
        }
        this.localSink[0].setTarget(videoSink);
    }

    public void setMicMute(boolean z, boolean z2, boolean z3) {
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant;
        if (this.micMute == z || this.micSwitching) {
            return;
        }
        this.micMute = z;
        ChatObject.Call call = this.groupCall;
        if (call != null) {
            if (!z3 && (tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) call.participants.get(getSelfId())) != null && tL_groupCallParticipant.muted && !tL_groupCallParticipant.can_self_unmute) {
                z3 = true;
            }
            if (z3) {
                editCallMember(UserConfig.getInstance(this.currentAccount).getCurrentUser(), Boolean.valueOf(z), null, null, null, null);
                DispatchQueue dispatchQueue = Utilities.globalQueue;
                Runnable runnable = new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda42
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPService.this.lambda$setMicMute$0();
                    }
                };
                this.updateNotificationRunnable = runnable;
                dispatchQueue.postRunnable(runnable);
            }
        }
        this.unmutedByHold = !this.micMute && z2;
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance != null) {
            nativeInstance.setMuteMicrophone(z);
        }
        Iterator<StateListener> it = this.stateListeners.iterator();
        while (it.hasNext()) {
            it.next().onAudioSettingsChanged();
        }
    }

    public void setNoiseSupressionEnabled(boolean z) {
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance == null) {
            return;
        }
        nativeInstance.setNoiseSuppressionEnabled(z);
    }

    public void setParticipantVolume(TLRPC.TL_groupCallParticipant tL_groupCallParticipant, int i) {
        int i2;
        NativeInstance nativeInstance = this.tgVoip[0];
        int i3 = tL_groupCallParticipant.source;
        double d = i;
        Double.isNaN(d);
        double d2 = d / 10000.0d;
        nativeInstance.setVolume(i3, d2);
        TLRPC.TL_groupCallParticipantVideo tL_groupCallParticipantVideo = tL_groupCallParticipant.presentation;
        if (tL_groupCallParticipantVideo == null || (i2 = tL_groupCallParticipantVideo.audio_source) == 0) {
            return;
        }
        this.tgVoip[0].setVolume(i2, d2);
    }

    public void setParticipantsVolume() {
        if (this.tgVoip[0] != null) {
            int size = this.groupCall.participants.size();
            for (int i = 0; i < size; i++) {
                TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) this.groupCall.participants.valueAt(i);
                if (!tL_groupCallParticipant.self && tL_groupCallParticipant.source != 0 && (tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted)) {
                    if (tL_groupCallParticipant.muted_by_you) {
                        setParticipantVolume(tL_groupCallParticipant, 0);
                    } else {
                        setParticipantVolume(tL_groupCallParticipant, ChatObject.getParticipantVolume(tL_groupCallParticipant));
                    }
                }
            }
        }
    }

    public void setRemoteSink(VideoSink videoSink, boolean z) {
        this.remoteSink[z ? 1 : 0].setTarget(videoSink);
    }

    public void setSinks(VideoSink videoSink, VideoSink videoSink2) {
        setSinks(videoSink, false, videoSink2);
    }

    public void setSinks(VideoSink videoSink, boolean z, VideoSink videoSink2) {
        ProxyVideoSink proxyVideoSink = this.localSink[z ? 1 : 0];
        ProxyVideoSink proxyVideoSink2 = this.remoteSink[z ? 1 : 0];
        if (proxyVideoSink != null) {
            proxyVideoSink.setTarget(videoSink);
        }
        if (proxyVideoSink2 != null) {
            proxyVideoSink2.setTarget(videoSink2);
        }
    }

    protected void setSwitchingCamera(boolean z, boolean z2) {
        this.switchingCamera = z;
        if (z) {
            return;
        }
        this.isFrontFaceCamera = z2;
        for (int i = 0; i < this.stateListeners.size(); i++) {
            this.stateListeners.get(i).onCameraSwitch(this.isFrontFaceCamera);
        }
    }

    public void setVideoState(boolean z, int i) {
        int i2;
        char c = this.groupCall != null ? z ? 1 : 0 : (char) 0;
        NativeInstance nativeInstance = this.tgVoip[c];
        if (nativeInstance != null) {
            this.videoState[c] = i;
            nativeInstance.setVideoState(i);
            long j = this.captureDevice[z ? 1 : 0];
            if (j != 0) {
                NativeInstance.setVideoStateCapturer(j, this.videoState[c]);
            }
            if (z) {
                return;
            }
            if (this.groupCall != null) {
                editCallMember(UserConfig.getInstance(this.currentAccount).getCurrentUser(), null, Boolean.valueOf(this.videoState[0] != 2), null, null, null);
            }
            checkIsNear();
            return;
        }
        long[] jArr = this.captureDevice;
        long j2 = jArr[z ? 1 : 0];
        if (j2 != 0) {
            this.videoState[c] = i;
            NativeInstance.setVideoStateCapturer(j2, i);
        } else {
            if (i != 2 || (i2 = this.currentState) == 17 || i2 == 11) {
                return;
            }
            jArr[z ? 1 : 0] = NativeInstance.createVideoCapturer(this.localSink[c], this.isFrontFaceCamera ? 1 : 0);
            this.videoState[c] = 2;
        }
    }

    public void setupCaptureDevice(boolean z, boolean z2) {
        NativeInstance nativeInstance;
        if (!z) {
            long j = this.captureDevice[z ? 1 : 0];
            if (j == 0 || (nativeInstance = this.tgVoip[z ? 1 : 0]) == null) {
                return;
            }
            nativeInstance.setupOutgoingVideoCreated(j);
            this.destroyCaptureDevice[z ? 1 : 0] = false;
            this.videoState[z ? 1 : 0] = 2;
        }
        if (this.micMute == z2) {
            setMicMute(!z2, false, false);
            this.micSwitching = true;
        }
        if (this.groupCall != null) {
            editCallMember(UserConfig.getInstance(this.currentAccount).getCurrentUser(), Boolean.valueOf(!z2), Boolean.valueOf(this.videoState[0] != 2), null, null, new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda55
                @Override // java.lang.Runnable
                public final void run() {
                    VoIPService.this.lambda$setupCaptureDevice$13();
                }
            });
        }
    }

    public void startRingtoneAndVibration() {
        if (this.startedRinging) {
            return;
        }
        startRingtoneAndVibration(this.user.id);
        this.startedRinging = true;
    }

    @Override // org.telegram.messenger.voip.VoIPServiceState
    public void stopRinging() {
        synchronized (sync) {
            try {
                MediaPlayer mediaPlayer = this.ringtonePlayer;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    this.ringtonePlayer.release();
                    this.ringtonePlayer = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Vibrator vibrator = this.vibrator;
        if (vibrator != null) {
            vibrator.cancel();
            this.vibrator = null;
        }
    }

    public void stopScreenCapture() {
        if (this.groupCall == null || this.videoState[1] != 2) {
            return;
        }
        TL_phone.leaveGroupCallPresentation leavegroupcallpresentation = new TL_phone.leaveGroupCallPresentation();
        leavegroupcallpresentation.call = this.groupCall.getInputGroupCall();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(leavegroupcallpresentation, new RequestDelegate() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda86
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                VoIPService.this.lambda$stopScreenCapture$14(tLObject, tL_error);
            }
        });
        NativeInstance nativeInstance = this.tgVoip[1];
        if (nativeInstance != null) {
            Utilities.globalQueue.postRunnable(new VoIPService$$ExternalSyntheticLambda81(nativeInstance));
        }
        this.mySource[1] = 0;
        this.tgVoip[1] = null;
        this.destroyCaptureDevice[1] = true;
        this.captureDevice[1] = 0;
        this.videoState[1] = 0;
        AccountInstance.getInstance(this.currentAccount).getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupCallScreencastStateChanged, new Object[0]);
    }

    public void swapSinks() {
        this.localSink[0].swap();
        this.remoteSink[0].swap();
    }

    public void switchCamera() {
        NativeInstance nativeInstance = this.tgVoip[0];
        if (nativeInstance != null && nativeInstance.hasVideoCapturer() && !this.switchingCamera) {
            this.switchingCamera = true;
            this.tgVoip[0].switchCamera(!this.isFrontFaceCamera);
            return;
        }
        long j = this.captureDevice[0];
        if (j == 0 || this.switchingCamera) {
            return;
        }
        NativeInstance.switchCameraCapturer(j, !this.isFrontFaceCamera);
    }

    public void switchToSpeaker() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                VoIPService.this.lambda$switchToSpeaker$62();
            }
        }, 500L);
    }

    public void toggleSpeakerphoneOrShowRouteSheet(Context context, boolean z) {
        toggleSpeakerphoneOrShowRouteSheet(context, z, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b7, code lost:
    
        if (r9 == 8) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00c7, code lost:
    
        r8.setAudioRoute(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c6, code lost:
    
        r0 = 5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c4, code lost:
    
        if (r9 == 2) goto L45;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void toggleSpeakerphoneOrShowRouteSheet(Context context, boolean z, final Integer num) {
        CallConnection callConnection;
        CallAudioState callAudioState;
        CallConnection callConnection2;
        CallAudioState callAudioState2;
        int route;
        CallAudioState callAudioState3;
        int route2;
        Window window;
        int i;
        int i2 = 2;
        if (isBluetoothHeadsetConnected() && hasEarpiece()) {
            BottomSheet.Builder cellType = new BottomSheet.Builder(context).setTitle(LocaleController.getString(R.string.VoipOutputDevices), true).selectedPos(num).setCellType(num != null ? BottomSheet.Builder.CELL_TYPE_CALL : 0);
            String string = LocaleController.getString(R.string.VoipAudioRoutingSpeaker);
            String string2 = LocaleController.getString(this.isHeadsetPlugged ? R.string.VoipAudioRoutingHeadset : R.string.VoipAudioRoutingEarpiece);
            String str = this.currentBluetoothDeviceName;
            if (str == null) {
                str = LocaleController.getString(R.string.VoipAudioRoutingBluetooth);
            }
            BottomSheet.Builder items = cellType.setItems(new CharSequence[]{string, string2, str}, new int[]{R.drawable.msg_call_speaker, this.isHeadsetPlugged ? R.drawable.calls_menu_headset : R.drawable.msg_call_earpiece, R.drawable.msg_call_bluetooth}, new DialogInterface.OnClickListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda92
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    VoIPService.this.lambda$toggleSpeakerphoneOrShowRouteSheet$63(dialogInterface, i3);
                }
            });
            final BottomSheet create = items.create();
            create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda93
                @Override // android.content.DialogInterface.OnShowListener
                public final void onShow(DialogInterface dialogInterface) {
                    VoIPService.lambda$toggleSpeakerphoneOrShowRouteSheet$64(BottomSheet.this, num, dialogInterface);
                }
            });
            if (z) {
                if (Build.VERSION.SDK_INT >= 26) {
                    window = create.getWindow();
                    i = 2038;
                } else {
                    window = create.getWindow();
                    i = 2003;
                }
                window.setType(i);
            }
            items.show();
            return;
        }
        boolean z2 = USE_CONNECTION_SERVICE;
        if (z2 && (callConnection = this.systemCallConnection) != null) {
            callAudioState = callConnection.getCallAudioState();
            if (callAudioState != null) {
                if (hasEarpiece()) {
                    callConnection2 = this.systemCallConnection;
                    callAudioState3 = callConnection2.getCallAudioState();
                    route2 = callAudioState3.getRoute();
                    i2 = 8;
                } else {
                    callConnection2 = this.systemCallConnection;
                    callAudioState2 = callConnection2.getCallAudioState();
                    route = callAudioState2.getRoute();
                }
            }
        }
        if (!this.audioConfigured || z2) {
            this.speakerphoneStateToSet = !this.speakerphoneStateToSet;
            Iterator<StateListener> it = this.stateListeners.iterator();
            while (it.hasNext()) {
                it.next().onAudioSettingsChanged();
            }
            return;
        }
        AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        VoipAudioManager voipAudioManager = VoipAudioManager.get();
        if (hasEarpiece()) {
            voipAudioManager.setSpeakerphoneOn(!voipAudioManager.isSpeakerphoneOn());
        } else {
            audioManager.setBluetoothScoOn(!audioManager.isBluetoothScoOn());
        }
        voipAudioManager.isBluetoothAndSpeakerOnAsync(new Utilities.Callback2() { // from class: org.telegram.messenger.voip.VoIPService$$ExternalSyntheticLambda94
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                VoIPService.this.lambda$toggleSpeakerphoneOrShowRouteSheet$65((Boolean) obj, (Boolean) obj2);
            }
        });
    }

    public void unregisterStateListener(StateListener stateListener) {
        this.stateListeners.remove(stateListener);
    }

    public void updateCurrentForegroundType() {
        if (this.lastForegroundType == getCurrentForegroundType() || !this.foregroundStarted) {
            return;
        }
        stopForeground(true);
        if (Build.VERSION.SDK_INT < 33) {
            startForeground(this.foregroundId, this.foregroundNotification);
            return;
        }
        int i = this.foregroundId;
        Notification notification = this.foregroundNotification;
        int currentForegroundType = getCurrentForegroundType();
        this.lastForegroundType = currentForegroundType;
        startForeground(i, notification, currentForegroundType);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updateOutputGainControlState() {
        CallAudioState callAudioState;
        int route;
        if (hasRtmpStream()) {
            return;
        }
        int i = 0;
        if (this.tgVoip[0] != null) {
            if (USE_CONNECTION_SERVICE) {
                callAudioState = this.systemCallConnection.getCallAudioState();
                route = callAudioState.getRoute();
                boolean z = route == 1 ? 1 : 0;
                this.tgVoip[0].setAudioOutputGainControlEnabled(z);
                this.tgVoip[0].setEchoCancellationStrength(!z);
                return;
            }
            AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            boolean isSpeakerphoneOn = VoipAudioManager.get().isSpeakerphoneOn();
            this.tgVoip[0].setAudioOutputGainControlEnabled((!hasEarpiece() || isSpeakerphoneOn || audioManager.isBluetoothScoOn() || this.isHeadsetPlugged) ? false : true);
            NativeInstance nativeInstance = this.tgVoip[0];
            if (!this.isHeadsetPlugged && (!hasEarpiece() || isSpeakerphoneOn || audioManager.isBluetoothScoOn() || this.isHeadsetPlugged)) {
                i = 1;
            }
            nativeInstance.setEchoCancellationStrength(i);
        }
    }
}
