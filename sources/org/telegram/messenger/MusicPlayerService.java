package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import java.io.File;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.LaunchActivity;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class MusicPlayerService extends Service implements NotificationCenter.NotificationCenterDelegate {
    private static final int ID_NOTIFICATION = 5;
    public static final String NOTIFY_CLOSE = "org.telegram.android.musicplayer.close";
    public static final String NOTIFY_NEXT = "org.telegram.android.musicplayer.next";
    public static final String NOTIFY_PAUSE = "org.telegram.android.musicplayer.pause";
    public static final String NOTIFY_PLAY = "org.telegram.android.musicplayer.play";
    public static final String NOTIFY_PREVIOUS = "org.telegram.android.musicplayer.previous";
    public static final String NOTIFY_SEEK = "org.telegram.android.musicplayer.seek";
    private static boolean supportBigNotifications;
    private static boolean supportLockScreenControls;
    private Bitmap albumArtPlaceholder;
    private AudioManager audioManager;
    private boolean foregroundServiceIsStarted;
    private BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver() { // from class: org.telegram.messenger.MusicPlayerService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.media.AUDIO_BECOMING_NOISY".equals(intent.getAction())) {
                MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
            }
        }
    };
    private ImageReceiver imageReceiver;
    private String loadingFilePath;
    private MediaSession mediaSession;
    private int notificationMessageID;
    private PlaybackState.Builder playbackState;
    private RemoteControlClient remoteControlClient;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    static {
        int i = Build.VERSION.SDK_INT;
        boolean z = true;
        supportBigNotifications = i >= 16;
        if (i >= 21 && TextUtils.isEmpty(AndroidUtilities.getSystemProperty("ro.miui.ui.version.code"))) {
            z = false;
        }
        supportLockScreenControls = z;
    }

    @Override // android.app.Service
    public void onCreate() {
        this.audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileLoaded);
        }
        ImageReceiver imageReceiver = new ImageReceiver(null);
        this.imageReceiver = imageReceiver;
        imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.messenger.MusicPlayerService$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver2, boolean z, boolean z2, boolean z3) {
                MusicPlayerService.this.lambda$onCreate$0(imageReceiver2, z, z2, z3);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void didSetImageBitmap(int i2, String str, Drawable drawable) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i2, str, drawable);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver2) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver2);
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            this.mediaSession = new MediaSession(this, "telegramAudioPlayer");
            this.playbackState = new PlaybackState.Builder();
            this.albumArtPlaceholder = Bitmap.createBitmap(AndroidUtilities.dp(102.0f), AndroidUtilities.dp(102.0f), Bitmap.Config.ARGB_8888);
            Drawable drawable = getResources().getDrawable(R.drawable.nocover_big);
            drawable.setBounds(0, 0, this.albumArtPlaceholder.getWidth(), this.albumArtPlaceholder.getHeight());
            drawable.draw(new Canvas(this.albumArtPlaceholder));
            this.mediaSession.setCallback(new MediaSession.Callback() { // from class: org.telegram.messenger.MusicPlayerService.2
                @Override // android.media.session.MediaSession.Callback
                public void onStop() {
                }

                @Override // android.media.session.MediaSession.Callback
                public void onPlay() {
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                }

                @Override // android.media.session.MediaSession.Callback
                public void onPause() {
                    MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
                }

                @Override // android.media.session.MediaSession.Callback
                public void onSkipToNext() {
                    MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject == null || !playingMessageObject.isMusic()) {
                        return;
                    }
                    MediaController.getInstance().playNextMessage();
                }

                @Override // android.media.session.MediaSession.Callback
                public void onSkipToPrevious() {
                    MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject == null || !playingMessageObject.isMusic()) {
                        return;
                    }
                    MediaController.getInstance().playPreviousMessage();
                }

                @Override // android.media.session.MediaSession.Callback
                public void onSeekTo(long j) {
                    MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject != null) {
                        MediaController.getInstance().seekToProgress(playingMessageObject, ((float) (j / 1000)) / ((float) playingMessageObject.getDuration()));
                        MusicPlayerService.this.updatePlaybackState(j);
                    }
                }
            });
            this.mediaSession.setActive(true);
        }
        registerReceiver(this.headsetPlugReceiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
        super.onCreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        if (!z || TextUtils.isEmpty(this.loadingFilePath)) {
            return;
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            createNotification(playingMessageObject, true);
        }
        this.loadingFilePath = null;
    }

    @Override // android.app.Service
    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            try {
                if ((getPackageName() + ".STOP_PLAYER").equals(intent.getAction())) {
                    MediaController.getInstance().cleanupPlayer(true, true);
                    return 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MusicPlayerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MusicPlayerService.this.stopSelf();
                }
            });
            return 1;
        }
        if (supportLockScreenControls) {
            ComponentName componentName = new ComponentName(getApplicationContext(), MusicPlayerReceiver.class.getName());
            try {
                if (this.remoteControlClient == null) {
                    this.audioManager.registerMediaButtonEventReceiver(componentName);
                    Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
                    intent2.setComponent(componentName);
                    RemoteControlClient remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(this, 0, intent2, fixIntentFlags(ConnectionsManager.FileTypeVideo)));
                    this.remoteControlClient = remoteControlClient;
                    this.audioManager.registerRemoteControlClient(remoteControlClient);
                }
                this.remoteControlClient.setTransportControlFlags(189);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        createNotification(playingMessageObject, false);
        return 1;
    }

    private Bitmap loadArtworkFromUrl(String str, boolean z, boolean z2) {
        ImageLoader.getHttpFileName(str);
        File httpFilePath = ImageLoader.getHttpFilePath(str, "jpg");
        if (httpFilePath.exists()) {
            return ImageLoader.loadBitmap(httpFilePath.getAbsolutePath(), null, z ? 600.0f : 100.0f, z ? 600.0f : 100.0f, false);
        }
        if (z2) {
            this.loadingFilePath = httpFilePath.getAbsolutePath();
            if (!z) {
                this.imageReceiver.setImage(str, "48_48", null, null, 0L);
            }
        } else {
            this.loadingFilePath = null;
        }
        return null;
    }

    private Bitmap getAvatarBitmap(TLObject tLObject, boolean z, boolean z2) {
        AvatarDrawable avatarDrawable;
        int i = z ? 600 : 100;
        try {
            if (tLObject instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                TLRPC$FileLocation tLRPC$FileLocation = z ? tLRPC$User.photo.photo_big : tLRPC$User.photo.photo_small;
                if (tLRPC$FileLocation != null) {
                    File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$FileLocation, true);
                    if (pathToAttach.exists()) {
                        float f = i;
                        return ImageLoader.loadBitmap(pathToAttach.getAbsolutePath(), null, f, f, false);
                    } else if (z) {
                        if (z2) {
                            this.loadingFilePath = FileLoader.getAttachFileName(tLRPC$FileLocation);
                            this.imageReceiver.setImage(ImageLocation.getForUser(tLRPC$User, 0), "", null, null, null, 0);
                        } else {
                            this.loadingFilePath = null;
                        }
                    }
                }
            } else {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                TLRPC$FileLocation tLRPC$FileLocation2 = z ? tLRPC$Chat.photo.photo_big : tLRPC$Chat.photo.photo_small;
                if (tLRPC$FileLocation2 != null) {
                    File pathToAttach2 = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$FileLocation2, true);
                    if (pathToAttach2.exists()) {
                        float f2 = i;
                        return ImageLoader.loadBitmap(pathToAttach2.getAbsolutePath(), null, f2, f2, false);
                    } else if (z) {
                        if (z2) {
                            this.loadingFilePath = FileLoader.getAttachFileName(tLRPC$FileLocation2);
                            this.imageReceiver.setImage(ImageLocation.getForChat(tLRPC$Chat, 0), "", null, null, null, 0);
                        } else {
                            this.loadingFilePath = null;
                        }
                    }
                }
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        if (z) {
            return null;
        }
        Theme.createDialogsResources(this);
        if (tLObject instanceof TLRPC$User) {
            avatarDrawable = new AvatarDrawable((TLRPC$User) tLObject);
        } else {
            avatarDrawable = new AvatarDrawable((TLRPC$Chat) tLObject);
        }
        avatarDrawable.setRoundRadius(1);
        float f3 = i;
        Bitmap createBitmap = Bitmap.createBitmap(AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Bitmap.Config.ARGB_8888);
        avatarDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        avatarDrawable.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:141:0x048b  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x064c  */
    /* JADX WARN: Removed duplicated region for block: B:233:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x019e  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01b0  */
    @SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void createNotification(MessageObject messageObject, boolean z) {
        long j;
        Bitmap bitmap;
        Bitmap bitmap2;
        Bitmap avatarBitmap;
        Bitmap bitmap3;
        Bitmap bitmap4;
        int i;
        AudioInfo audioInfo;
        Bitmap bitmap5;
        String str;
        String str2;
        int i2;
        String str3;
        String str4;
        int i3;
        String str5;
        String musicTitle = messageObject.getMusicTitle();
        String musicAuthor = messageObject.getMusicAuthor();
        AudioInfo audioInfo2 = MediaController.getInstance().getAudioInfo();
        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
        if (messageObject.isMusic()) {
            intent.setAction("com.tmessages.openplayer");
            intent.addCategory("android.intent.category.LAUNCHER");
        } else if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            intent.setAction("android.intent.action.VIEW");
            TLRPC$Peer tLRPC$Peer = messageObject.messageOwner.peer_id;
            if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                j = tLRPC$Peer.user_id;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                j = tLRPC$Peer.chat_id;
            } else {
                j = tLRPC$Peer instanceof TLRPC$TL_peerChannel ? tLRPC$Peer.channel_id : 0L;
            }
            if (j != 0) {
                if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                    intent.setData(Uri.parse("tg://openmessage?user_id=" + j + "&message_id=" + messageObject.getId()));
                } else {
                    intent.setData(Uri.parse("tg://openmessage?chat_id=" + j + "&message_id=" + messageObject.getId()));
                }
            }
        }
        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, fixIntentFlags(ConnectionsManager.FileTypeVideo));
        long duration = (long) (messageObject.getDuration() * 1000.0d);
        if (messageObject.isMusic()) {
            String artworkUrl = messageObject.getArtworkUrl(true);
            String artworkUrl2 = messageObject.getArtworkUrl(false);
            bitmap2 = audioInfo2 != null ? audioInfo2.getSmallCover() : null;
            bitmap3 = audioInfo2 != null ? audioInfo2.getCover() : null;
            this.loadingFilePath = null;
            this.imageReceiver.setImageBitmap((Drawable) null);
            if (bitmap2 == null && !TextUtils.isEmpty(artworkUrl)) {
                bitmap3 = loadArtworkFromUrl(artworkUrl2, true, !z);
                if (bitmap3 == null) {
                    bitmap2 = loadArtworkFromUrl(artworkUrl, false, !z);
                    bitmap3 = bitmap2;
                } else {
                    bitmap2 = loadArtworkFromUrl(artworkUrl2, false, !z);
                }
            } else {
                this.loadingFilePath = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(messageObject.getDocument()).getAbsolutePath();
            }
        } else if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            long senderId = messageObject.getSenderId();
            if (messageObject.isFromUser()) {
                TLRPC$User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(senderId));
                if (user != null) {
                    musicTitle = UserObject.getUserName(user);
                    bitmap = getAvatarBitmap(user, true, !z);
                    avatarBitmap = getAvatarBitmap(user, false, !z);
                } else {
                    avatarBitmap = null;
                    bitmap = null;
                }
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(-senderId));
                if (chat != null) {
                    musicTitle = chat.title;
                    bitmap = getAvatarBitmap(chat, true, !z);
                    avatarBitmap = getAvatarBitmap(chat, false, !z);
                } else {
                    bitmap = null;
                    bitmap2 = null;
                    bitmap3 = (bitmap == null || bitmap2 == null) ? bitmap : bitmap2;
                    musicAuthor = !messageObject.isVoice() ? LocaleController.getString("AttachAudio", R.string.AttachAudio) : LocaleController.getString("AttachRound", R.string.AttachRound);
                }
            }
            bitmap2 = avatarBitmap;
            if (bitmap == null) {
            }
            if (!messageObject.isVoice()) {
            }
        } else {
            bitmap4 = null;
            bitmap2 = null;
            i = Build.VERSION.SDK_INT;
            Bitmap bitmap6 = bitmap2;
            if (i < 21) {
                boolean z2 = !MediaController.getInstance().isMessagePaused();
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), fixIntentFlags(301989888));
                PendingIntent service = PendingIntent.getService(getApplicationContext(), 0, new Intent(this, getClass()).setAction(getPackageName() + ".STOP_PLAYER"), fixIntentFlags(301989888));
                PendingIntent broadcast2 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(z2 ? NOTIFY_PAUSE : NOTIFY_PLAY).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), fixIntentFlags(301989888));
                PendingIntent broadcast3 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), fixIntentFlags(301989888));
                Bitmap bitmap7 = bitmap4;
                PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_SEEK).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), fixIntentFlags(301989888));
                Notification.MediaStyle mediaSession = new Notification.MediaStyle().setMediaSession(this.mediaSession.getSessionToken());
                if (messageObject.isMusic()) {
                    mediaSession.setShowActionsInCompactView(0, 1, 2);
                } else if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                    mediaSession.setShowActionsInCompactView(0);
                }
                Notification.Builder builder = new Notification.Builder(this);
                builder.setSmallIcon(R.drawable.player).setOngoing(z2).setContentTitle(musicTitle).setContentText(musicAuthor).setSubText((audioInfo2 == null || !messageObject.isMusic()) ? null : audioInfo2.getAlbum()).setContentIntent(activity).setDeleteIntent(service).setShowWhen(false).setCategory("transport").setPriority(2).setStyle(mediaSession);
                if (i >= 26) {
                    NotificationsController.checkOtherNotificationsChannel();
                    builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
                }
                if (bitmap6 != null) {
                    builder.setLargeIcon(bitmap6);
                } else {
                    builder.setLargeIcon(this.albumArtPlaceholder);
                }
                String string = LocaleController.getString("Next", R.string.Next);
                String string2 = LocaleController.getString("AccDescrPrevious", R.string.AccDescrPrevious);
                if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                    str3 = musicTitle;
                    str4 = musicAuthor;
                    this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
                    if (messageObject.isMusic()) {
                        builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_previous, string2, broadcast).build());
                    }
                    builder.addAction(new Notification.Action.Builder(R.drawable.loading_animation2, LocaleController.getString("Loading", R.string.Loading), (PendingIntent) null).build());
                    if (messageObject.isMusic()) {
                        builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_next, string, broadcast3).build());
                    }
                    audioInfo = audioInfo2;
                } else {
                    str3 = musicTitle;
                    str4 = musicAuthor;
                    audioInfo = audioInfo2;
                    this.playbackState.setState(z2 ? 3 : 2, MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000, getPlaybackSpeed(z2, messageObject)).setActions(messageObject.isMusic() ? 822L : 774L);
                    if (z2) {
                        i3 = R.string.AccActionPause;
                        str5 = "AccActionPause";
                    } else {
                        i3 = R.string.AccActionPlay;
                        str5 = "AccActionPlay";
                    }
                    String string3 = LocaleController.getString(str5, i3);
                    if (messageObject.isMusic()) {
                        builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_previous, string2, broadcast).build());
                    }
                    builder.addAction(new Notification.Action.Builder(z2 ? R.drawable.ic_action_pause : R.drawable.ic_action_play, string3, broadcast2).build());
                    if (messageObject.isMusic()) {
                        builder.addAction(new Notification.Action.Builder(R.drawable.ic_action_next, string, broadcast3).build());
                    }
                }
                this.mediaSession.setPlaybackState(this.playbackState.build());
                bitmap5 = bitmap7;
                str2 = str4;
                str = str3;
                this.mediaSession.setMetadata(new MediaMetadata.Builder().putBitmap("android.media.metadata.ALBUM_ART", bitmap5).putString("android.media.metadata.ALBUM_ARTIST", str2).putString("android.media.metadata.ARTIST", str2).putLong("android.media.metadata.DURATION", duration).putString("android.media.metadata.TITLE", str).putString("android.media.metadata.ALBUM", (audioInfo == null || !messageObject.isMusic()) ? null : audioInfo.getAlbum()).build());
                builder.setVisibility(1);
                Notification build = builder.build();
                if (i >= 31) {
                    if (!this.foregroundServiceIsStarted) {
                        this.foregroundServiceIsStarted = true;
                        startForeground(5, build);
                    } else {
                        ((NotificationManager) getSystemService("notification")).notify(5, build);
                    }
                } else if (z2) {
                    startForeground(5, build);
                } else {
                    stopForeground(false);
                    ((NotificationManager) getSystemService("notification")).notify(5, build);
                }
            } else {
                audioInfo = audioInfo2;
                bitmap5 = bitmap4;
                str = musicTitle;
                str2 = musicAuthor;
                RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.player_small_notification);
                RemoteViews remoteViews2 = supportBigNotifications ? new RemoteViews(getApplicationContext().getPackageName(), R.layout.player_big_notification) : null;
                Notification build2 = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.player).setContentIntent(activity).setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).setContentTitle(str).build();
                build2.contentView = remoteViews;
                if (supportBigNotifications) {
                    build2.bigContentView = remoteViews2;
                }
                setListeners(remoteViews);
                if (supportBigNotifications) {
                    setListeners(remoteViews2);
                }
                if (bitmap6 != null) {
                    RemoteViews remoteViews3 = build2.contentView;
                    int i4 = R.id.player_album_art;
                    remoteViews3.setImageViewBitmap(i4, bitmap6);
                    if (supportBigNotifications) {
                        build2.bigContentView.setImageViewBitmap(i4, bitmap6);
                    }
                } else {
                    RemoteViews remoteViews4 = build2.contentView;
                    int i5 = R.id.player_album_art;
                    remoteViews4.setImageViewResource(i5, R.drawable.nocover_small);
                    if (supportBigNotifications) {
                        build2.bigContentView.setImageViewResource(i5, R.drawable.nocover_big);
                    }
                }
                if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                    RemoteViews remoteViews5 = build2.contentView;
                    int i6 = R.id.player_pause;
                    remoteViews5.setViewVisibility(i6, 8);
                    RemoteViews remoteViews6 = build2.contentView;
                    int i7 = R.id.player_play;
                    remoteViews6.setViewVisibility(i7, 8);
                    RemoteViews remoteViews7 = build2.contentView;
                    int i8 = R.id.player_next;
                    remoteViews7.setViewVisibility(i8, 8);
                    RemoteViews remoteViews8 = build2.contentView;
                    int i9 = R.id.player_previous;
                    remoteViews8.setViewVisibility(i9, 8);
                    RemoteViews remoteViews9 = build2.contentView;
                    int i10 = R.id.player_progress_bar;
                    remoteViews9.setViewVisibility(i10, 0);
                    if (supportBigNotifications) {
                        build2.bigContentView.setViewVisibility(i6, 8);
                        build2.bigContentView.setViewVisibility(i7, 8);
                        build2.bigContentView.setViewVisibility(i8, 8);
                        build2.bigContentView.setViewVisibility(i9, 8);
                        build2.bigContentView.setViewVisibility(i10, 0);
                    }
                } else {
                    RemoteViews remoteViews10 = build2.contentView;
                    int i11 = R.id.player_progress_bar;
                    remoteViews10.setViewVisibility(i11, 8);
                    if (messageObject.isMusic()) {
                        build2.contentView.setViewVisibility(R.id.player_next, 0);
                        build2.contentView.setViewVisibility(R.id.player_previous, 0);
                    } else {
                        build2.bigContentView.setViewVisibility(R.id.player_next, 8);
                        build2.bigContentView.setViewVisibility(R.id.player_previous, 8);
                    }
                    if (supportBigNotifications) {
                        if (messageObject.isMusic()) {
                            build2.bigContentView.setViewVisibility(R.id.player_next, 0);
                            build2.bigContentView.setViewVisibility(R.id.player_previous, 0);
                            i2 = 8;
                        } else {
                            i2 = 8;
                            build2.bigContentView.setViewVisibility(R.id.player_next, 8);
                            build2.bigContentView.setViewVisibility(R.id.player_previous, 8);
                        }
                        build2.bigContentView.setViewVisibility(i11, i2);
                    } else {
                        i2 = 8;
                    }
                    if (MediaController.getInstance().isMessagePaused()) {
                        RemoteViews remoteViews11 = build2.contentView;
                        int i12 = R.id.player_pause;
                        remoteViews11.setViewVisibility(i12, i2);
                        RemoteViews remoteViews12 = build2.contentView;
                        int i13 = R.id.player_play;
                        remoteViews12.setViewVisibility(i13, 0);
                        if (supportBigNotifications) {
                            build2.bigContentView.setViewVisibility(i12, i2);
                            build2.bigContentView.setViewVisibility(i13, 0);
                        }
                    } else {
                        RemoteViews remoteViews13 = build2.contentView;
                        int i14 = R.id.player_pause;
                        remoteViews13.setViewVisibility(i14, 0);
                        RemoteViews remoteViews14 = build2.contentView;
                        int i15 = R.id.player_play;
                        remoteViews14.setViewVisibility(i15, i2);
                        if (supportBigNotifications) {
                            build2.bigContentView.setViewVisibility(i14, 0);
                            build2.bigContentView.setViewVisibility(i15, i2);
                        }
                    }
                }
                RemoteViews remoteViews15 = build2.contentView;
                int i16 = R.id.player_song_name;
                remoteViews15.setTextViewText(i16, str);
                RemoteViews remoteViews16 = build2.contentView;
                int i17 = R.id.player_author_name;
                remoteViews16.setTextViewText(i17, str2);
                if (supportBigNotifications) {
                    build2.bigContentView.setTextViewText(i16, str);
                    build2.bigContentView.setTextViewText(i17, str2);
                    build2.bigContentView.setTextViewText(R.id.player_album_title, (audioInfo == null || TextUtils.isEmpty(audioInfo.getAlbum())) ? "" : audioInfo.getAlbum());
                }
                build2.flags |= 2;
                startForeground(5, build2);
            }
            if (this.remoteControlClient == null) {
                int id = MediaController.getInstance().getPlayingMessageObject().getId();
                if (this.notificationMessageID != id) {
                    this.notificationMessageID = id;
                    RemoteControlClient.MetadataEditor editMetadata = this.remoteControlClient.editMetadata(true);
                    editMetadata.putString(2, str2);
                    editMetadata.putString(7, str);
                    if (audioInfo != null && !TextUtils.isEmpty(audioInfo.getAlbum())) {
                        editMetadata.putString(1, audioInfo.getAlbum());
                    }
                    editMetadata.putLong(9, MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000);
                    if (bitmap5 != null) {
                        try {
                            editMetadata.putBitmap(100, bitmap5);
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
                    }
                    editMetadata.apply();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MusicPlayerService.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (MusicPlayerService.this.remoteControlClient == null || MediaController.getInstance().getPlayingMessageObject() == null) {
                                return;
                            }
                            if (MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration != -9223372036854775807L) {
                                RemoteControlClient.MetadataEditor editMetadata2 = MusicPlayerService.this.remoteControlClient.editMetadata(false);
                                editMetadata2.putLong(9, MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000);
                                editMetadata2.apply();
                                if (Build.VERSION.SDK_INT < 18) {
                                    MusicPlayerService.this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3);
                                    return;
                                } else {
                                    MusicPlayerService.this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, Math.max(MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000, 100L), MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                                    return;
                                }
                            }
                            AndroidUtilities.runOnUIThread(this, 500L);
                        }
                    }, 1000L);
                }
                if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                    this.remoteControlClient.setPlaybackState(8);
                    return;
                }
                RemoteControlClient.MetadataEditor editMetadata2 = this.remoteControlClient.editMetadata(false);
                editMetadata2.putLong(9, MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000);
                editMetadata2.apply();
                if (Build.VERSION.SDK_INT >= 18) {
                    this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, Math.max(MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000, 100L), MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                    return;
                } else {
                    this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3);
                    return;
                }
            }
            return;
        }
        bitmap4 = bitmap3;
        i = Build.VERSION.SDK_INT;
        Bitmap bitmap62 = bitmap2;
        if (i < 21) {
        }
        if (this.remoteControlClient == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlaybackState(long j) {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        boolean z = !MediaController.getInstance().isMessagePaused();
        if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
        } else {
            long j2 = 774;
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject.isMusic()) {
                j2 = 822;
            }
            this.playbackState.setState(z ? 3 : 2, j, getPlaybackSpeed(z, playingMessageObject)).setActions(j2);
        }
        this.mediaSession.setPlaybackState(this.playbackState.build());
    }

    private float getPlaybackSpeed(boolean z, MessageObject messageObject) {
        if (z) {
            if (messageObject.isVoice() || messageObject.isRoundVideo()) {
                return MediaController.getInstance().getPlaybackSpeed(false);
            }
            return 1.0f;
        }
        return 0.0f;
    }

    public void setListeners(RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.player_previous, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_close, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_CLOSE), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_pause, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PAUSE), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_next, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT), fixIntentFlags(167772160)));
        remoteViews.setOnClickPendingIntent(R.id.player_play, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PLAY), fixIntentFlags(167772160)));
    }

    private int fixIntentFlags(int i) {
        return (Build.VERSION.SDK_INT >= 31 || !XiaomiUtilities.isMIUI()) ? i : i & (-100663297);
    }

    @Override // android.app.Service
    @SuppressLint({"NewApi"})
    public void onDestroy() {
        unregisterReceiver(this.headsetPlugReceiver);
        super.onDestroy();
        stopForeground(true);
        RemoteControlClient remoteControlClient = this.remoteControlClient;
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor editMetadata = remoteControlClient.editMetadata(true);
            editMetadata.clear();
            editMetadata.apply();
            this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.mediaSession.release();
        }
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileLoaded);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        String str;
        String str2;
        if (i == NotificationCenter.messagePlayingPlayStateChanged) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                createNotification(playingMessageObject, false);
            } else {
                stopSelf();
            }
        } else if (i == NotificationCenter.messagePlayingDidSeek) {
            long round = Math.round(MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * ((Float) objArr[1]).floatValue()) * 1000;
            updatePlaybackState(round);
            RemoteControlClient remoteControlClient = this.remoteControlClient;
            if (remoteControlClient == null || Build.VERSION.SDK_INT < 18) {
                return;
            }
            remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, round, MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
        } else if (i == NotificationCenter.httpFileDidLoad) {
            String str3 = (String) objArr[0];
            MessageObject playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject2 == null || (str2 = this.loadingFilePath) == null || !str2.equals(str3)) {
                return;
            }
            createNotification(playingMessageObject2, false);
        } else if (i == NotificationCenter.fileLoaded) {
            String str4 = (String) objArr[0];
            MessageObject playingMessageObject3 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject3 == null || (str = this.loadingFilePath) == null || !str.equals(str4)) {
                return;
            }
            createNotification(playingMessageObject3, false);
        }
    }
}
