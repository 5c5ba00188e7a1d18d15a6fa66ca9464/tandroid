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
    public interface TimelineDelegate {

        /* loaded from: classes4.dex */
        public final /* synthetic */ class -CC {
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

    public static int heightDp() {
        return R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
    }

    private long getBaseDuration() {
        if (this.hasVideo) {
            return this.videoDuration;
        }
        if (this.hasRound) {
            return this.roundDuration;
        }
        if (this.hasAudio) {
            return this.audioDuration;
        }
        return Math.max(1L, this.audioDuration);
    }

    public void setCover() {
        this.isCover = true;
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
        this.loopProgress = new AnimatedFloat(0.0f, this, 0L, 340L, cubicBezierInterpolator);
        this.loopProgressFrom = -1L;
        this.pressHandle = -1;
        this.pressType = -1;
        this.scrollingVideo = true;
        this.scrolling = false;
        this.selectedVideoRadii = new float[8];
        this.waveformRadii = new float[8];
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
        this.backgroundBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 0);
        this.audioBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 3);
        this.audioWaveformBlur = new BlurringShader.StoryBlurDrawer(blurManager, this, 4);
        this.onLongPress = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$new$5(viewGroup, resourcesProvider, blurManager, view);
            }
        };
    }

    public /* synthetic */ void lambda$new$5(ViewGroup viewGroup, Theme.ResourcesProvider resourcesProvider, BlurringShader.BlurManager blurManager, View view) {
        int i = this.pressType;
        try {
            if (i == 2 && this.hasAudio) {
                SliderView onValueChange = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.audioVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda4
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        TimelineView.this.lambda$new$0((Float) obj);
                    }
                });
                long min = Math.min(getBaseDuration(), 120000L);
                int i2 = this.w;
                int i3 = this.px;
                int i4 = this.ph;
                ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange).addSpaceGap().add(R.drawable.msg_delete, LocaleController.getString(R.string.StoryAudioRemove), new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        TimelineView.this.lambda$new$1();
                    }
                }).setGravity(5).forceTop(true).translate((-(this.w - Math.min((i2 - i3) - i4, (i3 + i4) + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioRight, 1.0f, this.audioSelectedT.get()) * ((float) this.audioDuration))) / ((float) min)) * this.sw)))) + AndroidUtilities.dp(18.0f), (((this.h - this.py) - (this.hasVideo ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasAudio ? getAudioHeight() + AndroidUtilities.dp(4.0f) : 0.0f)).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
                performHapticFeedback(0, 1);
            } else if (i == 1 && this.hasRound) {
                SliderView onValueChange2 = new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.roundVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda5
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        TimelineView.this.lambda$new$2((Float) obj);
                    }
                });
                long min2 = Math.min(getBaseDuration(), 120000L);
                int i5 = this.w;
                int i6 = this.px;
                int i7 = this.ph;
                ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange2).addSpaceGap().add(R.drawable.msg_delete, LocaleController.getString(R.string.StoryRoundRemove), new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        TimelineView.this.lambda$new$3();
                    }
                }).setGravity(5).forceTop(true).translate((-(this.w - Math.min((i5 - i6) - i7, (i6 + i7) + (((((float) (this.roundOffset - this.scroll)) + (AndroidUtilities.lerp(this.roundRight, 1.0f, this.roundSelectedT.get()) * ((float) this.roundDuration))) / ((float) min2)) * this.sw)))) + AndroidUtilities.dp(18.0f), ((this.h - this.py) - (this.hasVideo ? AndroidUtilities.dp(4.0f) + getVideoHeight() : 0.0f)) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f)).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
                performHapticFeedback(0, 1);
            } else if (i != 0 || !this.hasVideo) {
            } else {
                ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(new SliderView(getContext(), 0).setMinMax(0.0f, 1.5f).setValue(this.videoVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda6
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        TimelineView.this.lambda$new$4((Float) obj);
                    }
                })).setGravity(5).forceTop(true).translate(AndroidUtilities.dp(18.0f), (this.h - this.py) - (this.hasVideo ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f)).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
                performHapticFeedback(0, 1);
            }
        } catch (Exception unused) {
        }
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

    public void setDelegate(TimelineDelegate timelineDelegate) {
        this.delegate = timelineDelegate;
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
            setupVideoThumbs();
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

    public void setRoundNull(boolean z) {
        setRound(null, 0L, 0L, 0.0f, 0.0f, 0.0f, z);
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

    private void setupVideoThumbs() {
        if (getMeasuredWidth() <= 0 || this.thumbs != null) {
            return;
        }
        boolean z = this.isMainVideoRound;
        String str = this.videoPath;
        int i = this.w;
        int i2 = this.px;
        int i3 = (i - i2) - i2;
        int dp = AndroidUtilities.dp(38.0f);
        long j = this.videoDuration;
        VideoThumbsLoader videoThumbsLoader = new VideoThumbsLoader(this, z, str, i3, dp, j > 2 ? Long.valueOf(j) : null);
        this.thumbs = videoThumbsLoader;
        if (videoThumbsLoader.getDuration() > 0) {
            this.videoDuration = this.thumbs.getDuration();
        }
        setupRoundThumbs();
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
            VideoThumbsLoader videoThumbsLoader = new VideoThumbsLoader(false, str, i3, dp, j > 2 ? Long.valueOf(j) : null, this.hasVideo ? this.videoDuration : 120000L);
            this.roundThumbs = videoThumbsLoader;
            if (videoThumbsLoader.getDuration() > 0) {
                this.roundDuration = this.roundThumbs.getDuration();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0024, code lost:
        if (((float) (r4 + 240)) >= (((float) r7) * r11.videoRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x004c, code lost:
        if (((float) (r4 + 240)) >= (((float) r7) * r11.audioRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0070, code lost:
        if (((float) (r4 + 240)) >= (((float) r6) * r11.audioRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0072, code lost:
        r11.loopProgressFrom = -1;
        r11.loopProgress.set(1.0f, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setProgress(long j) {
        boolean z = this.hasVideo;
        if (z) {
            long j2 = this.progress;
            if (j < j2) {
                long j3 = this.videoDuration;
                if (((float) j) <= (((float) j3) * this.videoLeft) + 240.0f) {
                }
            }
        }
        if (this.hasAudio && !this.hasRound && !z) {
            long j4 = this.progress;
            if (j < j4) {
                long j5 = this.audioDuration;
                if (((float) j) <= (((float) j5) * this.audioLeft) + 240.0f) {
                }
            }
        }
        if (this.hasRound && !z) {
            long j6 = this.progress;
            if (j < j6) {
                long j7 = this.roundDuration;
                if (((float) j) <= (((float) j7) * this.audioLeft) + 240.0f) {
                }
            }
        }
        this.progress = j;
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

    private int detectHandle(MotionEvent motionEvent) {
        float f;
        float f2;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        long min = Math.min(getBaseDuration(), 120000L);
        float f3 = (float) min;
        float clamp = this.px + this.ph + (this.sw * (((float) ((Utilities.clamp(this.progress, getBaseDuration(), 0L) + (!this.hasVideo ? this.audioOffset : 0L)) - this.scroll)) / f3));
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
                int i = this.px;
                int i2 = this.ph;
                float f4 = this.videoLeft;
                long j = this.videoDuration;
                long j2 = this.scroll;
                int i3 = this.sw;
                float f5 = i + i2 + ((((f4 * ((float) j)) - ((float) j2)) / f3) * i3);
                float f6 = i + i2 + ((((this.videoRight * ((float) j)) - ((float) j2)) / f3) * i3);
                if (x >= f5 - AndroidUtilities.dp(15.0f) && x <= AndroidUtilities.dp(5.0f) + f5) {
                    return 2;
                }
                if (x >= f6 - AndroidUtilities.dp(5.0f) && x <= AndroidUtilities.dp(15.0f) + f6) {
                    return 3;
                }
                if (x >= f5 && x <= f6 && (this.videoLeft > 0.01f || this.videoRight < 0.99f)) {
                    return 4;
                }
            } else if (z) {
                int i4 = this.px;
                int i5 = this.ph;
                long j3 = this.roundOffset;
                float f7 = this.roundLeft;
                long j4 = this.roundDuration;
                long j5 = this.scroll;
                int i6 = this.sw;
                float f8 = i4 + i5 + ((((((float) j3) + (f7 * ((float) j4))) - ((float) j5)) / f3) * i6);
                float f9 = i4 + i5 + ((((((float) j3) + (this.roundRight * ((float) j4))) - ((float) j5)) / f3) * i6);
                if (this.roundSelected || !this.hasVideo) {
                    if (x >= f8 - AndroidUtilities.dp(15.0f)) {
                        f2 = 5.0f;
                        if (x <= AndroidUtilities.dp(5.0f) + f8) {
                            return 10;
                        }
                    } else {
                        f2 = 5.0f;
                    }
                    if (x >= f9 - AndroidUtilities.dp(f2) && x <= AndroidUtilities.dp(15.0f) + f9) {
                        return 11;
                    }
                    if (x >= f8 && x <= f9) {
                        return !this.hasVideo ? 12 : 9;
                    }
                    int i7 = this.px;
                    int i8 = this.ph;
                    long j6 = this.roundOffset;
                    long j7 = this.scroll;
                    int i9 = this.sw;
                    f8 = i7 + i8 + ((((float) (j6 - j7)) / f3) * i9);
                    f9 = ((((float) ((j6 + this.roundDuration) - j7)) / f3) * i9) + i7 + i8;
                }
                if (x >= f8 && x <= f9) {
                    return 9;
                }
            } else if (this.hasAudio) {
                int i10 = this.px;
                int i11 = this.ph;
                long j8 = this.audioOffset;
                float f10 = this.audioLeft;
                long j9 = this.audioDuration;
                long j10 = this.scroll;
                int i12 = this.sw;
                float f11 = i10 + i11 + ((((((float) j8) + (f10 * ((float) j9))) - ((float) j10)) / f3) * i12);
                float f12 = i10 + i11 + ((((((float) j8) + (this.audioRight * ((float) j9))) - ((float) j10)) / f3) * i12);
                if (this.audioSelected || (!this.hasVideo && !this.hasRound)) {
                    if (x >= f11 - AndroidUtilities.dp(15.0f)) {
                        f = 5.0f;
                        if (x <= AndroidUtilities.dp(5.0f) + f11) {
                            return 6;
                        }
                    } else {
                        f = 5.0f;
                    }
                    if (x >= f12 - AndroidUtilities.dp(f) && x <= AndroidUtilities.dp(15.0f) + f12) {
                        return 7;
                    }
                    if (x >= f11 && x <= f12) {
                        return !this.hasVideo ? 8 : 5;
                    }
                    int i13 = this.px;
                    int i14 = this.ph;
                    long j11 = this.audioOffset;
                    long j12 = this.scroll;
                    int i15 = this.sw;
                    f11 = i13 + i14 + ((((float) (j11 - j12)) / f3) * i15);
                    f12 = i13 + i14 + ((((float) ((j11 + this.audioDuration) - j12)) / f3) * i15);
                }
                if (x >= f11 && x <= f12) {
                    return 5;
                }
            }
            return (this.videoDuration <= 120000 || !z2) ? -1 : 1;
        }
        return 0;
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

    public boolean isDragging() {
        return this.dragged;
    }

    private boolean setProgressAt(float f, boolean z) {
        if (this.hasVideo || this.hasAudio) {
            float min = (((f - this.px) - this.ph) / this.sw) * ((float) Math.min(getBaseDuration(), 120000L));
            boolean z2 = this.hasVideo;
            final long clamp = Utilities.clamp(min + ((float) (!z2 ? -this.audioOffset : 0L)) + ((float) this.scroll), (float) (z2 ? this.videoDuration : this.audioDuration), 0.0f);
            boolean z3 = this.hasVideo;
            if (z3) {
                float f2 = (float) clamp;
                long j = this.videoDuration;
                if (f2 / ((float) j) < this.videoLeft || f2 / ((float) j) > this.videoRight) {
                    return false;
                }
            }
            if (this.hasAudio && !z3) {
                float f3 = (float) clamp;
                long j2 = this.audioDuration;
                if (f3 / ((float) j2) < this.audioLeft || f3 / ((float) j2) > this.audioRight) {
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
                Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda2
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

    public /* synthetic */ void lambda$setProgressAt$6(long j) {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onProgressChange(j, false);
        }
    }

    private float getVideoHeight() {
        if (this.hasVideo) {
            return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.videoSelectedT.set((this.audioSelected || this.roundSelected) ? false : true));
        }
        return 0.0f;
    }

    private float getAudioHeight() {
        return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.audioSelectedT.set(this.audioSelected));
    }

    private float getRoundHeight() {
        if (this.hasRound) {
            return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.roundSelectedT.set(this.roundSelected));
        }
        return 0.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:849:0x09a1  */
    /* JADX WARN: Removed duplicated region for block: B:852:0x09ab A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:859:0x09c6  */
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
        long j;
        boolean z3;
        long j2;
        long j3;
        long j4;
        float f;
        VelocityTracker velocityTracker3;
        long j5;
        long j6;
        boolean z4;
        VelocityTracker velocityTracker4;
        VelocityTracker velocityTracker5;
        float max;
        boolean z5;
        float f2;
        boolean z6;
        boolean z7;
        float max2;
        float min;
        boolean z8;
        boolean z9;
        float max3;
        float max4;
        TimelineDelegate timelineDelegate2;
        boolean z10 = this.hasVideo;
        if (z10 || this.hasAudio || this.hasRound) {
            int i = this.h;
            int i2 = this.py;
            float videoHeight = ((((i - i2) - i2) - (z10 ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasAudio ? getAudioHeight() + AndroidUtilities.dp(4.0f) : 0.0f)) - (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f);
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
                    int i3 = this.h - this.py;
                    if (this.hasVideo) {
                        float f3 = i3;
                        if (motionEvent.getY() < f3 && motionEvent.getY() > (f3 - getVideoHeight()) - AndroidUtilities.dp(2.0f)) {
                            this.pressType = 0;
                        }
                        i3 = (int) (f3 - (getVideoHeight() + AndroidUtilities.dp(4.0f)));
                    }
                    if (this.pressType == -1 && this.hasRound) {
                        float f4 = i3;
                        if (motionEvent.getY() < f4 && motionEvent.getY() > (f4 - getRoundHeight()) - AndroidUtilities.dp(2.0f)) {
                            this.pressType = 1;
                        }
                        i3 = (int) (f4 - (getRoundHeight() + AndroidUtilities.dp(4.0f)));
                    }
                    if (this.pressType == -1 && this.hasAudio) {
                        float f5 = i3;
                        if (motionEvent.getY() < f5 && motionEvent.getY() > (f5 - getAudioHeight()) - AndroidUtilities.dp(2.0f)) {
                            this.pressType = 2;
                        }
                        getAudioHeight();
                        AndroidUtilities.dp(4.0f);
                    }
                    this.pressTime = System.currentTimeMillis();
                    int i4 = this.pressHandle;
                    this.draggingProgress = i4 == 0 || i4 == -1 || i4 == 1;
                    this.hadDragChange = false;
                    if (i4 == 1 || i4 == 5 || i4 == 8) {
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
                        long min2 = Math.min(getBaseDuration(), 120000L);
                        int i5 = this.pressHandle;
                        if (i5 == 1) {
                            this.scroll = Utilities.clamp(((float) this.scroll) - ((x / this.sw) * ((float) min2)), (float) (this.videoDuration - min2), 0.0f);
                            invalidate();
                            this.dragged = true;
                            this.draggingProgress = false;
                        } else if (i5 == 2 || i5 == 3 || i5 == 4) {
                            long j7 = this.videoDuration;
                            float f6 = (x / this.sw) * (((float) min2) / ((float) j7));
                            if (i5 == 2) {
                                float clamp = Utilities.clamp(this.videoLeft + f6, this.videoRight - (1000.0f / ((float) j7)), 0.0f);
                                this.videoLeft = clamp;
                                TimelineDelegate timelineDelegate3 = this.delegate;
                                if (timelineDelegate3 != null) {
                                    timelineDelegate3.onVideoLeftChange(clamp);
                                }
                                float f7 = this.videoRight;
                                float f8 = this.videoLeft;
                                long j8 = this.videoDuration;
                                if (f7 - f8 > 59000.0f / ((float) j8)) {
                                    float min3 = Math.min(1.0f, f8 + (59000.0f / ((float) j8)));
                                    this.videoRight = min3;
                                    TimelineDelegate timelineDelegate4 = this.delegate;
                                    if (timelineDelegate4 != null) {
                                        timelineDelegate4.onVideoRightChange(min3);
                                    }
                                }
                            } else if (i5 == 3) {
                                float clamp2 = Utilities.clamp(this.videoRight + f6, 1.0f, this.videoLeft + (1000.0f / ((float) j7)));
                                this.videoRight = clamp2;
                                TimelineDelegate timelineDelegate5 = this.delegate;
                                if (timelineDelegate5 != null) {
                                    timelineDelegate5.onVideoRightChange(clamp2);
                                }
                                float f9 = this.videoRight;
                                long j9 = this.videoDuration;
                                if (f9 - this.videoLeft > 59000.0f / ((float) j9)) {
                                    float max5 = Math.max(0.0f, f9 - (59000.0f / ((float) j9)));
                                    this.videoLeft = max5;
                                    TimelineDelegate timelineDelegate6 = this.delegate;
                                    if (timelineDelegate6 != null) {
                                        timelineDelegate6.onVideoLeftChange(max5);
                                    }
                                }
                            } else if (i5 == 4) {
                                if (f6 > 0.0f) {
                                    max = Math.min(1.0f - this.videoRight, f6);
                                } else {
                                    max = Math.max(-this.videoLeft, f6);
                                }
                                float f10 = this.videoLeft + max;
                                this.videoLeft = f10;
                                this.videoRight += max;
                                TimelineDelegate timelineDelegate7 = this.delegate;
                                if (timelineDelegate7 != null) {
                                    timelineDelegate7.onVideoLeftChange(f10);
                                    this.delegate.onVideoRightChange(this.videoRight);
                                }
                            }
                            long j10 = this.progress;
                            long j11 = this.videoDuration;
                            float f11 = ((float) j10) / ((float) j11);
                            float f12 = this.videoLeft;
                            if (f11 < f12 || ((float) j10) / ((float) j11) > this.videoRight) {
                                long j12 = f12 * ((float) j11);
                                this.progress = j12;
                                TimelineDelegate timelineDelegate8 = this.delegate;
                                if (timelineDelegate8 != null) {
                                    z5 = false;
                                    timelineDelegate8.onProgressChange(j12, false);
                                    invalidate();
                                    this.dragged = true;
                                    this.draggingProgress = z5;
                                }
                            }
                            z5 = false;
                            invalidate();
                            this.dragged = true;
                            this.draggingProgress = z5;
                        } else if (i5 == 6 || i5 == 7 || i5 == 8) {
                            float f13 = (x / this.sw) * (((float) min2) / ((float) this.audioDuration));
                            if (i5 == 6) {
                                float minAudioSelect = this.audioRight - (((float) minAudioSelect()) / ((float) this.audioDuration));
                                float max6 = (float) Math.max(0L, this.scroll - this.audioOffset);
                                long j13 = this.audioDuration;
                                float f14 = max6 / ((float) j13);
                                boolean z11 = this.hasVideo;
                                if (!z11 && !this.hasRound) {
                                    f14 = Math.max(f14, this.audioRight - (59000.0f / ((float) j13)));
                                    if (!this.hadDragChange && f13 < 0.0f && this.audioLeft <= this.audioRight - (59000.0f / ((float) this.audioDuration))) {
                                        this.pressHandle = 8;
                                    }
                                } else if (z11) {
                                    f14 = Math.max(f14, (((this.videoLeft * ((float) this.videoDuration)) + ((float) this.scroll)) - ((float) this.audioOffset)) / ((float) j13));
                                } else if (this.hasRound) {
                                    f14 = Math.max(f14, (((this.roundLeft * ((float) this.roundDuration)) + ((float) this.scroll)) - ((float) this.audioOffset)) / ((float) j13));
                                }
                                float f15 = this.audioLeft;
                                float clamp3 = Utilities.clamp(f15 + f13, minAudioSelect, f14);
                                this.audioLeft = clamp3;
                                if (Math.abs(f15 - clamp3) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate9 = this.delegate;
                                if (timelineDelegate9 != null) {
                                    timelineDelegate9.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                                }
                                TimelineDelegate timelineDelegate10 = this.delegate;
                                if (timelineDelegate10 != null) {
                                    timelineDelegate10.onAudioLeftChange(this.audioLeft);
                                }
                            } else if (i5 == 7) {
                                float min4 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min2)) / ((float) this.audioDuration));
                                float f16 = this.audioLeft;
                                float minAudioSelect2 = (float) minAudioSelect();
                                long j14 = this.audioDuration;
                                float f17 = f16 + (minAudioSelect2 / ((float) j14));
                                boolean z12 = this.hasVideo;
                                if (!z12 && !this.hasRound) {
                                    float min5 = Math.min(min4, this.audioLeft + (59000.0f / ((float) j14)));
                                    if (this.hadDragChange || f13 <= 0.0f) {
                                        f2 = min5;
                                    } else {
                                        f2 = min5;
                                        if (this.audioRight >= this.audioLeft + (59000.0f / ((float) this.audioDuration))) {
                                            this.pressHandle = 8;
                                        }
                                    }
                                    min4 = f2;
                                } else if (z12) {
                                    min4 = Math.min(min4, (((this.videoRight * ((float) this.videoDuration)) + ((float) this.scroll)) - ((float) this.audioOffset)) / ((float) j14));
                                } else if (this.hasRound) {
                                    min4 = Math.min(min4, (((this.roundRight * ((float) this.roundDuration)) + ((float) this.scroll)) - ((float) this.audioOffset)) / ((float) j14));
                                }
                                float f18 = this.audioRight;
                                float clamp4 = Utilities.clamp(f18 + f13, min4, f17);
                                this.audioRight = clamp4;
                                if (Math.abs(f18 - clamp4) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate11 = this.delegate;
                                if (timelineDelegate11 != null) {
                                    timelineDelegate11.onAudioRightChange(this.audioRight);
                                }
                            }
                            if (this.pressHandle == 8) {
                                float max7 = ((float) Math.max(0L, this.scroll - this.audioOffset)) / ((float) this.audioDuration);
                                float min6 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min2)) / ((float) this.audioDuration));
                                if (f13 > 0.0f) {
                                    max2 = Math.min(Math.max(0.0f, min6 - this.audioRight), f13);
                                } else {
                                    max2 = Math.max(Math.min(0.0f, max7 - this.audioLeft), f13);
                                }
                                float f19 = this.audioLeft + max2;
                                this.audioLeft = f19;
                                this.audioRight += max2;
                                TimelineDelegate timelineDelegate12 = this.delegate;
                                if (timelineDelegate12 != null) {
                                    timelineDelegate12.onAudioLeftChange(f19);
                                    this.delegate.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                                    this.delegate.onAudioRightChange(this.audioRight);
                                }
                                TimelineDelegate timelineDelegate13 = this.delegate;
                                if (timelineDelegate13 != null) {
                                    timelineDelegate13.onProgressDragChange(true);
                                }
                            }
                            if (!this.hasVideo && !this.hasRound) {
                                this.progress = this.audioLeft * ((float) this.audioDuration);
                                TimelineDelegate timelineDelegate14 = this.delegate;
                                if (timelineDelegate14 != null) {
                                    z6 = true;
                                    timelineDelegate14.onProgressDragChange(true);
                                    z7 = false;
                                    this.delegate.onProgressChange(this.progress, false);
                                    invalidate();
                                    this.dragged = z6;
                                    this.draggingProgress = z7;
                                }
                            }
                            z6 = true;
                            z7 = false;
                            invalidate();
                            this.dragged = z6;
                            this.draggingProgress = z7;
                        } else if (i5 == 10 || i5 == 11 || i5 == 12) {
                            float f20 = (x / this.sw) * (((float) min2) / ((float) this.roundDuration));
                            if (i5 == 10) {
                                float minAudioSelect3 = this.roundRight - (((float) minAudioSelect()) / ((float) this.roundDuration));
                                long j15 = this.roundDuration;
                                float max8 = ((float) Math.max(0L, this.scroll - this.roundOffset)) / ((float) j15);
                                if (!this.hasVideo) {
                                    max4 = Math.max(max8, this.roundRight - (59000.0f / ((float) j15)));
                                    if (!this.hadDragChange && f20 < 0.0f && this.roundLeft <= this.roundRight - (59000.0f / ((float) this.roundDuration))) {
                                        this.pressHandle = 8;
                                    }
                                } else {
                                    max4 = Math.max(max8, (((this.videoLeft * ((float) this.videoDuration)) + ((float) this.scroll)) - ((float) this.roundOffset)) / ((float) j15));
                                }
                                float f21 = this.roundLeft;
                                float clamp5 = Utilities.clamp(f21 + f20, minAudioSelect3, max4);
                                this.roundLeft = clamp5;
                                if (Math.abs(f21 - clamp5) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate15 = this.delegate;
                                if (timelineDelegate15 != null) {
                                    timelineDelegate15.onRoundOffsetChange(this.roundOffset + (this.roundLeft * ((float) this.roundDuration)));
                                }
                                TimelineDelegate timelineDelegate16 = this.delegate;
                                if (timelineDelegate16 != null) {
                                    timelineDelegate16.onRoundLeftChange(this.roundLeft);
                                }
                            } else if (i5 == 11) {
                                float min7 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.roundOffset) + min2)) / ((float) this.roundDuration));
                                float f22 = this.roundLeft;
                                long j16 = this.roundDuration;
                                float minAudioSelect4 = f22 + (((float) minAudioSelect()) / ((float) j16));
                                if (!this.hasVideo) {
                                    min = Math.min(min7, this.roundLeft + (59000.0f / ((float) j16)));
                                    if (!this.hadDragChange && f20 > 0.0f && this.roundRight >= this.roundLeft + (59000.0f / ((float) this.roundDuration))) {
                                        this.pressHandle = 8;
                                    }
                                } else {
                                    min = Math.min(min7, (((this.videoRight * ((float) this.videoDuration)) + ((float) this.scroll)) - ((float) this.roundOffset)) / ((float) j16));
                                }
                                float f23 = this.roundRight;
                                float clamp6 = Utilities.clamp(f23 + f20, min, minAudioSelect4);
                                this.roundRight = clamp6;
                                if (Math.abs(f23 - clamp6) > 0.01f) {
                                    this.hadDragChange = true;
                                }
                                TimelineDelegate timelineDelegate17 = this.delegate;
                                if (timelineDelegate17 != null) {
                                    timelineDelegate17.onRoundRightChange(this.roundRight);
                                }
                            }
                            if (this.pressHandle == 12) {
                                float max9 = ((float) Math.max(0L, this.scroll - this.roundOffset)) / ((float) this.roundDuration);
                                float min8 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.roundOffset) + min2)) / ((float) this.roundDuration));
                                if (f20 > 0.0f) {
                                    max3 = Math.min(min8 - this.roundRight, f20);
                                } else {
                                    max3 = Math.max(max9 - this.roundLeft, f20);
                                }
                                float f24 = this.roundLeft + max3;
                                this.roundLeft = f24;
                                this.roundRight += max3;
                                TimelineDelegate timelineDelegate18 = this.delegate;
                                if (timelineDelegate18 != null) {
                                    timelineDelegate18.onRoundLeftChange(f24);
                                    this.delegate.onRoundOffsetChange(this.roundOffset + (this.roundLeft * ((float) this.roundDuration)));
                                    this.delegate.onRoundRightChange(this.roundRight);
                                }
                                TimelineDelegate timelineDelegate19 = this.delegate;
                                if (timelineDelegate19 != null) {
                                    timelineDelegate19.onProgressDragChange(true);
                                }
                            }
                            if (!this.hasVideo) {
                                this.progress = this.roundLeft * ((float) this.roundDuration);
                                TimelineDelegate timelineDelegate20 = this.delegate;
                                if (timelineDelegate20 != null) {
                                    z8 = true;
                                    timelineDelegate20.onProgressDragChange(true);
                                    z9 = false;
                                    this.delegate.onProgressChange(this.progress, false);
                                    invalidate();
                                    this.dragged = z8;
                                    this.draggingProgress = z9;
                                }
                            }
                            z8 = true;
                            z9 = false;
                            invalidate();
                            this.dragged = z8;
                            this.draggingProgress = z9;
                        } else if (i5 == 5) {
                            moveAudioOffset((x / this.sw) * ((float) min2));
                            this.dragged = true;
                            this.draggingProgress = false;
                        } else if (i5 == 9) {
                            moveRoundOffset((x / this.sw) * ((float) min2));
                            this.dragged = true;
                            this.draggingProgress = false;
                        } else if (this.draggingProgress) {
                            setProgressAt(motionEvent.getX(), currentTimeMillis - this.lastTime < 350);
                            if (!this.dragged && (timelineDelegate2 = this.delegate) != null) {
                                timelineDelegate2.onProgressDragChange(true);
                            }
                            this.dragged = true;
                        }
                        this.lastX = motionEvent.getX();
                    }
                    if (this.dragged) {
                        AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                    }
                    int i6 = this.pressHandle;
                    if ((i6 == 1 || i6 == 5 || i6 == 8) && (velocityTracker5 = this.velocityTracker) != null) {
                        velocityTracker5.addMovement(motionEvent);
                    }
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                    this.scroller.abortAnimation();
                    if (motionEvent.getAction() == 1) {
                        if (System.currentTimeMillis() - this.pressTime <= ViewConfiguration.getTapTimeout() && !this.dragged) {
                            if (this.isCover) {
                                float f25 = this.videoRight - this.videoLeft;
                                float x2 = (((motionEvent.getX() - this.px) - this.ph) / this.sw) * (1.0f - f25);
                                this.videoLeft = x2;
                                this.videoRight = f25 + x2;
                                TimelineDelegate timelineDelegate21 = this.delegate;
                                if (timelineDelegate21 != null) {
                                    timelineDelegate21.onVideoLeftChange(x2);
                                    this.delegate.onVideoRightChange(this.videoRight);
                                }
                                invalidate();
                            } else {
                                int i7 = this.pressType;
                                if (i7 == 2 && !this.audioSelected) {
                                    this.audioSelected = true;
                                    this.roundSelected = false;
                                    TimelineDelegate timelineDelegate22 = this.delegate;
                                    if (timelineDelegate22 != null) {
                                        timelineDelegate22.onRoundSelectChange(false);
                                    }
                                    invalidate();
                                } else if (i7 == 1 && !this.roundSelected) {
                                    this.audioSelected = false;
                                    this.roundSelected = true;
                                    TimelineDelegate timelineDelegate23 = this.delegate;
                                    if (timelineDelegate23 != null) {
                                        timelineDelegate23.onRoundSelectChange(true);
                                    }
                                    invalidate();
                                } else if (i7 != 2 && this.audioSelected) {
                                    this.audioSelected = false;
                                    this.roundSelected = false;
                                    TimelineDelegate timelineDelegate24 = this.delegate;
                                    if (timelineDelegate24 != null) {
                                        timelineDelegate24.onRoundSelectChange(false);
                                    }
                                    invalidate();
                                } else if (i7 != 1 && this.roundSelected) {
                                    this.audioSelected = false;
                                    this.roundSelected = false;
                                    TimelineDelegate timelineDelegate25 = this.delegate;
                                    if (timelineDelegate25 != null) {
                                        timelineDelegate25.onRoundSelectChange(false);
                                    }
                                    invalidate();
                                } else {
                                    long j17 = this.progress;
                                    if (setProgressAt(motionEvent.getX(), false) && Math.abs(this.progress - j17) > 400) {
                                        this.loopProgressFrom = j17;
                                        this.loopProgress.set(1.0f, true);
                                        invalidate();
                                    }
                                }
                            }
                        } else {
                            int i8 = this.pressHandle;
                            if (i8 == 1 && (velocityTracker4 = this.velocityTracker) != null) {
                                velocityTracker4.computeCurrentVelocity(1000);
                                int xVelocity = (int) this.velocityTracker.getXVelocity();
                                this.scrollingVideo = true;
                                if (Math.abs(xVelocity) > AndroidUtilities.dp(100.0f)) {
                                    long min9 = Math.min(this.videoDuration, 120000L);
                                    int i9 = this.px;
                                    float f26 = (float) min9;
                                    int i10 = this.sw;
                                    int i11 = (int) (i9 + ((((float) this.scroll) / f26) * i10));
                                    int i12 = (int) (i9 + ((((float) (this.videoDuration - min9)) / f26) * i10));
                                    this.scrolling = true;
                                    Scroller scroller = this.scroller;
                                    this.wasScrollX = i11;
                                    scroller.fling(i11, 0, -xVelocity, 0, i9, i12, 0, 0);
                                    z4 = false;
                                }
                                z4 = true;
                            } else if ((i8 == 5 || (i8 == 8 && !this.dragged)) && this.audioSelected && (velocityTracker2 = this.velocityTracker) != null) {
                                velocityTracker2.computeCurrentVelocity(this.hasVideo ? 1000 : 1500);
                                int xVelocity2 = (int) this.velocityTracker.getXVelocity();
                                this.scrollingVideo = false;
                                if (Math.abs(xVelocity2) > AndroidUtilities.dp(100.0f)) {
                                    float min10 = (float) Math.min(getBaseDuration(), 120000L);
                                    int i13 = (int) (this.px + this.ph + ((((float) this.audioOffset) / min10) * this.sw));
                                    if (this.hasVideo) {
                                        float f27 = this.videoRight;
                                        j3 = this.videoDuration;
                                        j4 = this.audioDuration;
                                        j2 = (f27 * ((float) j3)) - ((float) (0 * j4));
                                        f = this.videoLeft;
                                    } else if (this.hasRound) {
                                        float f28 = this.roundRight;
                                        j3 = this.roundDuration;
                                        j4 = this.audioDuration;
                                        j2 = (f28 * ((float) j3)) - ((float) (0 * j4));
                                        f = this.roundLeft;
                                    } else {
                                        j = -(this.audioDuration - Math.min(getBaseDuration(), 120000L));
                                        z3 = true;
                                        j2 = 0;
                                        this.scrolling = z3;
                                        Scroller scroller2 = this.scroller;
                                        this.wasScrollX = i13;
                                        int i14 = this.px;
                                        int i15 = this.ph;
                                        int i16 = this.sw;
                                        scroller2.fling(i13, 0, xVelocity2, 0, (int) (i14 + i15 + ((((float) j) / min10) * i16)), (int) (i14 + i15 + ((((float) j2) / min10) * i16)), 0, 0);
                                        z4 = false;
                                    }
                                    j = (f * ((float) j3)) - ((float) (j4 * 1));
                                    z3 = true;
                                    this.scrolling = z3;
                                    Scroller scroller22 = this.scroller;
                                    this.wasScrollX = i13;
                                    int i142 = this.px;
                                    int i152 = this.ph;
                                    int i162 = this.sw;
                                    scroller22.fling(i13, 0, xVelocity2, 0, (int) (i142 + i152 + ((((float) j) / min10) * i162)), (int) (i142 + i152 + ((((float) j2) / min10) * i162)), 0, 0);
                                    z4 = false;
                                }
                                z4 = true;
                            } else if ((i8 == 9 || (i8 == 12 && !this.dragged)) && this.roundSelected && (velocityTracker3 = this.velocityTracker) != null) {
                                velocityTracker3.computeCurrentVelocity(this.hasVideo ? 1000 : 1500);
                                int xVelocity3 = (int) this.velocityTracker.getXVelocity();
                                this.scrollingVideo = false;
                                if (Math.abs(xVelocity3) > AndroidUtilities.dp(100.0f)) {
                                    float min11 = (float) Math.min(getBaseDuration(), 120000L);
                                    int i17 = (int) (this.px + this.ph + ((((float) this.roundOffset) / min11) * this.sw));
                                    if (this.hasVideo) {
                                        float f29 = this.videoRight;
                                        long j18 = this.videoDuration;
                                        long j19 = this.roundDuration;
                                        j5 = (f29 * ((float) j18)) - ((float) (0 * j19));
                                        j6 = (this.videoLeft * ((float) j18)) - ((float) (j19 * 1));
                                    } else {
                                        j5 = 0;
                                        j6 = -(this.roundDuration - Math.min(getBaseDuration(), 120000L));
                                    }
                                    this.scrolling = true;
                                    Scroller scroller3 = this.scroller;
                                    this.wasScrollX = i17;
                                    int i18 = this.px;
                                    int i19 = this.ph;
                                    int i20 = this.sw;
                                    scroller3.fling(i17, 0, xVelocity3, 0, (int) (i18 + i19 + ((((float) j6) / min11) * i20)), (int) (i18 + i19 + ((((float) j5) / min11) * i20)), 0, 0);
                                    z = false;
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
                                }
                            }
                            z = z4;
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

    private long minAudioSelect() {
        return Math.max(1000.0f, ((float) Math.min(this.hasVideo ? this.videoDuration : this.hasRound ? this.roundDuration : this.audioDuration, 59000L)) * 0.15f);
    }

    private void moveAudioOffset(float f) {
        float f2;
        long j;
        float f3;
        long j2;
        float f4;
        long j3;
        long j4;
        long j5;
        long j6;
        long j7;
        long j8;
        TimelineDelegate timelineDelegate;
        long j9;
        long clamp;
        boolean z = this.hasVideo;
        if (!z && !this.hasRound) {
            long j10 = this.audioOffset;
            long clamp2 = Utilities.clamp(j10 + f, 0L, -(this.audioDuration - Math.min(getBaseDuration(), 120000L)));
            this.audioOffset = clamp2;
            float f5 = (float) (clamp2 - j10);
            this.audioLeft = Utilities.clamp(this.audioLeft - (f5 / ((float) this.audioDuration)), 1.0f, 0.0f);
            this.audioRight = Utilities.clamp(this.audioRight - (f5 / ((float) this.audioDuration)), 1.0f, 0.0f);
            TimelineDelegate timelineDelegate2 = this.delegate;
            if (timelineDelegate2 != null) {
                timelineDelegate2.onAudioLeftChange(this.audioLeft);
                this.delegate.onAudioRightChange(this.audioRight);
            }
        } else if (this.audioSelected) {
            if (z) {
                f2 = this.videoLeft;
                j = this.videoDuration;
            } else {
                f2 = this.roundLeft;
                j = this.roundDuration;
            }
            float f6 = f2 * ((float) j);
            if (z) {
                f3 = this.videoRight;
                j2 = this.videoDuration;
            } else {
                f3 = this.roundRight;
                j2 = this.roundDuration;
            }
            float f7 = f3 * ((float) j2);
            if (z) {
                f4 = this.videoRight - this.videoLeft;
                j3 = this.videoDuration;
            } else {
                f4 = this.roundRight - this.roundLeft;
                j3 = this.roundDuration;
            }
            float f8 = f4 * ((float) j3);
            float f9 = this.audioRight;
            long j11 = this.audioDuration;
            float f10 = this.audioLeft;
            long j12 = f6 - (((float) j11) * f10);
            float min = Math.min(f9 - f10, f8 / ((float) j11));
            long j13 = this.audioOffset;
            long j14 = f;
            if (j13 + j14 > f7 - (((float) j11) * f9)) {
                float clamp3 = Utilities.clamp(((f7 - ((float) j13)) - ((float) j14)) / ((float) this.audioDuration), 1.0f, min);
                this.audioRight = clamp3;
                float clamp4 = Utilities.clamp(clamp3 - min, 1.0f, 0.0f);
                this.audioLeft = clamp4;
                float f11 = this.audioRight;
                long j15 = this.audioDuration;
                long j16 = f7 - (f11 * ((float) j15));
                long j17 = f6 - (clamp4 * ((float) j15));
                if (j16 < j17) {
                    j7 = j17;
                    j6 = j16;
                } else {
                    j6 = j17;
                    j7 = j16;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j14, j7, j6);
                TimelineDelegate timelineDelegate3 = this.delegate;
                if (timelineDelegate3 != null) {
                    timelineDelegate3.onAudioLeftChange(this.audioLeft);
                    this.delegate.onAudioRightChange(this.audioRight);
                }
            } else if (j13 + j14 < j12) {
                float clamp5 = Utilities.clamp(((f6 - ((float) j13)) - ((float) j14)) / ((float) this.audioDuration), 1.0f - min, 0.0f);
                this.audioLeft = clamp5;
                float clamp6 = Utilities.clamp(clamp5 + min, 1.0f, 0.0f);
                this.audioRight = clamp6;
                long j18 = this.audioDuration;
                long j19 = f7 - (clamp6 * ((float) j18));
                long j20 = f6 - (this.audioLeft * ((float) j18));
                if (j19 < j20) {
                    j5 = j19;
                    j4 = j20;
                } else {
                    j4 = j19;
                    j5 = j20;
                }
                this.audioOffset = Utilities.clamp(this.audioOffset + j14, j4, j5);
                TimelineDelegate timelineDelegate4 = this.delegate;
                if (timelineDelegate4 != null) {
                    timelineDelegate4.onAudioLeftChange(this.audioLeft);
                    this.delegate.onAudioRightChange(this.audioRight);
                }
            } else {
                this.audioOffset = j13 + j14;
            }
        } else {
            long j21 = this.audioOffset + f;
            long j22 = this.audioDuration;
            this.audioOffset = Utilities.clamp(j21, ((float) getBaseDuration()) - (((float) j22) * this.audioRight), (-this.audioLeft) * ((float) j22));
        }
        invalidate();
        TimelineDelegate timelineDelegate5 = this.delegate;
        if (timelineDelegate5 != null) {
            timelineDelegate5.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
        }
        boolean z2 = this.dragged;
        if (!z2 && (timelineDelegate = this.delegate) != null) {
            timelineDelegate.onProgressDragChange(true);
            if (this.hasVideo) {
                long j23 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f12 = this.videoRight;
                long j24 = this.videoDuration;
                clamp = Utilities.clamp(j23, f12 * ((float) j24), this.videoLeft * ((float) j24));
            } else if (this.hasRound) {
                long j25 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f13 = this.roundRight;
                long j26 = this.roundDuration;
                clamp = Utilities.clamp(j25, f13 * ((float) j26), this.roundLeft * ((float) j26));
            } else {
                float f14 = this.audioLeft;
                clamp = Utilities.clamp(f14 * ((float) j9), this.audioDuration, 0L);
            }
            if (this.hasVideo && Math.abs(this.progress - clamp) > 400) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            TimelineDelegate timelineDelegate6 = this.delegate;
            this.progress = clamp;
            timelineDelegate6.onProgressChange(clamp, false);
        } else if (z2 || this.scrolling) {
            if (this.hasVideo) {
                long j27 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f15 = this.videoRight;
                long j28 = this.videoDuration;
                this.progress = Utilities.clamp(j27, f15 * ((float) j28), this.videoLeft * ((float) j28));
            } else if (this.hasRound) {
                long j29 = this.audioOffset + (this.audioLeft * ((float) this.audioDuration));
                float f16 = this.roundRight;
                long j30 = this.videoDuration;
                this.progress = Utilities.clamp(j29, f16 * ((float) j30), this.roundLeft * ((float) j30));
            } else {
                float f17 = this.audioLeft;
                this.progress = Utilities.clamp(f17 * ((float) j8), this.audioDuration, 0L);
            }
            TimelineDelegate timelineDelegate7 = this.delegate;
            if (timelineDelegate7 != null) {
                timelineDelegate7.onProgressChange(this.progress, false);
            }
        }
    }

    private void moveRoundOffset(float f) {
        long j;
        long j2;
        long j3;
        long j4;
        long j5;
        TimelineDelegate timelineDelegate;
        long j6;
        long clamp;
        if (!this.hasVideo) {
            long j7 = this.roundOffset;
            long clamp2 = Utilities.clamp(j7 + f, 0L, -(this.roundDuration - Math.min(getBaseDuration(), 120000L)));
            this.roundOffset = clamp2;
            float f2 = (float) (clamp2 - j7);
            this.roundLeft = Utilities.clamp(this.roundLeft - (f2 / ((float) this.roundDuration)), 1.0f, 0.0f);
            this.roundRight = Utilities.clamp(this.roundRight - (f2 / ((float) this.roundDuration)), 1.0f, 0.0f);
            TimelineDelegate timelineDelegate2 = this.delegate;
            if (timelineDelegate2 != null) {
                timelineDelegate2.onAudioLeftChange(this.roundLeft);
                this.delegate.onAudioRightChange(this.roundRight);
            }
        } else if (this.roundSelected) {
            float f3 = this.videoRight;
            long j8 = this.videoDuration;
            float f4 = this.roundRight;
            long j9 = this.roundDuration;
            long j10 = (((float) j8) * f3) - (((float) j9) * f4);
            float f5 = this.videoLeft;
            float f6 = this.roundLeft;
            long j11 = (((float) j8) * f5) - (((float) j9) * f6);
            float min = Math.min(f4 - f6, ((f3 - f5) * ((float) j8)) / ((float) j9));
            long j12 = this.roundOffset;
            long j13 = f;
            if (j12 + j13 > j10) {
                float clamp3 = Utilities.clamp((((this.videoRight * ((float) this.videoDuration)) - ((float) j12)) - ((float) j13)) / ((float) this.roundDuration), 1.0f, min);
                this.roundRight = clamp3;
                float clamp4 = Utilities.clamp(clamp3 - min, 1.0f, 0.0f);
                this.roundLeft = clamp4;
                float f7 = this.videoRight;
                long j14 = this.videoDuration;
                float f8 = this.roundRight;
                long j15 = this.roundDuration;
                long j16 = (f7 * ((float) j14)) - (f8 * ((float) j15));
                long j17 = (this.videoLeft * ((float) j14)) - (clamp4 * ((float) j15));
                if (j16 < j17) {
                    j4 = j17;
                    j3 = j16;
                } else {
                    j3 = j17;
                    j4 = j16;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j13, j4, j3);
                TimelineDelegate timelineDelegate3 = this.delegate;
                if (timelineDelegate3 != null) {
                    timelineDelegate3.onRoundLeftChange(this.roundLeft);
                    this.delegate.onRoundRightChange(this.roundRight);
                }
            } else if (j12 + j13 < j11) {
                float clamp5 = Utilities.clamp((((this.videoLeft * ((float) this.videoDuration)) - ((float) j12)) - ((float) j13)) / ((float) this.roundDuration), 1.0f - min, 0.0f);
                this.roundLeft = clamp5;
                float clamp6 = Utilities.clamp(clamp5 + min, 1.0f, 0.0f);
                this.roundRight = clamp6;
                float f9 = this.videoRight;
                long j18 = this.videoDuration;
                long j19 = this.roundDuration;
                long j20 = (f9 * ((float) j18)) - (clamp6 * ((float) j19));
                long j21 = (this.videoLeft * ((float) j18)) - (this.roundLeft * ((float) j19));
                if (j20 < j21) {
                    j2 = j20;
                    j = j21;
                } else {
                    j = j20;
                    j2 = j21;
                }
                this.roundOffset = Utilities.clamp(this.roundOffset + j13, j, j2);
                TimelineDelegate timelineDelegate4 = this.delegate;
                if (timelineDelegate4 != null) {
                    timelineDelegate4.onRoundLeftChange(this.roundLeft);
                    this.delegate.onRoundRightChange(this.roundRight);
                }
            } else {
                this.roundOffset = j12 + j13;
            }
        } else {
            long j22 = this.roundOffset + f;
            long j23 = this.roundDuration;
            this.roundOffset = Utilities.clamp(j22, ((float) getBaseDuration()) - (((float) j23) * this.roundRight), (-this.roundLeft) * ((float) j23));
        }
        invalidate();
        TimelineDelegate timelineDelegate5 = this.delegate;
        if (timelineDelegate5 != null) {
            timelineDelegate5.onRoundOffsetChange(this.roundOffset + (this.roundLeft * ((float) this.roundDuration)));
        }
        boolean z = this.dragged;
        if (!z && (timelineDelegate = this.delegate) != null) {
            timelineDelegate.onProgressDragChange(true);
            if (this.hasVideo) {
                long j24 = this.roundOffset + (this.roundLeft * ((float) this.roundDuration));
                float f10 = this.videoRight;
                long j25 = this.videoDuration;
                clamp = Utilities.clamp(j24, f10 * ((float) j25), this.videoLeft * ((float) j25));
            } else {
                float f11 = this.roundLeft;
                clamp = Utilities.clamp(f11 * ((float) j6), this.roundDuration, 0L);
            }
            if (this.hasVideo && Math.abs(this.progress - clamp) > 400) {
                this.loopProgressFrom = this.progress;
                this.loopProgress.set(1.0f, true);
            }
            TimelineDelegate timelineDelegate6 = this.delegate;
            this.progress = clamp;
            timelineDelegate6.onProgressChange(clamp, false);
        } else if (z || this.scrolling) {
            if (this.hasVideo) {
                long j26 = this.roundOffset + (this.roundLeft * ((float) this.roundDuration));
                float f12 = this.videoRight;
                long j27 = this.videoDuration;
                this.progress = Utilities.clamp(j26, f12 * ((float) j27), this.videoLeft * ((float) j27));
            } else {
                float f13 = this.roundLeft;
                this.progress = Utilities.clamp(f13 * ((float) j5), this.roundDuration, 0L);
            }
            TimelineDelegate timelineDelegate7 = this.delegate;
            if (timelineDelegate7 != null) {
                timelineDelegate7.onProgressChange(this.progress, false);
            }
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
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
                int i3 = this.sw;
                float f = (float) min;
                moveAudioOffset(((((currX - i) - i2) / i3) * f) - ((((this.wasScrollX - i) - i2) / i3) * f));
            }
            invalidate();
            this.wasScrollX = currX;
        } else if (this.scrolling) {
            this.scrolling = false;
            TimelineDelegate timelineDelegate = this.delegate;
            if (timelineDelegate != null) {
                timelineDelegate.onProgressDragChange(false);
            }
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

        public void check(float f, float f2, float f3, float f4, float f5, long j, float f6, float f7, float f8) {
            if (TimelineView.this.waveform != null) {
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
                    return;
                }
                return;
            }
            rewind();
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
    }

    /* JADX WARN: Removed duplicated region for block: B:433:0x03d7  */
    /* JADX WARN: Removed duplicated region for block: B:438:0x040f  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        float f;
        long j;
        Paint paint;
        float f2;
        float f3;
        float f4;
        float f5;
        long j2;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        long j3;
        float f11;
        double d;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        long j4;
        float f17;
        float f18;
        float f19;
        int i;
        int i2;
        float max;
        float f20;
        float f21;
        float max2;
        Paint paint2;
        long j5;
        float f22;
        float lerp;
        float lerp2;
        float f23;
        Paint paint3;
        long j6;
        double d2;
        long j7;
        float f24;
        float f25;
        Paint paint4;
        long j8;
        float f26;
        float f27;
        boolean z;
        Bitmap bitmap;
        float f28;
        float f29;
        float f30;
        Bitmap bitmap2;
        TimelineView timelineView = this;
        Canvas canvas2 = canvas;
        Paint paint5 = timelineView.backgroundBlur.getPaint(1.0f);
        long min = Math.min(getBaseDuration(), 120000L);
        float f31 = timelineView.hasVideo ? 1.0f : 0.0f;
        float f32 = timelineView.videoSelectedT.set((timelineView.audioSelected || timelineView.roundSelected) ? false : true);
        if (timelineView.hasVideo) {
            canvas.save();
            float videoHeight = getVideoHeight();
            long j9 = timelineView.videoDuration;
            if (j9 <= 0) {
                f28 = f31;
                f29 = 0.0f;
            } else {
                f28 = f31;
                f29 = (timelineView.px + timelineView.ph) - ((((float) timelineView.scroll) / ((float) min)) * timelineView.sw);
            }
            int i3 = timelineView.ph;
            float f33 = f29 - i3;
            float f34 = (j9 <= 0 ? 0.0f : timelineView.px + i3 + ((((float) (j9 - timelineView.scroll)) / ((float) min)) * timelineView.sw)) + i3;
            RectF rectF = timelineView.videoBounds;
            int i4 = timelineView.h;
            int i5 = timelineView.py;
            rectF.set(f33, (i4 - i5) - videoHeight, f34, i4 - i5);
            timelineView.videoClipPath.rewind();
            timelineView.videoClipPath.addRoundRect(timelineView.videoBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas2.clipPath(timelineView.videoClipPath);
            VideoThumbsLoader videoThumbsLoader = timelineView.thumbs;
            if (videoThumbsLoader != null) {
                float frameWidth = videoThumbsLoader.getFrameWidth();
                int max3 = (int) Math.max(0.0d, Math.floor((f33 - timelineView.px) / frameWidth));
                int min2 = (int) Math.min(timelineView.thumbs.count, Math.ceil(((f34 - f33) - timelineView.px) / frameWidth) + 1.0d);
                int i6 = (int) timelineView.videoBounds.top;
                boolean z2 = timelineView.thumbs.frames.size() >= min2;
                boolean z3 = z2 && !timelineView.isMainVideoRound;
                if (z3) {
                    int i7 = max3;
                    while (true) {
                        if (i7 >= Math.min(timelineView.thumbs.frames.size(), min2)) {
                            break;
                        } else if (((VideoThumbsLoader.BitmapFrame) timelineView.thumbs.frames.get(i7)).bitmap == null) {
                            z3 = false;
                            break;
                        } else {
                            i7++;
                        }
                    }
                }
                if (!z3) {
                    if (paint5 == null) {
                        canvas2.drawColor(1073741824);
                    } else {
                        canvas2.drawRect(timelineView.videoBounds, paint5);
                        canvas2.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                    }
                }
                while (max3 < Math.min(timelineView.thumbs.frames.size(), min2)) {
                    VideoThumbsLoader.BitmapFrame bitmapFrame = (VideoThumbsLoader.BitmapFrame) timelineView.thumbs.frames.get(max3);
                    if (bitmapFrame.bitmap != null) {
                        timelineView.videoFramePaint.setAlpha((int) (bitmapFrame.getAlpha() * 255.0f));
                        canvas2.drawBitmap(bitmapFrame.bitmap, f33, i6 - ((int) ((bitmap2.getHeight() - videoHeight) / 2.0f)), timelineView.videoFramePaint);
                    }
                    f33 += frameWidth;
                    max3++;
                }
                if (!z2) {
                    timelineView.thumbs.load();
                }
            }
            timelineView.selectedVideoClipPath.rewind();
            if (timelineView.isCover) {
                f30 = videoHeight;
                j = min;
                f = f28;
                paint = paint5;
            } else {
                RectF rectF2 = AndroidUtilities.rectTmp;
                int i8 = timelineView.px;
                int i9 = timelineView.ph;
                float f35 = timelineView.videoLeft;
                long j10 = timelineView.videoDuration;
                f = f28;
                paint = paint5;
                long j11 = timelineView.scroll;
                float f36 = (float) min;
                j = min;
                int i10 = timelineView.sw;
                float f37 = ((i8 + i9) + ((((((float) j10) * f35) - ((float) j11)) / f36) * i10)) - (f35 <= 0.0f ? i9 : 0);
                int i11 = timelineView.h;
                int i12 = timelineView.py;
                float f38 = (i11 - i12) - videoHeight;
                f30 = videoHeight;
                float f39 = timelineView.videoRight;
                float f40 = i8 + i9 + ((((((float) j10) * f39) - ((float) j11)) / f36) * i10);
                if (f39 < 1.0f) {
                    i9 = 0;
                }
                rectF2.set(f37, f38, f40 + i9, i11 - i12);
                timelineView.selectedVideoClipPath.addRoundRect(rectF2, timelineView.selectedVideoRadii, Path.Direction.CW);
                canvas2.clipPath(timelineView.selectedVideoClipPath, Region.Op.DIFFERENCE);
                canvas2.drawColor(1342177280);
            }
            canvas.restore();
            f2 = f30;
        } else {
            f = f31;
            j = min;
            paint = paint5;
            f2 = 0.0f;
        }
        float dp = AndroidUtilities.dp(4.0f);
        float f41 = timelineView.roundT.set(timelineView.hasRound);
        float f42 = timelineView.roundSelectedT.set(timelineView.hasRound && timelineView.roundSelected);
        float roundHeight = getRoundHeight() * f41;
        if (f41 > 0.0f) {
            if (timelineView.hasVideo) {
                long j12 = j;
                float f43 = (float) j12;
                f25 = timelineView.px + timelineView.ph + (((((float) (timelineView.roundOffset - timelineView.scroll)) + (AndroidUtilities.lerp(timelineView.roundLeft, 0.0f, f42) * ((float) timelineView.roundDuration))) / f43) * timelineView.sw);
                f6 = f41;
                f24 = timelineView.px + timelineView.ph + (((((float) (timelineView.roundOffset - timelineView.scroll)) + (AndroidUtilities.lerp(timelineView.roundRight, 1.0f, f42) * ((float) timelineView.roundDuration))) / f43) * timelineView.sw);
                j7 = j12;
                f3 = f42;
            } else {
                long j13 = j;
                f6 = f41;
                int i13 = timelineView.px;
                int i14 = timelineView.ph;
                long j14 = timelineView.roundOffset;
                long j15 = timelineView.scroll;
                float f44 = (float) j13;
                timelineView = this;
                f3 = f42;
                int i15 = timelineView.sw;
                j7 = j13;
                float f45 = i13 + i14 + ((((float) (j14 - j15)) / f44) * i15);
                f24 = ((((float) ((j14 - j15) + timelineView.roundDuration)) / f44) * i15) + i13 + i14;
                f25 = f45;
            }
            float f46 = ((timelineView.h - timelineView.py) - f2) - (dp * f);
            RectF rectF3 = timelineView.roundBounds;
            int i16 = timelineView.ph;
            rectF3.set(f25 - i16, f46 - roundHeight, f24 + i16, f46);
            timelineView.roundClipPath.rewind();
            timelineView.roundClipPath.addRoundRect(timelineView.roundBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.save();
            canvas2.clipPath(timelineView.roundClipPath);
            VideoThumbsLoader videoThumbsLoader2 = timelineView.roundThumbs;
            if (videoThumbsLoader2 != null) {
                long j16 = timelineView.roundDuration;
                if (j16 <= 0) {
                    j8 = j7;
                    f26 = 0.0f;
                } else {
                    j8 = j7;
                    f26 = timelineView.px + timelineView.ph + ((((float) (timelineView.roundOffset - timelineView.scroll)) / ((float) j8)) * timelineView.sw);
                }
                int i17 = timelineView.ph;
                float f47 = f26 - i17;
                float f48 = (j16 <= 0 ? 0.0f : ((((float) ((timelineView.roundOffset + j16) - timelineView.scroll)) / ((float) j8)) * timelineView.sw) + timelineView.px + i17) + i17;
                int frameWidth2 = videoThumbsLoader2.getFrameWidth();
                if (timelineView.hasVideo) {
                    f27 = timelineView.px + timelineView.ph + ((((float) (timelineView.roundOffset - timelineView.scroll)) / ((float) j8)) * timelineView.sw);
                } else {
                    f27 = timelineView.px;
                }
                float f49 = frameWidth2;
                int max4 = (int) Math.max(0.0d, Math.floor((f47 - f27) / f49));
                int min3 = (int) Math.min(timelineView.roundThumbs.count, Math.ceil((f48 - f47) / f49) + 1.0d);
                int i18 = (int) timelineView.roundBounds.top;
                boolean z4 = timelineView.roundThumbs.frames.size() >= min3;
                if (z4) {
                    for (int i19 = max4; i19 < Math.min(timelineView.roundThumbs.frames.size(), min3); i19++) {
                        if (((VideoThumbsLoader.BitmapFrame) timelineView.roundThumbs.frames.get(i19)).bitmap == null) {
                            z = false;
                            break;
                        }
                    }
                }
                z = z4;
                if (!z) {
                    if (paint == null) {
                        canvas2.drawColor(1073741824);
                    } else {
                        paint4 = paint;
                        canvas2.drawRect(timelineView.roundBounds, paint4);
                        canvas2.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                        while (max4 < Math.min(timelineView.roundThumbs.frames.size(), min3)) {
                            VideoThumbsLoader.BitmapFrame bitmapFrame2 = (VideoThumbsLoader.BitmapFrame) timelineView.roundThumbs.frames.get(max4);
                            if (bitmapFrame2.bitmap != null) {
                                timelineView.videoFramePaint.setAlpha((int) (bitmapFrame2.getAlpha() * 255.0f));
                                canvas2.drawBitmap(bitmapFrame2.bitmap, f47, i18 - ((int) ((bitmap.getHeight() - roundHeight) / 2.0f)), timelineView.videoFramePaint);
                            }
                            f47 += f49;
                            max4++;
                        }
                        if (!z4) {
                            timelineView.roundThumbs.load();
                        }
                    }
                }
                paint4 = paint;
                while (max4 < Math.min(timelineView.roundThumbs.frames.size(), min3)) {
                }
                if (!z4) {
                }
            } else {
                paint4 = paint;
                j8 = j7;
            }
            timelineView.selectedVideoClipPath.rewind();
            RectF rectF4 = AndroidUtilities.rectTmp;
            int i20 = timelineView.px;
            int i21 = timelineView.ph;
            float f50 = timelineView.roundLeft;
            long j17 = timelineView.roundDuration;
            paint = paint4;
            long j18 = timelineView.scroll;
            f5 = dp;
            f4 = f2;
            long j19 = timelineView.roundOffset;
            float f51 = (float) j8;
            j2 = j8;
            int i22 = timelineView.sw;
            float f52 = 1.0f - f3;
            float f53 = (((i20 + i21) + (((((((float) j17) * f50) - ((float) j18)) + ((float) j19)) / f51) * i22)) - (f50 <= 0.0f ? i21 : 0)) - (i21 * f52);
            RectF rectF5 = timelineView.roundBounds;
            float f54 = rectF5.top;
            float f55 = timelineView.roundRight;
            rectF4.set(f53, f54, i20 + i21 + (((((((float) j17) * f55) - ((float) j18)) + ((float) j19)) / f51) * i22) + (f55 >= 1.0f ? i21 : 0) + (i21 * f52), rectF5.bottom);
            timelineView.selectedVideoClipPath.addRoundRect(rectF4, timelineView.selectedVideoRadii, Path.Direction.CW);
            canvas2 = canvas;
            canvas2.clipPath(timelineView.selectedVideoClipPath, Region.Op.DIFFERENCE);
            canvas2.drawColor(1342177280);
            canvas.restore();
        } else {
            f3 = f42;
            f4 = f2;
            f5 = dp;
            j2 = j;
            f6 = f41;
        }
        float f56 = timelineView.audioT.set(timelineView.hasAudio);
        float f57 = timelineView.audioSelectedT.set(timelineView.hasAudio && timelineView.audioSelected);
        float audioHeight = getAudioHeight() * f56;
        if (f56 > 0.0f) {
            Paint paint6 = timelineView.audioBlur.getPaint(f56);
            canvas.save();
            if (timelineView.hasVideo || timelineView.hasRound) {
                paint2 = paint6;
                j5 = j2;
                f22 = f56;
                float f58 = (float) j5;
                lerp = timelineView.px + timelineView.ph + (((((float) (timelineView.audioOffset - timelineView.scroll)) + (AndroidUtilities.lerp(timelineView.audioLeft, 0.0f, f57) * ((float) timelineView.audioDuration))) / f58) * timelineView.sw);
                lerp2 = timelineView.px + timelineView.ph + (((((float) (timelineView.audioOffset - timelineView.scroll)) + (AndroidUtilities.lerp(timelineView.audioRight, 1.0f, f57) * ((float) timelineView.audioDuration))) / f58) * timelineView.sw);
            } else {
                int i23 = timelineView.px;
                int i24 = timelineView.ph;
                long j20 = timelineView.audioOffset;
                long j21 = timelineView.scroll;
                j5 = j2;
                f22 = f56;
                float f59 = (float) j5;
                paint2 = paint6;
                int i25 = timelineView.sw;
                lerp2 = i23 + i24 + ((((float) ((j20 - j21) + timelineView.audioDuration)) / f59) * i25);
                lerp = i23 + i24 + ((((float) (j20 - j21)) / f59) * i25);
            }
            float f60 = ((((timelineView.h - timelineView.py) - f4) - (f5 * f)) - roundHeight) - (f5 * f6);
            RectF rectF6 = timelineView.audioBounds;
            int i26 = timelineView.ph;
            float f61 = f60 - audioHeight;
            rectF6.set(lerp - i26, f61, lerp2 + i26, f60);
            timelineView.audioClipPath.rewind();
            timelineView.audioClipPath.addRoundRect(timelineView.audioBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.clipPath(timelineView.audioClipPath);
            if (paint2 == null) {
                f23 = f22;
                canvas.drawColor(Theme.multAlpha(1073741824, f23));
                paint3 = paint2;
            } else {
                f23 = f22;
                paint3 = paint2;
                canvas.drawRect(timelineView.audioBounds, paint3);
                canvas.drawColor(Theme.multAlpha(AndroidUtilities.DARK_STATUS_BAR_OVERLAY, f23));
            }
            if (timelineView.waveform == null || paint3 == null) {
                j6 = j5;
                d2 = 0.0d;
            } else {
                Paint paint7 = timelineView.audioWaveformBlur.getPaint(0.4f * f23);
                if (paint7 == null) {
                    paint7 = timelineView.waveformPaint;
                    paint7.setAlpha((int) (64.0f * f23));
                }
                float f62 = timelineView.waveformMax.set(timelineView.waveform.getMaxBar(), !timelineView.waveformIsLoaded);
                timelineView.waveformIsLoaded = timelineView.waveform.getLoadedCount() > 0;
                j6 = j5;
                d2 = 0.0d;
                timelineView.waveformPath.check(timelineView.px + timelineView.ph + ((((float) (timelineView.audioOffset - timelineView.scroll)) / ((float) j5)) * timelineView.sw), lerp, lerp2, f57, timelineView.waveformLoaded.set(timelineView.waveform.getLoadedCount()), j6, audioHeight, f62, f60);
                canvas.drawPath(timelineView.waveformPath, paint7);
            }
            if (f57 < 1.0f) {
                int i27 = timelineView.px;
                int i28 = timelineView.ph;
                long j22 = timelineView.audioOffset;
                long j23 = timelineView.scroll;
                f8 = audioHeight;
                f12 = f23;
                float f63 = timelineView.audioLeft;
                long j24 = timelineView.audioDuration;
                f7 = f57;
                float f64 = ((float) (j22 - j23)) + (f63 * ((float) j24));
                float f65 = (float) j6;
                int i29 = timelineView.sw;
                long j25 = j6;
                float f66 = i27 + i28 + ((f64 / f65) * i29);
                float f67 = i28 + i27 + (((((float) (j22 - j23)) + (timelineView.audioRight * ((float) j24))) / f65) * i29);
                float max5 = (Math.max(i27, f66) + Math.min(timelineView.w - timelineView.px, f67)) / 2.0f;
                float dp2 = f61 + AndroidUtilities.dp(14.0f);
                float max6 = Math.max(0.0f, (Math.min(timelineView.w - timelineView.px, f67) - Math.max(timelineView.px, f66)) - AndroidUtilities.dp(24.0f));
                float dpf2 = AndroidUtilities.dpf2(13.0f) + ((timelineView.audioAuthor == null && timelineView.audioTitle == null) ? 0.0f : AndroidUtilities.dpf2(3.11f) + timelineView.audioAuthorWidth + AndroidUtilities.dpf2(9.66f) + timelineView.audioTitleWidth);
                boolean z5 = dpf2 < max6;
                float min4 = max5 - (Math.min(dpf2, max6) / 2.0f);
                timelineView.audioIcon.setBounds((int) min4, (int) (dp2 - (AndroidUtilities.dp(13.0f) / 2.0f)), (int) (AndroidUtilities.dp(13.0f) + min4), (int) ((AndroidUtilities.dp(13.0f) / 2.0f) + dp2));
                float f68 = 1.0f - f7;
                float f69 = f68 * 255.0f;
                timelineView.audioIcon.setAlpha((int) f69);
                timelineView.audioIcon.draw(canvas);
                float dpf22 = min4 + AndroidUtilities.dpf2(16.11f);
                d = d2;
                f9 = f;
                j3 = j25;
                f10 = f3;
                canvas.saveLayerAlpha(0.0f, 0.0f, timelineView.w, timelineView.h, 255, 31);
                float min5 = Math.min(f67, timelineView.w) - AndroidUtilities.dp(12.0f);
                f11 = 0.0f;
                canvas.clipRect(dpf22, 0.0f, min5, timelineView.h);
                if (timelineView.audioAuthor != null) {
                    canvas.save();
                    canvas.translate(dpf22 - timelineView.audioAuthorLeft, dp2 - (timelineView.audioAuthor.getHeight() / 2.0f));
                    timelineView.audioAuthorPaint.setAlpha((int) (f69 * f12));
                    timelineView.audioAuthor.draw(canvas);
                    canvas.restore();
                    dpf22 += timelineView.audioAuthorWidth;
                }
                if (timelineView.audioAuthor != null && timelineView.audioTitle != null) {
                    float dpf23 = dpf22 + AndroidUtilities.dpf2(3.66f);
                    int alpha = timelineView.audioDotPaint.getAlpha();
                    timelineView.audioDotPaint.setAlpha((int) (alpha * f68));
                    canvas.drawCircle(AndroidUtilities.dp(1.0f) + dpf23, dp2, AndroidUtilities.dp(1.0f), timelineView.audioDotPaint);
                    timelineView.audioDotPaint.setAlpha(alpha);
                    dpf22 = dpf23 + AndroidUtilities.dpf2(2.0f) + AndroidUtilities.dpf2(4.0f);
                }
                if (timelineView.audioTitle != null) {
                    canvas.save();
                    canvas.translate(dpf22 - timelineView.audioTitleLeft, dp2 - (timelineView.audioTitle.getHeight() / 2.0f));
                    timelineView.audioTitlePaint.setAlpha((int) (f69 * f12));
                    timelineView.audioTitle.draw(canvas);
                    canvas.restore();
                }
                if (!z5) {
                    timelineView.ellipsizeMatrix.reset();
                    timelineView.ellipsizeMatrix.postScale(AndroidUtilities.dpf2(8.0f) / 16.0f, 1.0f);
                    timelineView.ellipsizeMatrix.postTranslate(min5 - AndroidUtilities.dp(8.0f), 0.0f);
                    timelineView.ellipsizeGradient.setLocalMatrix(timelineView.ellipsizeMatrix);
                    canvas.drawRect(min5 - AndroidUtilities.dp(8.0f), f61, min5, f60, timelineView.ellipsizePaint);
                }
                canvas.restore();
            } else {
                j3 = j6;
                f7 = f57;
                f8 = audioHeight;
                f12 = f23;
                d = d2;
                f9 = f;
                f10 = f3;
                f11 = 0.0f;
            }
            canvas.restore();
        } else {
            f7 = f57;
            f8 = audioHeight;
            f9 = f;
            f10 = f3;
            j3 = j2;
            f11 = 0.0f;
            d = 0.0d;
            f12 = f56;
        }
        boolean z6 = timelineView.hasVideo;
        float f70 = f12 * ((z6 || timelineView.hasRound) ? f7 : 1.0f);
        float f71 = f6 * ((z6 || timelineView.hasAudio) ? f10 : 1.0f);
        int i30 = timelineView.h;
        int i31 = timelineView.py;
        float f72 = f9;
        float f73 = f5 * f72;
        float f74 = f5 * f6;
        float f75 = (((((((i30 - i31) - f4) - f73) - roundHeight) - f74) - f8) * f70) + f11 + (((((i30 - i31) - f4) - f73) - roundHeight) * f71) + (((i30 - i31) - f4) * f32);
        float f76 = ((((((i30 - i31) - f4) - f73) - roundHeight) - f74) * f70) + f11 + ((((i30 - i31) - f4) - f73) * f71) + ((i30 - i31) * f32);
        long j26 = timelineView.audioOffset;
        float f77 = timelineView.audioLeft;
        long j27 = timelineView.audioDuration;
        float f78 = ((((float) j26) + (f77 * ((float) j27))) * f70) + f11;
        long j28 = timelineView.roundOffset;
        float f79 = (float) j28;
        float f80 = timelineView.roundLeft;
        long j29 = timelineView.roundDuration;
        float f81 = f78 + ((f79 + (f80 * ((float) j29))) * f71);
        float f82 = timelineView.videoLeft;
        long j30 = timelineView.videoDuration;
        float f83 = ((((float) j26) + (timelineView.audioRight * ((float) j27))) * f70) + 0.0f + ((((float) j28) + (timelineView.roundRight * ((float) j29))) * f71) + (timelineView.videoRight * ((float) j30) * f32);
        int i32 = timelineView.px;
        int i33 = timelineView.ph;
        long j31 = timelineView.scroll;
        long j32 = j3;
        float f84 = (float) j32;
        int i34 = timelineView.sw;
        float f85 = ((((f81 + ((f82 * ((float) j30)) * f32)) - ((float) j31)) / f84) * i34) + i32 + i33;
        float f86 = i32 + i33 + (((f83 - ((float) j31)) / f84) * i34);
        if (!timelineView.hasAudio || z6) {
            f13 = f6;
            f14 = f12;
            f12 = Math.max(f72, f13);
        } else {
            f13 = f6;
            f14 = f12;
        }
        if (f14 > d || f13 > d || f72 > d) {
            if (timelineView.hasVideo || timelineView.hasRound) {
                f15 = f7;
                f16 = 1.0f;
            } else {
                f15 = f7;
                f16 = AndroidUtilities.lerp(0.6f, 1.0f, f15) * f14;
            }
            j4 = j32;
            drawRegion(canvas, paint, f75, f76, f85, f86, f16 * f12);
            if (!timelineView.hasVideo || (!(timelineView.hasAudio || timelineView.hasRound) || (f15 <= 0.0f && f10 <= 0.0f))) {
                f17 = f14;
            } else {
                float f87 = timelineView.h - timelineView.py;
                int i35 = timelineView.ph;
                int i36 = timelineView.px;
                float f88 = timelineView.videoLeft;
                long j33 = timelineView.videoDuration;
                long j34 = timelineView.scroll;
                int i37 = timelineView.sw;
                f17 = f14;
                drawRegion(canvas, paint, (i - i2) - f4, f87, ((((f88 * ((float) j33)) - ((float) j34)) / f84) * i37) + i35 + i36, i35 + i36 + ((((timelineView.videoRight * ((float) j33)) - ((float) j34)) / f84) * i37), 0.8f);
            }
            float f89 = timelineView.loopProgress.set(0.0f);
            float max7 = ((((timelineView.h - timelineView.py) - f4) - ((f8 + (f5 * Math.max(f13, f72))) * f17)) - ((roundHeight + f73) * f13)) - AndroidUtilities.dpf2(4.3f);
            float dpf24 = (timelineView.h - timelineView.py) + AndroidUtilities.dpf2(4.3f);
            if (f89 > 0.0f) {
                long j35 = timelineView.loopProgressFrom;
                if (j35 == -1) {
                    if (timelineView.hasVideo) {
                        f18 = (float) timelineView.videoDuration;
                        f19 = timelineView.videoRight;
                    } else if (timelineView.hasRound) {
                        f18 = (float) timelineView.roundDuration;
                        f19 = timelineView.roundRight;
                    } else {
                        f18 = (float) timelineView.audioDuration;
                        f19 = timelineView.audioRight;
                    }
                    j35 = f18 * f19;
                }
                drawProgress(canvas, max7, dpf24, j35, f89 * f12);
            }
            drawProgress(canvas, max7, dpf24, timelineView.progress, (1.0f - f89) * f12);
        } else {
            j4 = j32;
        }
        if (timelineView.dragged) {
            long dp3 = (AndroidUtilities.dp(32.0f) / timelineView.sw) * f84 * (1.0f / (1000.0f / AndroidUtilities.screenRefreshRate));
            int i38 = timelineView.pressHandle;
            int i39 = -1;
            if (i38 == 4) {
                float f90 = timelineView.videoLeft;
                long j36 = timelineView.scroll;
                long j37 = timelineView.videoDuration;
                if (f90 >= ((float) j36) / ((float) j37)) {
                    i39 = timelineView.videoRight > ((float) (j36 + j4)) / ((float) j37) ? 1 : 0;
                }
                long j38 = i39 * dp3;
                long clamp = Utilities.clamp(j36 + j38, j37 - j4, 0L);
                timelineView.scroll = clamp;
                timelineView.progress += j38;
                float f91 = ((float) (clamp - j36)) / ((float) timelineView.videoDuration);
                if (f91 > 0.0f) {
                    f21 = 1.0f;
                    max2 = Math.min(1.0f - timelineView.videoRight, f91);
                } else {
                    f21 = 1.0f;
                    max2 = Math.max(0.0f - timelineView.videoLeft, f91);
                }
                timelineView.videoLeft = Utilities.clamp(timelineView.videoLeft + max2, f21, 0.0f);
                timelineView.videoRight = Utilities.clamp(timelineView.videoRight + max2, f21, 0.0f);
                TimelineDelegate timelineDelegate = timelineView.delegate;
                if (timelineDelegate != null) {
                    timelineDelegate.onVideoLeftChange(timelineView.videoLeft);
                    timelineView.delegate.onVideoRightChange(timelineView.videoRight);
                }
                invalidate();
            } else if (i38 == 8) {
                float f92 = timelineView.audioLeft;
                long j39 = timelineView.audioOffset;
                float f93 = (float) ((-j39) + 100);
                long j40 = timelineView.audioDuration;
                if (f92 >= f93 / ((float) j40)) {
                    i39 = timelineView.audioRight >= ((float) (((-j39) + j4) - 100)) / ((float) j40) ? 1 : 0;
                }
                if (i39 != 0) {
                    if (timelineView.audioSelected && timelineView.hasVideo) {
                        long j41 = j39 - (i39 * dp3);
                        float f94 = timelineView.videoRight;
                        long j42 = timelineView.videoDuration;
                        timelineView.audioOffset = Utilities.clamp(j41, (f94 * ((float) j42)) - (f92 * ((float) j40)), (timelineView.videoLeft * ((float) j42)) - (timelineView.audioRight * ((float) j40)));
                    } else if (timelineView.roundSelected && timelineView.hasRound) {
                        long j43 = j39 - (i39 * dp3);
                        float f95 = timelineView.roundRight;
                        long j44 = timelineView.roundDuration;
                        timelineView.audioOffset = Utilities.clamp(j43, (f95 * ((float) j44)) - (f92 * ((float) j40)), (timelineView.roundLeft * ((float) j44)) - (timelineView.audioRight * ((float) j40)));
                    } else {
                        timelineView.audioOffset = Utilities.clamp(j39 - (i39 * dp3), 0L, -(j40 - Math.min(getBaseDuration(), 120000L)));
                    }
                    float f96 = ((float) (-(timelineView.audioOffset - j39))) / ((float) timelineView.audioDuration);
                    if (f96 > 0.0f) {
                        max = Math.min(1.0f - timelineView.audioRight, f96);
                    } else {
                        max = Math.max(0.0f - timelineView.audioLeft, f96);
                    }
                    if (timelineView.hasVideo) {
                        f20 = 0.0f;
                    } else {
                        long j45 = timelineView.audioDuration;
                        f20 = 0.0f;
                        timelineView.progress = Utilities.clamp(((float) timelineView.progress) + (((float) j45) * max), (float) j45, 0.0f);
                    }
                    timelineView.audioLeft = Utilities.clamp(timelineView.audioLeft + max, 1.0f, f20);
                    timelineView.audioRight = Utilities.clamp(timelineView.audioRight + max, 1.0f, f20);
                    TimelineDelegate timelineDelegate2 = timelineView.delegate;
                    if (timelineDelegate2 != null) {
                        timelineDelegate2.onAudioLeftChange(timelineView.audioLeft);
                        timelineView.delegate.onAudioRightChange(timelineView.audioRight);
                        timelineView.delegate.onProgressChange(timelineView.progress, false);
                    }
                    invalidate();
                }
            }
        }
    }

    private void drawRegion(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, float f5) {
        if (f5 <= 0.0f) {
            return;
        }
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(f3 - AndroidUtilities.dp(10.0f), f, f4 + AndroidUtilities.dp(10.0f), f2);
        canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, 255, 31);
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
        this.regionHandlePaint.setAlpha(255);
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
            setupVideoThumbs();
        }
        if (this.audioPath == null || this.waveform != null) {
            return;
        }
        setupAudioWaveform();
    }

    /* loaded from: classes4.dex */
    public class VideoThumbsLoader {
        private final Paint bitmapPaint;
        private Path clipPath;
        private final int count;
        private boolean destroyed;
        private long duration;
        private final int frameHeight;
        private final long frameIterator;
        private final int frameWidth;
        private final ArrayList<BitmapFrame> frames;
        private final boolean isRound;
        private boolean loading;
        private MediaMetadataRetriever metadataRetriever;
        private long nextFrame;

        public VideoThumbsLoader(TimelineView timelineView, boolean z, String str, int i, int i2, Long l) {
            this(z, str, i, i2, l, 120000L);
        }

        /* JADX WARN: Code restructure failed: missing block: B:65:0x0068, code lost:
            if (r7 != 270) goto L24;
         */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0081  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x008b A[ADDED_TO_REGION] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public VideoThumbsLoader(boolean z, String str, int i, int i2, Long l, long j) {
            Exception e;
            int i3;
            float f;
            int max;
            String extractMetadata;
            TimelineView.this = r5;
            this.frames = new ArrayList<>();
            this.loading = false;
            this.bitmapPaint = new Paint(3);
            this.isRound = z;
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.metadataRetriever = mediaMetadataRetriever;
            long j2 = 120000;
            try {
                mediaMetadataRetriever.setDataSource(str);
                String extractMetadata2 = this.metadataRetriever.extractMetadata(9);
                if (extractMetadata2 != null) {
                    j2 = Long.parseLong(extractMetadata2);
                    this.duration = j2;
                }
                String extractMetadata3 = this.metadataRetriever.extractMetadata(18);
                i3 = extractMetadata3 != null ? Integer.parseInt(extractMetadata3) : 0;
                try {
                    String extractMetadata4 = this.metadataRetriever.extractMetadata(19);
                    r5 = extractMetadata4 != null ? Integer.parseInt(extractMetadata4) : 0;
                } catch (Exception e2) {
                    e = e2;
                    r5 = i3;
                    i3 = 0;
                    this.metadataRetriever = null;
                    FileLog.e(e);
                    int i4 = i3;
                    i3 = r5;
                    r5 = i4;
                    if (l != null) {
                    }
                    f = 1.0f;
                    if (i3 != 0) {
                        f = i3 / r5;
                    }
                    float clamp = Utilities.clamp(f, 1.3333334f, 0.5625f);
                    this.frameHeight = Math.max(1, i2);
                    this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp));
                    int ceil = (int) Math.ceil(((((float) Math.max(j2, j)) / ((float) j)) * i) / max);
                    this.count = ceil;
                    long j3 = ((float) j2) / ceil;
                    this.frameIterator = j3;
                    this.nextFrame = -j3;
                    load();
                }
                try {
                    extractMetadata = this.metadataRetriever.extractMetadata(24);
                } catch (Exception e3) {
                    e = e3;
                    int i5 = i3;
                    i3 = r5;
                    r5 = i5;
                    this.metadataRetriever = null;
                    FileLog.e(e);
                    int i42 = i3;
                    i3 = r5;
                    r5 = i42;
                    if (l != null) {
                    }
                    f = 1.0f;
                    if (i3 != 0) {
                    }
                    float clamp2 = Utilities.clamp(f, 1.3333334f, 0.5625f);
                    this.frameHeight = Math.max(1, i2);
                    this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp2));
                    int ceil2 = (int) Math.ceil(((((float) Math.max(j2, j)) / ((float) j)) * i) / max);
                    this.count = ceil2;
                    long j32 = ((float) j2) / ceil2;
                    this.frameIterator = j32;
                    this.nextFrame = -j32;
                    load();
                }
            } catch (Exception e4) {
                e = e4;
            }
            if (extractMetadata != null) {
                int parseInt = Integer.parseInt(extractMetadata);
                if (parseInt != 90) {
                }
                int i422 = i3;
                i3 = r5;
                r5 = i422;
            }
            if (l != null) {
                j2 = l.longValue();
                this.duration = j2;
            }
            f = 1.0f;
            if (i3 != 0 && r5 != 0) {
                f = i3 / r5;
            }
            float clamp22 = Utilities.clamp(f, 1.3333334f, 0.5625f);
            this.frameHeight = Math.max(1, i2);
            this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp22));
            int ceil22 = (int) Math.ceil(((((float) Math.max(j2, j)) / ((float) j)) * i) / max);
            this.count = ceil22;
            long j322 = ((float) j2) / ceil22;
            this.frameIterator = j322;
            this.nextFrame = -j322;
            load();
        }

        public int getFrameWidth() {
            return this.frameWidth;
        }

        public long getDuration() {
            return this.duration;
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

        /* renamed from: receiveFrame */
        public void lambda$retrieveFrame$0(Bitmap bitmap) {
            if (!this.loading || this.destroyed) {
                return;
            }
            this.frames.add(new BitmapFrame(bitmap));
            this.loading = false;
            TimelineView.this.invalidate();
        }

        public void destroy() {
            this.destroyed = true;
            Utilities.themeQueue.cancelRunnable(new TimelineView$VideoThumbsLoader$$ExternalSyntheticLambda0(this));
            Iterator<BitmapFrame> it = this.frames.iterator();
            while (it.hasNext()) {
                Bitmap bitmap = it.next().bitmap;
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
    }

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
                this.waveformLoader = new FfmpegAudioWaveformLoader(str, round, new Utilities.Callback2() { // from class: org.telegram.ui.Stories.recorder.TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        TimelineView.AudioWaveformLoader.this.lambda$run$0((short[]) obj, ((Integer) obj2).intValue());
                    }
                });
            } else {
                Utilities.phoneBookQueue.postRunnable(new TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda0(this));
            }
        }

        public void run() {
            MediaCodec.BufferInfo bufferInfo;
            int i;
            int i2;
            ByteBuffer outputBuffer;
            int i3;
            boolean z;
            short s;
            ByteBuffer outputBuffer2;
            ByteBuffer inputBuffer;
            try {
                int round = Math.round((((float) (this.duration * this.inputFormat.getInteger("sample-rate"))) / this.count) / 5.0f);
                MediaCodec createDecoderByType = MediaCodec.createDecoderByType(this.inputFormat.getString("mime"));
                if (createDecoderByType == null) {
                    return;
                }
                int i4 = 0;
                createDecoderByType.configure(this.inputFormat, (Surface) null, (MediaCrypto) null, 0);
                createDecoderByType.start();
                ByteBuffer[] inputBuffers = createDecoderByType.getInputBuffers();
                ByteBuffer[] outputBuffers = createDecoderByType.getOutputBuffers();
                final short[] sArr = new short[32];
                int i5 = -1;
                boolean z2 = false;
                int i6 = 0;
                int i7 = 0;
                int i8 = 0;
                short s2 = 0;
                while (true) {
                    MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                    int dequeueInputBuffer = createDecoderByType.dequeueInputBuffer(2500L);
                    if (dequeueInputBuffer >= 0) {
                        if (Build.VERSION.SDK_INT < 21) {
                            inputBuffer = inputBuffers[dequeueInputBuffer];
                        } else {
                            inputBuffer = createDecoderByType.getInputBuffer(dequeueInputBuffer);
                        }
                        int readSampleData = this.extractor.readSampleData(inputBuffer, i4);
                        if (readSampleData < 0) {
                            bufferInfo = bufferInfo2;
                            i = i5;
                            createDecoderByType.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                            z2 = true;
                        } else {
                            bufferInfo = bufferInfo2;
                            i = i5;
                            createDecoderByType.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, this.extractor.getSampleTime(), 0);
                            this.extractor.advance();
                        }
                    } else {
                        bufferInfo = bufferInfo2;
                        i = i5;
                    }
                    if (i >= 0) {
                        if (Build.VERSION.SDK_INT < 21) {
                            outputBuffer2 = outputBuffers[i];
                        } else {
                            outputBuffer2 = createDecoderByType.getOutputBuffer(i);
                        }
                        outputBuffer2.position(0);
                    }
                    i5 = createDecoderByType.dequeueOutputBuffer(bufferInfo, 2500L);
                    while (i5 != -1 && !z2) {
                        if (i5 >= 0) {
                            if (Build.VERSION.SDK_INT < 21) {
                                outputBuffer = outputBuffers[i5];
                            } else {
                                outputBuffer = createDecoderByType.getOutputBuffer(i5);
                            }
                            if (outputBuffer == null || bufferInfo.size <= 0) {
                                i3 = i8;
                            } else {
                                int i9 = i8;
                                while (outputBuffer.remaining() > 0) {
                                    short s3 = (short) (((outputBuffer.get() & 255) << 8) | (outputBuffer.get() & 255));
                                    if (i9 >= round) {
                                        sArr[i6 - i7] = s2;
                                        int i10 = i6 + 1;
                                        final int i11 = i10 - i7;
                                        if (i11 >= sArr.length || i10 >= this.count) {
                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$AudioWaveformLoader$$ExternalSyntheticLambda1
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    TimelineView.AudioWaveformLoader.this.lambda$run$0(sArr, i11);
                                                }
                                            });
                                            sArr = new short[sArr.length];
                                            i7 = i10;
                                        }
                                        i6 = i10;
                                        if (i10 >= this.data.length) {
                                            z = false;
                                            i3 = 0;
                                            s2 = 0;
                                            break;
                                        }
                                        s = 0;
                                        i9 = 0;
                                    } else {
                                        s = s2;
                                    }
                                    s2 = s < s3 ? s3 : s;
                                    i9++;
                                    if (outputBuffer.remaining() < 8) {
                                        break;
                                    }
                                    outputBuffer.position(outputBuffer.position() + 8);
                                }
                                i3 = i9;
                            }
                            z = false;
                            createDecoderByType.releaseOutputBuffer(i5, z);
                            if ((bufferInfo.flags & 4) != 0) {
                                i8 = i3;
                                i2 = i6;
                                z2 = true;
                                break;
                            }
                            i8 = i3;
                        } else if (i5 == -3) {
                            outputBuffers = createDecoderByType.getOutputBuffers();
                        }
                        i5 = createDecoderByType.dequeueOutputBuffer(bufferInfo, 2500L);
                    }
                    i2 = i6;
                    synchronized (this.lock) {
                        if (!this.stop) {
                            if (z2 || i2 >= this.count) {
                                break;
                            }
                            i6 = i2;
                            i4 = 0;
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

        /* renamed from: receiveData */
        public void lambda$run$0(short[] sArr, int i) {
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = this.loaded;
                int i4 = i3 + i2;
                short[] sArr2 = this.data;
                if (i4 >= sArr2.length) {
                    break;
                }
                sArr2[i3 + i2] = sArr[i2];
                if (this.max < sArr[i2]) {
                    this.max = sArr[i2];
                }
            }
            this.loaded += i;
            TimelineView.this.invalidate();
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

        public short getMaxBar() {
            return this.max;
        }

        public short getBar(int i) {
            return this.data[i];
        }

        public int getLoadedCount() {
            return this.loaded;
        }

        public int getCount() {
            return this.count;
        }
    }

    public int getContentHeight() {
        return (int) (this.py + (this.hasVideo ? getVideoHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + (this.hasRound ? getRoundHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + (this.hasAudio ? getAudioHeight() + AndroidUtilities.dp(4.0f) : 0.0f) + this.py);
    }
}
