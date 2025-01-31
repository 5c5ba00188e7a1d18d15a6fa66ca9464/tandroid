package org.telegram.ui.Stories.recorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Stories.recorder.TimelineView;

/* loaded from: classes5.dex */
public class TimelineView extends View {
    private Runnable askExactSeek;
    private StaticLayout audioAuthor;
    private float audioAuthorLeft;
    private final TextPaint audioAuthorPaint;
    private float audioAuthorWidth;
    private final BlurringShader.StoryBlurDrawer audioBlur;
    private final RectF audioBounds;
    private final Path audioClipPath;
    private final Paint audioDotPaint;
    private long audioDuration;
    private final Drawable audioIcon;
    private float audioLeft;
    private long audioOffset;
    private String audioPath;
    private float audioRight;
    private boolean audioSelected;
    private final AnimatedFloat audioSelectedT;
    private final AnimatedFloat audioT;
    private StaticLayout audioTitle;
    private float audioTitleLeft;
    private final TextPaint audioTitlePaint;
    private float audioTitleWidth;
    private float audioVolume;
    private final BlurringShader.StoryBlurDrawer audioWaveformBlur;
    private final BlurringShader.StoryBlurDrawer backgroundBlur;
    private final BlurringShader.BlurManager blurManager;
    private final Path collageClipPath;
    private final Paint collageFramePaint;
    private Track collageMain;
    private int collageSelected;
    private final ArrayList collageTracks;
    private final ArrayList collageWaveforms;
    private long coverEnd;
    private long coverStart;
    private TimelineDelegate delegate;
    private float dragSpeed;
    private boolean dragged;
    private boolean draggingProgress;
    private final LinearGradient ellipsizeGradient;
    private final Matrix ellipsizeMatrix;
    private final Paint ellipsizePaint;
    private int h;
    private boolean hadDragChange;
    private boolean hasAudio;
    private boolean hasRound;
    private boolean isCover;
    private long lastTime;
    private float lastX;
    private final AnimatedFloat loopProgress;
    private long loopProgressFrom;
    private final Runnable onLongPress;
    private Runnable onTimelineClick;
    public boolean open;
    private final AnimatedFloat openT;
    private int ph;
    private int pressCollageIndex;
    private int pressHandle;
    private int pressHandleCollageIndex;
    private long pressTime;
    private int pressType;
    private final View previewContainer;
    private long progress;
    private final Paint progressShadowPaint;
    private final Paint progressWhitePaint;
    private int px;
    private int py;
    private final Paint regionCutPaint;
    private final Paint regionHandlePaint;
    private final Paint regionPaint;
    private boolean resetWaveform;
    private final Theme.ResourcesProvider resourcesProvider;
    private final RectF roundBounds;
    private final Path roundClipPath;
    private long roundDuration;
    private float roundLeft;
    private long roundOffset;
    private String roundPath;
    private float roundRight;
    private boolean roundSelected;
    private final AnimatedFloat roundSelectedT;
    private final AnimatedFloat roundT;
    private VideoThumbsLoader roundThumbs;
    private float roundVolume;
    private long scroll;
    private final Scroller scroller;
    private boolean scrolling;
    private int scrollingCollage;
    private boolean scrollingVideo;
    private final Path selectedCollageClipPath;
    private final Path selectedVideoClipPath;
    final float[] selectedVideoRadii;
    private int sw;
    private final RectF timelineBounds;
    private final Path timelineClipPath;
    private final Drawable timelineIcon;
    private final Text timelineText;
    private final AnimatedFloat timelineWaveformLoaded;
    private final AnimatedFloat timelineWaveformMax;
    private final WaveformPath timelineWaveformPath;
    private VelocityTracker velocityTracker;
    private final RectF videoBounds;
    private final Path videoClipPath;
    private final Paint videoFramePaint;
    private Track videoTrack;
    private int w;
    private int wasScrollX;
    private AudioWaveformLoader waveform;
    private boolean waveformIsLoaded;
    private final AnimatedFloat waveformMax;
    private final Paint waveformPaint;
    private final WaveformPath waveformPath;

    /* JADX INFO: Access modifiers changed from: private */
    class AudioWaveformLoader {
        private final AnimatedFloat animatedLoaded;
        private final int count;
        private final short[] data;
        private long duration;
        private final MediaExtractor extractor;
        private MediaFormat inputFormat;
        private short max;
        private FfmpegAudioWaveformLoader waveformLoader;
        private int loaded = 0;
        private final Object lock = new Object();
        private boolean stop = false;

        public AudioWaveformLoader(String str, int i) {
            this.animatedLoaded = new AnimatedFloat(TimelineView.this, 0L, 600L, CubicBezierInterpolator.EASE_OUT_QUINT);
            int i2 = 0;
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.extractor = mediaExtractor;
            String str2 = null;
            try {
                mediaExtractor.setDataSource(str);
                int trackCount = mediaExtractor.getTrackCount();
                while (true) {
                    if (i2 < trackCount) {
                        MediaFormat trackFormat = this.extractor.getTrackFormat(i2);
                        str2 = trackFormat.getString("mime");
                        if (str2 != null && str2.startsWith("audio/")) {
                            this.extractor.selectTrack(i2);
                            this.inputFormat = trackFormat;
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                MediaFormat mediaFormat = this.inputFormat;
                if (mediaFormat != null) {
                    this.duration = mediaFormat.getLong("durationUs") / 1000000;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            int min = Math.min(Math.round((((this.duration * 1000) / Math.min(TimelineView.this.videoTrack != null ? TimelineView.this.videoTrack.duration : !TimelineView.this.collageTracks.isEmpty() ? TimelineView.this.getBaseDuration() : TimelineView.this.hasRound ? TimelineView.this.roundDuration : this.duration * 1000, TimelineView.this.getMaxScrollDuration())) * i) / Math.round(AndroidUtilities.dpf2(3.3333f))), 4000);
            this.count = min;
            this.data = new short[min];
            if (this.duration <= 0 || this.inputFormat == null) {
                return;
            }
            if ("audio/mpeg".equals(str2) || "audio/mp3".equals(str2) || "audio/mp4a".equals(str2) || MediaController.AUDIO_MIME_TYPE.equals(str2)) {
                this.waveformLoader = new FfmpegAudioWaveformLoader(str, min, new Utilities.Callback2() { // from class: org.telegram.ui.Stories.recorder.TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        TimelineView.AudioWaveformLoader.this.lambda$run$0((short[]) obj, ((Integer) obj2).intValue());
                    }
                });
            } else {
                Utilities.phoneBookQueue.postRunnable(new TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda0(this));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: receiveData, reason: merged with bridge method [inline-methods] */
        public void lambda$run$0(short[] sArr, int i) {
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = this.loaded + i2;
                short[] sArr2 = this.data;
                if (i3 >= sArr2.length) {
                    break;
                }
                sArr2[i3] = sArr[i2];
                short s = this.max;
                short s2 = sArr[i2];
                if (s < s2) {
                    this.max = s2;
                }
            }
            this.loaded += i;
            TimelineView.this.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x015d, code lost:
        
            r3 = r16;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            MediaCodec.BufferInfo bufferInfo;
            int i;
            int i2;
            short s;
            try {
                int round = Math.round(((this.duration * this.inputFormat.getInteger("sample-rate")) / this.count) / 5.0f);
                MediaCodec createDecoderByType = MediaCodec.createDecoderByType(this.inputFormat.getString("mime"));
                if (createDecoderByType == null) {
                    return;
                }
                createDecoderByType.configure(this.inputFormat, (Surface) null, (MediaCrypto) null, 0);
                createDecoderByType.start();
                ByteBuffer[] inputBuffers = createDecoderByType.getInputBuffers();
                ByteBuffer[] outputBuffers = createDecoderByType.getOutputBuffers();
                final short[] sArr = new short[32];
                int i3 = -1;
                int i4 = 0;
                boolean z = false;
                int i5 = 0;
                int i6 = 0;
                short s2 = 0;
                while (true) {
                    MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                    int dequeueInputBuffer = createDecoderByType.dequeueInputBuffer(2500L);
                    if (dequeueInputBuffer >= 0) {
                        int readSampleData = this.extractor.readSampleData(Build.VERSION.SDK_INT < 21 ? inputBuffers[dequeueInputBuffer] : createDecoderByType.getInputBuffer(dequeueInputBuffer), 0);
                        if (readSampleData < 0) {
                            i = 21;
                            bufferInfo = bufferInfo2;
                            createDecoderByType.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                            z = true;
                        } else {
                            bufferInfo = bufferInfo2;
                            i = 21;
                            createDecoderByType.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, this.extractor.getSampleTime(), 0);
                            this.extractor.advance();
                        }
                    } else {
                        bufferInfo = bufferInfo2;
                        i = 21;
                    }
                    if (i3 >= 0) {
                        (Build.VERSION.SDK_INT < i ? outputBuffers[i3] : createDecoderByType.getOutputBuffer(i3)).position(0);
                    }
                    MediaCodec.BufferInfo bufferInfo3 = bufferInfo;
                    long j = 2500;
                    while (true) {
                        i3 = createDecoderByType.dequeueOutputBuffer(bufferInfo3, j);
                        if (i3 == -1 || z) {
                            break;
                        }
                        if (i3 >= 0) {
                            ByteBuffer outputBuffer = Build.VERSION.SDK_INT < i ? outputBuffers[i3] : createDecoderByType.getOutputBuffer(i3);
                            if (outputBuffer != null && bufferInfo3.size > 0) {
                                int i7 = i6;
                                while (outputBuffer.remaining() > 0) {
                                    short s3 = (short) (((outputBuffer.get() & 255) << 8) | (outputBuffer.get() & 255));
                                    if (i7 >= round) {
                                        sArr[i4 - i5] = s2;
                                        int i8 = i4 + 1;
                                        final int i9 = i8 - i5;
                                        if (i9 >= sArr.length || i8 >= this.count) {
                                            short[] sArr2 = new short[sArr.length];
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda2
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    TimelineView.AudioWaveformLoader.this.lambda$run$0(sArr, i9);
                                                }
                                            });
                                            sArr = sArr2;
                                            i5 = i8;
                                        }
                                        i4 = i8;
                                        if (i8 >= this.data.length) {
                                            i6 = 0;
                                            s2 = 0;
                                            break;
                                        } else {
                                            s = 0;
                                            i7 = 0;
                                        }
                                    } else {
                                        s = s2;
                                    }
                                    s2 = s < s3 ? s3 : s;
                                    i7++;
                                    if (outputBuffer.remaining() < 8) {
                                        break;
                                    } else {
                                        outputBuffer.position(outputBuffer.position() + 8);
                                    }
                                }
                                i6 = i7;
                            }
                            createDecoderByType.releaseOutputBuffer(i3, false);
                            if ((bufferInfo3.flags & 4) != 0) {
                                i2 = i4;
                                z = true;
                                break;
                            }
                        } else if (i3 == -3) {
                            outputBuffers = createDecoderByType.getOutputBuffers();
                        }
                        j = 2500;
                        i = 21;
                    }
                    synchronized (this.lock) {
                        try {
                            if (!this.stop) {
                                if (z || i2 >= this.count) {
                                    break;
                                } else {
                                    i4 = i2;
                                }
                            } else {
                                break;
                            }
                        } finally {
                        }
                    }
                }
                createDecoderByType.stop();
                createDecoderByType.release();
                this.extractor.release();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        public void destroy() {
            FfmpegAudioWaveformLoader ffmpegAudioWaveformLoader = this.waveformLoader;
            if (ffmpegAudioWaveformLoader != null) {
                ffmpegAudioWaveformLoader.destroy();
            }
            Utilities.phoneBookQueue.cancelRunnable(new TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda0(this));
            synchronized (this.lock) {
                this.stop = true;
            }
        }

        public short getBar(int i) {
            return this.data[i];
        }

        public int getCount() {
            return this.count;
        }

        public int getLoadedCount() {
            return this.loaded;
        }

        public short getMaxBar() {
            return this.max;
        }
    }

    public interface TimelineDelegate {

        public abstract /* synthetic */ class -CC {
            public static void $default$onAudioLeftChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onAudioOffsetChange(TimelineDelegate timelineDelegate, long j) {
            }

            public static void $default$onAudioRemove(TimelineDelegate timelineDelegate) {
            }

            public static void $default$onAudioRightChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onAudioVolumeChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onProgressChange(TimelineDelegate timelineDelegate, long j, boolean z) {
            }

            public static void $default$onProgressDragChange(TimelineDelegate timelineDelegate, boolean z) {
            }

            public static void $default$onRoundLeftChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onRoundOffsetChange(TimelineDelegate timelineDelegate, long j) {
            }

            public static void $default$onRoundRemove(TimelineDelegate timelineDelegate) {
            }

            public static void $default$onRoundRightChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onRoundSelectChange(TimelineDelegate timelineDelegate, boolean z) {
            }

            public static void $default$onRoundVolumeChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onVideoLeftChange(TimelineDelegate timelineDelegate, int i, float f) {
            }

            public static void $default$onVideoOffsetChange(TimelineDelegate timelineDelegate, int i, long j) {
            }

            public static void $default$onVideoRightChange(TimelineDelegate timelineDelegate, int i, float f) {
            }

            public static void $default$onVideoRightChange(TimelineDelegate timelineDelegate, boolean z, float f) {
            }

            public static void $default$onVideoSelected(TimelineDelegate timelineDelegate, int i) {
            }

            public static void $default$onVideoVolumeChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onVideoVolumeChange(TimelineDelegate timelineDelegate, int i, float f) {
            }
        }

        void onAudioLeftChange(float f);

        void onAudioOffsetChange(long j);

        void onAudioRemove();

        void onAudioRightChange(float f);

        void onAudioVolumeChange(float f);

        void onProgressChange(long j, boolean z);

        void onProgressDragChange(boolean z);

        void onRoundLeftChange(float f);

        void onRoundOffsetChange(long j);

        void onRoundRemove();

        void onRoundRightChange(float f);

        void onRoundSelectChange(boolean z);

        void onRoundVolumeChange(float f);

        void onVideoLeftChange(int i, float f);

        void onVideoLeftChange(boolean z, float f);

        void onVideoOffsetChange(int i, long j);

        void onVideoRightChange(int i, float f);

        void onVideoRightChange(boolean z, float f);

        void onVideoSelected(int i);

        void onVideoVolumeChange(float f);

        void onVideoVolumeChange(int i, float f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Track {
        final RectF bounds;
        long duration;
        int index;
        boolean isRound;
        float left;
        long offset;
        String path;
        float right;
        private final AnimatedFloat selectedT;
        VideoThumbsLoader thumbs;
        float volume;

        private Track() {
            this.bounds = new RectF();
            this.selectedT = new AnimatedFloat(TimelineView.this, 360L, CubicBezierInterpolator.EASE_OUT_QUINT);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setupThumbs$0() {
            VideoThumbsLoader videoThumbsLoader = this.thumbs;
            if (videoThumbsLoader == null || videoThumbsLoader.getDuration() <= 0) {
                return;
            }
            this.duration = this.thumbs.getDuration();
            TimelineView.this.sortCollage();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setupThumbs(boolean z) {
            if (TimelineView.this.getMeasuredWidth() > 0) {
                VideoThumbsLoader videoThumbsLoader = this.thumbs;
                if (videoThumbsLoader == null || z) {
                    if (videoThumbsLoader != null) {
                        videoThumbsLoader.destroy();
                        this.thumbs = null;
                    }
                    TimelineView timelineView = TimelineView.this;
                    boolean z2 = this.isRound;
                    String str = this.path;
                    int i = (timelineView.w - TimelineView.this.px) - TimelineView.this.px;
                    int dp = AndroidUtilities.dp(38.0f);
                    long j = this.duration;
                    this.thumbs = timelineView.new VideoThumbsLoader(z2, str, i, dp, j > 2 ? Long.valueOf(j) : null, TimelineView.this.getMaxScrollDuration(), TimelineView.this.coverStart, TimelineView.this.coverEnd, new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$Track$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            TimelineView.Track.this.lambda$setupThumbs$0();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setupWaveform(boolean z) {
            int i = this.index;
            if (i < 0 || i >= TimelineView.this.collageWaveforms.size()) {
                return;
            }
            AudioWaveformLoader audioWaveformLoader = (AudioWaveformLoader) TimelineView.this.collageWaveforms.get(this.index);
            if (TimelineView.this.getMeasuredWidth() > 0) {
                if (audioWaveformLoader == null || z) {
                    if (audioWaveformLoader != null) {
                        audioWaveformLoader.destroy();
                    }
                    TimelineView timelineView = TimelineView.this;
                    TimelineView.this.collageWaveforms.set(this.index, timelineView.new AudioWaveformLoader(this.path, (timelineView.getMeasuredWidth() - TimelineView.this.getPaddingLeft()) - TimelineView.this.getPaddingRight()));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class VideoThumbsLoader {
        private Path clipPath;
        private int count;
        private boolean destroyed;
        private long duration;
        private volatile int frameHeight;
        private volatile long frameIterator;
        private volatile int frameWidth;
        private final boolean isRound;
        private long nextFrame;
        private final ArrayList frames = new ArrayList();
        private boolean loading = false;
        private final Paint bitmapPaint = new Paint(3);
        private MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

        public class BitmapFrame {
            private final AnimatedFloat alpha;
            public Bitmap bitmap;

            public BitmapFrame(Bitmap bitmap) {
                this.alpha = new AnimatedFloat(0.0f, TimelineView.this, 0L, 240L, CubicBezierInterpolator.EASE_OUT_QUINT);
                this.bitmap = bitmap;
            }

            public float getAlpha() {
                return this.alpha.set(1.0f);
            }
        }

        public VideoThumbsLoader(boolean z, final String str, final int i, final int i2, final Long l, final long j, final long j2, final long j3, final Runnable runnable) {
            this.isRound = z;
            Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TimelineView.VideoThumbsLoader.this.lambda$new$0(str, l, j2, j3, i2, j, i, runnable);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x005c, code lost:
        
            if (r0 != 270) goto L26;
         */
        /* JADX WARN: Removed duplicated region for block: B:22:0x006c  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0078  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0080 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00ce  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00d9  */
        /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$new$0(String str, Long l, long j, long j2, int i, long j3, int i2, Runnable runnable) {
            int i3;
            String extractMetadata;
            long maxScrollDuration = TimelineView.this.getMaxScrollDuration();
            try {
                this.metadataRetriever.setDataSource(str);
                String extractMetadata2 = this.metadataRetriever.extractMetadata(9);
                if (extractMetadata2 != null) {
                    maxScrollDuration = Long.parseLong(extractMetadata2);
                    this.duration = maxScrollDuration;
                }
                String extractMetadata3 = this.metadataRetriever.extractMetadata(18);
                i3 = extractMetadata3 != null ? Integer.parseInt(extractMetadata3) : 0;
                try {
                    String extractMetadata4 = this.metadataRetriever.extractMetadata(19);
                    r7 = extractMetadata4 != null ? Integer.parseInt(extractMetadata4) : 0;
                    extractMetadata = this.metadataRetriever.extractMetadata(24);
                } catch (Exception e) {
                    e = e;
                    this.metadataRetriever = null;
                    FileLog.e(e);
                    int i4 = i3;
                    i3 = r7;
                    r7 = i4;
                    if (l != null) {
                    }
                    if (j != -1) {
                    }
                    float clamp = Utilities.clamp((r7 != 0 || i3 == 0) ? 1.0f : r7 / i3, 1.3333334f, 0.5625f);
                    this.frameHeight = Math.max(1, i);
                    this.frameWidth = Math.max(1, (int) Math.ceil(i * clamp));
                    int ceil = (int) Math.ceil(((Math.max(maxScrollDuration, j3) / j3) * i2) / this.frameWidth);
                    this.count = ceil;
                    this.frameIterator = (long) (maxScrollDuration / ceil);
                    this.nextFrame = -this.frameIterator;
                    if (j != -1) {
                    }
                    load();
                    if (runnable != null) {
                    }
                }
            } catch (Exception e2) {
                e = e2;
                i3 = 0;
            }
            if (extractMetadata != null) {
                int parseInt = Integer.parseInt(extractMetadata);
                if (parseInt != 90) {
                }
                if (l != null) {
                    maxScrollDuration = l.longValue();
                    this.duration = maxScrollDuration;
                }
                if (j != -1 && j2 != -1) {
                    maxScrollDuration = j2 - j;
                }
                float clamp2 = Utilities.clamp((r7 != 0 || i3 == 0) ? 1.0f : r7 / i3, 1.3333334f, 0.5625f);
                this.frameHeight = Math.max(1, i);
                this.frameWidth = Math.max(1, (int) Math.ceil(i * clamp2));
                int ceil2 = (int) Math.ceil(((Math.max(maxScrollDuration, j3) / j3) * i2) / this.frameWidth);
                this.count = ceil2;
                this.frameIterator = (long) (maxScrollDuration / ceil2);
                this.nextFrame = -this.frameIterator;
                if (j != -1) {
                    this.nextFrame = j - this.frameIterator;
                }
                load();
                if (runnable != null) {
                    AndroidUtilities.runOnUIThread(runnable);
                    return;
                }
                return;
            }
            int i42 = i3;
            i3 = r7;
            r7 = i42;
            if (l != null) {
            }
            if (j != -1) {
                maxScrollDuration = j2 - j;
            }
            float clamp22 = Utilities.clamp((r7 != 0 || i3 == 0) ? 1.0f : r7 / i3, 1.3333334f, 0.5625f);
            this.frameHeight = Math.max(1, i);
            this.frameWidth = Math.max(1, (int) Math.ceil(i * clamp22));
            int ceil22 = (int) Math.ceil(((Math.max(maxScrollDuration, j3) / j3) * i2) / this.frameWidth);
            this.count = ceil22;
            this.frameIterator = (long) (maxScrollDuration / ceil22);
            this.nextFrame = -this.frameIterator;
            if (j != -1) {
            }
            load();
            if (runnable != null) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: receiveFrame, reason: merged with bridge method [inline-methods] */
        public void lambda$retrieveFrame$1(Bitmap bitmap) {
            if (!this.loading || this.destroyed) {
                return;
            }
            this.frames.add(new BitmapFrame(bitmap));
            this.loading = false;
            TimelineView.this.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void retrieveFrame() {
            MediaMetadataRetriever mediaMetadataRetriever = this.metadataRetriever;
            if (mediaMetadataRetriever == null) {
                return;
            }
            final Bitmap bitmap = null;
            try {
                bitmap = mediaMetadataRetriever.getFrameAtTime(this.nextFrame * 1000, 2);
                if (bitmap != null) {
                    Bitmap createBitmap = Bitmap.createBitmap(this.frameWidth, this.frameHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float max = Math.max(this.frameWidth / bitmap.getWidth(), this.frameHeight / bitmap.getHeight());
                    Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    Rect rect2 = new Rect((int) ((createBitmap.getWidth() - (bitmap.getWidth() * max)) / 2.0f), (int) ((createBitmap.getHeight() - (bitmap.getHeight() * max)) / 2.0f), (int) ((createBitmap.getWidth() + (bitmap.getWidth() * max)) / 2.0f), (int) ((createBitmap.getHeight() + (bitmap.getHeight() * max)) / 2.0f));
                    if (this.isRound) {
                        if (this.clipPath == null) {
                            this.clipPath = new Path();
                        }
                        this.clipPath.rewind();
                        this.clipPath.addCircle(this.frameWidth / 2.0f, this.frameHeight / 2.0f, Math.min(this.frameWidth, this.frameHeight) / 2.0f, Path.Direction.CW);
                        canvas.clipPath(this.clipPath);
                    }
                    canvas.drawBitmap(bitmap, rect, rect2, this.bitmapPaint);
                    bitmap.recycle();
                    bitmap = createBitmap;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TimelineView.VideoThumbsLoader.this.lambda$retrieveFrame$1(bitmap);
                }
            });
        }

        public void destroy() {
            this.destroyed = true;
            Utilities.themeQueue.cancelRunnable(new TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda0(this));
            Iterator it = this.frames.iterator();
            while (it.hasNext()) {
                Bitmap bitmap = ((BitmapFrame) it.next()).bitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
            this.frames.clear();
            MediaMetadataRetriever mediaMetadataRetriever = this.metadataRetriever;
            if (mediaMetadataRetriever != null) {
                try {
                    mediaMetadataRetriever.release();
                } catch (Exception e) {
                    this.metadataRetriever = null;
                    FileLog.e(e);
                }
            }
        }

        public long getDuration() {
            return this.duration;
        }

        public int getFrameWidth() {
            return this.frameWidth;
        }

        public void load() {
            if (this.loading || this.metadataRetriever == null || this.frames.size() >= this.count) {
                return;
            }
            this.loading = true;
            this.nextFrame += this.frameIterator;
            Utilities.themeQueue.cancelRunnable(new TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda0(this));
            Utilities.themeQueue.postRunnable(new TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda0(this));
        }
    }

    static class WaveformPath extends Path {
        private float lastAudioHeight;
        private float lastAudioSelected;
        private float lastBottom;
        private float lastLeft;
        private float lastMaxBar;
        private float lastRight;
        private long lastScrollDuration;
        private float lastStart;
        private ArrayList lastWaveformCounts;
        private ArrayList lastWaveformLoaded;
        private final int ph = AndroidUtilities.dp(10.0f);
        private final float[] waveformRadii;

        WaveformPath() {
            this.waveformRadii = new float[]{r1, r1, r1, r1, 0.0f, 0.0f, 0.0f, 0.0f};
            float dp = AndroidUtilities.dp(2.0f);
        }

        private boolean eqCount(ArrayList arrayList, ArrayList arrayList2) {
            if (arrayList == null && arrayList2 == null) {
                return true;
            }
            if (arrayList == null || arrayList2 == null || arrayList.size() != arrayList2.size()) {
                return false;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                if (((Integer) arrayList.get(i)).intValue() != (arrayList2.get(i) == null ? 0 : ((AudioWaveformLoader) arrayList2.get(i)).getCount())) {
                    return false;
                }
            }
            return true;
        }

        private boolean eqLoadedCounts(ArrayList arrayList, ArrayList arrayList2) {
            if (arrayList == null && arrayList2 == null) {
                return true;
            }
            if (arrayList == null || arrayList2 == null || arrayList.size() != arrayList2.size()) {
                return false;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                if (((Float) arrayList.get(i)).floatValue() != (arrayList2.get(i) == null ? 0.0f : ((AudioWaveformLoader) arrayList2.get(i)).animatedLoaded.set(((AudioWaveformLoader) arrayList2.get(i)).getLoadedCount()))) {
                    return false;
                }
            }
            return true;
        }

        public static int getMaxBar(ArrayList arrayList) {
            if (arrayList == null) {
                return 0;
            }
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (arrayList.get(i2) != null) {
                    i += ((AudioWaveformLoader) arrayList.get(i2)).getMaxBar();
                }
            }
            return i;
        }

        private void layout(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, AudioWaveformLoader audioWaveformLoader) {
            rewind();
            float round = Math.round(AndroidUtilities.dpf2(3.3333f));
            int count = audioWaveformLoader.getCount();
            int min = Math.min(count - 1, (int) Math.ceil(((f3 + this.ph) - f) / round));
            for (int max = Math.max(0, (int) (((f2 - this.ph) - f) / round)); max <= min; max++) {
                float f9 = max;
                float dp = (f9 * round) + f + AndroidUtilities.dp(2.0f);
                float bar = f5 <= 0.0f ? 0.0f : (audioWaveformLoader.getBar(max) / f5) * f6 * 0.6f;
                if (f9 < f8 && max + 1 > f8) {
                    bar *= f8 - f9;
                } else if (f9 > f8) {
                    bar = 0.0f;
                }
                if (dp < f2 || dp > f3) {
                    bar *= f4;
                    if (bar <= 0.0f) {
                    }
                }
                float max2 = Math.max(bar, AndroidUtilities.lerp(AndroidUtilities.dpf2(0.66f), AndroidUtilities.dpf2(1.5f), f4));
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(dp, AndroidUtilities.lerp(f7 - max2, f7 - ((f6 + max2) / 2.0f), f4), AndroidUtilities.dpf2(1.66f) + dp, AndroidUtilities.lerp(f7, f7 - ((f6 - max2) / 2.0f), f4));
                addRoundRect(rectF, this.waveformRadii, Path.Direction.CW);
            }
        }

        private void layout(float f, float f2, float f3, float f4, float f5, float f6, float f7, ArrayList arrayList, ArrayList arrayList2) {
            rewind();
            float round = Math.round(AndroidUtilities.dpf2(3.3333f));
            int i = 0;
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                if (arrayList2.get(i2) != null) {
                    i = Math.max(i, ((AudioWaveformLoader) arrayList2.get(i2)).getCount());
                }
            }
            int max = Math.max(0, (int) (((f2 - this.ph) - f) / round));
            int min = Math.min(i - 1, (int) Math.ceil(((f3 + this.ph) - f) / round));
            while (max <= min) {
                float f8 = max;
                float dp = f + (f8 * round) + AndroidUtilities.dp(2.0f);
                int i3 = 0;
                for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                    short bar = (arrayList2.get(i4) == null || max >= ((AudioWaveformLoader) arrayList2.get(i4)).getCount()) ? (short) 0 : ((AudioWaveformLoader) arrayList2.get(i4)).getBar(max);
                    if (f8 < ((Float) arrayList.get(i4)).floatValue() && max + 1 > ((Float) arrayList.get(i4)).floatValue()) {
                        bar = (short) (bar * (((Float) arrayList.get(i4)).floatValue() - f8));
                    } else if (f8 > ((Float) arrayList.get(i4)).floatValue()) {
                        bar = 0;
                    }
                    i3 += bar;
                }
                float f9 = f5 <= 0.0f ? 0.0f : (i3 / f5) * f6 * 0.6f;
                if (dp < f2 || dp > f3) {
                    f9 *= f4;
                    if (f9 <= 0.0f) {
                        max++;
                    }
                }
                float max2 = Math.max(f9, AndroidUtilities.lerp(AndroidUtilities.dpf2(0.66f), AndroidUtilities.dpf2(1.5f), f4));
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(dp, AndroidUtilities.lerp(f7 - max2, f7 - ((f6 + max2) / 2.0f), f4), AndroidUtilities.dpf2(1.66f) + dp, AndroidUtilities.lerp(f7, f7 - ((f6 - max2) / 2.0f), f4));
                addRoundRect(rectF, this.waveformRadii, Path.Direction.CW);
                max++;
            }
        }

        public void check(float f, float f2, float f3, float f4, float f5, float f6, float f7, ArrayList arrayList) {
            if (arrayList == null || arrayList.isEmpty()) {
                rewind();
                return;
            }
            if (Math.abs(this.lastAudioHeight - f5) > 1.0f || Math.abs(this.lastMaxBar - f6) > 0.01f || Math.abs(this.lastAudioSelected - f4) > 0.1f || Math.abs(this.lastBottom - f7) > 1.0f || Math.abs(this.lastStart - f) > 1.0f || Math.abs(this.lastLeft - f2) > 1.0f || Math.abs(this.lastRight - f3) > 1.0f || eqCount(this.lastWaveformCounts, arrayList) || eqLoadedCounts(this.lastWaveformLoaded, arrayList)) {
                ArrayList arrayList2 = this.lastWaveformCounts;
                if (arrayList2 == null) {
                    this.lastWaveformCounts = new ArrayList();
                } else {
                    arrayList2.clear();
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    this.lastWaveformCounts.add(Integer.valueOf(arrayList.get(i) == null ? 0 : ((AudioWaveformLoader) arrayList.get(i)).getCount()));
                }
                ArrayList arrayList3 = this.lastWaveformLoaded;
                if (arrayList3 == null) {
                    this.lastWaveformLoaded = new ArrayList();
                } else {
                    arrayList3.clear();
                }
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    this.lastWaveformLoaded.add(Float.valueOf(arrayList.get(i2) == null ? 0.0f : ((AudioWaveformLoader) arrayList.get(i2)).animatedLoaded.set(((AudioWaveformLoader) arrayList.get(i2)).getLoadedCount())));
                }
                this.lastStart = f;
                this.lastLeft = f2;
                this.lastRight = f3;
                this.lastAudioSelected = f4;
                this.lastMaxBar = f6;
                this.lastAudioHeight = f5;
                this.lastBottom = f7;
                layout(f, f2, f3, f4, f6, f5, f7, this.lastWaveformLoaded, arrayList);
            }
        }

        public void check(float f, float f2, float f3, float f4, long j, float f5, float f6, float f7, AudioWaveformLoader audioWaveformLoader) {
            ArrayList arrayList;
            if (audioWaveformLoader == null) {
                rewind();
                return;
            }
            float f8 = audioWaveformLoader.animatedLoaded.set(audioWaveformLoader.getLoadedCount());
            if (this.lastScrollDuration == j && Math.abs(this.lastAudioHeight - f5) <= 1.0f && Math.abs(this.lastMaxBar - f6) <= 0.01f && Math.abs(this.lastAudioSelected - f4) <= 0.1f && Math.abs(this.lastBottom - f7) <= 1.0f && Math.abs(this.lastStart - f) <= 1.0f && Math.abs(this.lastLeft - f2) <= 1.0f && Math.abs(this.lastRight - f3) <= 1.0f && (arrayList = this.lastWaveformCounts) != null && arrayList.size() == 1) {
                ArrayList arrayList2 = this.lastWaveformLoaded;
                if (Math.abs(((arrayList2 == null || arrayList2.isEmpty()) ? 0.0f : ((Float) this.lastWaveformLoaded.get(0)).floatValue()) - f8) <= 0.01f) {
                    return;
                }
            }
            ArrayList arrayList3 = this.lastWaveformCounts;
            if (arrayList3 == null) {
                this.lastWaveformCounts = new ArrayList();
            } else {
                arrayList3.clear();
            }
            this.lastWaveformCounts.add(Integer.valueOf(audioWaveformLoader.getCount()));
            ArrayList arrayList4 = this.lastWaveformLoaded;
            if (arrayList4 == null) {
                this.lastWaveformLoaded = new ArrayList();
            } else {
                arrayList4.clear();
            }
            this.lastWaveformLoaded.add(Float.valueOf(f8));
            this.lastStart = f;
            this.lastLeft = f2;
            this.lastRight = f3;
            this.lastAudioSelected = f4;
            this.lastMaxBar = f6;
            this.lastAudioHeight = f5;
            this.lastBottom = f7;
            layout(f, f2, f3, f4, f6, f5, f7, audioWaveformLoader.animatedLoaded.set(audioWaveformLoader.getLoadedCount()), audioWaveformLoader);
        }
    }

    public TimelineView(Context context, final ViewGroup viewGroup, final View view, final Theme.ResourcesProvider resourcesProvider, final BlurringShader.BlurManager blurManager) {
        super(context);
        this.collageSelected = 0;
        this.collageWaveforms = new ArrayList();
        this.collageTracks = new ArrayList();
        this.collageFramePaint = new Paint(3);
        this.collageClipPath = new Path();
        this.selectedCollageClipPath = new Path();
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.roundT = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.roundSelectedT = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.audioT = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.audioSelectedT = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.waveformMax = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.timelineWaveformLoaded = new AnimatedFloat(this, 0L, 600L, cubicBezierInterpolator);
        this.timelineWaveformMax = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.openT = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.open = true;
        this.timelineBounds = new RectF();
        this.timelineClipPath = new Path();
        this.timelineWaveformPath = new WaveformPath();
        this.videoBounds = new RectF();
        this.videoFramePaint = new Paint(3);
        this.videoClipPath = new Path();
        this.selectedVideoClipPath = new Path();
        this.roundBounds = new RectF();
        this.roundClipPath = new Path();
        Paint paint = new Paint(1);
        this.regionPaint = paint;
        Paint paint2 = new Paint(1);
        this.regionCutPaint = paint2;
        Paint paint3 = new Paint(1);
        this.regionHandlePaint = paint3;
        Paint paint4 = new Paint(1);
        this.progressShadowPaint = paint4;
        Paint paint5 = new Paint(1);
        this.progressWhitePaint = paint5;
        this.audioBounds = new RectF();
        this.audioClipPath = new Path();
        Paint paint6 = new Paint(1);
        this.waveformPaint = paint6;
        this.waveformPath = new WaveformPath();
        Paint paint7 = new Paint(1);
        this.audioDotPaint = paint7;
        TextPaint textPaint = new TextPaint(1);
        this.audioAuthorPaint = textPaint;
        TextPaint textPaint2 = new TextPaint(1);
        this.audioTitlePaint = textPaint2;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 16.0f, 0.0f, new int[]{16777215, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        this.ellipsizeGradient = linearGradient;
        this.ellipsizeMatrix = new Matrix();
        Paint paint8 = new Paint(1);
        this.ellipsizePaint = paint8;
        this.scroller = new Scroller(getContext());
        this.coverStart = -1L;
        this.coverEnd = -1L;
        this.loopProgress = new AnimatedFloat(0.0f, this, 0L, 340L, cubicBezierInterpolator);
        this.loopProgressFrom = -1L;
        this.pressHandle = -1;
        this.pressHandleCollageIndex = -1;
        this.pressType = -1;
        this.pressCollageIndex = -1;
        this.dragSpeed = 1.0f;
        this.scrollingVideo = true;
        this.scrollingCollage = -1;
        this.scrolling = false;
        this.selectedVideoRadii = new float[8];
        this.previewContainer = view;
        this.resourcesProvider = resourcesProvider;
        paint7.setColor(ConnectionsManager.DEFAULT_DATACENTER_ID);
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        textPaint.setTypeface(AndroidUtilities.bold());
        textPaint.setColor(-1);
        textPaint2.setTextSize(AndroidUtilities.dp(12.0f));
        textPaint2.setColor(-1);
        paint6.setColor(1090519039);
        paint8.setShader(linearGradient);
        paint8.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setColor(-1);
        paint.setShadowLayer(AndroidUtilities.dp(1.0f), 0.0f, AndroidUtilities.dp(1.0f), 436207616);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint3.setColor(-16777216);
        paint5.setColor(-1);
        paint4.setColor(637534208);
        this.timelineText = new Text(LocaleController.getString(R.string.StoryTimeline), 12.0f, AndroidUtilities.bold());
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.timeline).mutate();
        this.timelineIcon = mutate;
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
        mutate.setColorFilter(new PorterDuffColorFilter(-1, mode));
        Drawable mutate2 = getContext().getResources().getDrawable(R.drawable.filled_widget_music).mutate();
        this.audioIcon = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(-1, mode));
        this.blurManager = blurManager;
        this.backgroundBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 0);
        this.audioBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 3);
        this.audioWaveformBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 4);
        this.onLongPress = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$new$6(viewGroup, resourcesProvider, blurManager, view);
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0143 A[LOOP:0: B:38:0x0143->B:42:0x01ff, LOOP_START, PHI: r6
      0x0143: PHI (r6v37 int) = (r6v11 int), (r6v38 int) binds: [B:37:0x0141, B:42:0x01ff] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0203  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int detectHandle(MotionEvent motionEvent) {
        boolean z;
        float f;
        float f2;
        float f3;
        float f4;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        long min = Math.min(getBaseDuration(), getMaxScrollDuration());
        float clamp = Utilities.clamp(this.progress, getBaseDuration(), 0L);
        Track track = this.collageMain;
        float f5 = min;
        float f6 = this.px + this.ph + (this.sw * (((clamp + (track != null ? track.offset + (track.left * track.duration) : this.videoTrack == null ? this.audioOffset : 0L)) - this.scroll) / f5));
        if (!this.isCover && x >= f6 - AndroidUtilities.dp(12.0f) && x <= f6 + AndroidUtilities.dp(12.0f)) {
            return 0;
        }
        boolean z2 = this.videoTrack != null && y > (((float) (this.h - this.py)) - getVideoHeight()) - ((float) AndroidUtilities.dp(2.0f));
        boolean z3 = !this.collageTracks.isEmpty() && y > ((((((float) (this.h - this.py)) - getVideoHeight()) - ((float) AndroidUtilities.dp(4.0f))) - getCollageHeight()) - ((float) AndroidUtilities.dp(4.0f))) - ((float) AndroidUtilities.dp(2.0f)) && y < (((float) (this.h - this.py)) - getVideoHeight()) - ((float) AndroidUtilities.dp(2.0f));
        if (this.hasRound) {
            if (y > (((((((this.h - this.py) - getVideoHeight()) - AndroidUtilities.dp(4.0f)) - getCollageHeight()) - AndroidUtilities.dp(this.collageTracks.isEmpty() ? 0.0f : 4.0f)) - getRoundHeight()) - AndroidUtilities.dp(4.0f)) - AndroidUtilities.dp(2.0f)) {
                if (y < ((((this.h - this.py) - getVideoHeight()) - AndroidUtilities.dp(2.0f)) - getCollageHeight()) - AndroidUtilities.dp(this.collageTracks.isEmpty() ? 0.0f : 4.0f)) {
                    z = true;
                    if (!z3) {
                        for (int i = 0; i < this.collageTracks.size(); i++) {
                            Track track2 = (Track) this.collageTracks.get(i);
                            RectF rectF = AndroidUtilities.rectTmp;
                            rectF.set(track2.bounds);
                            rectF.inset(-AndroidUtilities.dp(2.0f), -AndroidUtilities.dp(2.0f));
                            if (rectF.contains(x, y)) {
                                float f7 = this.px + this.ph;
                                float f8 = track2.offset;
                                float f9 = this.sw;
                                float f10 = ((f8 / f5) * f9) + f7;
                                float f11 = track2.left;
                                float f12 = track2.duration;
                                float f13 = ((((f11 * f12) + f8) / f5) * f9) + f7;
                                float f14 = (((f8 + (track2.right * f12)) / f5) * f9) + f7;
                                float f15 = f7 + (((r4 + r11) / f5) * f9);
                                this.pressHandleCollageIndex = i;
                                if (x >= f13 - AndroidUtilities.dp(15.0f)) {
                                    f4 = 5.0f;
                                    if (x <= AndroidUtilities.dp(5.0f) + f13) {
                                        return 13;
                                    }
                                } else {
                                    f4 = 5.0f;
                                }
                                if (x >= f14 - AndroidUtilities.dp(f4) && x <= AndroidUtilities.dp(15.0f) + f14) {
                                    return 14;
                                }
                                if (x < f13 || x > f14 || (track2.left <= 0.01f && track2.right >= 0.99f)) {
                                    return (x < f10 || x > f15) ? -1 : 16;
                                }
                                return 15;
                            }
                        }
                    } else if (z2) {
                        float f16 = this.px + this.ph;
                        Track track3 = this.videoTrack;
                        float f17 = track3.left;
                        float f18 = track3.duration;
                        float f19 = this.scroll;
                        float f20 = this.sw;
                        float f21 = ((((f17 * f18) - f19) / f5) * f20) + f16;
                        float f22 = f16 + ((((track3.right * f18) - f19) / f5) * f20);
                        if (this.isCover) {
                            return (getBaseDuration() < getMaxScrollDuration() || (x >= f21 - ((float) AndroidUtilities.dp(15.0f)) && x <= f22 + ((float) AndroidUtilities.dp(15.0f)))) ? 4 : 1;
                        }
                        if (x >= f21 - AndroidUtilities.dp(15.0f)) {
                            f3 = 5.0f;
                            if (x <= AndroidUtilities.dp(5.0f) + f21) {
                                return 2;
                            }
                        } else {
                            f3 = 5.0f;
                        }
                        if (x >= f22 - AndroidUtilities.dp(f3) && x <= AndroidUtilities.dp(15.0f) + f22) {
                            return 3;
                        }
                        if (x >= f21 && x <= f22) {
                            Track track4 = this.videoTrack;
                            if (track4.left > 0.01f || track4.right < 0.99f) {
                                return 4;
                            }
                        }
                    } else if (z) {
                        float f23 = this.px + this.ph;
                        float f24 = this.roundOffset;
                        float f25 = this.roundLeft;
                        float f26 = this.roundDuration;
                        float f27 = this.scroll;
                        float f28 = this.sw;
                        float f29 = (((((f25 * f26) + f24) - f27) / f5) * f28) + f23;
                        float f30 = f23 + ((((f24 + (this.roundRight * f26)) - f27) / f5) * f28);
                        if (this.roundSelected || this.videoTrack == null) {
                            if (x >= f29 - AndroidUtilities.dp(15.0f)) {
                                f2 = 5.0f;
                                if (x <= AndroidUtilities.dp(5.0f) + f29) {
                                    return 10;
                                }
                            } else {
                                f2 = 5.0f;
                            }
                            if (x >= f30 - AndroidUtilities.dp(f2) && x <= AndroidUtilities.dp(15.0f) + f30) {
                                return 11;
                            }
                            if (x >= f29 && x <= f30) {
                                return this.videoTrack == null ? 12 : 9;
                            }
                            float f31 = this.px + this.ph;
                            long j = this.roundOffset;
                            long j2 = this.scroll;
                            float f32 = this.sw;
                            float f33 = (((j - j2) / f5) * f32) + f31;
                            f30 = f31 + ((((j + this.roundDuration) - j2) / f5) * f32);
                            f29 = f33;
                        }
                        if (x >= f29 && x <= f30) {
                            return 9;
                        }
                    } else if (this.hasAudio) {
                        float f34 = this.px + this.ph;
                        float f35 = this.audioOffset;
                        float f36 = this.audioLeft;
                        float f37 = this.audioDuration;
                        float f38 = this.scroll;
                        float f39 = this.sw;
                        float f40 = (((((f36 * f37) + f35) - f38) / f5) * f39) + f34;
                        float f41 = f34 + ((((f35 + (this.audioRight * f37)) - f38) / f5) * f39);
                        if (this.audioSelected || (this.videoTrack == null && !this.hasRound)) {
                            if (x >= f40 - AndroidUtilities.dp(15.0f)) {
                                f = 5.0f;
                                if (x <= AndroidUtilities.dp(5.0f) + f40) {
                                    return 6;
                                }
                            } else {
                                f = 5.0f;
                            }
                            if (x >= f41 - AndroidUtilities.dp(f) && x <= AndroidUtilities.dp(15.0f) + f41) {
                                return 7;
                            }
                            if (x >= f40 && x <= f41) {
                                return this.videoTrack == null ? 8 : 5;
                            }
                            float f42 = this.px + this.ph;
                            long j3 = this.audioOffset;
                            long j4 = this.scroll;
                            float f43 = this.sw;
                            float f44 = (((j3 - j4) / f5) * f43) + f42;
                            f41 = f42 + ((((j3 + this.audioDuration) - j4) / f5) * f43);
                            f40 = f44;
                        }
                        if (x >= f40 && x <= f41) {
                            return 5;
                        }
                    }
                    Track track5 = this.videoTrack;
                    return track5 != null ? -1 : -1;
                }
            }
        }
        z = false;
        if (!z3) {
        }
        Track track52 = this.videoTrack;
        return track52 != null ? -1 : -1;
    }

    private void drawProgress(Canvas canvas, float f, float f2, long j, float f3) {
        float f4;
        if (this.isCover) {
            return;
        }
        long min = Math.min(getBaseDuration(), getMaxScrollDuration());
        float clamp = Utilities.clamp(j, getBaseDuration(), 0L);
        Track track = this.collageMain;
        if (track != null) {
            f4 = track.offset + (track.left * track.duration);
        } else {
            f4 = this.videoTrack == null ? this.audioOffset : 0L;
        }
        float f5 = this.px + this.ph + (this.sw * (((clamp + f4) - this.scroll) / min));
        float f6 = (((f2 - f) / 2.0f) / 2.0f) * (1.0f - f3);
        float f7 = f + f6;
        float f8 = f2 - f6;
        this.progressShadowPaint.setAlpha((int) (38.0f * f3));
        this.progressWhitePaint.setAlpha((int) (f3 * 255.0f));
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(f5 - AndroidUtilities.dpf2(1.5f), f7, AndroidUtilities.dpf2(1.5f) + f5, f8);
        rectF.inset(-AndroidUtilities.dpf2(0.66f), -AndroidUtilities.dpf2(0.66f));
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.progressShadowPaint);
        rectF.set(f5 - AndroidUtilities.dpf2(1.5f), f7, f5 + AndroidUtilities.dpf2(1.5f), f8);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.progressWhitePaint);
    }

    private void drawRegion(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, float f5) {
        if (f5 <= 0.0f) {
            return;
        }
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(f3 - AndroidUtilities.dp(10.0f), f, f4 + AndroidUtilities.dp(10.0f), f2);
        canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, NotificationCenter.liveLocationsChanged, 31);
        int i = (int) (255.0f * f5);
        this.regionPaint.setAlpha(i);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.regionPaint);
        rectF.inset(AndroidUtilities.dp(this.isCover ? 2.5f : 10.0f), AndroidUtilities.dp(2.0f));
        if (this.isCover) {
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), this.regionCutPaint);
        } else {
            canvas.drawRect(rectF, this.regionCutPaint);
        }
        float dp = AndroidUtilities.dp(2.0f);
        float dp2 = AndroidUtilities.dp(10.0f);
        Paint paint2 = paint != null ? paint : this.regionHandlePaint;
        this.regionHandlePaint.setAlpha(NotificationCenter.liveLocationsChanged);
        paint2.setAlpha(i);
        float f6 = f + f2;
        float f7 = (f6 - dp2) / 2.0f;
        float f8 = (f6 + dp2) / 2.0f;
        rectF.set(f3 - ((AndroidUtilities.dp(this.isCover ? 2.0f : 10.0f) - dp) / 2.0f), f7, f3 - ((AndroidUtilities.dp(this.isCover ? 2.0f : 10.0f) + dp) / 2.0f), f8);
        if (!this.isCover) {
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), paint2);
            if (paint != null && !this.isCover) {
                this.regionHandlePaint.setAlpha((int) (f5 * 48.0f));
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.regionHandlePaint);
            }
        }
        rectF.set(f4 + ((AndroidUtilities.dp(this.isCover ? 2.5f : 10.0f) - dp) / 2.0f), f7, f4 + ((AndroidUtilities.dp(this.isCover ? 2.5f : 10.0f) + dp) / 2.0f), f8);
        if (!this.isCover) {
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), paint2);
            if (paint != null) {
                this.regionHandlePaint.setAlpha((int) (f5 * 48.0f));
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.regionHandlePaint);
            }
        }
        canvas.restore();
    }

    private float getAudioHeight() {
        return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.audioSelectedT.set(this.audioSelected));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getBaseDuration() {
        Track track = this.videoTrack;
        return Math.max(1L, (track == null && (track = this.collageMain) == null) ? this.hasRound ? this.roundDuration : this.audioDuration : track.duration);
    }

    private float getCollageHeight() {
        if (this.collageTracks.isEmpty()) {
            return 0.0f;
        }
        float f = 0.0f;
        for (int i = 0; i < this.collageTracks.size(); i++) {
            if (f > 0.0f) {
                f += AndroidUtilities.dp(4.0f);
            }
            f += AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), ((Track) this.collageTracks.get(i)).selectedT.get());
        }
        return f;
    }

    private float getRoundHeight() {
        if (!this.hasRound) {
            return 0.0f;
        }
        return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.roundSelectedT.set(this.roundSelected));
    }

    private float getVideoHeight() {
        if (this.videoTrack == null) {
            return 0.0f;
        }
        return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), r0.selectedT.get());
    }

    public static int heightDp() {
        return 388;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Float f) {
        this.audioVolume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onAudioVolumeChange(f.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onAudioRemove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Float f) {
        this.roundVolume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onRoundVolumeChange(f.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onRoundRemove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(Float f) {
        this.videoTrack.volume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onVideoVolumeChange(f.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(Track track, Float f) {
        track.volume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onVideoVolumeChange(track.index, f.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(ViewGroup viewGroup, Theme.ResourcesProvider resourcesProvider, BlurringShader.BlurManager blurManager, View view) {
        int i;
        ItemOptions forceTop;
        float dp;
        RectF rectF;
        int i2 = this.pressType;
        if (i2 == 2 && this.hasAudio) {
            SliderView onValueChange = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.audioVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda4
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    TimelineView.this.lambda$new$0((Float) obj);
                }
            });
            long min = Math.min(getBaseDuration(), getMaxScrollDuration());
            int i3 = this.w;
            int i4 = this.px;
            int i5 = this.ph;
            float min2 = Math.min((i3 - i4) - i5, i4 + i5 + ((((this.audioOffset - this.scroll) + (AndroidUtilities.lerp(this.audioRight, 1.0f, this.audioSelectedT.get()) * this.audioDuration)) / min) * this.sw));
            forceTop = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange).addSpaceGap().add(R.drawable.msg_delete, LocaleController.getString(R.string.StoryAudioRemove), new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    TimelineView.this.lambda$new$1();
                }
            }).setGravity(5).forceTop(true);
            dp = (-(this.w - min2)) + AndroidUtilities.dp(18.0f);
            rectF = this.audioBounds;
        } else if (i2 == 1 && this.hasRound) {
            SliderView onValueChange2 = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.roundVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    TimelineView.this.lambda$new$2((Float) obj);
                }
            });
            long min3 = Math.min(getBaseDuration(), getMaxScrollDuration());
            int i6 = this.w;
            int i7 = this.px;
            int i8 = this.ph;
            float min4 = Math.min((i6 - i7) - i8, i7 + i8 + ((((this.roundOffset - this.scroll) + (AndroidUtilities.lerp(this.roundRight, 1.0f, this.roundSelectedT.get()) * this.roundDuration)) / min3) * this.sw));
            forceTop = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange2).addSpaceGap().add(R.drawable.msg_delete, LocaleController.getString(R.string.StoryRoundRemove), new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    TimelineView.this.lambda$new$3();
                }
            }).setGravity(5).forceTop(true);
            dp = (-(this.w - min4)) + AndroidUtilities.dp(18.0f);
            rectF = this.roundBounds;
        } else if (i2 == 0 && this.videoTrack != null) {
            forceTop = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.videoTrack.volume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda8
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    TimelineView.this.lambda$new$4((Float) obj);
                }
            })).setGravity(5).forceTop(true);
            dp = AndroidUtilities.dp(18.0f);
            rectF = this.videoBounds;
        } else {
            if (i2 != 3 || (i = this.pressCollageIndex) < 0 || i >= this.collageTracks.size()) {
                return;
            }
            final Track track = (Track) this.collageTracks.get(this.pressCollageIndex);
            forceTop = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(track.volume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda9
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    TimelineView.this.lambda$new$5(track, (Float) obj);
                }
            })).setGravity(5).forceTop(true);
            dp = AndroidUtilities.dp(18.0f);
            rectF = track.bounds;
        }
        forceTop.translate(dp, rectF.top).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
        try {
            performHapticFeedback(0, 1);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setProgressAt$9(long j) {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onProgressChange(j, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRoundThumbs$8() {
        VideoThumbsLoader videoThumbsLoader = this.roundThumbs;
        if (videoThumbsLoader == null || videoThumbsLoader.getDuration() <= 0) {
            return;
        }
        this.roundDuration = this.roundThumbs.getDuration();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortCollage$7(Track track, Track track2) {
        return (int) (track2.duration - track.duration);
    }

    private long minAudioSelect() {
        return (long) Math.max(1000.0f, Math.min(getBaseDuration(), 59000L) * 0.15f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0131, code lost:
    
        r1.onAudioLeftChange(r22.audioLeft);
        r22.delegate.onAudioRightChange(r22.audioRight);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00e7, code lost:
    
        if (r1 != null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x004b, code lost:
    
        if (r1 != null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x012f, code lost:
    
        if (r1 != null) goto L39;
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void moveAudioOffset(float f) {
        float f2;
        long j;
        float f3;
        long j2;
        long j3;
        long j4;
        TimelineDelegate timelineDelegate;
        long j5;
        long j6;
        long clamp;
        long j7;
        long j8;
        long j9;
        TimelineDelegate timelineDelegate2;
        long j10;
        TimelineDelegate timelineDelegate3;
        long j11;
        long j12;
        float f4;
        Track track = this.videoTrack;
        if (track == null && !this.hasRound) {
            long j13 = this.audioOffset;
            long clamp2 = Utilities.clamp(j13 + ((long) f), 0L, -(this.audioDuration - Math.min(getBaseDuration(), getMaxScrollDuration())));
            this.audioOffset = clamp2;
            float f5 = clamp2 - j13;
            this.audioLeft = Utilities.clamp(this.audioLeft - (f5 / this.audioDuration), 1.0f, 0.0f);
            this.audioRight = Utilities.clamp(this.audioRight - (f5 / this.audioDuration), 1.0f, 0.0f);
            timelineDelegate = this.delegate;
        } else if (this.audioSelected) {
            if (track != null) {
                f2 = track.left;
                j = track.duration;
            } else {
                f2 = this.roundLeft;
                j = this.roundDuration;
            }
            float f6 = f2 * j;
            if (track != null) {
                f3 = track.right;
                j2 = track.duration;
            } else {
                f3 = this.roundRight;
                j2 = this.roundDuration;
            }
            float f7 = f3 * j2;
            float f8 = track != null ? (track.right - track.left) * track.duration : this.roundDuration * (this.roundRight - this.roundLeft);
            float f9 = this.audioRight;
            float f10 = this.audioDuration;
            long j14 = (long) (f7 - (f9 * f10));
            float f11 = this.audioLeft;
            long j15 = (long) (f6 - (f11 * f10));
            float min = Math.min(f9 - f11, f8 / f10);
            long j16 = this.audioOffset;
            long j17 = (long) f;
            long j18 = j16 + j17;
            if (j18 > j14) {
                float clamp3 = Utilities.clamp(((f7 - j16) - j17) / this.audioDuration, 1.0f, min);
                this.audioRight = clamp3;
                float clamp4 = Utilities.clamp(clamp3 - min, 1.0f, 0.0f);
                this.audioLeft = clamp4;
                float f12 = this.audioRight;
                float f13 = this.audioDuration;
                long j19 = (long) (f7 - (f12 * f13));
                long j20 = (long) (f6 - (clamp4 * f13));
                if (j19 < j20) {
                    j6 = j20;
                    j5 = j19;
                } else {
                    j5 = j20;
                    j6 = j19;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j17, j6, j5);
                timelineDelegate = this.delegate;
            } else if (j18 < j15) {
                float clamp5 = Utilities.clamp(((f6 - j16) - j17) / this.audioDuration, 1.0f - min, 0.0f);
                this.audioLeft = clamp5;
                float clamp6 = Utilities.clamp(clamp5 + min, 1.0f, 0.0f);
                this.audioRight = clamp6;
                float f14 = this.audioDuration;
                long j21 = (long) (f7 - (clamp6 * f14));
                long j22 = (long) (f6 - (this.audioLeft * f14));
                if (j21 < j22) {
                    j4 = j22;
                    j3 = j21;
                } else {
                    j3 = j22;
                    j4 = j21;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j17, j4, j3);
                timelineDelegate = this.delegate;
            } else {
                this.audioOffset = j18;
            }
        } else {
            long j23 = this.audioOffset + ((long) f);
            float baseDuration = getBaseDuration();
            float f15 = this.audioDuration;
            this.audioOffset = Utilities.clamp(j23, (long) (baseDuration - (this.audioRight * f15)), (long) ((-this.audioLeft) * f15));
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onAudioOffsetChange(this.audioOffset + ((long) (this.audioLeft * this.audioDuration)));
        }
        boolean z = this.dragged;
        if (!z && (timelineDelegate3 = this.delegate) != null) {
            timelineDelegate3.onProgressDragChange(true);
            Track track2 = this.videoTrack;
            if (track2 != null) {
                j11 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f16 = track2.right;
                float f17 = track2.duration;
                j12 = (long) (f16 * f17);
                f4 = track2.left * f17;
            } else if (this.hasRound) {
                j11 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f18 = this.roundRight;
                float f19 = this.roundDuration;
                j12 = (long) (f18 * f19);
                f4 = this.roundLeft * f19;
            } else {
                float f20 = this.audioLeft;
                long j24 = this.audioDuration;
                j10 = Utilities.clamp((long) (f20 * j24), j24, 0L);
                if (this.videoTrack != null && Math.abs(this.progress - j10) > 400) {
                    this.loopProgressFrom = this.progress;
                    this.loopProgress.set(1.0f, true);
                }
                timelineDelegate2 = this.delegate;
                this.progress = j10;
            }
            j10 = Utilities.clamp(j11, j12, (long) f4);
            if (this.videoTrack != null) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            timelineDelegate2 = this.delegate;
            this.progress = j10;
        } else {
            if (!z && !this.scrolling) {
                return;
            }
            Track track3 = this.videoTrack;
            if (track3 != null) {
                j7 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f21 = track3.right;
                float f22 = track3.duration;
                j8 = (long) (f21 * f22);
                j9 = (long) (track3.left * f22);
            } else if (!this.hasRound || track3 == null) {
                float f23 = this.audioLeft;
                long j25 = this.audioDuration;
                clamp = Utilities.clamp((long) (f23 * j25), j25, 0L);
                this.progress = clamp;
                timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    return;
                } else {
                    j10 = this.progress;
                }
            } else {
                j7 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f24 = this.roundRight;
                float f25 = track3.duration;
                j8 = (long) (f24 * f25);
                j9 = (long) (this.roundLeft * f25);
            }
            clamp = Utilities.clamp(j7, j8, j9);
            this.progress = clamp;
            timelineDelegate2 = this.delegate;
            if (timelineDelegate2 != null) {
            }
        }
        timelineDelegate2.onProgressChange(j10, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x009f, code lost:
    
        if (r2 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00fd, code lost:
    
        r2.onVideoLeftChange(r22.index, r22.left);
        r21.delegate.onVideoRightChange(r22.index, r22.right);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00fb, code lost:
    
        if (r2 != null) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void moveCollageOffset(Track track, float f) {
        long clamp;
        TimelineDelegate timelineDelegate;
        long j;
        TimelineDelegate timelineDelegate2;
        long j2;
        long j3;
        TimelineDelegate timelineDelegate3;
        long j4;
        long j5;
        if (track == null) {
            return;
        }
        Track track2 = this.collageMain;
        if (track2 != track && track2 != null) {
            if (this.collageSelected == this.collageTracks.indexOf(track)) {
                Track track3 = this.collageMain;
                float f2 = track3.duration;
                float f3 = track.right;
                float f4 = track.duration;
                long j6 = (long) ((f2 * 1.0f) - (f3 * f4));
                float f5 = track.left;
                long j7 = (long) ((f2 * 0.0f) - (f5 * f4));
                float min = Math.min(f3 - f5, ((track3.right - track3.left) * f2) / f4);
                long j8 = track.offset;
                long j9 = (long) f;
                long j10 = j8 + j9;
                if (j10 > j6) {
                    float clamp2 = Utilities.clamp((((this.collageMain.right * r2.duration) - j8) - j9) / track.duration, 1.0f, min);
                    track.right = clamp2;
                    float clamp3 = Utilities.clamp(clamp2 - min, 1.0f, 0.0f);
                    track.left = clamp3;
                    Track track4 = this.collageMain;
                    float f6 = track4.right;
                    float f7 = track4.duration;
                    float f8 = track.right;
                    float f9 = track.duration;
                    long j11 = (long) ((f6 * f7) - (f8 * f9));
                    long j12 = (long) ((track4.left * f7) - (clamp3 * f9));
                    if (j11 < j12) {
                        j5 = j12;
                        j4 = j11;
                    } else {
                        j4 = j12;
                        j5 = j11;
                    }
                    track.offset = Utilities.clamp(track.offset + j9, j5, j4);
                    timelineDelegate3 = this.delegate;
                } else if (j10 < j7) {
                    float clamp4 = Utilities.clamp((((this.collageMain.left * r2.duration) - j8) - j9) / track.duration, 1.0f - min, 0.0f);
                    track.left = clamp4;
                    float clamp5 = Utilities.clamp(clamp4 + min, 1.0f, 0.0f);
                    track.right = clamp5;
                    Track track5 = this.collageMain;
                    float f10 = track5.right;
                    float f11 = track5.duration;
                    float f12 = track.duration;
                    long j13 = (long) ((f10 * f11) - (clamp5 * f12));
                    long j14 = (long) ((track5.left * f11) - (track.left * f12));
                    if (j13 < j14) {
                        j3 = j13;
                        j2 = j14;
                    } else {
                        j2 = j13;
                        j3 = j14;
                    }
                    track.offset = Utilities.clamp(track.offset + j9, j2, j3);
                    timelineDelegate3 = this.delegate;
                } else {
                    track.offset = j10;
                }
            } else {
                long j15 = track.offset + ((long) f);
                float baseDuration = getBaseDuration();
                float f13 = track.duration;
                track.offset = Utilities.clamp(j15, (long) (baseDuration - (track.right * f13)), (long) ((-track.left) * f13));
            }
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onVideoOffsetChange(track.index, track.offset);
        }
        boolean z = this.dragged;
        if (!z && (timelineDelegate2 = this.delegate) != null) {
            timelineDelegate2.onProgressDragChange(true);
            Track track6 = this.collageMain;
            if (track6 == track || track6 == null) {
                float f14 = track.left;
                long j16 = track.duration;
                j = Utilities.clamp((long) (f14 * j16), j16, 0L);
            } else {
                long j17 = track.offset + ((long) (track.left * track.duration));
                float f15 = track6.right;
                float f16 = track6.duration;
                j = Utilities.clamp(j17, (long) (f15 * f16), (long) (track6.left * f16));
            }
            Track track7 = this.collageMain;
            if (track7 != track && track7 != null && Math.abs(this.progress - j) > 400) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            timelineDelegate = this.delegate;
            this.progress = j;
        } else {
            if (!z && !this.scrolling) {
                return;
            }
            Track track8 = this.collageMain;
            if (track8 == track || track8 == null) {
                float f17 = track.left;
                long j18 = track.duration;
                clamp = Utilities.clamp((long) (f17 * j18), j18, 0L);
            } else {
                long j19 = track.offset + ((long) (track.left * track.duration));
                float f18 = track8.right;
                float f19 = track8.duration;
                clamp = Utilities.clamp(j19, (long) (f18 * f19), (long) (track8.left * f19));
            }
            this.progress = clamp;
            timelineDelegate = this.delegate;
            if (timelineDelegate == null) {
                return;
            } else {
                j = this.progress;
            }
        }
        timelineDelegate.onProgressChange(j, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0128, code lost:
    
        r1.onRoundLeftChange(r18.roundLeft);
        r18.delegate.onRoundRightChange(r18.roundRight);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00cd, code lost:
    
        if (r1 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0047, code lost:
    
        if (r1 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0126, code lost:
    
        if (r1 != null) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void moveRoundOffset(float f) {
        long j;
        long j2;
        TimelineDelegate timelineDelegate;
        long j3;
        long j4;
        long clamp;
        TimelineDelegate timelineDelegate2;
        long j5;
        TimelineDelegate timelineDelegate3;
        long j6;
        long j7;
        long j8;
        Track track = this.videoTrack;
        if (track == null) {
            long j9 = this.roundOffset;
            long clamp2 = Utilities.clamp(j9 + ((long) f), 0L, -(this.roundDuration - Math.min(getBaseDuration(), getMaxScrollDuration())));
            this.roundOffset = clamp2;
            float f2 = clamp2 - j9;
            this.roundLeft = Utilities.clamp(this.roundLeft - (f2 / this.roundDuration), 1.0f, 0.0f);
            this.roundRight = Utilities.clamp(this.roundRight - (f2 / this.roundDuration), 1.0f, 0.0f);
            timelineDelegate = this.delegate;
        } else if (this.roundSelected) {
            float f3 = track.right;
            float f4 = track.duration;
            float f5 = this.roundRight;
            float f6 = this.roundDuration;
            long j10 = (long) ((f3 * f4) - (f5 * f6));
            float f7 = track.left;
            float f8 = this.roundLeft;
            long j11 = (long) ((f7 * f4) - (f8 * f6));
            float min = Math.min(f5 - f8, ((f3 - f7) * f4) / f6);
            long j12 = this.roundOffset;
            long j13 = (long) f;
            long j14 = j12 + j13;
            if (j14 > j10) {
                float clamp3 = Utilities.clamp((((this.videoTrack.right * r1.duration) - j12) - j13) / this.roundDuration, 1.0f, min);
                this.roundRight = clamp3;
                float clamp4 = Utilities.clamp(clamp3 - min, 1.0f, 0.0f);
                this.roundLeft = clamp4;
                Track track2 = this.videoTrack;
                float f9 = track2.right;
                float f10 = track2.duration;
                float f11 = this.roundRight;
                float f12 = this.roundDuration;
                long j15 = (long) ((f9 * f10) - (f11 * f12));
                long j16 = (long) ((track2.left * f10) - (clamp4 * f12));
                if (j15 < j16) {
                    j4 = j16;
                    j3 = j15;
                } else {
                    j3 = j16;
                    j4 = j15;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j13, j4, j3);
                timelineDelegate = this.delegate;
            } else if (j14 < j11) {
                float clamp5 = Utilities.clamp((((this.videoTrack.left * r1.duration) - j12) - j13) / this.roundDuration, 1.0f - min, 0.0f);
                this.roundLeft = clamp5;
                float clamp6 = Utilities.clamp(clamp5 + min, 1.0f, 0.0f);
                this.roundRight = clamp6;
                Track track3 = this.videoTrack;
                float f13 = track3.right;
                float f14 = track3.duration;
                float f15 = this.roundDuration;
                long j17 = (long) ((f13 * f14) - (clamp6 * f15));
                long j18 = (long) ((track3.left * f14) - (this.roundLeft * f15));
                if (j17 < j18) {
                    j2 = j17;
                    j = j18;
                } else {
                    j = j17;
                    j2 = j18;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j13, j, j2);
                timelineDelegate = this.delegate;
            } else {
                this.roundOffset = j14;
            }
        } else {
            long j19 = this.roundOffset + ((long) f);
            float baseDuration = getBaseDuration();
            float f16 = this.roundDuration;
            this.roundOffset = Utilities.clamp(j19, (long) (baseDuration - (this.roundRight * f16)), (long) ((-this.roundLeft) * f16));
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onRoundOffsetChange(this.roundOffset + ((long) (this.roundLeft * this.roundDuration)));
        }
        boolean z = this.dragged;
        if (!z && (timelineDelegate3 = this.delegate) != null) {
            timelineDelegate3.onProgressDragChange(true);
            Track track4 = this.videoTrack;
            if (track4 != null) {
                j7 = this.roundOffset + ((long) (this.roundLeft * this.roundDuration));
                float f17 = track4.right;
                float f18 = track4.duration;
                j6 = (long) (f17 * f18);
                j8 = (long) (track4.left * f18);
            } else {
                float f19 = this.roundLeft;
                j6 = this.roundDuration;
                j7 = (long) (f19 * j6);
                j8 = 0;
            }
            j5 = Utilities.clamp(j7, j6, j8);
            if (this.videoTrack != null && Math.abs(this.progress - j5) > 400) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            timelineDelegate2 = this.delegate;
            this.progress = j5;
        } else {
            if (!z && !this.scrolling) {
                return;
            }
            Track track5 = this.videoTrack;
            if (track5 != null) {
                long j20 = this.roundOffset + ((long) (this.roundLeft * this.roundDuration));
                float f20 = track5.right;
                float f21 = track5.duration;
                clamp = Utilities.clamp(j20, (long) (f20 * f21), (long) (track5.left * f21));
            } else {
                float f22 = this.roundLeft;
                long j21 = this.roundDuration;
                clamp = Utilities.clamp((long) (f22 * j21), j21, 0L);
            }
            this.progress = clamp;
            timelineDelegate2 = this.delegate;
            if (timelineDelegate2 == null) {
                return;
            } else {
                j5 = this.progress;
            }
        }
        timelineDelegate2.onProgressChange(j5, false);
    }

    private boolean setProgressAt(float f, boolean z) {
        if (this.videoTrack == null && !this.hasAudio && this.collageTracks.isEmpty()) {
            return false;
        }
        long min = Math.min(getBaseDuration(), getMaxScrollDuration());
        float f2 = ((f - this.px) - this.ph) / this.sw;
        Track track = this.collageMain;
        long j = track != null ? (long) (track.offset + (track.left * track.duration)) : 0L;
        float f3 = f2 * min;
        if (track == null) {
            j = this.videoTrack == null ? this.audioOffset : 0L;
        }
        final long clamp = (long) Utilities.clamp((f3 - j) + this.scroll, getBaseDuration(), 0.0f);
        Track track2 = this.videoTrack;
        if (track2 != null) {
            float f4 = clamp / track2.duration;
            if (f4 < track2.left || f4 > track2.right) {
                return false;
            }
        }
        Track track3 = this.collageMain;
        if (track3 != null && (clamp < 0 || clamp >= ((long) ((track3.right - track3.left) * track3.duration)))) {
            return false;
        }
        if (this.hasAudio && track2 == null && this.collageTracks.isEmpty()) {
            float f5 = clamp / this.audioDuration;
            if (f5 < this.audioLeft || f5 > this.audioRight) {
                return false;
            }
        }
        this.progress = clamp;
        invalidate();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onProgressChange(clamp, z);
        }
        Runnable runnable = this.askExactSeek;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.askExactSeek = null;
        }
        if (!z) {
            return true;
        }
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$setProgressAt$9(clamp);
            }
        };
        this.askExactSeek = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, 150L);
        return true;
    }

    private void setupAudioWaveform() {
        if (getMeasuredWidth() > 0) {
            if (this.waveform == null || this.resetWaveform) {
                this.waveform = new AudioWaveformLoader(this.audioPath, (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
                this.waveformIsLoaded = false;
                this.waveformMax.set(1.0f, true);
            }
        }
    }

    private void setupRoundThumbs() {
        if (getMeasuredWidth() <= 0 || this.roundThumbs != null) {
            return;
        }
        Track track = this.videoTrack;
        if (track == null || track.duration >= 1) {
            String str = this.roundPath;
            int i = this.w;
            int i2 = this.px;
            int i3 = (i - i2) - i2;
            int dp = AndroidUtilities.dp(38.0f);
            long j = this.roundDuration;
            Long valueOf = j > 2 ? Long.valueOf(j) : null;
            Track track2 = this.videoTrack;
            this.roundThumbs = new VideoThumbsLoader(false, str, i3, dp, valueOf, track2 != null ? track2.duration : getMaxScrollDuration(), -1L, -1L, new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TimelineView.this.lambda$setupRoundThumbs$8();
                }
            });
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.scroller.computeScrollOffset()) {
            if (this.scrolling) {
                this.scrolling = false;
                TimelineDelegate timelineDelegate = this.delegate;
                if (timelineDelegate != null) {
                    timelineDelegate.onProgressDragChange(false);
                    return;
                }
                return;
            }
            return;
        }
        int currX = this.scroller.getCurrX();
        long min = Math.min(getBaseDuration(), getMaxScrollDuration());
        if (this.scrollingVideo) {
            this.scroll = (long) Math.max(0.0f, (((currX - this.px) - this.ph) / this.sw) * min);
        } else {
            if (!this.audioSelected) {
                this.scroller.abortAnimation();
                return;
            }
            int i = this.px;
            int i2 = this.ph;
            float f = this.sw;
            float f2 = min;
            moveAudioOffset(((((currX - i) - i2) / f) * f2) - ((((this.wasScrollX - i) - i2) / f) * f2));
        }
        invalidate();
        this.wasScrollX = currX;
    }

    /* JADX WARN: Removed duplicated region for block: B:133:0x0680 A[LOOP:4: B:133:0x0680->B:139:0x06bf, LOOP_START, PHI: r1 r11
      0x0680: PHI (r1v190 int) = (r1v189 int), (r1v191 int) binds: [B:132:0x067e, B:139:0x06bf] A[DONT_GENERATE, DONT_INLINE]
      0x0680: PHI (r11v50 float) = (r11v49 float), (r11v51 float) binds: [B:132:0x067e, B:139:0x06bf] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x06c5  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x095e A[LOOP:6: B:219:0x095e->B:225:0x099d, LOOP_START, PHI: r3 r5
      0x095e: PHI (r3v104 int) = (r3v103 int), (r3v105 int) binds: [B:218:0x095c, B:225:0x099d] A[DONT_GENERATE, DONT_INLINE]
      0x095e: PHI (r5v79 float) = (r5v78 float), (r5v80 float) binds: [B:218:0x095c, B:225:0x099d] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x09a3  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0c67  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x115c  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x1173  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x119b  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x1167  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x0fa2  */
    /* JADX WARN: Removed duplicated region for block: B:430:0x0e46  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x0c0a  */
    /* JADX WARN: Removed duplicated region for block: B:440:0x0c2c  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x0c2e  */
    /* JADX WARN: Removed duplicated region for block: B:486:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x039f  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x03f1  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        long j;
        float f;
        int i;
        long j2;
        long j3;
        long j4;
        long j5;
        TimelineDelegate timelineDelegate;
        Track track;
        Track track2;
        long j6;
        int i2;
        float f2;
        float max;
        float f3;
        boolean z;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        long j7;
        Canvas canvas2;
        float f12;
        float f13;
        float f14;
        float f15;
        long j8;
        float f16;
        long j9;
        float f17;
        float f18;
        float max2;
        float f19;
        float f20;
        float f21;
        float f22;
        float f23;
        float f24;
        float f25;
        TimelineView timelineView;
        Canvas canvas3;
        Paint paint;
        float f26;
        float f27;
        float f28;
        float f29;
        float f30;
        Paint paint2;
        float f31;
        float lerp;
        float lerp2;
        Paint paint3;
        int multAlpha;
        long j10;
        float f32;
        Paint paint4;
        long j11;
        float f33;
        BlurringShader.BlurManager blurManager;
        float f34;
        float f35;
        long j12;
        float f36;
        float f37;
        long j13;
        Paint paint5;
        boolean z2;
        float f38;
        long j14;
        float f39;
        float f40;
        float f41;
        float f42;
        float f43;
        Paint paint6;
        long j15;
        long j16;
        float f44;
        float f45;
        float f46;
        boolean z3;
        float f47;
        float f48;
        float f49;
        float f50;
        Paint paint7;
        boolean z4;
        int i3;
        BlurringShader.BlurManager blurManager2;
        Canvas canvas4 = canvas;
        Paint paint8 = this.backgroundBlur.getPaint(1.0f);
        float f51 = this.openT.set(this.open);
        long min = Math.min(getBaseDuration(), getMaxScrollDuration());
        if (f51 < 1.0f) {
            this.timelineBounds.set(this.px, (this.h - this.py) - AndroidUtilities.dp(28.0f), this.w - this.px, this.h - this.py);
            this.timelineClipPath.rewind();
            this.timelineClipPath.addRoundRect(this.timelineBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas4.saveLayerAlpha(this.timelineBounds, (int) ((1.0f - f51) * 255.0f), 31);
            canvas4.clipPath(this.timelineClipPath);
            if (this.blurManager.hasRenderNode()) {
                this.backgroundBlur.drawRect(canvas4);
            } else if (paint8 == null) {
                canvas4.drawColor(1073741824);
                if (this.collageWaveforms.isEmpty() && (blurManager2 = this.blurManager) != null && blurManager2.hasRenderNode()) {
                    float f52 = this.timelineWaveformMax.set(WaveformPath.getMaxBar(this.collageWaveforms));
                    WaveformPath waveformPath = this.timelineWaveformPath;
                    RectF rectF = this.timelineBounds;
                    waveformPath.check(this.px + this.ph + (((this.audioOffset - this.scroll) / min) * this.sw), rectF.left, rectF.right, 0.0f, AndroidUtilities.dp(28.0f), f52, this.timelineBounds.bottom, this.collageWaveforms);
                    canvas4.saveLayerAlpha(this.timelineBounds, 102, 31);
                    canvas4.clipPath(this.timelineWaveformPath);
                    this.audioWaveformBlur.drawRect(canvas4);
                    canvas.restore();
                } else if (!this.collageWaveforms.isEmpty()) {
                    Paint paint9 = this.audioWaveformBlur.getPaint(0.4f);
                    if (paint9 == null) {
                        paint9 = this.waveformPaint;
                        paint9.setAlpha(64);
                    }
                    float f53 = this.timelineWaveformMax.set(WaveformPath.getMaxBar(this.collageWaveforms));
                    WaveformPath waveformPath2 = this.timelineWaveformPath;
                    RectF rectF2 = this.timelineBounds;
                    waveformPath2.check(this.px + this.ph + (((this.audioOffset - this.scroll) / min) * this.sw), rectF2.left, rectF2.right, 0.0f, AndroidUtilities.dp(28.0f), f53, this.timelineBounds.bottom, this.collageWaveforms);
                    canvas4.drawPath(this.timelineWaveformPath, paint9);
                }
                float currentWidth = ((this.timelineText.getCurrentWidth() + AndroidUtilities.dp(3.66f)) + this.timelineIcon.getIntrinsicWidth()) / 2.0f;
                int centerX = (int) (this.timelineBounds.centerX() - currentWidth);
                int centerY = (int) this.timelineBounds.centerY();
                Drawable drawable = this.timelineIcon;
                drawable.setBounds(centerX, centerY - (drawable.getIntrinsicHeight() / 2), this.timelineIcon.getIntrinsicWidth() + centerX, (this.timelineIcon.getIntrinsicHeight() / 2) + centerY);
                this.timelineIcon.setAlpha(NotificationCenter.unconfirmedAuthUpdate);
                this.timelineIcon.draw(canvas4);
                this.timelineText.draw(canvas, (this.timelineBounds.centerX() - currentWidth) + this.timelineIcon.getIntrinsicWidth() + AndroidUtilities.dp(3.66f), centerY, -1, 0.75f);
                canvas.restore();
            } else {
                canvas4.drawRect(this.timelineBounds, paint8);
            }
            canvas4.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
            if (this.collageWaveforms.isEmpty()) {
            }
            if (!this.collageWaveforms.isEmpty()) {
            }
            float currentWidth2 = ((this.timelineText.getCurrentWidth() + AndroidUtilities.dp(3.66f)) + this.timelineIcon.getIntrinsicWidth()) / 2.0f;
            int centerX2 = (int) (this.timelineBounds.centerX() - currentWidth2);
            int centerY2 = (int) this.timelineBounds.centerY();
            Drawable drawable2 = this.timelineIcon;
            drawable2.setBounds(centerX2, centerY2 - (drawable2.getIntrinsicHeight() / 2), this.timelineIcon.getIntrinsicWidth() + centerX2, (this.timelineIcon.getIntrinsicHeight() / 2) + centerY2);
            this.timelineIcon.setAlpha(NotificationCenter.unconfirmedAuthUpdate);
            this.timelineIcon.draw(canvas4);
            this.timelineText.draw(canvas, (this.timelineBounds.centerX() - currentWidth2) + this.timelineIcon.getIntrinsicWidth() + AndroidUtilities.dp(3.66f), centerY2, -1, 0.75f);
            canvas.restore();
        }
        if (f51 > 0.0f) {
            if (f51 < 1.0f) {
                f3 = 0.0f;
                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (f51 * 255.0f), 31);
                z = true;
            } else {
                f3 = 0.0f;
                z = false;
            }
            Track track3 = this.videoTrack;
            float f54 = track3 != null ? 1.0f : 0.0f;
            float f55 = track3 != null ? track3.selectedT.set((this.audioSelected || this.roundSelected) ? false : true) : 0.0f;
            float f56 = this.h - this.py;
            float dp = AndroidUtilities.dp(4.0f);
            Paint paint10 = paint8;
            if (this.videoTrack != null) {
                canvas.save();
                float videoHeight = getVideoHeight();
                Track track4 = this.videoTrack;
                float f57 = track4.left;
                long j17 = track4.duration;
                float f58 = j17;
                float f59 = (f57 * f58 * f55) + f3;
                float f60 = (track4.right * f58 * f55) + f3;
                float f61 = j17 <= 0 ? 0.0f : (this.px + this.ph) - ((this.scroll / min) * this.sw);
                int i4 = this.ph;
                float f62 = i4;
                float f63 = f61 - f62;
                if (j17 <= 0) {
                    f47 = f59;
                    f48 = f55;
                    f7 = f60;
                    f49 = 0.0f;
                } else {
                    f47 = f59;
                    float f64 = this.px + i4;
                    f48 = f55;
                    f7 = f60;
                    f49 = (((j17 - this.scroll) / min) * this.sw) + f64;
                }
                this.videoBounds.set(f63, f56 - videoHeight, f49 + f62, f56);
                float f65 = f56 - ((dp * f54) + videoHeight);
                RectF rectF3 = this.videoBounds;
                float f66 = (rectF3.top * f48) + 0.0f;
                float f67 = (rectF3.bottom * f48) + 0.0f;
                this.videoClipPath.rewind();
                this.videoClipPath.addRoundRect(this.videoBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
                canvas4.clipPath(this.videoClipPath);
                VideoThumbsLoader videoThumbsLoader = this.videoTrack.thumbs;
                if (videoThumbsLoader != null) {
                    int frameWidth = videoThumbsLoader.getFrameWidth();
                    float f68 = frameWidth;
                    f8 = f66;
                    f50 = f54;
                    int max3 = (int) Math.max(0.0d, Math.floor((f63 - this.px) / f68));
                    int min2 = (int) Math.min(this.videoTrack.thumbs.count, Math.ceil(((r4 - f63) - this.px) / f68) + 1.0d);
                    int i5 = (int) this.videoBounds.top;
                    boolean z5 = this.videoTrack.thumbs.frames.size() >= min2;
                    boolean z6 = (frameWidth == 0 || !z5 || this.videoTrack.isRound) ? false : true;
                    f9 = f67;
                    if (z6) {
                        while (max3 < Math.min(this.videoTrack.thumbs.frames.size(), min2)) {
                            if (((VideoThumbsLoader.BitmapFrame) this.videoTrack.thumbs.frames.get(max3)).bitmap == null) {
                                z4 = false;
                                break;
                            }
                            max3++;
                        }
                    }
                    z4 = z6;
                    if (!z4) {
                        if (this.blurManager.hasRenderNode()) {
                            this.backgroundBlur.drawRect(canvas4);
                            canvas4.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                        } else if (paint10 == null) {
                            canvas4.drawColor(1073741824);
                        } else {
                            paint7 = paint10;
                            canvas4.drawRect(this.videoBounds, paint7);
                            canvas4.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                            if (frameWidth != 0) {
                                int i6 = max3;
                                while (i6 < Math.min(this.videoTrack.thumbs.frames.size(), min2)) {
                                    VideoThumbsLoader.BitmapFrame bitmapFrame = (VideoThumbsLoader.BitmapFrame) this.videoTrack.thumbs.frames.get(i6);
                                    if (bitmapFrame.bitmap != null) {
                                        i3 = min2;
                                        this.videoFramePaint.setAlpha((int) (bitmapFrame.getAlpha() * 255.0f));
                                        canvas4.drawBitmap(bitmapFrame.bitmap, f63, i5 - ((int) ((r0.getHeight() - videoHeight) / 2.0f)), this.videoFramePaint);
                                    } else {
                                        i3 = min2;
                                    }
                                    f63 += f68;
                                    i6++;
                                    min2 = i3;
                                }
                            }
                            if (!z5) {
                                this.videoTrack.thumbs.load();
                            }
                        }
                    }
                    paint7 = paint10;
                    if (frameWidth != 0) {
                    }
                    if (!z5) {
                    }
                } else {
                    f9 = f67;
                    f8 = f66;
                    f50 = f54;
                    paint7 = paint10;
                }
                this.selectedVideoClipPath.rewind();
                if (this.isCover) {
                    f4 = f50;
                    paint10 = paint7;
                } else {
                    RectF rectF4 = AndroidUtilities.rectTmp;
                    float f69 = this.px + this.ph;
                    Track track5 = this.videoTrack;
                    float f70 = track5.left;
                    float f71 = track5.duration;
                    paint10 = paint7;
                    float f72 = this.scroll;
                    float f73 = min;
                    float f74 = this.sw;
                    float f75 = (((((f70 * f71) - f72) / f73) * f74) + f69) - (f70 <= 0.0f ? r5 : 0);
                    f4 = f50;
                    float f76 = this.h - this.py;
                    float f77 = f76 - videoHeight;
                    float f78 = track5.right;
                    rectF4.set(f75, f77, f69 + ((((f71 * f78) - f72) / f73) * f74) + (f78 >= 1.0f ? r5 : 0), f76);
                    this.selectedVideoClipPath.addRoundRect(rectF4, this.selectedVideoRadii, Path.Direction.CW);
                    canvas4.clipPath(this.selectedVideoClipPath, Region.Op.DIFFERENCE);
                    canvas4.drawColor(1342177280);
                }
                canvas.restore();
                f6 = videoHeight;
                f5 = f47;
                f56 = f65;
            } else {
                f4 = f54;
                f5 = 0.0f;
                f6 = 0.0f;
                f7 = 0.0f;
                f8 = 0.0f;
                f9 = 0.0f;
            }
            if (this.collageTracks.isEmpty()) {
                f10 = dp;
                f11 = f6;
                j7 = min;
                canvas2 = canvas4;
                f12 = 0.0f;
            } else {
                getCollageHeight();
                int i7 = 0;
                while (i7 < this.collageTracks.size()) {
                    Track track6 = (Track) this.collageTracks.get(i7);
                    float f79 = track6.selectedT.set((this.audioSelected || this.roundSelected || this.collageSelected != i7) ? false : true);
                    if (track6 != this.collageMain) {
                        f38 = f6;
                        float f80 = min;
                        f40 = this.px + this.ph + ((((track6.offset - this.scroll) + (AndroidUtilities.lerp(track6.left, 0.0f, f79) * track6.duration)) / f80) * this.sw);
                        f39 = this.px + this.ph + ((((track6.offset - this.scroll) + (AndroidUtilities.lerp(track6.right, 1.0f, f79) * track6.duration)) / f80) * this.sw);
                        j14 = min;
                    } else {
                        f38 = f6;
                        float f81 = this.px + this.ph;
                        long j18 = track6.offset - this.scroll;
                        float f82 = min;
                        float f83 = this.sw;
                        j14 = min;
                        f39 = f81 + (((j18 + track6.duration) / f82) * f83);
                        f40 = ((j18 / f82) * f83) + f81;
                    }
                    canvas.save();
                    float lerp3 = AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), f79);
                    RectF rectF5 = track6.bounds;
                    float f84 = this.ph;
                    rectF5.set(f40 - f84, f56 - lerp3, f39 + f84, f56);
                    RectF rectF6 = track6.bounds;
                    f8 += rectF6.top * f79;
                    f9 += rectF6.bottom * f79;
                    float f85 = track6.offset;
                    float f86 = track6.left;
                    float f87 = track6.duration;
                    float f88 = f5 + (((f86 * f87) + f85) * f79);
                    f7 += (f85 + (track6.right * f87)) * f79;
                    this.collageClipPath.rewind();
                    this.collageClipPath.addRoundRect(track6.bounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
                    canvas.clipPath(this.collageClipPath);
                    VideoThumbsLoader videoThumbsLoader2 = track6.thumbs;
                    if (videoThumbsLoader2 != null) {
                        long j19 = track6.duration;
                        if (j19 <= 0) {
                            j16 = j14;
                            f44 = 0.0f;
                        } else {
                            j16 = j14;
                            f44 = this.px + this.ph + (((track6.offset - this.scroll) / j16) * this.sw);
                        }
                        int i8 = this.ph;
                        float f89 = f44 - i8;
                        if (j19 <= 0) {
                            f41 = f56;
                            f45 = dp;
                            f43 = f88;
                            f46 = 0.0f;
                        } else {
                            f43 = f88;
                            float f90 = this.px + i8;
                            f41 = f56;
                            f45 = dp;
                            f46 = f90 + ((((track6.offset + j19) - this.scroll) / j16) * this.sw);
                        }
                        int frameWidth2 = videoThumbsLoader2.getFrameWidth();
                        f42 = f45;
                        float f91 = frameWidth2;
                        j14 = j16;
                        int max4 = (int) Math.max(0.0d, Math.floor((f89 - ((this.px + this.ph) + (((track6.offset - this.scroll) / j16) * this.sw))) / f91));
                        int min3 = (int) Math.min(track6.thumbs.count, Math.ceil(((f46 + r13) - f89) / f91) + 1.0d);
                        int i9 = (int) track6.bounds.top;
                        boolean z7 = track6.thumbs.frames.size() >= min3;
                        if (z7) {
                            for (int i10 = max4; i10 < Math.min(track6.thumbs.frames.size(), min3); i10++) {
                                if (((VideoThumbsLoader.BitmapFrame) track6.thumbs.frames.get(i10)).bitmap == null) {
                                    z3 = false;
                                    break;
                                }
                            }
                        }
                        z3 = z7;
                        if (!z3) {
                            if (this.blurManager.hasRenderNode()) {
                                this.backgroundBlur.drawRect(canvas);
                                canvas.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                            } else if (paint10 == null) {
                                canvas.drawColor(1073741824);
                            } else {
                                paint6 = paint10;
                                canvas.drawRect(track6.bounds, paint6);
                                canvas.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                                if (frameWidth2 != 0) {
                                    while (max4 < Math.min(track6.thumbs.frames.size(), min3)) {
                                        VideoThumbsLoader.BitmapFrame bitmapFrame2 = (VideoThumbsLoader.BitmapFrame) track6.thumbs.frames.get(max4);
                                        if (bitmapFrame2.bitmap != null) {
                                            this.collageFramePaint.setAlpha((int) (bitmapFrame2.getAlpha() * 255.0f));
                                            canvas.drawBitmap(bitmapFrame2.bitmap, f89, i9 - ((int) ((r0.getHeight() - lerp3) / 2.0f)), this.collageFramePaint);
                                        }
                                        f89 += f91;
                                        max4++;
                                    }
                                }
                                if (!z7) {
                                    track6.thumbs.load();
                                }
                            }
                        }
                        paint6 = paint10;
                        if (frameWidth2 != 0) {
                        }
                        if (!z7) {
                        }
                    } else {
                        f41 = f56;
                        f42 = dp;
                        f43 = f88;
                        paint6 = paint10;
                    }
                    this.selectedCollageClipPath.rewind();
                    if (this.isCover) {
                        paint10 = paint6;
                        j15 = j14;
                    } else {
                        RectF rectF7 = AndroidUtilities.rectTmp;
                        float f92 = this.px + this.ph;
                        float f93 = track6.left;
                        float f94 = track6.duration;
                        float f95 = this.scroll;
                        float f96 = track6.offset;
                        paint10 = paint6;
                        long j20 = j14;
                        float f97 = j20;
                        j15 = j20;
                        float f98 = this.sw;
                        float f99 = ((((((f93 * f94) - f95) + f96) / f97) * f98) + f92) - (f93 <= 0.0f ? r4 : 0);
                        RectF rectF8 = track6.bounds;
                        float f100 = rectF8.top;
                        float f101 = track6.right;
                        rectF7.set(f99, f100, f92 + (((((f94 * f101) - f95) + f96) / f97) * f98) + (f101 >= 1.0f ? r4 : 0), rectF8.bottom);
                        this.selectedCollageClipPath.addRoundRect(rectF7, this.selectedVideoRadii, Path.Direction.CW);
                        canvas.clipPath(this.selectedCollageClipPath, Region.Op.DIFFERENCE);
                        canvas.drawColor(1342177280);
                    }
                    canvas.restore();
                    f56 = f41 - (lerp3 + (f42 * 1.0f));
                    i7++;
                    dp = f42;
                    canvas4 = canvas;
                    f6 = f38;
                    f5 = f43;
                    min = j15;
                }
                f10 = dp;
                f11 = f6;
                j7 = min;
                canvas2 = canvas4;
                f12 = 1.0f;
            }
            float f102 = this.roundT.set(this.hasRound);
            float f103 = this.roundSelectedT.set(this.hasRound && this.roundSelected);
            float roundHeight = getRoundHeight() * f102;
            float f104 = ((this.videoTrack == null && !this.hasAudio && this.collageTracks.isEmpty()) ? 1.0f : f103) * f102;
            if (f102 > 0.0f) {
                long j21 = this.roundOffset;
                float f105 = j21;
                float f106 = this.roundLeft;
                f13 = f12;
                float f107 = this.roundDuration;
                float f108 = f5 + ((f105 + (f106 * f107)) * f104);
                f7 += (f105 + (this.roundRight * f107)) * f104;
                if (this.videoTrack != null) {
                    j12 = j7;
                    float f109 = j12;
                    f37 = this.px + this.ph + ((((j21 - this.scroll) + (AndroidUtilities.lerp(f106, 0.0f, f103) * this.roundDuration)) / f109) * this.sw);
                    f34 = f104;
                    f35 = f10;
                    f36 = this.px + this.ph + ((((this.roundOffset - this.scroll) + (AndroidUtilities.lerp(this.roundRight, 1.0f, f103) * this.roundDuration)) / f109) * this.sw);
                    f15 = f103;
                } else {
                    f34 = f104;
                    f35 = f10;
                    j12 = j7;
                    float f110 = this.px + this.ph;
                    long j22 = j21 - this.scroll;
                    float f111 = j12;
                    f15 = f103;
                    float f112 = this.sw;
                    float f113 = ((j22 / f111) * f112) + f110;
                    f36 = (((j22 + r14) / f111) * f112) + f110;
                    f37 = f113;
                }
                RectF rectF9 = this.roundBounds;
                float f114 = this.ph;
                rectF9.set(f37 - f114, f56 - roundHeight, f36 + f114, f56);
                float f115 = f56 - ((f35 * f102) + roundHeight);
                RectF rectF10 = this.roundBounds;
                f8 += rectF10.top * f34;
                f9 += rectF10.bottom * f34;
                this.roundClipPath.rewind();
                this.roundClipPath.addRoundRect(this.roundBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
                canvas.save();
                canvas2.clipPath(this.roundClipPath);
                VideoThumbsLoader videoThumbsLoader3 = this.roundThumbs;
                if (videoThumbsLoader3 != null) {
                    long j23 = this.roundDuration;
                    float f116 = j23 <= 0 ? 0.0f : this.px + this.ph + (((this.roundOffset - this.scroll) / j12) * this.sw);
                    float f117 = this.ph;
                    float f118 = f116 - f117;
                    float f119 = (j23 <= 0 ? 0.0f : this.px + r6 + ((((this.roundOffset + j23) - this.scroll) / j12) * this.sw)) + f117;
                    int frameWidth3 = videoThumbsLoader3.getFrameWidth();
                    float f120 = frameWidth3;
                    j13 = j12;
                    int max5 = (int) Math.max(0.0d, Math.floor((f118 - (this.videoTrack != null ? (this.px + this.ph) + (((this.roundOffset - this.scroll) / j12) * this.sw) : this.px)) / f120));
                    int min4 = (int) Math.min(this.roundThumbs.count, Math.ceil((f119 - f118) / f120) + 1.0d);
                    int i11 = (int) this.roundBounds.top;
                    boolean z8 = this.roundThumbs.frames.size() >= min4;
                    if (z8) {
                        for (int i12 = max5; i12 < Math.min(this.roundThumbs.frames.size(), min4); i12++) {
                            if (((VideoThumbsLoader.BitmapFrame) this.roundThumbs.frames.get(i12)).bitmap == null) {
                                z2 = false;
                                break;
                            }
                        }
                    }
                    z2 = z8;
                    if (!z2) {
                        if (this.blurManager.hasRenderNode()) {
                            this.backgroundBlur.drawRect(canvas2);
                            canvas2.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                        } else if (paint10 == null) {
                            canvas2.drawColor(1073741824);
                        } else {
                            paint5 = paint10;
                            canvas2.drawRect(this.roundBounds, paint5);
                            canvas2.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                            if (frameWidth3 != 0) {
                                while (max5 < Math.min(this.roundThumbs.frames.size(), min4)) {
                                    VideoThumbsLoader.BitmapFrame bitmapFrame3 = (VideoThumbsLoader.BitmapFrame) this.roundThumbs.frames.get(max5);
                                    if (bitmapFrame3.bitmap != null) {
                                        this.videoFramePaint.setAlpha((int) (bitmapFrame3.getAlpha() * 255.0f));
                                        canvas2.drawBitmap(bitmapFrame3.bitmap, f118, i11 - ((int) ((r2.getHeight() - roundHeight) / 2.0f)), this.videoFramePaint);
                                    }
                                    f118 += f120;
                                    max5++;
                                }
                            }
                            if (!z8) {
                                this.roundThumbs.load();
                            }
                        }
                    }
                    paint5 = paint10;
                    if (frameWidth3 != 0) {
                    }
                    if (!z8) {
                    }
                } else {
                    j13 = j12;
                    paint5 = paint10;
                }
                this.selectedVideoClipPath.rewind();
                RectF rectF11 = AndroidUtilities.rectTmp;
                int i13 = this.px;
                int i14 = this.ph;
                float f121 = i13 + i14;
                float f122 = this.roundLeft;
                float f123 = this.roundDuration;
                float f124 = this.scroll;
                float f125 = this.roundOffset;
                j8 = j13;
                float f126 = j8;
                float f127 = this.sw;
                float f128 = ((((((f122 * f123) - f124) + f125) / f126) * f127) + f121) - (f122 <= 0.0f ? i14 : 0);
                float f129 = i14 * (1.0f - f15);
                float f130 = f128 - f129;
                RectF rectF12 = this.roundBounds;
                paint10 = paint5;
                float f131 = rectF12.top;
                f14 = f102;
                float f132 = this.roundRight;
                rectF11.set(f130, f131, f121 + (((((f123 * f132) - f124) + f125) / f126) * f127) + (f132 >= 1.0f ? i14 : 0) + f129, rectF12.bottom);
                this.selectedVideoClipPath.addRoundRect(rectF11, this.selectedVideoRadii, Path.Direction.CW);
                canvas2.clipPath(this.selectedVideoClipPath, Region.Op.DIFFERENCE);
                canvas2.drawColor(1342177280);
                canvas.restore();
                f5 = f108;
                f56 = f115;
            } else {
                f13 = f12;
                f14 = f102;
                f15 = f103;
                j8 = j7;
            }
            float f133 = this.audioT.set(this.hasAudio);
            float f134 = this.audioSelectedT.set(this.hasAudio && this.audioSelected);
            float audioHeight = getAudioHeight() * f133;
            float f135 = ((this.videoTrack == null && !this.hasRound && this.collageTracks.isEmpty()) ? 1.0f : f134) * f133;
            if (f133 > 0.0f) {
                float f136 = this.audioOffset;
                float f137 = this.audioLeft;
                float f138 = this.audioDuration;
                float f139 = f5 + (((f137 * f138) + f136) * f135);
                f7 += (f136 + (this.audioRight * f138)) * f135;
                Paint paint11 = this.audioBlur.getPaint(f133);
                canvas.save();
                if (this.videoTrack == null && !this.hasRound && this.collageTracks.isEmpty()) {
                    float f140 = this.px + this.ph;
                    long j24 = this.audioOffset - this.scroll;
                    float f141 = j8;
                    f31 = f139;
                    float f142 = this.sw;
                    lerp = ((j24 / f141) * f142) + f140;
                    f30 = f135;
                    paint2 = paint11;
                    lerp2 = f140 + (((j24 + this.audioDuration) / f141) * f142);
                } else {
                    f30 = f135;
                    paint2 = paint11;
                    f31 = f139;
                    float f143 = j8;
                    lerp = this.px + this.ph + ((((this.audioOffset - this.scroll) + (AndroidUtilities.lerp(this.audioLeft, 0.0f, f134) * this.audioDuration)) / f143) * this.sw);
                    lerp2 = this.px + this.ph + ((((this.audioOffset - this.scroll) + (AndroidUtilities.lerp(this.audioRight, 1.0f, f134) * this.audioDuration)) / f143) * this.sw);
                }
                RectF rectF13 = this.audioBounds;
                float f144 = this.ph;
                rectF13.set(lerp - f144, f56 - audioHeight, f144 + lerp2, f56);
                RectF rectF14 = this.audioBounds;
                f8 += rectF14.top * f30;
                f9 += rectF14.bottom * f30;
                this.audioClipPath.rewind();
                this.audioClipPath.addRoundRect(this.audioBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
                canvas2.clipPath(this.audioClipPath);
                BlurringShader.BlurManager blurManager3 = this.blurManager;
                if (blurManager3 != null && blurManager3.hasRenderNode()) {
                    this.backgroundBlur.drawRect(canvas2);
                    multAlpha = Theme.multAlpha(AndroidUtilities.DARK_STATUS_BAR_OVERLAY, f133);
                } else if (paint2 == null) {
                    multAlpha = Theme.multAlpha(1073741824, f133);
                } else {
                    paint3 = paint2;
                    canvas2.drawRect(this.audioBounds, paint3);
                    canvas2.drawColor(Theme.multAlpha(AndroidUtilities.DARK_STATUS_BAR_OVERLAY, f133));
                    if (this.waveform == null && (blurManager = this.blurManager) != null && blurManager.hasRenderNode()) {
                        float f145 = this.waveformMax.set(this.waveform.getMaxBar(), !this.waveformIsLoaded);
                        this.waveformIsLoaded = this.waveform.getLoadedCount() > 0;
                        f32 = 0.0f;
                        j10 = j8;
                        this.waveformPath.check(this.px + this.ph + (((this.audioOffset - this.scroll) / j8) * this.sw), lerp, lerp2, f134, j10, audioHeight, f145, this.audioBounds.bottom, this.waveform);
                        canvas2.saveLayerAlpha(this.audioBounds, 102, 31);
                        canvas2.clipPath(this.waveformPath);
                        this.audioWaveformBlur.drawRect(canvas2);
                        canvas.restore();
                    } else {
                        j10 = j8;
                        f32 = 0.0f;
                        if (this.waveform != null && paint3 != null) {
                            paint4 = this.audioWaveformBlur.getPaint(0.4f * f133);
                            if (paint4 == null) {
                                paint4 = this.waveformPaint;
                                paint4.setAlpha((int) (64.0f * f133));
                            }
                            float f146 = this.waveformMax.set(this.waveform.getMaxBar(), !this.waveformIsLoaded);
                            this.waveformIsLoaded = this.waveform.getLoadedCount() <= 0;
                            j11 = j10;
                            this.waveformPath.check(this.px + this.ph + (((this.audioOffset - this.scroll) / j11) * this.sw), lerp, lerp2, f134, j11, audioHeight, f146, this.audioBounds.bottom, this.waveform);
                            canvas2.drawPath(this.waveformPath, paint4);
                            if (f134 >= 1.0f) {
                                int i15 = this.px;
                                float f147 = this.ph + i15;
                                float f148 = this.audioOffset - this.scroll;
                                float f149 = this.audioLeft;
                                float f150 = this.audioDuration;
                                float f151 = j11;
                                float f152 = this.sw;
                                float f153 = ((((f149 * f150) + f148) / f151) * f152) + f147;
                                float f154 = f147 + (((f148 + (this.audioRight * f150)) / f151) * f152);
                                float max6 = (Math.max(i15, f153) + Math.min(this.w - this.px, f154)) / 2.0f;
                                float centerY3 = this.audioBounds.centerY();
                                float max7 = Math.max(f32, (Math.min(this.w - this.px, f154) - Math.max(this.px, f153)) - AndroidUtilities.dp(24.0f));
                                float dpf2 = AndroidUtilities.dpf2(13.0f) + ((this.audioAuthor == null && this.audioTitle == null) ? 0.0f : AndroidUtilities.dpf2(3.11f) + this.audioAuthorWidth + AndroidUtilities.dpf2(9.66f) + this.audioTitleWidth);
                                boolean z9 = dpf2 < max7;
                                float min5 = max6 - (Math.min(dpf2, max7) / 2.0f);
                                this.audioIcon.setBounds((int) min5, (int) (centerY3 - (AndroidUtilities.dp(13.0f) / 2.0f)), (int) (AndroidUtilities.dp(13.0f) + min5), (int) ((AndroidUtilities.dp(13.0f) / 2.0f) + centerY3));
                                float f155 = 1.0f - f134;
                                float f156 = f155 * 255.0f;
                                this.audioIcon.setAlpha((int) f156);
                                this.audioIcon.draw(canvas2);
                                float dpf22 = min5 + AndroidUtilities.dpf2(16.11f);
                                j9 = j11;
                                f16 = f134;
                                f17 = f4;
                                canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, NotificationCenter.liveLocationsChanged, 31);
                                float min6 = Math.min(f154, this.w) - AndroidUtilities.dp(12.0f);
                                f = 0.0f;
                                canvas2.clipRect(dpf22, 0.0f, min6, this.h);
                                if (this.audioAuthor != null) {
                                    canvas.save();
                                    canvas2.translate(dpf22 - this.audioAuthorLeft, centerY3 - (this.audioAuthor.getHeight() / 2.0f));
                                    this.audioAuthorPaint.setAlpha((int) (f156 * f133));
                                    this.audioAuthor.draw(canvas2);
                                    canvas.restore();
                                    f33 = this.audioAuthorWidth + dpf22;
                                } else {
                                    f33 = dpf22;
                                }
                                if (this.audioAuthor != null && this.audioTitle != null) {
                                    float dpf23 = f33 + AndroidUtilities.dpf2(3.66f);
                                    int alpha = this.audioDotPaint.getAlpha();
                                    this.audioDotPaint.setAlpha((int) (alpha * f155));
                                    canvas2.drawCircle(AndroidUtilities.dp(1.0f) + dpf23, centerY3, AndroidUtilities.dp(1.0f), this.audioDotPaint);
                                    this.audioDotPaint.setAlpha(alpha);
                                    f33 = dpf23 + AndroidUtilities.dpf2(2.0f) + AndroidUtilities.dpf2(4.0f);
                                }
                                if (this.audioTitle != null) {
                                    canvas.save();
                                    canvas2.translate(f33 - this.audioTitleLeft, centerY3 - (this.audioTitle.getHeight() / 2.0f));
                                    this.audioTitlePaint.setAlpha((int) (f156 * f133));
                                    this.audioTitle.draw(canvas2);
                                    canvas.restore();
                                }
                                if (!z9) {
                                    this.ellipsizeMatrix.reset();
                                    this.ellipsizeMatrix.postScale(AndroidUtilities.dpf2(8.0f) / 16.0f, 1.0f);
                                    this.ellipsizeMatrix.postTranslate(min6 - AndroidUtilities.dp(8.0f), 0.0f);
                                    this.ellipsizeGradient.setLocalMatrix(this.ellipsizeMatrix);
                                    RectF rectF15 = this.audioBounds;
                                    canvas.drawRect(min6 - AndroidUtilities.dp(8.0f), rectF15.top, min6, rectF15.bottom, this.ellipsizePaint);
                                }
                                canvas.restore();
                            } else {
                                j9 = j11;
                                f16 = f134;
                                f17 = f4;
                                f = 0.0f;
                            }
                            canvas.restore();
                            f5 = f31;
                        }
                    }
                    j11 = j10;
                    if (f134 >= 1.0f) {
                    }
                    canvas.restore();
                    f5 = f31;
                }
                canvas2.drawColor(multAlpha);
                paint3 = paint2;
                if (this.waveform == null) {
                }
                j10 = j8;
                f32 = 0.0f;
                if (this.waveform != null) {
                    paint4 = this.audioWaveformBlur.getPaint(0.4f * f133);
                    if (paint4 == null) {
                    }
                    float f1462 = this.waveformMax.set(this.waveform.getMaxBar(), !this.waveformIsLoaded);
                    this.waveformIsLoaded = this.waveform.getLoadedCount() <= 0;
                    j11 = j10;
                    this.waveformPath.check(this.px + this.ph + (((this.audioOffset - this.scroll) / j11) * this.sw), lerp, lerp2, f134, j11, audioHeight, f1462, this.audioBounds.bottom, this.waveform);
                    canvas2.drawPath(this.waveformPath, paint4);
                    if (f134 >= 1.0f) {
                    }
                    canvas.restore();
                    f5 = f31;
                }
                j11 = j10;
                if (f134 >= 1.0f) {
                }
                canvas.restore();
                f5 = f31;
            } else {
                f16 = f134;
                j9 = j8;
                f17 = f4;
                f = 0.0f;
            }
            float f157 = f8;
            float f158 = this.px + this.ph;
            float f159 = this.scroll;
            j = j9;
            float f160 = j;
            float f161 = this.sw;
            float f162 = f158 + (((f5 - f159) / f160) * f161);
            float f163 = f158 + (((f7 - f159) / f160) * f161);
            if (!this.collageTracks.isEmpty()) {
                f18 = f14;
                max2 = f13;
            } else if (this.hasAudio && this.videoTrack == null) {
                max2 = f133;
                f18 = f14;
            } else {
                f18 = f14;
                max2 = Math.max(f17, f18);
            }
            if (f133 > 0.0d || f18 > 0.0d || f17 > 0.0d || f13 > 0.0d) {
                if (this.videoTrack == null && !this.hasRound && this.collageTracks.isEmpty()) {
                    f19 = f16;
                    f20 = AndroidUtilities.lerp(0.6f, 1.0f, f19) * f133;
                } else {
                    f19 = f16;
                    f20 = 1.0f;
                }
                drawRegion(canvas, paint10, f157, f9, f162, f163, f20 * max2);
                Track track7 = this.videoTrack;
                if (track7 == null || (!(this.hasAudio || this.hasRound) || (f19 <= f && f15 <= f))) {
                    if (this.collageMain != null && this.collageTracks.size() > 1) {
                        Track track8 = this.collageMain;
                        RectF rectF16 = track8.bounds;
                        f21 = rectF16.top;
                        f22 = rectF16.bottom;
                        float f164 = this.ph + this.px;
                        float f165 = track8.offset;
                        float f166 = track8.left;
                        float f167 = track8.duration;
                        float f168 = this.scroll;
                        float f169 = this.sw;
                        f23 = (((((f166 * f167) + f165) - f168) / f160) * f169) + f164;
                        f24 = f164 + ((((f165 + (track8.right * f167)) - f168) / f160) * f169);
                        f25 = 0.8f;
                        timelineView = this;
                        canvas3 = canvas;
                        paint = paint10;
                    }
                    f26 = this.loopProgress.set(f);
                    float contentHeight = ((this.h - getContentHeight()) + this.py) - AndroidUtilities.dpf2(2.3f);
                    float dpf24 = (this.h - this.py) + AndroidUtilities.dpf2(4.3f);
                    if (f26 > f) {
                        long j25 = this.loopProgressFrom;
                        if (j25 == -1) {
                            Track track9 = this.videoTrack;
                            if (track9 != null) {
                                f29 = track9.duration * track9.right;
                            } else {
                                Track track10 = this.collageMain;
                                if (track10 != null) {
                                    f29 = track10.duration * (track10.right - track10.left);
                                } else {
                                    if (this.hasRound) {
                                        f27 = this.roundDuration;
                                        f28 = this.roundRight;
                                    } else {
                                        f27 = this.audioDuration;
                                        f28 = this.audioRight;
                                    }
                                    j25 = (long) (f27 * f28);
                                }
                            }
                            j25 = (long) f29;
                        }
                        drawProgress(canvas, contentHeight, dpf24, j25, f26 * max2);
                    }
                    drawProgress(canvas, contentHeight, dpf24, this.progress, (1.0f - f26) * max2);
                } else {
                    f22 = this.h - this.py;
                    f21 = f22 - f11;
                    float f170 = this.ph + this.px;
                    float f171 = track7.left;
                    float f172 = track7.duration;
                    float f173 = this.scroll;
                    float f174 = this.sw;
                    float f175 = f170 + ((((f171 * f172) - f173) / f160) * f174);
                    f24 = f170 + ((((track7.right * f172) - f173) / f160) * f174);
                    f25 = 0.8f;
                    timelineView = this;
                    canvas3 = canvas;
                    paint = paint10;
                    f23 = f175;
                }
                timelineView.drawRegion(canvas3, paint, f21, f22, f23, f24, f25);
                f26 = this.loopProgress.set(f);
                float contentHeight2 = ((this.h - getContentHeight()) + this.py) - AndroidUtilities.dpf2(2.3f);
                float dpf242 = (this.h - this.py) + AndroidUtilities.dpf2(4.3f);
                if (f26 > f) {
                }
                drawProgress(canvas, contentHeight2, dpf242, this.progress, (1.0f - f26) * max2);
            }
            if (z) {
                canvas.restore();
            }
        } else {
            j = min;
            f = 0.0f;
        }
        if (this.dragged) {
            float f176 = 1.0f / (1000.0f / AndroidUtilities.screenRefreshRate);
            float f177 = this.dragSpeed;
            long dp2 = (long) ((AndroidUtilities.dp(32.0f) / this.sw) * j * f176 * f177);
            if (this.isCover) {
                this.dragSpeed = f177 + (f176 * 0.25f);
            }
            int i16 = this.pressHandle;
            if (i16 == 4 && (track2 = this.videoTrack) != null) {
                float f178 = track2.left;
                long j26 = this.scroll;
                long j27 = track2.duration;
                float f179 = j27;
                if (f178 < j26 / f179) {
                    j6 = j27;
                    i2 = -1;
                } else {
                    j6 = j27;
                    if (track2.right > (j26 + j) / f179) {
                        i2 = 1;
                    } else {
                        this.dragSpeed = 1.0f;
                        i2 = 0;
                    }
                }
                long j28 = i2 * dp2;
                this.scroll = Utilities.clamp(j26 + j28, j6 - j, 0L);
                this.progress += j28;
                Track track11 = this.videoTrack;
                float f180 = (r2 - j26) / track11.duration;
                if (f180 > f) {
                    f2 = 1.0f;
                    max = Math.min(1.0f - track11.right, f180);
                } else {
                    f2 = 1.0f;
                    max = Math.max(f - track11.left, f180);
                }
                Track track12 = this.videoTrack;
                track12.left = Utilities.clamp(track12.left + max, f2, f);
                Track track13 = this.videoTrack;
                track13.right = Utilities.clamp(track13.right + max, f2, f);
                TimelineDelegate timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    timelineDelegate2.onVideoLeftChange(false, this.videoTrack.left);
                    this.delegate.onVideoRightChange(false, this.videoTrack.right);
                }
            } else if (i16 == 8) {
                float f181 = this.audioLeft;
                long j29 = this.audioOffset;
                float f182 = (-j29) + 100;
                long j30 = this.audioDuration;
                float f183 = j30;
                if (f181 < f182 / f183) {
                    i = -1;
                } else if (this.audioRight >= ((r9 + j) - 100) / f183) {
                    i = 1;
                } else {
                    this.dragSpeed = 1.0f;
                    i = 0;
                }
                if (i != 0) {
                    if (this.audioSelected && (track = this.videoTrack) != null) {
                        j2 = j29 - (i * dp2);
                        float f184 = track.right;
                        float f185 = track.duration;
                        long j31 = (long) ((f184 * f185) - (f181 * f183));
                        j3 = (long) ((track.left * f185) - (this.audioRight * f183));
                        j4 = j31;
                    } else if (this.roundSelected && this.hasRound) {
                        j2 = j29 - (i * dp2);
                        float f186 = this.roundRight;
                        float f187 = this.roundDuration;
                        j4 = (long) ((f186 * f187) - (f181 * f183));
                        j5 = (long) ((this.roundLeft * f187) - (this.audioRight * f183));
                        this.audioOffset = Utilities.clamp(j2, j4, j5);
                        float f188 = (-(this.audioOffset - j29)) / this.audioDuration;
                        float min7 = f188 <= f ? Math.min(1.0f - this.audioRight, f188) : Math.max(f - this.audioLeft, f188);
                        if (this.videoTrack == null) {
                            float f189 = this.progress;
                            float f190 = this.audioDuration;
                            this.progress = (long) Utilities.clamp(f189 + (min7 * f190), f190, f);
                        }
                        this.audioLeft = Utilities.clamp(this.audioLeft + min7, 1.0f, f);
                        this.audioRight = Utilities.clamp(this.audioRight + min7, 1.0f, f);
                        timelineDelegate = this.delegate;
                        if (timelineDelegate != null) {
                            timelineDelegate.onAudioLeftChange(this.audioLeft);
                            this.delegate.onAudioRightChange(this.audioRight);
                            this.delegate.onProgressChange(this.progress, false);
                        }
                    } else {
                        j2 = j29 - (i * dp2);
                        j3 = -(j30 - Math.min(getBaseDuration(), getMaxScrollDuration()));
                        j4 = 0;
                    }
                    j5 = j3;
                    this.audioOffset = Utilities.clamp(j2, j4, j5);
                    float f1882 = (-(this.audioOffset - j29)) / this.audioDuration;
                    if (f1882 <= f) {
                    }
                    if (this.videoTrack == null) {
                    }
                    this.audioLeft = Utilities.clamp(this.audioLeft + min7, 1.0f, f);
                    this.audioRight = Utilities.clamp(this.audioRight + min7, 1.0f, f);
                    timelineDelegate = this.delegate;
                    if (timelineDelegate != null) {
                    }
                }
            }
            invalidate();
            return;
        }
        this.dragSpeed = 1.0f;
    }

    public int getContentHeight() {
        return (int) (this.py + (this.videoTrack != null ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + (this.collageTracks.isEmpty() ? 0.0f : getCollageHeight() + AndroidUtilities.dp(4.0f)) + (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + (this.hasAudio ? AndroidUtilities.dp(4.0f) + getAudioHeight() : 0.0f) + this.py);
    }

    public long getMaxScrollDuration() {
        return this.collageTracks.isEmpty() ? 120000L : 70000L;
    }

    public int getTimelineHeight() {
        return AndroidUtilities.lerp(this.py + AndroidUtilities.dp(28.0f) + this.py, getContentHeight(), this.openT.get());
    }

    public boolean isDragging() {
        return this.dragged;
    }

    public void normalizeScrollByVideo() {
        long min = Math.min(getBaseDuration(), getMaxScrollDuration());
        Track track = this.videoTrack;
        float f = (track.right + track.left) / 2.0f;
        long j = track.duration;
        this.scroll = Utilities.clamp((long) ((f * j) - (min / 2.0f)), j - min, 0L);
        invalidate();
    }

    public boolean onBackPressed() {
        boolean z = false;
        if (this.audioSelected) {
            this.audioSelected = false;
            z = true;
            if (this.hasRound && this.videoTrack == null) {
                this.roundSelected = true;
                TimelineDelegate timelineDelegate = this.delegate;
                if (timelineDelegate != null) {
                    timelineDelegate.onRoundSelectChange(true);
                }
            }
        }
        return z;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        this.audioAuthorPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.audioTitlePaint.setTextSize(AndroidUtilities.dp(12.0f));
        int dp = AndroidUtilities.dp(12.0f);
        this.px = dp;
        int dp2 = AndroidUtilities.dp(5.0f);
        this.py = dp2;
        setPadding(dp, dp2, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(5.0f));
        int size = View.MeasureSpec.getSize(i);
        this.w = size;
        int dp3 = AndroidUtilities.dp(heightDp());
        this.h = dp3;
        setMeasuredDimension(size, dp3);
        int dp4 = AndroidUtilities.dp(10.0f);
        this.ph = dp4;
        this.sw = (this.w - (dp4 * 2)) - (this.px * 2);
        Track track = this.videoTrack;
        if (track != null && track.path != null && track.thumbs == null) {
            track.setupThumbs(false);
        }
        if (!this.collageTracks.isEmpty()) {
            Iterator it = this.collageTracks.iterator();
            while (it.hasNext()) {
                Track track2 = (Track) it.next();
                if (track2.path != null && track2.thumbs == null) {
                    track2.setupThumbs(false);
                    track2.setupWaveform(false);
                }
            }
        }
        if (this.audioPath == null || this.waveform != null) {
            return;
        }
        setupAudioWaveform();
    }

    /* JADX WARN: Code restructure failed: missing block: B:107:0x0210, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0902, code lost:
    
        r26.dragSpeed = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0906, code lost:
    
        r26.dragged = true;
        r26.draggingProgress = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0268, code lost:
    
        if (r1 != null) goto L121;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0305, code lost:
    
        if (r26.dragged != false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x04a5, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x04ba, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x04ce, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x04fa, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0701, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x08fe, code lost:
    
        if (r26.dragged == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0a28, code lost:
    
        if (r1 != null) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0a5c, code lost:
    
        r1.onRoundSelectChange(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0a4a, code lost:
    
        if (r1 != null) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0a5a, code lost:
    
        if (r1 != null) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0aaf, code lost:
    
        if (java.lang.Math.abs(r1) > org.telegram.messenger.AndroidUtilities.dp(100.0f)) goto L501;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0ad3, code lost:
    
        r3 = java.lang.Math.min(r26.videoTrack.duration, getMaxScrollDuration());
        r5 = r26.px;
        r6 = r5;
        r8 = r3;
        r9 = r26.sw;
        r7 = (int) (((r26.scroll / r8) * r9) + r6);
        r3 = (int) (r6 + (((r26.videoTrack.duration - r3) / r8) * r9));
        r26.scrolling = true;
        r4 = r26.scroller;
        r26.wasScrollX = r7;
        r24 = 0;
        r25 = 0;
        r19 = 0;
        r21 = 0;
        r17 = r4;
        r18 = r7;
        r20 = -r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0ad1, code lost:
    
        if (java.lang.Math.abs(r1) > org.telegram.messenger.AndroidUtilities.dp(100.0f)) goto L501;
     */
    /* JADX WARN: Removed duplicated region for block: B:137:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:485:0x0c55  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x0c5f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0c7a  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01ae  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        Runnable runnable;
        VelocityTracker velocityTracker;
        TimelineDelegate timelineDelegate;
        VelocityTracker velocityTracker2;
        int xVelocity;
        float min;
        int i;
        Track track;
        long j;
        int i2;
        boolean z2;
        long j2;
        float f;
        float f2;
        float f3;
        float f4;
        long j3;
        VelocityTracker velocityTracker3;
        VelocityTracker velocityTracker4;
        int xVelocity2;
        VelocityTracker velocityTracker5;
        TimelineDelegate timelineDelegate2;
        Runnable runnable2;
        VelocityTracker velocityTracker6;
        float min2;
        float max;
        float min3;
        float max2;
        boolean z3;
        TimelineDelegate timelineDelegate3;
        int i3;
        int i4;
        TimelineDelegate timelineDelegate4;
        float f5;
        float f6;
        TimelineDelegate timelineDelegate5;
        VelocityTracker obtain;
        if (this.videoTrack == null && this.collageTracks.isEmpty() && !this.hasAudio && !this.hasRound) {
            return false;
        }
        float timelineHeight = this.h - getTimelineHeight();
        if (motionEvent.getAction() == 0 && motionEvent.getY() < timelineHeight) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (motionEvent.getAction() == 0) {
            Runnable runnable3 = this.askExactSeek;
            if (runnable3 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable3);
                this.askExactSeek = null;
            }
            this.scroller.abortAnimation();
            this.pressHandleCollageIndex = -1;
            this.pressHandle = detectHandle(motionEvent);
            this.pressType = -1;
            this.pressCollageIndex = -1;
            int i5 = this.h - this.py;
            if (!this.open && this.timelineBounds.contains(motionEvent.getX(), motionEvent.getY())) {
                this.pressType = 10;
                this.pressHandle = -1;
            }
            if (this.pressType == -1 && this.videoTrack != null) {
                float f7 = i5;
                if (motionEvent.getY() < f7 && motionEvent.getY() > (f7 - getVideoHeight()) - AndroidUtilities.dp(2.0f)) {
                    this.pressType = 0;
                }
                i5 = (int) (f7 - (getVideoHeight() + AndroidUtilities.dp(4.0f)));
            }
            if (this.pressType == -1 && !this.collageTracks.isEmpty()) {
                int i6 = 0;
                while (true) {
                    if (i6 >= this.collageTracks.size()) {
                        break;
                    }
                    float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), ((Track) this.collageTracks.get(i6)).selectedT.get());
                    float f8 = i5;
                    if (motionEvent.getY() < f8 && motionEvent.getY() > (f8 - lerp) - AndroidUtilities.dp(2.0f)) {
                        this.pressType = 3;
                        this.pressCollageIndex = i6;
                        break;
                    }
                    i5 = (int) (f8 - (lerp + AndroidUtilities.dp(4.0f)));
                    i6++;
                }
            }
            if (this.pressType == -1 && this.hasRound) {
                float f9 = i5;
                if (motionEvent.getY() < f9 && motionEvent.getY() > (f9 - getRoundHeight()) - AndroidUtilities.dp(2.0f)) {
                    this.pressType = 1;
                }
                i5 = (int) (f9 - (getRoundHeight() + AndroidUtilities.dp(4.0f)));
            }
            if (this.pressType == -1 && this.hasAudio) {
                float f10 = i5;
                if (motionEvent.getY() < f10 && motionEvent.getY() > (f10 - getAudioHeight()) - AndroidUtilities.dp(2.0f)) {
                    this.pressType = 2;
                }
                getAudioHeight();
                AndroidUtilities.dp(4.0f);
            }
            this.pressTime = System.currentTimeMillis();
            int i7 = this.pressHandle;
            this.draggingProgress = i7 == 0 || i7 == -1 || i7 == 1;
            this.hadDragChange = false;
            if (i7 == 1 || i7 == 5 || i7 == 8) {
                obtain = VelocityTracker.obtain();
            } else {
                VelocityTracker velocityTracker7 = this.velocityTracker;
                if (velocityTracker7 != null) {
                    velocityTracker7.recycle();
                    obtain = null;
                }
                this.dragSpeed = 1.0f;
                this.dragged = false;
                this.lastX = motionEvent.getX();
                if (!this.isCover) {
                    AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                    AndroidUtilities.runOnUIThread(this.onLongPress, ViewConfiguration.getLongPressTimeout());
                }
            }
            this.velocityTracker = obtain;
            this.dragSpeed = 1.0f;
            this.dragged = false;
            this.lastX = motionEvent.getX();
            if (!this.isCover) {
            }
        } else if (motionEvent.getAction() == 2) {
            float x = motionEvent.getX() - this.lastX;
            if (this.open && (this.dragged || Math.abs(x) > AndroidUtilities.touchSlop)) {
                long min4 = Math.min(getBaseDuration(), getMaxScrollDuration());
                Track track2 = this.videoTrack;
                if (track2 != null && this.pressHandle == 1) {
                    this.scroll = (long) Utilities.clamp(this.scroll - ((x / this.sw) * min4), track2.duration - min4, 0.0f);
                    invalidate();
                } else if (track2 == null || !((i4 = this.pressHandle) == 2 || i4 == 3 || i4 == 4)) {
                    int i8 = this.pressHandle;
                    if (i8 == 6 || i8 == 7 || i8 == 8) {
                        float f11 = (x / this.sw) * (min4 / this.audioDuration);
                        if (i8 == 6) {
                            float minAudioSelect = this.audioRight - (minAudioSelect() / this.audioDuration);
                            float max3 = Math.max(0L, this.scroll - this.audioOffset);
                            float f12 = this.audioDuration;
                            float f13 = max3 / f12;
                            Track track3 = this.videoTrack;
                            if (track3 != null || (track3 = this.collageMain) != null) {
                                max = Math.max(f13, (((track3.left * track3.duration) + this.scroll) - this.audioOffset) / f12);
                            } else if (this.hasRound) {
                                max = Math.max(f13, (((this.roundLeft * this.roundDuration) + this.scroll) - this.audioOffset) / f12);
                            } else {
                                max = Math.max(f13, this.audioRight - (59000.0f / f12));
                                if (!this.hadDragChange && f11 < 0.0f && this.audioLeft <= this.audioRight - (59000.0f / this.audioDuration)) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f14 = this.audioLeft;
                            float clamp = Utilities.clamp(f14 + f11, minAudioSelect, max);
                            this.audioLeft = clamp;
                            if (Math.abs(f14 - clamp) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate6 = this.delegate;
                            if (timelineDelegate6 != null) {
                                timelineDelegate6.onAudioOffsetChange(this.audioOffset + ((long) (this.audioLeft * this.audioDuration)));
                            }
                            TimelineDelegate timelineDelegate7 = this.delegate;
                            if (timelineDelegate7 != null) {
                                timelineDelegate7.onAudioLeftChange(this.audioLeft);
                            }
                        } else if (i8 == 7) {
                            float min5 = Math.min(1.0f, Math.max(0L, (this.scroll - this.audioOffset) + min4) / this.audioDuration);
                            float f15 = this.audioLeft;
                            float minAudioSelect2 = minAudioSelect();
                            float f16 = this.audioDuration;
                            float f17 = f15 + (minAudioSelect2 / f16);
                            Track track4 = this.videoTrack;
                            if (track4 != null || (track4 = this.collageMain) != null) {
                                min2 = Math.min(min5, (((track4.right * track4.duration) + this.scroll) - this.audioOffset) / f16);
                            } else if (this.hasRound) {
                                min2 = Math.min(min5, (((this.roundRight * this.roundDuration) + this.scroll) - this.audioOffset) / f16);
                            } else {
                                min2 = Math.min(min5, this.audioLeft + (59000.0f / f16));
                                if (!this.hadDragChange && f11 > 0.0f && this.audioRight >= this.audioLeft + (59000.0f / this.audioDuration)) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f18 = this.audioRight;
                            float clamp2 = Utilities.clamp(f18 + f11, min2, f17);
                            this.audioRight = clamp2;
                            if (Math.abs(f18 - clamp2) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate8 = this.delegate;
                            if (timelineDelegate8 != null) {
                                timelineDelegate8.onAudioRightChange(this.audioRight);
                            }
                        }
                        if (this.pressHandle == 8) {
                            float min6 = f11 > 0.0f ? Math.min(Math.max(0.0f, Math.min(1.0f, Math.max(0L, (this.scroll - this.audioOffset) + min4) / this.audioDuration) - this.audioRight), f11) : Math.max(Math.min(0.0f, (Math.max(0L, this.scroll - this.audioOffset) / this.audioDuration) - this.audioLeft), f11);
                            float f19 = this.audioLeft + min6;
                            this.audioLeft = f19;
                            this.audioRight += min6;
                            TimelineDelegate timelineDelegate9 = this.delegate;
                            if (timelineDelegate9 != null) {
                                timelineDelegate9.onAudioLeftChange(f19);
                                this.delegate.onAudioOffsetChange(this.audioOffset + ((long) (this.audioLeft * this.audioDuration)));
                                this.delegate.onAudioRightChange(this.audioRight);
                            }
                            TimelineDelegate timelineDelegate10 = this.delegate;
                            if (timelineDelegate10 != null) {
                                timelineDelegate10.onProgressDragChange(true);
                            }
                        }
                        if (this.videoTrack == null && !this.hasRound) {
                            this.progress = (long) (this.audioLeft * this.audioDuration);
                            TimelineDelegate timelineDelegate11 = this.delegate;
                            if (timelineDelegate11 != null) {
                                timelineDelegate11.onProgressDragChange(true);
                                this.delegate.onProgressChange(this.progress, false);
                            }
                        }
                        invalidate();
                    } else if (i8 == 10 || i8 == 11 || i8 == 12) {
                        float f20 = (x / this.sw) * (min4 / this.roundDuration);
                        if (i8 == 10) {
                            float minAudioSelect3 = this.roundRight - (minAudioSelect() / this.roundDuration);
                            float max4 = Math.max(0L, this.scroll - this.roundOffset);
                            float f21 = this.roundDuration;
                            float f22 = max4 / f21;
                            Track track5 = this.videoTrack;
                            if (track5 == null && (track5 = this.collageMain) == null) {
                                max2 = Math.max(f22, this.roundRight - (59000.0f / f21));
                                if (!this.hadDragChange && f20 < 0.0f && this.roundLeft <= this.roundRight - (59000.0f / this.roundDuration)) {
                                    this.pressHandle = 8;
                                }
                            } else {
                                max2 = Math.max(f22, (((track5.left * track5.duration) + this.scroll) - this.roundOffset) / f21);
                            }
                            float f23 = this.roundLeft;
                            float clamp3 = Utilities.clamp(f23 + f20, minAudioSelect3, max2);
                            this.roundLeft = clamp3;
                            if (Math.abs(f23 - clamp3) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate12 = this.delegate;
                            if (timelineDelegate12 != null) {
                                timelineDelegate12.onRoundOffsetChange(this.roundOffset + ((long) (this.roundLeft * this.roundDuration)));
                            }
                            TimelineDelegate timelineDelegate13 = this.delegate;
                            if (timelineDelegate13 != null) {
                                timelineDelegate13.onRoundLeftChange(this.roundLeft);
                            }
                        } else if (i8 == 11) {
                            float min7 = Math.min(1.0f, Math.max(0L, (this.scroll - this.roundOffset) + min4) / this.roundDuration);
                            float f24 = this.roundLeft;
                            float minAudioSelect4 = minAudioSelect();
                            float f25 = this.roundDuration;
                            float f26 = f24 + (minAudioSelect4 / f25);
                            Track track6 = this.videoTrack;
                            if (track6 != null) {
                                min7 = Math.min(min7, (((track6.right * track6.duration) + this.scroll) - this.roundOffset) / f25);
                            }
                            Track track7 = this.collageMain;
                            if (track7 != null) {
                                min3 = Math.min(min7, (((track7.right * track7.duration) + this.scroll) - this.roundOffset) / this.roundDuration);
                            } else {
                                min3 = Math.min(min7, this.roundLeft + (59000.0f / this.roundDuration));
                                if (!this.hadDragChange && f20 > 0.0f && this.roundRight >= this.roundLeft + (59000.0f / this.roundDuration)) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f27 = this.roundRight;
                            float clamp4 = Utilities.clamp(f27 + f20, min3, f26);
                            this.roundRight = clamp4;
                            if (Math.abs(f27 - clamp4) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate14 = this.delegate;
                            if (timelineDelegate14 != null) {
                                timelineDelegate14.onRoundRightChange(this.roundRight);
                            }
                        }
                        if (this.pressHandle == 12) {
                            float min8 = f20 > 0.0f ? Math.min(Math.min(1.0f, Math.max(0L, (this.scroll - this.roundOffset) + min4) / this.roundDuration) - this.roundRight, f20) : Math.max((Math.max(0L, this.scroll - this.roundOffset) / this.roundDuration) - this.roundLeft, f20);
                            float f28 = this.roundLeft + min8;
                            this.roundLeft = f28;
                            this.roundRight += min8;
                            TimelineDelegate timelineDelegate15 = this.delegate;
                            if (timelineDelegate15 != null) {
                                timelineDelegate15.onRoundLeftChange(f28);
                                this.delegate.onRoundOffsetChange(this.roundOffset + ((long) (this.roundLeft * this.roundDuration)));
                                this.delegate.onRoundRightChange(this.roundRight);
                            }
                            TimelineDelegate timelineDelegate16 = this.delegate;
                            if (timelineDelegate16 != null) {
                                timelineDelegate16.onProgressDragChange(true);
                            }
                        }
                        if (this.videoTrack == null) {
                            this.progress = (long) (this.roundLeft * this.roundDuration);
                            TimelineDelegate timelineDelegate17 = this.delegate;
                            if (timelineDelegate17 != null) {
                                timelineDelegate17.onProgressDragChange(true);
                                this.delegate.onProgressChange(this.progress, false);
                            }
                        }
                        invalidate();
                    } else {
                        int i9 = this.pressHandleCollageIndex;
                        if (i9 < 0 || i9 >= this.collageTracks.size() || !((i3 = this.pressHandle) == 13 || i3 == 14 || i3 == 15)) {
                            int i10 = this.pressHandle;
                            if (i10 == 5) {
                                moveAudioOffset((x / this.sw) * min4);
                            } else if (i10 == 9) {
                                moveRoundOffset((x / this.sw) * min4);
                            } else {
                                int i11 = this.pressHandleCollageIndex;
                                if (i11 >= 0 && i11 < this.collageTracks.size() && this.pressHandle == 16) {
                                    moveCollageOffset((Track) this.collageTracks.get(this.pressHandleCollageIndex), (x / this.sw) * min4);
                                } else if (this.draggingProgress) {
                                    setProgressAt(motionEvent.getX(), currentTimeMillis - this.lastTime < 350);
                                    if (this.dragged || (timelineDelegate3 = this.delegate) == null) {
                                        z3 = true;
                                    } else {
                                        z3 = true;
                                        timelineDelegate3.onProgressDragChange(true);
                                    }
                                    if (!this.dragged) {
                                        this.dragSpeed = 1.0f;
                                    }
                                    this.dragged = z3;
                                }
                            }
                        } else {
                            Track track8 = (Track) this.collageTracks.get(this.pressHandleCollageIndex);
                            float f29 = (x / this.sw) * (min4 / track8.duration);
                            int i12 = this.pressHandle;
                            if (i12 == 13) {
                                float minAudioSelect5 = track8.right - (minAudioSelect() / track8.duration);
                                float max5 = Math.max(0L, this.scroll - track8.offset);
                                float f30 = track8.duration;
                                float f31 = max5 / f30;
                                if (track8 == this.collageMain) {
                                    f31 = Math.max(f31, track8.right - (59000.0f / f30));
                                    if (!this.hadDragChange && f29 < 0.0f && track8.left <= track8.right - (59000.0f / track8.duration)) {
                                        this.pressHandle = 15;
                                    }
                                }
                                float f32 = track8.left;
                                float clamp5 = Utilities.clamp(f32 + f29, minAudioSelect5, f31);
                                track8.left = clamp5;
                                if (Math.abs(f32 - clamp5) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate18 = this.delegate;
                                if (timelineDelegate18 != null) {
                                    timelineDelegate18.onVideoOffsetChange(track8.index, track8.offset);
                                }
                                TimelineDelegate timelineDelegate19 = this.delegate;
                                if (timelineDelegate19 != null) {
                                    timelineDelegate19.onVideoLeftChange(track8.index, track8.left);
                                }
                            } else if (i12 == 14) {
                                float min9 = Math.min(1.0f, Math.max(0L, (this.scroll - track8.offset) + min4) / track8.duration);
                                float f33 = track8.left;
                                float minAudioSelect6 = minAudioSelect();
                                float f34 = track8.duration;
                                float f35 = f33 + (minAudioSelect6 / f34);
                                if (track8 == this.collageMain) {
                                    min9 = Math.min(min9, track8.left + (59000.0f / f34));
                                    if (!this.hadDragChange && f29 > 0.0f && track8.right >= track8.left + (59000.0f / track8.duration)) {
                                        this.pressHandle = 15;
                                    }
                                }
                                float f36 = track8.right;
                                float clamp6 = Utilities.clamp(f36 + f29, min9, f35);
                                track8.right = clamp6;
                                if (Math.abs(f36 - clamp6) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate20 = this.delegate;
                                if (timelineDelegate20 != null) {
                                    timelineDelegate20.onVideoRightChange(track8.index, track8.right);
                                }
                            }
                            if (this.pressHandle == 15) {
                                float min10 = f29 > 0.0f ? Math.min(Math.min(1.0f, Math.max(0L, (this.scroll - track8.offset) + min4) / track8.duration) - track8.right, f29) : Math.max((Math.max(0L, this.scroll - track8.offset) / track8.duration) - track8.left, f29);
                                float f37 = track8.left + min10;
                                track8.left = f37;
                                track8.right += min10;
                                TimelineDelegate timelineDelegate21 = this.delegate;
                                if (timelineDelegate21 != null) {
                                    timelineDelegate21.onVideoLeftChange(track8.index, f37);
                                    this.delegate.onVideoOffsetChange(track8.index, track8.offset);
                                    this.delegate.onVideoRightChange(track8.index, track8.right);
                                }
                                TimelineDelegate timelineDelegate22 = this.delegate;
                                if (timelineDelegate22 != null) {
                                    timelineDelegate22.onProgressDragChange(true);
                                }
                            }
                            invalidate();
                        }
                    }
                } else {
                    float f38 = track2.duration;
                    float f39 = (x / this.sw) * (min4 / f38);
                    if (i4 == 2) {
                        track2.left = Utilities.clamp(track2.left + f39, track2.right - (1000.0f / f38), 0.0f);
                        TimelineDelegate timelineDelegate23 = this.delegate;
                        if (timelineDelegate23 != null) {
                            timelineDelegate23.onVideoLeftChange(false, this.videoTrack.left);
                        }
                        Track track9 = this.videoTrack;
                        float f40 = track9.right;
                        float f41 = track9.left;
                        float f42 = 59000.0f / track9.duration;
                        if (f40 - f41 > f42) {
                            track9.right = Math.min(1.0f, f41 + f42);
                            timelineDelegate4 = this.delegate;
                        }
                        float f43 = this.progress;
                        Track track10 = this.videoTrack;
                        float f44 = track10.duration;
                        f5 = f43 / f44;
                        f6 = track10.left;
                        if (f5 >= f6 || f5 > track10.right) {
                            long j4 = (long) (f6 * f44);
                            this.progress = j4;
                            timelineDelegate5 = this.delegate;
                            if (timelineDelegate5 != null) {
                                timelineDelegate5.onProgressChange(j4, false);
                            }
                        }
                        invalidate();
                    } else {
                        if (i4 == 3) {
                            track2.right = Utilities.clamp(track2.right + f39, 1.0f, track2.left + (1000.0f / f38));
                            TimelineDelegate timelineDelegate24 = this.delegate;
                            if (timelineDelegate24 != null) {
                                timelineDelegate24.onVideoRightChange(false, this.videoTrack.right);
                            }
                            Track track11 = this.videoTrack;
                            float f45 = track11.right;
                            float f46 = 59000.0f / track11.duration;
                            if (f45 - track11.left > f46) {
                                track11.left = Math.max(0.0f, f45 - f46);
                                TimelineDelegate timelineDelegate25 = this.delegate;
                                if (timelineDelegate25 != null) {
                                    timelineDelegate25.onVideoLeftChange(false, this.videoTrack.left);
                                }
                            }
                        } else if (i4 == 4) {
                            float min11 = f39 > 0.0f ? Math.min(1.0f - track2.right, f39) : Math.max(-track2.left, f39);
                            Track track12 = this.videoTrack;
                            float f47 = track12.left + min11;
                            track12.left = f47;
                            track12.right += min11;
                            TimelineDelegate timelineDelegate26 = this.delegate;
                            if (timelineDelegate26 != null) {
                                timelineDelegate26.onVideoLeftChange(false, f47);
                                timelineDelegate4 = this.delegate;
                                timelineDelegate4.onVideoRightChange(false, this.videoTrack.right);
                            }
                        }
                        float f432 = this.progress;
                        Track track102 = this.videoTrack;
                        float f442 = track102.duration;
                        f5 = f432 / f442;
                        f6 = track102.left;
                        if (f5 >= f6) {
                        }
                        long j42 = (long) (f6 * f442);
                        this.progress = j42;
                        timelineDelegate5 = this.delegate;
                        if (timelineDelegate5 != null) {
                        }
                        invalidate();
                    }
                }
                this.lastX = motionEvent.getX();
            }
            if (this.dragged) {
                AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
            }
            int i13 = this.pressHandle;
            if ((i13 == 1 || i13 == 5 || i13 == 8) && (velocityTracker6 = this.velocityTracker) != null) {
                velocityTracker6.addMovement(motionEvent);
            }
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
            this.scroller.abortAnimation();
            if (motionEvent.getAction() == 1) {
                if ((System.currentTimeMillis() - this.pressTime > ViewConfiguration.getTapTimeout() || this.dragged) && this.open) {
                    int i14 = this.pressHandle;
                    int i15 = MediaDataController.MAX_STYLE_RUNS_COUNT;
                    if (i14 == 16 && (velocityTracker5 = this.velocityTracker) != null) {
                        velocityTracker5.computeCurrentVelocity(MediaDataController.MAX_STYLE_RUNS_COUNT);
                        xVelocity2 = (int) this.velocityTracker.getXVelocity();
                        this.scrollingVideo = true;
                        if (this.videoTrack != null) {
                        }
                    } else if (i14 != 1 || (velocityTracker4 = this.velocityTracker) == null) {
                        if ((i14 == 5 || (i14 == 8 && !this.dragged)) && this.audioSelected && (velocityTracker2 = this.velocityTracker) != null) {
                            if (this.videoTrack == null) {
                                i15 = 1500;
                            }
                            velocityTracker2.computeCurrentVelocity(i15);
                            xVelocity = (int) this.velocityTracker.getXVelocity();
                            this.scrollingVideo = false;
                            if (Math.abs(xVelocity) > AndroidUtilities.dp(100.0f)) {
                                min = Math.min(getBaseDuration(), getMaxScrollDuration());
                                i = (int) (this.px + this.ph + ((this.audioOffset / min) * this.sw));
                                track = this.videoTrack;
                                if (track != null) {
                                    float f48 = track.right;
                                    f3 = track.duration;
                                    f4 = f48 * f3;
                                    j3 = this.audioDuration;
                                    j2 = (long) (f4 - 0);
                                    f = track.left * f3;
                                    f2 = j3;
                                    j = (long) (f - f2);
                                    i2 = xVelocity;
                                    z2 = true;
                                } else if (this.hasRound) {
                                    float f49 = this.roundRight;
                                    float f50 = this.roundDuration;
                                    long j5 = this.audioDuration;
                                    j2 = (long) ((f49 * f50) - 0);
                                    f = this.roundLeft * f50;
                                    f2 = j5;
                                    j = (long) (f - f2);
                                    i2 = xVelocity;
                                    z2 = true;
                                } else {
                                    j = -(this.audioDuration - Math.min(getBaseDuration(), getMaxScrollDuration()));
                                    i2 = xVelocity;
                                    z2 = true;
                                    j2 = 0;
                                }
                            }
                        } else if ((i14 == 9 || (i14 == 12 && !this.dragged)) && this.roundSelected && (velocityTracker3 = this.velocityTracker) != null) {
                            if (this.videoTrack == null) {
                                i15 = 1500;
                            }
                            velocityTracker3.computeCurrentVelocity(i15);
                            xVelocity = (int) this.velocityTracker.getXVelocity();
                            this.scrollingVideo = false;
                            if (Math.abs(xVelocity) > AndroidUtilities.dp(100.0f)) {
                                min = Math.min(getBaseDuration(), getMaxScrollDuration());
                                i = (int) (this.px + this.ph + ((this.roundOffset / min) * this.sw));
                                track = this.videoTrack;
                                if (track != null) {
                                    float f51 = track.right;
                                    f3 = track.duration;
                                    f4 = f51 * f3;
                                    j3 = this.roundDuration;
                                    j2 = (long) (f4 - 0);
                                    f = track.left * f3;
                                    f2 = j3;
                                    j = (long) (f - f2);
                                    i2 = xVelocity;
                                    z2 = true;
                                } else {
                                    j = -(this.roundDuration - Math.min(getBaseDuration(), getMaxScrollDuration()));
                                    i2 = xVelocity;
                                    j2 = 0;
                                    z2 = true;
                                }
                            }
                        }
                        this.scrolling = z2;
                        Scroller scroller = this.scroller;
                        this.wasScrollX = i;
                        float f52 = this.px + this.ph;
                        float f53 = this.sw;
                        int i16 = (int) (((j / min) * f53) + f52);
                        int i17 = (int) (f52 + ((j2 / min) * f53));
                        int i18 = 0;
                        int i19 = 0;
                        int i20 = 0;
                        int i21 = 0;
                        Scroller scroller2 = scroller;
                        int i22 = i;
                        scroller2.fling(i22, i20, i2, i21, i16, i17, i18, i19);
                        z = false;
                        runnable = this.askExactSeek;
                        if (runnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(runnable);
                            this.askExactSeek = null;
                        }
                        if (this.dragged && z && (timelineDelegate = this.delegate) != null) {
                            timelineDelegate.onProgressDragChange(false);
                        }
                        this.dragSpeed = 1.0f;
                        this.dragged = false;
                        this.draggingProgress = false;
                        this.pressTime = -1L;
                        this.pressHandle = -1;
                        velocityTracker = this.velocityTracker;
                        if (velocityTracker != null) {
                            velocityTracker.recycle();
                            this.velocityTracker = null;
                        }
                    } else {
                        velocityTracker4.computeCurrentVelocity(MediaDataController.MAX_STYLE_RUNS_COUNT);
                        xVelocity2 = (int) this.velocityTracker.getXVelocity();
                        this.scrollingVideo = true;
                        if (this.videoTrack != null) {
                        }
                    }
                } else if (this.open) {
                    if (!this.isCover || this.videoTrack == null) {
                        int i23 = this.pressType;
                        if (i23 == 3) {
                            int i24 = (this.audioSelected || this.roundSelected) ? -1 : this.collageSelected;
                            int i25 = this.pressCollageIndex;
                            if (i24 != i25) {
                                this.audioSelected = false;
                                this.roundSelected = false;
                                this.collageSelected = i25;
                                if (this.delegate != null && i25 >= 0 && i25 < this.collageTracks.size()) {
                                    this.delegate.onVideoSelected(((Track) this.collageTracks.get(this.pressCollageIndex)).index);
                                }
                            }
                        }
                        if (i23 == 2 && !this.audioSelected) {
                            this.audioSelected = true;
                            this.roundSelected = false;
                            timelineDelegate2 = this.delegate;
                        } else if (i23 == 1 && !this.roundSelected) {
                            this.audioSelected = false;
                            this.roundSelected = true;
                            TimelineDelegate timelineDelegate27 = this.delegate;
                            if (timelineDelegate27 != null) {
                                timelineDelegate27.onRoundSelectChange(true);
                            }
                        } else if (i23 != 2 && this.audioSelected) {
                            this.audioSelected = false;
                            this.roundSelected = false;
                            timelineDelegate2 = this.delegate;
                        } else if (i23 == 1 || !this.roundSelected) {
                            long j6 = this.progress;
                            if (setProgressAt(motionEvent.getX(), false) && Math.abs(this.progress - j6) > 400) {
                                this.loopProgressFrom = j6;
                                this.loopProgress.set(1.0f, true);
                            }
                        } else {
                            this.audioSelected = false;
                            this.roundSelected = false;
                            timelineDelegate2 = this.delegate;
                        }
                        runnable = this.askExactSeek;
                        if (runnable != null) {
                        }
                        if (this.dragged) {
                            timelineDelegate.onProgressDragChange(false);
                        }
                        this.dragSpeed = 1.0f;
                        this.dragged = false;
                        this.draggingProgress = false;
                        this.pressTime = -1L;
                        this.pressHandle = -1;
                        velocityTracker = this.velocityTracker;
                        if (velocityTracker != null) {
                        }
                    } else {
                        long clamp7 = (long) Utilities.clamp(((((motionEvent.getX() - this.px) - this.ph) / this.sw) * Math.min(getBaseDuration(), getMaxScrollDuration())) + this.scroll, getBaseDuration(), 0.0f);
                        Track track13 = this.videoTrack;
                        float f54 = track13.right - track13.left;
                        track13.left = (clamp7 / getBaseDuration()) * (1.0f - f54);
                        Track track14 = this.videoTrack;
                        float f55 = track14.left;
                        track14.right = f54 + f55;
                        TimelineDelegate timelineDelegate28 = this.delegate;
                        if (timelineDelegate28 != null) {
                            timelineDelegate28.onVideoLeftChange(true, f55);
                            this.delegate.onVideoRightChange(true, this.videoTrack.right);
                        }
                    }
                    invalidate();
                } else if (this.pressType == 10 && (runnable2 = this.onTimelineClick) != null) {
                    runnable2.run();
                }
            }
            z = true;
            runnable = this.askExactSeek;
            if (runnable != null) {
            }
            if (this.dragged) {
            }
            this.dragSpeed = 1.0f;
            this.dragged = false;
            this.draggingProgress = false;
            this.pressTime = -1L;
            this.pressHandle = -1;
            velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
            }
        }
        this.lastTime = System.currentTimeMillis();
        return true;
    }

    public void selectRound(boolean z) {
        if (z && this.hasRound) {
            this.roundSelected = true;
            this.audioSelected = false;
        } else {
            this.roundSelected = false;
            this.audioSelected = this.hasAudio && this.videoTrack == null;
        }
        invalidate();
    }

    public void setAudio(String str, String str2, String str3, long j, long j2, float f, float f2, float f3, boolean z) {
        String str4;
        String str5;
        if (!TextUtils.equals(this.audioPath, str)) {
            AudioWaveformLoader audioWaveformLoader = this.waveform;
            if (audioWaveformLoader != null) {
                audioWaveformLoader.destroy();
                this.waveform = null;
                this.waveformIsLoaded = false;
            }
            this.audioPath = str;
            setupAudioWaveform();
        }
        this.audioPath = str;
        boolean z2 = !TextUtils.isEmpty(str);
        this.hasAudio = z2;
        if (z2) {
            str4 = str2;
            str5 = str3;
        } else {
            this.audioSelected = false;
            str4 = null;
            str5 = null;
        }
        if (TextUtils.isEmpty(str4)) {
            str4 = null;
        }
        if (TextUtils.isEmpty(str5)) {
            str5 = null;
        }
        if (this.hasAudio) {
            this.audioDuration = j;
            this.audioOffset = j2 - ((long) (j * f));
            this.audioLeft = f;
            this.audioRight = f2;
            this.audioVolume = f3;
            if (str4 != null) {
                StaticLayout staticLayout = new StaticLayout(str4, this.audioAuthorPaint, 99999, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.audioAuthor = staticLayout;
                this.audioAuthorWidth = staticLayout.getLineCount() > 0 ? this.audioAuthor.getLineWidth(0) : 0.0f;
                this.audioAuthorLeft = this.audioAuthor.getLineCount() > 0 ? this.audioAuthor.getLineLeft(0) : 0.0f;
            } else {
                this.audioAuthorWidth = 0.0f;
                this.audioAuthor = null;
            }
            if (str5 != null) {
                StaticLayout staticLayout2 = new StaticLayout(str5, this.audioTitlePaint, 99999, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.audioTitle = staticLayout2;
                this.audioTitleWidth = staticLayout2.getLineCount() > 0 ? this.audioTitle.getLineWidth(0) : 0.0f;
                this.audioTitleLeft = this.audioTitle.getLineCount() > 0 ? this.audioTitle.getLineLeft(0) : 0.0f;
            } else {
                this.audioTitleWidth = 0.0f;
                this.audioTitle = null;
            }
        }
        if (!z) {
            this.audioT.set(this.hasAudio, true);
        }
        invalidate();
    }

    public void setCollage(ArrayList<StoryEntry> arrayList) {
        VideoThumbsLoader videoThumbsLoader;
        for (int i = 0; i < this.collageTracks.size(); i++) {
            Track track = (Track) this.collageTracks.get(i);
            if (track != null && (videoThumbsLoader = track.thumbs) != null) {
                videoThumbsLoader.destroy();
            }
        }
        this.collageTracks.clear();
        for (int i2 = 0; i2 < this.collageWaveforms.size(); i2++) {
            AudioWaveformLoader audioWaveformLoader = (AudioWaveformLoader) this.collageWaveforms.get(i2);
            if (audioWaveformLoader != null) {
                audioWaveformLoader.destroy();
            }
        }
        this.collageWaveforms.clear();
        this.timelineWaveformMax.set(1.0f, true);
        if (arrayList != null) {
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                this.collageWaveforms.add(null);
                StoryEntry storyEntry = arrayList.get(i3);
                if (storyEntry.isVideo) {
                    Track track2 = new Track();
                    track2.index = i3;
                    track2.isRound = false;
                    track2.path = storyEntry.file.getAbsolutePath();
                    track2.duration = storyEntry.duration;
                    track2.offset = storyEntry.videoOffset;
                    track2.volume = storyEntry.videoVolume;
                    track2.left = storyEntry.videoLeft;
                    track2.right = storyEntry.videoRight;
                    track2.setupThumbs(false);
                    track2.setupWaveform(false);
                    this.collageTracks.add(track2);
                }
            }
        }
        sortCollage();
        this.collageSelected = 0;
    }

    public void setCover() {
        this.isCover = true;
    }

    public void setCoverVideo(long j, long j2) {
        this.coverStart = j;
        this.coverEnd = j2;
        Track track = this.videoTrack;
        if (track != null) {
            track.setupThumbs(true);
        }
    }

    public void setDelegate(TimelineDelegate timelineDelegate) {
        this.delegate = timelineDelegate;
    }

    public void setOnTimelineClick(Runnable runnable) {
        this.onTimelineClick = runnable;
    }

    public void setOpen(boolean z, boolean z2) {
        if (this.open == z && z2) {
            return;
        }
        this.open = z;
        if (!z2) {
            this.openT.set(z, true);
        }
        invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004a, code lost:
    
        if ((r4 + 240) >= (r7 * r9.audioRight)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006d, code lost:
    
        if ((r4 + 240) >= (r6 * r9.audioRight)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0023, code lost:
    
        if ((r4 + 240) >= (r7 * r0.right)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x006f, code lost:
    
        r9.loopProgressFrom = -1;
        r9.loopProgress.set(1.0f, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setProgress(long j) {
        Track track = this.videoTrack;
        if (track != null) {
            if (j < this.progress) {
                float f = j;
                float f2 = track.duration;
                if (f <= (track.left * f2) + 240.0f) {
                }
            }
        }
        if (this.hasAudio && !this.hasRound && track == null) {
            if (j < this.progress) {
                float f3 = j;
                float f4 = this.audioDuration;
                if (f3 <= (this.audioLeft * f4) + 240.0f) {
                }
            }
        }
        if (this.hasRound && track == null) {
            if (j < this.progress) {
                float f5 = j;
                float f6 = this.roundDuration;
                if (f5 <= (this.audioLeft * f6) + 240.0f) {
                }
            }
        }
        this.progress = j;
        invalidate();
    }

    public void setRound(String str, long j, long j2, float f, float f2, float f3, boolean z) {
        if (TextUtils.equals(this.roundPath, str)) {
            return;
        }
        VideoThumbsLoader videoThumbsLoader = this.roundThumbs;
        if (videoThumbsLoader != null) {
            videoThumbsLoader.destroy();
            this.roundThumbs = null;
        }
        long j3 = this.roundDuration;
        if (str != null) {
            this.roundPath = str;
            this.roundDuration = j;
            this.roundOffset = j2 - ((long) (j * f));
            this.roundLeft = f;
            this.roundRight = f2;
            this.roundVolume = f3;
            setupRoundThumbs();
            if (this.videoTrack == null) {
                this.audioSelected = false;
                this.roundSelected = true;
            }
        } else {
            this.roundPath = null;
            this.roundDuration = 1L;
            this.roundSelected = false;
        }
        this.hasRound = this.roundPath != null;
        if (j3 != j && this.videoTrack == null && this.waveform != null) {
            this.resetWaveform = true;
            setupAudioWaveform();
        }
        if (this.hasAudio && this.hasRound && this.videoTrack == null) {
            this.audioLeft = 0.0f;
            this.audioRight = Utilities.clamp(j / this.audioDuration, 1.0f, 0.0f);
        }
        if (!z) {
            this.roundSelectedT.set(this.roundSelected, true);
            this.audioSelectedT.set(this.audioSelected, true);
            this.roundT.set(this.hasRound, true);
        }
        invalidate();
    }

    public void setRoundNull(boolean z) {
        setRound(null, 0L, 0L, 0.0f, 0.0f, 0.0f, z);
    }

    public void setVideo(boolean z, String str, long j, float f) {
        Track track = this.videoTrack;
        if (TextUtils.equals(track == null ? null : track.path, str)) {
            return;
        }
        Track track2 = this.videoTrack;
        if (track2 != null) {
            VideoThumbsLoader videoThumbsLoader = track2.thumbs;
            if (videoThumbsLoader != null) {
                videoThumbsLoader.destroy();
                this.videoTrack.thumbs = null;
            }
            this.videoTrack = null;
        }
        if (str != null) {
            this.scroll = 0L;
            Track track3 = new Track();
            this.videoTrack = track3;
            track3.isRound = z;
            track3.path = str;
            track3.duration = j;
            track3.volume = f;
            track3.setupThumbs(false);
        } else {
            this.videoTrack = null;
            this.scroll = 0L;
        }
        if (!this.hasRound) {
            this.roundSelected = false;
        }
        this.progress = 0L;
        invalidate();
    }

    public void setVideoLeft(float f) {
        Track track = this.videoTrack;
        if (track == null) {
            return;
        }
        track.left = f;
        invalidate();
    }

    public void setVideoRight(float f) {
        Track track = this.videoTrack;
        if (track == null) {
            return;
        }
        track.right = f;
        invalidate();
    }

    public void sortCollage() {
        Collections.sort(this.collageTracks, new Comparator() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sortCollage$7;
                lambda$sortCollage$7 = TimelineView.lambda$sortCollage$7((TimelineView.Track) obj, (TimelineView.Track) obj2);
                return lambda$sortCollage$7;
            }
        });
        this.collageMain = this.collageTracks.isEmpty() ? null : (Track) this.collageTracks.get(0);
    }
}
