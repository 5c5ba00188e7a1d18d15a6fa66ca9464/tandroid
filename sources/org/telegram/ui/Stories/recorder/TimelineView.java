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
/* loaded from: classes4.dex */
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

    /* loaded from: classes4.dex */
    public class AudioWaveformLoader {
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
            TimelineView.this = r8;
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
            int round = Math.round(((((float) (this.duration * 1000)) / ((float) Math.min(r8.hasVideo ? r8.videoDuration : r8.hasRound ? r8.roundDuration : this.duration * 1000, 120000L))) * i) / Math.round(AndroidUtilities.dpf2(3.3333f)));
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

        /* renamed from: receiveData */
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

        /* JADX WARN: Code restructure failed: missing block: B:170:0x015d, code lost:
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
                int round = Math.round((((float) (this.duration * this.inputFormat.getInteger("sample-rate"))) / this.count) / 5.0f);
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
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda2
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    TimelineView.AudioWaveformLoader.this.lambda$run$0(sArr, i9);
                                                }
                                            });
                                            sArr = new short[sArr.length];
                                            i5 = i8;
                                        }
                                        i4 = i8;
                                        if (i8 >= this.data.length) {
                                            i6 = 0;
                                            s2 = 0;
                                            break;
                                        }
                                        s = 0;
                                        i7 = 0;
                                    } else {
                                        s = s2;
                                    }
                                    s2 = s < s3 ? s3 : s;
                                    i7++;
                                    if (outputBuffer.remaining() < 8) {
                                        break;
                                    }
                                    outputBuffer.position(outputBuffer.position() + 8);
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
                        if (!this.stop) {
                            if (z || i2 >= this.count) {
                                break;
                            }
                            i4 = i2;
                        } else {
                            break;
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

    /* loaded from: classes4.dex */
    public interface TimelineDelegate {

        /* loaded from: classes4.dex */
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

    /* loaded from: classes4.dex */
    public class VideoThumbsLoader {
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

        /* loaded from: classes4.dex */
        public class BitmapFrame {
            private final AnimatedFloat alpha;
            public Bitmap bitmap;

            public BitmapFrame(Bitmap bitmap) {
                VideoThumbsLoader.this = r10;
                this.alpha = new AnimatedFloat(0.0f, TimelineView.this, 0L, 240L, CubicBezierInterpolator.EASE_OUT_QUINT);
                this.bitmap = bitmap;
            }

            public float getAlpha() {
                return this.alpha.set(1.0f);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:72:0x007a, code lost:
            if (r0 != 270) goto L39;
         */
        /* JADX WARN: Removed duplicated region for block: B:77:0x008a  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:84:0x009e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:89:0x00e8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public VideoThumbsLoader(boolean z, String str, int i, int i2, Long l, long j, long j2, long j3) {
            int i3;
            int max;
            String extractMetadata;
            TimelineView.this = r15;
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
                    int ceil = (int) Math.ceil(((((float) Math.max(j4, j)) / ((float) j)) * i) / max);
                    this.count = ceil;
                    long j5 = ((float) j4) / ceil;
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
                int ceil2 = (int) Math.ceil(((((float) Math.max(j4, j)) / ((float) j)) * i) / max);
                this.count = ceil2;
                long j52 = ((float) j4) / ceil2;
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
            int ceil22 = (int) Math.ceil(((((float) Math.max(j4, j)) / ((float) j)) * i) / max);
            this.count = ceil22;
            long j522 = ((float) j4) / ceil22;
            this.frameIterator = j522;
            this.nextFrame = -j522;
            if (j2 != -1) {
            }
            load();
        }

        /* renamed from: receiveFrame */
        public void lambda$retrieveFrame$0(Bitmap bitmap) {
            if (!this.loading || this.destroyed) {
                return;
            }
            this.frames.add(new BitmapFrame(bitmap));
            this.loading = false;
            TimelineView.this.invalidate();
        }

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
                        Path path = this.clipPath;
                        int i = this.frameWidth;
                        int i2 = this.frameHeight;
                        path.addCircle(i / 2.0f, i2 / 2.0f, Math.min(i, i2) / 2.0f, Path.Direction.CW);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class WaveformPath extends Path {
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
            TimelineView.this = r1;
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
            } else if (this.lastWaveformCount != TimelineView.this.waveform.getCount() || Math.abs(this.lastAnimatedLoaded - f5) > 0.01f || this.lastScrollDuration != j || Math.abs(this.lastAudioHeight - f6) > 1.0f || Math.abs(this.lastMaxBar - f7) > 0.01f || Math.abs(this.lastAudioSelected - f4) > 0.1f || Math.abs(this.lastBottom - f8) > 1.0f || Math.abs(this.lastStart - f) > 1.0f || Math.abs(this.lastLeft - f2) > 1.0f || Math.abs(this.lastRight - f3) > 1.0f) {
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
        float min = (float) Math.min(getBaseDuration(), 120000L);
        float clamp = this.px + this.ph + (this.sw * (((float) ((Utilities.clamp(this.progress, getBaseDuration(), 0L) + (!this.hasVideo ? this.audioOffset : 0L)) - this.scroll)) / min));
        boolean z = false;
        if (this.isCover || x < clamp - AndroidUtilities.dp(12.0f) || x > clamp + AndroidUtilities.dp(12.0f)) {
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
                float f3 = (float) this.videoDuration;
                float f4 = (float) this.scroll;
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
                float f9 = (float) this.roundOffset;
                float f10 = this.roundLeft;
                float f11 = (float) this.roundDuration;
                float f12 = (float) this.scroll;
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
                    f15 = f16 + ((((float) ((j + this.roundDuration) - j2)) / min) * f17);
                    f14 = ((((float) (j - j2)) / min) * f17) + f16;
                }
                if (x >= f14 && x <= f15) {
                    return 9;
                }
            } else if (this.hasAudio) {
                float f18 = this.px + this.ph;
                float f19 = (float) this.audioOffset;
                float f20 = this.audioLeft;
                float f21 = (float) this.audioDuration;
                float f22 = (float) this.scroll;
                float f23 = this.sw;
                float f24 = (((((f20 * f21) + f19) - f22) / min) * f23) + f18;
                float f25 = f18 + ((((f19 + (this.audioRight * f21)) - f22) / min) * f23);
                if (this.audioSelected || (!this.hasVideo && !this.hasRound)) {
                    if (x >= f24 - AndroidUtilities.dp(15.0f) && x <= AndroidUtilities.dp(5.0f) + f24) {
                        return 6;
                    }
                    if (x >= f25 - AndroidUtilities.dp(5.0f) && x <= AndroidUtilities.dp(15.0f) + f25) {
                        return 7;
                    }
                    if (x >= f24 && x <= f25) {
                        return !this.hasVideo ? 8 : 5;
                    }
                    float f26 = this.px + this.ph;
                    long j3 = this.audioOffset;
                    long j4 = this.scroll;
                    float f27 = this.sw;
                    f25 = ((((float) ((j3 + this.audioDuration) - j4)) / min) * f27) + f26;
                    f24 = ((((float) (j3 - j4)) / min) * f27) + f26;
                }
                if (x >= f24 && x <= f25) {
                    return 5;
                }
            }
            return (this.videoDuration <= 120000 || !z2) ? -1 : 1;
        }
        return 0;
    }

    private void drawProgress(Canvas canvas, float f, float f2, long j, float f3) {
        if (this.isCover) {
            return;
        }
        float clamp = this.px + this.ph + (this.sw * (((float) ((Utilities.clamp(j, getBaseDuration(), 0L) + (!this.hasVideo ? this.audioOffset : 0L)) - this.scroll)) / ((float) Math.min(getBaseDuration(), 120000L))));
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
        canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, NotificationCenter.closeSearchByActiveAction, 31);
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
        this.regionHandlePaint.setAlpha(NotificationCenter.closeSearchByActiveAction);
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
        if (this.hasRound) {
            return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.roundSelectedT.set(this.roundSelected));
        }
        return 0.0f;
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

    public /* synthetic */ void lambda$new$0(Float f) {
        this.audioVolume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onAudioVolumeChange(f.floatValue());
        }
    }

    public /* synthetic */ void lambda$new$1() {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onAudioRemove();
        }
    }

    public /* synthetic */ void lambda$new$2(Float f) {
        this.roundVolume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onRoundVolumeChange(f.floatValue());
        }
    }

    public /* synthetic */ void lambda$new$3() {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onRoundRemove();
        }
    }

    public /* synthetic */ void lambda$new$4(Float f) {
        this.videoVolume = f.floatValue();
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onVideoVolumeChange(f.floatValue());
        }
    }

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
                min = Math.min((i5 - i6) - i7, i6 + i7 + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioRight, 1.0f, this.audioSelectedT.get()) * ((float) this.audioDuration))) / ((float) min2)) * this.sw));
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
            } else if (i4 != 1 || !this.hasRound) {
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
            } else {
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
                min = Math.min((i8 - i9) - i10, i9 + i10 + (((((float) (this.roundOffset - this.scroll)) + (AndroidUtilities.lerp(this.roundRight, 1.0f, this.roundSelectedT.get()) * ((float) this.roundDuration))) / ((float) min3)) * this.sw));
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

    public /* synthetic */ void lambda$setProgressAt$6(long j) {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onProgressChange(j, false);
        }
    }

    private long minAudioSelect() {
        return Math.max(1000.0f, ((float) Math.min(this.hasVideo ? this.videoDuration : this.hasRound ? this.roundDuration : this.audioDuration, 59000L)) * 0.15f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:113:0x00e4, code lost:
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x012c, code lost:
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x012e, code lost:
        r1.onAudioLeftChange(r22.audioLeft);
        r22.delegate.onAudioRightChange(r22.audioRight);
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x004a, code lost:
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Removed duplicated region for block: B:162:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:165:? A[RETURN, SYNTHETIC] */
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
        long j8;
        long clamp;
        long j9;
        float f5;
        long j10;
        float f6;
        TimelineDelegate timelineDelegate2;
        long j11;
        TimelineDelegate timelineDelegate3;
        long j12;
        long j13;
        float f7;
        long j14;
        float f8;
        boolean z = this.hasVideo;
        if (!z && !this.hasRound) {
            long j15 = this.audioOffset;
            long clamp2 = Utilities.clamp(j15 + f, 0L, -(this.audioDuration - Math.min(getBaseDuration(), 120000L)));
            this.audioOffset = clamp2;
            float f9 = (float) (clamp2 - j15);
            this.audioLeft = Utilities.clamp(this.audioLeft - (f9 / ((float) this.audioDuration)), 1.0f, 0.0f);
            this.audioRight = Utilities.clamp(this.audioRight - (f9 / ((float) this.audioDuration)), 1.0f, 0.0f);
            timelineDelegate = this.delegate;
        } else if (this.audioSelected) {
            if (z) {
                f2 = this.videoLeft;
                j = this.videoDuration;
            } else {
                f2 = this.roundLeft;
                j = this.roundDuration;
            }
            float f10 = f2 * ((float) j);
            if (z) {
                f3 = this.videoRight;
                j2 = this.videoDuration;
            } else {
                f3 = this.roundRight;
                j2 = this.roundDuration;
            }
            float f11 = f3 * ((float) j2);
            if (z) {
                f4 = this.videoRight - this.videoLeft;
                j3 = this.videoDuration;
            } else {
                f4 = this.roundRight - this.roundLeft;
                j3 = this.roundDuration;
            }
            float f12 = f4 * ((float) j3);
            float f13 = this.audioRight;
            float f14 = (float) this.audioDuration;
            float f15 = this.audioLeft;
            long j16 = f10 - (f15 * f14);
            float min = Math.min(f13 - f15, f12 / f14);
            long j17 = this.audioOffset;
            long j18 = f;
            long j19 = j17 + j18;
            if (j19 > f11 - (f13 * f14)) {
                float clamp3 = Utilities.clamp(((f11 - ((float) j17)) - ((float) j18)) / ((float) this.audioDuration), 1.0f, min);
                this.audioRight = clamp3;
                float clamp4 = Utilities.clamp(clamp3 - min, 1.0f, 0.0f);
                this.audioLeft = clamp4;
                float f16 = this.audioRight;
                float f17 = (float) this.audioDuration;
                long j20 = f11 - (f16 * f17);
                long j21 = f10 - (clamp4 * f17);
                if (j20 < j21) {
                    j7 = j21;
                    j6 = j20;
                } else {
                    j6 = j21;
                    j7 = j20;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j18, j7, j6);
                timelineDelegate = this.delegate;
            } else if (j19 < j16) {
                float clamp5 = Utilities.clamp(((f10 - ((float) j17)) - ((float) j18)) / ((float) this.audioDuration), 1.0f - min, 0.0f);
                this.audioLeft = clamp5;
                float clamp6 = Utilities.clamp(clamp5 + min, 1.0f, 0.0f);
                this.audioRight = clamp6;
                float f18 = (float) this.audioDuration;
                long j22 = f11 - (clamp6 * f18);
                long j23 = f10 - (this.audioLeft * f18);
                if (j22 < j23) {
                    j5 = j23;
                    j4 = j22;
                } else {
                    j4 = j23;
                    j5 = j22;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j18, j5, j4);
                timelineDelegate = this.delegate;
            } else {
                this.audioOffset = j19;
            }
        } else {
            long j24 = this.audioOffset + f;
            float f19 = (float) this.audioDuration;
            this.audioOffset = Utilities.clamp(j24, ((float) getBaseDuration()) - (this.audioRight * f19), (-this.audioLeft) * f19);
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
        }
        boolean z2 = this.dragged;
        if (!z2 && (timelineDelegate3 = this.delegate) != null) {
            timelineDelegate3.onProgressDragChange(true);
            if (this.hasVideo) {
                j13 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f20 = this.videoRight;
                f7 = (float) this.videoDuration;
                j14 = f20 * f7;
                f8 = this.videoLeft;
            } else if (this.hasRound) {
                j13 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f21 = this.roundRight;
                f7 = (float) this.roundDuration;
                j14 = f21 * f7;
                f8 = this.roundLeft;
            } else {
                float f22 = this.audioLeft;
                j11 = Utilities.clamp(f22 * ((float) j12), this.audioDuration, 0L);
                if (this.hasVideo && Math.abs(this.progress - j11) > 400) {
                    this.loopProgressFrom = this.progress;
                    this.loopProgress.set(1.0f, true);
                }
                timelineDelegate2 = this.delegate;
                this.progress = j11;
            }
            j11 = Utilities.clamp(j13, j14, f8 * f7);
            if (this.hasVideo) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            timelineDelegate2 = this.delegate;
            this.progress = j11;
        } else if (!z2 && !this.scrolling) {
            return;
        } else {
            if (this.hasVideo) {
                j9 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f23 = this.videoRight;
                f5 = (float) this.videoDuration;
                j10 = f23 * f5;
                f6 = this.videoLeft;
            } else if (this.hasRound) {
                j9 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f24 = this.roundRight;
                f5 = (float) this.videoDuration;
                j10 = f24 * f5;
                f6 = this.roundLeft;
            } else {
                float f25 = this.audioLeft;
                clamp = Utilities.clamp(f25 * ((float) j8), this.audioDuration, 0L);
                this.progress = clamp;
                timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    return;
                }
                j11 = this.progress;
            }
            clamp = Utilities.clamp(j9, j10, f6 * f5);
            this.progress = clamp;
            timelineDelegate2 = this.delegate;
            if (timelineDelegate2 != null) {
            }
        }
        timelineDelegate2.onProgressChange(j11, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:77:0x00cf, code lost:
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0121, code lost:
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0123, code lost:
        r1.onRoundLeftChange(r17.roundLeft);
        r17.delegate.onRoundRightChange(r17.roundRight);
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
        long j5;
        long clamp;
        TimelineDelegate timelineDelegate2;
        long j6;
        long clamp2;
        if (!this.hasVideo) {
            long j7 = this.roundOffset;
            long clamp3 = Utilities.clamp(j7 + f, 0L, -(this.roundDuration - Math.min(getBaseDuration(), 120000L)));
            this.roundOffset = clamp3;
            float f2 = (float) (clamp3 - j7);
            this.roundLeft = Utilities.clamp(this.roundLeft - (f2 / ((float) this.roundDuration)), 1.0f, 0.0f);
            this.roundRight = Utilities.clamp(this.roundRight - (f2 / ((float) this.roundDuration)), 1.0f, 0.0f);
            TimelineDelegate timelineDelegate3 = this.delegate;
            if (timelineDelegate3 != null) {
                timelineDelegate3.onAudioLeftChange(this.roundLeft);
                this.delegate.onAudioRightChange(this.roundRight);
            }
        } else if (this.roundSelected) {
            float f3 = this.videoRight;
            float f4 = (float) this.videoDuration;
            float f5 = this.roundRight;
            float f6 = (float) this.roundDuration;
            long j8 = (f3 * f4) - (f5 * f6);
            float f7 = this.videoLeft;
            float f8 = this.roundLeft;
            long j9 = (f7 * f4) - (f8 * f6);
            float min = Math.min(f5 - f8, ((f3 - f7) * f4) / f6);
            long j10 = this.roundOffset;
            long j11 = f;
            long j12 = j10 + j11;
            if (j12 > j8) {
                float clamp4 = Utilities.clamp((((this.videoRight * ((float) this.videoDuration)) - ((float) j10)) - ((float) j11)) / ((float) this.roundDuration), 1.0f, min);
                this.roundRight = clamp4;
                float clamp5 = Utilities.clamp(clamp4 - min, 1.0f, 0.0f);
                this.roundLeft = clamp5;
                float f9 = this.videoRight;
                float f10 = (float) this.videoDuration;
                float f11 = this.roundRight;
                float f12 = (float) this.roundDuration;
                long j13 = (f9 * f10) - (f11 * f12);
                long j14 = (this.videoLeft * f10) - (clamp5 * f12);
                if (j13 < j14) {
                    j4 = j14;
                    j3 = j13;
                } else {
                    j3 = j14;
                    j4 = j13;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j11, j4, j3);
                timelineDelegate = this.delegate;
            } else if (j12 < j9) {
                float clamp6 = Utilities.clamp((((this.videoLeft * ((float) this.videoDuration)) - ((float) j10)) - ((float) j11)) / ((float) this.roundDuration), 1.0f - min, 0.0f);
                this.roundLeft = clamp6;
                float clamp7 = Utilities.clamp(clamp6 + min, 1.0f, 0.0f);
                this.roundRight = clamp7;
                float f13 = this.videoRight;
                float f14 = (float) this.videoDuration;
                float f15 = (float) this.roundDuration;
                long j15 = (f13 * f14) - (clamp7 * f15);
                long j16 = (this.videoLeft * f14) - (this.roundLeft * f15);
                if (j15 < j16) {
                    j2 = j15;
                    j = j16;
                } else {
                    j = j15;
                    j2 = j16;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j11, j, j2);
                timelineDelegate = this.delegate;
            } else {
                this.roundOffset = j12;
            }
        } else {
            long j17 = this.roundOffset + f;
            float f16 = (float) this.roundDuration;
            this.roundOffset = Utilities.clamp(j17, ((float) getBaseDuration()) - (this.roundRight * f16), (-this.roundLeft) * f16);
        }
        invalidate();
        TimelineDelegate timelineDelegate4 = this.delegate;
        if (timelineDelegate4 != null) {
            timelineDelegate4.onRoundOffsetChange(this.roundOffset + (this.roundLeft * ((float) this.roundDuration)));
        }
        boolean z = this.dragged;
        if (z || (timelineDelegate2 = this.delegate) == null) {
            if (z || this.scrolling) {
                if (this.hasVideo) {
                    long j18 = this.roundOffset + (this.roundLeft * ((float) this.roundDuration));
                    float f17 = this.videoRight;
                    float f18 = (float) this.videoDuration;
                    clamp = Utilities.clamp(j18, f17 * f18, this.videoLeft * f18);
                } else {
                    float f19 = this.roundLeft;
                    clamp = Utilities.clamp(f19 * ((float) j5), this.roundDuration, 0L);
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
            long j19 = this.roundOffset + (this.roundLeft * ((float) this.roundDuration));
            float f20 = this.videoRight;
            float f21 = (float) this.videoDuration;
            clamp2 = Utilities.clamp(j19, f20 * f21, this.videoLeft * f21);
        } else {
            float f22 = this.roundLeft;
            clamp2 = Utilities.clamp(f22 * ((float) j6), this.roundDuration, 0L);
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
        if (this.hasVideo || this.hasAudio) {
            float min = (((f - this.px) - this.ph) / this.sw) * ((float) Math.min(getBaseDuration(), 120000L));
            boolean z2 = this.hasVideo;
            final long clamp = Utilities.clamp(min + ((float) (!z2 ? -this.audioOffset : 0L)) + ((float) this.scroll), (float) (z2 ? this.videoDuration : this.audioDuration), 0.0f);
            boolean z3 = this.hasVideo;
            if (z3) {
                float f2 = ((float) clamp) / ((float) this.videoDuration);
                if (f2 < this.videoLeft || f2 > this.videoRight) {
                    return false;
                }
            }
            if (this.hasAudio && !z3) {
                float f3 = ((float) clamp) / ((float) this.audioDuration);
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
            if (z) {
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
            return true;
        }
        return false;
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
            this.scroll = Math.max(0.0f, (((currX - this.px) - this.ph) / this.sw) * ((float) min));
        } else if (!this.audioSelected) {
            this.scroller.abortAnimation();
            return;
        } else {
            int i = this.px;
            int i2 = this.ph;
            float f = this.sw;
            float f2 = (float) min;
            moveAudioOffset(((((currX - i) - i2) / f) * f2) - ((((this.wasScrollX - i) - i2) / f) * f2));
        }
        invalidate();
        this.wasScrollX = currX;
    }

    /* JADX WARN: Removed duplicated region for block: B:617:0x0ac8  */
    /* JADX WARN: Removed duplicated region for block: B:618:0x0ad3  */
    /* JADX WARN: Removed duplicated region for block: B:621:0x0adf  */
    /* JADX WARN: Removed duplicated region for block: B:624:0x0b07  */
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
        int i2;
        boolean z;
        Bitmap bitmap;
        int i3;
        int i4;
        float f27;
        Bitmap bitmap2;
        int i5;
        Paint paint2 = this.backgroundBlur.getPaint(1.0f);
        long min = Math.min(getBaseDuration(), 120000L);
        float f28 = this.hasVideo ? 1.0f : 0.0f;
        float f29 = this.videoSelectedT.set((this.audioSelected || this.roundSelected) ? false : true);
        if (this.hasVideo) {
            canvas.save();
            float videoHeight = getVideoHeight();
            f = f28;
            long j5 = this.videoDuration;
            float f30 = j5 <= 0 ? 0.0f : (this.px + this.ph) - ((((float) this.scroll) / ((float) min)) * this.sw);
            float f31 = this.ph;
            float f32 = f30 - f31;
            float f33 = (j5 <= 0 ? 0.0f : ((((float) (j5 - this.scroll)) / ((float) min)) * this.sw) + this.px + i4) + f31;
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
                int i6 = (int) this.videoBounds.top;
                boolean z2 = this.thumbs.frames.size() >= min2;
                boolean z3 = z2 && !this.isMainVideoRound;
                if (z3) {
                    int i7 = max2;
                    while (true) {
                        if (i7 >= Math.min(this.thumbs.frames.size(), min2)) {
                            break;
                        } else if (((VideoThumbsLoader.BitmapFrame) this.thumbs.frames.get(i7)).bitmap == null) {
                            z3 = false;
                            break;
                        } else {
                            i7++;
                        }
                    }
                }
                if (!z3) {
                    if (paint2 == null) {
                        i5 = 1073741824;
                    } else {
                        canvas.drawRect(this.videoBounds, paint2);
                        i5 = AndroidUtilities.DARK_STATUS_BAR_OVERLAY;
                    }
                    canvas.drawColor(i5);
                }
                while (max2 < Math.min(this.thumbs.frames.size(), min2)) {
                    VideoThumbsLoader.BitmapFrame bitmapFrame = (VideoThumbsLoader.BitmapFrame) this.thumbs.frames.get(max2);
                    if (bitmapFrame.bitmap != null) {
                        this.videoFramePaint.setAlpha((int) (bitmapFrame.getAlpha() * 255.0f));
                        canvas.drawBitmap(bitmapFrame.bitmap, f32, i6 - ((int) ((bitmap2.getHeight() - videoHeight) / 2.0f)), this.videoFramePaint);
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
                int i8 = this.px;
                int i9 = this.ph;
                float f35 = i8 + i9;
                float f36 = this.videoLeft;
                float f37 = (float) this.videoDuration;
                float f38 = (float) this.scroll;
                float f39 = (float) min;
                float f40 = this.sw;
                float f41 = (((((f36 * f37) - f38) / f39) * f40) + f35) - (f36 <= 0.0f ? i9 : 0);
                float f42 = this.h - this.py;
                float f43 = f42 - videoHeight;
                f27 = videoHeight;
                float f44 = this.videoRight;
                float f45 = f35 + ((((f37 * f44) - f38) / f39) * f40);
                if (f44 < 1.0f) {
                    i9 = 0;
                }
                rectF2.set(f41, f43, f45 + i9, f42);
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
                float f48 = (float) min;
                f26 = this.px + this.ph + (((((float) (this.roundOffset - this.scroll)) + (AndroidUtilities.lerp(this.roundLeft, 0.0f, f47) * ((float) this.roundDuration))) / f48) * this.sw);
                f25 = this.px + this.ph + (((((float) (this.roundOffset - this.scroll)) + (AndroidUtilities.lerp(this.roundRight, 1.0f, f47) * ((float) this.roundDuration))) / f48) * this.sw);
                f3 = f47;
                f4 = f46;
            } else {
                float f49 = this.px + this.ph;
                long j6 = this.roundOffset - this.scroll;
                float f50 = (float) min;
                float f51 = this.sw;
                f3 = f47;
                f4 = f46;
                f25 = ((((float) (j6 + this.roundDuration)) / f50) * f51) + f49;
                f26 = ((((float) j6) / f50) * f51) + f49;
            }
            float f52 = ((this.h - this.py) - f2) - (dp * f);
            RectF rectF3 = this.roundBounds;
            float f53 = this.ph;
            rectF3.set(f26 - f53, f52 - roundHeight, f25 + f53, f52);
            this.roundClipPath.rewind();
            this.roundClipPath.addRoundRect(this.roundBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.save();
            canvas.clipPath(this.roundClipPath);
            VideoThumbsLoader videoThumbsLoader2 = this.roundThumbs;
            if (videoThumbsLoader2 != null) {
                long j7 = this.roundDuration;
                float f54 = j7 <= 0 ? 0.0f : this.px + this.ph + ((((float) (this.roundOffset - this.scroll)) / ((float) min)) * this.sw);
                float f55 = this.ph;
                float f56 = f54 - f55;
                float f57 = (j7 <= 0 ? 0.0f : ((((float) ((this.roundOffset + j7) - this.scroll)) / ((float) min)) * this.sw) + this.px + i2) + f55;
                float frameWidth2 = videoThumbsLoader2.getFrameWidth();
                int max3 = (int) Math.max(0.0d, Math.floor((f56 - (this.hasVideo ? (this.px + this.ph) + ((((float) (this.roundOffset - this.scroll)) / ((float) min)) * this.sw) : this.px)) / frameWidth2));
                int min3 = (int) Math.min(this.roundThumbs.count, Math.ceil((f57 - f56) / frameWidth2) + 1.0d);
                int i10 = (int) this.roundBounds.top;
                boolean z4 = this.roundThumbs.frames.size() >= min3;
                if (z4) {
                    for (int i11 = max3; i11 < Math.min(this.roundThumbs.frames.size(), min3); i11++) {
                        if (((VideoThumbsLoader.BitmapFrame) this.roundThumbs.frames.get(i11)).bitmap == null) {
                            z = false;
                            break;
                        }
                    }
                }
                z = z4;
                if (!z) {
                    if (paint2 == null) {
                        i3 = 1073741824;
                    } else {
                        canvas.drawRect(this.roundBounds, paint2);
                        i3 = AndroidUtilities.DARK_STATUS_BAR_OVERLAY;
                    }
                    canvas.drawColor(i3);
                }
                while (max3 < Math.min(this.roundThumbs.frames.size(), min3)) {
                    VideoThumbsLoader.BitmapFrame bitmapFrame2 = (VideoThumbsLoader.BitmapFrame) this.roundThumbs.frames.get(max3);
                    if (bitmapFrame2.bitmap != null) {
                        this.videoFramePaint.setAlpha((int) (bitmapFrame2.getAlpha() * 255.0f));
                        canvas.drawBitmap(bitmapFrame2.bitmap, f56, i10 - ((int) ((bitmap.getHeight() - roundHeight) / 2.0f)), this.videoFramePaint);
                    }
                    f56 += frameWidth2;
                    max3++;
                }
                if (!z4) {
                    this.roundThumbs.load();
                }
            }
            this.selectedVideoClipPath.rewind();
            RectF rectF4 = AndroidUtilities.rectTmp;
            int i12 = this.px;
            int i13 = this.ph;
            float f58 = i12 + i13;
            float f59 = this.roundLeft;
            float f60 = (float) this.roundDuration;
            float f61 = (float) this.scroll;
            float f62 = (float) this.roundOffset;
            float f63 = (float) min;
            paint = paint2;
            float f64 = this.sw;
            float f65 = ((((((f59 * f60) - f61) + f62) / f63) * f64) + f58) - (f59 <= 0.0f ? i13 : 0);
            float f66 = i13 * (1.0f - f3);
            float f67 = f65 - f66;
            RectF rectF5 = this.roundBounds;
            f5 = dp;
            float f68 = rectF5.top;
            f6 = f2;
            float f69 = this.roundRight;
            rectF4.set(f67, f68, f58 + (((((f60 * f69) - f61) + f62) / f63) * f64) + (f69 >= 1.0f ? i13 : 0) + f66, rectF5.bottom);
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
        float f70 = this.audioT.set(this.hasAudio);
        float f71 = this.audioSelectedT.set(this.hasAudio && this.audioSelected);
        float audioHeight = getAudioHeight() * f70;
        if (f70 > 0.0f) {
            Paint paint3 = this.audioBlur.getPaint(f70);
            canvas.save();
            if (this.hasVideo || this.hasRound) {
                float f72 = (float) min;
                lerp = this.px + this.ph + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioLeft, 0.0f, f71) * ((float) this.audioDuration))) / f72) * this.sw);
                lerp2 = this.px + this.ph + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioRight, 1.0f, f71) * ((float) this.audioDuration))) / f72) * this.sw);
            } else {
                float f73 = this.px + this.ph;
                long j8 = this.audioOffset - this.scroll;
                float f74 = (float) min;
                float f75 = this.sw;
                lerp2 = f73 + ((((float) (j8 + this.audioDuration)) / f74) * f75);
                lerp = ((((float) j8) / f74) * f75) + f73;
            }
            float f76 = ((((this.h - this.py) - f6) - (f5 * f)) - roundHeight) - (f5 * f4);
            RectF rectF6 = this.audioBounds;
            float f77 = this.ph;
            float f78 = f76 - audioHeight;
            rectF6.set(lerp - f77, f78, lerp2 + f77, f76);
            this.audioClipPath.rewind();
            this.audioClipPath.addRoundRect(this.audioBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.clipPath(this.audioClipPath);
            if (paint3 == null) {
                i = 1073741824;
            } else {
                canvas.drawRect(this.audioBounds, paint3);
                i = AndroidUtilities.DARK_STATUS_BAR_OVERLAY;
            }
            canvas.drawColor(Theme.multAlpha(i, f70));
            if (this.waveform == null || paint3 == null) {
                d2 = 0.0d;
            } else {
                Paint paint4 = this.audioWaveformBlur.getPaint(0.4f * f70);
                if (paint4 == null) {
                    paint4 = this.waveformPaint;
                    paint4.setAlpha((int) (64.0f * f70));
                }
                float f79 = this.waveformMax.set(this.waveform.getMaxBar(), !this.waveformIsLoaded);
                this.waveformIsLoaded = this.waveform.getLoadedCount() > 0;
                d2 = 0.0d;
                this.waveformPath.check(this.px + this.ph + ((((float) (this.audioOffset - this.scroll)) / ((float) min)) * this.sw), lerp, lerp2, f71, this.waveformLoaded.set(this.waveform.getLoadedCount()), min, audioHeight, f79, f76);
                canvas.drawPath(this.waveformPath, paint4);
            }
            if (f71 < 1.0f) {
                int i14 = this.px;
                float f80 = this.ph + i14;
                float f81 = (float) (this.audioOffset - this.scroll);
                float f82 = this.audioLeft;
                float f83 = (float) this.audioDuration;
                float f84 = (float) min;
                float f85 = this.sw;
                float f86 = ((((f82 * f83) + f81) / f84) * f85) + f80;
                float f87 = f80 + (((f81 + (this.audioRight * f83)) / f84) * f85);
                float max4 = (Math.max(i14, f86) + Math.min(this.w - this.px, f87)) / 2.0f;
                float dp2 = f78 + AndroidUtilities.dp(14.0f);
                float max5 = Math.max(0.0f, (Math.min(this.w - this.px, f87) - Math.max(this.px, f86)) - AndroidUtilities.dp(24.0f));
                float dpf2 = AndroidUtilities.dpf2(13.0f) + ((this.audioAuthor == null && this.audioTitle == null) ? 0.0f : AndroidUtilities.dpf2(3.11f) + this.audioAuthorWidth + AndroidUtilities.dpf2(9.66f) + this.audioTitleWidth);
                boolean z5 = dpf2 < max5;
                float min4 = max4 - (Math.min(dpf2, max5) / 2.0f);
                this.audioIcon.setBounds((int) min4, (int) (dp2 - (AndroidUtilities.dp(13.0f) / 2.0f)), (int) (AndroidUtilities.dp(13.0f) + min4), (int) ((AndroidUtilities.dp(13.0f) / 2.0f) + dp2));
                float f88 = 1.0f - f71;
                float f89 = f88 * 255.0f;
                this.audioIcon.setAlpha((int) f89);
                this.audioIcon.draw(canvas);
                float dpf22 = min4 + AndroidUtilities.dpf2(16.11f);
                d = 0.0d;
                f9 = f3;
                f7 = f;
                f8 = f4;
                f10 = f5;
                j = min;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, NotificationCenter.closeSearchByActiveAction, 31);
                float min5 = Math.min(f87, this.w) - AndroidUtilities.dp(12.0f);
                f11 = 0.0f;
                canvas.clipRect(dpf22, 0.0f, min5, this.h);
                if (this.audioAuthor != null) {
                    canvas.save();
                    canvas.translate(dpf22 - this.audioAuthorLeft, dp2 - (this.audioAuthor.getHeight() / 2.0f));
                    this.audioAuthorPaint.setAlpha((int) (f89 * f70));
                    this.audioAuthor.draw(canvas);
                    canvas.restore();
                    f24 = dpf22 + this.audioAuthorWidth;
                } else {
                    f24 = dpf22;
                }
                if (this.audioAuthor != null && this.audioTitle != null) {
                    float dpf23 = f24 + AndroidUtilities.dpf2(3.66f);
                    int alpha = this.audioDotPaint.getAlpha();
                    this.audioDotPaint.setAlpha((int) (alpha * f88));
                    canvas.drawCircle(AndroidUtilities.dp(1.0f) + dpf23, dp2, AndroidUtilities.dp(1.0f), this.audioDotPaint);
                    this.audioDotPaint.setAlpha(alpha);
                    f24 = dpf23 + AndroidUtilities.dpf2(2.0f) + AndroidUtilities.dpf2(4.0f);
                }
                if (this.audioTitle != null) {
                    canvas.save();
                    canvas.translate(f24 - this.audioTitleLeft, dp2 - (this.audioTitle.getHeight() / 2.0f));
                    this.audioTitlePaint.setAlpha((int) (f89 * f70));
                    this.audioTitle.draw(canvas);
                    canvas.restore();
                }
                if (!z5) {
                    this.ellipsizeMatrix.reset();
                    this.ellipsizeMatrix.postScale(AndroidUtilities.dpf2(8.0f) / 16.0f, 1.0f);
                    this.ellipsizeMatrix.postTranslate(min5 - AndroidUtilities.dp(8.0f), 0.0f);
                    this.ellipsizeGradient.setLocalMatrix(this.ellipsizeMatrix);
                    canvas.drawRect(min5 - AndroidUtilities.dp(8.0f), f78, min5, f76, this.ellipsizePaint);
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
        float f90 = ((z6 || this.hasRound) ? f71 : 1.0f) * f70;
        if (z6 || this.hasAudio) {
            f12 = f9;
            f13 = f8;
        } else {
            f13 = f8;
            f12 = 1.0f;
        }
        float f91 = f13 * f12;
        float f92 = this.h - this.py;
        float f93 = f92 - f6;
        float f94 = f7;
        float f95 = f10 * f94;
        float f96 = f93 - f95;
        float f97 = f96 - roundHeight;
        float f98 = f97 - (f10 * f13);
        float f99 = ((f98 - audioHeight) * f90) + f11 + (f97 * f91) + (f93 * f29);
        float f100 = (f98 * f90) + f11 + (f96 * f91) + (f92 * f29);
        float f101 = (float) this.audioOffset;
        float f102 = this.audioLeft;
        float f103 = (float) this.audioDuration;
        float f104 = (float) this.roundOffset;
        float f105 = this.roundLeft;
        float f106 = f70;
        float f107 = (float) this.roundDuration;
        float f108 = (((f102 * f103) + f101) * f90) + f11 + (((f105 * f107) + f104) * f91);
        float f109 = this.videoLeft;
        float f110 = f13;
        float f111 = (float) this.videoDuration;
        float f112 = ((f101 + (this.audioRight * f103)) * f90) + f11 + ((f104 + (this.roundRight * f107)) * f91) + (this.videoRight * f111 * f29);
        float f113 = this.px + this.ph;
        float f114 = (float) this.scroll;
        long j9 = j;
        float f115 = (float) j9;
        float f116 = this.sw;
        float f117 = ((((f108 + ((f109 * f111) * f29)) - f114) / f115) * f116) + f113;
        float f118 = f113 + (((f112 - f114) / f115) * f116);
        if (!this.hasAudio || z6) {
            f14 = f110;
            f15 = f106;
            f106 = Math.max(f94, f14);
        } else {
            f15 = f106;
            f14 = f110;
        }
        if (f15 > d || f14 > d || f94 > d) {
            if (this.hasVideo || this.hasRound) {
                f16 = f71;
                f17 = 1.0f;
            } else {
                f16 = f71;
                f17 = AndroidUtilities.lerp(0.6f, 1.0f, f16) * f15;
            }
            float f119 = f16;
            j2 = j9;
            f18 = 0.0f;
            drawRegion(canvas, paint, f99, f100, f117, f118, f17 * f106);
            if (this.hasVideo && ((this.hasAudio || this.hasRound) && (f119 > 0.0f || f9 > 0.0f))) {
                float f120 = this.h - this.py;
                float f121 = this.ph + this.px;
                float f122 = this.videoLeft;
                float f123 = (float) this.videoDuration;
                float f124 = (float) this.scroll;
                float f125 = this.sw;
                drawRegion(canvas, paint, f120 - f6, f120, f121 + ((((f122 * f123) - f124) / f115) * f125), f121 + ((((this.videoRight * f123) - f124) / f115) * f125), 0.8f);
            }
            float f126 = this.loopProgress.set(0.0f);
            float max6 = ((((this.h - this.py) - f6) - ((audioHeight + (f10 * Math.max(f14, f94))) * f15)) - ((roundHeight + f95) * f14)) - AndroidUtilities.dpf2(4.3f);
            float dpf24 = (this.h - this.py) + AndroidUtilities.dpf2(4.3f);
            if (f126 > 0.0f) {
                long j10 = this.loopProgressFrom;
                if (j10 == -1) {
                    if (this.hasVideo) {
                        f19 = (float) this.videoDuration;
                        f20 = this.videoRight;
                    } else if (this.hasRound) {
                        f19 = (float) this.roundDuration;
                        f20 = this.roundRight;
                    } else {
                        f19 = (float) this.audioDuration;
                        f20 = this.audioRight;
                    }
                    j10 = f19 * f20;
                }
                drawProgress(canvas, max6, dpf24, j10, f126 * f106);
            }
            drawProgress(canvas, max6, dpf24, this.progress, (1.0f - f126) * f106);
        } else {
            j2 = j9;
            f18 = 0.0f;
        }
        if (this.dragged) {
            long dp3 = (AndroidUtilities.dp(32.0f) / this.sw) * f115 * (1.0f / (1000.0f / AndroidUtilities.screenRefreshRate));
            int i15 = this.pressHandle;
            if (i15 == 4) {
                float f127 = this.videoLeft;
                long j11 = this.scroll;
                long j12 = this.videoDuration;
                float f128 = (float) j12;
                long j13 = (f127 >= ((float) j11) / f128 ? this.videoRight > ((float) (j11 + j2)) / f128 ? 1 : 0 : -1) * dp3;
                long clamp2 = Utilities.clamp(j11 + j13, j12 - j2, 0L);
                this.scroll = clamp2;
                this.progress += j13;
                float f129 = ((float) (clamp2 - j11)) / ((float) this.videoDuration);
                if (f129 > f18) {
                    f23 = 1.0f;
                    max = Math.min(1.0f - this.videoRight, f129);
                } else {
                    f23 = 1.0f;
                    max = Math.max(f18 - this.videoLeft, f129);
                }
                this.videoLeft = Utilities.clamp(this.videoLeft + max, f23, f18);
                this.videoRight = Utilities.clamp(this.videoRight + max, f23, f18);
                TimelineDelegate timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    timelineDelegate2.onVideoLeftChange(this.videoLeft);
                    this.delegate.onVideoRightChange(this.videoRight);
                }
            } else if (i15 != 8) {
                return;
            } else {
                float f130 = this.audioLeft;
                long j14 = this.audioOffset;
                long j15 = -j14;
                float f131 = (float) (j15 + 100);
                long j16 = this.audioDuration;
                float f132 = (float) j16;
                int i16 = f130 >= f131 / f132 ? this.audioRight >= ((float) ((j15 + j2) - 100)) / f132 ? 1 : 0 : -1;
                if (i16 == 0) {
                    return;
                }
                if (this.audioSelected && this.hasVideo) {
                    j3 = j14 - (i16 * dp3);
                    float f133 = this.videoRight;
                    f21 = (float) this.videoDuration;
                    j4 = (f133 * f21) - (f130 * f132);
                    f22 = this.videoLeft;
                } else if (this.roundSelected && this.hasRound) {
                    j3 = j14 - (i16 * dp3);
                    float f134 = this.roundRight;
                    f21 = (float) this.roundDuration;
                    j4 = (f134 * f21) - (f130 * f132);
                    f22 = this.roundLeft;
                } else {
                    clamp = Utilities.clamp(j14 - (i16 * dp3), 0L, -(j16 - Math.min(getBaseDuration(), 120000L)));
                    this.audioOffset = clamp;
                    float f135 = ((float) (-(this.audioOffset - j14))) / ((float) this.audioDuration);
                    float min6 = f135 <= f18 ? Math.min(1.0f - this.audioRight, f135) : Math.max(f18 - this.audioLeft, f135);
                    if (!this.hasVideo) {
                        float f136 = (float) this.audioDuration;
                        this.progress = Utilities.clamp(((float) this.progress) + (min6 * f136), f136, f18);
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
                clamp = Utilities.clamp(j3, j4, (f22 * f21) - (this.audioRight * f132));
                this.audioOffset = clamp;
                float f1352 = ((float) (-(this.audioOffset - j14))) / ((float) this.audioDuration);
                if (f1352 <= f18) {
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

    /* JADX WARN: Code restructure failed: missing block: B:587:0x03ac, code lost:
        if (r1 != null) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0570, code lost:
        if (r1 != null) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x0572, code lost:
        r1.onProgressDragChange(true);
        r4 = false;
        r26.delegate.onProgressChange(r26.progress, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x06ea, code lost:
        if (r1 != null) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x070d, code lost:
        if (r1 != null) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x071d, code lost:
        if (r1 != null) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x071f, code lost:
        r1.onRoundSelectChange(r3);
     */
    /* JADX WARN: Removed duplicated region for block: B:813:0x08cc  */
    /* JADX WARN: Removed duplicated region for block: B:816:0x08d6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:823:0x08f1  */
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
        if (z5 || this.hasAudio || this.hasRound) {
            int i10 = this.h;
            int i11 = this.py;
            float videoHeight = ((((i10 - i11) - i11) - (z5 ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasAudio ? getAudioHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f);
            if (motionEvent.getAction() != 0 || motionEvent.getY() >= videoHeight) {
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
                            this.scroll = Utilities.clamp(((float) this.scroll) - ((x / this.sw) * ((float) min3)), (float) (this.videoDuration - min3), 0.0f);
                            invalidate();
                        } else if (i14 == 2 || i14 == 3 || i14 == 4) {
                            float f9 = (float) this.videoDuration;
                            float f10 = (x / this.sw) * (((float) min3) / f9);
                            if (i14 == 2) {
                                float clamp = Utilities.clamp(this.videoLeft + f10, this.videoRight - (1000.0f / f9), 0.0f);
                                this.videoLeft = clamp;
                                TimelineDelegate timelineDelegate5 = this.delegate;
                                if (timelineDelegate5 != null) {
                                    timelineDelegate5.onVideoLeftChange(clamp);
                                }
                                float f11 = this.videoRight;
                                float f12 = this.videoLeft;
                                float f13 = 59000.0f / ((float) this.videoDuration);
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
                                float f15 = 59000.0f / ((float) this.videoDuration);
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
                            float f17 = (float) this.videoDuration;
                            float f18 = ((float) this.progress) / f17;
                            float f19 = this.videoLeft;
                            if (f18 < f19 || f18 > this.videoRight) {
                                long j7 = f19 * f17;
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
                                float f20 = (x / this.sw) * (((float) min3) / ((float) this.audioDuration));
                                if (i14 == 6) {
                                    float minAudioSelect = this.audioRight - (((float) minAudioSelect()) / ((float) this.audioDuration));
                                    float max3 = (float) Math.max(0L, this.scroll - this.audioOffset);
                                    float f21 = (float) this.audioDuration;
                                    float f22 = max3 / f21;
                                    boolean z6 = this.hasVideo;
                                    if (z6 || this.hasRound) {
                                        if (z6) {
                                            f5 = this.videoLeft;
                                            j5 = this.videoDuration;
                                        } else if (this.hasRound) {
                                            f5 = this.roundLeft;
                                            j5 = this.roundDuration;
                                        }
                                        f22 = Math.max(f22, (((f5 * ((float) j5)) + ((float) this.scroll)) - ((float) this.audioOffset)) / f21);
                                    } else {
                                        f22 = Math.max(f22, this.audioRight - (59000.0f / f21));
                                        if (!this.hadDragChange && f20 < 0.0f && this.audioLeft <= this.audioRight - (59000.0f / ((float) this.audioDuration))) {
                                            this.pressHandle = 8;
                                        }
                                    }
                                    float f23 = this.audioLeft;
                                    float clamp3 = Utilities.clamp(f23 + f20, minAudioSelect, f22);
                                    this.audioLeft = clamp3;
                                    if (Math.abs(f23 - clamp3) > 0.01f) {
                                        this.hadDragChange = true;
                                    }
                                    TimelineDelegate timelineDelegate11 = this.delegate;
                                    if (timelineDelegate11 != null) {
                                        timelineDelegate11.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                                    }
                                    TimelineDelegate timelineDelegate12 = this.delegate;
                                    if (timelineDelegate12 != null) {
                                        timelineDelegate12.onAudioLeftChange(this.audioLeft);
                                    }
                                } else if (i14 == 7) {
                                    float min6 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min3)) / ((float) this.audioDuration));
                                    float f24 = this.audioLeft;
                                    float f25 = (float) this.audioDuration;
                                    float minAudioSelect2 = f24 + (((float) minAudioSelect()) / f25);
                                    boolean z7 = this.hasVideo;
                                    if (z7 || this.hasRound) {
                                        if (z7) {
                                            f4 = this.videoRight;
                                            j4 = this.videoDuration;
                                        } else if (this.hasRound) {
                                            f4 = this.roundRight;
                                            j4 = this.roundDuration;
                                        }
                                        min6 = Math.min(min6, (((f4 * ((float) j4)) + ((float) this.scroll)) - ((float) this.audioOffset)) / f25);
                                    } else {
                                        min6 = Math.min(min6, this.audioLeft + (59000.0f / f25));
                                        if (!this.hadDragChange && f20 > 0.0f && this.audioRight >= this.audioLeft + (59000.0f / ((float) this.audioDuration))) {
                                            this.pressHandle = 8;
                                        }
                                    }
                                    float f26 = this.audioRight;
                                    float clamp4 = Utilities.clamp(f26 + f20, min6, minAudioSelect2);
                                    this.audioRight = clamp4;
                                    if (Math.abs(f26 - clamp4) > 0.01f) {
                                        this.hadDragChange = true;
                                    }
                                    TimelineDelegate timelineDelegate13 = this.delegate;
                                    if (timelineDelegate13 != null) {
                                        timelineDelegate13.onAudioRightChange(this.audioRight);
                                    }
                                }
                                if (this.pressHandle == 8) {
                                    float min7 = f20 > 0.0f ? Math.min(Math.max(0.0f, Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min3)) / ((float) this.audioDuration)) - this.audioRight), f20) : Math.max(Math.min(0.0f, (((float) Math.max(0L, this.scroll - this.audioOffset)) / ((float) this.audioDuration)) - this.audioLeft), f20);
                                    float f27 = this.audioLeft + min7;
                                    this.audioLeft = f27;
                                    this.audioRight += min7;
                                    TimelineDelegate timelineDelegate14 = this.delegate;
                                    if (timelineDelegate14 != null) {
                                        timelineDelegate14.onAudioLeftChange(f27);
                                        this.delegate.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                                        this.delegate.onAudioRightChange(this.audioRight);
                                    }
                                    TimelineDelegate timelineDelegate15 = this.delegate;
                                    if (timelineDelegate15 != null) {
                                        timelineDelegate15.onProgressDragChange(true);
                                    }
                                }
                                if (!this.hasVideo && !this.hasRound) {
                                    this.progress = this.audioLeft * ((float) this.audioDuration);
                                    timelineDelegate3 = this.delegate;
                                }
                                z4 = false;
                            } else if (i14 == 10 || i14 == 11 || i14 == 12) {
                                float f28 = (x / this.sw) * (((float) min3) / ((float) this.roundDuration));
                                if (i14 == 10) {
                                    float minAudioSelect3 = this.roundRight - (((float) minAudioSelect()) / ((float) this.roundDuration));
                                    j6 = min3;
                                    float f29 = (float) this.roundDuration;
                                    float max4 = ((float) Math.max(0L, this.scroll - this.roundOffset)) / f29;
                                    if (this.hasVideo) {
                                        max = Math.max(max4, (((this.videoLeft * ((float) this.videoDuration)) + ((float) this.scroll)) - ((float) this.roundOffset)) / f29);
                                    } else {
                                        max = Math.max(max4, this.roundRight - (59000.0f / f29));
                                        if (!this.hadDragChange && f28 < 0.0f && this.roundLeft <= this.roundRight - (59000.0f / ((float) this.roundDuration))) {
                                            this.pressHandle = 8;
                                        }
                                    }
                                    float f30 = this.roundLeft;
                                    float clamp5 = Utilities.clamp(f30 + f28, minAudioSelect3, max);
                                    this.roundLeft = clamp5;
                                    if (Math.abs(f30 - clamp5) > 0.01f) {
                                        this.hadDragChange = true;
                                    }
                                    TimelineDelegate timelineDelegate16 = this.delegate;
                                    if (timelineDelegate16 != null) {
                                        timelineDelegate16.onRoundOffsetChange(this.roundOffset + (this.roundLeft * ((float) this.roundDuration)));
                                    }
                                    TimelineDelegate timelineDelegate17 = this.delegate;
                                    if (timelineDelegate17 != null) {
                                        timelineDelegate17.onRoundLeftChange(this.roundLeft);
                                    }
                                } else {
                                    j6 = min3;
                                    if (i14 == 11) {
                                        float min8 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.roundOffset) + j6)) / ((float) this.roundDuration));
                                        float f31 = this.roundLeft;
                                        float f32 = (float) this.roundDuration;
                                        float minAudioSelect4 = f31 + (((float) minAudioSelect()) / f32);
                                        if (this.hasVideo) {
                                            min2 = Math.min(min8, (((this.videoRight * ((float) this.videoDuration)) + ((float) this.scroll)) - ((float) this.roundOffset)) / f32);
                                        } else {
                                            min2 = Math.min(min8, this.roundLeft + (59000.0f / f32));
                                            if (!this.hadDragChange && f28 > 0.0f && this.roundRight >= this.roundLeft + (59000.0f / ((float) this.roundDuration))) {
                                                this.pressHandle = 8;
                                            }
                                        }
                                        float f33 = this.roundRight;
                                        float clamp6 = Utilities.clamp(f33 + f28, min2, minAudioSelect4);
                                        this.roundRight = clamp6;
                                        if (Math.abs(f33 - clamp6) > 0.01f) {
                                            this.hadDragChange = true;
                                        }
                                        TimelineDelegate timelineDelegate18 = this.delegate;
                                        if (timelineDelegate18 != null) {
                                            timelineDelegate18.onRoundRightChange(this.roundRight);
                                        }
                                    }
                                }
                                if (this.pressHandle == 12) {
                                    float min9 = f28 > 0.0f ? Math.min(Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.roundOffset) + j6)) / ((float) this.roundDuration)) - this.roundRight, f28) : Math.max((((float) Math.max(0L, this.scroll - this.roundOffset)) / ((float) this.roundDuration)) - this.roundLeft, f28);
                                    float f34 = this.roundLeft + min9;
                                    this.roundLeft = f34;
                                    this.roundRight += min9;
                                    TimelineDelegate timelineDelegate19 = this.delegate;
                                    if (timelineDelegate19 != null) {
                                        timelineDelegate19.onRoundLeftChange(f34);
                                        this.delegate.onRoundOffsetChange(this.roundOffset + (this.roundLeft * ((float) this.roundDuration)));
                                        this.delegate.onRoundRightChange(this.roundRight);
                                    }
                                    TimelineDelegate timelineDelegate20 = this.delegate;
                                    if (timelineDelegate20 != null) {
                                        timelineDelegate20.onProgressDragChange(true);
                                    }
                                }
                                if (!this.hasVideo) {
                                    this.progress = this.roundLeft * ((float) this.roundDuration);
                                    timelineDelegate3 = this.delegate;
                                }
                                z4 = false;
                            } else if (i14 == 5) {
                                moveAudioOffset((x / this.sw) * ((float) min3));
                            } else if (i14 == 9) {
                                moveRoundOffset((x / this.sw) * ((float) min3));
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
                                        min = (float) Math.min(getBaseDuration(), 120000L);
                                        i = (int) (this.px + this.ph + ((((float) this.audioOffset) / min) * this.sw));
                                        if (this.hasVideo) {
                                            float f35 = this.videoRight;
                                            f = (float) this.videoDuration;
                                            f3 = f35 * f;
                                            j3 = this.audioDuration;
                                            j2 = f3 - ((float) 0);
                                            f2 = this.videoLeft;
                                            j = (f2 * f) - ((float) j3);
                                            i2 = xVelocity;
                                        } else if (this.hasRound) {
                                            float f36 = this.roundRight;
                                            f = (float) this.roundDuration;
                                            j3 = this.audioDuration;
                                            j2 = (f36 * f) - ((float) 0);
                                            f2 = this.roundLeft;
                                            j = (f2 * f) - ((float) j3);
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
                                        min = (float) Math.min(getBaseDuration(), 120000L);
                                        i = (int) (this.px + this.ph + ((((float) this.roundOffset) / min) * this.sw));
                                        if (this.hasVideo) {
                                            float f37 = this.videoRight;
                                            f = (float) this.videoDuration;
                                            f3 = f37 * f;
                                            j3 = this.roundDuration;
                                            j2 = f3 - ((float) 0);
                                            f2 = this.videoLeft;
                                            j = (f2 * f) - ((float) j3);
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
                                float f38 = this.px + this.ph;
                                float f39 = this.sw;
                                i3 = (int) (((((float) j) / min) * f39) + f38);
                                i4 = (int) (f38 + ((((float) j2) / min) * f39));
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
                                    float f40 = i3;
                                    float f41 = (float) min10;
                                    float f42 = this.sw;
                                    int i17 = (int) (((((float) this.scroll) / f41) * f42) + f40);
                                    i4 = (int) (f40 + ((((float) (this.videoDuration - min10)) / f41) * f42));
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
                                float f43 = this.videoRight - this.videoLeft;
                                float x2 = (((motionEvent.getX() - this.px) - this.ph) / this.sw) * (1.0f - f43);
                                this.videoLeft = x2;
                                this.videoRight = f43 + x2;
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
            return false;
        }
        return false;
    }

    public void selectRound(boolean z) {
        boolean z2 = true;
        if (z && this.hasRound) {
            this.roundSelected = true;
            this.audioSelected = false;
        } else {
            this.roundSelected = false;
            this.audioSelected = (!this.hasAudio || this.hasVideo) ? false : false;
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
            this.audioOffset = j2 - (((float) j) * f);
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

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0023, code lost:
        if (((float) (r4 + 240)) >= (r7 * r9.videoRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x004a, code lost:
        if (((float) (r4 + 240)) >= (r7 * r9.audioRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x006d, code lost:
        if (((float) (r4 + 240)) >= (r6 * r9.audioRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x006f, code lost:
        r9.loopProgressFrom = -1;
        r9.loopProgress.set(1.0f, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setProgress(long j) {
        boolean z = this.hasVideo;
        if (z) {
            long j2 = this.progress;
            if (j < j2) {
                float f = (float) this.videoDuration;
                if (((float) j) <= (this.videoLeft * f) + 240.0f) {
                }
            }
        }
        if (this.hasAudio && !this.hasRound && !z) {
            long j3 = this.progress;
            if (j < j3) {
                float f2 = (float) this.audioDuration;
                if (((float) j) <= (this.audioLeft * f2) + 240.0f) {
                }
            }
        }
        if (this.hasRound && !z) {
            long j4 = this.progress;
            if (j < j4) {
                float f3 = (float) this.roundDuration;
                if (((float) j) <= (this.audioLeft * f3) + 240.0f) {
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
            this.roundOffset = j2 - (((float) j) * f);
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
            this.audioRight = Utilities.clamp(((float) j) / ((float) this.audioDuration), 1.0f, 0.0f);
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
