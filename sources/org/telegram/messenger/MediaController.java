package org.telegram.messenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.view.TextureView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.video.MediaCodecVideoConvertor;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$InputDocument;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_encryptedChat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_messages;
import org.telegram.tgnet.TLRPC$TL_messages_search;
import org.telegram.tgnet.TLRPC$TL_messages_searchGlobal;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes.dex */
public class MediaController implements AudioManager.OnAudioFocusChangeListener, NotificationCenter.NotificationCenterDelegate, SensorEventListener {
    private static final int AUDIO_FOCUSED = 2;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    public static final String AUIDO_MIME_TYPE = "audio/mp4a-latm";
    private static volatile MediaController Instance = null;
    public static final int VIDEO_BITRATE_1080 = 6800000;
    public static final int VIDEO_BITRATE_360 = 750000;
    public static final int VIDEO_BITRATE_480 = 1000000;
    public static final int VIDEO_BITRATE_720 = 2621440;
    public static final String VIDEO_MIME_TYPE = "video/avc";
    private static final float VOLUME_DUCK = 0.2f;
    private static final float VOLUME_NORMAL = 1.0f;
    public static AlbumEntry allMediaAlbumEntry;
    public static ArrayList<AlbumEntry> allMediaAlbums;
    public static ArrayList<AlbumEntry> allPhotoAlbums;
    public static AlbumEntry allPhotosAlbumEntry;
    public static AlbumEntry allVideosAlbumEntry;
    private static Runnable broadcastPhotosRunnable;
    private static final String[] projectionPhotos;
    private static final String[] projectionVideo;
    private static Runnable refreshGalleryRunnable;
    private static long volumeBarLastTimeShown;
    private Sensor accelerometerSensor;
    private boolean accelerometerVertical;
    private boolean allowStartRecord;
    private AudioInfo audioInfo;
    private AudioRecord audioRecorder;
    private float audioVolume;
    private ValueAnimator audioVolumeAnimator;
    private Activity baseActivity;
    private boolean callInProgress;
    private int countLess;
    private AspectRatioFrameLayout currentAspectRatioFrameLayout;
    private float currentAspectRatioFrameLayoutRatio;
    private boolean currentAspectRatioFrameLayoutReady;
    private int currentAspectRatioFrameLayoutRotation;
    private int currentPlaylistNum;
    private TextureView currentTextureView;
    private FrameLayout currentTextureViewContainer;
    private boolean downloadingCurrentMessage;
    private ExternalObserver externalObserver;
    private View feedbackView;
    private ByteBuffer fileBuffer;
    private DispatchQueue fileEncodingQueue;
    private BaseFragment flagSecureFragment;
    private boolean forceLoopCurrentPlaylist;
    private MessageObject goingToShowMessageObject;
    private Sensor gravitySensor;
    private int hasAudioFocus;
    private boolean hasRecordAudioFocus;
    private boolean ignoreOnPause;
    private boolean ignoreProximity;
    private boolean inputFieldHasText;
    private InternalObserver internalObserver;
    private boolean isDrawingWasReady;
    private boolean isStreamingCurrentAudio;
    private long lastAccelerometerDetected;
    private int lastChatAccount;
    private long lastChatEnterTime;
    private long lastChatLeaveTime;
    private ArrayList<Long> lastChatVisibleMessages;
    private long lastMediaCheckTime;
    private int lastMessageId;
    private long lastSaveTime;
    private TLRPC$EncryptedChat lastSecretChat;
    private TLRPC$User lastUser;
    private Sensor linearSensor;
    private boolean loadingPlaylist;
    private boolean manualRecording;
    private String[] mediaProjections;
    private PipRoundVideoView pipRoundVideoView;
    private int pipSwitchingState;
    private boolean playMusicAgain;
    private int playerNum;
    private boolean playerWasReady;
    private MessageObject playingMessageObject;
    private int playlistClassGuid;
    private PlaylistGlobalSearchParams playlistGlobalSearchParams;
    private long playlistMergeDialogId;
    private float previousAccValue;
    private boolean proximityHasDifferentValues;
    private Sensor proximitySensor;
    private boolean proximityTouched;
    private PowerManager.WakeLock proximityWakeLock;
    private ChatActivity raiseChat;
    private boolean raiseToEarRecord;
    private int raisedToBack;
    private int raisedToTop;
    private int raisedToTopSign;
    private long recordDialogId;
    private DispatchQueue recordQueue;
    private MessageObject recordReplyingMsg;
    private MessageObject recordReplyingTopMsg;
    private Runnable recordStartRunnable;
    private long recordStartTime;
    private long recordTimeCount;
    private TLRPC$TL_document recordingAudio;
    private File recordingAudioFile;
    private int recordingCurrentAccount;
    private boolean resumeAudioOnFocusGain;
    private long samplesCount;
    private float seekToProgressPending;
    private int sendAfterDone;
    private boolean sendAfterDoneNotify;
    private int sendAfterDoneScheduleDate;
    private SensorManager sensorManager;
    private boolean sensorsStarted;
    private String shouldSavePositionForCurrentAudio;
    private int startObserverToken;
    private StopMediaObserverRunnable stopMediaObserverRunnable;
    private long timeSinceRaise;
    private boolean useFrontSpeaker;
    private VideoPlayer videoPlayer;
    private ArrayList<MessageObject> voiceMessagesPlaylist;
    private SparseArray<MessageObject> voiceMessagesPlaylistMap;
    private boolean voiceMessagesPlaylistUnread;
    private long writedFileLenght;
    private int writedFrame;
    AudioManager.OnAudioFocusChangeListener audioRecordFocusChangedListener = new AudioManager.OnAudioFocusChangeListener() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda2
        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public final void onAudioFocusChange(int i) {
            MediaController.this.lambda$new$0(i);
        }
    };
    private final Object videoConvertSync = new Object();
    private long lastTimestamp = 0;
    private float lastProximityValue = -100.0f;
    private float[] gravity = new float[3];
    private float[] gravityFast = new float[3];
    private float[] linearAcceleration = new float[3];
    private int audioFocus = 0;
    private ArrayList<VideoConvertMessage> videoConvertQueue = new ArrayList<>();
    private final Object videoQueueSync = new Object();
    private HashMap<String, MessageObject> generatingWaveform = new HashMap<>();
    public boolean isSilent = false;
    private boolean isPaused = false;
    private VideoPlayer audioPlayer = null;
    private VideoPlayer emojiSoundPlayer = null;
    private int emojiSoundPlayerNum = 0;
    private float currentPlaybackSpeed = VOLUME_NORMAL;
    private float currentMusicPlaybackSpeed = VOLUME_NORMAL;
    private float fastPlaybackSpeed = VOLUME_NORMAL;
    private float fastMusicPlaybackSpeed = VOLUME_NORMAL;
    private long lastProgress = 0;
    private Timer progressTimer = null;
    private final Object progressTimerSync = new Object();
    private ArrayList<MessageObject> playlist = new ArrayList<>();
    private HashMap<Integer, MessageObject> playlistMap = new HashMap<>();
    private ArrayList<MessageObject> shuffledPlaylist = new ArrayList<>();
    private boolean[] playlistEndReached = {false, false};
    private int[] playlistMaxId = {ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID};
    private Runnable setLoadingRunnable = new Runnable() { // from class: org.telegram.messenger.MediaController.1
        @Override // java.lang.Runnable
        public void run() {
            if (MediaController.this.playingMessageObject == null) {
                return;
            }
            FileLoader.getInstance(MediaController.this.playingMessageObject.currentAccount).setLoadingVideo(MediaController.this.playingMessageObject.getDocument(), true, false);
        }
    };
    private int recordingGuid = -1;
    private short[] recordSamples = new short[1024];
    private final Object sync = new Object();
    private ArrayList<ByteBuffer> recordBuffers = new ArrayList<>();
    public int recordBufferSize = 1280;
    public int sampleRate = 48000;
    private Runnable recordRunnable = new 2();
    private final ValueAnimator.AnimatorUpdateListener audioVolumeUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.messenger.MediaController.3
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            MediaController.this.audioVolume = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            MediaController.this.setPlayerVolume();
        }
    };

    /* loaded from: classes.dex */
    public static class AudioEntry {
        public String author;
        public int duration;
        public String genre;
        public long id;
        public MessageObject messageObject;
        public String path;
        public String title;
    }

    /* loaded from: classes.dex */
    public static class SavedFilterState {
        public float blurAngle;
        public float blurExcludeBlurSize;
        public Point blurExcludePoint;
        public float blurExcludeSize;
        public int blurType;
        public float contrastValue;
        public PhotoFilterView.CurvesToolValue curvesToolValue = new PhotoFilterView.CurvesToolValue();
        public float enhanceValue;
        public float exposureValue;
        public float fadeValue;
        public float grainValue;
        public float highlightsValue;
        public float saturationValue;
        public float shadowsValue;
        public float sharpenValue;
        public float softenSkinValue;
        public int tintHighlightsColor;
        public int tintShadowsColor;
        public float vignetteValue;
        public float warmthValue;
    }

    /* loaded from: classes.dex */
    public interface VideoConvertorListener {
        boolean checkConversionCanceled();

        void didWriteData(long j, float f);
    }

    private static int getVideoBitrateWithFactor(float f) {
        return (int) (f * 2000.0f * 1000.0f * 1.13f);
    }

    public static native byte[] getWaveform(String str);

    public static native int isOpusFile(String str);

    private static boolean isRecognizedFormat(int i) {
        if (i == 39 || i == 2130706688) {
            return true;
        }
        switch (i) {
            case 19:
            case 20:
            case 21:
                return true;
            default:
                return false;
        }
    }

    private native int startRecord(String str, int i);

    private native void stopRecord();

    /* JADX INFO: Access modifiers changed from: private */
    public native int writeFrame(ByteBuffer byteBuffer, int i);

    public native byte[] getWaveform2(short[] sArr, int i);

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    static /* synthetic */ long access$1614(MediaController mediaController, long j) {
        long j2 = mediaController.recordTimeCount + j;
        mediaController.recordTimeCount = j2;
        return j2;
    }

    static /* synthetic */ int access$1708(MediaController mediaController) {
        int i = mediaController.writedFrame;
        mediaController.writedFrame = i + 1;
        return i;
    }

    public boolean isBuffering() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer != null) {
            return videoPlayer.isBuffering();
        }
        return false;
    }

    /* loaded from: classes.dex */
    private static class AudioBuffer {
        ByteBuffer buffer;
        byte[] bufferBytes;
        int finished;
        long pcmOffset;
        int size;

        public AudioBuffer(int i) {
            this.buffer = ByteBuffer.allocateDirect(i);
            this.bufferBytes = new byte[i];
        }
    }

    static {
        String[] strArr = new String[9];
        strArr[0] = "_id";
        strArr[1] = "bucket_id";
        strArr[2] = "bucket_display_name";
        strArr[3] = "_data";
        int i = Build.VERSION.SDK_INT;
        strArr[4] = i > 28 ? "date_modified" : "datetaken";
        strArr[5] = "orientation";
        strArr[6] = "width";
        strArr[7] = "height";
        strArr[8] = "_size";
        projectionPhotos = strArr;
        String[] strArr2 = new String[9];
        strArr2[0] = "_id";
        strArr2[1] = "bucket_id";
        strArr2[2] = "bucket_display_name";
        strArr2[3] = "_data";
        strArr2[4] = i <= 28 ? "datetaken" : "date_modified";
        strArr2[5] = "duration";
        strArr2[6] = "width";
        strArr2[7] = "height";
        strArr2[8] = "_size";
        projectionVideo = strArr2;
        allMediaAlbums = new ArrayList<>();
        allPhotoAlbums = new ArrayList<>();
    }

    /* loaded from: classes.dex */
    public static class AlbumEntry {
        public int bucketId;
        public String bucketName;
        public PhotoEntry coverPhoto;
        public ArrayList<PhotoEntry> photos = new ArrayList<>();
        public SparseArray<PhotoEntry> photosByIds = new SparseArray<>();
        public boolean videoOnly;

        public AlbumEntry(int i, String str, PhotoEntry photoEntry) {
            this.bucketId = i;
            this.bucketName = str;
            this.coverPhoto = photoEntry;
        }

        public void addPhoto(PhotoEntry photoEntry) {
            this.photos.add(photoEntry);
            this.photosByIds.put(photoEntry.imageId, photoEntry);
        }
    }

    /* loaded from: classes.dex */
    public static class CropState {
        public float cropPx;
        public float cropPy;
        public float cropRotate;
        public boolean freeform;
        public int height;
        public boolean initied;
        public float lockedAspectRatio;
        public Matrix matrix;
        public boolean mirrored;
        public float scale;
        public float stateScale;
        public int transformHeight;
        public int transformRotation;
        public int transformWidth;
        public int width;
        public float cropScale = MediaController.VOLUME_NORMAL;
        public float cropPw = MediaController.VOLUME_NORMAL;
        public float cropPh = MediaController.VOLUME_NORMAL;

        public CropState clone() {
            CropState cropState = new CropState();
            cropState.cropPx = this.cropPx;
            cropState.cropPy = this.cropPy;
            cropState.cropScale = this.cropScale;
            cropState.cropRotate = this.cropRotate;
            cropState.cropPw = this.cropPw;
            cropState.cropPh = this.cropPh;
            cropState.transformWidth = this.transformWidth;
            cropState.transformHeight = this.transformHeight;
            cropState.transformRotation = this.transformRotation;
            cropState.mirrored = this.mirrored;
            cropState.stateScale = this.stateScale;
            cropState.scale = this.scale;
            cropState.matrix = this.matrix;
            cropState.width = this.width;
            cropState.height = this.height;
            cropState.freeform = this.freeform;
            cropState.lockedAspectRatio = this.lockedAspectRatio;
            cropState.initied = this.initied;
            return cropState;
        }
    }

    /* loaded from: classes.dex */
    public static class MediaEditState {
        public long averageDuration;
        public CharSequence caption;
        public CropState cropState;
        public ArrayList<VideoEditedInfo.MediaEntity> croppedMediaEntities;
        public String croppedPaintPath;
        public VideoEditedInfo editedInfo;
        public ArrayList<TLRPC$MessageEntity> entities;
        public String filterPath;
        public String fullPaintPath;
        public String imagePath;
        public boolean isCropped;
        public boolean isFiltered;
        public boolean isPainted;
        public ArrayList<VideoEditedInfo.MediaEntity> mediaEntities;
        public String paintPath;
        public SavedFilterState savedFilterState;
        public ArrayList<TLRPC$InputDocument> stickers;
        public String thumbPath;
        public int ttl;

        public String getPath() {
            return null;
        }

        public void reset() {
            this.caption = null;
            this.thumbPath = null;
            this.filterPath = null;
            this.imagePath = null;
            this.paintPath = null;
            this.croppedPaintPath = null;
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.ttl = 0;
            this.mediaEntities = null;
            this.editedInfo = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers = null;
            this.cropState = null;
        }

        public void copyFrom(MediaEditState mediaEditState) {
            this.caption = mediaEditState.caption;
            this.thumbPath = mediaEditState.thumbPath;
            this.imagePath = mediaEditState.imagePath;
            this.filterPath = mediaEditState.filterPath;
            this.paintPath = mediaEditState.paintPath;
            this.croppedPaintPath = mediaEditState.croppedPaintPath;
            this.fullPaintPath = mediaEditState.fullPaintPath;
            this.entities = mediaEditState.entities;
            this.savedFilterState = mediaEditState.savedFilterState;
            this.mediaEntities = mediaEditState.mediaEntities;
            this.croppedMediaEntities = mediaEditState.croppedMediaEntities;
            this.stickers = mediaEditState.stickers;
            this.editedInfo = mediaEditState.editedInfo;
            this.averageDuration = mediaEditState.averageDuration;
            this.isFiltered = mediaEditState.isFiltered;
            this.isPainted = mediaEditState.isPainted;
            this.isCropped = mediaEditState.isCropped;
            this.ttl = mediaEditState.ttl;
            this.cropState = mediaEditState.cropState;
        }
    }

    /* loaded from: classes.dex */
    public static class PhotoEntry extends MediaEditState {
        public int bucketId;
        public boolean canDeleteAfter;
        public long dateTaken;
        public int duration;
        public TLRPC$VideoSize emojiMarkup;
        public boolean hasSpoiler;
        public int height;
        public int imageId;
        public int invert;
        public boolean isAttachSpoilerRevealed;
        public boolean isChatPreviewSpoilerRevealed;
        public boolean isMuted;
        public boolean isVideo;
        public int orientation;
        public String path;
        public long size;
        public int width;

        public PhotoEntry(int i, int i2, long j, String str, int i3, boolean z, int i4, int i5, long j2) {
            this.bucketId = i;
            this.imageId = i2;
            this.dateTaken = j;
            this.path = str;
            this.width = i4;
            this.height = i5;
            this.size = j2;
            if (z) {
                this.duration = i3;
            } else {
                this.orientation = i3;
            }
            this.isVideo = z;
        }

        public PhotoEntry setOrientation(Pair<Integer, Integer> pair) {
            this.orientation = ((Integer) pair.first).intValue();
            this.invert = ((Integer) pair.second).intValue();
            return this;
        }

        public PhotoEntry setOrientation(int i, int i2) {
            this.orientation = i;
            this.invert = i2;
            return this;
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public void copyFrom(MediaEditState mediaEditState) {
            super.copyFrom(mediaEditState);
            this.hasSpoiler = (mediaEditState instanceof PhotoEntry) && ((PhotoEntry) mediaEditState).hasSpoiler;
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public String getPath() {
            return this.path;
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public void reset() {
            if (this.isVideo && this.filterPath != null) {
                new File(this.filterPath).delete();
                this.filterPath = null;
            }
            this.hasSpoiler = false;
            super.reset();
        }
    }

    /* loaded from: classes.dex */
    public static class SearchImage extends MediaEditState {
        public CharSequence caption;
        public int date;
        public TLRPC$Document document;
        public int height;
        public String id;
        public String imageUrl;
        public TLRPC$BotInlineResult inlineResult;
        public HashMap<String, String> params;
        public TLRPC$Photo photo;
        public TLRPC$PhotoSize photoSize;
        public int size;
        public TLRPC$PhotoSize thumbPhotoSize;
        public String thumbUrl;
        public int type;
        public int width;

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public String getPath() {
            if (this.photoSize != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.photoSize, true).getAbsolutePath();
            }
            if (this.document != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.document, true).getAbsolutePath();
            }
            return ImageLoader.getHttpFilePath(this.imageUrl, "jpg").getAbsolutePath();
        }

        @Override // org.telegram.messenger.MediaController.MediaEditState
        public void reset() {
            super.reset();
        }

        public String getAttachName() {
            TLRPC$PhotoSize tLRPC$PhotoSize = this.photoSize;
            if (tLRPC$PhotoSize != null) {
                return FileLoader.getAttachFileName(tLRPC$PhotoSize);
            }
            TLRPC$Document tLRPC$Document = this.document;
            if (tLRPC$Document != null) {
                return FileLoader.getAttachFileName(tLRPC$Document);
            }
            return Utilities.MD5(this.imageUrl) + "." + ImageLoader.getHttpUrlExtension(this.imageUrl, "jpg");
        }

        public String getPathToAttach() {
            if (this.photoSize != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.photoSize, true).getAbsolutePath();
            }
            if (this.document != null) {
                return FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(this.document, true).getAbsolutePath();
            }
            return this.imageUrl;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        if (i != 1) {
            this.hasRecordAudioFocus = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VideoConvertMessage {
        public int currentAccount;
        public MessageObject messageObject;
        public VideoEditedInfo videoEditedInfo;

        public VideoConvertMessage(MessageObject messageObject, VideoEditedInfo videoEditedInfo) {
            this.messageObject = messageObject;
            this.currentAccount = messageObject.currentAccount;
            this.videoEditedInfo = videoEditedInfo;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 2 implements Runnable {
        2() {
        }

        /* JADX WARN: Removed duplicated region for block: B:47:0x0115  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            ByteBuffer allocateDirect;
            double d;
            double d2;
            double d3;
            if (MediaController.this.audioRecorder != null) {
                if (!MediaController.this.recordBuffers.isEmpty()) {
                    allocateDirect = (ByteBuffer) MediaController.this.recordBuffers.get(0);
                    MediaController.this.recordBuffers.remove(0);
                } else {
                    allocateDirect = ByteBuffer.allocateDirect(MediaController.this.recordBufferSize);
                    allocateDirect.order(ByteOrder.nativeOrder());
                }
                final ByteBuffer byteBuffer = allocateDirect;
                byteBuffer.rewind();
                int read = MediaController.this.audioRecorder.read(byteBuffer, byteBuffer.capacity());
                if (read <= 0) {
                    MediaController.this.recordBuffers.add(byteBuffer);
                    if (MediaController.this.sendAfterDone != 3) {
                        MediaController mediaController = MediaController.this;
                        mediaController.stopRecordingInternal(mediaController.sendAfterDone, MediaController.this.sendAfterDoneNotify, MediaController.this.sendAfterDoneScheduleDate);
                        return;
                    }
                    return;
                }
                byteBuffer.limit(read);
                try {
                    long j = MediaController.this.samplesCount + (read / 2);
                    double d4 = MediaController.this.samplesCount;
                    double d5 = j;
                    Double.isNaN(d4);
                    Double.isNaN(d5);
                    double d6 = d4 / d5;
                    double length = MediaController.this.recordSamples.length;
                    Double.isNaN(length);
                    int i = (int) (d6 * length);
                    int length2 = MediaController.this.recordSamples.length - i;
                    float f = 0.0f;
                    if (i != 0) {
                        float length3 = MediaController.this.recordSamples.length / i;
                        float f2 = 0.0f;
                        for (int i2 = 0; i2 < i; i2++) {
                            MediaController.this.recordSamples[i2] = MediaController.this.recordSamples[(int) f2];
                            f2 += length3;
                        }
                    }
                    float f3 = (read / 2.0f) / length2;
                    d2 = 0.0d;
                    for (int i3 = 0; i3 < read / 2; i3++) {
                        try {
                            short s = byteBuffer.getShort();
                            if (Build.VERSION.SDK_INT < 21) {
                                if (s > 2500) {
                                    d3 = s * s;
                                    Double.isNaN(d3);
                                }
                                if (i3 == ((int) f) && i < MediaController.this.recordSamples.length) {
                                    MediaController.this.recordSamples[i] = s;
                                    f += f3;
                                    i++;
                                }
                            } else {
                                d3 = s * s;
                                Double.isNaN(d3);
                            }
                            d2 += d3;
                            if (i3 == ((int) f)) {
                                MediaController.this.recordSamples[i] = s;
                                f += f3;
                                i++;
                            }
                        } catch (Exception e) {
                            e = e;
                            d = d2;
                            FileLog.e(e);
                            d2 = d;
                            byteBuffer.position(0);
                            double d7 = read;
                            Double.isNaN(d7);
                            final double sqrt = Math.sqrt((d2 / d7) / 2.0d);
                            if (read != byteBuffer.capacity()) {
                            }
                            MediaController.this.fileEncodingQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MediaController.2.this.lambda$run$1(byteBuffer, r3);
                                }
                            });
                            MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MediaController.2.this.lambda$run$2(sqrt);
                                }
                            });
                        }
                    }
                    MediaController.this.samplesCount = j;
                } catch (Exception e2) {
                    e = e2;
                    d = 0.0d;
                }
                byteBuffer.position(0);
                double d72 = read;
                Double.isNaN(d72);
                final double sqrt2 = Math.sqrt((d2 / d72) / 2.0d);
                final boolean z = read != byteBuffer.capacity();
                MediaController.this.fileEncodingQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.2.this.lambda$run$1(byteBuffer, z);
                    }
                });
                MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.2.this.lambda$run$2(sqrt2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final ByteBuffer byteBuffer, boolean z) {
            int i;
            while (byteBuffer.hasRemaining()) {
                if (byteBuffer.remaining() > MediaController.this.fileBuffer.remaining()) {
                    i = byteBuffer.limit();
                    byteBuffer.limit(MediaController.this.fileBuffer.remaining() + byteBuffer.position());
                } else {
                    i = -1;
                }
                MediaController.this.fileBuffer.put(byteBuffer);
                if (MediaController.this.fileBuffer.position() == MediaController.this.fileBuffer.limit() || z) {
                    MediaController mediaController = MediaController.this;
                    if (mediaController.writeFrame(mediaController.fileBuffer, !z ? MediaController.this.fileBuffer.limit() : byteBuffer.position()) != 0) {
                        MediaController.this.fileBuffer.rewind();
                        MediaController mediaController2 = MediaController.this;
                        MediaController.access$1614(mediaController2, (mediaController2.fileBuffer.limit() / 2) / (MediaController.this.sampleRate / 1000));
                        MediaController.access$1708(MediaController.this);
                    } else {
                        FileLog.e("writing frame failed");
                    }
                }
                if (i != -1) {
                    byteBuffer.limit(i);
                }
            }
            MediaController.this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.2.this.lambda$run$0(byteBuffer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(ByteBuffer byteBuffer) {
            MediaController.this.recordBuffers.add(byteBuffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$2(double d) {
            NotificationCenter.getInstance(MediaController.this.recordingCurrentAccount).postNotificationName(NotificationCenter.recordProgressChanged, Integer.valueOf(MediaController.this.recordingGuid), Double.valueOf(d));
        }
    }

    /* loaded from: classes.dex */
    private class InternalObserver extends ContentObserver {
        public InternalObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            MediaController.this.processMediaObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        }
    }

    /* loaded from: classes.dex */
    private class ExternalObserver extends ContentObserver {
        public ExternalObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            MediaController.this.processMediaObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class GalleryObserverInternal extends ContentObserver {
        public GalleryObserverInternal() {
            super(null);
        }

        private void scheduleReloadRunnable() {
            AndroidUtilities.runOnUIThread(MediaController.refreshGalleryRunnable = new Runnable() { // from class: org.telegram.messenger.MediaController$GalleryObserverInternal$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.GalleryObserverInternal.this.lambda$scheduleReloadRunnable$0();
                }
            }, 2000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$scheduleReloadRunnable$0() {
            if (!PhotoViewer.getInstance().isVisible()) {
                Runnable unused = MediaController.refreshGalleryRunnable = null;
                MediaController.loadGalleryPhotosAlbums(0);
                return;
            }
            scheduleReloadRunnable();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            scheduleReloadRunnable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class GalleryObserverExternal extends ContentObserver {
        public GalleryObserverExternal() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            AndroidUtilities.runOnUIThread(MediaController.refreshGalleryRunnable = MediaController$GalleryObserverExternal$$ExternalSyntheticLambda0.INSTANCE, 2000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onChange$0() {
            Runnable unused = MediaController.refreshGalleryRunnable = null;
            MediaController.loadGalleryPhotosAlbums(0);
        }
    }

    public static void checkGallery() {
        AlbumEntry albumEntry;
        if (Build.VERSION.SDK_INT < 24 || (albumEntry = allPhotosAlbumEntry) == null) {
            return;
        }
        final int size = albumEntry.photos.size();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.lambda$checkGallery$1(size);
            }
        }, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a0, code lost:
        if (r10 != null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00a7, code lost:
        if (r10 == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00a9, code lost:
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ae, code lost:
        if (r17 == r9) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00b0, code lost:
        r0 = org.telegram.messenger.MediaController.refreshGalleryRunnable;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00b2, code lost:
        if (r0 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00b4, code lost:
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r0);
        org.telegram.messenger.MediaController.refreshGalleryRunnable = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00b9, code lost:
        loadGalleryPhotosAlbums(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00bc, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:?, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0051 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0069 A[Catch: all -> 0x00a3, TryCatch #0 {all -> 0x00a3, blocks: (B:32:0x0063, B:34:0x0069, B:36:0x006f, B:38:0x0075, B:42:0x0081, B:44:0x0095, B:46:0x009b, B:40:0x007b), top: B:71:0x0063 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$checkGallery$1(int i) {
        Cursor cursor;
        Cursor cursor2;
        int i2;
        Context context;
        int i3;
        try {
            context = ApplicationLoader.applicationContext;
        } catch (Throwable th) {
            th = th;
            cursor = null;
        }
        try {
            if ((Build.VERSION.SDK_INT < 33 || !(context.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") == 0 || context.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") == 0 || context.checkSelfPermission("android.permission.READ_MEDIA_AUDIO") == 0)) && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                cursor = null;
            } else {
                cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"COUNT(_id)"}, null, null, null);
                if (cursor != null) {
                    try {
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            FileLog.e(th);
                            if (cursor != null) {
                                cursor.close();
                            }
                            cursor2 = cursor;
                            i2 = 0;
                            Context context2 = ApplicationLoader.applicationContext;
                            if (Build.VERSION.SDK_INT >= 33) {
                            }
                            i2 += cursor2.getInt(0);
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                    }
                    if (cursor.moveToNext()) {
                        i3 = cursor.getInt(0) + 0;
                        cursor2 = cursor2;
                        i2 = i3;
                        Context context22 = ApplicationLoader.applicationContext;
                        if (((Build.VERSION.SDK_INT >= 33 && (context22.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") == 0 || context22.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") == 0 || context22.checkSelfPermission("android.permission.READ_MEDIA_AUDIO") == 0)) || context22.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) && (cursor2 = MediaStore.Images.Media.query(context22.getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"COUNT(_id)"}, null, null, null)) != null && cursor2.moveToNext()) {
                            i2 += cursor2.getInt(0);
                        }
                    }
                }
            }
            Context context222 = ApplicationLoader.applicationContext;
            if (Build.VERSION.SDK_INT >= 33) {
                i2 += cursor2.getInt(0);
            }
            i2 += cursor2.getInt(0);
        } catch (Throwable th3) {
            try {
                FileLog.e(th3);
            } finally {
                if (cursor2 != null) {
                    cursor2.close();
                }
            }
        }
        i3 = 0;
        cursor2 = cursor2;
        i2 = i3;
    }

    /* loaded from: classes.dex */
    private final class StopMediaObserverRunnable implements Runnable {
        public int currentObserverToken;

        private StopMediaObserverRunnable() {
            this.currentObserverToken = 0;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.currentObserverToken == MediaController.this.startObserverToken) {
                try {
                    if (MediaController.this.internalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.internalObserver);
                        MediaController.this.internalObserver = null;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                try {
                    if (MediaController.this.externalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.externalObserver);
                        MediaController.this.externalObserver = null;
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        }
    }

    public static MediaController getInstance() {
        MediaController mediaController = Instance;
        if (mediaController == null) {
            synchronized (MediaController.class) {
                mediaController = Instance;
                if (mediaController == null) {
                    mediaController = new MediaController();
                    Instance = mediaController;
                }
            }
        }
        return mediaController;
    }

    public MediaController() {
        DispatchQueue dispatchQueue = new DispatchQueue("recordQueue");
        this.recordQueue = dispatchQueue;
        dispatchQueue.setPriority(10);
        DispatchQueue dispatchQueue2 = new DispatchQueue("fileEncodingQueue");
        this.fileEncodingQueue = dispatchQueue2;
        dispatchQueue2.setPriority(10);
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$new$2();
            }
        });
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$new$3();
            }
        });
        this.fileBuffer = ByteBuffer.allocateDirect(1920);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$new$4();
            }
        });
        String[] strArr = new String[7];
        strArr[0] = "_data";
        strArr[1] = "_display_name";
        strArr[2] = "bucket_display_name";
        strArr[3] = Build.VERSION.SDK_INT > 28 ? "date_modified" : "datetaken";
        strArr[4] = "title";
        strArr[5] = "width";
        strArr[6] = "height";
        this.mediaProjections = strArr;
        ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
        try {
            contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, new GalleryObserverExternal());
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            contentResolver.registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, true, new GalleryObserverInternal());
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, new GalleryObserverExternal());
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        try {
            contentResolver.registerContentObserver(MediaStore.Video.Media.INTERNAL_CONTENT_URI, true, new GalleryObserverInternal());
        } catch (Exception e4) {
            FileLog.e(e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        try {
            this.sampleRate = 48000;
            int minBufferSize = AudioRecord.getMinBufferSize(48000, 16, 2);
            if (minBufferSize <= 0) {
                minBufferSize = 1280;
            }
            this.recordBufferSize = minBufferSize;
            for (int i = 0; i < 5; i++) {
                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.recordBufferSize);
                allocateDirect.order(ByteOrder.nativeOrder());
                this.recordBuffers.add(allocateDirect);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        try {
            this.currentPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("playbackSpeed", VOLUME_NORMAL);
            this.currentMusicPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("musicPlaybackSpeed", VOLUME_NORMAL);
            this.fastPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("fastPlaybackSpeed", 1.8f);
            this.fastMusicPlaybackSpeed = MessagesController.getGlobalMainSettings().getFloat("fastMusicPlaybackSpeed", 1.8f);
            SensorManager sensorManager = (SensorManager) ApplicationLoader.applicationContext.getSystemService("sensor");
            this.sensorManager = sensorManager;
            this.linearSensor = sensorManager.getDefaultSensor(10);
            Sensor defaultSensor = this.sensorManager.getDefaultSensor(9);
            this.gravitySensor = defaultSensor;
            if (this.linearSensor == null || defaultSensor == null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("gravity or linear sensor not found");
                }
                this.accelerometerSensor = this.sensorManager.getDefaultSensor(1);
                this.linearSensor = null;
                this.gravitySensor = null;
            }
            this.proximitySensor = this.sensorManager.getDefaultSensor(8);
            this.proximityWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(32, "telegram:proximity_lock");
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            4 r1 = new 4();
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            if (telephonyManager != null) {
                telephonyManager.listen(r1, 32);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 4 extends PhoneStateListener {
        4() {
        }

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(final int i, String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.4.this.lambda$onCallStateChanged$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCallStateChanged$0(int i) {
            if (i != 1) {
                if (i == 0) {
                    MediaController.this.callInProgress = false;
                    return;
                } else if (i == 2) {
                    EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
                    if (embedBottomSheet != null) {
                        embedBottomSheet.pause();
                    }
                    MediaController.this.callInProgress = true;
                    return;
                } else {
                    return;
                }
            }
            MediaController mediaController = MediaController.this;
            if (!mediaController.isPlayingMessage(mediaController.playingMessageObject) || MediaController.this.isMessagePaused()) {
                if (MediaController.this.recordStartRunnable != null || MediaController.this.recordingAudio != null) {
                    MediaController.this.stopRecording(2, false, 0);
                }
            } else {
                MediaController mediaController2 = MediaController.this;
                mediaController2.lambda$startAudioAgain$7(mediaController2.playingMessageObject);
            }
            EmbedBottomSheet embedBottomSheet2 = EmbedBottomSheet.getInstance();
            if (embedBottomSheet2 != null) {
                embedBottomSheet2.pause();
            }
            MediaController.this.callInProgress = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.didReceiveNewMessages);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagesDeleted);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.removeAllMessagesFromDialog);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.musicDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.mediaDidLoad);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.playerDidStartPlaying);
        }
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$onAudioFocusChange$5(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAudioFocusChange$5(int i) {
        if (i == -1) {
            if (isPlayingMessage(getPlayingMessageObject()) && !isMessagePaused()) {
                lambda$startAudioAgain$7(this.playingMessageObject);
            }
            this.hasAudioFocus = 0;
            this.audioFocus = 0;
        } else if (i == 1) {
            this.audioFocus = 2;
            if (this.resumeAudioOnFocusGain) {
                this.resumeAudioOnFocusGain = false;
                if (isPlayingMessage(getPlayingMessageObject()) && isMessagePaused()) {
                    playMessage(getPlayingMessageObject());
                }
            }
        } else if (i == -3) {
            this.audioFocus = 1;
        } else if (i == -2) {
            this.audioFocus = 0;
            if (isPlayingMessage(getPlayingMessageObject()) && !isMessagePaused()) {
                lambda$startAudioAgain$7(this.playingMessageObject);
                this.resumeAudioOnFocusGain = true;
            }
        }
        setPlayerVolume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayerVolume() {
        float f;
        try {
            if (this.isSilent) {
                f = 0.0f;
            } else {
                f = this.audioFocus != 1 ? VOLUME_NORMAL : VOLUME_DUCK;
            }
            VideoPlayer videoPlayer = this.audioPlayer;
            if (videoPlayer != null) {
                videoPlayer.setVolume(f * this.audioVolume);
                return;
            }
            VideoPlayer videoPlayer2 = this.videoPlayer;
            if (videoPlayer2 != null) {
                videoPlayer2.setVolume(f);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public VideoPlayer getVideoPlayer() {
        return this.videoPlayer;
    }

    private void startProgressTimer(MessageObject messageObject) {
        synchronized (this.progressTimerSync) {
            Timer timer = this.progressTimer;
            if (timer != null) {
                try {
                    timer.cancel();
                    this.progressTimer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            messageObject.getFileName();
            Timer timer2 = new Timer();
            this.progressTimer = timer2;
            timer2.schedule(new 5(messageObject), 0L, 17L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 5 extends TimerTask {
        final /* synthetic */ MessageObject val$currentPlayingMessageObject;

        5(MessageObject messageObject) {
            this.val$currentPlayingMessageObject = messageObject;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            synchronized (MediaController.this.sync) {
                final MessageObject messageObject = this.val$currentPlayingMessageObject;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$5$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.5.this.lambda$run$1(messageObject);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(MessageObject messageObject) {
            long duration;
            long currentPosition;
            final float f;
            float f2;
            if ((MediaController.this.audioPlayer == null && MediaController.this.videoPlayer == null) || MediaController.this.isPaused) {
                return;
            }
            try {
                if (MediaController.this.videoPlayer != null) {
                    duration = MediaController.this.videoPlayer.getDuration();
                    currentPosition = MediaController.this.videoPlayer.getCurrentPosition();
                    if (currentPosition >= 0 && duration > 0) {
                        float f3 = (float) duration;
                        f2 = ((float) MediaController.this.videoPlayer.getBufferedPosition()) / f3;
                        f = ((float) currentPosition) / f3;
                        if (f >= MediaController.VOLUME_NORMAL) {
                            return;
                        }
                    }
                    return;
                }
                duration = MediaController.this.audioPlayer.getDuration();
                currentPosition = MediaController.this.audioPlayer.getCurrentPosition();
                float f4 = duration >= 0 ? ((float) currentPosition) / ((float) duration) : 0.0f;
                float bufferedPosition = ((float) MediaController.this.audioPlayer.getBufferedPosition()) / ((float) duration);
                if (duration != -9223372036854775807L && currentPosition >= 0 && MediaController.this.seekToProgressPending == 0.0f) {
                    f = f4;
                    f2 = bufferedPosition;
                }
                return;
                MediaController.this.lastProgress = currentPosition;
                messageObject.audioPlayerDuration = (int) (duration / 1000);
                messageObject.audioProgress = f;
                messageObject.audioProgressSec = (int) (MediaController.this.lastProgress / 1000);
                messageObject.bufferedProgress = f2;
                if (f >= 0.0f && MediaController.this.shouldSavePositionForCurrentAudio != null && SystemClock.elapsedRealtime() - MediaController.this.lastSaveTime >= 1000) {
                    final String str = MediaController.this.shouldSavePositionForCurrentAudio;
                    MediaController.this.lastSaveTime = SystemClock.elapsedRealtime();
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$5$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.5.lambda$run$0(str, f);
                        }
                    });
                }
                NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(messageObject.getId()), Float.valueOf(f));
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$run$0(String str, float f) {
            ApplicationLoader.applicationContext.getSharedPreferences("media_saved_pos", 0).edit().putFloat(str, f).commit();
        }
    }

    private void stopProgressTimer() {
        synchronized (this.progressTimerSync) {
            Timer timer = this.progressTimer;
            if (timer != null) {
                try {
                    timer.cancel();
                    this.progressTimer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void cleanup() {
        cleanupPlayer(true, true);
        this.audioInfo = null;
        this.playMusicAgain = false;
        for (int i = 0; i < 4; i++) {
            DownloadController.getInstance(i).cleanup();
        }
        this.videoConvertQueue.clear();
        this.generatingWaveform.clear();
        this.voiceMessagesPlaylist = null;
        this.voiceMessagesPlaylistMap = null;
        clearPlaylist();
        cancelVideoConvert(null);
    }

    private void clearPlaylist() {
        this.playlist.clear();
        this.playlistMap.clear();
        this.shuffledPlaylist.clear();
        this.playlistClassGuid = 0;
        boolean[] zArr = this.playlistEndReached;
        zArr[1] = false;
        zArr[0] = false;
        this.playlistMergeDialogId = 0L;
        int[] iArr = this.playlistMaxId;
        iArr[1] = Integer.MAX_VALUE;
        iArr[0] = Integer.MAX_VALUE;
        this.loadingPlaylist = false;
        this.playlistGlobalSearchParams = null;
    }

    public void startMediaObserver() {
        ApplicationLoader.applicationHandler.removeCallbacks(this.stopMediaObserverRunnable);
        this.startObserverToken++;
        try {
            if (this.internalObserver == null) {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ExternalObserver externalObserver = new ExternalObserver();
                this.externalObserver = externalObserver;
                contentResolver.registerContentObserver(uri, false, externalObserver);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            if (this.externalObserver == null) {
                ContentResolver contentResolver2 = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri2 = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                InternalObserver internalObserver = new InternalObserver();
                this.internalObserver = internalObserver;
                contentResolver2.registerContentObserver(uri2, false, internalObserver);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    public void stopMediaObserver() {
        if (this.stopMediaObserverRunnable == null) {
            this.stopMediaObserverRunnable = new StopMediaObserverRunnable();
        }
        this.stopMediaObserverRunnable.currentObserverToken = this.startObserverToken;
        ApplicationLoader.applicationHandler.postDelayed(this.stopMediaObserverRunnable, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007a, code lost:
        if (r10 != 0) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void processMediaObserver(Uri uri) {
        Cursor cursor = null;
        try {
            try {
                android.graphics.Point realScreenSize = AndroidUtilities.getRealScreenSize();
                cursor = ApplicationLoader.applicationContext.getContentResolver().query(uri, this.mediaProjections, null, null, "date_added DESC LIMIT 1");
                final ArrayList arrayList = new ArrayList();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        String string2 = cursor.getString(1);
                        String string3 = cursor.getString(2);
                        long j = cursor.getLong(3);
                        String string4 = cursor.getString(4);
                        int i = cursor.getInt(5);
                        int i2 = cursor.getInt(6);
                        if ((string != null && string.toLowerCase().contains("screenshot")) || ((string2 != null && string2.toLowerCase().contains("screenshot")) || ((string3 != null && string3.toLowerCase().contains("screenshot")) || (string4 != null && string4.toLowerCase().contains("screenshot"))))) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(string, options);
                            i = options.outWidth;
                            i2 = options.outHeight;
                            if (i > 0 && i2 > 0) {
                                try {
                                    int i3 = realScreenSize.x;
                                    if (i == i3) {
                                        if (i2 == realScreenSize.y) {
                                        }
                                    }
                                    if (i2 == i3 && i == realScreenSize.y) {
                                    }
                                } catch (Exception unused) {
                                    arrayList.add(Long.valueOf(j));
                                }
                            }
                            arrayList.add(Long.valueOf(j));
                        }
                    }
                    cursor.close();
                }
                if (!arrayList.isEmpty()) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda30
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.this.lambda$processMediaObserver$6(arrayList);
                        }
                    });
                }
                if (cursor == null) {
                    return;
                }
            } catch (Exception e) {
                FileLog.e(e);
                if (cursor == null) {
                    return;
                }
            }
            try {
                cursor.close();
            } catch (Exception unused2) {
            }
        } catch (Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception unused3) {
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processMediaObserver$6(ArrayList arrayList) {
        NotificationCenter.getInstance(this.lastChatAccount).postNotificationName(NotificationCenter.screenshotTook, new Object[0]);
        checkScreenshots(arrayList);
    }

    private void checkScreenshots(ArrayList<Long> arrayList) {
        if (arrayList == null || arrayList.isEmpty() || this.lastChatEnterTime == 0) {
            return;
        }
        if (this.lastUser != null || (this.lastSecretChat instanceof TLRPC$TL_encryptedChat)) {
            boolean z = false;
            for (int i = 0; i < arrayList.size(); i++) {
                Long l = arrayList.get(i);
                if ((this.lastMediaCheckTime == 0 || l.longValue() > this.lastMediaCheckTime) && l.longValue() >= this.lastChatEnterTime && (this.lastChatLeaveTime == 0 || l.longValue() <= this.lastChatLeaveTime + 2000)) {
                    this.lastMediaCheckTime = Math.max(this.lastMediaCheckTime, l.longValue());
                    z = true;
                }
            }
            if (z) {
                if (this.lastSecretChat != null) {
                    SecretChatHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastSecretChat, this.lastChatVisibleMessages, null);
                } else {
                    SendMessagesHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastUser, this.lastMessageId, null);
                }
            }
        }
    }

    public void setLastVisibleMessageIds(int i, long j, long j2, TLRPC$User tLRPC$User, TLRPC$EncryptedChat tLRPC$EncryptedChat, ArrayList<Long> arrayList, int i2) {
        this.lastChatEnterTime = j;
        this.lastChatLeaveTime = j2;
        this.lastChatAccount = i;
        this.lastSecretChat = tLRPC$EncryptedChat;
        this.lastUser = tLRPC$User;
        this.lastMessageId = i2;
        this.lastChatVisibleMessages = arrayList;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ArrayList<MessageObject> arrayList;
        int indexOf;
        int i3 = 0;
        if (i == NotificationCenter.fileLoaded || i == NotificationCenter.httpFileDidLoad) {
            String str = (String) objArr[0];
            MessageObject messageObject = this.playingMessageObject;
            if (messageObject != null && messageObject.currentAccount == i2 && FileLoader.getAttachFileName(messageObject.getDocument()).equals(str)) {
                if (this.downloadingCurrentMessage) {
                    this.playMusicAgain = true;
                    playMessage(this.playingMessageObject);
                } else if (this.audioInfo == null) {
                    try {
                        this.audioInfo = AudioInfo.getAudioInfo(FileLoader.getInstance(UserConfig.selectedAccount).getPathToMessage(this.playingMessageObject.messageOwner));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
        } else if (i == NotificationCenter.messagesDeleted) {
            if (((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            long longValue = ((Long) objArr[1]).longValue();
            ArrayList arrayList2 = (ArrayList) objArr[0];
            MessageObject messageObject2 = this.playingMessageObject;
            if (messageObject2 != null && longValue == messageObject2.messageOwner.peer_id.channel_id && arrayList2.contains(Integer.valueOf(messageObject2.getId()))) {
                cleanupPlayer(true, true);
            }
            ArrayList<MessageObject> arrayList3 = this.voiceMessagesPlaylist;
            if (arrayList3 == null || arrayList3.isEmpty() || longValue != this.voiceMessagesPlaylist.get(0).messageOwner.peer_id.channel_id) {
                return;
            }
            while (i3 < arrayList2.size()) {
                Integer num = (Integer) arrayList2.get(i3);
                MessageObject messageObject3 = this.voiceMessagesPlaylistMap.get(num.intValue());
                this.voiceMessagesPlaylistMap.remove(num.intValue());
                if (messageObject3 != null) {
                    this.voiceMessagesPlaylist.remove(messageObject3);
                }
                i3++;
            }
        } else if (i == NotificationCenter.removeAllMessagesFromDialog) {
            long longValue2 = ((Long) objArr[0]).longValue();
            MessageObject messageObject4 = this.playingMessageObject;
            if (messageObject4 == null || messageObject4.getDialogId() != longValue2) {
                return;
            }
            cleanupPlayer(false, true);
        } else if (i == NotificationCenter.musicDidLoad) {
            long longValue3 = ((Long) objArr[0]).longValue();
            MessageObject messageObject5 = this.playingMessageObject;
            if (messageObject5 == null || !messageObject5.isMusic() || this.playingMessageObject.getDialogId() != longValue3 || this.playingMessageObject.scheduled) {
                return;
            }
            this.playlist.addAll(0, (ArrayList) objArr[1]);
            this.playlist.addAll((ArrayList) objArr[2]);
            int size = this.playlist.size();
            for (int i4 = 0; i4 < size; i4++) {
                MessageObject messageObject6 = this.playlist.get(i4);
                this.playlistMap.put(Integer.valueOf(messageObject6.getId()), messageObject6);
                int[] iArr = this.playlistMaxId;
                iArr[0] = Math.min(iArr[0], messageObject6.getId());
            }
            sortPlaylist();
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            } else {
                MessageObject messageObject7 = this.playingMessageObject;
                if (messageObject7 != null && (indexOf = this.playlist.indexOf(messageObject7)) >= 0) {
                    this.currentPlaylistNum = indexOf;
                }
            }
            this.playlistClassGuid = ConnectionsManager.generateClassGuid();
        } else if (i == NotificationCenter.mediaDidLoad) {
            if (((Integer) objArr[3]).intValue() != this.playlistClassGuid || this.playingMessageObject == null) {
                return;
            }
            long longValue4 = ((Long) objArr[0]).longValue();
            ((Integer) objArr[4]).intValue();
            ArrayList arrayList4 = (ArrayList) objArr[2];
            DialogObject.isEncryptedDialog(longValue4);
            char c = longValue4 == this.playlistMergeDialogId ? (char) 1 : (char) 0;
            if (!arrayList4.isEmpty()) {
                this.playlistEndReached[c] = ((Boolean) objArr[5]).booleanValue();
            }
            int i5 = 0;
            for (int i6 = 0; i6 < arrayList4.size(); i6++) {
                MessageObject messageObject8 = (MessageObject) arrayList4.get(i6);
                if (!this.playlistMap.containsKey(Integer.valueOf(messageObject8.getId()))) {
                    i5++;
                    this.playlist.add(0, messageObject8);
                    this.playlistMap.put(Integer.valueOf(messageObject8.getId()), messageObject8);
                    int[] iArr2 = this.playlistMaxId;
                    iArr2[c] = Math.min(iArr2[c], messageObject8.getId());
                }
            }
            sortPlaylist();
            int indexOf2 = this.playlist.indexOf(this.playingMessageObject);
            if (indexOf2 >= 0) {
                this.currentPlaylistNum = indexOf2;
            }
            this.loadingPlaylist = false;
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            }
            if (i5 != 0) {
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.moreMusicDidLoad, Integer.valueOf(i5));
            }
        } else if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue() || (arrayList = this.voiceMessagesPlaylist) == null || arrayList.isEmpty() || ((Long) objArr[0]).longValue() != this.voiceMessagesPlaylist.get(0).getDialogId()) {
                return;
            }
            ArrayList arrayList5 = (ArrayList) objArr[1];
            while (i3 < arrayList5.size()) {
                MessageObject messageObject9 = (MessageObject) arrayList5.get(i3);
                if ((messageObject9.isVoice() || messageObject9.isRoundVideo()) && (!this.voiceMessagesPlaylistUnread || (messageObject9.isContentUnread() && !messageObject9.isOut()))) {
                    this.voiceMessagesPlaylist.add(messageObject9);
                    this.voiceMessagesPlaylistMap.put(messageObject9.getId(), messageObject9);
                }
                i3++;
            }
        } else if (i == NotificationCenter.playerDidStartPlaying && !getInstance().isCurrentPlayer((VideoPlayer) objArr[0])) {
            getInstance().lambda$startAudioAgain$7(getInstance().getPlayingMessageObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isRecordingAudio() {
        return (this.recordStartRunnable == null && this.recordingAudio == null) ? false : true;
    }

    private boolean isNearToSensor(float f) {
        return f < 5.0f && f != this.proximitySensor.getMaximumRange();
    }

    public boolean isRecordingOrListeningByProximity() {
        MessageObject messageObject;
        return this.proximityTouched && (isRecordingAudio() || ((messageObject = this.playingMessageObject) != null && (messageObject.isVoice() || this.playingMessageObject.isRoundVideo())));
    }

    private boolean forbidRaiseToListen() {
        AudioDeviceInfo[] devices;
        try {
            if (Build.VERSION.SDK_INT < 23) {
                return NotificationsController.audioManager.isWiredHeadsetOn() || NotificationsController.audioManager.isBluetoothA2dpOn() || NotificationsController.audioManager.isBluetoothScoOn();
            }
            for (AudioDeviceInfo audioDeviceInfo : NotificationsController.audioManager.getDevices(2)) {
                int type = audioDeviceInfo.getType();
                if ((type == 8 || type == 7 || type == 26 || type == 27 || type == 4 || type == 3) && audioDeviceInfo.isSink()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        double d;
        boolean z;
        int i;
        PowerManager.WakeLock wakeLock;
        PowerManager.WakeLock wakeLock2;
        PowerManager.WakeLock wakeLock3;
        MessageObject messageObject;
        PowerManager.WakeLock wakeLock4;
        PowerManager.WakeLock wakeLock5;
        if (this.sensorsStarted && VoIPService.getSharedInstance() == null) {
            if (sensorEvent.sensor.getType() == 8) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("proximity changed to " + sensorEvent.values[0] + " max value = " + sensorEvent.sensor.getMaximumRange());
                }
                float f = this.lastProximityValue;
                float[] fArr = sensorEvent.values;
                if (f != fArr[0]) {
                    this.proximityHasDifferentValues = true;
                }
                this.lastProximityValue = fArr[0];
                if (this.proximityHasDifferentValues) {
                    this.proximityTouched = isNearToSensor(fArr[0]);
                }
            } else {
                Sensor sensor = sensorEvent.sensor;
                if (sensor == this.accelerometerSensor) {
                    long j = this.lastTimestamp;
                    if (j == 0) {
                        d = 0.9800000190734863d;
                    } else {
                        double d2 = sensorEvent.timestamp - j;
                        Double.isNaN(d2);
                        d = 1.0d / ((d2 / 1.0E9d) + 1.0d);
                    }
                    this.lastTimestamp = sensorEvent.timestamp;
                    float[] fArr2 = this.gravity;
                    double d3 = fArr2[0];
                    Double.isNaN(d3);
                    double d4 = 1.0d - d;
                    float[] fArr3 = sensorEvent.values;
                    double d5 = fArr3[0];
                    Double.isNaN(d5);
                    fArr2[0] = (float) ((d3 * d) + (d5 * d4));
                    double d6 = fArr2[1];
                    Double.isNaN(d6);
                    double d7 = fArr3[1];
                    Double.isNaN(d7);
                    fArr2[1] = (float) ((d6 * d) + (d7 * d4));
                    double d8 = fArr2[2];
                    Double.isNaN(d8);
                    double d9 = d * d8;
                    double d10 = fArr3[2];
                    Double.isNaN(d10);
                    fArr2[2] = (float) (d9 + (d4 * d10));
                    float[] fArr4 = this.gravityFast;
                    fArr4[0] = (fArr2[0] * 0.8f) + (fArr3[0] * 0.19999999f);
                    fArr4[1] = (fArr2[1] * 0.8f) + (fArr3[1] * 0.19999999f);
                    fArr4[2] = (fArr2[2] * 0.8f) + (fArr3[2] * 0.19999999f);
                    float[] fArr5 = this.linearAcceleration;
                    fArr5[0] = fArr3[0] - fArr2[0];
                    fArr5[1] = fArr3[1] - fArr2[1];
                    fArr5[2] = fArr3[2] - fArr2[2];
                } else if (sensor == this.linearSensor) {
                    float[] fArr6 = this.linearAcceleration;
                    float[] fArr7 = sensorEvent.values;
                    fArr6[0] = fArr7[0];
                    fArr6[1] = fArr7[1];
                    fArr6[2] = fArr7[2];
                } else if (sensor == this.gravitySensor) {
                    float[] fArr8 = this.gravityFast;
                    float[] fArr9 = this.gravity;
                    float[] fArr10 = sensorEvent.values;
                    float f2 = fArr10[0];
                    fArr9[0] = f2;
                    fArr8[0] = f2;
                    float f3 = fArr10[1];
                    fArr9[1] = f3;
                    fArr8[1] = f3;
                    float f4 = fArr10[2];
                    fArr9[2] = f4;
                    fArr8[2] = f4;
                }
            }
            Sensor sensor2 = sensorEvent.sensor;
            if (sensor2 == this.linearSensor || sensor2 == this.gravitySensor || sensor2 == this.accelerometerSensor) {
                float[] fArr11 = this.gravity;
                float f5 = fArr11[0];
                float[] fArr12 = this.linearAcceleration;
                float f6 = (f5 * fArr12[0]) + (fArr11[1] * fArr12[1]) + (fArr11[2] * fArr12[2]);
                int i2 = this.raisedToBack;
                if (i2 != 6 && ((f6 > 0.0f && this.previousAccValue > 0.0f) || (f6 < 0.0f && this.previousAccValue < 0.0f))) {
                    if (f6 > 0.0f) {
                        z = f6 > 15.0f;
                        i = 1;
                    } else {
                        z = f6 < -15.0f;
                        i = 2;
                    }
                    int i3 = this.raisedToTopSign;
                    if (i3 != 0 && i3 != i) {
                        int i4 = this.raisedToTop;
                        if (i4 != 6 || !z) {
                            if (!z) {
                                this.countLess++;
                            }
                            if (this.countLess == 10 || i4 != 6 || i2 != 0) {
                                this.raisedToTop = 0;
                                this.raisedToTopSign = 0;
                                this.raisedToBack = 0;
                                this.countLess = 0;
                            }
                        } else if (i2 < 6) {
                            int i5 = i2 + 1;
                            this.raisedToBack = i5;
                            if (i5 == 6) {
                                this.raisedToTop = 0;
                                this.raisedToTopSign = 0;
                                this.countLess = 0;
                                this.timeSinceRaise = System.currentTimeMillis();
                                if (BuildVars.LOGS_ENABLED && BuildVars.DEBUG_PRIVATE_VERSION) {
                                    FileLog.d("motion detected");
                                }
                            }
                        }
                    } else if (z && i2 == 0 && (i3 == 0 || i3 == i)) {
                        int i6 = this.raisedToTop;
                        if (i6 < 6 && !this.proximityTouched) {
                            this.raisedToTopSign = i;
                            int i7 = i6 + 1;
                            this.raisedToTop = i7;
                            if (i7 == 6) {
                                this.countLess = 0;
                            }
                        }
                    } else {
                        if (!z) {
                            this.countLess++;
                        }
                        if (i3 != i || this.countLess == 10 || this.raisedToTop != 6 || i2 != 0) {
                            this.raisedToBack = 0;
                            this.raisedToTop = 0;
                            this.raisedToTopSign = 0;
                            this.countLess = 0;
                        }
                    }
                }
                this.previousAccValue = f6;
                float[] fArr13 = this.gravityFast;
                this.accelerometerVertical = fArr13[1] > 2.5f && Math.abs(fArr13[2]) < 4.0f && Math.abs(this.gravityFast[0]) > 1.5f;
            }
            if (this.raisedToBack == 6 || this.accelerometerVertical) {
                this.lastAccelerometerDetected = System.currentTimeMillis();
            }
            if (this.proximityTouched && ((this.raisedToBack == 6 || this.accelerometerVertical || System.currentTimeMillis() - this.lastAccelerometerDetected < 60) && !VoIPService.isAnyKindOfCallActive() && !this.manualRecording && !forbidRaiseToListen())) {
                if (SharedConfig.enabledRaiseTo(true) && this.playingMessageObject == null && this.recordStartRunnable == null && this.recordingAudio == null && !PhotoViewer.getInstance().isVisible() && ApplicationLoader.isScreenOn && !this.inputFieldHasText && this.allowStartRecord && this.raiseChat != null && !this.callInProgress) {
                    if (!this.raiseToEarRecord) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("start record");
                        }
                        this.useFrontSpeaker = true;
                        if (!this.raiseChat.playFirstUnreadVoiceMessage()) {
                            this.raiseToEarRecord = true;
                            this.useFrontSpeaker = false;
                            startRecording(this.raiseChat.getCurrentAccount(), this.raiseChat.getDialogId(), null, this.raiseChat.getThreadMessage(), this.raiseChat.getClassGuid(), false);
                        }
                        if (this.useFrontSpeaker) {
                            setUseFrontSpeaker(true);
                        }
                        this.ignoreOnPause = true;
                        if (this.proximityHasDifferentValues && (wakeLock5 = this.proximityWakeLock) != null && !wakeLock5.isHeld()) {
                            this.proximityWakeLock.acquire();
                        }
                    }
                } else if (SharedConfig.enabledRaiseTo(false) && (messageObject = this.playingMessageObject) != null && ((messageObject.isVoice() || this.playingMessageObject.isRoundVideo()) && !this.useFrontSpeaker)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("start listen");
                    }
                    if (this.proximityHasDifferentValues && (wakeLock4 = this.proximityWakeLock) != null && !wakeLock4.isHeld()) {
                        this.proximityWakeLock.acquire();
                    }
                    setUseFrontSpeaker(true);
                    startAudioAgain(false);
                    this.ignoreOnPause = true;
                }
                this.raisedToBack = 0;
                this.raisedToTop = 0;
                this.raisedToTopSign = 0;
                this.countLess = 0;
            } else if (this.proximityTouched && ((((this.accelerometerSensor == null || this.linearSensor == null) && this.gravitySensor == null) || ignoreAccelerometerGestures()) && !VoIPService.isAnyKindOfCallActive())) {
                if (this.playingMessageObject != null && !ApplicationLoader.mainInterfacePaused && ((this.playingMessageObject.isVoice() || this.playingMessageObject.isRoundVideo()) && SharedConfig.enabledRaiseTo(false) && !this.useFrontSpeaker && !this.manualRecording && !forbidRaiseToListen())) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("start listen by proximity only");
                    }
                    if (this.proximityHasDifferentValues && (wakeLock3 = this.proximityWakeLock) != null && !wakeLock3.isHeld()) {
                        this.proximityWakeLock.acquire();
                    }
                    setUseFrontSpeaker(true);
                    startAudioAgain(false);
                    this.ignoreOnPause = true;
                }
            } else if (!this.proximityTouched && !this.manualRecording) {
                if (this.raiseToEarRecord) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("stop record");
                    }
                    stopRecording(2, false, 0);
                    this.raiseToEarRecord = false;
                    this.ignoreOnPause = false;
                    if (this.proximityHasDifferentValues && (wakeLock2 = this.proximityWakeLock) != null && wakeLock2.isHeld()) {
                        this.proximityWakeLock.release();
                    }
                } else if (this.useFrontSpeaker) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("stop listen");
                    }
                    this.useFrontSpeaker = false;
                    startAudioAgain(true);
                    this.ignoreOnPause = false;
                    if (this.proximityHasDifferentValues && (wakeLock = this.proximityWakeLock) != null && wakeLock.isHeld()) {
                        this.proximityWakeLock.release();
                    }
                }
            }
            if (this.timeSinceRaise == 0 || this.raisedToBack != 6 || Math.abs(System.currentTimeMillis() - this.timeSinceRaise) <= 1000) {
                return;
            }
            this.raisedToBack = 0;
            this.raisedToTop = 0;
            this.raisedToTopSign = 0;
            this.countLess = 0;
            this.timeSinceRaise = 0L;
        }
    }

    private void setUseFrontSpeaker(boolean z) {
        this.useFrontSpeaker = z;
        AudioManager audioManager = NotificationsController.audioManager;
        if (z) {
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(false);
            return;
        }
        audioManager.setSpeakerphoneOn(true);
    }

    public void startRecordingIfFromSpeaker() {
        if (this.useFrontSpeaker && this.raiseChat != null && this.allowStartRecord && SharedConfig.enabledRaiseTo(true)) {
            this.raiseToEarRecord = true;
            startRecording(this.raiseChat.getCurrentAccount(), this.raiseChat.getDialogId(), null, this.raiseChat.getThreadMessage(), this.raiseChat.getClassGuid(), false);
            this.ignoreOnPause = true;
        }
    }

    private void startAudioAgain(boolean z) {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject == null) {
            return;
        }
        NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.audioRouteChanged, Boolean.valueOf(this.useFrontSpeaker));
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setStreamType(this.useFrontSpeaker ? 0 : 3);
            if (!z) {
                if (this.videoPlayer.getCurrentPosition() < 1000) {
                    this.videoPlayer.seekTo(0L);
                }
                this.videoPlayer.play();
                return;
            }
            lambda$startAudioAgain$7(this.playingMessageObject);
            return;
        }
        VideoPlayer videoPlayer2 = this.audioPlayer;
        boolean z2 = videoPlayer2 != null;
        final MessageObject messageObject2 = this.playingMessageObject;
        float f = messageObject2.audioProgress;
        int i = messageObject2.audioPlayerDuration;
        if (z || videoPlayer2 == null || !videoPlayer2.isPlaying() || i * f > VOLUME_NORMAL) {
            messageObject2.audioProgress = f;
        } else {
            messageObject2.audioProgress = 0.0f;
        }
        cleanupPlayer(false, true);
        playMessage(messageObject2);
        if (z) {
            if (z2) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda31
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.this.lambda$startAudioAgain$7(messageObject2);
                    }
                }, 100L);
            } else {
                lambda$startAudioAgain$7(messageObject2);
            }
        }
    }

    public void setInputFieldHasText(boolean z) {
        this.inputFieldHasText = z;
    }

    public void setAllowStartRecord(boolean z) {
        this.allowStartRecord = z;
    }

    public void startRaiseToEarSensors(ChatActivity chatActivity) {
        if (chatActivity != null) {
            if ((this.accelerometerSensor == null && (this.gravitySensor == null || this.linearAcceleration == null)) || this.proximitySensor == null) {
                return;
            }
            if (!SharedConfig.enabledRaiseTo(false)) {
                MessageObject messageObject = this.playingMessageObject;
                if (messageObject == null) {
                    return;
                }
                if (!messageObject.isVoice() && !this.playingMessageObject.isRoundVideo()) {
                    return;
                }
            }
            this.raiseChat = chatActivity;
            if (this.sensorsStarted) {
                return;
            }
            float[] fArr = this.gravity;
            fArr[2] = 0.0f;
            fArr[1] = 0.0f;
            fArr[0] = 0.0f;
            float[] fArr2 = this.linearAcceleration;
            fArr2[2] = 0.0f;
            fArr2[1] = 0.0f;
            fArr2[0] = 0.0f;
            float[] fArr3 = this.gravityFast;
            fArr3[2] = 0.0f;
            fArr3[1] = 0.0f;
            fArr3[0] = 0.0f;
            this.lastTimestamp = 0L;
            this.previousAccValue = 0.0f;
            this.raisedToTop = 0;
            this.raisedToTopSign = 0;
            this.countLess = 0;
            this.raisedToBack = 0;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$startRaiseToEarSensors$8();
                }
            });
            this.sensorsStarted = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRaiseToEarSensors$8() {
        Sensor sensor = this.gravitySensor;
        if (sensor != null) {
            this.sensorManager.registerListener(this, sensor, 30000);
        }
        Sensor sensor2 = this.linearSensor;
        if (sensor2 != null) {
            this.sensorManager.registerListener(this, sensor2, 30000);
        }
        Sensor sensor3 = this.accelerometerSensor;
        if (sensor3 != null) {
            this.sensorManager.registerListener(this, sensor3, 30000);
        }
        this.sensorManager.registerListener(this, this.proximitySensor, 3);
    }

    public void stopRaiseToEarSensors(ChatActivity chatActivity, boolean z, boolean z2) {
        PowerManager.WakeLock wakeLock;
        if (this.ignoreOnPause) {
            this.ignoreOnPause = false;
            return;
        }
        if (z2) {
            stopRecording(z ? 2 : 0, false, 0);
        }
        if (!this.sensorsStarted || this.ignoreOnPause) {
            return;
        }
        if ((this.accelerometerSensor == null && (this.gravitySensor == null || this.linearAcceleration == null)) || this.proximitySensor == null || this.raiseChat != chatActivity) {
            return;
        }
        this.raiseChat = null;
        this.sensorsStarted = false;
        this.accelerometerVertical = false;
        this.proximityTouched = false;
        this.raiseToEarRecord = false;
        this.useFrontSpeaker = false;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$stopRaiseToEarSensors$9();
            }
        });
        if (this.proximityHasDifferentValues && (wakeLock = this.proximityWakeLock) != null && wakeLock.isHeld()) {
            this.proximityWakeLock.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRaiseToEarSensors$9() {
        Sensor sensor = this.linearSensor;
        if (sensor != null) {
            this.sensorManager.unregisterListener(this, sensor);
        }
        Sensor sensor2 = this.gravitySensor;
        if (sensor2 != null) {
            this.sensorManager.unregisterListener(this, sensor2);
        }
        Sensor sensor3 = this.accelerometerSensor;
        if (sensor3 != null) {
            this.sensorManager.unregisterListener(this, sensor3);
        }
        this.sensorManager.unregisterListener(this, this.proximitySensor);
    }

    public void cleanupPlayer(boolean z, boolean z2) {
        cleanupPlayer(z, z2, false, false);
    }

    public void cleanupPlayer(boolean z, boolean z2, boolean z3, boolean z4) {
        PipRoundVideoView pipRoundVideoView;
        MessageObject messageObject;
        if (this.audioPlayer != null) {
            ValueAnimator valueAnimator = this.audioVolumeAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllUpdateListeners();
                this.audioVolumeAnimator.cancel();
            }
            if (this.audioPlayer.isPlaying() && (messageObject = this.playingMessageObject) != null && !messageObject.isVoice()) {
                final VideoPlayer videoPlayer = this.audioPlayer;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.audioVolume, 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        MediaController.this.lambda$cleanupPlayer$10(videoPlayer, valueAnimator2);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.messenger.MediaController.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        try {
                            videoPlayer.releasePlayer(true);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                });
                ofFloat.setDuration(300L);
                ofFloat.start();
            } else {
                try {
                    this.audioPlayer.releasePlayer(true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            this.audioPlayer = null;
            Theme.unrefAudioVisualizeDrawable(this.playingMessageObject);
        } else {
            VideoPlayer videoPlayer2 = this.videoPlayer;
            if (videoPlayer2 != null) {
                this.currentAspectRatioFrameLayout = null;
                this.currentTextureViewContainer = null;
                this.currentAspectRatioFrameLayoutReady = false;
                this.isDrawingWasReady = false;
                this.currentTextureView = null;
                this.goingToShowMessageObject = null;
                if (z4) {
                    PhotoViewer.getInstance().injectVideoPlayer(this.videoPlayer);
                    MessageObject messageObject2 = this.playingMessageObject;
                    this.goingToShowMessageObject = messageObject2;
                    NotificationCenter.getInstance(messageObject2.currentAccount).postNotificationName(NotificationCenter.messagePlayingGoingToStop, this.playingMessageObject, Boolean.TRUE);
                } else {
                    long currentPosition = videoPlayer2.getCurrentPosition();
                    MessageObject messageObject3 = this.playingMessageObject;
                    if (messageObject3 != null && messageObject3.isVideo() && currentPosition > 0) {
                        MessageObject messageObject4 = this.playingMessageObject;
                        messageObject4.audioProgressMs = (int) currentPosition;
                        NotificationCenter.getInstance(messageObject4.currentAccount).postNotificationName(NotificationCenter.messagePlayingGoingToStop, this.playingMessageObject, Boolean.FALSE);
                    }
                    this.videoPlayer.releasePlayer(true);
                    this.videoPlayer = null;
                }
                try {
                    this.baseActivity.getWindow().clearFlags(128);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (this.playingMessageObject != null && !z4) {
                    AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                    FileLoader.getInstance(this.playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
                }
            }
        }
        stopProgressTimer();
        this.lastProgress = 0L;
        this.isPaused = false;
        if (!this.useFrontSpeaker && !SharedConfig.enabledRaiseTo(true)) {
            ChatActivity chatActivity = this.raiseChat;
            stopRaiseToEarSensors(chatActivity, false, false);
            this.raiseChat = chatActivity;
        }
        PowerManager.WakeLock wakeLock = this.proximityWakeLock;
        if (wakeLock != null && wakeLock.isHeld() && !this.proximityTouched) {
            this.proximityWakeLock.release();
        }
        MessageObject messageObject5 = this.playingMessageObject;
        if (messageObject5 != null) {
            if (this.downloadingCurrentMessage) {
                FileLoader.getInstance(messageObject5.currentAccount).cancelLoadFile(this.playingMessageObject.getDocument());
            }
            MessageObject messageObject6 = this.playingMessageObject;
            if (z) {
                messageObject6.resetPlayingProgress();
                NotificationCenter.getInstance(messageObject6.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
            }
            this.playingMessageObject = null;
            this.downloadingCurrentMessage = false;
            if (z) {
                NotificationsController.audioManager.abandonAudioFocus(this);
                this.hasAudioFocus = 0;
                int i = -1;
                ArrayList<MessageObject> arrayList = this.voiceMessagesPlaylist;
                if (arrayList != null) {
                    if (z3 && (i = arrayList.indexOf(messageObject6)) >= 0) {
                        this.voiceMessagesPlaylist.remove(i);
                        this.voiceMessagesPlaylistMap.remove(messageObject6.getId());
                        if (this.voiceMessagesPlaylist.isEmpty()) {
                            this.voiceMessagesPlaylist = null;
                            this.voiceMessagesPlaylistMap = null;
                        }
                    } else {
                        this.voiceMessagesPlaylist = null;
                        this.voiceMessagesPlaylistMap = null;
                    }
                }
                ArrayList<MessageObject> arrayList2 = this.voiceMessagesPlaylist;
                if (arrayList2 != null && i < arrayList2.size()) {
                    MessageObject messageObject7 = this.voiceMessagesPlaylist.get(i);
                    playMessage(messageObject7);
                    if (!messageObject7.isRoundVideo() && (pipRoundVideoView = this.pipRoundVideoView) != null) {
                        pipRoundVideoView.close(true);
                        this.pipRoundVideoView = null;
                    }
                } else {
                    if ((messageObject6.isVoice() || messageObject6.isRoundVideo()) && messageObject6.getId() != 0) {
                        startRecordingIfFromSpeaker();
                    }
                    NotificationCenter.getInstance(messageObject6.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidReset, Integer.valueOf(messageObject6.getId()), Boolean.valueOf(z2));
                    this.pipSwitchingState = 0;
                    PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
                    if (pipRoundVideoView2 != null) {
                        pipRoundVideoView2.close(true);
                        this.pipRoundVideoView = null;
                    }
                }
            }
            if (z2) {
                ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupPlayer$10(VideoPlayer videoPlayer, ValueAnimator valueAnimator) {
        videoPlayer.setVolume((this.audioFocus != 1 ? VOLUME_NORMAL : VOLUME_DUCK) * ((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public boolean isGoingToShowMessageObject(MessageObject messageObject) {
        return this.goingToShowMessageObject == messageObject;
    }

    public void resetGoingToShowMessageObject() {
        this.goingToShowMessageObject = null;
    }

    private boolean isSamePlayingMessage(MessageObject messageObject) {
        MessageObject messageObject2 = this.playingMessageObject;
        if (messageObject2 != null && messageObject2.getDialogId() == messageObject.getDialogId() && this.playingMessageObject.getId() == messageObject.getId()) {
            if ((this.playingMessageObject.eventId == 0) == (messageObject.eventId == 0)) {
                return true;
            }
        }
        return false;
    }

    public boolean seekToProgress(MessageObject messageObject, float f) {
        MessageObject messageObject2 = this.playingMessageObject;
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && messageObject2 != null && isSamePlayingMessage(messageObject)) {
            try {
                VideoPlayer videoPlayer = this.audioPlayer;
                if (videoPlayer != null) {
                    long duration = videoPlayer.getDuration();
                    if (duration == -9223372036854775807L) {
                        this.seekToProgressPending = f;
                    } else {
                        messageObject2.audioProgress = f;
                        long j = (int) (((float) duration) * f);
                        this.audioPlayer.seekTo(j);
                        this.lastProgress = j;
                    }
                } else {
                    VideoPlayer videoPlayer2 = this.videoPlayer;
                    if (videoPlayer2 != null) {
                        videoPlayer2.seekTo(((float) videoPlayer2.getDuration()) * f);
                    }
                }
                NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidSeek, Integer.valueOf(messageObject2.getId()), Float.valueOf(f));
                return true;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return false;
    }

    public long getDuration() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer == null) {
            return 0L;
        }
        return videoPlayer.getDuration();
    }

    public MessageObject getPlayingMessageObject() {
        return this.playingMessageObject;
    }

    public int getPlayingMessageObjectNum() {
        return this.currentPlaylistNum;
    }

    private void buildShuffledPlayList() {
        if (this.playlist.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.playlist);
        this.shuffledPlaylist.clear();
        MessageObject messageObject = this.playlist.get(this.currentPlaylistNum);
        arrayList.remove(this.currentPlaylistNum);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            int nextInt = Utilities.random.nextInt(arrayList.size());
            this.shuffledPlaylist.add((MessageObject) arrayList.get(nextInt));
            arrayList.remove(nextInt);
        }
        this.shuffledPlaylist.add(messageObject);
        this.currentPlaylistNum = this.shuffledPlaylist.size() - 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0103  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadMoreMusic() {
        MessageObject messageObject;
        final int i;
        long j;
        long j2;
        long j3;
        TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal;
        if (this.loadingPlaylist || (messageObject = this.playingMessageObject) == null || messageObject.scheduled || DialogObject.isEncryptedDialog(messageObject.getDialogId()) || (i = this.playlistClassGuid) == 0) {
            return;
        }
        PlaylistGlobalSearchParams playlistGlobalSearchParams = this.playlistGlobalSearchParams;
        if (playlistGlobalSearchParams != null) {
            if (playlistGlobalSearchParams.endReached || this.playlist.isEmpty()) {
                return;
            }
            final int i2 = this.playlist.get(0).currentAccount;
            if (this.playlistGlobalSearchParams.dialogId != 0) {
                TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
                PlaylistGlobalSearchParams playlistGlobalSearchParams2 = this.playlistGlobalSearchParams;
                tLRPC$TL_messages_search.q = playlistGlobalSearchParams2.query;
                tLRPC$TL_messages_search.limit = 20;
                FiltersView.MediaFilterData mediaFilterData = playlistGlobalSearchParams2.filter;
                tLRPC$TL_messages_search.filter = mediaFilterData == null ? new TLRPC$TL_inputMessagesFilterEmpty() : mediaFilterData.filter;
                tLRPC$TL_messages_search.peer = AccountInstance.getInstance(i2).getMessagesController().getInputPeer(this.playlistGlobalSearchParams.dialogId);
                ArrayList<MessageObject> arrayList = this.playlist;
                tLRPC$TL_messages_search.offset_id = arrayList.get(arrayList.size() - 1).getId();
                PlaylistGlobalSearchParams playlistGlobalSearchParams3 = this.playlistGlobalSearchParams;
                long j4 = playlistGlobalSearchParams3.minDate;
                if (j4 > 0) {
                    tLRPC$TL_messages_search.min_date = (int) (j4 / 1000);
                }
                long j5 = playlistGlobalSearchParams3.maxDate;
                tLRPC$TL_messages_searchGlobal = tLRPC$TL_messages_search;
                if (j5 > 0) {
                    tLRPC$TL_messages_search.min_date = (int) (j5 / 1000);
                    tLRPC$TL_messages_searchGlobal = tLRPC$TL_messages_search;
                }
            } else {
                TLRPC$TL_messages_searchGlobal tLRPC$TL_messages_searchGlobal2 = new TLRPC$TL_messages_searchGlobal();
                tLRPC$TL_messages_searchGlobal2.limit = 20;
                PlaylistGlobalSearchParams playlistGlobalSearchParams4 = this.playlistGlobalSearchParams;
                tLRPC$TL_messages_searchGlobal2.q = playlistGlobalSearchParams4.query;
                tLRPC$TL_messages_searchGlobal2.filter = playlistGlobalSearchParams4.filter.filter;
                ArrayList<MessageObject> arrayList2 = this.playlist;
                MessageObject messageObject2 = arrayList2.get(arrayList2.size() - 1);
                tLRPC$TL_messages_searchGlobal2.offset_id = messageObject2.getId();
                PlaylistGlobalSearchParams playlistGlobalSearchParams5 = this.playlistGlobalSearchParams;
                tLRPC$TL_messages_searchGlobal2.offset_rate = playlistGlobalSearchParams5.nextSearchRate;
                tLRPC$TL_messages_searchGlobal2.flags |= 1;
                tLRPC$TL_messages_searchGlobal2.folder_id = playlistGlobalSearchParams5.folderId;
                TLRPC$Peer tLRPC$Peer = messageObject2.messageOwner.peer_id;
                long j6 = tLRPC$Peer.channel_id;
                if (j6 == 0) {
                    j6 = tLRPC$Peer.chat_id;
                    if (j6 == 0) {
                        j = tLRPC$Peer.user_id;
                        tLRPC$TL_messages_searchGlobal2.offset_peer = MessagesController.getInstance(i2).getInputPeer(j);
                        PlaylistGlobalSearchParams playlistGlobalSearchParams6 = this.playlistGlobalSearchParams;
                        j2 = playlistGlobalSearchParams6.minDate;
                        if (j2 > 0) {
                            tLRPC$TL_messages_searchGlobal2.min_date = (int) (j2 / 1000);
                        }
                        j3 = playlistGlobalSearchParams6.maxDate;
                        tLRPC$TL_messages_searchGlobal = tLRPC$TL_messages_searchGlobal2;
                        if (j3 > 0) {
                            tLRPC$TL_messages_searchGlobal2.min_date = (int) (j3 / 1000);
                            tLRPC$TL_messages_searchGlobal = tLRPC$TL_messages_searchGlobal2;
                        }
                    }
                }
                j = -j6;
                tLRPC$TL_messages_searchGlobal2.offset_peer = MessagesController.getInstance(i2).getInputPeer(j);
                PlaylistGlobalSearchParams playlistGlobalSearchParams62 = this.playlistGlobalSearchParams;
                j2 = playlistGlobalSearchParams62.minDate;
                if (j2 > 0) {
                }
                j3 = playlistGlobalSearchParams62.maxDate;
                tLRPC$TL_messages_searchGlobal = tLRPC$TL_messages_searchGlobal2;
                if (j3 > 0) {
                }
            }
            this.loadingPlaylist = true;
            ConnectionsManager.getInstance(i2).sendRequest(tLRPC$TL_messages_searchGlobal, new RequestDelegate() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda43
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MediaController.this.lambda$loadMoreMusic$12(i, i2, tLObject, tLRPC$TL_error);
                }
            });
            return;
        }
        boolean[] zArr = this.playlistEndReached;
        if (!zArr[0]) {
            this.loadingPlaylist = true;
            AccountInstance.getInstance(this.playingMessageObject.currentAccount).getMediaDataController().loadMedia(this.playingMessageObject.getDialogId(), 50, this.playlistMaxId[0], 0, 4, 0, 1, this.playlistClassGuid, 0);
        } else if (this.playlistMergeDialogId == 0 || zArr[1]) {
        } else {
            this.loadingPlaylist = true;
            AccountInstance.getInstance(this.playingMessageObject.currentAccount).getMediaDataController().loadMedia(this.playlistMergeDialogId, 50, this.playlistMaxId[0], 0, 4, 0, 1, this.playlistClassGuid, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMoreMusic$12(final int i, final int i2, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$loadMoreMusic$11(i, tLRPC$TL_error, tLObject, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMoreMusic$11(int i, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i2) {
        PlaylistGlobalSearchParams playlistGlobalSearchParams;
        if (this.playlistClassGuid != i || (playlistGlobalSearchParams = this.playlistGlobalSearchParams) == null || this.playingMessageObject == null || tLRPC$TL_error != null) {
            return;
        }
        this.loadingPlaylist = false;
        TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
        playlistGlobalSearchParams.nextSearchRate = tLRPC$messages_Messages.next_rate;
        MessagesStorage.getInstance(i2).putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
        MessagesController.getInstance(i2).putUsers(tLRPC$messages_Messages.users, false);
        MessagesController.getInstance(i2).putChats(tLRPC$messages_Messages.chats, false);
        int size = tLRPC$messages_Messages.messages.size();
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            MessageObject messageObject = new MessageObject(i2, tLRPC$messages_Messages.messages.get(i4), false, true);
            if (!this.playlistMap.containsKey(Integer.valueOf(messageObject.getId()))) {
                this.playlist.add(0, messageObject);
                this.playlistMap.put(Integer.valueOf(messageObject.getId()), messageObject);
                i3++;
            }
        }
        sortPlaylist();
        this.loadingPlaylist = false;
        this.playlistGlobalSearchParams.endReached = this.playlist.size() == this.playlistGlobalSearchParams.totalCount;
        if (SharedConfig.shuffleMusic) {
            buildShuffledPlayList();
        }
        if (i3 != 0) {
            NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.moreMusicDidLoad, Integer.valueOf(i3));
        }
    }

    public boolean setPlaylist(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, PlaylistGlobalSearchParams playlistGlobalSearchParams) {
        return setPlaylist(arrayList, messageObject, j, true, playlistGlobalSearchParams);
    }

    public boolean setPlaylist(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j) {
        return setPlaylist(arrayList, messageObject, j, true, null);
    }

    public boolean setPlaylist(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, boolean z, PlaylistGlobalSearchParams playlistGlobalSearchParams) {
        if (this.playingMessageObject == messageObject) {
            int indexOf = this.playlist.indexOf(messageObject);
            if (indexOf >= 0) {
                this.currentPlaylistNum = indexOf;
            }
            return playMessage(messageObject);
        }
        this.forceLoopCurrentPlaylist = !z;
        this.playlistMergeDialogId = j;
        this.playMusicAgain = !this.playlist.isEmpty();
        clearPlaylist();
        this.playlistGlobalSearchParams = playlistGlobalSearchParams;
        boolean z2 = false;
        if (!arrayList.isEmpty() && DialogObject.isEncryptedDialog(arrayList.get(0).getDialogId())) {
            z2 = true;
        }
        int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int i2 = Integer.MIN_VALUE;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            MessageObject messageObject2 = arrayList.get(size);
            if (messageObject2.isMusic()) {
                int id = messageObject2.getId();
                if (id > 0 || z2) {
                    i = Math.min(i, id);
                    i2 = Math.max(i2, id);
                }
                this.playlist.add(messageObject2);
                this.playlistMap.put(Integer.valueOf(id), messageObject2);
            }
        }
        sortPlaylist();
        int indexOf2 = this.playlist.indexOf(messageObject);
        this.currentPlaylistNum = indexOf2;
        if (indexOf2 == -1) {
            clearPlaylist();
            this.currentPlaylistNum = this.playlist.size();
            this.playlist.add(messageObject);
            this.playlistMap.put(Integer.valueOf(messageObject.getId()), messageObject);
        }
        if (messageObject.isMusic() && !messageObject.scheduled) {
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
            }
            if (z) {
                if (this.playlistGlobalSearchParams == null) {
                    MediaDataController.getInstance(messageObject.currentAccount).loadMusic(messageObject.getDialogId(), i, i2);
                } else {
                    this.playlistClassGuid = ConnectionsManager.generateClassGuid();
                }
            }
        }
        return playMessage(messageObject);
    }

    private void sortPlaylist() {
        Collections.sort(this.playlist, MediaController$$ExternalSyntheticLambda42.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortPlaylist$13(MessageObject messageObject, MessageObject messageObject2) {
        int compare;
        int id = messageObject.getId();
        int id2 = messageObject2.getId();
        long j = messageObject.messageOwner.grouped_id;
        long j2 = messageObject2.messageOwner.grouped_id;
        if (id >= 0 || id2 >= 0) {
            if (j != 0 && j == j2) {
                compare = Integer.compare(id2, id);
            } else {
                return Integer.compare(id, id2);
            }
        } else if (j != 0 && j == j2) {
            compare = Integer.compare(id, id2);
        } else {
            return Integer.compare(id2, id);
        }
        return -compare;
    }

    public void playNextMessage() {
        playNextMessageWithoutOrder(false);
    }

    public boolean findMessageInPlaylistAndPlay(MessageObject messageObject) {
        int indexOf = this.playlist.indexOf(messageObject);
        if (indexOf == -1) {
            return playMessage(messageObject);
        }
        playMessageAtIndex(indexOf);
        return true;
    }

    public void playMessageAtIndex(int i) {
        int i2 = this.currentPlaylistNum;
        if (i2 < 0 || i2 >= this.playlist.size()) {
            return;
        }
        this.currentPlaylistNum = i;
        this.playMusicAgain = true;
        MessageObject messageObject = this.playlist.get(i);
        if (this.playingMessageObject != null && !isSamePlayingMessage(messageObject)) {
            this.playingMessageObject.resetPlayingProgress();
        }
        playMessage(messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playNextMessageWithoutOrder(boolean z) {
        int i;
        ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (z && (((i = SharedConfig.repeatMode) == 2 || (i == 1 && arrayList.size() == 1)) && !this.forceLoopCurrentPlaylist)) {
            cleanupPlayer(false, false);
            MessageObject messageObject = arrayList.get(this.currentPlaylistNum);
            messageObject.audioProgress = 0.0f;
            messageObject.audioProgressSec = 0;
            playMessage(messageObject);
            return;
        }
        if (traversePlaylist(arrayList, SharedConfig.playOrderReversed ? 1 : -1) && z && SharedConfig.repeatMode == 0 && !this.forceLoopCurrentPlaylist) {
            VideoPlayer videoPlayer = this.audioPlayer;
            if (videoPlayer == null && this.videoPlayer == null) {
                return;
            }
            if (videoPlayer != null) {
                try {
                    videoPlayer.releasePlayer(true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.audioPlayer = null;
                Theme.unrefAudioVisualizeDrawable(this.playingMessageObject);
            } else {
                this.currentAspectRatioFrameLayout = null;
                this.currentTextureViewContainer = null;
                this.currentAspectRatioFrameLayoutReady = false;
                this.currentTextureView = null;
                this.videoPlayer.releasePlayer(true);
                this.videoPlayer = null;
                try {
                    this.baseActivity.getWindow().clearFlags(128);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                FileLoader.getInstance(this.playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
            }
            stopProgressTimer();
            this.lastProgress = 0L;
            this.isPaused = true;
            MessageObject messageObject2 = this.playingMessageObject;
            messageObject2.audioProgress = 0.0f;
            messageObject2.audioProgressSec = 0;
            NotificationCenter.getInstance(messageObject2.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
            NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
            return;
        }
        int i2 = this.currentPlaylistNum;
        if (i2 < 0 || i2 >= arrayList.size()) {
            return;
        }
        MessageObject messageObject3 = this.playingMessageObject;
        if (messageObject3 != null) {
            messageObject3.resetPlayingProgress();
        }
        this.playMusicAgain = true;
        playMessage(arrayList.get(this.currentPlaylistNum));
    }

    public void playPreviousMessage() {
        int i;
        ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (arrayList.isEmpty() || (i = this.currentPlaylistNum) < 0 || i >= arrayList.size()) {
            return;
        }
        MessageObject messageObject = arrayList.get(this.currentPlaylistNum);
        if (messageObject.audioProgressSec > 10) {
            seekToProgress(messageObject, 0.0f);
            return;
        }
        traversePlaylist(arrayList, SharedConfig.playOrderReversed ? -1 : 1);
        if (this.currentPlaylistNum >= arrayList.size()) {
            return;
        }
        this.playMusicAgain = true;
        playMessage(arrayList.get(this.currentPlaylistNum));
    }

    private boolean traversePlaylist(ArrayList<MessageObject> arrayList, int i) {
        MessageObject messageObject;
        int i2;
        MessageObject messageObject2;
        int i3 = this.currentPlaylistNum;
        boolean z = ConnectionsManager.getInstance(UserConfig.selectedAccount).getConnectionState() == 2;
        this.currentPlaylistNum += i;
        if (z) {
            while (this.currentPlaylistNum < arrayList.size() && (i2 = this.currentPlaylistNum) >= 0 && ((messageObject2 = arrayList.get(i2)) == null || !messageObject2.mediaExists)) {
                this.currentPlaylistNum += i;
            }
        }
        if (this.currentPlaylistNum >= arrayList.size() || this.currentPlaylistNum < 0) {
            this.currentPlaylistNum = this.currentPlaylistNum >= arrayList.size() ? 0 : arrayList.size() - 1;
            if (z) {
                while (true) {
                    int i4 = this.currentPlaylistNum;
                    if (i4 < 0 || i4 >= arrayList.size()) {
                        break;
                    }
                    int i5 = this.currentPlaylistNum;
                    if (i > 0) {
                        if (i5 > i3) {
                            break;
                        }
                        messageObject = arrayList.get(this.currentPlaylistNum);
                        if (messageObject == null && messageObject.mediaExists) {
                            break;
                        }
                        this.currentPlaylistNum += i;
                    } else {
                        if (i5 < i3) {
                            break;
                        }
                        messageObject = arrayList.get(this.currentPlaylistNum);
                        if (messageObject == null) {
                        }
                        this.currentPlaylistNum += i;
                    }
                }
                if (this.currentPlaylistNum >= arrayList.size() || this.currentPlaylistNum < 0) {
                    this.currentPlaylistNum = this.currentPlaylistNum < arrayList.size() ? arrayList.size() - 1 : 0;
                    return true;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkIsNextMediaFileDownloaded() {
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject == null || !messageObject.isMusic()) {
            return;
        }
        checkIsNextMusicFileDownloaded(this.playingMessageObject.currentAccount);
    }

    private void checkIsNextVoiceFileDownloaded(int i) {
        ArrayList<MessageObject> arrayList = this.voiceMessagesPlaylist;
        if (arrayList == null || arrayList.size() < 2) {
            return;
        }
        MessageObject messageObject = this.voiceMessagesPlaylist.get(1);
        String str = messageObject.messageOwner.attachPath;
        File file = null;
        if (str != null && str.length() > 0) {
            File file2 = new File(messageObject.messageOwner.attachPath);
            if (file2.exists()) {
                file = file2;
            }
        }
        File pathToMessage = file != null ? file : FileLoader.getInstance(i).getPathToMessage(messageObject.messageOwner);
        pathToMessage.exists();
        if (pathToMessage == file || pathToMessage.exists()) {
            return;
        }
        FileLoader.getInstance(i).loadFile(messageObject.getDocument(), messageObject, 0, 0);
    }

    private void checkIsNextMusicFileDownloaded(int i) {
        int i2;
        if (DownloadController.getInstance(i).canDownloadNextTrack()) {
            ArrayList<MessageObject> arrayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
            if (arrayList == null || arrayList.size() < 2) {
                return;
            }
            if (SharedConfig.playOrderReversed) {
                i2 = this.currentPlaylistNum + 1;
                if (i2 >= arrayList.size()) {
                    i2 = 0;
                }
            } else {
                i2 = this.currentPlaylistNum - 1;
                if (i2 < 0) {
                    i2 = arrayList.size() - 1;
                }
            }
            if (i2 < 0 || i2 >= arrayList.size()) {
                return;
            }
            MessageObject messageObject = arrayList.get(i2);
            File file = null;
            if (!TextUtils.isEmpty(messageObject.messageOwner.attachPath)) {
                File file2 = new File(messageObject.messageOwner.attachPath);
                if (file2.exists()) {
                    file = file2;
                }
            }
            File pathToMessage = file != null ? file : FileLoader.getInstance(i).getPathToMessage(messageObject.messageOwner);
            pathToMessage.exists();
            if (pathToMessage == file || pathToMessage.exists() || !messageObject.isMusic()) {
                return;
            }
            FileLoader.getInstance(i).loadFile(messageObject.getDocument(), messageObject, 0, 0);
        }
    }

    public void setVoiceMessagesPlaylist(ArrayList<MessageObject> arrayList, boolean z) {
        ArrayList<MessageObject> arrayList2 = arrayList != null ? new ArrayList<>(arrayList) : null;
        this.voiceMessagesPlaylist = arrayList2;
        if (arrayList2 != null) {
            this.voiceMessagesPlaylistUnread = z;
            this.voiceMessagesPlaylistMap = new SparseArray<>();
            for (int i = 0; i < this.voiceMessagesPlaylist.size(); i++) {
                MessageObject messageObject = this.voiceMessagesPlaylist.get(i);
                this.voiceMessagesPlaylistMap.put(messageObject.getId(), messageObject);
            }
        }
    }

    private void checkAudioFocus(MessageObject messageObject) {
        int i;
        int requestAudioFocus;
        if (messageObject.isVoice() || messageObject.isRoundVideo()) {
            i = this.useFrontSpeaker ? 3 : 2;
        } else {
            i = 1;
        }
        if (this.hasAudioFocus != i) {
            this.hasAudioFocus = i;
            if (i == 3) {
                requestAudioFocus = NotificationsController.audioManager.requestAudioFocus(this, 0, 1);
            } else {
                requestAudioFocus = NotificationsController.audioManager.requestAudioFocus(this, 3, (i != 2 || SharedConfig.pauseMusicOnMedia) ? 1 : 3);
            }
            if (requestAudioFocus == 1) {
                this.audioFocus = 2;
            }
        }
    }

    public boolean isPiPShown() {
        return this.pipRoundVideoView != null;
    }

    public void setCurrentVideoVisible(boolean z) {
        AspectRatioFrameLayout aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        if (aspectRatioFrameLayout == null) {
            return;
        }
        if (z) {
            PipRoundVideoView pipRoundVideoView = this.pipRoundVideoView;
            if (pipRoundVideoView != null) {
                this.pipSwitchingState = 2;
                pipRoundVideoView.close(true);
                this.pipRoundVideoView = null;
                return;
            }
            if (aspectRatioFrameLayout.getParent() == null) {
                this.currentTextureViewContainer.addView(this.currentAspectRatioFrameLayout);
            }
            this.videoPlayer.setTextureView(this.currentTextureView);
        } else if (aspectRatioFrameLayout.getParent() != null) {
            this.pipSwitchingState = 1;
            this.currentTextureViewContainer.removeView(this.currentAspectRatioFrameLayout);
        } else {
            if (this.pipRoundVideoView == null) {
                try {
                    PipRoundVideoView pipRoundVideoView2 = new PipRoundVideoView();
                    this.pipRoundVideoView = pipRoundVideoView2;
                    pipRoundVideoView2.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda12
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.this.lambda$setCurrentVideoVisible$14();
                        }
                    });
                } catch (Exception unused) {
                    this.pipRoundVideoView = null;
                }
            }
            PipRoundVideoView pipRoundVideoView3 = this.pipRoundVideoView;
            if (pipRoundVideoView3 != null) {
                this.videoPlayer.setTextureView(pipRoundVideoView3.getTextureView());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCurrentVideoVisible$14() {
        cleanupPlayer(true, true);
    }

    public void setTextureView(TextureView textureView, AspectRatioFrameLayout aspectRatioFrameLayout, FrameLayout frameLayout, boolean z) {
        setTextureView(textureView, aspectRatioFrameLayout, frameLayout, z, null);
    }

    public void setTextureView(TextureView textureView, AspectRatioFrameLayout aspectRatioFrameLayout, FrameLayout frameLayout, boolean z, Runnable runnable) {
        if (textureView == null) {
            return;
        }
        boolean z2 = true;
        if (!z && this.currentTextureView == textureView) {
            this.pipSwitchingState = 1;
            this.currentTextureView = null;
            this.currentAspectRatioFrameLayout = null;
            this.currentTextureViewContainer = null;
        } else if (this.videoPlayer == null || textureView == this.currentTextureView) {
        } else {
            this.isDrawingWasReady = (aspectRatioFrameLayout == null || !aspectRatioFrameLayout.isDrawingReady()) ? false : false;
            this.currentTextureView = textureView;
            if (runnable != null && this.pipRoundVideoView == null) {
                try {
                    PipRoundVideoView pipRoundVideoView = new PipRoundVideoView();
                    this.pipRoundVideoView = pipRoundVideoView;
                    pipRoundVideoView.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.this.lambda$setTextureView$15();
                        }
                    });
                } catch (Exception unused) {
                    this.pipRoundVideoView = null;
                }
            }
            PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
            if (pipRoundVideoView2 != null) {
                this.videoPlayer.setTextureView(pipRoundVideoView2.getTextureView());
            } else {
                this.videoPlayer.setTextureView(this.currentTextureView);
            }
            this.currentAspectRatioFrameLayout = aspectRatioFrameLayout;
            this.currentTextureViewContainer = frameLayout;
            if (!this.currentAspectRatioFrameLayoutReady || aspectRatioFrameLayout == null) {
                return;
            }
            aspectRatioFrameLayout.setAspectRatio(this.currentAspectRatioFrameLayoutRatio, this.currentAspectRatioFrameLayoutRotation);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTextureView$15() {
        cleanupPlayer(true, true);
    }

    public void setBaseActivity(Activity activity, boolean z) {
        if (z) {
            this.baseActivity = activity;
        } else if (this.baseActivity == activity) {
            this.baseActivity = null;
        }
    }

    public void setFeedbackView(View view, boolean z) {
        if (z) {
            this.feedbackView = view;
        } else if (this.feedbackView == view) {
            this.feedbackView = null;
        }
    }

    public void setPlaybackSpeed(boolean z, float f) {
        if (z) {
            if (this.currentMusicPlaybackSpeed >= 6.0f && f == VOLUME_NORMAL && this.playingMessageObject != null) {
                this.audioPlayer.pause();
                final MessageObject messageObject = this.playingMessageObject;
                final float f2 = messageObject.audioProgress;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda32
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.this.lambda$setPlaybackSpeed$16(messageObject, f2);
                    }
                }, 50L);
            }
            this.currentMusicPlaybackSpeed = f;
            if (Math.abs(f - VOLUME_NORMAL) > 0.001f) {
                this.fastMusicPlaybackSpeed = f;
            }
        } else {
            this.currentPlaybackSpeed = f;
            if (Math.abs(f - VOLUME_NORMAL) > 0.001f) {
                this.fastPlaybackSpeed = f;
            }
        }
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer != null) {
            videoPlayer.setPlaybackSpeed(Math.round(f * 10.0f) / 10.0f);
        } else {
            VideoPlayer videoPlayer2 = this.videoPlayer;
            if (videoPlayer2 != null) {
                videoPlayer2.setPlaybackSpeed(Math.round(f * 10.0f) / 10.0f);
            }
        }
        MessagesController.getGlobalMainSettings().edit().putFloat(z ? "musicPlaybackSpeed" : "playbackSpeed", f).putFloat(z ? "fastMusicPlaybackSpeed" : "fastPlaybackSpeed", z ? this.fastMusicPlaybackSpeed : this.fastPlaybackSpeed).commit();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.messagePlayingSpeedChanged, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setPlaybackSpeed$16(MessageObject messageObject, float f) {
        if (this.audioPlayer == null || this.playingMessageObject == null || this.isPaused) {
            return;
        }
        if (isSamePlayingMessage(messageObject)) {
            seekToProgress(this.playingMessageObject, f);
        }
        this.audioPlayer.play();
    }

    public float getPlaybackSpeed(boolean z) {
        return z ? this.currentMusicPlaybackSpeed : this.currentPlaybackSpeed;
    }

    public float getFastPlaybackSpeed(boolean z) {
        return z ? this.fastMusicPlaybackSpeed : this.fastPlaybackSpeed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVideoState(MessageObject messageObject, int[] iArr, boolean z, boolean z2, int i) {
        MessageObject messageObject2;
        if (this.videoPlayer == null) {
            return;
        }
        if (i != 4 && i != 1) {
            try {
                this.baseActivity.getWindow().addFlags(128);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                this.baseActivity.getWindow().clearFlags(128);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        if (i == 3) {
            this.playerWasReady = true;
            MessageObject messageObject3 = this.playingMessageObject;
            if (messageObject3 != null && (messageObject3.isVideo() || this.playingMessageObject.isRoundVideo())) {
                AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                FileLoader.getInstance(messageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
            }
            this.currentAspectRatioFrameLayoutReady = true;
        } else if (i == 2) {
            if (!z2 || (messageObject2 = this.playingMessageObject) == null) {
                return;
            }
            if (messageObject2.isVideo() || this.playingMessageObject.isRoundVideo()) {
                if (this.playerWasReady) {
                    this.setLoadingRunnable.run();
                } else {
                    AndroidUtilities.runOnUIThread(this.setLoadingRunnable, 1000L);
                }
            }
        } else if (this.videoPlayer.isPlaying() && i == 4) {
            if (this.playingMessageObject.isVideo() && !z && (iArr == null || iArr[0] < 4)) {
                this.videoPlayer.seekTo(0L);
                if (iArr != null) {
                    iArr[0] = iArr[0] + 1;
                    return;
                }
                return;
            }
            cleanupPlayer(true, true, true, false);
        }
    }

    public void injectVideoPlayer(VideoPlayer videoPlayer, MessageObject messageObject) {
        if (videoPlayer == null || messageObject == null) {
            return;
        }
        FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
        this.playerWasReady = false;
        clearPlaylist();
        this.videoPlayer = videoPlayer;
        this.playingMessageObject = messageObject;
        int i = this.playerNum + 1;
        this.playerNum = i;
        videoPlayer.setDelegate(new 7(i, messageObject, null, true));
        this.currentAspectRatioFrameLayoutReady = false;
        TextureView textureView = this.currentTextureView;
        if (textureView != null) {
            this.videoPlayer.setTextureView(textureView);
        }
        checkAudioFocus(messageObject);
        setPlayerVolume();
        this.isPaused = false;
        this.lastProgress = 0L;
        MessageObject messageObject2 = this.playingMessageObject;
        this.playingMessageObject = messageObject;
        if (!SharedConfig.enabledRaiseTo(true)) {
            startRaiseToEarSensors(this.raiseChat);
        }
        startProgressTimer(this.playingMessageObject);
        NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidStart, messageObject, messageObject2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 7 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ boolean val$destroyAtEnd;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ int[] val$playCount;
        final /* synthetic */ int val$tag;

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        7(int i, MessageObject messageObject, int[] iArr, boolean z) {
            this.val$tag = i;
            this.val$messageObject = messageObject;
            this.val$playCount = iArr;
            this.val$destroyAtEnd = z;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, int i) {
            if (this.val$tag != MediaController.this.playerNum) {
                return;
            }
            MediaController.this.updateVideoState(this.val$messageObject, this.val$playCount, this.val$destroyAtEnd, z, i);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            FileLog.e(exc);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            MediaController.this.currentAspectRatioFrameLayoutRotation = i3;
            if (i3 != 90 && i3 != 270) {
                i2 = i;
                i = i2;
            }
            MediaController.this.currentAspectRatioFrameLayoutRatio = i == 0 ? MediaController.VOLUME_NORMAL : (i2 * f) / i;
            if (MediaController.this.currentAspectRatioFrameLayout != null) {
                MediaController.this.currentAspectRatioFrameLayout.setAspectRatio(MediaController.this.currentAspectRatioFrameLayoutRatio, MediaController.this.currentAspectRatioFrameLayoutRotation);
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            if (MediaController.this.currentAspectRatioFrameLayout == null || MediaController.this.currentAspectRatioFrameLayout.isDrawingReady()) {
                return;
            }
            MediaController.this.isDrawingWasReady = true;
            MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
            MediaController.this.currentTextureViewContainer.setTag(1);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            if (MediaController.this.videoPlayer == null) {
                return false;
            }
            if (MediaController.this.pipSwitchingState == 2) {
                if (MediaController.this.currentAspectRatioFrameLayout != null) {
                    if (MediaController.this.isDrawingWasReady) {
                        MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                    }
                    if (MediaController.this.currentAspectRatioFrameLayout.getParent() == null) {
                        MediaController.this.currentTextureViewContainer.addView(MediaController.this.currentAspectRatioFrameLayout);
                    }
                    if (MediaController.this.currentTextureView.getSurfaceTexture() != surfaceTexture) {
                        MediaController.this.currentTextureView.setSurfaceTexture(surfaceTexture);
                    }
                    MediaController.this.videoPlayer.setTextureView(MediaController.this.currentTextureView);
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            } else if (MediaController.this.pipSwitchingState == 1) {
                if (MediaController.this.baseActivity != null) {
                    if (MediaController.this.pipRoundVideoView == null) {
                        try {
                            MediaController.this.pipRoundVideoView = new PipRoundVideoView();
                            MediaController.this.pipRoundVideoView.show(MediaController.this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$7$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MediaController.7.this.lambda$onSurfaceDestroyed$0();
                                }
                            });
                        } catch (Exception unused) {
                            MediaController.this.pipRoundVideoView = null;
                        }
                    }
                    if (MediaController.this.pipRoundVideoView != null) {
                        if (MediaController.this.pipRoundVideoView.getTextureView().getSurfaceTexture() != surfaceTexture) {
                            MediaController.this.pipRoundVideoView.getTextureView().setSurfaceTexture(surfaceTexture);
                        }
                        MediaController.this.videoPlayer.setTextureView(MediaController.this.pipRoundVideoView.getTextureView());
                    }
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isInjectingVideoPlayer()) {
                PhotoViewer.getInstance().injectVideoPlayerSurface(surfaceTexture);
                return true;
            } else {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceDestroyed$0() {
            MediaController.this.cleanupPlayer(true, true);
        }
    }

    public void playEmojiSound(final AccountInstance accountInstance, String str, final MessagesController.EmojiSound emojiSound, final boolean z) {
        if (emojiSound == null) {
            return;
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$playEmojiSound$19(emojiSound, accountInstance, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playEmojiSound$19(MessagesController.EmojiSound emojiSound, final AccountInstance accountInstance, boolean z) {
        final TLRPC$TL_document tLRPC$TL_document = new TLRPC$TL_document();
        tLRPC$TL_document.access_hash = emojiSound.accessHash;
        tLRPC$TL_document.id = emojiSound.id;
        tLRPC$TL_document.mime_type = "sound/ogg";
        tLRPC$TL_document.file_reference = emojiSound.fileReference;
        tLRPC$TL_document.dc_id = accountInstance.getConnectionsManager().getCurrentDatacenterId();
        final File pathToAttach = FileLoader.getInstance(accountInstance.getCurrentAccount()).getPathToAttach(tLRPC$TL_document, true);
        if (!pathToAttach.exists()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.lambda$playEmojiSound$18(AccountInstance.this, tLRPC$TL_document);
                }
            });
        } else if (z) {
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$playEmojiSound$17(pathToAttach);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playEmojiSound$17(File file) {
        try {
            int i = this.emojiSoundPlayerNum + 1;
            this.emojiSoundPlayerNum = i;
            VideoPlayer videoPlayer = this.emojiSoundPlayer;
            if (videoPlayer != null) {
                videoPlayer.releasePlayer(true);
            }
            VideoPlayer videoPlayer2 = new VideoPlayer(false);
            this.emojiSoundPlayer = videoPlayer2;
            videoPlayer2.setDelegate(new 8(i));
            this.emojiSoundPlayer.preparePlayer(Uri.fromFile(file), "other");
            this.emojiSoundPlayer.setStreamType(3);
            this.emojiSoundPlayer.play();
        } catch (Exception e) {
            FileLog.e(e);
            VideoPlayer videoPlayer3 = this.emojiSoundPlayer;
            if (videoPlayer3 != null) {
                videoPlayer3.releasePlayer(true);
                this.emojiSoundPlayer = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 8 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ int val$tag;

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
        }

        8(int i) {
            this.val$tag = i;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, final int i) {
            final int i2 = this.val$tag;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$8$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.8.this.lambda$onStateChanged$0(i2, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStateChanged$0(int i, int i2) {
            if (i == MediaController.this.emojiSoundPlayerNum && i2 == 4 && MediaController.this.emojiSoundPlayer != null) {
                try {
                    MediaController.this.emojiSoundPlayer.releasePlayer(true);
                    MediaController.this.emojiSoundPlayer = null;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playEmojiSound$18(AccountInstance accountInstance, TLRPC$Document tLRPC$Document) {
        accountInstance.getFileLoader().loadFile(tLRPC$Document, null, 1, 1);
    }

    public void checkVolumeBarUI() {
        if (this.isSilent) {
            return;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (Math.abs(currentTimeMillis - volumeBarLastTimeShown) < 5000) {
                return;
            }
            AudioManager audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            int i = this.useFrontSpeaker ? 0 : 3;
            int streamVolume = audioManager.getStreamVolume(i);
            if (streamVolume == 0) {
                audioManager.adjustStreamVolume(i, streamVolume, 1);
                volumeBarLastTimeShown = currentTimeMillis;
            }
        } catch (Exception unused) {
        }
    }

    private void setBluetoothScoOn(boolean z) {
        AudioManager audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        if (!(audioManager.isBluetoothScoAvailableOffCall() && SharedConfig.recordViaSco) && z) {
            return;
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            try {
                if (defaultAdapter.getProfileConnectionState(1) != 2) {
                }
                if (!z && !audioManager.isBluetoothScoOn()) {
                    audioManager.startBluetoothSco();
                    return;
                } else if (z && audioManager.isBluetoothScoOn()) {
                    audioManager.stopBluetoothSco();
                    return;
                }
            } catch (SecurityException unused) {
                return;
            } catch (Throwable th) {
                FileLog.e(th);
                return;
            }
        }
        if (z) {
            return;
        }
        if (!z) {
        }
        if (z) {
        }
    }

    public boolean playMessage(MessageObject messageObject) {
        return playMessage(messageObject, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:182:0x047c  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0487  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0587  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0463 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x04a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean playMessage(final MessageObject messageObject, boolean z) {
        File file;
        boolean z2;
        boolean z3;
        PipRoundVideoView pipRoundVideoView;
        PowerManager.WakeLock wakeLock;
        if (messageObject == null) {
            return false;
        }
        this.isSilent = z;
        checkVolumeBarUI();
        if ((this.audioPlayer != null || this.videoPlayer != null) && isSamePlayingMessage(messageObject)) {
            if (this.isPaused) {
                resumeAudio(messageObject);
            }
            if (!SharedConfig.enabledRaiseTo(true)) {
                startRaiseToEarSensors(this.raiseChat);
            }
            return true;
        }
        if (!messageObject.isOut() && messageObject.isContentUnread()) {
            MessagesController.getInstance(messageObject.currentAccount).markMessageContentAsRead(messageObject);
        }
        boolean z4 = this.playMusicAgain;
        boolean z5 = !z4;
        MessageObject messageObject2 = this.playingMessageObject;
        if (messageObject2 != null) {
            if (!z4) {
                messageObject2.resetPlayingProgress();
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
            }
            z5 = false;
        }
        cleanupPlayer(z5, false);
        this.shouldSavePositionForCurrentAudio = null;
        this.lastSaveTime = 0L;
        this.playMusicAgain = false;
        this.seekToProgressPending = 0.0f;
        String str = messageObject.messageOwner.attachPath;
        if (str == null || str.length() <= 0) {
            file = null;
            z2 = false;
        } else {
            File file2 = new File(messageObject.messageOwner.attachPath);
            z2 = file2.exists();
            file = !z2 ? null : file2;
        }
        final File pathToMessage = file != null ? file : FileLoader.getInstance(messageObject.currentAccount).getPathToMessage(messageObject.messageOwner);
        boolean z6 = SharedConfig.streamMedia && (messageObject.isMusic() || messageObject.isRoundVideo() || (messageObject.isVideo() && messageObject.canStreamVideo())) && !DialogObject.isEncryptedDialog(messageObject.getDialogId());
        if (pathToMessage != file && !(z2 = pathToMessage.exists()) && !z6) {
            FileLoader.getInstance(messageObject.currentAccount).loadFile(messageObject.getDocument(), messageObject, 0, 0);
            this.downloadingCurrentMessage = true;
            this.isPaused = false;
            this.lastProgress = 0L;
            this.audioInfo = null;
            this.playingMessageObject = messageObject;
            if (messageObject.isMusic()) {
                try {
                    ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            } else {
                ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
            }
            NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
            return true;
        }
        boolean z7 = z2;
        this.downloadingCurrentMessage = false;
        if (messageObject.isMusic()) {
            checkIsNextMusicFileDownloaded(messageObject.currentAccount);
        } else {
            checkIsNextVoiceFileDownloaded(messageObject.currentAccount);
        }
        AspectRatioFrameLayout aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            this.isDrawingWasReady = false;
            aspectRatioFrameLayout.setDrawingReady(false);
        }
        boolean isVideo = messageObject.isVideo();
        if (messageObject.isRoundVideo() || isVideo) {
            final File file3 = pathToMessage;
            File file4 = file;
            FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
            this.playerWasReady = false;
            if (isVideo && (messageObject.messageOwner.peer_id.channel_id != 0 || messageObject.audioProgress > 0.1f)) {
                z3 = false;
                int[] iArr = (isVideo || messageObject.getDuration() > 30) ? null : new int[]{1};
                clearPlaylist();
                VideoPlayer videoPlayer = new VideoPlayer();
                this.videoPlayer = videoPlayer;
                videoPlayer.setLooping(z);
                int i = this.playerNum + 1;
                this.playerNum = i;
                this.videoPlayer.setDelegate(new 9(i, messageObject, iArr, z3));
                this.currentAspectRatioFrameLayoutReady = false;
                if (this.pipRoundVideoView == null || !MessagesController.getInstance(messageObject.currentAccount).isDialogVisible(messageObject.getDialogId(), messageObject.scheduled)) {
                    if (this.pipRoundVideoView == null) {
                        try {
                            PipRoundVideoView pipRoundVideoView2 = new PipRoundVideoView();
                            this.pipRoundVideoView = pipRoundVideoView2;
                            pipRoundVideoView2.show(this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda11
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MediaController.this.lambda$playMessage$20();
                                }
                            });
                        } catch (Exception unused) {
                            this.pipRoundVideoView = null;
                        }
                    }
                    pipRoundVideoView = this.pipRoundVideoView;
                    if (pipRoundVideoView != null) {
                        this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                    }
                } else {
                    TextureView textureView = this.currentTextureView;
                    if (textureView != null) {
                        this.videoPlayer.setTextureView(textureView);
                    }
                }
                if (!z7) {
                    if (!messageObject.mediaExists && file3 != file4) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda35
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaController.lambda$playMessage$21(MessageObject.this, file3);
                            }
                        });
                    }
                    this.videoPlayer.preparePlayer(Uri.fromFile(file3), "other");
                } else {
                    try {
                        int fileReference = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                        TLRPC$Document document = messageObject.getDocument();
                        StringBuilder sb = new StringBuilder();
                        sb.append("?account=");
                        sb.append(messageObject.currentAccount);
                        sb.append("&id=");
                        sb.append(document.id);
                        sb.append("&hash=");
                        sb.append(document.access_hash);
                        sb.append("&dc=");
                        sb.append(document.dc_id);
                        sb.append("&size=");
                        sb.append(document.size);
                        sb.append("&mime=");
                        sb.append(URLEncoder.encode(document.mime_type, "UTF-8"));
                        sb.append("&rid=");
                        sb.append(fileReference);
                        sb.append("&name=");
                        sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), "UTF-8"));
                        sb.append("&reference=");
                        byte[] bArr = document.file_reference;
                        if (bArr == null) {
                            bArr = new byte[0];
                        }
                        sb.append(Utilities.bytesToHex(bArr));
                        this.videoPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb.toString()), "other");
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                if (!messageObject.isRoundVideo()) {
                    this.videoPlayer.setStreamType(this.useFrontSpeaker ? 0 : 3);
                    if (Math.abs(this.currentPlaybackSpeed - VOLUME_NORMAL) > 0.001f) {
                        this.videoPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                    }
                    float f = messageObject.forceSeekTo;
                    if (f >= 0.0f) {
                        this.seekToProgressPending = f;
                        messageObject.audioProgress = f;
                        messageObject.forceSeekTo = -1.0f;
                    }
                } else {
                    this.videoPlayer.setStreamType(3);
                }
            }
            z3 = true;
            if (isVideo) {
            }
            clearPlaylist();
            VideoPlayer videoPlayer2 = new VideoPlayer();
            this.videoPlayer = videoPlayer2;
            videoPlayer2.setLooping(z);
            int i2 = this.playerNum + 1;
            this.playerNum = i2;
            this.videoPlayer.setDelegate(new 9(i2, messageObject, iArr, z3));
            this.currentAspectRatioFrameLayoutReady = false;
            if (this.pipRoundVideoView == null) {
            }
            if (this.pipRoundVideoView == null) {
            }
            pipRoundVideoView = this.pipRoundVideoView;
            if (pipRoundVideoView != null) {
            }
            if (!z7) {
            }
            if (!messageObject.isRoundVideo()) {
            }
        } else {
            PipRoundVideoView pipRoundVideoView3 = this.pipRoundVideoView;
            if (pipRoundVideoView3 != null) {
                pipRoundVideoView3.close(true);
                this.pipRoundVideoView = null;
            }
            try {
                VideoPlayer videoPlayer3 = new VideoPlayer();
                this.audioPlayer = videoPlayer3;
                final int i3 = this.playerNum + 1;
                this.playerNum = i3;
                videoPlayer3.setDelegate(new VideoPlayer.VideoPlayerDelegate() { // from class: org.telegram.messenger.MediaController.10
                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public void onError(VideoPlayer videoPlayer4, Exception exc) {
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public void onRenderedFirstFrame() {
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
                        VideoPlayer.VideoPlayerDelegate.-CC.$default$onRenderedFirstFrame(this, eventTime);
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
                        VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekFinished(this, eventTime);
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
                        VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekStarted(this, eventTime);
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                        return false;
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public void onVideoSizeChanged(int i4, int i5, int i6, float f2) {
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                    public void onStateChanged(boolean z8, int i4) {
                        if (i3 != MediaController.this.playerNum) {
                            return;
                        }
                        if (i4 != 4 && ((i4 != 1 && i4 != 2) || !z8 || messageObject.audioProgress < 0.999f)) {
                            if (MediaController.this.audioPlayer == null || MediaController.this.seekToProgressPending == 0.0f) {
                                return;
                            }
                            if (i4 == 3 || i4 == 1) {
                                long duration = (int) (((float) MediaController.this.audioPlayer.getDuration()) * MediaController.this.seekToProgressPending);
                                MediaController.this.audioPlayer.seekTo(duration);
                                MediaController.this.lastProgress = duration;
                                MediaController.this.seekToProgressPending = 0.0f;
                                return;
                            }
                            return;
                        }
                        MessageObject messageObject3 = messageObject;
                        messageObject3.audioProgress = MediaController.VOLUME_NORMAL;
                        NotificationCenter.getInstance(messageObject3.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(messageObject.getId()), 0);
                        if (!MediaController.this.playlist.isEmpty() && (MediaController.this.playlist.size() > 1 || !messageObject.isVoice())) {
                            MediaController.this.playNextMessageWithoutOrder(true);
                        } else {
                            MediaController.this.cleanupPlayer(true, true, messageObject.isVoice(), false);
                        }
                    }
                });
                this.audioPlayer.setAudioVisualizerDelegate(new VideoPlayer.AudioVisualizerDelegate() { // from class: org.telegram.messenger.MediaController.11
                    @Override // org.telegram.ui.Components.VideoPlayer.AudioVisualizerDelegate
                    public void onVisualizerUpdate(boolean z8, boolean z9, float[] fArr) {
                        Theme.getCurrentAudiVisualizerDrawable().setWaveform(z8, z9, fArr);
                    }

                    @Override // org.telegram.ui.Components.VideoPlayer.AudioVisualizerDelegate
                    public boolean needUpdate() {
                        return Theme.getCurrentAudiVisualizerDrawable().getParentView() != null;
                    }
                });
                if (z7) {
                    if (!messageObject.mediaExists && pathToMessage != file) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda36
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaController.lambda$playMessage$22(MessageObject.this, pathToMessage);
                            }
                        });
                    }
                    this.audioPlayer.preparePlayer(Uri.fromFile(pathToMessage), "other");
                    this.isStreamingCurrentAudio = false;
                } else {
                    int fileReference2 = FileLoader.getInstance(messageObject.currentAccount).getFileReference(messageObject);
                    TLRPC$Document document2 = messageObject.getDocument();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("?account=");
                    sb2.append(messageObject.currentAccount);
                    sb2.append("&id=");
                    sb2.append(document2.id);
                    sb2.append("&hash=");
                    sb2.append(document2.access_hash);
                    sb2.append("&dc=");
                    sb2.append(document2.dc_id);
                    sb2.append("&size=");
                    sb2.append(document2.size);
                    sb2.append("&mime=");
                    sb2.append(URLEncoder.encode(document2.mime_type, "UTF-8"));
                    sb2.append("&rid=");
                    sb2.append(fileReference2);
                    sb2.append("&name=");
                    sb2.append(URLEncoder.encode(FileLoader.getDocumentFileName(document2), "UTF-8"));
                    sb2.append("&reference=");
                    byte[] bArr2 = document2.file_reference;
                    if (bArr2 == null) {
                        bArr2 = new byte[0];
                    }
                    sb2.append(Utilities.bytesToHex(bArr2));
                    this.audioPlayer.preparePlayer(Uri.parse("tg://" + messageObject.getFileName() + sb2.toString()), "other");
                    this.isStreamingCurrentAudio = true;
                }
                if (messageObject.isVoice()) {
                    String fileName = messageObject.getFileName();
                    if (fileName != null && messageObject.getDuration() >= 300) {
                        float f2 = ApplicationLoader.applicationContext.getSharedPreferences("media_saved_pos", 0).getFloat(fileName, -1.0f);
                        if (f2 > 0.0f && f2 < 0.99f) {
                            this.seekToProgressPending = f2;
                            messageObject.audioProgress = f2;
                        }
                        this.shouldSavePositionForCurrentAudio = fileName;
                    }
                    if (Math.abs(this.currentPlaybackSpeed - VOLUME_NORMAL) > 0.001f) {
                        this.audioPlayer.setPlaybackSpeed(Math.round(this.currentPlaybackSpeed * 10.0f) / 10.0f);
                    }
                    this.audioInfo = null;
                    clearPlaylist();
                } else {
                    try {
                        this.audioInfo = AudioInfo.getAudioInfo(pathToMessage);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    String fileName2 = messageObject.getFileName();
                    if (!TextUtils.isEmpty(fileName2) && messageObject.getDuration() >= 600) {
                        float f3 = ApplicationLoader.applicationContext.getSharedPreferences("media_saved_pos", 0).getFloat(fileName2, -1.0f);
                        if (f3 > 0.0f && f3 < 0.999f) {
                            this.seekToProgressPending = f3;
                            messageObject.audioProgress = f3;
                        }
                        this.shouldSavePositionForCurrentAudio = fileName2;
                        if (Math.abs(this.currentMusicPlaybackSpeed - VOLUME_NORMAL) > 0.001f) {
                            this.audioPlayer.setPlaybackSpeed(Math.round(this.currentMusicPlaybackSpeed * 10.0f) / 10.0f);
                        }
                    }
                }
                float f4 = messageObject.forceSeekTo;
                if (f4 >= 0.0f) {
                    this.seekToProgressPending = f4;
                    messageObject.audioProgress = f4;
                    messageObject.forceSeekTo = -1.0f;
                }
                this.audioPlayer.setStreamType(this.useFrontSpeaker ? 0 : 3);
                this.audioPlayer.play();
                if (!messageObject.isVoice()) {
                    ValueAnimator valueAnimator = this.audioVolumeAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        this.audioVolumeAnimator.cancel();
                    }
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(this.audioVolume, VOLUME_NORMAL);
                    this.audioVolumeAnimator = ofFloat;
                    ofFloat.addUpdateListener(this.audioVolumeUpdateListener);
                    this.audioVolumeAnimator.setDuration(300L);
                    this.audioVolumeAnimator.start();
                } else {
                    this.audioVolume = VOLUME_NORMAL;
                    setPlayerVolume();
                }
            } catch (Exception e3) {
                FileLog.e(e3);
                NotificationCenter notificationCenter = NotificationCenter.getInstance(messageObject.currentAccount);
                int i4 = NotificationCenter.messagePlayingPlayStateChanged;
                Object[] objArr = new Object[1];
                MessageObject messageObject3 = this.playingMessageObject;
                objArr[0] = Integer.valueOf(messageObject3 != null ? messageObject3.getId() : 0);
                notificationCenter.postNotificationName(i4, objArr);
                VideoPlayer videoPlayer4 = this.audioPlayer;
                if (videoPlayer4 != null) {
                    videoPlayer4.releasePlayer(true);
                    this.audioPlayer = null;
                    Theme.unrefAudioVisualizeDrawable(this.playingMessageObject);
                    this.isPaused = false;
                    this.playingMessageObject = null;
                    this.downloadingCurrentMessage = false;
                }
                return false;
            }
        }
        checkAudioFocus(messageObject);
        setPlayerVolume();
        this.isPaused = false;
        this.lastProgress = 0L;
        this.playingMessageObject = messageObject;
        if (!SharedConfig.enabledRaiseTo(true)) {
            startRaiseToEarSensors(this.raiseChat);
        }
        if (!ApplicationLoader.mainInterfacePaused && (wakeLock = this.proximityWakeLock) != null && !wakeLock.isHeld() && ((this.playingMessageObject.isVoice() || this.playingMessageObject.isRoundVideo()) && SharedConfig.enabledRaiseTo(false) && ignoreAccelerometerGestures())) {
            this.proximityWakeLock.acquire();
        }
        startProgressTimer(this.playingMessageObject);
        NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidStart, messageObject, messageObject2);
        VideoPlayer videoPlayer5 = this.videoPlayer;
        if (videoPlayer5 != null) {
            try {
                if (this.playingMessageObject.audioProgress != 0.0f) {
                    long duration = videoPlayer5.getDuration();
                    if (duration == -9223372036854775807L) {
                        duration = this.playingMessageObject.getDuration() * 1000;
                    }
                    MessageObject messageObject4 = this.playingMessageObject;
                    int i5 = (int) (((float) duration) * messageObject4.audioProgress);
                    int i6 = messageObject4.audioProgressMs;
                    if (i6 != 0) {
                        messageObject4.audioProgressMs = 0;
                        i5 = i6;
                    }
                    this.videoPlayer.seekTo(i5);
                }
            } catch (Exception e4) {
                MessageObject messageObject5 = this.playingMessageObject;
                messageObject5.audioProgress = 0.0f;
                messageObject5.audioProgressSec = 0;
                NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
                FileLog.e(e4);
            }
            this.videoPlayer.play();
        } else {
            VideoPlayer videoPlayer6 = this.audioPlayer;
            if (videoPlayer6 != null) {
                try {
                    if (this.playingMessageObject.audioProgress != 0.0f) {
                        long duration2 = videoPlayer6.getDuration();
                        if (duration2 == -9223372036854775807L) {
                            duration2 = this.playingMessageObject.getDuration() * 1000;
                        }
                        this.audioPlayer.seekTo((int) (((float) duration2) * this.playingMessageObject.audioProgress));
                    }
                } catch (Exception e5) {
                    this.playingMessageObject.resetPlayingProgress();
                    NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), 0);
                    FileLog.e(e5);
                }
            }
        }
        MessageObject messageObject6 = this.playingMessageObject;
        if (messageObject6 != null && messageObject6.isMusic()) {
            try {
                ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
                return true;
            } catch (Throwable th2) {
                FileLog.e(th2);
                return true;
            }
        }
        ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 9 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ boolean val$destroyAtEnd;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ int[] val$playCount;
        final /* synthetic */ int val$tag;

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* bridge */ /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        9(int i, MessageObject messageObject, int[] iArr, boolean z) {
            this.val$tag = i;
            this.val$messageObject = messageObject;
            this.val$playCount = iArr;
            this.val$destroyAtEnd = z;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, int i) {
            if (this.val$tag != MediaController.this.playerNum) {
                return;
            }
            MediaController.this.updateVideoState(this.val$messageObject, this.val$playCount, this.val$destroyAtEnd, z, i);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            FileLog.e(exc);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            MediaController.this.currentAspectRatioFrameLayoutRotation = i3;
            if (i3 != 90 && i3 != 270) {
                i2 = i;
                i = i2;
            }
            MediaController.this.currentAspectRatioFrameLayoutRatio = i == 0 ? MediaController.VOLUME_NORMAL : (i2 * f) / i;
            if (MediaController.this.currentAspectRatioFrameLayout != null) {
                MediaController.this.currentAspectRatioFrameLayout.setAspectRatio(MediaController.this.currentAspectRatioFrameLayoutRatio, MediaController.this.currentAspectRatioFrameLayoutRotation);
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            if (MediaController.this.currentAspectRatioFrameLayout == null || MediaController.this.currentAspectRatioFrameLayout.isDrawingReady()) {
                return;
            }
            MediaController.this.isDrawingWasReady = true;
            MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
            MediaController.this.currentTextureViewContainer.setTag(1);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            if (MediaController.this.videoPlayer == null) {
                return false;
            }
            if (MediaController.this.pipSwitchingState == 2) {
                if (MediaController.this.currentAspectRatioFrameLayout != null) {
                    if (MediaController.this.isDrawingWasReady) {
                        MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                    }
                    if (MediaController.this.currentAspectRatioFrameLayout.getParent() == null) {
                        MediaController.this.currentTextureViewContainer.addView(MediaController.this.currentAspectRatioFrameLayout);
                    }
                    if (MediaController.this.currentTextureView.getSurfaceTexture() != surfaceTexture) {
                        MediaController.this.currentTextureView.setSurfaceTexture(surfaceTexture);
                    }
                    MediaController.this.videoPlayer.setTextureView(MediaController.this.currentTextureView);
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            } else if (MediaController.this.pipSwitchingState == 1) {
                if (MediaController.this.baseActivity != null) {
                    if (MediaController.this.pipRoundVideoView == null) {
                        try {
                            MediaController.this.pipRoundVideoView = new PipRoundVideoView();
                            MediaController.this.pipRoundVideoView.show(MediaController.this.baseActivity, new Runnable() { // from class: org.telegram.messenger.MediaController$9$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MediaController.9.this.lambda$onSurfaceDestroyed$0();
                                }
                            });
                        } catch (Exception unused) {
                            MediaController.this.pipRoundVideoView = null;
                        }
                    }
                    if (MediaController.this.pipRoundVideoView != null) {
                        if (MediaController.this.pipRoundVideoView.getTextureView().getSurfaceTexture() != surfaceTexture) {
                            MediaController.this.pipRoundVideoView.getTextureView().setSurfaceTexture(surfaceTexture);
                        }
                        MediaController.this.videoPlayer.setTextureView(MediaController.this.pipRoundVideoView.getTextureView());
                    }
                }
                MediaController.this.pipSwitchingState = 0;
                return true;
            } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isInjectingVideoPlayer()) {
                PhotoViewer.getInstance().injectVideoPlayerSurface(surfaceTexture);
                return true;
            } else {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceDestroyed$0() {
            MediaController.this.cleanupPlayer(true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playMessage$20() {
        cleanupPlayer(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playMessage$21(MessageObject messageObject, File file) {
        NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject.getDocument()), file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playMessage$22(MessageObject messageObject, File file) {
        NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.fileLoaded, FileLoader.getAttachFileName(messageObject.getDocument()), file);
    }

    public static boolean ignoreAccelerometerGestures() {
        return Build.MANUFACTURER.equalsIgnoreCase("samsung");
    }

    public void updateSilent(boolean z) {
        this.isSilent = z;
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setLooping(z);
        }
        setPlayerVolume();
        checkVolumeBarUI();
        MessageObject messageObject = this.playingMessageObject;
        if (messageObject != null) {
            NotificationCenter notificationCenter = NotificationCenter.getInstance(messageObject.currentAccount);
            int i = NotificationCenter.messagePlayingPlayStateChanged;
            Object[] objArr = new Object[1];
            MessageObject messageObject2 = this.playingMessageObject;
            objArr[0] = Integer.valueOf(messageObject2 != null ? messageObject2.getId() : 0);
            notificationCenter.postNotificationName(i, objArr);
        }
    }

    public AudioInfo getAudioInfo() {
        return this.audioInfo;
    }

    public void setPlaybackOrderType(int i) {
        boolean z = SharedConfig.shuffleMusic;
        SharedConfig.setPlaybackOrderType(i);
        boolean z2 = SharedConfig.shuffleMusic;
        if (z != z2) {
            if (z2) {
                buildShuffledPlayList();
                return;
            }
            MessageObject messageObject = this.playingMessageObject;
            if (messageObject != null) {
                int indexOf = this.playlist.indexOf(messageObject);
                this.currentPlaylistNum = indexOf;
                if (indexOf == -1) {
                    clearPlaylist();
                    cleanupPlayer(true, true);
                }
            }
        }
    }

    public boolean isStreamingCurrentAudio() {
        return this.isStreamingCurrentAudio;
    }

    public boolean isCurrentPlayer(VideoPlayer videoPlayer) {
        return this.videoPlayer == videoPlayer || this.audioPlayer == videoPlayer;
    }

    /* renamed from: pauseMessage */
    public boolean lambda$startAudioAgain$7(MessageObject messageObject) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null && isSamePlayingMessage(messageObject)) {
            stopProgressTimer();
            try {
                if (this.audioPlayer != null) {
                    if (!this.playingMessageObject.isVoice() && this.playingMessageObject.getDuration() * (VOLUME_NORMAL - this.playingMessageObject.audioProgress) > VOLUME_NORMAL && LaunchActivity.isResumed) {
                        ValueAnimator valueAnimator = this.audioVolumeAnimator;
                        if (valueAnimator != null) {
                            valueAnimator.removeAllUpdateListeners();
                            this.audioVolumeAnimator.cancel();
                        }
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(VOLUME_NORMAL, 0.0f);
                        this.audioVolumeAnimator = ofFloat;
                        ofFloat.addUpdateListener(this.audioVolumeUpdateListener);
                        this.audioVolumeAnimator.setDuration(300L);
                        this.audioVolumeAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.messenger.MediaController.12
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (MediaController.this.audioPlayer != null) {
                                    MediaController.this.audioPlayer.pause();
                                }
                            }
                        });
                        this.audioVolumeAnimator.start();
                    } else {
                        this.audioPlayer.pause();
                    }
                } else {
                    VideoPlayer videoPlayer = this.videoPlayer;
                    if (videoPlayer != null) {
                        videoPlayer.pause();
                    }
                }
                this.isPaused = true;
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                return true;
            } catch (Exception e) {
                FileLog.e(e);
                this.isPaused = false;
            }
        }
        return false;
    }

    private boolean resumeAudio(MessageObject messageObject) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null && isSamePlayingMessage(messageObject)) {
            try {
                startProgressTimer(this.playingMessageObject);
                ValueAnimator valueAnimator = this.audioVolumeAnimator;
                if (valueAnimator != null) {
                    valueAnimator.removeAllListeners();
                    this.audioVolumeAnimator.cancel();
                }
                if (!messageObject.isVoice() && !messageObject.isRoundVideo()) {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(this.audioVolume, VOLUME_NORMAL);
                    this.audioVolumeAnimator = ofFloat;
                    ofFloat.addUpdateListener(this.audioVolumeUpdateListener);
                    this.audioVolumeAnimator.setDuration(300L);
                    this.audioVolumeAnimator.start();
                } else {
                    this.audioVolume = VOLUME_NORMAL;
                    setPlayerVolume();
                }
                VideoPlayer videoPlayer = this.audioPlayer;
                if (videoPlayer != null) {
                    videoPlayer.play();
                } else {
                    VideoPlayer videoPlayer2 = this.videoPlayer;
                    if (videoPlayer2 != null) {
                        videoPlayer2.play();
                    }
                }
                checkAudioFocus(messageObject);
                this.isPaused = false;
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                return true;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return false;
    }

    public boolean isVideoDrawingReady() {
        AspectRatioFrameLayout aspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        return aspectRatioFrameLayout != null && aspectRatioFrameLayout.isDrawingReady();
    }

    public ArrayList<MessageObject> getPlaylist() {
        return this.playlist;
    }

    public boolean isPlayingMessage(MessageObject messageObject) {
        MessageObject messageObject2;
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && (messageObject2 = this.playingMessageObject) != null) {
            long j = messageObject2.eventId;
            if (j != 0 && j == messageObject.eventId) {
                return !this.downloadingCurrentMessage;
            }
            if (isSamePlayingMessage(messageObject)) {
                return !this.downloadingCurrentMessage;
            }
        }
        return false;
    }

    public boolean isPlayingMessageAndReadyToDraw(MessageObject messageObject) {
        return this.isDrawingWasReady && isPlayingMessage(messageObject);
    }

    public boolean isMessagePaused() {
        return this.isPaused || this.downloadingCurrentMessage;
    }

    public boolean isDownloadingCurrentMessage() {
        return this.downloadingCurrentMessage;
    }

    public void setReplyingMessage(MessageObject messageObject, MessageObject messageObject2) {
        this.recordReplyingMsg = messageObject;
        this.recordReplyingTopMsg = messageObject2;
    }

    public void requestAudioFocus(boolean z) {
        if (z) {
            if (!this.hasRecordAudioFocus && SharedConfig.pauseMusicOnRecord && NotificationsController.audioManager.requestAudioFocus(this.audioRecordFocusChangedListener, 3, 2) == 1) {
                this.hasRecordAudioFocus = true;
            }
        } else if (this.hasRecordAudioFocus) {
            NotificationsController.audioManager.abandonAudioFocus(this.audioRecordFocusChangedListener);
            this.hasRecordAudioFocus = false;
        }
    }

    public void startRecording(final int i, final long j, final MessageObject messageObject, final MessageObject messageObject2, final int i2, boolean z) {
        boolean z2;
        boolean z3;
        MessageObject messageObject3 = this.playingMessageObject;
        if (messageObject3 == null || !isPlayingMessage(messageObject3) || isMessagePaused()) {
            z2 = z;
            z3 = false;
        } else {
            z2 = z;
            z3 = true;
        }
        this.manualRecording = z2;
        requestAudioFocus(true);
        try {
            this.feedbackView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        DispatchQueue dispatchQueue = this.recordQueue;
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$startRecording$27(i, i2, j, messageObject, messageObject2);
            }
        };
        this.recordStartRunnable = runnable;
        dispatchQueue.postRunnable(runnable, z3 ? 500L : 50L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$27(final int i, final int i2, long j, MessageObject messageObject, MessageObject messageObject2) {
        if (this.audioRecorder != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$startRecording$23(i, i2);
                }
            });
            return;
        }
        setBluetoothScoOn(true);
        this.sendAfterDone = 0;
        TLRPC$TL_document tLRPC$TL_document = new TLRPC$TL_document();
        this.recordingAudio = tLRPC$TL_document;
        this.recordingGuid = i2;
        tLRPC$TL_document.file_reference = new byte[0];
        tLRPC$TL_document.dc_id = Integer.MIN_VALUE;
        tLRPC$TL_document.id = SharedConfig.getLastLocalId();
        this.recordingAudio.user_id = UserConfig.getInstance(i).getClientUserId();
        TLRPC$TL_document tLRPC$TL_document2 = this.recordingAudio;
        tLRPC$TL_document2.mime_type = "audio/ogg";
        tLRPC$TL_document2.file_reference = new byte[0];
        SharedConfig.saveConfig();
        File directory = FileLoader.getDirectory(1);
        this.recordingAudioFile = new File(directory, System.currentTimeMillis() + "_" + FileLoader.getAttachFileName(this.recordingAudio)) { // from class: org.telegram.messenger.MediaController.13
            @Override // java.io.File
            public boolean delete() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file");
                }
                return super.delete();
            }
        };
        FileLoader.getDirectory(4).mkdirs();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start recording internal " + this.recordingAudioFile.getPath() + " " + this.recordingAudioFile.exists());
        }
        AutoDeleteMediaTask.lockFile(this.recordingAudioFile);
        try {
            if (startRecord(this.recordingAudioFile.getPath(), this.sampleRate) == 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda21
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.this.lambda$startRecording$24(i, i2);
                    }
                });
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("cant init encoder");
                    return;
                }
                return;
            }
            this.audioRecorder = new AudioRecord(0, this.sampleRate, 16, 2, this.recordBufferSize);
            this.recordStartTime = System.currentTimeMillis();
            this.recordTimeCount = 0L;
            this.writedFrame = 0;
            this.samplesCount = 0L;
            this.recordDialogId = j;
            this.recordingCurrentAccount = i;
            this.recordReplyingMsg = messageObject;
            this.recordReplyingTopMsg = messageObject2;
            this.fileBuffer.rewind();
            this.audioRecorder.startRecording();
            this.recordQueue.postRunnable(this.recordRunnable);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$startRecording$26(i, i2);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
            this.recordingAudio = null;
            stopRecord();
            AutoDeleteMediaTask.unlockFile(this.recordingAudioFile);
            this.recordingAudioFile.delete();
            this.recordingAudioFile = null;
            try {
                this.audioRecorder.release();
                this.audioRecorder = null;
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            setBluetoothScoOn(false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$startRecording$25(i, i2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$23(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStartError, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$24(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStartError, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$25(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStartError, Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRecording$26(int i, int i2) {
        this.recordStartRunnable = null;
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStarted, Integer.valueOf(i2), Boolean.TRUE);
    }

    public void generateWaveform(final MessageObject messageObject) {
        final String str = messageObject.getId() + "_" + messageObject.getDialogId();
        final String absolutePath = FileLoader.getInstance(messageObject.currentAccount).getPathToMessage(messageObject.messageOwner).getAbsolutePath();
        if (this.generatingWaveform.containsKey(str)) {
            return;
        }
        this.generatingWaveform.put(str, messageObject);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$generateWaveform$29(absolutePath, str, messageObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateWaveform$29(String str, final String str2, final MessageObject messageObject) {
        try {
            final byte[] waveform = getWaveform(str);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$generateWaveform$28(str2, waveform, messageObject);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateWaveform$28(String str, byte[] bArr, MessageObject messageObject) {
        MessageObject remove = this.generatingWaveform.remove(str);
        if (remove == null || bArr == null || remove.getDocument() == null) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= remove.getDocument().attributes.size()) {
                break;
            }
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = remove.getDocument().attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                tLRPC$DocumentAttribute.waveform = bArr;
                tLRPC$DocumentAttribute.flags |= 4;
                break;
            }
            i++;
        }
        TLRPC$TL_messages_messages tLRPC$TL_messages_messages = new TLRPC$TL_messages_messages();
        tLRPC$TL_messages_messages.messages.add(remove.messageOwner);
        MessagesStorage.getInstance(remove.currentAccount).putMessages((TLRPC$messages_Messages) tLRPC$TL_messages_messages, remove.getDialogId(), -1, 0, false, messageObject.scheduled, 0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(remove);
        NotificationCenter.getInstance(remove.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(remove.getDialogId()), arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecordingInternal(final int i, final boolean z, final int i2) {
        if (i != 0) {
            final TLRPC$TL_document tLRPC$TL_document = this.recordingAudio;
            final File file = this.recordingAudioFile;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("stop recording internal filename " + this.recordingAudioFile.getPath());
            }
            this.fileEncodingQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.this.lambda$stopRecordingInternal$31(file, tLRPC$TL_document, i, z, i2);
                }
            });
        } else {
            AutoDeleteMediaTask.unlockFile(this.recordingAudioFile);
            File file2 = this.recordingAudioFile;
            if (file2 != null) {
                file2.delete();
            }
            requestAudioFocus(false);
        }
        try {
            AudioRecord audioRecord = this.audioRecorder;
            if (audioRecord != null) {
                audioRecord.release();
                this.audioRecorder = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.recordingAudio = null;
        this.recordingAudioFile = null;
        this.manualRecording = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecordingInternal$31(final File file, final TLRPC$TL_document tLRPC$TL_document, final int i, final boolean z, final int i2) {
        stopRecord();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("stop recording internal in queue " + file.exists() + " " + file.length());
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$stopRecordingInternal$30(file, tLRPC$TL_document, i, z, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecordingInternal$30(File file, TLRPC$TL_document tLRPC$TL_document, int i, boolean z, int i2) {
        boolean z2;
        char c;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("stop recording internal " + file.exists() + " " + file.length() + "  recordTimeCount " + this.recordTimeCount + " writedFrames" + this.writedFrame);
        }
        if (!file.exists() && BuildVars.DEBUG_VERSION) {
            FileLog.e(new RuntimeException("file not found :( recordTimeCount " + this.recordTimeCount + " writedFrames" + this.writedFrame));
        }
        tLRPC$TL_document.date = ConnectionsManager.getInstance(this.recordingCurrentAccount).getCurrentTime();
        tLRPC$TL_document.size = (int) file.length();
        TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
        tLRPC$TL_documentAttributeAudio.voice = true;
        short[] sArr = this.recordSamples;
        byte[] waveform2 = getWaveform2(sArr, sArr.length);
        tLRPC$TL_documentAttributeAudio.waveform = waveform2;
        if (waveform2 != null) {
            tLRPC$TL_documentAttributeAudio.flags |= 4;
        }
        long j = this.recordTimeCount;
        tLRPC$TL_documentAttributeAudio.duration = (int) (j / 1000);
        tLRPC$TL_document.attributes.add(tLRPC$TL_documentAttributeAudio);
        if (j > 700) {
            if (i == 1) {
                c = 1;
                SendMessagesHelper.getInstance(this.recordingCurrentAccount).sendMessage(tLRPC$TL_document, null, file.getAbsolutePath(), this.recordDialogId, this.recordReplyingMsg, this.recordReplyingTopMsg, null, null, null, null, z, i2, 0, null, null, false);
            } else {
                c = 1;
            }
            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.recordingCurrentAccount);
            int i3 = NotificationCenter.audioDidSent;
            Object[] objArr = new Object[3];
            z2 = false;
            objArr[0] = Integer.valueOf(this.recordingGuid);
            objArr[c] = i == 2 ? tLRPC$TL_document : null;
            objArr[2] = i == 2 ? file.getAbsolutePath() : null;
            notificationCenter.postNotificationName(i3, objArr);
        } else {
            z2 = false;
            NotificationCenter.getInstance(this.recordingCurrentAccount).postNotificationName(NotificationCenter.audioRecordTooShort, Integer.valueOf(this.recordingGuid), Boolean.FALSE, Integer.valueOf((int) j));
            AutoDeleteMediaTask.unlockFile(file);
            file.delete();
        }
        requestAudioFocus(z2);
    }

    public void stopRecording(final int i, final boolean z, final int i2) {
        Runnable runnable = this.recordStartRunnable;
        if (runnable != null) {
            this.recordQueue.cancelRunnable(runnable);
            this.recordStartRunnable = null;
        }
        this.recordQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$stopRecording$33(i, z, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecording$33(final int i, boolean z, int i2) {
        if (this.sendAfterDone == 3) {
            this.sendAfterDone = 0;
            stopRecordingInternal(i, z, i2);
            return;
        }
        AudioRecord audioRecord = this.audioRecorder;
        if (audioRecord == null) {
            return;
        }
        try {
            this.sendAfterDone = i;
            this.sendAfterDoneNotify = z;
            this.sendAfterDoneScheduleDate = i2;
            audioRecord.stop();
            setBluetoothScoOn(false);
        } catch (Exception e) {
            FileLog.e(e);
            if (this.recordingAudioFile != null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("delete voice file");
                }
                this.recordingAudioFile.delete();
            }
        }
        if (i == 0) {
            stopRecordingInternal(0, false, 0);
        }
        try {
            this.feedbackView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$stopRecording$32(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopRecording$32(int i) {
        NotificationCenter notificationCenter = NotificationCenter.getInstance(this.recordingCurrentAccount);
        int i2 = NotificationCenter.recordStopped;
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(this.recordingGuid);
        objArr[1] = Integer.valueOf(i == 2 ? 1 : 0);
        notificationCenter.postNotificationName(i2, objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MediaLoader implements NotificationCenter.NotificationCenterDelegate {
        private boolean cancelled;
        private int copiedFiles;
        private AccountInstance currentAccount;
        private boolean finished;
        private float finishedProgress;
        private boolean isMusic;
        private HashMap<String, MessageObject> loadingMessageObjects = new HashMap<>();
        private ArrayList<MessageObject> messageObjects;
        private MessagesStorage.IntCallback onFinishRunnable;
        private AlertDialog progressDialog;
        private CountDownLatch waitingForFile;

        public MediaLoader(Context context, AccountInstance accountInstance, ArrayList<MessageObject> arrayList, MessagesStorage.IntCallback intCallback) {
            this.currentAccount = accountInstance;
            this.messageObjects = arrayList;
            this.onFinishRunnable = intCallback;
            this.isMusic = arrayList.get(0).isMusic();
            this.currentAccount.getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
            this.currentAccount.getNotificationCenter().addObserver(this, NotificationCenter.fileLoadProgressChanged);
            this.currentAccount.getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
            AlertDialog alertDialog = new AlertDialog(context, 2);
            this.progressDialog = alertDialog;
            alertDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.setCancelable(true);
            this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    MediaController.MediaLoader.this.lambda$new$0(dialogInterface);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(DialogInterface dialogInterface) {
            this.cancelled = true;
        }

        public void start() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.MediaLoader.this.lambda$start$1();
                }
            }, 250L);
            new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.MediaLoader.this.lambda$start$2();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$1() {
            if (this.finished) {
                return;
            }
            this.progressDialog.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$start$2() {
            File externalStoragePublicDirectory;
            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    int size = this.messageObjects.size();
                    for (int i = 0; i < size; i++) {
                        MessageObject messageObject = this.messageObjects.get(i);
                        String str = messageObject.messageOwner.attachPath;
                        String documentName = messageObject.getDocumentName();
                        if (str != null && str.length() > 0 && !new File(str).exists()) {
                            str = null;
                        }
                        if (str == null || str.length() == 0) {
                            str = FileLoader.getInstance(this.currentAccount.getCurrentAccount()).getPathToMessage(messageObject.messageOwner).toString();
                        }
                        File file = new File(str);
                        if (!file.exists()) {
                            this.waitingForFile = new CountDownLatch(1);
                            addMessageToLoad(messageObject);
                            this.waitingForFile.await();
                        }
                        if (this.cancelled) {
                            break;
                        }
                        if (file.exists()) {
                            MediaController.saveFileInternal(this.isMusic ? 3 : 2, file, documentName);
                            this.copiedFiles++;
                        }
                    }
                } else {
                    if (this.isMusic) {
                        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                    } else {
                        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    }
                    externalStoragePublicDirectory.mkdir();
                    int size2 = this.messageObjects.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        MessageObject messageObject2 = this.messageObjects.get(i2);
                        String documentName2 = messageObject2.getDocumentName();
                        File file2 = new File(externalStoragePublicDirectory, documentName2);
                        if (file2.exists()) {
                            int lastIndexOf = documentName2.lastIndexOf(46);
                            int i3 = 0;
                            while (true) {
                                if (i3 >= 10) {
                                    break;
                                }
                                File file3 = new File(externalStoragePublicDirectory, lastIndexOf != -1 ? documentName2.substring(0, lastIndexOf) + "(" + (i3 + 1) + ")" + documentName2.substring(lastIndexOf) : documentName2 + "(" + (i3 + 1) + ")");
                                if (!file3.exists()) {
                                    file2 = file3;
                                    break;
                                } else {
                                    i3++;
                                    file2 = file3;
                                }
                            }
                        }
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }
                        String str2 = messageObject2.messageOwner.attachPath;
                        if (str2 != null && str2.length() > 0 && !new File(str2).exists()) {
                            str2 = null;
                        }
                        if (str2 == null || str2.length() == 0) {
                            str2 = FileLoader.getInstance(this.currentAccount.getCurrentAccount()).getPathToMessage(messageObject2.messageOwner).toString();
                        }
                        File file4 = new File(str2);
                        if (!file4.exists()) {
                            this.waitingForFile = new CountDownLatch(1);
                            addMessageToLoad(messageObject2);
                            this.waitingForFile.await();
                        }
                        if (file4.exists()) {
                            copyFile(file4, file2, messageObject2.getMimeType());
                            this.copiedFiles++;
                        }
                    }
                }
                checkIfFinished();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        private void checkIfFinished() {
            if (this.loadingMessageObjects.isEmpty()) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaController.MediaLoader.this.lambda$checkIfFinished$4();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkIfFinished$4() {
            try {
                if (this.progressDialog.isShowing()) {
                    this.progressDialog.dismiss();
                } else {
                    this.finished = true;
                }
                if (this.onFinishRunnable != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.MediaLoader.this.lambda$checkIfFinished$3();
                        }
                    });
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.currentAccount.getNotificationCenter().removeObserver(this, NotificationCenter.fileLoaded);
            this.currentAccount.getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            this.currentAccount.getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadFailed);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkIfFinished$3() {
            this.onFinishRunnable.run(this.copiedFiles);
        }

        private void addMessageToLoad(final MessageObject messageObject) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.MediaLoader.this.lambda$addMessageToLoad$5(messageObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addMessageToLoad$5(MessageObject messageObject) {
            TLRPC$Document document = messageObject.getDocument();
            if (document == null) {
                return;
            }
            this.loadingMessageObjects.put(FileLoader.getAttachFileName(document), messageObject);
            this.currentAccount.getFileLoader().loadFile(document, messageObject, 0, 0);
        }

        /* JADX WARN: Removed duplicated region for block: B:91:0x017c A[Catch: all -> 0x0180, TRY_ENTER, TRY_LEAVE, TryCatch #13 {all -> 0x0186, blocks: (B:96:0x0185, B:62:0x014b, B:67:0x0157, B:91:0x017c), top: B:116:0x0015 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean copyFile(File file, File file2, String str) {
            FileInputStream fileInputStream;
            Throwable th;
            FileInputStream fileInputStream2;
            Throwable th2;
            String str2;
            if (AndroidUtilities.isInternalUri(Uri.fromFile(file))) {
                return false;
            }
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                    file2.delete();
                    return false;
                }
            } catch (Exception e2) {
                e = e2;
                FileLog.e(e);
                file2.delete();
                return false;
            }
            try {
                try {
                    FileChannel channel = fileInputStream.getChannel();
                    try {
                        try {
                            try {
                                FileChannel channel2 = new FileOutputStream(file2).getChannel();
                                try {
                                    long size = channel.size();
                                    if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", new Class[0]).invoke(fileInputStream.getFD(), new Object[0])).intValue())) {
                                        if (this.progressDialog != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda1
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    MediaController.MediaLoader.this.lambda$copyFile$6();
                                                }
                                            });
                                        }
                                        if (channel2 != null) {
                                            try {
                                                channel2.close();
                                            } catch (Throwable th3) {
                                                th2 = th3;
                                                if (channel != null) {
                                                }
                                                throw th2;
                                            }
                                        }
                                        try {
                                            channel.close();
                                            fileInputStream.close();
                                            return false;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            fileInputStream2 = fileInputStream;
                                            fileInputStream2.close();
                                            throw th;
                                        }
                                    }
                                    long j = 0;
                                    long j2 = 0;
                                    while (j < size && !this.cancelled) {
                                        FileInputStream fileInputStream3 = fileInputStream;
                                        long j3 = j;
                                        try {
                                            channel2.transferFrom(channel, j, Math.min(4096L, size - j));
                                            j = j3 + 4096;
                                            if (j >= size || j2 <= SystemClock.elapsedRealtime() - 500) {
                                                long elapsedRealtime = SystemClock.elapsedRealtime();
                                                final int size2 = (int) (this.finishedProgress + (((100.0f / this.messageObjects.size()) * ((float) j3)) / ((float) size)));
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda6
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        MediaController.MediaLoader.this.lambda$copyFile$7(size2);
                                                    }
                                                });
                                                j2 = elapsedRealtime;
                                            }
                                            fileInputStream = fileInputStream3;
                                        } catch (Throwable th5) {
                                            th = th5;
                                            Throwable th6 = th;
                                            if (channel2 != null) {
                                                channel2.close();
                                            }
                                            throw th6;
                                        }
                                    }
                                    FileInputStream fileInputStream4 = fileInputStream;
                                    if (!this.cancelled) {
                                        if (this.isMusic) {
                                            AndroidUtilities.addMediaToGallery(file2);
                                        } else {
                                            DownloadManager downloadManager = (DownloadManager) ApplicationLoader.applicationContext.getSystemService("download");
                                            if (TextUtils.isEmpty(str)) {
                                                MimeTypeMap singleton = MimeTypeMap.getSingleton();
                                                String name = file2.getName();
                                                int lastIndexOf = name.lastIndexOf(46);
                                                if (lastIndexOf != -1) {
                                                    String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(name.substring(lastIndexOf + 1).toLowerCase());
                                                    if (TextUtils.isEmpty(mimeTypeFromExtension)) {
                                                        mimeTypeFromExtension = "text/plain";
                                                    }
                                                    str2 = mimeTypeFromExtension;
                                                } else {
                                                    str2 = "text/plain";
                                                }
                                            } else {
                                                str2 = str;
                                            }
                                            downloadManager.addCompletedDownload(file2.getName(), file2.getName(), false, str2, file2.getAbsolutePath(), file2.length(), true);
                                        }
                                        float size3 = this.finishedProgress + (100.0f / this.messageObjects.size());
                                        this.finishedProgress = size3;
                                        final int i = (int) size3;
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda8
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                MediaController.MediaLoader.this.lambda$copyFile$8(i);
                                            }
                                        });
                                        if (channel2 != null) {
                                            channel2.close();
                                        }
                                        channel.close();
                                        fileInputStream4.close();
                                        return true;
                                    }
                                    if (channel2 != null) {
                                        channel2.close();
                                    }
                                    channel.close();
                                    fileInputStream4.close();
                                    file2.delete();
                                    return false;
                                } catch (Throwable th7) {
                                    th = th7;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                th2 = th;
                                if (channel != null) {
                                    channel.close();
                                }
                                throw th2;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            th2 = th;
                            if (channel != null) {
                            }
                            throw th2;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                    }
                } catch (Throwable th11) {
                    th = th11;
                    fileInputStream2 = fileInputStream;
                    th = th;
                    fileInputStream2.close();
                    throw th;
                }
            } catch (Throwable th12) {
                th = th12;
                th = th;
                fileInputStream2.close();
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$copyFile$6() {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$copyFile$7(int i) {
            try {
                this.progressDialog.setProgress(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$copyFile$8(int i) {
            try {
                this.progressDialog.setProgress(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.fileLoaded || i == NotificationCenter.fileLoadFailed) {
                if (this.loadingMessageObjects.remove((String) objArr[0]) != null) {
                    this.waitingForFile.countDown();
                }
            } else if (i == NotificationCenter.fileLoadProgressChanged) {
                if (this.loadingMessageObjects.containsKey((String) objArr[0])) {
                    final int longValue = (int) (this.finishedProgress + (((((float) ((Long) objArr[1]).longValue()) / ((float) ((Long) objArr[2]).longValue())) / this.messageObjects.size()) * 100.0f));
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$MediaLoader$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.MediaLoader.this.lambda$didReceivedNotification$9(longValue);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$9(int i) {
            try {
                this.progressDialog.setProgress(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static void saveFilesFromMessages(Context context, AccountInstance accountInstance, ArrayList<MessageObject> arrayList, MessagesStorage.IntCallback intCallback) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        new MediaLoader(context, accountInstance, arrayList, intCallback).start();
    }

    public static void saveFile(String str, Context context, int i, String str2, String str3) {
        saveFile(str, context, i, str2, str3, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void saveFile(String str, Context context, final int i, final String str2, final String str3, final Runnable runnable) {
        final File file;
        final AlertDialog alertDialog;
        if (str == null || context == null) {
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            File file2 = new File(str);
            if (file2.exists() && !AndroidUtilities.isInternalUri(Uri.fromFile(file2))) {
                file = file2;
                if (file != null) {
                    return;
                }
                final boolean[] zArr = {false};
                if (file.exists()) {
                    final boolean[] zArr2 = new boolean[1];
                    if (i != 0) {
                        try {
                            final AlertDialog alertDialog2 = new AlertDialog(context, 2);
                            alertDialog2.setMessage(LocaleController.getString("Loading", R.string.Loading));
                            alertDialog2.setCanceledOnTouchOutside(false);
                            alertDialog2.setCancelable(true);
                            alertDialog2.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda1
                                @Override // android.content.DialogInterface.OnCancelListener
                                public final void onCancel(DialogInterface dialogInterface) {
                                    MediaController.lambda$saveFile$34(zArr, dialogInterface);
                                }
                            });
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda40
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MediaController.lambda$saveFile$35(zArr2, alertDialog2);
                                }
                            }, 250L);
                            alertDialog = alertDialog2;
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaController.lambda$saveFile$39(i, file, str2, alertDialog, zArr, str3, runnable, zArr2);
                            }
                        }).start();
                        return;
                    }
                    alertDialog = null;
                    new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaController.lambda$saveFile$39(i, file, str2, alertDialog, zArr, str3, runnable, zArr2);
                        }
                    }).start();
                    return;
                }
                return;
            }
        }
        file = null;
        if (file != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveFile$34(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveFile$35(boolean[] zArr, AlertDialog alertDialog) {
        if (zArr[0]) {
            return;
        }
        alertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:115:0x01de A[Catch: Exception -> 0x021b, TryCatch #11 {Exception -> 0x021b, blocks: (B:3:0x0008, B:5:0x000e, B:124:0x0217, B:9:0x001c, B:29:0x00d7, B:31:0x00dd, B:32:0x00e0, B:113:0x01da, B:115:0x01de, B:120:0x01e9, B:121:0x020c, B:112:0x01d5, B:11:0x003b, B:13:0x005a, B:15:0x0067, B:17:0x007a, B:23:0x008c, B:25:0x00c6, B:28:0x00d3, B:24:0x00af, B:14:0x0061), top: B:139:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0215 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0221  */
    /* JADX WARN: Removed duplicated region for block: B:153:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01b6 A[Catch: all -> 0x01ba, TRY_ENTER, TRY_LEAVE, TryCatch #15 {all -> 0x01c0, blocks: (B:96:0x01bf, B:72:0x0195, B:91:0x01b6), top: B:144:0x00ec }] */
    /* JADX WARN: Type inference failed for: r22v11 */
    /* JADX WARN: Type inference failed for: r22v3, types: [java.io.File] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$saveFile$39(int i, File file, String str, final AlertDialog alertDialog, boolean[] zArr, String str2, Runnable runnable, final boolean[] zArr2) {
        File externalStoragePublicDirectory;
        File file2;
        char c;
        boolean z;
        File file3;
        boolean z2;
        FileInputStream fileInputStream;
        Throwable th;
        Throwable th2;
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                z2 = saveFileInternal(i, file, null);
            } else {
                if (i == 0) {
                    File file4 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Telegram");
                    file4.mkdirs();
                    file2 = new File(file4, AndroidUtilities.generateFileName(0, FileLoader.getFileExtension(file)));
                } else if (i == 1) {
                    File file5 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Telegram");
                    file5.mkdirs();
                    file2 = new File(file5, AndroidUtilities.generateFileName(1, FileLoader.getFileExtension(file)));
                } else {
                    if (i == 2) {
                        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    } else {
                        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                    }
                    File file6 = new File(externalStoragePublicDirectory, "Telegram");
                    file6.mkdirs();
                    file2 = new File(file6, str);
                    if (file2.exists()) {
                        int lastIndexOf = str.lastIndexOf(46);
                        int i2 = 0;
                        while (true) {
                            if (i2 >= 10) {
                                break;
                            }
                            File file7 = new File(file6, lastIndexOf != -1 ? str.substring(0, lastIndexOf) + "(" + (i2 + 1) + ")" + str.substring(lastIndexOf) : str + "(" + (i2 + 1) + ")");
                            if (!file7.exists()) {
                                file2 = file7;
                                break;
                            } else {
                                i2++;
                                file2 = file7;
                            }
                        }
                    }
                }
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                long currentTimeMillis = System.currentTimeMillis() - 500;
                try {
                    try {
                        fileInputStream = new FileInputStream(file);
                    } catch (Exception e) {
                        e = e;
                        str = file2;
                        FileLog.e(e);
                        c = 0;
                        z = false;
                        file3 = str;
                        if (zArr[c]) {
                        }
                        if (z2) {
                        }
                        if (z2) {
                            AndroidUtilities.runOnUIThread(runnable);
                        }
                        if (alertDialog != null) {
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                    c = 0;
                    z = false;
                    file3 = str;
                    if (zArr[c]) {
                    }
                    if (z2) {
                    }
                    if (z2) {
                    }
                    if (alertDialog != null) {
                    }
                }
                try {
                    try {
                        FileChannel channel = fileInputStream.getChannel();
                        try {
                            try {
                                FileChannel channel2 = new FileOutputStream(file2).getChannel();
                                try {
                                    long size = channel.size();
                                    if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", new Class[0]).invoke(fileInputStream.getFD(), new Object[0])).intValue())) {
                                        if (alertDialog != null) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda37
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    MediaController.lambda$saveFile$36(AlertDialog.this);
                                                }
                                            });
                                        }
                                        if (channel2 != null) {
                                            try {
                                                channel2.close();
                                            } catch (Throwable th3) {
                                                th2 = th3;
                                                if (channel != null) {
                                                    channel.close();
                                                }
                                                throw th2;
                                            }
                                        }
                                        try {
                                            channel.close();
                                            fileInputStream.close();
                                            return;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            fileInputStream.close();
                                            throw th;
                                        }
                                    }
                                    long j = 0;
                                    for (char c2 = 0; j < size && !zArr[c2]; c2 = 0) {
                                        File file8 = file2;
                                        try {
                                            long j2 = j;
                                            long j3 = size;
                                            channel2.transferFrom(channel, j2, Math.min(4096L, size - j));
                                            if (alertDialog != null && currentTimeMillis <= System.currentTimeMillis() - 500) {
                                                currentTimeMillis = System.currentTimeMillis();
                                                final int i3 = (int) ((((float) j2) / ((float) j3)) * 100.0f);
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda38
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        MediaController.lambda$saveFile$37(AlertDialog.this, i3);
                                                    }
                                                });
                                            }
                                            j = 4096 + j2;
                                            file2 = file8;
                                            size = j3;
                                        } catch (Throwable th5) {
                                            th = th5;
                                            Throwable th6 = th;
                                            if (channel2 != null) {
                                                channel2.close();
                                            }
                                            throw th6;
                                        }
                                    }
                                    file3 = file2;
                                    if (channel2 != null) {
                                        channel2.close();
                                    }
                                    channel.close();
                                    fileInputStream.close();
                                    c = 0;
                                    z = true;
                                    if (zArr[c]) {
                                        file3.delete();
                                        z2 = false;
                                    } else {
                                        z2 = z;
                                    }
                                    if (z2) {
                                        if (i == 2) {
                                            ((DownloadManager) ApplicationLoader.applicationContext.getSystemService("download")).addCompletedDownload(file3.getName(), file3.getName(), false, str2, file3.getAbsolutePath(), file3.length(), true);
                                        } else {
                                            AndroidUtilities.addMediaToGallery(file3.getAbsoluteFile());
                                        }
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                th2 = th;
                                if (channel != null) {
                                }
                                throw th2;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            th2 = th;
                            if (channel != null) {
                            }
                            throw th2;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                        th = th;
                        fileInputStream.close();
                        throw th;
                    }
                } catch (Throwable th11) {
                    th = th11;
                    th = th;
                    fileInputStream.close();
                    throw th;
                }
            }
            if (z2 && runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        if (alertDialog != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.lambda$saveFile$38(AlertDialog.this, zArr2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveFile$36(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveFile$37(AlertDialog alertDialog, int i) {
        try {
            alertDialog.setProgress(i);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveFile$38(AlertDialog alertDialog, boolean[] zArr) {
        try {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            } else {
                zArr[0] = true;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean saveFileInternal(int i, File file, String str) {
        Uri contentUri;
        try {
            ContentValues contentValues = new ContentValues();
            String fileExtension = FileLoader.getFileExtension(file);
            String mimeTypeFromExtension = fileExtension != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension) : null;
            if ((i == 0 || i == 1) && mimeTypeFromExtension != null) {
                if (mimeTypeFromExtension.startsWith("image")) {
                    i = 0;
                }
                if (mimeTypeFromExtension.startsWith(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                    i = 1;
                }
            }
            if (i == 0) {
                if (str == null) {
                    str = AndroidUtilities.generateFileName(0, fileExtension);
                }
                contentUri = MediaStore.Images.Media.getContentUri("external_primary");
                File file2 = new File(Environment.DIRECTORY_PICTURES, "Telegram");
                contentValues.put("relative_path", file2 + File.separator);
                contentValues.put("_display_name", str);
                contentValues.put("mime_type", mimeTypeFromExtension);
            } else if (i == 1) {
                if (str == null) {
                    str = AndroidUtilities.generateFileName(1, fileExtension);
                }
                File file3 = new File(Environment.DIRECTORY_MOVIES, "Telegram");
                contentValues.put("relative_path", file3 + File.separator);
                contentUri = MediaStore.Video.Media.getContentUri("external_primary");
                contentValues.put("_display_name", str);
            } else if (i == 2) {
                if (str == null) {
                    str = file.getName();
                }
                File file4 = new File(Environment.DIRECTORY_DOWNLOADS, "Telegram");
                contentValues.put("relative_path", file4 + File.separator);
                contentUri = MediaStore.Downloads.getContentUri("external_primary");
                contentValues.put("_display_name", str);
            } else {
                if (str == null) {
                    str = file.getName();
                }
                File file5 = new File(Environment.DIRECTORY_MUSIC, "Telegram");
                contentValues.put("relative_path", file5 + File.separator);
                contentUri = MediaStore.Audio.Media.getContentUri("external_primary");
                contentValues.put("_display_name", str);
            }
            contentValues.put("mime_type", mimeTypeFromExtension);
            Uri insert = ApplicationLoader.applicationContext.getContentResolver().insert(contentUri, contentValues);
            if (insert != null) {
                FileInputStream fileInputStream = new FileInputStream(file);
                AndroidUtilities.copyFile(fileInputStream, ApplicationLoader.applicationContext.getContentResolver().openOutputStream(insert));
                fileInputStream.close();
            }
            return true;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x00c5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:65:0x00bf -> B:76:0x00c2). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getStickerExt(Uri uri) {
        FileInputStream fileInputStream;
        InputStream inputStream = null;
        try {
            fileInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
        } catch (Exception unused) {
            fileInputStream = null;
        } catch (Throwable th) {
            th = th;
            if (inputStream != null) {
            }
            throw th;
        }
        try {
            try {
                if (fileInputStream == null) {
                    try {
                        File file = new File(uri.getPath());
                        if (file.exists()) {
                            fileInputStream = new FileInputStream(file);
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    }
                }
                byte[] bArr = new byte[12];
                if (fileInputStream.read(bArr, 0, 12) == 12) {
                    if (bArr[0] == -119 && bArr[1] == 80 && bArr[2] == 78 && bArr[3] == 71 && bArr[4] == 13 && bArr[5] == 10 && bArr[6] == 26 && bArr[7] == 10) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                        return "png";
                    } else if (bArr[0] == 31 && bArr[1] == -117) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e3) {
                            FileLog.e(e3);
                        }
                        return "tgs";
                    } else {
                        String lowerCase = new String(bArr).toLowerCase();
                        if (lowerCase.startsWith("riff")) {
                            if (lowerCase.endsWith("webp")) {
                                try {
                                    fileInputStream.close();
                                } catch (Exception e4) {
                                    FileLog.e(e4);
                                }
                                return "webp";
                            }
                        }
                    }
                }
                fileInputStream.close();
            } catch (Throwable th2) {
                th = th2;
                inputStream = fileInputStream;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e5) {
                        FileLog.e(e5);
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
            FileLog.e(e6);
        }
        return null;
    }

    public static boolean isWebp(Uri uri) {
        InputStream inputStream = null;
        try {
            try {
                try {
                    inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                    byte[] bArr = new byte[12];
                    if (inputStream.read(bArr, 0, 12) == 12) {
                        String lowerCase = new String(bArr).toLowerCase();
                        if (lowerCase.startsWith("riff")) {
                            if (lowerCase.endsWith("webp")) {
                                try {
                                    inputStream.close();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                return true;
                            }
                        }
                    }
                    inputStream.close();
                } catch (Exception e2) {
                    FileLog.e(e2);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
            return false;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x003d -> B:32:0x0040). Please submit an issue!!! */
    public static boolean isGif(Uri uri) {
        InputStream inputStream = null;
        try {
            try {
                try {
                    inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                    byte[] bArr = new byte[3];
                    if (inputStream.read(bArr, 0, 3) == 3) {
                        if (new String(bArr).equalsIgnoreCase("gif")) {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            return true;
                        }
                    }
                    inputStream.close();
                } catch (Throwable th) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                FileLog.e(e3);
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception e4) {
            FileLog.e(e4);
        }
        return false;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) {
            return "";
        }
        try {
            if (uri.getScheme().equals("content")) {
                try {
                    Cursor query = ApplicationLoader.applicationContext.getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
                    r2 = query.moveToFirst() ? query.getString(query.getColumnIndex("_display_name")) : null;
                    query.close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            if (r2 == null) {
                String path = uri.getPath();
                int lastIndexOf = path.lastIndexOf(47);
                return lastIndexOf != -1 ? path.substring(lastIndexOf + 1) : path;
            }
            return r2;
        } catch (Exception e2) {
            FileLog.e(e2);
            return "";
        }
    }

    public static String copyFileToCache(Uri uri, String str) {
        return copyFileToCache(uri, str, -1L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x0135, code lost:
        r3 = r5.getAbsolutePath();
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0139, code lost:
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x013d, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x013e, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:138:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x018b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x01a6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x01b1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0180 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:172:0x00ab A[EDGE_INSN: B:172:0x00ab->B:27:0x00ab ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0168 A[LOOP:0: B:16:0x0048->B:96:0x0168, LOOP_END] */
    @SuppressLint({"DiscouragedPrivateApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String copyFileToCache(Uri uri, String str, long j) {
        Throwable th;
        File file;
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        FileOutputStream fileOutputStream2;
        FileOutputStream fileOutputStream3;
        String fixFileName;
        File sharingDirectory;
        int lastIndexOf;
        File file2;
        int i;
        String absolutePath;
        int i2 = 0;
        InputStream inputStream2 = null;
        try {
            try {
                fixFileName = FileLoader.fixFileName(getFileName(uri));
                if (fixFileName == null) {
                    int lastLocalId = SharedConfig.getLastLocalId();
                    SharedConfig.saveConfig();
                    fixFileName = String.format(Locale.US, "%d.%s", Integer.valueOf(lastLocalId), str);
                }
                sharingDirectory = AndroidUtilities.getSharingDirectory();
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                sharingDirectory.mkdirs();
                if (AndroidUtilities.isInternalUri(Uri.fromFile(sharingDirectory))) {
                    if (j > 0 && 0 > j) {
                        sharingDirectory.delete();
                    }
                    return null;
                }
                int i3 = 0;
                while (true) {
                    File sharingDirectory2 = AndroidUtilities.getSharingDirectory();
                    if (i3 == 0) {
                        file2 = new File(sharingDirectory2, fixFileName);
                    } else {
                        if (fixFileName.lastIndexOf(".") > 0) {
                            file = new File(sharingDirectory2, fixFileName.substring(0, lastIndexOf) + " (" + i3 + ")" + fixFileName.substring(lastIndexOf));
                            i3++;
                            if (file.exists()) {
                                break;
                            }
                        } else {
                            file2 = new File(sharingDirectory2, fixFileName + " (" + i3 + ")");
                        }
                    }
                    file = file2;
                    i3++;
                    if (file.exists()) {
                    }
                }
                inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                try {
                    if ((inputStream instanceof FileInputStream) && AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", new Class[0]).invoke(((FileInputStream) inputStream).getFD(), new Object[0])).intValue())) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                        if (j > 0 && 0 > j) {
                            file.delete();
                        }
                        return null;
                    }
                    fileOutputStream2 = new FileOutputStream(file);
                    try {
                        byte[] bArr = new byte[20480];
                        i = 0;
                        while (true) {
                            try {
                                int read = inputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream2.write(bArr, 0, read);
                                i += read;
                                if (j > 0) {
                                    long j2 = i;
                                    if (j2 > j) {
                                        try {
                                            inputStream.close();
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                        try {
                                            fileOutputStream2.close();
                                        } catch (Exception e3) {
                                            FileLog.e(e3);
                                        }
                                        if (j > 0 && j2 > j) {
                                            file.delete();
                                        }
                                        return null;
                                    }
                                }
                            } catch (Exception e4) {
                                e = e4;
                                i2 = i;
                                FileLog.e(e);
                                if (inputStream != null) {
                                }
                                if (fileOutputStream2 != 0) {
                                }
                                if (j > 0) {
                                    file.delete();
                                }
                                return null;
                            } catch (Throwable th3) {
                                th = th3;
                                inputStream2 = inputStream;
                                i2 = i;
                                fileOutputStream3 = fileOutputStream2;
                                th = th;
                                fileOutputStream = fileOutputStream3;
                                if (inputStream2 != null) {
                                }
                                if (fileOutputStream != null) {
                                }
                                if (j > 0) {
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e5) {
                        e = e5;
                    }
                } catch (Exception e6) {
                    e = e6;
                    fileOutputStream2 = 0;
                } catch (Throwable th4) {
                    th = th4;
                    fileOutputStream3 = null;
                    inputStream2 = inputStream;
                    th = th;
                    fileOutputStream = fileOutputStream3;
                    if (inputStream2 != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    if (j > 0) {
                        file.delete();
                    }
                    throw th;
                }
                try {
                    fileOutputStream2.close();
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
                if (j > 0 && i > j) {
                    file.delete();
                }
                return absolutePath;
                if (j > 0) {
                    file.delete();
                }
                return absolutePath;
            } catch (Exception e8) {
                e = e8;
                inputStream = null;
                fileOutputStream2 = inputStream;
                FileLog.e(e);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                }
                if (fileOutputStream2 != 0) {
                    try {
                        fileOutputStream2.close();
                    } catch (Exception e10) {
                        FileLog.e(e10);
                    }
                }
                if (j > 0 && i2 > j) {
                    file.delete();
                }
                return null;
            } catch (Throwable th5) {
                th = th5;
                fileOutputStream = null;
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Exception e11) {
                        FileLog.e(e11);
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e12) {
                        FileLog.e(e12);
                    }
                }
                if (j > 0 && i2 > j) {
                    file.delete();
                }
                throw th;
            }
        } catch (Exception e13) {
            e = e13;
            file = null;
            inputStream = null;
        } catch (Throwable th6) {
            th = th6;
            file = null;
            fileOutputStream = null;
        }
    }

    public static void loadGalleryPhotosAlbums(final int i) {
        Thread thread = new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.lambda$loadGalleryPhotosAlbums$41(i);
            }
        });
        thread.setPriority(1);
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x03c7, code lost:
        if (r0.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b4, code lost:
        if (r0.checkSelfPermission("android.permission.READ_MEDIA_AUDIO") == 0) goto L197;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x03bf  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x03f8  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x040f  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x056b A[LOOP:0: B:275:0x0565->B:277:0x056b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:300:0x03ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0103 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0366 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0555 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:344:0x0547 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$loadGalleryPhotosAlbums$41(int i) {
        SparseArray sparseArray;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        ArrayList arrayList;
        SparseArray sparseArray2;
        String str11;
        String str12;
        Cursor cursor;
        Object obj;
        Object obj2;
        Object obj3;
        Exception exc;
        Exception exc2;
        int i2;
        int i3;
        AlbumEntry albumEntry;
        int i4;
        int i5;
        int i6;
        AlbumEntry albumEntry2;
        Context context;
        int i7;
        String str13;
        int i8;
        int i9;
        int i10;
        AlbumEntry albumEntry3;
        AlbumEntry albumEntry4;
        int i11;
        int i12;
        AlbumEntry albumEntry5;
        AlbumEntry albumEntry6;
        int i13;
        AlbumEntry albumEntry7;
        StringBuilder sb;
        String str14 = "_size";
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = arrayList2;
        SparseArray sparseArray3 = new SparseArray();
        SparseArray sparseArray4 = new SparseArray();
        AlbumEntry albumEntry8 = null;
        try {
            sb = new StringBuilder();
            sparseArray = sparseArray4;
        } catch (Exception e) {
            e = e;
            sparseArray = sparseArray4;
        }
        try {
            try {
                sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
                sb.append("/Camera/");
                str = sb.toString();
            } catch (Exception e2) {
                e = e2;
                FileLog.e(e);
                str = null;
                str2 = str;
                context = ApplicationLoader.applicationContext;
                i7 = Build.VERSION.SDK_INT;
                str13 = "AllMedia";
                if (i7 >= 23) {
                }
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] strArr = projectionPhotos;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(i7 <= 28 ? "date_modified" : "datetaken");
                sb2.append(" DESC");
                cursor = MediaStore.Images.Media.query(contentResolver, uri, strArr, null, null, sb2.toString());
                if (cursor == null) {
                }
            }
        } catch (Throwable th) {
            th = th;
            str3 = "_size";
            str4 = "height";
            str5 = "width";
            str6 = "_data";
            str7 = "bucket_display_name";
            str8 = "bucket_id";
            str9 = "_id";
            str10 = " DESC";
            arrayList = arrayList4;
            sparseArray2 = sparseArray3;
            str11 = str2;
            str12 = str13;
            cursor = null;
            obj = null;
            Object obj4 = obj;
            obj2 = obj4;
            obj3 = obj4;
            try {
                FileLog.e(th);
                if (cursor != null) {
                }
                AlbumEntry albumEntry9 = obj;
                try {
                    Context context2 = ApplicationLoader.applicationContext;
                    i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 23) {
                    }
                    ContentResolver contentResolver2 = ApplicationLoader.applicationContext.getContentResolver();
                    Uri uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] strArr2 = projectionVideo;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(i3 > 28 ? "date_modified" : "datetaken");
                    sb3.append(str10);
                    cursor = MediaStore.Images.Media.query(contentResolver2, uri2, strArr2, null, null, sb3.toString());
                    if (cursor != null) {
                    }
                    if (cursor != null) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                AlbumEntry albumEntry10 = albumEntry8;
                AlbumEntry albumEntry11 = obj3;
                Integer num = obj2;
                while (i2 < arrayList.size()) {
                }
                broadcastNewPhotos(i, arrayList, arrayList3, num, albumEntry11, albumEntry9, albumEntry10, 0);
            } finally {
            }
        }
        str2 = str;
        try {
            context = ApplicationLoader.applicationContext;
            i7 = Build.VERSION.SDK_INT;
            str13 = "AllMedia";
        } catch (Throwable th3) {
            th = th3;
            str3 = "_size";
            str4 = "height";
            str5 = "width";
            str6 = "_data";
            str7 = "bucket_display_name";
            str8 = "bucket_id";
            str9 = "_id";
            str10 = " DESC";
            arrayList = arrayList4;
            sparseArray2 = sparseArray3;
            str11 = str2;
            str12 = "AllMedia";
        }
        if (i7 >= 23) {
            if (i7 < 33) {
                try {
                    if (context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                    }
                } catch (Throwable th4) {
                    th = th4;
                    str3 = "_size";
                    str4 = "height";
                    str5 = "width";
                    str6 = "_data";
                    str7 = "bucket_display_name";
                    str8 = "bucket_id";
                    str9 = "_id";
                    str10 = " DESC";
                    arrayList = arrayList4;
                    sparseArray2 = sparseArray3;
                    cursor = null;
                    obj = null;
                    obj3 = null;
                    obj2 = null;
                    str11 = str2;
                    str12 = str13;
                    obj3 = obj3;
                    FileLog.e(th);
                    if (cursor != null) {
                    }
                    AlbumEntry albumEntry92 = obj;
                    Context context22 = ApplicationLoader.applicationContext;
                    i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 23) {
                    }
                    ContentResolver contentResolver22 = ApplicationLoader.applicationContext.getContentResolver();
                    Uri uri22 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] strArr22 = projectionVideo;
                    StringBuilder sb32 = new StringBuilder();
                    sb32.append(i3 > 28 ? "date_modified" : "datetaken");
                    sb32.append(str10);
                    cursor = MediaStore.Images.Media.query(contentResolver22, uri22, strArr22, null, null, sb32.toString());
                    if (cursor != null) {
                    }
                    if (cursor != null) {
                    }
                    AlbumEntry albumEntry102 = albumEntry8;
                    AlbumEntry albumEntry112 = obj3;
                    Integer num2 = obj2;
                    while (i2 < arrayList.size()) {
                    }
                    broadcastNewPhotos(i, arrayList, arrayList3, num2, albumEntry112, albumEntry92, albumEntry102, 0);
                }
            }
            if (i7 >= 33) {
                if (context.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") != 0 && context.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") != 0) {
                }
            }
            str3 = "_size";
            str4 = "height";
            str5 = "width";
            str6 = "_data";
            str7 = "bucket_display_name";
            str8 = "bucket_id";
            str9 = "_id";
            str10 = " DESC";
            arrayList = arrayList4;
            sparseArray2 = sparseArray3;
            cursor = null;
            obj = null;
            obj3 = null;
            obj2 = null;
            str11 = str2;
            str12 = str13;
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e3) {
                    exc = e3;
                    FileLog.e(exc);
                    AlbumEntry albumEntry922 = obj;
                    Context context222 = ApplicationLoader.applicationContext;
                    i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 23) {
                    }
                    ContentResolver contentResolver222 = ApplicationLoader.applicationContext.getContentResolver();
                    Uri uri222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] strArr222 = projectionVideo;
                    StringBuilder sb322 = new StringBuilder();
                    sb322.append(i3 > 28 ? "date_modified" : "datetaken");
                    sb322.append(str10);
                    cursor = MediaStore.Images.Media.query(contentResolver222, uri222, strArr222, null, null, sb322.toString());
                    if (cursor != null) {
                    }
                    if (cursor != null) {
                    }
                    AlbumEntry albumEntry1022 = albumEntry8;
                    AlbumEntry albumEntry1122 = obj3;
                    Integer num22 = obj2;
                    while (i2 < arrayList.size()) {
                    }
                    broadcastNewPhotos(i, arrayList, arrayList3, num22, albumEntry1122, albumEntry922, albumEntry1022, 0);
                }
            }
            AlbumEntry albumEntry9222 = obj;
            Context context2222 = ApplicationLoader.applicationContext;
            i3 = Build.VERSION.SDK_INT;
            if (i3 >= 23) {
                if (i3 >= 33) {
                }
                if (i3 < 33) {
                    if (context2222.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") != 0) {
                        if (context2222.checkSelfPermission("android.permission.READ_MEDIA_VIDEO") != 0) {
                            if (context2222.checkSelfPermission("android.permission.READ_MEDIA_AUDIO") != 0) {
                            }
                        }
                    }
                }
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e4) {
                        exc2 = e4;
                        FileLog.e(exc2);
                        AlbumEntry albumEntry10222 = albumEntry8;
                        AlbumEntry albumEntry11222 = obj3;
                        Integer num222 = obj2;
                        while (i2 < arrayList.size()) {
                        }
                        broadcastNewPhotos(i, arrayList, arrayList3, num222, albumEntry11222, albumEntry9222, albumEntry10222, 0);
                    }
                }
                AlbumEntry albumEntry102222 = albumEntry8;
                AlbumEntry albumEntry112222 = obj3;
                Integer num2222 = obj2;
                for (i2 = 0; i2 < arrayList.size(); i2++) {
                    Collections.sort(((AlbumEntry) arrayList.get(i2)).photos, MediaController$$ExternalSyntheticLambda41.INSTANCE);
                }
                broadcastNewPhotos(i, arrayList, arrayList3, num2222, albumEntry112222, albumEntry9222, albumEntry102222, 0);
            }
            ContentResolver contentResolver2222 = ApplicationLoader.applicationContext.getContentResolver();
            Uri uri2222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] strArr2222 = projectionVideo;
            StringBuilder sb3222 = new StringBuilder();
            sb3222.append(i3 > 28 ? "date_modified" : "datetaken");
            sb3222.append(str10);
            cursor = MediaStore.Images.Media.query(contentResolver2222, uri2222, strArr2222, null, null, sb3222.toString());
            if (cursor != null) {
                try {
                    int columnIndex = cursor.getColumnIndex(str9);
                    int columnIndex2 = cursor.getColumnIndex(str8);
                    int columnIndex3 = cursor.getColumnIndex(str7);
                    int columnIndex4 = cursor.getColumnIndex(str6);
                    int columnIndex5 = cursor.getColumnIndex(i3 > 28 ? "date_modified" : "datetaken");
                    int columnIndex6 = cursor.getColumnIndex("duration");
                    int columnIndex7 = cursor.getColumnIndex(str5);
                    int columnIndex8 = cursor.getColumnIndex(str4);
                    int columnIndex9 = cursor.getColumnIndex(str3);
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(columnIndex4);
                        if (!TextUtils.isEmpty(string)) {
                            int i14 = cursor.getInt(columnIndex);
                            int i15 = columnIndex;
                            int i16 = cursor.getInt(columnIndex2);
                            int i17 = columnIndex2;
                            String string2 = cursor.getString(columnIndex3);
                            Cursor cursor2 = cursor;
                            try {
                                int i18 = columnIndex4;
                                int i19 = columnIndex6;
                                PhotoEntry photoEntry = new PhotoEntry(i16, i14, cursor.getLong(columnIndex5), string, (int) (cursor.getLong(columnIndex6) / 1000), true, cursor.getInt(columnIndex7), cursor.getInt(columnIndex8), cursor.getLong(columnIndex9));
                                if (albumEntry8 == null) {
                                    try {
                                        i4 = columnIndex5;
                                        albumEntry = new AlbumEntry(0, LocaleController.getString("AllVideos", R.string.AllVideos), photoEntry);
                                        i5 = 1;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        cursor = cursor2;
                                        try {
                                            FileLog.e(th);
                                            if (cursor != null) {
                                            }
                                            AlbumEntry albumEntry1022222 = albumEntry8;
                                            AlbumEntry albumEntry1122222 = obj3;
                                            Integer num22222 = obj2;
                                            while (i2 < arrayList.size()) {
                                            }
                                            broadcastNewPhotos(i, arrayList, arrayList3, num22222, albumEntry1122222, albumEntry9222, albumEntry1022222, 0);
                                        } finally {
                                        }
                                    }
                                    try {
                                        albumEntry.videoOnly = true;
                                        if (obj3 == null) {
                                            i5 = 0;
                                        }
                                        if (albumEntry9222 != null) {
                                            i5++;
                                        }
                                        arrayList.add(i5, albumEntry);
                                    } catch (Throwable th6) {
                                        th = th6;
                                        albumEntry8 = albumEntry;
                                        cursor = cursor2;
                                        FileLog.e(th);
                                        if (cursor != null) {
                                        }
                                        AlbumEntry albumEntry10222222 = albumEntry8;
                                        AlbumEntry albumEntry11222222 = obj3;
                                        Integer num222222 = obj2;
                                        while (i2 < arrayList.size()) {
                                        }
                                        broadcastNewPhotos(i, arrayList, arrayList3, num222222, albumEntry11222222, albumEntry9222, albumEntry10222222, 0);
                                    }
                                } else {
                                    i4 = columnIndex5;
                                    albumEntry = albumEntry8;
                                }
                                if (obj3 == null) {
                                    i6 = columnIndex3;
                                    albumEntry2 = new AlbumEntry(0, LocaleController.getString(str12, R.string.AllMedia), photoEntry);
                                    try {
                                        arrayList.add(0, albumEntry2);
                                    } catch (Throwable th7) {
                                        th = th7;
                                        obj3 = albumEntry2;
                                        albumEntry8 = albumEntry;
                                        cursor = cursor2;
                                        FileLog.e(th);
                                        if (cursor != null) {
                                        }
                                        AlbumEntry albumEntry102222222 = albumEntry8;
                                        AlbumEntry albumEntry112222222 = obj3;
                                        Integer num2222222 = obj2;
                                        while (i2 < arrayList.size()) {
                                        }
                                        broadcastNewPhotos(i, arrayList, arrayList3, num2222222, albumEntry112222222, albumEntry9222, albumEntry102222222, 0);
                                    }
                                } else {
                                    i6 = columnIndex3;
                                    albumEntry2 = obj3;
                                }
                                try {
                                    albumEntry.addPhoto(photoEntry);
                                    albumEntry2.addPhoto(photoEntry);
                                    AlbumEntry albumEntry12 = (AlbumEntry) sparseArray2.get(i16);
                                    if (albumEntry12 == null) {
                                        albumEntry12 = new AlbumEntry(i16, string2, photoEntry);
                                        sparseArray2.put(i16, albumEntry12);
                                        if (obj2 == null && str11 != null && string != null && string.startsWith(str11)) {
                                            try {
                                                arrayList.add(0, albumEntry12);
                                                obj2 = Integer.valueOf(i16);
                                            } catch (Throwable th8) {
                                                th = th8;
                                                obj3 = albumEntry2;
                                                albumEntry8 = albumEntry;
                                                cursor = cursor2;
                                                FileLog.e(th);
                                                if (cursor != null) {
                                                    try {
                                                        cursor.close();
                                                    } catch (Exception e5) {
                                                        exc2 = e5;
                                                        FileLog.e(exc2);
                                                        AlbumEntry albumEntry1022222222 = albumEntry8;
                                                        AlbumEntry albumEntry1122222222 = obj3;
                                                        Integer num22222222 = obj2;
                                                        while (i2 < arrayList.size()) {
                                                        }
                                                        broadcastNewPhotos(i, arrayList, arrayList3, num22222222, albumEntry1122222222, albumEntry9222, albumEntry1022222222, 0);
                                                    }
                                                }
                                                AlbumEntry albumEntry10222222222 = albumEntry8;
                                                AlbumEntry albumEntry11222222222 = obj3;
                                                Integer num222222222 = obj2;
                                                while (i2 < arrayList.size()) {
                                                }
                                                broadcastNewPhotos(i, arrayList, arrayList3, num222222222, albumEntry11222222222, albumEntry9222, albumEntry10222222222, 0);
                                            }
                                        } else {
                                            arrayList.add(albumEntry12);
                                        }
                                    }
                                    albumEntry12.addPhoto(photoEntry);
                                    obj3 = albumEntry2;
                                    albumEntry8 = albumEntry;
                                    columnIndex = i15;
                                    columnIndex2 = i17;
                                    columnIndex5 = i4;
                                    columnIndex3 = i6;
                                    cursor = cursor2;
                                    columnIndex4 = i18;
                                    columnIndex6 = i19;
                                } catch (Throwable th9) {
                                    th = th9;
                                }
                            } catch (Throwable th10) {
                                th = th10;
                            }
                        }
                    }
                } catch (Throwable th11) {
                    th = th11;
                }
            }
            if (cursor != null) {
            }
            AlbumEntry albumEntry102222222222 = albumEntry8;
            AlbumEntry albumEntry112222222222 = obj3;
            Integer num2222222222 = obj2;
            while (i2 < arrayList.size()) {
            }
            broadcastNewPhotos(i, arrayList, arrayList3, num2222222222, albumEntry112222222222, albumEntry9222, albumEntry102222222222, 0);
        }
        ContentResolver contentResolver3 = context.getContentResolver();
        Uri uri3 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] strArr3 = projectionPhotos;
        StringBuilder sb22 = new StringBuilder();
        sb22.append(i7 <= 28 ? "date_modified" : "datetaken");
        sb22.append(" DESC");
        cursor = MediaStore.Images.Media.query(contentResolver3, uri3, strArr3, null, null, sb22.toString());
        if (cursor == null) {
            try {
                int columnIndex10 = cursor.getColumnIndex("_id");
                str9 = "_id";
                try {
                    int columnIndex11 = cursor.getColumnIndex("bucket_id");
                    str8 = "bucket_id";
                    try {
                        int columnIndex12 = cursor.getColumnIndex("bucket_display_name");
                        str7 = "bucket_display_name";
                        try {
                            int columnIndex13 = cursor.getColumnIndex("_data");
                            str6 = "_data";
                            try {
                                int columnIndex14 = cursor.getColumnIndex(i7 > 28 ? "date_modified" : "datetaken");
                                int columnIndex15 = cursor.getColumnIndex("orientation");
                                str10 = " DESC";
                                try {
                                    int columnIndex16 = cursor.getColumnIndex("width");
                                    str5 = "width";
                                    try {
                                        int columnIndex17 = cursor.getColumnIndex("height");
                                        str4 = "height";
                                        try {
                                            int columnIndex18 = cursor.getColumnIndex("_size");
                                            obj = null;
                                            obj3 = null;
                                            obj2 = null;
                                            Integer num3 = null;
                                            while (cursor.moveToNext()) {
                                                try {
                                                    str3 = str14;
                                                    try {
                                                        String string3 = cursor.getString(columnIndex13);
                                                        if (TextUtils.isEmpty(string3)) {
                                                            str14 = str3;
                                                        } else {
                                                            int i20 = cursor.getInt(columnIndex10);
                                                            int i21 = columnIndex10;
                                                            int i22 = cursor.getInt(columnIndex11);
                                                            int i23 = columnIndex13;
                                                            String string4 = cursor.getString(columnIndex12);
                                                            Cursor cursor3 = cursor;
                                                            try {
                                                                PhotoEntry photoEntry2 = new PhotoEntry(i22, i20, cursor.getLong(columnIndex14), string3, cursor.getInt(columnIndex15), false, cursor.getInt(columnIndex16), cursor.getInt(columnIndex17), cursor.getLong(columnIndex18));
                                                                if (obj == null) {
                                                                    i8 = columnIndex18;
                                                                    i9 = columnIndex17;
                                                                    i10 = columnIndex14;
                                                                    albumEntry3 = new AlbumEntry(0, LocaleController.getString("AllPhotos", R.string.AllPhotos), photoEntry2);
                                                                    try {
                                                                        arrayList3.add(0, albumEntry3);
                                                                    } catch (Throwable th12) {
                                                                        th = th12;
                                                                        obj = albumEntry3;
                                                                        arrayList = arrayList4;
                                                                        sparseArray2 = sparseArray3;
                                                                        str11 = str2;
                                                                        str12 = str13;
                                                                        cursor = cursor3;
                                                                        obj3 = obj3;
                                                                        FileLog.e(th);
                                                                        if (cursor != null) {
                                                                            try {
                                                                                cursor.close();
                                                                            } catch (Exception e6) {
                                                                                exc = e6;
                                                                                FileLog.e(exc);
                                                                                AlbumEntry albumEntry92222 = obj;
                                                                                Context context22222 = ApplicationLoader.applicationContext;
                                                                                i3 = Build.VERSION.SDK_INT;
                                                                                if (i3 >= 23) {
                                                                                }
                                                                                ContentResolver contentResolver22222 = ApplicationLoader.applicationContext.getContentResolver();
                                                                                Uri uri22222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                                String[] strArr22222 = projectionVideo;
                                                                                StringBuilder sb32222 = new StringBuilder();
                                                                                sb32222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                                                sb32222.append(str10);
                                                                                cursor = MediaStore.Images.Media.query(contentResolver22222, uri22222, strArr22222, null, null, sb32222.toString());
                                                                                if (cursor != null) {
                                                                                }
                                                                                if (cursor != null) {
                                                                                }
                                                                                AlbumEntry albumEntry1022222222222 = albumEntry8;
                                                                                AlbumEntry albumEntry1122222222222 = obj3;
                                                                                Integer num22222222222 = obj2;
                                                                                while (i2 < arrayList.size()) {
                                                                                }
                                                                                broadcastNewPhotos(i, arrayList, arrayList3, num22222222222, albumEntry1122222222222, albumEntry92222, albumEntry1022222222222, 0);
                                                                            }
                                                                        }
                                                                        AlbumEntry albumEntry922222 = obj;
                                                                        Context context222222 = ApplicationLoader.applicationContext;
                                                                        i3 = Build.VERSION.SDK_INT;
                                                                        if (i3 >= 23) {
                                                                        }
                                                                        ContentResolver contentResolver222222 = ApplicationLoader.applicationContext.getContentResolver();
                                                                        Uri uri222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                        String[] strArr222222 = projectionVideo;
                                                                        StringBuilder sb322222 = new StringBuilder();
                                                                        sb322222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                                        sb322222.append(str10);
                                                                        cursor = MediaStore.Images.Media.query(contentResolver222222, uri222222, strArr222222, null, null, sb322222.toString());
                                                                        if (cursor != null) {
                                                                        }
                                                                        if (cursor != null) {
                                                                        }
                                                                        AlbumEntry albumEntry10222222222222 = albumEntry8;
                                                                        AlbumEntry albumEntry11222222222222 = obj3;
                                                                        Integer num222222222222 = obj2;
                                                                        while (i2 < arrayList.size()) {
                                                                        }
                                                                        broadcastNewPhotos(i, arrayList, arrayList3, num222222222222, albumEntry11222222222222, albumEntry922222, albumEntry10222222222222, 0);
                                                                    }
                                                                } else {
                                                                    i8 = columnIndex18;
                                                                    i9 = columnIndex17;
                                                                    i10 = columnIndex14;
                                                                    albumEntry3 = obj;
                                                                }
                                                                if (obj3 == null) {
                                                                    try {
                                                                        i11 = columnIndex12;
                                                                        str12 = str13;
                                                                        try {
                                                                            i12 = columnIndex11;
                                                                            albumEntry4 = new AlbumEntry(0, LocaleController.getString(str12, R.string.AllMedia), photoEntry2);
                                                                            arrayList = arrayList4;
                                                                            try {
                                                                                arrayList.add(0, albumEntry4);
                                                                            } catch (Throwable th13) {
                                                                                th = th13;
                                                                                obj = albumEntry3;
                                                                                obj3 = albumEntry4;
                                                                                sparseArray2 = sparseArray3;
                                                                                str11 = str2;
                                                                                cursor = cursor3;
                                                                                obj3 = obj3;
                                                                                FileLog.e(th);
                                                                                if (cursor != null) {
                                                                                }
                                                                                AlbumEntry albumEntry9222222 = obj;
                                                                                Context context2222222 = ApplicationLoader.applicationContext;
                                                                                i3 = Build.VERSION.SDK_INT;
                                                                                if (i3 >= 23) {
                                                                                }
                                                                                ContentResolver contentResolver2222222 = ApplicationLoader.applicationContext.getContentResolver();
                                                                                Uri uri2222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                                String[] strArr2222222 = projectionVideo;
                                                                                StringBuilder sb3222222 = new StringBuilder();
                                                                                sb3222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                                                sb3222222.append(str10);
                                                                                cursor = MediaStore.Images.Media.query(contentResolver2222222, uri2222222, strArr2222222, null, null, sb3222222.toString());
                                                                                if (cursor != null) {
                                                                                }
                                                                                if (cursor != null) {
                                                                                }
                                                                                AlbumEntry albumEntry102222222222222 = albumEntry8;
                                                                                AlbumEntry albumEntry112222222222222 = obj3;
                                                                                Integer num2222222222222 = obj2;
                                                                                while (i2 < arrayList.size()) {
                                                                                }
                                                                                broadcastNewPhotos(i, arrayList, arrayList3, num2222222222222, albumEntry112222222222222, albumEntry9222222, albumEntry102222222222222, 0);
                                                                            }
                                                                        } catch (Throwable th14) {
                                                                            th = th14;
                                                                            arrayList = arrayList4;
                                                                            obj = albumEntry3;
                                                                            sparseArray2 = sparseArray3;
                                                                            str11 = str2;
                                                                            cursor = cursor3;
                                                                            obj3 = obj3;
                                                                            FileLog.e(th);
                                                                            if (cursor != null) {
                                                                            }
                                                                            AlbumEntry albumEntry92222222 = obj;
                                                                            Context context22222222 = ApplicationLoader.applicationContext;
                                                                            i3 = Build.VERSION.SDK_INT;
                                                                            if (i3 >= 23) {
                                                                            }
                                                                            ContentResolver contentResolver22222222 = ApplicationLoader.applicationContext.getContentResolver();
                                                                            Uri uri22222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                            String[] strArr22222222 = projectionVideo;
                                                                            StringBuilder sb32222222 = new StringBuilder();
                                                                            sb32222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                                            sb32222222.append(str10);
                                                                            cursor = MediaStore.Images.Media.query(contentResolver22222222, uri22222222, strArr22222222, null, null, sb32222222.toString());
                                                                            if (cursor != null) {
                                                                            }
                                                                            if (cursor != null) {
                                                                            }
                                                                            AlbumEntry albumEntry1022222222222222 = albumEntry8;
                                                                            AlbumEntry albumEntry1122222222222222 = obj3;
                                                                            Integer num22222222222222 = obj2;
                                                                            while (i2 < arrayList.size()) {
                                                                            }
                                                                            broadcastNewPhotos(i, arrayList, arrayList3, num22222222222222, albumEntry1122222222222222, albumEntry92222222, albumEntry1022222222222222, 0);
                                                                        }
                                                                    } catch (Throwable th15) {
                                                                        th = th15;
                                                                        arrayList = arrayList4;
                                                                        str12 = str13;
                                                                    }
                                                                } else {
                                                                    i11 = columnIndex12;
                                                                    arrayList = arrayList4;
                                                                    str12 = str13;
                                                                    i12 = columnIndex11;
                                                                    albumEntry4 = obj3;
                                                                }
                                                                try {
                                                                    albumEntry3.addPhoto(photoEntry2);
                                                                    albumEntry4.addPhoto(photoEntry2);
                                                                    sparseArray2 = sparseArray3;
                                                                    try {
                                                                        AlbumEntry albumEntry13 = (AlbumEntry) sparseArray2.get(i22);
                                                                        if (albumEntry13 == null) {
                                                                            albumEntry6 = albumEntry3;
                                                                            try {
                                                                                albumEntry7 = new AlbumEntry(i22, string4, photoEntry2);
                                                                                sparseArray2.put(i22, albumEntry7);
                                                                                if (obj2 != null || str2 == null || string3 == null) {
                                                                                    albumEntry5 = albumEntry4;
                                                                                    str11 = str2;
                                                                                } else {
                                                                                    albumEntry5 = albumEntry4;
                                                                                    str11 = str2;
                                                                                    try {
                                                                                        if (string3.startsWith(str11)) {
                                                                                            i13 = columnIndex16;
                                                                                            arrayList.add(0, albumEntry7);
                                                                                            obj2 = Integer.valueOf(i22);
                                                                                        }
                                                                                    } catch (Throwable th16) {
                                                                                        th = th16;
                                                                                        obj3 = albumEntry5;
                                                                                        cursor = cursor3;
                                                                                        obj = albumEntry6;
                                                                                        FileLog.e(th);
                                                                                        if (cursor != null) {
                                                                                        }
                                                                                        AlbumEntry albumEntry922222222 = obj;
                                                                                        Context context222222222 = ApplicationLoader.applicationContext;
                                                                                        i3 = Build.VERSION.SDK_INT;
                                                                                        if (i3 >= 23) {
                                                                                        }
                                                                                        ContentResolver contentResolver222222222 = ApplicationLoader.applicationContext.getContentResolver();
                                                                                        Uri uri222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                                        String[] strArr222222222 = projectionVideo;
                                                                                        StringBuilder sb322222222 = new StringBuilder();
                                                                                        sb322222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                                                        sb322222222.append(str10);
                                                                                        cursor = MediaStore.Images.Media.query(contentResolver222222222, uri222222222, strArr222222222, null, null, sb322222222.toString());
                                                                                        if (cursor != null) {
                                                                                        }
                                                                                        if (cursor != null) {
                                                                                        }
                                                                                        AlbumEntry albumEntry10222222222222222 = albumEntry8;
                                                                                        AlbumEntry albumEntry11222222222222222 = obj3;
                                                                                        Integer num222222222222222 = obj2;
                                                                                        while (i2 < arrayList.size()) {
                                                                                        }
                                                                                        broadcastNewPhotos(i, arrayList, arrayList3, num222222222222222, albumEntry11222222222222222, albumEntry922222222, albumEntry10222222222222222, 0);
                                                                                    }
                                                                                }
                                                                                i13 = columnIndex16;
                                                                                arrayList.add(albumEntry7);
                                                                            } catch (Throwable th17) {
                                                                                th = th17;
                                                                                albumEntry5 = albumEntry4;
                                                                                str11 = str2;
                                                                                obj3 = albumEntry5;
                                                                                cursor = cursor3;
                                                                                obj = albumEntry6;
                                                                                FileLog.e(th);
                                                                                if (cursor != null) {
                                                                                }
                                                                                AlbumEntry albumEntry9222222222 = obj;
                                                                                Context context2222222222 = ApplicationLoader.applicationContext;
                                                                                i3 = Build.VERSION.SDK_INT;
                                                                                if (i3 >= 23) {
                                                                                }
                                                                                ContentResolver contentResolver2222222222 = ApplicationLoader.applicationContext.getContentResolver();
                                                                                Uri uri2222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                                                String[] strArr2222222222 = projectionVideo;
                                                                                StringBuilder sb3222222222 = new StringBuilder();
                                                                                sb3222222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                                                sb3222222222.append(str10);
                                                                                cursor = MediaStore.Images.Media.query(contentResolver2222222222, uri2222222222, strArr2222222222, null, null, sb3222222222.toString());
                                                                                if (cursor != null) {
                                                                                }
                                                                                if (cursor != null) {
                                                                                }
                                                                                AlbumEntry albumEntry102222222222222222 = albumEntry8;
                                                                                AlbumEntry albumEntry112222222222222222 = obj3;
                                                                                Integer num2222222222222222 = obj2;
                                                                                while (i2 < arrayList.size()) {
                                                                                }
                                                                                broadcastNewPhotos(i, arrayList, arrayList3, num2222222222222222, albumEntry112222222222222222, albumEntry9222222222, albumEntry102222222222222222, 0);
                                                                            }
                                                                        } else {
                                                                            albumEntry6 = albumEntry3;
                                                                            albumEntry5 = albumEntry4;
                                                                            str11 = str2;
                                                                            i13 = columnIndex16;
                                                                            albumEntry7 = albumEntry13;
                                                                        }
                                                                        albumEntry7.addPhoto(photoEntry2);
                                                                        SparseArray sparseArray5 = sparseArray;
                                                                        AlbumEntry albumEntry14 = (AlbumEntry) sparseArray5.get(i22);
                                                                        if (albumEntry14 == null) {
                                                                            albumEntry14 = new AlbumEntry(i22, string4, photoEntry2);
                                                                            sparseArray5.put(i22, albumEntry14);
                                                                            if (num3 == null && str11 != null && string3 != null && string3.startsWith(str11)) {
                                                                                arrayList3.add(0, albumEntry14);
                                                                                num3 = Integer.valueOf(i22);
                                                                            } else {
                                                                                arrayList3.add(albumEntry14);
                                                                            }
                                                                        }
                                                                        albumEntry14.addPhoto(photoEntry2);
                                                                        sparseArray = sparseArray5;
                                                                        arrayList4 = arrayList;
                                                                        columnIndex16 = i13;
                                                                        obj3 = albumEntry5;
                                                                        str14 = str3;
                                                                        columnIndex18 = i8;
                                                                        columnIndex14 = i10;
                                                                        columnIndex10 = i21;
                                                                        columnIndex13 = i23;
                                                                        cursor = cursor3;
                                                                        str2 = str11;
                                                                        obj = albumEntry6;
                                                                        columnIndex17 = i9;
                                                                        sparseArray3 = sparseArray2;
                                                                        columnIndex11 = i12;
                                                                        str13 = str12;
                                                                        columnIndex12 = i11;
                                                                    } catch (Throwable th18) {
                                                                        th = th18;
                                                                        albumEntry6 = albumEntry3;
                                                                    }
                                                                } catch (Throwable th19) {
                                                                    th = th19;
                                                                    albumEntry5 = albumEntry4;
                                                                    sparseArray2 = sparseArray3;
                                                                    str11 = str2;
                                                                    albumEntry6 = albumEntry3;
                                                                }
                                                            } catch (Throwable th20) {
                                                                th = th20;
                                                            }
                                                        }
                                                    } catch (Throwable th21) {
                                                        th = th21;
                                                        arrayList = arrayList4;
                                                        sparseArray2 = sparseArray3;
                                                        str11 = str2;
                                                        str12 = str13;
                                                        obj3 = obj3;
                                                        FileLog.e(th);
                                                        if (cursor != null) {
                                                        }
                                                        AlbumEntry albumEntry92222222222 = obj;
                                                        Context context22222222222 = ApplicationLoader.applicationContext;
                                                        i3 = Build.VERSION.SDK_INT;
                                                        if (i3 >= 23) {
                                                        }
                                                        ContentResolver contentResolver22222222222 = ApplicationLoader.applicationContext.getContentResolver();
                                                        Uri uri22222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                                        String[] strArr22222222222 = projectionVideo;
                                                        StringBuilder sb32222222222 = new StringBuilder();
                                                        sb32222222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                                        sb32222222222.append(str10);
                                                        cursor = MediaStore.Images.Media.query(contentResolver22222222222, uri22222222222, strArr22222222222, null, null, sb32222222222.toString());
                                                        if (cursor != null) {
                                                        }
                                                        if (cursor != null) {
                                                        }
                                                        AlbumEntry albumEntry1022222222222222222 = albumEntry8;
                                                        AlbumEntry albumEntry1122222222222222222 = obj3;
                                                        Integer num22222222222222222 = obj2;
                                                        while (i2 < arrayList.size()) {
                                                        }
                                                        broadcastNewPhotos(i, arrayList, arrayList3, num22222222222222222, albumEntry1122222222222222222, albumEntry92222222222, albumEntry1022222222222222222, 0);
                                                    }
                                                } catch (Throwable th22) {
                                                    th = th22;
                                                    str3 = str14;
                                                }
                                            }
                                            str3 = str14;
                                            arrayList = arrayList4;
                                            sparseArray2 = sparseArray3;
                                            str11 = str2;
                                            str12 = str13;
                                            if (cursor != null) {
                                            }
                                        } catch (Throwable th23) {
                                            th = th23;
                                            str3 = "_size";
                                            arrayList = arrayList4;
                                            sparseArray2 = sparseArray3;
                                            str11 = str2;
                                            str12 = str13;
                                            obj = null;
                                            Object obj42 = obj;
                                            obj2 = obj42;
                                            obj3 = obj42;
                                            FileLog.e(th);
                                            if (cursor != null) {
                                            }
                                            AlbumEntry albumEntry922222222222 = obj;
                                            Context context222222222222 = ApplicationLoader.applicationContext;
                                            i3 = Build.VERSION.SDK_INT;
                                            if (i3 >= 23) {
                                            }
                                            ContentResolver contentResolver222222222222 = ApplicationLoader.applicationContext.getContentResolver();
                                            Uri uri222222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                            String[] strArr222222222222 = projectionVideo;
                                            StringBuilder sb322222222222 = new StringBuilder();
                                            sb322222222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                            sb322222222222.append(str10);
                                            cursor = MediaStore.Images.Media.query(contentResolver222222222222, uri222222222222, strArr222222222222, null, null, sb322222222222.toString());
                                            if (cursor != null) {
                                            }
                                            if (cursor != null) {
                                            }
                                            AlbumEntry albumEntry10222222222222222222 = albumEntry8;
                                            AlbumEntry albumEntry11222222222222222222 = obj3;
                                            Integer num222222222222222222 = obj2;
                                            while (i2 < arrayList.size()) {
                                            }
                                            broadcastNewPhotos(i, arrayList, arrayList3, num222222222222222222, albumEntry11222222222222222222, albumEntry922222222222, albumEntry10222222222222222222, 0);
                                        }
                                    } catch (Throwable th24) {
                                        th = th24;
                                        str3 = "_size";
                                        str4 = "height";
                                    }
                                } catch (Throwable th25) {
                                    th = th25;
                                    str3 = "_size";
                                    str4 = "height";
                                    str5 = "width";
                                }
                            } catch (Throwable th26) {
                                th = th26;
                                str3 = "_size";
                                str4 = "height";
                                str5 = "width";
                                str10 = " DESC";
                                arrayList = arrayList4;
                                sparseArray2 = sparseArray3;
                                str11 = str2;
                                str12 = str13;
                                obj = null;
                                Object obj422 = obj;
                                obj2 = obj422;
                                obj3 = obj422;
                                FileLog.e(th);
                                if (cursor != null) {
                                }
                                AlbumEntry albumEntry9222222222222 = obj;
                                Context context2222222222222 = ApplicationLoader.applicationContext;
                                i3 = Build.VERSION.SDK_INT;
                                if (i3 >= 23) {
                                }
                                ContentResolver contentResolver2222222222222 = ApplicationLoader.applicationContext.getContentResolver();
                                Uri uri2222222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                String[] strArr2222222222222 = projectionVideo;
                                StringBuilder sb3222222222222 = new StringBuilder();
                                sb3222222222222.append(i3 > 28 ? "date_modified" : "datetaken");
                                sb3222222222222.append(str10);
                                cursor = MediaStore.Images.Media.query(contentResolver2222222222222, uri2222222222222, strArr2222222222222, null, null, sb3222222222222.toString());
                                if (cursor != null) {
                                }
                                if (cursor != null) {
                                }
                                AlbumEntry albumEntry102222222222222222222 = albumEntry8;
                                AlbumEntry albumEntry112222222222222222222 = obj3;
                                Integer num2222222222222222222 = obj2;
                                while (i2 < arrayList.size()) {
                                }
                                broadcastNewPhotos(i, arrayList, arrayList3, num2222222222222222222, albumEntry112222222222222222222, albumEntry9222222222222, albumEntry102222222222222222222, 0);
                            }
                        } catch (Throwable th27) {
                            th = th27;
                            str3 = "_size";
                            str4 = "height";
                            str5 = "width";
                            str6 = "_data";
                        }
                    } catch (Throwable th28) {
                        th = th28;
                        str3 = "_size";
                        str4 = "height";
                        str5 = "width";
                        str6 = "_data";
                        str7 = "bucket_display_name";
                    }
                } catch (Throwable th29) {
                    th = th29;
                    str3 = "_size";
                    str4 = "height";
                    str5 = "width";
                    str6 = "_data";
                    str7 = "bucket_display_name";
                    str8 = "bucket_id";
                }
            } catch (Throwable th30) {
                th = th30;
                str3 = "_size";
                str4 = "height";
                str5 = "width";
                str6 = "_data";
                str7 = "bucket_display_name";
                str8 = "bucket_id";
                str9 = "_id";
            }
            AlbumEntry albumEntry92222222222222 = obj;
            Context context22222222222222 = ApplicationLoader.applicationContext;
            i3 = Build.VERSION.SDK_INT;
            if (i3 >= 23) {
            }
            ContentResolver contentResolver22222222222222 = ApplicationLoader.applicationContext.getContentResolver();
            Uri uri22222222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] strArr22222222222222 = projectionVideo;
            StringBuilder sb32222222222222 = new StringBuilder();
            sb32222222222222.append(i3 > 28 ? "date_modified" : "datetaken");
            sb32222222222222.append(str10);
            cursor = MediaStore.Images.Media.query(contentResolver22222222222222, uri22222222222222, strArr22222222222222, null, null, sb32222222222222.toString());
            if (cursor != null) {
            }
            if (cursor != null) {
            }
            AlbumEntry albumEntry1022222222222222222222 = albumEntry8;
            AlbumEntry albumEntry1122222222222222222222 = obj3;
            Integer num22222222222222222222 = obj2;
            while (i2 < arrayList.size()) {
            }
            broadcastNewPhotos(i, arrayList, arrayList3, num22222222222222222222, albumEntry1122222222222222222222, albumEntry92222222222222, albumEntry1022222222222222222222, 0);
        }
        str3 = "_size";
        str4 = "height";
        str5 = "width";
        str6 = "_data";
        str7 = "bucket_display_name";
        str8 = "bucket_id";
        str9 = "_id";
        str10 = " DESC";
        arrayList = arrayList4;
        sparseArray2 = sparseArray3;
        str11 = str2;
        str12 = str13;
        obj = null;
        obj3 = null;
        obj2 = null;
        if (cursor != null) {
        }
        AlbumEntry albumEntry922222222222222 = obj;
        Context context222222222222222 = ApplicationLoader.applicationContext;
        i3 = Build.VERSION.SDK_INT;
        if (i3 >= 23) {
        }
        ContentResolver contentResolver222222222222222 = ApplicationLoader.applicationContext.getContentResolver();
        Uri uri222222222222222 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] strArr222222222222222 = projectionVideo;
        StringBuilder sb322222222222222 = new StringBuilder();
        sb322222222222222.append(i3 > 28 ? "date_modified" : "datetaken");
        sb322222222222222.append(str10);
        cursor = MediaStore.Images.Media.query(contentResolver222222222222222, uri222222222222222, strArr222222222222222, null, null, sb322222222222222.toString());
        if (cursor != null) {
        }
        if (cursor != null) {
        }
        AlbumEntry albumEntry10222222222222222222222 = albumEntry8;
        AlbumEntry albumEntry11222222222222222222222 = obj3;
        Integer num222222222222222222222 = obj2;
        while (i2 < arrayList.size()) {
        }
        broadcastNewPhotos(i, arrayList, arrayList3, num222222222222222222222, albumEntry11222222222222222222222, albumEntry922222222222222, albumEntry10222222222222222222222, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadGalleryPhotosAlbums$40(PhotoEntry photoEntry, PhotoEntry photoEntry2) {
        long j = photoEntry.dateTaken;
        long j2 = photoEntry2.dateTaken;
        if (j < j2) {
            return 1;
        }
        return j > j2 ? -1 : 0;
    }

    private static void broadcastNewPhotos(final int i, final ArrayList<AlbumEntry> arrayList, final ArrayList<AlbumEntry> arrayList2, final Integer num, final AlbumEntry albumEntry, final AlbumEntry albumEntry2, final AlbumEntry albumEntry3, int i2) {
        Runnable runnable = broadcastPhotosRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.lambda$broadcastNewPhotos$42(i, arrayList, arrayList2, num, albumEntry, albumEntry2, albumEntry3);
            }
        };
        broadcastPhotosRunnable = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$broadcastNewPhotos$42(int i, ArrayList arrayList, ArrayList arrayList2, Integer num, AlbumEntry albumEntry, AlbumEntry albumEntry2, AlbumEntry albumEntry3) {
        if (PhotoViewer.getInstance().isVisible()) {
            broadcastNewPhotos(i, arrayList, arrayList2, num, albumEntry, albumEntry2, albumEntry3, 1000);
            return;
        }
        allMediaAlbums = arrayList;
        allPhotoAlbums = arrayList2;
        broadcastPhotosRunnable = null;
        allPhotosAlbumEntry = albumEntry2;
        allMediaAlbumEntry = albumEntry;
        allVideosAlbumEntry = albumEntry3;
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.albumsDidLoad, Integer.valueOf(i), arrayList, arrayList2, num);
    }

    public void scheduleVideoConvert(MessageObject messageObject) {
        scheduleVideoConvert(messageObject, false);
    }

    public boolean scheduleVideoConvert(MessageObject messageObject, boolean z) {
        if (messageObject == null || messageObject.videoEditedInfo == null) {
            return false;
        }
        if (!z || this.videoConvertQueue.isEmpty()) {
            if (z) {
                new File(messageObject.messageOwner.attachPath).delete();
            }
            this.videoConvertQueue.add(new VideoConvertMessage(messageObject, messageObject.videoEditedInfo));
            if (this.videoConvertQueue.size() == 1) {
                startVideoConvertFromQueue();
            }
            return true;
        }
        return false;
    }

    public void cancelVideoConvert(MessageObject messageObject) {
        if (messageObject == null || this.videoConvertQueue.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.videoConvertQueue.size(); i++) {
            VideoConvertMessage videoConvertMessage = this.videoConvertQueue.get(i);
            MessageObject messageObject2 = videoConvertMessage.messageObject;
            if (messageObject2.equals(messageObject) && messageObject2.currentAccount == messageObject.currentAccount) {
                if (i == 0) {
                    synchronized (this.videoConvertSync) {
                        videoConvertMessage.videoEditedInfo.canceled = true;
                    }
                    return;
                }
                this.videoConvertQueue.remove(i);
                return;
            }
        }
    }

    private boolean startVideoConvertFromQueue() {
        int i = 0;
        if (this.videoConvertQueue.isEmpty()) {
            return false;
        }
        VideoConvertMessage videoConvertMessage = this.videoConvertQueue.get(0);
        MessageObject messageObject = videoConvertMessage.messageObject;
        VideoEditedInfo videoEditedInfo = videoConvertMessage.videoEditedInfo;
        synchronized (this.videoConvertSync) {
            if (videoEditedInfo != null) {
                videoEditedInfo.canceled = false;
            }
        }
        Intent intent = new Intent(ApplicationLoader.applicationContext, VideoEncodingService.class);
        intent.putExtra("path", messageObject.messageOwner.attachPath);
        intent.putExtra("currentAccount", messageObject.currentAccount);
        if (messageObject.messageOwner.media.document != null) {
            while (true) {
                if (i >= messageObject.messageOwner.media.document.attributes.size()) {
                    break;
                } else if (messageObject.messageOwner.media.document.attributes.get(i) instanceof TLRPC$TL_documentAttributeAnimated) {
                    intent.putExtra("gif", true);
                    break;
                } else {
                    i++;
                }
            }
        }
        if (messageObject.getId() != 0) {
            try {
                ApplicationLoader.applicationContext.startService(intent);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        VideoConvertRunnable.runConversion(videoConvertMessage);
        return true;
    }

    @SuppressLint({"NewApi"})
    public static MediaCodecInfo selectCodec(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        MediaCodecInfo mediaCodecInfo = null;
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        String name = codecInfoAt.getName();
                        if (name != null && (!name.equals("OMX.SEC.avc.enc") || name.equals("OMX.SEC.AVC.Encoder"))) {
                            return codecInfoAt;
                        }
                        mediaCodecInfo = codecInfoAt;
                    }
                }
                continue;
            }
        }
        return mediaCodecInfo;
    }

    @SuppressLint({"NewApi"})
    public static int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        int i;
        MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int[] iArr = capabilitiesForType.colorFormats;
            if (i2 >= iArr.length) {
                return i3;
            }
            i = iArr[i2];
            if (isRecognizedFormat(i)) {
                if (!mediaCodecInfo.getName().equals("OMX.SEC.AVC.Encoder") || i != 19) {
                    break;
                }
                i3 = i;
            }
            i2++;
        }
        return i;
    }

    public static int findTrack(MediaExtractor mediaExtractor, boolean z) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            String string = mediaExtractor.getTrackFormat(i).getString("mime");
            if (z) {
                if (string.startsWith("audio/")) {
                    return i;
                }
            } else if (string.startsWith("video/")) {
                return i;
            }
        }
        return -5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void didWriteData(final VideoConvertMessage videoConvertMessage, final File file, final boolean z, final long j, final long j2, final boolean z2, final float f) {
        VideoEditedInfo videoEditedInfo = videoConvertMessage.videoEditedInfo;
        final boolean z3 = videoEditedInfo.videoConvertFirstWrite;
        if (z3) {
            videoEditedInfo.videoConvertFirstWrite = false;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaController$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                MediaController.this.lambda$didWriteData$43(z2, z, videoConvertMessage, file, f, j, z3, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didWriteData$43(boolean z, boolean z2, VideoConvertMessage videoConvertMessage, File file, float f, long j, boolean z3, long j2) {
        if (z || z2) {
            synchronized (this.videoConvertSync) {
                videoConvertMessage.videoEditedInfo.canceled = false;
            }
            this.videoConvertQueue.remove(videoConvertMessage);
            startVideoConvertFromQueue();
        }
        if (z) {
            NotificationCenter.getInstance(videoConvertMessage.currentAccount).postNotificationName(NotificationCenter.filePreparingFailed, videoConvertMessage.messageObject, file.toString(), Float.valueOf(f), Long.valueOf(j));
            return;
        }
        if (z3) {
            NotificationCenter.getInstance(videoConvertMessage.currentAccount).postNotificationName(NotificationCenter.filePreparingStarted, videoConvertMessage.messageObject, file.toString(), Float.valueOf(f), Long.valueOf(j));
        }
        NotificationCenter notificationCenter = NotificationCenter.getInstance(videoConvertMessage.currentAccount);
        int i = NotificationCenter.fileNewChunkAvailable;
        Object[] objArr = new Object[6];
        objArr[0] = videoConvertMessage.messageObject;
        objArr[1] = file.toString();
        objArr[2] = Long.valueOf(j2);
        objArr[3] = Long.valueOf(z2 ? file.length() : 0L);
        objArr[4] = Float.valueOf(f);
        objArr[5] = Long.valueOf(j);
        notificationCenter.postNotificationName(i, objArr);
    }

    public void pauseByRewind() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer != null) {
            videoPlayer.pause();
        }
    }

    public void resumeByRewind() {
        VideoPlayer videoPlayer = this.audioPlayer;
        if (videoPlayer == null || this.playingMessageObject == null || this.isPaused) {
            return;
        }
        if (videoPlayer.isBuffering()) {
            MessageObject messageObject = this.playingMessageObject;
            cleanupPlayer(false, false);
            playMessage(messageObject);
            return;
        }
        this.audioPlayer.play();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VideoConvertRunnable implements Runnable {
        private VideoConvertMessage convertMessage;

        private VideoConvertRunnable(VideoConvertMessage videoConvertMessage) {
            this.convertMessage = videoConvertMessage;
        }

        @Override // java.lang.Runnable
        public void run() {
            MediaController.getInstance().convertVideo(this.convertMessage);
        }

        public static void runConversion(final VideoConvertMessage videoConvertMessage) {
            new Thread(new Runnable() { // from class: org.telegram.messenger.MediaController$VideoConvertRunnable$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.VideoConvertRunnable.lambda$runConversion$0(MediaController.VideoConvertMessage.this);
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$runConversion$0(VideoConvertMessage videoConvertMessage) {
            try {
                Thread thread = new Thread(new VideoConvertRunnable(videoConvertMessage), "VideoConvertRunnable");
                thread.start();
                thread.join();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01fa A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean convertVideo(final VideoConvertMessage videoConvertMessage) {
        File file;
        int i;
        int i2;
        final VideoEditedInfo videoEditedInfo;
        long j;
        long j2;
        int i3;
        int i4;
        int i5;
        boolean z;
        MessageObject messageObject = videoConvertMessage.messageObject;
        VideoEditedInfo videoEditedInfo2 = videoConvertMessage.videoEditedInfo;
        if (messageObject == null || videoEditedInfo2 == null) {
            return false;
        }
        String str = videoEditedInfo2.originalPath;
        long j3 = videoEditedInfo2.startTime;
        long j4 = videoEditedInfo2.avatarStartTime;
        long j5 = videoEditedInfo2.endTime;
        int i6 = videoEditedInfo2.resultWidth;
        int i7 = videoEditedInfo2.resultHeight;
        int i8 = videoEditedInfo2.rotationValue;
        int i9 = videoEditedInfo2.originalWidth;
        int i10 = videoEditedInfo2.originalHeight;
        int i11 = videoEditedInfo2.framerate;
        int i12 = videoEditedInfo2.bitrate;
        int i13 = videoEditedInfo2.originalBitrate;
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(messageObject.getDialogId());
        File file2 = new File(messageObject.messageOwner.attachPath);
        if (file2.exists()) {
            file2.delete();
        }
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            file = file2;
            sb.append("begin convert ");
            sb.append(str);
            sb.append(" startTime = ");
            sb.append(j3);
            sb.append(" avatarStartTime = ");
            sb.append(j4);
            sb.append(" endTime ");
            sb.append(j5);
            sb.append(" rWidth = ");
            sb.append(i6);
            sb.append(" rHeight = ");
            sb.append(i7);
            sb.append(" rotation = ");
            sb.append(i8);
            sb.append(" oWidth = ");
            sb.append(i9);
            sb.append(" oHeight = ");
            sb.append(i10);
            sb.append(" framerate = ");
            sb.append(i11);
            sb.append(" bitrate = ");
            sb.append(i12);
            sb.append(" originalBitrate = ");
            sb.append(i13);
            FileLog.d(sb.toString());
        } else {
            file = file2;
        }
        if (str == null) {
            str = "";
        }
        if (j3 > 0 && j5 > 0) {
            j2 = j5 - j3;
        } else if (j5 <= 0) {
            if (j3 > 0) {
                i = i6;
                i2 = i7;
                videoEditedInfo = videoEditedInfo2;
                j = videoEditedInfo.originalDuration - j3;
            } else {
                i = i6;
                i2 = i7;
                videoEditedInfo = videoEditedInfo2;
                j = videoEditedInfo.originalDuration;
            }
            j2 = j;
            if (i11 != 0) {
                i11 = 25;
            } else if (i11 > 59) {
                i11 = 59;
            }
            if (i8 != 90 || i8 == 270) {
                i3 = i;
                i = i2;
            } else {
                i3 = i2;
            }
            if (!videoEditedInfo.shouldLimitFps || i11 <= 40) {
                i4 = i11;
            } else {
                i4 = i11;
                if (Math.min(i3, i) <= 480) {
                    i5 = 30;
                    boolean z2 = (j4 != -1 && videoEditedInfo.cropState == null && videoEditedInfo.mediaEntities == null && videoEditedInfo.paintPath == null && videoEditedInfo.filterState == null && i == i9 && i3 == i10 && i8 == 0 && !videoEditedInfo.roundVideo && j3 == -1) ? false : true;
                    SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("videoconvert", 0);
                    long currentTimeMillis = System.currentTimeMillis();
                    final File file3 = file;
                    VideoConvertorListener videoConvertorListener = new VideoConvertorListener() { // from class: org.telegram.messenger.MediaController.14
                        private long lastAvailableSize = 0;

                        @Override // org.telegram.messenger.MediaController.VideoConvertorListener
                        public boolean checkConversionCanceled() {
                            return videoEditedInfo.canceled;
                        }

                        @Override // org.telegram.messenger.MediaController.VideoConvertorListener
                        public void didWriteData(long j6, float f) {
                            if (videoEditedInfo.canceled) {
                                return;
                            }
                            if (j6 < 0) {
                                j6 = file3.length();
                            }
                            long j7 = j6;
                            if (videoEditedInfo.needUpdateProgress || this.lastAvailableSize != j7) {
                                this.lastAvailableSize = j7;
                                MediaController.this.didWriteData(videoConvertMessage, file3, false, 0L, j7, false, f);
                            }
                        }
                    };
                    videoEditedInfo.videoConvertFirstWrite = true;
                    MediaCodecVideoConvertor mediaCodecVideoConvertor = new MediaCodecVideoConvertor();
                    boolean convertVideo = mediaCodecVideoConvertor.convertVideo(str, file3, i8, isEncryptedDialog, i9, i10, i, i3, i5, i12, i13, j3, j5, j4, z2, j2, videoEditedInfo.filterState, videoEditedInfo.paintPath, videoEditedInfo.mediaEntities, videoEditedInfo.isPhoto, videoEditedInfo.cropState, videoEditedInfo.roundVideo, videoConvertorListener);
                    z = videoEditedInfo.canceled;
                    if (!z) {
                        synchronized (this.videoConvertSync) {
                            z = videoEditedInfo.canceled;
                        }
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("time=" + (System.currentTimeMillis() - currentTimeMillis) + " canceled=" + z);
                    }
                    sharedPreferences.edit().putBoolean("isPreviousOk", true).apply();
                    didWriteData(videoConvertMessage, file3, true, mediaCodecVideoConvertor.getLastFrameTimestamp(), file3.length(), !convertVideo || z, VOLUME_NORMAL);
                    return true;
                }
            }
            i5 = i4;
            if (j4 != -1) {
            }
            SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("videoconvert", 0);
            long currentTimeMillis2 = System.currentTimeMillis();
            final File file32 = file;
            VideoConvertorListener videoConvertorListener2 = new VideoConvertorListener() { // from class: org.telegram.messenger.MediaController.14
                private long lastAvailableSize = 0;

                @Override // org.telegram.messenger.MediaController.VideoConvertorListener
                public boolean checkConversionCanceled() {
                    return videoEditedInfo.canceled;
                }

                @Override // org.telegram.messenger.MediaController.VideoConvertorListener
                public void didWriteData(long j6, float f) {
                    if (videoEditedInfo.canceled) {
                        return;
                    }
                    if (j6 < 0) {
                        j6 = file32.length();
                    }
                    long j7 = j6;
                    if (videoEditedInfo.needUpdateProgress || this.lastAvailableSize != j7) {
                        this.lastAvailableSize = j7;
                        MediaController.this.didWriteData(videoConvertMessage, file32, false, 0L, j7, false, f);
                    }
                }
            };
            videoEditedInfo.videoConvertFirstWrite = true;
            MediaCodecVideoConvertor mediaCodecVideoConvertor2 = new MediaCodecVideoConvertor();
            boolean convertVideo2 = mediaCodecVideoConvertor2.convertVideo(str, file32, i8, isEncryptedDialog, i9, i10, i, i3, i5, i12, i13, j3, j5, j4, z2, j2, videoEditedInfo.filterState, videoEditedInfo.paintPath, videoEditedInfo.mediaEntities, videoEditedInfo.isPhoto, videoEditedInfo.cropState, videoEditedInfo.roundVideo, videoConvertorListener2);
            z = videoEditedInfo.canceled;
            if (!z) {
            }
            if (BuildVars.LOGS_ENABLED) {
            }
            sharedPreferences2.edit().putBoolean("isPreviousOk", true).apply();
            didWriteData(videoConvertMessage, file32, true, mediaCodecVideoConvertor2.getLastFrameTimestamp(), file32.length(), !convertVideo2 || z, VOLUME_NORMAL);
            return true;
        } else {
            j2 = j5;
        }
        i = i6;
        i2 = i7;
        videoEditedInfo = videoEditedInfo2;
        if (i11 != 0) {
        }
        if (i8 != 90) {
        }
        i3 = i;
        i = i2;
        if (videoEditedInfo.shouldLimitFps) {
        }
        i4 = i11;
        i5 = i4;
        if (j4 != -1) {
        }
        SharedPreferences sharedPreferences22 = ApplicationLoader.applicationContext.getSharedPreferences("videoconvert", 0);
        long currentTimeMillis22 = System.currentTimeMillis();
        final File file322 = file;
        VideoConvertorListener videoConvertorListener22 = new VideoConvertorListener() { // from class: org.telegram.messenger.MediaController.14
            private long lastAvailableSize = 0;

            @Override // org.telegram.messenger.MediaController.VideoConvertorListener
            public boolean checkConversionCanceled() {
                return videoEditedInfo.canceled;
            }

            @Override // org.telegram.messenger.MediaController.VideoConvertorListener
            public void didWriteData(long j6, float f) {
                if (videoEditedInfo.canceled) {
                    return;
                }
                if (j6 < 0) {
                    j6 = file322.length();
                }
                long j7 = j6;
                if (videoEditedInfo.needUpdateProgress || this.lastAvailableSize != j7) {
                    this.lastAvailableSize = j7;
                    MediaController.this.didWriteData(videoConvertMessage, file322, false, 0L, j7, false, f);
                }
            }
        };
        videoEditedInfo.videoConvertFirstWrite = true;
        MediaCodecVideoConvertor mediaCodecVideoConvertor22 = new MediaCodecVideoConvertor();
        boolean convertVideo22 = mediaCodecVideoConvertor22.convertVideo(str, file322, i8, isEncryptedDialog, i9, i10, i, i3, i5, i12, i13, j3, j5, j4, z2, j2, videoEditedInfo.filterState, videoEditedInfo.paintPath, videoEditedInfo.mediaEntities, videoEditedInfo.isPhoto, videoEditedInfo.cropState, videoEditedInfo.roundVideo, videoConvertorListener22);
        z = videoEditedInfo.canceled;
        if (!z) {
        }
        if (BuildVars.LOGS_ENABLED) {
        }
        sharedPreferences22.edit().putBoolean("isPreviousOk", true).apply();
        didWriteData(videoConvertMessage, file322, true, mediaCodecVideoConvertor22.getLastFrameTimestamp(), file322.length(), !convertVideo22 || z, VOLUME_NORMAL);
        return true;
    }

    public static int getVideoBitrate(String str) {
        int i;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            i = Integer.parseInt(mediaMetadataRetriever.extractMetadata(20));
        } catch (Exception e) {
            FileLog.e(e);
            i = 0;
        }
        try {
            mediaMetadataRetriever.release();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0056 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int makeVideoBitrate(int i, int i2, int i3, int i4, int i5) {
        int i6;
        float f;
        int min = Math.min(i4, i5);
        float f2 = VOLUME_NORMAL;
        if (min >= 1080) {
            i6 = VIDEO_BITRATE_1080;
        } else if (Math.min(i4, i5) < 720) {
            if (Math.min(i4, i5) >= 480) {
                i6 = VIDEO_BITRATE_480;
                f2 = 0.75f;
                f = 0.9f;
            } else {
                i6 = VIDEO_BITRATE_360;
                f2 = 0.6f;
                f = 0.7f;
            }
            int min2 = (int) (((int) (i3 / Math.min(i / i4, i2 / i5))) * f2);
            int videoBitrateWithFactor = (int) (getVideoBitrateWithFactor(f) / (921600.0f / (i5 * i4)));
            return i3 >= videoBitrateWithFactor ? min2 : min2 > i6 ? i6 : Math.max(min2, videoBitrateWithFactor);
        } else {
            i6 = 2600000;
        }
        f = VOLUME_NORMAL;
        int min22 = (int) (((int) (i3 / Math.min(i / i4, i2 / i5))) * f2);
        int videoBitrateWithFactor2 = (int) (getVideoBitrateWithFactor(f) / (921600.0f / (i5 * i4)));
        if (i3 >= videoBitrateWithFactor2) {
        }
    }

    /* loaded from: classes.dex */
    public static class PlaylistGlobalSearchParams {
        final long dialogId;
        public boolean endReached;
        final FiltersView.MediaFilterData filter;
        public int folderId;
        final long maxDate;
        final long minDate;
        public int nextSearchRate;
        final String query;
        public int totalCount;

        public PlaylistGlobalSearchParams(String str, long j, long j2, long j3, FiltersView.MediaFilterData mediaFilterData) {
            this.filter = mediaFilterData;
            this.query = str;
            this.dialogId = j;
            this.minDate = j2;
            this.maxDate = j3;
        }
    }

    public boolean currentPlaylistIsGlobalSearch() {
        return this.playlistGlobalSearchParams != null;
    }
}
