package org.telegram.messenger.video;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.PhotoViewerWebView;
import org.telegram.ui.Components.VideoPlayer;
/* loaded from: classes.dex */
public class VideoPlayerRewinder {
    private long rewindBackSeekPlayerPosition;
    public boolean rewindByBackSeek;
    public int rewindCount;
    private boolean rewindForward;
    private long rewindLastTime;
    private long rewindLastUpdatePlayerTime;
    private long startRewindFrom;
    private Runnable updateRewindRunnable;
    private VideoPlayer videoPlayer;
    private PhotoViewerWebView webView;
    private float playSpeed = 1.0f;
    private final Runnable backSeek = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerRewinder.1
        @Override // java.lang.Runnable
        public void run() {
            if (VideoPlayerRewinder.this.videoPlayer == null && VideoPlayerRewinder.this.webView == null) {
                return;
            }
            long duration = VideoPlayerRewinder.this.getDuration();
            if (duration == 0 || duration == -9223372036854775807L) {
                VideoPlayerRewinder.this.rewindLastTime = System.currentTimeMillis();
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - VideoPlayerRewinder.this.rewindLastTime;
            VideoPlayerRewinder.this.rewindLastTime = currentTimeMillis;
            VideoPlayerRewinder videoPlayerRewinder = VideoPlayerRewinder.this;
            int i = videoPlayerRewinder.rewindCount;
            long j2 = j * (i == 1 ? 3L : i == 2 ? 6L : 12L);
            if (videoPlayerRewinder.rewindForward) {
                VideoPlayerRewinder.access$514(VideoPlayerRewinder.this, j2);
            } else {
                VideoPlayerRewinder.access$522(VideoPlayerRewinder.this, j2);
            }
            if (VideoPlayerRewinder.this.rewindBackSeekPlayerPosition < 0) {
                VideoPlayerRewinder.this.rewindBackSeekPlayerPosition = 0L;
            } else if (VideoPlayerRewinder.this.rewindBackSeekPlayerPosition > duration) {
                VideoPlayerRewinder.this.rewindBackSeekPlayerPosition = duration;
            }
            VideoPlayerRewinder videoPlayerRewinder2 = VideoPlayerRewinder.this;
            if (videoPlayerRewinder2.rewindByBackSeek && videoPlayerRewinder2.rewindLastTime - VideoPlayerRewinder.this.rewindLastUpdatePlayerTime > 350) {
                VideoPlayerRewinder videoPlayerRewinder3 = VideoPlayerRewinder.this;
                videoPlayerRewinder3.rewindLastUpdatePlayerTime = videoPlayerRewinder3.rewindLastTime;
                VideoPlayerRewinder videoPlayerRewinder4 = VideoPlayerRewinder.this;
                videoPlayerRewinder4.seekTo(videoPlayerRewinder4.rewindBackSeekPlayerPosition);
            }
            long j3 = VideoPlayerRewinder.this.rewindBackSeekPlayerPosition - VideoPlayerRewinder.this.startRewindFrom;
            float duration2 = ((float) VideoPlayerRewinder.this.rewindBackSeekPlayerPosition) / ((float) VideoPlayerRewinder.this.getDuration());
            VideoPlayerRewinder videoPlayerRewinder5 = VideoPlayerRewinder.this;
            videoPlayerRewinder5.updateRewindProgressUi(j3, duration2, videoPlayerRewinder5.rewindByBackSeek);
            if (VideoPlayerRewinder.this.rewindBackSeekPlayerPosition == 0 || VideoPlayerRewinder.this.rewindBackSeekPlayerPosition >= duration) {
                VideoPlayerRewinder videoPlayerRewinder6 = VideoPlayerRewinder.this;
                if (videoPlayerRewinder6.rewindByBackSeek) {
                    videoPlayerRewinder6.rewindLastUpdatePlayerTime = videoPlayerRewinder6.rewindLastTime;
                    VideoPlayerRewinder videoPlayerRewinder7 = VideoPlayerRewinder.this;
                    videoPlayerRewinder7.seekTo(videoPlayerRewinder7.rewindBackSeekPlayerPosition);
                }
                VideoPlayerRewinder.this.cancelRewind();
            }
            VideoPlayerRewinder videoPlayerRewinder8 = VideoPlayerRewinder.this;
            if (videoPlayerRewinder8.rewindCount <= 0) {
                return;
            }
            AndroidUtilities.runOnUIThread(videoPlayerRewinder8.backSeek, 16L);
        }
    };

    protected void onRewindCanceled() {
    }

    protected void onRewindStart(boolean z) {
    }

    protected void updateRewindProgressUi(long j, float f, boolean z) {
    }

    static /* synthetic */ long access$514(VideoPlayerRewinder videoPlayerRewinder, long j) {
        long j2 = videoPlayerRewinder.rewindBackSeekPlayerPosition + j;
        videoPlayerRewinder.rewindBackSeekPlayerPosition = j2;
        return j2;
    }

    static /* synthetic */ long access$522(VideoPlayerRewinder videoPlayerRewinder, long j) {
        long j2 = videoPlayerRewinder.rewindBackSeekPlayerPosition - j;
        videoPlayerRewinder.rewindBackSeekPlayerPosition = j2;
        return j2;
    }

    public void startRewind(PhotoViewerWebView photoViewerWebView, boolean z, float f) {
        this.webView = photoViewerWebView;
        this.playSpeed = f;
        this.rewindForward = z;
        cancelRewind();
        incrementRewindCount();
    }

    public void startRewind(VideoPlayer videoPlayer, boolean z, float f) {
        this.videoPlayer = videoPlayer;
        this.playSpeed = f;
        this.rewindForward = z;
        cancelRewind();
        incrementRewindCount();
    }

    public void cancelRewind() {
        if (this.rewindCount != 0) {
            this.rewindCount = 0;
            if (this.videoPlayer != null || this.webView != null) {
                if (this.rewindByBackSeek) {
                    seekTo(this.rewindBackSeekPlayerPosition);
                } else {
                    seekTo(getCurrentPosition());
                }
                setPlaybackSpeed(this.playSpeed);
            }
        }
        AndroidUtilities.cancelRunOnUIThread(this.backSeek);
        Runnable runnable = this.updateRewindRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.updateRewindRunnable = null;
        }
        onRewindCanceled();
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0046, code lost:
        if (r0 != 2) goto L23;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void incrementRewindCount() {
        if (this.videoPlayer == null && this.webView == null) {
            return;
        }
        int i = this.rewindCount + 1;
        this.rewindCount = i;
        boolean z = false;
        if (i == 1) {
            if (this.rewindForward && isPlaying()) {
                this.rewindByBackSeek = false;
            } else {
                this.rewindByBackSeek = true;
            }
        }
        if (this.rewindForward && !this.rewindByBackSeek) {
            int i2 = this.rewindCount;
            if (i2 == 1) {
                setPlaybackSpeed(4.0f);
            } else if (i2 == 2) {
                setPlaybackSpeed(7.0f);
            } else {
                setPlaybackSpeed(13.0f);
                if (this.rewindCount == 1) {
                }
                AndroidUtilities.cancelRunOnUIThread(this.backSeek);
                AndroidUtilities.runOnUIThread(this.backSeek);
                if (!z) {
                }
            }
            z = true;
            if (this.rewindCount == 1) {
            }
            AndroidUtilities.cancelRunOnUIThread(this.backSeek);
            AndroidUtilities.runOnUIThread(this.backSeek);
            if (!z) {
            }
        } else {
            int i3 = this.rewindCount;
            if (i3 != 1) {
            }
            z = true;
            if (this.rewindCount == 1) {
                this.rewindBackSeekPlayerPosition = getCurrentPosition();
                long currentTimeMillis = System.currentTimeMillis();
                this.rewindLastTime = currentTimeMillis;
                this.rewindLastUpdatePlayerTime = currentTimeMillis;
                this.startRewindFrom = getCurrentPosition();
                onRewindStart(this.rewindForward);
            }
            AndroidUtilities.cancelRunOnUIThread(this.backSeek);
            AndroidUtilities.runOnUIThread(this.backSeek);
            if (!z) {
                return;
            }
            Runnable runnable = this.updateRewindRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.video.VideoPlayerRewinder$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPlayerRewinder.this.lambda$incrementRewindCount$0();
                }
            };
            this.updateRewindRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$incrementRewindCount$0() {
        this.updateRewindRunnable = null;
        incrementRewindCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void seekTo(long j) {
        PhotoViewerWebView photoViewerWebView = this.webView;
        if (photoViewerWebView != null) {
            photoViewerWebView.seekTo(j);
            return;
        }
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        videoPlayer.seekTo(j);
    }

    private void setPlaybackSpeed(float f) {
        PhotoViewerWebView photoViewerWebView = this.webView;
        if (photoViewerWebView != null) {
            photoViewerWebView.setPlaybackSpeed(f);
            return;
        }
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        videoPlayer.setPlaybackSpeed(f);
    }

    private long getCurrentPosition() {
        PhotoViewerWebView photoViewerWebView = this.webView;
        if (photoViewerWebView != null) {
            return photoViewerWebView.getCurrentPosition();
        }
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            return videoPlayer.getCurrentPosition();
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getDuration() {
        PhotoViewerWebView photoViewerWebView = this.webView;
        if (photoViewerWebView != null) {
            return photoViewerWebView.getVideoDuration();
        }
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            return videoPlayer.getDuration();
        }
        return 0L;
    }

    private boolean isPlaying() {
        PhotoViewerWebView photoViewerWebView = this.webView;
        if (photoViewerWebView != null) {
            return photoViewerWebView.isPlaying();
        }
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            return videoPlayer.isPlaying();
        }
        return false;
    }

    public float getVideoProgress() {
        return ((float) this.rewindBackSeekPlayerPosition) / ((float) getDuration());
    }
}
