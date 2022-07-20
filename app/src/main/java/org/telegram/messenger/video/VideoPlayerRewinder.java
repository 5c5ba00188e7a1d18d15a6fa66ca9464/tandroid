package org.telegram.messenger.video;

import org.telegram.messenger.AndroidUtilities;
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
    private float playSpeed = 1.0f;
    private final Runnable backSeek = new AnonymousClass1();

    protected void onRewindCanceled() {
    }

    protected void onRewindStart(boolean z) {
    }

    protected void updateRewindProgressUi(long j, float f, boolean z) {
    }

    static /* synthetic */ long access$314(VideoPlayerRewinder videoPlayerRewinder, long j) {
        long j2 = videoPlayerRewinder.rewindBackSeekPlayerPosition + j;
        videoPlayerRewinder.rewindBackSeekPlayerPosition = j2;
        return j2;
    }

    static /* synthetic */ long access$322(VideoPlayerRewinder videoPlayerRewinder, long j) {
        long j2 = videoPlayerRewinder.rewindBackSeekPlayerPosition - j;
        videoPlayerRewinder.rewindBackSeekPlayerPosition = j2;
        return j2;
    }

    /* renamed from: org.telegram.messenger.video.VideoPlayerRewinder$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            VideoPlayerRewinder.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (VideoPlayerRewinder.this.videoPlayer == null) {
                return;
            }
            long duration = VideoPlayerRewinder.this.videoPlayer.getDuration();
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
                VideoPlayerRewinder.access$314(VideoPlayerRewinder.this, j2);
            } else {
                VideoPlayerRewinder.access$322(VideoPlayerRewinder.this, j2);
            }
            if (VideoPlayerRewinder.this.rewindBackSeekPlayerPosition < 0) {
                VideoPlayerRewinder.this.rewindBackSeekPlayerPosition = 0L;
            } else if (VideoPlayerRewinder.this.rewindBackSeekPlayerPosition > duration) {
                VideoPlayerRewinder.this.rewindBackSeekPlayerPosition = duration;
            }
            VideoPlayerRewinder videoPlayerRewinder2 = VideoPlayerRewinder.this;
            if (videoPlayerRewinder2.rewindByBackSeek && videoPlayerRewinder2.videoPlayer != null && VideoPlayerRewinder.this.rewindLastTime - VideoPlayerRewinder.this.rewindLastUpdatePlayerTime > 350) {
                VideoPlayerRewinder videoPlayerRewinder3 = VideoPlayerRewinder.this;
                videoPlayerRewinder3.rewindLastUpdatePlayerTime = videoPlayerRewinder3.rewindLastTime;
                VideoPlayerRewinder.this.videoPlayer.seekTo(VideoPlayerRewinder.this.rewindBackSeekPlayerPosition);
            }
            if (VideoPlayerRewinder.this.videoPlayer != null) {
                long j3 = VideoPlayerRewinder.this.rewindBackSeekPlayerPosition - VideoPlayerRewinder.this.startRewindFrom;
                float duration2 = ((float) VideoPlayerRewinder.this.rewindBackSeekPlayerPosition) / ((float) VideoPlayerRewinder.this.videoPlayer.getDuration());
                VideoPlayerRewinder videoPlayerRewinder4 = VideoPlayerRewinder.this;
                videoPlayerRewinder4.updateRewindProgressUi(j3, duration2, videoPlayerRewinder4.rewindByBackSeek);
            }
            if (VideoPlayerRewinder.this.rewindBackSeekPlayerPosition == 0 || VideoPlayerRewinder.this.rewindBackSeekPlayerPosition >= duration) {
                VideoPlayerRewinder videoPlayerRewinder5 = VideoPlayerRewinder.this;
                if (videoPlayerRewinder5.rewindByBackSeek && videoPlayerRewinder5.videoPlayer != null) {
                    VideoPlayerRewinder videoPlayerRewinder6 = VideoPlayerRewinder.this;
                    videoPlayerRewinder6.rewindLastUpdatePlayerTime = videoPlayerRewinder6.rewindLastTime;
                    VideoPlayerRewinder.this.videoPlayer.seekTo(VideoPlayerRewinder.this.rewindBackSeekPlayerPosition);
                }
                VideoPlayerRewinder.this.cancelRewind();
            }
            VideoPlayerRewinder videoPlayerRewinder7 = VideoPlayerRewinder.this;
            if (videoPlayerRewinder7.rewindCount <= 0) {
                return;
            }
            AndroidUtilities.runOnUIThread(videoPlayerRewinder7.backSeek, 16L);
        }
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
            VideoPlayer videoPlayer = this.videoPlayer;
            if (videoPlayer != null) {
                if (this.rewindByBackSeek) {
                    videoPlayer.seekTo(this.rewindBackSeekPlayerPosition);
                } else {
                    this.videoPlayer.seekTo(videoPlayer.getCurrentPosition());
                }
                this.videoPlayer.setPlaybackSpeed(this.playSpeed);
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

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0048, code lost:
        if (r0 != 2) goto L27;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void incrementRewindCount() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        int i = this.rewindCount + 1;
        this.rewindCount = i;
        boolean z = false;
        if (i == 1) {
            if (this.rewindForward && videoPlayer.isPlaying()) {
                this.rewindByBackSeek = false;
            } else {
                this.rewindByBackSeek = true;
            }
        }
        if (this.rewindForward && !this.rewindByBackSeek) {
            int i2 = this.rewindCount;
            if (i2 == 1) {
                this.videoPlayer.setPlaybackSpeed(4.0f);
            } else if (i2 == 2) {
                this.videoPlayer.setPlaybackSpeed(7.0f);
            } else {
                this.videoPlayer.setPlaybackSpeed(13.0f);
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
                this.rewindBackSeekPlayerPosition = this.videoPlayer.getCurrentPosition();
                long currentTimeMillis = System.currentTimeMillis();
                this.rewindLastTime = currentTimeMillis;
                this.rewindLastUpdatePlayerTime = currentTimeMillis;
                this.startRewindFrom = this.videoPlayer.getCurrentPosition();
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
            VideoPlayerRewinder$$ExternalSyntheticLambda0 videoPlayerRewinder$$ExternalSyntheticLambda0 = new VideoPlayerRewinder$$ExternalSyntheticLambda0(this);
            this.updateRewindRunnable = videoPlayerRewinder$$ExternalSyntheticLambda0;
            AndroidUtilities.runOnUIThread(videoPlayerRewinder$$ExternalSyntheticLambda0, 2000L);
        }
    }

    public /* synthetic */ void lambda$incrementRewindCount$0() {
        this.updateRewindRunnable = null;
        incrementRewindCount();
    }

    public float getVideoProgress() {
        return ((float) this.rewindBackSeekPlayerPosition) / ((float) this.videoPlayer.getDuration());
    }
}
