package org.telegram.messenger.video;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FileStreamLoadOperation;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.video.VideoPlayerHolderBase;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.VideoPlayer;

/* loaded from: classes3.dex */
public class VideoPlayerHolderBase {
    private boolean allowMultipleInstances;
    boolean audioDisabled;
    Uri contentUri;
    private int currentAccount;
    public volatile long currentPosition;
    public TLRPC.Document document;
    private volatile long duration;
    public boolean firstFrameRendered;
    Runnable initRunnable;
    int lastState;
    private Runnable onErrorListener;
    private Runnable onReadyListener;
    private Runnable onSeekUpdate;
    public boolean paused;
    public long pendingSeekTo;
    long playerDuration;
    public Bitmap playerStubBitmap;
    public Paint playerStubPaint;
    public float progress;
    public volatile boolean released;
    private volatile boolean seeking;
    long startTime;
    public boolean stubAvailable;
    private Surface surface;
    private SurfaceView surfaceView;
    private TextureView textureView;
    public Uri uri;
    VideoPlayer videoPlayer;
    final DispatchQueue dispatchQueue = Utilities.getOrCreatePlayerQueue();
    Runnable progressRunnable = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase.1
        @Override // java.lang.Runnable
        public void run() {
            VideoPlayerHolderBase videoPlayerHolderBase = VideoPlayerHolderBase.this;
            VideoPlayer videoPlayer = videoPlayerHolderBase.videoPlayer;
            if (videoPlayer != null) {
                if (videoPlayerHolderBase.lastState == 4) {
                    videoPlayerHolderBase.progress = 1.0f;
                } else {
                    videoPlayerHolderBase.currentPosition = videoPlayer.getCurrentPosition();
                    VideoPlayerHolderBase videoPlayerHolderBase2 = VideoPlayerHolderBase.this;
                    videoPlayerHolderBase2.playerDuration = videoPlayerHolderBase2.videoPlayer.getDuration();
                }
                VideoPlayerHolderBase videoPlayerHolderBase3 = VideoPlayerHolderBase.this;
                int i = videoPlayerHolderBase3.lastState;
                if (i == 3 || i == 2) {
                    videoPlayerHolderBase3.dispatchQueue.cancelRunnable(videoPlayerHolderBase3.progressRunnable);
                    VideoPlayerHolderBase videoPlayerHolderBase4 = VideoPlayerHolderBase.this;
                    videoPlayerHolderBase4.dispatchQueue.postRunnable(videoPlayerHolderBase4.progressRunnable, 16L);
                }
            }
        }
    };
    private volatile int triesCount = 3;
    private volatile boolean firstSeek = true;
    private volatile long lastSeek = -1;
    private long lastBetterSeek = -1;
    public float currentSeek = 0.0f;
    public volatile float currentSeekThread = 0.0f;
    private final Runnable betterSeek = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            VideoPlayerHolderBase.this.lambda$new$13();
        }
    };
    private final Runnable updateSeek = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            VideoPlayerHolderBase.this.lambda$new$14();
        }
    };

    class 2 implements VideoPlayer.VideoPlayerDelegate {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$0(long j) {
            VideoPlayerHolderBase videoPlayerHolderBase;
            Uri uri;
            if (VideoPlayerHolderBase.this.released || (uri = (videoPlayerHolderBase = VideoPlayerHolderBase.this).uri) == null) {
                return;
            }
            videoPlayerHolderBase.videoPlayer.preparePlayer(uri, "other");
            VideoPlayerHolderBase.this.videoPlayer.seekTo(j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$1() {
            if (VideoPlayerHolderBase.this.onErrorListener != null) {
                VideoPlayerHolderBase.this.onErrorListener.run();
                VideoPlayerHolderBase.this.onErrorListener = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRenderedFirstFrame$2() {
            if (VideoPlayerHolderBase.this.released) {
                return;
            }
            VideoPlayerHolderBase.this.onRenderedFirstFrame();
            if (VideoPlayerHolderBase.this.onReadyListener != null) {
                VideoPlayerHolderBase.this.onReadyListener.run();
                VideoPlayerHolderBase.this.onReadyListener = null;
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            FileLog.e(exc);
            final long currentPosition = VideoPlayerHolderBase.this.getCurrentPosition();
            VideoPlayerHolderBase.access$010(VideoPlayerHolderBase.this);
            if (VideoPlayerHolderBase.this.triesCount <= 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$2$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoPlayerHolderBase.2.this.lambda$onError$1();
                    }
                });
                return;
            }
            VideoPlayerHolderBase videoPlayerHolderBase = VideoPlayerHolderBase.this;
            DispatchQueue dispatchQueue = videoPlayerHolderBase.dispatchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerHolderBase.2.this.lambda$onError$0(currentPosition);
                }
            };
            videoPlayerHolderBase.initRunnable = runnable;
            dispatchQueue.postRunnable(runnable);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerHolderBase.2.this.lambda$onRenderedFirstFrame$2();
                }
            }, VideoPlayerHolderBase.this.surface != null ? 0L : VideoPlayerHolderBase.this.surfaceView == null ? 16L : 32L);
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
        public void onStateChanged(boolean z, int i) {
            VideoPlayerHolderBase videoPlayerHolderBase = VideoPlayerHolderBase.this;
            videoPlayerHolderBase.lastState = i;
            videoPlayerHolderBase.currentPosition = videoPlayerHolderBase.videoPlayer.getCurrentPosition();
            VideoPlayerHolderBase videoPlayerHolderBase2 = VideoPlayerHolderBase.this;
            videoPlayerHolderBase2.playerDuration = videoPlayerHolderBase2.videoPlayer.getDuration();
            if (i == 3 || i == 2) {
                VideoPlayerHolderBase videoPlayerHolderBase3 = VideoPlayerHolderBase.this;
                videoPlayerHolderBase3.dispatchQueue.cancelRunnable(videoPlayerHolderBase3.progressRunnable);
                VideoPlayerHolderBase videoPlayerHolderBase4 = VideoPlayerHolderBase.this;
                videoPlayerHolderBase4.dispatchQueue.postRunnable(videoPlayerHolderBase4.progressRunnable);
            } else if (i == 4) {
                if (VideoPlayerHolderBase.this.needRepeat()) {
                    VideoPlayerHolderBase videoPlayerHolderBase5 = VideoPlayerHolderBase.this;
                    videoPlayerHolderBase5.progress = 0.0f;
                    videoPlayerHolderBase5.videoPlayer.seekTo(0L);
                    VideoPlayerHolderBase.this.videoPlayer.play();
                } else {
                    VideoPlayerHolderBase.this.progress = 1.0f;
                }
            }
            VideoPlayerHolderBase.this.onStateChanged(z, i);
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
            VideoPlayerHolderBase.this.onVideoSizeChanged(i, i2, i3, f);
        }
    }

    static /* synthetic */ int access$010(VideoPlayerHolderBase videoPlayerHolderBase) {
        int i = videoPlayerHolderBase.triesCount;
        videoPlayerHolderBase.triesCount = i - 1;
        return i;
    }

    private void ensurePlayerCreated(boolean z) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
        }
        VideoPlayer videoPlayer2 = new VideoPlayer(false, z);
        this.videoPlayer = videoPlayer2;
        videoPlayer2.allowMultipleInstances = this.allowMultipleInstances;
        videoPlayer2.setDelegate(new 2());
        this.videoPlayer.setIsStory();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loopBack$9() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.seekTo(0L);
        }
        this.progress = 0.0f;
        this.currentPosition = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$13() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$14() {
        if (this.videoPlayer == null) {
            return;
        }
        long j = (long) (this.currentSeekThread * this.duration);
        if (this.lastSeek <= -1) {
            this.lastSeek = j;
        }
        if (Math.abs(j - this.lastSeek) >= (this.firstSeek ? 350 : 40)) {
            this.firstSeek = false;
            this.lastBetterSeek = j;
            this.dispatchQueue.cancelRunnable(this.betterSeek);
            this.dispatchQueue.postRunnable(this.betterSeek, 300L);
            VideoPlayer videoPlayer = this.videoPlayer;
            this.lastSeek = j;
            videoPlayer.seekTo(j, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pause$4() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.pause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$play$6() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            Surface surface = this.surface;
            if (surface != null) {
                videoPlayer.setSurface(surface);
            } else {
                SurfaceView surfaceView = this.surfaceView;
                if (surfaceView != null) {
                    videoPlayer.setSurfaceView(surfaceView);
                } else {
                    videoPlayer.setTextureView(this.textureView);
                }
            }
            long j = this.pendingSeekTo;
            if (j > 0) {
                this.videoPlayer.seekTo(j);
                this.pendingSeekTo = 0L;
            }
            this.videoPlayer.setPlayWhenReady(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$play$7(float f) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            Surface surface = this.surface;
            if (surface != null) {
                videoPlayer.setSurface(surface);
            } else {
                SurfaceView surfaceView = this.surfaceView;
                if (surfaceView != null) {
                    videoPlayer.setSurfaceView(surfaceView);
                } else {
                    videoPlayer.setTextureView(this.textureView);
                }
            }
            long j = this.pendingSeekTo;
            if (j > 0) {
                this.videoPlayer.seekTo(j);
                this.pendingSeekTo = 0L;
            }
            this.videoPlayer.setPlaybackSpeed(f);
            this.videoPlayer.setPlayWhenReady(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preparePlayer$0(boolean z, float f, Uri uri) {
        if (this.released) {
            return;
        }
        ensurePlayerCreated(z);
        this.videoPlayer.setPlaybackSpeed(f);
        FileLog.d("videoplayerholderbase.preparePlayer(): preparePlayer new player as preload uri=" + uri);
        this.videoPlayer.preparePlayer(uri, "other", 0);
        this.videoPlayer.setPlayWhenReady(false);
        this.videoPlayer.setWorkerQueue(this.dispatchQueue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$release$3(TLRPC.Document document, Runnable runnable) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setSurface(null);
            this.videoPlayer.setTextureView(null);
            this.videoPlayer.setSurfaceView(null);
            this.videoPlayer.releasePlayer(false);
        }
        if (document != null) {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(document);
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
        this.videoPlayer = null;
        this.dispatchQueue.cancelRunnable(this.progressRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$seekTo$11(long j) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            this.pendingSeekTo = j;
        } else {
            videoPlayer.seekTo(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$seekTo$12(long j, boolean z, Runnable runnable) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            this.pendingSeekTo = j;
        } else {
            videoPlayer.seekTo(j, z, runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAudioEnabled$8(boolean z, boolean z2) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        boolean isPlaying = videoPlayer.isPlaying();
        if (!z || this.videoPlayer.createdWithAudioTrack()) {
            this.videoPlayer.setVolume(z ? 1.0f : 0.0f);
            return;
        }
        this.videoPlayer.pause();
        long currentPosition = this.videoPlayer.getCurrentPosition();
        this.videoPlayer.releasePlayer(false);
        this.videoPlayer = null;
        ensurePlayerCreated(this.audioDisabled);
        Uri uri = this.uri;
        if (uri == null) {
            uri = this.contentUri;
        }
        FileLog.d("videoplayerholderbase.setAudioEnabled(): repreparePlayer as audio track is enabled back uri=" + uri);
        this.videoPlayer.preparePlayer(uri, "other");
        this.videoPlayer.setWorkerQueue(this.dispatchQueue);
        if (!z2) {
            Surface surface = this.surface;
            if (surface != null) {
                this.videoPlayer.setSurface(surface);
            } else {
                SurfaceView surfaceView = this.surfaceView;
                if (surfaceView != null) {
                    this.videoPlayer.setSurfaceView(surfaceView);
                } else {
                    this.videoPlayer.setTextureView(this.textureView);
                }
            }
        }
        this.videoPlayer.seekTo(currentPosition + 50);
        if (!isPlaying || z2) {
            this.videoPlayer.setPlayWhenReady(false);
            this.videoPlayer.pause();
        } else {
            this.videoPlayer.setPlayWhenReady(true);
            this.videoPlayer.play();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setSpeed$5(float f) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setPlaybackSpeed(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVolume$10(float f) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.setVolume(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$1() {
        this.initRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0067, code lost:
    
        if (r2 != null) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a9, code lost:
    
        r1.videoPlayer.setTextureView(r1.textureView);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a3, code lost:
    
        r1.videoPlayer.setSurfaceView(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a1, code lost:
    
        if (r2 != null) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$start$2(boolean z, float f, Uri uri, boolean z2, boolean z3, long j) {
        Surface surface;
        SurfaceView surfaceView;
        VideoPlayer videoPlayer;
        boolean z4;
        if (this.released) {
            FileLog.d("videoplayerholderbase returned from start: released");
            return;
        }
        if (this.videoPlayer != null) {
            FileLog.d("videoplayerholderbase.start(): player already exist");
            if (!z2) {
                Surface surface2 = this.surface;
                if (surface2 != null) {
                    this.videoPlayer.setSurface(surface2);
                } else {
                    SurfaceView surfaceView2 = this.surfaceView;
                    if (surfaceView2 != null) {
                        this.videoPlayer.setSurfaceView(surfaceView2);
                    } else {
                        this.videoPlayer.setTextureView(this.textureView);
                    }
                }
                this.videoPlayer.play();
            } else if (z3) {
                surface = this.surface;
                if (surface == null) {
                    surfaceView = this.surfaceView;
                }
                this.videoPlayer.setSurface(surface);
            }
            if (j > 0) {
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerHolderBase.this.lambda$start$1();
                }
            });
        }
        ensurePlayerCreated(z);
        this.videoPlayer.setPlaybackSpeed(f);
        FileLog.d("videoplayerholderbase.start(): preparePlayer new player uri=" + uri);
        this.videoPlayer.preparePlayer(uri, "other");
        this.videoPlayer.setWorkerQueue(this.dispatchQueue);
        if (z2) {
            if (z3) {
                surface = this.surface;
                if (surface == null) {
                    surfaceView = this.surfaceView;
                }
                this.videoPlayer.setSurface(surface);
            }
            if (j > 0) {
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerHolderBase.this.lambda$start$1();
                }
            });
        }
        Surface surface3 = this.surface;
        if (surface3 != null) {
            this.videoPlayer.setSurface(surface3);
        } else {
            SurfaceView surfaceView3 = this.surfaceView;
            if (surfaceView3 != null) {
                this.videoPlayer.setSurfaceView(surfaceView3);
            } else {
                this.videoPlayer.setTextureView(this.textureView);
            }
        }
        videoPlayer = this.videoPlayer;
        z4 = true;
        videoPlayer.setPlayWhenReady(z4);
        if (j > 0) {
            this.videoPlayer.seekTo(j);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$start$1();
            }
        });
        videoPlayer = this.videoPlayer;
        z4 = false;
        videoPlayer.setPlayWhenReady(z4);
        if (j > 0) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$start$1();
            }
        });
    }

    public void allowMultipleInstances(boolean z) {
        this.allowMultipleInstances = z;
    }

    public long getCurrentPosition() {
        return this.currentPosition;
    }

    public Uri getCurrentUri() {
        return this.contentUri;
    }

    public long getDuration() {
        return this.playerDuration;
    }

    public float getPlaybackProgress(long j) {
        if (this.lastState == 4) {
            this.progress = 1.0f;
        } else {
            this.progress = j != 0 ? this.currentPosition / j : this.currentPosition / this.playerDuration;
            if (!this.seeking) {
                this.currentSeek = this.progress;
                this.lastSeek = this.currentPosition;
            }
        }
        return this.progress;
    }

    public boolean isBuffering() {
        return !this.released && this.lastState == 2;
    }

    public boolean isPlaying() {
        return !this.paused;
    }

    public void loopBack() {
        this.progress = 0.0f;
        this.lastState = 1;
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$loopBack$9();
            }
        });
    }

    public boolean needRepeat() {
        return false;
    }

    public void onRenderedFirstFrame() {
    }

    public void onStateChanged(boolean z, int i) {
    }

    protected void onVideoSizeChanged(int i, int i2, int i3, float f) {
    }

    public void pause() {
        if (this.released || this.paused) {
            return;
        }
        this.paused = true;
        prepareStub();
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$pause$4();
            }
        });
    }

    public void play() {
        if (!this.released && this.paused) {
            this.paused = false;
            this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerHolderBase.this.lambda$play$6();
                }
            });
        }
    }

    public void play(final float f) {
        if (!this.released && this.paused) {
            this.paused = false;
            this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerHolderBase.this.lambda$play$7(f);
                }
            });
        }
    }

    public void preparePlayer(final Uri uri, final boolean z, final float f) {
        this.audioDisabled = z;
        this.currentAccount = this.currentAccount;
        this.contentUri = uri;
        this.paused = true;
        Runnable runnable = this.initRunnable;
        if (runnable != null) {
            this.dispatchQueue.cancelRunnable(runnable);
        }
        DispatchQueue dispatchQueue = this.dispatchQueue;
        Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$preparePlayer$0(z, f, uri);
            }
        };
        this.initRunnable = runnable2;
        dispatchQueue.postRunnable(runnable2);
    }

    public void prepareStub() {
        SurfaceView surfaceView = this.surfaceView;
        if (surfaceView != null && this.firstFrameRendered && surfaceView.getHolder().getSurface().isValid()) {
            this.stubAvailable = true;
            if (this.playerStubBitmap == null) {
                this.playerStubBitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
                this.playerStubPaint = new Paint(1);
            }
            if (Build.VERSION.SDK_INT >= 24) {
                AndroidUtilities.getBitmapFromSurface(this.surfaceView, this.playerStubBitmap);
                if (this.playerStubBitmap.getPixel(0, 0) == 0) {
                    this.stubAvailable = false;
                }
            }
        }
    }

    public boolean release(final Runnable runnable) {
        final TLRPC.Document document = this.document;
        if (document != null && FileStreamLoadOperation.getStreamPrioriy(document) != 0) {
            FileStreamLoadOperation.setPriorityForDocument(document, 0);
            FileLoader.getInstance(this.currentAccount).changePriority(0, document, null, null, null, null, null);
        }
        this.released = true;
        this.dispatchQueue.cancelRunnable(this.initRunnable);
        this.dispatchQueue.cancelRunnable(this.progressRunnable);
        this.initRunnable = null;
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$release$3(document, runnable);
            }
        });
        Bitmap bitmap = this.playerStubBitmap;
        if (bitmap != null) {
            AndroidUtilities.recycleBitmap(bitmap);
            this.playerStubBitmap = null;
        }
        return true;
    }

    public float seek(float f, long j) {
        if (this.videoPlayer == null) {
            return this.currentSeek;
        }
        this.duration = j;
        float clamp = Utilities.clamp(this.currentSeek + f, 1.0f, 0.0f);
        this.currentSeek = clamp;
        this.currentSeekThread = clamp;
        this.dispatchQueue.cancelRunnable(this.updateSeek);
        this.dispatchQueue.postRunnable(this.updateSeek);
        return this.currentSeek;
    }

    public void seekTo(final long j) {
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$seekTo$11(j);
            }
        });
    }

    public void seekTo(final long j, final boolean z, final Runnable runnable) {
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$seekTo$12(j, z, runnable);
            }
        });
    }

    public void setAudioEnabled(final boolean z, final boolean z2) {
        boolean z3 = !z;
        if (this.audioDisabled == z3) {
            return;
        }
        this.audioDisabled = z3;
        this.triesCount = 3;
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$setAudioEnabled$8(z, z2);
            }
        });
    }

    public void setOnErrorListener(Runnable runnable) {
        this.onErrorListener = runnable;
    }

    public void setOnReadyListener(Runnable runnable) {
        this.onReadyListener = runnable;
    }

    public void setOnSeekUpdate(Runnable runnable) {
        this.onSeekUpdate = runnable;
    }

    public void setSeeking(boolean z) {
        if (z && !this.seeking) {
            this.firstSeek = true;
        }
        this.seeking = z;
        if (z) {
            return;
        }
        this.dispatchQueue.cancelRunnable(this.betterSeek);
    }

    public void setSpeed(final float f) {
        if (this.released) {
            return;
        }
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$setSpeed$5(f);
            }
        });
    }

    public void setVolume(final float f) {
        this.dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$setVolume$10(f);
            }
        });
    }

    public void start(final boolean z, final boolean z2, final Uri uri, final long j, final boolean z3, final float f) {
        this.startTime = System.currentTimeMillis();
        this.audioDisabled = z3;
        this.paused = z2;
        this.triesCount = 3;
        if (j > 0) {
            this.currentPosition = j;
        }
        DispatchQueue dispatchQueue = this.dispatchQueue;
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerHolderBase$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                VideoPlayerHolderBase.this.lambda$start$2(z3, f, uri, z2, z, j);
            }
        };
        this.initRunnable = runnable;
        dispatchQueue.postRunnable(runnable);
    }

    public VideoPlayerHolderBase with(Surface surface) {
        this.surfaceView = null;
        this.textureView = null;
        this.surface = surface;
        return this;
    }

    public VideoPlayerHolderBase with(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.textureView = null;
        this.surface = null;
        return this;
    }

    public VideoPlayerHolderBase with(TextureView textureView) {
        this.surfaceView = null;
        this.textureView = textureView;
        this.surface = null;
        return this;
    }
}
