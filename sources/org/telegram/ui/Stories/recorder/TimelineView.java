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
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
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
    private final ViewGroup container;
    private long coverEnd;
    private long coverStart;
    private TimelineDelegate delegate;
    private boolean dragged;
    private boolean draggingProgress;
    private final LinearGradient ellipsizeGradient;
    private final Matrix ellipsizeMatrix;
    private final Paint ellipsizePaint;
    private int h;
    private boolean hadDragChange;
    private boolean hasAudio;
    private boolean hasRound;
    private boolean hasVideo;
    private boolean isCover;
    private boolean isMainVideoRound;
    private long lastTime;
    private float lastX;
    private final AnimatedFloat loopProgress;
    private long loopProgressFrom;
    private final Runnable onLongPress;
    private int ph;
    private int pressHandle;
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
    private boolean scrollingVideo;
    private final Path selectedVideoClipPath;
    final float[] selectedVideoRadii;
    private int sw;
    private VideoThumbsLoader thumbs;
    private VelocityTracker velocityTracker;
    private final RectF videoBounds;
    private final Path videoClipPath;
    private long videoDuration;
    private final Paint videoFramePaint;
    private float videoLeft;
    private String videoPath;
    private float videoRight;
    private final AnimatedFloat videoSelectedT;
    private float videoVolume;
    private int w;
    private int wasScrollX;
    private AudioWaveformLoader waveform;
    private boolean waveformIsLoaded;
    private final AnimatedFloat waveformLoaded;
    private final AnimatedFloat waveformMax;
    private final Paint waveformPaint;
    private final WaveformPath waveformPath;
    final float[] waveformRadii;

    /* JADX INFO: Access modifiers changed from: private */
    class AudioWaveformLoader {
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
            int round = Math.round((((this.duration * 1000) / Math.min(TimelineView.this.hasVideo ? TimelineView.this.videoDuration : TimelineView.this.hasRound ? TimelineView.this.roundDuration : this.duration * 1000, 120000L)) * i) / Math.round(AndroidUtilities.dpf2(3.3333f)));
            this.count = round;
            this.data = new short[round];
            if (this.duration <= 0 || this.inputFormat == null) {
                return;
            }
            if ("audio/mpeg".equals(str2) || "audio/mp3".equals(str2)) {
                this.waveformLoader = new FfmpegAudioWaveformLoader(str, round, new Utilities.Callback2() { // from class: org.telegram.ui.Stories.recorder.TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda1
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

    interface TimelineDelegate {

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

            public static void $default$onVideoRightChange(TimelineDelegate timelineDelegate, float f) {
            }

            public static void $default$onVideoVolumeChange(TimelineDelegate timelineDelegate, float f) {
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

        void onVideoLeftChange(float f);

        void onVideoRightChange(float f);

        void onVideoVolumeChange(float f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class VideoThumbsLoader {
        private Path clipPath;
        private final int count;
        private boolean destroyed;
        private long duration;
        private final int frameHeight;
        private final long frameIterator;
        private final int frameWidth;
        private final boolean isRound;
        private MediaMetadataRetriever metadataRetriever;
        private long nextFrame;
        private final ArrayList frames = new ArrayList();
        private boolean loading = false;
        private final Paint bitmapPaint = new Paint(3);

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

        /* JADX WARN: Code restructure failed: missing block: B:20:0x007a, code lost:
        
            if (r0 != 270) goto L26;
         */
        /* JADX WARN: Removed duplicated region for block: B:22:0x008a  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x009e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00e8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public VideoThumbsLoader(boolean z, String str, int i, int i2, Long l, long j, long j2, long j3) {
            int i3;
            String extractMetadata;
            this.isRound = z;
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.metadataRetriever = mediaMetadataRetriever;
            long j4 = 120000;
            try {
                mediaMetadataRetriever.setDataSource(str);
                String extractMetadata2 = this.metadataRetriever.extractMetadata(9);
                if (extractMetadata2 != null) {
                    j4 = Long.parseLong(extractMetadata2);
                    this.duration = j4;
                }
                String extractMetadata3 = this.metadataRetriever.extractMetadata(18);
                i3 = extractMetadata3 != null ? Integer.parseInt(extractMetadata3) : 0;
                try {
                    String extractMetadata4 = this.metadataRetriever.extractMetadata(19);
                    r5 = extractMetadata4 != null ? Integer.parseInt(extractMetadata4) : 0;
                    extractMetadata = this.metadataRetriever.extractMetadata(24);
                } catch (Exception e) {
                    e = e;
                    this.metadataRetriever = null;
                    FileLog.e(e);
                    int i4 = i3;
                    i3 = r5;
                    r5 = i4;
                    if (l != null) {
                    }
                    if (j2 != -1) {
                    }
                    float clamp = Utilities.clamp((r5 != 0 || i3 == 0) ? 1.0f : r5 / i3, 1.3333334f, 0.5625f);
                    this.frameHeight = Math.max(1, i2);
                    this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp));
                    int ceil = (int) Math.ceil(((Math.max(j4, j) / j) * i) / r0);
                    this.count = ceil;
                    long j5 = (long) (j4 / ceil);
                    this.frameIterator = j5;
                    this.nextFrame = -j5;
                    if (j2 != -1) {
                    }
                    load();
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
                    j4 = l.longValue();
                    this.duration = j4;
                }
                if (j2 != -1 && j3 != -1) {
                    j4 = j3 - j2;
                }
                float clamp2 = Utilities.clamp((r5 != 0 || i3 == 0) ? 1.0f : r5 / i3, 1.3333334f, 0.5625f);
                this.frameHeight = Math.max(1, i2);
                this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp2));
                int ceil2 = (int) Math.ceil(((Math.max(j4, j) / j) * i) / r0);
                this.count = ceil2;
                long j52 = (long) (j4 / ceil2);
                this.frameIterator = j52;
                this.nextFrame = -j52;
                if (j2 != -1) {
                    this.nextFrame = j2 - j52;
                }
                load();
            }
            int i42 = i3;
            i3 = r5;
            r5 = i42;
            if (l != null) {
            }
            if (j2 != -1) {
                j4 = j3 - j2;
            }
            float clamp22 = Utilities.clamp((r5 != 0 || i3 == 0) ? 1.0f : r5 / i3, 1.3333334f, 0.5625f);
            this.frameHeight = Math.max(1, i2);
            this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp22));
            int ceil22 = (int) Math.ceil(((Math.max(j4, j) / j) * i) / r0);
            this.count = ceil22;
            long j522 = (long) (j4 / ceil22);
            this.frameIterator = j522;
            this.nextFrame = -j522;
            if (j2 != -1) {
            }
            load();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: receiveFrame, reason: merged with bridge method [inline-methods] */
        public void lambda$retrieveFrame$0(Bitmap bitmap) {
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
                        this.clipPath.addCircle(this.frameWidth / 2.0f, this.frameHeight / 2.0f, Math.min(r6, r9) / 2.0f, Path.Direction.CW);
                        canvas.clipPath(this.clipPath);
                    }
                    canvas.drawBitmap(bitmap, rect, rect2, this.bitmapPaint);
                    bitmap.recycle();
                    bitmap = createBitmap;
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TimelineView.VideoThumbsLoader.this.lambda$retrieveFrame$0(bitmap);
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

    class WaveformPath extends Path {
        private float lastAnimatedLoaded;
        private float lastAudioHeight;
        private float lastAudioSelected;
        private float lastBottom;
        private float lastLeft;
        private float lastMaxBar;
        private float lastRight;
        private long lastScrollDuration;
        private float lastStart;
        private int lastWaveformCount;

        WaveformPath() {
        }

        private void layout(float f, float f2, float f3, float f4, float f5, long j, float f6, float f7, float f8) {
            TimelineView.this.waveformPath.rewind();
            float round = Math.round(AndroidUtilities.dpf2(3.3333f));
            int min = Math.min(TimelineView.this.waveform.getCount() - 1, (int) Math.ceil(((TimelineView.this.ph + f3) - f) / round));
            for (int max = Math.max(0, (int) (((f2 - TimelineView.this.ph) - f) / round)); max <= min; max++) {
                float f9 = max;
                float dp = (f9 * round) + f + AndroidUtilities.dp(2.0f);
                float bar = f6 <= 0.0f ? 0.0f : (TimelineView.this.waveform.getBar(max) / f6) * f7 * 0.6f;
                if (f9 < f5 && max + 1 > f5) {
                    bar *= f5 - f9;
                } else if (f9 > f5) {
                    bar = 0.0f;
                }
                if (dp < f2 || dp > f3) {
                    bar *= f4;
                    if (bar <= 0.0f) {
                    }
                }
                float max2 = Math.max(bar, AndroidUtilities.lerp(AndroidUtilities.dpf2(0.66f), AndroidUtilities.dpf2(1.5f), f4));
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(dp, AndroidUtilities.lerp(f8 - max2, f8 - ((f7 + max2) / 2.0f), f4), AndroidUtilities.dpf2(1.66f) + dp, AndroidUtilities.lerp(f8, f8 - ((f7 - max2) / 2.0f), f4));
                TimelineView.this.waveformPath.addRoundRect(rectF, TimelineView.this.waveformRadii, Path.Direction.CW);
            }
        }

        public void check(float f, float f2, float f3, float f4, float f5, long j, float f6, float f7, float f8) {
            if (TimelineView.this.waveform == null) {
                rewind();
                return;
            }
            if (this.lastWaveformCount != TimelineView.this.waveform.getCount() || Math.abs(this.lastAnimatedLoaded - f5) > 0.01f || this.lastScrollDuration != j || Math.abs(this.lastAudioHeight - f6) > 1.0f || Math.abs(this.lastMaxBar - f7) > 0.01f || Math.abs(this.lastAudioSelected - f4) > 0.1f || Math.abs(this.lastBottom - f8) > 1.0f || Math.abs(this.lastStart - f) > 1.0f || Math.abs(this.lastLeft - f2) > 1.0f || Math.abs(this.lastRight - f3) > 1.0f) {
                this.lastWaveformCount = TimelineView.this.waveform.getCount();
                this.lastStart = f;
                this.lastLeft = f2;
                this.lastRight = f3;
                this.lastAudioSelected = f4;
                this.lastAnimatedLoaded = f5;
                this.lastScrollDuration = j;
                this.lastMaxBar = f7;
                this.lastAudioHeight = f6;
                this.lastBottom = f8;
                layout(f, f2, f3, f4, f5, j, f7, f6, f8);
            }
        }
    }

    public TimelineView(Context context, final ViewGroup viewGroup, final View view, final Theme.ResourcesProvider resourcesProvider, final BlurringShader.BlurManager blurManager) {
        super(context);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.roundT = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.roundSelectedT = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.audioT = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.audioSelectedT = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.videoSelectedT = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.waveformLoaded = new AnimatedFloat(this, 0L, 600L, cubicBezierInterpolator);
        this.waveformMax = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
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
        this.pressType = -1;
        this.scrollingVideo = true;
        this.scrolling = false;
        this.selectedVideoRadii = new float[8];
        this.waveformRadii = new float[8];
        this.container = viewGroup;
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
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.filled_widget_music).mutate();
        this.audioIcon = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
        this.blurManager = blurManager;
        this.backgroundBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 0);
        this.audioBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 3);
        this.audioWaveformBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 4);
        this.onLongPress = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$new$5(viewGroup, resourcesProvider, blurManager, view);
            }
        };
    }

    private int detectHandle(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float min = Math.min(getBaseDuration(), 120000L);
        float clamp = this.px + this.ph + (this.sw * (((Utilities.clamp(this.progress, getBaseDuration(), 0L) + (!this.hasVideo ? this.audioOffset : 0L)) - this.scroll) / min));
        boolean z = false;
        if (!this.isCover && x >= clamp - AndroidUtilities.dp(12.0f) && x <= clamp + AndroidUtilities.dp(12.0f)) {
            return 0;
        }
        boolean z2 = this.hasVideo && y > (((float) (this.h - this.py)) - getVideoHeight()) - ((float) AndroidUtilities.dp(2.0f));
        if (this.hasRound && y > (((((this.h - this.py) - getVideoHeight()) - AndroidUtilities.dp(4.0f)) - getRoundHeight()) - AndroidUtilities.dp(4.0f)) - AndroidUtilities.dp(2.0f) && y < ((this.h - this.py) - getVideoHeight()) - AndroidUtilities.dp(2.0f)) {
            z = true;
        }
        if (z2) {
            if (this.isCover) {
                return 4;
            }
            float f = this.px + this.ph;
            float f2 = this.videoLeft;
            float f3 = this.videoDuration;
            float f4 = this.scroll;
            float f5 = this.sw;
            float f6 = ((((f2 * f3) - f4) / min) * f5) + f;
            float f7 = f + ((((this.videoRight * f3) - f4) / min) * f5);
            if (x >= f6 - AndroidUtilities.dp(15.0f) && x <= AndroidUtilities.dp(5.0f) + f6) {
                return 2;
            }
            if (x >= f7 - AndroidUtilities.dp(5.0f) && x <= AndroidUtilities.dp(15.0f) + f7) {
                return 3;
            }
            if (x >= f6 && x <= f7 && (this.videoLeft > 0.01f || this.videoRight < 0.99f)) {
                return 4;
            }
        } else if (z) {
            float f8 = this.px + this.ph;
            float f9 = this.roundOffset;
            float f10 = this.roundLeft;
            float f11 = this.roundDuration;
            float f12 = this.scroll;
            float f13 = this.sw;
            float f14 = (((((f10 * f11) + f9) - f12) / min) * f13) + f8;
            float f15 = f8 + ((((f9 + (this.roundRight * f11)) - f12) / min) * f13);
            if (this.roundSelected || !this.hasVideo) {
                if (x >= f14 - AndroidUtilities.dp(15.0f) && x <= AndroidUtilities.dp(5.0f) + f14) {
                    return 10;
                }
                if (x >= f15 - AndroidUtilities.dp(5.0f) && x <= AndroidUtilities.dp(15.0f) + f15) {
                    return 11;
                }
                if (x >= f14 && x <= f15) {
                    return !this.hasVideo ? 12 : 9;
                }
                float f16 = this.px + this.ph;
                long j = this.roundOffset;
                long j2 = this.scroll;
                float f17 = this.sw;
                float f18 = (((j - j2) / min) * f17) + f16;
                f15 = f16 + ((((j + this.roundDuration) - j2) / min) * f17);
                f14 = f18;
            }
            if (x >= f14 && x <= f15) {
                return 9;
            }
        } else if (this.hasAudio) {
            float f19 = this.px + this.ph;
            float f20 = this.audioOffset;
            float f21 = this.audioLeft;
            float f22 = this.audioDuration;
            float f23 = this.scroll;
            float f24 = this.sw;
            float f25 = (((((f21 * f22) + f20) - f23) / min) * f24) + f19;
            float f26 = f19 + ((((f20 + (this.audioRight * f22)) - f23) / min) * f24);
            if (this.audioSelected || (!this.hasVideo && !this.hasRound)) {
                if (x >= f25 - AndroidUtilities.dp(15.0f) && x <= AndroidUtilities.dp(5.0f) + f25) {
                    return 6;
                }
                if (x >= f26 - AndroidUtilities.dp(5.0f) && x <= AndroidUtilities.dp(15.0f) + f26) {
                    return 7;
                }
                if (x >= f25 && x <= f26) {
                    return !this.hasVideo ? 8 : 5;
                }
                float f27 = this.px + this.ph;
                long j3 = this.audioOffset;
                long j4 = this.scroll;
                float f28 = this.sw;
                float f29 = (((j3 - j4) / min) * f28) + f27;
                f26 = ((((j3 + this.audioDuration) - j4) / min) * f28) + f27;
                f25 = f29;
            }
            if (x >= f25 && x <= f26) {
                return 5;
            }
        }
        return (this.videoDuration <= 120000 || !z2) ? -1 : 1;
    }

    private void drawProgress(Canvas canvas, float f, float f2, long j, float f3) {
        if (this.isCover) {
            return;
        }
        float clamp = this.px + this.ph + (this.sw * (((Utilities.clamp(j, getBaseDuration(), 0L) + (!this.hasVideo ? this.audioOffset : 0L)) - this.scroll) / Math.min(getBaseDuration(), 120000L)));
        float f4 = (((f + f2) / 2.0f) / 2.0f) * (1.0f - f3);
        float f5 = f + f4;
        float f6 = f2 - f4;
        this.progressShadowPaint.setAlpha((int) (38.0f * f3));
        this.progressWhitePaint.setAlpha((int) (f3 * 255.0f));
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(clamp - AndroidUtilities.dpf2(1.5f), f5, AndroidUtilities.dpf2(1.5f) + clamp, f6);
        rectF.inset(-AndroidUtilities.dpf2(0.66f), -AndroidUtilities.dpf2(0.66f));
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.progressShadowPaint);
        rectF.set(clamp - AndroidUtilities.dpf2(1.5f), f5, clamp + AndroidUtilities.dpf2(1.5f), f6);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.progressWhitePaint);
    }

    private void drawRegion(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, float f5) {
        if (f5 <= 0.0f) {
            return;
        }
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(f3 - AndroidUtilities.dp(10.0f), f, f4 + AndroidUtilities.dp(10.0f), f2);
        canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, NotificationCenter.notificationsCountUpdated, 31);
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
        this.regionHandlePaint.setAlpha(NotificationCenter.notificationsCountUpdated);
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

    private long getBaseDuration() {
        return this.hasVideo ? this.videoDuration : this.hasRound ? this.roundDuration : this.hasAudio ? this.audioDuration : Math.max(1L, this.audioDuration);
    }

    private float getRoundHeight() {
        if (!this.hasRound) {
            return 0.0f;
        }
        return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.roundSelectedT.set(this.roundSelected));
    }

    private float getVideoHeight() {
        if (this.hasVideo) {
            return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.videoSelectedT.set((this.audioSelected || this.roundSelected) ? false : true));
        }
        return 0.0f;
    }

    public static int heightDp() {
        return 112;
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
        this.videoVolume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onVideoVolumeChange(f.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(ViewGroup viewGroup, Theme.ResourcesProvider resourcesProvider, BlurringShader.BlurManager blurManager, View view) {
        int i;
        int i2;
        float min;
        float f;
        float f2;
        float roundHeight;
        ItemOptions addSpaceGap;
        int i3;
        String string;
        Runnable runnable;
        float f3;
        float f4;
        int i4 = this.pressType;
        try {
            if (i4 == 2 && this.hasAudio) {
                SliderView onValueChange = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.audioVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        TimelineView.this.lambda$new$0((Float) obj);
                    }
                });
                long min2 = Math.min(getBaseDuration(), 120000L);
                int i5 = this.w;
                int i6 = this.px;
                int i7 = this.ph;
                min = Math.min((i5 - i6) - i7, i6 + i7 + ((((this.audioOffset - this.scroll) + (AndroidUtilities.lerp(this.audioRight, 1.0f, this.audioSelectedT.get()) * this.audioDuration)) / min2) * this.sw));
                float f5 = this.h - this.py;
                if (this.hasVideo) {
                    f3 = 4.0f;
                    f4 = getVideoHeight() + AndroidUtilities.dp(4.0f);
                } else {
                    f3 = 4.0f;
                    f4 = 0.0f;
                }
                roundHeight = ((f5 - f4) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(f3) : 0.0f)) - (this.hasAudio ? getAudioHeight() + AndroidUtilities.dp(f3) : 0.0f);
                addSpaceGap = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange).addSpaceGap();
                i3 = R.drawable.msg_delete;
                string = LocaleController.getString(R.string.StoryAudioRemove);
                runnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        TimelineView.this.lambda$new$1();
                    }
                };
            } else {
                if (i4 != 1 || !this.hasRound) {
                    if (i4 == 0 && this.hasVideo) {
                        i = 1;
                        ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.videoVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda6
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                TimelineView.this.lambda$new$4((Float) obj);
                            }
                        })).setGravity(5).forceTop(true).translate(AndroidUtilities.dp(18.0f), (this.h - this.py) - (this.hasVideo ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f)).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
                        i2 = 0;
                        performHapticFeedback(i2, i);
                        return;
                    }
                    return;
                }
                SliderView onValueChange2 = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.roundVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda4
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        TimelineView.this.lambda$new$2((Float) obj);
                    }
                });
                long min3 = Math.min(getBaseDuration(), 120000L);
                int i8 = this.w;
                int i9 = this.px;
                int i10 = this.ph;
                min = Math.min((i8 - i9) - i10, i9 + i10 + ((((this.roundOffset - this.scroll) + (AndroidUtilities.lerp(this.roundRight, 1.0f, this.roundSelectedT.get()) * this.roundDuration)) / min3) * this.sw));
                float f6 = this.h - this.py;
                if (this.hasVideo) {
                    f = 4.0f;
                    f2 = getVideoHeight() + AndroidUtilities.dp(4.0f);
                } else {
                    f = 4.0f;
                    f2 = 0.0f;
                }
                roundHeight = (f6 - f2) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(f) : 0.0f);
                addSpaceGap = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange2).addSpaceGap();
                i3 = R.drawable.msg_delete;
                string = LocaleController.getString(R.string.StoryRoundRemove);
                runnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        TimelineView.this.lambda$new$3();
                    }
                };
            }
            performHapticFeedback(i2, i);
            return;
        } catch (Exception unused) {
            return;
        }
        addSpaceGap.add(i3, string, runnable).setGravity(5).forceTop(true).translate((-(this.w - min)) + AndroidUtilities.dp(18.0f), roundHeight).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
        i2 = 0;
        i = 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setProgressAt$6(long j) {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onProgressChange(j, false);
        }
    }

    private long minAudioSelect() {
        return (long) Math.max(1000.0f, Math.min(this.hasVideo ? this.videoDuration : this.hasRound ? this.roundDuration : this.audioDuration, 59000L) * 0.15f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x012e, code lost:
    
        r1.onAudioLeftChange(r22.audioLeft);
        r22.delegate.onAudioRightChange(r22.audioRight);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00e4, code lost:
    
        if (r1 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x004a, code lost:
    
        if (r1 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x012c, code lost:
    
        if (r1 != null) goto L40;
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void moveAudioOffset(float f) {
        float f2;
        long j;
        float f3;
        long j2;
        float f4;
        long j3;
        long j4;
        long j5;
        TimelineDelegate timelineDelegate;
        long j6;
        long j7;
        long clamp;
        long j8;
        float f5;
        long j9;
        float f6;
        TimelineDelegate timelineDelegate2;
        long j10;
        TimelineDelegate timelineDelegate3;
        long j11;
        float f7;
        long j12;
        float f8;
        boolean z = this.hasVideo;
        if (!z && !this.hasRound) {
            long j13 = this.audioOffset;
            long clamp2 = Utilities.clamp(j13 + ((long) f), 0L, -(this.audioDuration - Math.min(getBaseDuration(), 120000L)));
            this.audioOffset = clamp2;
            float f9 = clamp2 - j13;
            this.audioLeft = Utilities.clamp(this.audioLeft - (f9 / this.audioDuration), 1.0f, 0.0f);
            this.audioRight = Utilities.clamp(this.audioRight - (f9 / this.audioDuration), 1.0f, 0.0f);
            timelineDelegate = this.delegate;
        } else if (this.audioSelected) {
            if (z) {
                f2 = this.videoLeft;
                j = this.videoDuration;
            } else {
                f2 = this.roundLeft;
                j = this.roundDuration;
            }
            float f10 = f2 * j;
            if (z) {
                f3 = this.videoRight;
                j2 = this.videoDuration;
            } else {
                f3 = this.roundRight;
                j2 = this.roundDuration;
            }
            float f11 = f3 * j2;
            if (z) {
                f4 = this.videoRight - this.videoLeft;
                j3 = this.videoDuration;
            } else {
                f4 = this.roundRight - this.roundLeft;
                j3 = this.roundDuration;
            }
            float f12 = f4 * j3;
            float f13 = this.audioRight;
            float f14 = this.audioDuration;
            long j14 = (long) (f11 - (f13 * f14));
            float f15 = this.audioLeft;
            long j15 = (long) (f10 - (f15 * f14));
            float min = Math.min(f13 - f15, f12 / f14);
            long j16 = this.audioOffset;
            long j17 = (long) f;
            long j18 = j16 + j17;
            if (j18 > j14) {
                float clamp3 = Utilities.clamp(((f11 - j16) - j17) / this.audioDuration, 1.0f, min);
                this.audioRight = clamp3;
                float clamp4 = Utilities.clamp(clamp3 - min, 1.0f, 0.0f);
                this.audioLeft = clamp4;
                float f16 = this.audioRight;
                float f17 = this.audioDuration;
                long j19 = (long) (f11 - (f16 * f17));
                long j20 = (long) (f10 - (clamp4 * f17));
                if (j19 < j20) {
                    j7 = j20;
                    j6 = j19;
                } else {
                    j6 = j20;
                    j7 = j19;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j17, j7, j6);
                timelineDelegate = this.delegate;
            } else if (j18 < j15) {
                float clamp5 = Utilities.clamp(((f10 - j16) - j17) / this.audioDuration, 1.0f - min, 0.0f);
                this.audioLeft = clamp5;
                float clamp6 = Utilities.clamp(clamp5 + min, 1.0f, 0.0f);
                this.audioRight = clamp6;
                float f18 = this.audioDuration;
                long j21 = (long) (f11 - (clamp6 * f18));
                long j22 = (long) (f10 - (this.audioLeft * f18));
                if (j21 < j22) {
                    j5 = j22;
                    j4 = j21;
                } else {
                    j4 = j22;
                    j5 = j21;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j17, j5, j4);
                timelineDelegate = this.delegate;
            } else {
                this.audioOffset = j18;
            }
        } else {
            long j23 = this.audioOffset + ((long) f);
            float baseDuration = getBaseDuration();
            float f19 = this.audioDuration;
            this.audioOffset = Utilities.clamp(j23, (long) (baseDuration - (this.audioRight * f19)), (long) ((-this.audioLeft) * f19));
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onAudioOffsetChange(this.audioOffset + ((long) (this.audioLeft * this.audioDuration)));
        }
        boolean z2 = this.dragged;
        if (!z2 && (timelineDelegate3 = this.delegate) != null) {
            timelineDelegate3.onProgressDragChange(true);
            if (this.hasVideo) {
                j11 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f20 = this.videoRight;
                f7 = this.videoDuration;
                j12 = (long) (f20 * f7);
                f8 = this.videoLeft;
            } else if (this.hasRound) {
                j11 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f21 = this.roundRight;
                f7 = this.roundDuration;
                j12 = (long) (f21 * f7);
                f8 = this.roundLeft;
            } else {
                float f22 = this.audioLeft;
                long j24 = this.audioDuration;
                j10 = Utilities.clamp((long) (f22 * j24), j24, 0L);
                if (this.hasVideo && Math.abs(this.progress - j10) > 400) {
                    this.loopProgressFrom = this.progress;
                    this.loopProgress.set(1.0f, true);
                }
                timelineDelegate2 = this.delegate;
                this.progress = j10;
            }
            j10 = Utilities.clamp(j11, j12, (long) (f8 * f7));
            if (this.hasVideo) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            timelineDelegate2 = this.delegate;
            this.progress = j10;
        } else {
            if (!z2 && !this.scrolling) {
                return;
            }
            if (this.hasVideo) {
                j8 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f23 = this.videoRight;
                f5 = this.videoDuration;
                j9 = (long) (f23 * f5);
                f6 = this.videoLeft;
            } else if (this.hasRound) {
                j8 = this.audioOffset + ((long) (this.audioLeft * this.audioDuration));
                float f24 = this.roundRight;
                f5 = this.videoDuration;
                j9 = (long) (f24 * f5);
                f6 = this.roundLeft;
            } else {
                float f25 = this.audioLeft;
                long j25 = this.audioDuration;
                clamp = Utilities.clamp((long) (f25 * j25), j25, 0L);
                this.progress = clamp;
                timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    return;
                } else {
                    j10 = this.progress;
                }
            }
            clamp = Utilities.clamp(j8, j9, (long) (f6 * f5));
            this.progress = clamp;
            timelineDelegate2 = this.delegate;
            if (timelineDelegate2 != null) {
            }
        }
        timelineDelegate2.onProgressChange(j10, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x00cf, code lost:
    
        if (r1 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0123, code lost:
    
        r1.onRoundLeftChange(r17.roundLeft);
        r17.delegate.onRoundRightChange(r17.roundRight);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0121, code lost:
    
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
        long clamp2;
        if (!this.hasVideo) {
            long j5 = this.roundOffset;
            long clamp3 = Utilities.clamp(j5 + ((long) f), 0L, -(this.roundDuration - Math.min(getBaseDuration(), 120000L)));
            this.roundOffset = clamp3;
            float f2 = clamp3 - j5;
            this.roundLeft = Utilities.clamp(this.roundLeft - (f2 / this.roundDuration), 1.0f, 0.0f);
            this.roundRight = Utilities.clamp(this.roundRight - (f2 / this.roundDuration), 1.0f, 0.0f);
            TimelineDelegate timelineDelegate3 = this.delegate;
            if (timelineDelegate3 != null) {
                timelineDelegate3.onAudioLeftChange(this.roundLeft);
                this.delegate.onAudioRightChange(this.roundRight);
            }
        } else if (this.roundSelected) {
            float f3 = this.videoRight;
            float f4 = this.videoDuration;
            float f5 = this.roundRight;
            float f6 = this.roundDuration;
            long j6 = (long) ((f3 * f4) - (f5 * f6));
            float f7 = this.videoLeft;
            float f8 = this.roundLeft;
            long j7 = (long) ((f7 * f4) - (f8 * f6));
            float min = Math.min(f5 - f8, ((f3 - f7) * f4) / f6);
            long j8 = this.roundOffset;
            long j9 = (long) f;
            long j10 = j8 + j9;
            if (j10 > j6) {
                float clamp4 = Utilities.clamp((((this.videoRight * this.videoDuration) - j8) - j9) / this.roundDuration, 1.0f, min);
                this.roundRight = clamp4;
                float clamp5 = Utilities.clamp(clamp4 - min, 1.0f, 0.0f);
                this.roundLeft = clamp5;
                float f9 = this.videoRight;
                float f10 = this.videoDuration;
                float f11 = this.roundRight;
                float f12 = this.roundDuration;
                long j11 = (long) ((f9 * f10) - (f11 * f12));
                long j12 = (long) ((this.videoLeft * f10) - (clamp5 * f12));
                if (j11 < j12) {
                    j4 = j12;
                    j3 = j11;
                } else {
                    j3 = j12;
                    j4 = j11;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j9, j4, j3);
                timelineDelegate = this.delegate;
            } else if (j10 < j7) {
                float clamp6 = Utilities.clamp((((this.videoLeft * this.videoDuration) - j8) - j9) / this.roundDuration, 1.0f - min, 0.0f);
                this.roundLeft = clamp6;
                float clamp7 = Utilities.clamp(clamp6 + min, 1.0f, 0.0f);
                this.roundRight = clamp7;
                float f13 = this.videoRight;
                float f14 = this.videoDuration;
                float f15 = this.roundDuration;
                long j13 = (long) ((f13 * f14) - (clamp7 * f15));
                long j14 = (long) ((this.videoLeft * f14) - (this.roundLeft * f15));
                if (j13 < j14) {
                    j2 = j13;
                    j = j14;
                } else {
                    j = j13;
                    j2 = j14;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j9, j, j2);
                timelineDelegate = this.delegate;
            } else {
                this.roundOffset = j10;
            }
        } else {
            long j15 = this.roundOffset + ((long) f);
            float baseDuration = getBaseDuration();
            float f16 = this.roundDuration;
            this.roundOffset = Utilities.clamp(j15, (long) (baseDuration - (this.roundRight * f16)), (long) ((-this.roundLeft) * f16));
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onRoundOffsetChange(this.roundOffset + ((long) (this.roundLeft * this.roundDuration)));
        }
        boolean z = this.dragged;
        if (z || (timelineDelegate2 = this.delegate) == null) {
            if (z || this.scrolling) {
                if (this.hasVideo) {
                    long j16 = this.roundOffset + ((long) (this.roundLeft * this.roundDuration));
                    float f17 = this.videoRight;
                    float f18 = this.videoDuration;
                    clamp = Utilities.clamp(j16, (long) (f17 * f18), (long) (this.videoLeft * f18));
                } else {
                    float f19 = this.roundLeft;
                    long j17 = this.roundDuration;
                    clamp = Utilities.clamp((long) (f19 * j17), j17, 0L);
                }
                this.progress = clamp;
                TimelineDelegate timelineDelegate5 = this.delegate;
                if (timelineDelegate5 != null) {
                    timelineDelegate5.onProgressChange(this.progress, false);
                    return;
                }
                return;
            }
            return;
        }
        timelineDelegate2.onProgressDragChange(true);
        if (this.hasVideo) {
            long j18 = this.roundOffset + ((long) (this.roundLeft * this.roundDuration));
            float f20 = this.videoRight;
            float f21 = this.videoDuration;
            clamp2 = Utilities.clamp(j18, (long) (f20 * f21), (long) (this.videoLeft * f21));
        } else {
            float f22 = this.roundLeft;
            long j19 = this.roundDuration;
            clamp2 = Utilities.clamp((long) (f22 * j19), j19, 0L);
        }
        if (this.hasVideo && Math.abs(this.progress - clamp2) > 400) {
            this.loopProgressFrom = this.progress;
            this.loopProgress.set(1.0f, true);
        }
        TimelineDelegate timelineDelegate6 = this.delegate;
        this.progress = clamp2;
        timelineDelegate6.onProgressChange(clamp2, false);
    }

    private boolean setProgressAt(float f, boolean z) {
        if (!this.hasVideo && !this.hasAudio) {
            return false;
        }
        float min = (((f - this.px) - this.ph) / this.sw) * Math.min(getBaseDuration(), 120000L);
        boolean z2 = this.hasVideo;
        final long clamp = (long) Utilities.clamp(min + (!z2 ? -this.audioOffset : 0L) + this.scroll, z2 ? this.videoDuration : this.audioDuration, 0.0f);
        boolean z3 = this.hasVideo;
        if (z3) {
            float f2 = clamp / this.videoDuration;
            if (f2 < this.videoLeft || f2 > this.videoRight) {
                return false;
            }
        }
        if (this.hasAudio && !z3) {
            float f3 = clamp / this.audioDuration;
            if (f3 < this.audioLeft || f3 > this.audioRight) {
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
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$setProgressAt$6(clamp);
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
                this.waveformLoaded.set(0.0f, true);
                this.waveformMax.set(1.0f, true);
            }
        }
    }

    private void setupRoundThumbs() {
        if (getMeasuredWidth() <= 0 || this.roundThumbs != null) {
            return;
        }
        if (!this.hasVideo || this.videoDuration >= 1) {
            String str = this.roundPath;
            int i = this.w;
            int i2 = this.px;
            int i3 = (i - i2) - i2;
            int dp = AndroidUtilities.dp(38.0f);
            long j = this.roundDuration;
            VideoThumbsLoader videoThumbsLoader = new VideoThumbsLoader(false, str, i3, dp, j > 2 ? Long.valueOf(j) : null, this.hasVideo ? this.videoDuration : 120000L, -1L, -1L);
            this.roundThumbs = videoThumbsLoader;
            if (videoThumbsLoader.getDuration() > 0) {
                this.roundDuration = this.roundThumbs.getDuration();
            }
        }
    }

    private void setupVideoThumbs(boolean z) {
        if (getMeasuredWidth() > 0) {
            VideoThumbsLoader videoThumbsLoader = this.thumbs;
            if (videoThumbsLoader == null || z) {
                if (videoThumbsLoader != null) {
                    videoThumbsLoader.destroy();
                    this.thumbs = null;
                }
                boolean z2 = this.isMainVideoRound;
                String str = this.videoPath;
                int i = this.w;
                int i2 = this.px;
                int i3 = (i - i2) - i2;
                int dp = AndroidUtilities.dp(38.0f);
                long j = this.videoDuration;
                VideoThumbsLoader videoThumbsLoader2 = new VideoThumbsLoader(z2, str, i3, dp, j > 2 ? Long.valueOf(j) : null, 120000L, this.coverStart, this.coverEnd);
                this.thumbs = videoThumbsLoader2;
                if (videoThumbsLoader2.getDuration() > 0) {
                    this.videoDuration = this.thumbs.getDuration();
                }
                setupRoundThumbs();
            }
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
        long min = Math.min(getBaseDuration(), 120000L);
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

    /* JADX WARN: Removed duplicated region for block: B:239:0x0ac8  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0adf  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0b07  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0ad3  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        Paint paint;
        long j;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        double d;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        long j2;
        float f18;
        float f19;
        float f20;
        long clamp;
        long j3;
        float f21;
        long j4;
        float f22;
        TimelineDelegate timelineDelegate;
        float f23;
        float max;
        float lerp;
        float lerp2;
        int i;
        double d2;
        float f24;
        float f25;
        float f26;
        boolean z;
        int i2;
        float f27;
        int i3;
        Paint paint2 = this.backgroundBlur.getPaint(1.0f);
        long min = Math.min(getBaseDuration(), 120000L);
        float f28 = this.hasVideo ? 1.0f : 0.0f;
        float f29 = this.videoSelectedT.set((this.audioSelected || this.roundSelected) ? false : true);
        if (this.hasVideo) {
            canvas.save();
            float videoHeight = getVideoHeight();
            f = f28;
            long j5 = this.videoDuration;
            float f30 = j5 <= 0 ? 0.0f : (this.px + this.ph) - ((this.scroll / min) * this.sw);
            float f31 = this.ph;
            float f32 = f30 - f31;
            float f33 = (j5 <= 0 ? 0.0f : (((j5 - this.scroll) / min) * this.sw) + this.px + r12) + f31;
            RectF rectF = this.videoBounds;
            float f34 = this.h - this.py;
            rectF.set(f32, f34 - videoHeight, f33, f34);
            this.videoClipPath.rewind();
            this.videoClipPath.addRoundRect(this.videoBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.clipPath(this.videoClipPath);
            VideoThumbsLoader videoThumbsLoader = this.thumbs;
            if (videoThumbsLoader != null) {
                float frameWidth = videoThumbsLoader.getFrameWidth();
                int max2 = (int) Math.max(0.0d, Math.floor((f32 - this.px) / frameWidth));
                int min2 = (int) Math.min(this.thumbs.count, Math.ceil(((f33 - f32) - this.px) / frameWidth) + 1.0d);
                int i4 = (int) this.videoBounds.top;
                boolean z2 = this.thumbs.frames.size() >= min2;
                boolean z3 = z2 && !this.isMainVideoRound;
                if (z3) {
                    int i5 = max2;
                    while (true) {
                        if (i5 >= Math.min(this.thumbs.frames.size(), min2)) {
                            break;
                        }
                        if (((VideoThumbsLoader.BitmapFrame) this.thumbs.frames.get(i5)).bitmap == null) {
                            z3 = false;
                            break;
                        }
                        i5++;
                    }
                }
                if (!z3) {
                    if (paint2 == null) {
                        i3 = 1073741824;
                    } else {
                        canvas.drawRect(this.videoBounds, paint2);
                        i3 = AndroidUtilities.DARK_STATUS_BAR_OVERLAY;
                    }
                    canvas.drawColor(i3);
                }
                while (max2 < Math.min(this.thumbs.frames.size(), min2)) {
                    VideoThumbsLoader.BitmapFrame bitmapFrame = (VideoThumbsLoader.BitmapFrame) this.thumbs.frames.get(max2);
                    if (bitmapFrame.bitmap != null) {
                        this.videoFramePaint.setAlpha((int) (bitmapFrame.getAlpha() * 255.0f));
                        canvas.drawBitmap(bitmapFrame.bitmap, f32, i4 - ((int) ((r1.getHeight() - videoHeight) / 2.0f)), this.videoFramePaint);
                    }
                    f32 += frameWidth;
                    max2++;
                }
                if (!z2) {
                    this.thumbs.load();
                }
            }
            this.selectedVideoClipPath.rewind();
            if (this.isCover) {
                f27 = videoHeight;
            } else {
                RectF rectF2 = AndroidUtilities.rectTmp;
                int i6 = this.px;
                int i7 = this.ph;
                float f35 = i6 + i7;
                float f36 = this.videoLeft;
                float f37 = this.videoDuration;
                float f38 = this.scroll;
                float f39 = min;
                float f40 = this.sw;
                float f41 = (((((f36 * f37) - f38) / f39) * f40) + f35) - (f36 <= 0.0f ? i7 : 0);
                float f42 = this.h - this.py;
                float f43 = f42 - videoHeight;
                f27 = videoHeight;
                float f44 = this.videoRight;
                float f45 = f35 + ((((f37 * f44) - f38) / f39) * f40);
                if (f44 < 1.0f) {
                    i7 = 0;
                }
                rectF2.set(f41, f43, f45 + i7, f42);
                this.selectedVideoClipPath.addRoundRect(rectF2, this.selectedVideoRadii, Path.Direction.CW);
                canvas.clipPath(this.selectedVideoClipPath, Region.Op.DIFFERENCE);
                canvas.drawColor(1342177280);
            }
            canvas.restore();
            f2 = f27;
        } else {
            f = f28;
            f2 = 0.0f;
        }
        float dp = AndroidUtilities.dp(4.0f);
        float f46 = this.roundT.set(this.hasRound);
        float f47 = this.roundSelectedT.set(this.hasRound && this.roundSelected);
        float roundHeight = getRoundHeight() * f46;
        if (f46 > 0.0f) {
            if (this.hasVideo) {
                float f48 = min;
                f26 = this.px + this.ph + ((((this.roundOffset - this.scroll) + (AndroidUtilities.lerp(this.roundLeft, 0.0f, f47) * this.roundDuration)) / f48) * this.sw);
                f25 = this.px + this.ph + ((((this.roundOffset - this.scroll) + (AndroidUtilities.lerp(this.roundRight, 1.0f, f47) * this.roundDuration)) / f48) * this.sw);
                f3 = f47;
                f4 = f46;
            } else {
                float f49 = this.px + this.ph;
                long j6 = this.roundOffset - this.scroll;
                float f50 = min;
                float f51 = this.sw;
                float f52 = ((j6 / f50) * f51) + f49;
                f3 = f47;
                f4 = f46;
                f25 = (((j6 + this.roundDuration) / f50) * f51) + f49;
                f26 = f52;
            }
            float f53 = ((this.h - this.py) - f2) - (dp * f);
            RectF rectF3 = this.roundBounds;
            float f54 = this.ph;
            rectF3.set(f26 - f54, f53 - roundHeight, f25 + f54, f53);
            this.roundClipPath.rewind();
            this.roundClipPath.addRoundRect(this.roundBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.save();
            canvas.clipPath(this.roundClipPath);
            VideoThumbsLoader videoThumbsLoader2 = this.roundThumbs;
            if (videoThumbsLoader2 != null) {
                long j7 = this.roundDuration;
                float f55 = j7 <= 0 ? 0.0f : this.px + this.ph + (((this.roundOffset - this.scroll) / min) * this.sw);
                float f56 = this.ph;
                float f57 = f55 - f56;
                float f58 = (j7 <= 0 ? 0.0f : ((((this.roundOffset + j7) - this.scroll) / min) * this.sw) + this.px + r4) + f56;
                float frameWidth2 = videoThumbsLoader2.getFrameWidth();
                int max3 = (int) Math.max(0.0d, Math.floor((f57 - (this.hasVideo ? (this.px + this.ph) + (((this.roundOffset - this.scroll) / min) * this.sw) : this.px)) / frameWidth2));
                int min3 = (int) Math.min(this.roundThumbs.count, Math.ceil((f58 - f57) / frameWidth2) + 1.0d);
                int i8 = (int) this.roundBounds.top;
                boolean z4 = this.roundThumbs.frames.size() >= min3;
                if (z4) {
                    for (int i9 = max3; i9 < Math.min(this.roundThumbs.frames.size(), min3); i9++) {
                        if (((VideoThumbsLoader.BitmapFrame) this.roundThumbs.frames.get(i9)).bitmap == null) {
                            z = false;
                            break;
                        }
                    }
                }
                z = z4;
                if (!z) {
                    if (paint2 == null) {
                        i2 = 1073741824;
                    } else {
                        canvas.drawRect(this.roundBounds, paint2);
                        i2 = AndroidUtilities.DARK_STATUS_BAR_OVERLAY;
                    }
                    canvas.drawColor(i2);
                }
                while (max3 < Math.min(this.roundThumbs.frames.size(), min3)) {
                    VideoThumbsLoader.BitmapFrame bitmapFrame2 = (VideoThumbsLoader.BitmapFrame) this.roundThumbs.frames.get(max3);
                    if (bitmapFrame2.bitmap != null) {
                        this.videoFramePaint.setAlpha((int) (bitmapFrame2.getAlpha() * 255.0f));
                        canvas.drawBitmap(bitmapFrame2.bitmap, f57, i8 - ((int) ((r13.getHeight() - roundHeight) / 2.0f)), this.videoFramePaint);
                    }
                    f57 += frameWidth2;
                    max3++;
                }
                if (!z4) {
                    this.roundThumbs.load();
                }
            }
            this.selectedVideoClipPath.rewind();
            RectF rectF4 = AndroidUtilities.rectTmp;
            int i10 = this.px;
            int i11 = this.ph;
            float f59 = i10 + i11;
            float f60 = this.roundLeft;
            float f61 = this.roundDuration;
            float f62 = this.scroll;
            float f63 = this.roundOffset;
            float f64 = min;
            paint = paint2;
            float f65 = this.sw;
            float f66 = ((((((f60 * f61) - f62) + f63) / f64) * f65) + f59) - (f60 <= 0.0f ? i11 : 0);
            float f67 = i11 * (1.0f - f3);
            float f68 = f66 - f67;
            RectF rectF5 = this.roundBounds;
            f5 = dp;
            float f69 = rectF5.top;
            f6 = f2;
            float f70 = this.roundRight;
            rectF4.set(f68, f69, f59 + (((((f61 * f70) - f62) + f63) / f64) * f65) + (f70 >= 1.0f ? i11 : 0) + f67, rectF5.bottom);
            this.selectedVideoClipPath.addRoundRect(rectF4, this.selectedVideoRadii, Path.Direction.CW);
            canvas.clipPath(this.selectedVideoClipPath, Region.Op.DIFFERENCE);
            canvas.drawColor(1342177280);
            canvas.restore();
        } else {
            f3 = f47;
            f4 = f46;
            f5 = dp;
            f6 = f2;
            paint = paint2;
        }
        float f71 = this.audioT.set(this.hasAudio);
        float f72 = this.audioSelectedT.set(this.hasAudio && this.audioSelected);
        float audioHeight = getAudioHeight() * f71;
        if (f71 > 0.0f) {
            Paint paint3 = this.audioBlur.getPaint(f71);
            canvas.save();
            if (this.hasVideo || this.hasRound) {
                float f73 = min;
                lerp = this.px + this.ph + ((((this.audioOffset - this.scroll) + (AndroidUtilities.lerp(this.audioLeft, 0.0f, f72) * this.audioDuration)) / f73) * this.sw);
                lerp2 = this.px + this.ph + ((((this.audioOffset - this.scroll) + (AndroidUtilities.lerp(this.audioRight, 1.0f, f72) * this.audioDuration)) / f73) * this.sw);
            } else {
                float f74 = this.px + this.ph;
                long j8 = this.audioOffset - this.scroll;
                float f75 = min;
                float f76 = this.sw;
                lerp2 = f74 + (((j8 + this.audioDuration) / f75) * f76);
                lerp = ((j8 / f75) * f76) + f74;
            }
            float f77 = ((((this.h - this.py) - f6) - (f5 * f)) - roundHeight) - (f5 * f4);
            RectF rectF6 = this.audioBounds;
            float f78 = this.ph;
            float f79 = f77 - audioHeight;
            rectF6.set(lerp - f78, f79, lerp2 + f78, f77);
            this.audioClipPath.rewind();
            this.audioClipPath.addRoundRect(this.audioBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.clipPath(this.audioClipPath);
            if (paint3 == null) {
                i = 1073741824;
            } else {
                canvas.drawRect(this.audioBounds, paint3);
                i = AndroidUtilities.DARK_STATUS_BAR_OVERLAY;
            }
            canvas.drawColor(Theme.multAlpha(i, f71));
            if (this.waveform == null || paint3 == null) {
                d2 = 0.0d;
            } else {
                Paint paint4 = this.audioWaveformBlur.getPaint(0.4f * f71);
                if (paint4 == null) {
                    paint4 = this.waveformPaint;
                    paint4.setAlpha((int) (64.0f * f71));
                }
                float f80 = this.waveformMax.set(this.waveform.getMaxBar(), !this.waveformIsLoaded);
                this.waveformIsLoaded = this.waveform.getLoadedCount() > 0;
                d2 = 0.0d;
                this.waveformPath.check(this.px + this.ph + (((this.audioOffset - this.scroll) / min) * this.sw), lerp, lerp2, f72, this.waveformLoaded.set(this.waveform.getLoadedCount()), min, audioHeight, f80, f77);
                canvas.drawPath(this.waveformPath, paint4);
            }
            if (f72 < 1.0f) {
                int i12 = this.px;
                float f81 = this.ph + i12;
                float f82 = this.audioOffset - this.scroll;
                float f83 = this.audioLeft;
                float f84 = this.audioDuration;
                float f85 = min;
                float f86 = this.sw;
                float f87 = ((((f83 * f84) + f82) / f85) * f86) + f81;
                float f88 = f81 + (((f82 + (this.audioRight * f84)) / f85) * f86);
                float max4 = (Math.max(i12, f87) + Math.min(this.w - this.px, f88)) / 2.0f;
                float dp2 = f79 + AndroidUtilities.dp(14.0f);
                float max5 = Math.max(0.0f, (Math.min(this.w - this.px, f88) - Math.max(this.px, f87)) - AndroidUtilities.dp(24.0f));
                float dpf2 = AndroidUtilities.dpf2(13.0f) + ((this.audioAuthor == null && this.audioTitle == null) ? 0.0f : AndroidUtilities.dpf2(3.11f) + this.audioAuthorWidth + AndroidUtilities.dpf2(9.66f) + this.audioTitleWidth);
                boolean z5 = dpf2 < max5;
                float min4 = max4 - (Math.min(dpf2, max5) / 2.0f);
                this.audioIcon.setBounds((int) min4, (int) (dp2 - (AndroidUtilities.dp(13.0f) / 2.0f)), (int) (AndroidUtilities.dp(13.0f) + min4), (int) ((AndroidUtilities.dp(13.0f) / 2.0f) + dp2));
                float f89 = 1.0f - f72;
                float f90 = f89 * 255.0f;
                this.audioIcon.setAlpha((int) f90);
                this.audioIcon.draw(canvas);
                float dpf22 = min4 + AndroidUtilities.dpf2(16.11f);
                d = 0.0d;
                f9 = f3;
                f7 = f;
                f8 = f4;
                f10 = f5;
                j = min;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, NotificationCenter.notificationsCountUpdated, 31);
                float min5 = Math.min(f88, this.w) - AndroidUtilities.dp(12.0f);
                f11 = 0.0f;
                canvas.clipRect(dpf22, 0.0f, min5, this.h);
                if (this.audioAuthor != null) {
                    canvas.save();
                    canvas.translate(dpf22 - this.audioAuthorLeft, dp2 - (this.audioAuthor.getHeight() / 2.0f));
                    this.audioAuthorPaint.setAlpha((int) (f90 * f71));
                    this.audioAuthor.draw(canvas);
                    canvas.restore();
                    f24 = dpf22 + this.audioAuthorWidth;
                } else {
                    f24 = dpf22;
                }
                if (this.audioAuthor != null && this.audioTitle != null) {
                    float dpf23 = f24 + AndroidUtilities.dpf2(3.66f);
                    int alpha = this.audioDotPaint.getAlpha();
                    this.audioDotPaint.setAlpha((int) (alpha * f89));
                    canvas.drawCircle(AndroidUtilities.dp(1.0f) + dpf23, dp2, AndroidUtilities.dp(1.0f), this.audioDotPaint);
                    this.audioDotPaint.setAlpha(alpha);
                    f24 = dpf23 + AndroidUtilities.dpf2(2.0f) + AndroidUtilities.dpf2(4.0f);
                }
                if (this.audioTitle != null) {
                    canvas.save();
                    canvas.translate(f24 - this.audioTitleLeft, dp2 - (this.audioTitle.getHeight() / 2.0f));
                    this.audioTitlePaint.setAlpha((int) (f90 * f71));
                    this.audioTitle.draw(canvas);
                    canvas.restore();
                }
                if (!z5) {
                    this.ellipsizeMatrix.reset();
                    this.ellipsizeMatrix.postScale(AndroidUtilities.dpf2(8.0f) / 16.0f, 1.0f);
                    this.ellipsizeMatrix.postTranslate(min5 - AndroidUtilities.dp(8.0f), 0.0f);
                    this.ellipsizeGradient.setLocalMatrix(this.ellipsizeMatrix);
                    canvas.drawRect(min5 - AndroidUtilities.dp(8.0f), f79, min5, f77, this.ellipsizePaint);
                }
                canvas.restore();
            } else {
                d = d2;
                j = min;
                f7 = f;
                f8 = f4;
                f9 = f3;
                f10 = f5;
                f11 = 0.0f;
            }
            canvas.restore();
        } else {
            j = min;
            f7 = f;
            f8 = f4;
            f9 = f3;
            f10 = f5;
            f11 = 0.0f;
            d = 0.0d;
        }
        boolean z6 = this.hasVideo;
        float f91 = ((z6 || this.hasRound) ? f72 : 1.0f) * f71;
        if (z6 || this.hasAudio) {
            f12 = f9;
            f13 = f8;
        } else {
            f13 = f8;
            f12 = 1.0f;
        }
        float f92 = f13 * f12;
        float f93 = this.h - this.py;
        float f94 = f93 - f6;
        float f95 = f7;
        float f96 = f10 * f95;
        float f97 = f94 - f96;
        float f98 = f97 - roundHeight;
        float f99 = f98 - (f10 * f13);
        float f100 = ((f99 - audioHeight) * f91) + f11 + (f98 * f92) + (f94 * f29);
        float f101 = (f99 * f91) + f11 + (f97 * f92) + (f93 * f29);
        float f102 = this.audioOffset;
        float f103 = this.audioLeft;
        float f104 = this.audioDuration;
        float f105 = this.roundOffset;
        float f106 = this.roundLeft;
        float f107 = f71;
        float f108 = this.roundDuration;
        float f109 = (((f103 * f104) + f102) * f91) + f11 + (((f106 * f108) + f105) * f92);
        float f110 = this.videoLeft;
        float f111 = f13;
        float f112 = this.videoDuration;
        float f113 = ((f102 + (this.audioRight * f104)) * f91) + f11 + ((f105 + (this.roundRight * f108)) * f92) + (this.videoRight * f112 * f29);
        float f114 = this.px + this.ph;
        float f115 = this.scroll;
        long j9 = j;
        float f116 = j9;
        float f117 = this.sw;
        float f118 = ((((f109 + ((f110 * f112) * f29)) - f115) / f116) * f117) + f114;
        float f119 = f114 + (((f113 - f115) / f116) * f117);
        if (!this.hasAudio || z6) {
            f14 = f111;
            f15 = f107;
            f107 = Math.max(f95, f14);
        } else {
            f15 = f107;
            f14 = f111;
        }
        if (f15 > d || f14 > d || f95 > d) {
            if (this.hasVideo || this.hasRound) {
                f16 = f72;
                f17 = 1.0f;
            } else {
                f16 = f72;
                f17 = AndroidUtilities.lerp(0.6f, 1.0f, f16) * f15;
            }
            float f120 = f16;
            j2 = j9;
            f18 = 0.0f;
            drawRegion(canvas, paint, f100, f101, f118, f119, f17 * f107);
            if (this.hasVideo && ((this.hasAudio || this.hasRound) && (f120 > 0.0f || f9 > 0.0f))) {
                float f121 = this.h - this.py;
                float f122 = this.ph + this.px;
                float f123 = this.videoLeft;
                float f124 = this.videoDuration;
                float f125 = this.scroll;
                float f126 = this.sw;
                drawRegion(canvas, paint, f121 - f6, f121, f122 + ((((f123 * f124) - f125) / f116) * f126), f122 + ((((this.videoRight * f124) - f125) / f116) * f126), 0.8f);
            }
            float f127 = this.loopProgress.set(0.0f);
            float max6 = ((((this.h - this.py) - f6) - ((audioHeight + (f10 * Math.max(f14, f95))) * f15)) - ((roundHeight + f96) * f14)) - AndroidUtilities.dpf2(4.3f);
            float dpf24 = (this.h - this.py) + AndroidUtilities.dpf2(4.3f);
            if (f127 > 0.0f) {
                long j10 = this.loopProgressFrom;
                if (j10 == -1) {
                    if (this.hasVideo) {
                        f19 = this.videoDuration;
                        f20 = this.videoRight;
                    } else if (this.hasRound) {
                        f19 = this.roundDuration;
                        f20 = this.roundRight;
                    } else {
                        f19 = this.audioDuration;
                        f20 = this.audioRight;
                    }
                    j10 = (long) (f19 * f20);
                }
                drawProgress(canvas, max6, dpf24, j10, f127 * f107);
            }
            drawProgress(canvas, max6, dpf24, this.progress, (1.0f - f127) * f107);
        } else {
            j2 = j9;
            f18 = 0.0f;
        }
        if (this.dragged) {
            long dp3 = (long) ((AndroidUtilities.dp(32.0f) / this.sw) * f116 * (1.0f / (1000.0f / AndroidUtilities.screenRefreshRate)));
            int i13 = this.pressHandle;
            if (i13 == 4) {
                float f128 = this.videoLeft;
                long j11 = this.scroll;
                long j12 = this.videoDuration;
                float f129 = j12;
                long j13 = (f128 >= ((float) j11) / f129 ? this.videoRight > ((float) (j11 + j2)) / f129 ? 1 : 0 : -1) * dp3;
                this.scroll = Utilities.clamp(j11 + j13, j12 - j2, 0L);
                this.progress += j13;
                float f130 = (r0 - j11) / this.videoDuration;
                if (f130 > f18) {
                    f23 = 1.0f;
                    max = Math.min(1.0f - this.videoRight, f130);
                } else {
                    f23 = 1.0f;
                    max = Math.max(f18 - this.videoLeft, f130);
                }
                this.videoLeft = Utilities.clamp(this.videoLeft + max, f23, f18);
                this.videoRight = Utilities.clamp(this.videoRight + max, f23, f18);
                TimelineDelegate timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    timelineDelegate2.onVideoLeftChange(this.videoLeft);
                    this.delegate.onVideoRightChange(this.videoRight);
                }
            } else {
                if (i13 != 8) {
                    return;
                }
                float f131 = this.audioLeft;
                long j14 = this.audioOffset;
                long j15 = -j14;
                float f132 = j15 + 100;
                long j16 = this.audioDuration;
                float f133 = j16;
                int i14 = f131 >= f132 / f133 ? this.audioRight >= ((float) ((j15 + j2) - 100)) / f133 ? 1 : 0 : -1;
                if (i14 == 0) {
                    return;
                }
                if (this.audioSelected && this.hasVideo) {
                    j3 = j14 - (i14 * dp3);
                    float f134 = this.videoRight;
                    f21 = this.videoDuration;
                    j4 = (long) ((f134 * f21) - (f131 * f133));
                    f22 = this.videoLeft;
                } else if (this.roundSelected && this.hasRound) {
                    j3 = j14 - (i14 * dp3);
                    float f135 = this.roundRight;
                    f21 = this.roundDuration;
                    j4 = (long) ((f135 * f21) - (f131 * f133));
                    f22 = this.roundLeft;
                } else {
                    clamp = Utilities.clamp(j14 - (i14 * dp3), 0L, -(j16 - Math.min(getBaseDuration(), 120000L)));
                    this.audioOffset = clamp;
                    float f136 = (-(this.audioOffset - j14)) / this.audioDuration;
                    float min6 = f136 <= f18 ? Math.min(1.0f - this.audioRight, f136) : Math.max(f18 - this.audioLeft, f136);
                    if (!this.hasVideo) {
                        float f137 = this.progress;
                        float f138 = this.audioDuration;
                        this.progress = (long) Utilities.clamp(f137 + (min6 * f138), f138, f18);
                    }
                    this.audioLeft = Utilities.clamp(this.audioLeft + min6, 1.0f, f18);
                    this.audioRight = Utilities.clamp(this.audioRight + min6, 1.0f, f18);
                    timelineDelegate = this.delegate;
                    if (timelineDelegate != null) {
                        timelineDelegate.onAudioLeftChange(this.audioLeft);
                        this.delegate.onAudioRightChange(this.audioRight);
                        this.delegate.onProgressChange(this.progress, false);
                    }
                }
                clamp = Utilities.clamp(j3, j4, (long) ((f22 * f21) - (this.audioRight * f133)));
                this.audioOffset = clamp;
                float f1362 = (-(this.audioOffset - j14)) / this.audioDuration;
                if (f1362 <= f18) {
                }
                if (!this.hasVideo) {
                }
                this.audioLeft = Utilities.clamp(this.audioLeft + min6, 1.0f, f18);
                this.audioRight = Utilities.clamp(this.audioRight + min6, 1.0f, f18);
                timelineDelegate = this.delegate;
                if (timelineDelegate != null) {
                }
            }
            invalidate();
        }
    }

    public int getContentHeight() {
        return (int) (this.py + (this.hasVideo ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + (this.hasAudio ? AndroidUtilities.dp(4.0f) + getAudioHeight() : 0.0f) + this.py);
    }

    public boolean isDragging() {
        return this.dragged;
    }

    public boolean onBackPressed() {
        boolean z = false;
        if (this.audioSelected) {
            this.audioSelected = false;
            z = true;
            if (this.hasRound && !this.hasVideo) {
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
        float[] fArr = this.waveformRadii;
        float dp = AndroidUtilities.dp(2.0f);
        fArr[3] = dp;
        fArr[2] = dp;
        fArr[1] = dp;
        fArr[0] = dp;
        float[] fArr2 = this.waveformRadii;
        fArr2[7] = 0.0f;
        fArr2[6] = 0.0f;
        fArr2[5] = 0.0f;
        fArr2[4] = 0.0f;
        int dp2 = AndroidUtilities.dp(12.0f);
        this.px = dp2;
        int dp3 = AndroidUtilities.dp(5.0f);
        this.py = dp3;
        setPadding(dp2, dp3, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(5.0f));
        int size = View.MeasureSpec.getSize(i);
        this.w = size;
        int dp4 = AndroidUtilities.dp(heightDp());
        this.h = dp4;
        setMeasuredDimension(size, dp4);
        int dp5 = AndroidUtilities.dp(10.0f);
        this.ph = dp5;
        this.sw = (this.w - (dp5 * 2)) - (this.px * 2);
        if (this.videoPath != null && this.thumbs == null) {
            setupVideoThumbs(false);
        }
        if (this.audioPath == null || this.waveform != null) {
            return;
        }
        setupAudioWaveform();
    }

    /* JADX WARN: Code restructure failed: missing block: B:161:0x03ac, code lost:
    
        if (r1 != null) goto L247;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0572, code lost:
    
        r1.onProgressDragChange(true);
        r4 = false;
        r26.delegate.onProgressChange(r26.progress, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x0570, code lost:
    
        if (r1 != null) goto L247;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x06ea, code lost:
    
        if (r1 != null) goto L337;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x071f, code lost:
    
        r1.onRoundSelectChange(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x070d, code lost:
    
        if (r1 != null) goto L337;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x071d, code lost:
    
        if (r1 != null) goto L337;
     */
    /* JADX WARN: Removed duplicated region for block: B:347:0x08cc  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x08d6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:356:0x08f1  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        Runnable runnable;
        boolean z2;
        VelocityTracker velocityTracker;
        TimelineDelegate timelineDelegate;
        VelocityTracker velocityTracker2;
        int xVelocity;
        float min;
        int i;
        long j;
        int i2;
        long j2;
        float f;
        long j3;
        float f2;
        float f3;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        Scroller scroller;
        int i9;
        VelocityTracker velocityTracker3;
        VelocityTracker velocityTracker4;
        boolean z3;
        TimelineDelegate timelineDelegate2;
        boolean z4;
        float f4;
        long j4;
        TimelineDelegate timelineDelegate3;
        float f5;
        long j5;
        long j6;
        float min2;
        float max;
        TimelineDelegate timelineDelegate4;
        VelocityTracker velocityTracker5;
        boolean z5 = this.hasVideo;
        if (!z5 && !this.hasAudio && !this.hasRound) {
            return false;
        }
        int i10 = this.h;
        int i11 = this.py;
        float videoHeight = ((((i10 - i11) - i11) - (z5 ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasAudio ? getAudioHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f);
        if (motionEvent.getAction() == 0 && motionEvent.getY() < videoHeight) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (motionEvent.getAction() == 0) {
            Runnable runnable2 = this.askExactSeek;
            if (runnable2 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable2);
                this.askExactSeek = null;
            }
            this.scroller.abortAnimation();
            this.pressHandle = detectHandle(motionEvent);
            this.pressType = -1;
            int i12 = this.h - this.py;
            if (this.hasVideo) {
                float f6 = i12;
                if (motionEvent.getY() < f6 && motionEvent.getY() > (f6 - getVideoHeight()) - AndroidUtilities.dp(2.0f)) {
                    this.pressType = 0;
                }
                i12 = (int) (f6 - (getVideoHeight() + AndroidUtilities.dp(4.0f)));
            }
            if (this.pressType == -1 && this.hasRound) {
                float f7 = i12;
                if (motionEvent.getY() < f7 && motionEvent.getY() > (f7 - getRoundHeight()) - AndroidUtilities.dp(2.0f)) {
                    this.pressType = 1;
                }
                i12 = (int) (f7 - (getRoundHeight() + AndroidUtilities.dp(4.0f)));
            }
            if (this.pressType == -1 && this.hasAudio) {
                float f8 = i12;
                if (motionEvent.getY() < f8 && motionEvent.getY() > (f8 - getAudioHeight()) - AndroidUtilities.dp(2.0f)) {
                    this.pressType = 2;
                }
                getAudioHeight();
                AndroidUtilities.dp(4.0f);
            }
            this.pressTime = System.currentTimeMillis();
            int i13 = this.pressHandle;
            this.draggingProgress = i13 == 0 || i13 == -1 || i13 == 1;
            this.hadDragChange = false;
            if (i13 == 1 || i13 == 5 || i13 == 8) {
                this.velocityTracker = VelocityTracker.obtain();
            } else {
                VelocityTracker velocityTracker6 = this.velocityTracker;
                if (velocityTracker6 != null) {
                    velocityTracker6.recycle();
                    this.velocityTracker = null;
                }
            }
            this.dragged = false;
            this.lastX = motionEvent.getX();
            if (!this.isCover) {
                AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                AndroidUtilities.runOnUIThread(this.onLongPress, ViewConfiguration.getLongPressTimeout());
            }
        } else if (motionEvent.getAction() == 2) {
            float x = motionEvent.getX() - this.lastX;
            if (this.dragged || Math.abs(x) > AndroidUtilities.touchSlop) {
                long min3 = Math.min(getBaseDuration(), 120000L);
                int i14 = this.pressHandle;
                if (i14 == 1) {
                    this.scroll = (long) Utilities.clamp(this.scroll - ((x / this.sw) * min3), this.videoDuration - min3, 0.0f);
                    invalidate();
                } else if (i14 == 2 || i14 == 3 || i14 == 4) {
                    float f9 = this.videoDuration;
                    float f10 = (x / this.sw) * (min3 / f9);
                    if (i14 == 2) {
                        float clamp = Utilities.clamp(this.videoLeft + f10, this.videoRight - (1000.0f / f9), 0.0f);
                        this.videoLeft = clamp;
                        TimelineDelegate timelineDelegate5 = this.delegate;
                        if (timelineDelegate5 != null) {
                            timelineDelegate5.onVideoLeftChange(clamp);
                        }
                        float f11 = this.videoRight;
                        float f12 = this.videoLeft;
                        float f13 = 59000.0f / this.videoDuration;
                        if (f11 - f12 > f13) {
                            float min4 = Math.min(1.0f, f12 + f13);
                            this.videoRight = min4;
                            TimelineDelegate timelineDelegate6 = this.delegate;
                            if (timelineDelegate6 != null) {
                                timelineDelegate6.onVideoRightChange(min4);
                            }
                        }
                    } else if (i14 == 3) {
                        float clamp2 = Utilities.clamp(this.videoRight + f10, 1.0f, this.videoLeft + (1000.0f / f9));
                        this.videoRight = clamp2;
                        TimelineDelegate timelineDelegate7 = this.delegate;
                        if (timelineDelegate7 != null) {
                            timelineDelegate7.onVideoRightChange(clamp2);
                        }
                        float f14 = this.videoRight;
                        float f15 = 59000.0f / this.videoDuration;
                        if (f14 - this.videoLeft > f15) {
                            float max2 = Math.max(0.0f, f14 - f15);
                            this.videoLeft = max2;
                            TimelineDelegate timelineDelegate8 = this.delegate;
                            if (timelineDelegate8 != null) {
                                timelineDelegate8.onVideoLeftChange(max2);
                            }
                        }
                    } else if (i14 == 4) {
                        float min5 = f10 > 0.0f ? Math.min(1.0f - this.videoRight, f10) : Math.max(-this.videoLeft, f10);
                        float f16 = this.videoLeft + min5;
                        this.videoLeft = f16;
                        this.videoRight += min5;
                        TimelineDelegate timelineDelegate9 = this.delegate;
                        if (timelineDelegate9 != null) {
                            timelineDelegate9.onVideoLeftChange(f16);
                            this.delegate.onVideoRightChange(this.videoRight);
                        }
                    }
                    float f17 = this.progress;
                    float f18 = this.videoDuration;
                    float f19 = f17 / f18;
                    float f20 = this.videoLeft;
                    if (f19 < f20 || f19 > this.videoRight) {
                        long j7 = (long) (f20 * f18);
                        this.progress = j7;
                        TimelineDelegate timelineDelegate10 = this.delegate;
                        if (timelineDelegate10 != null) {
                            z4 = false;
                            timelineDelegate10.onProgressChange(j7, false);
                            invalidate();
                            this.dragged = true;
                            this.draggingProgress = z4;
                            this.lastX = motionEvent.getX();
                        }
                    }
                    z4 = false;
                    invalidate();
                    this.dragged = true;
                    this.draggingProgress = z4;
                    this.lastX = motionEvent.getX();
                } else {
                    if (i14 == 6 || i14 == 7 || i14 == 8) {
                        float f21 = (x / this.sw) * (min3 / this.audioDuration);
                        if (i14 == 6) {
                            float minAudioSelect = this.audioRight - (minAudioSelect() / this.audioDuration);
                            float max3 = Math.max(0L, this.scroll - this.audioOffset);
                            float f22 = this.audioDuration;
                            float f23 = max3 / f22;
                            boolean z6 = this.hasVideo;
                            if (z6 || this.hasRound) {
                                if (z6) {
                                    f5 = this.videoLeft;
                                    j5 = this.videoDuration;
                                } else if (this.hasRound) {
                                    f5 = this.roundLeft;
                                    j5 = this.roundDuration;
                                }
                                f23 = Math.max(f23, (((f5 * j5) + this.scroll) - this.audioOffset) / f22);
                            } else {
                                f23 = Math.max(f23, this.audioRight - (59000.0f / f22));
                                if (!this.hadDragChange && f21 < 0.0f && this.audioLeft <= this.audioRight - (59000.0f / this.audioDuration)) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f24 = this.audioLeft;
                            float clamp3 = Utilities.clamp(f24 + f21, minAudioSelect, f23);
                            this.audioLeft = clamp3;
                            if (Math.abs(f24 - clamp3) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate11 = this.delegate;
                            if (timelineDelegate11 != null) {
                                timelineDelegate11.onAudioOffsetChange(this.audioOffset + ((long) (this.audioLeft * this.audioDuration)));
                            }
                            TimelineDelegate timelineDelegate12 = this.delegate;
                            if (timelineDelegate12 != null) {
                                timelineDelegate12.onAudioLeftChange(this.audioLeft);
                            }
                        } else if (i14 == 7) {
                            float min6 = Math.min(1.0f, Math.max(0L, (this.scroll - this.audioOffset) + min3) / this.audioDuration);
                            float f25 = this.audioLeft;
                            float minAudioSelect2 = minAudioSelect();
                            float f26 = this.audioDuration;
                            float f27 = f25 + (minAudioSelect2 / f26);
                            boolean z7 = this.hasVideo;
                            if (z7 || this.hasRound) {
                                if (z7) {
                                    f4 = this.videoRight;
                                    j4 = this.videoDuration;
                                } else if (this.hasRound) {
                                    f4 = this.roundRight;
                                    j4 = this.roundDuration;
                                }
                                min6 = Math.min(min6, (((f4 * j4) + this.scroll) - this.audioOffset) / f26);
                            } else {
                                min6 = Math.min(min6, this.audioLeft + (59000.0f / f26));
                                if (!this.hadDragChange && f21 > 0.0f && this.audioRight >= this.audioLeft + (59000.0f / this.audioDuration)) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f28 = this.audioRight;
                            float clamp4 = Utilities.clamp(f28 + f21, min6, f27);
                            this.audioRight = clamp4;
                            if (Math.abs(f28 - clamp4) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate13 = this.delegate;
                            if (timelineDelegate13 != null) {
                                timelineDelegate13.onAudioRightChange(this.audioRight);
                            }
                        }
                        if (this.pressHandle == 8) {
                            float min7 = f21 > 0.0f ? Math.min(Math.max(0.0f, Math.min(1.0f, Math.max(0L, (this.scroll - this.audioOffset) + min3) / this.audioDuration) - this.audioRight), f21) : Math.max(Math.min(0.0f, (Math.max(0L, this.scroll - this.audioOffset) / this.audioDuration) - this.audioLeft), f21);
                            float f29 = this.audioLeft + min7;
                            this.audioLeft = f29;
                            this.audioRight += min7;
                            TimelineDelegate timelineDelegate14 = this.delegate;
                            if (timelineDelegate14 != null) {
                                timelineDelegate14.onAudioLeftChange(f29);
                                this.delegate.onAudioOffsetChange(this.audioOffset + ((long) (this.audioLeft * this.audioDuration)));
                                this.delegate.onAudioRightChange(this.audioRight);
                            }
                            TimelineDelegate timelineDelegate15 = this.delegate;
                            if (timelineDelegate15 != null) {
                                timelineDelegate15.onProgressDragChange(true);
                            }
                        }
                        if (!this.hasVideo && !this.hasRound) {
                            this.progress = (long) (this.audioLeft * this.audioDuration);
                            timelineDelegate3 = this.delegate;
                        }
                        z4 = false;
                    } else if (i14 == 10 || i14 == 11 || i14 == 12) {
                        float f30 = (x / this.sw) * (min3 / this.roundDuration);
                        if (i14 == 10) {
                            float minAudioSelect3 = this.roundRight - (minAudioSelect() / this.roundDuration);
                            j6 = min3;
                            float max4 = Math.max(0L, this.scroll - this.roundOffset);
                            float f31 = this.roundDuration;
                            float f32 = max4 / f31;
                            if (this.hasVideo) {
                                max = Math.max(f32, (((this.videoLeft * this.videoDuration) + this.scroll) - this.roundOffset) / f31);
                            } else {
                                max = Math.max(f32, this.roundRight - (59000.0f / f31));
                                if (!this.hadDragChange && f30 < 0.0f && this.roundLeft <= this.roundRight - (59000.0f / this.roundDuration)) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f33 = this.roundLeft;
                            float clamp5 = Utilities.clamp(f33 + f30, minAudioSelect3, max);
                            this.roundLeft = clamp5;
                            if (Math.abs(f33 - clamp5) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate16 = this.delegate;
                            if (timelineDelegate16 != null) {
                                timelineDelegate16.onRoundOffsetChange(this.roundOffset + ((long) (this.roundLeft * this.roundDuration)));
                            }
                            TimelineDelegate timelineDelegate17 = this.delegate;
                            if (timelineDelegate17 != null) {
                                timelineDelegate17.onRoundLeftChange(this.roundLeft);
                            }
                        } else {
                            j6 = min3;
                            if (i14 == 11) {
                                float min8 = Math.min(1.0f, Math.max(0L, (this.scroll - this.roundOffset) + j6) / this.roundDuration);
                                float f34 = this.roundLeft;
                                float minAudioSelect4 = minAudioSelect();
                                float f35 = this.roundDuration;
                                float f36 = f34 + (minAudioSelect4 / f35);
                                if (this.hasVideo) {
                                    min2 = Math.min(min8, (((this.videoRight * this.videoDuration) + this.scroll) - this.roundOffset) / f35);
                                } else {
                                    min2 = Math.min(min8, this.roundLeft + (59000.0f / f35));
                                    if (!this.hadDragChange && f30 > 0.0f && this.roundRight >= this.roundLeft + (59000.0f / this.roundDuration)) {
                                        this.pressHandle = 8;
                                    }
                                }
                                float f37 = this.roundRight;
                                float clamp6 = Utilities.clamp(f37 + f30, min2, f36);
                                this.roundRight = clamp6;
                                if (Math.abs(f37 - clamp6) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate18 = this.delegate;
                                if (timelineDelegate18 != null) {
                                    timelineDelegate18.onRoundRightChange(this.roundRight);
                                }
                            }
                        }
                        if (this.pressHandle == 12) {
                            float min9 = f30 > 0.0f ? Math.min(Math.min(1.0f, Math.max(0L, (this.scroll - this.roundOffset) + j6) / this.roundDuration) - this.roundRight, f30) : Math.max((Math.max(0L, this.scroll - this.roundOffset) / this.roundDuration) - this.roundLeft, f30);
                            float f38 = this.roundLeft + min9;
                            this.roundLeft = f38;
                            this.roundRight += min9;
                            TimelineDelegate timelineDelegate19 = this.delegate;
                            if (timelineDelegate19 != null) {
                                timelineDelegate19.onRoundLeftChange(f38);
                                this.delegate.onRoundOffsetChange(this.roundOffset + ((long) (this.roundLeft * this.roundDuration)));
                                this.delegate.onRoundRightChange(this.roundRight);
                            }
                            TimelineDelegate timelineDelegate20 = this.delegate;
                            if (timelineDelegate20 != null) {
                                timelineDelegate20.onProgressDragChange(true);
                            }
                        }
                        if (!this.hasVideo) {
                            this.progress = (long) (this.roundLeft * this.roundDuration);
                            timelineDelegate3 = this.delegate;
                        }
                        z4 = false;
                    } else if (i14 == 5) {
                        moveAudioOffset((x / this.sw) * min3);
                    } else if (i14 == 9) {
                        moveRoundOffset((x / this.sw) * min3);
                    } else {
                        if (this.draggingProgress) {
                            setProgressAt(motionEvent.getX(), currentTimeMillis - this.lastTime < 350);
                            if (!this.dragged && (timelineDelegate4 = this.delegate) != null) {
                                timelineDelegate4.onProgressDragChange(true);
                            }
                            this.dragged = true;
                        }
                        this.lastX = motionEvent.getX();
                    }
                    invalidate();
                    this.dragged = true;
                    this.draggingProgress = z4;
                    this.lastX = motionEvent.getX();
                }
                this.dragged = true;
                this.draggingProgress = false;
                this.lastX = motionEvent.getX();
            }
            if (this.dragged) {
                AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
            }
            int i15 = this.pressHandle;
            if ((i15 == 1 || i15 == 5 || i15 == 8) && (velocityTracker5 = this.velocityTracker) != null) {
                velocityTracker5.addMovement(motionEvent);
            }
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
            this.scroller.abortAnimation();
            if (motionEvent.getAction() == 1) {
                if (System.currentTimeMillis() - this.pressTime > ViewConfiguration.getTapTimeout() || this.dragged) {
                    int i16 = this.pressHandle;
                    if (i16 != 1 || (velocityTracker4 = this.velocityTracker) == null) {
                        if ((i16 == 5 || (i16 == 8 && !this.dragged)) && this.audioSelected && (velocityTracker2 = this.velocityTracker) != null) {
                            velocityTracker2.computeCurrentVelocity(this.hasVideo ? 1000 : 1500);
                            xVelocity = (int) this.velocityTracker.getXVelocity();
                            this.scrollingVideo = false;
                            if (Math.abs(xVelocity) > AndroidUtilities.dp(100.0f)) {
                                min = Math.min(getBaseDuration(), 120000L);
                                i = (int) (this.px + this.ph + ((this.audioOffset / min) * this.sw));
                                if (this.hasVideo) {
                                    float f39 = this.videoRight;
                                    f = this.videoDuration;
                                    f3 = f39 * f;
                                    j3 = this.audioDuration;
                                    j2 = (long) (f3 - 0);
                                    f2 = this.videoLeft;
                                    j = (long) ((f2 * f) - j3);
                                    i2 = xVelocity;
                                } else if (this.hasRound) {
                                    float f40 = this.roundRight;
                                    f = this.roundDuration;
                                    j3 = this.audioDuration;
                                    j2 = (long) ((f40 * f) - 0);
                                    f2 = this.roundLeft;
                                    j = (long) ((f2 * f) - j3);
                                    i2 = xVelocity;
                                } else {
                                    j = -(this.audioDuration - Math.min(getBaseDuration(), 120000L));
                                    i2 = xVelocity;
                                    j2 = 0;
                                }
                            }
                        } else if ((i16 == 9 || (i16 == 12 && !this.dragged)) && this.roundSelected && (velocityTracker3 = this.velocityTracker) != null) {
                            velocityTracker3.computeCurrentVelocity(this.hasVideo ? 1000 : 1500);
                            xVelocity = (int) this.velocityTracker.getXVelocity();
                            this.scrollingVideo = false;
                            if (Math.abs(xVelocity) > AndroidUtilities.dp(100.0f)) {
                                min = Math.min(getBaseDuration(), 120000L);
                                i = (int) (this.px + this.ph + ((this.roundOffset / min) * this.sw));
                                if (this.hasVideo) {
                                    float f41 = this.videoRight;
                                    f = this.videoDuration;
                                    f3 = f41 * f;
                                    j3 = this.roundDuration;
                                    j2 = (long) (f3 - 0);
                                    f2 = this.videoLeft;
                                    j = (long) ((f2 * f) - j3);
                                    i2 = xVelocity;
                                } else {
                                    j2 = 0;
                                    j = -(this.roundDuration - Math.min(getBaseDuration(), 120000L));
                                    i2 = xVelocity;
                                }
                            }
                        }
                        this.scrolling = true;
                        Scroller scroller2 = this.scroller;
                        this.wasScrollX = i;
                        float f42 = this.px + this.ph;
                        float f43 = this.sw;
                        i3 = (int) (((j / min) * f43) + f42);
                        i4 = (int) (f42 + ((j2 / min) * f43));
                        i5 = 0;
                        i6 = 0;
                        i7 = 0;
                        i8 = 0;
                        scroller = scroller2;
                        i9 = i;
                        scroller.fling(i9, i7, i2, i8, i3, i4, i5, i6);
                        z = false;
                    } else {
                        velocityTracker4.computeCurrentVelocity(1000);
                        int xVelocity2 = (int) this.velocityTracker.getXVelocity();
                        this.scrollingVideo = true;
                        if (Math.abs(xVelocity2) > AndroidUtilities.dp(100.0f)) {
                            long min10 = Math.min(this.videoDuration, 120000L);
                            i3 = this.px;
                            float f44 = i3;
                            float f45 = min10;
                            float f46 = this.sw;
                            int i17 = (int) (((this.scroll / f45) * f46) + f44);
                            i4 = (int) (f44 + (((this.videoDuration - min10) / f45) * f46));
                            this.scrolling = true;
                            Scroller scroller3 = this.scroller;
                            this.wasScrollX = i17;
                            i5 = 0;
                            i6 = 0;
                            i7 = 0;
                            i8 = 0;
                            scroller = scroller3;
                            i9 = i17;
                            i2 = -xVelocity2;
                            scroller.fling(i9, i7, i2, i8, i3, i4, i5, i6);
                            z = false;
                        }
                    }
                    runnable = this.askExactSeek;
                    if (runnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.askExactSeek = null;
                    }
                    if (this.dragged || !z || (timelineDelegate = this.delegate) == null) {
                        z2 = false;
                    } else {
                        z2 = false;
                        timelineDelegate.onProgressDragChange(false);
                    }
                    this.dragged = z2;
                    this.draggingProgress = z2;
                    this.pressTime = -1L;
                    this.pressHandle = -1;
                    velocityTracker = this.velocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        this.velocityTracker = null;
                    }
                } else {
                    if (this.isCover) {
                        float f47 = this.videoRight - this.videoLeft;
                        float x2 = (((motionEvent.getX() - this.px) - this.ph) / this.sw) * (1.0f - f47);
                        this.videoLeft = x2;
                        this.videoRight = f47 + x2;
                        TimelineDelegate timelineDelegate21 = this.delegate;
                        if (timelineDelegate21 != null) {
                            timelineDelegate21.onVideoLeftChange(x2);
                            this.delegate.onVideoRightChange(this.videoRight);
                        }
                    } else {
                        int i18 = this.pressType;
                        if (i18 == 2 && !this.audioSelected) {
                            this.audioSelected = true;
                            z3 = false;
                            this.roundSelected = false;
                            timelineDelegate2 = this.delegate;
                        } else if (i18 == 1 && !this.roundSelected) {
                            this.audioSelected = false;
                            this.roundSelected = true;
                            TimelineDelegate timelineDelegate22 = this.delegate;
                            if (timelineDelegate22 != null) {
                                timelineDelegate22.onRoundSelectChange(true);
                            }
                        } else if (i18 == 2 || !this.audioSelected) {
                            z3 = false;
                            if (i18 == 1 || !this.roundSelected) {
                                long j8 = this.progress;
                                if (setProgressAt(motionEvent.getX(), false) && Math.abs(this.progress - j8) > 400) {
                                    this.loopProgressFrom = j8;
                                    this.loopProgress.set(1.0f, true);
                                }
                            } else {
                                this.audioSelected = false;
                                this.roundSelected = false;
                                timelineDelegate2 = this.delegate;
                            }
                            invalidate();
                        } else {
                            z3 = false;
                            this.audioSelected = false;
                            this.roundSelected = false;
                            timelineDelegate2 = this.delegate;
                        }
                        runnable = this.askExactSeek;
                        if (runnable != null) {
                        }
                        if (this.dragged) {
                        }
                        z2 = false;
                        this.dragged = z2;
                        this.draggingProgress = z2;
                        this.pressTime = -1L;
                        this.pressHandle = -1;
                        velocityTracker = this.velocityTracker;
                        if (velocityTracker != null) {
                        }
                    }
                    invalidate();
                }
            }
            z = true;
            runnable = this.askExactSeek;
            if (runnable != null) {
            }
            if (this.dragged) {
            }
            z2 = false;
            this.dragged = z2;
            this.draggingProgress = z2;
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
            this.audioSelected = this.hasAudio && !this.hasVideo;
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
                this.waveformLoaded.set(0.0f, true);
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

    public void setCover() {
        this.isCover = true;
    }

    public void setCoverVideo(long j, long j2) {
        this.coverStart = j;
        this.coverEnd = j2;
        setupVideoThumbs(true);
    }

    public void setDelegate(TimelineDelegate timelineDelegate) {
        this.delegate = timelineDelegate;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004a, code lost:
    
        if ((r4 + 240) >= (r7 * r9.audioRight)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006d, code lost:
    
        if ((r4 + 240) >= (r6 * r9.audioRight)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0023, code lost:
    
        if ((r4 + 240) >= (r7 * r9.videoRight)) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x006f, code lost:
    
        r9.loopProgressFrom = -1;
        r9.loopProgress.set(1.0f, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setProgress(long j) {
        boolean z = this.hasVideo;
        if (z) {
            if (j < this.progress) {
                float f = j;
                float f2 = this.videoDuration;
                if (f <= (this.videoLeft * f2) + 240.0f) {
                }
            }
        }
        if (this.hasAudio && !this.hasRound && !z) {
            if (j < this.progress) {
                float f3 = j;
                float f4 = this.audioDuration;
                if (f3 <= (this.audioLeft * f4) + 240.0f) {
                }
            }
        }
        if (this.hasRound && !z) {
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
            if (!this.hasVideo) {
                this.audioSelected = false;
                this.roundSelected = true;
            }
        } else {
            this.roundPath = null;
            this.roundDuration = 1L;
            this.roundSelected = false;
        }
        this.hasRound = this.roundPath != null;
        if (j3 != j && !this.hasVideo && this.waveform != null) {
            this.resetWaveform = true;
            setupAudioWaveform();
        }
        if (this.hasAudio && this.hasRound && !this.hasVideo) {
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
        if (TextUtils.equals(this.videoPath, str)) {
            return;
        }
        VideoThumbsLoader videoThumbsLoader = this.thumbs;
        if (videoThumbsLoader != null) {
            videoThumbsLoader.destroy();
            this.thumbs = null;
        }
        this.isMainVideoRound = z;
        if (str != null) {
            this.scroll = 0L;
            this.videoPath = str;
            this.videoDuration = j;
            this.videoVolume = f;
            setupVideoThumbs(false);
        } else {
            this.videoPath = null;
            this.videoDuration = 1L;
            this.scroll = 0L;
        }
        if (!this.hasRound) {
            this.roundSelected = false;
        }
        this.hasVideo = this.videoPath != null;
        this.progress = 0L;
        invalidate();
    }

    public void setVideoLeft(float f) {
        this.videoLeft = f;
        invalidate();
    }

    public void setVideoRight(float f) {
        this.videoRight = f;
        invalidate();
    }
}
