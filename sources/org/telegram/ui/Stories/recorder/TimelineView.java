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
    private boolean hasVideo;
    private long lastTime;
    private float lastX;
    private final AnimatedFloat loopProgress;
    private final Runnable onLongPress;
    private int ph;
    private int pressHandle;
    private long pressTime;
    private boolean pressVideo;
    private long progress;
    private final Paint progressShadowPaint;
    private final Paint progressWhitePaint;
    private int px;
    private int py;
    private final Paint regionCutPaint;
    private final Paint regionHandlePaint;
    private final Paint regionPaint;
    private final Theme.ResourcesProvider resourcesProvider;
    private long scroll;
    private final Scroller scroller;
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
    private int w;
    private AudioWaveformLoader waveform;
    private boolean waveformIsLoaded;
    private final AnimatedFloat waveformLoaded;
    private final AnimatedFloat waveformMax;
    private final Paint waveformPaint;
    private final Path waveformPath;
    final float[] waveformRadii;

    /* loaded from: classes4.dex */
    public interface TimelineDelegate {
        void onAudioLeftChange(float f);

        void onAudioOffsetChange(long j);

        void onAudioRemove();

        void onAudioRightChange(float f);

        void onAudioVolumeChange(float f);

        void onProgressChange(long j, boolean z);

        void onProgressDragChange(boolean z);

        void onVideoLeftChange(float f);

        void onVideoRightChange(float f);
    }

    private long getBaseDuration() {
        if (this.hasVideo) {
            return this.videoDuration;
        }
        if (this.hasAudio) {
            return this.audioDuration;
        }
        return Math.max(1L, this.audioDuration);
    }

    public TimelineView(Context context, final ViewGroup viewGroup, final View view, final Theme.ResourcesProvider resourcesProvider, final BlurringShader.BlurManager blurManager) {
        super(context);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.audioT = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.audioSelectedT = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.waveformLoaded = new AnimatedFloat(this, 0L, 600L, cubicBezierInterpolator);
        this.waveformMax = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        this.videoBounds = new RectF();
        this.videoFramePaint = new Paint(3);
        this.videoClipPath = new Path();
        this.selectedVideoClipPath = new Path();
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
        this.waveformPath = new Path();
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
        this.loopProgress = new AnimatedFloat(0.0f, this, 0L, 200L, CubicBezierInterpolator.EASE_BOTH);
        this.pressHandle = -1;
        this.pressVideo = true;
        this.scrollingVideo = true;
        this.selectedVideoRadii = new float[8];
        this.waveformRadii = new float[8];
        this.resourcesProvider = resourcesProvider;
        paint7.setColor(ConnectionsManager.DEFAULT_DATACENTER_ID);
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
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
        this.onLongPress = new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$new$2(viewGroup, resourcesProvider, blurManager, view);
            }
        };
    }

    public /* synthetic */ void lambda$new$2(ViewGroup viewGroup, Theme.ResourcesProvider resourcesProvider, BlurringShader.BlurManager blurManager, View view) {
        int i;
        if (this.pressVideo || !this.hasAudio) {
            return;
        }
        VolumeSliderView onValueChange = new VolumeSliderView(getContext()).setVolume(this.audioVolume).setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                TimelineView.this.lambda$new$0((Float) obj);
            }
        });
        long min = Math.min(getBaseDuration(), 120000L);
        int i2 = this.w;
        int i3 = this.px;
        int i4 = this.ph;
        float min2 = Math.min((i2 - i3) - i4, i3 + i4 + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioRight, 1.0f, this.audioSelectedT.get()) * ((float) this.audioDuration))) / ((float) min)) * this.sw));
        ItemOptions forceTop = ItemOptions.makeOptions(viewGroup, resourcesProvider, this).addView(onValueChange).addSpaceGap().add(R.drawable.msg_delete, LocaleController.getString(R.string.StoryAudioRemove), new Runnable() { // from class: org.telegram.ui.Stories.recorder.TimelineView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimelineView.this.lambda$new$1();
            }
        }).setGravity(5).forceTop(true);
        float dp = AndroidUtilities.dp(6.0f) - (this.w - min2);
        int dp2 = AndroidUtilities.dp(4.0f);
        if (this.hasVideo) {
            i = 0;
        } else {
            i = AndroidUtilities.dp(this.audioSelected ? 35.0f : 40.0f);
        }
        forceTop.translate(dp, dp2 + i).show().setBlurBackground(blurManager, -view.getX(), -view.getY());
        try {
            performHapticFeedback(0, 1);
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

    public void setDelegate(TimelineDelegate timelineDelegate) {
        this.delegate = timelineDelegate;
    }

    public void setVideo(String str, long j) {
        if (TextUtils.equals(this.videoPath, str)) {
            return;
        }
        VideoThumbsLoader videoThumbsLoader = this.thumbs;
        if (videoThumbsLoader != null) {
            videoThumbsLoader.destroy();
            this.thumbs = null;
        }
        if (str != null) {
            this.scroll = 0L;
            this.videoPath = str;
            this.videoDuration = j;
            setupVideoThumbs();
        } else {
            this.videoPath = null;
            this.videoDuration = 1L;
            this.scroll = 0L;
        }
        this.hasVideo = this.videoPath != null;
        this.progress = 0L;
        invalidate();
    }

    private void setupVideoThumbs() {
        if (getMeasuredWidth() <= 0 || this.thumbs != null) {
            return;
        }
        VideoThumbsLoader videoThumbsLoader = new VideoThumbsLoader(this.videoPath, (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), AndroidUtilities.dp(38.0f));
        this.thumbs = videoThumbsLoader;
        if (videoThumbsLoader.getDuration() > 0) {
            this.videoDuration = this.thumbs.getDuration();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0024, code lost:
        if (((float) (r4 + 240)) >= (((float) r6) * r10.videoRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0046, code lost:
        if (((float) (r4 + 240)) >= (((float) r6) * r10.audioRight)) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0048, code lost:
        r10.loopProgress.set(1.0f, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setProgress(long j) {
        if (this.hasVideo) {
            long j2 = this.progress;
            if (j < j2) {
                long j3 = this.videoDuration;
                if (((float) j) <= (((float) j3) * this.videoLeft) + 240.0f) {
                }
            }
        }
        if (this.hasAudio) {
            long j4 = this.progress;
            if (j < j4) {
                long j5 = this.audioDuration;
                if (((float) j) <= (((float) j5) * this.audioLeft) + 240.0f) {
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
        this.audioPath = str;
        AudioWaveformLoader audioWaveformLoader = this.waveform;
        if (audioWaveformLoader != null) {
            audioWaveformLoader.destroy();
            this.waveform = null;
            this.waveformIsLoaded = false;
            this.waveformLoaded.set(0.0f, true);
        }
        setupAudioWaveform();
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
        if (getMeasuredWidth() <= 0 || this.waveform != null) {
            return;
        }
        this.waveform = new AudioWaveformLoader(this.audioPath, (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
        this.waveformIsLoaded = false;
        this.waveformLoaded.set(0.0f, true);
        this.waveformMax.set(1.0f, true);
    }

    private int detectHandle(MotionEvent motionEvent) {
        float f;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float min = (float) Math.min(getBaseDuration(), 120000L);
        float clamp = this.px + this.ph + (this.sw * (((float) ((Utilities.clamp(this.progress, getBaseDuration(), 0L) + (!this.hasVideo ? this.audioOffset : 0L)) - this.scroll)) / min));
        if (x < clamp - AndroidUtilities.dp(12.0f) || x > clamp + AndroidUtilities.dp(12.0f)) {
            boolean z = y > (((float) (this.h - this.py)) - getVideoHeight()) - ((float) AndroidUtilities.dp(2.0f));
            if (z) {
                int i = this.px;
                int i2 = this.ph;
                float f2 = this.videoLeft;
                long j = this.videoDuration;
                long j2 = this.scroll;
                int i3 = this.sw;
                float f3 = i + i2 + ((((f2 * ((float) j)) - ((float) j2)) / min) * i3);
                float f4 = i + i2 + ((((this.videoRight * ((float) j)) - ((float) j2)) / min) * i3);
                if (x >= f3 - AndroidUtilities.dp(15.0f) && x <= AndroidUtilities.dp(5.0f) + f3) {
                    return 2;
                }
                if (x >= f4 - AndroidUtilities.dp(5.0f) && x <= AndroidUtilities.dp(15.0f) + f4) {
                    return 3;
                }
                if (x >= f3 && x <= f4 && (this.videoLeft > 0.01f || this.videoRight < 0.99f)) {
                    return 4;
                }
            } else if (this.hasAudio) {
                int i4 = this.px;
                int i5 = this.ph;
                long j3 = this.audioOffset;
                float f5 = this.audioLeft;
                long j4 = this.audioDuration;
                float f6 = ((float) j3) + (f5 * ((float) j4));
                long j5 = this.scroll;
                int i6 = this.sw;
                float f7 = i4 + i5 + (((f6 - ((float) j5)) / min) * i6);
                float f8 = i4 + i5 + ((((((float) j3) + (this.audioRight * ((float) j4))) - ((float) j5)) / min) * i6);
                if (this.audioSelected || !this.hasVideo) {
                    if (x >= f7 - AndroidUtilities.dp(15.0f)) {
                        f = 5.0f;
                        if (x <= AndroidUtilities.dp(5.0f) + f7) {
                            return 6;
                        }
                    } else {
                        f = 5.0f;
                    }
                    if (x >= f8 - AndroidUtilities.dp(f) && x <= AndroidUtilities.dp(15.0f) + f8) {
                        return 7;
                    }
                    if (x >= f7 && x <= f8) {
                        return (this.audioLeft > (((float) Math.max(0L, this.scroll - this.audioOffset)) / ((float) this.audioDuration)) + 0.01f || this.audioRight < Math.min(1.0f, Math.max(0.0f, ((float) (this.scroll - this.audioOffset)) + ((float) Math.min(120000L, getBaseDuration()))) / ((float) this.audioDuration)) - 0.01f) ? 8 : 5;
                    }
                    int i7 = this.px;
                    int i8 = this.ph;
                    long j6 = this.audioOffset;
                    long j7 = this.scroll;
                    int i9 = this.sw;
                    f7 = i7 + i8 + ((((float) (j6 - j7)) / min) * i9);
                    f8 = i7 + i8 + ((((float) ((j6 + this.audioDuration) - j7)) / min) * i9);
                }
                if (x >= f7 && x <= f8) {
                    return 5;
                }
            }
            return (this.videoDuration <= 120000 || !z) ? -1 : 1;
        }
        return 0;
    }

    public boolean onBackPressed() {
        if (this.audioSelected) {
            this.audioSelected = false;
            return true;
        }
        return false;
    }

    public boolean isDragging() {
        return this.dragged;
    }

    private void setProgressAt(float f, boolean z) {
        if (this.hasVideo || this.hasAudio) {
            float min = (((f - this.px) - this.ph) / this.sw) * ((float) Math.min(getBaseDuration(), 120000L));
            boolean z2 = this.hasVideo;
            final long clamp = Utilities.clamp(min + ((float) (!z2 ? -this.audioOffset : 0L)) + ((float) this.scroll), (float) (z2 ? this.videoDuration : this.audioDuration), 0.0f);
            boolean z3 = this.hasVideo;
            if (z3) {
                float f2 = (float) clamp;
                long j = this.videoDuration;
                if (f2 / ((float) j) < this.videoLeft || f2 / ((float) j) > this.videoRight) {
                    return;
                }
            }
            if (this.hasAudio && !z3) {
                float f3 = (float) clamp;
                long j2 = this.audioDuration;
                if (f3 / ((float) j2) < this.audioLeft || f3 / ((float) j2) > this.audioRight) {
                    return;
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
                        TimelineView.this.lambda$setProgressAt$3(clamp);
                    }
                };
                this.askExactSeek = runnable2;
                AndroidUtilities.runOnUIThread(runnable2, 150L);
            }
        }
    }

    public /* synthetic */ void lambda$setProgressAt$3(long j) {
        TimelineDelegate timelineDelegate = this.delegate;
        if (timelineDelegate != null) {
            timelineDelegate.onProgressChange(j, false);
        }
    }

    private float getVideoHeight() {
        if (this.hasVideo) {
            return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), 1.0f - this.audioSelectedT.set(this.audioSelected));
        }
        return 0.0f;
    }

    private float getAudioHeight() {
        return AndroidUtilities.lerp(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(38.0f), this.audioSelectedT.set(this.audioSelected));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        TimelineDelegate timelineDelegate;
        VelocityTracker velocityTracker;
        int i;
        int i2;
        VelocityTracker velocityTracker2;
        VelocityTracker velocityTracker3;
        float max;
        boolean z2;
        boolean z3;
        float max2;
        boolean z4;
        TimelineDelegate timelineDelegate2;
        boolean z5;
        boolean z6;
        TimelineDelegate timelineDelegate3;
        boolean z7;
        int i3;
        if (this.hasVideo || this.hasAudio) {
            long currentTimeMillis = System.currentTimeMillis();
            if (motionEvent.getAction() == 0) {
                Runnable runnable = this.askExactSeek;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.askExactSeek = null;
                }
                this.scroller.abortAnimation();
                this.pressHandle = detectHandle(motionEvent);
                if (this.hasAudio) {
                    if (motionEvent.getY() <= ((this.h - this.py) - getVideoHeight()) - (this.hasVideo ? AndroidUtilities.dp(4.0f) : 0)) {
                        z7 = false;
                        this.pressVideo = z7;
                        this.pressTime = System.currentTimeMillis();
                        i3 = this.pressHandle;
                        this.draggingProgress = i3 != 0 || i3 == -1 || i3 == 1;
                        this.hadDragChange = false;
                        if (i3 != 1 || i3 == 5 || i3 == 8) {
                            this.velocityTracker = VelocityTracker.obtain();
                        } else {
                            VelocityTracker velocityTracker4 = this.velocityTracker;
                            if (velocityTracker4 != null) {
                                velocityTracker4.recycle();
                                this.velocityTracker = null;
                            }
                        }
                        this.dragged = false;
                        this.lastX = motionEvent.getX();
                        AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                        AndroidUtilities.runOnUIThread(this.onLongPress, ViewConfiguration.getLongPressTimeout());
                    }
                }
                z7 = true;
                this.pressVideo = z7;
                this.pressTime = System.currentTimeMillis();
                i3 = this.pressHandle;
                this.draggingProgress = i3 != 0 || i3 == -1 || i3 == 1;
                this.hadDragChange = false;
                if (i3 != 1) {
                }
                this.velocityTracker = VelocityTracker.obtain();
                this.dragged = false;
                this.lastX = motionEvent.getX();
                AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                AndroidUtilities.runOnUIThread(this.onLongPress, ViewConfiguration.getLongPressTimeout());
            } else if (motionEvent.getAction() == 2) {
                float x = motionEvent.getX() - this.lastX;
                if (this.dragged || Math.abs(x) > AndroidUtilities.touchSlop) {
                    long min = Math.min(getBaseDuration(), 120000L);
                    int i4 = this.pressHandle;
                    if (i4 == 1) {
                        this.scroll = Utilities.clamp(((float) this.scroll) - ((x / this.sw) * ((float) min)), (float) (this.videoDuration - min), 0.0f);
                        invalidate();
                        this.dragged = true;
                        this.draggingProgress = false;
                    } else if (i4 == 2 || i4 == 3 || i4 == 4) {
                        long j = this.videoDuration;
                        float f = (x / this.sw) * (((float) min) / ((float) j));
                        if (i4 == 2) {
                            float clamp = Utilities.clamp(this.videoLeft + f, this.videoRight - (1000.0f / ((float) j)), 0.0f);
                            this.videoLeft = clamp;
                            TimelineDelegate timelineDelegate4 = this.delegate;
                            if (timelineDelegate4 != null) {
                                timelineDelegate4.onVideoLeftChange(clamp);
                            }
                            float f2 = this.videoRight;
                            float f3 = this.videoLeft;
                            long j2 = this.videoDuration;
                            if (f2 - f3 > 59000.0f / ((float) j2)) {
                                float min2 = Math.min(1.0f, f3 + (59000.0f / ((float) j2)));
                                this.videoRight = min2;
                                TimelineDelegate timelineDelegate5 = this.delegate;
                                if (timelineDelegate5 != null) {
                                    timelineDelegate5.onVideoRightChange(min2);
                                }
                            }
                        } else if (i4 == 3) {
                            float clamp2 = Utilities.clamp(this.videoRight + f, 1.0f, this.videoLeft + (1000.0f / ((float) j)));
                            this.videoRight = clamp2;
                            TimelineDelegate timelineDelegate6 = this.delegate;
                            if (timelineDelegate6 != null) {
                                timelineDelegate6.onVideoRightChange(clamp2);
                            }
                            float f4 = this.videoRight;
                            long j3 = this.videoDuration;
                            if (f4 - this.videoLeft > 59000.0f / ((float) j3)) {
                                float max3 = Math.max(0.0f, f4 - (59000.0f / ((float) j3)));
                                this.videoLeft = max3;
                                TimelineDelegate timelineDelegate7 = this.delegate;
                                if (timelineDelegate7 != null) {
                                    timelineDelegate7.onVideoLeftChange(max3);
                                }
                            }
                        } else if (i4 == 4) {
                            if (f > 0.0f) {
                                max = Math.min(1.0f - this.videoRight, f);
                            } else {
                                max = Math.max(-this.videoLeft, f);
                            }
                            float f5 = this.videoLeft + max;
                            this.videoLeft = f5;
                            this.videoRight += max;
                            TimelineDelegate timelineDelegate8 = this.delegate;
                            if (timelineDelegate8 != null) {
                                timelineDelegate8.onVideoLeftChange(f5);
                                this.delegate.onVideoRightChange(this.videoRight);
                            }
                        }
                        long j4 = this.progress;
                        long j5 = this.videoDuration;
                        float f6 = ((float) j4) / ((float) j5);
                        float f7 = this.videoLeft;
                        if (f6 < f7 || ((float) j4) / ((float) j5) > this.videoRight) {
                            long j6 = f7 * ((float) j5);
                            this.progress = j6;
                            TimelineDelegate timelineDelegate9 = this.delegate;
                            if (timelineDelegate9 != null) {
                                z2 = false;
                                timelineDelegate9.onProgressChange(j6, false);
                                invalidate();
                                this.dragged = true;
                                this.draggingProgress = z2;
                            }
                        }
                        z2 = false;
                        invalidate();
                        this.dragged = true;
                        this.draggingProgress = z2;
                    } else if (i4 == 6 || i4 == 7 || i4 == 8) {
                        long j7 = this.audioDuration;
                        float f8 = (x / this.sw) * (((float) min) / ((float) j7));
                        if (i4 == 6) {
                            float f9 = this.audioRight - (1000.0f / ((float) j7));
                            float max4 = (float) Math.max(0L, this.scroll - this.audioOffset);
                            long j8 = this.audioDuration;
                            float f10 = max4 / ((float) j8);
                            if (!this.hasVideo) {
                                f10 = Math.max(f10, this.audioRight - (59000.0f / ((float) j8)));
                                if (!this.hadDragChange && f8 < 0.0f && this.audioLeft <= this.audioRight - (59000.0f / ((float) this.audioDuration))) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f11 = this.audioLeft;
                            float clamp3 = Utilities.clamp(f11 + f8, f9, f10);
                            this.audioLeft = clamp3;
                            if (Math.abs(f11 - clamp3) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate10 = this.delegate;
                            if (timelineDelegate10 != null) {
                                timelineDelegate10.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                            }
                            TimelineDelegate timelineDelegate11 = this.delegate;
                            if (timelineDelegate11 != null) {
                                timelineDelegate11.onAudioLeftChange(this.audioLeft);
                            }
                        } else if (i4 == 7) {
                            float min3 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min)) / ((float) this.audioDuration));
                            float f12 = this.audioLeft;
                            long j9 = this.audioDuration;
                            float f13 = f12 + (1000.0f / ((float) j9));
                            if (!this.hasVideo) {
                                min3 = Math.min(min3, f12 + (59000.0f / ((float) j9)));
                                if (!this.hadDragChange && f8 > 0.0f && this.audioRight >= this.audioLeft + (59000.0f / ((float) this.audioDuration))) {
                                    this.pressHandle = 8;
                                }
                            }
                            float f14 = this.audioRight;
                            float clamp4 = Utilities.clamp(f14 + f8, min3, f13);
                            this.audioRight = clamp4;
                            if (Math.abs(f14 - clamp4) > 0.01f) {
                                this.hadDragChange = true;
                            }
                            TimelineDelegate timelineDelegate12 = this.delegate;
                            if (timelineDelegate12 != null) {
                                timelineDelegate12.onAudioRightChange(this.audioRight);
                            }
                        }
                        if (this.pressHandle == 8) {
                            float max5 = ((float) Math.max(0L, this.scroll - this.audioOffset)) / ((float) this.audioDuration);
                            float min4 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min)) / ((float) this.audioDuration));
                            if (f8 > 0.0f) {
                                max2 = Math.min(min4 - this.audioRight, f8);
                            } else {
                                max2 = Math.max(max5 - this.audioLeft, f8);
                            }
                            float f15 = this.audioLeft + max2;
                            this.audioLeft = f15;
                            this.audioRight += max2;
                            TimelineDelegate timelineDelegate13 = this.delegate;
                            if (timelineDelegate13 != null) {
                                timelineDelegate13.onAudioLeftChange(f15);
                                this.delegate.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                                this.delegate.onAudioRightChange(this.audioRight);
                            }
                        }
                        if (!this.hasVideo) {
                            long j10 = this.progress;
                            long j11 = this.audioDuration;
                            float f16 = ((float) j10) / ((float) j11);
                            float f17 = this.audioLeft;
                            if (f16 < f17 || ((float) j10) / ((float) j11) > this.audioRight) {
                                long j12 = f17 * ((float) j11);
                                this.progress = j12;
                                TimelineDelegate timelineDelegate14 = this.delegate;
                                if (timelineDelegate14 != null) {
                                    z3 = false;
                                    timelineDelegate14.onProgressChange(j12, false);
                                    invalidate();
                                    this.dragged = true;
                                    this.draggingProgress = z3;
                                }
                            }
                        }
                        z3 = false;
                        invalidate();
                        this.dragged = true;
                        this.draggingProgress = z3;
                    } else if (i4 == 5) {
                        float f18 = (x / this.sw) * ((float) min);
                        if (!this.hasVideo) {
                            this.audioOffset = Utilities.clamp(this.audioOffset + f18, 0L, -(this.audioDuration - Math.min(getBaseDuration(), 120000L)));
                        } else if (this.audioSelected) {
                            this.audioOffset = Utilities.clamp(this.audioOffset + f18, ((float) getBaseDuration()) - (((float) this.audioDuration) * this.audioRight), Math.min(getBaseDuration(), this.audioDuration) - Math.max(getBaseDuration(), this.audioDuration));
                        } else {
                            long j13 = this.audioOffset + f18;
                            float baseDuration = (float) getBaseDuration();
                            long j14 = this.audioDuration;
                            this.audioOffset = Utilities.clamp(j13, baseDuration - (((float) j14) * this.audioRight), (-this.audioLeft) * ((float) j14));
                        }
                        float max6 = ((float) Math.max(0L, this.scroll - this.audioOffset)) / ((float) this.audioDuration);
                        float min5 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min)) / ((float) this.audioDuration));
                        float f19 = this.audioRight;
                        float f20 = this.audioLeft;
                        float f21 = f19 - f20;
                        if (f20 < max6) {
                            this.audioLeft = max6;
                            this.audioRight = Math.min(1.0f, max6 + f21);
                            z5 = true;
                        } else {
                            z5 = false;
                        }
                        if (this.audioRight > min5) {
                            this.audioRight = min5;
                            this.audioLeft = Math.max(0.0f, min5 - f21);
                            z5 = true;
                        }
                        TimelineDelegate timelineDelegate15 = this.delegate;
                        if (timelineDelegate15 != null && z5) {
                            timelineDelegate15.onAudioLeftChange(this.audioLeft);
                            this.delegate.onAudioRightChange(this.audioRight);
                        }
                        if (!this.hasVideo) {
                            long j15 = this.progress;
                            long j16 = this.audioDuration;
                            float f22 = ((float) j15) / ((float) j16);
                            float f23 = this.audioLeft;
                            if (f22 < f23 || ((float) j15) / ((float) j16) > this.audioRight) {
                                long j17 = f23 * ((float) j16);
                                this.progress = j17;
                                TimelineDelegate timelineDelegate16 = this.delegate;
                                if (timelineDelegate16 != null) {
                                    timelineDelegate16.onProgressChange(j17, false);
                                }
                            }
                        }
                        invalidate();
                        TimelineDelegate timelineDelegate17 = this.delegate;
                        if (timelineDelegate17 != null) {
                            timelineDelegate17.onAudioOffsetChange(this.audioOffset + (this.audioLeft * ((float) this.audioDuration)));
                        }
                        if (this.dragged || (timelineDelegate3 = this.delegate) == null) {
                            z6 = true;
                        } else {
                            z6 = true;
                            timelineDelegate3.onProgressDragChange(true);
                        }
                        this.dragged = z6;
                        this.draggingProgress = false;
                    } else if (this.draggingProgress) {
                        setProgressAt(motionEvent.getX(), currentTimeMillis - this.lastTime < 350);
                        if (this.dragged || (timelineDelegate2 = this.delegate) == null) {
                            z4 = true;
                        } else {
                            z4 = true;
                            timelineDelegate2.onProgressDragChange(true);
                        }
                        this.dragged = z4;
                    }
                    this.lastX = motionEvent.getX();
                }
                if (this.dragged) {
                    AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                }
                int i5 = this.pressHandle;
                if ((i5 == 1 || i5 == 5 || i5 == 8) && (velocityTracker3 = this.velocityTracker) != null) {
                    velocityTracker3.addMovement(motionEvent);
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                AndroidUtilities.cancelRunOnUIThread(this.onLongPress);
                this.scroller.abortAnimation();
                if (motionEvent.getAction() == 1) {
                    if (System.currentTimeMillis() - this.pressTime <= ViewConfiguration.getTapTimeout() && !this.dragged) {
                        boolean z8 = this.pressVideo;
                        if (!z8 && !this.audioSelected) {
                            this.audioSelected = true;
                            invalidate();
                        } else if (z8 && this.audioSelected) {
                            this.audioSelected = false;
                            invalidate();
                        } else {
                            setProgressAt(motionEvent.getX(), false);
                        }
                    } else {
                        int i6 = this.pressHandle;
                        if (i6 == 1 && (velocityTracker2 = this.velocityTracker) != null) {
                            velocityTracker2.computeCurrentVelocity(1000);
                            int xVelocity = (int) this.velocityTracker.getXVelocity();
                            this.scrollingVideo = true;
                            if (Math.abs(xVelocity) > AndroidUtilities.dp(100.0f)) {
                                long min6 = Math.min(this.videoDuration, 120000L);
                                int i7 = this.px;
                                float f24 = (float) min6;
                                int i8 = this.sw;
                                this.scroller.fling((int) (i7 + ((((float) this.scroll) / f24) * i8)), 0, -xVelocity, 0, i7, (int) (i7 + ((((float) (this.videoDuration - min6)) / f24) * i8)), 0, 0);
                            }
                        } else if ((i6 == 5 || (i6 == 8 && !this.dragged)) && this.hasVideo && this.audioSelected && (velocityTracker = this.velocityTracker) != null) {
                            velocityTracker.computeCurrentVelocity(1000);
                            int xVelocity2 = (int) this.velocityTracker.getXVelocity();
                            this.scrollingVideo = false;
                            if (Math.abs(xVelocity2) > AndroidUtilities.dp(100.0f)) {
                                float min7 = (float) Math.min(getBaseDuration(), 120000L);
                                int i9 = (int) (this.px + ((((float) this.audioOffset) / min7) * this.sw));
                                long max7 = Math.max(getBaseDuration(), this.audioDuration);
                                long min8 = Math.min(getBaseDuration(), this.audioDuration);
                                this.scroller.fling(i9, 0, xVelocity2, 0, (int) (i + i2 + ((((float) (min8 - max7)) / min7) * this.sw)), this.px + this.ph, 0, 0);
                            }
                        }
                    }
                }
                Runnable runnable2 = this.askExactSeek;
                if (runnable2 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable2);
                    this.askExactSeek = null;
                }
                if (!this.dragged || (timelineDelegate = this.delegate) == null) {
                    z = false;
                } else {
                    z = false;
                    timelineDelegate.onProgressDragChange(false);
                }
                this.dragged = z;
                this.draggingProgress = z;
                this.pressTime = -1L;
                this.pressHandle = -1;
                VelocityTracker velocityTracker5 = this.velocityTracker;
                if (velocityTracker5 != null) {
                    velocityTracker5.recycle();
                    this.velocityTracker = null;
                }
            }
            this.lastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            float currX = this.scroller.getCurrX();
            long min = Math.min(getBaseDuration(), 120000L);
            if (this.scrollingVideo) {
                this.scroll = Math.max(0.0f, (((currX - this.px) - this.ph) / this.sw) * ((float) min));
            } else {
                long j = (((currX - this.px) - this.ph) / this.sw) * ((float) min);
                this.audioOffset = j;
                float max = ((float) Math.max(0L, this.scroll - j)) / ((float) this.audioDuration);
                float min2 = Math.min(1.0f, ((float) Math.max(0L, (this.scroll - this.audioOffset) + min)) / ((float) this.audioDuration));
                boolean z = false;
                float f = this.audioRight;
                float f2 = this.audioLeft;
                float f3 = f - f2;
                boolean z2 = true;
                if (f2 < max) {
                    this.audioLeft = max;
                    this.audioRight = Math.min(1.0f, max + f3);
                    z = true;
                }
                if (this.audioRight > min2) {
                    this.audioRight = min2;
                    this.audioLeft = Math.max(0.0f, min2 - f3);
                } else {
                    z2 = z;
                }
                TimelineDelegate timelineDelegate = this.delegate;
                if (timelineDelegate != null && z2) {
                    timelineDelegate.onAudioLeftChange(this.audioLeft);
                    this.delegate.onAudioRightChange(this.audioRight);
                }
            }
            invalidate();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:316:0x03d0  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        float f;
        Paint paint;
        long j;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        long j2;
        double d;
        float f8;
        float f9;
        int i;
        float max;
        int i2;
        float f10;
        float max2;
        long j3;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        int i3;
        float f18;
        float f19;
        float f20;
        boolean z;
        int i4;
        Bitmap bitmap;
        Paint paint2 = this.backgroundBlur.getPaint(1.0f);
        long min = Math.min(getBaseDuration(), 120000L);
        boolean z2 = this.hasVideo;
        float f21 = z2 ? 1.0f : 0.0f;
        if (z2) {
            canvas.save();
            float videoHeight = getVideoHeight();
            long j4 = this.videoDuration;
            float f22 = j4 <= 0 ? 0.0f : (this.px + this.ph) - ((((float) this.scroll) / ((float) min)) * this.sw);
            int i5 = this.ph;
            float f23 = f22 - i5;
            float f24 = (j4 <= 0 ? 0.0f : ((((float) (j4 - this.scroll)) / ((float) min)) * this.sw) + this.px + i5) + i5;
            RectF rectF = this.videoBounds;
            int i6 = this.h;
            int i7 = this.py;
            rectF.set(f23, (i6 - i7) - videoHeight, f24, i6 - i7);
            this.videoClipPath.rewind();
            this.videoClipPath.addRoundRect(this.videoBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.clipPath(this.videoClipPath);
            VideoThumbsLoader videoThumbsLoader = this.thumbs;
            if (videoThumbsLoader != null) {
                float frameWidth = videoThumbsLoader.getFrameWidth();
                double d2 = this.thumbs.count;
                int max3 = (int) Math.max(0.0d, Math.floor((f23 - this.px) / frameWidth));
                int min2 = (int) Math.min(d2, Math.ceil(((f24 - f23) - this.px) / frameWidth) + 1.0d);
                int i8 = (int) ((this.h - this.py) - videoHeight);
                boolean z3 = this.thumbs.frames.size() >= min2;
                if (z3) {
                    for (int i9 = max3; i9 < Math.min(this.thumbs.frames.size(), min2); i9++) {
                        if (((VideoThumbsLoader.BitmapFrame) this.thumbs.frames.get(i9)).bitmap == null) {
                            z = false;
                            break;
                        }
                    }
                }
                z = z3;
                if (!z) {
                    if (paint2 == null) {
                        canvas.drawColor(1073741824);
                    } else {
                        canvas.drawRect(this.videoBounds, paint2);
                        canvas.drawColor(AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
                    }
                }
                while (max3 < Math.min(this.thumbs.frames.size(), min2)) {
                    VideoThumbsLoader.BitmapFrame bitmapFrame = (VideoThumbsLoader.BitmapFrame) this.thumbs.frames.get(max3);
                    if (bitmapFrame.bitmap != null) {
                        i4 = min2;
                        this.videoFramePaint.setAlpha((int) (bitmapFrame.getAlpha() * 255.0f));
                        canvas.drawBitmap(bitmapFrame.bitmap, f23, i8 - ((int) ((bitmap.getHeight() - videoHeight) / 2.0f)), this.videoFramePaint);
                    } else {
                        i4 = min2;
                    }
                    f23 += frameWidth;
                    max3++;
                    min2 = i4;
                }
                if (!z3) {
                    this.thumbs.load();
                }
            }
            this.selectedVideoClipPath.rewind();
            RectF rectF2 = AndroidUtilities.rectTmp;
            int i10 = this.px;
            int i11 = this.ph;
            float f25 = this.videoLeft;
            long j5 = this.videoDuration;
            f = f21;
            paint = paint2;
            long j6 = this.scroll;
            float f26 = (float) min;
            j = min;
            int i12 = this.sw;
            float f27 = ((i10 + i11) + ((((((float) j5) * f25) - ((float) j6)) / f26) * i12)) - (f25 <= 0.0f ? i11 : 0);
            int i13 = this.h;
            int i14 = this.py;
            float f28 = (i13 - i14) - videoHeight;
            f2 = videoHeight;
            float f29 = this.videoRight;
            float f30 = i10 + i11 + ((((((float) j5) * f29) - ((float) j6)) / f26) * i12);
            if (f29 < 1.0f) {
                i11 = 0;
            }
            rectF2.set(f27, f28, f30 + i11, i13 - i14);
            this.selectedVideoClipPath.addRoundRect(rectF2, this.selectedVideoRadii, Path.Direction.CW);
            canvas.clipPath(this.selectedVideoClipPath, Region.Op.DIFFERENCE);
            canvas.drawColor(Integer.MIN_VALUE);
            canvas.restore();
        } else {
            f = f21;
            paint = paint2;
            j = min;
            f2 = 0.0f;
        }
        float f31 = this.audioT.set(this.hasAudio);
        float f32 = this.audioSelectedT.set(this.hasAudio && this.audioSelected);
        float dp = AndroidUtilities.dp(4.0f);
        float audioHeight = getAudioHeight() * f31;
        if (f31 > 0.0f) {
            Paint paint3 = this.audioBlur.getPaint(f31);
            canvas.save();
            if (this.hasVideo) {
                j3 = j;
                float f33 = (float) j3;
                f14 = this.px + this.ph + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioLeft, 0.0f, f32) * ((float) this.audioDuration))) / f33) * this.sw);
                f5 = dp;
                f13 = this.px + this.ph + (((((float) (this.audioOffset - this.scroll)) + (AndroidUtilities.lerp(this.audioRight, 1.0f, f32) * ((float) this.audioDuration))) / f33) * this.sw);
                f12 = f32;
                f11 = f31;
            } else {
                f5 = dp;
                j3 = j;
                int i15 = this.px;
                int i16 = this.ph;
                long j7 = this.audioOffset;
                long j8 = this.scroll;
                f11 = f31;
                float f34 = (float) j3;
                int i17 = this.sw;
                f12 = f32;
                float f35 = i15 + i16 + ((((float) (j7 - j8)) / f34) * i17);
                f13 = ((((float) ((j7 - j8) + this.audioDuration)) / f34) * i17) + i15 + i16;
                f14 = f35;
            }
            float f36 = ((this.h - this.py) - f2) - (f5 * f);
            RectF rectF3 = this.audioBounds;
            int i18 = this.ph;
            float f37 = f36 - audioHeight;
            rectF3.set(f14 - i18, f37, i18 + f13, f36);
            this.audioClipPath.rewind();
            this.audioClipPath.addRoundRect(this.audioBounds, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), Path.Direction.CW);
            canvas.clipPath(this.audioClipPath);
            if (paint3 == null) {
                f15 = f11;
                canvas.drawColor(Theme.multAlpha(1073741824, f15));
            } else {
                f15 = f11;
                canvas.drawRect(this.audioBounds, paint3);
                canvas.drawColor(Theme.multAlpha(AndroidUtilities.DARK_STATUS_BAR_OVERLAY, f15));
            }
            if (this.waveform == null || paint3 == null) {
                f16 = f37;
                f4 = f15;
                f17 = f12;
            } else {
                Paint paint4 = this.audioWaveformBlur.getPaint(0.4f * f15);
                if (paint4 == null) {
                    paint4 = this.waveformPaint;
                    paint4.setAlpha((int) (64.0f * f15));
                }
                float f38 = this.waveformMax.set(this.waveform.getMaxBar(), !this.waveformIsLoaded);
                this.waveformIsLoaded = this.waveform.getLoadedCount() > 0;
                float f39 = this.waveformLoaded.set(this.waveform.getLoadedCount());
                float round = Math.round(AndroidUtilities.dpf2(3.3333f));
                this.waveformPath.rewind();
                int i19 = this.px;
                int i20 = this.ph;
                f16 = f37;
                f4 = f15;
                float f40 = i19 + i20 + ((((float) (this.audioOffset - this.scroll)) / ((float) j3)) * this.sw);
                int max4 = Math.max(0, (int) (((f14 - i20) - f40) / round));
                int min3 = Math.min(this.waveform.getCount() - 1, (int) Math.ceil(((this.ph + f13) - f40) / round));
                while (max4 <= min3) {
                    float f41 = max4;
                    float dp2 = (f41 * round) + f40 + AndroidUtilities.dp(2.0f);
                    float bar = f38 <= 0.0f ? 0.0f : (this.waveform.getBar(max4) / f38) * audioHeight * 0.6f;
                    if (f41 < f39) {
                        i3 = min3;
                        if (max4 + 1 > f39) {
                            bar = (f39 - f41) * bar;
                            if (dp2 >= f14 || dp2 > f13) {
                                bar *= f12;
                                if (bar <= 0.0f) {
                                    f18 = f14;
                                    f19 = f13;
                                    f20 = f12;
                                    max4++;
                                    f12 = f20;
                                    min3 = i3;
                                    f13 = f19;
                                    f14 = f18;
                                }
                            }
                            f19 = f13;
                            f20 = f12;
                            float max5 = Math.max(bar, AndroidUtilities.lerp(AndroidUtilities.dpf2(0.66f), AndroidUtilities.dpf2(1.5f), f20));
                            RectF rectF4 = AndroidUtilities.rectTmp;
                            f18 = f14;
                            rectF4.set(dp2, AndroidUtilities.lerp(f36 - max5, f36 - ((audioHeight + max5) / 2.0f), f20), AndroidUtilities.dpf2(1.66f) + dp2, AndroidUtilities.lerp(f36, f36 - ((audioHeight - max5) / 2.0f), f20));
                            this.waveformPath.addRoundRect(rectF4, this.waveformRadii, Path.Direction.CW);
                            max4++;
                            f12 = f20;
                            min3 = i3;
                            f13 = f19;
                            f14 = f18;
                        }
                    } else {
                        i3 = min3;
                    }
                    if (f41 > f39) {
                        bar = 0.0f;
                    }
                    if (dp2 >= f14) {
                    }
                    bar *= f12;
                    if (bar <= 0.0f) {
                    }
                    f19 = f13;
                    f20 = f12;
                    float max52 = Math.max(bar, AndroidUtilities.lerp(AndroidUtilities.dpf2(0.66f), AndroidUtilities.dpf2(1.5f), f20));
                    RectF rectF42 = AndroidUtilities.rectTmp;
                    f18 = f14;
                    rectF42.set(dp2, AndroidUtilities.lerp(f36 - max52, f36 - ((audioHeight + max52) / 2.0f), f20), AndroidUtilities.dpf2(1.66f) + dp2, AndroidUtilities.lerp(f36, f36 - ((audioHeight - max52) / 2.0f), f20));
                    this.waveformPath.addRoundRect(rectF42, this.waveformRadii, Path.Direction.CW);
                    max4++;
                    f12 = f20;
                    min3 = i3;
                    f13 = f19;
                    f14 = f18;
                }
                f17 = f12;
                canvas.drawPath(this.waveformPath, paint4);
            }
            if (f17 < 1.0f) {
                int i21 = this.px;
                int i22 = this.ph;
                long j9 = this.audioOffset;
                long j10 = this.scroll;
                f6 = audioHeight;
                float f42 = this.audioLeft;
                long j11 = this.audioDuration;
                float f43 = (float) j3;
                int i23 = this.sw;
                float f44 = i21 + i22 + (((((float) (j9 - j10)) + (f42 * ((float) j11))) / f43) * i23);
                float f45 = i22 + i21 + (((((float) (j9 - j10)) + (this.audioRight * ((float) j11))) / f43) * i23);
                float max6 = (Math.max(i21, f44) + Math.min(this.w - this.px, f45)) / 2.0f;
                float dp3 = f16 + AndroidUtilities.dp(14.0f);
                float max7 = Math.max(0.0f, (Math.min(this.w - this.px, f45) - Math.max(this.px, f44)) - AndroidUtilities.dp(24.0f));
                float dpf2 = AndroidUtilities.dpf2(13.0f) + ((this.audioAuthor == null && this.audioTitle == null) ? 0.0f : AndroidUtilities.dpf2(3.11f) + this.audioAuthorWidth + AndroidUtilities.dpf2(9.66f) + this.audioTitleWidth);
                boolean z4 = dpf2 < max7;
                float min4 = max6 - (Math.min(dpf2, max7) / 2.0f);
                this.audioIcon.setBounds((int) min4, (int) (dp3 - (AndroidUtilities.dp(13.0f) / 2.0f)), (int) (AndroidUtilities.dp(13.0f) + min4), (int) ((AndroidUtilities.dp(13.0f) / 2.0f) + dp3));
                float f46 = 1.0f - f17;
                float f47 = f46 * 255.0f;
                this.audioIcon.setAlpha((int) f47);
                this.audioIcon.draw(canvas);
                float dpf22 = min4 + AndroidUtilities.dpf2(16.11f);
                f3 = f17;
                d = 0.0d;
                j2 = j3;
                f7 = f;
                canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, 255, 31);
                float min5 = Math.min(f45, this.w) - AndroidUtilities.dp(12.0f);
                canvas.clipRect(dpf22, 0.0f, min5, this.h);
                if (this.audioAuthor != null) {
                    canvas.save();
                    canvas.translate(dpf22 - this.audioAuthorLeft, dp3 - (this.audioAuthor.getHeight() / 2.0f));
                    this.audioAuthorPaint.setAlpha((int) (f47 * f4));
                    this.audioAuthor.draw(canvas);
                    canvas.restore();
                    dpf22 += this.audioAuthorWidth;
                }
                if (this.audioAuthor != null && this.audioTitle != null) {
                    float dpf23 = dpf22 + AndroidUtilities.dpf2(3.66f);
                    int alpha = this.audioDotPaint.getAlpha();
                    this.audioDotPaint.setAlpha((int) (alpha * f46));
                    canvas.drawCircle(AndroidUtilities.dp(1.0f) + dpf23, dp3, AndroidUtilities.dp(1.0f), this.audioDotPaint);
                    this.audioDotPaint.setAlpha(alpha);
                    dpf22 = dpf23 + AndroidUtilities.dpf2(2.0f) + AndroidUtilities.dpf2(4.0f);
                }
                if (this.audioTitle != null) {
                    canvas.save();
                    canvas.translate(dpf22 - this.audioTitleLeft, dp3 - (this.audioTitle.getHeight() / 2.0f));
                    this.audioTitlePaint.setAlpha((int) (f47 * f4));
                    this.audioTitle.draw(canvas);
                    canvas.restore();
                }
                if (!z4) {
                    this.ellipsizeMatrix.reset();
                    this.ellipsizeMatrix.postScale(AndroidUtilities.dpf2(8.0f) / 16.0f, 1.0f);
                    this.ellipsizeMatrix.postTranslate(min5 - AndroidUtilities.dp(8.0f), 0.0f);
                    this.ellipsizeGradient.setLocalMatrix(this.ellipsizeMatrix);
                    canvas.drawRect(min5 - AndroidUtilities.dp(8.0f), f16, min5, f36, this.ellipsizePaint);
                }
                canvas.restore();
            } else {
                f3 = f17;
                j2 = j3;
                f6 = audioHeight;
                f7 = f;
                d = 0.0d;
            }
            canvas.restore();
        } else {
            f3 = f32;
            f4 = f31;
            f5 = dp;
            f6 = audioHeight;
            f7 = f;
            j2 = j;
            d = 0.0d;
        }
        int i24 = this.h;
        int i25 = this.py;
        float f48 = f7;
        float f49 = f5 * f48;
        float lerp = AndroidUtilities.lerp((i24 - i25) - f2, (((i24 - i25) - f2) - f49) - f6, this.hasVideo ? f3 : 1.0f);
        int i26 = this.h;
        int i27 = this.py;
        float f50 = f3;
        float lerp2 = AndroidUtilities.lerp(i26 - i27, ((i26 - i27) - f2) - f49, f50);
        float lerp3 = AndroidUtilities.lerp(this.videoLeft * ((float) this.videoDuration), ((float) this.audioOffset) + (this.audioLeft * ((float) this.audioDuration)), this.hasVideo ? f50 : 1.0f);
        float lerp4 = AndroidUtilities.lerp(this.videoRight * ((float) this.videoDuration), ((float) this.audioOffset) + (this.audioRight * ((float) this.audioDuration)), this.hasVideo ? f50 : 1.0f);
        int i28 = this.px;
        int i29 = this.ph;
        long j12 = this.scroll;
        float f51 = (float) j2;
        int i30 = this.sw;
        float f52 = i28 + i29 + (((lerp3 - ((float) j12)) / f51) * i30);
        float f53 = i28 + i29 + (((lerp4 - ((float) j12)) / f51) * i30);
        float f54 = f4;
        if (f54 > d || f48 > d) {
            drawRegion(canvas, paint, lerp, lerp2, f52, f53, this.hasVideo ? 1.0f : AndroidUtilities.lerp(0.6f, 1.0f, f50) * f54);
            float f55 = this.loopProgress.set(0.0f);
            float dpf24 = (((this.h - this.py) - f2) - ((f6 + f49) * f54)) - AndroidUtilities.dpf2(4.3f);
            float dpf25 = (this.h - this.py) + AndroidUtilities.dpf2(4.3f);
            if (f55 > 0.0f) {
                if (this.hasVideo) {
                    f8 = (float) this.videoDuration;
                    f9 = this.videoRight;
                } else {
                    f8 = (float) this.audioDuration;
                    f9 = this.audioRight;
                }
                drawProgress(canvas, dpf24, dpf25, f8 * f9, f55);
            }
            drawProgress(canvas, dpf24, dpf25, this.progress, 1.0f - f55);
        }
        if (this.dragged) {
            long dp4 = (AndroidUtilities.dp(86.0f) / this.sw) * f51 * (1.0f / (1000.0f / AndroidUtilities.screenRefreshRate));
            int i31 = this.pressHandle;
            if (i31 == 4) {
                float f56 = this.videoLeft;
                long j13 = this.scroll;
                long j14 = this.videoDuration;
                if (f56 < ((float) j13) / ((float) j14)) {
                    i2 = -1;
                } else {
                    i2 = this.videoRight > ((float) (j13 + j2)) / ((float) j14) ? 1 : 0;
                }
                long j15 = i2 * dp4;
                long clamp = Utilities.clamp(j13 + j15, j14 - j2, 0L);
                this.scroll = clamp;
                this.progress += j15;
                float f57 = ((float) (clamp - j13)) / ((float) this.videoDuration);
                if (f57 > 0.0f) {
                    f10 = 1.0f;
                    max2 = Math.min(1.0f - this.videoRight, f57);
                } else {
                    f10 = 1.0f;
                    max2 = Math.max(0.0f - this.videoLeft, f57);
                }
                this.videoLeft = Utilities.clamp(this.videoLeft + max2, f10, 0.0f);
                this.videoRight = Utilities.clamp(this.videoRight + max2, f10, 0.0f);
                TimelineDelegate timelineDelegate = this.delegate;
                if (timelineDelegate != null) {
                    timelineDelegate.onVideoLeftChange(this.videoLeft);
                    this.delegate.onVideoRightChange(this.videoRight);
                }
                invalidate();
            } else if (!this.hasVideo && i31 == 8) {
                float f58 = this.audioLeft;
                long j16 = this.audioOffset;
                float f59 = (float) ((-j16) + 100);
                long j17 = this.audioDuration;
                if (f58 < f59 / ((float) j17)) {
                    i = -1;
                } else {
                    i = this.audioRight >= ((float) (((-j16) + j2) - 100)) / ((float) j17) ? 1 : 0;
                }
                long clamp2 = Utilities.clamp(j16 - (i * dp4), 0L, -(j17 - Math.min(getBaseDuration(), 120000L)));
                this.audioOffset = clamp2;
                float f60 = ((float) (-(clamp2 - j16))) / ((float) this.audioDuration);
                if (f60 > 0.0f) {
                    max = Math.min(1.0f - this.audioRight, f60);
                } else {
                    max = Math.max(0.0f - this.audioLeft, f60);
                }
                long j18 = this.audioDuration;
                this.progress = Utilities.clamp(((float) this.progress) + (((float) j18) * max), (float) j18, 0.0f);
                this.audioLeft = Utilities.clamp(this.audioLeft + max, 1.0f, 0.0f);
                this.audioRight = Utilities.clamp(this.audioRight + max, 1.0f, 0.0f);
                TimelineDelegate timelineDelegate2 = this.delegate;
                if (timelineDelegate2 != null) {
                    timelineDelegate2.onAudioLeftChange(this.audioLeft);
                    this.delegate.onAudioRightChange(this.audioRight);
                    this.delegate.onProgressChange(this.progress, false);
                }
                invalidate();
            }
        }
    }

    private void drawRegion(Canvas canvas, Paint paint, float f, float f2, float f3, float f4, float f5) {
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(f3 - AndroidUtilities.dp(10.0f), f, f4 + AndroidUtilities.dp(10.0f), f2);
        canvas.saveLayerAlpha(0.0f, 0.0f, this.w, this.h, 255, 31);
        int i = (int) (255.0f * f5);
        this.regionPaint.setAlpha(i);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.regionPaint);
        rectF.inset(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.0f));
        canvas.drawRect(rectF, this.regionCutPaint);
        float dp = AndroidUtilities.dp(2.0f);
        float dp2 = AndroidUtilities.dp(10.0f);
        Paint paint2 = paint != null ? paint : this.regionHandlePaint;
        paint2.setAlpha(i);
        float f6 = f + f2;
        float f7 = (f6 - dp2) / 2.0f;
        float f8 = (f6 + dp2) / 2.0f;
        rectF.set(f3 - ((AndroidUtilities.dp(10.0f) - dp) / 2.0f), f7, f3 - ((AndroidUtilities.dp(10.0f) + dp) / 2.0f), f8);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), paint2);
        rectF.set(f4 + ((AndroidUtilities.dp(10.0f) - dp) / 2.0f), f7, f4 + ((AndroidUtilities.dp(10.0f) + dp) / 2.0f), f8);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), paint2);
        canvas.restore();
    }

    private void drawProgress(Canvas canvas, float f, float f2, long j, float f3) {
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
        int dp4 = AndroidUtilities.dp(80.0f);
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
        private final int count;
        private boolean destroyed;
        private long duration;
        private final int frameHeight;
        private final long frameIterator;
        private final int frameWidth;
        private MediaMetadataRetriever metadataRetriever;
        private long nextFrame;
        private final ArrayList<BitmapFrame> frames = new ArrayList<>();
        private boolean loading = false;
        private final Paint bitmapPaint = new Paint(3);

        /* JADX WARN: Code restructure failed: missing block: B:71:0x0068, code lost:
            if (r0 != 270) goto L27;
         */
        /* JADX WARN: Removed duplicated region for block: B:86:0x0087 A[ADDED_TO_REGION] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public VideoThumbsLoader(String str, int i, int i2) {
            Exception e;
            long j;
            int i3;
            float f;
            int max;
            String extractMetadata;
            TimelineView.this = r9;
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.metadataRetriever = mediaMetadataRetriever;
            try {
                mediaMetadataRetriever.setDataSource(str);
                String extractMetadata2 = this.metadataRetriever.extractMetadata(9);
                if (extractMetadata2 != null) {
                    j = Long.parseLong(extractMetadata2);
                    try {
                        this.duration = j;
                    } catch (Exception e2) {
                        e = e2;
                        i3 = 0;
                        this.metadataRetriever = null;
                        FileLog.e(e);
                        int i4 = i3;
                        i3 = r9;
                        r9 = i4;
                        f = 1.0f;
                        if (i3 != 0) {
                        }
                        float clamp = Utilities.clamp(f, 1.3333334f, 0.5625f);
                        this.frameHeight = Math.max(1, i2);
                        this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp));
                        int ceil = (int) Math.ceil(((((float) Math.max(j, 120000L)) / 120000.0f) * i) / max);
                        this.count = ceil;
                        long j2 = ((float) j) / ceil;
                        this.frameIterator = j2;
                        this.nextFrame = -j2;
                        load();
                    }
                } else {
                    j = 120000;
                }
                String extractMetadata3 = this.metadataRetriever.extractMetadata(18);
                i3 = extractMetadata3 != null ? Integer.parseInt(extractMetadata3) : 0;
                try {
                    String extractMetadata4 = this.metadataRetriever.extractMetadata(19);
                    r9 = extractMetadata4 != null ? Integer.parseInt(extractMetadata4) : 0;
                } catch (Exception e3) {
                    e = e3;
                    r9 = i3;
                    i3 = 0;
                    this.metadataRetriever = null;
                    FileLog.e(e);
                    int i42 = i3;
                    i3 = r9;
                    r9 = i42;
                    f = 1.0f;
                    if (i3 != 0) {
                        f = i3 / r9;
                    }
                    float clamp2 = Utilities.clamp(f, 1.3333334f, 0.5625f);
                    this.frameHeight = Math.max(1, i2);
                    this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp2));
                    int ceil2 = (int) Math.ceil(((((float) Math.max(j, 120000L)) / 120000.0f) * i) / max);
                    this.count = ceil2;
                    long j22 = ((float) j) / ceil2;
                    this.frameIterator = j22;
                    this.nextFrame = -j22;
                    load();
                }
                try {
                    extractMetadata = this.metadataRetriever.extractMetadata(24);
                } catch (Exception e4) {
                    e = e4;
                    int i5 = i3;
                    i3 = r9;
                    r9 = i5;
                    this.metadataRetriever = null;
                    FileLog.e(e);
                    int i422 = i3;
                    i3 = r9;
                    r9 = i422;
                    f = 1.0f;
                    if (i3 != 0) {
                    }
                    float clamp22 = Utilities.clamp(f, 1.3333334f, 0.5625f);
                    this.frameHeight = Math.max(1, i2);
                    this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp22));
                    int ceil22 = (int) Math.ceil(((((float) Math.max(j, 120000L)) / 120000.0f) * i) / max);
                    this.count = ceil22;
                    long j222 = ((float) j) / ceil22;
                    this.frameIterator = j222;
                    this.nextFrame = -j222;
                    load();
                }
            } catch (Exception e5) {
                e = e5;
                j = 120000;
            }
            if (extractMetadata != null) {
                int parseInt = Integer.parseInt(extractMetadata);
                if (parseInt != 90) {
                }
                int i4222 = i3;
                i3 = r9;
                r9 = i4222;
            }
            f = 1.0f;
            if (i3 != 0 && r9 != 0) {
                f = i3 / r9;
            }
            float clamp222 = Utilities.clamp(f, 1.3333334f, 0.5625f);
            this.frameHeight = Math.max(1, i2);
            this.frameWidth = Math.max(1, (int) Math.ceil(i2 * clamp222));
            int ceil222 = (int) Math.ceil(((((float) Math.max(j, 120000L)) / 120000.0f) * i) / max);
            this.count = ceil222;
            long j2222 = ((float) j) / ceil222;
            this.frameIterator = j2222;
            this.nextFrame = -j2222;
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
                    canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect((int) ((createBitmap.getWidth() - (bitmap.getWidth() * max)) / 2.0f), (int) ((createBitmap.getHeight() - (bitmap.getHeight() * max)) / 2.0f), (int) ((createBitmap.getWidth() + (bitmap.getWidth() * max)) / 2.0f), (int) ((createBitmap.getHeight() + (bitmap.getHeight() * max)) / 2.0f)), this.bitmapPaint);
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
            int round = Math.round(((((float) (this.duration * 1000)) / ((float) Math.min(r8.hasVideo ? r8.videoDuration : this.duration * 1000, 120000L))) * i) / Math.round(AndroidUtilities.dpf2(3.3333f)));
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
}
